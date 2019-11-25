package it.prima.countries.ui.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import it.prima.countries.model.Currency
import kotlinx.android.synthetic.main.item_currency_list.view.*

class CurrencyViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    fun bind(currency: Currency) {
        itemView.apply {
            text_name.text = currency.name
            text_symbol.text = currency.symbol
        }
    }
}