package com.intuso.housemate.object.proxy;

import com.google.inject.Injector;
import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.parameter.Parameter;
import com.intuso.housemate.api.object.parameter.ParameterData;
import com.intuso.housemate.api.object.parameter.ParameterListener;
import com.intuso.utilities.log.Log;

/**
 * @param <TYPE> the type of the type
 * @param <PARAMETER> the type of the parameter
 */
public abstract class ProxyParameter<
            TYPE extends ProxyType<?, ?, ?, ?>,
            PARAMETER extends ProxyParameter<TYPE, PARAMETER>>
        extends ProxyObject<ParameterData, NoChildrenData, NoChildrenProxyObject, PARAMETER, ParameterListener>
        implements Parameter<TYPE> {

    /**
     * @param log {@inheritDoc}
     * @param injector {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxyParameter(Log log, Injector injector, ParameterData data) {
        super(log, injector, data);
    }

    @Override
    public String getTypeId() {
        return getData().getType();
    }

    @Override
    public final TYPE getType() {
        return (TYPE) getProxyRoot().getTypes().get(getData().getType());
    }
}
