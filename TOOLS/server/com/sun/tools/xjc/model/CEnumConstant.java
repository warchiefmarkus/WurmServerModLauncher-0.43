/*    */ package com.sun.tools.xjc.model;
/*    */ 
/*    */ import com.sun.tools.xjc.model.nav.NClass;
/*    */ import com.sun.tools.xjc.model.nav.NType;
/*    */ import com.sun.xml.bind.v2.model.core.EnumConstant;
/*    */ import com.sun.xml.bind.v2.model.core.EnumLeafInfo;
/*    */ import org.xml.sax.Locator;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class CEnumConstant
/*    */   implements EnumConstant<NType, NClass>
/*    */ {
/*    */   public final String name;
/*    */   public final String javadoc;
/*    */   private final String lexical;
/*    */   private CEnumLeafInfo parent;
/*    */   private final Locator locator;
/*    */   
/*    */   public CEnumConstant(String name, String javadoc, String lexical, Locator loc) {
/* 66 */     assert name != null;
/* 67 */     this.name = name;
/* 68 */     this.javadoc = javadoc;
/* 69 */     this.lexical = lexical;
/* 70 */     this.locator = loc;
/*    */   }
/*    */   
/*    */   public CEnumLeafInfo getEnclosingClass() {
/* 74 */     return this.parent;
/*    */   }
/*    */   
/*    */   void setParent(CEnumLeafInfo parent) {
/* 78 */     this.parent = parent;
/*    */   }
/*    */   
/*    */   public String getLexicalValue() {
/* 82 */     return this.lexical;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 86 */     return this.name;
/*    */   }
/*    */   
/*    */   public Locator getLocator() {
/* 90 */     return this.locator;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\model\CEnumConstant.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */