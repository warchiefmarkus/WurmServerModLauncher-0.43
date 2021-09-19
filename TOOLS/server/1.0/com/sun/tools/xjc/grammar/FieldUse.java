/*     */ package 1.0.com.sun.tools.xjc.grammar;
/*     */ 
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.tools.xjc.generator.field.FieldRendererFactory;
/*     */ import com.sun.tools.xjc.grammar.ClassItem;
/*     */ import com.sun.tools.xjc.grammar.DefaultValue;
/*     */ import com.sun.tools.xjc.grammar.FieldItem;
/*     */ import com.sun.tools.xjc.grammar.util.Multiplicity;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
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
/*     */ public final class FieldUse
/*     */ {
/*     */   public final String name;
/*     */   public final ClassItem owner;
/*     */   public final JCodeModel codeModel;
/*     */   public JType type;
/*  47 */   public final Set items = new HashSet();
/*     */   
/*     */   public Multiplicity multiplicity;
/*     */ 
/*     */   
/*     */   protected FieldUse(String name, ClassItem _owner) {
/*  53 */     this.name = name;
/*  54 */     this.owner = _owner;
/*  55 */     this.codeModel = this.owner.owner.codeModel;
/*     */   }
/*     */   
/*     */   public final JCodeModel getCodeModel() {
/*  59 */     return this.owner.owner.codeModel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FieldRendererFactory getRealization() {
/*  71 */     Iterator itr = this.items.iterator();
/*  72 */     while (itr.hasNext()) {
/*  73 */       FieldRendererFactory frf = ((FieldItem)itr.next()).realization;
/*  74 */       if (frf != null) return frf; 
/*     */     } 
/*  76 */     return null;
/*     */   }
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
/*     */   public DefaultValue[] getDefaultValues() {
/*  90 */     Iterator itr = this.items.iterator();
/*  91 */     while (itr.hasNext()) {
/*  92 */       DefaultValue[] dv = ((FieldItem)itr.next()).defaultValues;
/*  93 */       if (dv != null) return dv; 
/*     */     } 
/*  95 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getJavadoc() {
/* 105 */     StringBuffer buf = new StringBuffer();
/* 106 */     FieldItem[] items = getItems();
/* 107 */     for (int i = 0; i < items.length; i++) {
/* 108 */       if ((items[i]).javadoc != null) {
/* 109 */         if (i != 0) buf.append("\n\n"); 
/* 110 */         buf.append((items[i]).javadoc);
/*     */       } 
/* 112 */     }  return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isUnboxable() {
/* 124 */     FieldItem[] items = getItems();
/* 125 */     for (int i = 0; i < items.length; i++) {
/* 126 */       if (!items[i].isUnboxable(this.codeModel))
/* 127 */         return false; 
/*     */     } 
/* 129 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDelegated() {
/* 138 */     FieldItem[] items = getItems();
/* 139 */     for (int i = 0; i < items.length; i++) {
/* 140 */       if (items[i].isDelegated())
/* 141 */         return true; 
/* 142 */     }  return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void disableDelegation() {
/* 149 */     FieldItem[] items = getItems();
/* 150 */     for (int i = 0; i < items.length; i++) {
/* 151 */       items[i].setDelegation(false);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FieldItem[] getItems() {
/* 159 */     return (FieldItem[])this.items.toArray((Object[])new FieldItem[0]);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\grammar\FieldUse.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */