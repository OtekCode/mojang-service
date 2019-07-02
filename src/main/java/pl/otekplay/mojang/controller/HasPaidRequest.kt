package pl.otekplay.mojang.controller

import io.ktor.locations.Location


@Location("/api/mojang/haspaid/{name}")
data class HasPaidRequest(val name:String)