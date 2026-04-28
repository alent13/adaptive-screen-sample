package com.example.doublescreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun AdaptiveListScreen(title: String) {
    val adaptiveInfo = currentWindowAdaptiveInfo()
    // Используем WindowWidthSizeClass из androidx.window.core.layout
    val scaffoldDirective = calculatePaneScaffoldDirective(adaptiveInfo).copy(
        maxHorizontalPartitions = if (adaptiveInfo.windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT) 1 else 2
    )
    val navigator = rememberListDetailPaneScaffoldNavigator<Int>(
        scaffoldDirective = scaffoldDirective
    )

    val isDetailOpen = navigator.currentDestination?.content != null

    BackHandler(isDetailOpen || navigator.canNavigateBack()) {
        if (isDetailOpen) {
            // В двухпанельном режиме переход к List вернет нас в состояние "ничего не выбрано"
            navigator.navigateTo(ListDetailPaneScaffoldRole.List)
        } else {
            navigator.navigateBack()
        }
    }

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                ListContent(
                    title = title,
                    onItemClick = { id ->
                        navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, id)
                    }
                )
            }
        },
        detailPane = {
            AnimatedPane {
                val selectedId = navigator.currentDestination?.content
                if (selectedId != null) {
                    DetailContent(id = selectedId)
                } else {
                    EmptyDetailContent()
                }
            }
        }
    )
}

@Composable
fun ListContent(title: String, onItemClick: (Int) -> Unit) {
    val items = (1..20).toList()
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(items) { id ->
                Text(
                    text = "Item $id from $title",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onItemClick(id) }
                        .padding(vertical = 12.dp)
                )
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun DetailContent(id: Int) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Details for Item $id",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "This is the detailed information for item $id. " +
                    "In adaptive mode, this pane is visible alongside the list. " +
                    "On phones, it takes up the full screen.",
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
fun EmptyDetailContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Select an item to see details",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
