package com.example.zipkr.domain.roadname.dto.response

data class SearchPostalCodeResponse(
    val postalCode: Int,
    val roadNameAddress: String,
    val jibunAddress: String
)