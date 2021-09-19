/*    */ package 1.0.com.sun.tools.xjc.reader.dtd.bindinfo;
/*    */ 
/*    */ import org.dom4j.Element;
/*    */ import org.dom4j.QName;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class DOM4JLocator
/*    */ {
/*    */   private static final String locationNamespace = "http://www.sun.com/xmlns/jaxb/dom4j-location";
/*    */   private static final String systemId = "systemid";
/*    */   private static final String column = "column";
/*    */   private static final String line = "line";
/*    */   
/*    */   public static void setLocationInfo(Element e, Locator loc) {
/* 22 */     e.addAttribute(QName.get("systemid", "http://www.sun.com/xmlns/jaxb/dom4j-location"), loc.getSystemId());
/*    */ 
/*    */     
/* 25 */     e.addAttribute(QName.get("column", "http://www.sun.com/xmlns/jaxb/dom4j-location"), Integer.toString(loc.getLineNumber()));
/*    */ 
/*    */     
/* 28 */     e.addAttribute(QName.get("line", "http://www.sun.com/xmlns/jaxb/dom4j-location"), Integer.toString(loc.getColumnNumber()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Locator getLocationInfo(Element e) {
/* 41 */     if (e.attribute(QName.get("systemid", "http://www.sun.com/xmlns/jaxb/dom4j-location")) == null) {
/* 42 */       return null;
/*    */     }
/* 44 */     return (Locator)new Object(e);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\dtd\bindinfo\DOM4JLocator.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */