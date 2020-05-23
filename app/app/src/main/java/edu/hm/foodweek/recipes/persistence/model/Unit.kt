package edu.hm.foodweek.recipes.persistence.model

enum class UnitScale {
    Millilitre,Litre,Gram,KiloGram,Pieces
}

data class Unit(
    val value:Int,
    val scale: UnitScale
)