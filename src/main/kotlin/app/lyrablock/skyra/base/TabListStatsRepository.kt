package app.lyrablock.skyra.base

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TabListStatsRepository : KoinComponent {
    val tabListRepository: TabListRepository by inject()

    val stats get() = tabListRepository.widgetNameToEntries["Stats"]
}