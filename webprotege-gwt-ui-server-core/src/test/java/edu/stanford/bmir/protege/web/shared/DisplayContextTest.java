package edu.stanford.bmir.protege.web.shared;

import edu.stanford.bmir.protege.web.MockingUtils;
import edu.stanford.bmir.protege.web.server.jackson.ObjectMapperProvider;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.form.field.FormRegionId;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.JsonbTester;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DisplayContextTest {

    private JacksonTester<DisplayContext> jsonTester;

    private ProjectId projectId;

    private PerspectiveId perspectiveId;

    private ViewId viewId;

    private ViewNodeId viewNodeId;

    private Map<String, String> viewProperties;

    private java.util.List<FormId> formIds;

    private FormRegionId formFieldId;

    private java.util.List<java.util.List<OWLEntity>> selectedPaths;

    private OWLClass owlClass;

    @Before
    public void setUp() {

        JacksonTester.initFields(this, new ObjectMapperProvider().get());

        projectId = ProjectId.valueOf("507b382f-f9db-451c-a0dd-d4e70bb42df7");
        perspectiveId = PerspectiveId.get("21a06f9a-54e9-4946-aeb3-c2366930cd36");
        viewId = ViewId.create("b6389f3d-733d-4dd5-8184-f65e9649fd89");
        viewNodeId = ViewNodeId.get("403f23b3-bb5c-44ad-9f5e-1fc7be87239e");
        viewProperties = Map.of("key", "value");
        formIds = List.of(FormId.get("772f3308-fa5a-4390-b451-6f30172d2cf2"));
        formFieldId = FormRegionId.get("4cbf6d51-f8df-47f3-b5da-a78a723be8dc");
        owlClass = DataFactory.getOWLClass("http://example.org/Class1");
        selectedPaths = java.util.List.of(List.of(owlClass));
    }

    @Test
    public void shouldThrowNullPointerExceptionForNullArguments() {
        assertThatThrownBy(() -> DisplayContext.create(null, perspectiveId, viewId, viewNodeId, viewProperties, formIds, formFieldId, selectedPaths))
                .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> DisplayContext.create(projectId, null, viewId, viewNodeId, viewProperties, formIds, formFieldId, selectedPaths))
                .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> DisplayContext.create(projectId, perspectiveId, null, viewNodeId, viewProperties, formIds, formFieldId, selectedPaths))
                .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> DisplayContext.create(projectId, perspectiveId, viewId, null, viewProperties, formIds, formFieldId, selectedPaths))
                .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> DisplayContext.create(projectId, perspectiveId, viewId, viewNodeId, null, formIds, formFieldId, selectedPaths))
                .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> DisplayContext.create(projectId, perspectiveId, viewId, viewNodeId, viewProperties, formIds, formFieldId, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void shouldSerializeToJsn() throws Exception {
        DisplayContext displayContext = DisplayContext.create(projectId, perspectiveId, viewId, viewNodeId, viewProperties, formIds, formFieldId, selectedPaths);

        JsonContent<DisplayContext> jsonContent = jsonTester.write(displayContext);

        assertThat(jsonContent).hasJsonPathStringValue("$.projectId", projectId.getId());
        assertThat(jsonContent).hasJsonPathStringValue("$.perspectiveId", perspectiveId.getId());
        assertThat(jsonContent).hasJsonPathStringValue("$.viewId", viewId.getId());
        assertThat(jsonContent).hasJsonPathStringValue("$.viewNodeId", viewNodeId.getId());
        assertThat(jsonContent).hasJsonPathStringValue("$.viewProperties.key", "value");
        assertThat(jsonContent).hasJsonPathStringValue("$.formIds[0]", formIds.get(0).getId());
        assertThat(jsonContent).hasJsonPathStringValue("$.formFieldId", formFieldId.getId());
        assertThat(jsonContent).hasJsonPathStringValue("$.selectedPaths[0][0].iri", selectedPaths.get(0).get(0).getIRI().toString());
    }

    @Test
    public void shouldDeserializeFromJson() throws Exception {
        String json = "{\n" +
                "                    \"projectId\": \"507b382f-f9db-451c-a0dd-d4e70bb42df7\",\n" +
                "                    \"perspectiveId\": \"21a06f9a-54e9-4946-aeb3-c2366930cd36\",\n" +
                "                    \"viewId\": \"b6389f3d-733d-4dd5-8184-f65e9649fd89\",\n" +
                "                    \"viewNodeId\": \"403f23b3-bb5c-44ad-9f5e-1fc7be87239e\",\n" +
                "                    \"viewProperties\": {\"key\": \"value\"},\n" +
                "                    \"formIds\": [\"772f3308-fa5a-4390-b451-6f30172d2cf2\"],\n" +
                "                    \"formFieldId\": \"4cbf6d51-f8df-47f3-b5da-a78a723be8dc\",\n" +
                "                    \"selectedPaths\": [[{\"@type\" : \"Class\", \"iri\":\"http://example.org/Class1\"}]]\n" +
                "                }";

        var deserialized = jsonTester.parseObject(json);

        assertThat(deserialized.getProjectId()).isEqualTo(projectId);
        assertThat(deserialized.getPerspectiveId()).isEqualTo(perspectiveId);
        assertThat(deserialized.getViewId()).isEqualTo(viewId);
        assertThat(deserialized.getViewNodeId()).isEqualTo(viewNodeId);
        assertThat(deserialized.getViewProperties()).isEqualTo(viewProperties);
        assertThat(deserialized.getFormIds()).isEqualTo(formIds);
        assertThat(deserialized.getFormFieldId()).isEqualTo(formFieldId);
        assertThat(deserialized.getSelectedPaths().get(0).get(0).getIRI().toString()).isEqualTo("http://example.org/Class1");
    }

}