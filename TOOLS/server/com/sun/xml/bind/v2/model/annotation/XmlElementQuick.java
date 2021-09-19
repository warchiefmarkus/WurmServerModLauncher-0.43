/*    */ package com.sun.xml.bind.v2.model.annotation;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import javax.xml.bind.annotation.XmlElement;
/*    */ 
/*    */ 
/*    */ 
/*    */ final class XmlElementQuick
/*    */   extends Quick
/*    */   implements XmlElement
/*    */ {
/*    */   private final XmlElement core;
/*    */   
/*    */   public XmlElementQuick(Locatable upstream, XmlElement core) {
/* 15 */     super(upstream);
/* 16 */     this.core = core;
/*    */   }
/*    */   
/*    */   protected Annotation getAnnotation() {
/* 20 */     return (Annotation)this.core;
/*    */   }
/*    */   
/*    */   protected Quick newInstance(Locatable upstream, Annotation core) {
/* 24 */     return new XmlElementQuick(upstream, (XmlElement)core);
/*    */   }
/*    */   
/*    */   public Class<XmlElement> annotationType() {
/* 28 */     return XmlElement.class;
/*    */   }
/*    */   
/*    */   public String name() {
/* 32 */     return this.core.name();
/*    */   }
/*    */   
/*    */   public Class type() {
/* 36 */     return this.core.type();
/*    */   }
/*    */   
/*    */   public String namespace() {
/* 40 */     return this.core.namespace();
/*    */   }
/*    */   
/*    */   public String defaultValue() {
/* 44 */     return this.core.defaultValue();
/*    */   }
/*    */   
/*    */   public boolean required() {
/* 48 */     return this.core.required();
/*    */   }
/*    */   
/*    */   public boolean nillable() {
/* 52 */     return this.core.nillable();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\annotation\XmlElementQuick.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */