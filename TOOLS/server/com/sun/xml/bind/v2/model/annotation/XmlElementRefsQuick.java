/*    */ package com.sun.xml.bind.v2.model.annotation;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import javax.xml.bind.annotation.XmlElementRef;
/*    */ import javax.xml.bind.annotation.XmlElementRefs;
/*    */ 
/*    */ 
/*    */ 
/*    */ final class XmlElementRefsQuick
/*    */   extends Quick
/*    */   implements XmlElementRefs
/*    */ {
/*    */   private final XmlElementRefs core;
/*    */   
/*    */   public XmlElementRefsQuick(Locatable upstream, XmlElementRefs core) {
/* 16 */     super(upstream);
/* 17 */     this.core = core;
/*    */   }
/*    */   
/*    */   protected Annotation getAnnotation() {
/* 21 */     return (Annotation)this.core;
/*    */   }
/*    */   
/*    */   protected Quick newInstance(Locatable upstream, Annotation core) {
/* 25 */     return new XmlElementRefsQuick(upstream, (XmlElementRefs)core);
/*    */   }
/*    */   
/*    */   public Class<XmlElementRefs> annotationType() {
/* 29 */     return XmlElementRefs.class;
/*    */   }
/*    */   
/*    */   public XmlElementRef[] value() {
/* 33 */     return this.core.value();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\annotation\XmlElementRefsQuick.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */