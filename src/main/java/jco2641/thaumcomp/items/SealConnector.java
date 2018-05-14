package jco2641.thaumcomp.items;

import jco2641.thaumcomp.Reference;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SealConnector extends Item {
    public SealConnector() {
        setRegistryName("sealconnector");
        setUnlocalizedName(Reference.MODID + ".sealconnector");
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this,0,new ModelResourceLocation(getRegistryName(),"inventory"));
    }
}
