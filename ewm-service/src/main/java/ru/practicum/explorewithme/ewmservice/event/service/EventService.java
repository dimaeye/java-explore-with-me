package ru.practicum.explorewithme.ewmservice.event.service;

import ru.practicum.explorewithme.ewmservice.event.exception.BadEventStateException;
import ru.practicum.explorewithme.ewmservice.event.exception.EventNotFoundException;
import ru.practicum.explorewithme.ewmservice.event.model.Event;
import ru.practicum.explorewithme.ewmservice.event.model.StateAction;

import java.util.List;

public interface EventService {
    //#region Private
    List<Event> getEventsByUserId(int userId, int from, int size);

    /**
     * @throws EventNotFoundException when event is not found
     */
    Event getUserEventById(int userId, int eventId);

    /**
     * @throws EventNotFoundException when event is not found
     */
    Event addEvent(int userId, Event event);

    /**
     * @throws EventNotFoundException when event is not found
     * @throws BadEventStateException if eventState not in PENDING or CANCELED
     */
    Event updateEvent(int userId, int eventId, StateAction stateAction, Event event);
    //#endregion

    //#region Admin
    List<Event> getAllEvents(FindEventsByAdminParams findEventsByAdminParams);

    /**
     * @throws EventNotFoundException when event is not found
     * @throws BadEventStateException if bad stateAction and eventState
     */
    Event updateEvent(int eventId, StateAction stateAction, Event event);
    //#endregion

    //#region Public
    List<Event> getAllEvents(FindEventsByUserParams findEventsByUserParams, String ip);

    /**
     * @throws EventNotFoundException when event is not found or not published
     */
    Event getEventById(int eventId, String ip);
    //#endregion


}
