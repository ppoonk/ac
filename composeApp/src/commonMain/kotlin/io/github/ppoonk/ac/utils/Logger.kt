package io.github.ppoonk.ac.utils

import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.Logger  as KermitLogger
import co.touchlab.kermit.Severity
import co.touchlab.kermit.platformLogWriter
import kotlinx.datetime.Clock

object Logger {
    private val kermitLogger = KermitLogger

    private var enable: Boolean = false
    fun enable(v: Boolean): Unit {
        enable = v
    }

    fun setMinSeverity(min: String): Unit {
        kermitLogger.mutableConfig.minSeverity = when (min) {
            "Info", "info" -> Severity.Info
            "Error", "error" -> Severity.Error
            "Debug", "debug" -> Severity.Debug
            else -> Severity.Error
        }
    }

    fun displayLog(display: Boolean, callback: (String) -> Unit = {}): Unit {
        enable = display
        if (display) {
            kermitLogger.addLogWriter(object : LogWriter() {
                override fun log(
                    severity: Severity,
                    message: String,
                    tag: String,
                    throwable: Throwable?
                ) {
                    callback("[$severity][${Clock.System.now()}][$tag]: $message")
                }
            })
        } else {
            kermitLogger.setLogWriters(platformLogWriter())
        }

    }

    fun info(tag: String = "", throwable: Throwable? = null, message: () -> String): Unit {
        if (enable) {
            kermitLogger.i(tag, throwable, message)
        }
    }

    fun debug(tag: String = "", throwable: Throwable? = null, message: () -> String): Unit {
        if (enable) {
            kermitLogger.d(tag, throwable, message)
        }
    }

    fun error(tag: String = "", throwable: Throwable? = null, message: () -> String): Unit {
        if (enable) {
            kermitLogger.e(tag, throwable, message)
        }
    }

    // tag
    const val DATASTORE = "DATASTORE"
    const val PAGING = "PAGING"
    const val API_HTTP_CLIENT = "API_HTTP_CLIENT"
    const val JSON = "JSON"
    const val COMPOSE_UI = "COMPOSE_UI"
    const val VIEW_MODEL = "VIEW_MODEL"
}
