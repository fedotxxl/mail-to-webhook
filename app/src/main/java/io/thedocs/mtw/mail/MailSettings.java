package io.thedocs.mtw.mail;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by fbelov on 22.03.18.
 */
@ConfigurationProperties(prefix = "mail")
@Getter
@Setter
public class MailSettings {
    private String host;
    private String user;
    private String password;
    private String folderToCheck;
    private String urlToNotify;
}
