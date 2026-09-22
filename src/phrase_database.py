import sqlite3
from pathlib import Path


# ==========================================
# PROJECT PATH
# ==========================================

PROJECT_ROOT = Path(__file__).resolve().parents[1]

DATABASE_PATH = PROJECT_ROOT / "data" / "classroom_phrases.db"


# ==========================================
# CONNECT TO DATABASE
# ==========================================

connection = sqlite3.connect(DATABASE_PATH)

cursor = connection.cursor()


# ==========================================
# CREATE TABLE
# ==========================================

cursor.execute("""
CREATE TABLE IF NOT EXISTS classroom_phrases (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    hindi TEXT NOT NULL,
    santhali TEXT NOT NULL,
    audio_file TEXT
)
""")


# ==========================================
# CLASSROOM PHRASES
# ==========================================

phrases = [
    (
        "बच्चों, किताब खोलो।",
        "ᱜᱤᱫᱽᱨᱟᱹᱠᱚ, ᱯᱚᱛᱚᱵ ᱫᱚ ᱠᱷᱚᱞᱟᱣ ᱢᱮ ᱾",
        "phrase_1.wav"
    ),
    (
        "ध्यान से सुनो।",
        "ᱢᱚᱱᱮ ᱠᱟᱛᱮ ᱵᱟᱰᱟᱭ ᱢᱮ ᱾",
        "phrase_2.wav"
    ),
    (
        "मेरे पीछे दोहराओ।",
        "ᱤᱧ ᱛᱟᱭᱚᱢ ᱫᱟᱨᱟᱢ ᱢᱮ ᱾",
        "phrase_3.wav"
    ),
    (
        "यह क्या है?",
        "ᱱᱚᱶᱟ ᱫᱚ ᱪᱮᱫ?",
        "phrase_4.wav"
    ),
    (
        "बैठ जाओ।",
        "ᱥᱮᱱ ᱢᱮ ᱾",
        "phrase_5.wav"
    ),
    (
        "खड़े हो जाओ।",
        "ᱛᱷᱟᱯᱚᱱ ᱢᱮ ᱾",
        "phrase_6.wav"
    ),
    (
        "कौन जवाब देगा?",
        "ᱡᱟᱦᱟᱸᱭ ᱫᱚ ᱩᱛᱛᱚᱨ ᱮ ᱮᱢᱼᱟ?",
        "phrase_7.wav"
    ),
    (
        "एक से दस तक गिनो।",
        "ᱢᱤᱫ ᱠᱷᱚᱱ ᱜᱮᱞ ᱫᱷᱟᱹᱵᱤᱡ ᱜᱤᱭᱩᱱ ᱢᱮ ᱾",
        "phrase_8.wav"
    ),
    (
        "बच्चों, बोर्ड की तरफ देखो।",
        "ᱜᱤᱫᱽᱨᱟᱹᱠᱚ, ᱵᱳᱨᱰ ᱥᱮᱫ ᱧᱮᱞ ᱢᱮ ᱾",
        "phrase_9.wav"
    ),
    (
        "बहुत अच्छा!",
        "ᱟᱹᱰᱤ ᱱᱟᱯᱟᱭ ᱠᱟᱱᱟ ᱾",
        "phrase_10.wav"
    )
]


# ==========================================
# INSERT PHRASES
# ==========================================

cursor.executemany("""
INSERT INTO classroom_phrases
(hindi, santhali, audio_file)
VALUES (?, ?, ?)
""", phrases)


# ==========================================
# SAVE DATABASE
# ==========================================

connection.commit()

connection.close()


print("======================================")
print("✅ Classroom phrase database created!")
print("======================================")
print(f"Database: {DATABASE_PATH}")
print(f"Total phrases added: {len(phrases)}")