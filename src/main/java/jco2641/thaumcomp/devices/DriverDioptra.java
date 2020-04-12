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

import java.util.ArrayList;

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

        @Callback(doc = "function():float -- Get the vis in the current chunk")
        public Object[] getVis(final Context context, final Arguments args) {
            final float vis = AuraHelper.getVis(tileEntity.getWorld(), tileEntity.getPos());
            return new Object[]{vis};
        }

        @Callback(doc = "function():float -- Get the flux in the current chunk")
        public Object[] getFlux(final Context context, final Arguments args) {
            final float flux = AuraHelper.getFlux(tileEntity.getWorld(), tileEntity.getPos());
            return new Object[]{flux};
        }

        @Callback(doc = "function(int N:number of chunks):list(float) -- Get the Vis in the surrounding N(<7) chunk radius")
        public Object[] getVisMap(final Context context, final Arguments args) {
            ArrayList<Float> visValues = new ArrayList<>();
            int radius;
            if ( args.checkInteger(0) > 5 ) {
                radius = 6;
            } else {
                radius = args.checkInteger(0);
            }
            int offs = (16 * radius);
            for (int zoffs = -offs; zoffs <= offs; zoffs += 16) {
                for ( int xoffs = -offs; xoffs <= offs; xoffs += 16 ) {
                    visValues.add((float) AuraHelper.getVis(tileEntity.getWorld(), tileEntity.getPos().add(xoffs, 0, zoffs)));
                }
            }
            return new Object[]{visValues};
        }

        @Callback(doc = "function(int N: number of chunks):list(float) -- Get the Flux in the surrounding N(<7) chunk radius")
        public Object[] getFluxMap(final Context context, final Arguments args) {
            ArrayList<Float> fluxValues = new ArrayList<>();
            int radius;
            if (args.checkInteger(0) > 5) {
                radius = 6;
            } else {
                radius = args.checkInteger(0);
            }
            int offs = (16 * radius);
            for (int zoffs = -offs; zoffs <= offs; zoffs += 16) {
                for (int xoffs = -offs; xoffs <= offs; xoffs += 16) {
                    fluxValues.add((float) AuraHelper.getFlux(tileEntity.getWorld(), tileEntity.getPos().add(xoffs, 0, zoffs)));
                }
            }
            return new Object[]{fluxValues};
        }
    }
}
