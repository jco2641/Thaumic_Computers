package jco2641.thaumcomp.aspects;

import li.cil.oc.api.driver.Converter;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.AspectHelper;

import java.util.HashMap;
import java.util.Map;

/*
    Derived from work Copyright (c) 2013-2015 Florian "Sangar" Nücke published under MIT license
*/

public class ConverterAspectItem implements Converter {

    @Override
    public void convert(final Object value, final Map<Object,Object> output) {
        if (value instanceof ItemStack) {
            final AspectList list = AspectHelper.getObjectAspects((ItemStack) value);
            final HashMap<Object,Object> out = new HashMap<>();
            if(list == null) return;
            if(list.size() == 0) return;
            for (Aspect a : list.getAspects()) {
                out.put(a.getName(), list.getAmount(a));
            }
            output.put("aspects",out);
        }
    }
}
