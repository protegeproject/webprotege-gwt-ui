package edu.stanford.bmir.protege.web.shared.hierarchy;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.bmir.protege.web.client.hierarchy.ClassHierarchyDescriptor;
import edu.stanford.bmir.protege.web.client.hierarchy.HierarchyDescriptor;
import edu.stanford.bmir.protege.web.server.jackson.ObjectMapperProvider;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class NamedHierarchyJsonTest {

        private JacksonTester<NamedHierarchy> jacksonTester;

        private OWLDataFactory dataFactory;

        @Before
        public void setup() {
            ObjectMapper objectMapper = new ObjectMapperProvider().get();
            JacksonTester.initFields(this, objectMapper);
        }

        @Test
        public void testSerializeNamedHierarchy() throws Exception {
            dataFactory = DataFactory.get();
            HierarchyId hierarchyId = HierarchyId.get("test-hierarchy-id");
            LanguageMap label = LanguageMap.of("en", "Test Label");
            LanguageMap description = LanguageMap.of("en", "Test Description");
            OWLClass owlClass = dataFactory.getOWLThing();
            HierarchyDescriptor hierarchyDescriptor = ClassHierarchyDescriptor.get(Collections.singleton(owlClass));

            NamedHierarchy namedHierarchy = NamedHierarchy.get(hierarchyId, label, description, hierarchyDescriptor);

            JsonContent<NamedHierarchy> json = jacksonTester.write(namedHierarchy);
            System.out.println(json.getJson());

            assertThat(json).hasJsonPathStringValue("$.hierarchyId");
            assertThat(json).hasJsonPathMapValue("$.label");
            assertThat(json).hasJsonPathMapValue("$.description");
            assertThat(json).hasJsonPathMapValue("$.hierarchyDescriptor");
        }

        @Test
        public void testDeserializeNamedHierarchy() throws Exception {
            var json = "{\"hierarchyId\": \"test-hierarchy-id\",\n" +
                    "            \"label\": {\n" +
                    "                \"en\": \"Test Label\"\n" +
                    "            },\n" +
                    "            \"description\": {\n" +
                    "                \"en\": \"Test Description\"\n" +
                    "            },\n" +
                    "            \"hierarchyDescriptor\": {\n" +
                    "                \"@type\": \"ClassHierarchyDescriptor\",\n" +
                    "                \"roots\": [\n" +
                    "                    {\n" +
                                            "\"@type\": \"Class\"," +
                    "                        \"iri\": \"http://www.w3.org/2002/07/owl#Thing\"\n" +
                    "                    }\n" +
                    "                ]\n" +
                    "            }\n" +
                    "        }";

            var objectContent = jacksonTester.parse(json);
            var namedHierarchy = objectContent.getObject();

            assertThat(namedHierarchy.getHierarchyId()).isNotNull();
            assertThat(namedHierarchy.getLabel()).isNotNull();
            assertThat(namedHierarchy.getDescription()).isNotNull();
            assertThat(namedHierarchy.getHierarchyDescriptor()).isNotNull();
        }

    }
