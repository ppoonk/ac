package io.github.ppoonk.ac.ui.component

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class ThemeManager(defaultDarkTheme: Boolean = false) {
    var isDarkTheme by mutableStateOf(defaultDarkTheme)
        private set

    fun toggleTheme() {
        isDarkTheme = !isDarkTheme
    }
}