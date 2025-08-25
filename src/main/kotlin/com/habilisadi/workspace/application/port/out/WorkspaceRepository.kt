package com.habilisadi.workspace.application.port.out

import com.habilisadi.workspace.domain.model.WorkspaceEntity
import org.springframework.data.jpa.repository.JpaRepository

interface WorkspaceRepository : JpaRepository<WorkspaceEntity, String>