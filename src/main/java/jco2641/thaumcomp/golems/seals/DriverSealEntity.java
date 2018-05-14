package jco2641.thaumcomp.golems.seals;

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

/*    // Try the special way to get the seal entity
    @Override
    public ManagedEnvironment createEnvironment(final World world, BlockPos pos, EnumFacing side) {
        int dim = world.provider.getDimension();
        SealPos sealPos = new SealPos(pos,side);
        return new EnvironmentSealEntity(SealHandler.getSealEntity(dim,sealPos));
    }*/

}