/*     */ package com.sun.tools.xjc.model;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
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
/*     */ public final class CCustomizations
/*     */   extends ArrayList<CPluginCustomization>
/*     */ {
/*     */   CCustomizations next;
/*     */   private CCustomizable owner;
/*     */   
/*     */   public CCustomizations() {}
/*     */   
/*     */   public CCustomizations(Collection<? extends CPluginCustomization> cPluginCustomizations) {
/*  75 */     super(cPluginCustomizations);
/*     */   }
/*     */   
/*     */   void setParent(Model model, CCustomizable owner) {
/*  79 */     if (this.owner != null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  85 */     this.next = model.customizations;
/*  86 */     model.customizations = this;
/*  87 */     assert owner != null;
/*  88 */     this.owner = owner;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CCustomizable getOwner() {
/*  97 */     assert this.owner != null;
/*  98 */     return this.owner;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CPluginCustomization find(String nsUri) {
/* 106 */     for (CPluginCustomization p : this) {
/* 107 */       if (fixNull(p.element.getNamespaceURI()).equals(nsUri))
/* 108 */         return p; 
/*     */     } 
/* 110 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CPluginCustomization find(String nsUri, String localName) {
/* 118 */     for (CPluginCustomization p : this) {
/* 119 */       if (fixNull(p.element.getNamespaceURI()).equals(nsUri) && fixNull(p.element.getLocalName()).equals(localName))
/*     */       {
/* 121 */         return p; } 
/*     */     } 
/* 123 */     return null;
/*     */   }
/*     */   
/*     */   private String fixNull(String s) {
/* 127 */     if (s == null) return ""; 
/* 128 */     return s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 134 */   public static final CCustomizations EMPTY = new CCustomizations();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CCustomizations merge(CCustomizations lhs, CCustomizations rhs) {
/* 140 */     if (lhs == null || lhs.isEmpty()) return rhs; 
/* 141 */     if (rhs == null || rhs.isEmpty()) return lhs;
/*     */     
/* 143 */     CCustomizations r = new CCustomizations(lhs);
/* 144 */     r.addAll(rhs);
/* 145 */     return r;
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/* 149 */     return (this == o);
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 153 */     return System.identityHashCode(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\model\CCustomizations.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */