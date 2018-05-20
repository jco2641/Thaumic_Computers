package jco2641.thaumcomp.items;

import jco2641.thaumcomp.Reference;
import li.cil.oc.api.Driver;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.golems.ISealDisplayer;
import thaumcraft.api.golems.seals.ISealEntity;
import thaumcraft.api.golems.seals.SealPos;
import thaumcraft.common.entities.construct.golem.seals.SealHandler;

public class ItemSealConnector extends Item implements ISealDisplayer {

    public ItemSealConnector() {
        setRegistryName("sealconnector");
        setUnlocalizedName(Reference.MODID + ".sealconnector");
        setMaxStackSize(1);
        setCreativeTab(CreativeTabs.MISC);
    }

    //TODO: onRightClick to show info when clicked in the air


    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this,0,new ModelResourceLocation(getRegistryName(),"inventory"));
    }

    int tierFromDriver(ItemStack stack){
        return Driver.driverFor(stack).tier(stack);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);

        if(!world.isRemote){
            if(stack.hasTagCompound()){
                NBTTagCompound data = stack.getTagCompound();
                if(data.hasNoTags()){
                    player.sendMessage(new TextComponentString("Seal connector not bound"));
                    return new ActionResult<>(EnumActionResult.PASS,stack);
                }
                if(!player.isSneaking()) {
                    //display binding when not sneaking
                    if(data.hasKey("thaumcomp:coord")&&data.hasKey("thaumcomp:sealName")){
                        int[] tag = data.getIntArray("thaumcomp:coord");
                        String face = EnumFacing.getFront(tag[4]).getName();
                        String nametag = data.getString("thaumcomp:sealName");
                        String message = String.format("Seal connector bound to:\n%s\nDim: %d\nX: %d\nY: %d\nZ: %d\nFace: %s",nametag,tag[3],tag[0],tag[1],tag[2],face);
                        player.sendMessage(new TextComponentString(message));
                    } else {
                        player.sendMessage(new TextComponentString("Seal connector not bound"));
                        return new ActionResult<>(EnumActionResult.PASS,stack);
                    }

                    return new ActionResult<>(EnumActionResult.SUCCESS,stack);
                } else {
                    //clear binding when sneaking
                    data.removeTag("thaumcomp:sealName");
                    data.removeTag("thaumcomp:coord");
                    player.sendMessage(new TextComponentString("Seal connector binding removed"));
                    return new ActionResult<>(EnumActionResult.SUCCESS,stack);
                }
            } else {
                player.sendMessage(new TextComponentString("Seal connector not bound"));
            }
        }
        return new ActionResult<>(EnumActionResult.PASS,stack);
    }
//FIXME: both of them are getting called when clicking on a block :(
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if(!world.isRemote) {
            if(player.isSneaking()) {
                int dim = world.provider.getDimension();
                ISealEntity se = SealHandler.getSealEntity(dim, new SealPos(pos, side));
                if (se != null) {
                    player.swingArm(hand);
                    if (!stack.hasTagCompound()) {
                        stack.setTagCompound(new NBTTagCompound());
                    }
                    NBTTagCompound data = stack.getTagCompound();
                    String sealtype = se.getSeal().getKey();
                    String sealULName = String.format("item.seal.%s.name",sealtype.split(":")[1]);
                    String sealName = I18n.format(sealULName);
                    data.setString("thaumcomp:sealName", sealName);
                    data.setIntArray("thaumcomp:coord", new int[]{pos.getX(), pos.getY(), pos.getZ(), dim, side.getIndex()});
                    ITextComponent message = new TextComponentString("Binding to:\n");
                    message.appendText(sealName);
                    message.appendText(String.format("\nDim: %d\nX: %d\nY: %d\nZ: %d\nFace: %s",dim,pos.getX(),pos.getY(),pos.getZ(),side.getName()));
                    player.sendMessage(message);

                    return EnumActionResult.SUCCESS;
                } else {
                    player.sendMessage(new TextComponentString("No seal there"));
                }
            } else {
                if (stack.hasTagCompound()) {
                    NBTTagCompound data = stack.getTagCompound();
                    if (data.hasKey("thaumcomp:coord") && data.hasKey("thaumcomp:sealName")) {
                        int[] tag = data.getIntArray("thaumcomp:coord");
                        String face = EnumFacing.getFront(tag[4]).getName();
                        String nametag = data.getString("thaumcomp:sealName");
                        String message = String.format("Seal connector bound to:\n%s\nDim: %d\nX: %d\nY: %d\nZ: %d\nFace: %s", nametag, tag[3], tag[0], tag[1], tag[2], face);
                        player.sendMessage(new TextComponentString(message));
                    } else {
                        player.sendMessage(new TextComponentString("Seal connector not bound"));
                    }
                } else {
                    player.sendMessage(new TextComponentString("Seal connector not bound"));
                }
            }
        }
        return EnumActionResult.SUCCESS;
    }
}
