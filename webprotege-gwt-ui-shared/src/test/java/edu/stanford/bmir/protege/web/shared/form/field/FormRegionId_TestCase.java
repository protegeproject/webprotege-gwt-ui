package edu.stanford.bmir.protege.web.shared.form.field;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class FormRegionId_TestCase {

    public static final String UUID = "12345678-1234-1234-1234-123456789abc";

    @Test
    public void shouldGetFormRegionIdWithSuppliedUUID() {
        FormRegionId id = FormRegionId.get(UUID);
        assertThat(id.getId(), equalTo(UUID));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAcceptMalformedId() {
        FormRegionId.get("NotAUUID");
    }

    @Test(expected = NullPointerException.class)
    public void shouldNotAcceptNull() {
        FormRegionId.get(null);
    }
}