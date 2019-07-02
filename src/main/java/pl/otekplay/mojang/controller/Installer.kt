package pl.otekplay.mojang.controller

import io.ktor.application.Application
import io.ktor.locations.get
import io.ktor.routing.routing

fun Application.installMojangController() = routing {
    get<HasPaidRequest> {
        Controller.hasPaid(this, it)
    }
}
