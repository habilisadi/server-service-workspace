package com.habilisadi.workspace.domain.model

import com.habilisadi.workspace.shared.utils.CodeUtil
import jakarta.persistence.Embeddable

@Embeddable
data class InvitationCode(
    var value: String
) {
    init {
        require(value.length == 30) { "Invitation code must be 30 characters long" }
    }

    companion object {
        fun generateCode(): InvitationCode {
            return InvitationCode(CodeUtil.generateRandomCode(30))
        }
    }

    fun regenerateCode() {
        this.value = CodeUtil.generateRandomCode(30)
    }


}
