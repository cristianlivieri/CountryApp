package it.prima.countries.ui.adapters

import android.app.Activity
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ahmadrosid.svgloader.SvgLoader
import it.prima.countries.model.Country
import it.prima.countries.ui.interfaces.Listener
import kotlinx.android.synthetic.main.item_country_list.view.*

class CountryViewHolder(itemView: View, private val listener: Listener<Country>) :
    RecyclerView.ViewHolder(itemView) {

    fun bind(country: Country, activity: Activity) {
        itemView.apply {
            setOnClickListener { listener.onItemClick(country) }
            text_view_title_item.text = country.name

            SvgLoader.pluck()
                .with(activity)
                .load(country.flag, image_view_flag_country)
        }
    }
}