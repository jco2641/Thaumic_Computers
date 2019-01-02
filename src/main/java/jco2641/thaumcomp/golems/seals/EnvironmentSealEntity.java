package jco2641.thaumcomp.golems.seals;

import jco2641.thaumcomp.util.ManagedTileEntityEnvironment;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.UsernameCache;

import thaumcraft.api.golems.EnumGolemTrait;
import thaumcraft.api.golems.seals.ISealConfigToggles;
import thaumcraft.api.golems.seals.ISealEntity;
import thaumcraft.api.golems.seals.ISealConfigFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public final class EnvironmentSealEntity extends ManagedTileEntityEnvironment<ISealEntity> {

    private World world;

    public EnvironmentSealEntity(final ISealEntity te, World world) {
        super(te,"golem_seal");
        this.world = world;
    }

    /* ================ *\
     * ==== ISeal ====  *
     * getForbiddenTags *
     * getRequiredTags  *
     * getType          *
    \* ================ */

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

    @Callback(doc = "function():string -- Get the type of the seal")
    public Object[] getType(final Context context, final Arguments args){
        final String sealType = tileEntity.getSeal().getKey();
        final String sealULName = String.format("item.seal.%s.name",sealType.split(":")[1]);
        return new Object[]{I18n.format(sealULName)};
    }

    /* ======================= *\
     * == ISealConfigFilter == *
     * getFilter               *
     * getFilterSize           *
     * getFilterSlot           *
     * getFilterSlotSize       *
     * getHasStackSizeLimiters *
    \* ======================= */

    @Callback(doc = "function():table -- Get the filter properties and contents")
    public Object[] getFilter(final Context context, final Arguments args){
        if(tileEntity.getSeal() instanceof ISealConfigFilter){

            final boolean blacklist = ((ISealConfigFilter) tileEntity.getSeal()).isBlacklist();
            final boolean limited = ((ISealConfigFilter) tileEntity.getSeal()).hasStacksizeLimiters();
            final int filterSize = ((ISealConfigFilter) tileEntity.getSeal()).getFilterSize();
            final HashMap<Object,Object> items = new HashMap<>();
            final HashMap<Object,Object> out = new HashMap<>();

            // Map: {blacklist=true/false,limited=true/false,items={item1=limit..itemN=limit}}

            out.put("blacklist",blacklist);
            out.put("limited",limited);

            for(int i = 0; i < filterSize; i++){
                items.put(((ISealConfigFilter) tileEntity.getSeal()).getFilterSlot(i),
                          ((ISealConfigFilter) tileEntity.getSeal()).getFilterSlotSize(i)
                );
            }

            out.put("items",items);

            return new Object[]{out};
        }
        return new Object[]{"No filter"};
    }

    @Callback(doc = "function():table -- Get the size of the filter in the seal")
    public Object[] getFilterSize(final Context context, final Arguments args){
        if(tileEntity.getSeal() instanceof ISealConfigFilter){
            int size = ((ISealConfigFilter)tileEntity.getSeal()).getFilterSize();
            return new Object[]{size};
        }
        return new Object[]{"No Filter"};
    }

    @Callback(doc = "function(slot:integer):String -- Get the item in specified filter slot")
    public Object[] getFilterSlot(final Context context, final Arguments args){
        if(tileEntity.getSeal() instanceof ISealConfigFilter){
            int slot = args.checkInteger(0);
            if (slot > 0) slot--;
            if (slot > ((ISealConfigFilter) tileEntity.getSeal()).getFilterSize()) return new Object[]{"Index out of range"};
            final ItemStack item = ((ISealConfigFilter) tileEntity.getSeal()).getFilterSlot(slot);
            return new Object[]{item.getDisplayName()};
        }
        return new Object[]{"No filter"};
    }

    @Callback(doc = "function(slot:integer):integer -- Get the stack size limit of specified filter slot")
    public Object[] getFilterSlotSize(final Context context, final Arguments args){
        if(tileEntity.getSeal() instanceof ISealConfigFilter){
            int slot = args.checkInteger(0);
            if (slot > 0) slot--;
            if (slot > ((ISealConfigFilter) tileEntity.getSeal()).getFilterSize()) return new Object[]{"Index out of range"};
            final int stacksize = ((ISealConfigFilter) tileEntity.getSeal()).getFilterSlotSize(slot);
            return new Object[]{stacksize};
        }
        return new Object[]{"No filter"};
    }

    @Callback(doc = "function():boolean -- Does filter have item stack size limits")
    public Object[] getHasStackSizeLimiters(final Context context, final Arguments args){
        if(tileEntity.getSeal() instanceof ISealConfigFilter){
            final boolean limited = ((ISealConfigFilter) tileEntity.getSeal()).hasStacksizeLimiters();
            return new Object[]{limited};
        }
        return new Object[]{"No filter"};
    }

    /* ====================== *\
     * = ISealConfigToggles = *
     * getProperties          *
    \* ====================== */

    @Callback(doc = "getProperties():table -- Get configuration toggles")
    public Object[] getProperties(final Context context, final Arguments args){
        final HashMap<Object,Object> filterProps = new HashMap<>();
        final HashMap<Object,Object> out = new HashMap<>();

        if(tileEntity.getSeal() instanceof ISealConfigToggles){
            final ISealConfigToggles.SealToggle[] toggles = (((ISealConfigToggles) tileEntity.getSeal()).getToggles());
            for(ISealConfigToggles.SealToggle s : toggles){
                filterProps.put(I18n.format(s.getName()), s.getValue());
            }
            out.put("properties",filterProps);
            return new Object[]{out};
        }
        return new Object[]{"No properties"};
    }

    /* ======================= *\
     * ===== ISealEntity ===== *
     * getArea                 *
     * getColor                *
     * getIsLocked             *
     * getIsRedstoneSensitive  *
     * getIsStoppedByRedstone  *
     * getOwner                *
     * getPriority             *
    \* ======================= */

    @Callback(doc = "function():table -- Get the seal effect dimensions")
    public Object[] getArea(final Context context, final Arguments args){
        final BlockPos area = tileEntity.getArea();
        final HashMap<Object,Object> out = new HashMap<>();
        out.put("X", area.getX());
        out.put("Y", area.getY());
        out.put("Z", area.getZ());
        return new Object[]{out};
    }

    @Callback(doc = "function():string -- Get the seal color")
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

    @Callback(doc = "function():boolean -- Is the seal locked to only controlling owner's golems")
    public Object[] getIsLocked(final Context context, final Arguments args) {
        final boolean locked = tileEntity.isLocked();
        return new Object[]{locked};
    }

    @Callback(doc = "function():boolean -- Does the seal get disabled by redstone signals")
    public Object[] getIsRedstoneSensitive(final Context context, final Arguments args) {
        final boolean sensitive = tileEntity.isRedstoneSensitive();
        return new Object[]{sensitive};
    }

    @Callback(doc = "function():boolean -- Is the seal currently disabled by redstone signal")
    public Object[] getIsStoppedByRedstone(final Context context, final Arguments args){
        final boolean stopped = tileEntity.isStoppedByRedstone(world);
        return new Object[]{stopped};
    }

    @Callback(doc = "function():string -- Get the name of the owner of the seal")
    public Object[] getOwner(final Context context, final Arguments args) {
        final String owner = tileEntity.getOwner();   // player.getUniqueID() of player that placed it
        final String ownerName = UsernameCache.getLastKnownUsername(UUID.fromString(owner));
        return new Object[]{ownerName};
    }

    @Callback(doc = "function():int -- Get the priority of golem tasks created by this seal")
    public Object[] getPriority(final Context context, final Arguments args){
        final byte priority = tileEntity.getPriority();
        return new Object[] {priority};
    }
}
