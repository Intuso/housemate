package com.intuso.housemate.object.real;

import com.intuso.housemate.api.object.NoChildrenWrappable;
import com.intuso.housemate.api.object.command.argument.Argument;
import com.intuso.housemate.api.object.command.argument.ArgumentListener;
import com.intuso.housemate.api.object.command.argument.ArgumentWrappable;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 10/07/12
 * Time: 00:58
 * To change this template use File | Settings | File Templates.
 */
public class RealArgument<O>
        extends RealObject<ArgumentWrappable, NoChildrenWrappable, RealObject<NoChildrenWrappable, ? ,?, ?>, ArgumentListener>
        implements Argument<RealType<?, ?, O>> {

    private RealType<?, ?, O> type;

    public RealArgument(RealResources resources, String id, String name, String description, RealType<?, ?, O> type) {
        super(resources, new ArgumentWrappable(id, name, description, type.getId()));
        this.type = type;
    }

    @Override
    public final RealType<?, ?, O> getType() {
        return type;
    }
}