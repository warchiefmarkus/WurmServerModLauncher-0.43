/*     */ package com.wurmonline.server.highways;
/*     */ 
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import java.util.LinkedList;
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
/*     */ 
/*     */ 
/*     */ public class Route
/*     */ {
/*     */   private final Node startNode;
/*     */   private final byte direction;
/*     */   private final int id;
/*  42 */   private final LinkedList<Item> catseyes = new LinkedList<>();
/*  43 */   private Node endNode = null;
/*  44 */   private float cost = 0.0F;
/*  45 */   private float distance = 0.0F;
/*  46 */   private Route oppositeRoute = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Route(Node startNode, byte direction, int id) {
/*  57 */     this.startNode = startNode;
/*  58 */     this.direction = direction;
/*  59 */     this.id = id;
/*  60 */     addCost(startNode.getWaystone(), direction);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node getStartNode() {
/*  70 */     return this.startNode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getDirection() {
/*  80 */     return this.direction;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getId() {
/*  91 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Route getOppositeRoute() {
/* 102 */     return this.oppositeRoute;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void SetOppositeRoute(Route oppositeRoute) {
/* 112 */     this.oppositeRoute = oppositeRoute;
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
/*     */   public void AddCatseye(Item catseye, boolean atFront, byte direction) {
/* 124 */     if (atFront) {
/* 125 */       this.catseyes.addFirst(catseye);
/*     */     } else {
/* 127 */       this.catseyes.add(catseye);
/* 128 */     }  addCost(catseye, direction);
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
/*     */   private void addCost(Item marker, byte direction) {
/* 140 */     float thiscost = 1.0F;
/*     */     
/* 142 */     if (direction == 2 || direction == 8 || direction == 32 || direction == Byte.MIN_VALUE)
/*     */     {
/*     */ 
/*     */       
/* 146 */       thiscost = 1.414F;
/*     */     }
/* 148 */     this.distance += thiscost;
/*     */     
/* 150 */     if (!marker.isOnSurface()) {
/* 151 */       thiscost *= 1.1F;
/*     */     }
/* 153 */     if (marker.getBridgeId() != -10L)
/* 154 */       thiscost *= 1.05F; 
/* 155 */     this.cost += thiscost;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Item[] getCatseyes() {
/* 165 */     return this.catseyes.<Item>toArray(new Item[this.catseyes.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LinkedList<Item> getCatseyesList() {
/* 175 */     return this.catseyes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LinkedList<Item> getCatseyesListCopy() {
/* 185 */     return new LinkedList<>(this.catseyes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsCatseye(Item catseye) {
/* 196 */     return this.catseyes.contains(catseye);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getCost() {
/* 206 */     return this.cost;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getDistance() {
/* 216 */     return this.distance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void AddEndNode(Node node) {
/* 226 */     this.endNode = node;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Node getEndNode() {
/* 237 */     return this.endNode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final short isOnHighwayPath(Player player) {
/* 247 */     List<Route> highwayPath = player.getHighwayPath();
/* 248 */     if (highwayPath != null)
/*     */     {
/* 250 */       for (int x = 0; x < highwayPath.size(); x++) {
/*     */         
/* 252 */         if (highwayPath.get(x) == this) {
/*     */ 
/*     */           
/* 255 */           float distance = 0.0F;
/* 256 */           for (int y = x; y < highwayPath.size(); y++)
/* 257 */             distance += ((Route)highwayPath.get(y)).getDistance(); 
/* 258 */           return (short)(int)distance;
/*     */         } 
/*     */       } 
/*     */     }
/* 262 */     return -1;
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
/*     */   public String toString() {
/* 274 */     StringBuilder buf = new StringBuilder();
/* 275 */     buf.append("{route:" + this.id);
/* 276 */     buf.append(" Start Waystone:" + this.startNode.toString());
/*     */     
/* 278 */     buf.append(" End waystone:");
/* 279 */     if (this.endNode != null) {
/* 280 */       buf.append(this.endNode.getWaystone().toString());
/*     */     } else {
/* 282 */       buf.append("missing");
/*     */     } 
/* 284 */     boolean first = true;
/* 285 */     for (Item catseye : this.catseyes) {
/*     */       
/* 287 */       if (first) {
/*     */         
/* 289 */         first = false;
/* 290 */         buf.append(" Catseyes:{");
/*     */       } else {
/*     */         
/* 293 */         buf.append(",");
/* 294 */       }  buf.append(catseye.toString());
/*     */     } 
/* 296 */     if (!first)
/* 297 */       buf.append("}"); 
/* 298 */     buf.append("}");
/* 299 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\highways\Route.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */