package com.example.marvelapi.ui.details

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Button
import android.widget.ToggleButton
import androidx.core.content.ContextCompat
import androidx.core.view.AccessibilityDelegateCompat
import androidx.core.view.ViewCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.example.marvelapi.R
import com.example.marvelapi.data.CharacterInterfaceBase
import com.example.marvelapi.data.database.FavoriteCharacterInterface
import com.example.marvelapi.databinding.FragmentDetailsBinding
import com.example.marvelapi.ui.base.BaseFragment
import com.example.marvelapi.utils.LoadImage
import kotlinx.coroutines.launch

class DetailsFragment : BaseFragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: DetailsFragmentArgs by navArgs()
    private val detailsViewModel: DetailsViewModel by viewModels { DetailsViewModel.Factory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)

        detailsViewModel.allFavorites.observe(viewLifecycleOwner) {}

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                detailsViewModel.getFromDatabaseById.collect { characters ->
                    if (characters.data?.id == args.data.id) {
                        binding.imButton.isChecked = true
                        binding.imButton.background = ContextCompat.getDrawable(requireContext(),
                            R.drawable.ic_favorite_yellow)
                    } else {
                        binding.imButton.isChecked = false
                        binding.imButton.background = ContextCompat.getDrawable(requireContext(),
                            R.drawable.ic_favorite_border_24)
                    }
                }
            }
        }

        //region Accessibility
        setupFavButtonAccessibility()
        setupImageViewAccessibility()
        //endregion
        return binding.root
    }

    private fun setupImageViewAccessibility() {
        binding.ivImageDetails.contentDescription = buildString {
        append(getString(R.string.accessibility_iv_image_of))
        append(args.data.name)
    }
    }

    //region Accessibility FavButton
    //TODO: !! TRANSFORM FAVBUTTON IN CUSTOM VIEW AND IMP ACCESSIBILITY TO NOT DRY
    private fun setupFavButtonAccessibility() {
        ViewCompat.setAccessibilityDelegate(binding.imButton,
            object : AccessibilityDelegateCompat() {
                override fun onInitializeAccessibilityNodeInfo(
                    host: View,
                    info: AccessibilityNodeInfoCompat,
                ) {
                    super.onInitializeAccessibilityNodeInfo(host, info)
                    info.contentDescription =
                        if (binding.imButton.isChecked) requireContext().getString(R.string.accessibility_fav_true_button)
                        else requireContext().getString(R.string.accessibility_fav_false_button)
                    info.className = Button::class.java.name
                    info.roleDescription = Button::class.java.simpleName
                    info.isCheckable = false
                    info.isClickable = true
                    info.addAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat(
                        AccessibilityNodeInfo.ACTION_CLICK,
                        requireContext().getString(R.string.accessibility_fav_action)))
                }
            })
    }
    //endregion

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val uri: Uri = Uri.parse(args.data.thumbnail.buildImage())
        LoadImage.loadImage(uri, binding.ivImageDetails)

        binding.tvCharacterTitle.text = args.data.name
        binding.tvCharacterDesc.text = args.data.description

        binding.imButton.setOnClickListener { this.onClick(binding.imButton, args.data) }
    }

    override fun onClick(view: ToggleButton, data: CharacterInterfaceBase) {
        super.onClick(view, data)
        if (!view.isChecked) {
            detailsViewModel.deleteFav(FavoriteCharacterInterface(name = data.name,
                description = data.description,
                id = data.id,
                imagePath = data.thumbnail.buildImage()))
            binding.imButton.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_favorite_border_24)
        } else {
            detailsViewModel.insert(FavoriteCharacterInterface(name = data.name,
                description = data.description,
                id = data.id,
                imagePath = data.thumbnail.buildImage()))
            binding.imButton.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_favorite_yellow)
        }
    }

    override fun toCallWhenHasInternetConnection() {
        detailsViewModel.getMarvelCharactersByIdDatabase(args.data.id.toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun showProgressBar() {
        TODO("Not yet implemented")
    }

    override fun hideProgressBar() {
        TODO("Not yet implemented")
    }
}