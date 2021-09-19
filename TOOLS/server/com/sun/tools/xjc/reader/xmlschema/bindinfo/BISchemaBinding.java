/*     */ package com.sun.tools.xjc.reader.xmlschema.bindinfo;
/*     */ 
/*     */ import com.sun.xml.xsom.XSComponent;
/*     */ import javax.xml.bind.annotation.XmlAttribute;
/*     */ import javax.xml.bind.annotation.XmlElement;
/*     */ import javax.xml.bind.annotation.XmlRootElement;
/*     */ import javax.xml.bind.annotation.XmlType;
/*     */ import javax.xml.namespace.QName;
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
/*     */ @XmlRootElement(name = "schemaBindings")
/*     */ public final class BISchemaBinding
/*     */   extends AbstractDeclarationImpl
/*     */ {
/*     */   @XmlType(propOrder = {})
/*     */   private static final class NameRules
/*     */   {
/*     */     private NameRules() {}
/*     */     
/*     */     @XmlElement
/*  67 */     BISchemaBinding.NamingRule typeName = BISchemaBinding.defaultNamingRule;
/*     */     @XmlElement
/*  69 */     BISchemaBinding.NamingRule elementName = BISchemaBinding.defaultNamingRule;
/*     */     @XmlElement
/*  71 */     BISchemaBinding.NamingRule attributeName = BISchemaBinding.defaultNamingRule;
/*     */     @XmlElement
/*  73 */     BISchemaBinding.NamingRule modelGroupName = BISchemaBinding.defaultNamingRule;
/*     */     @XmlElement
/*  75 */     BISchemaBinding.NamingRule anonymousTypeName = BISchemaBinding.defaultNamingRule;
/*     */   }
/*     */   
/*     */   @XmlElement
/*  79 */   private NameRules nameXmlTransform = new NameRules();
/*     */   
/*     */   private static final class PackageInfo {
/*     */     @XmlAttribute
/*     */     String name;
/*     */     @XmlElement
/*     */     String javadoc;
/*     */     
/*     */     private PackageInfo() {} }
/*     */   @XmlElement(name = "package")
/*  89 */   private PackageInfo packageInfo = new PackageInfo();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlAttribute(name = "map")
/*     */   public boolean map = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 102 */   private static final NamingRule defaultNamingRule = new NamingRule("", "");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class NamingRule
/*     */   {
/*     */     @XmlAttribute
/* 114 */     private String prefix = "";
/*     */     @XmlAttribute
/* 116 */     private String suffix = "";
/*     */     
/*     */     public NamingRule(String _prefix, String _suffix) {
/* 119 */       this.prefix = _prefix;
/* 120 */       this.suffix = _suffix;
/*     */     }
/*     */ 
/*     */     
/*     */     public NamingRule() {}
/*     */ 
/*     */     
/*     */     public String mangle(String originalName) {
/* 128 */       return this.prefix + originalName + this.suffix;
/*     */     }
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
/*     */   public String mangleClassName(String name, XSComponent cmp) {
/* 143 */     if (cmp instanceof com.sun.xml.xsom.XSType)
/* 144 */       return this.nameXmlTransform.typeName.mangle(name); 
/* 145 */     if (cmp instanceof com.sun.xml.xsom.XSElementDecl)
/* 146 */       return this.nameXmlTransform.elementName.mangle(name); 
/* 147 */     if (cmp instanceof com.sun.xml.xsom.XSAttributeDecl)
/* 148 */       return this.nameXmlTransform.attributeName.mangle(name); 
/* 149 */     if (cmp instanceof com.sun.xml.xsom.XSModelGroup || cmp instanceof com.sun.xml.xsom.XSModelGroupDecl) {
/* 150 */       return this.nameXmlTransform.modelGroupName.mangle(name);
/*     */     }
/*     */     
/* 153 */     return name;
/*     */   }
/*     */   
/*     */   public String mangleAnonymousTypeClassName(String name) {
/* 157 */     return this.nameXmlTransform.anonymousTypeName.mangle(name);
/*     */   }
/*     */   
/*     */   public String getPackageName() {
/* 161 */     return this.packageInfo.name;
/*     */   } public String getJavadoc() {
/* 163 */     return this.packageInfo.javadoc;
/*     */   } public QName getName() {
/* 165 */     return NAME;
/* 166 */   } public static final QName NAME = new QName("http://java.sun.com/xml/ns/jaxb", "schemaBinding");
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\bindinfo\BISchemaBinding.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */