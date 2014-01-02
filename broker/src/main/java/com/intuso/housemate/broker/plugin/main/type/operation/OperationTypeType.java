package com.intuso.housemate.broker.plugin.main.type.operation;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeSerialiser;
import com.intuso.housemate.broker.plugin.PluginListener;
import com.intuso.housemate.broker.plugin.PluginManager;
import com.intuso.housemate.object.real.RealOption;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.impl.type.RealChoiceType;
import com.intuso.housemate.plugin.api.OperationType;
import com.intuso.housemate.plugin.api.Operator;
import com.intuso.housemate.plugin.api.PluginDescriptor;

import java.util.Map;

/**
 */
@Singleton
public class OperationTypeType extends RealChoiceType<OperationType> {

    public final static String ID = "operation-type";
    public final static String NAME = "Operation Type";
    public final static String DESCRIPTION = "Type of operation";

    private final TypeSerialiser<OperationType> serialiser;

    @Inject
    public OperationTypeType(RealResources realResources, TypeSerialiser<OperationType> serialiser) {
        super(realResources, ID, NAME, DESCRIPTION, 1, 1);
        this.serialiser = serialiser;
    }

    @Override
    public OperationType deserialise(TypeInstance instance) {
        return serialiser.deserialise(instance);
    }

    @Override
    public TypeInstance serialise(OperationType o) {
        return serialiser.serialise(o);
    }

    public final static class Serialiser implements TypeSerialiser<OperationType>, PluginListener {

        private final Map<String, OperationType> types = Maps.newHashMap();
        private final RealResources realResources;

        @Inject
        public Serialiser(RealResources realResources, PluginManager pluginManager) {
            this.realResources = realResources;
            pluginManager.addPluginListener(this, true);
        }

        @Override
        public TypeInstance serialise(OperationType type) {
            return type != null ? new TypeInstance(type.getId()) : null;
        }

        @Override
        public OperationType deserialise(TypeInstance instance) {
            return instance != null ? types.get(instance.getValue()) : null;
        }

        @Override
        public void pluginAdded(PluginDescriptor plugin) {
            OperationTypeType type = (OperationTypeType) types.get(ID);
            for(Operator<?, ?> operator : plugin.getOperators(realResources)) {
                if(types.get(operator.getOperationType().getId()) == null) {
                    types.put(operator.getOperationType().getId(), operator.getOperationType());
                    type.getOptions().add(new RealOption(realResources, operator.getOperationType().getId(),
                            operator.getOperationType().getName(), operator.getOperationType().getDescription()));
                }
            }
        }

        @Override
        public void pluginRemoved(PluginDescriptor plugin) {
            // todo remove them
        }
    }
}