package edu.stanford.bmir.protege.web.shared.dispatch.actions;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.event.GetProjectEventsResult;

import javax.annotation.Nonnull;
import java.io.Serializable;


@JsonTypeName("webprotege.projects.TranslateEventList")
public class TranslateEventListAction implements Action<GetProjectEventsResult>, Serializable, IsSerializable {

    private String eventList;
    @GwtSerializationConstructor
    public TranslateEventListAction(){

    }

    private TranslateEventListAction(String eventList) {
        this.eventList = eventList;
    }

    public static TranslateEventListAction create(@Nonnull String eventList) {
        return new TranslateEventListAction(eventList);
    }

    public String getEventList() {
        return eventList;
    }

    public void setEventList(String eventList) {
        this.eventList = eventList;
    }
}
