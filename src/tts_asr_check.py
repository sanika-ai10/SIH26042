from pathlib import Path
import wave
import onnx_asr

from piper import PiperVoice


# Project folder
PROJECT_ROOT = Path(__file__).resolve().parents[1]

# Models
TTS_MODEL_PATH = PROJECT_ROOT / "models" / "santhali-tts" / "sat_piper_model.onnx"
ASR_MODEL_PATH = PROJECT_ROOT / "models" / "indicconformer-sat"

# Audio file
AUDIO_PATH = PROJECT_ROOT / "santhali_control_test.wav"


# ==========================================
# 1. Known Santhali sentence
# ==========================================

santhali_text = "ᱜᱤᱫᱽᱨᱟᱹ ᱠᱚ ᱯᱚᱛᱚᱵ ᱠᱚ ᱠᱷᱚᱞᱟᱣ ᱢᱮ ᱾"

print("Known Santhali text:")
print(santhali_text)


# ==========================================
# 2. Santhali Text → Speech
# ==========================================

print("\nLoading Santhali TTS model...")

tts = PiperVoice.load(str(TTS_MODEL_PATH))

print("✅ TTS model loaded!")

print("\n🔊 Generating Santhali audio...")

with wave.open(str(AUDIO_PATH), "wb") as wav_file:
    tts.synthesize_wav(santhali_text, wav_file)

print("✅ Audio generated!")


# ==========================================
# 3. Santhali Speech → Text
# ==========================================

print("\nLoading Santhali ASR model...")

asr = onnx_asr.load_model(
    "nemo-conformer-ctc",
    path=str(ASR_MODEL_PATH),
    providers=["CPUExecutionProvider"]
)

print("✅ ASR model loaded!")

print("\n🎤 Recognizing the generated audio...")

recognized_text = asr.recognize(str(AUDIO_PATH))


# ==========================================
# 4. Compare
# ==========================================

print("\n======================================")
print("Original Santhali:")
print(santhali_text)

print("\nASR recognized:")
print(recognized_text)

print("======================================")