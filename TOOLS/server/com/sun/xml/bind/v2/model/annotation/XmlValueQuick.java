/*    */ package com.sun.xml.bind.v2.model.annotation;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import javax.xml.bind.annotation.XmlValue;
/*    */ 
/*    */ 
/*    */ 
/*    */ final class XmlValueQuick
/*    */   extends Quick
/*    */   implements XmlValue
/*    */ {
/*    */   private final XmlValue core;
/*    */   
/*    */   public XmlValueQuick(Locatable upstream, XmlValue core) {
/* 15 */     super(upstream);
/* 16 */     this.core = core;
/*    */   }
/*    */   
/*    */   protected Annotation getAnnotation() {
/* 20 */     return (Annotation)this.core;
/*    */   }
/*    */   
/*    */   protected Quick newInstance(Locatable upstream, Annotation core) {
/* 24 */     return new XmlValueQuick(upstream, (XmlValue)core);
/*    */   }
/*    */   
/*    */   public Class<XmlValue> annotationType() {
/* 28 */     return XmlValue.class;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\annotation\XmlValueQuick.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */