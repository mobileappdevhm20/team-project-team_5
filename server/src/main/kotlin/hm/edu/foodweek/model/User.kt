package hm.edu.foodweek.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity()
@Table(name = "users")
data class User(
        @Id
        @Column(name = "user_id")
        var userId: String,
        var username: String?,

        @JsonIgnore
        @OneToMany(mappedBy = "creator", cascade = [CascadeType.ALL])
        var ownMealPlans: List<MealPlan> = emptyList(),

        @JsonIgnore
        @ManyToMany(cascade = [CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH])
        @JoinTable(name = "meal_plan_subscription",
                joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "user_id")],
                inverseJoinColumns = [JoinColumn(name = "plan_id", referencedColumnName = "plan_id")])
        var subscribedPlans: MutableList<MealPlan> = mutableListOf()

)