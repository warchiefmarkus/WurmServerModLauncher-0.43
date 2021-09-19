/*    */ package com.sun.xml.txw2.output;
/*    */ 
/*    */ import javax.xml.transform.Result;
/*    */ import javax.xml.transform.dom.DOMResult;
/*    */ import javax.xml.transform.sax.SAXResult;
/*    */ import javax.xml.transform.stream.StreamResult;
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
/*    */ public abstract class ResultFactory
/*    */ {
/*    */   public static XmlSerializer createSerializer(Result result) {
/* 50 */     if (result instanceof SAXResult)
/* 51 */       return new SaxSerializer((SAXResult)result); 
/* 52 */     if (result instanceof DOMResult)
/* 53 */       return new DomSerializer((DOMResult)result); 
/* 54 */     if (result instanceof StreamResult) {
/* 55 */       return new StreamSerializer((StreamResult)result);
/*    */     }
/* 57 */     throw new UnsupportedOperationException("Unsupported Result type: " + result.getClass().getName());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\txw2\output\ResultFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */