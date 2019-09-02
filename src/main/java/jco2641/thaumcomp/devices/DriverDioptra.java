package jco2641.thaumcomp.devices;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.prefab.DriverSidedTileEntity;
import li.cil.oc.api.network.ManagedEnvironment;
import thaumcraft.api.aura.AuraHelper;
import thaumcraft.common.tiles.devices.TileDioptra;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import jco2641.thaumcomp.util.ManagedTileEntityEnvironment;

/*
    Derived from work Copyright (c) 2013-2015 Florian "Sangar" Nücke published under MIT license
*/

public class DriverDioptra extends DriverSidedTileEntity {
    @Override
    public Class<?> getTileEntityClass() {
        return TileDioptra.class;
    }

    @Override
    public ManagedEnvironment createEnvironment(final World world, BlockPos pos, EnumFacing side) {
        return new EnvironmentAspectContainer((TileDioptra)world.getTileEntity(pos));
    }

    public static final class EnvironmentAspectContainer extends ManagedTileEntityEnvironment<TileDioptra> {
        public EnvironmentAspectContainer(final TileDioptra dioptra) {
            super(dioptra, "thaumic_dioptra");
        }

        @Callback(doc = "function():number -- Get the vis in the current chunk")
        public Object[] getVis(final Context context, final Arguments args) {
            final float vis = AuraHelper.getVis(tileEntity.getWorld(), tileEntity.getPos());
            return new Object[]{vis};
        }

        @Callback(doc = "function():number -- Get the flux in the current chunk")
        public Object[] getFlux(final Context context, final Arguments args) {
            final float flux = AuraHelper.getFlux(tileEntity.getWorld(), tileEntity.getPos());
            return new Object[]{flux};
        }
    }
}
