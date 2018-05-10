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

import java.util.ArrayList;
import java.util.HashMap;

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
            final AspectList list = tileEntity.getAspects();
            final HashMap<Object,Object> out = new HashMap<>();
            for (Aspect a : list.getAspects()) {
                out.put(a.getName(), list.getAmount(a));
            }
            return new Object[]{out};
        }

        @Callback(doc = "function():table -- Get the names of aspects stored in the block")
        public Object[] getAspectNames(final Context context, final Arguments args) {
            final AspectList list = tileEntity.getAspects();
            Aspect[] array = list.getAspects();
            ArrayList<String> aspectNames = new ArrayList<>();
            for (int i = 0; i < array.length; i++) {
                aspectNames.add(array[i].getName());
            }
            return new Object[]{aspectNames};
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
