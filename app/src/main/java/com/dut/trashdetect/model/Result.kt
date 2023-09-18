package com.dut.trashdetect.model

data class Result(
    val name: String,
    val percentage: Float
) {
    constructor() : this (
        "", 0f
    )
}
