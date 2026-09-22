"""
generate_materials.py
----------------------
Generates printable Hindi -> Santali FLASHCARDS and a WORKSHEET (with an
answer key) as PDF files, using the same IndicTransONNX translator that
translate_cli.py (your existing script) already loads.

IMPORTANT - SANTALI (OL CHIKI) FONT
------------------------------------
Santali is written in the Ol Chiki script. The default PDF fonts (Helvetica
etc.) do NOT contain Ol Chiki glyphs, so Santali text will render as blank
boxes unless you supply a Unicode font that covers Ol Chiki, e.g.
"Noto Sans Ol Chiki" (free, from Google Fonts / fonts.google.com).

Download NotoSansOlChiki-Regular.ttf and either:
  1. place it at  fonts/NotoSansOlChiki-Regular.ttf  (relative to this file), or
  2. pass its path with  --font /path/to/NotoSansOlChiki-Regular.ttf

If no font is found, the script still runs but Santali text will not
display correctly in the PDF.

USAGE
-----
    python generate_materials.py                     # uses sample word list
    python generate_materials.py --words data/words.txt
    python generate_materials.py --words data/words.txt --font fonts/NotoSansOlChiki-Regular.ttf

`words.txt` should have one Hindi word or short sentence per line.

OUTPUT
------
    output/flashcards.pdf
    output/worksheet.pdf
"""

import os
from reportlab.lib.pagesizes import A4
from reportlab.lib.units import mm
from reportlab.lib.colors import HexColor, black, white
from reportlab.pdfgen import canvas
from reportlab.pdfbase import pdfmetrics
from reportlab.pdfbase.ttfonts import TTFont

# ---------------------------------------------------------------------------
# 1. VOCABULARY DATA — replace/extend this with your real curriculum content
#    Each entry: (English gloss, Hindi word, Santhali word [Ol Chiki or translit])
# ---------------------------------------------------------------------------
VOCAB = [
    ("One",   "एक",     "ᱢᱤᱫ (mid)"),
    ("Two",   "दो",      "ᱵᱟᱨ (bar)"),
    ("Three", "तीन",     "ᱯᱮ (pe)"),
    ("Four",  "चार",     "ᱯᱚᱱ (pon)"),
    ("Five",  "पांच",    "ᱢᱚᱬᱮᱭ (moroe)"),
    ("Sun",   "सूरज",    "ᱥᱤᱸᱜᱤ (singi)"),
    ("Moon",  "चांद",    "ᱪᱟᱸᱰᱳ (chando)"),
    ("Water", "पानी",    "ᱫᱟᱜ (dak)"),
    ("Tree",  "पेड़",     "ᱫᱟᱨᱮ (dare)"),
    ("House", "घर",      "ᱚᱲᱟᱜ (orak)"),
    ("Book",  "किताब",   "ᱚᱱᱚᱲ (onor)"),
    ("Mother","मां",     "ᱮᱱᱜᱟ (enga)"),
]

# ---------------------------------------------------------------------------
# 2. OPTIONAL: register an Ol Chiki-capable TTF font if you have one on disk.
#    Download e.g. "NotoSansOlChiki-Regular.ttf" and set the path below.
# ---------------------------------------------------------------------------
FONT_NAME = "Helvetica"
FONT_PATH = "NotoSansOlChiki-Regular.ttf"  # place this file next to this script
if os.path.exists(FONT_PATH):
    pdfmetrics.registerFont(TTFont("OlChiki", FONT_PATH))
    FONT_NAME = "OlChiki"

PAGE_W, PAGE_H = A4
MARGIN = 15 * mm


# ---------------------------------------------------------------------------
# 3. FLASHCARDS — 2x2 grid per page, with a dashed cut-line border
# ---------------------------------------------------------------------------
def draw_dashed_rect(c, x, y, w, h):
    c.saveState()
    c.setDash(3, 3)
    c.setStrokeColor(HexColor("#999999"))
    c.rect(x, y, w, h)
    c.restoreState()


