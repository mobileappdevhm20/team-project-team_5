package hm.edu.foodweek.controller

import hm.edu.foodweek.controller.exceptions.BadRequestException
import hm.edu.foodweek.controller.exceptions.NotFoundException
import hm.edu.foodweek.controller.exceptions.UnauthorizedException
import hm.edu.foodweek.dto.MealPlanBriefDto
import hm.edu.foodweek.dto.MealPlanDetailedDto
import hm.edu.foodweek.model.Meal
import hm.edu.foodweek.model.MealPlan
import hm.edu.foodweek.model.User
import hm.edu.foodweek.repository.MealPlanRepository
import hm.edu.foodweek.repository.MealRepository
import hm.edu.foodweek.repository.RecipeRepository
import hm.edu.foodweek.repository.UserRepository
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping(value = ["mealplans"])
class MealPlanController(
        val mealPlanRepository: MealPlanRepository,
        val recipeRepository: RecipeRepository,
        val userRepository: UserRepository,
        val mealRepository: MealRepository,
        val mapper: ModelMapper
) {
    @Value("\${admin.id}")
    var adminId: String = ""

    @GetMapping
    fun getAll(pageable: Pageable, @RequestParam search: Optional<String>): Page<MealPlanBriefDto> {
        if (search.isPresent) {
            return mealPlanRepository.findAllWithSearch(search.get(), pageable).map { mapper.map(it, MealPlanBriefDto::class.java) }
        }
        return mealPlanRepository.findAll(pageable).map { mapper.map(it, MealPlanBriefDto::class.java) }
    }

    @GetMapping("/{mealPlanId}")
    fun getOne(@PathVariable mealPlanId: Long): MealPlanDetailedDto {
        val plan = mealPlanRepository
                .findById(mealPlanId)
                .orElseThrow { NotFoundException("MealPlan with id $mealPlanId does not exist!") }

        return mapper.map(plan, MealPlanDetailedDto::class.java)
    }

    @PostMapping
    fun create(@RequestBody input: MealPlan, @RequestHeader user: Optional<String>): ResponseEntity<Any> {
        // Attach or create creator
        val creator = verifyUserIsAuthorized(user)

        // Intermediate clearance of fields
        val inMeals = input.meals
        input.meals = emptyList()
        input.creator = null

        // Create meal plan and attach meals
        var saved = mealPlanRepository.save(input)
        if (inMeals != null) {
            saved = attachMealsToMealPlan(saved, inMeals)
        }

        // Add meal plan to users own meal plans
        creator.ownMealPlans?.add(saved)
        userRepository.save(creator)
        saved.creator = creator

        return ResponseEntity(mapper.map(mealPlanRepository.save(saved), MealPlanDetailedDto::class.java), HttpStatus.CREATED)
    }


    @PutMapping("/{mealPlanId}")
    fun update(@PathVariable mealPlanId: Long, @RequestBody input: MealPlan, @RequestHeader user: Optional<String>): ResponseEntity<Any> {
        val mealPlan = verifyUserIsAuthorizedAndMealPlanExists(mealPlanId, user)

        // Update attributes
        mealPlan.title = input.title
        mealPlan.description = input.description
        mealPlan.imageURL = input.imageURL
        mealPlan.draft = input.draft

        // Update meals
        mealPlan.meals = input.meals?.map {
            val recipeID = it.recipe.recipeId
                    ?: return ResponseEntity.badRequest().body("Each recipe has to have its 'recipeID' specified!")
            val matchingRecipe = recipeRepository.findById(recipeID)
            if (matchingRecipe.isEmpty) {
                return ResponseEntity.badRequest().body("Recipe with id $recipeID does not exist")
            }
            it.recipe = matchingRecipe.get()

            it
        }

        val saved = mealPlanRepository.save(mealPlan)
        return ResponseEntity(mapper.map(saved, MealPlanDetailedDto::class.java), HttpStatus.CREATED)
    }

    @DeleteMapping("/{mealPlanId}")
    fun delete(@PathVariable mealPlanId: Long, @RequestHeader user: Optional<String>): ResponseEntity<Any> {
        val mealPlan = verifyUserIsAuthorizedAndMealPlanExists(mealPlanId, user)

        // Remove from subscriptions
        mealPlan.subscribers?.forEach {
            it.subscribedPlans?.remove(mealPlan)
        }

        mealPlanRepository.delete(mealPlan)
        return ResponseEntity(HttpStatus.OK)
    }

    fun attachMealsToMealPlan(created: MealPlan, meals: List<Meal>): MealPlan {
        created.meals = meals
                // Attach mealplan to meals
                .onEach { it.mealPlan = created }
                // Attach recipes to meals
                .onEach {
                    val recipeID = it.recipe.recipeId
                            ?: throw BadRequestException("Each recipe has to have its 'recipeID' specified!")
                    val matchingRecipe = recipeRepository.findById(recipeID)
                    if (matchingRecipe.isEmpty) {
                        throw BadRequestException("Recipe with id $recipeID does not exist")
                    }
                    it.recipe = matchingRecipe.get()
                }
                .onEach { mealRepository.save(it) }

        return created
    }

    fun verifyUserIsAuthorized(user: Optional<String>): User {
        if (user.isEmpty) {
            throw UnauthorizedException()
        }

        var existingUser = userRepository.findById(user.get())
        if (existingUser.isEmpty) {
            existingUser = Optional.of(userRepository.save(User(user.get(), null, mutableListOf(), mutableListOf())))
        }

        if (user.isEmpty || user.isPresent && user.get() != adminId && user.get() != existingUser.get().userId) {
            throw UnauthorizedException()
        }

        return existingUser.get()
    }

    fun verifyUserIsAuthorizedAndMealPlanExists(mealPlanId: Long, user: Optional<String>): MealPlan {
        val mealPlan = mealPlanRepository.findById(mealPlanId)
        if (mealPlan.isEmpty) {
            throw NotFoundException("Mealplan with id ${mealPlanId} does not exist!")
        }

        if (user.isEmpty || user.isPresent && user.get() != adminId && user.get() != mealPlan.get().creator!!.userId) {
            throw UnauthorizedException()
        }

        return mealPlan.get()
    }

}