package com.habilisadi.workspace.adapter.`in`.web

import com.habilisadi.workspace.application.dto.InvitationRequest
import com.habilisadi.workspace.application.dto.WorkspaceUserCommand
import com.habilisadi.workspace.application.port.`in`.PendingInvitationUseCase
import com.habilisadi.workspace.application.port.`in`.SaveWorkspaceUserUseCase
import com.habilisadi.workspace.application.port.`in`.SendEmailInvitationUseCase
import com.habilisadi.workspace.application.port.`in`.VerifyInvitationUseCase
import jakarta.validation.Valid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/v1/public/invitations")
class PublicInvitationController(
    private val pendingInvitationUseCase: PendingInvitationUseCase,
    private val sendEmailInvitationUseCase: SendEmailInvitationUseCase,
    private val verifyInvitationUseCase: VerifyInvitationUseCase,
    private val saveWorkspaceUserUseCase: SaveWorkspaceUserUseCase,
) {
    @PostMapping("")
    suspend fun sendInvitation(
        @Valid @RequestBody req: InvitationRequest.SendInvitation,
    ) = withContext(Dispatchers.IO) {
        val command = req.toCommand()

        val pendingCommand = pendingInvitationUseCase.pendingInvitation(command)

        val pendingResponse = sendEmailInvitationUseCase
            .pendingSend(pendingCommand)

        pendingResponse
    }

    @GetMapping("verify")
    suspend fun verifyCode(
        req: InvitationRequest.VerifyInvitation
    ) = supervisorScope {

        val isSameCode = async {
            val verifyCodeCommand = req.toVerifyCodeCommand()

            verifyInvitationUseCase.verifyCode(verifyCodeCommand)
        }

        val pairIsSameUser = async {
            val verifyUserCommand = req.toVerifyUserCommand()

            verifyInvitationUseCase.verifyUser(verifyUserCommand)
        }

        val (isSameUser, verifyInvitationRes) = pairIsSameUser.await()


        if (!isSameCode.await() || !isSameUser) {
            throw IllegalArgumentException("Invalid invitation code or user")
        }

        val workspaceUserCommand =
            WorkspaceUserCommand
                .SaveWorkspaceUser
                .from(verifyInvitationRes)

        val res = saveWorkspaceUserUseCase.save(workspaceUserCommand)

        res
    }
}