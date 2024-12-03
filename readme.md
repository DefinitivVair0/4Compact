# 4Compact
This is my try on an image compression method wich packs the RGB color information of four pixels into one ARGB value, thus reducing the number of pixels by 75%.

Imortant note: This is just a proof of concept and DOES NOT WORK.
Since the program inputs a 24bit image (/ 32 and discards alpha) but generates a 32bit image (including alpha), the file size of the compressed image is the same as the original image (or lower but pixel alpha information will be lost).
-> the uncompressed image is way larger than the original (working on fixing that -> uncompressed should only be 24bit instead of 32bit) -> original file size >= compressed file size = uncompressed file size
-> but: the concept works -> Color banding and loss of color information but contours and greater contrasts are still as visible as in the original.


# How to use
1. Copy the to be compacted image into the execution directory and rename to "image.png"
2. Run the program
3. The program generates both the compressed and the re-uncompressed version of the image (selection will be added in the future)

(The program might not work the way it was meant to but at least it looks cool?)
