@file:Suppress("SpellCheckingInspection")

package app.lyrablock.skyra.feature.mining

import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import kotlin.math.round

@Suppress("unused")
enum class Mineable(
    val type: BlockType,
    val displayName: String,
    val block: Block,
    val strength: Int,
    val instaMine: Int,
    val breakingPower: Int = 0
) {
    STONE(BlockType.BLOCK, "Stone", Blocks.STONE, 15, 450),
    HARD_STONE(BlockType.BLOCK, "Hard Stone", Blocks.INFESTED_STONE, 50, 1500),
    COBBLESTONE(BlockType.BLOCK, "Cobblestone", Blocks.COBBLESTONE, 20, 600),
    END_STONE(BlockType.BLOCK, "End Stone", Blocks.END_STONE, 30, 1801),
    OBSIDIAN(BlockType.BLOCK, "Obsidian", Blocks.OBSIDIAN, 500, 30001),
    NETHERRACK(BlockType.BLOCK, "Netherrack", Blocks.NETHERRACK, 4, 241),
    ICE(BlockType.BLOCK, "Ice", Blocks.ICE, 5, 301),
    COAL_ORE(BlockType.ORE, "Coal", Blocks.COAL_ORE, 30, 1801),
    IRON_ORE(BlockType.ORE, "Iron", Blocks.IRON_ORE, 30, 1801),
    GOLD_ORE(BlockType.ORE, "Gold", Blocks.GOLD_ORE, 30, 1801),
    LAPIS_LAZULI_ORE(BlockType.ORE, "Lapis", Blocks.LAPIS_ORE, 30, 1801),
    REDSTONE_ORE(BlockType.ORE, "Redstone", Blocks.REDSTONE_ORE, 30, 1801),
    EMERALD_ORE(BlockType.ORE, "Emerald", Blocks.EMERALD_ORE, 30, 1801),
    DIAMOND_ORE(BlockType.ORE, "Diamond", Blocks.DIAMOND_ORE, 30, 1801),
    NETHER_QUARTZ_ORE(BlockType.ORE, "Quartz", Blocks.NETHER_QUARTZ_ORE, 30, 1801),
    PURE_COAL(BlockType.ORE, "Pure Coal", Blocks.COAL_BLOCK, 600, 36001),
    PURE_IRON(BlockType.ORE, "Pure Iron", Blocks.IRON_BLOCK, 600, 36001),
    PURE_GOLD(BlockType.ORE, "Pure Gold", Blocks.GOLD_BLOCK, 600, 36001),
    PURE_LAPIS(BlockType.ORE, "Pure Lapis", Blocks.LAPIS_BLOCK, 600, 36001),
    PURE_REDSTONE(BlockType.ORE, "Pure Redstone", Blocks.REDSTONE_BLOCK, 600, 36001),
    PURE_EMERALD(BlockType.ORE, "Pure Emerald", Blocks.EMERALD_BLOCK, 600, 36001),
    PURE_DIAMOND(BlockType.ORE, "Pure Diamond", Blocks.DIAMOND_BLOCK, 600, 36001),
    MITHRIL_GRAY_1(BlockType.DWARVEN_METAL, "Mithril", Blocks.GRAY_WOOL, 500, 30001),
    MITHRIL_GRAY_2(BlockType.DWARVEN_METAL, "Mithril", Blocks.CYAN_TERRACOTTA, 500, 30001),
    MITHRIL_PRISMARINE_1(BlockType.DWARVEN_METAL, "Mithril", Blocks.PRISMARINE, 800, 48001),
    MITHRIL_PRISMARINE_2(BlockType.DWARVEN_METAL, "Mithril", Blocks.DARK_PRISMARINE, 800, 48001),
    MITHRIL_PRISMARINE_3(BlockType.DWARVEN_METAL, "Mithril", Blocks.PRISMARINE_BRICKS, 800, 48001),
    MITHRIL_BLUE(BlockType.DWARVEN_METAL, "Mithril", Blocks.LIGHT_BLUE_WOOL, 1500, 90001),
    TITANIUM(BlockType.DWARVEN_METAL, "Titanium", Blocks.DIORITE, 2000, 120001),
    RUBY_GEMSTONE_BLOCK(BlockType.GEMSTONE, "Ruby", Blocks.RED_STAINED_GLASS, 2300, 138001),
    RUBY_GEMSTONE_PANE(BlockType.GEMSTONE, "Ruby", Blocks.RED_STAINED_GLASS_PANE, 2300, 138001),
    AMBER_GEMSTONE_BLOCK(BlockType.GEMSTONE, "Amber", Blocks.ORANGE_STAINED_GLASS, 3000, 180001),
    AMBER_GEMSTONE_PANE(BlockType.GEMSTONE, "Amber", Blocks.ORANGE_STAINED_GLASS_PANE, 3000, 180001),
    AMETHYST_GEMSTONE_BLOCK(BlockType.GEMSTONE, "Amethyst", Blocks.PURPLE_STAINED_GLASS, 3000, 180001),
    AMETHYST_GEMSTONE_PANE(BlockType.GEMSTONE, "Amethyst", Blocks.PURPLE_STAINED_GLASS_PANE, 3000, 180001),
    JADE_GEMSTONE_BLOCK(BlockType.GEMSTONE, "Jade", Blocks.LIME_STAINED_GLASS, 3000, 180001),
    JADE_GEMSTONE_PANE(BlockType.GEMSTONE, "Jade", Blocks.LIME_STAINED_GLASS_PANE, 3000, 180001),
    SAPPHIRE_GEMSTONE_BLOCK(BlockType.GEMSTONE, "Sapphire", Blocks.LIGHT_BLUE_STAINED_GLASS, 3000, 180001),
    SAPPHIRE_GEMSTONE_PANE(BlockType.GEMSTONE, "Sapphire", Blocks.LIGHT_BLUE_STAINED_GLASS_PANE, 3000, 180001),
    OPAL_GEMSTONE_BLOCK(BlockType.GEMSTONE, "Opal", Blocks.WHITE_STAINED_GLASS, 3000, 180001),
    OPAL_GEMSTONE_PANE(BlockType.GEMSTONE, "Opal", Blocks.WHITE_STAINED_GLASS_PANE, 3000, 180001),
    TOPAZ_GEMSTONE_BLOCK(BlockType.GEMSTONE, "Topaz", Blocks.YELLOW_STAINED_GLASS, 3800, 228001),
    TOPAZ_GEMSTONE_PANE(BlockType.GEMSTONE, "Topaz", Blocks.YELLOW_STAINED_GLASS_PANE, 3800, 228001),
    JASPER_GEMSTONE_BLOCK(BlockType.GEMSTONE, "Jasper", Blocks.PINK_STAINED_GLASS, 4800, 288001),
    JASPER_GEMSTONE_PANE(BlockType.GEMSTONE, "Jasper", Blocks.PINK_STAINED_GLASS_PANE, 4800, 288001),
    GLACITE(BlockType.DWARVEN_METAL, "Glacite", Blocks.PACKED_ICE, 6000, 360001),
    UMBER_1(BlockType.DWARVEN_METAL, "Umber", Blocks.TERRACOTTA, 5600, 336001),
    UMBER_2(BlockType.DWARVEN_METAL, "Umber", Blocks.BROWN_TERRACOTTA, 5600, 336001),
    UMBER_3(BlockType.DWARVEN_METAL, "Umber", Blocks.RED_SANDSTONE_SLAB, 5600, 336001),
    TUNGSTEN_1(BlockType.DWARVEN_METAL, "Tungsten", Blocks.INFESTED_COBBLESTONE, 5600, 336001),
    TUNGSTEN_2(BlockType.DWARVEN_METAL, "Tungsten", Blocks.CLAY, 5600, 336001),
    ONYX_GEMSTONE_BLOCK(BlockType.GEMSTONE, "Onyx", Blocks.BLACK_STAINED_GLASS, 5200, 312001),
    ONYX_GEMSTONE_PANE(BlockType.GEMSTONE, "Onyx", Blocks.BLACK_STAINED_GLASS_PANE, 5200, 312001),
    AQUAMARINE_GEMSTONE_BLOCK(BlockType.GEMSTONE, "Aquamarine", Blocks.BLUE_STAINED_GLASS, 5200, 312001),
    AQUAMARINE_GEMSTONE_PANE(BlockType.GEMSTONE, "Aquamarine", Blocks.BLUE_STAINED_GLASS_PANE, 5200, 312001),
    CITRINE_GEMSTONE_BLOCK(BlockType.GEMSTONE, "Citrine", Blocks.BROWN_STAINED_GLASS, 5200, 312001),
    CITRINE_GEMSTONE_PANE(BlockType.GEMSTONE, "Citrine", Blocks.BROWN_STAINED_GLASS_PANE, 5200, 312001),
    PERIDOT_GEMSTONE_BLOCK(BlockType.GEMSTONE, "Peridot", Blocks.GREEN_STAINED_GLASS, 5200, 312001),
    PERIDOT_GEMSTONE_PANE(BlockType.GEMSTONE, "Peridot", Blocks.GREEN_STAINED_GLASS_PANE, 5200, 312001),
    SULPHUR(BlockType.ORE, "Sulphur", Blocks.SPONGE, 500, 30001);

    /**
     * Get the time to mine this block with the given dig speed.
     *
     * @param digSpeed The dig speed, which is mining speed after several modifiers (namely Precision Mining buff, and
     * debuff in water / not on ground)
     *
     * @return Time to mine in ticks
     */
    fun timeToMine(digSpeed: Int): Int {
        if (digSpeed >= instaMine) return 0
        val time = round(30f * strength / digSpeed).toInt()
        // Softcap limits how fast a block can be mined to at least 4 ticks (or 0.2s).
        // The softcap can be bypassed by breaking a block instantly.
        return time.coerceAtLeast(4)
    }

    companion object {
        val fromBlock = entries.associateBy { it.block }
    }
}
