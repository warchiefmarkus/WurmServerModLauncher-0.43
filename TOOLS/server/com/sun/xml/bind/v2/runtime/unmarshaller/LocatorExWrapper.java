/*    */ package com.sun.xml.bind.v2.runtime.unmarshaller;
/*    */ 
/*    */ import javax.xml.bind.ValidationEventLocator;
/*    */ import javax.xml.bind.helpers.ValidationEventLocatorImpl;
/*    */ import org.xml.sax.Locator;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class LocatorExWrapper
/*    */   implements LocatorEx
/*    */ {
/*    */   private final Locator locator;
/*    */   
/*    */   public LocatorExWrapper(Locator locator) {
/* 53 */     this.locator = locator;
/*    */   }
/*    */   
/*    */   public ValidationEventLocator getLocation() {
/* 57 */     return (ValidationEventLocator)new ValidationEventLocatorImpl(this.locator);
/*    */   }
/*    */   
/*    */   public String getPublicId() {
/* 61 */     return this.locator.getPublicId();
/*    */   }
/*    */   
/*    */   public String getSystemId() {
/* 65 */     return this.locator.getSystemId();
/*    */   }
/*    */   
/*    */   public int getLineNumber() {
/* 69 */     return this.locator.getLineNumber();
/*    */   }
/*    */   
/*    */   public int getColumnNumber() {
/* 73 */     return this.locator.getColumnNumber();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtim\\unmarshaller\LocatorExWrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */