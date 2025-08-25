package com.habilisadi.workspace.application.port.`in`

import com.habilisadi.auth.shared.application.dto.ResponseStatus
import com.habilisadi.workspace.application.dto.InvitationCommand

interface SendEmailInvitationUseCase {
    fun pendingSend(command: InvitationCommand.Pending): ResponseStatus<Boolean>
}