package com.example.zipkr.global.config

import org.hibernate.boot.model.FunctionContributions
import org.hibernate.boot.model.FunctionContributor

class CustomMySQLFunctionContributor : FunctionContributor {

    override fun contributeFunctions(functionContributions: FunctionContributions) {
        val typeConfiguration = functionContributions.typeConfiguration
        val doubleType = typeConfiguration.basicTypeRegistry
            .resolve(org.hibernate.type.StandardBasicTypes.DOUBLE)

        functionContributions.functionRegistry
            .patternDescriptorBuilder(
                "match_against",
                "MATCH(?1) AGAINST (?2 IN BOOLEAN MODE)"
            )
            .setInvariantType(doubleType)
            .setExactArgumentCount(2)
            .register()
    }
}
