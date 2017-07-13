package com.afoth.examples.matedit.domain

import org.springframework.data.annotation.PersistenceConstructor
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import javax.persistence.*

@Entity
class Material @PersistenceConstructor constructor(
        @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
        val id: Long? = null,
        val name: String = "",
        @OneToMany(cascade = arrayOf(CascadeType.ALL), fetch = FetchType.EAGER)
        val propertyValues: MutableList<PropertyValue> = mutableListOf())

@Service
class MaterialService(private val repo: MaterialRepository) {

    fun findAll() = repo.findAll()
    fun save(material: Material) = repo.save(material)
    fun count() = repo.count()
}

@Repository
interface MaterialRepository: JpaRepository<Material, Long>

