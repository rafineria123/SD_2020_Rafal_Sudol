package pl.okazje.project.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import pl.okazje.project.entities.User;
import pl.okazje.project.services.EmailService;
import pl.okazje.project.services.UserService;

import javax.mail.MessagingException;
import java.util.UUID;

@Component
public class RegistrationListener implements
        ApplicationListener<OnRegistrationCompleteEvent> {


    @Autowired
    private UserService userService;


    @Autowired
    private EmailService mailSender;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
            this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.createVerificationToken(user,token);
        String recipientAddress = user.getEmail();
        String subject = "Norgie - Aktywuj swoje konto";
        String confirmationUrl = "/registrationConfirm?token=" + token;
        mailSender.sendEmail(recipientAddress,subject,"Aby aktywowac swoje konto kliknij w poniższy link,\n http://54.227.52.123" + confirmationUrl);
    }
}
