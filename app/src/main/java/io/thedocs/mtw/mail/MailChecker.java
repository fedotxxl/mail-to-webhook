package io.thedocs.mtw.mail;

import io.thedocs.soyuz.log.LoggerEvents;
import io.thedocs.soyuz.to;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import javax.annotation.PostConstruct;
import javax.mail.Message;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by fbelov on 22.03.18.
 */
@AllArgsConstructor
public class MailChecker {

    private static final LoggerEvents loge = LoggerEvents.getInstance(MailChecker.class);

    private MailService service;
    private MailSettings settings;
    private int repeatIntervalInSeconds;

    @PostConstruct
    private void setUp() {
        new Thread(this::doCheckForever).start();
    }

    @SneakyThrows
    private void doCheckForever() {
        while (true) {
            try {
                doCheckAndNotify();
            } catch (Exception e) {
                loge.error("mail.check.e", e);
            }

            TimeUnit.SECONDS.sleep(repeatIntervalInSeconds);
        }
    }

    private void doCheckAndNotify() {
        List<Message> messages = service.check(settings.getHost(), settings.getUser(), settings.getPassword(), settings.getFolderToCheck());

        loge.info("mails", to.map("messages", messages));
    }
}
