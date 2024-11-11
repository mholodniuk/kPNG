package kpng.processing.handlers

import kpng.model.StandardChunk
import kpng.processing.dependency.Requires
import kpng.metadata.ChunkMetadata
import kpng.metadata.IHDR
import kpng.metadata.bKGd


@Requires(StandardChunk.IHDR)
class bKGdHandler : ChunkHandler() {
    override val chunkType = StandardChunk.bKGD.name

    override fun parse(bytes: ByteArray): ChunkMetadata {
        require(neededChunk != null) {
            "bKGd chunk requires parsing IHDR chunk first"
        }

        return when {
            isPalette(bytes) -> bKGd(type = bKGd.Type.PALETTE, paletteIndex = consumeValue(bytes, 1))
            isGray(bytes) -> bKGd(type = bKGd.Type.GRAY, gray = consumeValue(bytes, 2))
            isRGB(bytes) -> {
                val red = consumeValue(bytes, 2)
                val green = consumeValue(bytes, 2)
                val blue = consumeValue(bytes, 2)
                bKGd(type = bKGd.Type.RGB, rgb = bKGd.RGB(red, green, blue))
            }

            else -> throw IllegalArgumentException("bKGD chunk contains invalid data")
        }
    }

    private fun isPalette(bytes: ByteArray): Boolean {
        return (IHDR().colorType == 3 && bytes.size == 1)
    }

    private fun isGray(bytes: ByteArray): Boolean {
        return ((IHDR().colorType == 0 || IHDR().colorType == 4) && bytes.size == 2)
    }

    private fun isRGB(bytes: ByteArray): Boolean {
        return ((IHDR().colorType == 2 || IHDR().colorType == 6) && bytes.size == 6)
    }
    
    private fun IHDR() = neededChunk as IHDR
}