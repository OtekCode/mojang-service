package pl.otekplay.mojang.error

object Errors {
    val TIMEOUT_REQUEST_ERROR = Error("Request has been timeouted.")
    val HIT_MOJANG_LIMIT_ERROR = Error("Mojang request limit hit!")
}