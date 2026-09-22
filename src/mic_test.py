import sounddevice as sd
import wave
import numpy as np

print("🎤 Recording for 5 seconds...")
print("Speak something in Hindi!")

# Record audio
recording = sd.rec(
    int(5 * 16000),
    samplerate=16000,
    channels=1,
    dtype="float32"
)

sd.wait()

print("✅ Recording finished!")

# Convert audio to 16-bit PCM
audio_int16 = np.int16(recording * 32767)

# Save as WAV
with wave.open("test.wav", "wb") as wav_file:
    wav_file.setnchannels(1)
    wav_file.setsampwidth(2)
    wav_file.setframerate(16000)
    wav_file.writeframes(audio_int16.tobytes())

print("💾 Audio saved as test.wav")