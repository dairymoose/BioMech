# BioMech: Forge 1.20.1 → NeoForge 1.21.1 migration

**Status: compiles, `./gradlew build` produces `biomech-1.21.1-1.27.jar`, and `runClient`
launches to the main menu.** All 4 mixins apply; registries/configs/renderers initialize.

### Runtime fixes made after first launch
- **Event-bus split**: `ClientModEvents` mixed static mod-bus handlers with an instance registered on
  the game bus, which NeoForge rejects. Static mod-bus handlers now live in `ClientModBusEvents`
  (`@EventBusSubscriber(bus=MOD)`) as thin delegators; the game-bus instance is registered via the
  `ClientModEvents` constructor.
- **Config load ordering**: `BioMechConfig.reinit()` read SERVER values while handling the COMMON/CLIENT
  config-load event (NeoForge loads each type separately). Cross-spec reads are now guarded by
  `spec.isLoaded()` so client options apply at startup and SERVER values apply when that config loads.
- **Stale config**: delete `run/config/biomech-*.toml` from the 1.20.1 run once (a spec mismatch made
  NeoForge attempt an atomic rewrite that failed). Fresh configs write cleanly.


This crossed both the Forge→NeoForge split and the 1.20.5 data-components rewrite. All Java
compiles against NeoForge 21.1.233; the jar packages the expanded `neoforge.mods.toml`, the
access transformer, the mixin config, and the 1.21-singularized data tree.

## Toolchain
- **NeoGradle** `net.neoforged.gradle.userdev` 7.0.192, **Gradle 8.14**, **Java 21**.
- **NeoForge 21.1.233**, AzureLib `azurelib-neo-1.21.1:3.1.0`, JEI `1.21.1-neoforge:19.27.0.340`.
- Build/run on JDK 21: `JAVA_HOME=".../jdk-21.0.5.11-hotspot" ./gradlew build`.
- Non-essential dev mods (Xaero's, EMI, EMI-Loot, spark, optiscale, tagtooltips, config-menus,
  kotlin-for-forge, fzzy-config) were **stripped** (none referenced in code).

## DONE (compiles)
- Build system, `neoforge.mods.toml`, `pack.mcmeta`, gradle wrapper, **access transformer**
  (SRG→Mojang names; declared via `minecraft.accessTransformers` in build.gradle — NeoGradle does
  not auto-detect it), mixin config (`JAVA_21`, refmap removed), data dirs singularised for 1.21.
- **Networking** fully on the payload system (`CustomPacketPayload`/`StreamCodec`/`IPayloadContext`,
  `RegisterPayloadHandlersEvent`); all 15 packets + send sites.
- **AzureLib** relocation to `…common.*` (118 files); `AzureLib.ITEM_UUID_TAG` (NBT) → `AzureLib.AZ_ID`
  data component.
- `net.minecraftforge.*`→`net.neoforged.*`; `ForgeConfigSpec`→`ModConfigSpec`; `RegistryObject`→
  `DeferredHolder`; `ForgeRegistries`→`BuiltInRegistries`; reach/gravity attrs → vanilla `Attributes`;
  `DistExecutor`→`DistExec` shim; `@EventBusSubscriber`/`Bus.GAME`.
- **BioMech.java**: new mod ctor `(IEventBus, ModContainer)`, `NeoForge.EVENT_BUS`, config via
  `modContainer.registerConfig`; tick events split Pre/Post; `LivingIncomingDamageEvent`,
  `LivingDamageEvent.Pre`(`getNewDamage`/`setNewDamage`), `CriticalHitEvent.setCriticalHit`,
  `MobEffectEvent.Applicable.Result.DO_NOT_APPLY`, `RenderGuiLayerEvent`/`VanillaGuiLayers`,
  creative tab, `RegisterMenuScreensEvent`.
- **Data Components**: `ItemNbtHelper` bridges old `getOrCreateTag/getTag` onto `CUSTOM_DATA`
  (read-modify-write); all ~14 sites migrated (re-entrancy guard preserved in the PowerArm block).
- **Attributes**: `AttributeModifier(ResourceLocation, double, Operation.ADD_VALUE)`; `PermanentModifiers`
  → ResourceLocations; `Holder<Attribute>` throughout (`ArmUtil`, `removePermanentModifiers`).
- **Armor material**: `ArmorBase`+subclasses take `Holder<ArmorMaterial>`; 0-defense direct holder.
- **Conditions**: codec-based `ICondition` (`CraftingFlagCondition` record + `CONDITION_CODECS`).
- **Serialization**: ItemStack `saveOptional`/`parseOptional` with `HolderLookup.Provider` via
  `BioMech.registryAccess()`; `BlockEntity` `saveAdditional/loadAdditional(..., Provider)`,
  `ContainerHelper` provider-aware; `NbtIo` `Path`-based.
- **Recipes (menu)**: `getRecipeFor`→`Optional<RecipeHolder<…>>`, `CraftingInput` via `asCraftInput()`.
- **Misc 1.21 API**: `appendHoverText(…TooltipContext…)`, `Block.codec()`, `playerWillDestroy` returns
  BlockState, `pickupBlock(Player,…)`, entity `defineSynchedData(Builder)`, `PickaxeItem(Tier,Props)`,
  `setupRotations` 4th float, VertexConsumer `addVertex/setColor/setLight`, `ParticleRenderType.begin`
  returns BufferBuilder, `ImageButton` WidgetSprites, `getEquipmentSlotForItem` (instance),
  `Minecraft.getTimer().getGameTimeDeltaPartialTick`.

## REMAINING — RUNTIME validation (compiles, but verify in-game)
These did not block compilation but should be tested when launching the client/server:

1. **Recipe / loot / advancement JSON content** — 1.21 changed formats. Most likely needed:
   crafting result `{"item": "...", "count": n}` → `{"id": "...", "count": n}`; verify the
   `neoforge:conditions` wrapper and the `biomech:crafting_flag` condition load; check loot-table and
   advancement schemas. (Java condition codec is registered; the JSON may need the new wrapper key.)
2. **Mixins** — `PlayerModelMixin`, `LivingEntityUpdateFallFlyingMixin`, `PlayerFallFlyingMixin`,
   `EntityMoveMixin`: confirm each injection point still resolves against 1.21.1 (method names/targets).
3. **Block interaction** — DONE: `BioMechStationBlock.use(...)` ported to 1.21
   `useWithoutItem(BlockState, Level, BlockPos, Player, BlockHitResult)` (InteractionHand dropped).
   Right-click opens the station again.
4. **GUI buttons** — DONE: replaced the 3 `ImageButton`s with `TexturedButton`
   (`client/widget/TexturedButton.java`), a custom Button that blits the original GUI_LOCATION u/v
   sub-region with the hover-row offset (1.21's ImageButton dropped u/v in favour of WidgetSprites).
5. **Intentionally dropped/simplified features** (compile shims — restore if desired):
   - Binding-curse prevention in the station/storage slot `mayPickup` (enchantments are data-driven now).
   - Frostbite/powder-snow GUI overlay cancel for the elytra chestplate (no dedicated 1.21 gui layer;
     freeze state is already cleared each tick).
   - Teleportation-crystal "home" uses `Vec3.atBottomCenterOf(respawnPos)` instead of the now-private
     `findRespawnAndUseSpawnBlock` (no spawn-block-aware offset).
6. `pack.mcmeta` uses `pack_format` 34 with `supported_formats [34,48]`; adjust if the loader complains.
