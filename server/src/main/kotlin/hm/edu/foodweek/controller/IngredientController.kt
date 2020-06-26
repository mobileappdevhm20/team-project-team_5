package hm.edu.foodweek.controller

import hm.edu.foodweek.dto.IngredientDto
import hm.edu.foodweek.repository.IngredientRepository
import org.modelmapper.ModelMapper
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["ingredients"])
class IngredientController(
        val ingredientRepository: IngredientRepository,
        val mapper: ModelMapper
) {
    @GetMapping
    fun getAll(pageable: Pageable): Page<IngredientDto> {
        return ingredientRepository.findAll(pageable).map { mapper.map(it, IngredientDto::class.java) }
    }

}