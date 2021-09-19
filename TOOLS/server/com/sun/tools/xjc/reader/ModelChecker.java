/*     */ package com.sun.tools.xjc.reader;
/*     */ 
/*     */ import com.sun.tools.xjc.ErrorReceiver;
/*     */ import com.sun.tools.xjc.model.CClassInfo;
/*     */ import com.sun.tools.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.xjc.model.Model;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ModelChecker
/*     */ {
/*  59 */   private final Model model = Ring.<Model>get(Model.class);
/*  60 */   private final ErrorReceiver errorReceiver = Ring.<ErrorReceiver>get(ErrorReceiver.class);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void check() {
/*  66 */     for (CClassInfo ci : this.model.beans().values())
/*  67 */       check(ci); 
/*     */   }
/*     */   
/*     */   private void check(CClassInfo ci) {
/*  71 */     List<CPropertyInfo> props = ci.getProperties();
/*  72 */     Map<QName, CPropertyInfo> collisionTable = new HashMap<QName, CPropertyInfo>();
/*     */     
/*     */     int i;
/*  75 */     label34: for (i = 0; i < props.size(); i++) {
/*  76 */       CPropertyInfo p1 = props.get(i);
/*     */       
/*  78 */       if (p1.getName(true).equals("Class")) {
/*  79 */         this.errorReceiver.error(p1.locator, Messages.PROPERTY_CLASS_IS_RESERVED.format(new Object[0]));
/*     */       }
/*     */       else {
/*     */         
/*  83 */         QName n = p1.collectElementNames(collisionTable);
/*  84 */         if (n != null) {
/*  85 */           CPropertyInfo p2 = collisionTable.get(n);
/*  86 */           this.errorReceiver.error(p1.locator, Messages.DUPLICATE_ELEMENT.format(new Object[] { n }));
/*  87 */           this.errorReceiver.error(p2.locator, Messages.ERR_RELEVANT_LOCATION.format(new Object[0]));
/*     */         } 
/*     */         
/*  90 */         for (int j = i + 1; j < props.size(); j++) {
/*  91 */           if (checkPropertyCollision(p1, props.get(j)))
/*     */             continue label34; 
/*     */         } 
/*  94 */         for (CClassInfo c = ci.getBaseClass(); c != null; c = c.getBaseClass()) {
/*  95 */           for (CPropertyInfo p2 : c.getProperties()) {
/*  96 */             if (checkPropertyCollision(p1, p2))
/*     */               continue label34; 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   } private boolean checkPropertyCollision(CPropertyInfo p1, CPropertyInfo p2) {
/* 103 */     if (!p1.getName(true).equals(p2.getName(true)))
/* 104 */       return false; 
/* 105 */     this.errorReceiver.error(p1.locator, Messages.DUPLICATE_PROPERTY.format(new Object[] { p1.getName(true) }));
/* 106 */     this.errorReceiver.error(p2.locator, Messages.ERR_RELEVANT_LOCATION.format(new Object[0]));
/* 107 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\ModelChecker.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */