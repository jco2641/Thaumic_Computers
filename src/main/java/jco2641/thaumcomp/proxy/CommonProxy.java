package jco2641.thaumcomp.proxy;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.RegistryEvent;

import li.cil.oc.api.Driver;
import li.cil.oc.api.driver.DriverItem;

import jco2641.thaumcomp.aspects.ConverterAspectItem;
import jco2641.thaumcomp.aspects.DriverAspectContainer;
import jco2641.thaumcomp.golems.seals.DriverSealConnector;
import jco2641.thaumcomp.devices.DriverDioptra;
import jco2641.thaumcomp.items.ItemSealConnector;

@Mod.EventBusSubscriber
public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        // Do things
    }

    public void init(FMLInitializationEvent event) {

        // Add OC Drivers
        Driver.add(new DriverDioptra());
        Driver.add(new DriverAspectContainer());
        Driver.add(new ConverterAspectItem());
        Driver.add((DriverItem) new DriverSealConnector());
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
        // Basic assets.thaumcomp.textures.items
        event.getRegistry().register(new ItemClass());
*/
        event.getRegistry().register(new ItemSealConnector());
    }
}
