package hm.edu.foodweek.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
@Table(name = "recipes")
data class Recipe(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "recipe_id")
        var recipeId: Long?,
        @Column(columnDefinition = "varchar(2000)")
        var title: String?,
        @Column(columnDefinition = "varchar(600)")
        var image: String?,
        @Column(columnDefinition = "text")
        var description: String?,
        @OneToMany(mappedBy = "recipe", cascade = [CascadeType.ALL])
        var ingredients: List<IngredientAmount>?,

        @ElementCollection
        @Column(columnDefinition = "text")
        var steps: List<String>?,
        @ElementCollection
        var labels: Set<String>?,

        @JsonIgnore
        @OneToMany(mappedBy = "recipe", cascade = [CascadeType.ALL])
        var meals: List<Meal>?
)