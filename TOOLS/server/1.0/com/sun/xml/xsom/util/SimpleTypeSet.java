/*    */ package 1.0.com.sun.xml.xsom.util;
/*    */ 
/*    */ import com.sun.xml.xsom.XSType;
/*    */ import com.sun.xml.xsom.util.TypeSet;
/*    */ import java.util.Set;
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
/*    */ public class SimpleTypeSet
/*    */   extends TypeSet
/*    */ {
/*    */   private final Set typeSet;
/*    */   
/*    */   public SimpleTypeSet(Set s) {
/* 29 */     this.typeSet = s;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean contains(XSType type) {
/* 36 */     return this.typeSet.contains(type);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xso\\util\SimpleTypeSet.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */