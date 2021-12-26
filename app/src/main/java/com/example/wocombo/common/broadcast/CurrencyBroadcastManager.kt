package com.example.wocombo.common.broadcast

import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.wocombo.core.domain.models.Currency
import org.koin.core.component.KoinComponent

class CurrencyBroadcastManager: KoinComponent {

    private lateinit var broadcaster: LocalBroadcastManager

    fun assignCurrencies(currencies: List<Currency>, context: Context) {
        broadcaster = LocalBroadcastManager.getInstance(context)
        val intent =
            Intent(BroadcastConst.CURRENCIES_INTENT).apply {
                putParcelableArrayListExtra(BroadcastConst.CURRENCIES, ArrayList(currencies))
            }
        broadcaster.sendBroadcast(intent)
    }
}