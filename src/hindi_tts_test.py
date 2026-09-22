from pathlib import Path
import wave

from piper import PiperVoice


PROJECT_ROOT = Path(__file__).resolve().parents[1]

MODEL_PATH = (
    PROJECT_ROOT
    / "models"
    / "hindi-tts"
    / "hi"
    / "hi_IN"
    / "pratham"
    / "medium"
    / "hi_IN-pratham-medium.onnx"
)

OUTPUT_PATH = PROJECT_ROOT / "hindi_test.wav"


print("Loading Hindi TTS model...")

voice = PiperVoice.load(str(MODEL_PATH))

print("✅ Hindi TTS model loaded!")

hindi_text = "धन्यवाद। आपका स्वागत है।"

print("\n🔊 Generating Hindi speech...")

with wave.open(str(OUTPUT_PATH), "wb") as wav_file:
    voice.synthesize_wav(hindi_text, wav_file)

print("✅ Speech generated!")
print("💾 Saved to:", OUTPUT_PATH)