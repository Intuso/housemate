package com.intuso.housemate.annotations.plugin;

import com.intuso.housemate.object.broker.real.BrokerRealCondition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 02/06/13
 * Time: 22:16
 * To change this template use File | Settings | File Templates.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Conditions {
    Class<? extends BrokerRealCondition>[] value();
}
