package com.intuso.housemate.client.api.internal.ability;

import com.intuso.housemate.client.api.internal.annotation.*;
import com.intuso.utilities.collection.ManagedCollection;

/**
 * API for controlling playback
 */
public interface Playback {

    @Id(value = "playback-control", name = "Playback Control", description = "Playback control")
    interface Control extends Ability {

        String ID = Control.class.getAnnotation(Id.class).value();

        /**
         * Play
         */
        @Command
        @Id(value = "play", name = "Play", description = "Play")
        void play();

        /**
         * Pause
         */
        @Command
        @Id(value = "pause", name = "Pause", description = "Pause")
        void pause();

        /**
         * Stop
         */
        @Command
        @Id(value = "stop", name = "Stop", description = "Stop")
        void stopPlayback();

        /**
         * Forward
         */
        @Command
        @Id(value = "forward", name = "Forward", description = "Forward")
        void forward();

        /**
         * Rewind
         */
        @Command
        @Id(value = "rewind", name = "Rewind", description = "Rewind")
        void rewind();
    }

    @Id(value = "playback-state", name = "Playback State", description = "Playback state")
    interface State extends Ability {

        String ID = State.class.getAnnotation(Id.class).value();

        /**
         * Add a listener
         */
        @AddListener
        ManagedCollection.Registration addListener(Listener listener);

        interface Listener {

            /**
             * Callback for when playback speed changes
             *
             * @param speed number indicating playback speed, or null if unknown
             */
            @Value
            @Id(value = "speed", name = "Playback Speed", description = "Speed of playback relative to normal speed, eg 8 means 8x, -4 means rewind 4x")
            void speed(Integer speed);
        }
    }
}
