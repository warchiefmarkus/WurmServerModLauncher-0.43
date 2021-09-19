/*    */ package com.sun.xml.bind.v2.runtime.unmarshaller;
/*    */ 
/*    */ import com.sun.xml.bind.util.AttributesImpl;
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
/*    */ public final class AttributesExImpl
/*    */   extends AttributesImpl
/*    */   implements AttributesEx
/*    */ {
/*    */   public CharSequence getData(int idx) {
/* 50 */     return getValue(idx);
/*    */   }
/*    */   
/*    */   public CharSequence getData(String nsUri, String localName) {
/* 54 */     return getValue(nsUri, localName);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtim\\unmarshaller\AttributesExImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */