package jco2641.thaumcomp.golems.seals;

import jco2641.thaumcomp.util.ManagedTileEntityEnvironment;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.UsernameCache;
import thaumcraft.api.golems.EnumGolemTrait;
import thaumcraft.api.golems.seals.ISealConfigToggles;
import thaumcraft.api.golems.seals.ISealEntity;
import thaumcraft.common.entities.construct.golem.seals.SealFiltered;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public final class EnvironmentSealEntity extends ManagedTileEntityEnvironment<ISealEntity> {

    public EnvironmentSealEntity(final ISealEntity te ) {
        super(te,"golem_seal");
    }

    @Callback(doc = "function():table -- Get the name of the owner of the seal")
    public Object[] getOwner(final Context context, final Arguments args) {
        final String owner = tileEntity.getOwner();   // player.getUniqueID() of player that placed it
        final String ownerName = UsernameCache.getLastKnownUsername(UUID.fromString(owner));
        return new Object[]{ownerName};
    }

    @Callback(doc = "function():table -- Get the type of the seal")
    public Object[] getType(final Context context, final Arguments args){
        final String sealType = tileEntity.getSeal().getKey();
        final String sealULName = String.format("item.seal.%s.name",sealType.split(":")[1]);
        return new Object[]{I18n.format(sealULName)};
    }

    @Callback(doc = "function():table -- Get the seal effect dimensions")
    public Object[] getArea(final Context context, final Arguments args){
        final BlockPos area = tileEntity.getArea();
        final HashMap<Object,Object> out = new HashMap<>();
        out.put("X", area.getX());
        out.put("Y", area.getY());
        out.put("Z", area.getZ());
        return new Object[]{out};
    }

    @Callback(doc = "function():table -- Get the seal color")
    public Object[] getColor(final Context context, final Arguments args) {
        final byte color = tileEntity.getColor();
        String colorName;
        if(color==0){
            colorName = "All";
        } else {
            colorName = EnumDyeColor.byMetadata(color - 1).getName();
        }
        return new Object[]{colorName};
    }

    @Callback(doc = "function():table -- Does the seal get disabled by redstone signals")
    public Object[] getRedstoneSensitive(final Context context, final Arguments args) {
        final boolean sensitive = tileEntity.isRedstoneSensitive();
        return new Object[]{sensitive};
    }

    @Callback(doc = "function:table -- Get the tags golems must have to obey this seal")
    public Object[] getRequiredTags(final Context context, final Arguments args) {
        final EnumGolemTrait[] tags = tileEntity.getSeal().getRequiredTags();
        final ArrayList<String> out = new ArrayList<>();

        if(tags == null || tags.length < 1 ) { return new Object[]{"No tags"}; }

        for (EnumGolemTrait trait : tags){
            out.add(trait.getLocalizedName());
        }
        return new Object[]{out};
    }

    @Callback(doc = "function:table -- Get the tags golems must not have to obey this seal")
    public Object[] getForbiddenTags(final Context context, final Arguments args) {
        final EnumGolemTrait[] tags = tileEntity.getSeal().getForbiddenTags();
        final ArrayList<String> out = new ArrayList<>();

        if(tags == null || tags.length < 1) { return new Object[]{"No tags"}; }

        for (EnumGolemTrait trait : tags){
            out.add(trait.getLocalizedName());
        }
        return new Object[]{out};
    }

    @Callback(doc = "function():table -- Get the size of the filter in the seal")
    public Object[] getFilterSize(final Context context, final Arguments args){
        if(tileEntity.getSeal() instanceof SealFiltered){
            int size = ((SealFiltered)tileEntity.getSeal()).getFilterSize();
            return new Object[]{size};
        }
        return new Object[]{"0"};
    }

    @Callback(doc = "function():table -- Get the filter properties and contents")
    public Object[] getFilter(final Context context, final Arguments args){
        if(tileEntity.getSeal() instanceof SealFiltered){

            final boolean blacklist = ((SealFiltered) tileEntity.getSeal()).isBlacklist();
            final NonNullList<ItemStack> itemStacks = ((SealFiltered) tileEntity.getSeal()).getInv();
            final HashMap<Object,Object> out = new HashMap<>();
            final ArrayList<Object> items = new ArrayList<>();
            final HashMap<Object,Object> filterProps = new HashMap<>();

            // Without iSealConfigToggles
            // Map: {blacklist=true/false,items=[item1,item2..itemN]}

            out.put("blacklist",blacklist);

            for(ItemStack s : itemStacks){
                items.add(s.getDisplayName());
            }
            out.put("items",items);

            // With iSealConfigToggles
            // Add Map: {properties={name1=value1..nameN=valueN}}
            if(tileEntity.getSeal() instanceof ISealConfigToggles){
                final ISealConfigToggles.SealToggle[] toggles = (((ISealConfigToggles) tileEntity.getSeal()).getToggles());
                for(ISealConfigToggles.SealToggle s : toggles){
                    filterProps.put(I18n.format(s.getName()), s.getValue());
                }
                out.put("properties",filterProps);
            }
            return new Object[]{out};
        }
        return new Object[]{"No filter"};
    }
}
