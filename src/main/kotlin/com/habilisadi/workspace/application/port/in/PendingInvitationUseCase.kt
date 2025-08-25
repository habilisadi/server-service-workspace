package com.habilisadi.workspace.application.port.`in`

import com.habilisadi.workspace.application.dto.InvitationCommand

interface PendingInvitationUseCase {
    fun pendingInvitation(command: InvitationCommand.Sending): InvitationCommand.Pending
}