package com.example.zipkr.domain.roadname.service

import com.example.zipkr.domain.roadname.dto.response.SearchPostalCodeResponse
import com.example.zipkr.domain.roadname.repository.RoadNameRepository
import org.springframework.transaction.annotation.Transactional
import org.springframework.stereotype.Service

@Service
class RoadNameServiceImpl(
    private val roadNameRepositoryCustom: RoadNameRepository
) : RoadNameService {

    @Transactional(readOnly = true)
    override fun searchPostalCode(keyword: String): List<SearchPostalCodeResponse> {
        return roadNameRepositoryCustom.searchPostalCode(keyword)
    }
}
