package edu.stanford.bmir.protege.web.client.events;

import edu.stanford.bmir.protege.web.shared.event.WebProtegeEvent;

import java.util.Collection;

public interface ChangeRequestEventsHandler {

    void handle(Collection<WebProtegeEvent<?>> events);
}
