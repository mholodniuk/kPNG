package kpng.model

data class Chunk(
    val type: String,
    val length: Int,
    val offset: Int,
    val bytes: List<Byte>,
    val crc: String
) {
    val isCritical get() = type[0].isUpperCase()
    val isPublic get() = type[1].isUpperCase()
    val isReserved get() = type[2].isUpperCase()
    val isUnsafeToCopy get() = type[3].isUpperCase()
}