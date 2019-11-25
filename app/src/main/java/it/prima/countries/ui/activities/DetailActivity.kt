package it.prima.countries.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmadrosid.svgloader.SvgLoader
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import it.prima.countries.R
import it.prima.countries.model.Country
import it.prima.countries.model.Currency
import it.prima.countries.model.Language
import it.prima.countries.ui.adapters.CurrencyAdapter
import it.prima.countries.ui.adapters.LanguageAdapter
import it.prima.countries.utils.Constants
import it.prima.countries.utils.NetworkUtils
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var map: GoogleMap
    private var country: Country? = null
    private var parentLayout: View? = null
    private var currencyAdapter: CurrencyAdapter? = null
    private var languageAdapter: LanguageAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val intent = this.intent
        val bundle = intent.extras
        country = bundle!!.getSerializable(Constants.ARG_COUNTRIES) as Country
        parentLayout = findViewById(R.id.content)
        setActionBar()
        initView()
    }

    private fun setActionBar() {
        setSupportActionBar(toolbar)
        collapsing_layout.title = country!!.name
        collapsing_layout.setCollapsedTitleTextColor(resources.getColor(R.color.black, null))

        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        text_capital.text = getString(R.string.capital) + " " + country!!.capital
        text_region.text = getString(R.string.region) + " " + country!!.region
        text_demonym.text = getString(R.string.demonym) + " " + country!!.demonym
        text_area.text = getString(R.string.area) + " " + country!!.area
        text_population.text = getString(R.string.population) + " " + country!!.population
        text_native_name.text = country!!.nativeName

        // This library allows uploading svg files
        SvgLoader.pluck()
            .with(this)
            .load(country!!.flag, image_view_flag_country)

        if (NetworkUtils.hasNetwork()!!) {
            loadMap()
            app_bar.setExpanded(true)
            map_container.visibility = View.VISIBLE
        } else {
            app_bar.setExpanded(false)
            map_container.visibility = View.GONE
        }
        initList()
    }

    private fun initList() {
        recycler_view_currency.setHasFixedSize(true)
        recycler_view_currency.layoutManager = LinearLayoutManager(this)
        currencyAdapter = CurrencyAdapter(generateCurrencyList()!!)
        recycler_view_currency.adapter = currencyAdapter

        recycler_view_languages.setHasFixedSize(true)
        recycler_view_languages.layoutManager = LinearLayoutManager(this)
        languageAdapter = LanguageAdapter(generateLanguageList()!!)
        recycler_view_languages.adapter = languageAdapter
    }

    private fun generateCurrencyList(): MutableList<Currency>? {
        val currencies = country!!.currencies
        val list: ArrayList<Currency> = ArrayList()
        for (currency in currencies) {
            list.add(Currency(currency.name, currency.symbol))
        }
        return list
    }

    private fun generateLanguageList(): MutableList<Language>? {
        val languages = country!!.languages
        val list: ArrayList<Language> = ArrayList()
        for (language in languages) {
            list.add(Language(language.name))
        }
        return list
    }

    private fun loadMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync {
            map = it
            val latLng = LatLng(country!!.latlng[0], country!!.latlng[1])
            val markerOptions = MarkerOptions()
            markerOptions.position(latLng)
            map.addMarker(markerOptions)
            map.moveCamera(CameraUpdateFactory.newLatLng(latLng))
            map.animateCamera(CameraUpdateFactory.zoomTo(3f))
            map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.theme))
            map.uiSettings.isScrollGesturesEnabled = false

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        SvgLoader.pluck().close()
    }
}
