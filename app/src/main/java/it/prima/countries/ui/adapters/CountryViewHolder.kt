package it.prima.countries.ui.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import it.prima.countries.model.Country
import it.prima.countries.ui.interfaces.Listener
import kotlinx.android.synthetic.main.item_country_list.view.*

class CountryViewHolder(itemView: View, private val listener: Listener<Country>) :
    RecyclerView.ViewHolder(itemView) {

    fun bind(country: Country) {
        itemView.apply {
            setOnClickListener { listener.onItemClick(country) }
            text_view_title_item.text = country.name

            val path = "https://www.countryflags.io/${country.alpha2Code}/flat/64.png"
            Picasso.get()
                .load(path)
                .fit()
                .centerCrop()
                .into(image_view_flag_country)
        }
    }
}