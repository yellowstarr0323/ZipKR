package com.example.zipkr.domain.roadname.service

import com.example.zipkr.domain.roadname.dto.response.SearchPostalCodeResponse
import org.springframework.stereotype.Service

interface RoadNameService {

    fun searchPostalCode(keyword:String): List<SearchPostalCodeResponse>
}