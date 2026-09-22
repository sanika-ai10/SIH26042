import json
import numpy as np
from vosk import Model, KaldiRecognizer

print("Loading offline Hindi ASR model (Vosk)...")
model = Model("model-hi")
recognizer = KaldiRecognizer(model, 16000)
print("Hindi model loaded successfully!")

# Generate 1 second of sample 16kHz audio buffer
sample_rate = 16000
duration = 1.0
t = np.linspace(0, duration, int(sample_rate * duration), endpoint=False)
audio_signal = (0.2 * np.sin(2 * np.pi * 440 * t) * 32767).astype(np.int16)
byte_data = audio_signal.tobytes()

# Process audio
recognizer.AcceptWaveform(byte_data)
result = json.loads(recognizer.FinalResult())

print("\n--- Pipeline Verification ---")
print("ASR Decoded Result:", result.get("text", ""))
print("SUCCESS: Offline Hindi ASR engine initialized and executed cleanly!")