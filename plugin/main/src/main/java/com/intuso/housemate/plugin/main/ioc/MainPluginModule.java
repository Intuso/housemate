package com.intuso.housemate.plugin.main.ioc;

import com.intuso.housemate.client.api.internal.annotation.Id;
import com.intuso.housemate.client.api.internal.plugin.AnnotatedPluginModule;
import com.intuso.housemate.client.api.internal.plugin.ConditionDrivers;
import com.intuso.housemate.client.api.internal.plugin.FeatureDrivers;
import com.intuso.housemate.client.api.internal.plugin.TaskDrivers;
import com.intuso.housemate.plugin.main.condition.*;
import com.intuso.housemate.plugin.main.feature.PowerByCommandFeature;
import com.intuso.housemate.plugin.main.task.Delay;
import com.intuso.housemate.plugin.main.task.RandomDelay;

/**
 */
@Id(value = "main-plugin",
        name = "Main plugin",
        description = "Plugin containing the core types and factories")
@FeatureDrivers({PowerByCommandFeature.class})
@ConditionDrivers({And.class,
        Or.class,
        Not.class,
        DayOfTheWeek.class,
        TimeOfTheDay.class})
@TaskDrivers({Delay.class,
        RandomDelay.class})
public class MainPluginModule extends AnnotatedPluginModule {}
