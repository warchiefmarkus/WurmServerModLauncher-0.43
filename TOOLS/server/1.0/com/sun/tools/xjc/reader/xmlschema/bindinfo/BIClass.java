/*    */ package 1.0.com.sun.tools.xjc.reader.xmlschema.bindinfo;
/*    */ 
/*    */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.AbstractDeclarationImpl;
/*    */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIGlobalBinding;
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
/*    */ public final class BIClass
/*    */   extends AbstractDeclarationImpl
/*    */ {
/*    */   private final String className;
/*    */   private final String userSpecifiedImplClass;
/*    */   private final String javadoc;
/*    */   
/*    */   public BIClass(Locator loc, String _className, String _implClass, String _javadoc) {
/* 28 */     super(loc);
/* 29 */     this.className = _className;
/* 30 */     this.javadoc = _javadoc;
/* 31 */     this.userSpecifiedImplClass = _implClass;
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
/*    */   public String getClassName() {
/* 45 */     if (this.className == null) return null;
/*    */     
/* 47 */     BIGlobalBinding gb = getBuilder().getGlobalBinding();
/*    */     
/* 49 */     if (gb.isJavaNamingConventionEnabled()) {
/* 50 */       return gb.getNameConverter().toClassName(this.className);
/*    */     }
/*    */     
/* 53 */     return this.className;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getUserSpecifiedImplClass() {
/* 64 */     return this.userSpecifiedImplClass;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getJavadoc() {
/* 73 */     return this.javadoc;
/*    */   } public QName getName() {
/* 75 */     return NAME;
/*    */   }
/*    */   
/* 78 */   public static final QName NAME = new QName("http://java.sun.com/xml/ns/jaxb", "class");
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\bindinfo\BIClass.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */