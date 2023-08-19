package ru.practicum.explorewithme.ewmservice.event.service;

import ru.practicum.explorewithme.ewmservice.event.model.Event;
import ru.practicum.explorewithme.ewmservice.event.model.StateAction;

import java.util.List;

public interface EventService {
    //#region Private
    List<Event> getEventsByUserId(int userId, int from, int size);

    Event getUserEventById(int userId, int eventId);

    Event addEvent(int userId, Event event);

    Event updateEvent(int userId, int eventId, StateAction stateAction, Event event);
    //#endregion

    //#region Admin
    List<Event> getAllEvents(FindEventsByAdminParams findEventsByAdminParams);

    Event updateEvent(int eventId, StateAction stateAction, Event event);
    //#endregion

    //#region Public
    List<Event> getAllEvents(FindEventsByUserParams findEventsByUserParams, String ip);

    Event getEventById(int eventId, String ip);
    //#endregion


}
