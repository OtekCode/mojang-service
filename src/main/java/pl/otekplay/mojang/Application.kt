package pl.otekplay.mojang

import com.codahale.metrics.JmxReporter
import com.codahale.metrics.Slf4jReporter
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.application.log
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.gson.GsonConverter
import io.ktor.http.ContentType
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Locations
import io.ktor.metrics.dropwizard.DropwizardMetrics
import pl.otekplay.mojang.controller.installMojangController
import java.util.concurrent.TimeUnit


@UseExperimental(KtorExperimentalLocationsAPI::class)
fun Application.main() {
    install(CallLogging)
    install(Locations)
    install(ContentNegotiation) {
        register(ContentType.Application.Json, GsonConverter())
    }
//    install(DropwizardMetrics) {
//        Slf4jReporter.forRegistry(registry)
//            .outputTo(log)
//            .convertRatesTo(TimeUnit.SECONDS)
//            .convertDurationsTo(TimeUnit.MILLISECONDS)
//            .build()
//            .start(10, TimeUnit.SECONDS)
//    }
    installMojangController()
}


