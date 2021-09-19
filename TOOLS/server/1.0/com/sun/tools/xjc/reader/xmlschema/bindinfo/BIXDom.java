/*    */ package 1.0.com.sun.tools.xjc.reader.xmlschema.bindinfo;
/*    */ 
/*    */ import com.sun.msv.grammar.Expression;
/*    */ import com.sun.msv.grammar.NameClass;
/*    */ import com.sun.tools.xjc.grammar.AnnotatedGrammar;
/*    */ import com.sun.tools.xjc.grammar.ext.DOMItemFactory;
/*    */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.AbstractDeclarationImpl;
/*    */ import javax.xml.namespace.QName;
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
/*    */ public class BIXDom
/*    */   extends AbstractDeclarationImpl
/*    */ {
/*    */   private final DOMItemFactory factory;
/*    */   
/*    */   public BIXDom(DOMItemFactory _factory, Locator _loc) {
/* 27 */     super(_loc);
/* 28 */     this.factory = _factory;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Expression create(NameClass nc, AnnotatedGrammar grammar, Locator loc) {
/* 35 */     markAsAcknowledged();
/* 36 */     return (Expression)this.factory.create(nc, grammar, loc);
/*    */   }
/*    */   public final QName getName() {
/* 39 */     return NAME;
/*    */   }
/*    */   
/* 42 */   public static final QName NAME = new QName("http://java.sun.com/xml/ns/jaxb/xjc", "dom");
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\bindinfo\BIXDom.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */