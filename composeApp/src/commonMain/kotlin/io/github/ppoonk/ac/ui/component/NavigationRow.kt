package io.github.ppoonk.ac.ui.component

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


@Composable
fun NavigationRow(
    pageState: PagerState,
    destinations: List<ACDestination>,
    modifier: Modifier = Modifier

): Unit {
    val scope = rememberCoroutineScope()
    LazyRow(
        modifier = modifier
    ) {
        itemsIndexed(destinations) { index, d ->
            val selected = d == destinations[pageState.currentPage]
            val color =
                if (selected) LocalContentColor.current else MaterialTheme.colorScheme.outline
            Column(
                modifier = Modifier.width(IntrinsicSize.Max).padding(end = 16.dp)
            ) {
                TextButton(onClick = {
                    scope.launch {
                        pageState.animateScrollToPage(
                            page = index,
                            animationSpec = tween(durationMillis = 500)
                        )
                    }
                }) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        d.icon?.let { ACIconSmall(it, contentDescription = d.title, tint = color) }
                        Spacer(Modifier.width(8.dp))
                        d.title?.let { Text(it, style = MaterialTheme.typography.titleMedium) }
                    }
                }
                if (selected) {
                    ACHorizontalLine(Modifier.height(5.dp).fillMaxWidth())
                } else {
                    Spacer(Modifier.height(5.dp))
                }
            }
        }
    }
}