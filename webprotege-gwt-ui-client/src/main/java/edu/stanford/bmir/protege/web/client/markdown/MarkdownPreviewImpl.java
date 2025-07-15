package edu.stanford.bmir.protege.web.client.markdown;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

import javax.inject.Inject;

public class MarkdownPreviewImpl extends Composite implements MarkdownPreview {

    interface MarkdownPreviewImplUiBinder extends UiBinder<HTMLPanel, MarkdownPreviewImpl> {

    }

    private static MarkdownPreviewImplUiBinder ourUiBinder = GWT.create(MarkdownPreviewImplUiBinder.class);

    @Inject
    public MarkdownPreviewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setMarkdown(String markdown) {
        String html = Marked.parse(markdown);
        getElement().setInnerSafeHtml(SafeHtmlUtils.fromTrustedString(html));
    }
}