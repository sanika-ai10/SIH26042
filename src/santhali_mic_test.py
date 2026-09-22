import sounddevice as sd
import wave
import numpy as np

print("🎤 Recording Santhali speech for 5 seconds...")
print("Speak a simple Santhali sentence!")

recording = sd.rec(
    int(5 * 16000),
    samplerate=16000,
    channels=1,
    dtype="float32"
)

sd.wait()

print("✅ Recording finished!")

audio_int16 = np.int16(recording * 32767)

with wave.open("santhali_input.wav", "wb") as wav_file:
    wav_file.setnchannels(1)
    wav_file.setsampwidth(2)
    wav_file.setframerate(16000)
    wav_file.writeframes(audio_int16.tobytes())

print("💾 Saved as santhali_input.wav")
