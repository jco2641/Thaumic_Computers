package jco2641.thaumcomp.golems.seals;

import jco2641.thaumcomp.util.ManagedTileEntityEnvironment;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import thaumcraft.api.golems.seals.ISealEntity;

public final class EnvironmentSealEntity extends ManagedTileEntityEnvironment<ISealEntity> {

    public EnvironmentSealEntity(final ISealEntity te ) {
        super(te,"golem_seal");
    }

    //TODO: API Functions go here
    @Callback(doc = "function():table -- Get the name of the owner of the seal")
    public Object[] getOwner(final Context context, final Arguments args) {
        return new Object[]{tileEntity.getOwner()};
    }

}
