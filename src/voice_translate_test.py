from pathlib import Path
import onnx_asr

from translate import IndicTransONNX


# Find project folder
PROJECT_ROOT = Path(__file__).resolve().parents[1]

# Model and audio locations
ASR_MODEL_PATH = PROJECT_ROOT / "models" / "indicconformer-hi"
TRANSLATION_MODEL_PATH = PROJECT_ROOT / "models" / "indictrans2-int8"
AUDIO_PATH = PROJECT_ROOT / "test.wav"


print("======================================")
print("   SIH26042 Voice Translation Test")
print("======================================")

# Load Hindi speech recognition model
print("\nLoading Hindi ASR model...")

asr_model = onnx_asr.load_model(
    "nemo-conformer-ctc",
    path=str(ASR_MODEL_PATH),
    providers=["CPUExecutionProvider"]
)

print("✅ Hindi ASR model loaded!")


# Load Hindi → Santhali translation model
print("\nLoading Hindi → Santhali translation model...")

translator = IndicTransONNX(str(TRANSLATION_MODEL_PATH))

print("✅ Translation model loaded!")


# Convert speech → Hindi text
print("\n🎤 Converting Hindi speech to text...")

hindi_text = asr_model.recognize(str(AUDIO_PATH))

print("Hindi:", hindi_text)


# Translate Hindi → Santhali
print("\n🌐 Translating Hindi → Santhali...")

santhali_text = translator.translate(
    hindi_text,
    src_lang="hin_Deva",
    tgt_lang="sat_Olck"
)

print("Santhali:", santhali_text)


print("\n======================================")
print("✅ Voice → Hindi → Santhali complete!")
print("======================================")