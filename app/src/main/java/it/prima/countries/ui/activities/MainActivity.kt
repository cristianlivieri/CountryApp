package it.prima.countries.ui.activities

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Explode
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import it.prima.countries.R
import it.prima.countries.ui.adapters.CountryAdapter
import it.prima.countries.model.Country
import it.prima.countries.ui.interfaces.Listener
import it.prima.countries.utils.Constants
import it.prima.countries.view.MainActivityView
import it.prima.countries.viewmodel.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

class MainActivity : AppCompatActivity(), MainActivityView, Listener<Country> {

    private var adapter: CountryAdapter? = null
    private var list: MutableList<Country>? = null
    private var viewModel: MainActivityViewModel? = null
    private var region = "Africa"

    //TODO tab layout reset filter
    //TODO use view pager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = MainActivityViewModel()
        viewModel!!.attach(this)

        list = ArrayList()
        viewModel!!.loadCountries("Africa", this@MainActivity)

        setSearchView()
        setClickTab()

        with(window) {
            exitTransition = Explode()
        }

        pull_to_refresh.setOnRefreshListener {
            viewModel!!.loadCountries(region, this@MainActivity)
            pull_to_refresh.isRefreshing = false
        }
    }

    private fun setClickTab() {
        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {}

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab_layout.selectedTabPosition) {
                    0 -> {
                        region = "Africa"
                        viewModel!!.loadCountries(region, this@MainActivity)
                    }
                    1 -> {
                        region = "Americas"
                        viewModel!!.loadCountries(region, this@MainActivity)
                    }
                    2 -> {
                        region = "Asia"
                        viewModel!!.loadCountries(region, this@MainActivity)
                    }
                    3 -> {
                        region = "Europe"
                        viewModel!!.loadCountries(region, this@MainActivity)
                    }
                    4 -> {
                        region = "Oceania"
                        viewModel!!.loadCountries(region, this@MainActivity)
                    }
                }
            }
        })
    }

    private fun setSearchView() {
        card_view.setOnClickListener {
            search_view.isIconified = false
        }
        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                adapter!!.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter!!.filter.filter(newText)
                return false
            }
        })
    }

    private fun initView(list: MutableList<Country>) {
        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager = LinearLayoutManager(this)
        adapter = CountryAdapter(list, this)
        recycler_view.adapter = adapter
    }

    override fun onStop() {
        super.onStop()
        viewModel!!.clearSubscriptions()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel!!.detach()
    }

    override fun onBackPressed() {
        if (!search_view!!.isIconified) {
            search_view!!.isIconified = true
            return
        }
        super.onBackPressed()
    }

    override fun load(countriesList: MutableList<Country>) {
        initView(countriesList)
    }

    override fun error(e: Throwable) {
        Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
    }

    override fun error() {
        Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
    }

    override fun onItemClick(item: Country) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(Constants.ARG_COUNTRIES, item)
        val options = ActivityOptions.makeSceneTransitionAnimation(this)
        startActivity(intent, options.toBundle())
    }
}
