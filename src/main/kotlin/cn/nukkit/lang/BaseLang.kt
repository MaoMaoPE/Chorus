package cn.nukkit.lang

import cn.nukkit.utils.JSONUtils
import com.google.gson.reflect.TypeToken
import io.netty.util.internal.EmptyArrays
import lombok.extern.slf4j.Slf4j
import java.io.*
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.util.regex.MatchResult
import java.util.regex.Pattern

/**
 * @author MagicDroidX (Nukkit Project)
 */
@Slf4j
class BaseLang @JvmOverloads constructor(lang: String, path: String? = null, fallback: String = FALLBACK_LANGUAGE) {
    /**
     * The Lang name.
     */
    protected val langName: String

    /**
     * 本地语言，从nukkit.yml中指定
     */
    var langMap: Map<String, String>? = null
        protected set

    /**
     * 备选语言映射，当从本地语言映射中无法翻译时调用备选语言映射，默认为英文
     */
    var fallbackLangMap: Map<String, String>? = HashMap()
        protected set

    //用于提取字符串中%后带有[a-zA-Z0-9_.-]这些字符的字符串的模式
    private val split: Pattern = Pattern.compile("%[A-Za-z0-9_.-]+")


    init {
        var path = path
        this.langName = lang.lowercase()
        val useFallback = lang != fallback

        if (path == null) {
            path = "language/"
            try {
                this.langMap = this.loadLang(javaClass.module.getResourceAsStream(path + this.langName + "/lang.json"))
                if (useFallback) this.fallbackLangMap = this.loadLang(
                    javaClass.module.getResourceAsStream(
                        "$path$fallback/lang.json"
                    )
                )
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        } else {
            this.langMap = this.loadLang(Path.of(path).resolve(this.langName + "/lang.json").toString())
            if (useFallback) this.fallbackLangMap = this.loadLang("$path$fallback/lang.json")
        }
        if (this.fallbackLangMap == null) this.fallbackLangMap = this.langMap
    }

    val name: String?
        get() = this.get("language.name")

    fun getLang(): String {
        return langName
    }

    protected fun loadLang(path: String): Map<String, String>? {
        try {
            val file = File(path)
            if (!file.exists() || file.isDirectory) {
                throw FileNotFoundException()
            }
            FileInputStream(file).use { stream ->
                return parseLang(BufferedReader(InputStreamReader(stream, StandardCharsets.UTF_8)))
            }
        } catch (e: IOException) {
            BaseLang.log.error("Failed to load language at {}", path, e)
            return null
        }
    }

    protected fun loadLang(stream: InputStream): Map<String, String>? {
        try {
            return parseLang(BufferedReader(InputStreamReader(stream, StandardCharsets.UTF_8)))
        } catch (e: IOException) {
            BaseLang.log.error("Failed to parse the language input stream", e)
            return null
        }
    }

    @Throws(IOException::class)
    private fun parseLang(reader: BufferedReader): Map<String, String> {
        return JSONUtils.from<Map<String, String>>(reader, object : TypeToken<Map<String?, String?>?>() {
        })
    }

    /**
     * 翻译一个文本key，key从语言文件中查询
     *
     * @param key the key
     * @return the string
     */
    fun tr(key: String): String {
        return tr(key, *EmptyArrays.EMPTY_STRINGS)
    }

    /**
     * 翻译一个文本key，key从语言文件中查询，并且按照给定参数填充结果
     *
     * @param key  the key
     * @param args the args
     * @return the string
     */
    fun tr(key: String, vararg args: String): String {
        var baseText = parseLanguageText(key)
        for (i in args.indices) {
            baseText = baseText.replace("{%$i}", parseLanguageText(args[i].toString()))
        }
        return baseText
    }

    /**
     * 翻译一个文本key，key从语言文件中查询，并且按照给定参数填充结果
     *
     *
     * Translate a text key, the key is queried from the language file and the result is populated according to the given parameters
     *
     * @param key  the key
     * @param args the args
     * @return the string
     */
    fun tr(key: String, vararg args: Any): String {
        var baseText = parseLanguageText(key)
        for (i in args.indices) {
            baseText = baseText.replace("{%$i}", parseLanguageText(parseArg(args[i])))
        }
        return baseText
    }

    fun tr(c: TextContainer): String {
        var baseText = this.parseLanguageText(c.getText())
        if (c is TranslationContainer) {
            for (i in c.parameters.indices) {
                baseText = baseText.replace("{%$i}", this.parseLanguageText(c.parameters[i]))
            }
        }
        return baseText
    }

    /**
     * 翻译一个文本key，key从语言文件中查询，并且按照给定参数填充结果
     *
     *
     * Translate a text key, the key is queried from the language file and the result is populated according to the given parameters
     *
     * @param str    the str
     * @param params the params
     * @param prefix str的前缀<br></br>Prefix of str
     * @param mode   为true，则只翻译以指定前缀的多语言文本，为false则只翻译不带有指定前缀的多语言文本<br></br>If true translate only multilingual text with the specified prefix, false translate only multilingual text without the specified prefix
     * @return the string
     */
    fun tr(str: String, params: Array<String>, prefix: String, mode: Boolean): String {
        var baseText = parseLanguageText(str, prefix, mode)
        for (i in params.indices) {
            baseText = baseText.replace("{%$i}", parseLanguageText(parseArg(params[i]), prefix, mode))
        }
        return baseText
    }

    /**
     * 获取指定id对应的多语言文本，若不存在则返回null
     *
     * @param id the id
     * @return the string
     */
    fun internalGet(id: String): String? {
        if (langMap!!.containsKey(id)) {
            return langMap!![id]
        } else if (fallbackLangMap!!.containsKey(id)) {
            return fallbackLangMap!![id]
        }
        return null
    }

    /**
     * 获取指定id对应的多语言文本，若不存在则返回id本身
     *
     * @param id the id
     * @return the string
     */
    fun get(id: String): String? {
        if (langMap!!.containsKey(id)) {
            return langMap!![id]
        } else if (fallbackLangMap!!.containsKey(id)) {
            return fallbackLangMap!![id]
        }
        return id
    }

    protected fun parseArg(arg: Any): String {
        return when (arg.javaClass.simpleName) {
            "int[]" -> {
                (arg as IntArray).contentToString()
            }

            "double[]" -> {
                (arg as DoubleArray).contentToString()
            }

            "float[]" -> {
                (arg as FloatArray).contentToString()
            }

            "short[]" -> {
                (arg as ShortArray).contentToString()
            }

            "byte[]" -> {
                (arg as ByteArray).contentToString()
            }

            "long[]" -> {
                (arg as LongArray).contentToString()
            }

            "boolean[]" -> {
                (arg as BooleanArray).contentToString()
            }

            else -> {
                arg.toString()
            }
        }
    }

    protected fun parseLanguageText(str: String): String {
        val result = internalGet(str)
        if (result != null) {
            return result
        } else {
            val matcher = split.matcher(str)
            return matcher.replaceAll { m: MatchResult ->
                this.get(
                    m.group().substring(1)
                )
            }
        }
    }

    protected fun parseLanguageText(str: String, prefix: String, mode: Boolean): String {
        if (mode && !str.startsWith(prefix)) {
            return str
        }
        if (!mode && str.startsWith(prefix)) {
            return str
        }
        val result = internalGet(str)
        if (result != null) {
            return result
        } else {
            val matcher = split.matcher(str)
            return matcher.replaceAll { m: MatchResult ->
                val s = m.group().substring(1)
                if (mode) {
                    if (s.startsWith(prefix)) {
                        return@replaceAll this.get(s)
                    } else return@replaceAll s
                } else {
                    if (!s.startsWith(prefix)) {
                        return@replaceAll this.get(s)
                    } else return@replaceAll s
                }
            }
        }
    }

    companion object {
        /**
         * 默认备选语言，对应language文件夹
         */
        const val FALLBACK_LANGUAGE: String = "eng"
    }
}
