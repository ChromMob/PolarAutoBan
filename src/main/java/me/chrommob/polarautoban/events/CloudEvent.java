package me.chrommob.polarautoban.events;

import java.util.function.Consumer;

import me.chrommob.polarautoban.action.ActionTaker;
import top.polar.api.user.event.CloudDetectionEvent;

public class CloudEvent implements Consumer<CloudDetectionEvent> {
    private final ActionTaker at;
    public CloudEvent(ActionTaker at) {
        this.at = at;
    }
    @Override
    public void accept(CloudDetectionEvent event) {
        at.takeAction(event.cloudCheckType().name().toLowerCase(), event.user().username());
    }
    
}
