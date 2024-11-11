package kpng.processing.handlers

import kpng.model.StandardChunk
import kpng.metadata.ChunkMetadata
import kpng.metadata.tIME
import java.time.LocalDateTime


class tIMEHandler : ChunkHandler() {
    override val chunkType = StandardChunk.tIME.name
    override fun parse(bytes: ByteArray): ChunkMetadata {
        require(bytes.size == 7) {
            "tIME chunk should contain 7 bytes of data"
        }

        val year = consumeValue(bytes, 2)
        val month = consumeValue(bytes, 1)
        val day = consumeValue(bytes, 1)
        val hour = consumeValue(bytes, 1)
        val minute = consumeValue(bytes, 1)
        val second = consumeValue(bytes, 1)

        return tIME(LocalDateTime.of(year, month, day, hour, minute, second))
    }
}