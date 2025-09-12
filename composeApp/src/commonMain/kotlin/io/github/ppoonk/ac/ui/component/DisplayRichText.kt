package io.github.ppoonk.ac.ui.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.OutlinedRichTextEditor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ACDisplayRichText(
    type: ACDrawerRichTextEditorType = ACDrawerRichTextEditorType.Html,
    text: String,
    richTextState: RichTextState = rememberRichTextState(),
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(text) {
        if (type == ACDrawerRichTextEditorType.Markdown) {
            richTextState.setMarkdown(text)
        } else {
            richTextState.setHtml(text)
        }
    }

    OutlinedRichTextEditor(
        modifier = modifier,
        state = richTextState,
        readOnly = true,
        textStyle = LocalTextStyle.current.copy(fontFamily = FontFamily.Monospace),
    )
}