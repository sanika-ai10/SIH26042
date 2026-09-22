from pathlib import Path
import wave

from piper import PiperVoice
from translate import IndicTransONNX


# ==========================================
# PROJECT PATHS
# ==========================================

PROJECT_ROOT = Path(__file__).resolve().parents[1]

TRANSLATION_MODEL_PATH = PROJECT_ROOT / "models" / "indictrans2-int8"

TTS_MODEL_PATH = (
    PROJECT_ROOT
    / "models"
    / "santhali-tts"
    / "sat_piper_model.onnx"
)

PHRASES_PATH = PROJECT_ROOT / "data" / "classroom_phrases.txt"

OUTPUT_DIR = PROJECT_ROOT / "classroom_audio"

OUTPUT_DIR.mkdir(exist_ok=True)


# ==========================================
# LOAD MODELS
# ==========================================

print("Loading translation model...")
translator = IndicTransONNX(str(TRANSLATION_MODEL_PATH))
print("✅ Translation model loaded!")

print("\nLoading Santhali TTS model...")
voice = PiperVoice.load(str(TTS_MODEL_PATH))
print("✅ Santhali TTS model loaded!")


# ==========================================
# READ CLASSROOM PHRASES
# ==========================================

with open(PHRASES_PATH, "r", encoding="utf-8") as file:
    phrases = [line.strip() for line in file if line.strip()]


# ==========================================
# TRANSLATE + GENERATE AUDIO
# ==========================================

print("\n======================================")
print("   Classroom Audio Generator")
print("======================================")

for number, hindi_text in enumerate(phrases, start=1):

    print(f"\n{number}. Hindi:")
    print(hindi_text)

    santhali_text = translator.translate(
        hindi_text,
        src_lang="hin_Deva",
        tgt_lang="sat_Olck"
    )

    print("Santhali:")
    print(santhali_text)

    output_path = OUTPUT_DIR / f"phrase_{number}.wav"

    with wave.open(str(output_path), "wb") as wav_file:
        voice.synthesize_wav(santhali_text, wav_file)

    print("🔊 Audio saved:", output_path.name)


print("\n======================================")
print("✅ All classroom audio generated!")
print("======================================")