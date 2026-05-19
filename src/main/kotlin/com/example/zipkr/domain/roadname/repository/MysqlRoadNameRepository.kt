package com.example.zipkr.domain.roadname.repository

import com.example.zipkr.domain.roadname.dto.response.SearchPostalCodeResponse
import com.example.zipkr.domain.roadname.entity.QRoadNameEntity.roadNameEntity
import com.querydsl.core.types.dsl.BooleanTemplate
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.core.types.dsl.StringPath
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class MysqlRoadNameRepository(
    private val queryFactory: JPAQueryFactory
) : RoadNameRepository {

    companion object {
        private const val PAGE_SIZE = 10
    }

    override fun searchByText(keyword: String, page: Int): List<SearchPostalCodeResponse> {
        return queryFactory
            .selectFrom(roadNameEntity)
            .where(matchAgainst(roadNameEntity.korFullText, toFullTextKeyword(keyword)))
            .offset((page * PAGE_SIZE).toLong())
            .limit(PAGE_SIZE.toLong())
            .fetch()
            .map { SearchPostalCodeResponse.from(it) }
    }

    override fun searchByInitialConsonant(keyword: String, page: Int): List<SearchPostalCodeResponse> {
        return queryFactory
            .selectFrom(roadNameEntity)
            .where(matchAgainst(roadNameEntity.korInitialFullText, toInitialKeyword(keyword)))
            .offset((page * PAGE_SIZE).toLong())
            .limit(PAGE_SIZE.toLong())
            .fetch()
            .map { SearchPostalCodeResponse.from(it) }
    }

    // 공백이 있으면 구문 검색("따박골로 11"), 없으면 전방 검색(따박골로*)
    private fun toFullTextKeyword(keyword: String): String =
        if (keyword.contains(" ")) "\"$keyword\"" else "$keyword*"

    // 초성은 와일드카드 없이 그대로
    private fun toInitialKeyword(keyword: String): String = keyword

    private fun matchAgainst(field: StringPath, keyword: String): BooleanTemplate =
        Expressions.booleanTemplate(
            "function('match_against', {0}, {1}) > 0",
            field,
            keyword
        )
}