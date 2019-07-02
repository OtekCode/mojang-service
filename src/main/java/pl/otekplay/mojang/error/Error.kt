package pl.otekplay.mojang.error

data class Error(
    val message:String
){
    val status = "error"
}