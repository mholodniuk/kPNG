package kpng.processing.handlers

import kpng.model.StandardChunk
import kpng.processing.dependency.Requires
import kpng.metadata.ChunkMetadata
import kpng.metadata.IHDR
import kpng.metadata.PLTE

@Requires(StandardChunk.IHDR)
class PLTEHandler : ChunkHandler() {
    override val chunkType = StandardChunk.PLTE.name

    override fun parse(bytes: ByteArray): ChunkMetadata {
        validateBytes(bytes)

        val palette = mutableListOf<PLTE.ColorEntry>()

        for (idx in bytes.indices step 3) {
            val red = readColor(bytes, idx)
            val green = readColor(bytes, idx + 1)
            val blue = readColor(bytes, idx + 2)
            palette.add(PLTE.ColorEntry(red, green, blue))
        }

        return PLTE(palette, palette.size)
    }

    private fun validateBytes(bytes: ByteArray) {
        require(neededChunk != null) {
            "bKGd chunk requires parsing IHDR chunk first"
        }
        val ihdr = neededChunk as IHDR
        require(bytes.size % 3 == 0) {
            "PLTE chunk length is not divisible by 3"
        }
        val numEntries = bytes.size / 3
        require(!(ihdr.colorType == 0 || ihdr.colorType == 4)) {
            "PLTE chunk should not appear for color types 0 and 4"
        }
        require(!(ihdr.colorType == 3 && numEntries > 1 shl ihdr.bitDepth || numEntries > 256)) {
            "Number of palette entries is out of range"
        }
        require(!(ihdr.colorType != 3 && numEntries > 0)) {
            "PLTE chunk should not appear for color types other than 3"
        }
    }

    private fun readColor(bytes: ByteArray, index: Int): Int {
        return bytes[index].toUByte().toInt()
    }
}