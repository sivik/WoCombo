package com.example.wocombo.core.presentation.ui.transmissionlist.schedules

import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wocombo.R
import com.example.wocombo.app.services.ServiceManager
import com.example.wocombo.common.broadcast.ScheduleBroadcastConst
import com.example.wocombo.common.broadcast.ScheduleReceiver
import com.example.wocombo.common.extensions.viewInflateBinding
import com.example.wocombo.common.functional.Failure
import com.example.wocombo.common.functional.observe
import com.example.wocombo.common.navigation.BaseNavigation
import com.example.wocombo.core.domain.errors.CommunicationsFailures
import com.example.wocombo.core.domain.errors.ScheduleFailures
import com.example.wocombo.core.domain.usecases.DownloadSchedulesUseCase
import com.example.wocombo.core.presentation.enums.InfoViewState
import com.example.wocombo.core.presentation.ui.transmissionlist.TransmissionListViewModel
import com.example.wocombo.core.presentation.ui.transmissionlist.schedules.adapter.ScheduleListAdapter
import com.example.wocombo.databinding.FragmentScheduleListBinding
import de.mateware.snacky.Snacky
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class ScheduleListFragment : Fragment() {

    private val binding by viewInflateBinding(FragmentScheduleListBinding::inflate)
    private val vm: ScheduleListViewModel by viewModel()
    private val navigation: BaseNavigation by inject()
    private val parentVm by sharedViewModel<TransmissionListViewModel>()

    /*Aktualnie lepszym i zalecanym rozwiązaniem byłoby użycie work managera*/
    private val scheduleBroadcastReceiver = ScheduleReceiver { schedules ->
        (binding.rvScheduleList.adapter as? ScheduleListAdapter)?.update(schedules)
    }

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

    override fun onResume() {
        val filter = IntentFilter(ScheduleBroadcastConst.SCHEDULE_INTENT)
        LocalBroadcastManager.getInstance(requireActivity())
            .registerReceiver(scheduleBroadcastReceiver, filter)
        ServiceManager.startDownloadService(requireActivity(), navigation)
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(requireActivity())
            .unregisterReceiver(scheduleBroadcastReceiver)
    }

    private fun downloadSchedules() {
        showScheduleViewState(InfoViewState.LOADING)
        vm.downloadSchedules()
    }

    private fun initAdapter() {
        binding.rvScheduleList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ScheduleListAdapter(LinkedList())
            adapter?.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.ALLOW
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

                    is CommunicationsFailures.ConnectionFailure ->{
                        showScheduleViewState(InfoViewState.ERROR)
                        getString(R.string.err_timeout_failure)
                    }

                    is CommunicationsFailures.NoInternetFailure -> {
                        showScheduleViewState(InfoViewState.NO_INTERNET)
                        getString(R.string.err_no_internet_failure)
                    }

                    in listOf(
                        CommunicationsFailures.InternalServerFailure,
                        ScheduleFailures.DownloadSchedulesFailure
                    ) -> {
                        showScheduleViewState(InfoViewState.ERROR)
                        getString(R.string.err_internal_server_failure)
                    }

                    is Failure.UnknownFailure -> {
                        showScheduleViewState(InfoViewState.ERROR)
                        getString(R.string.err_unknown_failure)
                    }
                    else -> {
                        showScheduleViewState(InfoViewState.ERROR)
                        getString(R.string.err_unknown_failure)
                    }
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
        when (viewState) {
            InfoViewState.SHOW_ELEMENTS -> {
                binding.rvScheduleList.visibility = View.VISIBLE
                binding.clStateContainer.visibility = View.GONE
                binding.clLoadingContainer.visibility = View.GONE
            }
            InfoViewState.NO_INTERNET -> {
                binding.rvScheduleList.visibility = View.GONE
                binding.clLoadingContainer.visibility = View.GONE
                binding.clStateContainer.visibility = View.VISIBLE
                binding.ivStateIcon.setImageDrawable(
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_no_internet_conn)
                )
                binding.tvStateTitle.text = getString(R.string.err_no_internet_title)
                binding.tvStateDescription.text = getString(R.string.err_no_internet_failure)
            }
            InfoViewState.LOADING -> {
                binding.rvScheduleList.visibility = View.GONE
                binding.clStateContainer.visibility = View.GONE
                binding.clLoadingContainer.visibility = View.VISIBLE
            }
            InfoViewState.ERROR -> {
                binding.rvScheduleList.visibility = View.GONE
                binding.clLoadingContainer.visibility = View.GONE
                binding.clStateContainer.visibility = View.VISIBLE
                binding.ivStateIcon.setImageDrawable(
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_error)
                )
                binding.tvStateTitle.text = getString(R.string.err_no_unknown_state_title)
                binding.tvStateDescription.text = getString(R.string.err_unknown_failure)
            }
        }
    }
}


