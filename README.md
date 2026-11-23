# OpenAugustus

Complete, compilable, semantically cleaned pure-Java (JDK 8) source reconstruction of **Augustus b4.23.3** (Minecraft 1.8.8/1.8.9 + OptiFine HD U M5 + ShadersMod + Baritone 1.2.19 + WDL 1.8.9 + JLayer 1.0.1 + TheAltening + ViaVersion 5.7.1 / ViaBackwards / ViaRewind / ViaAprilFools / ViaLegacy + ElectricLauncher).

All runtime bytecode, JNIC native methods (`client_jnic.dll` and `launcher_jnic.dll`), embedded resources, runtime dependencies, server-pushed encryption keys, post-auth client initializations, and obfuscated/decompiled source artifacts have been restored to clean, readable Java (`4,884` source files, `0` obfuscated packages, classes, fields, methods, `$` inner-class files, or `varN`/`labelN` locals). The original offline vendor/launcher/backend entrypoints (`ElectricLauncherMain`, `LauncherBootstrap`, `EncryptedClassLoader`, `AugustusServerClient`, `AugustusServerHandler`, `OnlineConfigManager` under `net.augustus.vendor.*`) are preserved in compilable form for reference while kept statically unreachable from `Start`/`Main`.

## Backup Mirror (Anti-DMCA)

> [!IMPORTANT]
> **Save / bookmark the anti-DMCA backup mirror:** https://deobf.su/yxzero/openaugustus
>
> Please bookmark this mirror — if this GitHub repository is ever taken down, the full source and future updates will remain available at https://deobf.su/yxzero/openaugustus.

---

## Project Layout

```text
openaugustus/
├── pom.xml                                        # Maven project (JDK 8)
├── libs/
│   └── augustus-runtime-deps.jar                  # Bundled third-party runtime libraries
└── src/main/
    ├── java/                                      # 4,884 clean Java source files (zero obf/ dirs or $ files)
    │   ├── Start.java                             # Local client launcher entrypoint
    │   ├── net/minecraft/                         # Minecraft 1.8.8/1.8.9 (1,583 files, MCP-mapped, inner classes re-nested)
    │   ├── net/augustus/                          # Augustus b4.23.3 client & modules (1,962 files)
    │   │   ├── modules/                           # Combat, Movement, World, Render, Player, Legit, Latency, Misc, Fun & modes
    │   │   ├── settings/                          # Setting types, options, properties & serialization
    │   │   ├── events/                            # EventManager, 68 DietrichEvents2 events & listener interfaces
    │   │   ├── commands/                          # Command dispatcher & 30 client commands
    │   │   ├── clickgui/, ui/, font/, render/     # ClickGUI, menus, unicode fonts, shaders & HUD rendering
    │   │   ├── cosmetics/                         # MoreCosmetics, GeckoLib models & Molang parser/AST
    │   │   ├── config/, quests/, ai/, viaversion/, utils/  # Configs, quests, ONNX rotation, ViaVersion & helpers
    │   │   └── vendor/                            # Restored ElectricLauncher, auth packets, crypto & server stack (341 files)
    │   ├── net/optifine/                          # OptiFine HD U M5 + ShadersMod (358 files)
    │   ├── baritone/                              # Baritone 1.2.19 (324 files)
    │   ├── wdl/                                   # World Downloader 1.8.9 (103 files)
    │   ├── javazoom/                              # JLayer 1.0.1 MP3 decoder/player (71 files)
    │   ├── com/thealtening/, org/json/            # TheAltening auth & json-simple (26 files)
    │   ├── net/raphimc/                           # ViaLegacy (362 files)
    │   └── net/labymod/, net/lenni0451/, net/arikia/, ...
    └── resources/                                 # Assets (fonts, shaders, sounds, capes, cosmetics, packs, ViaVersion data, ONNX natives)
```

---

## Building

Requires **JDK 8** (with JavaFX `jfxrt.jar` for `ElectricLauncher`) and **Maven 3.x**:

```bash
mvn compile
```

The project compiles cleanly to `target/classes` (`6,229` compiled classes, zero `native` JNIC dependencies).

### Offline vs. Server-Backed Features

All local gameplay, ClickGUI, local & encrypted `.json` configs, custom capes, self cosmetics, shaders, Baritone, WDL, and multi-version protocol translation (`1.7.2`–`1.21.11` via ViaVersion/ViaLegacy) run fully offline.

Only features backed by `connect.electriclauncher.de:1338` are intentionally disconnected:
- Cloud config browser/upload (`OnlineConfigManager`)
- Augustus IRC / AquaMessenger (`AquaMessengerClient`)
- Other-player Augustus tags/cosmetics sync
- Server-pushed daily quests (`QuestManager`) and Augustus account rank/EP/level (`ClientUserProfile`)

