package jco2641.thaumcomp.golems.seals;

import jco2641.thaumcomp.config.ModConfig;
import jco2641.thaumcomp.util.ManagedTileEntityEnvironment;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.UsernameCache;

import thaumcraft.api.golems.EnumGolemTrait;
import thaumcraft.api.golems.seals.ISealConfigArea;
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
     * getIsBlacklist          *
     * ----- setters -----     *
     * setFilterSlot           *
     * setFilterSlotSize       *
     * setIsBlacklist          *
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
            if (slot > ((ISealConfigFilter) tileEntity.getSeal()).getFilterSize() || slot < 1) return new Object[]{"Index out of range"};
            slot--;
            final ItemStack item = ((ISealConfigFilter) tileEntity.getSeal()).getFilterSlot(slot);
            return new Object[]{item.getDisplayName()};
        }
        return new Object[]{"No filter"};
    }

    @Callback(doc = "function(slot:integer):integer -- Get the stack size limit of specified filter slot")
    public Object[] getFilterSlotSize(final Context context, final Arguments args){
        if(tileEntity.getSeal() instanceof ISealConfigFilter){
            int slot = args.checkInteger(0);
            if (slot > ((ISealConfigFilter) tileEntity.getSeal()).getFilterSize() || slot < 1) return new Object[]{"Slot index out of range"};
            slot--;
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

    @Callback(doc = "function():boolean -- Is the filter a blacklist or whitelist")
    public Object[] getIsBlacklist(final Context context, final Arguments args) {
        if(tileEntity.getSeal() instanceof ISealConfigFilter){
            final boolean blacklist = ((ISealConfigFilter) tileEntity.getSeal()).isBlacklist();
            return new Object[]{blacklist};
        }
        return new Object[]{"No filter"};
    }

    @Callback(doc = "function(slot:int, item:string)string -- Set item in specified filter slot, returns item in slot")
    public Object[] setFilterSlot(final Context context, final Arguments args) {
        if(ModConfig.allowSetFilterSlot) {
            if(tileEntity.getSeal() instanceof ISealConfigFilter){
                int slot = args.checkInteger(0);
                if (slot > ((ISealConfigFilter) tileEntity.getSeal()).getFilterSize() || slot < 1) return new Object[]{"Slot index out of range"};
                slot--;
                String name = args.checkString(1);
                ResourceLocation rlItem = new ResourceLocation(name);
                Item item = Item.REGISTRY.getObject(rlItem);
                ItemStack itemStack;
                if(item != null) {
                    itemStack = new ItemStack(item);
                } else {
                    return new Object[] {"No such item" + name};
                }
                ((ISealConfigFilter) tileEntity.getSeal()).setFilterSlot(slot,itemStack);
            } else {
                return new Object[]{"No filter"};
            }
        } else {
            return new Object[] {"API action not allowed, see config"};
        }
        return getFilterSlot(null,args);
    }

    @Callback(doc = "function(slot:int,size:int):string -- Set size of item stack in filter slot, returns slot size")
    public Object[] setFilterSlotSize(final Context context, final Arguments args) {
        if(ModConfig.allowSetFilterSlotSize){
            if(tileEntity.getSeal() instanceof ISealConfigFilter) {
                int slot = args.checkInteger(0);
                int size = args.checkInteger(1);
                if (slot > ((ISealConfigFilter) tileEntity.getSeal()).getFilterSize() || slot < 1) return new Object[]{"Slot index out of range"};
                slot--;
                if(getFilterSlot(null,args)[0].toString().equalsIgnoreCase("air")) return getFilterSlotSize(null, args);
                ((ISealConfigFilter) tileEntity.getSeal()).setFilterSlotSize(slot,size);
            } else {
                return new Object[] {"No Filter"};
            }
        } else {
            return new Object[] {"API action not allowed, see config"};
        }
        return getFilterSlotSize(null, args);
    }

    @Callback(doc = "function(blacklist:boolean):boolean -- Sets if the seal filter is a blacklist or whitelist, returns if the filter is blacklist or whitelist")
    public Object[] setIsBlacklist(final Context context, final Arguments args) {
        if(ModConfig.allowSetBlacklist) {
            if(tileEntity.getSeal() instanceof ISealConfigFilter) {
                boolean bl = args.checkBoolean(0);
                ((ISealConfigFilter) tileEntity.getSeal()).setBlacklist(bl);
            } else {
                return new Object[] {"No Filter"};
            }
        } else {
            return new Object[] {"API action not allowed, see config"};
        }
        return getIsBlacklist(null,null);
    }

    /* ====================== *\
     * = ISealConfigToggles = *
     * getProperties          *
    \* ====================== */

    @Callback(doc = "function():table -- Get configuration toggles")
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
     * ------- Setters ------- *
     * setArea                 *
     * setColor                *
     * setIsLocked             *
     * setIsRedstoneSensitive  *
     * setPriority             *
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

    @Callback (doc = "function(x:int,y:int,z:int):table -- Set the Area affected by the seal, returns area value")
    public Object[] setArea(final Context context, final Arguments args) {
        if(ModConfig.allowSetArea) {
            int x = args.checkInteger(0);
            int y = args.checkInteger(1);
            int z = args.checkInteger(2);
            //clamp values to [0-8] because those are limits TC gui will permit
            x = x > 8 ? 8 : x < 0 ? 0 : x;
            y = y > 8 ? 8 : y < 0 ? 0 : y;
            z = z > 8 ? 8 : z < 0 ? 0 : z;
            BlockPos pos = new BlockPos(x, y, z);
            if (tileEntity.getSeal() instanceof ISealConfigArea) {
                tileEntity.setArea(pos);
                return getArea(null,null);
            }
        } else {
            return new Object[] {"API action not allowed, see config"};
        }
        return getArea(null,null);
    }

    @Callback(doc = "function(color:int):string -- Set the color of the seal, returns seal color")
    public Object[] setColor(final Context context, final Arguments args) {
        //Valid values are -1-15, where -1 is any color, and 0-15 correspond to the standard minecraft colors (white..black)
        if(ModConfig.allowSetColor) {
            int c = args.checkInteger(0);
            c = c > 15 ? 15 : c < -1 ? -1 : c;
            c++;
            tileEntity.setColor((byte)c);
        } else {
            return new Object[] {"API action not allowed, see config"};
        }
        return getColor(null,null);
    }

    @Callback(doc = "function(locked:boolean):boolean -- Set the locked state of the seal, returns lock state")
    public Object[] setIsLocked(final Context context, final Arguments args) {
        if(ModConfig.allowSetLocked) {
            boolean l = args.checkBoolean(0);
            tileEntity.setLocked(l);
        } else {
            return new Object[] {"API action not allowed, see config"};
        }
        return getIsLocked(null,null);
    }

    @Callback(doc = "function(sensitive:boolean):boolean -- Set the redstone sensitive state of the seal, returns state")
    public Object[] setIsRedstoneSensitive(final Context context, final Arguments args) {
        if(ModConfig.allowSetRedstoneSensitive) {
            boolean s = args.checkBoolean(0);
            tileEntity.setRedstoneSensitive(s);
        } else {
            return new Object[] {"API action not allowed, see config"};
        }
        return getIsRedstoneSensitive(null,null);
    }

    @Callback(doc = "function(priority:int):int -- Set the seal task priority, returns priority")
    public Object[] setPriority(final Context context, final Arguments args) {
        if(ModConfig.allowSetPriority) {
            int p = args.checkInteger(0);
            p = p > 5 ? 5 : p < -5 ? -5 : p;
            tileEntity.setPriority((byte)p);
        } else {
            return new Object[] {"API action not allowed, see config"};
        }
        return getPriority(null,null);
    }

}
