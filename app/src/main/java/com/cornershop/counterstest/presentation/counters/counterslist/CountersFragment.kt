package com.cornershop.counterstest.presentation.counters.counterslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.cornershop.counterstest.R
import com.cornershop.counterstest.databinding.FragmentCountersBinding
import com.cornershop.counterstest.presentation.base.BaseFragment
import com.cornershop.counterstest.presentation.extension.showAlertDialog
import com.cornershop.counterstest.presentation.util.actionmode.ActionModeDelegate
import com.cornershop.counterstest.presentation.util.share.ShareDelegate
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CountersFragment : BaseFragment<CountersViewModel>() {

    override val viewModel: CountersViewModel by viewModels()

    @Inject
    lateinit var actionModeDelegate: ActionModeDelegate
    @Inject
    lateinit var shareDelegate: ShareDelegate

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
        setActionModeDelegate()
        viewModel.goToAddCounter.observeEvent {
            findNavController().navigate(R.id.add_counter_fragment)
        }
        viewModel.modifyCounterError.observeEvent {
            showAlertDialog(
                title = getString(
                    R.string.error_updating_counter_title,
                    it.counter.title,
                    it.modifiedCount
                ),
                message = getString(R.string.connection_error_description),
                positiveButtonText = R.string.dismiss,
                negativeButtonText = R.string.retry
            ) { isPositive ->
                if (!isPositive) {
                    viewModel.onRetryModifyCounterClicked(it)
                }
            }
        }
        viewModel.isEditing.observe { isEditing ->
            if (isEditing) {
                (activity as? AppCompatActivity)?.let { actionModeDelegate.start(it) }
            } else {
                actionModeDelegate.finish()
            }
        }
        viewModel.confirmDelete.observeEvent {
            showAlertDialog(
                title = getString(R.string.delete_x_question, it.title),
                positiveButtonText = R.string.delete,
                negativeButtonText = R.string.cancel
            ) { isPositive ->
                if (isPositive) {
                    viewModel.onDeleteConfirmed(it)
                }
            }
        }
        viewModel.deleteError.observeEvent {
            showAlertDialog(
                title = getString(R.string.error_deleting_counter_title),
                message = getString(R.string.connection_error_description),
                positiveButtonText = R.string.ok
            )
        }
        viewModel.share.observeEvent { counter ->
            context?.let {
                shareDelegate.share(
                    it,
                    getString(R.string.n_per_counter_name, counter.count, counter.title)
                )
            }
        }
    }

    private fun setActionModeDelegate() {
        actionModeDelegate.set(
            getString(R.string.n_selected, SINGLE_ITEM),
            R.menu.counters_editing_menu
        )
        actionModeDelegate.onMenuItemClicked = {
            when (it) {
                R.id.delete -> viewModel.onDeleteClicked()
                R.id.share -> viewModel.onShareClicked()
            }
        }
        actionModeDelegate.onDestroyClicked = {
            viewModel.onEditingEnded()
        }
    }

    companion object {
        private const val SINGLE_ITEM = 1
    }
}
