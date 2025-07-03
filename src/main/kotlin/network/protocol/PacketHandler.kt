package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.experimental.network.MigrationPacket

interface PacketHandler {
    fun handle(pk: MigrationPacket<*>) {}

    fun handle(pk: AnimatePacket) {}

    fun handle(pk: ClientToServerHandshakePacket) {}

    fun handle(pk: CodeBuilderSourcePacket) {}

    fun handle(pk: CommandBlockUpdatePacket) {}

    fun handle(pk: CommandRequestPacket) {}

    fun handle(pk: ContainerClosePacket) {}

    fun handle(pk: DebugInfoPacket) {}

    fun handle(pk: ServerboundDiagnosticsPacket) {}

    fun handle(pk: EduUriResourcePacket) {}

    fun handle(pk: EmoteListPacket) {}

    fun handle(pk: EmotePacket) {}

    fun handle(pk: EntityEventPacket) {}

    fun handle(pk: GUIDataPickItemPacket) {}

    fun handle(pk: HurtArmorPacket) {}

    fun handle(pk: InteractPacket) {}

    fun handle(pk: InventoryTransactionPacket) {}

    fun handle(pk: ItemRegistryPacket) {}

    fun handle(pk: ItemStackRequestPacket) {}

    fun handle(pk: ItemStackResponsePacket) {}

    fun handle(pk: LabTablePacket) {}

    fun handle(pk: LecternUpdatePacket) {}

    fun handle(pk: LessonProgressPacket) {}

    fun handle(pk: LevelChunkPacket) {}

    fun handle(pk: LevelEventGenericPacket) {}

    fun handle(pk: LevelEventPacket) {}

    fun handle(pk: LevelSoundEventPacket) {}

    fun handle(pk: LoginPacket) {}

    fun handle(pk: MapCreateLockedCopyPacket) {}

    fun handle(pk: MapInfoRequestPacket) {}

    fun handle(pk: MobArmorEquipmentPacket) {}

    fun handle(pk: MobEffectPacket) {}

    fun handle(pk: MobEquipmentPacket) {}

    fun handle(pk: ModalFormRequestPacket) {}

    fun handle(pk: ModalFormResponsePacket) {}

    fun handle(pk: MoveEntityAbsolutePacket) {}

    fun handle(pk: MoveEntityDeltaPacket) {}

    fun handle(pk: MovementEffectPacket) {}

    fun handle(pk: MovePlayerPacket) {}

    fun handle(pk: NPCDialoguePacket) {}

    fun handle(pk: NPCRequestPacket) {}

    fun handle(pk: NetworkChunkPublisherUpdatePacket) {}

    fun handle(pk: NetworkSettingsPacket) {}

    fun handle(pk: OnScreenTextureAnimationPacket) {}

    fun handle(pk: OpenSignPacket) {}

    fun handle(pk: PacketViolationWarningPacket) {}

    fun handle(pk: PlaySoundPacket) {}

    fun handle(pk: PlayStatusPacket) {}

    fun handle(pk: PlayerActionPacket) {}

    fun handle(pk: PlayerArmorDamagePacket) {}

    fun handle(pk: PlayerAuthInputPacket) {}

    fun handle(pk: PlayerEnchantOptionsPacket) {}

    fun handle(pk: PlayerFogPacket) {}

    fun handle(pk: PlayerHotbarPacket) {}

    fun handle(pk: PlayerListPacket) {}

    fun handle(pk: PlayerLocationPacket) {}

    fun handle(pk: PlayerSkinPacket) {}

    fun handle(pk: PlayerStartItemCoolDownPacket) {}

    fun handle(pk: PositionTrackingDBClientRequestPacket) {}

    fun handle(pk: PositionTrackingDBServerBroadcastPacket) {}

    fun handle(pk: RemoveActorPacket) {}

    fun handle(pk: RemoveObjectivePacket) {}

    fun handle(pk: RemoveVolumeEntityPacket) {}

