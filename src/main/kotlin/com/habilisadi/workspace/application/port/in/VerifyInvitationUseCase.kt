package com.habilisadi.workspace.application.port.`in`

import com.habilisadi.workspace.VerifyInvitationResponse
import com.habilisadi.workspace.application.dto.InvitationCommand

interface VerifyInvitationUseCase {
    fun verifyCode(command: InvitationCommand.VerifyCode): Boolean
    fun verifyUser(command: InvitationCommand.VerifyUser): Pair<Boolean, VerifyInvitationResponse>
}