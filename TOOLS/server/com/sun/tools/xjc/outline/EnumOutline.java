/*    */ package com.sun.tools.xjc.outline;
/*    */ 
/*    */ import com.sun.codemodel.JDefinedClass;
/*    */ import com.sun.istack.NotNull;
/*    */ import com.sun.tools.xjc.model.CEnumLeafInfo;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class EnumOutline
/*    */ {
/*    */   public final CEnumLeafInfo target;
/*    */   public final JDefinedClass clazz;
/* 69 */   public final List<EnumConstantOutline> constants = new ArrayList<EnumConstantOutline>();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public PackageOutline _package() {
/* 76 */     return parent().getPackageContext(this.clazz._package());
/*    */   }
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public abstract Outline parent();
/*    */ 
/*    */   
/*    */   protected EnumOutline(CEnumLeafInfo target, JDefinedClass clazz) {
/* 85 */     this.target = target;
/* 86 */     this.clazz = clazz;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\outline\EnumOutline.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */