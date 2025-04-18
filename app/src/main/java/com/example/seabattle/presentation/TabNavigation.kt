package com.example.seabattle.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier


@Composable
fun TabNavigation(
    modifier: Modifier = Modifier,
    tabs: List<TabItem>,
    onTabSelected: (TabItem) -> Unit,
    initialTab: TabItem
) {

    var tabIndex by remember { mutableStateOf(tabs.indexOf(initialTab)) }

    Column(modifier = modifier.fillMaxWidth()) {
        TabRow(selectedTabIndex = tabIndex) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    text = { Text(tab.title) },
                    icon = { Icon(tab.icon, contentDescription = tab.icon.name) },
                    selected = tabIndex == index,
                    onClick = {
                        tabIndex = index
                        onTabSelected(tab)
                    }
                )
            }
        }
    }
}