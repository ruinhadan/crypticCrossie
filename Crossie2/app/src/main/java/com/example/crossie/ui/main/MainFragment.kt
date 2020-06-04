package com.example.crossie.ui.main

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.crossie.databinding.MainFragmentBinding

class MainFragment : Fragment() {
    private val viewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProviders.of(this, MainViewModel.Factory(activity.application))
            .get(MainViewModel::class.java)
    }

    @SuppressLint("FragmentLiveDataObserve")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = MainFragmentBinding.inflate(inflater)
        (activity as AppCompatActivity).supportActionBar?.title = "Cryptic Crossword"
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        viewModel.cluesFound.observe(viewLifecycleOwner, Observer {
            when(it) {
                null -> Toast.makeText(this.context, "Found some unsolved clues. Try them out :)", Toast.LENGTH_SHORT).show()
                else -> Toast.makeText(this.context, it, Toast.LENGTH_SHORT).show()
            }
        })
        binding.playGame.setOnClickListener {
            this.findNavController()
                .navigate(MainFragmentDirections.actionMainFragmentToGameFragment())
        }
        binding.setLifecycleOwner(viewLifecycleOwner)
        binding.howToButton.setOnClickListener { it ->
            this.findNavController()
                .navigate(MainFragmentDirections.actionMainFragmentToHowToFragment())
        }
        binding.rulesButton.setOnClickListener { it ->
            this.findNavController()
                .navigate(MainFragmentDirections.actionMainFragmentToRulesFragment())
        }
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
