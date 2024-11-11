import kpng.metadata.ChunkInterpreter
import kpng.parser.ImageParser
import kpng.processing.handlers.*
import kpng.util.readFileAsByteArray


fun main() {
    val image = readFileAsByteArray("samples/bKGD-pHYs-tIME-tEXt.png")

    val chunks = ImageParser(image).parse()

    val metadata = ChunkInterpreter()
        .addChunks(chunks)
        .registerChunkHandlers(
            IHDRHandler(),
            bKGdHandler(),
            gAMAHandler(),
            pHYsHandler(),
            tIMEHandler(),
            tEXtHandler(),
            pHYsHandler()
        )
        .getMetadata()

    metadata.forEach(::println)
}