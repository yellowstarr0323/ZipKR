package com.example.zipkr.domain.roadname.service

import com.example.zipkr.domain.roadname.dto.response.SearchPostalCodeResponse
import com.example.zipkr.domain.roadname.repository.RoadNameRepository
import org.springframework.transaction.annotation.Transactional
import org.springframework.stereotype.Service

@Service
class RoadNameServiceImpl(
    private val roadNameRepository: RoadNameRepository
) : RoadNameService {

    @Transactional(readOnly = true)
    override fun searchPostalCode(keyword: String, page: Int): List<SearchPostalCodeResponse> {
        return if (containsInitialConsonant(keyword)) {
            roadNameRepository.searchByInitialConsonant(keyword, page)
        } else {
            roadNameRepository.searchByText(keyword, page)
        }
    }

    private fun containsInitialConsonant(keyword: String): Boolean =
        keyword.any { it in 'ㄱ'..'ㅎ' }
}
