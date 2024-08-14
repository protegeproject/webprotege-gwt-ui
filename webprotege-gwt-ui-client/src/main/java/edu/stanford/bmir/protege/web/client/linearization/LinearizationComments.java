package edu.stanford.bmir.protege.web.client.linearization;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.*;

import java.util.function.Consumer;
import java.util.logging.Logger;

public class LinearizationComments  implements ClickHandler {

    Logger logger = java.util.logging.Logger.getLogger("LinearizationComments");

    private Button shownWidget;

    private String markDownText;

    private boolean isReadOnly = true;

    private LinearizationCommentsModal linearizationCommentsModal;

    private LinearizationCommentsResourceBundle.LinearizationCommentsCss style = LinearizationCommentsResourceBundle.INSTANCE.style();

    // Constructor
    public LinearizationComments(String text, LinearizationCommentsModal linearizationParentModal) {
        style.ensureInjected();
        markDownText = text;
        shownWidget = new Button();
        setText(text);
        shownWidget.addClickHandler(this);
        shownWidget.addStyleName(style.getLinearizationComments());
        this.linearizationCommentsModal = linearizationParentModal;
    }


    public void setText(String text) {
        String strippedText = stripMarkdown(text);
        if(strippedText.length() < 50) {
            this.shownWidget.setText(strippedText);
        } else {
            this.shownWidget.setText(strippedText.substring(0, 46) + " ...");
        }

    }

    public String getText() {
        return markDownText;
    }

    public Widget asWidget() {
        return shownWidget;
    }

    @Override
    public void onClick(ClickEvent event) {
        // Handle the click event here
        // For example, you can change the label's text on click
        if(!this.isReadOnly) {
            Consumer<String> handler = body -> {
                this.markDownText = body;
                setText(body);
            };

            this.linearizationCommentsModal.showModal(markDownText, handler);
        }

    }

    // Method to add external ClickHandlers
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return shownWidget.addClickHandler(handler);
    }


    public void enable(){
        this.isReadOnly = false;
    }

    public void disable() {
        this.isReadOnly = true;
    }

    private String stripMarkdown(String markdownText) {
        if (markdownText == null) {
            return "";
        }

        // Remove Markdown headings
        markdownText = markdownText.replaceAll("#+\\s", "");

        // Remove Markdown bold and italic
        markdownText = markdownText.replaceAll("[*_]{1,3}", "");

        // Remove Markdown links
        markdownText = markdownText.replaceAll("\\[(.*?)\\]\\(.*?\\)", "$1");

        // Remove Markdown images
        markdownText = markdownText.replaceAll("!\\[(.*?)\\]\\(.*?\\)", "$1");

        // Remove Markdown blockquotes
        markdownText = markdownText.replaceAll("^>\\s?", "");

        // Remove Markdown code blocks
        markdownText = markdownText.replaceAll("`([^`]*)`", "$1");

        // Regular expression to match triple backticks around text
        markdownText = markdownText.replaceAll("```([^`]*)```", "$1");

        // Remove Markdown horizontal rules
        markdownText = markdownText.replaceAll("^---$", "");

        // Remove Markdown lists
        markdownText = markdownText.replaceAll("^\\s*[-+*]\\s", "");

        // Remove Markdown ordered lists
        markdownText = markdownText.replaceAll("^\\d+\\.\\s", "");

        // Remove Markdown horizontal rules
        markdownText = markdownText.replaceAll("-{3,}", "");

        // Remove Markdown line breaks
        markdownText = markdownText.replaceAll("\n", " ");

        return markdownText.trim();
    }

}
