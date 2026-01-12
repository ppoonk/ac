package io.github.ppoonk.ac.ui.component

import io.github.ppoonk.ac.ui.component.richText.RichTextStyleRow
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.OutlinedRichTextEditor
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.AngleLeft
import compose.icons.fontawesomeicons.solid.Save


enum class ACDrawerRichTextEditorType {
    Markdown,
    Html,
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ACRichTextEditorDrawer(
    type: ACDrawerRichTextEditorType = ACDrawerRichTextEditorType.Html,
    text: String = "",
    onSaved: (String) -> Unit = {},
    richTextState: RichTextState = rememberRichTextState(),
    expanded: Boolean = false,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
    sheetMaxWidth: Dp = BottomSheetDefaults.SheetMaxWidth,
    shape: Shape = BottomSheetDefaults.ExpandedShape,
    containerColor: Color = BottomSheetDefaults.ContainerColor,
    contentColor: Color = contentColorFor(containerColor),
    tonalElevation: Dp = 0.dp,
    scrimColor: Color = BottomSheetDefaults.ScrimColor,
//    dragHandle: @Composable() (() -> Unit)? = { BottomSheetDefaults.DragHandle() },
    dragHandle: @Composable() (() -> Unit)? = {
        ACDragHandle(
            start = {
                IconButton(onClick = {
                    onDismissRequest()
                    richTextState.clear()
                }) {
                    ACIcon(FontAwesomeIcons.Solid.AngleLeft, null)
                }
            },
            end = {
                IconButton(onClick = {
                    val exportString = if (type == ACDrawerRichTextEditorType.Markdown) {
                        richTextState.toMarkdown()
                    } else {
                        richTextState.toHtml()
                    }
                    onSaved(exportString)
                    onDismissRequest()
                    richTextState.clear()
                }) {
                    ACIcon(FontAwesomeIcons.Solid.Save, null)
                }
            }
        )
    },
    contentWindowInsets: @Composable () -> WindowInsets = { BottomSheetDefaults.windowInsets },
    properties: ModalBottomSheetProperties = ModalBottomSheetDefaults.properties
//    content: @Composable() (ColumnScope.() -> Unit)
) {
    LaunchedEffect(Unit) {
        richTextState.config.linkColor = Color(0xFF1d9bd1)
        richTextState.config.linkTextDecoration = TextDecoration.None
        richTextState.config.codeSpanColor = Color(0xFFd7882d)
        richTextState.config.codeSpanBackgroundColor = Color.Transparent
        richTextState.config.codeSpanStrokeColor = Color(0xFF494b4d)
        richTextState.config.unorderedListIndent = 38
        richTextState.config.orderedListIndent = 40
    }
    LaunchedEffect(expanded) {
        if (expanded) {
            if (type == ACDrawerRichTextEditorType.Markdown) {
                richTextState.setMarkdown(text)
            } else {
                richTextState.setHtml(text)
            }
        }
    }
    ACModalBottomSheet(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = sheetState,
        sheetMaxWidth = sheetMaxWidth,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        scrimColor = scrimColor,
        dragHandle = dragHandle,
        contentWindowInsets = contentWindowInsets,
        properties = properties,
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
        ) {

            RichTextStyleRow(
                state = richTextState,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            OutlinedRichTextEditor(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                state = richTextState,
                textStyle = LocalTextStyle.current.copy(fontFamily = FontFamily.Monospace),
            )
        }
    }
}

