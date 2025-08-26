package com.habilisadi.workspace.application.dto

import com.habilisadi.workspace.domain.model.Invitation
import com.habilisadi.workspace.domain.model.InvitationCode
import java.time.Instant
import java.time.temporal.ChronoUnit

class InvitationCommand {
    data class Sending(
        val emails: List<String>,
        val workspacePk: String
    ) {
        fun toEntities(workspaceName: String) =
            emails.map { email ->
                Invitation(
                    email = email,
                    workspacePk = workspacePk,
                    workspaceName = workspaceName,
                    expiredAt = Instant.now().plus(15, ChronoUnit.DAYS)
                )
            }
    }

    data class Pending(
        val invitationUsers: List<InvitationUser>,
        val workspacePk: String,
        val workspaceName: String,
        val expiredAt: Instant
    ) {

        data class InvitationUser(
            val email: String,
            val invitationCode: InvitationCode
        )

        companion object {
            fun fromInvitation(invitations: List<Invitation>): Pending {
                val invitationUsers = invitations.map { invitation ->
                    InvitationUser(invitation.email, invitation.code)
                }

                return Pending(
                    invitationUsers = invitationUsers,
                    workspacePk = invitations[0].workspacePk,
                    workspaceName = invitations[0].workspaceName,
                    expiredAt = invitations[0].expiredAt
                )
            }

        }

        fun toGrpcRequest(): com.habilisadi.workspace.InvitationRequest {
            val invitationUsers = invitationUsers.map { invitationUser ->
                com.habilisadi.workspace.InvitationUser.newBuilder()
                    .setEmail(invitationUser.email)
                    .setInvitationCode(invitationUser.invitationCode.value)
                    .build()
            }

            val invitationRequestBuilder = com.habilisadi.workspace.InvitationRequest.newBuilder()

            invitationRequestBuilder.invitationUserList.addAll(invitationUsers)

            return invitationRequestBuilder
                .setWorkspacePk(workspacePk)
                .setWorkspaceName(workspaceName)
                .setExpiredAt(expiredAt.toString())
                .build()
        }


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