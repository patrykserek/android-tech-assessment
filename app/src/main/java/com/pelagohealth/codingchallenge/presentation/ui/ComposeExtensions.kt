package com.pelagohealth.codingchallenge.presentation.ui

import androidx.compose.ui.Modifier

/**
 * Source: https://patrickmichalik.com/blog/adding-modifiers-conditionally-in-jetpack-compose
 */
inline fun Modifier.thenIf(
    condition: Boolean,
    crossinline other: Modifier.() -> Modifier,
) = if (condition) other() else this
