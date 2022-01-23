package com.example.wocombo.core.presentation.ui.transmissionlist.currencies

import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wocombo.R
import com.example.wocombo.app.services.DownloadCurrenciesService
import com.example.wocombo.app.services.ServiceManager
import com.example.wocombo.common.broadcast.BroadcastConst
import com.example.wocombo.common.broadcast.CurrencyReceiver
import com.example.wocombo.common.extensions.viewInflateBinding
import com.example.wocombo.common.functional.Failure
import com.example.wocombo.common.functional.Result
import com.example.wocombo.common.functional.observe
import com.example.wocombo.common.logs.LoggerTags
import com.example.wocombo.common.navigation.BaseNavigation
import com.example.wocombo.core.domain.errors.CommunicationsFailures
import com.example.wocombo.core.domain.errors.CurrencyFailures
import com.example.wocombo.core.domain.models.Currency
import com.example.wocombo.core.domain.usecases.DownloadCurrenciesFlowUseCase
import com.example.wocombo.core.presentation.enums.InfoViewState
import com.example.wocombo.core.presentation.enums.SortType
import com.example.wocombo.core.presentation.ui.transmissionlist.TransmissionListViewModel
import com.example.wocombo.core.presentation.ui.transmissionlist.currencies.adapter.CurrencyListAdapter
import com.example.wocombo.databinding.FragmentCurrencyListBinding
import de.mateware.snacky.Snacky
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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
        observe(parentVm.sortLiveData, ::handleSortScheduleList)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initObservers()
        initAdapter()
        initSwipeListener()
        downloadCurrencies()
        super.onViewCreated(view, savedInstanceState)
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

    private fun initObservers(){
        lifecycleScope.launch {
            try {
                vm.currencies.flowWithLifecycle(
                    lifecycle = viewLifecycleOwner.lifecycle,
                    minActiveState = Lifecycle.State.RESUMED
                ).catch { e ->
                    Log.e(LoggerTags.CURRENCY, "Cannot get currencies from remote", e)
                    throw e
                }.collectLatest { result ->
                    result?.let { response ->
                       when(response){
                           is Result.Error -> handleDownloadCurrencyError(response)
                           is Result.Loading -> showCurrencyViewState(InfoViewState.LOADING)
                           is Result.Success -> handleCurrencyListDownload(response.data)
                       }
                    }
                }
            } catch (e: Exception) {
                Log.e(LoggerTags.CURRENCY, "Cannot get currencies", e)
                cancel()
            }
        }
    }

    private fun handleDownloadCurrencyError(response: Result<DownloadCurrenciesFlowUseCase.Response>) {
        val error = when (response.failure) {

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

    private fun handleCurrencyListDownload(result: DownloadCurrenciesFlowUseCase.Response?) {
        result?.let { response ->
            binding.srlCurrencyList.isRefreshing = false
            response.currencies?.let { currencyList ->
                showCurrencyViewState(InfoViewState.SHOW_ELEMENTS)
                updateCurrencyList(currencyList)
            }
        }
    }


    private fun handleSortScheduleList(sortType: SortType?) {
        sortType?.let {
            showCurrencyViewState(InfoViewState.LOADING)
            vm.downloadCurrencies(it)
        }
    }

    private fun updateCurrencyList(currencies: List<Currency>) =
        (binding.rvCurrencyList.adapter as? CurrencyListAdapter)?.submitList(currencies)

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