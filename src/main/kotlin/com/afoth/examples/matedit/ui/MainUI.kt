package com.afoth.examples.matedit.ui

import com.afoth.examples.matedit.domain.*
import com.vaadin.data.provider.DataProvider
import com.vaadin.event.selection.SelectionEvent
import com.vaadin.server.VaadinRequest
import com.vaadin.spring.annotation.SpringUI
import com.vaadin.ui.*
import java.util.stream.Stream
import kotlin.streams.toList

@SpringUI(path = "")
class MainUI(final val materialService: MaterialService,
             final val propertyService: PropertyService,
             final val propertyValueService: PropertyValueService) : UI() {

    val materialProvider : DataProvider<Material, Void> =
            DataProvider.fromCallbacks(
                    { materialService.findAll().stream() },
                    { materialService.count().toInt() })

    val propertyValueProvider : DataProvider<PropertyValue, Void> =
            DataProvider.fromCallbacks(
                    // { currentMaterial?.let { propertyValueService.findAllByMaterial(it)?.stream() } },
                    // { currentMaterial?.let { propertyValueService.countByMaterial(it) }?.toInt() ?: 0 }
                    { currentMaterial?.propertyValues?.stream() ?: Stream.empty() },
                    { currentMaterial?.propertyValues?.size ?: 0 }
                    )


    var currentMaterial: Material? = null

    val newMaterialName = TextField().apply { placeholder = "Name" }
    val newProperty = ComboBox<Property>().apply {
        isEmptySelectionAllowed = false
        isTextInputAllowed = false
        itemCaptionGenerator = ItemCaptionGenerator { property -> property.name }
        val properties = propertyService.findAll()
        setItems(properties)
        setSelectedItem(properties.first())
    }
    val newPropertyValue = TextField().apply { placeholder = "Value" }

    override fun init(request: VaadinRequest?) {
        content = VerticalLayout().apply {
            addComponents(HorizontalLayout().apply {
                addComponents(VerticalLayout().apply {
                    addComponents(Grid<Material>().apply {
                        addColumn { it.id }.caption = "ID"
                        addColumn { it.name }.caption = "Name"
                        addColumn { it.propertyValues.size }.caption = "# Properties"
                        setDataProvider(materialProvider)
                        addSelectionListener { selectMaterial(it) }
                    }, HorizontalLayout().apply {
                        addComponents(
                                newMaterialName,
                                Button("Add material").apply { addClickListener{ addMaterial(it)}
                        })
                    })
                }, VerticalLayout().apply {
                    addComponents(Grid<PropertyValue>().apply {
                        addColumn { it.id }.caption = "ID"
                        addColumn { it.property?.name }.caption = "Property"
                        addColumn { it.value }.caption = "Value"
                        setDataProvider(propertyValueProvider)
                    }, HorizontalLayout().apply {
                        addComponents(
                                newProperty,
                                newPropertyValue,
                                Button("Add property").apply { addClickListener{ addProperty(it)}
                        })
                    })
                })
            })
        }
    }

    private fun addProperty(event: Button.ClickEvent?) {
        val property = newProperty.selectedItem.get()
        val value = newPropertyValue.value
        val propertyValue = PropertyValue(null, currentMaterial, property, value)
        propertyValueService.save(propertyValue)
        currentMaterial!!.propertyValues.add(propertyValue)
        currentMaterial = materialService.save(currentMaterial!!)
        materialProvider.refreshAll()
        propertyValueProvider.refreshAll()
    }

    private fun selectMaterial(event: SelectionEvent<Material>?) {
        currentMaterial = event?.firstSelectedItem?.orElse(Material()) as Material
        propertyValueProvider.refreshAll()
    }

    private fun addMaterial(event: Button.ClickEvent?) {
        val name = newMaterialName.value
        val material = Material(null, name, mutableListOf())
        currentMaterial = materialService.save(material)
        materialProvider.refreshAll()
        propertyValueProvider.refreshAll()
    }

}