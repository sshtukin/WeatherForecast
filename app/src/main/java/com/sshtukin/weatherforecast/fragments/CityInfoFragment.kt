package com.sshtukin.weatherforecast.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.sshtukin.weatherforecast.R
import com.sshtukin.weatherforecast.models.Weather
import kotlinx.android.synthetic.main.fragment_city_info.*

/**
 * Fragment shows some data about chosen city.
 *
 * @author Sergey Shtukin
 */

class CityInfoFragment : Fragment() {

    lateinit var weather: Weather

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_city_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<Weather>(WEATHER_KEY)?.let {
            weather = it
        }
        tvCityName.text = weather.cityName
        tvCityInfo.text = weather.cityInfo
        Glide
            .with(view.context)
            .load(weather.cityPhoto)
            .into(ivCityPhoto)
    }

    companion object {
        private const val WEATHER_KEY = "1001"

        fun newInstance(weather: Weather) = CityInfoFragment().apply {
            arguments = Bundle().apply {
                putParcelable(WEATHER_KEY, weather)
            }
        }
    }
}
