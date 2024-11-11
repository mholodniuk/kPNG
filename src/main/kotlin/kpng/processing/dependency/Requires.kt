package kpng.processing.dependency

import kpng.model.StandardChunk

@Target(AnnotationTarget.CLASS)
annotation class Requires(val type: StandardChunk)
