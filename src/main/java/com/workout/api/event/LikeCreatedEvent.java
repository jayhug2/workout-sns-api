package com.workout.api.event;

import com.workout.api.entity.Like;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class LikeCreatedEvent extends ApplicationEvent {
    private final Like like;

    public LikeCreatedEvent(Object source, Like like) {
        super(source);
        this.like = like;
    }
}
