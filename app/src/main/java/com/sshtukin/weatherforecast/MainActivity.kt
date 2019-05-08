package com.sshtukin.weatherforecast

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.sshtukin.weatherforecast.fragments.DetailsFragment
import com.sshtukin.weatherforecast.fragments.WeatherListFragment
import com.sshtukin.weatherforecast.models.Weather

/**
 * Activity which contains fragment holder
 *
 * @author Sergey Shtukin
 */

class MainActivity : AppCompatActivity(), RecyclerViewClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setFragment(R.id.fragment_holder, WeatherListFragment())
    }

    override fun onItemClicked(weather: Weather?) {
        setFragment(R.id.fragment_holder, DetailsFragment.newInstance(weather), true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    private fun setFragment(
        holderId: Int,
        fragment: Fragment,
        backStack: Boolean = false
    ) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(
            R.anim.enter_from_right,
            R.anim.exit_to_left,
            R.anim.enter_from_left,
            R.anim.exit_to_right
        )
        transaction.replace(holderId, fragment)
        if (backStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }
}
