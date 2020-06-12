package hm.edu.foodweek.controller

import hm.edu.foodweek.controller.exceptions.BadRequestException
import hm.edu.foodweek.controller.exceptions.NotFoundException
import hm.edu.foodweek.controller.exceptions.UnauthorizedException
import hm.edu.foodweek.dto.RecipeBriefDto
import hm.edu.foodweek.dto.RecipeDetailedDto
import hm.edu.foodweek.model.IngredientAmountKey
import hm.edu.foodweek.model.Recipe
import hm.edu.foodweek.repository.IngredientRepository
import hm.edu.foodweek.repository.RecipeRepository
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping(value = ["recipes"])
class RecipeController(
        val recipeRepository: RecipeRepository,
        val ingredientRepository: IngredientRepository,
        val mapper: ModelMapper
) {

    @Value("\${admin.id}")
    var adminId: String = ""

    @GetMapping
    fun getAll(pageable: Pageable, @RequestParam search: Optional<String>): Page<RecipeBriefDto> {
        if (search.isPresent) {
            return recipeRepository.findAllWithSearch(search.get(), pageable).map { mapper.map(it, RecipeBriefDto::class.java) }
        }
        return recipeRepository.findAll(pageable).map { mapper.map(it, RecipeBriefDto::class.java) }
    }

    @GetMapping("/{recipeId}")
    fun getOne(@PathVariable recipeId: Long): RecipeDetailedDto {
        val recipe = recipeRepository
                .findById(recipeId)
                .orElseThrow { NotFoundException("MealPlan with id $recipeId does not exist!") }

        return mapper.map(recipe, RecipeDetailedDto::class.java)
    }

    @PostMapping
    fun create(@RequestBody input: Recipe, @RequestHeader user: Optional<String>): ResponseEntity<RecipeDetailedDto> {
        verifyUserIsAdmin(user)
        return ResponseEntity(mapper.map(recipeRepository.save(updateIngredients(input)), RecipeDetailedDto::class.java), HttpStatus.CREATED)
    }

    @PutMapping("/{recipeId}")
    fun update(@PathVariable recipeId: Long, @RequestHeader user: Optional<String>, @RequestBody input: Recipe): ResponseEntity<RecipeDetailedDto> {
        verifyUserIsAdmin(user)

        val recipe = recipeRepository.findById(recipeId)
        if (recipe.isEmpty) {
            throw NotFoundException("Recipe with id $recipeId does not exist!")
        }
        input.recipeId = recipeId
        return ResponseEntity(mapper.map(recipeRepository.save(updateIngredients(input)), RecipeDetailedDto::class.java), HttpStatus.OK)
    }

    @DeleteMapping("/{recipeId}")
    fun delete(@PathVariable recipeId: Long, @RequestHeader user: Optional<String>): ResponseEntity<Any> {
        verifyUserIsAdmin(user)

        val recipe = recipeRepository.findById(recipeId)
        if (recipe.isEmpty) {
            return ResponseEntity("Recipe with id $recipeId does not exist!", HttpStatus.NOT_FOUND)
        }

        recipeRepository.delete(recipe.get())
        return ResponseEntity(HttpStatus.OK)
    }

    fun verifyUserIsAdmin(user: Optional<String>) {
        if (user.isEmpty || user.isPresent && user.get() != adminId) {
            throw UnauthorizedException()
        }
    }

    private fun updateIngredients(input: Recipe): Recipe {
        // Create ingredients
        val ingredients = input.ingredients?.map {
            if (it.ingredient == null) {
                throw BadRequestException("It is not allowed to specify an IngredientAmount with 'ingredient' set to null!")
            }
            var ingredient = ingredientRepository.findByName(it.ingredient!!.name)
            if (ingredient.isEmpty) {
                ingredient = Optional.of(ingredientRepository.save(it.ingredient!!))
            }

            it.ingredient = ingredient.get()
            it
        }

        // Save recipe without ingredients
        input.ingredients = emptyList()
        val createdRecipe = recipeRepository.save(input)

        // Link ingredients to recipe and update recipe
        createdRecipe.ingredients = ingredients?.map {
            it.id = IngredientAmountKey(createdRecipe.recipeId!!, it.ingredient!!.ingredientId)
            it
        }
        return createdRecipe
    }
}