/*    */ package 1.0.com.sun.tools.xjc.grammar.ext;
/*    */ 
/*    */ import com.sun.msv.grammar.NameClass;
/*    */ import com.sun.tools.xjc.grammar.AnnotatedGrammar;
/*    */ import com.sun.tools.xjc.grammar.ExternalItem;
/*    */ import com.sun.tools.xjc.grammar.ext.Dom4jItem;
/*    */ import com.sun.tools.xjc.grammar.ext.W3CDOMItem;
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
/*    */ public abstract class DOMItemFactory
/*    */ {
/*    */   public abstract ExternalItem create(NameClass paramNameClass, AnnotatedGrammar paramAnnotatedGrammar, Locator paramLocator);
/*    */   
/*    */   public static com.sun.tools.xjc.grammar.ext.DOMItemFactory getInstance(String type) throws UndefinedNameException {
/* 29 */     type = type.toUpperCase();
/*    */     
/* 31 */     if (type.equals("W3C"))
/* 32 */       return W3CDOMItem.factory; 
/* 33 */     if (type.equals("DOM4J")) {
/* 34 */       return Dom4jItem.factory;
/*    */     }
/* 36 */     throw new UndefinedNameException(type);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\grammar\ext\DOMItemFactory.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */