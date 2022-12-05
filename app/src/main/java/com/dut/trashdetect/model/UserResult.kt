package com.dut.trashdetect.model

data class UserResult(
    val userId: String,
    val createdAt: Long,
    val result: Result,
    val imageUrl: String
) {
    constructor() : this(
        "",
        0L,
        Result(),
        ""
    )
}
