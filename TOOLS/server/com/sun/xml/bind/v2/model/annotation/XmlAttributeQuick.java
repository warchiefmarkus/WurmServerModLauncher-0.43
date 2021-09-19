/*    */ package com.sun.xml.bind.v2.model.annotation;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import javax.xml.bind.annotation.XmlAttribute;
/*    */ 
/*    */ 
/*    */ 
/*    */ final class XmlAttributeQuick
/*    */   extends Quick
/*    */   implements XmlAttribute
/*    */ {
/*    */   private final XmlAttribute core;
/*    */   
/*    */   public XmlAttributeQuick(Locatable upstream, XmlAttribute core) {
/* 15 */     super(upstream);
/* 16 */     this.core = core;
/*    */   }
/*    */   
/*    */   protected Annotation getAnnotation() {
/* 20 */     return (Annotation)this.core;
/*    */   }
/*    */   
/*    */   protected Quick newInstance(Locatable upstream, Annotation core) {
/* 24 */     return new XmlAttributeQuick(upstream, (XmlAttribute)core);
/*    */   }
/*    */   
/*    */   public Class<XmlAttribute> annotationType() {
/* 28 */     return XmlAttribute.class;
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
/*    */   public boolean required() {
/* 40 */     return this.core.required();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\annotation\XmlAttributeQuick.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */