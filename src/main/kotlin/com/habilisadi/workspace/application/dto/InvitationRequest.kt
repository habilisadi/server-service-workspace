package com.habilisadi.workspace.application.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

class InvitationRequest {
    data class SendInvitation(
        @field:NotNull(message = "email must not be null")
        @field:NotBlank(message = "email must not be blank")
        @field:Email(message = "email must be valid")
        val email: List<String>,

        @field:NotNull(message = "email must not be null")
        @field:NotBlank(message = "email must not be blank")
        val workspacePk: String
    ) {
        fun toCommand(): InvitationCommand.Sending {
            // todo: 유효성 검사
            return InvitationCommand.Sending(email, workspacePk)
        }
    }

    data class VerifyInvitation(
        @field:NotNull(message = "email must not be null")
        @field:NotBlank(message = "email must not be blank")
        @field:Email(message = "email must be valid")
        val email: String,

        @field:NotNull(message = "email must not be null")
        @field:NotBlank(message = "email must not be blank")
        val workspacePk: String,

        @field:NotNull(message = "email must not be null")
        @field:NotBlank(message = "email must not be blank")
        val code: String,
    ) {
        fun toVerifyCodeCommand(): InvitationCommand.VerifyCode {
            // todo: 유효성 검사
            return InvitationCommand.VerifyCode(email, workspacePk, code)
        }

        fun toVerifyUserCommand(): InvitationCommand.VerifyUser {
            return InvitationCommand.VerifyUser(workspacePk, email)
        }

    }
}