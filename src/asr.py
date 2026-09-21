from transformers import pipeline

print("Loading Hindi ASR model...")

asr = pipeline(
    "automatic-speech-recognition",
    model="ai4bharat/indicconformer_stt_hi_hybrid_ctc_rnnt_large"
)

print("Hindi ASR model loaded successfully!")