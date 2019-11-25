package it.prima.countries.model

import java.io.Serializable

class Country(
    val name: String?,
    val region: String,
    val capital: String,
    val population: Long,
    val flag: String?,
    val demonym: String?,
    val currencies: ArrayList<Currency>,
    val area: Double?,
    var latlng: Array<Double>,
    val nativeName: String?,
    val languages: ArrayList<Language>
) : Serializable