package pl.okazje.project.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import pl.okazje.project.entities.Token;
import pl.okazje.project.entities.User;
import pl.okazje.project.repositories.TokenRepository;
import pl.okazje.project.services.SendMail;
import pl.okazje.project.services.UserService;

import javax.mail.MessagingException;
import java.util.UUID;

@Component
public class RegistrationListener implements
        ApplicationListener<OnRegistrationCompleteEvent> {


    @Autowired
    private UserService userService;


    @Autowired
    private SendMail mailSender;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        try {
            this.confirmRegistration(event);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) throws MessagingException {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.createVerificationToken(user,token);

        String recipientAddress = user.getEmail();
        String subject = "Norgie - Aktywuj swoje konto";
        String confirmationUrl = "/registrationConfirm?token=" + token;

        mailSender.sendingMail(recipientAddress,subject,"Aby aktywowac swoje konto kliknij w poniższy link,\n http://54.92.215.34/" + confirmationUrl);

    }
}
