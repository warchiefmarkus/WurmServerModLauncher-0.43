/*    */ package org.seamless.util.mail;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Email
/*    */ {
/*    */   protected String sender;
/*    */   protected String recipient;
/*    */   protected String subject;
/*    */   protected String plaintext;
/*    */   protected String html;
/*    */   
/*    */   public Email(String sender, String recipient, String subject, String plaintext) {
/* 29 */     this(sender, recipient, subject, plaintext, null);
/*    */   }
/*    */   
/*    */   public Email(String sender, String recipient, String subject, String plaintext, String html) {
/* 33 */     this.sender = sender;
/* 34 */     this.recipient = recipient;
/* 35 */     this.subject = subject;
/* 36 */     this.plaintext = plaintext;
/* 37 */     this.html = html;
/*    */   }
/*    */   
/*    */   public String getSender() {
/* 41 */     return this.sender;
/*    */   }
/*    */   
/*    */   public void setSender(String sender) {
/* 45 */     this.sender = sender;
/*    */   }
/*    */   
/*    */   public String getRecipient() {
/* 49 */     return this.recipient;
/*    */   }
/*    */   
/*    */   public void setRecipient(String recipient) {
/* 53 */     this.recipient = recipient;
/*    */   }
/*    */   
/*    */   public String getSubject() {
/* 57 */     return this.subject;
/*    */   }
/*    */   
/*    */   public void setSubject(String subject) {
/* 61 */     this.subject = subject;
/*    */   }
/*    */   
/*    */   public String getPlaintext() {
/* 65 */     return this.plaintext;
/*    */   }
/*    */   
/*    */   public void setPlaintext(String plaintext) {
/* 69 */     this.plaintext = plaintext;
/*    */   }
/*    */   
/*    */   public String getHtml() {
/* 73 */     return this.html;
/*    */   }
/*    */   
/*    */   public void setHtml(String html) {
/* 77 */     this.html = html;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamles\\util\mail\Email.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */