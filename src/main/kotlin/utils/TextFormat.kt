package org.chorus_oss.chorus.utils

import com.google.common.collect.Maps
import java.util.regex.Pattern

/**
 * The special character which prefixes all format codes. Use this if
 * you need to dynamically convert format codes from your custom format.
 */
const val ESCAPE: Char = '\u00A7'

/**
 * All supported formatting values for chat and console.
 */
enum class TextFormat(
    val char: Char, private val intCode: Int,
    /**
     * Checks if this code is a format code as opposed to a color code.
     */
    val isFormat: Boolean = false
) {
    /**
     * Represents black.
     */
    BLACK('0', 0x00),

    /**
     * Represents dark blue.
     */
    DARK_BLUE('1', 0x1),

    /**
     * Represents dark green.
     */
    DARK_GREEN('2', 0x2),

    /**
     * Represents dark blue (aqua).
     */
    DARK_AQUA('3', 0x3),

    /**
     * Represents dark red.
     */
    DARK_RED('4', 0x4),

    /**
     * Represents dark purple.
     */
    DARK_PURPLE('5', 0x5),

    /**
     * Represents gold.
     */
    GOLD('6', 0x6),

    /**
     * Represents gray.
     */
    GRAY('7', 0x7),

    /**
     * Represents dark gray.
     */
    DARK_GRAY('8', 0x8),

    /**
     * Represents blue.
     */
    BLUE('9', 0x9),

    /**
     * Represents green.
     */
    GREEN('a', 0xA),

    /**
     * Represents aqua.
     */
    AQUA('b', 0xB),

    /**
     * Represents red.
     */
    RED('c', 0xC),

    /**
     * Represents light purple.
     */
    LIGHT_PURPLE('d', 0xD),

    /**
     * Represents yellow.
     */
    YELLOW('e', 0xE),

    /**
     * Represents white.
     */
    WHITE('f', 0xF),

    /**
     * Represents minecoins gold.
     */
    MINECOIN_GOLD('g', 0x16),

    /**
     * Represents material quartz.
     */
    MATERIAL_QUARTZ('h', 0x17),

    /**
     * Represents material iron.
     */
    MATERIAL_IRON('i', 0x18),

    /**
     * Represents material netherite.
     */
    MATERIAL_NETHERITE('j', 0x19),

    /**
     * Represents material redstone.
     */
    MATERIAL_REDSTONE('m', 0x20),

    /**
     * Represents material copper.
     */
    MATERIAL_COPPER('n', 0x21),

    /**
     * Represents material gold.
     */
    MATERIAL_GOLD('p', 0x22),

    /**
     * Represents material emerald.
     */
    MATERIAL_EMERALD('q', 0x23),

    /**
     * Represents material diamond.
     */
    MATERIAL_DIAMOND('s', 0x24),

    /**
     * Represents material lapis.
     */
    MATERIAL_LAPIS('t', 0x25),

    /**
     * Represents material amethyst.
     */
    MATERIAL_AMETHYST('u', 0x26),

    /**
     * Represents material resin.
     */
    MATERIAL_RESIN('v', 0x27),

    /**
     * Makes the text obfuscated.
     */
    OBFUSCATED('k', 0x10, true),

    /**
     * Makes the text bold.
     */
    BOLD('l', 0x11, true),

    /**
     * Makes the text italic.
     */
    ITALIC('o', 0x14, true),

    /**
     * Resets all previous chat colors or formats.
     */
    RESET('r', 0x15);

    companion object {
        private val CLEAN_PATTERN: Pattern = Pattern.compile("(?i)$ESCAPE[0-9A-V]")
        private val BY_ID: MutableMap<Int, TextFormat> = Maps.newTreeMap()
        private val BY_CHAR: MutableMap<Char, TextFormat> = HashMap()

        init {
            for (color in entries) {
                BY_ID[color.intCode] = color
                BY_CHAR[color.char] = color
            }
        }

        /**
         * Gets the TextFormat represented by the specified format code.
         *
         * @param code Code to check
         * @return Associative [TextFormat] with the given code,
         * or null if it doesn't exist
         */
        fun getByChar(code: Char): TextFormat? {
            return BY_CHAR[code]
        }

        /**
         * Gets the TextFormat represented by the specified format code.
         *
         * @param code Code to check
         * @return Associative [TextFormat] with the given code,
         * or null if it doesn't exist
         */
        fun getByChar(code: String?): TextFormat? {
            if (code == null || code.length <= 1) {
                return null
            }

            return BY_CHAR[code[0]]
        }

        /**
         * Cleans the given message of all format codes.
         *
         * @param input String to clean.
         * @return A copy of the input string, without any formatting.
         */
        @JvmStatic
        @JvmOverloads
        fun clean(input: String, recursive: Boolean = false): String {
            val result = CLEAN_PATTERN.matcher(input).replaceAll("")

            if (recursive && CLEAN_PATTERN.matcher(result).find()) {
                return clean(result, true)
            }
            return result
        }

        /**
         * Translates a string using an alternate format code character into a
         * string that uses the internal ESCAPE format code
         * character. The alternate format code character will only be replaced if
         * it is immediately followed by 0-9, A-G, a-g, K-O, k-o, R or r.
         *
         * @param altFormatChar   The alternate format code character to replace. Ex: &amp;
         * @param textToTranslate Text containing the alternate format code character.
         * @return Text containing the ESCAPE format code character.
         */
        fun colorize(altFormatChar: Char, textToTranslate: String): String {
            val b = textToTranslate.toCharArray()
            for (i in 0..<b.size - 1) {
                if (b[i] == altFormatChar && "0123456789AaBbCcDdEeFfGgHhIiJjMmNnPpQqSsTtUuKkLlOoRr".indexOf(b[i + 1]) > -1) {
                    b[i] = ESCAPE
                    b[i + 1] = b[i + 1].lowercaseChar()
                }
            }
            return String(b)
        }

        /**
         * Translates a string, using an ampersand (&amp;) as an alternate format code
         * character, into a string that uses the internal ESCAPE format
         * code character. The alternate format code character will only be replaced if
         * it is immediately followed by 0-9, A-G, a-g, K-O, k-o, R or r.
         *
         * @param textToTranslate Text containing the alternate format code character.
         * @return Text containing the ESCAPE format code character.
         */
        fun colorize(textToTranslate: String): String {
            return colorize('&', textToTranslate)
        }

        /**
         * Gets the chat color used at the end of the given input string.
         *
         * @param input Input string to retrieve the colors from.
         * @return Any remaining chat color to pass onto the next line.
         */
        fun getLastColors(input: String): String {
            val result = StringBuilder()
            val length = input.length

            // Search backwards from the end as it is faster
            for (index in length - 1 downTo -1 + 1) {
                val section = input[index]
                if (section == ESCAPE && index < length - 1) {
                    val c = input[index + 1]
                    val color = getByChar(c)

                    if (color != null) {
                        result.insert(0, color.toString())

                        // Once we find a color or reset we can stop searching
                        if (color.isColor || color == RESET) {
                            break
                        }
                    }
                }
            }

            return result.toString()
        }
    }

    override fun toString(): String {
        return "$ESCAPE$char"
    }

    val isColor: Boolean
        /**
         * Checks if this code is a color code as opposed to a format code.
         */
        get() = !isFormat && this != RESET
}
