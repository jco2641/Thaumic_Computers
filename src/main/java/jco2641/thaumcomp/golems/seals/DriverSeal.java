package jco2641.thaumcomp.golems.seals;

import jco2641.thaumcomp.util.ManagedTileEntityEnvironment;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverSidedTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thaumcraft.api.golems.seals.ISeal;

public class DriverSeal extends DriverSidedTileEntity {

    @Override
    public Class<?> getTileEntityClass() {
        return ISeal.class;
    }

    @Override
    public ManagedEnvironment createEnvironment(final World world, BlockPos pos, EnumFacing side) {
        return new EnvironmentSeal((ISeal) world.getTileEntity(pos));
    }

    public static final class EnvironmentSeal extends ManagedTileEntityEnvironment<ISeal> {

        public EnvironmentSeal(final ISeal te ) {
            super(te,"golem_seal");
        }

        //TODO: API Functions go here
        @Callback(doc = "function():String -- Get my name")
        public Object[] getName(final Context context, final Arguments args) {
            String name = tileEntity.getKey();
            return new Object[]{name};
        }

    }

}