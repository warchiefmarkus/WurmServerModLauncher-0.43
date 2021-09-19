/*    */ package 1.0.com.sun.tools.xjc.reader.xmlschema.bindinfo;
/*    */ 
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
/*    */ 
/*    */ 
/*    */ public class BIConversion
/*    */   extends AbstractDeclarationImpl
/*    */ {
/*    */   private final Transducer transducer;
/*    */   
/*    */   public BIConversion(Locator loc, Transducer transducer) {
/* 29 */     super(loc);
/* 30 */     this.transducer = transducer;
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
/*    */   public Transducer getTransducer() {
/* 43 */     return this.transducer;
/*    */   }
/*    */   public final QName getName() {
/* 46 */     return NAME;
/*    */   }
/*    */   
/* 49 */   public static final QName NAME = new QName("http://java.sun.com/xml/ns/jaxb", "conversion");
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\bindinfo\BIConversion.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */