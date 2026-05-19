package com.example.zipkr.domain.roadname.repository

import com.example.zipkr.domain.roadname.dto.response.SearchPostalCodeResponse

interface RoadNameRepository {

    fun searchByText(keyword: String, page: Int): List<SearchPostalCodeResponse>

    fun searchByInitialConsonant(keyword: String, page: Int): List<SearchPostalCodeResponse>
}