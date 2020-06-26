package hm.edu.foodweek.model

import javax.persistence.*

@Entity
@Table(name = "ingredients")
data class Ingredient(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val ingredientId: Long,
        @Column(columnDefinition = "varchar(600)")
        val name: String
)