---

## Protection Analysis & How Each Layer Was Defeated

Augustus b4.23.3 uses a multi-layer protection stack combining an encrypted in-memory classloader, Zelix KlassMaster (ZKM), JNIC native compilation, a custom mTLS/RSA/AES-256-GCM backend protocol, silent anti-crack sabotage, and residual obfuscated identifiers/ZKM scaffolding after bytecode decompilation. Below is each mechanism and how it was dismantled.

### 1. Encrypted In-Memory Client Archive (`ElectricLauncher` / `EncryptedClassLoader`)

- **Protection:** The client JAR is never written to disk in plaintext. `ElectricLauncher` authenticates over TLS, fetches and decrypts the archive in memory (`ClientArchiveHolder`, custom `AesCrypto` and `ByteRotator`), and loads classes through `EncryptedClassLoader` using hex-named encrypted entries alongside bundled streaming-ZIP dependencies (`csize = 0` data descriptors).
- **Defeat:** Captured live HotSpot 8 process memory dumps in the main menu and in-world. Extracted the full decrypted client bytecode (`6,122` classes) and third-party libraries directly from process memory, including stream-decompressing `csize = 0` ZIP local file headers to recover 16 classes that standard central-directory carvers miss (notably `UserConnectionViaAPI`, `StructuredDataTypeBase`, and `FlowSequenceEndToken` required by ViaVersion).

### 2. Zelix KlassMaster (ZKM) Bytecode Obfuscation

- **Protection:** Both the client and launcher jars were heavily obfuscated with ZKM:
  - Global and per-class `invokedynamic` call-site obfuscation (`net/4p`, `net/4r`, `net/4x` and `launcher/4e`, `launcher/4j`, `launcher/4s`)
  - DES-encrypted string and numeric constant tables initialized in `<clinit>`
  - Inter-procedural 64-bit call-site key propagation (`long` and split `short`/`int`/`char` parameters XORed with per-class constants)
  - Control-flow flattening (state-machine dispatch loops), static opaque predicates, and synthetic exception handlers
  - Identifier renaming across classes, fields, and methods
- **Defeat:** Built a custom ASM-based deobfuscation pipeline to statically resolve every layer:
  1. Propagated inter-procedural ZKM call-site keys across the call graph.
  2. Decrypted all DES string, integer, and long constant tables and inlined them at call sites.
  3. Resolved both global and per-class local `invokedynamic` sites back to direct `INVOKE*` / `GET*` / `PUT*` instructions.
  4. Unflattened control-flow state machines, folded static opaque predicates, and stripped dead exception handlers.
  5. Remapped Minecraft 1.8.8/1.8.9, OptiFine, Baritone, and named Augustus modules/settings before decompiling with Vineflower/Fernflower.

### 3. JNIC Native Compilation (`client_jnic.dll` & `launcher_jnic.dll`)

- **Protection:** `50` sensitive methods (`27` in the client — including rotation/movement modes, core module helpers, and `AugustusServerHandler` — plus `23` in `ElectricLauncher`) were compiled to x64 native code via JNIC into two DLLs (~18 MB client, ~11.5 MB launcher) unpacked at runtime by `dev.jnic.*.JNICLoader`. Several of these classes were *hybrid* ZKM+JNIC classes whose native `<clinit>` initialized ZKM DES/shift/local-Indy tables in native code.
- **Defeat:**
  1. Extracted both unwrapped JNIC DLLs directly from the dump/resources (neither DLL used native virtualization or anti-dump protection).
  2. Built IDA Pro databases annotated with custom JNI struct/vtable headers (`jni_ida.h`) and resolved every global `jclass`, `jmethodID`, and `jfieldID` handle table.
  3. Used **Unicorn x64 CPU emulation** to run native `<clinit>` routines and extract the exact hybrid ZKM DES keys, shift tables, and local-Indy dispatch mappings.
  4. Lifted all `50` native methods back into verified 1:1 pure Java implementations and removed the JNIC loader stubs and `.dat` DLL blobs completely.

### 4. Custom mTLS / RSA / AES-256-GCM Transport & Server-Pushed Config Key

- **Protection:** `AugustusServerClient` establishes a mutual-TLS connection (`client.p12` + `truststore.jks`) to `connect.electriclauncher.de:1338`, decodes matrix-obfuscated RSA keys (`VendorRsaKeyStore` / `RsaKeyMatrixCodec`), and derives AES-256-GCM session keys. Crucially, the **32-byte AES-256 key** used to encrypt/decrypt user `.json` configs and clipboard exports is **not stored in the bytecode**: `ModuleConfigStorage.aesKey` initializes to a dummy key in `<clinit>`, and the server only pushes the real key after authentication via `ConfigEncryptionKeyPacket` (writing it to `ModuleConfigStorage.aesKey` and `ClipboardConfigCodec.aesKey`).
- **Defeat:** Because `ConfigEncryptionKeyPacket` had not yet populated the key in the menu dump, diffing every static `byte[]` mirror across all `5,506` loaded `net/*` classes between the menu dump and the in-game dump pinpointed the exact `byte[32]` heap object (`ModuleConfigStorage.aesKey` / `ClipboardConfigCodec.aesKey`). Embedding the recovered key restored full decryption and saving of encrypted Augustus configs.

### 5. Silent Anti-Crack Sabotage & Auth-Gated Initialization

- **Protection:** Rather than crashing immediately when unauthenticated, Augustus ships subtle sabotage stand-ins and gates core local subsystems inside the post-auth packet handler (`AuthSuccessPacket` in `AugustusServerHandler`):
  - **Sabotaged rotation helper (`YawPitchHelper` → `PlayerRotationHandler`):** Before auth, `Augustus.yawPitchHelper` uses `YawPitchHelper`, which wraps yaw to `[-360, 360)` instead of `[-180, 180)`, inverts the sync condition on `S08PacketPlayerPosLook`, ignores yaw `>= 500`, and omits the rotation callback.
  - **Sabotaged mouse wheel tracker (`ScaledMouseWheelTracker` → `MouseWheelTracker`):** Before auth, `Augustus.mouseWheelTracker` multiplies `Mouse.getDWheel() * 4` and overwrites instead of accumulating.
  - **Auth-gated subsystems:** ViaVersion / ViaBackwards / ViaRewind / ViaAprilFools / ViaLegacy (`ViaAugustus.initialize`), Alt Manager skin preloading (`MinotarAvatarCache.preloadAccountAvatars`), and `alarm.ogg` extraction only ran after `AuthSuccessPacket`.
  - **Killswitches & timestamp traps:** `AntiSamplerGuard` can swap the real RNG (`DefaultRandomGenerator`) for a constant-value sabotage RNG (`DeterministicRandomProvider`); `LicenseTimestampStore.timestampBytes` checks a 6-day launch timestamp injected by the launcher into `assets/augustus/pictures/logo_64x64.png` across 45 modules; `VendorSessionTicker` ties tick execution to backend heartbeats.
- **Defeat:** Compared live `Augustus` singleton fields in both memory dumps against the decompiled startup path and audited every branch of `AugustusServerHandler`:
  - Initialized `PlayerRotationHandler`, `MouseWheelTracker`, `MinotarAvatarCache.preloadAccountAvatars`, `alarm.ogg` extraction, and async `ViaAugustus.initialize` directly in `Augustus.initializeClient` (also fixing a static-init ordering bug in `ViaLegacyGameProfileFetcher`).
  - Added a standalone timestamp fallback in `ProtectorManager.readTimestampHeader` when the launcher-injected `logo_64x64.png` timestamp resource is absent.
  - Decoupled non-vendor gameplay tick logic from `VendorSessionTicker` into `Minecraft.java` and verified via full static bytecode reachability analysis that all offline vendor/launcher/backend entrypoints (`ElectricLauncherMain`, `LauncherBootstrap`, `EncryptedClassLoader`, `AugustusServerClient`, `AugustusServerHandler`, `OnlineConfigManager`) remain **0** reachable paths from `Start`/`Main`.

### 6. Source Cleanup & Semantic Remapping

- **Artifacts:** After bytecode deobfuscation and decompilation, the source still contained dead ZKM key parameters (`long`/`short`/`int`/`char` call-site keys), unused `HashMap(13)` caches, `a(byte[])` decoders, flow-guard stubs, obfuscated packages/identifiers (`net.augustus.obf`, `baritone.obf`, `C_*`/`B_*`/`L_*`/`f_4*`/`m_4*`), `varN`/`labelN` decompiler locals, and `914` flattened `$` inner-class files.
- **Defeat:**
  1. Ran whole-program ASM liveness analysis + a `javac` AST pass to strip all proven-dead ZKM parameters, key expressions, caches, decoders, and `ZkmNameMapper`.
  2. Renamed and relocated all `2,064` obfuscated classes, `18,915` fields, and `16,727` method override groups to clean semantic/upstream packages and names via `javac` AST refactoring.
  3. Replaced decompiler locals/labels/casts/bridges and re-nested all `914` `$` inner classes back into their enclosing files.
  4. Verified exact behavior against the baseline classes via ASM bytecode equivalence checking and differential runtime tests.
