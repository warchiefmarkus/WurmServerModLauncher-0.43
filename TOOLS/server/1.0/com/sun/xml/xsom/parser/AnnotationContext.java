/*    */ package 1.0.com.sun.xml.xsom.parser;
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
/* 25 */     this.name = _name;
/*    */   }
/*    */   public String toString() {
/* 28 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/* 32 */   public static final com.sun.xml.xsom.parser.AnnotationContext SCHEMA = new com.sun.xml.xsom.parser.AnnotationContext("schema");
/*    */   
/* 34 */   public static final com.sun.xml.xsom.parser.AnnotationContext NOTATION = new com.sun.xml.xsom.parser.AnnotationContext("notation");
/*    */   
/* 36 */   public static final com.sun.xml.xsom.parser.AnnotationContext ELEMENT_DECL = new com.sun.xml.xsom.parser.AnnotationContext("element");
/*    */   
/* 38 */   public static final com.sun.xml.xsom.parser.AnnotationContext IDENTITY_CONSTRAINT = new com.sun.xml.xsom.parser.AnnotationContext("identityConstraint");
/*    */   
/* 40 */   public static final com.sun.xml.xsom.parser.AnnotationContext MODELGROUP_DECL = new com.sun.xml.xsom.parser.AnnotationContext("modelGroupDecl");
/*    */   
/* 42 */   public static final com.sun.xml.xsom.parser.AnnotationContext SIMPLETYPE_DECL = new com.sun.xml.xsom.parser.AnnotationContext("simpleTypeDecl");
/*    */   
/* 44 */   public static final com.sun.xml.xsom.parser.AnnotationContext COMPLEXTYPE_DECL = new com.sun.xml.xsom.parser.AnnotationContext("complexTypeDecl");
/*    */   
/* 46 */   public static final com.sun.xml.xsom.parser.AnnotationContext PARTICLE = new com.sun.xml.xsom.parser.AnnotationContext("particle");
/*    */   
/* 48 */   public static final com.sun.xml.xsom.parser.AnnotationContext MODELGROUP = new com.sun.xml.xsom.parser.AnnotationContext("modelGroup");
/*    */   
/* 50 */   public static final com.sun.xml.xsom.parser.AnnotationContext ATTRIBUTE_USE = new com.sun.xml.xsom.parser.AnnotationContext("attributeUse");
/*    */   
/* 52 */   public static final com.sun.xml.xsom.parser.AnnotationContext WILDCARD = new com.sun.xml.xsom.parser.AnnotationContext("wildcard");
/*    */   
/* 54 */   public static final com.sun.xml.xsom.parser.AnnotationContext ATTRIBUTE_GROUP = new com.sun.xml.xsom.parser.AnnotationContext("attributeGroup");
/*    */   
/* 56 */   public static final com.sun.xml.xsom.parser.AnnotationContext ATTRIBUTE_DECL = new com.sun.xml.xsom.parser.AnnotationContext("attributeDecl");
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\parser\AnnotationContext.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */