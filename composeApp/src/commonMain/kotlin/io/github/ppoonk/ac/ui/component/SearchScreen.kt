package io.github.ppoonk.ac.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ACSearchScreen(
    value: String,
) {
    Scaffold(
        topBar = {
            ACSearchScreenTopBar(
                value
            )
        }
    ) {
        Column(
            modifier = Modifier.padding(it).padding(horizontal = 16.dp)
        ) {
            ACSearchScreenContent()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ACSearchScreenTopBar(
    value: String,
) {
    var expanded by remember { mutableStateOf(false) }
    ACTopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ACTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = value,
                    onValueChange = {},
                    prefix = {
                        Box {
                            Text(
                                "选择类型",
                                modifier = Modifier.clickable {
                                    expanded = !expanded
                                })

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = {
                                    expanded = !expanded
                                }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("ID") },
                                    onClick = {
                                        expanded = !expanded
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Email") },
                                    onClick = {
                                        expanded = !expanded
                                    }
                                )
                            }
                        }
                    },
                )
            }
        },
    )
}

@Composable
fun ACSearchScreenContent() {
    Text("历史")
}

