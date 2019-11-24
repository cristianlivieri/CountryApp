package it.prima.countries.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import it.prima.countries.R
import it.prima.countries.model.Country
import it.prima.countries.utils.Constants
import kotlinx.android.synthetic.main.activity_detail.*
import com.squareup.picasso.Picasso
import java.util.*

class DetailActivity : AppCompatActivity() {

    //TODO load map
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val country = intent.getParcelableExtra<Country>(Constants.ARG_COUNTRIES)

        text_title.text = country!!.name

        val pathHd = "https://cdn.countryflags.com/thumbs/${country.name.toLowerCase(Locale.getDefault())}/flag-800.png"

        Picasso.get()
            .load(pathHd)
            .fit()
            .centerCrop()
            .into(image_background)
    }
}
