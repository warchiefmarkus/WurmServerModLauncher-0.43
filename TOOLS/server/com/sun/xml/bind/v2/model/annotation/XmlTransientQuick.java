/*    */ package com.sun.xml.bind.v2.model.annotation;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import javax.xml.bind.annotation.XmlTransient;
/*    */ 
/*    */ 
/*    */ 
/*    */ final class XmlTransientQuick
/*    */   extends Quick
/*    */   implements XmlTransient
/*    */ {
/*    */   private final XmlTransient core;
/*    */   
/*    */   public XmlTransientQuick(Locatable upstream, XmlTransient core) {
/* 15 */     super(upstream);
/* 16 */     this.core = core;
/*    */   }
/*    */   
/*    */   protected Annotation getAnnotation() {
/* 20 */     return (Annotation)this.core;
/*    */   }
/*    */   
/*    */   protected Quick newInstance(Locatable upstream, Annotation core) {
/* 24 */     return new XmlTransientQuick(upstream, (XmlTransient)core);
/*    */   }
/*    */   
/*    */   public Class<XmlTransient> annotationType() {
/* 28 */     return XmlTransient.class;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\annotation\XmlTransientQuick.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */