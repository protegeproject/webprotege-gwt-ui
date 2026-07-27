package edu.stanford.bmir.protege.web.shared.event;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Covers the {@code latestOnly} anchor flag added for
 * protegeproject/webprotege-gwt-ui#301. Written in JUnit 5 because this module
 * runs the JUnit-platform provider without a vintage engine, so JUnit 4 test
 * classes here are silently skipped.
 */
public class GetProjectEventsAction_LatestOnly_TestCase {

    @Test
    public void anchorFactorySetsLatestOnly() {
        GetProjectEventsAction action = GetProjectEventsAction.anchor(ProjectId.getNil());
        assertThat(action.isLatestOnly()).isTrue();
        assertThat(action.getSinceTag()).isEqualTo(EventTag.getFirst());
    }

    @Test
    public void createFactoryLeavesLatestOnlyFalse() {
        GetProjectEventsAction action = GetProjectEventsAction.create(EventTag.get(3), ProjectId.getNil());
        assertThat(action.isLatestOnly()).isFalse();
    }

    @Test
    public void latestOnlyIsPartOfEquality() {
        // Same projectId and sinceTag: they must differ purely on the flag, or
        // the JSON/GWT-RPC round-trip tests could not detect the flag dropping.
        GetProjectEventsAction anchor = GetProjectEventsAction.anchor(ProjectId.getNil());
        GetProjectEventsAction since = GetProjectEventsAction.create(EventTag.getFirst(), ProjectId.getNil());
        assertThat(anchor).isNotEqualTo(since);
        assertThat(anchor.hashCode()).isNotEqualTo(since.hashCode());
    }

    @Test
    public void gwtRpcRoundTripPreservesAnchorFlag() throws SerializationException {
        GetProjectEventsAction original = GetProjectEventsAction.anchor(ProjectId.getNil());
        assertThat(gwtRpcRoundTrip(original)).isEqualTo(original);
    }

    @Test
    public void gwtRpcRoundTripPreservesSinceTagWhenNotAnchoring() throws SerializationException {
        GetProjectEventsAction original = GetProjectEventsAction.create(EventTag.get(7), ProjectId.getNil());
        GetProjectEventsAction roundTripped = gwtRpcRoundTrip(original);
        assertThat(roundTripped).isEqualTo(original);
        assertThat(roundTripped.isLatestOnly()).isFalse();
        assertThat(roundTripped.getSinceTag()).isEqualTo(EventTag.get(7));
    }

    /**
     * Runs the action through the custom GWT-RPC serializer exactly as the wire
     * does: {@code serialize} records the primitives, then {@code instantiate}
     * reads them back in the same order.
     */
    private static GetProjectEventsAction gwtRpcRoundTrip(GetProjectEventsAction action) throws SerializationException {
        SerializationStreamWriter writer = mock(SerializationStreamWriter.class);
        GetProjectEventsAction_CustomFieldSerializer.serialize(writer, action);

        ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> intCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Boolean> boolCaptor = ArgumentCaptor.forClass(Boolean.class);
        verify(writer).writeString(stringCaptor.capture());
        verify(writer).writeInt(intCaptor.capture());
        verify(writer).writeBoolean(boolCaptor.capture());

        SerializationStreamReader reader = mock(SerializationStreamReader.class);
        when(reader.readString()).thenReturn(stringCaptor.getValue());
        when(reader.readInt()).thenReturn(intCaptor.getValue());
        when(reader.readBoolean()).thenReturn(boolCaptor.getValue());

        return GetProjectEventsAction_CustomFieldSerializer.instantiate(reader);
    }
}
