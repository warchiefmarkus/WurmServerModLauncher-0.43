/*    */ package com.sun.xml.bind.v2.schemagen;
/*    */ 
/*    */ import com.sun.xml.bind.v2.schemagen.xmlschema.ContentModelContainer;
/*    */ import com.sun.xml.bind.v2.schemagen.xmlschema.Particle;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ enum GroupKind
/*    */ {
/* 48 */   ALL("all"), SEQUENCE("sequence"), CHOICE("choice");
/*    */   
/*    */   private final String name;
/*    */   
/*    */   GroupKind(String name) {
/* 53 */     this.name = name;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   Particle write(ContentModelContainer parent) {
/* 60 */     return (Particle)parent._element(this.name, Particle.class);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\schemagen\GroupKind.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */