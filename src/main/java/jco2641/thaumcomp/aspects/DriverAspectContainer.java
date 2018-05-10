package jco2641.thaumcomp.aspects;

import com.google.common.base.Preconditions;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.prefab.DriverSidedTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import li.cil.oc.api.network.ManagedEnvironment;
import net.minecraft.world.World;
import jco2641.thaumcomp.util.ManagedTileEntityEnvironment;

public class DriverAspectContainer extends DriverSidedTileEntity {

    @Override
    public Class<?> getTileEntityClass() {
        return IAspectContainer.class;
    }

    @Override
    public ManagedEnvironment createEnvironment(final World world, BlockPos pos, EnumFacing side) {
        return new EnvironmentAspectContainer((IAspectContainer) world.getTileEntity(pos));
    }

    public static final class EnvironmentAspectContainer extends ManagedTileEntityEnvironment<IAspectContainer> {

        public EnvironmentAspectContainer( final IAspectContainer te ){
            super(te,"aspect container");
        }

        @Callback(doc = "function():table -- Get the Aspects stored in the block")
        public Object[] getAspects(final Context context, final Arguments args) {
            return new Object[]{tileEntity};
        }

        @Callback(doc = "function(aspect:string):number -- Get amount of specific aspect stored in this block")
        public Object[] getAspectCount(final Context context, final Arguments args) {
            final Aspect aspect = Aspect.getAspect(args.checkString(0).toLowerCase());
            Preconditions.checkNotNull(aspect, "Invalid aspect name");
            final AspectList list = tileEntity.getAspects();
            return new Object[]{list.getAmount(aspect)};
        }
    }
}
