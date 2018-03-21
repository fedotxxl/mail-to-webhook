package io.thedocs.mtw;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.belov.soyuz.json.JacksonMapper;
import io.belov.soyuz.json.JacksonUtils;
import io.thedocs.mtw.mail.MailSettings;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.autoconfigure.web.HttpEncodingAutoConfiguration;
import org.springframework.boot.autoconfigure.web.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ServerPropertiesAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@ImportResource({"classpath:/applicationContext.xml"})
@EnableTransactionManagement(proxyTargetClass = true)
@Import({
        App.Config.class,
        MailSettings.class,

        // ORDER IS IMPORTANT! Configurations should be before autoconfigurations

        HttpEncodingAutoConfiguration.class,
        HttpMessageConvertersAutoConfiguration.class,
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        LiquibaseAutoConfiguration.class,
        JacksonAutoConfiguration.class,
        PropertyPlaceholderAutoConfiguration.class,
        TransactionAutoConfiguration.class,
        ServerPropertiesAutoConfiguration.class,
        JooqAutoConfiguration.class,
})
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    public static class Config {

        @Bean
        public ObjectMapper objectMapper() {
            ObjectMapper mapper = new ObjectMapper();

            mapper.findAndRegisterModules();
            mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

            SimpleModule module = new SimpleModule();
            module.addSerializer(ZonedDateTime.class, new JsonSerializer<ZonedDateTime>() {
                @Override
                public void serialize(ZonedDateTime zonedDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
                    jsonGenerator.writeString(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZZ").format(zonedDateTime));
                }
            });
            mapper.registerModule(module);

            JacksonUtils.CONFIGURER_FAIL_SAFE.accept(mapper);

            return mapper;
        }


        @Bean
        public JacksonMapper jacksonMapper() {
            ObjectMapper mapper = new ObjectMapper();

            mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            mapper.registerModule(new JavaTimeModule());

            JacksonUtils.CONFIGURER_CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES.accept(mapper);
            JacksonUtils.CONFIGURER_FAIL_SAFE.accept(mapper);

            return new JacksonMapper(mapper);
        }
    }

}
