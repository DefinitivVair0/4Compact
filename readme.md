# 4Compact
This is my try on an image compression method which packs the RGB color information of four pixels into one ARGB value, thus reducing the number of pixels by 75%.

Important note: This is just a proof of concept and DOES NOT WORK.
-> the program inputs a 24bit image (/ 32 and discards alpha) but has to pack 4 channels into one, the bit depth of the compressed (and thus re-uncompressed) image will be decreased drastically.
-> the compressed and uncompressed files are either the same size (for very colourful images) or smaller (for more monotone pictures) because of the decrease in bit depth.
    -> the program itself does not lower the file size but the pixel amount.

But: the concept works -> sharp contours and greater contrasts are still as visible as in the original in exchange for color banding and loss of color information.


# How to use
1. Copy the to be compacted image into the execution directory and rename to "image.png"
2. Run the program
3. The program generates the compressed (--encode / -e) or re-uncompressed image
   (-> when no (valid) argument is supplied the image will be both en- and decoded.)

(The program does not work the way it was meant to, but at least it looks cool?)