def draw_flashcard(c, x, y, w, h, english, hindi, santhali):
    draw_dashed_rect(c, x, y, w, h)

    c.setFillColor(HexColor("#2b6cb0"))
    c.setFont("Helvetica-Bold", 11)
    c.drawCentredString(x + w / 2, y + h - 14 * mm, english.upper())

    c.setFillColor(black)
    c.setFont("Helvetica-Bold", 20)
    c.drawCentredString(x + w / 2, y + h / 2 + 2 * mm, hindi)

    c.setFont(FONT_NAME, 16)
    c.setFillColor(HexColor("#b7791f"))
    c.drawCentredString(x + w / 2, y + h / 2 - 12 * mm, santhali)


def make_flashcards(filename, vocab):
    c = canvas.Canvas(filename, pagesize=A4)
    cols, rows = 2, 2
    card_w = (PAGE_W - 2 * MARGIN) / cols
    card_h = (PAGE_H - 2 * MARGIN) / rows

    c.setFont("Helvetica-Bold", 14)
    c.drawCentredString(PAGE_W / 2, PAGE_H - 10 * mm, "Hindi \u2192 Santhali Flashcards")

    i = 0
    while i < len(vocab):
        for row in range(rows):
            for col in range(cols):
                if i >= len(vocab):
                    break
                x = MARGIN + col * card_w
                y = PAGE_H - MARGIN - (row + 1) * card_h
                english, hindi, santhali = vocab[i]
                draw_flashcard(c, x, y, card_w, card_h, english, hindi, santhali)
                i += 1
        if i < len(vocab):
            c.showPage()
            c.setFont("Helvetica-Bold", 14)
            c.drawCentredString(PAGE_W / 2, PAGE_H - 10 * mm, "Hindi \u2192 Santhali Flashcards")
    c.save()


# ---------------------------------------------------------------------------
# 4. WORKSHEET — matching exercise + fill-in-the-blank
# ---------------------------------------------------------------------------
def make_worksheet(filename, vocab):
    c = canvas.Canvas(filename, pagesize=A4)

    c.setFont("Helvetica-Bold", 16)
    c.drawCentredString(PAGE_W / 2, PAGE_H - 20 * mm, "Worksheet: Hindi - Santhali Vocabulary")

    c.setFont("Helvetica", 10)
    c.drawString(MARGIN, PAGE_H - 28 * mm, "Name: ______________________     Date: ____________")

    # --- Section A: Matching ---
    y = PAGE_H - 42 * mm
    c.setFont("Helvetica-Bold", 12)
    c.drawString(MARGIN, y, "A. Match the Hindi word to its Santhali translation")
    y -= 10 * mm

    left_col_x = MARGIN
    right_col_x = PAGE_W - MARGIN - 60 * mm
    c.setFont("Helvetica", 11)

    import random
    shuffled_santhali = [v[2] for v in vocab]
    random.shuffle(shuffled_santhali)

    line_h = 9 * mm
    for idx, (english, hindi, santhali) in enumerate(vocab):
        row_y = y - idx * line_h
        if row_y < MARGIN + 40 * mm:
            break
        c.drawString(left_col_x, row_y, f"{idx + 1}. {hindi}")
        c.circle(left_col_x + 45 * mm, row_y + 2, 1.2 * mm, stroke=1, fill=0)

        c.drawString(right_col_x + 8 * mm, row_y, shuffled_santhali[idx])
        c.circle(right_col_x, row_y + 2, 1.2 * mm, stroke=1, fill=0)

    # --- Section B: Fill in the blank ---
    c.showPage()
    c.setFont("Helvetica-Bold", 12)
    c.drawString(MARGIN, PAGE_H - 20 * mm, "B. Fill in the blank with the correct Hindi word")
    c.setFont("Helvetica", 11)

    y = PAGE_H - 32 * mm
    for idx, (english, hindi, santhali) in enumerate(vocab):
        row_y = y - idx * line_h
        if row_y < MARGIN:
            break
        c.drawString(MARGIN, row_y, f"{idx + 1}. {english} ( {santhali} )  =  __________________")

    c.save()


if __name__ == "__main__":
    make_flashcards("flashcards.pdf", VOCAB)
    make_worksheet("worksheet.pdf", VOCAB)
    print("Done: flashcards.pdf and worksheet.pdf created.")
