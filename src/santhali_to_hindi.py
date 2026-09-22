from pathlib import Path
import wave
import subprocess

import sounddevice as sd
import numpy as np
import onnx_asr
from piper import PiperVoice

from translate import IndicTransONNX


# ==========================================
# PROJECT PATHS
# ==========================================

PROJECT_ROOT = Path(__file__).resolve().parents[1]

ASR_MODEL_PATH = PROJECT_ROOT / "models" / "indicconformer-sat"

TRANSLATION_MODEL_PATH = PROJECT_ROOT / "models" / "indictrans2-int8"

TTS_MODEL_PATH = (
    PROJECT_ROOT
    / "models"
    / "hindi-tts"
    / "hi"
    / "hi_IN"
    / "pratham"
    / "medium"
    / "hi_IN-pratham-medium.onnx"
)

AUDIO_INPUT = PROJECT_ROOT / "santhali_input.wav"
AUDIO_OUTPUT = PROJECT_ROOT / "hindi_output.wav"


# ==========================================
# RECORD SANTHALI
# ==========================================

print("======================================")
print("   SIH26042 Santhali → Hindi")
print("======================================")

print("\n🎤 Speak in Santhali for 5 seconds...")

recording = sd.rec(
    int(5 * 16000),
    samplerate=16000,
    channels=1,
    dtype="float32"
)

sd.wait()

print("✅ Recording finished!")

audio_int16 = np.int16(recording * 32767)

with wave.open(str(AUDIO_INPUT), "wb") as wav_file:
    wav_file.setnchannels(1)
    wav_file.setsampwidth(2)
    wav_file.setframerate(16000)
    wav_file.writeframes(audio_int16.tobytes())


# ==========================================
# SANTHALI ASR
# ==========================================

print("\nLoading Santhali ASR model...")

asr_model = onnx_asr.load_model(
    "nemo-conformer-ctc",
    path=str(ASR_MODEL_PATH),
    providers=["CPUExecutionProvider"]
)

print("✅ Santhali ASR model loaded!")

print("\n📝 Converting Santhali speech to text...")

santhali_text = asr_model.recognize(str(AUDIO_INPUT))

print("Santhali:", santhali_text)


# ==========================================
# SANTHALI → HINDI TRANSLATION
# ==========================================

print("\n🌐 Translating Santhali → Hindi...")

translator = IndicTransONNX(str(TRANSLATION_MODEL_PATH))

hindi_text = translator.translate(
    santhali_text,
    src_lang="sat_Olck",
    tgt_lang="hin_Deva"
)

print("Hindi:", hindi_text)


# ==========================================
# HINDI TEXT → SPEECH
# ==========================================

print("\n🔊 Generating Hindi speech...")

voice = PiperVoice.load(str(TTS_MODEL_PATH))

with wave.open(str(AUDIO_OUTPUT), "wb") as wav_file:
    voice.synthesize_wav(hindi_text, wav_file)

print("✅ Hindi speech generated!")


# ==========================================
# PLAY HINDI AUDIO
# ==========================================

print("\n🔊 Playing Hindi translation...")

subprocess.run(["afplay", str(AUDIO_OUTPUT)])

print("✅ Audio playback finished!")

print("\n======================================")
print("🎉 Santhali → Hindi voice translation complete!")
print("======================================")