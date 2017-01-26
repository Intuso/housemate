package com.intuso.housemate.client.real.impl.internal.type;

import com.google.inject.Inject;
import com.intuso.housemate.client.api.internal.type.serialiser.IntegerSerialiser;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.real.impl.internal.ioc.Type;
import com.intuso.utilities.listener.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Type for an integer
 */
public class IntegerType extends RealPrimitiveType<Integer> {

    @Inject
    public IntegerType(@Type Logger logger, ManagedCollectionFactory managedCollectionFactory) {
        super(ChildUtil.logger(logger, Integer.class.getName()),
                new PrimitiveData("int", "Integer", "A whole number"),
                IntegerSerialiser.INSTANCE,
                managedCollectionFactory);
    }
}
