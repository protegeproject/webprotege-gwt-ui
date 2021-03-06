package edu.stanford.bmir.protege.web.client.primitive;

import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.dom.client.HasKeyUpHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SuggestOracle;
import edu.stanford.bmir.protege.web.client.anchor.HasAnchor;
import edu.stanford.bmir.protege.web.client.library.common.HasPlaceholder;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.client.library.suggest.EntitySuggestion;
import org.semanticweb.owlapi.model.EntityType;

import java.util.Optional;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/01/2014
 */
public interface PrimitiveDataEditorView extends IsWidget, HasText, HasValueChangeHandlers<String>, HasSelectionHandlers<EntitySuggestion>, HasAnchor, HasEnabled, HasPlaceholder, HasFocusHandlers, HasKeyUpHandlers, HasRequestFocus {

    void setDeprecated(boolean deprecated);

    enum Mode {SINGLE_LINE, MULTI_LINE}

    /**
     * Sets the line mode for this editor (single or multi-line).
     * @param mode The mode.  Not {@code null}.
     */
    void setMode(Mode mode);

    void setSuggestOracle(SuggestOracle oracle);

    void setAutoSelectSuggestions(boolean autoSelectSuggestions);

    void setPrimitiveDataStyleName(Optional<String> styleName);

    void clearPrimitiveDataStyleName();

    void setTitle(String title);

    void showErrorMessage(SafeHtml errorMessage, Set<EntityType<?>> expectedTypes);

    void clearErrorMessage();

    void setWrap(boolean wrap);

    void requestFocus();
}
