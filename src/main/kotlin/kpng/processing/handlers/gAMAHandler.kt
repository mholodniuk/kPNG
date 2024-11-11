package kpng.processing.handlers

import kpng.model.StandardChunk
import kpng.metadata.ChunkMetadata
import kpng.metadata.gAMA
import kpng.util.toInt

class gAMAHandler : ChunkHandler() {
    override val chunkType = StandardChunk.gAMA.name

    override fun parse(bytes: ByteArray): ChunkMetadata {
        val gammaFactor = bytes.toInt().toDouble() / 100000

        return gAMA(gammaFactor)
    }
}