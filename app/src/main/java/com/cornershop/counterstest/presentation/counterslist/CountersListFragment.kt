package com.cornershop.counterstest.presentation.counterslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.cornershop.counterstest.databinding.FragmentCountersListBinding
import com.cornershop.counterstest.presentation.BaseFragment

class CountersListFragment : BaseFragment<CountersListViewModel>() {

    override val viewModel: CountersListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentCountersListBinding.inflate(inflater, container, false).bindLayout {
        vm = viewModel
        shouldShowToolbar = false
    }
}
