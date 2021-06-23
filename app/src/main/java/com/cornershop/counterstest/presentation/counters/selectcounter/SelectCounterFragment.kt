package com.cornershop.counterstest.presentation.counters.selectcounter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.cornershop.counterstest.R
import com.cornershop.counterstest.databinding.FragmentSelectCounterBinding
import com.cornershop.counterstest.presentation.base.BaseFragment
import com.cornershop.counterstest.presentation.counters.addcounter.AddCounterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectCounterFragment : BaseFragment<AddCounterViewModel>() {

    override val viewModel: AddCounterViewModel by hiltNavGraphViewModels(R.id.add_counter_graph)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentSelectCounterBinding.inflate(inflater, container, false).bindLayout {
        vm = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.goToAddCounter.observeEvent {
            findNavController().popBackStack()
        }
    }
}