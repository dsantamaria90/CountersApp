package com.cornershop.counterstest.presentation.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.cornershop.counterstest.R
import com.cornershop.counterstest.databinding.FragmentWelcomeBinding
import com.cornershop.counterstest.presentation.BaseFragment

class WelcomeFragment : BaseFragment<WelcomeViewModel>() {

    override val viewModel: WelcomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentWelcomeBinding.inflate(inflater, container, false).bindLayout {
        vm = viewModel
        shouldShowToolbar = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.goToCountersList.observeEvent {
            findNavController().navigate(R.id.counters_list_fragment)
        }
    }
}