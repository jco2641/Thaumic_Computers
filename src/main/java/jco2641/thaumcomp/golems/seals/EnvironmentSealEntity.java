package jco2641.thaumcomp.golems.seals;

import jco2641.thaumcomp.util.ManagedTileEntityEnvironment;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.UsernameCache;
import thaumcraft.api.golems.seals.ISealEntity;

import java.util.HashMap;
import java.util.UUID;

public final class EnvironmentSealEntity extends ManagedTileEntityEnvironment<ISealEntity> {

    public EnvironmentSealEntity(final ISealEntity te ) {
        super(te,"golem_seal");
    }

    //TODO: API Functions go here
    @Callback(doc = "function():table -- Get the name of the owner of the seal")
    public Object[] getOwner(final Context context, final Arguments args) {
        String owner = tileEntity.getOwner();   // player.getUniqueID() of player that placed it
        String ownerName = UsernameCache.getLastKnownUsername(UUID.fromString(owner));
        return new Object[]{ownerName};
    }

    @Callback(doc = "function():table -- Get the type of the seal")
    public Object[] getType(final Context context, final Arguments args){
        String sealtype = tileEntity.getSeal().getKey();
        String sealULName = String.format("item.seal.%s.name",sealtype.split(":")[1]);
        return new Object[]{I18n.format(sealULName)};
    }

    @Callback(doc = "function():table -- Get the seal effect dimensions")
    public Object[] getArea(final Context context, final Arguments args){
        BlockPos area = tileEntity.getArea();
        final HashMap<Object,Object> out = new HashMap<>();
        out.put("X", area.getX());
        out.put("Y", area.getY());
        out.put("Z", area.getZ());
        return new Object[]{out};
    }
}
