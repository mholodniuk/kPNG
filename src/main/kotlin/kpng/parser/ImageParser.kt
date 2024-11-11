package kpng.parser

import kpng.model.Chunk
import kpng.util.*


class ImageParser(private var imageBytes: ByteArray) {
    private var currentOffset = PNG_SIGNATURE_SIZE

    init {
        val signature = imageBytes.take(PNG_SIGNATURE_SIZE).toHexString()
        require(signature == PNG_SIGNATURE) {
            "Invalid PNG file signature. Should be $PNG_SIGNATURE but was $signature"
        }
    }

    fun parse() = generateSequence { parseChunk() }.takeUntilIEND().toList()

    private fun parseChunk(): Chunk {
        val offset = currentOffset
        val length = readLength()
        val type = readType()
        val rawBytes = readRawBytes(length)
        val crc = readCRC()

        return Chunk(type, length, offset, rawBytes.toList(), crc)
    }

    private fun readLength(): Int {
        val offset = currentOffset
        incrementOffsetBy(LENGTH_FIELD_LENGTH)
        return (imageBytes[offset].toInt() shl 24) or
                (imageBytes[offset + 1].toInt() and 0xFF shl 16) or
                (imageBytes[offset + 2].toInt() and 0xFF shl 8) or
                (imageBytes[offset + 3].toInt() and 0xFF)
    }

    private fun readType(): String {
        return getBytesAndIncrementOffset(TYPE_FIELD_LENGTH).decodeToString()
    }

    private fun readCRC(): String {
        return getBytesAndIncrementOffset(CRC_FIELD_LENGTH).toHexString()
    }

    private fun readRawBytes(length: Int): ByteArray {
        val end = (currentOffset + length) - 1
        return imageBytes.sliceArray(currentOffset..end).also { incrementOffsetBy(length) }
    }

    private fun getBytesAndIncrementOffset(numOfBytes: Int): ByteArray {
        return ByteArray(numOfBytes) { imageBytes[currentOffset + it] }.also { incrementOffsetBy(numOfBytes) }
    }

    private fun incrementOffsetBy(numOfBytes: Int) {
        currentOffset += numOfBytes
    }

    private fun Sequence<Chunk>.takeUntilIEND() = takeWhileInclusive { it.type != "IEND" }.also { currentOffset = PNG_SIGNATURE_SIZE }

    companion object {
        private const val PNG_SIGNATURE = "89504e470d0a1a0a"
        private const val PNG_SIGNATURE_SIZE = 8
        private const val LENGTH_FIELD_LENGTH = 4
        private const val TYPE_FIELD_LENGTH = 4
        private const val CRC_FIELD_LENGTH = 4
    }
}