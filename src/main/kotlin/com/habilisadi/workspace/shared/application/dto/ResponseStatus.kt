package com.habilisadi.auth.shared.application.dto

data class ResponseStatus<T>(
    val status: Int,
    val message: String,
    val data: T? = null
) {
    companion object {
        fun <T> success(data: T? = null): ResponseStatus<T> {
            return ResponseStatus(200, "Success", data)
        }

        fun <T> error(message: String): ResponseStatus<T> {
            return ResponseStatus(400, message)
        }

        fun <T> successData(data: T, message: String): ResponseStatus<T> {
            return ResponseStatus(200, message, data)
        }
    }

}
