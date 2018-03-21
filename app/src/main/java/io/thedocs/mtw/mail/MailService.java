package io.thedocs.mtw.mail;

import io.thedocs.soyuz.to;
import lombok.SneakyThrows;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import java.util.List;
import java.util.Properties;

public class MailService {

    @SneakyThrows
    public List<Message> check(String host, String user, String password, String folderToCheck) {
            //create properties field
            Properties properties = new Properties();

            properties.put("mail.pop3.host", host);
            properties.put("mail.pop3.port", "995");
            properties.put("mail.pop3.starttls.enable", "true");
            Session emailSession = Session.getDefaultInstance(properties);

            //create the POP3 store object and connect with the pop server
            Store store = emailSession.getStore("pop3s");

            store.connect(host, user, password);

            //create the folder object and open it
            Folder emailFolder = store.getFolder(folderToCheck);
            emailFolder.open(Folder.READ_ONLY);

            // retrieve the messages from the folder in an array and print it
            Message[] messages = emailFolder.getMessages();

            //close the store and folder objects
            emailFolder.close(false);
            store.close();

        return to.list(messages);
    }
}