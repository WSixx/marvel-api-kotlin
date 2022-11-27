package com.example.marvelapi.ui.home

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.view.accessibility.AccessibilityNodeInfo.ACTION_CLICK
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction
import android.widget.ToggleButton
import androidx.appcompat.widget.SearchView
import androidx.core.view.AccessibilityDelegateCompat
import androidx.core.view.ViewCompat
import androidx.core.view.ViewCompat.onInitializeAccessibilityNodeInfo
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.marvelapi.data.CharacterInterfaceBase
import com.example.marvelapi.data.database.FavoriteCharacterInterface
import com.example.marvelapi.data.remote.CharacterInterfaceResult
import com.example.marvelapi.databinding.FragmentHomeBinding
import com.example.marvelapi.ui.base.BaseFragment
import com.example.marvelapi.utils.Resource
import kotlinx.coroutines.launch


class HomeFragment : BaseFragment(),
    SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    companion object {
        private var TAG: String = Companion::class.java.simpleName
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: HomeAdapter

    private val homeViewModel: HomeViewModel by viewModels { HomeViewModel.Factory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupRecycleView()

        //region  Accessibility
        setSearchViewAccessibility()
        //endregion


        homeViewModel.allFavorites.observe(viewLifecycleOwner) { fav ->
            fav?.let { adapter.setFavorites(fav) }
        }

        binding.svHome.setOnQueryTextListener(this)

        observeData(homeViewModel)
        return root
    }

    //region SearchView Accessibility
    /**
     * Sets some accessibility behaviors
     * !!<p>not as I like but is ok for now</p>
     */
    private fun setSearchViewAccessibility() {
        ViewCompat.setAccessibilityDelegate(binding.svHome, object : AccessibilityDelegateCompat() {

            override fun onInitializeAccessibilityNodeInfo(
                host: View,
                info: AccessibilityNodeInfoCompat,
            ) {
                super.onInitializeAccessibilityNodeInfo(host, info)
                print("Entrou no onInitializeAccessibilityNodeInfo")
                info.contentDescription = "Pesquisar personagem pelo nome"
                info.className = android.widget.SearchView::class.java.name
                info.roleDescription = "Barra de Pesquisa"
            }
        })
    }
    //endregion

    private fun setupRecycleView() {
        adapter = HomeAdapter(this)

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvCharacters.layoutManager = layoutManager
        binding.rvCharacters.adapter = adapter
    }

    private fun observeData(homeViewModel: HomeViewModel) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.allCharactersStatus.collect { characters ->
                    when (characters) {
                        is Resource.Loading -> {
                            showProgressBar()
                            Log.d(TAG, "Loading")
                        }
                        is Resource.Success -> {
                            hideProgressBar()
                            showAllCharacters(characters.data)
                            Log.d(TAG, "Success")
                        }
                        is Resource.Error -> {
                            hideProgressBar()
                            Log.e(TAG, characters.message.toString())
                        }
                    }
                }
            }
        }
    }

    override fun toCallWhenHasInternetConnection() {
        homeViewModel.getMarvelCharacters()
    }

    private fun showAllCharacters(data: List<CharacterInterfaceResult>?) {
        adapter.setData(data as MutableList<CharacterInterfaceResult>)
    }

    override fun onClick(view: ToggleButton, data: CharacterInterfaceBase) {
        if (!view.isChecked) {
            homeViewModel.deleteFav(
                FavoriteCharacterInterface(
                    name = data.name,
                    description = data.description,
                    id = data.id,
                    imagePath = data.thumbnail.buildImage()))
        } else {
            homeViewModel.insert(FavoriteCharacterInterface(
                name = data.name,
                description = data.description,
                id = data.id,
                imagePath = data.thumbnail.buildImage()))
        }
        super.onClick(view, data)
    }


    //region progressBar
    override fun showProgressBar() {
        binding.pbMain.isVisible = true
    }

    override fun hideProgressBar() {
        binding.pbMain.isVisible = false
    }
    //endregion

    //region SearchView
    override fun onQueryTextSubmit(query: String?): Boolean {
        homeViewModel.getMarvelCharactersByName(query.toString())
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return if (TextUtils.isEmpty(newText)) {
            homeViewModel.getMarvelCharacters()
            false
        } else {
            homeViewModel.getMarvelCharactersByName(newText!!)
            true
        }
    }

    override fun onClose(): Boolean {
        homeViewModel.getMarvelCharacters()
        return true
    }
    //endregion

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}