package com.afoth.examples.matedit.domain

import org.springframework.data.annotation.PersistenceConstructor
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import javax.persistence.*

@Entity
class PropertyValue @PersistenceConstructor constructor(
        @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
        val id: Long? = null,
        @ManyToOne val material: Material? = null,
        @ManyToOne val property: Property? = null,
        val value: String = ""
)


@Service
class PropertyValueService(private val repo: PropertyValueRepository) {

    fun findAllByMaterial(material: Material?) = material?.let { repo.findAllByMaterial(it) }
    fun countByMaterial(material: Material?) = material?.let { repo.findAllByMaterial(it).count() }
    fun save(propertyValue: PropertyValue?) = repo.save(propertyValue)
}

@Repository
interface PropertyValueRepository: JpaRepository<PropertyValue, Long> {
    fun findAllByMaterial(material: Material): List<PropertyValue>
}

