package com.intuso.housemate.api.object.property;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.resources.Resources;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 11/02/13
 * Time: 09:13
 * To change this template use File | Settings | File Templates.
 */
public interface PropertyFactory<
            R extends Resources,
            P extends Property<?, ?, ?>>
        extends HousemateObjectFactory<R, PropertyWrappable, P> {
}