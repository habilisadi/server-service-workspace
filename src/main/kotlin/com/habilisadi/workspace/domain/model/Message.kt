package com.habilisadi.workspace.domain.model

//@Embeddable
data class Message(
//    @Convert
    var value: Content, // todo: 객체로 변경할 예정 object ot jsonb
) {

    data class Content(
        val type: String,
        val value: String,
        val files: MutableList<File> = mutableListOf(),
        val thumbnail: String,
        val links: MutableList<Link>,
    )

    data class File(
        val name: String,
        val url: String,
        val size: Long,
        val type: String,
        val thumbnail: String,
    )

    data class Link(
        val title: String,
        val description: String,
        val url: String,
        val image: String,
    )


}
