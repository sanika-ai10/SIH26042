import queue
import sounddevice as sd
import json
from vosk import Model, KaldiRecognizer

# Initialize audio queue
audio_queue = queue.Queue()

def audio_callback(indata, frames, time, status):
    if status:
        print(status, flush=True)
    audio_queue.put(bytes(indata))

# Load the local offline Hindi model
print("Loading offline Hindi ASR model...")
model = Model("model-hi")
recognizer = KaldiRecognizer(model, 16000)

print("\n==========================================")
print("PALASH-AI: Hindi Speech-to-Text Module")
print("Status: Listening via microphone (Offline)")
print("Press Ctrl + C to stop")
print("==========================================\n")

try:
    with sd.RawInputStream(samplerate=16000, blocksize=8000, dtype='int16',
                           channels=1, callback=audio_callback):
        while True:
            data = audio_queue.get()
            if recognizer.AcceptWaveform(data):
                result = json.loads(recognizer.Result())
                recognized_text = result.get("text", "").strip()
                
                # Final Hindi text ready to hand off to your teammate's translation module
                if recognized_text:
                    print(f"\n[Final Hindi Transcript]: {recognized_text}\n")
            else:
                partial = json.loads(recognizer.PartialResult())
                partial_text = partial.get("partial", "").strip()
                if partial_text:
                    print(f"\rListening: {partial_text}", end="", flush=True)

except KeyboardInterrupt:
    print("\nHindi ASR module stopped.")
