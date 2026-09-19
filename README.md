# AI-Powered Vernacular Pedagogy and Real-Time Translation Tool

### SIH26042 | Smart India Hackathon 2026

An AI-powered, offline-first educational solution designed to support **mother-tongue-based primary education** by enabling Hindi-to-Santhali translation and voice-based learning support.

## Problem Statement

**AI-Powered Vernacular Pedagogy and Real-Time Translation Tool for Mother Tongue-Based Primary Education**

The project aims to reduce language barriers between Hindi-speaking teachers and Santhali-speaking students by providing translation and educational content in the student's mother tongue.

## Proposed Solution

The system combines speech recognition, machine translation, text-to-speech, and curriculum-aligned educational resources.

### Core Pipeline

**Hindi Speech/Text → Hindi ASR → Hindi Text → Hindi-to-Santhali Translation → Santhali/Ol Chiki Text → Santhali TTS → Text + Audio**

## Key Features

- Hindi speech-to-text
- Hindi-to-Santhali translation
- Santhali text-to-speech
- Bilingual Hindi + Santhali learning content
- Worksheets and visual flashcards
- Offline-first architecture
- Local audio and content caching
- Android-based deployment
- On-device AI optimization

## Technologies

### AI / NLP
- IndicConformer
- IndicTrans2
- Indic Parler-TTS

### Application
- Python
- Kotlin
- Android
- SQLite

### Offline / Edge
- ONNX Runtime Mobile
- Model Quantization
- Local Audio & Content Cache

## Project Status

**Prototype / Under Development**

The current implementation focuses on the Hindi-to-Santhali translation foundation. Future development includes speech recognition, text-to-speech, Android integration, curriculum content, worksheets, flashcards, and low-resource device optimization.

## Target

The solution is initially designed for **low-resource Android devices** and educational environments with limited internet connectivity.

## References

- Smart India Hackathon 2026 — SIH26042
- NIPUN Bharat
- AI4Bharat — IndicTrans2
- AI4Bharat — IndicConformer
- AI4Bharat — Indic Parler-TTS
