package com.example.wocombo.core.presentation.helpers

import android.view.MenuItem

data class MenuItemsContainer(
    var sortActionBarMenuItem: MenuItem? = null,
    var filtersActionBarMenuItem: MenuItem? = null,
    var notificationMenuItem: MenuItem? = null,
    var previousSelectedSortMenuItem: MenuItem? = null
)
