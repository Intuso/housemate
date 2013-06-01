package com.intuso.housemate.api.object.rule;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.resources.Resources;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 11/02/13
 * Time: 09:13
 * To change this template use File | Settings | File Templates.
 */
public interface RuleFactory<
            R extends Resources,
            Ru extends Rule<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>>
        extends HousemateObjectFactory<R, RuleWrappable, Ru> {
}
