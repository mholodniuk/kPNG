package kpng.processing.handlers

import kpng.model.StandardChunk
import kpng.metadata.ChunkMetadata
import kpng.metadata.IHDR


class IHDRHandler : ChunkHandler() {
    override val chunkType = StandardChunk.IHDR.name

    override fun parse(bytes: ByteArray): ChunkMetadata {
        val width = consumeValue(bytes, 4)
        val height = consumeValue(bytes, 4)
        val bitDepth = consumeValue(bytes, 1)
        val colorType = consumeValue(bytes, 1)
        val compressionMethod = consumeValue(bytes, 1)
        val filterMethod = consumeValue(bytes, 1)
        val interlaceMethod = consumeValue(bytes, 1)

        return IHDR(width, height, bitDepth, colorType, compressionMethod, filterMethod, interlaceMethod)
    }
}