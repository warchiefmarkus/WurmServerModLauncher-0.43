/*    */ package 1.0.com.sun.tools.xjc.reader.xmlschema.bindinfo;
/*    */ 
/*    */ import com.sun.tools.xjc.grammar.id.IDREFTransducer;
/*    */ import com.sun.tools.xjc.grammar.id.SymbolSpace;
/*    */ import com.sun.tools.xjc.grammar.xducer.Transducer;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BIXIdSymbolSpace
/*    */   extends AbstractDeclarationImpl
/*    */ {
/*    */   private final String name;
/*    */   
/*    */   public BIXIdSymbolSpace(Locator _loc, String _name) {
/* 29 */     super(_loc);
/* 30 */     this.name = _name;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Transducer makeTransducer(Transducer core) {
/* 38 */     markAsAcknowledged();
/* 39 */     SymbolSpace ss = (getBuilder()).grammar.getSymbolSpace(this.name);
/*    */     
/* 41 */     if (core.isID()) {
/* 42 */       return (Transducer)new Object(this, core, ss);
/*    */     }
/*    */ 
/*    */     
/* 46 */     return (Transducer)new IDREFTransducer((getBuilder()).grammar.codeModel, ss, true);
/*    */   }
/*    */   public QName getName() {
/* 49 */     return NAME;
/*    */   }
/*    */   
/* 52 */   public static final QName NAME = new QName("http://java.sun.com/xml/ns/jaxb/xjc", "idSymbolSpace");
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\bindinfo\BIXIdSymbolSpace.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */