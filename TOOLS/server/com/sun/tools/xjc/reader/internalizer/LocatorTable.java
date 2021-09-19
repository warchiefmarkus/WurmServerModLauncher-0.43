/*    */ package com.sun.tools.xjc.reader.internalizer;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.w3c.dom.Element;
/*    */ import org.xml.sax.Locator;
/*    */ import org.xml.sax.helpers.LocatorImpl;
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
/*    */ public final class LocatorTable
/*    */ {
/* 53 */   private final Map startLocations = new HashMap<Object, Object>();
/*    */ 
/*    */   
/* 56 */   private final Map endLocations = new HashMap<Object, Object>();
/*    */   
/*    */   public void storeStartLocation(Element e, Locator loc) {
/* 59 */     this.startLocations.put(e, new LocatorImpl(loc));
/*    */   }
/*    */   
/*    */   public void storeEndLocation(Element e, Locator loc) {
/* 63 */     this.endLocations.put(e, new LocatorImpl(loc));
/*    */   }
/*    */   
/*    */   public Locator getStartLocation(Element e) {
/* 67 */     return (Locator)this.startLocations.get(e);
/*    */   }
/*    */   
/*    */   public Locator getEndLocation(Element e) {
/* 71 */     return (Locator)this.endLocations.get(e);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\internalizer\LocatorTable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */