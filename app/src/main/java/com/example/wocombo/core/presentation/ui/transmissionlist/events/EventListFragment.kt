package com.example.wocombo.core.presentation.ui.transmissionlist.events

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
import com.example.wocombo.common.navigation.BaseNavigation
import com.example.wocombo.core.domain.errors.CommunicationsFailures
import com.example.wocombo.core.domain.errors.EventFailures
import com.example.wocombo.core.domain.usecases.DownloadEventsUseCase
import com.example.wocombo.core.presentation.enums.InfoViewState
import com.example.wocombo.core.presentation.ui.transmissionlist.TransmissionListViewModel
import com.example.wocombo.core.presentation.ui.transmissionlist.events.adapter.EventListAdapter
import com.example.wocombo.databinding.FragmentEventListBinding
import de.mateware.snacky.Snacky
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class EventListFragment : Fragment() {

    private val binding by viewInflateBinding(FragmentEventListBinding::inflate)
    private val navigation: BaseNavigation by inject()

    private val vm: EventListViewModel by viewModel()
    private val parentVm by sharedViewModel<TransmissionListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        observe(vm.eventsLiveData, ::handleEventListDownload)
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
        downloadEvents()
    }

    private fun downloadEvents() {
        showCarrierViewState(InfoViewState.LOADING)
        vm.downloadEvents()
    }

    private fun initAdapter() {
        binding.rvEventList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = EventListAdapter(LinkedList())
            (adapter as EventListAdapter).onEventSelected = { selectedEvent ->
                navigation.openPlayback(selectedEvent.title, selectedEvent.videoUrl)
            }
        }
    }

    private fun initSwipeListener() {
        binding.srlEventList.setOnRefreshListener {
            showCarrierViewState(InfoViewState.LOADING)
            binding.srlEventList.isRefreshing = true
            downloadEvents()
        }
    }

    private fun handleEventListDownload(result: DownloadEventsUseCase.Result?) {
        result?.let { response ->
            binding.srlEventList.isRefreshing = false
            response.events?.let { events ->
                showCarrierViewState(InfoViewState.SHOW_ELEMENTS)
                (binding.rvEventList.adapter as EventListAdapter).update(events)

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
                        EventFailures.DownloadEventsFailure
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

    private fun showCarrierViewState(viewState: InfoViewState) {
        when(viewState){
            InfoViewState.SHOW_ELEMENTS -> {
                binding.rvEventList.visibility = View.VISIBLE
                binding.clStateContainer.visibility = View.GONE
                binding.clLoadingContainer.visibility = View.GONE
            }
            InfoViewState.NO_INTERNET -> {
                binding.rvEventList.visibility = View.GONE
                binding.clLoadingContainer.visibility = View.GONE
                binding.clStateContainer.visibility = View.VISIBLE
                binding.tvStateTitle.text = getString(R.string.err_no_internet_title)
                binding.tvStateDescription.text = getString(R.string.err_no_internet_failure)
            }
            InfoViewState.LOADING -> {
                binding.rvEventList.visibility = View.GONE
                binding.clStateContainer.visibility = View.GONE
                binding.clLoadingContainer.visibility = View.VISIBLE
            }
            InfoViewState.ERROR ->  {
                binding.rvEventList.visibility = View.GONE
                binding.clLoadingContainer.visibility = View.GONE
                binding.clStateContainer.visibility = View.VISIBLE
                binding.tvStateTitle.text = getString(R.string.err_no_unknown_state_title)
                binding.tvStateDescription.text = getString(R.string.err_unknown_failure)
            }
        }
    }
}