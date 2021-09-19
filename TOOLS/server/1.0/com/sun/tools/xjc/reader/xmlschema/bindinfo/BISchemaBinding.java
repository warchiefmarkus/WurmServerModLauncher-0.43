/*     */ package 1.0.com.sun.tools.xjc.reader.xmlschema.bindinfo;
/*     */ 
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.AbstractDeclarationImpl;
/*     */ import com.sun.xml.xsom.XSComponent;
/*     */ import javax.xml.namespace.QName;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class BISchemaBinding
/*     */   extends AbstractDeclarationImpl
/*     */ {
/*     */   private final NamingRule typeNamingRule;
/*     */   private final NamingRule elementNamingRule;
/*     */   private final NamingRule attributeNamingRule;
/*     */   private final NamingRule modelGroupNamingRule;
/*     */   private final NamingRule anonymousTypeNamingRule;
/*     */   private String packageName;
/*     */   private final String javadoc;
/*  40 */   private static final NamingRule defaultNamingRule = new NamingRule("", "");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BISchemaBinding(String _packageName, String _javadoc, NamingRule rType, NamingRule rElement, NamingRule rAttribute, NamingRule rModelGroup, NamingRule rAnonymousType, Locator _loc) {
/*  69 */     super(_loc);
/*  70 */     this.packageName = _packageName;
/*  71 */     this.javadoc = _javadoc;
/*     */     
/*  73 */     if (rType == null) rType = defaultNamingRule; 
/*  74 */     if (rElement == null) rElement = defaultNamingRule; 
/*  75 */     if (rAttribute == null) rAttribute = defaultNamingRule; 
/*  76 */     if (rModelGroup == null) rModelGroup = defaultNamingRule; 
/*  77 */     if (rAnonymousType == null) rAnonymousType = new NamingRule("", "Type");
/*     */     
/*  79 */     this.typeNamingRule = rType;
/*  80 */     this.elementNamingRule = rElement;
/*  81 */     this.attributeNamingRule = rAttribute;
/*  82 */     this.modelGroupNamingRule = rModelGroup;
/*  83 */     this.anonymousTypeNamingRule = rAnonymousType;
/*     */ 
/*     */     
/*  86 */     markAsAcknowledged();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String mangleClassName(String name, XSComponent cmp) {
/* 101 */     if (cmp instanceof com.sun.xml.xsom.XSType)
/* 102 */       return this.typeNamingRule.mangle(name); 
/* 103 */     if (cmp instanceof com.sun.xml.xsom.XSElementDecl)
/* 104 */       return this.elementNamingRule.mangle(name); 
/* 105 */     if (cmp instanceof com.sun.xml.xsom.XSAttributeDecl)
/* 106 */       return this.attributeNamingRule.mangle(name); 
/* 107 */     if (cmp instanceof com.sun.xml.xsom.XSModelGroup || cmp instanceof com.sun.xml.xsom.XSModelGroupDecl) {
/* 108 */       return this.modelGroupNamingRule.mangle(name);
/*     */     }
/*     */     
/* 111 */     return name;
/*     */   }
/*     */   
/*     */   public String mangleAnonymousTypeClassName(String name) {
/* 115 */     return this.anonymousTypeNamingRule.mangle(name);
/*     */   }
/*     */   
/*     */   public void setPackageName(String val) {
/* 119 */     this.packageName = val; } public String getPackageName() {
/* 120 */     return this.packageName;
/*     */   } public String getJavadoc() {
/* 122 */     return this.javadoc;
/*     */   } public QName getName() {
/* 124 */     return NAME;
/* 125 */   } public static final QName NAME = new QName("http://java.sun.com/xml/ns/jaxb", "schemaBinding");
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\bindinfo\BISchemaBinding.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */