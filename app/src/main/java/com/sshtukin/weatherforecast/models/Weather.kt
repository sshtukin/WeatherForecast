package com.sshtukin.weatherforecast.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Weather(
    var cityName: String,
    var cityInfo: String,
    var cityPhoto: String,
    var weatherTemp: Int,
    var weatherDescription: String,
    var weatherPicture: String,
    var isFavourite: Boolean
) : Parcelable