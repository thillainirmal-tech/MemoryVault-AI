package dev.thillainirmal.memoryvault;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:memory-security;MODE=PostgreSQL;DB_CLOSE_DELAY=-1",
        "spring.datasource.username=sa", "spring.datasource.password=",
        "memoryvault.encryption-key=AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=",
        "memoryvault.api-key=integration-test-key-32-characters"
})
@AutoConfigureMockMvc
class MemorySecurityIntegrationTest {
    @Autowired MockMvc mvc;

    @Test void landingIsPublicButMemoryRequiresAuthentication() throws Exception {
        mvc.perform(get("/")).andExpect(status().isOk());
        mvc.perform(get("/api/memory/test")).andExpect(status().isForbidden());
    }

    @Test void authenticatedWriteRedactsSecretsAndValidatesTtl() throws Exception {
        mvc.perform(post("/api/memory/test").header("X-API-Key", "integration-test-key-32-characters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"role\":\"user\",\"content\":\"email me at person@example.com api_key=secret\",\"mediaType\":\"text/plain\",\"ttlHours\":24}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.content").value(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("person@example.com"))));
        mvc.perform(post("/api/memory/test").header("X-API-Key", "integration-test-key-32-characters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"role\":\"user\",\"content\":\"hello\",\"ttlHours\":0}"))
                .andExpect(status().isBadRequest());
    }
}
