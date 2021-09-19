/*     */ package com.wurmonline.server.highways;
/*     */ 
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.Wagoner;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.villages.Village;
/*     */ import com.wurmonline.shared.constants.HighwayConstants;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PathToCalculate
/*     */   implements HighwayConstants
/*     */ {
/*     */   private final Creature creature;
/*     */   private final Node startNode;
/*     */   private final Village destinationVillage;
/*  47 */   private List<Route> bestPath = null;
/*  48 */   private byte checkDir = 0;
/*  49 */   private float bestDistance = 99999.0F;
/*  50 */   private float bestCost = 99999.0F;
/*     */ 
/*     */   
/*     */   PathToCalculate(@Nullable Creature creature, Node startNode, @Nullable Village village, byte checkDir) {
/*  54 */     this.creature = creature;
/*  55 */     this.startNode = startNode;
/*  56 */     this.destinationVillage = village;
/*  57 */     this.checkDir = checkDir;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   class Distanced
/*     */   {
/*     */     private final Village village;
/*     */ 
/*     */     
/*     */     private final int distance;
/*     */ 
/*     */ 
/*     */     
/*     */     Distanced(Village village, int distance) {
/*  72 */       this.village = village;
/*  73 */       this.distance = distance;
/*     */     }
/*     */ 
/*     */     
/*     */     Village getVillage() {
/*  78 */       return this.village;
/*     */     }
/*     */ 
/*     */     
/*     */     int getDistance() {
/*  83 */       return this.distance;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void calculate() {
/*  93 */     if (this.destinationVillage == null) {
/*     */ 
/*     */ 
/*     */       
/*  97 */       Village[] villages = Routes.getVillages();
/*  98 */       HashSet<Distanced> distanceSet = new HashSet<>();
/*  99 */       for (Village village : villages) {
/*     */         
/* 101 */         if (village != this.startNode.getVillage()) {
/*     */ 
/*     */ 
/*     */           
/* 105 */           int dx = this.startNode.getWaystone().getTileX() - village.getTokenX();
/* 106 */           int dy = this.startNode.getWaystone().getTileY() - village.getTokenY();
/* 107 */           int crowfly = (int)Math.sqrt((dx * dx + dy * dy));
/* 108 */           distanceSet.add(new Distanced(village, crowfly));
/*     */         } 
/*     */       } 
/*     */       
/* 112 */       if (distanceSet.size() == 0) {
/*     */         return;
/*     */       }
/* 115 */       Distanced[] distanced = (Distanced[])distanceSet.toArray((Object[])new Distanced[distanceSet.size()]);
/*     */       
/* 117 */       Arrays.sort(distanced, new Comparator<Distanced>()
/*     */           {
/*     */             
/*     */             public int compare(PathToCalculate.Distanced o1, PathToCalculate.Distanced o2)
/*     */             {
/* 122 */               return Integer.compare(o1.distance, o2.distance);
/*     */             }
/*     */           });
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 129 */       if (this.checkDir == 0) {
/*     */         
/* 131 */         calcClosest(distanced, (byte)1);
/* 132 */         calcClosest(distanced, (byte)2);
/* 133 */         calcClosest(distanced, (byte)4);
/* 134 */         calcClosest(distanced, (byte)8);
/* 135 */         calcClosest(distanced, (byte)16);
/* 136 */         calcClosest(distanced, (byte)32);
/* 137 */         calcClosest(distanced, (byte)64);
/* 138 */         calcClosest(distanced, -128);
/*     */       } else {
/*     */         
/* 141 */         calcClosest(distanced, this.checkDir);
/*     */       } 
/* 143 */       this.startNode.getWaystone().updateModelNameOnGroundItem();
/*     */     } else {
/*     */       
/* 146 */       calculate(this.destinationVillage, this.checkDir);
/*     */     } 
/*     */   }
/*     */   
/*     */   void calcClosest(Distanced[] distanced, byte dir) {
/* 151 */     Route route = this.startNode.getRoute(dir);
/* 152 */     if (route == null)
/*     */       return; 
/* 154 */     List<Route> closestPath = null;
/* 155 */     float closestDistance = 99999.0F;
/* 156 */     float closestCost = 99999.0F;
/* 157 */     Village closestVillage = null;
/* 158 */     for (Distanced distance : distanced) {
/*     */       
/* 160 */       if (distance.getDistance() > closestDistance)
/*     */         break; 
/* 162 */       calculate(distance.getVillage(), dir);
/*     */       
/* 164 */       if (this.bestPath != null && this.bestCost < closestCost) {
/*     */         
/* 166 */         closestPath = this.bestPath;
/* 167 */         closestDistance = this.bestDistance;
/* 168 */         closestCost = this.bestCost;
/* 169 */         closestVillage = distance.getVillage();
/*     */       } 
/*     */     } 
/*     */     
/* 173 */     if (closestPath != null) {
/*     */ 
/*     */       
/* 176 */       short distanceTiles = (short)(int)closestDistance;
/* 177 */       this.startNode.addClosestVillage(dir, closestVillage.getName(), distanceTiles);
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 182 */       Node endNode = route.getEndNode();
/* 183 */       Item waystone = endNode.getWaystone();
/* 184 */       String wagonerName = Wagoner.getWagonerNameFrom(waystone.getWurmId());
/* 185 */       if (wagonerName.length() > 0) {
/* 186 */         this.startNode.addClosestVillage(dir, wagonerName, (short)(int)route.getDistance());
/*     */       } else {
/* 188 */         this.startNode.addClosestVillage(dir, "", (short)0);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   void calculate(Village village, byte initialDir) {
/* 194 */     this.bestPath = null;
/* 195 */     this.bestCost = 99999.0F;
/* 196 */     this.bestDistance = 99999.0F;
/* 197 */     int pno = 1;
/* 198 */     int bestno = 0;
/* 199 */     for (Node goalNode : Routes.getNodesFor(village)) {
/*     */ 
/*     */       
/* 202 */       List<Route> path = AStarSearch.findPath(this.startNode, goalNode, initialDir);
/*     */       
/* 204 */       if (path != null && !path.isEmpty()) {
/*     */ 
/*     */ 
/*     */         
/* 208 */         float cost = 0.0F;
/* 209 */         float distance = 0.0F;
/* 210 */         for (Route route : path) {
/*     */           
/* 212 */           float ncost = route.getCost();
/* 213 */           cost += ncost;
/* 214 */           float ndistance = route.getDistance();
/* 215 */           distance += ndistance;
/*     */         } 
/* 217 */         if (this.bestPath == null || cost < this.bestCost) {
/*     */           
/* 219 */           this.bestCost = cost;
/* 220 */           this.bestPath = path;
/* 221 */           bestno = pno;
/* 222 */           this.bestDistance = distance;
/*     */         } 
/* 224 */         pno++;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 229 */     if (this.creature != null) {
/*     */       
/* 231 */       String oldDestination = this.creature.getHighwayPathDestination();
/* 232 */       this.creature.setHighwayPath(village.getName(), this.bestPath);
/*     */       
/* 234 */       if (this.bestPath != null && !this.bestPath.isEmpty()) {
/* 235 */         this.creature.setLastWaystoneChecked(((Route)this.bestPath.get(0)).getStartNode().getWaystone().getWurmId());
/*     */       } else {
/* 237 */         this.creature.setLastWaystoneChecked(-10L);
/*     */       } 
/* 239 */       if (this.creature.isPlayer())
/*     */       {
/*     */         
/* 242 */         if (this.creature.getPower() > 1) {
/* 243 */           Routes.queuePlayerMessage((Player)this.creature, pno + " route" + ((pno != 1) ? "s" : "") + " checked." + ((bestno > 0) ? (" Best route found was number " + bestno + " and its cost is " + this.bestCost + ".") : " No routes found!"));
/*     */         
/*     */         }
/* 246 */         else if (this.bestPath == null) {
/* 247 */           Routes.queuePlayerMessage((Player)this.creature, "No routes found to " + village.getName() + "!");
/* 248 */         } else if (!oldDestination.equals(village.getName())) {
/*     */           
/* 250 */           Routes.queuePlayerMessage((Player)this.creature, "Route found to " + village.getName() + "!");
/*     */         } 
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
/*     */   public static final List<Route> getRoute(long startWaystoneId, long endWaystoneId) {
/* 266 */     Node startNode = Routes.getNode(startWaystoneId);
/* 267 */     Node endNode = Routes.getNode(endWaystoneId);
/*     */     
/* 269 */     if (startNode == null || endNode == null) {
/* 270 */       return null;
/*     */     }
/* 272 */     List<Route> path = AStarSearch.findPath(startNode, endNode, (byte)0);
/* 273 */     if (path != null && !path.isEmpty())
/* 274 */       return path; 
/* 275 */     return null;
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
/*     */   public static final float getRouteDistance(long startWaystoneId, long endWaystoneId) {
/* 288 */     List<Route> path = getRoute(startWaystoneId, endWaystoneId);
/*     */     
/* 290 */     if (path != null) {
/*     */ 
/*     */       
/* 293 */       float distance = 0.0F;
/* 294 */       for (Route route : path)
/*     */       {
/* 296 */         distance += route.getDistance();
/*     */       }
/* 298 */       return distance;
/*     */     } 
/* 300 */     return 99999.0F;
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
/*     */   public static final boolean isVillageConnected(long startWaystoneId, Village village) {
/* 313 */     Node startNode = Routes.getNode(startWaystoneId);
/* 314 */     if (startNode == null)
/* 315 */       return false; 
/* 316 */     for (Node goalNode : Routes.getNodesFor(village)) {
/*     */ 
/*     */       
/* 319 */       List<Route> path = AStarSearch.findPath(startNode, goalNode, (byte)0);
/*     */       
/* 321 */       if (path != null && !path.isEmpty())
/*     */       {
/* 323 */         return true;
/*     */       }
/*     */     } 
/* 326 */     return false;
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
/*     */   public static final boolean isWaystoneConnected(long startWaystoneId, long endWaystoneId) {
/* 339 */     Node startNode = Routes.getNode(startWaystoneId);
/* 340 */     if (startNode == null)
/* 341 */       return false; 
/* 342 */     Node endNode = Routes.getNode(endWaystoneId);
/* 343 */     if (endNode == null) {
/* 344 */       return false;
/*     */     }
/* 346 */     List<Route> path = AStarSearch.findPath(startNode, endNode, (byte)0);
/*     */     
/* 348 */     if (path != null && !path.isEmpty())
/*     */     {
/* 350 */       return true;
/*     */     }
/* 352 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\highways\PathToCalculate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */