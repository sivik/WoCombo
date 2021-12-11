package com.example.wocombo.common.extensions

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.core.text.bold
import com.example.wocombo.R
import com.example.wocombo.common.date.DateTimePatterns
import org.joda.time.DateTime
import org.joda.time.Period

fun TextView.getSpannableDateWithPeriodText(
    prefix: Int,
    dateTime: DateTime
): SpannableStringBuilder {

    val dateText = context.getString(
        prefix,
        dateTime.toString(DateTimePatterns.SHORT_SLASH)
    )

    val fcs = ForegroundColorSpan(Color.rgb(158, 0, 0))
    val period = Period(DateTime.now(), dateTime)

    val shortPeriodDays = 0..5

    val remainingText = try {
        when (val days = period.toStandardDays().days) {
            0 -> when (val hours = period.toStandardHours().hours) {
                0 -> {
                    val minutes = period.toStandardMinutes().minutes
                    if (minutes > 0) {
                        context.getString(R.string.remaining_more_min, minutes)
                    } else {
                        ""
                    }
                }
                1 -> context.getString(R.string.remaining_one_hour)
                in 2..Int.MAX_VALUE -> context.getString(
                    R.string.remaining_more_hours,
                    hours
                )
                else -> ""
            }
            1 -> context.getString(R.string.remaining_one_day)
            in 2..Int.MAX_VALUE -> context.getString(
                R.string.remaining_more_days,
                days
            )
            else -> ""
        }
    } catch (e: Exception) {
        return if (dateTime.isBeforeNow) {
            SpannableStringBuilder()
                .append(dateText)
        } else {
            SpannableStringBuilder().append(dateText).append(" (30+ days)")
        }
    }

    return SpannableStringBuilder()
        .append(dateText)
        .bold {
            when (period.toStandardDays().days) {
                in shortPeriodDays -> {
                    append(remainingText)
                    setSpan(
                        fcs,
                        dateText.length,
                        dateText.length + remainingText.length,
                        Spannable.SPAN_INCLUSIVE_INCLUSIVE
                    )
                }
                in shortPeriodDays.last..Int.MAX_VALUE -> {
                    append(remainingText)
                }
            }
        }
}