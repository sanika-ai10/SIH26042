from pathlib import Path
import wave
import sounddevice as sd
import numpy as np
import onnx_asr
import subprocess

from piper import PiperVoice
from translate import IndicTransONNX


# ==========================================
# PROJECT PATHS
# ==========================================

PROJECT_ROOT = Path(__file__).resolve().parents[1]

ASR_MODEL_PATH = PROJECT_ROOT / "models" / "indicconformer-hi"
TRANSLATION_MODEL_PATH = PROJECT_ROOT / "models" / "indictrans2-int8"
TTS_MODEL_PATH = PROJECT_ROOT / "models" / "santhali-tts" / "sat_piper_model.onnx"

AUDIO_INPUT = PROJECT_ROOT / "voice_input.wav"
AUDIO_OUTPUT = PROJECT_ROOT / "santhali_output.wav"


# ==========================================
# RECORD HINDI SPEECH
# ==========================================

print("======================================")
print("   SIH26042 Voice-to-Voice Translator")
print("======================================")

print("\n🎤 Speak in Hindi for 5 seconds...")

recording = sd.rec(
    int(5 * 16000),
    samplerate=16000,
    channels=1,
    dtype="float32"
)

sd.wait()

print("✅ Recording finished!")


# Save microphone recording
audio_int16 = np.int16(recording * 32767)

with wave.open(str(AUDIO_INPUT), "wb") as wav_file:
    wav_file.setnchannels(1)
    wav_file.setsampwidth(2)
    wav_file.setframerate(16000)
    wav_file.writeframes(audio_int16.tobytes())


# ==========================================
# LOAD HINDI ASR
# ==========================================

print("\nLoading Hindi speech recognition model...")

asr_model = onnx_asr.load_model(
    "nemo-conformer-ctc",
    path=str(ASR_MODEL_PATH),
    providers=["CPUExecutionProvider"]
)

print("✅ ASR model loaded!")


# ==========================================
# HINDI SPEECH → HINDI TEXT
# ==========================================

print("\n📝 Converting Hindi speech to text...")

hindi_text = asr_model.recognize(str(AUDIO_INPUT))

print("Hindi:", hindi_text)


# ==========================================
# LOAD TRANSLATION MODEL
# ==========================================

print("\nLoading Hindi → Santhali translation model...")

translator = IndicTransONNX(str(TRANSLATION_MODEL_PATH))

print("✅ Translation model loaded!")


# ==========================================
# HINDI TEXT → SANTHALI TEXT
# ==========================================

print("\n🌐 Translating Hindi → Santhali...")

santhali_text = translator.translate(
    hindi_text,
    src_lang="hin_Deva",
    tgt_lang="sat_Olck"
)

print("Santhali:", santhali_text)


# ==========================================
# LOAD SANTHALI TTS
# ==========================================

print("\nLoading Santhali TTS model...")

voice = PiperVoice.load(str(TTS_MODEL_PATH))

print("✅ TTS model loaded!")


# ==========================================
# SANTHALI TEXT → SPEECH
# ==========================================

print("\n🔊 Generating Santhali speech...")

with wave.open(str(AUDIO_OUTPUT), "wb") as wav_file:
    voice.synthesize_wav(santhali_text, wav_file)

print("✅ Santhali speech generated!")

print("\n======================================")
print("🎉 VOICE-TO-VOICE TRANSLATION COMPLETE!")
print("======================================")

print(f"\n🔊 Audio saved to:")
print(AUDIO_OUTPUT)
# Play the generated Santhali audio
print("\n🔊 Playing Santhali translation...")

subprocess.run(["afplay", str(AUDIO_OUTPUT)])

print("✅ Audio playback finished!")
