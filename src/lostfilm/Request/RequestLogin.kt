package lostfilm.Request

data class RequestLogin(
        val act: String,
        val type: String,
        val mail: String,
        val pass: String
)
