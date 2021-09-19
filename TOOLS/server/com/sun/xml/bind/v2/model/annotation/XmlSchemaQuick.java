/*    */ package com.sun.xml.bind.v2.model.annotation;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import javax.xml.bind.annotation.XmlNs;
/*    */ import javax.xml.bind.annotation.XmlNsForm;
/*    */ import javax.xml.bind.annotation.XmlSchema;
/*    */ 
/*    */ 
/*    */ 
/*    */ final class XmlSchemaQuick
/*    */   extends Quick
/*    */   implements XmlSchema
/*    */ {
/*    */   private final XmlSchema core;
/*    */   
/*    */   public XmlSchemaQuick(Locatable upstream, XmlSchema core) {
/* 17 */     super(upstream);
/* 18 */     this.core = core;
/*    */   }
/*    */   
/*    */   protected Annotation getAnnotation() {
/* 22 */     return (Annotation)this.core;
/*    */   }
/*    */   
/*    */   protected Quick newInstance(Locatable upstream, Annotation core) {
/* 26 */     return new XmlSchemaQuick(upstream, (XmlSchema)core);
/*    */   }
/*    */   
/*    */   public Class<XmlSchema> annotationType() {
/* 30 */     return XmlSchema.class;
/*    */   }
/*    */   
/*    */   public String location() {
/* 34 */     return this.core.location();
/*    */   }
/*    */   
/*    */   public String namespace() {
/* 38 */     return this.core.namespace();
/*    */   }
/*    */   
/*    */   public XmlNs[] xmlns() {
/* 42 */     return this.core.xmlns();
/*    */   }
/*    */   
/*    */   public XmlNsForm elementFormDefault() {
/* 46 */     return this.core.elementFormDefault();
/*    */   }
/*    */   
/*    */   public XmlNsForm attributeFormDefault() {
/* 50 */     return this.core.attributeFormDefault();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\annotation\XmlSchemaQuick.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */