/*    */ package com.sun.xml.xsom.util;
/*    */ 
/*    */ import com.sun.xml.xsom.XSType;
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
/* 39 */     this.typeSet = s;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean contains(XSType type) {
/* 46 */     return this.typeSet.contains(type);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xso\\util\SimpleTypeSet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */