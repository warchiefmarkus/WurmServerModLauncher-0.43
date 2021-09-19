/*    */ package com.sun.xml.bind.v2.model.annotation;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import javax.xml.bind.annotation.XmlRootElement;
/*    */ 
/*    */ 
/*    */ 
/*    */ final class XmlRootElementQuick
/*    */   extends Quick
/*    */   implements XmlRootElement
/*    */ {
/*    */   private final XmlRootElement core;
/*    */   
/*    */   public XmlRootElementQuick(Locatable upstream, XmlRootElement core) {
/* 15 */     super(upstream);
/* 16 */     this.core = core;
/*    */   }
/*    */   
/*    */   protected Annotation getAnnotation() {
/* 20 */     return (Annotation)this.core;
/*    */   }
/*    */   
/*    */   protected Quick newInstance(Locatable upstream, Annotation core) {
/* 24 */     return new XmlRootElementQuick(upstream, (XmlRootElement)core);
/*    */   }
/*    */   
/*    */   public Class<XmlRootElement> annotationType() {
/* 28 */     return XmlRootElement.class;
/*    */   }
/*    */   
/*    */   public String name() {
/* 32 */     return this.core.name();
/*    */   }
/*    */   
/*    */   public String namespace() {
/* 36 */     return this.core.namespace();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\annotation\XmlRootElementQuick.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */