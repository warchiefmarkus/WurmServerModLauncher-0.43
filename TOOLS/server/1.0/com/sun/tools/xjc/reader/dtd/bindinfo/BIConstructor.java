/*    */ package 1.0.com.sun.tools.xjc.reader.dtd.bindinfo;
/*    */ 
/*    */ import com.sun.tools.xjc.grammar.ClassItem;
/*    */ import com.sun.tools.xjc.reader.annotator.AnnotatorController;
/*    */ import com.sun.tools.xjc.reader.dtd.bindinfo.DOM4JLocator;
/*    */ import com.sun.xml.bind.JAXBAssertionError;
/*    */ import java.util.StringTokenizer;
/*    */ import java.util.Vector;
/*    */ import org.dom4j.Element;
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
/*    */ public class BIConstructor
/*    */ {
/*    */   private final Element dom;
/*    */   private final String[] properties;
/*    */   
/*    */   BIConstructor(Element _node) {
/* 46 */     this.dom = _node;
/*    */     
/* 48 */     StringTokenizer tokens = new StringTokenizer(_node.attributeValue("properties"));
/*    */ 
/*    */     
/* 51 */     Vector vec = new Vector();
/* 52 */     while (tokens.hasMoreTokens())
/* 53 */       vec.add(tokens.nextToken()); 
/* 54 */     this.properties = vec.<String>toArray(new String[0]);
/*    */     
/* 56 */     if (this.properties.length == 0) {
/* 57 */       throw new JAXBAssertionError("this error should be catched by the validator");
/*    */     }
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void createDeclaration(ClassItem cls, AnnotatorController controller) {
/* 76 */     cls.addConstructor(this.properties);
/*    */   }
/*    */ 
/*    */   
/*    */   public Locator getSourceLocation() {
/* 81 */     return DOM4JLocator.getLocationInfo(this.dom);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\dtd\bindinfo\BIConstructor.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */