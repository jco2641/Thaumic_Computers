package jco2641.thaumcomp.golems.seals;

import jco2641.thaumcomp.items.ItemSealConnector;
import li.cil.oc.api.driver.EnvironmentProvider;
import li.cil.oc.api.driver.item.HostAware;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.network.EnvironmentHost;
import li.cil.oc.api.prefab.DriverItem;
import li.cil.oc.api.driver.item.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import thaumcraft.api.golems.seals.ISealEntity;
import thaumcraft.api.golems.seals.SealPos;
import thaumcraft.api.golems.GolemHelper;
import thaumcraft.common.entities.construct.golem.seals.SealHandler;

public class DriverSealConnector extends DriverItem implements HostAware, EnvironmentProvider {

    @Override
    public Class getEnvironment(ItemStack stack){
        return EnvironmentSealEntity.class;
    }

    @Override
    public boolean worksWith(ItemStack stack, Class<? extends EnvironmentHost> host){
        return isAdapter(host) && worksWith(stack);
    }

    @Override
    public boolean worksWith(ItemStack stack){
        return stack.getItem() instanceof ItemSealConnector;
    }

    @Override
    public String slot(ItemStack stack) { return Slot.Upgrade; }

    @Override
    public int tier(final ItemStack stack) {
        return 1;
    } //Tier 2

    @Override
    public ManagedEnvironment createEnvironment(ItemStack stack, EnvironmentHost host) {
        if (host.world() != null && !host.world().isRemote){
            if(stack.hasTagCompound()){
                NBTTagCompound data = stack.getTagCompound();
                if(data.hasKey("thaumcomp:coord")){
                    int[] tag = data.getIntArray("thaumcomp:coord");  //x,y,z,dim,side
                    if(host.world().provider.getDimension() == tag[3]){
                        BlockPos pos = new BlockPos(tag[0],tag[1],tag[2]);
                        EnumFacing facing = EnumFacing.getFront(tag[4]);
                        ISealEntity se = GolemHelper.getSealEntity(tag[3], new SealPos(pos, facing));
                        if(se != null){
                            return new EnvironmentSealEntity(se);
                        }
                    }
                }
            }
        }
        return null;
    }
}
