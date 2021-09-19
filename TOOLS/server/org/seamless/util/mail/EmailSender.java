/*     */ package org.seamless.util.mail;
/*     */ 
/*     */ import java.util.Date;
/*     */ import java.util.Properties;
/*     */ import javax.mail.Address;
/*     */ import javax.mail.BodyPart;
/*     */ import javax.mail.Message;
/*     */ import javax.mail.MessagingException;
/*     */ import javax.mail.Multipart;
/*     */ import javax.mail.Session;
/*     */ import javax.mail.Transport;
/*     */ import javax.mail.internet.InternetAddress;
/*     */ import javax.mail.internet.MimeBodyPart;
/*     */ import javax.mail.internet.MimeMessage;
/*     */ import javax.mail.internet.MimeMultipart;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EmailSender
/*     */ {
/*  34 */   protected final Properties properties = new Properties();
/*     */   protected final String host;
/*     */   protected final String user;
/*     */   protected final String password;
/*     */   
/*     */   public EmailSender(String host, String user, String password) {
/*  40 */     if (host == null || host.length() == 0) {
/*  41 */       throw new IllegalArgumentException("Host is required");
/*     */     }
/*  43 */     this.host = host;
/*  44 */     this.user = user;
/*  45 */     this.password = password;
/*     */     
/*  47 */     this.properties.put("mail.smtp.port", "25");
/*  48 */     this.properties.put("mail.smtp.socketFactory.fallback", "false");
/*  49 */     this.properties.put("mail.smtp.quitwait", "false");
/*  50 */     this.properties.put("mail.smtp.host", host);
/*  51 */     this.properties.put("mail.smtp.starttls.enable", "true");
/*  52 */     if (user != null && password != null) {
/*  53 */       this.properties.put("mail.smtp.auth", "true");
/*     */     }
/*     */   }
/*     */   
/*     */   public Properties getProperties() {
/*  58 */     return this.properties;
/*     */   }
/*     */   
/*     */   public String getHost() {
/*  62 */     return this.host;
/*     */   }
/*     */   
/*     */   public String getUser() {
/*  66 */     return this.user;
/*     */   }
/*     */   
/*     */   public String getPassword() {
/*  70 */     return this.password;
/*     */   }
/*     */   
/*     */   public void send(Email email) throws MessagingException {
/*  74 */     Session session = createSession();
/*     */     
/*  76 */     MimeMessage msg = new MimeMessage(session);
/*     */     
/*  78 */     msg.setFrom((Address)new InternetAddress(email.getSender()));
/*     */     
/*  80 */     InternetAddress[] receipients = { new InternetAddress(email.getRecipient()) };
/*  81 */     msg.setRecipients(Message.RecipientType.TO, (Address[])receipients);
/*     */     
/*  83 */     msg.setSubject(email.getSubject());
/*     */     
/*  85 */     msg.setSentDate(new Date());
/*     */     
/*  87 */     msg.setContent(createContent(email));
/*     */     
/*  89 */     Transport transport = createConnectedTransport(session);
/*  90 */     transport.sendMessage((Message)msg, msg.getAllRecipients());
/*  91 */     transport.close();
/*     */   }
/*     */   
/*     */   protected Multipart createContent(Email email) throws MessagingException {
/*  95 */     MimeBodyPart partOne = new MimeBodyPart();
/*  96 */     partOne.setText(email.getPlaintext());
/*     */     
/*  98 */     MimeMultipart mimeMultipart = new MimeMultipart("alternative");
/*  99 */     mimeMultipart.addBodyPart((BodyPart)partOne);
/*     */     
/* 101 */     if (email.getHtml() != null) {
/* 102 */       MimeBodyPart partTwo = new MimeBodyPart();
/* 103 */       partTwo.setContent(email.getHtml(), "text/html");
/* 104 */       mimeMultipart.addBodyPart((BodyPart)partTwo);
/*     */     } 
/* 106 */     return (Multipart)mimeMultipart;
/*     */   }
/*     */   
/*     */   protected Session createSession() {
/* 110 */     return Session.getInstance(this.properties, null);
/*     */   }
/*     */   
/*     */   protected Transport createConnectedTransport(Session session) throws MessagingException {
/* 114 */     Transport transport = session.getTransport("smtp");
/* 115 */     if (this.user != null && this.password != null) {
/* 116 */       transport.connect(this.user, this.password);
/*     */     } else {
/* 118 */       transport.connect();
/* 119 */     }  return transport;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamles\\util\mail\EmailSender.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */