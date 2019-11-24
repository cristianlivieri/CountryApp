package it.prima.countries.viewmodel

import android.content.Context
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import it.prima.countries.api.ApiService
import it.prima.countries.utils.Constants
import it.prima.countries.utils.NetworkUtils
import it.prima.countries.view.MainActivityView

class MainActivityViewModel {
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var view: MainActivityView? = null

    private fun createRetrofit(context: Context): ApiService {
        val retrofit = NetworkUtils.createRetrofit(Constants.BASE_URL, context)
        return retrofit.create(ApiService::class.java)
    }

    fun loadCountries(region: String, context: Context) {
        compositeDisposable.add(createRetrofit(context).getAll()
            .subscribeOn(Schedulers.io())
            .flatMapIterable { v -> v }
            .filter { countries -> countries.region == region }
            .toList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                view!!.load(response)
            }, { t -> view!!.error(t) })
        )
    }

    fun attach(view: MainActivityView) {
        this.view = view
    }

    fun detach() {
        view = null
    }

    fun clearSubscriptions() {
        compositeDisposable.clear()
    }
}