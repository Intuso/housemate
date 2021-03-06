package com.intuso.housemate.client.real.impl.internal.type;

import com.google.inject.Inject;
import com.intuso.housemate.client.api.internal.type.serialiser.ByteObjectSerialiser;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.real.impl.internal.ioc.Type;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Type for a boolean
 */
public class ByteObjectType extends RealPrimitiveType<Byte> {

    @Inject
    public ByteObjectType(@Type Logger logger,
                          ManagedCollectionFactory managedCollectionFactory) {
        super(ChildUtil.logger(logger, Byte.class.getName()),
                new PrimitiveData(Byte.class.getName(), "Byte", "A number between 0 and 255 inclusive"),
                ByteObjectSerialiser.INSTANCE,
                managedCollectionFactory);
    }
}
