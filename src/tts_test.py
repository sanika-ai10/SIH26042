from pathlib import Path
import wave

from piper import PiperVoice


# Find the main project folder
PROJECT_ROOT = Path(__file__).resolve().parents[1]

# Location of our Santhali TTS model
MODEL_PATH = PROJECT_ROOT / "models" / "santhali-tts" / "sat_piper_model.onnx"

# Output audio file
OUTPUT_PATH = PROJECT_ROOT / "santhali_test.wav"


print("Loading Santhali TTS model...")

voice = PiperVoice.load(str(MODEL_PATH))

print("✅ Santhali TTS model loaded!")


# Santhali text
santhali_text = "ᱜᱤᱫᱽᱨᱟᱹ ᱠᱚ ᱵᱤᱨᱫᱟᱹᱜᱟᱲ ᱨᱮ ᱪᱟᱞᱟᱣᱚᱜ ᱠᱟᱱᱟ ᱾"


print("🔊 Generating Santhali speech...")


# Create a proper WAV file
with wave.open(str(OUTPUT_PATH), "wb") as wav_file:
    voice.synthesize_wav(santhali_text, wav_file)


print("✅ Speech generated!")
print(f"💾 Saved to: {OUTPUT_PATH}")