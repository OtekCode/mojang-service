package pl.otekplay.mojang.fetcher

import kotlinx.coroutines.*
import pl.otekplay.mojang.checker.Checker
import pl.otekplay.mojang.repository.Profile
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.LinkedBlockingDeque
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

object Fetcher {
    private val REQUESTS = ConcurrentHashMap<String, CompletableDeferred<Profile>>()
    private val NAMES = LinkedBlockingDeque<String>()

    private val MAIN_COROUNTINE = newSingleThreadContext("FetcherMain")

    init {
        println("Starting fetcher thread...")
        thread { start() }
    }

    fun start() = runBlocking {
        withContext(MAIN_COROUNTINE) {
            while (isActive) {
                if (NAMES.isEmpty()) {
                    delay(1000)
                    continue
                }
                println("Splitting ${NAMES.size} names for requests...")
                val requests = collectRequests()
                println("Collected ${requests.size} requests:")
                requests.forEachIndexed { i, array ->
                    println("${i + 1}.Request[Names: ${array.size}]")
                }
                println("Sending ${requests.size} requests...")
                requests.map { launch(context = Dispatchers.IO, start = CoroutineStart.LAZY) { Checker.check(it) } }
                    .forEach { it.start() }
                delay(250)
                println("Waiting for requests...")
            }
        }
    }


    private fun collectRequests(): ArrayList<Array<String>> {
        val requests = arrayListOf<Array<String>>()
        while (NAMES.isNotEmpty()) requests.add(collectNames())
        return requests
    }

    private fun collectNames(): Array<String> {
        val requestNames = hashSetOf<String>()
        while (requestNames.size < 100) {
            val first = NAMES.pollFirst() ?: break
            requestNames.add(first)
        }
        return requestNames.toTypedArray()
    }


    fun request(nameRequest: String): Deferred<Profile> {
        val name = nameRequest.toLowerCase()
        val def = REQUESTS.getOrPut(name) { CompletableDeferred() }
        if (!NAMES.contains(name)) NAMES.add(name)
        return def
    }

    fun complete(profile: Profile) {
        val nameRequest = profile.name.toLowerCase()
        val def = REQUESTS.remove(nameRequest) ?: return
        def.complete(profile)
    }

    fun error(nameRequest: String, exception: FetchedException) {
        val name = nameRequest.toLowerCase()
        val def = REQUESTS.remove(name) ?: return
        def.completeExceptionally(exception)
    }

}