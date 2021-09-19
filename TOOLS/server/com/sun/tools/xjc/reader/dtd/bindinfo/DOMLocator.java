/*    */ package com.sun.tools.xjc.reader.dtd.bindinfo;
/*    */ 
/*    */ import org.w3c.dom.Element;
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
/*    */ class DOMLocator
/*    */ {
/*    */   private static final String locationNamespace = "http://www.sun.com/xmlns/jaxb/dom-location";
/*    */   private static final String systemId = "systemid";
/*    */   private static final String column = "column";
/*    */   private static final String line = "line";
/*    */   
/*    */   public static void setLocationInfo(Element e, Locator loc) {
/* 51 */     e.setAttributeNS("http://www.sun.com/xmlns/jaxb/dom-location", "loc:systemid", loc.getSystemId());
/* 52 */     e.setAttributeNS("http://www.sun.com/xmlns/jaxb/dom-location", "loc:column", Integer.toString(loc.getLineNumber()));
/* 53 */     e.setAttributeNS("http://www.sun.com/xmlns/jaxb/dom-location", "loc:line", Integer.toString(loc.getColumnNumber()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Locator getLocationInfo(final Element e) {
/* 64 */     if (DOMUtil.getAttribute(e, "http://www.sun.com/xmlns/jaxb/dom-location", "systemid") == null) {
/* 65 */       return null;
/*    */     }
/* 67 */     return new Locator() {
/*    */         public int getLineNumber() {
/* 69 */           return Integer.parseInt(DOMUtil.getAttribute(e, "http://www.sun.com/xmlns/jaxb/dom-location", "line"));
/*    */         }
/*    */         public int getColumnNumber() {
/* 72 */           return Integer.parseInt(DOMUtil.getAttribute(e, "http://www.sun.com/xmlns/jaxb/dom-location", "column"));
/*    */         }
/*    */         public String getSystemId() {
/* 75 */           return DOMUtil.getAttribute(e, "http://www.sun.com/xmlns/jaxb/dom-location", "systemid");
/*    */         }
/*    */         public String getPublicId() {
/* 78 */           return null;
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\dtd\bindinfo\DOMLocator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */