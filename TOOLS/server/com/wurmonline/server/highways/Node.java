/*     */ package com.wurmonline.server.highways;
/*     */ 
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.villages.Village;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
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
/*     */ public class Node
/*     */   extends AStarNode
/*     */ {
/*     */   private final Item waystone;
/*  40 */   private Village village = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  45 */   final ConcurrentHashMap<Byte, Route> routes = new ConcurrentHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  50 */   final ConcurrentHashMap<Byte, AStarNode> neighbours = new ConcurrentHashMap<>();
/*     */ 
/*     */ 
/*     */   
/*  54 */   final ConcurrentHashMap<Byte, ClosestVillage> pointers = new ConcurrentHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node(Item waystone) {
/*  62 */     this.waystone = waystone;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getWurmId() {
/*  67 */     return this.waystone.getWurmId();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Item getWaystone() {
/*  77 */     return this.waystone;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVillage(Village village) {
/*  87 */     this.village = village;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRouteCount() {
/*  97 */     return this.routes.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Village getVillage() {
/* 107 */     return this.village;
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
/*     */   public void AddRoute(byte direction, Route route) {
/* 120 */     this.routes.put(Byte.valueOf(direction), route);
/*     */     
/* 122 */     this.neighbours.put(Byte.valueOf(direction), route.getEndNode());
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
/*     */   public void addClosestVillage(byte direction, String name, short distance) {
/* 136 */     this.pointers.put(Byte.valueOf(direction), new ClosestVillage(name, distance));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ClosestVillage getClosestVillage(byte direction) {
/* 148 */     return this.pointers.get(Byte.valueOf(direction));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Route getRoute(byte direction) {
/* 160 */     return this.routes.get(Byte.valueOf(direction));
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
/*     */   public byte getNodeDir(Node node) {
/* 172 */     byte bestdir = 0;
/* 173 */     float bestCost = 99999.0F;
/*     */     
/* 175 */     for (Map.Entry<Byte, Route> entry : this.routes.entrySet()) {
/*     */       
/* 177 */       Route route = entry.getValue();
/* 178 */       byte dir = ((Byte)entry.getKey()).byteValue();
/* 179 */       if (route.getEndNode() == node || route.getStartNode() == node)
/*     */       {
/* 181 */         if (route.getCost() < bestCost) {
/*     */           
/* 183 */           bestCost = route.getCost();
/* 184 */           bestdir = dir;
/*     */         } 
/*     */       }
/*     */     } 
/* 188 */     return bestdir;
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
/*     */   public boolean removeRoute(Route oldRoute) {
/* 200 */     for (Map.Entry<Byte, Route> entry : this.routes.entrySet()) {
/*     */       
/* 202 */       if (entry.getValue() == oldRoute)
/*     */       {
/* 204 */         this.routes.remove(entry.getKey());
/*     */       }
/*     */     } 
/* 207 */     for (Map.Entry<Byte, AStarNode> entry : this.neighbours.entrySet()) {
/*     */       
/* 209 */       if (entry.getValue() == oldRoute.getEndNode())
/*     */       {
/* 211 */         this.neighbours.remove(entry.getKey());
/*     */       }
/*     */     } 
/*     */     
/* 215 */     return (this.village == null && this.routes.isEmpty());
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
/*     */   public float getCost(AStarNode node) {
/* 230 */     Route route = findRoute(node);
/* 231 */     if (route != null)
/* 232 */       return route.getCost(); 
/* 233 */     return 99999.0F;
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
/*     */   public float getDistance(AStarNode node) {
/* 245 */     Route route = findRoute(node);
/* 246 */     if (route != null)
/* 247 */       return route.getDistance(); 
/* 248 */     return 99999.0F;
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
/*     */   public float getEstimatedCost(AStarNode node) {
/* 261 */     Route route = findRoute(node);
/* 262 */     if (route != null) {
/*     */       
/* 264 */       int diffx = Math.abs(this.waystone.getTileX() - ((Node)node).waystone.getTileX());
/* 265 */       int diffy = Math.abs(this.waystone.getTileY() - ((Node)node).waystone.getTileY());
/* 266 */       return (diffx + diffy);
/*     */     } 
/* 268 */     return 99999.0F;
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
/*     */   @Nullable
/*     */   private Route findRoute(AStarNode node) {
/* 281 */     for (Map.Entry<Byte, AStarNode> entry : this.neighbours.entrySet()) {
/*     */       
/* 283 */       if (entry.getValue() == node)
/*     */       {
/* 285 */         return this.routes.get(entry.getKey());
/*     */       }
/*     */     } 
/* 288 */     return null;
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
/*     */   public List<AStarNode> getNeighbours(byte dir) {
/* 302 */     ArrayList<AStarNode> alist = new ArrayList<>();
/* 303 */     if (dir != 0) {
/*     */       
/* 305 */       Route route = getRoute(dir);
/* 306 */       if (route != null && route.getEndNode() != null)
/*     */       {
/* 308 */         alist.add(this.neighbours.get(Byte.valueOf(dir)));
/*     */       
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 314 */       for (Map.Entry<Byte, AStarNode> entry : this.neighbours.entrySet()) {
/*     */         
/* 316 */         if (!alist.contains(entry.getValue()))
/* 317 */           alist.add(entry.getValue()); 
/*     */       } 
/*     */     } 
/* 320 */     return alist;
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
/*     */   public ConcurrentHashMap<Byte, Route> getRoutes(byte dir) {
/* 333 */     if (dir != 0) {
/*     */       
/* 335 */       ConcurrentHashMap<Byte, Route> lroutes = new ConcurrentHashMap<>();
/* 336 */       Route route = this.routes.get(Byte.valueOf(dir));
/* 337 */       if (route != null)
/* 338 */         lroutes.put(Byte.valueOf(dir), route); 
/* 339 */       return lroutes;
/*     */     } 
/*     */ 
/*     */     
/* 343 */     return this.routes;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 349 */     StringBuilder buf = new StringBuilder();
/* 350 */     buf.append("{Node:" + this.waystone.getWurmId());
/* 351 */     boolean first = true;
/* 352 */     for (Map.Entry<Byte, Route> entry : this.routes.entrySet()) {
/*     */       
/* 354 */       if (first) {
/*     */         
/* 356 */         first = false;
/* 357 */         buf.append("{");
/*     */       } else {
/*     */         
/* 360 */         buf.append(",");
/* 361 */       }  buf.append(" {Dir:");
/* 362 */       buf.append(MethodsHighways.getLinkDirString(((Byte)entry.getKey()).byteValue()));
/* 363 */       buf.append(",Cost:");
/* 364 */       buf.append(((Route)entry.getValue()).getCost());
/* 365 */       buf.append(",Route:");
/* 366 */       buf.append(((Route)entry.getValue()).getId());
/*     */       
/* 368 */       buf.append("}");
/*     */     } 
/* 370 */     if (!first)
/* 371 */       buf.append("}"); 
/* 372 */     buf.append("}");
/* 373 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\highways\Node.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */