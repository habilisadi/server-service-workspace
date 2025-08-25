package com.habilisadi.workspace.domain.converter

import com.habilisadi.workspace.domain.model.UserRoleType
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class WorkspaceUserRoleTypesConverter : AttributeConverter<MutableSet<UserRoleType>, String> {
    override fun convertToDatabaseColumn(attribute: MutableSet<UserRoleType>?): String {
        return attribute?.joinToString(",") { it.name } ?: ""
    }

    override fun convertToEntityAttribute(dbData: String?): MutableSet<UserRoleType> {
        return dbData?.split(",")?.map { UserRoleType.valueOf(it) }?.toMutableSet() ?: mutableSetOf()
    }
}