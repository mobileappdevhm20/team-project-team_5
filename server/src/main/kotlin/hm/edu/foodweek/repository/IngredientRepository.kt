package hm.edu.foodweek.repository

import hm.edu.foodweek.model.Ingredient
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface IngredientRepository : PagingAndSortingRepository<Ingredient, Long> {
    fun findByName(name: String): Optional<Ingredient>
}