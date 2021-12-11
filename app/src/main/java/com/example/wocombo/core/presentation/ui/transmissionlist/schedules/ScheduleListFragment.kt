package com.example.wocombo.core.presentation.ui.transmissionlist.schedules

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wocombo.R
import com.example.wocombo.common.extensions.viewInflateBinding
import com.example.wocombo.common.functional.Failure
import com.example.wocombo.common.functional.observe
import com.example.wocombo.core.domain.errors.CommunicationsFailures
import com.example.wocombo.core.domain.errors.ScheduleFailures
import com.example.wocombo.core.domain.usecases.DownloadSchedulesUseCase
import com.example.wocombo.core.presentation.enums.InfoViewState
import com.example.wocombo.core.presentation.ui.transmissionlist.TransmissionListViewModel
import com.example.wocombo.core.presentation.ui.transmissionlist.schedules.adapter.ScheduleListAdapter
import com.example.wocombo.databinding.FragmentScheduleListBinding
import de.mateware.snacky.Snacky
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class ScheduleListFragment : Fragment() {

    private val binding by viewInflateBinding(FragmentScheduleListBinding::inflate)
    private val vm: ScheduleListViewModel by viewModel()
    private val parentVm by sharedViewModel<TransmissionListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        observe(vm.scheduleLiveData, ::handleScheduleListDownload)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initSwipeListener()
        downloadSchedules()
    }

    private fun downloadSchedules() {
        showScheduleViewState(InfoViewState.LOADING)
        vm.downloadSchedules()
    }

    private fun initAdapter() {
        binding.rvScheduleList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ScheduleListAdapter(LinkedList())
        }
    }

    private fun initSwipeListener() {
        binding.srlScheduleList.setOnRefreshListener {
            showScheduleViewState(InfoViewState.LOADING)
            binding.srlScheduleList.isRefreshing = true
            downloadSchedules()
        }
    }

    private fun handleScheduleListDownload(result: DownloadSchedulesUseCase.Result?) {
        result?.let { response ->
            binding.srlScheduleList.isRefreshing = false
            response.scheduleList?.let { schedules ->
                showScheduleViewState(InfoViewState.SHOW_ELEMENTS)
                (binding.rvScheduleList.adapter as ScheduleListAdapter).update(schedules)

                //todo posortowac zgodnie z parentVm.sortLiveData
            }

            response.failure?.let { failure ->
                val error = when (failure) {

                    is CommunicationsFailures.ConnectionFailure ->
                        getString(R.string.err_timeout_failure)

                    is CommunicationsFailures.NoInternetFailure ->
                        getString(R.string.err_no_internet_failure)

                    in listOf(
                        CommunicationsFailures.InternalServerFailure,
                        ScheduleFailures.DownloadSchedulesFailure
                    ) -> getString(R.string.err_internal_server_failure)

                    is Failure.UnknownFailure -> { getString(R.string.err_unknown_failure) }
                    else -> { getString(R.string.err_unknown_failure) }
                }
                Snacky.builder()
                    .setActivity(requireActivity())
                    .setText(error)
                    .setDuration(Snacky.LENGTH_LONG)
                    .error()
                    .show()
            }
        }
    }

    private fun showScheduleViewState(viewState: InfoViewState) {
        when(viewState){
            InfoViewState.SHOW_ELEMENTS -> {
                binding.rvScheduleList.visibility = View.VISIBLE
                binding.clStateContainer.visibility = View.GONE
                binding.clLoadingContainer.visibility = View.GONE
            }
            InfoViewState.NO_INTERNET -> {
                binding.rvScheduleList.visibility = View.GONE
                binding.clLoadingContainer.visibility = View.GONE
                binding.clStateContainer.visibility = View.VISIBLE
                binding.tvStateTitle.text = getString(R.string.err_no_internet_title)
                binding.tvStateDescription.text = getString(R.string.err_no_internet_failure)
            }
            InfoViewState.LOADING -> {
                binding.rvScheduleList.visibility = View.GONE
                binding.clStateContainer.visibility = View.GONE
                binding.clLoadingContainer.visibility = View.VISIBLE
            }
            InfoViewState.ERROR ->  {
                binding.rvScheduleList.visibility = View.GONE
                binding.clLoadingContainer.visibility = View.GONE
                binding.clStateContainer.visibility = View.VISIBLE
                binding.tvStateTitle.text = getString(R.string.err_no_unknown_state_title)
                binding.tvStateDescription.text = getString(R.string.err_unknown_failure)
            }
        }
    }
}


