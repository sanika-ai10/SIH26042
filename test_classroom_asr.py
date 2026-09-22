from asr_service import HindiASR

asr = HindiASR()

test_prompts = [
    "किताब खोलो",          # Open the book
    "यहाँ देखो",             # Look here
    "चुपचाप बैठो",          # Sit quietly
    "अपना नाम बताओ",       # Tell your name
    "गृहकार्य दिखाओ",       # Show your homework
    "पाठ ध्यान से सुनो",      # Listen carefully to the lesson
    "श्यामपट्ट पर लिखो"       # Write on the blackboard
]

print("\n--- Classroom Benchmark ---")
print("Target phrases to test:")
for i, p in enumerate(test_prompts, 1):
    print(f"  {i}. {p}")
print("---------------------------\n")

for i in range(1, 6):
    print(f"\n[Test Run {i}/5]")
    transcription = asr.listen_and_transcribe()
    print(f"Captured: {transcription}")