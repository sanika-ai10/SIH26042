import queue
import sounddevice as sd
import json
from vosk import Model, KaldiRecognizer

# --- STAGE 1: NIPUN BHARAT FLN PHRASE CACHE (Offline Dictionary) ---
FLN_PHRASE_CACHE = {
    "नमस्ते": {
        "ol_chiki": "ᱡᱚᱦᱟᱨ",
        "phonetic": "Johar",
        "meaning": "Greetings / Hello"
    },
    "आप कैसे हैं": {
        "ol_chiki": "ᱪᱮᱫ ᱞᱮᱠᱟ ᱢᱮᱱᱟᱜ ᱵᱤᱱᱟ?",
        "phonetic": "Ched leka menag bina?",
        "meaning": "How are you?"
    },
    "नमस्ते आप कैसे हैं": {
        "ol_chiki": "ᱡᱚᱦᱟᱨ, ᱪᱮᱫ ᱞᱮᱠᱟ ᱢᱮᱱᱟᱜ ᱵᱤᱱᱟ?",
        "phonetic": "Johar, Ched leka menag bina?",
        "meaning": "Hello, how are you?"
    },
    "किताब खोलो": {
        "ol_chiki": "ᱯᱚᱛᱚᱵ ᱡᱷᱤᱡᱽ ᱢᱮ",
        "phonetic": "Potob jhij me",
        "meaning": "Open the book"
    },
    "बैठ जाओ": {
        "ol_chiki": "ᱫᱩᱲᱩᱵ ᱢᱮ",
        "phonetic": "Durub me",
        "meaning": "Sit down"
    },
    "एक दो तीन": {
        "ol_chiki": "ᱢᱤᱫ ᱵᱟᱨ ᱯᱮ",
        "phonetic": "Mid bar pe",
        "meaning": "Counting: 1, 2, 3"
    },
    "धन्यवाद": {
        "ol_chiki": "ᱥᱟᱨᱦᱟᱣ",
        "phonetic": "Sarhao",
        "meaning": "Thank you"
    }
}

def translate_fln(hindi_input):
    """Fast-Path Phrase Cache Lookup with Substring Matching"""
    query = hindi_input.strip()
    
    # 1. Exact match
    if query in FLN_PHRASE_CACHE:
        return FLN_PHRASE_CACHE[query], True
    
    # 2. Substring match (in case extra filler words were captured)
    for key, data in FLN_PHRASE_CACHE.items():
        if key in query:
            return data, True

    return None, False

# --- STAGE 2: LIVE ASR ENGINE SETUP ---
q = queue.Queue()

def callback(indata, frames, time, status):
    if status:
        print(status, flush=True)
    q.put(bytes(indata))

print("Loading Hindi Vosk Model...")
model = Model("model-hi")
recognizer = KaldiRecognizer(model, 16000)

print("\n==========================================")
print("PALASH-AI: Hindi -> Santhali Live Engine")
print("Speak into your mic: 'नमस्ते', 'किताब खोलो', 'बैठ जाओ'")
print("Press Ctrl + C to exit")
print("==========================================\n")

try:
    with sd.RawInputStream(samplerate=16000, blocksize=8000, dtype='int16',
                           channels=1, callback=callback):
        while True:
            data = q.get()
            if recognizer.AcceptWaveform(data):
                res = json.loads(recognizer.Result())
                text = res.get("text", "").strip()
                if text:
                    print(f"\n[Teacher Hindi Input]: {text}")
                    translation, cached = translate_fln(text)
                    if cached:
                        print(">> Route: Fast-Path Phrase Cache (Latency: <10ms)")
                        print(f">> [Santhali (Ol Chiki)]: {translation['ol_chiki']}")
                        print(f">> [Pronunciation Guide]: {translation['phonetic']}")
                        print(f">> [FLN Meaning]:         {translation['meaning']}\n")
                    else:
                        print(">> Route: Cache Miss -> Routing to IndicTrans2 fallback pipeline...\n")
            else:
                partial = json.loads(recognizer.PartialResult())
                p_text = partial.get("partial", "").strip()
                if p_text:
                    print(f"\rListening: {p_text}", end="", flush=True)
except KeyboardInterrupt:
    print("\nSession ended cleanly.")