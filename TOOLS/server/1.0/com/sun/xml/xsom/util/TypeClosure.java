/*    */ package 1.0.com.sun.xml.xsom.util;
/*    */ 
/*    */ import com.sun.xml.xsom.XSType;
/*    */ import com.sun.xml.xsom.util.TypeSet;
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
/*    */ public class TypeClosure
/*    */   extends TypeSet
/*    */ {
/*    */   private final TypeSet typeSet;
/*    */   
/*    */   public TypeClosure(TypeSet typeSet) {
/* 28 */     this.typeSet = typeSet;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean contains(XSType type) {
/* 37 */     if (this.typeSet.contains(type)) {
/* 38 */       return true;
/*    */     }
/* 40 */     XSType baseType = type.getBaseType();
/* 41 */     if (baseType == null) {
/* 42 */       return false;
/*    */     }
/*    */     
/* 45 */     return contains(baseType);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xso\\util\TypeClosure.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */