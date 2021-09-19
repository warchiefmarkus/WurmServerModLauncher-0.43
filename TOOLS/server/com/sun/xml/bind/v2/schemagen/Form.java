/*     */ package com.sun.xml.bind.v2.schemagen;
/*     */ 
/*     */ import com.sun.xml.bind.v2.schemagen.xmlschema.LocalAttribute;
/*     */ import com.sun.xml.bind.v2.schemagen.xmlschema.LocalElement;
/*     */ import com.sun.xml.bind.v2.schemagen.xmlschema.Schema;
/*     */ import com.sun.xml.txw2.TypedXmlWriter;
/*     */ import javax.xml.bind.annotation.XmlNsForm;
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
/*     */ enum Form
/*     */ {
/*  53 */   QUALIFIED(XmlNsForm.QUALIFIED, true) {
/*     */     void declare(String attName, Schema schema) {
/*  55 */       schema._attribute(attName, "qualified");
/*     */     }
/*     */   },
/*  58 */   UNQUALIFIED(XmlNsForm.UNQUALIFIED, false)
/*     */   {
/*     */     void declare(String attName, Schema schema)
/*     */     {
/*  62 */       schema._attribute(attName, "unqualified");
/*     */     }
/*     */   },
/*  65 */   UNSET(XmlNsForm.UNSET, false)
/*     */   {
/*     */     void declare(String attName, Schema schema) {}
/*     */   };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final XmlNsForm xnf;
/*     */ 
/*     */   
/*     */   public final boolean isEffectivelyQualified;
/*     */ 
/*     */ 
/*     */   
/*     */   Form(XmlNsForm xnf, boolean effectivelyQualified) {
/*  81 */     this.xnf = xnf;
/*  82 */     this.isEffectivelyQualified = effectivelyQualified;
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
/*     */   public void writeForm(LocalElement e, QName tagName) {
/*  95 */     _writeForm((TypedXmlWriter)e, tagName);
/*     */   }
/*     */   
/*     */   public void writeForm(LocalAttribute a, QName tagName) {
/*  99 */     _writeForm((TypedXmlWriter)a, tagName);
/*     */   }
/*     */   
/*     */   private void _writeForm(TypedXmlWriter e, QName tagName) {
/* 103 */     boolean qualified = (tagName.getNamespaceURI().length() > 0);
/*     */     
/* 105 */     if (qualified && this != QUALIFIED) {
/* 106 */       e._attribute("form", "qualified");
/*     */     }
/* 108 */     else if (!qualified && this == QUALIFIED) {
/* 109 */       e._attribute("form", "unqualified");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Form get(XmlNsForm xnf) {
/* 116 */     for (Form v : values()) {
/* 117 */       if (v.xnf == xnf)
/* 118 */         return v; 
/*     */     } 
/* 120 */     throw new IllegalArgumentException();
/*     */   }
/*     */   
/*     */   abstract void declare(String paramString, Schema paramSchema);
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\schemagen\Form.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */