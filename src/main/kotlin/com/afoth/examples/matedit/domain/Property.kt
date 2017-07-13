package com.afoth.examples.matedit.domain

import org.springframework.data.annotation.PersistenceConstructor
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Property @PersistenceConstructor constructor(
        @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
        val id: Long? = null,
        val name: String = "")


@Service
class PropertyService(private val repo: PropertyRepository) {

    fun findAll() = repo.findAll()
    fun save(property: Property) = repo.save(property)
}

@Repository
interface PropertyRepository: JpaRepository<Property, Long>

