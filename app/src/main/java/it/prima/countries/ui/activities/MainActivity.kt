package it.prima.countries.ui.activities

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.transition.Explode
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import it.prima.countries.R
import it.prima.countries.model.Country
import it.prima.countries.ui.adapters.CountryAdapter
import it.prima.countries.ui.interfaces.Listener
import it.prima.countries.utils.Constants
import it.prima.countries.view.MainActivityView
import it.prima.countries.viewmodel.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.progress_bar

class MainActivity : AppCompatActivity(), MainActivityView, Listener<Country> {

    private var adapter: CountryAdapter? = null
    private var list: MutableList<Country>? = null
    private lateinit var viewModel: MainActivityViewModel
    private var region = Constants.AFRICA
    private var parentLayout: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        parentLayout = findViewById(R.id.content)
        list = ArrayList()

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        viewModel.attach(this)
        viewModel.loadCountries(region)

        setSearchView()
        setClickTab()

        with(window) {
            exitTransition = Explode()
        }

    }

    private fun setClickTab() {
        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {}

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab_layout.selectedTabPosition) {
                    0 -> setTabSelected(Constants.AFRICA, 0)
                    1 -> setTabSelected(Constants.AMERICAS, 1)
                    2 -> setTabSelected(Constants.ASIA, 2)
                    3 -> setTabSelected(Constants.EUROPE, 3)
                    4 -> setTabSelected(Constants.OCEANIA, 4)
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        tab_layout.getTabAt(viewModel.tabSelected)!!.select()
    }

    private fun setTabSelected(region: String, tabSelected: Int) {
        viewModel.tabSelected = tabSelected
        viewModel.loadCountries(region)
        search_view.setQuery("", false)
        search_view.clearFocus()
        search_view!!.isIconified = true
    }

    private fun setSearchView() {
        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isNotEmpty()) {
                    adapter!!.filter.filter(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (search_view.query.isNullOrEmpty()) {
                    adapter!!.clear()
                } else if (newText.isNotEmpty()) {
                    adapter!!.filter.filter(newText)
                }
                return false
            }
        })
    }

    private fun initView(list: MutableList<Country>) {
        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager = LinearLayoutManager(this)
        adapter = CountryAdapter(list, this, this)
        recycler_view.adapter = adapter
    }

    override fun onStop() {
        super.onStop()
        viewModel.clearSubscriptions()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.detach()
    }

    override fun onBackPressed() {
        if (!search_view.isIconified) {
            search_view.isIconified = true
            return
        }
        super.onBackPressed()
    }

    override fun load(countriesList: MutableList<Country>) {
        initView(countriesList)
        text_error.visibility = View.GONE
        recycler_view.visibility = View.VISIBLE
    }

    override fun error(e: Throwable) {
        text_error.visibility = View.VISIBLE
        recycler_view.visibility = View.GONE
        Log.d(TAG, "Error: " + e.message)
    }

    override fun error() {
        text_error.visibility = View.VISIBLE
        recycler_view.visibility = View.GONE
        Log.d(TAG, "Error")
    }

    override fun onItemClick(item: Country) {
        val bundle = Bundle()
        bundle.putSerializable(Constants.ARG_COUNTRIES, item)
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtras(bundle)
        val options = ActivityOptions.makeSceneTransitionAnimation(this)
        startActivity(intent, options.toBundle())
    }

    override fun showProgress(show: Boolean) {
        if (show) {
            progress_bar.visibility = View.VISIBLE
            recycler_view.visibility = View.GONE
        } else {
            progress_bar.visibility = View.GONE
            recycler_view.visibility = View.VISIBLE
        }
    }

    companion object {
        val TAG = MainActivity::class.java.name
    }
}
