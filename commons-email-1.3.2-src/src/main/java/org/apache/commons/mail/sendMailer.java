package org.apache.commons.mail;

public class sendMailer {
  public void sendMail(String to, String from, String subject, String msg) throws EmailException {
    Email email = new SimpleEmail();
    email.addTo(to);
    email.setFrom(from);
    email.setSubject(subject);
    email.setMsg(msg);
    email.setHostName("abc123.com");
    email.send();
  }
}
