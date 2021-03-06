package io.sphere.sdk.client;

import io.sphere.sdk.http.HttpClient;
import io.sphere.sdk.projects.Project;
import io.sphere.sdk.projects.queries.ProjectGet;
import io.sphere.sdk.test.IntegrationTest;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class ApacheClientTest extends IntegrationTest {
    @Test
    public void itWorks() throws Exception {
        final SphereClientConfig config = getSphereConfig();
        final HttpClient httpClient = ApacheHttpClientAdapter.of();
        final SphereAccessTokenSupplier supplier = SphereAccessTokenSupplier.ofOneTimeFetchingToken(config, httpClient, false);
        final SphereClient client = SphereClient.of(config, httpClient, supplier);
        final Project project = client.execute(ProjectGet.of()).join();
        assertThat(project.getKey()).isEqualTo(getSphereConfig().getProjectKey());
    }
}
