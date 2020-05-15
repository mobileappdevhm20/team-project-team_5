package edu.hm.foodweek.recipes

enum class UnitScale {
    Millilitre,Litre,Gram,KiloGram,Pieces
}

data class Unit(
    val value:Int,
    val scale:UnitScale
)