/*    */ package 1.0.com.sun.tools.xjc.reader.dtd.bindinfo;
/*    */ 
/*    */ import org.dom4j.DocumentFactory;
/*    */ import org.dom4j.ElementHandler;
/*    */ import org.dom4j.io.SAXContentHandler;
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
/*    */ class SAXContentHandlerEx
/*    */   extends SAXContentHandler
/*    */ {
/*    */   private final Locator[] loc;
/*    */   
/*    */   public static com.sun.tools.xjc.reader.dtd.bindinfo.SAXContentHandlerEx create() {
/* 29 */     return new com.sun.tools.xjc.reader.dtd.bindinfo.SAXContentHandlerEx(new Locator[1]);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private SAXContentHandlerEx(Locator[] loc) {
/* 35 */     super(DocumentFactory.getInstance(), (ElementHandler)new MyElementHandler(loc));
/* 36 */     this.loc = loc;
/*    */   }
/*    */   public void setDocumentLocator(Locator _loc) {
/* 39 */     this.loc[0] = _loc;
/* 40 */     super.setDocumentLocator(_loc);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\dtd\bindinfo\SAXContentHandlerEx.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */