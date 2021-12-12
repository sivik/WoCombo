package com.example.wocombo.core.presentation.ui.reminders

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.lifecycle.lifecycleScope
import com.example.wocombo.R
import com.example.wocombo.common.extensions.viewInflateBinding
import com.example.wocombo.common.functional.observe
import com.example.wocombo.common.logs.LoggerTags
import com.example.wocombo.common.navigation.BaseNavigation
import com.example.wocombo.core.domain.usecases.DeleteReminderUseCase
import com.example.wocombo.core.presentation.ui.reminders.adapter.RemindersAdapter
import com.example.wocombo.databinding.FragmentRemindersBinding
import de.mateware.snacky.Snacky
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class RemindersFragment : Fragment() {

    private val vm: RemindersViewModel by viewModel()
    private val binding by viewInflateBinding(FragmentRemindersBinding::inflate)
    private val navigation by inject<BaseNavigation>()
    private val remindersAdapter = RemindersAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            navigation.popBackStack()
        }
        observe(vm.deleteReminderLiveData, ::handleDeleteReminder)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getReminders()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun getReminders() {
        lifecycleScope.launch {
            try {
                vm.getReminders().catch { e ->
                    Log.e(LoggerTags.REMINDERS, "Cannot get reminders from db", e)
                    throw e
                }.collectLatest { pagingData ->
                    remindersAdapter.submitData(pagingData)
                }
            } catch (e: Exception) {
                Snacky.builder()
                    .setActivity(requireActivity())
                    .setText(R.string.err_unknown_failure)
                    .setDuration(Snacky.LENGTH_SHORT)
                    .error()
                    .show()
                cancel()
            }
        }
    }

    private fun handleDeleteReminder(result: DeleteReminderUseCase.Result?) {
        result?.let { response ->
            response.reminderId?.let {
                Snacky.builder()
                    .setActivity(requireActivity())
                    .setText(R.string.successfully_delete_reminder)
                    .setDuration(Snacky.LENGTH_SHORT)
                    .error()
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

}