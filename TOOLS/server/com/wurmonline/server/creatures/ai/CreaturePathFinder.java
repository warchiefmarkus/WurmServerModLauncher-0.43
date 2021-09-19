/*     */ package com.wurmonline.server.creatures.ai;
/*     */ 
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Timer;
/*     */ import java.util.TimerTask;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.logging.Level;
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
/*     */ 
/*     */ public class CreaturePathFinder
/*     */   extends TimerTask
/*     */ {
/*  37 */   private final Map<Creature, PathTile> pathTargets = new ConcurrentHashMap<>();
/*     */   
/*     */   private boolean keeprunning = true;
/*     */   
/*     */   public static final long SLEEP_TIME = 25L;
/*  42 */   private static final StaticPathFinder pathFinder = new StaticPathFinder();
/*     */   
/*  44 */   private static Logger logger = Logger.getLogger(CreaturePathFinder.class.getName());
/*     */ 
/*     */   
/*     */   public final void addTarget(Creature c, PathTile target) {
/*  48 */     this.pathTargets.put(c, target);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void removeTarget(Creature c) {
/*  53 */     this.pathTargets.remove(c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean log = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isLog() {
/*  72 */     return log;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void toggleLog() {
/*  77 */     setLog(!isLog());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLog(boolean nlog) {
/*  88 */     log = nlog;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void startRunning() {
/*  93 */     Timer timer = new Timer();
/*  94 */     timer.scheduleAtFixedRate(this, 40000L, 25L);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void shutDown() {
/*  99 */     this.keeprunning = false;
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
/*     */   public void run() {
/* 111 */     if (this.keeprunning) {
/*     */       
/* 113 */       long now = System.currentTimeMillis();
/* 114 */       if (!this.pathTargets.isEmpty())
/*     */       {
/* 116 */         for (Iterator<Map.Entry<Creature, PathTile>> it = this.pathTargets.entrySet().iterator(); it.hasNext(); ) {
/*     */           
/* 118 */           Map.Entry<Creature, PathTile> entry = it.next();
/* 119 */           Creature creature = entry.getKey();
/* 120 */           PathTile p = entry.getValue();
/*     */           
/*     */           try {
/* 123 */             Path path = creature.findPath(p.getTileX(), p.getTileY(), pathFinder);
/* 124 */             if (path != null)
/*     */             {
/* 126 */               if (p.hasSpecificPos()) {
/*     */                 
/* 128 */                 if (path.getPathTiles().isEmpty())
/*     */                 {
/* 130 */                   path.getPathTiles().add(new PathTile(creature.getTileX(), creature.getTileY(), creature
/* 131 */                         .getCurrentTileNum(), creature.isOnSurface(), creature.getFloorLevel()));
/*     */                 }
/* 133 */                 PathTile lastTile = path.getPathTiles().getLast();
/* 134 */                 lastTile.setSpecificPos(p.getPosX(), p.getPosY());
/*     */               } 
/* 136 */               creature.sendToLoggers("Found path to " + p.getTileX() + "," + p.getTileY());
/* 137 */               creature.getStatus().setPath(path);
/* 138 */               creature.receivedPath = true;
/* 139 */               it.remove();
/*     */             }
/*     */           
/* 142 */           } catch (NoPathException np) {
/*     */             
/* 144 */             creature.sendToLoggers("No Path to " + p.getTileX() + "," + p.getTileY() + " pathfindcounter=" + creature
/* 145 */                 .getPathfindCounter() + " || " + np.getMessage());
/* 146 */             it.remove();
/* 147 */             creature.setPathing(false, false);
/*     */           } 
/*     */         } 
/*     */       }
/* 151 */       if (log && System.currentTimeMillis() - now > 0L) {
/* 152 */         logger.log(Level.INFO, "Norm Finding paths took " + (
/* 153 */             System.currentTimeMillis() - now) + " ms for " + this.pathTargets.size());
/*     */       }
/*     */     } else {
/*     */       
/* 157 */       logger.log(Level.INFO, "Shutting down Norm pathfinder");
/* 158 */       cancel();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\ai\CreaturePathFinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */