package com.habilisadi.workspace.adapter.`in`.web

import com.habilisadi.workspace.application.dto.InvitationRequest
import com.habilisadi.workspace.application.dto.WorkspaceUserCommand
import com.habilisadi.workspace.application.port.`in`.SaveWorkspaceUserUseCase
import com.habilisadi.workspace.application.port.`in`.VerifyInvitationUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/private/workspaces")
class PrivateWorkspaceController(
    private val verifyInvitationUseCase: VerifyInvitationUseCase,
    private val saveWorkspaceUserUseCase: SaveWorkspaceUserUseCase,
) {
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