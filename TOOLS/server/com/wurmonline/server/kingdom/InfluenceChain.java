/*     */ package com.wurmonline.server.kingdom;
/*     */ 
/*     */ import com.wurmonline.server.NoSuchItemException;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.villages.Village;
/*     */ import com.wurmonline.server.villages.Villages;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.logging.Logger;
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
/*     */ public class InfluenceChain
/*     */ {
/*  34 */   protected static Logger logger = Logger.getLogger(InfluenceChain.class.getName());
/*     */ 
/*     */   
/*     */   public static final int MAX_TOWER_CHAIN_DISTANCE = 120;
/*     */   
/*  39 */   protected static HashMap<Byte, InfluenceChain> influenceChains = new HashMap<>();
/*     */ 
/*     */   
/*  42 */   protected ArrayList<Item> chainMarkers = new ArrayList<>();
/*  43 */   protected int chainedMarkers = 0;
/*     */ 
/*     */ 
/*     */   
/*     */   protected byte kingdom;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InfluenceChain(byte kingdom) {
/*  53 */     this.kingdom = kingdom;
/*  54 */     Village capital = Villages.getCapital(kingdom);
/*  55 */     if (capital != null) {
/*     */       
/*     */       try
/*     */       {
/*     */ 
/*     */         
/*  61 */         this.chainMarkers.add(capital.getToken());
/*     */       }
/*  63 */       catch (NoSuchItemException e)
/*     */       {
/*  65 */         logger.warning(String.format("Influence Chain Error: No token found for village %s.", new Object[] { capital.getName() }));
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/*  70 */       for (Village v : Villages.getVillages()) {
/*     */         
/*  72 */         if (v.kingdom == kingdom) {
/*     */           
/*  74 */           logger.info(String.format("Because kingdom %s has no capital, the village %s has been selected as it's influence chain start.", new Object[] {
/*  75 */                   Kingdoms.getKingdom(kingdom).getName(), v.getName() }));
/*  76 */           capital = v;
/*     */           break;
/*     */         } 
/*     */       } 
/*  80 */       if (capital != null) {
/*     */         
/*     */         try
/*     */         {
/*     */ 
/*     */           
/*  86 */           this.chainMarkers.add(capital.getToken());
/*     */         }
/*  88 */         catch (NoSuchItemException e)
/*     */         {
/*  90 */           logger.warning(String.format("Influence Chain Error: No token found for village %s.", new Object[] { capital.getName() }));
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/*  95 */         logger.warning(String.format("Influence Chain Error: There is no compatible villages for kingdom %s to start an influence chain.", new Object[] {
/*  96 */                 Kingdoms.getKingdom(kingdom).getName()
/*     */               }));
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
/*     */   public ArrayList<Item> getChainMarkers() {
/* 109 */     return this.chainMarkers;
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
/*     */   public void pulseChain(Item marker) {
/* 122 */     for (Item otherMarker : this.chainMarkers) {
/*     */ 
/*     */       
/* 125 */       if (otherMarker.isChained()) {
/*     */         continue;
/*     */       }
/* 128 */       int distX = Math.abs(marker.getTileX() - otherMarker.getTileX());
/* 129 */       int distY = Math.abs(marker.getTileY() - otherMarker.getTileY());
/* 130 */       int maxDist = Math.max(distX, distY);
/* 131 */       if (maxDist <= 120) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 136 */         otherMarker.setChained(true);
/* 137 */         this.chainedMarkers++;
/* 138 */         pulseChain(otherMarker);
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
/*     */   public void recalculateChain() {
/* 150 */     for (Item marker : this.chainMarkers)
/*     */     {
/* 152 */       marker.setChained(false);
/*     */     }
/* 154 */     Item capitalToken = this.chainMarkers.get(0);
/* 155 */     capitalToken.setChained(true);
/* 156 */     this.chainedMarkers = 1;
/* 157 */     for (Village v : Villages.getVillages()) {
/*     */ 
/*     */       
/* 160 */       if (v.kingdom == this.kingdom && v.isPermanent) {
/*     */         
/*     */         try {
/*     */ 
/*     */           
/* 165 */           Item villageToken = v.getToken();
/* 166 */           villageToken.setChained(true);
/* 167 */           this.chainedMarkers++;
/* 168 */           pulseChain(villageToken);
/*     */         }
/* 170 */         catch (NoSuchItemException e) {
/*     */           
/* 172 */           logger.warning(String.format("Influence Chain Error: No token found for village %s.", new Object[] { v.getName() }));
/*     */         } 
/*     */       }
/*     */     } 
/* 176 */     pulseChain(capitalToken);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static InfluenceChain getInfluenceChain(byte kingdom) {
/* 186 */     if (influenceChains.containsKey(Byte.valueOf(kingdom)))
/* 187 */       return influenceChains.get(Byte.valueOf(kingdom)); 
/* 188 */     InfluenceChain newChain = new InfluenceChain(kingdom);
/* 189 */     influenceChains.put(Byte.valueOf(kingdom), newChain);
/* 190 */     return newChain;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addToken(Item token) {
/* 199 */     if (this.chainMarkers.contains(token))
/*     */     {
/* 201 */       logger.info(String.format("Token at %d, %d already exists in the influence chain.", new Object[] {
/* 202 */               Integer.valueOf(token.getTileX()), Integer.valueOf(token.getTileY())
/*     */             })); } 
/* 204 */     this.chainMarkers.add(token);
/* 205 */     recalculateChain();
/* 206 */     logger.info(String.format("Added new village token to %s, which now has %d markers ad %d successfully linked.", new Object[] {
/* 207 */             Kingdoms.getKingdom(this.kingdom).getName(), Integer.valueOf(this.chainMarkers.size()), Integer.valueOf(this.chainedMarkers)
/*     */           }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void addTokenToChain(byte kingdom, Item token) {
/* 217 */     InfluenceChain kingdomChain = getInfluenceChain(kingdom);
/* 218 */     kingdomChain.addToken(token);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTower(Item tower) {
/* 227 */     if (this.chainMarkers.contains(tower)) {
/*     */       
/* 229 */       logger.info(String.format("Tower at %d, %d already exists in the influence chain.", new Object[] {
/* 230 */               Integer.valueOf(tower.getTileX()), Integer.valueOf(tower.getTileY()) }));
/*     */       return;
/*     */     } 
/* 233 */     this.chainMarkers.add(tower);
/* 234 */     recalculateChain();
/* 235 */     logger.info(String.format("Added new tower to %s, which now has %d markers and %d successfully linked.", new Object[] {
/* 236 */             Kingdoms.getKingdom(this.kingdom).getName(), Integer.valueOf(this.chainMarkers.size()), Integer.valueOf(this.chainedMarkers)
/*     */           }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void addTowerToChain(byte kingdom, Item tower) {
/* 246 */     InfluenceChain kingdomChain = getInfluenceChain(kingdom);
/* 247 */     kingdomChain.addTower(tower);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeTower(Item tower) {
/* 256 */     this.chainMarkers.remove(tower);
/* 257 */     recalculateChain();
/* 258 */     logger.info(String.format("Removed tower from %s, which now has %d markers and %d successfully linked.", new Object[] {
/* 259 */             Kingdoms.getKingdom(this.kingdom).getName(), Integer.valueOf(this.chainMarkers.size()), Integer.valueOf(this.chainedMarkers)
/*     */           }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void removeTowerFromChain(byte kingdom, Item tower) {
/* 269 */     InfluenceChain kingdomChain = getInfluenceChain(kingdom);
/* 270 */     kingdomChain.removeTower(tower);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\kingdom\InfluenceChain.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */