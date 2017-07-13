package com.afoth.examples.matedit.domain

import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class Initializer(private val propertyService: PropertyService) {

    @PostConstruct
    fun init() {
        listOf("Density", "Hardness", "Melting point", "Thermal capacity").forEach {
            val property = Property(null, it)
            propertyService.save(property);
        }
    }
}