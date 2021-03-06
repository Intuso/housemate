package com.intuso.housemate.client.api.internal.ability;

import com.intuso.housemate.client.api.internal.annotation.*;
import com.intuso.utilities.collection.ManagedCollection;

/**
 * API for controlling volume
 */
public interface Volume {

    @Id(value = "volume-control", name = "Volume Control", description = "Volume control")
    interface Control extends Ability {

        String ID = Control.class.getAnnotation(Id.class).value();

        /**
         * Mute the device
         */
        @Command
        @Id(value = "mute", name = "Mute", description = "Mute")
        void mute();

        /**
         * Mute the device
         */
        @Command
        @Id(value = "unmute", name = "Unmute", description = "Unmute")
        void unmute();

        /**
         * Turn the volume up
         */
        @Command
        @Id(value = "volume", name = "Set Volume", description = "Set the volume")
        void volume(@Id(value = "volume", name = "Volume", description = "The volume to set to") int volume);

        /**
         * Turn the volume up
         */
        @Command
        @Id(value = "up", name = "Volume Up", description = "Turn the volume up")
        void volumeUp();

        /**
         * Turn the volume down
         */
        @Command
        @Id(value = "down", name = "Volume Down", description = "Turn the volume down")
        void volumeDown();
    }

    @Id(value = "volume-state", name = "Volume State", description = "Volume state")
    interface State extends Ability {

        String ID = State.class.getAnnotation(Id.class).value();

        /**
         * Add a listener
         */
        @AddListener
        ManagedCollection.Registration addListener(Listener listener);

        interface Listener {

            /**
             * Callback for when the volume is muted
             *
             * @param mute whether the device is muted, or null if unknown
             */
            @Value
            @Id(value = "mute", name = "Mute", description = "Whether the device is muted")
            void mute(Boolean mute);

            /**
             * Callback for when the volume has changed
             *
             * @param volume the new volume
             */
            @Value
            @Id(value = "volume", name = "Current Volume", description = "The device's current volume")
            void volume(Integer volume);
        }
    }
}
