package at.hannibal2.skyhanni.test.command

import at.hannibal2.skyhanni.config.gui.utils.Utils
import at.hannibal2.skyhanni.test.LorenzTest
import at.hannibal2.skyhanni.utils.ItemUtils.getInternalName
import at.hannibal2.skyhanni.utils.ItemUtils.getLore
import at.hannibal2.skyhanni.utils.LorenzUtils
import net.minecraft.client.Minecraft

object CopyItemCommand {

    fun command(args: Array<String>) {
        try {
            val resultList = mutableListOf<String>()
            val itemStack = Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem()!!
            resultList.add("ITEM LORE")
            resultList.add("display name: '" + itemStack.displayName.toString() + "'")
            val itemID = itemStack.getInternalName()
            resultList.add("internalName: '$itemID'")
            resultList.add("")
            for (line in itemStack.getLore()) {
                resultList.add("'$line'")
                println(line)
            }
            resultList.add("")
            resultList.add("getTagCompound")
            if (itemStack.hasTagCompound()) {
                val tagCompound = itemStack.tagCompound
                for (s in tagCompound.keySet) {
                    resultList.add("  '$s'")
                }
                if (tagCompound.hasKey("ExtraAttributes")) {
                    resultList.add("")
                    resultList.add("ExtraAttributes")
                    val extraAttributes = tagCompound.getCompoundTag("ExtraAttributes")
                    LorenzTest.runn(extraAttributes, "  .  ")
                }
            }

            val string = resultList.joinToString("\n")
            Utils.copyToClipboard(string)
            LorenzUtils.debug("item info printed!")
            LorenzUtils.chat("§e[SkyHanni] item info copied into the clipboard!")
        } catch (_: Throwable) {
            LorenzUtils.chat("§c[SkyHanni] No item in hand!")
        }
    }
}