package com.example.wocombo.core.presentation.ui.transmissionlist.currencies.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wocombo.R
import com.example.wocombo.core.domain.enums.Indicator
import com.example.wocombo.core.domain.models.Currency
import com.example.wocombo.databinding.AdapterCurrencyListItemBinding
import java.math.RoundingMode
import java.util.ArrayList

class CurrencyListAdapter :
    ListAdapter<Currency, CurrencyListAdapter.CurrencyListViewHolder>(CurrencyDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyListViewHolder {
        val binding = AdapterCurrencyListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CurrencyListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrencyListViewHolder, position: Int) {
        val item = getItem(position)
        setNamesText(holder, item)
        setCurrencyPrice(holder, item)
        setPercentage(holder, item)
        setIndicator(holder, item)
        setImage(holder, item)
    }

    private fun setIndicator(holder: CurrencyListViewHolder, item: Currency) {
        when (item.indicator) {
            Indicator.INCREASE -> {
                holder.binding.ivIndicatorImage.setImageDrawable(
                    ContextCompat.getDrawable(
                        holder.itemView.context,
                        R.drawable.ic_baseline_trending_up_24
                    )
                )
            }
            Indicator.DECREASE -> {
                holder.binding.ivIndicatorImage.setImageDrawable(
                    ContextCompat.getDrawable(
                        holder.itemView.context,
                        R.drawable.ic_baseline_trending_down_24
                    )
                )
            }
            Indicator.EQUALS -> {
                holder.binding.ivIndicatorImage.setImageDrawable(
                    ContextCompat.getDrawable(
                        holder.itemView.context,
                        R.drawable.ic_baseline_trending_flat_24
                    )
                )
            }
            null -> {
                holder.binding.ivIndicatorImage.setImageDrawable(
                    ContextCompat.getDrawable(
                        holder.itemView.context,
                        R.drawable.ic_baseline_trending_flat_24
                    )
                )
            }
        }
    }

    private fun setCurrencyPrice(holder: CurrencyListViewHolder, item: Currency) {
        holder.binding.tvCurrencyValue.text = "${item.priceUsd.toBigDecimal().toPlainString()}$"
        holder.binding.tvCurrencyVolume.text =
            item.volume.toBigDecimal().setScale(4, RoundingMode.HALF_EVEN).toPlainString()
    }

    private fun setPercentage(holder: CurrencyListViewHolder, item: Currency) {
        when {
            item.percentHour > 0.0 -> setColorForText(
                holder.binding.tvCurrencyPercentage1H,
                R.color.colorIncrease
            )
            item.percentHour < 0.0 -> setColorForText(
                holder.binding.tvCurrencyPercentage1H,
                R.color.colorDecrease
            )
            item.percentHour == 0.0 -> setColorForText(
                holder.binding.tvCurrencyPercentage1H,
                R.color.colorTextNoActive
            )
        }

        when {
            item.percentDay > 0.0 -> setColorForText(
                holder.binding.tvCurrencyPercentage24H,
                R.color.colorIncrease
            )
            item.percentDay < 0.0 -> setColorForText(
                holder.binding.tvCurrencyPercentage24H,
                R.color.colorDecrease
            )
            item.percentDay == 0.0 -> setColorForText(
                holder.binding.tvCurrencyPercentage24H,
                R.color.colorTextNoActive
            )
        }

        holder.binding.tvCurrencyPercentage1H.text = "${item.percentHour}%"
        holder.binding.tvCurrencyPercentage24H.text = "${item.percentDay}%"
    }

    private fun setNamesText(holder: CurrencyListViewHolder, item: Currency) {
        holder.binding.tvCurrencyName.text = item.symbol
        holder.binding.tvSubtitle.text = item.name
    }

    private fun setImage(holder: CurrencyListViewHolder, item: Currency) {

        val icon = when (item.nameId) {
            "litecoin" -> ContextCompat.getDrawable(
                holder.itemView.context,
                R.drawable.ic_litecoin_ltc_logo
            )
            "uniswap" -> ContextCompat.getDrawable(
                holder.itemView.context,
                R.drawable.ic_uniswap_uni_logo
            )
            "bitcoin" -> ContextCompat.getDrawable(
                holder.itemView.context,
                R.drawable.ic_bitcoin_btc_logo
            )
            "ethereum" -> ContextCompat.getDrawable(
                holder.itemView.context,
                R.drawable.ic_ethereum_eth_logo
            )
            "binance-coin" -> ContextCompat.getDrawable(
                holder.itemView.context,
                R.drawable.ic_binance_coin_bnb_logo
            )
            "tether" -> ContextCompat.getDrawable(
                holder.itemView.context,
                R.drawable.ic_tether_usdt_logo
            )
            "solana" -> ContextCompat.getDrawable(
                holder.itemView.context,
                R.drawable.ic_solana_sol_logo
            )
            "cardano" -> ContextCompat.getDrawable(
                holder.itemView.context,
                R.drawable.ic_cardano_ada_logo
            )
            "usd-coin" -> ContextCompat.getDrawable(
                holder.itemView.context,
                R.drawable.ic_usd_coin_usdc_logo
            )
            "ripple" -> ContextCompat.getDrawable(
                holder.itemView.context,
                R.drawable.ic_xrp_xrp_logo
            )
            "terra-luna" -> ContextCompat.getDrawable(
                holder.itemView.context,
                R.drawable.ic_terra_luna_luna_logo
            )
            "avalanche" -> ContextCompat.getDrawable(
                holder.itemView.context,
                R.drawable.ic_avalanche_avax_logo
            )
            "dogecoin" -> ContextCompat.getDrawable(
                holder.itemView.context,
                R.drawable.ic_dogecoin_doge_logo
            )
            "polkadot" -> ContextCompat.getDrawable(
                holder.itemView.context,
                R.drawable.ic_polkadot_new_dot_logo
            )
            "shiba-inu" -> ContextCompat.getDrawable(
                holder.itemView.context,
                R.drawable.ic_shiba_inu_shib_logo
            )
            "binance-usd" -> ContextCompat.getDrawable(
                holder.itemView.context,
                R.drawable.ic_binance_usd_busd_logo
            )
            "wrapped-bitcoin" -> ContextCompat.getDrawable(
                holder.itemView.context,
                R.drawable.ic_wrapped_bitcoin_wbtc_logo
            )
            "chainlink" -> ContextCompat.getDrawable(
                holder.itemView.context,
                R.drawable.ic_chainlink_link_logo
            )
            "algorand" -> ContextCompat.getDrawable(
                holder.itemView.context,
                R.drawable.ic_algorand_algo_logo
            )
            "bitcoin-cash" -> ContextCompat.getDrawable(
                holder.itemView.context,
                R.drawable.ic_bitcoin_cash_bch_logo
            )
            else -> ContextCompat.getDrawable(holder.itemView.context, R.drawable.ic_ark_ark_logo)
        }

        Glide
            .with(holder.itemView.context)
            .load(icon)
            .centerInside()
            .placeholder(R.drawable.ic_baseline_downloading_24)
            .into(holder.binding.ivCurrencyImage)
    }

    private fun setColorForText(textView: TextView, color: Int) =
        textView.setTextColor(
            ContextCompat.getColor(textView.context, color)
        )


    override fun submitList(list: List<Currency>?) {
        super.submitList(list?.let { ArrayList(it) })
    }

    class CurrencyListViewHolder(val binding: AdapterCurrencyListItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}