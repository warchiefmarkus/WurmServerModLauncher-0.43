/*    */ package 1.0.com.sun.xml.xsom.util;
/*    */ 
/*    */ import com.sun.xml.xsom.XSType;
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
/*    */ public abstract class TypeSet
/*    */ {
/*    */   public abstract boolean contains(XSType paramXSType);
/*    */   
/*    */   public static com.sun.xml.xsom.util.TypeSet intersection(com.sun.xml.xsom.util.TypeSet a, com.sun.xml.xsom.util.TypeSet b) {
/* 41 */     return (com.sun.xml.xsom.util.TypeSet)new Object(a, b);
/*    */   }
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
/*    */   public static com.sun.xml.xsom.util.TypeSet union(com.sun.xml.xsom.util.TypeSet a, com.sun.xml.xsom.util.TypeSet b) {
/* 57 */     return (com.sun.xml.xsom.util.TypeSet)new Object(a, b);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xso\\util\TypeSet.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */