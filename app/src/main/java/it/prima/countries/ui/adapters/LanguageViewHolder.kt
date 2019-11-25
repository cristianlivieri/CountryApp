package it.prima.countries.ui.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import it.prima.countries.model.Language
import kotlinx.android.synthetic.main.item_currency_list.view.*

class LanguageViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    fun bind(language: Language) {
        itemView.apply {
            text_name.text = language.name
        }
    }
}