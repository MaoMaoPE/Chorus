package org.chorus.utils.exception

import com.dfsek.terra.lib.commons.lang3.exception.ExceptionUtils
import kotlin.math.max
import kotlin.math.min


/**
 * Custom runtime exceptions that support formatting strings
 */
open class FormativeRuntimeException : RuntimeException {
    private var indices: IntArray
    private var usedCount = 0
    override var message: String? = null

    @Transient
    private var throwable: Throwable? = null

    constructor() : super()

    constructor(message: String?) {
        this.message = message
    }

    constructor(cause: Throwable?) {
        this.throwable = cause
        this.message = ExceptionUtils.getStackTrace(throwable)
    }

    constructor(format: String?, vararg arguments: Any?) {
        init(format, *arguments)
        fillInStackTrace()
        this.message = formatMessage(format, *arguments)
        if (throwable != null) {
            this.message += System.lineSeparator() + ExceptionUtils.getStackTrace(throwable)
        }
    }

    override fun getMessage(): String {
        return message!!
    }

    override fun toString(): String {
        val s = javaClass.name
        val message = message
        return if (message != null) ("$s: $message") else s
    }

    private fun init(format: String?, vararg arguments: Any) {
        val len = max(1.0, (if (format == null) 0 else format.length shr 1).toDouble()).toInt() // divide by 2
        this.indices = IntArray(len) // LOG4J2-1542 ensure non-zero array length
        val placeholders = ParameterFormatter.countArgumentPlaceholders2(format, indices)
        initThrowable(arguments, placeholders)
        this.usedCount = min(placeholders.toDouble(), arguments.size.toDouble()).toInt()
    }

    private fun initThrowable(params: Array<Any>?, usedParams: Int) {
        if (params != null) {
            val argCount = params.size
            if (usedParams < argCount && this.throwable == null && params[argCount - 1] is Throwable) {
                this.throwable = params[argCount - 1] as Throwable
            }
        }
    }

    private fun formatMessage(format: String?, vararg arguments: Any): String {
        val stringBuilder = StringBuilder()
        if (indices[0] < 0) {
            ParameterFormatter.formatMessage(stringBuilder, format, arguments, usedCount)
        } else {
            ParameterFormatter.formatMessage2(stringBuilder, format, arguments, usedCount, indices)
        }
        return stringBuilder.toString()
    }
}
