package com.intuso.housemate.sample.plugin.comparator;

import com.intuso.housemate.plugin.api.ComparisonOperator;

public class LocationOperator implements ComparisonOperator {
    @Override
    public String getId() {
        return "location-comparator";
    }

    @Override
    public String getName() {
        return "Location Comparator";
    }

    @Override
    public String getDescription() {
        return "Comparator of locations";
    }
}
