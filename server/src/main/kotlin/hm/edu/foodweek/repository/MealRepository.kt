package hm.edu.foodweek.repository

import hm.edu.foodweek.model.Meal
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface MealRepository : PagingAndSortingRepository<Meal, Long> {}