package com.example.zipkr.domain.roadname.repository

import com.example.zipkr.domain.roadname.dto.response.SearchPostalCodeResponse

interface RoadNameRepository {

    fun searchPostalCode(keyword: String): List<SearchPostalCodeResponse>
}