package com.habilisadi.workspace.shared.utils

import org.springframework.stereotype.Component

@Component
class CodeUtil {
    companion object {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"

        fun generateRandomCode(length: Int): String {
            return (1..length)
                .map { chars.random() }
                .toString()
        }
    }
}