package it.prima.countries.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import it.prima.countries.CountriesApplication
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object NetworkUtils {

    private val context = CountriesApplication.appContext!!

    fun hasNetwork(): Boolean? {
        var isConnected: Boolean? = false // Initial Value
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnected)
            isConnected = true
        return isConnected
    }

    private fun setClient(): OkHttpClient {
        // Specifies a cache of 5MB.
        val size = (5 * 1024 * 1024).toLong()

        val cache = Cache(context.cacheDir, size)

        return OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor { chain ->
                var request = chain.request()
                request = if (hasNetwork()!!) {
                    // If there is Internet, get the cache that was stored 5 seconds ago.
                    request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
                } else {
                    // If there is no Internet, get the cache that was stored 7 days ago.
                    request.newBuilder().header(
                        "Cache-Control",
                        "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
                    ).build()
                }
                chain.proceed(request)
            }
            .build()
    }

    fun createRetrofit(url: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(setClient())
            .build()
    }
}