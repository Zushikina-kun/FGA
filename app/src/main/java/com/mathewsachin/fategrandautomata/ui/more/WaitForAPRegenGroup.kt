package com.mathewsachin.fategrandautomata.ui.more

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mathewsachin.fategrandautomata.R
import com.mathewsachin.fategrandautomata.prefs.core.PrefsCore
import com.mathewsachin.fategrandautomata.ui.icon
import com.mathewsachin.fategrandautomata.ui.prefs.Preference
import com.mathewsachin.fategrandautomata.ui.prefs.PreferenceGroup
import com.mathewsachin.fategrandautomata.ui.prefs.SeekBarPreference
import com.mathewsachin.fategrandautomata.ui.prefs.remember

@Composable
fun WaitForAPRegenGroup(
    prefs: PrefsCore
) {
    PreferenceGroup(title = stringResource(R.string.p_wait_ap_regen_text)) {
        var waitEnabled by prefs.waitAPRegen.remember()

        Row {
            Checkbox(
                checked = waitEnabled,
                onCheckedChange = { waitEnabled = it },
                modifier = Modifier
                    .padding(start = 16.dp)
                    .align(Alignment.CenterVertically)
                    .size(40.dp)
            )

            Preference(
                title = stringResource(R.string.p_wait_ap_regen_text),
                hint = stringResource(R.string.p_wait_ap_regen_text_summary),
                enabled = waitEnabled
            )
        }

        if (waitEnabled) {
            prefs.waitAPRegenMinutes.SeekBarPreference(
                title = stringResource(R.string.p_wait_ap_regen_minutes_text),
                summary = stringResource(R.string.p_wait_ap_regen_minutes_text_summary),
                valueRange = 1..60,
                icon = icon(R.drawable.ic_counter),
                valueRepresentation = { "$it min" }
            )
        }
    }
}