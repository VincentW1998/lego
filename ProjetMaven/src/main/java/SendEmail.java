import javafx.scene.control.Alert;

import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class SendEmail {

    public static boolean mailChecker(String mail){ //verifie que le texte donnee est bien un email
        if(mail.contains("@") && mail.contains(".") && mail.indexOf('@')< mail.lastIndexOf('.'))
            return true;
        return false;
    }

    public static void sendFileEmail(String to,File f) {

        // Recipient's email ID needs to be mentioned.

        // Sender's email ID needs to be mentioned
        String from = "LegoPI4.2019@gmail.com";

        // Assuming you are sending email from through gmails smtp
        String host = "smtp.gmail.com";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication(from, "ProjetInfo2");

            }

        });


        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("Brochure Lego");

            Multipart multipart = new MimeMultipart();

            MimeBodyPart attachmentPart = new MimeBodyPart();

            MimeBodyPart textPart = new MimeBodyPart();

            try {
//                    File f =new File("src/main/resources/Brochures/brochure.pdf");


                attachmentPart.attachFile(f);
                textPart.setText("Voici votre brochure");
                multipart.addBodyPart(textPart);
                multipart.addBodyPart(attachmentPart);
            } catch (IOException e) {
                e.printStackTrace();
            }
            message.setContent(multipart);

            System.out.println("sending...");
            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
//            mex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Aucune adresse email n'a été indiquée");
                alert.setContentText("Veuillez inscrire votre adresse email dans le champs indiqué");
                alert.showAndWait();
        }
    }

}
