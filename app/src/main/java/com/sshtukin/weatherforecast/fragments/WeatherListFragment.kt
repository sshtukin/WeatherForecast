package com.sshtukin.weatherforecast.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sshtukin.weatherforecast.R
import com.sshtukin.weatherforecast.RecyclerViewClickListener
import com.sshtukin.weatherforecast.models.Weather
import com.sshtukin.weatherforecast.models.WeatherModel
import kotlinx.android.synthetic.main.city_item.view.*
import kotlinx.android.synthetic.main.fragment_weatherlist.*
import kotlinx.android.synthetic.main.title_item.view.*

/**
 * Fragment with [recyclerView], which displays list of cities and theirs temps.
 * On item click fragment opens [DetailsFragment].
 *
 * @author Sergey Shtukin
 */

class WeatherListFragment : Fragment() {

    private lateinit var listener: RecyclerViewClickListener
    private lateinit var mCityAdapter: CityAdapter
    private var allCityList: MutableList<Weather> = ArrayList()
    private var favCityList: MutableList<Weather> = ArrayList()
    private var favAndAllCityList: MutableList<Weather?> = ArrayList()
    private var savedAdapterPosition = 0

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is RecyclerViewClickListener) {
            listener = context
            allCityList = WeatherModel().getWeatherList()
            for (city in allCityList) {
                if (city.isFavourite) favCityList.add(city)
            }
        } else throw RuntimeException()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_weatherlist, container, false)
    }

    override fun onStart() {
        super.onStart()
        generateCityList(favCityList, allCityList)
        mCityAdapter = CityAdapter(context)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = mCityAdapter
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    private fun generateCityList(favCityList: MutableList<Weather>, allCityList: MutableList<Weather>) {
        favAndAllCityList.clear()
        favAndAllCityList.add(null)
        favAndAllCityList.addAll(favCityList)
        favAndAllCityList.add(null)
        favAndAllCityList.addAll(allCityList)
    }

    inner class CityAdapter(val context: Context?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun getItemCount(): Int {
            return favAndAllCityList.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return if (viewType == CITY_VIEW_TYPE)
                CityHolder(LayoutInflater.from(context).inflate(R.layout.city_item, parent, false))
            else
                TitleHolder(
                    LayoutInflater.from(context).inflate(
                        R.layout.title_item,
                        parent,
                        false
                    )
                )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (holder.itemViewType) {
                CITY_VIEW_TYPE -> {
                    holder as CityHolder
                    holder.tvCityName.text = favAndAllCityList[position]?.cityName
                    holder.tvTemp.text = favAndAllCityList[position]?.weatherTemp.toString()
                    holder.tvInformation.text = favAndAllCityList[position]?.weatherDescription
                    Glide
                        .with(holder.itemView.context)
                        .load(favAndAllCityList[position]?.cityPhoto)
                        .apply(RequestOptions.circleCropTransform())
                        .into(holder.ivCityPhoto)
                    Glide
                        .with(holder.itemView.context)
                        .load(favAndAllCityList[position]?.weatherPicture)
                        .into(holder.ivWeatherPicture)
                }
                else -> {
                    holder as TitleHolder
                    if (position == 0) {
                        holder.title.text = getString(R.string.favourites)
                    }
                    if (position == favCityList.size + 1) {
                        holder.title.text = getString(R.string.all)
                    }
                }
            }
        }

        override fun getItemViewType(position: Int): Int {
            return if (position == 0 || position == favCityList.size + 1)
                TITLE_VIEW_TYPE
            else
                CITY_VIEW_TYPE
        }
    }

    inner class CityHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener, View.OnLongClickListener {
        val tvCityName: TextView = view.tvCityName
        val tvTemp: TextView = view.tvTemp
        val tvInformation: TextView = view.tvInformation
        val ivCityPhoto: ImageView = view.ivCityPhoto
        val ivWeatherPicture: ImageView = view.ivWeatherPicture

        init {
            view.setOnClickListener(this)
            view.setOnLongClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onItemClicked(favAndAllCityList[adapterPosition])
        }

        override fun onLongClick(v: View?): Boolean {
            callFavouritesDialog(adapterPosition)
            return true
        }
    }

    class TitleHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.tvTitle
    }

    private fun callFavouritesDialog(position: Int) {
        val favDialog = FavouritesDialogFragment.newInstance(favAndAllCityList[position]!!.isFavourite)
        favDialog.setTargetFragment(
            this,
            FAVOURITES_DIALOG_CODE
        )
        favDialog.show(fragmentManager, null)
        savedAdapterPosition = position
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FAVOURITES_DIALOG_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (favAndAllCityList[savedAdapterPosition]!!.isFavourite) {
                    favCityList.remove(favAndAllCityList[savedAdapterPosition])

                } else {
                    favAndAllCityList[savedAdapterPosition]?.let { favCityList.add(it) }
                }
                favAndAllCityList[savedAdapterPosition]?.isFavourite =
                    !favAndAllCityList[savedAdapterPosition]!!.isFavourite
                generateCityList(favCityList, allCityList)
                mCityAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sort_by_name -> {
                val sortedFav = favCityList.sortedWith(compareBy { it.cityName }) as MutableList
                val sortedAll = allCityList.sortedWith(compareBy { it.cityName }) as MutableList
                generateCityList(sortedFav, sortedAll)
            }

            R.id.sort_by_name_reverse -> {
                val sortedFav = favCityList.sortedWith(compareBy { it.cityName }).reversed() as MutableList
                val sortedAll = allCityList.sortedWith(compareBy { it.cityName }).reversed() as MutableList
                generateCityList(sortedFav, sortedAll)
            }

            R.id.sort_by_temp -> {
                val sortedFav = favCityList.sortedWith(compareBy { it.weatherTemp }) as MutableList
                val sortedAll = allCityList.sortedWith(compareBy { it.weatherTemp }) as MutableList
                generateCityList(sortedFav, sortedAll)
            }

            R.id.sort_by_temp_reverse -> {
                val sortedFav = favCityList.sortedWith(compareBy { it.weatherTemp }).reversed() as MutableList
                val sortedAll = allCityList.sortedWith(compareBy { it.weatherTemp }).reversed() as MutableList
                generateCityList(sortedFav, sortedAll)
            }
        }
        mCityAdapter.notifyDataSetChanged()
        return true
    }


    private companion object {
        private const val TITLE_VIEW_TYPE = 1
        private const val CITY_VIEW_TYPE = 2
        private const val FAVOURITES_DIALOG_CODE = 3
    }
}