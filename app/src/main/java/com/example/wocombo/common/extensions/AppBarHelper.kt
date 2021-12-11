package com.example.wocombo.common.extensions

import android.annotation.SuppressLint
import androidx.appcompat.app.ActionBar

object AppBarHelper {
    @SuppressLint("RestrictedApi")
    fun setShowHideAnimationForActionBar(actionBar: ActionBar?) {
        actionBar?.setShowHideAnimationEnabled(false)
    }
}