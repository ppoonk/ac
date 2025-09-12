package io.github.ppoonk.ac.ui.component

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.InfoCircle
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


/**
 * 全局提示
 */
@Composable
fun GlobalTip(
    snackbarHostState: SnackbarHostState,
    globalTipViewModel: GlobalTipViewModel
) {
    val scope = rememberCoroutineScope()
    val errorDialogWidget by globalTipViewModel.errorDialogWidget.collectAsState()
    val snackbarWidget by globalTipViewModel.snackbarWidget.collectAsState()
    val loadingWidget by globalTipViewModel.loadingWidget.collectAsState()


    if (errorDialogWidget.show) {
        ACErrorDialog(
            icon = { ACIcon(FontAwesomeIcons.Solid.InfoCircle, null) },
            title = { Text(errorDialogWidget.errorDialogTitle ?: "") },
            text = { Text(errorDialogWidget.errorDialogText ?: "") },
            onDismissRequest = { errorDialogWidget.errorDialogDismissRequest() },
            confirmButton = errorDialogWidget.errorDialogConfirmButton,
        )
    }

    LaunchedEffect(snackbarWidget.show) {
        if (snackbarWidget.show) {
            scope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = snackbarWidget.snackbarMessage ?: "",
                    actionLabel = snackbarWidget.snackbarActionLabel,
                    withDismissAction = true
                )
                when (result) {
                    SnackbarResult.ActionPerformed -> {
                        snackbarWidget.snackbarAction
                    }

                    SnackbarResult.Dismissed -> {

                    }
                }
                snackbarWidget.closeSnackbar()
            }
        }
    }

    if (loadingWidget.show) {
        ACLoadingDialog()
    }
}

class GlobalTipViewModel : ViewModel() {

    private val _errorDialogWidget = MutableStateFlow(ErrorDialogWidget())
    val errorDialogWidget: StateFlow<ErrorDialogWidget> = _errorDialogWidget

    private val _snackbarWidget = MutableStateFlow(SnackbarWidget())
    val snackbarWidget: StateFlow<SnackbarWidget> = _snackbarWidget

    private val _loadingWidget = MutableStateFlow(LoadingWidget())
    val loadingWidget: StateFlow<LoadingWidget> = _loadingWidget

    fun openErrorDialog(
        errorDialogTitle: String?,
        errorDialogText: String?,
        confirmButton: @Composable () -> Unit = {}
    ): Unit {
//        closeSnackbar()
//        closeLoading()
        _errorDialogWidget.value = _errorDialogWidget.value.copy(
            show = true,
            errorDialogTitle = errorDialogTitle,
            errorDialogText = errorDialogText,
            errorDialogDismissRequest = {
                closeErrorDialog()
            },
            errorDialogConfirmButton = confirmButton,
        )
    }

    fun closeErrorDialog(): Unit {
        _errorDialogWidget.value = _errorDialogWidget.value.copy(
            show = false,
            errorDialogTitle = null,
            errorDialogText = null,
            errorDialogDismissRequest = {},
            errorDialogConfirmButton = {}
        )
    }

    fun openSnackbar(
        snackbarMessage: String,
        snackbarActionLabel: String? = null,
        snackbarAction: () -> Unit = {},
    ): Unit {
//        closeErrorDialog()
//        closeLoading()
        _snackbarWidget.value = _snackbarWidget.value.copy(
            show = true,
            snackbarMessage = snackbarMessage,
            snackbarActionLabel = snackbarActionLabel,
            snackbarAction = snackbarAction,
            closeSnackbar = {
                closeSnackbar()
            },
        )
    }

    fun closeSnackbar(): Unit {
        _snackbarWidget.value = _snackbarWidget.value.copy(
            show = false,
            snackbarMessage = null,
            snackbarActionLabel = null,
            snackbarAction = {},
            closeSnackbar = {}
        )
    }

    fun openLoading(): Unit {
//        closeErrorDialog()
//        closeSnackbar()
        _loadingWidget.value = _loadingWidget.value.copy(
            show = true
        )
    }

    fun closeLoading(): Unit {
        viewModelScope.launch {
            delay(200) // 延时，避免开启与关闭间隔时间太短出现闪屏
            _loadingWidget.value = _loadingWidget.value.copy(
                show = false
            )
        }
    }
}

data class ErrorDialogWidget(
    val show: Boolean = false,
    val errorDialogTitle: String? = null,
    val errorDialogText: String? = null,
    val errorDialogDismissRequest: () -> Unit = {},
    val errorDialogConfirmButton: @Composable () -> Unit = {},
)

data class SnackbarWidget(
    val show: Boolean = false,
    val snackbarMessage: String? = null,
    val snackbarAction: () -> Unit = {},
    val snackbarActionLabel: String? = null,
    val closeSnackbar: () -> Unit = {},
)

data class LoadingWidget(
    val show: Boolean = false,
)