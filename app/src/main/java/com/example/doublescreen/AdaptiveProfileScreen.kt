package com.example.doublescreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun AdaptiveProfileScreen() {
    val adaptiveInfo = currentWindowAdaptiveInfo()
    // Используем WindowWidthSizeClass из androidx.window.core.layout
    val scaffoldDirective = calculatePaneScaffoldDirective(adaptiveInfo).copy(
        maxHorizontalPartitions = if (adaptiveInfo.windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT) 1 else 2
    )
    val navigator = rememberListDetailPaneScaffoldNavigator<String>(
        scaffoldDirective = scaffoldDirective
    )

    val isDetailOpen = navigator.currentDestination?.content != null

    BackHandler(isDetailOpen || navigator.canNavigateBack()) {
        if (isDetailOpen) {
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
                ProfileSummary(
                    onSectionClick = { section ->
                        navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, section)
                    }
                )
            }
        },
        detailPane = {
            AnimatedPane {
                val selectedSection = navigator.currentDestination?.content
                if (selectedSection != null) {
                    UserDetailContent(section = selectedSection)
                } else {
                    EmptyUserDetailContent()
                }
            }
        }
    )
}

@Composable
fun ProfileSummary(onSectionClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                tint = MaterialTheme.colorScheme.primary
            )
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text("John Doe", style = MaterialTheme.typography.headlineSmall)
                Text("Android Developer", style = MaterialTheme.typography.bodyMedium)
            }
        }

        HorizontalDivider()

        ProfileMenuItem("Personal Information", onClick = { onSectionClick("Personal Info") })
        ProfileMenuItem("Settings", onClick = { onSectionClick("Settings") })
        ProfileMenuItem("Security", onClick = { onSectionClick("Security") })
        ProfileMenuItem("Privacy Policy", onClick = { onSectionClick("Privacy") })
    }
}

@Composable
fun ProfileMenuItem(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 16.dp),
        style = MaterialTheme.typography.bodyLarge
    )
    HorizontalDivider()
}

@Composable
fun UserDetailContent(section: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = section,
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Detailed information about $section goes here. " +
                    "This view appears on the right in expanded mode and as a separate screen on compact devices.",
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
fun EmptyUserDetailContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Select a section to see more details about the user",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
