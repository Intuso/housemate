package com.intuso.housemate.web.client.bootstrap.widget.device.feature;

import com.google.common.collect.Sets;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.web.client.bootstrap.widget.command.PerformButton;
import com.intuso.housemate.web.client.object.GWTProxyCommand;
import com.intuso.housemate.web.client.object.GWTProxyDevice;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyType;
import com.intuso.housemate.web.client.object.device.feature.GWTProxyFeature;

import java.util.Set;

public class PlaybackControl
        extends GWTProxyFeature
        implements com.intuso.housemate.api.object.device.feature.PlaybackControl<GWTProxyCommand> {

    public PlaybackControl(GWTProxyDevice device) {
        super(device);
    }

    @Override
    public GWTProxyCommand getPlayCommand() {
        return device.getCommands() != null ? device.getCommands().get(PLAY_COMMAND) : null;
    }

    @Override
    public GWTProxyCommand getPauseCommand() {
        return device.getCommands() != null ? device.getCommands().get(PAUSE_COMMAND) : null;
    }

    @Override
    public GWTProxyCommand getStopCommand() {
        return device.getCommands() != null ? device.getCommands().get(STOP_COMMAND) : null;
    }

    @Override
    public GWTProxyCommand getForwardCommand() {
        return device.getCommands() != null ? device.getCommands().get(FORWARD_COMMAND) : null;
    }

    @Override
    public GWTProxyCommand getRewindCommand() {
        return device.getCommands() != null ? device.getCommands().get(REWIND_COMMAND) : null;
    }

    @Override
    public Set<String> getCommandIds() {
        return Sets.newHashSet(PLAY_COMMAND, PAUSE_COMMAND, STOP_COMMAND, FORWARD_COMMAND, REWIND_COMMAND);
    }

    @Override
    public Set<String> getValueIds() {
        return Sets.newHashSet();
    }

    @Override
    public Set<String> getPropertyIds() {
        return Sets.newHashSet();
    }

    @Override
    public String getTitle() {
        return "Playback";
    }

    @Override
    public Widget getWidget(GWTProxyList<TypeData<?>, GWTProxyType> types) {
        return new PlaybackWidget(types);
    }

    interface PlaybackWidgetUiBinder extends UiBinder<Widget, PlaybackWidget> {
    }

    private static PlaybackWidgetUiBinder ourUiBinder = GWT.create(PlaybackWidgetUiBinder.class);

    public class PlaybackWidget extends Composite {

        @UiField
        public PerformButton playButton;
        @UiField
        public PerformButton pauseButton;
        @UiField
        public PerformButton stopButton;
        @UiField
        public PerformButton forwardButton;
        @UiField
        public PerformButton rewindButton;

        private PlaybackWidget(GWTProxyList<TypeData<?>, GWTProxyType> types) {
            initWidget(ourUiBinder.createAndBindUi(this));

            playButton.setCommand(types, getPlayCommand());
            pauseButton.setCommand(types, getPauseCommand());
            stopButton.setCommand(types, getStopCommand());
            forwardButton.setCommand(types, getForwardCommand());
            rewindButton.setCommand(types, getRewindCommand());
        }
    }
}
