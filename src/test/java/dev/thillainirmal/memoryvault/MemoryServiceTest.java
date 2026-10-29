package dev.thillainirmal.memoryvault;
import org.junit.jupiter.api.*;import org.springframework.jdbc.core.JdbcTemplate;import org.springframework.jdbc.datasource.embedded.*;import static org.assertj.core.api.Assertions.assertThat;
class MemoryServiceTest{
 MemoryService s;@BeforeEach void setup(){var ds=new EmbeddedDatabaseBuilder().generateUniqueName(true).setType(EmbeddedDatabaseType.H2).addScript("schema.sql").build();s=new MemoryService(new JdbcTemplate(ds));}
 @Test void redactsSensitiveValues(){assertThat(s.redact("email a@b.com phone +91 9876543210 api_key=secret")).contains("[EMAIL_REDACTED]","[PHONE_REDACTED]","[SECRET_REDACTED]").doesNotContain("secret");}
 @Test void remembersAndRecalls(){s.remember("c","user","hello",null,24);assertThat(s.recall("c")).singleElement().extracting(MemoryService.Turn::content).isEqualTo("hello");}
 @Test void tracksMultimodalContext(){s.remember("c","user","diagram","image/png",24);assertThat(s.context("c").multimodal()).isTrue();}
 @Test void forgetsConversation(){s.remember("c","user","hello",null,24);assertThat(s.forget("c")).isEqualTo(1);assertThat(s.recall("c")).isEmpty();}
}
