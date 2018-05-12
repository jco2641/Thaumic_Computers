package jco2641.thaumcomp;

import jco2641.thaumcomp.aspects.ConverterAspectItem;
import jco2641.thaumcomp.aspects.DriverAspectContainer;
import jco2641.thaumcomp.golems.seals.DriverSeal;
import jco2641.thaumcomp.golems.seals.DriverSealEntity;
import li.cil.oc.api.Driver;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION, dependencies = Reference.DEPENDENCIES)
public class ThaumicComputers {

    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        logger.info("Done with preInit phase.");
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        Driver.add(new DriverAspectContainer());
        Driver.add(new ConverterAspectItem());

/*  These don't seem to work
        Driver.add(new DriverSealEntity());
        Driver.add(new DriverSeal());
*/
        logger.info("Done with init phase.");
    }
}
