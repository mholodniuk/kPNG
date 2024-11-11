package kpng.util

import java.io.File
import java.nio.ByteBuffer

fun ByteArray.toHexString() = joinToString(separator = "") { "%02x".format(it) }

fun List<Byte>.toHexString() = joinToString(separator = "") { "%02x".format(it) }

fun ByteArray.toInt() = ByteBuffer.wrap(this).getInt()

fun readFileAsByteArray(fileName: String): ByteArray {
    val classLoader = object {}.javaClass.classLoader
    val file = File(classLoader.getResource(fileName)?.file!!)

    return file.readBytes()
}

fun <T> Sequence<T>.takeWhileInclusive(predicate: (T) -> Boolean) = sequence {
    with(iterator()) {
        while (hasNext()) {
            val next = next()
            yield(next)
            if (!predicate(next)) break
        }
    }
}