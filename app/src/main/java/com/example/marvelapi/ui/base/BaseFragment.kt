package com.example.marvelapi.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.marvelapi.R
import com.example.marvelapi.helpers.network.InternetHelper
import com.example.marvelapi.helpers.network.NetworkStatusState
import com.example.marvelapi.helpers.network.NetworkViewModel
import com.example.marvelapi.ui.MyProgress
import com.example.marvelapi.ui.home.OnFavoriteClickListener
import kotlinx.coroutines.launch

/**
 *
 *  Base Fragment class with necessary inheritance
 *
 * created on 27/11/2022
 * @author Lucas Goncalves
 */
abstract class BaseFragment : Fragment(), MyProgress, OnFavoriteClickListener, InternetHelper {

    private val networkViewModel: NetworkViewModel by viewModels { NetworkViewModel.Factory }

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dialog = buildNoConnectionDialog()
        connectionObserver(dialog)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    /**
     * Observers internet connection
     */
    private fun connectionObserver(dialog: AlertDialog) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                networkViewModel.networkState.collect { status ->
                    when (status) {
                        NetworkStatusState.NetworkStatusConnected -> {
                            if (dialog.isShowing) dialog.dismiss()
                            toCallWhenHasInternetConnection()
                        }
                        NetworkStatusState.NetworkStatusDisconnected -> dialog.show()
                    }
                }
            }
        }
    }

    /**
     * Build's an AlertDialog when has no internet connection
     */
    private fun buildNoConnectionDialog(): AlertDialog {
        return AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.no_connection_message))
            .setTitle(getString(R.string.no_connection_title))
            .setCancelable(false)
            .create()
    }

}