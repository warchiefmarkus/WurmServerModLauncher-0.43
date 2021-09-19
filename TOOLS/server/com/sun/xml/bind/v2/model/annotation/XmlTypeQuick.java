/*    */ package com.sun.xml.bind.v2.model.annotation;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import javax.xml.bind.annotation.XmlType;
/*    */ 
/*    */ 
/*    */ 
/*    */ final class XmlTypeQuick
/*    */   extends Quick
/*    */   implements XmlType
/*    */ {
/*    */   private final XmlType core;
/*    */   
/*    */   public XmlTypeQuick(Locatable upstream, XmlType core) {
/* 15 */     super(upstream);
/* 16 */     this.core = core;
/*    */   }
/*    */   
/*    */   protected Annotation getAnnotation() {
/* 20 */     return (Annotation)this.core;
/*    */   }
/*    */   
/*    */   protected Quick newInstance(Locatable upstream, Annotation core) {
/* 24 */     return new XmlTypeQuick(upstream, (XmlType)core);
/*    */   }
/*    */   
/*    */   public Class<XmlType> annotationType() {
/* 28 */     return XmlType.class;
/*    */   }
/*    */   
/*    */   public String name() {
/* 32 */     return this.core.name();
/*    */   }
/*    */   
/*    */   public String namespace() {
/* 36 */     return this.core.namespace();
/*    */   }
/*    */   
/*    */   public String[] propOrder() {
/* 40 */     return this.core.propOrder();
/*    */   }
/*    */   
/*    */   public Class factoryClass() {
/* 44 */     return this.core.factoryClass();
/*    */   }
/*    */   
/*    */   public String factoryMethod() {
/* 48 */     return this.core.factoryMethod();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\annotation\XmlTypeQuick.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */