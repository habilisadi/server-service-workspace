package com.habilisadi.workspace.domain.model

import jakarta.persistence.Embeddable

@Embeddable
data class Domain(
    var value: String,
) {
    init {
        val regex = Regex("^(?!http://|https://)[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\$")
        require(value.isNotBlank()) { "Domain cannot be blank" }
        require(regex.matches(value)) { "Domain must be a valid domain name" }
    }


}
