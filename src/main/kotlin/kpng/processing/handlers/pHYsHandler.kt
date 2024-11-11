package kpng.processing.handlers

import kpng.model.StandardChunk
import kpng.metadata.ChunkMetadata
import kpng.metadata.pHYs


class pHYsHandler : ChunkHandler() {
    override val chunkType = StandardChunk.pHYs.name

    override fun parse(bytes: ByteArray): ChunkMetadata {
        require(bytes.size == 9) {
            "pHYs chunk should contain 9 bytes of data"
        }

        val pixelsPerUnitX = consumeValue(bytes, 4)
        val pixelsPerUnitY = consumeValue(bytes, 4)
        val unitSpecifier = consumeValue(bytes, 1)

        return pHYs(pixelsPerUnitX, pixelsPerUnitY, unitSpecifier)
    }
}