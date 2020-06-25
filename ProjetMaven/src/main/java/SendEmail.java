import javafx.scene.control.Alert;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

/* Classe qui permet d'envoyer la brochure courrante par mail*/
public class SendEmail {

    public static boolean mailChecker(String mail){ //verifie que le texte donnee est bien un email
        if(mail.contains("@") && mail.contains(".") && mail.indexOf('@')< mail.lastIndexOf('.'))
            return true;
        return false;
    }

    public static void sendFileEmail(String to,File f) {

        // adresse mail du projet
        String from = "LegoPI4.2019@gmail.com";

        String host = "smtp.gmail.com";

        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, "ProjetInfo2"); // mot de passe
            }
        });


        try {
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from));

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            message.setSubject("Brochure Lego"); // object du mail

            Multipart multipart = new MimeMultipart();

            MimeBodyPart attachmentPart = new MimeBodyPart(); // permet de joindre des fichiers au mail

            MimeBodyPart textPart = new MimeBodyPart();

            try {
                attachmentPart.attachFile(f);
                textPart.setText("Voici votre brochure \n"); // message du mail
                multipart.addBodyPart(textPart);
                multipart.addBodyPart(attachmentPart);
            } catch (IOException e) {
                e.printStackTrace();
            }
            message.setContent(multipart);

            System.out.println("Envoie en cours ...");
            // Send message
            Transport.send(message);
            System.out.println("Envoyé, regardez vos mails...");
        } catch (MessagingException mex) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Aucune adresse email n'a été indiquée");
                alert.setContentText("Veuillez inscrire votre adresse email dans le champs indiqué");
                alert.showAndWait();
        }
    }

}
