package com.example.wocombo.common.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.wocombo.core.domain.models.Currency

class CurrencyReceiver (
    private val setUnreadNotificationCountAction: (ArrayList<Currency>) -> Unit
) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val currencies: List<Currency> =
            intent.getParcelableArrayListExtra(BroadcastConst.CURRENCIES) ?: emptyList()
        setUnreadNotificationCountAction.invoke(ArrayList(currencies))
    }
}