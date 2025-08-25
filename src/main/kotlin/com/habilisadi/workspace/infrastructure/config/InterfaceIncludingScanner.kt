package com.habilisadi.workspace.infrastructure.config

import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.core.type.classreading.MetadataReader

class InterfaceIncludingScanner : ClassPathScanningCandidateComponentProvider(false) {

    override fun isCandidateComponent(metadataReader: MetadataReader): Boolean {
        val classMetadata = metadataReader.classMetadata

        // 인터페이스와 구체 클래스 모두 포함
        val isCandidate = classMetadata.isInterface || classMetadata.isConcrete

        return isCandidate
    }
}
