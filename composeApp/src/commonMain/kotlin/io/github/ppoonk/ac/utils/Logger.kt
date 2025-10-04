package io.github.ppoonk.ac.utils

import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.Logger  as KermitLogger
import co.touchlab.kermit.Severity
import co.touchlab.kermit.platformLogWriter
import kotlinx.datetime.Clock

object Logger {
    private val kermitLogger = KermitLogger

    var enable: Boolean = false
    var logWriterForDisplay: Boolean = false

    fun setMinSeverity(min: String): Unit {
        kermitLogger.mutableConfig.minSeverity = when (min) {
            "Error", "error" -> Severity.Error
            "Debug", "debug" -> Severity.Debug
            else -> Severity.Error
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

    fun addLogWriterForDisplay(callback: (String) -> Unit = {}): Unit {
        logWriterForDisplay = true
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
    }

    fun defaultLogWriter(): Unit {
        logWriterForDisplay = false
        kermitLogger.setLogWriters(platformLogWriter())
    }

    // tag
    const val DATASTORE = "DATASTORE"
    const val PAGING = "PAGING"
    const val API_HTTP_CLIENT = "API_HTTP_CLIENT"
    const val JSON = "JSON"
    const val COMPOSE_UI = "COMPOSE_UI"
    const val VIEW_MODEL = "VIEW_MODEL"
}
