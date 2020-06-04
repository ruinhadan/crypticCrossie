package com.example.crossie.game

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI

import com.example.crossie.R
import com.example.crossie.databinding.GameFragmentBinding

class GameFragment : Fragment() {

    private val viewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProviders.of(this, GameViewModel.Factory(activity.application)).get(GameViewModel::class.java)
    }

    @SuppressLint("RestrictedApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = "Cryptic Crossword"
        val binding = GameFragmentBinding.inflate(inflater)
        binding.setLifecycleOwner(viewLifecycleOwner)
        viewModel.hintVisible.observe(viewLifecycleOwner, Observer {
            binding.hintText.apply {
                visibility = when(it) {
                    true -> View.VISIBLE
                    else -> View.INVISIBLE
                }
            }
        })
        viewModel.solved.observe(viewLifecycleOwner, Observer{
                when (it) {
                    null -> {
                        binding.solvedAnswer.visibility = View.INVISIBLE
                        binding.answerText.visibility = View.VISIBLE
                        binding.checkButton.visibility = View.VISIBLE
                        binding.answerText.text.clear()
                    }
                    true -> {
                        binding.solvedAnswer.visibility = View.VISIBLE
                        binding.answerText.visibility = View.INVISIBLE
                        binding.checkButton.visibility = View.INVISIBLE
                    }
                    else -> {
                        Toast.makeText(this.context, "Oops, wrong answer :/", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )
        viewModel.nextButtonEnabled.observe(viewLifecycleOwner, Observer {
            when(it) {
                true -> binding.nextButton.visibility = View.VISIBLE
                else -> binding.nextButton.visibility = View.INVISIBLE
            }
        })
        viewModel.prevButtonEnabled.observe(viewLifecycleOwner, Observer {
            when(it) {
                true -> binding.prevButton.visibility = View.VISIBLE
                else -> binding.prevButton.visibility = View.INVISIBLE
            }
        })


        binding.checkButton.setOnClickListener {
            viewModel.checkAnswer(binding.answerText.text.toString())
        }
        binding.nextButton.setOnClickListener {
            binding.invalidateAll()
            viewModel.nextClue()
        }
        binding.prevButton.setOnClickListener {
            binding.invalidateAll()
            viewModel.prevClue()
        }
        binding.viewModel = viewModel
        (activity as AppCompatActivity).supportActionBar?.title = "Cryptic Crossword"
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }



}
