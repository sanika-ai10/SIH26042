import queue
import sounddevice as sd
import json
from vosk import Model, KaldiRecognizer

class HindiASR:
    def __init__(self, model_path="model-hi"):
        print("Loading offline Hindi ASR...")
        self.model = Model(model_path)
        self.recognizer = KaldiRecognizer(self.model, 16000)
        self.audio_queue = queue.Queue()

    def _callback(self, indata, frames, time, status):
        self.audio_queue.put(bytes(indata))

    def listen_and_transcribe(self):
        """
        Listens to microphone input and returns the final transcribed Hindi string
        as soon as a complete sentence/phrase is detected.
        """
        with sd.RawInputStream(samplerate=16000, blocksize=8000, dtype='int16',
                               channels=1, callback=self._callback):
            print("\n[ASR] Listening... speak a classroom command in Hindi:")
            while True:
                data = self.audio_queue.get()
                if self.recognizer.AcceptWaveform(data):
                    result = json.loads(self.recognizer.Result())
                    text = result.get("text", "").strip()
                    if text:
                        return text