/*    */ package 1.0.com.sun.xml.xsom.impl;
/*    */ 
/*    */ import com.sun.xml.xsom.XSComplexType;
/*    */ import com.sun.xml.xsom.XSType;
/*    */ import com.sun.xml.xsom.impl.SchemaImpl;
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
/*    */ class Util
/*    */ {
/*    */   private static XSType[] listDirectSubstitutables(XSType _this) {
/* 28 */     ArrayList r = new ArrayList();
/*    */ 
/*    */     
/* 31 */     Iterator itr = ((SchemaImpl)_this.getOwnerSchema()).parent.iterateTypes();
/* 32 */     while (itr.hasNext()) {
/* 33 */       XSType t = itr.next();
/* 34 */       if (t.getBaseType() == _this)
/* 35 */         r.add(t); 
/*    */     } 
/* 37 */     return r.<XSType>toArray(new XSType[r.size()]);
/*    */   }
/*    */   
/*    */   public static XSType[] listSubstitutables(XSType _this) {
/* 41 */     Set substitables = new HashSet();
/* 42 */     buildSubstitutables(_this, substitables);
/* 43 */     return (XSType[])substitables.toArray((Object[])new XSType[substitables.size()]);
/*    */   }
/*    */   
/*    */   public static void buildSubstitutables(XSType _this, Set substitutables) {
/* 47 */     if (_this.isLocal())
/* 48 */       return;  buildSubstitutables(_this, _this, substitutables);
/*    */   }
/*    */   
/*    */   private static void buildSubstitutables(XSType head, XSType _this, Set substitutables) {
/* 52 */     if (!isSubstitutable(head, _this)) {
/*    */       return;
/*    */     }
/* 55 */     if (substitutables.add(_this)) {
/* 56 */       XSType[] child = listDirectSubstitutables(_this);
/* 57 */       for (int i = 0; i < child.length; i++) {
/* 58 */         buildSubstitutables(head, child[i], substitutables);
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
/* 70 */     if (_base.isComplexType()) {
/* 71 */       XSComplexType base = _base.asComplexType();
/*    */       
/* 73 */       for (; base != derived; derived = derived.getBaseType()) {
/* 74 */         if (base.isSubstitutionProhibited(derived.getDerivationMethod()))
/* 75 */           return false; 
/*    */       } 
/* 77 */       return true;
/*    */     } 
/*    */     
/* 80 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\Util.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */