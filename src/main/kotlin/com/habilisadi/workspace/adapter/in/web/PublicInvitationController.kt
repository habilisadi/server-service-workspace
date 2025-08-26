package com.habilisadi.workspace.adapter.`in`.web

import com.habilisadi.workspace.application.dto.InvitationRequest
import com.habilisadi.workspace.application.port.`in`.PendingInvitationUseCase
import com.habilisadi.workspace.application.port.`in`.SendEmailInvitationUseCase
import jakarta.validation.Valid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/public/invitations")
class PublicInvitationController(
    private val pendingInvitationUseCase: PendingInvitationUseCase,
    private val sendEmailInvitationUseCase: SendEmailInvitationUseCase,

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


}