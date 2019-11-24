package it.prima.countries.api

import io.reactivex.Observable
import it.prima.countries.model.Country
import retrofit2.http.GET

interface ApiService {

    @GET("all")
    fun getAll(): Observable<MutableList<Country>>
}