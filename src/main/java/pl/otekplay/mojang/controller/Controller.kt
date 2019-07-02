package pl.otekplay.mojang.controller

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.util.pipeline.PipelineContext
import kotlinx.coroutines.*
import pl.otekplay.mojang.error.Errors
import pl.otekplay.mojang.fetcher.FetchedException
import pl.otekplay.mojang.fetcher.Fetcher
import pl.otekplay.mojang.repository.Profile

object Controller {

    suspend fun hasPaid(
        request: PipelineContext<Unit, ApplicationCall>,
        it: HasPaidRequest
    ) {
        val name = it.name
        val deferred = Fetcher.request(name)
        val call = request.call
        try {
            withTimeout(2500) {
                val profile: Profile
                try {
                    profile = deferred.await()
                    call.respond(profile.premium)
                } catch (e: FetchedException) {
                    call.respond(Errors.HIT_MOJANG_LIMIT_ERROR)
                    e.printStackTrace()
                }
            }
        } catch (e: TimeoutCancellationException) {
            call.respond(Errors.TIMEOUT_REQUEST_ERROR)
            e.printStackTrace()
        }


    }

}

