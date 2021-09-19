/*    */ package 1.0.com.sun.tools.xjc.reader.dtd.bindinfo;
/*    */ 
/*    */ import com.sun.tools.xjc.reader.dtd.bindinfo.DOM4JLocator;
/*    */ import com.sun.xml.bind.JAXBAssertionError;
/*    */ import java.util.StringTokenizer;
/*    */ import org.dom4j.Element;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class BIInterface
/*    */ {
/*    */   private final Element dom;
/*    */   private final String name;
/*    */   private final String[] members;
/*    */   private final String[] fields;
/*    */   
/*    */   BIInterface(Element e) {
/* 20 */     this.dom = e;
/* 21 */     this.name = e.attributeValue("name");
/* 22 */     this.members = parseTokens(e.attributeValue("members"));
/*    */     
/* 24 */     if (e.attribute("properties") != null) {
/* 25 */       this.fields = parseTokens(e.attributeValue("properties"));
/* 26 */       throw new JAXBAssertionError("//interface/@properties is not supported");
/*    */     } 
/* 28 */     this.fields = new String[0];
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
/*    */   public String name() {
/* 41 */     return this.name;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String[] members() {
/* 50 */     return this.members;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String[] fields() {
/* 56 */     return this.fields;
/*    */   }
/*    */ 
/*    */   
/*    */   public Locator getSourceLocation() {
/* 61 */     return DOM4JLocator.getLocationInfo(this.dom);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static String[] parseTokens(String value) {
/* 68 */     StringTokenizer tokens = new StringTokenizer(value);
/*    */     
/* 70 */     String[] r = new String[tokens.countTokens()];
/* 71 */     int i = 0;
/* 72 */     while (tokens.hasMoreTokens()) {
/* 73 */       r[i++] = tokens.nextToken();
/*    */     }
/* 75 */     return r;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\dtd\bindinfo\BIInterface.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */