package com.intuso.housemate.comms.serialiser.json.config;

import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.application.ApplicationData;
import com.intuso.housemate.api.object.application.instance.ApplicationInstanceData;
import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.hardware.HardwareData;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.option.OptionData;
import com.intuso.housemate.api.object.parameter.ParameterData;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.root.RootData;
import com.intuso.housemate.api.object.server.ServerData;
import com.intuso.housemate.api.object.subtype.SubTypeData;
import com.intuso.housemate.api.object.task.TaskData;
import com.intuso.housemate.api.object.type.*;
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.housemate.api.object.value.ValueData;

/**
 * Created by tomc on 04/06/14.
 */
public class DataAdapter extends RuntimeTypeAdapterFactory<HousemateData> {

    public DataAdapter() {
        super(HousemateData.class, PayloadAdapter.TYPE_FIELD_NAME, PayloadAdapter.ENUM_VALUE_FIELD_NAME);
        registerSubtype(NoChildrenData.class, "noChildren");
        registerSubtype(ApplicationData.class, "application");
        registerSubtype(ApplicationInstanceData.class, "applicationInstance");
        registerSubtype(AutomationData.class, "automation");
        registerSubtype(CommandData.class, "command");
        registerSubtype(ConditionData.class, "condition");
        registerSubtype(DeviceData.class, "device");
        registerSubtype(HardwareData.class, "hardware");
        registerSubtype(ListData.class, "list");
        registerSubtype(OptionData.class, "option");
        registerSubtype(ParameterData.class, "parameter");
        registerSubtype(PropertyData.class, "property");
        registerSubtype(ServerData.class, "server");
        registerSubtype(RootData.class, "root");
        registerSubtype(SubTypeData.class, "subType");
        registerSubtype(TaskData.class, "task");
        registerSubtype(ChoiceTypeData.class, "choiceType");
        registerSubtype(CompoundTypeData.class, "compoundType");
        registerSubtype(ObjectTypeData.class, "objectType");
        registerSubtype(RegexTypeData.class, "regexType");
        registerSubtype(SimpleTypeData.class, "simpleType");
        registerSubtype(UserData.class, "user");
        registerSubtype(ValueData.class, "value");
    }
}
