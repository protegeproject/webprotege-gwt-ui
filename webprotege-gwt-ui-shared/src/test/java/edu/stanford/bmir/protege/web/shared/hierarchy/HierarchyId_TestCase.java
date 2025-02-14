package edu.stanford.bmir.protege.web.shared.hierarchy;

import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class HierarchyId_TestCase {

    private static final String ID = "TheHierarchy";

    private HierarchyId hierarchyId;

    @BeforeEach
    public void setUp() {
        hierarchyId = HierarchyId.get(ID);
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void shouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            HierarchyId.get(null);
        });
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(hierarchyId, is(hierarchyId));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(hierarchyId.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(hierarchyId, is(HierarchyId.get(ID)));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(hierarchyId.hashCode(), is(HierarchyId.get(ID).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(hierarchyId.toString(), Matchers.startsWith("HierarchyId"));
    }


    @Test
    public void shouldReturnClassHierarchyIsBuiltIn() {
        Assertions.assertThat(HierarchyId.CLASS_HIERARCHY.isBuiltIn()).isTrue();
    }

    @Test
    public void shouldReturnObjectPropertyHierarchyIsBuiltIn() {
        Assertions.assertThat(HierarchyId.OBJECT_PROPERTY_HIERARCHY.isBuiltIn()).isTrue();
    }

    @Test
    public void shouldReturnDataPropertyHierarchyIsBuiltIn() {
        Assertions.assertThat(HierarchyId.DATA_PROPERTY_HIERARCHY.isBuiltIn()).isTrue();
    }

    @Test
    public void shouldReturnAnnotationPropertyHierarchyIsBuiltIn() {
        Assertions.assertThat(HierarchyId.ANNOTATION_PROPERTY_HIERARCHY.isBuiltIn()).isTrue();
    }

    @Test
    public void shouldReturnOtherHierarchyIsNotBuiltIn() {
        Assertions.assertThat(HierarchyId.get("other").isBuiltIn()).isFalse();
    }

}
