/*    */ package 1.0.com.sun.codemodel;
/*    */ 
/*    */ import com.sun.codemodel.JClass;
/*    */ import com.sun.codemodel.JCodeModel;
/*    */ import com.sun.codemodel.JGenerable;
/*    */ import com.sun.codemodel.JPrimitiveType;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class JType
/*    */   implements JGenerable
/*    */ {
/*    */   public static JPrimitiveType parse(JCodeModel codeModel, String typeName) {
/* 19 */     if (typeName.equals("void"))
/* 20 */       return codeModel.VOID; 
/* 21 */     if (typeName.equals("boolean"))
/* 22 */       return codeModel.BOOLEAN; 
/* 23 */     if (typeName.equals("byte"))
/* 24 */       return codeModel.BYTE; 
/* 25 */     if (typeName.equals("short"))
/* 26 */       return codeModel.SHORT; 
/* 27 */     if (typeName.equals("char"))
/* 28 */       return codeModel.CHAR; 
/* 29 */     if (typeName.equals("int"))
/* 30 */       return codeModel.INT; 
/* 31 */     if (typeName.equals("float"))
/* 32 */       return codeModel.FLOAT; 
/* 33 */     if (typeName.equals("long"))
/* 34 */       return codeModel.LONG; 
/* 35 */     if (typeName.equals("double")) {
/* 36 */       return codeModel.DOUBLE;
/*    */     }
/* 38 */     throw new IllegalArgumentException("Not a primitive type: " + typeName);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract JCodeModel owner();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract String fullName();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract String name();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract JClass array();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isArray() {
/* 70 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isPrimitive() {
/* 75 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final boolean isReference() {
/* 82 */     return !isPrimitive();
/*    */   }
/*    */   
/*    */   public com.sun.codemodel.JType elementType() {
/* 86 */     throw new IllegalArgumentException("Not an array type");
/*    */   }
/*    */   
/*    */   public String toString() {
/* 90 */     return getClass().getName() + "(" + fullName() + ")";
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\JType.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */