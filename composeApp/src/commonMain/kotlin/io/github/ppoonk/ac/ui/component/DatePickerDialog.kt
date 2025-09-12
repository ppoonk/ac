package io.github.ppoonk.ac.ui.component

import io.github.ppoonk.ac.utils.TimeUtils
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerDefaults.DateRangePickerHeadline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.Instant

@Composable
fun ACDateRangePickerDisplayRow(
    modifier: Modifier = Modifier,
    date: Pair<Instant?, Instant?>,
    onClick: () -> Unit
): Unit {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.clickable { onClick() }
    ) {
        ACLabelInfo(
            if (date.first != null) TimeUtils.instantToLocalDateString(
                date.first!!
            ) else "",
            lineHeight = 32.sp,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.widthIn(min = 120.dp).weight(6f),
            textAlign = TextAlign.Center,
            maxLines = 1
        )
        Spacer(Modifier.weight(1f))
        ACLabelInfo(
            if (date.second != null) TimeUtils.instantToLocalDateString(
                date.second!!
            ) else "",
            lineHeight = 32.sp,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.widthIn(min = 120.dp).weight(6f),
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ACDateRangePickerDialog(
    expanded: Boolean,
    onDateRangeSelected: (Pair<Long?, Long?>) -> Unit = {},
    onDismiss: () -> Unit
) {
    val dateRangePickerState = rememberDateRangePickerState()

    if (expanded) {
        DatePickerDialog(
            modifier = Modifier
                .height(500.dp)
                .padding(16.dp),
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(
                    onClick = {
                        onDateRangeSelected(
                            Pair(
                                dateRangePickerState.selectedStartDateMillis,
                                dateRangePickerState.selectedEndDateMillis
                            )
                        )
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        ) {
            DateRangePicker(
                state = dateRangePickerState,
                showModeToggle = false,
                title = {},
                headline = {
                    DateRangePickerHeadline(
                        selectedStartDateMillis = dateRangePickerState.selectedStartDateMillis,
                        selectedEndDateMillis = dateRangePickerState.selectedEndDateMillis,
                        displayMode = dateRangePickerState.displayMode,
                        dateFormatter = remember { DatePickerDefaults.dateFormatter() },
                        modifier = Modifier.padding(bottom = 16.dp).height(30.dp)
                    )
                },
            )
        }
    }
}