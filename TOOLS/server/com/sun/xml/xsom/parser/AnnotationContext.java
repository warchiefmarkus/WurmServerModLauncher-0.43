/*    */ package com.sun.xml.xsom.parser;
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
/*    */ public final class AnnotationContext
/*    */ {
/*    */   private final String name;
/*    */   
/*    */   private AnnotationContext(String _name) {
/* 35 */     this.name = _name;
/*    */   }
/*    */   public String toString() {
/* 38 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/* 42 */   public static final AnnotationContext SCHEMA = new AnnotationContext("schema");
/*    */   
/* 44 */   public static final AnnotationContext NOTATION = new AnnotationContext("notation");
/*    */   
/* 46 */   public static final AnnotationContext ELEMENT_DECL = new AnnotationContext("element");
/*    */   
/* 48 */   public static final AnnotationContext IDENTITY_CONSTRAINT = new AnnotationContext("identityConstraint");
/*    */   
/* 50 */   public static final AnnotationContext XPATH = new AnnotationContext("xpath");
/*    */   
/* 52 */   public static final AnnotationContext MODELGROUP_DECL = new AnnotationContext("modelGroupDecl");
/*    */   
/* 54 */   public static final AnnotationContext SIMPLETYPE_DECL = new AnnotationContext("simpleTypeDecl");
/*    */   
/* 56 */   public static final AnnotationContext COMPLEXTYPE_DECL = new AnnotationContext("complexTypeDecl");
/*    */   
/* 58 */   public static final AnnotationContext PARTICLE = new AnnotationContext("particle");
/*    */   
/* 60 */   public static final AnnotationContext MODELGROUP = new AnnotationContext("modelGroup");
/*    */   
/* 62 */   public static final AnnotationContext ATTRIBUTE_USE = new AnnotationContext("attributeUse");
/*    */   
/* 64 */   public static final AnnotationContext WILDCARD = new AnnotationContext("wildcard");
/*    */   
/* 66 */   public static final AnnotationContext ATTRIBUTE_GROUP = new AnnotationContext("attributeGroup");
/*    */   
/* 68 */   public static final AnnotationContext ATTRIBUTE_DECL = new AnnotationContext("attributeDecl");
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\parser\AnnotationContext.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */