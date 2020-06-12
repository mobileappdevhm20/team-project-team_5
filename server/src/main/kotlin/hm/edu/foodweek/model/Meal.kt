package hm.edu.foodweek.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
@Table(name = "meals")
data class Meal(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "meal_id")
        val mealId: Long,
        @Enumerated(EnumType.STRING)
        val day: WeekDay,
        @Enumerated(EnumType.STRING)
        val time: MealTime,
        @ManyToOne(cascade = [CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH])
        @JoinColumn(name = "recipe_id")
        var recipe: Recipe,

        @JsonIgnore
        @ManyToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "meal_plan_id")
        var mealPlan: MealPlan?
)