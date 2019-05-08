package com.sshtukin.weatherforecast.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.sshtukin.weatherforecast.R
import com.sshtukin.weatherforecast.models.Weather
import kotlinx.android.synthetic.main.fragment_weather_info.*

/**
 * Fragment shows some data about chosen city's weather.
 *
 * @author Sergey Shtukin
 */

class WeatherInfoFragment : Fragment() {

    lateinit var weather: Weather

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather_info, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<Weather>(WEATHER_KEY)?.let {
            weather = it
        }
        tvWeatherInfo.text = weather.weatherDescription
        Glide
            .with(view.context)
            .load(weather.weatherPicture)
            .into(ivWeatherPicture)
        tvTemp.text = weather.weatherTemp.toString()
    }

    companion object {
        private const val WEATHER_KEY = "1001"

        fun newInstance(weather: Weather) = WeatherInfoFragment().apply {
            arguments = Bundle().apply {
                putParcelable(WEATHER_KEY, weather)
            }
        }
    }
}
