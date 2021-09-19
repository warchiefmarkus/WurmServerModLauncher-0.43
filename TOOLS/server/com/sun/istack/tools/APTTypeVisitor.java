/*    */ package com.sun.istack.tools;
/*    */ 
/*    */ import com.sun.mirror.type.ArrayType;
/*    */ import com.sun.mirror.type.ClassType;
/*    */ import com.sun.mirror.type.InterfaceType;
/*    */ import com.sun.mirror.type.PrimitiveType;
/*    */ import com.sun.mirror.type.TypeMirror;
/*    */ import com.sun.mirror.type.TypeVariable;
/*    */ import com.sun.mirror.type.VoidType;
/*    */ import com.sun.mirror.type.WildcardType;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class APTTypeVisitor<T, P>
/*    */ {
/*    */   public final T apply(TypeMirror type, P param) {
/* 22 */     if (type instanceof ArrayType)
/* 23 */       return onArrayType((ArrayType)type, param); 
/* 24 */     if (type instanceof PrimitiveType)
/* 25 */       return onPrimitiveType((PrimitiveType)type, param); 
/* 26 */     if (type instanceof ClassType)
/* 27 */       return onClassType((ClassType)type, param); 
/* 28 */     if (type instanceof InterfaceType)
/* 29 */       return onInterfaceType((InterfaceType)type, param); 
/* 30 */     if (type instanceof TypeVariable)
/* 31 */       return onTypeVariable((TypeVariable)type, param); 
/* 32 */     if (type instanceof VoidType)
/* 33 */       return onVoidType((VoidType)type, param); 
/* 34 */     if (type instanceof WildcardType)
/* 35 */       return onWildcard((WildcardType)type, param); 
/*    */     assert false;
/* 37 */     throw new IllegalArgumentException();
/*    */   }
/*    */   
/*    */   protected abstract T onPrimitiveType(PrimitiveType paramPrimitiveType, P paramP);
/*    */   
/*    */   protected abstract T onArrayType(ArrayType paramArrayType, P paramP);
/*    */   
/*    */   protected abstract T onClassType(ClassType paramClassType, P paramP);
/*    */   
/*    */   protected abstract T onInterfaceType(InterfaceType paramInterfaceType, P paramP);
/*    */   
/*    */   protected abstract T onTypeVariable(TypeVariable paramTypeVariable, P paramP);
/*    */   
/*    */   protected abstract T onVoidType(VoidType paramVoidType, P paramP);
/*    */   
/*    */   protected abstract T onWildcard(WildcardType paramWildcardType, P paramP);
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\istack\tools\APTTypeVisitor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */