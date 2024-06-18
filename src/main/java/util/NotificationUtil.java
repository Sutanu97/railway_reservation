package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import dto.Traveller;
import entity.Train;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMessage.RecipientType;
import jakarta.servlet.http.HttpSession;

public class NotificationUtil {

	public static void sendEmailConfirmation(Train train,String fromStation, String toStation ,LocalDate dateOfJourney, String recipientEmailId, Map<String, Object> bookingStatus) {

//		LocalDate dateOfJourney = LocalDate.of(Integer.parseInt(doj.split("/")[2]), Integer.parseInt(doj.split("/")[1]), Integer.parseInt(doj.split("/")[0]));
		
		String subject = "Ticket booking confirmation : Train {0}, from {1}, to {2}, on {3}";
		subject = MessageFormat.format(subject, train.getNumber().toString(), fromStation, toStation, dateOfJourney.toString());
		int c = 1;
		StringBuffer text = new StringBuffer("Your ticket from {0} to {1} on {2} has been succesfully booked in {3}({4}).");
		text.append("\n");
		text.append("Your PNR no. is {5}").append("\n");
		text.append("Travellers:").append("\n");
		Map<Traveller, String> travellerVsBerthMap = (Map<Traveller, String>) bookingStatus.get("travellerVsBerth");
		for(Traveller traveller : travellerVsBerthMap.keySet()) {
			String berth = travellerVsBerthMap.get(traveller);
			text.append(c++).append(". ").append(traveller.getName()).append(" (").append(traveller.getGender()).append(",").append(traveller.getAge()).append(")").append("\t").append(berth).append("\n");
		}
		text.append("Wish you a happy and a comfortable journey!").append("\n\n");
		String content = text.toString();
		content = MessageFormat.format(content, fromStation, toStation, dateOfJourney.toString(), train.getName(), train.getNumber().toString(), bookingStatus.get("pnr"));
		try {
			InputStream ip = new FileInputStream(Constants.SMTP_CONFIG_FILE);
			Properties properties = new Properties();
			properties.load(ip);
			ip = new FileInputStream(Constants.CREDENTIALS_PROPERTIES_FILE);
			Properties cp = new Properties();
			cp.load(ip);
			Session mailSession = Session.getInstance(properties, new Authenticator() {
				
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(cp.getProperty("mail.username"), cp.getProperty("mail.password"));
				}
			});
			
			Message message = new MimeMessage(mailSession);
			message.setRecipient(RecipientType.TO, new InternetAddress(recipientEmailId));
			message.setFrom(new InternetAddress("bhattacharyasutanu97@gmail.com"));
			message.setSubject(subject);
			message.setText(content);
			Transport.send(message);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void sendCancellationEmailConfiguration(Integer pnr, List<String> travellers, String toEmail) {
		String subject = "Ticket cancellation confirmation : pnr {0}";
		subject = MessageFormat.format(subject, pnr);
		StringBuffer text = new StringBuffer();
		text.append("Your ticket with pnr {0} has been successfully cancelled for the below passengers!");
		text.append("\n");
		for(int i=1;i<= travellers.size();i++) {
			text.append(i).append(". ").append(travellers.get(i-1));
		}
		String content = text.toString();
		content = MessageFormat.format(content, pnr);
		try {
			FileInputStream fileInputStream = new FileInputStream(Constants.CREDENTIALS_PROPERTIES_FILE);
			Properties cp = new Properties();
			cp.load(fileInputStream);
			
			fileInputStream = new FileInputStream(Constants.SMTP_CONFIG_FILE);
			Properties properties = new Properties();
			properties.load(fileInputStream);
			
			Session mailSession = Session.getInstance(properties, new Authenticator() {

				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(cp.getProperty("mail.username"), cp.getProperty("mail.password"));
				}
				
			});
			
			Message message = new MimeMessage(mailSession);
			try {
				message.setFrom(new InternetAddress("bhattacharyasutanu97@gmail.com"));
				message.setRecipient(RecipientType.TO, new InternetAddress(toEmail));
				message.setSubject(subject);
				message.setText(content);
				Transport.send(message);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
