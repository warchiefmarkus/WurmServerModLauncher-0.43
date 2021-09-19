/*    */ package com.sun.xml.bind.v2.model.annotation;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import javax.xml.bind.annotation.XmlElementDecl;
/*    */ 
/*    */ 
/*    */ 
/*    */ final class XmlElementDeclQuick
/*    */   extends Quick
/*    */   implements XmlElementDecl
/*    */ {
/*    */   private final XmlElementDecl core;
/*    */   
/*    */   public XmlElementDeclQuick(Locatable upstream, XmlElementDecl core) {
/* 15 */     super(upstream);
/* 16 */     this.core = core;
/*    */   }
/*    */   
/*    */   protected Annotation getAnnotation() {
/* 20 */     return (Annotation)this.core;
/*    */   }
/*    */   
/*    */   protected Quick newInstance(Locatable upstream, Annotation core) {
/* 24 */     return new XmlElementDeclQuick(upstream, (XmlElementDecl)core);
/*    */   }
/*    */   
/*    */   public Class<XmlElementDecl> annotationType() {
/* 28 */     return XmlElementDecl.class;
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
/*    */   public String defaultValue() {
/* 40 */     return this.core.defaultValue();
/*    */   }
/*    */   
/*    */   public Class scope() {
/* 44 */     return this.core.scope();
/*    */   }
/*    */   
/*    */   public String substitutionHeadNamespace() {
/* 48 */     return this.core.substitutionHeadNamespace();
/*    */   }
/*    */   
/*    */   public String substitutionHeadName() {
/* 52 */     return this.core.substitutionHeadName();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\annotation\XmlElementDeclQuick.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */