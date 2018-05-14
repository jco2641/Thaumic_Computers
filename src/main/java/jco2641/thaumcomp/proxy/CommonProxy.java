package jco2641.thaumcomp.proxy;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.RegistryEvent;

import li.cil.oc.api.Driver;

import jco2641.thaumcomp.aspects.ConverterAspectItem;
import jco2641.thaumcomp.aspects.DriverAspectContainer;
import jco2641.thaumcomp.golems.seals.DriverSeal;
import jco2641.thaumcomp.golems.seals.DriverSealEntity;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        // Do things
    }

    public void init(FMLInitializationEvent event) {

        // Add OC Drivers
        Driver.add(new DriverAspectContainer());
        Driver.add(new ConverterAspectItem());

        //  FIXME: Seals are tile entities but the scanning that the adapter does is not detecting them
        Driver.add(new DriverSealEntity());
        Driver.add(new DriverSeal());

    }

    public void postInit(FMLPostInitializationEvent event) {
        // Do things
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        // Do things
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
/*
        // Basic item
        event.getRegistry().register(new ItemClass());
*/
    }
}