    fun handle(pk: RequestAbilityPacket) {}

    fun handle(pk: RequestChunkRadiusPacket) {}

    fun handle(pk: RequestNetworkSettingsPacket) {}

    fun handle(pk: RequestPermissionsPacket) {}

    fun handle(pk: ResourcePackClientResponsePacket) {}

    fun handle(pk: ResourcePackStackPacket) {}

    fun handle(pk: ResourcePacksInfoPacket) {}

    fun handle(pk: RespawnPacket) {}

    fun handle(pk: ScriptMessagePacket) {}

    fun handle(pk: ServerPostMovePositionPacket) {}

    fun handle(pk: ServerSettingsRequestPacket) {}

    fun handle(pk: ServerSettingsResponsePacket) {}

    fun handle(pk: ServerToClientHandshakePacket) {}

    fun handle(pk: SetCommandsEnabledPacket) {}

    fun handle(pk: SetDefaultGameTypePacket) {}

    fun handle(pk: SetDifficultyPacket) {}

    fun handle(pk: SetDisplayObjectivePacket) {}

    fun handle(pk: SetEntityDataPacket) {}

    fun handle(pk: SetEntityLinkPacket) {}

    fun handle(pk: SetEntityMotionPacket) {}

    fun handle(pk: SetHealthPacket) {}

    fun handle(pk: SetHudPacket) {}

    fun handle(pk: SetLastHurtByPacket) {}

    fun handle(pk: SetLocalPlayerAsInitializedPacket) {}

    fun handle(pk: SetPlayerGameTypePacket) {}

    fun handle(pk: SetPlayerInventoryOptionsPacket) {}

    fun handle(pk: SetScorePacket) {}

    fun handle(pk: SetScoreboardIdentityPacket) {}

    fun handle(pk: SetSpawnPositionPacket) {}

    fun handle(pk: SetTimePacket) {}

    fun handle(pk: SetTitlePacket) {}

    fun handle(pk: ShowCreditsPacket) {}

    fun handle(pk: ShowProfilePacket) {}

    fun handle(pk: SimpleEventPacket) {}

    fun handle(pk: SimulationTypePacket) {}

    fun handle(pk: SpawnParticleEffectPacket) {}

    fun handle(pk: StartGamePacket) {}

    fun handle(pk: StopSoundPacket) {}

    fun handle(pk: StructureBlockUpdatePacket) {}

    fun handle(pk: SubClientLoginPacket) {}

    fun handle(pk: SyncEntityPropertyPacket) {}

    fun handle(pk: TakeItemEntityPacket) {}

    fun handle(pk: TextPacket) {}

    fun handle(pk: TickingAreasLoadStatusPacket) {}

    fun handle(pk: ToastRequestPacket) {}

    fun handle(pk: ToggleCrafterSlotRequestPacket) {}

    fun handle(pk: TransferPacket) {}

    fun handle(pk: TrimDataPacket) {}

    fun handle(pk: UnlockedRecipesPacket) {}

    fun handle(pk: UpdateAbilitiesPacket) {}

    fun handle(pk: UpdateAdventureSettingsPacket) {}

    fun handle(pk: UpdateAttributesPacket) {}

    fun handle(pk: UpdateBlockPacket) {}

    fun handle(pk: UpdateBlockSyncedPacket) {}

    fun handle(pk: UpdateClientInputLocksPacket) {}

    fun handle(pk: UpdateEquipmentPacket) {}

    fun handle(pk: UpdatePlayerGameTypePacket) {}

    fun handle(pk: UpdateSoftEnumPacket) {}

    fun handle(pk: UpdateSubChunkBlocksPacket) {}

    fun handle(pk: UpdateTradePacket) {}

    fun handle(pk: SettingsCommandPacket) {}

    fun handle(pk: ServerboundLoadingScreenPacket) {}

    fun handle(pk: ClientMovementPredictionSyncPacket) {}
}
