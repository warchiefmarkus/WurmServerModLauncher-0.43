/*    */ package com.sun.tools.xjc.reader.gbind;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashSet;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
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
/*    */ public final class Graph
/*    */   implements Iterable<ConnectedComponent>
/*    */ {
/* 51 */   private final Element source = new SourceNode();
/* 52 */   private final Element sink = new SinkNode();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 57 */   private final List<ConnectedComponent> ccs = new ArrayList<ConnectedComponent>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Graph(Expression body) {
/* 67 */     Expression whole = new Sequence(new Sequence(this.source, body), this.sink);
/*    */ 
/*    */     
/* 70 */     whole.buildDAG(ElementSet.EMPTY_SET);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 76 */     this.source.assignDfsPostOrder(this.sink);
/* 77 */     this.source.buildStronglyConnectedComponents(this.ccs);
/*    */ 
/*    */     
/* 80 */     Set<Element> visited = new HashSet<Element>();
/* 81 */     for (ConnectedComponent cc : this.ccs) {
/* 82 */       visited.clear();
/* 83 */       if (this.source.checkCutSet(cc, visited)) {
/* 84 */         cc.isRequired = true;
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Iterator<ConnectedComponent> iterator() {
/* 93 */     return this.ccs.iterator();
/*    */   }
/*    */   
/*    */   public String toString() {
/* 97 */     return this.ccs.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\gbind\Graph.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */