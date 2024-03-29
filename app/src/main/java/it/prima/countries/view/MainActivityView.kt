package it.prima.countries.view

import it.prima.countries.model.Country

interface MainActivityView {

    fun load(countriesList: MutableList<Country>)

    fun showProgress(show: Boolean)

    fun error(e: Throwable)

    fun error()

}