package hm.edu.foodweek.controller

import hm.edu.foodweek.controller.exceptions.NotFoundException
import hm.edu.foodweek.controller.exceptions.UnauthorizedException
import hm.edu.foodweek.dto.MealPlanBriefDto
import hm.edu.foodweek.dto.UserBriefDto
import hm.edu.foodweek.dto.UserDetailDto
import hm.edu.foodweek.model.User
import hm.edu.foodweek.repository.MealPlanRepository
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
@RequestMapping(value = ["users"])
class UserController(
        val userRepository: UserRepository,
        val mealPlanRepository: MealPlanRepository,
        val mapper: ModelMapper
) {

    @Value("\${admin.id}")
    var adminId: String = ""

    @GetMapping
    fun getAll(pageable: Pageable, @RequestHeader user: Optional<String>): Page<UserBriefDto> {
        verifyUserIsAdmin(user)
        return userRepository.findAll(pageable).map { mapper.map(it, UserBriefDto::class.java) }
    }

    @GetMapping("/{userId}")
    fun getOne(@PathVariable userId: String, @RequestHeader user: Optional<String>): UserDetailDto {
        return mapper.map(verifyUserIsAuthorized(userId, user), UserDetailDto::class.java)
    }

    @PutMapping("/{userId}")
    fun updateUser(@PathVariable userId: String, @RequestBody update: User, @RequestHeader user: Optional<String>): UserBriefDto {
        val existingUser = verifyUserIsAuthorized(userId, user)
        existingUser.username = update.username
        return mapper.map(userRepository.save(existingUser), UserBriefDto::class.java)
    }

    @DeleteMapping("/{userId}")
    fun deleteUser(@PathVariable userId: String, @RequestHeader user: Optional<String>): ResponseEntity<Any> {
        verifyUserIsAdmin(user)
        val existingUser = userRepository.findById(userId).orElseThrow { throw NotFoundException("User with id $userId does not exist!") }

        // Remove all subscriptions of user
        existingUser.subscribedPlans?.clear()
        userRepository.save(existingUser)

        // Remove all owned mealplans
        existingUser.ownMealPlans?.forEach {
            it.subscribers?.forEach { sub ->
                sub.subscribedPlans?.remove(it)
                userRepository.save(sub)
            }
        }

        // Delete user
        userRepository.delete(existingUser)
        return ResponseEntity(HttpStatus.OK)
    }

    @GetMapping("/{userId}/subscribed")
    fun getSubscribedPlans(@PathVariable userId: String, @RequestHeader user: Optional<String>): List<MealPlanBriefDto> {
        return mapper.map(verifyUserIsAuthorized(userId, user), UserDetailDto::class.java).subscribedPlans
                ?: emptyList()
    }

    @PutMapping("/{userId}/subscribed/{mealPlanId}")
    fun subscribeMealPlan(@PathVariable userId: String, @PathVariable mealPlanId: Long, @RequestHeader user: Optional<String>): MealPlanBriefDto {
        val existingUser = verifyUserIsAuthorized(userId, user, true)
        val mealPlan = mealPlanRepository.findById(mealPlanId).orElseThrow { throw NotFoundException("MealPlan with id $mealPlanId does not exist!") }

        if (existingUser.subscribedPlans != null && !existingUser.subscribedPlans!!.contains(mealPlan)) {
            existingUser.subscribedPlans!!.add(mealPlan)
            userRepository.save(existingUser)
        }

        return mapper.map(mealPlan, MealPlanBriefDto::class.java)
    }

    @DeleteMapping("/{userId}/subscribed/{mealPlanId}")
    fun unsubscribeMealPlan(@PathVariable userId: String, @PathVariable mealPlanId: Long, @RequestHeader user: Optional<String>): MealPlanBriefDto {
        val existingUser = verifyUserIsAuthorized(userId, user, true)
        val mealPlan = mealPlanRepository.findById(mealPlanId).orElseThrow { throw NotFoundException("MealPlan with id $mealPlanId does not exist!") }

        if (existingUser.subscribedPlans != null && existingUser.subscribedPlans!!.contains(mealPlan)) {
            existingUser.subscribedPlans!!.remove(mealPlan)
            userRepository.save(existingUser)
        }

        return mapper.map(mealPlan, MealPlanBriefDto::class.java)
    }

    @GetMapping("/{userId}/owned")
    fun getOwnPlans(@PathVariable userId: String, @RequestHeader user: Optional<String>): List<MealPlanBriefDto> {
        return mapper.map(verifyUserIsAuthorized(userId, user), UserDetailDto::class.java).ownMealPlans ?: emptyList()
    }

    fun verifyUserIsAuthorized(userId: String, user: Optional<String>, createUser: Boolean = false) : User {
        var existingUser = userRepository.findById(userId)
        if (existingUser.isEmpty) {
            if (createUser && userId == user.get()) {
                existingUser = Optional.of(userRepository.save(User(userId, null, mutableListOf(), mutableListOf())))
            } else {
                throw NotFoundException("User with id $userId does not exist!")
            }
        }

        if (user.isEmpty || user.isPresent && user.get() != adminId && user.get() != existingUser.get().userId) {
            throw UnauthorizedException()
        }

        return existingUser.get()
    }

    fun verifyUserIsAdmin(user: Optional<String>) {
        if (user.isEmpty || user.isPresent && user.get() != adminId) {
            throw UnauthorizedException()
        }
    }

}