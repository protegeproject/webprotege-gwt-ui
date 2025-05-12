package edu.stanford.bmir.protege.web.client.role;

import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.editor.ValueEditorFactory;
import edu.stanford.bmir.protege.web.shared.access.Capability;

import javax.inject.Inject;
import javax.inject.Provider;

public class CapabilityValueEditorFactory implements ValueEditorFactory<Capability> {

    private final Provider<CapabilityValueEditor> capabilityValueEditorProvider;

    @Inject
    public CapabilityValueEditorFactory(Provider<CapabilityValueEditor> capabilityValueEditorProvider) {
        this.capabilityValueEditorProvider = capabilityValueEditorProvider;
    }

    @Override
    public ValueEditor<Capability> createEditor() {
        return capabilityValueEditorProvider.get();
    }
}
