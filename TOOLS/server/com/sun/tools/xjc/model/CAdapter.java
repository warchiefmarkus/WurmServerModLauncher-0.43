/*     */ package com.sun.tools.xjc.model;
/*     */ 
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.tools.xjc.model.nav.EagerNClass;
/*     */ import com.sun.tools.xjc.model.nav.NClass;
/*     */ import com.sun.tools.xjc.model.nav.NType;
/*     */ import com.sun.tools.xjc.model.nav.NavigatorImpl;
/*     */ import com.sun.tools.xjc.outline.Aspect;
/*     */ import com.sun.tools.xjc.outline.Outline;
/*     */ import com.sun.xml.bind.v2.model.core.Adapter;
/*     */ import com.sun.xml.bind.v2.model.nav.Navigator;
/*     */ import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
/*     */ import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
/*     */ import javax.xml.bind.annotation.adapters.XmlAdapter;
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
/*     */ public final class CAdapter
/*     */   extends Adapter<NType, NClass>
/*     */ {
/*     */   private JClass adapterClass1;
/*     */   private Class<? extends XmlAdapter> adapterClass2;
/*     */   
/*     */   public CAdapter(Class<? extends XmlAdapter> adapter, boolean copy) {
/*  78 */     super(getRef(adapter, copy), (Navigator)NavigatorImpl.theInstance);
/*  79 */     this.adapterClass1 = null;
/*  80 */     this.adapterClass2 = adapter;
/*     */   }
/*     */   
/*     */   static NClass getRef(final Class<? extends XmlAdapter> adapter, boolean copy) {
/*  84 */     if (copy)
/*     */     {
/*     */       
/*  87 */       return (NClass)new EagerNClass(adapter)
/*     */         {
/*     */           public JClass toType(Outline o, Aspect aspect) {
/*  90 */             return o.addRuntime(adapter);
/*     */           }
/*     */           
/*     */           public String fullName() {
/*  94 */             throw new UnsupportedOperationException();
/*     */           }
/*     */         };
/*     */     }
/*  98 */     return NavigatorImpl.theInstance.ref(adapter);
/*     */   }
/*     */ 
/*     */   
/*     */   public CAdapter(JClass adapter) {
/* 103 */     super(NavigatorImpl.theInstance.ref(adapter), (Navigator)NavigatorImpl.theInstance);
/* 104 */     this.adapterClass1 = adapter;
/* 105 */     this.adapterClass2 = null;
/*     */   }
/*     */   
/*     */   public JClass getAdapterClass(Outline o) {
/* 109 */     if (this.adapterClass1 == null)
/* 110 */       this.adapterClass1 = o.getCodeModel().ref(this.adapterClass2); 
/* 111 */     return ((NClass)this.adapterType).toType(o, Aspect.EXPOSED);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isWhitespaceAdapter() {
/* 119 */     return (this.adapterClass2 == CollapsedStringAdapter.class || this.adapterClass2 == NormalizedStringAdapter.class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<? extends XmlAdapter> getAdapterIfKnown() {
/* 128 */     return this.adapterClass2;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\model\CAdapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */