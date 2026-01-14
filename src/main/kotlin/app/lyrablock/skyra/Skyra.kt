package app.lyrablock.skyra

import app.lyrablock.skyra.base.tablist.TabListRepository
import app.lyrablock.skyra.feature.mining.prediction.HiddenBlocksRegistry
import app.lyrablock.skyra.feature.mining.prediction.MiningPrediction
import app.lyrablock.skyra.utils.hypixel.HypixelStatus
import net.fabricmc.api.ClientModInitializer
import net.hypixel.modapi.HypixelModAPI
import net.hypixel.modapi.packet.impl.clientbound.event.ClientboundLocationPacket
import org.apache.logging.log4j.LogManager
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

object Skyra : ClientModInitializer {
    private val logger = LogManager.getLogger("skyra")
    override fun onInitializeClient() {
        HypixelModAPI.getInstance().subscribeToEventPacket(ClientboundLocationPacket::class.java)

        val core = module {
            singleOf(::HiddenBlocksRegistry)
            singleOf(::HypixelStatus)
        }

        startKoin {
            printLogger()
            modules(
                core,
                TabListRepository.module,
                MiningPrediction.module,
            )
        }
    }
}