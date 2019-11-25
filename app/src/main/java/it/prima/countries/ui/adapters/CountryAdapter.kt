package it.prima.countries.ui.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import it.prima.countries.R
import it.prima.countries.model.Country
import it.prima.countries.ui.interfaces.Listener
import java.util.*
import kotlin.collections.ArrayList

class CountryAdapter(
    private val list: MutableList<Country>,
    private val listener: Listener<Country>,
    private val activity: Activity
) :
    RecyclerView.Adapter<CountryViewHolder>(), Filterable {

    private val listFull: List<Country>

    init {
        listFull = ArrayList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.item_country_list,
            parent, false
        )
        return CountryViewHolder(v, listener)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.apply {
            bind(list[position], activity)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun clear() {
        list.clear()
        list.addAll(listFull)
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = ArrayList<Country>()

                if (constraint.isNullOrEmpty()) {
                    filteredList.addAll(listFull)
                } else {
                    val filterPattern =
                        constraint.toString().toLowerCase(Locale.getDefault()).trim { it <= ' ' }

                    for (item in listFull) {
                        for (language in item.languages) {
                            if (language.name!!.toLowerCase(Locale.getDefault()).contains(
                                    filterPattern
                                )
                            ) {
                                filteredList.add(item)
                            }
                        }
                    }
                }

                val results = FilterResults()
                results.values = filteredList

                return results
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                list.clear()
                list.addAll(results.values as List<Country>)
                notifyDataSetChanged()
            }
        }
    }
}
