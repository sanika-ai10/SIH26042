# AI-Powered Vernacular Pedagogy and Real-Time Translation Tool

## SIH26042 — Smart India Hackathon 2026

An AI-powered, offline-first educational solution designed to support **mother-tongue-based primary education** by enabling Hindi-to-Santhali translation, voice interaction, and bilingual learning resources.

## 📌 Problem Statement

**SIH26042 — AI-Powered Vernacular Pedagogy and Real-Time Translation Tool for Mother Tongue-Based Primary Education**

Primary-school teachers may face difficulties communicating learning content when their language differs from the children's mother tongue. This project aims to bridge that language gap by providing Hindi-to-Santhali translation and educational content in both languages.

## 💡 Proposed Solution

The proposed system combines speech recognition, machine translation, text-to-speech, and curriculum-aligned educational resources.

The core translation pipeline is:

**Hindi Speech/Text → Hindi ASR → Hindi Text → Hindi-to-Santhali Translation → Santhali/Ol Chiki Text → Santhali TTS → Text + Audio**

The solution follows an **offline-first approach**, with the goal of enabling core functionality on low-resource Android devices.

## ✨ Key Features

- 🎤 Hindi speech-to-text
- 🔄 Hindi-to-Santhali translation
- 🔊 Santhali text-to-speech
- 📝 Bilingual Hindi + Santhali educational content
- 🃏 Curriculum-aligned worksheets and visual flashcards
- 📚 Curriculum-specific vocabulary
- 📵 Offline-first operation
- 💾 Local content and audio caching
- 📱 Android-based deployment
- ⚡ Optimization for resource-constrained devices

## 🧠 Technologies

### AI / NLP
- **IndicConformer** — Hindi speech recognition
- **IndicTrans2** — Hindi-to-Santhali translation
- **Indic Parler-TTS** — Santhali text-to-speech

### Application
- Android
- Python
- Kotlin
- SQLite

### Offline / Edge
- ONNX Runtime Mobile
- Model Quantization
- Local Audio & Content Cache

### Educational Content
- NIPUN Bharat FLN-aligned content
- Curriculum-specific vocabulary
- Bilingual worksheets
- Visual flashcards

## 🔄 System Workflow

```text
Teacher
   ↓
Hindi Speech / Text
   ↓
Hindi ASR
(IndicConformer)
   ↓
Hindi Text
   ↓
Phrase Cache
   ↓
Hindi → Santhali Translation
(IndicTrans2)
   ↓
Santhali / Ol Chiki Text
   ↓
Santhali TTS
(Indic Parler-TTS)
   ↓
Santhali Audio + Text
   ↓
Student
NIPUN Bharat FLN Content
          ↓
   Curriculum Engine
          ↓
Worksheets / Flashcards
          ↓
 Hindi + Santhali Content                                                                                                                                                                                                             
