package it.prima.countries.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import it.prima.countries.R
import it.prima.countries.model.Language

class LanguageAdapter(private val list: MutableList<Language>) :
    RecyclerView.Adapter<LanguageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_currency_list,
            parent, false
        )
        return LanguageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        holder.apply {
            bind(list[position])
        }
    }
}