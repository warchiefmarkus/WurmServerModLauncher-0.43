/*    */ package com.sun.xml.bind;
/*    */ 
/*    */ import javax.xml.bind.annotation.adapters.XmlAdapter;
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
/*    */ 
/*    */ public final class AnyTypeAdapter
/*    */   extends XmlAdapter<Object, Object>
/*    */ {
/*    */   public Object unmarshal(Object v) {
/* 55 */     return v;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object marshal(Object v) {
/* 62 */     return v;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\AnyTypeAdapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */