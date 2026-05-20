package com.example.zipkr.domain.roadname.service

import com.example.zipkr.domain.roadname.dto.response.SearchPostalCodeResponse
import com.example.zipkr.domain.roadname.repository.RoadNameJpaRepository
import org.springframework.transaction.annotation.Transactional
import org.springframework.stereotype.Service

@Service
class RoadNameServiceImpl(
    private val jpaRepository: RoadNameJpaRepository
) : RoadNameService {

    companion object {
        private const val PAGE_SIZE = 10
    }

    @Transactional(readOnly = true)
    override fun searchPostalCode(keyword: String, page: Int): List<SearchPostalCodeResponse> {
        val offset = page * PAGE_SIZE
        return if (containsInitialConsonant(keyword)) {
            jpaRepository.searchByInitialConsonant(keyword, PAGE_SIZE, offset)
        } else {
            jpaRepository.searchByText(toFullTextKeyword(keyword), PAGE_SIZE, offset)
        }.map { SearchPostalCodeResponse.from(it) }
    }

    private fun toFullTextKeyword(keyword: String): String =
        if (keyword.contains(" ")) "\"$keyword\"" else "$keyword*"

    private fun containsInitialConsonant(keyword: String): Boolean =
        keyword.any { it in 'ㄱ'..'ㅎ' }
}
