from pathlib import Path
import onnx_asr

# Find the main project folder
PROJECT_ROOT = Path(__file__).resolve().parents[1]

# Location of our downloaded Hindi ASR model
MODEL_PATH = PROJECT_ROOT / "models" / "indicconformer-hi"

# Location of our recorded audio
AUDIO_PATH = PROJECT_ROOT / "test.wav"

print("Loading Hindi ASR model...")

model = onnx_asr.load_model(
    "nemo-conformer-ctc",
    path=str(MODEL_PATH),
    providers=["CPUExecutionProvider"]
)
print("✅ Model loaded!")

print("🎤 Transcribing your recording...")

result = model.recognize(str(AUDIO_PATH))

print("\n==============================")
print("Hindi transcription:")
print(result)
print("==============================")