from pathlib import Path

from translate import IndicTransONNX


# ==========================================
# PROJECT PATHS
# ==========================================

PROJECT_ROOT = Path(__file__).resolve().parents[1]

MODEL_PATH = PROJECT_ROOT / "models" / "indictrans2-int8"

PHRASES_PATH = PROJECT_ROOT / "data" / "classroom_phrases.txt"


# ==========================================
# LOAD TRANSLATION MODEL
# ==========================================

print("Loading Hindi → Santhali translation model...")

translator = IndicTransONNX(str(MODEL_PATH))

print("✅ Translation model loaded!")


# ==========================================
# READ CLASSROOM PHRASES
# ==========================================

with open(PHRASES_PATH, "r", encoding="utf-8") as file:
    phrases = [line.strip() for line in file if line.strip()]


# ==========================================
# TRANSLATE
# ==========================================

print("\n======================================")
print("   Classroom Phrase Translation Test")
print("======================================")

for number, hindi_text in enumerate(phrases, start=1):

    santhali_text = translator.translate(
        hindi_text,
        src_lang="hin_Deva",
        tgt_lang="sat_Olck"
    )

    print(f"\n{number}. Hindi:")
    print(hindi_text)

    print("   Santhali:")
    print(santhali_text)


print("\n======================================")
print("✅ All classroom phrases translated!")
print("======================================")