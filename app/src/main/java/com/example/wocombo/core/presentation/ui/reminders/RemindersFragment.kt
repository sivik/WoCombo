package com.example.wocombo.core.presentation.ui.reminders

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wocombo.R
import com.example.wocombo.common.extensions.viewInflateBinding
import com.example.wocombo.common.functional.observe
import com.example.wocombo.common.logs.LoggerTags
import com.example.wocombo.common.navigation.BaseNavigation
import com.example.wocombo.core.domain.usecases.DeleteReminderUseCase
import com.example.wocombo.core.presentation.enums.InfoViewState
import com.example.wocombo.core.presentation.ui.reminders.adapter.RemindersAdapter
import com.example.wocombo.databinding.FragmentRemindersListBinding
import de.mateware.snacky.Snacky
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class RemindersFragment : Fragment() {

    private val vm: RemindersViewModel by viewModel()
    private val binding by viewInflateBinding(FragmentRemindersListBinding::inflate)
    private val navigation by inject<BaseNavigation>()
    private val remindersAdapter = RemindersAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            navigation.popBackStack()
        }
        observe(vm.deleteReminderLiveData, ::handleDeleteReminder)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecyclerView()
        initAdapter()
        initAdapterListener()
        initObservers()
        getReminders()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initObservers() {
        lifecycleScope.launch {
            try {
                vm.reminders.flowWithLifecycle(
                    lifecycle = viewLifecycleOwner.lifecycle,
                    minActiveState = Lifecycle.State.RESUMED
                ).catch { e ->
                    Log.e(LoggerTags.REMINDERS, "Cannot get reminders from db", e)
                    throw e
                }.collectLatest { result ->
                    result.pagingData?.let { reminders ->
                        reminders.collect { remindersAdapter.submitData(it) }
                    }
                    result.failure?.let {
                        Log.e(LoggerTags.REMINDERS, "Cannot get reminders")
                    }
                }
            } catch (e: Exception) {
                Log.e(LoggerTags.REMINDERS, "Cannot get reminders", e)
                cancel()
            }
        }
    }

    private fun getReminders() {
        vm.getReminders()
    }

    private fun initAdapterListener() {
        lifecycleScope.launch {
            remindersAdapter.loadStateFlow.collectLatest { loadStates ->
                when (loadStates.refresh) {
                    is LoadState.Loading ->showReminderViewState(InfoViewState.LOADING)
                    is LoadState.Error -> showReminderViewState(InfoViewState.ERROR)
                    else -> {
                        if (remindersAdapter.itemCount == 0) {
                            showReminderViewState(InfoViewState.NO_ELEMENTS)
                        } else {
                            showReminderViewState(InfoViewState.SHOW_ELEMENTS)
                        }
                    }
                }
            }
        }
    }

    private fun initAdapter() {
        remindersAdapter.onReminderDeleted = {
            vm.deleteReminder(it.id)
        }
    }

    private fun initRecyclerView() {
        binding.rvReminderList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = remindersAdapter
        }
    }

    private fun handleDeleteReminder(result: DeleteReminderUseCase.Result?) {
        result?.let { response ->
            response.reminderId?.let {
                Snacky.builder()
                    .setActivity(requireActivity())
                    .setText(R.string.successfully_delete_reminder)
                    .setDuration(Snacky.LENGTH_SHORT)
                    .success()
                    .show()
            }

            response.failure?.let {
                Snacky.builder()
                    .setActivity(requireActivity())
                    .setText(R.string.err_delete_reminder)
                    .setDuration(Snacky.LENGTH_SHORT)
                    .error()
                    .show()
            }
        }
    }

    private fun showReminderViewState(viewState: InfoViewState) {
        when (viewState) {
            InfoViewState.SHOW_ELEMENTS -> {
                binding.rvReminderList.visibility = View.VISIBLE
                binding.clStateContainer.visibility = View.GONE
                binding.clLoadingContainer.visibility = View.GONE
            }
            InfoViewState.NO_ELEMENTS -> {
                binding.rvReminderList.visibility = View.GONE
                binding.clLoadingContainer.visibility = View.GONE
                binding.clStateContainer.visibility = View.VISIBLE
                binding.ivStateIcon.setImageDrawable(
                    ContextCompat.getDrawable(requireContext(), R.drawable.box)
                )
                binding.tvStateTitle.text = getString(R.string.no_reminders)
                binding.tvStateDescription.text = getString(R.string.add_reminders_to_show_list)
            }

            InfoViewState.LOADING -> {
                binding.rvReminderList.visibility = View.GONE
                binding.clStateContainer.visibility = View.GONE
                binding.clLoadingContainer.visibility = View.VISIBLE
            }
            InfoViewState.ERROR -> {
                binding.rvReminderList.visibility = View.GONE
                binding.clLoadingContainer.visibility = View.GONE
                binding.clStateContainer.visibility = View.VISIBLE
                binding.ivStateIcon.setImageDrawable(
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_error)
                )
                binding.tvStateTitle.text = getString(R.string.err_no_unknown_state_title)
                binding.tvStateDescription.text = getString(R.string.err_unknown_failure)
            }
            InfoViewState.NO_INTERNET -> throw IllegalStateException("Reminders only from db")
        }
    }
}