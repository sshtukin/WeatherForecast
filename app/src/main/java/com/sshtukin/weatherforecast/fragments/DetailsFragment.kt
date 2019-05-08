package com.sshtukin.weatherforecast.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.sshtukin.weatherforecast.R
import com.sshtukin.weatherforecast.models.Weather
import kotlinx.android.synthetic.main.fragment_details.*

/**
 * Fragment with [viewPager], which displays [CityInfoFragment] and [WeatherInfoFragment].
 *
 * @author Sergey Shtukin
 */

class DetailsFragment : Fragment() {

    lateinit var weather: Weather

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onStart() {
        super.onStart()
        viewPager.adapter = ViewPagerAdapter(childFragmentManager)
        viewPager.setPageTransformer(true, ZoomOutPageTransformer())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<Weather>(DETAILS_KEY)?.let {
            weather = it
        }
    }

    inner class ViewPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> CityInfoFragment.newInstance(weather)
                else -> WeatherInfoFragment.newInstance(weather)
            }
        }

        override fun getCount(): Int {
            return 2
        }
    }

    inner class ZoomOutPageTransformer : ViewPager.PageTransformer {

        private val MIN_SCALE = 0.85f
        private val MIN_ALPHA = 0.5f

        override fun transformPage(view: View, position: Float) {
            view.apply {
                val pageWidth = width
                val pageHeight = height
                when {
                    position < -1 -> {
                        alpha = 0f
                    }
                    position <= 1 -> {
                        val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
                        val vertMargin = pageHeight * (1 - scaleFactor) / 2
                        val horzMargin = pageWidth * (1 - scaleFactor) / 2
                        translationX = if (position < 0) {
                            horzMargin - vertMargin / 2
                        } else {
                            horzMargin + vertMargin / 2
                        }

                        scaleX = scaleFactor
                        scaleY = scaleFactor
                        alpha = (MIN_ALPHA +
                                (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))
                    }
                    else -> {
                        alpha = 0f
                    }
                }
            }
        }
    }

    companion object {
        private const val DETAILS_KEY = "1001"

        fun newInstance(weather: Weather?) = DetailsFragment().apply {
            arguments = Bundle().apply {
                putParcelable(DETAILS_KEY, weather)
            }
        }
    }
}