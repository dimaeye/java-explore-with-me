package ru.practicum.explorewithme.ewmservice.request.service;

import ru.practicum.explorewithme.ewmservice.request.model.Request;
import ru.practicum.explorewithme.ewmservice.request.model.RequestStatus;

import java.util.List;

public interface RequestService {

    Request addParticipationRequest(int userId, int eventId);//Создать запрос на участие в событии

    List<Request> getAllUserRequests(int userId); //Все заявки на участие в событиях

    List<Request> getEventParticipants(int userId, int eventId); //Участники мероприятия

    Request cancelRequest(int userId, int requestId);

    List<Request> changeStatusOfAllRequests(int userId, int eventId, List<Integer> requestIds, RequestStatus requestStatus);
}
