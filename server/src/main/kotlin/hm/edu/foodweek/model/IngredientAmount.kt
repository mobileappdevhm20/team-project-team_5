package hm.edu.foodweek.model

import com.fasterxml.jackson.annotation.JsonIgnore
import java.io.Serializable
import javax.persistence.*

@Entity
data class IngredientAmount(
        @JsonIgnore
        @EmbeddedId
        var id: IngredientAmountKey?,

        @JsonIgnore
        @ManyToOne
        @MapsId("recipe_id")
        @JoinColumn(name = "recipe_id")
        val recipe: Recipe?,

        @ManyToOne(cascade = [CascadeType.MERGE])
        @MapsId("ingredient_id")
        @JoinColumn(name = "ingredient_id")
        var ingredient: Ingredient?,
        val measure: String?
)

@Embeddable
data class IngredientAmountKey(
        @Column(name = "recipe_id")
        val recipeId: Long,
        @Column(name = "ingredient_id")
        val ingredientId: Long
) : Serializable