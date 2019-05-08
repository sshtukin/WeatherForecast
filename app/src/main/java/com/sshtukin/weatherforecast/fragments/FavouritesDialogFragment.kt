package com.sshtukin.weatherforecast.fragments

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.sshtukin.weatherforecast.R

/**
 * Dialog fragment to add/remove to/from to favourites
 *
 * @author Sergey Shtukin
 */

class FavouritesDialogFragment : DialogFragment() {

    private var isFavourite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getBoolean(DIALOG_KEY)?.let {
            isFavourite = it
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            if (isFavourite) {
                builder.setTitle(getString(R.string.remove_text))
                builder.setPositiveButton(getString(R.string.yes))  { _, _ ->
                    targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, null)
                }
            } else {
                builder.setTitle(getString(R.string.add_text))
                builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
                    targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, null)
                }
            }
            builder.setNegativeButton(android.R.string.no) { _, _ ->
                targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_CANCELED, null)
            }
            builder.create()
        } ?: throw IllegalStateException(getString(R.string.activity_cannot_be_null))
    }

    companion object {
        private const val DIALOG_KEY = "1001"

        fun newInstance(isFavourite: Boolean) = FavouritesDialogFragment().apply {
            arguments = Bundle().apply {
                putBoolean(DIALOG_KEY, isFavourite)
            }
        }
    }
}
