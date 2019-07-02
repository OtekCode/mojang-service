package pl.otekplay.mojang.controller

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.util.pipeline.PipelineContext
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withContext
import pl.otekplay.mojang.fetcher.Fetcher

object Controller {

    suspend fun hasPaid(
        request: PipelineContext<Unit, ApplicationCall>,
        it: HasPaidRequest
    ) {
        val name = it.name
        val deferred = Fetcher.request(name)
        val profile = deferred.await()
        request.call.respond(profile.premium)


    }

}

