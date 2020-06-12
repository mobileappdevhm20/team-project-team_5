package hm.edu.foodweek.repository

import hm.edu.foodweek.model.Recipe
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface RecipeRepository : PagingAndSortingRepository<Recipe, Long> {

    @Query("""SELECT r FROM Recipe r WHERE
        r.title LIKE '%' || :search || '%'
        OR r.description LIKE '%' || :search || '%' """)
    fun findAllWithSearch(@Param("search") search: String, pageable: Pageable): Page<Recipe>

}