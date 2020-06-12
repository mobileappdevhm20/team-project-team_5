package hm.edu.foodweek.repository

import hm.edu.foodweek.model.MealPlan
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface MealPlanRepository : PagingAndSortingRepository<MealPlan, Long> {

    @Query("""SELECT m FROM MealPlan m WHERE
        m.title LIKE '%' || :search || '%'
        OR m.description LIKE '%' || :search || '%' """)
    fun findAllWithSearch(@Param("search") search: String, pageable: Pageable): Page<MealPlan>

}