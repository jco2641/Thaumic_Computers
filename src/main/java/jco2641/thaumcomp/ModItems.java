package jco2641.thaumcomp;

import jco2641.thaumcomp.items.ItemSealConnector;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems {

/*
    // Static instances of assets.thaumcomp.textures.items classes
    @GameRegistry.ObjectHolder("thaumcomp:itemname")
    public static ItemClass itemClass;
*/
    @GameRegistry.ObjectHolder("thaumcomp:sealconnector")
    public static ItemSealConnector itemSealConnector;

    // To make items from their JSON block models
    @SideOnly(Side.CLIENT)
    public static void initModels() {
        itemSealConnector.initModel();
    }

}
