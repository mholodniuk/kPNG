package kpng.metadata

import java.time.LocalDateTime


typealias MetadataMap = Map<String, Any>

open class ChunkMetadata(val rawData: MetadataMap? = null)

data class IHDR(
    val width: Int,
    val height: Int,
    val bitDepth: Int,
    val colorType: Int,
    val compressionMethod: Int,
    val filterMethod: Int,
    val interlaceMethod: Int,
) : ChunkMetadata()

data class tIME(
    val date: LocalDateTime,
) : ChunkMetadata()

data class tEXt(
    val keyword: String,
    val text: String,
) : ChunkMetadata()

data class gAMA(
    val gamma: Double,
) : ChunkMetadata()

data class bKGd(
    val type: Type,
    val paletteIndex: Int? = null,
    val gray: Int? = null,
    val rgb: RGB? = null
) : ChunkMetadata() {
    enum class Type { PALETTE, GRAY, RGB }
    data class RGB(val red: Int, val green: Int, val blue: Int)
}

data class PLTE(
    val palette: List<ColorEntry>,
    val numberOfEntries: Int
) : ChunkMetadata() {
    data class ColorEntry(val red: Int, val green: Int, val blue: Int)
}

data class pHYs(
    val pixelsPerUnitX: Int,
    val pixelsPerUnitY: Int,
    val unitSpecifier: Int
) : ChunkMetadata()

data class IDAT(val data: MetadataMap? = null) : ChunkMetadata(data)

data class IEND(val data: MetadataMap? = null) : ChunkMetadata(data)
