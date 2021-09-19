/*     */ package com.wurmonline.server;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileReader;
/*     */ import java.net.InetAddress;
/*     */ import java.util.Properties;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.mail.Address;
/*     */ import javax.mail.Authenticator;
/*     */ import javax.mail.Message;
/*     */ import javax.mail.MessagingException;
/*     */ import javax.mail.PasswordAuthentication;
/*     */ import javax.mail.Session;
/*     */ import javax.mail.Transport;
/*     */ import javax.mail.internet.AddressException;
/*     */ import javax.mail.internet.InternetAddress;
/*     */ import javax.mail.internet.MimeMessage;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Mailer
/*     */ {
/*     */   private static final String pwfileName = "passwordmail.html";
/*     */   private static final String regmailfileName1 = "registrationphase1.html";
/*     */   private static final String regmailfileName2 = "registrationphase2.html";
/*     */   private static final String premexpiryw = "premiumexpirywarning.html";
/*     */   private static final String accountdelw = "accountdeletionwarning.html";
/*     */   private static final String accountdels = "accountdeletionsilvers.html";
/*  49 */   private static String phaseOneMail = loadConfirmationMail1();
/*     */ 
/*     */ 
/*     */   
/*  53 */   private static String phaseTwoMail = loadConfirmationMail2();
/*     */ 
/*     */ 
/*     */   
/*  57 */   private static String passwordMail = loadPasswordMail();
/*     */   
/*  59 */   private static String accountDelMail = loadAccountDelMail();
/*     */   
/*  61 */   private static String accountDelPreventionMail = loadAccountDelPreventionMail();
/*  62 */   private static String premExpiryMail = loadPremExpiryMail();
/*     */   
/*  64 */   private static final Logger logger = Logger.getLogger(Mailer.class.getName());
/*  65 */   public static String smtpserver = "localhost";
/*  66 */   private static String smtpuser = "";
/*  67 */   private static String smtppw = "";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String amaserver = "";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sendMail(final String sender, final String receiver, final String subject, final String text) throws AddressException, MessagingException {
/*  80 */     (new Thread()
/*     */       {
/*     */ 
/*     */         
/*     */         public void run()
/*     */         {
/*     */           try {
/*  87 */             Properties props = new Properties();
/*  88 */             props.setProperty("mail.transport.protocol", "smtp");
/*     */             
/*  90 */             Mailer.SMTPAuthenticator pwa = null;
/*  91 */             if (Servers.localServer.LOGINSERVER) {
/*     */               
/*  93 */               props.put("mail.host", "");
/*  94 */               props.put("mail.smtp.auth", "true");
/*  95 */               pwa = new Mailer.SMTPAuthenticator();
/*     */             } else {
/*     */               
/*  98 */               props.put("mail.host", Mailer.smtpserver);
/*  99 */             }  props.put("mail.user", sender);
/*     */             
/* 101 */             if (Servers.localServer.LOGINSERVER) {
/* 102 */               props.put("mail.smtp.host", "");
/*     */             } else {
/* 104 */               props.put("mail.smtp.host", Mailer.smtpserver);
/* 105 */             }  props.put("mail.smtp.port", "25");
/*     */             
/* 107 */             Session session = Session.getDefaultInstance(props, pwa);
/* 108 */             Properties properties = session.getProperties();
/* 109 */             String key = "mail.smtp.localhost";
/* 110 */             String prop = properties.getProperty("mail.smtp.localhost");
/* 111 */             if (prop == null) {
/*     */               
/* 113 */               prop = Mailer.getLocalHost(session);
/* 114 */               properties.put("mail.smtp.localhost", prop);
/*     */             } 
/*     */ 
/*     */ 
/*     */             
/* 119 */             MimeMessage msg = new MimeMessage(session);
/* 120 */             msg.setContent(text, "text/html");
/* 121 */             msg.setSubject(subject);
/* 122 */             msg.setFrom((Address)new InternetAddress(sender));
/* 123 */             msg.addRecipient(MimeMessage.RecipientType.TO, (Address)new InternetAddress(receiver));
/*     */             
/* 125 */             msg.saveChanges();
/*     */             
/* 127 */             Transport transport = session.getTransport("smtp");
/* 128 */             transport.connect();
/* 129 */             transport.sendMessage((Message)msg, msg.getAllRecipients());
/* 130 */             transport.close();
/*     */           }
/* 132 */           catch (Exception ex) {
/*     */             
/* 134 */             Mailer.logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */           } 
/*     */         }
/* 137 */       }).start();
/*     */   }
/*     */   
/*     */   private static final class SMTPAuthenticator
/*     */     extends Authenticator {
/*     */     private SMTPAuthenticator() {}
/*     */     
/*     */     public PasswordAuthentication getPasswordAuthentication() {
/* 145 */       String username = Mailer.smtpuser;
/* 146 */       String password = Mailer.smtppw;
/* 147 */       return new PasswordAuthentication(username, password);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String getLocalHost(Session session) {
/* 157 */     String localHostName = null;
/* 158 */     String name = "smtp";
/*     */ 
/*     */     
/*     */     try {
/* 162 */       if (localHostName == null || localHostName.length() <= 0)
/* 163 */         localHostName = InetAddress.getLocalHost().getHostName(); 
/* 164 */       if (localHostName == null || localHostName.length() <= 0) {
/* 165 */         localHostName = session.getProperty("mail.smtp.localhost");
/*     */       }
/* 167 */     } catch (Exception uhex) {
/*     */       
/* 169 */       return "localhost";
/*     */     } 
/* 171 */     return localHostName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String getPhaseOneMail() {
/* 180 */     if (phaseOneMail == null)
/* 181 */       phaseOneMail = loadConfirmationMail1(); 
/* 182 */     return phaseOneMail;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String getPhaseTwoMail() {
/* 191 */     if (phaseTwoMail == null)
/* 192 */       phaseTwoMail = loadConfirmationMail2(); 
/* 193 */     return phaseTwoMail;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String getPasswordMail() {
/* 202 */     if (passwordMail == null)
/* 203 */       passwordMail = loadPasswordMail(); 
/* 204 */     return passwordMail;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String getAccountDelPreventionMail() {
/* 213 */     if (accountDelPreventionMail == null)
/* 214 */       accountDelPreventionMail = loadAccountDelPreventionMail(); 
/* 215 */     return accountDelPreventionMail;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String getAccountDelMail() {
/* 224 */     if (accountDelMail == null)
/* 225 */       accountDelMail = loadAccountDelMail(); 
/* 226 */     return accountDelMail;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String getPremExpiryMail() {
/* 235 */     if (premExpiryMail == null)
/* 236 */       premExpiryMail = loadPremExpiryMail(); 
/* 237 */     return premExpiryMail;
/*     */   }
/*     */ 
/*     */   
/*     */   private static final String loadConfirmationMail1() {
/* 242 */     try (BufferedReader in = new BufferedReader(new FileReader("registrationphase1.html"))) {
/*     */       
/* 244 */       StringBuilder buf = new StringBuilder();
/*     */       String str;
/* 246 */       while ((str = in.readLine()) != null)
/*     */       {
/* 248 */         buf.append(str);
/*     */       }
/* 250 */       in.close();
/* 251 */       return buf.toString();
/*     */     }
/* 253 */     catch (Exception exception) {
/*     */ 
/*     */ 
/*     */       
/* 257 */       return "";
/*     */     } 
/*     */   }
/*     */   
/*     */   private static final String loadConfirmationMail2() {
/* 262 */     try (BufferedReader in = new BufferedReader(new FileReader("registrationphase2.html"))) {
/*     */       
/* 264 */       StringBuilder buf = new StringBuilder();
/*     */       String str;
/* 266 */       while ((str = in.readLine()) != null)
/*     */       {
/* 268 */         buf.append(str);
/*     */       }
/* 270 */       in.close();
/* 271 */       return buf.toString();
/*     */     }
/* 273 */     catch (Exception exception) {
/*     */ 
/*     */ 
/*     */       
/* 277 */       return "";
/*     */     } 
/*     */   }
/*     */   
/*     */   private static final String loadPasswordMail() {
/* 282 */     try (BufferedReader in = new BufferedReader(new FileReader("passwordmail.html"))) {
/*     */       
/* 284 */       StringBuilder buf = new StringBuilder();
/*     */       String str;
/* 286 */       while ((str = in.readLine()) != null)
/*     */       {
/* 288 */         buf.append(str);
/*     */       }
/* 290 */       in.close();
/* 291 */       return buf.toString();
/*     */     }
/* 293 */     catch (Exception exception) {
/*     */ 
/*     */ 
/*     */       
/* 297 */       return "";
/*     */     } 
/*     */   }
/*     */   
/*     */   private static final String loadAccountDelMail() {
/* 302 */     try (BufferedReader in = new BufferedReader(new FileReader("accountdeletionwarning.html"))) {
/*     */       
/* 304 */       StringBuilder buf = new StringBuilder();
/*     */       String str;
/* 306 */       while ((str = in.readLine()) != null)
/*     */       {
/* 308 */         buf.append(str);
/*     */       }
/* 310 */       in.close();
/* 311 */       return buf.toString();
/*     */     }
/* 313 */     catch (Exception exception) {
/*     */ 
/*     */ 
/*     */       
/* 317 */       return "";
/*     */     } 
/*     */   }
/*     */   
/*     */   private static final String loadAccountDelPreventionMail() {
/* 322 */     try (BufferedReader in = new BufferedReader(new FileReader("accountdeletionsilvers.html"))) {
/*     */       
/* 324 */       StringBuilder buf = new StringBuilder();
/*     */       String str;
/* 326 */       while ((str = in.readLine()) != null)
/*     */       {
/* 328 */         buf.append(str);
/*     */       }
/* 330 */       in.close();
/* 331 */       return buf.toString();
/*     */     }
/* 333 */     catch (Exception exception) {
/*     */ 
/*     */ 
/*     */       
/* 337 */       return "";
/*     */     } 
/*     */   }
/*     */   
/*     */   private static final String loadPremExpiryMail() {
/* 342 */     try (BufferedReader in = new BufferedReader(new FileReader("premiumexpirywarning.html"))) {
/*     */       
/* 344 */       StringBuilder buf = new StringBuilder();
/*     */       String str;
/* 346 */       while ((str = in.readLine()) != null)
/*     */       {
/* 348 */         buf.append(str);
/*     */       }
/* 350 */       in.close();
/* 351 */       return buf.toString();
/*     */     }
/* 353 */     catch (Exception exception) {
/*     */ 
/*     */ 
/*     */       
/* 357 */       return "";
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {}
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\Mailer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */