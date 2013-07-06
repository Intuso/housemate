package com.intuso.housemate.api.object.type;

import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.subtype.SubTypeData;

/**
 *
 * Data object for a compound type
 */
public class CompoundTypeData extends TypeData<ListData<SubTypeData>> {

    private CompoundTypeData() {}

    /**
     * @param id {@inheritDoc}
     * @param name {@inheritDoc}
     * @param description {@inheritDoc}
     * @param minValues {@inheritDoc}
     * @param maxValues {@inheritDoc}
     */
    public CompoundTypeData(String id, String name, String description, int minValues, int maxValues) {
        super(id, name, description, minValues, maxValues);
    }

    @Override
    public HousemateData clone() {
        return new CompoundTypeData(getId(), getName(), getDescription(), getMinValues(), getMaxValues());
    }
}
