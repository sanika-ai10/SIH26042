# AI-Powered Vernacular Pedagogy and Real-Time Translation Tool


An AI-powered, offline-first educational solution designed to support mother-tongue-based primary education by enabling real-time Hindi-to-Santhali voice translation and bilingual learning support.

---

## Problem Statement
**AI-Powered Vernacular Pedagogy and Real-Time Translation Tool for Mother Tongue-Based Primary Education**  
In multilingual classrooms, language barriers between Hindi-speaking educators and Santhali-speaking primary students impede foundational literacy and numeracy (FLN). This project bridges that gap by providing an offline-capable, real-time voice-to-voice translation pipeline that renders Santhali in its native **Ol Chiki** script.

---

## Proposed Solution
The platform integrates an edge-optimized Speech-to-Text (ASR) engine, a fast-path curriculum phrase cache, machine translation, and speech synthesis into a low-latency pipeline designed for low-resource environments.

### Core Pipeline
Teacher Speaks (Hindi Audio)
│
▼
Offline Hindi ASR (Vosk / Kaldi)
│
▼
Recognized Hindi Text
│
▼
Fast-Path FLN Cache ──[Cache Miss]──► IndicTrans2 Engine
│                                   │
└─────────────────┬─────────────────┘
▼
Santhali Text (Ol Chiki)
│
▼
Santhali Audio Playback / TTS


---

## Key Features
- **Offline Hindi Speech-to-Text (ASR):** Ultra-low-latency real-time voice transcription running entirely on CPU via Vosk/Kaldi.
- **Fast-Path FLN Phrase Cache:** Sub-10ms localized translation lookup for frequent foundational classroom instructions.
- **Hindi-to-Santhali Translation:** Neural machine translation for dynamic classroom instruction.
- **Dual-Script Output:** Native Santhali displayed in authentic **Ol Chiki** script with phonetic guidance.
- **Zero Cloud Reliance:** Operates completely offline, making it functional in remote rural schools without active internet connectivity.
- **NIPUN Bharat Alignment:** Pre-configured pedagogical vocabulary tailored to primary grade curriculums.

---

## Technical Architecture

### Speech Recognition (ASR)
- **Engine:** Vosk (Kaldi-based acoustic model)
- **Model:** `vosk-model-small-hi-0.22` (~45 MB)
- **Execution:** Pure CPU inference, 16 kHz mono raw stream
- **Rationale:** Minimizes memory footprint and eliminates the high computational overhead (and GPU dependence) of large transformer models like IndicConformer, ensuring real-time response on budget hardware.

### Machine Translation & Speech
- **Translation:** IndicTrans2 (Hindi to Santhali)
- **Speech Synthesis:** Indic Parler-TTS / offline voice synthesis pipeline

### Edge & Runtime Environment
- **Python 3.10+**
- **sounddevice & NumPy:** Real-time audio queue and buffer management
- **Target Platform:** Low-resource Android tablets and budget classroom laptops

---
