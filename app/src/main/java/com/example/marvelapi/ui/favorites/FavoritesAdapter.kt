package com.example.marvelapi.ui.favorites

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.core.content.ContextCompat
import androidx.core.view.AccessibilityDelegateCompat
import androidx.core.view.ViewCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelapi.R
import com.example.marvelapi.data.database.FavoriteCharacterInterface
import com.example.marvelapi.ui.home.OnFavoriteClickListener
import com.example.marvelapi.utils.LoadImage
import com.example.marvelapi.utils.MyDiffCallback
import java.lang.ref.WeakReference

/**
 *
 *
 *
 * created on 25/11/2022
 * @author Lucas Goncalves
 */
class FavoritesAdapter(private val listener: OnFavoriteClickListener) :
    ListAdapter<FavoriteCharacterInterface, FavoritesAdapter.ViewHolder>(MyDiffCallback()) {

    class ViewHolder(view: View, listener: OnFavoriteClickListener) :
        RecyclerView.ViewHolder(view) {

        private lateinit var navController: NavController
        private lateinit var favoriteResult: FavoriteCharacterInterface
        private var weakContext : WeakReference<Context>? = WeakReference(view.context)

        val logoImage       : ImageView
        val title           : TextView
        val subTitle        : TextView
        val favButton       : ToggleButton

        init {
            logoImage = view.findViewById(R.id.iv_card_logo)
            title = view.findViewById(R.id.tv_title)
            subTitle = view.findViewById(R.id.tv_subtitle)
            favButton = view.findViewById(R.id.im_button)

            //region Accessibility FavButton
            //TODO: !! TRANSFORM FAVBUTTON IN CUSTOM VIEW AND IMP ACCESSIBILITY TO NOT DRY
            ViewCompat.setAccessibilityDelegate(favButton, object : AccessibilityDelegateCompat() {
                override fun onInitializeAccessibilityNodeInfo(
                    host: View,
                    info: AccessibilityNodeInfoCompat,
                ) {
                    super.onInitializeAccessibilityNodeInfo(host, info)
                    info.contentDescription =
                        if (favButton.isChecked) weakContext?.get()?.getString(R.string.accessibility_fav_true_button)
                        else weakContext?.get()?.getString(R.string.accessibility_fav_false_button)
                    info.className = Button::class.java.name
                    info.roleDescription = Button::class.java.simpleName
                    info.isCheckable = false
                    info.isClickable = true
                    info.addAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat(
                        AccessibilityNodeInfo.ACTION_CLICK,
                        weakContext?.get()?.getString(R.string.accessibility_fav_action)))
                }
            })
            //endregion

            setCardClickListener(view)
            setOnFavButtonClick(favButton, listener)
        }

        private fun setOnFavButtonClick(view: ToggleButton, listener: OnFavoriteClickListener) {
         view.setOnClickListener { listener.onClick(view, favoriteResult) }
        }

        fun setDataResult(data: FavoriteCharacterInterface) {
            favoriteResult = data
        }

        private fun setCardClickListener(view: View) {
            view.setOnClickListener {
                navController = Navigation.findNavController(itemView)
                val action = FavoritesFragmentDirections.actionNavFavoritesToNavDetails(this.favoriteResult)
                navController.navigate(action)
            }
        }
    }

    fun setData(characters: MutableList<FavoriteCharacterInterface>) {
        this.submitList(characters)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.home_adapter, viewGroup, false)
        return ViewHolder(view, listener)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val uri: Uri = Uri.parse(this.currentList[position].imagePath)
        LoadImage.loadImage(uri, viewHolder.logoImage)
        val context = viewHolder.favButton.context

        viewHolder.title.text = this.currentList[position].name
        viewHolder.subTitle.text = this.currentList[position].description
        viewHolder.favButton.background = ContextCompat.getDrawable(context, R.drawable.ic_favorite_yellow)
        viewHolder.setDataResult(this.currentList[position])
    }
}