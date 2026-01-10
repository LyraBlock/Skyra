package app.lyrablock.skyra.utils

import net.minecraft.client.Minecraft
import net.minecraft.core.BlockPos

val MC get() = Minecraft.getInstance()!!

operator fun BlockPos.component1() = x
operator fun BlockPos.component2() = y
operator fun BlockPos.component3() = z
