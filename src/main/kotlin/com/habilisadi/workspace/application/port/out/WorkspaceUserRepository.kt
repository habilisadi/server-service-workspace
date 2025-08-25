package com.habilisadi.workspace.application.port.out

import com.habilisadi.workspace.domain.model.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface WorkspaceUserRepository : JpaRepository<UserEntity, String>