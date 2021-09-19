/*    */ package com.sun.tools.xjc.util;
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
/*    */ public abstract class ReadOnlyAdapter<OnTheWire, InMemory>
/*    */   extends XmlAdapter<OnTheWire, InMemory>
/*    */ {
/*    */   public final OnTheWire marshal(InMemory onTheWire) {
/* 51 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xj\\util\ReadOnlyAdapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */