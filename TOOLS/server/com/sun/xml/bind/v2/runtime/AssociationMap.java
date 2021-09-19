/*     */ package com.sun.xml.bind.v2.runtime;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.Map;
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
/*     */ public final class AssociationMap<XmlNode>
/*     */ {
/*     */   static final class Entry<XmlNode>
/*     */   {
/*     */     private XmlNode element;
/*     */     private Object inner;
/*     */     private Object outer;
/*     */     
/*     */     public XmlNode element() {
/*  65 */       return this.element;
/*     */     }
/*     */     public Object inner() {
/*  68 */       return this.inner;
/*     */     }
/*     */     public Object outer() {
/*  71 */       return this.outer;
/*     */     }
/*     */   }
/*     */   
/*  75 */   private final Map<XmlNode, Entry<XmlNode>> byElement = new IdentityHashMap<XmlNode, Entry<XmlNode>>();
/*  76 */   private final Map<Object, Entry<XmlNode>> byPeer = new IdentityHashMap<Object, Entry<XmlNode>>();
/*  77 */   private final Set<XmlNode> usedNodes = new HashSet<XmlNode>();
/*     */ 
/*     */   
/*     */   public void addInner(XmlNode element, Object inner) {
/*  81 */     Entry<XmlNode> e = this.byElement.get(element);
/*  82 */     if (e != null) {
/*  83 */       if (e.inner != null)
/*  84 */         this.byPeer.remove(e.inner); 
/*  85 */       e.inner = inner;
/*     */     } else {
/*  87 */       e = new Entry<XmlNode>();
/*  88 */       e.element = element;
/*  89 */       e.inner = inner;
/*     */     } 
/*     */     
/*  92 */     this.byElement.put(element, e);
/*     */     
/*  94 */     Entry<XmlNode> old = this.byPeer.put(inner, e);
/*  95 */     if (old != null) {
/*  96 */       if (old.outer != null)
/*  97 */         this.byPeer.remove(old.outer); 
/*  98 */       if (old.element != null) {
/*  99 */         this.byElement.remove(old.element);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addOuter(XmlNode element, Object outer) {
/* 105 */     Entry<XmlNode> e = this.byElement.get(element);
/* 106 */     if (e != null) {
/* 107 */       if (e.outer != null)
/* 108 */         this.byPeer.remove(e.outer); 
/* 109 */       e.outer = outer;
/*     */     } else {
/* 111 */       e = new Entry<XmlNode>();
/* 112 */       e.element = element;
/* 113 */       e.outer = outer;
/*     */     } 
/*     */     
/* 116 */     this.byElement.put(element, e);
/*     */     
/* 118 */     Entry<XmlNode> old = this.byPeer.put(outer, e);
/* 119 */     if (old != null) {
/* 120 */       old.outer = null;
/*     */       
/* 122 */       if (old.inner == null)
/*     */       {
/* 124 */         this.byElement.remove(old.element); } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addUsed(XmlNode n) {
/* 129 */     this.usedNodes.add(n);
/*     */   }
/*     */   
/*     */   public Entry<XmlNode> byElement(Object e) {
/* 133 */     return this.byElement.get(e);
/*     */   }
/*     */   
/*     */   public Entry<XmlNode> byPeer(Object o) {
/* 137 */     return this.byPeer.get(o);
/*     */   }
/*     */   
/*     */   public Object getInnerPeer(XmlNode element) {
/* 141 */     Entry<XmlNode> e = byElement(element);
/* 142 */     if (e == null) return null; 
/* 143 */     return e.inner;
/*     */   }
/*     */   
/*     */   public Object getOuterPeer(XmlNode element) {
/* 147 */     Entry<XmlNode> e = byElement(element);
/* 148 */     if (e == null) return null; 
/* 149 */     return e.outer;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\AssociationMap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */