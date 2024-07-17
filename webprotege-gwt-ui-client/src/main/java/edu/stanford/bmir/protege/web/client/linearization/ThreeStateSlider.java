package edu.stanford.bmir.protege.web.client.linearization;


import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.*;
import org.tukaani.xz.check.Check;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;


public class ThreeStateSlider extends Composite {
    Logger logger = java.util.logging.Logger.getLogger("ThreeStateSlider");

    @UiTemplate("ThreeStateSlider.ui.xml")
    interface ThreeStateSliderUiBinder extends UiBinder<HTMLPanel, ThreeStateSlider> {}
    private static ThreeStateSliderUiBinder uiBinder = GWT.create(ThreeStateSliderUiBinder.class);

    @UiField
    SpanElement optionTrue;

    @UiField
    SpanElement optionFalse;

    @UiField
    SpanElement optionUnknown;

    List<SpanElement> optionsList;

    public ThreeStateSlider() {
        initWidget(uiBinder.createAndBindUi(this));


        Event.sinkEvents(optionTrue, Event.ONCLICK);
        Event.sinkEvents(optionFalse, Event.ONCLICK);
        Event.sinkEvents(optionUnknown, Event.ONCLICK);
        Event.setEventListener(optionTrue, new EventListener() {

            @Override
            public void onBrowserEvent(Event event) {
                if(Event.ONCLICK == event.getTypeInt()) {
                    handleValueChange(optionTrue, "TRUE");
                }
            }
        });

        Event.setEventListener(optionFalse, new EventListener() {

            @Override
            public void onBrowserEvent(Event event) {
                if(Event.ONCLICK == event.getTypeInt()) {
                    handleValueChange(optionFalse, "FALSE");
                }
            }
        });

        Event.setEventListener(optionUnknown, new EventListener() {

            @Override
            public void onBrowserEvent(Event event) {
                if(Event.ONCLICK == event.getTypeInt()) {
                    handleValueChange(optionUnknown, "UNKNOWN");
                }
            }
        });

        // Initialize event handlers for the radio buttons

        optionTrue.addClassName("toggle-yes");
        optionFalse.addClassName("toggle-no");
        optionUnknown.addClassName("toggle-unknown");
        optionsList  = Arrays.asList(optionFalse, optionUnknown, optionTrue);


        // Initialize state
        handleValueChange(optionFalse, "FALSE");  // Initially selected option 'U'
    }

    private void handleValueChange(Element element, String customValue) {


        element.addClassName("active");
        for(Element e: optionsList) {
            logger.info("ALEX comparing " + e +  " cu  " + element);
            if(!e.equals(element)) {
                e.removeClassName("active");
            }
        }
    }


}
