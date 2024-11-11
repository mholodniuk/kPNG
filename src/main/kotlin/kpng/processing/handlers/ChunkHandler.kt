package kpng.processing.handlers

import kpng.metadata.ChunkMetadata

abstract class ChunkHandler {
    private var iterator: Int = 0
    var neededChunk: ChunkMetadata? = null

    abstract val chunkType: String

    protected abstract fun parse(bytes: ByteArray): ChunkMetadata

    fun getMetadata(bytes: List<Byte>): ChunkMetadata {
        return parse(bytes.toByteArray()).also { iterator = 0 }
    }

    protected fun consumeValue(rawBytes: ByteArray, size: Int): Int {
        var result = 0
        (0..<size).forEach { i ->
            result = result shl 8 or (rawBytes[iterator + i].toInt() and 0xFF)
        }
        iterator += size
        return result
    }
}