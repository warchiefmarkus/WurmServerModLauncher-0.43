/*     */ package org.apache.http.params;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.http.annotation.NotThreadSafe;
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
/*     */ @NotThreadSafe
/*     */ public class BasicHttpParams
/*     */   extends AbstractHttpParams
/*     */   implements Serializable, Cloneable
/*     */ {
/*     */   private static final long serialVersionUID = -7086398485908701455L;
/*  54 */   private final HashMap<String, Object> parameters = new HashMap<String, Object>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getParameter(String name) {
/*  61 */     return this.parameters.get(name);
/*     */   }
/*     */   
/*     */   public HttpParams setParameter(String name, Object value) {
/*  65 */     this.parameters.put(name, value);
/*  66 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeParameter(String name) {
/*  71 */     if (this.parameters.containsKey(name)) {
/*  72 */       this.parameters.remove(name);
/*  73 */       return true;
/*     */     } 
/*  75 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParameters(String[] names, Object value) {
/*  86 */     for (int i = 0; i < names.length; i++) {
/*  87 */       setParameter(names[i], value);
/*     */     }
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
/*     */   
/*     */   public boolean isParameterSet(String name) {
/* 103 */     return (getParameter(name) != null);
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
/*     */   public boolean isParameterSetLocally(String name) {
/* 117 */     return (this.parameters.get(name) != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 124 */     this.parameters.clear();
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
/*     */   @Deprecated
/*     */   public HttpParams copy() {
/*     */     try {
/* 140 */       return (HttpParams)clone();
/* 141 */     } catch (CloneNotSupportedException ex) {
/* 142 */       throw new UnsupportedOperationException("Cloning not supported");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object clone() throws CloneNotSupportedException {
/* 152 */     BasicHttpParams clone = (BasicHttpParams)super.clone();
/* 153 */     copyParams(clone);
/* 154 */     return clone;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void copyParams(HttpParams target) {
/* 165 */     Iterator<Map.Entry<String, Object>> iter = this.parameters.entrySet().iterator();
/* 166 */     while (iter.hasNext()) {
/* 167 */       Map.Entry<String, Object> me = iter.next();
/* 168 */       if (me.getKey() instanceof String) {
/* 169 */         target.setParameter(me.getKey(), me.getValue());
/*     */       }
/*     */     } 
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
/*     */   public Set<String> getNames() {
/* 184 */     return new HashSet<String>(this.parameters.keySet());
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\params\BasicHttpParams.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */