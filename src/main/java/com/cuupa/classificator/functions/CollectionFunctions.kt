package com.cuupa.classificator.functions

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

suspend fun <A> Iterable<A>.forEachParallel(function: suspend (A) -> Unit) = coroutineScope {
    forEach { async { function(it) } }
}

suspend fun <A, B> Iterable<A>.mapParallel(function: suspend (A) -> B): List<B> = coroutineScope {
    map { async { function(it) } }.awaitAll()
}