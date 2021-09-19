/*    */ package com.sun.istack;
/*    */ 
/*    */ import javax.xml.stream.Location;
/*    */ import javax.xml.stream.XMLStreamException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class XMLStreamException2
/*    */   extends XMLStreamException
/*    */ {
/*    */   public XMLStreamException2(String msg) {
/* 13 */     super(msg);
/*    */   }
/*    */   
/*    */   public XMLStreamException2(Throwable th) {
/* 17 */     super(th);
/*    */   }
/*    */   
/*    */   public XMLStreamException2(String msg, Throwable th) {
/* 21 */     super(msg, th);
/*    */   }
/*    */   
/*    */   public XMLStreamException2(String msg, Location location) {
/* 25 */     super(msg, location);
/*    */   }
/*    */   
/*    */   public XMLStreamException2(String msg, Location location, Throwable th) {
/* 29 */     super(msg, location, th);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Throwable getCause() {
/* 36 */     return getNestedException();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\istack\XMLStreamException2.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */