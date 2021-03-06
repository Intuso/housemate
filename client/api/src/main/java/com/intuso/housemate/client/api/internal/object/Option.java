package com.intuso.housemate.client.api.internal.object;

import com.intuso.housemate.client.api.internal.object.view.NoView;

/**
 * @param <SUB_TYPES> the type of the sub types list
 */
public interface Option<SUB_TYPES extends List<? extends SubType<?, ?>, ?>,
        OPTION extends Option<?, ?>>
        extends Object<Option.Data, Option.Listener<? super OPTION>, NoView>, SubType.Container<SUB_TYPES> {

    String SUB_TYPES_ID = "subTypes";

    /**
     *
     * Listener interface for options
     */
    interface Listener<OPTION extends Option<?, ?>> extends Object.Listener {}

    /**
     *
     * Interface to show that the implementing object has a list of options
     */
    interface Container<OPTIONS extends Iterable<? extends Option<?, ?>>> {

        /**
         * Gets the option list
         * @return the option list
         */
        OPTIONS getOptions();
    }

    /**
     *
     * Data object for an option
     */
    final class Data extends Object.Data {

        private static final long serialVersionUID = -1L;

        public final static String OBJECT_CLASS = "option";

        public Data() {}

        public Data(String id, String name, String description) {
            super(OBJECT_CLASS, id, name, description);
        }
    }
}
