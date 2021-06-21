package com.cornershop.counterstest.presentation.counters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.cornershop.counterstest.R
import com.cornershop.counterstest.databinding.FragmentCountersBinding
import com.cornershop.counterstest.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CountersFragment : BaseFragment<CountersViewModel>() {

    override val viewModel: CountersViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentCountersBinding.inflate(inflater, container, false).bindLayout {
        vm = viewModel
        shouldShowToolbar = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.goToAddCounter.observeEvent {
            findNavController().navigate(R.id.add_counter_fragment)
        }
    }
}
