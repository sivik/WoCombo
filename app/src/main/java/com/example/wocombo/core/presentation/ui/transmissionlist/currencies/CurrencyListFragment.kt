package com.example.wocombo.core.presentation.ui.transmissionlist.currencies

import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wocombo.R
import com.example.wocombo.app.services.DownloadCurrenciesService
import com.example.wocombo.app.services.ServiceManager
import com.example.wocombo.common.broadcast.BroadcastConst
import com.example.wocombo.common.broadcast.CurrencyReceiver
import com.example.wocombo.common.extensions.viewInflateBinding
import com.example.wocombo.common.functional.Failure
import com.example.wocombo.common.functional.observe
import com.example.wocombo.common.navigation.BaseNavigation
import com.example.wocombo.core.domain.errors.CommunicationsFailures
import com.example.wocombo.core.domain.errors.CurrencyFailures
import com.example.wocombo.core.domain.errors.ScheduleFailures
import com.example.wocombo.core.domain.models.Currency
import com.example.wocombo.core.domain.usecases.DownloadCurrenciesUseCase
import com.example.wocombo.core.presentation.enums.InfoViewState
import com.example.wocombo.core.presentation.enums.SortType
import com.example.wocombo.core.presentation.ui.transmissionlist.TransmissionListViewModel
import com.example.wocombo.core.presentation.ui.transmissionlist.currencies.adapter.CurrencyListAdapter
import com.example.wocombo.databinding.FragmentCurrencyListBinding
import de.mateware.snacky.Snacky
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CurrencyListFragment : Fragment() {

    private val binding by viewInflateBinding(FragmentCurrencyListBinding::inflate)
    private val vm: CurrencyListViewModel by viewModel()
    private val navigation: BaseNavigation by inject()
    private val parentVm by sharedViewModel<TransmissionListViewModel>()

    private val currencyBroadcastReceiver = CurrencyReceiver { currencies ->
        val sortedList = when (parentVm.sortLiveData.value) {
            SortType.ASCENDING -> currencies.sortedBy { it.name }
            SortType.DESCENDING -> currencies.sortedByDescending { it.name }
            SortType.ASCENDING_VOLUME -> currencies.sortedBy { it.volume }
            SortType.DESCENDING_VOLUME -> currencies.sortedByDescending { it.volume }
            SortType.ASCENDING_PERCENT -> currencies.sortedBy { it.percentDay }
            SortType.DESCENDING_PERCENT -> currencies.sortedByDescending { it.percentDay }
            else -> currencies.sortedBy { it.name }
        }
        updateCurrencyList(sortedList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        observe(vm.currencyLiveData, ::handleCurrencyListDownload)
        observe(parentVm.sortLiveData, ::handleSortScheduleList)
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
        downloadCurrencies()
    }

    override fun onResume() {
        val filter = IntentFilter(BroadcastConst.CURRENCIES_INTENT)
        LocalBroadcastManager.getInstance(requireActivity())
            .registerReceiver(currencyBroadcastReceiver, filter)
        ServiceManager.startDownloadService(
            requireActivity().applicationContext,
            navigation,
            DownloadCurrenciesService::class
        )
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(requireActivity())
            .unregisterReceiver(currencyBroadcastReceiver)
    }

    private fun downloadCurrencies() {
        showCurrencyViewState(InfoViewState.LOADING)
        vm.downloadCurrencies(parentVm.sortLiveData.value ?: SortType.ASCENDING)
    }

    private fun initAdapter() {
        binding.rvCurrencyList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = CurrencyListAdapter()
        }
    }

    private fun initSwipeListener() {
        binding.srlCurrencyList.setOnRefreshListener {
            showCurrencyViewState(InfoViewState.LOADING)
            binding.srlCurrencyList.isRefreshing = true
            downloadCurrencies()
        }
    }

    private fun handleCurrencyListDownload(result: DownloadCurrenciesUseCase.Result?) {
        result?.let { response ->
            binding.srlCurrencyList.isRefreshing = false
            response.currencies?.let { currencyList ->
                showCurrencyViewState(InfoViewState.SHOW_ELEMENTS)
                updateCurrencyList(currencyList)
            }

            response.failure?.let { failure ->
                val error = when (failure) {

                    is CommunicationsFailures.ConnectionFailure -> {
                        showCurrencyViewState(InfoViewState.ERROR)
                        getString(R.string.err_timeout_failure)
                    }

                    is CommunicationsFailures.NoInternetFailure -> {
                        showCurrencyViewState(InfoViewState.NO_INTERNET)
                        getString(R.string.err_no_internet_failure)
                    }

                    in listOf(
                        CommunicationsFailures.InternalServerFailure,
                        CurrencyFailures.DownloadCurrenciesFailure
                    ) -> {
                        showCurrencyViewState(InfoViewState.ERROR)
                        getString(R.string.err_internal_server_failure)
                    }

                    is Failure.UnknownFailure -> {
                        showCurrencyViewState(InfoViewState.ERROR)
                        getString(R.string.err_unknown_failure)
                    }
                    else -> {
                        showCurrencyViewState(InfoViewState.ERROR)
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


    private fun handleSortScheduleList(sortType: SortType?) {
        sortType?.let {
            showCurrencyViewState(InfoViewState.LOADING)
            vm.downloadCurrencies(it)
        }
    }

    private fun updateCurrencyList(schedules: List<Currency>) =
        (binding.rvCurrencyList.adapter as? CurrencyListAdapter)?.submitList(schedules)

    private fun showCurrencyViewState(viewState: InfoViewState) {
        when (viewState) {
            InfoViewState.SHOW_ELEMENTS -> {
                binding.rvCurrencyList.visibility = View.VISIBLE
                binding.clStateContainer.visibility = View.GONE
                binding.clLoadingContainer.visibility = View.GONE
            }
            InfoViewState.NO_INTERNET -> {
                binding.rvCurrencyList.visibility = View.GONE
                binding.clLoadingContainer.visibility = View.GONE
                binding.clStateContainer.visibility = View.VISIBLE
                binding.ivStateIcon.setImageDrawable(
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_no_internet_conn)
                )
                binding.tvStateTitle.text = getString(R.string.err_no_internet_title)
                binding.tvStateDescription.text = getString(R.string.err_no_internet_failure)
            }
            InfoViewState.LOADING -> {
                binding.rvCurrencyList.visibility = View.GONE
                binding.clStateContainer.visibility = View.GONE
                binding.clLoadingContainer.visibility = View.VISIBLE
            }
            InfoViewState.ERROR -> {
                binding.rvCurrencyList.visibility = View.GONE
                binding.clLoadingContainer.visibility = View.GONE
                binding.clStateContainer.visibility = View.VISIBLE
                binding.ivStateIcon.setImageDrawable(
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_error)
                )
                binding.tvStateTitle.text = getString(R.string.err_no_unknown_state_title)
                binding.tvStateDescription.text = getString(R.string.err_unknown_failure)
            }
            InfoViewState.NO_ELEMENTS -> throw IllegalStateException("No elements in schedule no handled")
        }
    }
}