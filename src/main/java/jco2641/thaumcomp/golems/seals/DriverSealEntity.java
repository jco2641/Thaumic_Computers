package jco2641.thaumcomp.golems.seals;

import jco2641.thaumcomp.util.ManagedTileEntityEnvironment;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverSidedTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thaumcraft.api.golems.seals.ISealEntity;

public class DriverSealEntity extends DriverSidedTileEntity {

    @Override
    public Class<?> getTileEntityClass() {
        return ISealEntity.class;
    }

    @Override
    public ManagedEnvironment createEnvironment(final World world, BlockPos pos, EnumFacing side) {
        return new EnvironmentSealEntity((ISealEntity) world.getTileEntity(pos));
    }

    public static final class EnvironmentSealEntity extends ManagedTileEntityEnvironment<ISealEntity> {

        public EnvironmentSealEntity(final ISealEntity te ) {
            super(te,"golem_seal");
        }

        //TODO: API Functions go here

    }

}