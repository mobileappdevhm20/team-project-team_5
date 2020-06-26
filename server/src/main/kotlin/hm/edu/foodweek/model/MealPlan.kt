package hm.edu.foodweek.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
@Table(name = "meal_plans")
data class MealPlan(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "plan_id")
        val planId: Long?,
        @Column(columnDefinition = "varchar(2000)")
        var title: String?,
        @Column(columnDefinition = "text")
        var description: String?,
        @Column(columnDefinition = "varchar(600)")
        var imageURL: String?,

        @ManyToOne(cascade = [CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH])
        @JoinColumn(name = "creator_id")
        var creator: User?,

        @JsonIgnore
        @ManyToMany(mappedBy = "subscribedPlans")
        val subscribers: List<User>?,

        var draft: Boolean = false,

        @OneToMany(mappedBy = "mealPlan", cascade = [CascadeType.ALL])
        var meals: MutableList<Meal>?
)