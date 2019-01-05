package jco2641.thaumcomp.config;

import jco2641.thaumcomp.Reference;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid= Reference.MODID)
public class ModConfig {

    public static boolean allowSetArea                  = false;
    public static boolean allowSetColor                 = false;
    public static boolean allowSetLocked                = false;
    public static boolean allowSetRedstoneSensitive     = false;
    public static boolean allowSetPriority              = false;
    public static boolean allowSetFilterSlot            = false;
    public static boolean allowSetFilterSlotSize        = false;
    public static boolean allowSetBlacklist             = false;

    @Mod.EventBusSubscriber(modid = Reference.MODID)
    private static class EventHandler {
        @SubscribeEvent
        public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event ) {
            if (event.getModID().equals(Reference.MODID)) {
                ConfigManager.sync(Reference.MODID,Config.Type.INSTANCE);
            }
        }
    }
}
