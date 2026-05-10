package com.example.zipkr.domain.roadname.controller

import com.example.zipkr.domain.roadname.dto.response.SearchPostalCodeResponse
import com.example.zipkr.domain.roadname.service.RoadNameService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/road-name")
class RoadNameController (
    private val roadNameService: RoadNameService
) {

    @GetMapping("/postal-code")
    fun searchPostalCode(@RequestParam(name = "keyword") keyword:String):List<SearchPostalCodeResponse> {
        return roadNameService.searchPostalCode(keyword)
    }
}