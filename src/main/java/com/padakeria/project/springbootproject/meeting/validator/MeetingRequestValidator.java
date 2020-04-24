package com.padakeria.project.springbootproject.meeting.validator;

import com.padakeria.project.springbootproject.meeting.dto.MeetingRequestDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;

@Component
public class MeetingRequestValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(MeetingRequestDto.class);
    }

    @Override
    public void validate(Object object, Errors errors) {
        MeetingRequestDto meetingRequestDto = (MeetingRequestDto) object;
        LocalDateTime meetingStartingDate = meetingRequestDto.getMeetingStartingDate();
        LocalDateTime recruitStartingDate = meetingRequestDto.getRecruitStartingDate();
        LocalDateTime recruitEndingDate = meetingRequestDto.getRecruitEndingDate();
        if (meetingStartingDate != null && recruitStartingDate != null && recruitEndingDate != null) {
            if (meetingStartingDate.isBefore(recruitStartingDate)) {
                errors.rejectValue("meetingStartingDate", "invalid meetingStartingDate", "모임 시작일이 올바르지 않습니다.");
            } else if (meetingStartingDate.isBefore(recruitEndingDate)) {
                errors.rejectValue("meetingStartingDate", "invalid meetingStartingDate", "모임 시작일이 올바르지 않습니다.");
            } else if (recruitStartingDate.isAfter(recruitEndingDate)) {
                errors.rejectValue("recruitStartingDate", "invalid recruitStartingDate", "모임 모집 시작일이 올바르지 않습니다.");
            }
        } else {
            errors.reject("empty date", "날짜를 선택해주세요");
        }
    }
}
