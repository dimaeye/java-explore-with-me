package ru.practicum.explorewithme.ewmservice.event.model;

import java.util.Optional;

public enum StateAction {
    SEND_TO_REVIEW, CANCEL_REVIEW, PUBLISH_EVENT, REJECT_EVENT;

    public static Optional<StateAction> from(String action) {
        for (StateAction stateAction : values()) {
            if (stateAction.name().equalsIgnoreCase(action)) {
                return Optional.of(stateAction);
            }
        }
        return Optional.empty();
    }
}
