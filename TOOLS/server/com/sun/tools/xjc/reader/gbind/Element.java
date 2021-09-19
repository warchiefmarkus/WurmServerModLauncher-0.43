/*     */ package com.sun.tools.xjc.reader.gbind;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Element
/*     */   extends Expression
/*     */   implements ElementSet
/*     */ {
/*  65 */   final Set<Element> foreEdges = new LinkedHashSet<Element>();
/*  66 */   final Set<Element> backEdges = new LinkedHashSet<Element>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Element prevPostOrder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ConnectedComponent cc;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ElementSet lastSet() {
/*  91 */     return this;
/*     */   }
/*     */   
/*     */   boolean isNullable() {
/*  95 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isSource() {
/* 102 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isSink() {
/* 109 */     return false;
/*     */   }
/*     */   
/*     */   void buildDAG(ElementSet incoming) {
/* 113 */     incoming.addNext(this);
/*     */   }
/*     */   
/*     */   public void addNext(Element element) {
/* 117 */     this.foreEdges.add(element);
/* 118 */     element.backEdges.add(this);
/*     */   }
/*     */   
/*     */   public boolean contains(ElementSet rhs) {
/* 122 */     return (this == rhs || rhs == ElementSet.EMPTY_SET);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<Element> iterator() {
/* 132 */     return Collections.<Element>singleton(this).iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Element assignDfsPostOrder(Element prev) {
/* 142 */     if (this.prevPostOrder != null) {
/* 143 */       return prev;
/*     */     }
/* 145 */     this.prevPostOrder = this;
/*     */     
/* 147 */     for (Element next : this.foreEdges) {
/* 148 */       prev = next.assignDfsPostOrder(prev);
/*     */     }
/* 150 */     this.prevPostOrder = prev;
/* 151 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void buildStronglyConnectedComponents(List<ConnectedComponent> ccs) {
/* 159 */     for (Element cur = this; cur != cur.prevPostOrder; cur = cur.prevPostOrder) {
/* 160 */       if (!cur.belongsToSCC()) {
/*     */ 
/*     */ 
/*     */         
/* 164 */         ConnectedComponent cc = new ConnectedComponent();
/* 165 */         ccs.add(cc);
/*     */         
/* 167 */         cur.formConnectedComponent(cc);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private boolean belongsToSCC() {
/* 172 */     return (this.cc != null || isSource() || isSink());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void formConnectedComponent(ConnectedComponent group) {
/* 179 */     if (belongsToSCC()) {
/*     */       return;
/*     */     }
/* 182 */     this.cc = group;
/* 183 */     group.add(this);
/* 184 */     for (Element prev : this.backEdges) {
/* 185 */       prev.formConnectedComponent(group);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean hasSelfLoop() {
/* 190 */     assert this.foreEdges.contains(this) == this.backEdges.contains(this);
/*     */     
/* 192 */     return this.foreEdges.contains(this);
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
/*     */   final boolean checkCutSet(ConnectedComponent cc, Set<Element> visited) {
/* 205 */     assert belongsToSCC();
/*     */     
/* 207 */     if (isSink())
/*     */     {
/*     */       
/* 210 */       return false;
/*     */     }
/* 212 */     if (!visited.add(this)) {
/* 213 */       return true;
/*     */     }
/* 215 */     if (this.cc == cc) {
/* 216 */       return true;
/*     */     }
/* 218 */     for (Element next : this.foreEdges) {
/* 219 */       if (!next.checkCutSet(cc, visited))
/*     */       {
/* 221 */         return false;
/*     */       }
/*     */     } 
/* 224 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\gbind\Element.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */