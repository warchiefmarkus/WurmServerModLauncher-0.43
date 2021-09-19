/*    */ package com.sun.xml.bind.v2.model.annotation;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import javax.xml.bind.annotation.XmlSchemaType;
/*    */ 
/*    */ 
/*    */ 
/*    */ final class XmlSchemaTypeQuick
/*    */   extends Quick
/*    */   implements XmlSchemaType
/*    */ {
/*    */   private final XmlSchemaType core;
/*    */   
/*    */   public XmlSchemaTypeQuick(Locatable upstream, XmlSchemaType core) {
/* 15 */     super(upstream);
/* 16 */     this.core = core;
/*    */   }
/*    */   
/*    */   protected Annotation getAnnotation() {
/* 20 */     return (Annotation)this.core;
/*    */   }
/*    */   
/*    */   protected Quick newInstance(Locatable upstream, Annotation core) {
/* 24 */     return new XmlSchemaTypeQuick(upstream, (XmlSchemaType)core);
/*    */   }
/*    */   
/*    */   public Class<XmlSchemaType> annotationType() {
/* 28 */     return XmlSchemaType.class;
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
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\annotation\XmlSchemaTypeQuick.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */