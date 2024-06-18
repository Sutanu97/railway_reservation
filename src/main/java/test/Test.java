package test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.temporal.ChronoUnit;
import java.util.Properties;

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

public class Test {
	public static void main(String[] args) {
		System.out.println("In main");
		Test t = new Test();
		
		new Thread(()->{
			t.m1();
		},"childThread").start();
		
		System.out.println("End of main");
//		new Thread(()->{
//			t.m1();
//		}).start();
		
		
		//		String fileName = "/Users/sutanubhattacharya/Documents/mailConfigs.properties";
//		String username = "bhattacharyasutanu97";
//		String password = "gakgaidvrqvvqzas";
//		try {
//			InputStream ip = new FileInputStream(fileName);
//			Properties properties = new Properties();
//			properties.load(ip);
//			Session mailSession = Session.getInstance(properties, new Authenticator() {
//				
//				@Override
//				protected PasswordAuthentication getPasswordAuthentication() {
//					return new PasswordAuthentication(username, password);
//				}
//			});
//			
//			Message message = new MimeMessage(mailSession);
//			message.setRecipient(RecipientType.TO, new InternetAddress("sutbhat@gmail.com"));
//			message.setFrom(new InternetAddress("bhattacharyasutanu97@gmail.com"));
//			message.setText("helllo!");
//			Transport.send(message);
//			System.out.println("Email sent");
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (AddressException e) {
//			e.printStackTrace();
//		} catch (MessagingException e) {
//			e.printStackTrace();
//		}
		
	}

	private void m1() {
		System.out.println("In child");
		System.out.println(this);
		System.out.println("End of child");
	}
}
