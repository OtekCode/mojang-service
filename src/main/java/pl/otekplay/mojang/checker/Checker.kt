package pl.otekplay.mojang.checker

import com.google.gson.GsonBuilder
import io.ktor.client.request.post
import io.ktor.content.TextContent
import io.ktor.http.ContentType
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import pl.otekplay.mojang.client.client
import pl.otekplay.mojang.fetcher.FetchedException
import pl.otekplay.mojang.fetcher.Fetcher
import pl.otekplay.mojang.repository.Profile
import java.util.*
import kotlin.system.measureTimeMillis

object Checker {
    private const val MOJANG_URL = "https://api.mojang.com/profiles/minecraft"
    private val GSON = GsonBuilder().create()

    fun check(names: Array<String>) {
        println("Sending request with ${names.size}")
        val time = System.currentTimeMillis()
        try {
            val json = GSON.toJson(names)
            val ids = runBlocking {
                client.post<Array<Id>>(MOJANG_URL) {
                    body = TextContent(json, ContentType.Application.Json)
                }
            }
            val premiumNames = ids.map { it.name }
            val premiums = ids.map { Profile(getUniqueIdFromId(it.id), it.name, true, time) }.toTypedArray()
            val crackedNames = names.filter { name -> !premiumNames.any { it.equals(name, true) } }
            val cracked = crackedNames.map { Profile(getUniqueIdFromName(it), it, false, time) }.toTypedArray()
            val all = arrayOf(*cracked, *premiums)
            success(all)
        } catch (e: Exception) {
            val error = FetchedException("Failed to fetch: ${e.message}")
            failed(names, error)
            e.printStackTrace()
        }

    }


    private fun success(profiles: Array<Profile>) {
        println("Fetched ${profiles.size} names.")
        profiles.forEach { Fetcher.complete(it) }
    }

    private fun failed(names: Array<String>, exception: FetchedException) {
        println("Failed to fetch ${names.size} names.")
        names.forEach { Fetcher.error(it, exception) }
    }

    private fun getUniqueIdFromName(name: String) = UUID.nameUUIDFromBytes(("OfflinePlayer:$name").toByteArray())

    private fun getUniqueIdFromId(id: String) = UUID.fromString(
        id.substring(0, 8) + "-" + id.substring(8, 12) + "-" + id.substring(
            12,
            16
        ) + "-" + id.substring(16, 20) + "-" + id.substring(20, 32)
    )


}