from pathlib import Path
import wave
from piper import PiperVoice
from translate import IndicTransONNX


PROJECT_ROOT = Path(__file__).resolve().parents[1]

TRANSLATION_MODEL_PATH = PROJECT_ROOT / "models" / "indictrans2-int8"

TTS_MODEL_PATH = (
    PROJECT_ROOT
    / "models"
    / "santhali-tts"
    / "sat_piper_model.onnx"
)

OUTPUT_PATH = PROJECT_ROOT / "classroom_santhali.wav"


# ==========================================
# Hindi → Santhali
# ==========================================

translator = IndicTransONNX(str(TRANSLATION_MODEL_PATH))

hindi_text = "बच्चों, किताब खोलो।"

santhali_text = translator.translate(
    hindi_text,
    src_lang="hin_Deva",
    tgt_lang="sat_Olck"
)

print("Hindi:")
print(hindi_text)

print("\nSanthali:")
print(santhali_text)


# ==========================================
# Santhali → Speech
# ==========================================

print("\n🔊 Generating Santhali speech...")

voice = PiperVoice.load(str(TTS_MODEL_PATH))

with wave.open(str(OUTPUT_PATH), "wb") as wav_file:
    voice.synthesize_wav(santhali_text, wav_file)

print("✅ Santhali speech generated!")
print("💾 Saved to:", OUTPUT_PATH)