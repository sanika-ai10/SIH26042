package com.example.model

enum class FlashcardCategory(val labelHindi: String, val labelEnglish: String) {
    ALL("सभी", "All"),
    OBJECTS("कक्षा की वस्तुएं", "Classroom Objects"),
    FRUITS("फल और खाना", "Fruits & Food"),
    ANIMALS("पशु और पक्षी", "Animals & Birds"),
    NUMBERS("संख्याएं (FLN)", "Numbers"),
    NATURE("प्रकृति और शरीर", "Nature & Body")
}

data class FlashcardItem(
    val id: String,
    val hindiWord: String,
    val santhaliOlChiki: String,
    val pronunciationLatin: String,
    val pronunciationDevanagari: String,
    val category: FlashcardCategory,
    val emojiIcon: String,
    val englishMeaning: String,
    val sentenceHindi: String,
    val sentenceOlChiki: String
)
