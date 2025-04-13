package org.chorus.form.response

/**
 * The response of a [org.chorus.form.element.custom.ElementDropdown] or [org.chorus.form.element.custom.ElementStepSlider]
 */
data class ElementResponse(
    /**
     * The ordinal of the selected option or step
     */
    val elementId: Int = 0,

    /**
     * The text of the selected option or step
     */
    val elementText: String? = null,
)
