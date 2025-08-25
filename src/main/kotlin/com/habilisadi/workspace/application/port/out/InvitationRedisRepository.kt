package com.habilisadi.workspace.application.port.out

import com.habilisadi.workspace.domain.model.Invitation
import com.habilisadi.workspace.shared.application.port.out.BaseRedisRepository

interface InvitationRedisRepository : BaseRedisRepository<Invitation>