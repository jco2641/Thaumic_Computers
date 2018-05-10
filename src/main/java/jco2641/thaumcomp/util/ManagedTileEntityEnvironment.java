package jco2641.thaumcomp.util;

import li.cil.oc.api.Network;
import li.cil.oc.api.driver.NamedBlock;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.AbstractManagedEnvironment;


public class ManagedTileEntityEnvironment<T> extends AbstractManagedEnvironment implements NamedBlock {
    protected final T tileEntity;
    private String name;

    public ManagedTileEntityEnvironment(final T tileEntity, final String name) {
        this.tileEntity = tileEntity;
        this.name = name;

        setNode(Network.newNode(this, Visibility.Network).
                withComponent(name).
                create());
    }

    @Override
    public String preferredName() {
        return this.name;
    }

    @Override
    public int priority() {
        return 5;
    }
}