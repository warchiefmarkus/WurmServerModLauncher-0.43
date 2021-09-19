/*    */ package com.wurmonline.server.creatures.ai;
/*    */ 
/*    */ import java.util.LinkedList;
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
/*    */ public final class Path
/*    */ {
/*    */   private LinkedList<PathTile> path;
/*    */   
/*    */   Path() {
/* 34 */     this.path = new LinkedList<>();
/*    */   }
/*    */ 
/*    */   
/*    */   public Path(LinkedList<PathTile> pathlist) {
/* 39 */     this.path = pathlist;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PathTile getFirst() {
/* 47 */     return this.path.getFirst();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PathTile getTargetTile() {
/* 55 */     return this.path.getLast();
/*    */   }
/*    */ 
/*    */   
/*    */   public int getSize() {
/* 60 */     return this.path.size();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void removeFirst() {
/* 68 */     this.path.removeFirst();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isEmpty() {
/* 76 */     return (this.path == null || this.path.isEmpty());
/*    */   }
/*    */ 
/*    */   
/*    */   public LinkedList<PathTile> getPathTiles() {
/* 81 */     return this.path;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void clear() {
/* 91 */     if (this.path != null)
/*    */     {
/* 93 */       this.path.clear();
/*    */     }
/* 95 */     this.path = null;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\ai\Path.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */