package jco2641.thaumcomp.aspects;

import li.cil.oc.api.driver.Converter;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import java.util.ArrayList;
import java.util.Map;

public class ConverterIAspectContainer implements Converter {

    @Override
    public void convert(final Object value, final Map<Object, Object> output) {
        if (value instanceof IAspectContainer) {
            final IAspectContainer container = (IAspectContainer) value;
            AspectList list = container.getAspects();
            Aspect[] array = list.getAspects();
            ArrayList<String> aspectNames = new ArrayList<>();
            for (int i = 0; i < array.length; i++) {
                aspectNames.add(array[i].getName());
            }
            output.put("aspects", aspectNames.toArray());
        }
    }
}
