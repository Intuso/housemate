package com.intuso.housemate.client.real.impl.internal.annotation;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import com.intuso.housemate.client.real.impl.internal.RealCommandImpl;
import com.intuso.housemate.client.real.impl.internal.RealDeviceComponentImpl;
import com.intuso.housemate.client.real.impl.internal.RealPropertyImpl;
import com.intuso.housemate.client.real.impl.internal.RealValueImpl;
import org.slf4j.Logger;

/**
 * Created by tomc on 16/12/16.
 */
public class AnnotationParserImpl implements AnnotationParser {

    private final AnnotationParserInternal annotationParserInternal;
    private final AnnotationParserV1_0 annotationParserV1_0;

    @Inject
    public AnnotationParserImpl(AnnotationParserInternal annotationParserInternal, AnnotationParserV1_0 annotationParserV1_0) {
        this.annotationParserInternal = annotationParserInternal;
        this.annotationParserV1_0 = annotationParserV1_0;
    }

    @Override
    public Iterable<RealDeviceComponentImpl> findDeviceComponents(Logger logger, Object object) {
        return Iterables.concat(
                annotationParserInternal.findDeviceComponents(logger, object),
                annotationParserV1_0.findDeviceComponents(logger, object)
        );
    }

    @Override
    public Iterable<RealCommandImpl> findCommands(Logger logger, Object object) {
        return Iterables.concat(
                annotationParserInternal.findCommands(logger, object),
                annotationParserV1_0.findCommands(logger, object)
        );
    }

    @Override
    public Iterable<RealValueImpl<?>> findValues(Logger logger, Object object) {
        return Iterables.concat(
                annotationParserInternal.findValues(logger, object),
                annotationParserV1_0.findValues(logger, object)
        );
    }

    @Override
    public Iterable<RealPropertyImpl<?>> findProperties(Logger logger, Object object) {
        return Iterables.concat(
                annotationParserInternal.findProperties(logger, object),
                annotationParserV1_0.findProperties(logger, object)
        );
    }
}
