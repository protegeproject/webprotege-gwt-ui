package edu.stanford.bmir.protege.web.client.editor;

import edu.stanford.bmir.protege.web.client.frame.ObjectPropertyFrameEditor;
import edu.stanford.bmir.protege.web.client.uuid.UuidV4Provider;
import edu.stanford.bmir.protege.web.shared.frame.GetObjectPropertyFrameAction;
import edu.stanford.bmir.protege.web.shared.frame.GetObjectPropertyFrameResult;
import edu.stanford.bmir.protege.web.shared.frame.ObjectPropertyFrame;
import edu.stanford.bmir.protege.web.shared.frame.UpdateObjectPropertyFrameAction;
import edu.stanford.bmir.protege.web.shared.perspective.ChangeRequestId;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class ObjectPropertyFrameEditorManager implements EditorManager<OWLEntityContext, ObjectPropertyFrame, GetObjectPropertyFrameAction, GetObjectPropertyFrameResult> {

    private final ObjectPropertyFrameEditor editor;
    private final UuidV4Provider uuidV4Provider;

    @Inject
    public ObjectPropertyFrameEditorManager(ObjectPropertyFrameEditor editor, UuidV4Provider uuidV4Provider) {
        this.editor = editor;
        this.uuidV4Provider = uuidV4Provider;
    }

    @Override
    public EditorView<ObjectPropertyFrame> getView(OWLEntityContext editorContext) {
        return editor;
    }

    @Override
    public GetObjectPropertyFrameAction createAction(OWLEntityContext editorContext) {
        return GetObjectPropertyFrameAction.create(editorContext.getProjectId(), editorContext.getEntity().asOWLObjectProperty());
    }

    @Override
    public ObjectPropertyFrame extractObject(GetObjectPropertyFrameResult result) {
        return result.getFrame();
    }

    @Override
    public UpdateObjectPropertyFrameAction createUpdateObjectAction(ObjectPropertyFrame pristineObject, ObjectPropertyFrame editedObject, OWLEntityContext editorContext) {
        return UpdateObjectPropertyFrameAction.create(ChangeRequestId.get(uuidV4Provider.get()),
                                                        editorContext.getProjectId(),
                                                      pristineObject.toPlainFrame(),
                                                      editedObject.toPlainFrame());
    }

    @Override
    public String getDescription(OWLEntityContext editorContext) {
        return "Object property description";
    }
}
