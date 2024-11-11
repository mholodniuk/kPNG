package kpng.metadata

import kpng.model.Chunk
import kpng.processing.handlers.ChunkHandler
import kpng.processing.dependency.Requires

typealias ChunkType = String

class ChunkInterpreter {
    private val chunks: MutableList<Chunk> = mutableListOf()
    private val handlers: MutableMap<ChunkType, ChunkHandler> = mutableMapOf()

    fun registerChunkHandlers(vararg newHandlers: ChunkHandler) = apply { newHandlers.forEach { handlers[it.chunkType] = it } }

    fun addChunks(newChunks: Collection<Chunk>) = apply { chunks.addAll(newChunks) }

    fun getMetadata(): List<ChunkMetadata> {
        return chunks.mapNotNull { chunk ->
            val handler = getHandlerWithDependencies(chunk)
            handler?.getMetadata(chunk.bytes)
        }
    }

    private fun getHandlerWithDependencies(chunk: Chunk): ChunkHandler? {
        return handlers[chunk.type]?.apply {
            if (hasMissingDependency()) {
                fulfillHandlerDependency(chunk)
            }
        }
    }

    private fun ChunkHandler.hasMissingDependency(): Boolean {
        return neededChunk == null && this::class.annotations.any { it is Requires }
    }

    private fun ChunkHandler.fulfillHandlerDependency(chunk: Chunk) {
        val requiredChunk = this::class.annotations.find { it is Requires }!! as Requires

        val fillingChunk = chunks.find { it.type == requiredChunk.type.name }?.let { missingChunk ->
            handlers[missingChunk.type]?.getMetadata(missingChunk.bytes)
        }

        require(fillingChunk != null) {
            "Can't find required [${requiredChunk.type}] chunk handler to satisfy [${chunk.type}] chunk dependency"
        }

        this.neededChunk = fillingChunk
    }
}