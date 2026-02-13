package edu.stanford.bmir.protege.web.client.editor;

import edu.stanford.bmir.protege.web.client.frame.ClassFrameEditor;
import edu.stanford.bmir.protege.web.client.uuid.UuidV4Provider;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.GetClassFrameAction;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.UpdateClassFrameAction;
import edu.stanford.bmir.protege.web.shared.frame.ClassFrame;
import edu.stanford.bmir.protege.web.shared.frame.GetClassFrameResult;
import edu.stanford.bmir.protege.web.shared.frame.UpdateFrameAction;
import edu.stanford.bmir.protege.web.shared.perspective.ChangeRequestId;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class ClassFrameEditorManager implements EditorManager<OWLEntityContext, ClassFrame, GetClassFrameAction, GetClassFrameResult> {

    private ClassFrameEditor editor;
    private final UuidV4Provider uuidV4Provider;

    @Inject
    public ClassFrameEditorManager(ClassFrameEditor editor, UuidV4Provider uuidV4Provider) {
        this.editor = editor;
        this.uuidV4Provider = uuidV4Provider;
    }

    @Override
    public EditorView<ClassFrame> getView(OWLEntityContext context) {
        return editor;
    }

    @Override
    public GetClassFrameAction createAction(OWLEntityContext editorContext) {
        return GetClassFrameAction.create(editorContext.getEntity().asOWLClass(), editorContext.getProjectId());
    }

    @Override
    public ClassFrame extractObject(GetClassFrameResult result) {
        return result.getFrame();
    }

    @Override
    public UpdateFrameAction createUpdateObjectAction(ClassFrame pristineObject, ClassFrame editedObject, OWLEntityContext editorContext) {
        return UpdateClassFrameAction.create(ChangeRequestId.get(uuidV4Provider.get()),
                                            editorContext.getProjectId(),
                                             pristineObject.toPlainFrame(),
                                             editedObject.toPlainFrame());
    }

    @Override
    public String getDescription(OWLEntityContext editorContext) {
        return "Class description";
    }
}
