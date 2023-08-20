package ru.practicum.explorewithme.ewmservice.request.service;

import ru.practicum.explorewithme.ewmservice.event.exception.EventNotFoundException;
import ru.practicum.explorewithme.ewmservice.request.exception.BadRequestStateException;
import ru.practicum.explorewithme.ewmservice.request.exception.RequestNotFoundException;
import ru.practicum.explorewithme.ewmservice.request.model.Request;
import ru.practicum.explorewithme.ewmservice.request.model.RequestStatus;
import ru.practicum.explorewithme.ewmservice.user.exception.UserNotFoundException;

import java.util.List;

public interface RequestService {

    /**
     * @throws UserNotFoundException    when user not found
     * @throws EventNotFoundException   when event not found
     * @throws BadRequestStateException if the event status checks failed
     */
    Request addParticipationRequest(int userId, int eventId);

    List<Request> getAllUserRequests(int userId);

    List<Request> getEventParticipants(int userId, int eventId);

    /**
     * @throws RequestNotFoundException when request not found
     */
    Request cancelRequest(int userId, int requestId);

    /**
     * @throws EventNotFoundException   when event not found
     * @throws BadRequestStateException if the event status checks failed
     */
    List<Request> changeStatusOfAllRequests(int userId, int eventId, List<Integer> requestIds, RequestStatus requestStatus);
}
