package it.prima.countries.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import it.prima.countries.R
import it.prima.countries.model.Currency

class CurrencyAdapter(private val list: MutableList<Currency>) :
    RecyclerView.Adapter<CurrencyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_currency_list,
            parent, false
        )
        return CurrencyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        holder.apply {
            bind(list[position])
        }
    }
}