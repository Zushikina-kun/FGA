package com.mathewsachin.fategrandautomata.ui.fine_tune

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mathewsachin.fategrandautomata.R
import com.mathewsachin.fategrandautomata.prefs.core.PrefsCore
import com.mathewsachin.fategrandautomata.ui.FgaScreen
import com.mathewsachin.fategrandautomata.ui.GroupSelectorItem
import com.mathewsachin.fategrandautomata.ui.Heading
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FineTuneSettingsFragment : Fragment() {
    @Inject
    lateinit var prefs: PrefsCore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        ComposeView(requireContext()).apply {
            setContent {
                val vm: FineTuneSettingsViewModel = viewModel()

                FgaScreen {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        var selectedIndex by rememberSaveable { mutableStateOf(0) }

                        Heading(stringResource(R.string.p_fine_tune)) {
                            itemsIndexed(vm.groups) { index, it ->
                                GroupSelectorItem(
                                    item = stringResource(it.name),
                                    isSelected = selectedIndex == index,
                                    onSelect = { selectedIndex = index }
                                )
                            }
                        }

                        Divider()

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(top = 16.dp)
                        ) {
                            val selectedGroup = vm.groups[selectedIndex]

                            LazyColumn(
                                contentPadding = PaddingValues(bottom = 90.dp)
                            ) {
                                items(selectedGroup.items) {
                                    Card(
                                        modifier = Modifier
                                            .padding(horizontal = 16.dp)
                                            .padding(bottom = 16.dp)
                                    ) {
                                        it.FineTuneSetter()
                                    }
                                }
                            }

                            ExtendedFloatingActionButton(
                                text = {
                                    Text(
                                        stringResource(R.string.fine_tune_menu_reset_to_defaults),
                                        color = MaterialTheme.colors.onSecondary
                                    )
                                },
                                onClick = { vm.resetAll() },
                                icon = {
                                    Icon(
                                        painterResource(R.drawable.ic_refresh),
                                        contentDescription = stringResource(R.string.fine_tune_menu_reset_to_defaults),
                                        tint = MaterialTheme.colors.onSecondary
                                    )
                                },
                                backgroundColor = MaterialTheme.colors.secondary,
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
}