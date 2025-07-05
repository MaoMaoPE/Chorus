package org.chorus_oss.chorus.network.process.handler


import org.chorus_oss.chorus.Server
import org.chorus_oss.chorus.network.connection.BedrockSession
import org.chorus_oss.chorus.network.connection.util.EncryptionUtils.createHandshakeJwt
import org.chorus_oss.chorus.network.connection.util.EncryptionUtils.createKeyPair
import org.chorus_oss.chorus.network.connection.util.EncryptionUtils.generateRandomToken
import org.chorus_oss.chorus.network.connection.util.EncryptionUtils.getSecretKey
import org.chorus_oss.chorus.network.connection.util.EncryptionUtils.parseKey
import org.chorus_oss.chorus.network.process.SessionState
import org.chorus_oss.chorus.network.protocol.LoginPacket
import org.chorus_oss.chorus.network.protocol.ServerToClientHandshakePacket
import org.chorus_oss.chorus.network.protocol.types.InputMode
import org.chorus_oss.chorus.network.protocol.types.Platform
import org.chorus_oss.chorus.network.protocol.types.PlayerInfo
import org.chorus_oss.chorus.network.protocol.types.XboxLivePlayerInfo
import org.chorus_oss.chorus.utils.ClientChainData
import org.chorus_oss.chorus.utils.Loggable
import java.net.InetSocketAddress
import java.util.function.Consumer
import java.util.regex.Pattern

