/*     */ package com.wurmonline.server.highways;
/*     */ 
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AStarSearch
/*     */ {
/*     */   public static class PriorityList
/*     */     extends LinkedList<AStarNode>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     public void add(Comparable<AStarNode> object) {
/*  54 */       for (int i = 0; i < size(); i++) {
/*     */         
/*  56 */         if (object.compareTo(get(i)) <= 0) {
/*     */           
/*  58 */           add(i, (AStarNode)object);
/*     */           return;
/*     */         } 
/*     */       } 
/*  62 */       addLast((AStarNode)object);
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
/*     */   protected static List<Route> constructPath(AStarNode startNode, AStarNode node) {
/*  74 */     LinkedList<Route> path = new LinkedList<>();
/*  75 */     LinkedList<AStarNode> nodes = new LinkedList<>();
/*  76 */     while (node.pathParent != null) {
/*     */ 
/*     */       
/*  79 */       if (nodes.contains(node))
/*     */       {
/*  81 */         return null;
/*     */       }
/*  83 */       nodes.addFirst(node);
/*  84 */       path.addFirst(node.pathRoute);
/*  85 */       node = node.pathParent;
/*     */     } 
/*  87 */     return path;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<Route> findPath(AStarNode startNode, AStarNode goalNode, byte initialDir) {
/*  98 */     PriorityList openList = new PriorityList();
/*  99 */     LinkedList<AStarNode> closedList = new LinkedList<>();
/*     */     
/* 101 */     startNode.costFromStart = 0.0F;
/* 102 */     startNode.estimatedCostToGoal = startNode.getEstimatedCost(goalNode);
/* 103 */     startNode.pathParent = null;
/* 104 */     startNode.pathRoute = null;
/* 105 */     openList.add(startNode);
/* 106 */     byte checkDir = initialDir;
/*     */     
/* 108 */     while (!openList.isEmpty()) {
/*     */       
/* 110 */       AStarNode node = openList.removeFirst();
/* 111 */       if (node == goalNode)
/*     */       {
/*     */         
/* 114 */         return constructPath(startNode, goalNode);
/*     */       }
/* 116 */       ConcurrentHashMap<Byte, Route> routesMap = node.getRoutes(checkDir);
/* 117 */       for (Map.Entry<Byte, Route> entry : routesMap.entrySet()) {
/*     */         
/* 119 */         Route route = entry.getValue();
/* 120 */         AStarNode neighbourNode = route.getEndNode();
/* 121 */         boolean isOpen = openList.contains(neighbourNode);
/* 122 */         boolean isClosed = closedList.contains(neighbourNode);
/* 123 */         float costFromStart = node.costFromStart + route.getCost();
/* 124 */         if ((!isOpen && !isClosed) || costFromStart < neighbourNode.costFromStart) {
/*     */           
/* 126 */           neighbourNode.pathParent = node;
/* 127 */           neighbourNode.costFromStart = costFromStart;
/* 128 */           neighbourNode.estimatedCostToGoal = neighbourNode.getEstimatedCost(goalNode);
/* 129 */           neighbourNode.pathRoute = route;
/*     */           
/* 131 */           if (isClosed)
/*     */           {
/* 133 */             closedList.remove(neighbourNode);
/*     */           }
/* 135 */           if (!isOpen)
/*     */           {
/* 137 */             openList.add(neighbourNode);
/*     */           }
/*     */         } 
/*     */       } 
/* 141 */       closedList.add(node);
/*     */       
/* 143 */       checkDir = 0;
/*     */     } 
/*     */     
/* 146 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\highways\AStarSearch.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */