package jco2641.thaumcomp;

import jco2641.thaumcomp.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION, dependencies = Reference.DEPENDENCIES)
public class ThaumicComputers {

    @SidedProxy(clientSide = "jco2641.thaumcomp.proxy.ClientProxy",serverSide = "jco2641.thaumcomp.proxy.ServerProxy" )
    public static CommonProxy proxy;

    @Mod.Instance
    public static ThaumicComputers instance;

    public static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        proxy.preInit(event);
        logger.info("Done with preInit phase.");
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);

        logger.info("Done with init phase.");
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event){
        proxy.postInit(event);
    }
}
