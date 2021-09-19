/*    */ package com.sun.xml.xsom.impl;
/*    */ 
/*    */ import com.sun.xml.xsom.XSComplexType;
/*    */ import com.sun.xml.xsom.XSType;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashSet;
/*    */ import java.util.Iterator;
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
/*    */ class Util
/*    */ {
/*    */   private static XSType[] listDirectSubstitutables(XSType _this) {
/* 38 */     ArrayList<XSType> r = new ArrayList();
/*    */ 
/*    */     
/* 41 */     Iterator<XSType> itr = ((SchemaImpl)_this.getOwnerSchema()).parent.iterateTypes();
/* 42 */     while (itr.hasNext()) {
/* 43 */       XSType t = itr.next();
/* 44 */       if (t.getBaseType() == _this)
/* 45 */         r.add(t); 
/*    */     } 
/* 47 */     return r.<XSType>toArray(new XSType[r.size()]);
/*    */   }
/*    */   
/*    */   public static XSType[] listSubstitutables(XSType _this) {
/* 51 */     Set substitables = new HashSet();
/* 52 */     buildSubstitutables(_this, substitables);
/* 53 */     return (XSType[])substitables.toArray((Object[])new XSType[substitables.size()]);
/*    */   }
/*    */   
/*    */   public static void buildSubstitutables(XSType _this, Set substitutables) {
/* 57 */     if (_this.isLocal())
/* 58 */       return;  buildSubstitutables(_this, _this, substitutables);
/*    */   }
/*    */   
/*    */   private static void buildSubstitutables(XSType head, XSType _this, Set<XSType> substitutables) {
/* 62 */     if (!isSubstitutable(head, _this)) {
/*    */       return;
/*    */     }
/* 65 */     if (substitutables.add(_this)) {
/* 66 */       XSType[] child = listDirectSubstitutables(_this);
/* 67 */       for (int i = 0; i < child.length; i++) {
/* 68 */         buildSubstitutables(head, child[i], substitutables);
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static boolean isSubstitutable(XSType _base, XSType derived) {
/* 80 */     if (_base.isComplexType()) {
/* 81 */       XSComplexType base = _base.asComplexType();
/*    */       
/* 83 */       for (; base != derived; derived = derived.getBaseType()) {
/* 84 */         if (base.isSubstitutionProhibited(derived.getDerivationMethod()))
/* 85 */           return false; 
/*    */       } 
/* 87 */       return true;
/*    */     } 
/*    */     
/* 90 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\Util.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */