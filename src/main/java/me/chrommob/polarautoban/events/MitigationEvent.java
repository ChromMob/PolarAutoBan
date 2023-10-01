package me.chrommob.polarautoban.events;

import me.chrommob.polarautoban.action.ActionTaker;

import java.util.Map;
import java.util.function.Consumer;

public class MitigationEvent implements Consumer<top.polar.api.user.event.MitigationEvent> {
    private final ActionTaker at;
    public MitigationEvent(ActionTaker at) {
        this.at = at;
    }

    @Override
    public void accept(top.polar.api.user.event.MitigationEvent event) {
        at.takeAction(event.check().type().name().toLowerCase(), event.user().username());
    }
    
}
