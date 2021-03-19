package com.mathewsachin.fategrandautomata.ui.prefs

import androidx.compose.material.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.mathewsachin.fategrandautomata.prefs.core.Pref
import com.mathewsachin.fategrandautomata.ui.VectorIcon

@Composable
fun Pref<Boolean>.SwitchPreference(
    title: String,
    summary: String = "",
    singleLineTitle: Boolean = true,
    icon: VectorIcon? = null,
    enabled: Boolean = true,
    hint: String = ""
) {
    val onClicked: (Boolean) -> Unit = { set(it) }
    val state by collect()

    Preference(
        title = title,
        summary = summary,
        singleLineTitle = singleLineTitle,
        icon = icon,
        enabled = enabled,
        onClick = { onClicked(!state) },
        hint = hint
    ) { modifier ->
        Switch(
            checked = state,
            onCheckedChange = { onClicked(it) },
            enabled = enabled,
            modifier = modifier
        )
    }
}