package edu.stanford.bmir.protege.web.shared;

import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.form.field.FormRegionId;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class DisplayContextBuilderTest {

    private DisplayContextBuilder builder;

    @Before
    public void setup() {
        builder = new DisplayContextBuilder();
    }

    @Test
    public void shouldBuildProperly() {
        ProjectId projectId = ProjectId.getNil();
        PerspectiveId perspectiveId = PerspectiveId.generate();
        ViewId viewId = ViewId.create("TestViewId");
        ViewNodeId viewNodeId = ViewNodeId.get("TestNodeId");
        Map<String, String> viewProperties = Collections.emptyMap();
        List<FormId> formIds = Collections.emptyList();
        FormRegionId formRegionId = FormRegionId.get(UUID.randomUUID().toString());
        DisplayContext displayContext = builder.setProjectId(projectId)
                .setPerspectiveId(perspectiveId)
                .setViewId(viewId)
                .setViewNodeId(viewNodeId)
                .setViewProperties(Collections.emptyMap())
                .setFormIds(formIds)
                .setFormFieldId(formRegionId)
                .build();
        assertEquals(displayContext.getProjectId(), projectId);
        assertEquals(displayContext.getPerspectiveId(), perspectiveId);
        assertEquals(displayContext.getViewId(), viewId);
        assertEquals(displayContext.getViewProperties(), viewProperties);
        assertEquals(displayContext.getFormIds(), formIds);
        assertEquals(displayContext.getFormFieldId(), formRegionId);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldNotBuildWithoutProjectId() {
        PerspectiveId perspectiveId = PerspectiveId.generate();
        ViewId viewId = ViewId.create("TestViewId");
        Map<String, String> viewProperties = Collections.emptyMap();
        List<FormId> formIds = Collections.emptyList();
        FormRegionId formRegionId = FormRegionId.get(UUID.randomUUID().toString());
        builder
                .setPerspectiveId(perspectiveId)
                .setViewId(viewId)
                .setViewProperties(viewProperties)
                .setFormIds(formIds)
                .setFormFieldId(formRegionId)
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldNotBuildWithoutPerspectiveId() {
        ProjectId projectId = ProjectId.getNil();
        ViewId viewId = ViewId.create("TestViewId");
        Map<String, String> viewProperties = Collections.emptyMap();
        List<FormId> formIds = Collections.emptyList();
        FormRegionId formRegionId = FormRegionId.get(UUID.randomUUID().toString());
        builder.setProjectId(projectId)
                .setViewId(viewId)
                .setViewProperties(viewProperties)
                .setFormIds(formIds)
                .setFormFieldId(formRegionId)
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldNotBuildWithoutViewId() {
        ProjectId projectId = ProjectId.getNil();
        PerspectiveId perspectiveId = PerspectiveId.generate();
        Map<String, String> viewProperties = Collections.emptyMap();
        List<FormId> formIds = Collections.emptyList();
        FormRegionId formRegionId = FormRegionId.get(UUID.randomUUID().toString());
        builder.setProjectId(projectId)
                .setPerspectiveId(perspectiveId)
                .setViewProperties(viewProperties)
                .setFormIds(formIds)
                .setFormFieldId(formRegionId)
                .build();
    }



    @Test(expected = IllegalStateException.class)
    public void shouldNotBuildWithoutNodeProperties() {
        ProjectId projectId = ProjectId.getNil();
        PerspectiveId perspectiveId = PerspectiveId.generate();
        ViewId viewId = ViewId.create(UUID.randomUUID().toString());
        List<FormId> formIds = Collections.emptyList();
        FormRegionId formRegionId = FormRegionId.get(UUID.randomUUID().toString());
        builder.setProjectId(projectId)
                .setPerspectiveId(perspectiveId)
                .setViewId(viewId)
                .setFormIds(formIds)
                .setFormFieldId(formRegionId)
                .build();
    }

    @Test
    public void shouldBuildWithoutFormFieldId() {
        ProjectId projectId = ProjectId.getNil();
        PerspectiveId perspectiveId = PerspectiveId.generate();
        ViewId viewId = ViewId.create("TestViewId");
        ViewNodeId viewNodeId = ViewNodeId.get("TestViewNodeId");
        Map<String, String> viewProperties = Collections.emptyMap();
        List<FormId> formIds = Collections.emptyList();
        DisplayContext displayContext = builder.setProjectId(projectId)
                .setPerspectiveId(perspectiveId)
                .setViewId(viewId)
                .setViewNodeId(viewNodeId)
                .setViewProperties(viewProperties)
                .setFormIds(formIds)
                .build();
        assertEquals(displayContext.getProjectId(), projectId);
        assertEquals(displayContext.getPerspectiveId(), perspectiveId);
        assertEquals(displayContext.getViewId(), viewId);
        assertEquals(displayContext.getViewProperties(), viewProperties);
        assertEquals(displayContext.getFormIds(), formIds);
    }

}