package com.intuso.housemate.comms.api.internal.payload;

/**
 * Data object for a choice type
 */
public final class ChoiceTypeData extends TypeData<ListData<OptionData>> {

    private static final long serialVersionUID = -1L;

    public ChoiceTypeData() {}

    /**
     * @param id {@inheritDoc}
     * @param name {@inheritDoc}
     * @param description {@inheritDoc}
     * @param minValues {@inheritDoc}
     * @param maxValues {@inheritDoc}
     */
    public ChoiceTypeData(String id, String name, String description, int minValues, int maxValues) {
        super(id, name, description, minValues, maxValues);
    }

    @Override
    public HousemateData clone() {
        return new ChoiceTypeData(getId(), getName(), getDescription(), getMinValues(), getMaxValues());
    }
}