class LoginHandler(session: BedrockSession, private val consumer: Consumer<PlayerInfo>) :
    BedrockSessionPacketHandler(session) {

    override fun handle(pk: LoginPacket) {
        val server = session.server

        //check the player login time
        if (pk.issueUnixTime != -1L && Server.instance.checkLoginTime && System.currentTimeMillis() - pk.issueUnixTime > 20000) {
            val message = "disconnectionScreen.noReason"
            log.debug("disconnection due to noReason")
            session.sendPlayStatus(org.chorus_oss.protocol.packets.PlayStatusPacket.Companion.Status.LoginFailedClient, true)
            session.close(message)
            return
        }

        val chainData = ClientChainData.read(pk)

        //verify the player if enable the xbox-auth
        if (!chainData.isXboxAuthed && server.settings.serverSettings.xboxAuth) {
            log.debug("disconnection due to notAuthenticated")
            session.close("disconnectionScreen.notAuthenticated")
            return
        }

        //Verify the number of server player
        if (server.onlinePlayers.size >= server.maxPlayers) {
            log.debug("disconnection due to serverFull")
            session.close("disconnectionScreen.serverFull")
            return
        }

        //set proxy ip
        if (server.settings.baseSettings.waterdogpe && chainData.waterdogIP != null) {
            val oldAddress = session.address
            session.address = InetSocketAddress(chainData.waterdogIP, session.address!!.port)
            Server.instance.network.replaceSessionAddress(oldAddress, session.address!!, session)
        }

        // Verify if the titleId match with DeviceOs
        val predictedDeviceOS = getPredictedDeviceOS(chainData)
        if (predictedDeviceOS != chainData.deviceOS && server.settings.serverSettings.xboxAuth) {
            log.debug("disconnection due to mismatched device os, predicted: $predictedDeviceOS, actual: ${chainData.deviceOS}")
            session.close("§cPacket handling error")
            return
        }

        // Verify if the language is valid
        if (!isValidLanguage(chainData.languageCode!!)) {
            log.debug("disconnection due to invalid language")
            session.close("§cPacket handling error")
            return
        }

        // Verify if the GameVersion has valid format
        if (chainData.gameVersion!!.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray().size != 3 && !Server.instance.settings.debugSettings.allowBeta) {
            log.debug("disconnection due to invalid game version")
            session.close("§cPacket handling error")
            return
        }

        // Verify if the CurrentInputMode is valid
        val currentInputMode = chainData.currentInputMode
        if (currentInputMode <= InputMode.UNDEFINED.ordinal ||
            currentInputMode >= InputMode.COUNT.ordinal
        ) {
            log.debug("disconnection due to invalid input mode")
            session.close("§cPacket handling error")
            return
        }

        // Verify if the DefaultInputMode is valid
        val defaultInputMode = chainData.defaultInputMode
        if (defaultInputMode <= InputMode.UNDEFINED.ordinal ||
            defaultInputMode >= InputMode.COUNT.ordinal
        ) {
            log.debug("disconnection due to invalid input mode")
            session.close("§cPacket handling error")
            return
        }

        val uniqueId = pk.clientUUID!!
        val username = pk.username!!
        val usernameMatcher = playerNamePattern.matcher(username)

        if (!usernameMatcher.matches() ||
            username.equals("rcon", ignoreCase = true) ||
            username.equals("console", ignoreCase = true)
        ) {
            log.debug("disconnection due to invalidName")
            session.close("disconnectionScreen.invalidName")
            return
        }

        if (!pk.skin!!.isValid()) {
            log.debug("disconnection due to invalidSkin")
            session.close("disconnectionScreen.invalidSkin")
            return
        }

        val skin = pk.skin!!
        if (server.settings.playerSettings.forceSkinTrusted) {
            skin.setTrusted(true)
        }

        var info = PlayerInfo(
            username,
            uniqueId,
            skin,
            chainData
        )

        if (chainData.isXboxAuthed) {
            info = XboxLivePlayerInfo(
                username,
                uniqueId,
                skin,
                chainData,
                chainData.xuid!!
            )
        }

        consumer.accept(info)
        session.setAuthenticated()

        if (!server.isWhitelisted((info.username).lowercase())) {
            log.debug("disconnection due to white-listed")
            session.close("Server is white-listed")
            return
        }

        val entry = server.bannedPlayers.entries[info.username.lowercase()]
        if (entry != null) {
            val reason = entry.reason
            log.debug("disconnection due to named ban")
            session.close(if (reason.isNotEmpty()) "You are banned. Reason: $reason" else "You are banned")
            return
        }

        if (server.enabledNetworkEncryption) {
            this.enableEncryption(chainData)
        } else {
            session.machine.fire(SessionState.RESOURCE_PACK)
        }
    }

    private fun getPredictedDeviceOS(chainData: ClientChainData): Int {
        val titleId = chainData.titleId
        return when (titleId) {
            "896928775" -> Platform.WINDOWS_10.id
            "2047319603" -> Platform.SWITCH.id
            "1739947436" -> Platform.ANDROID.id
            "2044456598" -> Platform.PLAYSTATION.id
            "1828326430" -> Platform.XBOX_ONE.id
            "1810924247" -> Platform.IOS.id
            else -> 0
        }
    }

    private fun isValidLanguage(language: String): Boolean {
        val languagesCode: MutableSet<String> = mutableSetOf(
            "fr_CA", "fr_FR", "bg_BG",
            "cs_CZ", "da_DK", "de_DE",
            "el_GR", "en_GB", "en_US",
            "es_ES", "es_MX", "fi_FI",
            "hu_HU", "id_ID", "it_IT",
            "ja_JP", "ko_KR", "nb_NO",
            "nl_NL", "pl_PL", "pt_BR",
            "pt_PT", "ru_RU", "sk_SK",
            "sv_SE", "tr_TR", "uk_UA",
            "zh_CN", "zh_TW"
        )
        return languagesCode.contains(language)
    }

    private fun enableEncryption(data: ClientChainData) {
        try {
            val clientKey = parseKey(data.identityPublicKey)
            val encryptionKeyPair = createKeyPair()
            val encryptionToken = generateRandomToken()
            val encryptionKey = getSecretKey(
                encryptionKeyPair.private, clientKey,
                encryptionToken
            )
            val handshakeJwt = createHandshakeJwt(encryptionKeyPair, encryptionToken)
            // WTF
            if (session.isDisconnected) {
                return
            }
            val pk = ServerToClientHandshakePacket()
            pk.jwt = handshakeJwt
            session.sendPacketImmediately(pk)
            session.enableEncryption(encryptionKey)

            session.machine.fire(SessionState.ENCRYPTION)
        } catch (e: Exception) {
            log.error("Failed to prepare encryption", e)
            session.close("encryption error")
        }
    }

    companion object : Loggable {
        private val playerNamePattern: Pattern = Pattern.compile("^(?! )([a-zA-Z0-9_ ]{2,15}[a-zA-Z0-9_])(?<! )$")
    }
}
