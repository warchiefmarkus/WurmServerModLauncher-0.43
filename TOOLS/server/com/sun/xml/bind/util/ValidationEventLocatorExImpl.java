/*    */ package com.sun.xml.bind.util;
/*    */ 
/*    */ import com.sun.xml.bind.ValidationEventLocatorEx;
/*    */ import javax.xml.bind.helpers.ValidationEventLocatorImpl;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ValidationEventLocatorExImpl
/*    */   extends ValidationEventLocatorImpl
/*    */   implements ValidationEventLocatorEx
/*    */ {
/*    */   private final String fieldName;
/*    */   
/*    */   public ValidationEventLocatorExImpl(Object target, String fieldName) {
/* 58 */     super(target);
/* 59 */     this.fieldName = fieldName;
/*    */   }
/*    */   
/*    */   public String getFieldName() {
/* 63 */     return this.fieldName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 70 */     StringBuffer buf = new StringBuffer();
/* 71 */     buf.append("[url=");
/* 72 */     buf.append(getURL());
/* 73 */     buf.append(",line=");
/* 74 */     buf.append(getLineNumber());
/* 75 */     buf.append(",column=");
/* 76 */     buf.append(getColumnNumber());
/* 77 */     buf.append(",node=");
/* 78 */     buf.append(getNode());
/* 79 */     buf.append(",object=");
/* 80 */     buf.append(getObject());
/* 81 */     buf.append(",field=");
/* 82 */     buf.append(getFieldName());
/* 83 */     buf.append("]");
/*    */     
/* 85 */     return buf.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bin\\util\ValidationEventLocatorExImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */