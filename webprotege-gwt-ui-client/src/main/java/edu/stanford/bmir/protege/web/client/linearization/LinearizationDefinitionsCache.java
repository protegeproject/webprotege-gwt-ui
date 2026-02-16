package edu.stanford.bmir.protege.web.client.linearization;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchErrorMessageDisplay;
import edu.stanford.bmir.protege.web.shared.linearization.GetLinearizationDefinitionsAction;
import edu.stanford.bmir.protege.web.shared.linearization.GetLinearizationDefinitionsResult;
import edu.stanford.bmir.protege.web.shared.linearization.LinearizationDefinition;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Cache shared pentru definițiile de linearizare (globale, nu depind de proiect).
 * Evită apeluri multiple de backend când mai multe componente cer aceleași date.
 */
@edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton
public class LinearizationDefinitionsCache {

    private final DispatchServiceManager dispatch;
    private final DispatchErrorMessageDisplay errorDisplay;

    private List<LinearizationDefinition> cached = null;
    private final List<Consumer<List<LinearizationDefinition>>> pendingCallbacks = new ArrayList<>();
    private boolean loading = false;

    @Inject
    public LinearizationDefinitionsCache(@Nonnull DispatchServiceManager dispatch,
                                         @Nonnull DispatchErrorMessageDisplay errorDisplay) {
        this.dispatch = dispatch;
        this.errorDisplay = errorDisplay;
    }

    /**
     * Încarcă definițiile. Dacă sunt în cache, apelează callback-ul imediat.
     * Altfel face un singur request și notifică toate callback-urile în așteptare.
     */
    public void load(@Nonnull Consumer<List<LinearizationDefinition>> callback) {
        if (cached != null) {
            callback.accept(cached);
            return;
        }
        pendingCallbacks.add(callback);
        if (!loading) {
            loading = true;
            dispatch.execute(GetLinearizationDefinitionsAction.create(),
                    new DispatchServiceCallback<GetLinearizationDefinitionsResult>(errorDisplay) {
                        @Override
                        public void handleSuccess(GetLinearizationDefinitionsResult result) {
                            cached = result.getDefinitionList();
                            loading = false;
                            for (Consumer<List<LinearizationDefinition>> cb : pendingCallbacks) {
                                cb.accept(cached);
                            }
                            pendingCallbacks.clear();
                        }

                        @Override
                        public void handleFinally() {
                            loading = false;
                            if (cached == null) {
                                pendingCallbacks.clear();
                            }
                        }
                    });
        }
    }
}
