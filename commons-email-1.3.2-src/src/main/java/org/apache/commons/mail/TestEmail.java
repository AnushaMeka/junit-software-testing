package org.apache.commons.mail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jvnet.mock_javamail.Mailbox;

import junit.framework.TestCase;



public class TestEmail extends TestCase
{
	protected SimpleEmail email;
	//protected MockEmail email1;
	
	@SuppressWarnings("unused")
	private static final String[] valid_emails =
        {
           "anusha.meka@my.utsa.edu",
           "gummadi.meka@gmail.com",
           "xyz123@gmail.com"
        };

	@Before 
	protected void setUp() 
	{
		email = new SimpleEmail();
	}
	
	@Test
	public void testAddBcc() throws Exception 
	{
		List<InternetAddress> adresses = new ArrayList<InternetAddress>();
		
		adresses.add(new InternetAddress ("anusha.meka@my.utsa.edu"));
		email.addBcc("anusha.meka@my.utsa.edu");
		assertEquals(adresses, email.getBccAddresses());
		
		adresses.add(new InternetAddress ( "gummadi.meka@gmail.com"));
		adresses.add(new InternetAddress ("rishi.varahagiri@gmail.com"));
		adresses.add(new InternetAddress ("gummadivpurnachand@gmail.com"));
		
		String[] address = new String[] {"gummadi.meka@gmail.com", "rishi.varahagiri@gmail.com", "gummadivpurnachand@gmail.com"};
		email.addBcc(address);
		
		assertEquals(adresses, email.getBccAddresses());
	}
	
	@Test
	public void testAddCc() throws Exception
	{
		List<InternetAddress> testCc = new ArrayList<InternetAddress>();
		
		testCc.add(new InternetAddress("add_other@gmail.com"));
		
		email.addCc("add_other@gmail.com");
		
		assertEquals(testCc, email.getCcAddresses());
	}
	
	@Test
	public void testAddHeader() throws IllegalArgumentException
	{
		
		String valueIAE = "value can not be null or empty";
		String nameIAE = "name can not be null or empty";
		
		//Try Working Set
		email.addHeader("Test Name", "Test Value");
		
		//Try value empty
		try
		{
			email.addHeader("Test Name", null);
		}
		catch(IllegalArgumentException IAE)
		{
			assertTrue(IAE.getMessage().equals(valueIAE));
		}
		
		//Try name empty
		try
		{
			email.addHeader(null, "Test Value");
		}
		catch(IllegalArgumentException IAE)
		{
			assertTrue(IAE.getMessage().equals(nameIAE));
		}
	}
	
	@Test
	public void testAddReplyTo() throws EmailException, AddressException
	{
		List<InternetAddress> testList = new ArrayList<InternetAddress>();
		testList.add(new InternetAddress("anusha.meka@utsa.edu"));
		
		email.addReplyTo("anusha.meka@utsa.edu");
		
		assertEquals(testList,email.getReplyToAddresses());
	}
	
	
	@Test
	public void testBuildMimeMessage() throws EmailException, MessagingException
	{
		email.setHostName("fakeHost");
		email.setFrom("gummadivpurnachand@gmail.com");
		email.addTo("rishi.varahagiri@gmail.com");
		email.setSubject("Explaining how to test an email");
		
		Multipart multipart = new MimeMultipart();
	    BodyPart messageBodyPart = new MimeBodyPart();
	    messageBodyPart.setText("certainly not null");
	    multipart.addBodyPart(messageBodyPart);
	    email.setContent((MimeMultipart) multipart);
	    

		
		email.buildMimeMessage();
		
		try
		{
			email.buildMimeMessage();
		}
		catch(IllegalStateException e)
		{
			assertTrue(e.getMessage().equals("The MimeMessage is already built."));
		}
	}
	
	@Test 
	public void testBuildMimeMessage2() throws EmailException
	{
		email.setHostName("fakehost");
		email.setFrom("gummadivpurnachand@gmail.com");
		email.addTo("rishi.varahagiri@gmail.com");
		email.addHeader("do not take anything personal", "some value");
		email.addBcc("chicken@gmail.com");
		email.addCc("somemail@gmail.com");
		email.addReplyTo("burger@gmail.com");
		email.setPopBeforeSmtp(true, "issue fake host", "issue fake username", "give fake password");
		
		try
		{
			email.buildMimeMessage();
		}
		catch(EmailException e)
		{
			assertFalse(e.getMessage().equals(""));
		}
	}
	
	@Test
	public void testGetHostName()
	{
		assertEquals(null, email.getHostName());
		
		email.setHostName("Test Host Name");
		assertEquals("Test Host Name", email.getHostName());
		
		email.setMailSession(Session.getInstance(System.getProperties()));
		assertEquals(null, email.getHostName());
	}
	
	@Test
	public void testGetMailSession() throws EmailException
	{
		
		
		String emailE = "Cannot find valid hostname for mail session";
		
		try
		{
			email.getMailSession();
		}
		catch(EmailException e)
		{
			assertTrue(e.getMessage().equals(emailE));
		}
		
		
		
		email.setAuthentication("Test String", "Test Password");
		
		try
		{
			email.setHostName("Test Host Name");
			email.getMailSession();
		}
		catch(EmailException e)
		{
			assertTrue(e.getMessage().equals(emailE));
		}
	}
	
	@Test
	public void testGetSentDate1()
	{
		Date testDate = new Date();
		assertEquals(email.getSentDate().compareTo(testDate), 0);
	}
	
	@Test
	public void testGetSentDate2()
	{
		Date testDate = new Date();
		email.setSentDate(testDate);
		assertEquals(email.getSentDate().compareTo(testDate),0);
	}
	
	@Test
	public void testGetSocketConnectionTimeout()
	{
		int testTimeoutTime = email.getSocketConnectionTimeout();
		//default time is 60000 miliseconds
		assertEquals(testTimeoutTime, 60000); 
	}
	
	/*@Test
	public void testSend() throws EmailException, MessagingException
	{
		//Test Email Requirements for gmail
		email.setHostName("smtp.gmail.com");
		email.setSmtpPort(465);
		//email.setSSL(true);
		//email.setTLS(false);
		email.setFrom("gummadivpurnachand@gmail.com");
		email.addTo("rishi.varahagiri@gmail.com");
		email.setAuthentication("gummadivpurnachand@gmail.com", "you_are_the_best");
		
		assertEquals(email.send(), email.getMimeMessage().getMessageID());	
	}*/
	
	@Test
	public void testSend() throws Exception{
		  Mailbox.clearAll();
		  sendMailer mailSender = new sendMailer();
		  String subject = "Email mock test";
		  String body = "Email body mock test";
		  mailSender.sendMail("amc@gmail.com", "cinemax@gmail.com", subject, body);
		  List<javax.mail.Message> inbox = Mailbox.get("amc@gmail.com");
		  assertTrue(inbox.size()==1);
		}
	
	@Test
	public void testSetFrom() throws EmailException
	{
		assertEquals(email.setFrom("dummy_email@gmail.com"), email);
	}
	
	@Test
	public void testUpdateContentType()
	{
		
		email.updateContentType("; charset= ASCII");
		email.updateContentType("; charset=");
		email.updateContentType(null);
		
		email.updateContentType("text/");
		
	}
	
	@After
    public void tearDown() {
        email = null;
    }
	
	
}
