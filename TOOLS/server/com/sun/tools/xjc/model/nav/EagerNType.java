/*    */ package com.sun.tools.xjc.model.nav;
/*    */ 
/*    */ import com.sun.codemodel.JType;
/*    */ import com.sun.tools.xjc.outline.Aspect;
/*    */ import com.sun.tools.xjc.outline.Outline;
/*    */ import com.sun.xml.bind.v2.model.nav.Navigator;
/*    */ import java.lang.reflect.Type;
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
/*    */ class EagerNType
/*    */   implements NType
/*    */ {
/*    */   final Type t;
/*    */   
/*    */   public EagerNType(Type type) {
/* 53 */     this.t = type;
/* 54 */     assert this.t != null;
/*    */   }
/*    */   
/*    */   public JType toType(Outline o, Aspect aspect) {
/*    */     try {
/* 59 */       return o.getCodeModel().parseType(this.t.toString());
/* 60 */     } catch (ClassNotFoundException e) {
/* 61 */       throw new NoClassDefFoundError(e.getMessage());
/*    */     } 
/*    */   }
/*    */   
/*    */   public boolean equals(Object o) {
/* 66 */     if (this == o) return true; 
/* 67 */     if (!(o instanceof EagerNType)) return false;
/*    */     
/* 69 */     EagerNType eagerNType = (EagerNType)o;
/*    */     
/* 71 */     return this.t.equals(eagerNType.t);
/*    */   }
/*    */   
/*    */   public boolean isBoxedType() {
/* 75 */     return false;
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 79 */     return this.t.hashCode();
/*    */   }
/*    */   
/*    */   public String fullName() {
/* 83 */     return Navigator.REFLECTION.getTypeName(this.t);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\model\nav\EagerNType.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */