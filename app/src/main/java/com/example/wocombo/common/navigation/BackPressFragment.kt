package com.example.wocombo.common.navigation

interface BackPressFragment {
    //True when overriding default back press action
    fun onBackPressed(): Boolean
}