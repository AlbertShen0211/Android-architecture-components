package com.android.myapplication.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.android.myapplication.databinding.FragmentHomeBinding
import com.android.myapplication.util.InjectorUtils
import com.android.myapplication.viewmodel.HomeViewModel

class HomeFragment : Fragment() {
    private val TAG: String = HomeFragment::class.java.name
    private val homeViewModel: HomeViewModel by viewModels {
        InjectorUtils.provideHomeViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.viewModel = homeViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.tvNavigation.setOnClickListener {
            navigateToDetailPage("1", it)
        }

        binding.rootLayout.setOnClickListener {
            homeViewModel.onHomeViewClicked()
        }


        homeViewModel.err.observe(viewLifecycleOwner, Observer {
            Toast.makeText(activity, "err" + it, Toast.LENGTH_LONG).show()
        })
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        return binding.root
    }


    private fun navigateToDetailPage(plantId: String, view: View) {
        val direction =
            HomeFragmentDirections.actionNavigationHomeToDetailActivity(plantId)
        view.findNavController().navigate(direction)
    }
}