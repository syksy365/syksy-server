package site.syksy.rose.common.event;

import org.springframework.context.ApplicationEvent;

public class RemoveUserEvent extends ApplicationEvent {

    public RemoveUserEvent(Object source) {
        super(source);
    }
}
