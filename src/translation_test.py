import os
os.environ["TRANSFORMERS_NO_ADVISORY_WARNINGS"] = "1"

from pathlib import Path
from translate import IndicTransONNX

# Find our project folder
PROJECT_ROOT = Path(__file__).resolve().parents[1]

# Location of our downloaded model
MODEL_PATH = PROJECT_ROOT / "models" / "indictrans2-int8"

# Load the translation model
translator = IndicTransONNX(str(MODEL_PATH))

print("======================================")
print("   SIH26042 Hindi → Santhali Translator")
print("======================================")

# Ask the user for a Hindi sentence
hindi_text = input("Enter a Hindi sentence: ")

# Translate Hindi → Santhali
result = translator.translate(
    hindi_text,
    src_lang="hin_Deva",
    tgt_lang="sat_Olck"
)

print("\nHindi:", hindi_text)
print("Santhali:", result)
