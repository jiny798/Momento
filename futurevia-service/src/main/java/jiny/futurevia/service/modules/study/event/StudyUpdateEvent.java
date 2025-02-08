package jiny.futurevia.service.modules.study.event;

import jiny.futurevia.service.modules.study.domain.entity.Study;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class StudyUpdateEvent {
    private final Study study;
    private final String message;
}