from pathlib import Path
import onnx_asr

# Find the main project folder
PROJECT_ROOT = Path(__file__).resolve().parents[1]

# Santhali ASR model
MODEL_PATH = PROJECT_ROOT / "models" / "indicconformer-sat"

# Audio file we will use
AUDIO_PATH = PROJECT_ROOT / "santhali_input.wav"

print("Loading Santhali ASR model...")

model = onnx_asr.load_model(
    "nemo-conformer-ctc",
    path=str(MODEL_PATH),
    providers=["CPUExecutionProvider"]
)

print("✅ Santhali ASR model loaded!")

print("\n🎤 Transcribing Santhali audio...")

result = model.recognize(str(AUDIO_PATH))

print("\n==============================")
print("Santhali transcription:")
print(result)
print("==============================")