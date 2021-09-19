/*    */ package org.seamless.xml;
/*    */ 
/*    */ import org.xml.sax.SAXParseException;
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
/*    */ public class ParserException
/*    */   extends Exception
/*    */ {
/*    */   public ParserException() {}
/*    */   
/*    */   public ParserException(String s) {
/* 30 */     super(s);
/*    */   }
/*    */   
/*    */   public ParserException(String s, Throwable throwable) {
/* 34 */     super(s, throwable);
/*    */   }
/*    */   
/*    */   public ParserException(Throwable throwable) {
/* 38 */     super(throwable);
/*    */   }
/*    */   
/*    */   public ParserException(SAXParseException ex) {
/* 42 */     super("(Line/Column: " + ex.getLineNumber() + ":" + ex.getColumnNumber() + ") " + ex.getMessage());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamless\xml\ParserException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */