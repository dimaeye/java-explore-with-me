package ru.practicum.explorewithme.ewmservice.event.annotation;

import ru.practicum.explorewithme.ewmservice.event.dto.EventDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class CreateEventValidator implements ConstraintValidator<CreateEventConstraint, EventDTO<Integer>> {

    @Override
    public boolean isValid(EventDTO<Integer> eventDTO, ConstraintValidatorContext constraintValidatorContext) {
        List<String> errMessages = new ArrayList<>();

        if (eventDTO.getAnnotation() == null || eventDTO.getAnnotation().isBlank())
            errMessages.add("annotation can not be null or blank");
        if (eventDTO.getCategory() == null)
            errMessages.add("category can not be null");
        if (eventDTO.getDescription() == null || eventDTO.getDescription().isBlank())
            errMessages.add("description can not be null");
        if (eventDTO.getEventDate() == null)
            errMessages.add("eventDate can not be null");
        if (eventDTO.getLocation() == null)
            errMessages.add("location can not be null");
        if (eventDTO.getTitle() == null || eventDTO.getTitle().isBlank())
            errMessages.add("title can not be null");

        if (errMessages.isEmpty())
            return true;
        else {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(errMessages.toString())
                    .addConstraintViolation();
            return false;
        }
    }
}
