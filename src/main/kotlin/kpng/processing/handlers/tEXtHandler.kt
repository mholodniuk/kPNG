package kpng.processing.handlers

import kpng.model.StandardChunk
import kpng.metadata.ChunkMetadata
import kpng.metadata.tEXt


class tEXtHandler : ChunkHandler() {
    override val chunkType = StandardChunk.tEXt.name

    override fun parse(bytes: ByteArray): ChunkMetadata {
        val data = bytes.decodeToString()
        val separatorIndex = data.indexOf("\u0000")
        val keyword = data.substring(0, separatorIndex)
        val text = data.substring(separatorIndex + 1)

        return tEXt(keyword, text)
    }
}