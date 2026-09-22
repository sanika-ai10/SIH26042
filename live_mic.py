import queue
import sounddevice as sd
import json
from vosk import Model, KaldiRecognizer

q = queue.Queue()

def callback(indata, frames, time, status):
    if status:
        print(status, flush=True)
    q.put(bytes(indata))

print("Loading Hindi Model...")
model = Model("model-hi")
recognizer = KaldiRecognizer(model, 16000)

print("\n--- Model Ready ---")
print("Speak Hindi into your microphone now (Press Ctrl+C to stop)...")

with sd.RawInputStream(samplerate=16000, blocksize=8000, dtype='int16',
                       channels=1, callback=callback):
    while True:
        data = q.get()
        if recognizer.AcceptWaveform(data):
            res = json.loads(recognizer.Result())
            text = res.get("text", "").strip()
            if text:
                print("\n[Teacher Hindi Recognized]:", text)
        else:
            partial = json.loads(recognizer.PartialResult())
            p_text = partial.get("partial", "").strip()
            if p_text:
                print(f"\rListening: {p_text}", end="", flush=True)