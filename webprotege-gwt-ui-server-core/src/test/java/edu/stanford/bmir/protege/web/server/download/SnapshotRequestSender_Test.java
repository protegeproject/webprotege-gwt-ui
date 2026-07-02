package edu.stanford.bmir.protege.web.server.download;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.bmir.protege.web.server.jackson.ObjectMapperProvider;
import edu.stanford.bmir.protege.web.server.rpc.JsonRpcEndPoint;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class SnapshotRequestSender_Test {

    private SnapshotRequestSender sender;

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapperProvider().get();
        sender = new SnapshotRequestSender(HttpClient.newHttpClient(),
                                           JsonRpcEndPoint.get(URI.create("http://localhost:7777/api/execute")),
                                           objectMapper);
    }

    @Test
    public void shouldBuildRequestOnCreateSnapshotChannel() throws IOException {
        var body = buildBody(RevisionNumber.getRevisionNumber(3), DownloadFormat.RDF_XML);
        assertThat(body.get("method").asText(), is("webprotege.snapshots.CreateSnapshot"));
        assertThat(body.get("jsonrpc").asText(), is("2.0"));
    }

    @Test
    public void shouldSendDocumentFormatAsMimeType() throws IOException {
        var body = buildBody(RevisionNumber.getRevisionNumber(3), DownloadFormat.RDF_TURLE);
        assertThat(body.get("params").get("documentFormat").asText(), is("text/turtle"));
    }

    @Test
    public void shouldSendRevisionNumberAsLongValue() throws IOException {
        var body = buildBody(RevisionNumber.getRevisionNumber(33), DownloadFormat.RDF_XML);
        assertThat(body.get("params").get("revisionNumber").asLong(), is(33L));
    }

    @Test
    public void shouldSendHeadRevisionAsItsSentinelValue() throws IOException {
        var body = buildBody(RevisionNumber.getHeadRevisionNumber(), DownloadFormat.RDF_XML);
        assertThat(body.get("params").get("revisionNumber").asLong(),
                   is(RevisionNumber.getHeadRevisionNumber().getValue()));
    }

    @Test
    public void shouldSendProjectIdAndFileName() throws IOException {
        var body = buildBody(RevisionNumber.getRevisionNumber(3), DownloadFormat.RDF_XML);
        assertThat(body.get("params").get("projectId").asText(),
                   equalTo(ProjectId.getNil().getId()));
        assertThat(body.get("params").get("fileName").asText(), is("My-Project"));
    }

    private JsonNode buildBody(RevisionNumber revisionNumber, DownloadFormat format) throws IOException {
        var body = sender.buildJsonRpcRequestBody(ProjectId.getNil(),
                                                  revisionNumber,
                                                  format,
                                                  "My-Project");
        return objectMapper.readTree(body);
    }
}
