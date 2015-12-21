package com.intuso.housemate.client.real.impl.internal.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.intuso.housemate.client.real.api.internal.*;
import com.intuso.housemate.client.real.impl.internal.RealRootImpl;
import com.intuso.housemate.client.real.impl.internal.annotations.ioc.RealAnnotationsModule;
import com.intuso.housemate.client.real.impl.internal.factory.ioc.RealFactoryModule;
import com.intuso.housemate.client.real.impl.internal.type.*;

/**
 */
public class RealObjectModule extends AbstractModule {

    @Override
    protected void configure() {

        // install other required modules
        install(new RealFactoryModule());
        install(new RealAnnotationsModule());

        bind(RealRoot.class).to(RealRootImpl.class);
        bind(RealRootImpl.class).in(Scopes.SINGLETON);

        bind(RealAutomation.Container.class).to(RealRootImpl.class);
        bind(RealApplication.Container.class).to(RealRootImpl.class);
        bind(RealDevice.Container.class).to(RealRootImpl.class);
        bind(RealHardware.Container.class).to(RealRootImpl.class);
        bind(RealType.Container.class).to(RealRootImpl.class);
        bind(RealUser.Container.class).to(RealRootImpl.class);

        // bind everything as singletons that should be
        bind(ApplicationStatusType.class).in(Scopes.SINGLETON);
        bind(ApplicationInstanceStatusType.class).in(Scopes.SINGLETON);
        bind(BooleanType.class).in(Scopes.SINGLETON);
        bind(DaysType.class).in(Scopes.SINGLETON);
        bind(DoubleType.class).in(Scopes.SINGLETON);
        bind(EmailType.class).in(Scopes.SINGLETON);
        bind(IntegerType.class).in(Scopes.SINGLETON);
        bind(StringType.class).in(Scopes.SINGLETON);
        bind(TimeType.class).in(Scopes.SINGLETON);
        bind(TimeUnitType.class).in(Scopes.SINGLETON);
    }
}
