package com.habilisadi.workspace.application.dto

import com.habilisadi.workspace.domain.model.Invitation
import com.habilisadi.workspace.domain.model.InvitationCode
import java.time.Instant
import java.time.temporal.ChronoUnit

class InvitationCommand {
    data class Sending(
        val email: String,
        val workspacePk: String
    ) {
        fun toEntity(workspaceName: String) = Invitation(
            email = email,
            workspacePk = workspacePk,
            workspaceName = workspaceName,
            expiredAt = Instant.now().plus(15, ChronoUnit.DAYS)
        )
    }

    data class Pending(
        val email: String,
        val workspacePk: String,
        val workspaceName: String,
        val invitationCode: InvitationCode,
        val expiredAt: Instant
    ) {
        companion object {
            fun fromInvitation(invitation: Invitation) = Pending(
                email = invitation.email,
                workspacePk = invitation.workspacePk,
                workspaceName = invitation.workspaceName,
                invitationCode = invitation.code,
                expiredAt = invitation.expiredAt
            )
        }

        fun toGrpcRequest() =
            com.habilisadi.workspace.InvitationRequest.newBuilder()
                .setEmail(email)
                .setWorkspacePk(workspacePk)
                .setWorkspaceName(workspaceName)
                .setInvitationCode(invitationCode.value)
                .setExpiredAt(expiredAt.toString())
                .build()
    }

    data class VerifyCode(
        val workspacePk: String,
        val email: String,
        val code: String
    )

    data class VerifyUser(
        val workspacePk: String,
        val email: String
    ) {
        fun toGrpcRequest() =
            com.habilisadi.workspace.VerifyInvitationRequest.newBuilder()
                .setWorkspacePk(workspacePk)
                .setEmail(email)
                .build()
    }
}