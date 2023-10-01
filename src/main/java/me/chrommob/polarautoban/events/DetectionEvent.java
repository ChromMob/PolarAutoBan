package me.chrommob.polarautoban.events;


import me.chrommob.polarautoban.action.ActionTaker;
import top.polar.api.user.event.DetectionAlertEvent;

import java.util.function.Consumer;

public class DetectionEvent implements Consumer<DetectionAlertEvent> {
    private final ActionTaker at;
    public DetectionEvent(ActionTaker at) {
        this.at = at;
    }

    @Override
    public void accept(DetectionAlertEvent event) {
        at.takeAction(event.check().type().name().toLowerCase(), event.user().username());
    }
}
