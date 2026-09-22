import sqlite3
import subprocess
from pathlib import Path

from translate import IndicTransONNX
from piper import PiperVoice
import wave


# ==========================================
# PROJECT PATHS
# ==========================================

PROJECT_ROOT = Path(__file__).resolve().parents[1]

DATABASE_PATH = PROJECT_ROOT / "data" / "classroom_phrases.db"

TRANSLATION_MODEL_PATH = PROJECT_ROOT / "models" / "indictrans2-int8"

TTS_MODEL_PATH = (
    PROJECT_ROOT
    / "models"
    / "santhali-tts"
    / "sat_piper_model.onnx"
)

AUDIO_FOLDER = PROJECT_ROOT / "classroom_audio"

FALLBACK_AUDIO = PROJECT_ROOT / "fallback_santhali.wav"


# ==========================================
# GET USER INPUT
# ==========================================

print("======================================")
print("   Offline Teaching Assistant")
print("======================================")

hindi_text = input("\nEnter a Hindi classroom phrase: ")


# ==========================================
# SEARCH OFFLINE DATABASE
# ==========================================

connection = sqlite3.connect(DATABASE_PATH)

cursor = connection.cursor()

cursor.execute("""
SELECT santhali, audio_file
FROM classroom_phrases
WHERE hindi = ?
""", (hindi_text,))

result = cursor.fetchone()

connection.close()


# ==========================================
# PHRASE FOUND
# ==========================================

if result:

    santhali_text, audio_file = result

    print("\n✅ Phrase found in offline database!")

    print("\nHindi:")
    print(hindi_text)

    print("\nSanthali:")
    print(santhali_text)

    audio_path = AUDIO_FOLDER / audio_file

    print("\n🔊 Playing stored Santhali audio...")

    subprocess.run(["afplay", str(audio_path)])

    print("✅ Audio playback finished!")


# ==========================================
# PHRASE NOT FOUND
# ==========================================

else:

    print("\n⚠️ Phrase not found in offline database.")

    print("Using AI translation model...")

    translator = IndicTransONNX(
        str(TRANSLATION_MODEL_PATH)
    )

    santhali_text = translator.translate(
        hindi_text,
        src_lang="hin_Deva",
        tgt_lang="sat_Olck"
    )

    print("\nHindi:")
    print(hindi_text)

    print("\nSanthali:")
    print(santhali_text)


    # ======================================
    # GENERATE SANTHALI SPEECH
    # ======================================

    print("\n🔊 Generating Santhali speech...")

    voice = PiperVoice.load(
        str(TTS_MODEL_PATH)
    )

    with wave.open(
        str(FALLBACK_AUDIO),
        "wb"
    ) as wav_file:

        voice.synthesize_wav(
            santhali_text,
            wav_file
        )

    print("✅ Speech generated!")

    print("\n🔊 Playing Santhali translation...")

    subprocess.run(
        ["afplay", str(FALLBACK_AUDIO)]
    )

    print("✅ Audio playback finished!")


print("\n======================================")
print("✅ Translation complete!")
print("======================================")