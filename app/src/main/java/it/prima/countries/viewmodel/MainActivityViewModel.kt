package it.prima.countries.viewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import it.prima.countries.api.ApiService
import it.prima.countries.utils.Constants
import it.prima.countries.utils.NetworkUtils
import it.prima.countries.view.MainActivityView

class MainActivityViewModel : ViewModel() {
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var view: MainActivityView? = null
    var tabSelected = 0

    private fun createRetrofit(): ApiService {
        val retrofit = NetworkUtils.createRetrofit(Constants.BASE_URL)
        return retrofit.create(ApiService::class.java)
    }

    fun loadCountries(region: String) {
        view!!.showProgress(true)
        compositeDisposable.add(createRetrofit().getAll()
            .subscribeOn(Schedulers.io())
            .flatMapIterable { v -> v }
            .filter { countries -> countries.region == region }
            .toList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                view!!.load(response)
                view!!.showProgress(false)
            }, { t ->
                view!!.error(t)
                view!!.showProgress(false)
            })
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