package com.cornershop.counterstest.presentation.counters.addcounter

import android.os.Bundle
import android.view.*
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.cornershop.counterstest.R
import com.cornershop.counterstest.databinding.FragmentAddCounterBinding
import com.cornershop.counterstest.presentation.base.BaseFragment
import com.cornershop.counterstest.presentation.extension.showAlertDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddCounterFragment : BaseFragment<AddCounterViewModel>() {

    override val viewModel: AddCounterViewModel by hiltNavGraphViewModels(R.id.add_counter_graph)

    private var saveItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentAddCounterBinding.inflate(layoutInflater, container, false).bindLayout {
        vm = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.isLoading.observe { isLoading ->
            if (isLoading) {
                saveItem?.setActionView(R.layout.progress_bar_layout)
            } else {
                activity?.invalidateOptionsMenu()
            }
        }
        viewModel.error.observeEvent {
            showAlertDialog(
                title = getString(R.string.error_creating_counter_title),
                message = getString(R.string.connection_error_description),
                positiveButtonText = R.string.ok
            )
        }
        viewModel.goToCounters.observeEvent {
            findNavController().popBackStack()
        }
        viewModel.goToExamples.observeEvent {
            findNavController().navigate(R.id.select_counter_fragment)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_counter_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.save) {
            saveItem = item
            viewModel.onSaveClicked()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}