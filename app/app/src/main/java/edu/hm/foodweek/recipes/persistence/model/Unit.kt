package edu.hm.foodweek.recipes.persistence.model

import java.lang.StringBuilder

enum class UnitScale {
    Millilitre, Litre, Gram, KiloGram, Pieces
}

data class Unit(
    val value: Int,
    val scale: UnitScale
) {
    override fun toString(): String {
        val stringBuilder = StringBuilder(value.toString())
        stringBuilder.append(
            when (scale) {
                UnitScale.Millilitre -> "ml"
                UnitScale.Litre -> "l"
                UnitScale.Gram -> "g"
                UnitScale.KiloGram -> " kg"
                UnitScale.Pieces -> " x"
            }
        )
        return stringBuilder.toString()
    }

}