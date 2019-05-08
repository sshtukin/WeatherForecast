package com.sshtukin.weatherforecast

import com.sshtukin.weatherforecast.models.Weather

interface RecyclerViewClickListener {
    fun onItemClicked(weather: Weather?)
}
