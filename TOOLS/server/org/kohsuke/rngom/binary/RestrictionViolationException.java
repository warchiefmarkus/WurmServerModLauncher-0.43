/*    */ package org.kohsuke.rngom.binary;
/*    */ 
/*    */ import javax.xml.namespace.QName;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ final class RestrictionViolationException
/*    */   extends Exception {
/*    */   private String messageId;
/*    */   private Locator loc;
/*    */   private QName name;
/*    */   
/*    */   RestrictionViolationException(String messageId) {
/* 13 */     this.messageId = messageId;
/*    */   }
/*    */   
/*    */   RestrictionViolationException(String messageId, QName name) {
/* 17 */     this.messageId = messageId;
/* 18 */     this.name = name;
/*    */   }
/*    */   
/*    */   String getMessageId() {
/* 22 */     return this.messageId;
/*    */   }
/*    */   
/*    */   Locator getLocator() {
/* 26 */     return this.loc;
/*    */   }
/*    */   
/*    */   void maybeSetLocator(Locator loc) {
/* 30 */     if (this.loc == null)
/* 31 */       this.loc = loc; 
/*    */   }
/*    */   
/*    */   QName getName() {
/* 35 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\binary\RestrictionViolationException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */