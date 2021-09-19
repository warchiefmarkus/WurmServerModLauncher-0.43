/*    */ package com.sun.xml.bind.v2.model.annotation;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import javax.xml.bind.annotation.XmlEnum;
/*    */ 
/*    */ 
/*    */ 
/*    */ final class XmlEnumQuick
/*    */   extends Quick
/*    */   implements XmlEnum
/*    */ {
/*    */   private final XmlEnum core;
/*    */   
/*    */   public XmlEnumQuick(Locatable upstream, XmlEnum core) {
/* 15 */     super(upstream);
/* 16 */     this.core = core;
/*    */   }
/*    */   
/*    */   protected Annotation getAnnotation() {
/* 20 */     return (Annotation)this.core;
/*    */   }
/*    */   
/*    */   protected Quick newInstance(Locatable upstream, Annotation core) {
/* 24 */     return new XmlEnumQuick(upstream, (XmlEnum)core);
/*    */   }
/*    */   
/*    */   public Class<XmlEnum> annotationType() {
/* 28 */     return XmlEnum.class;
/*    */   }
/*    */   
/*    */   public Class value() {
/* 32 */     return this.core.value();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\annotation\XmlEnumQuick.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */