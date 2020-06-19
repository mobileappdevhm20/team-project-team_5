package edu.hm.foodweek.recipes.persistence.model

import java.lang.StringBuilder

data class Unit(
    val value: Int,
    val scale: String
) {
    override fun toString(): String {
        return StringBuilder().append(value).append(" ").append(scale.trim()).toString()
    }
}