package at.hannibal2.skyhanni.features.misc.tiarelay

import at.hannibal2.skyhanni.SkyHanniMod
import at.hannibal2.skyhanni.data.IslandType
import at.hannibal2.skyhanni.events.LorenzChatEvent
import at.hannibal2.skyhanni.test.GriffinUtils.drawWaypointFilled
import at.hannibal2.skyhanni.utils.LorenzColor
import at.hannibal2.skyhanni.utils.LorenzUtils
import at.hannibal2.skyhanni.utils.LorenzVec
import at.hannibal2.skyhanni.utils.RenderUtils.drawDynamicText
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class TiaRelayWaypoints {
    private var waypoint: LorenzVec? = null
    private var waypointName: String? = null
    private var island = IslandType.NONE

    @SubscribeEvent
    fun onChatMessage(event: LorenzChatEvent) {
        if (!LorenzUtils.inSkyBlock) return
        if (!SkyHanniMod.feature.misc.tiaRelayNextWaypoint) return

        val message = event.message
        for (relay in Relay.values()) {
            if (relay.chatMessage == message) {
                waypoint = relay.waypoint
                waypointName = relay.relayName
                island = relay.island
                return
            }
        }

        if (message == "§aYou completed the maintenance on the relay!") {
            waypoint = null
            island = IslandType.NONE
        }
    }

    @SubscribeEvent
    fun onRenderWorld(event: RenderWorldLastEvent) {
        if (!LorenzUtils.inSkyBlock) return

        if (SkyHanniMod.feature.misc.tiaRelayAllWaypoints) {
            for (relay in Relay.values()) {
                if (relay.island == LorenzUtils.skyBlockIsland) {
                    event.drawWaypointFilled(relay.waypoint, LorenzColor.LIGHT_PURPLE.toColor())
                    event.drawDynamicText(relay.waypoint, "§d" + relay.relayName, 1.5)
                }
            }
            return
        }

        if (!SkyHanniMod.feature.misc.tiaRelayNextWaypoint) return
        if (LorenzUtils.skyBlockIsland != island) return

        waypoint?.let {
            event.drawWaypointFilled(it, LorenzColor.LIGHT_PURPLE.toColor())
            event.drawDynamicText(it, "§d" + waypointName!!, 1.5)
        }
    }
}