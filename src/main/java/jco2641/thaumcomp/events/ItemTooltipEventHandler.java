package jco2641.thaumcomp.events;

import jco2641.thaumcomp.items.ItemSealConnector;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class ItemTooltipEventHandler {

    @SubscribeEvent(priority = EventPriority.NORMAL)

    public static void onEvent(ItemTooltipEvent event) {
        if (event.getItemStack().getItem() instanceof ItemSealConnector) {
            if ( event.getItemStack().hasTagCompound() ) {
                //get binding info and put into tool tip
                NBTTagCompound data = event.getItemStack().getTagCompound();
                if(data.hasKey("thaumcomp:coord")&&data.hasKey("thaumcomp:sealName")){
                    int[] tag = data.getIntArray("thaumcomp:coord");
                    String face = EnumFacing.getFront(tag[4]).getName();
                    String nametag = data.getString("thaumcomp:sealName");
                    String message = String.format("Seal connector bound to:\n%s\nDim: %d\nX: %d\nY: %d\nZ: %d\nFace: %s",nametag,tag[3],tag[0],tag[1],tag[2],face);
                    event.getToolTip().add(message);
                } else {
                    //have tag, but not bound - show not bound in tool tip
                    event.getToolTip().add("Seal connector not bound");
                }
            } else {
                //no tag - show not bound in tool tip
                event.getToolTip().add("Seal connector not bound");
            }
        }
        // Not a seal connector, don't do anything
    }
}
