package com.example.marvelapi.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.marvelapi.data.CharacterInterfaceBase
import com.example.marvelapi.data.database.FavoriteCharacterInterface
import com.example.marvelapi.databinding.FragmentFavoritesBinding
import com.example.marvelapi.ui.home.OnFavoriteClickListener

class FavoritesFragment : Fragment(), OnFavoriteClickListener {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val favoritesViewModel: FavoritesViewModel by viewModels { FavoritesViewModel.Factory }
    private lateinit var adapter: FavoritesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        adapter = FavoritesAdapter(this)

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvCharacters.layoutManager = layoutManager
        binding.rvCharacters.adapter = adapter

        favoritesViewModel.allFavorites.observe(viewLifecycleOwner) { fav ->
            fav?.let {
                adapter.setData(fav.toMutableList())
            }
        }
        return root
    }

    override fun onClick(view: ToggleButton, data: CharacterInterfaceBase) {
        favoritesViewModel.deleteFav(
            FavoriteCharacterInterface(
                name        = data.name,
                description = data.description,
                id          = data.id,
                imagePath   = data.imagePath))
        super.onClick(view, data)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}