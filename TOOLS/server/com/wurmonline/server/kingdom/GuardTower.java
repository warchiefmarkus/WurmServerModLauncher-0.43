/*      */ package com.wurmonline.server.kingdom;
/*      */ 
/*      */ import com.wurmonline.server.DbConnector;
/*      */ import com.wurmonline.server.Features;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.LoginHandler;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.NoSuchPlayerException;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.behaviours.MethodsCreatures;
/*      */ import com.wurmonline.server.behaviours.Terraforming;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.CreatureTemplateIds;
/*      */ import com.wurmonline.server.creatures.Creatures;
/*      */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*      */ import com.wurmonline.server.endgames.EndGameItem;
/*      */ import com.wurmonline.server.endgames.EndGameItems;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.skills.NoSuchSkillException;
/*      */ import com.wurmonline.server.skills.Skill;
/*      */ import com.wurmonline.server.skills.Skills;
/*      */ import com.wurmonline.server.structures.Fence;
/*      */ import com.wurmonline.server.utils.DbUtilities;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.villages.Villages;
/*      */ import com.wurmonline.server.zones.NoSuchZoneException;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.Zone;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import com.wurmonline.shared.constants.AttitudeConstants;
/*      */ import java.io.IOException;
/*      */ import java.sql.Connection;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.Random;
/*      */ import java.util.Set;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class GuardTower
/*      */   implements CreatureTemplateIds, MiscConstants, TimeConstants, AttitudeConstants
/*      */ {
/*      */   private static final String ADD_GUARD = "INSERT INTO TOWERGUARDS(TOWERID,CREATUREID) VALUES(?,?)";
/*      */   private static final String LOAD_GUARDS = "SELECT CREATUREID, RETURNED FROM TOWERGUARDS WHERE TOWERID=?";
/*      */   private static final String RETURN_GUARD = "UPDATE TOWERGUARDS SET RETURNED=? WHERE CREATUREID=?";
/*      */   private static final String DELETE_TOWER = "DELETE FROM TOWERGUARDS WHERE TOWERID=?";
/*      */   private static final String DELETE_CREATURE = "DELETE FROM TOWERGUARDS WHERE CREATUREID=?";
/*   72 */   private static Logger logger = Logger.getLogger(GuardTower.class.getName());
/*   73 */   private Item tower = null;
/*      */   
/*   75 */   private final Set<Creature> guards = new HashSet<>();
/*   76 */   private final LinkedList<Creature> freeGuards = new LinkedList<>();
/*      */   
/*   78 */   private static final Random rand = new Random();
/*   79 */   private long lastSentAttackMessage = 0L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   GuardTower(Item item) {
/*   89 */     this.tower = item;
/*   90 */     load();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Item getTower() {
/*  100 */     return this.tower;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getKingdom() {
/*  111 */     return this.tower.getAuxData();
/*      */   }
/*      */ 
/*      */   
/*      */   public final String getName() {
/*  116 */     String name = "unknown";
/*      */     
/*      */     try {
/*  119 */       name = Players.getInstance().getNameFor(this.tower.lastOwner);
/*      */     }
/*  121 */     catch (IOException|NoSuchPlayerException iOException) {}
/*      */ 
/*      */ 
/*      */     
/*  125 */     return name + " " + (this.tower.getWurmId() % 1000L);
/*      */   }
/*      */ 
/*      */   
/*      */   public final long getLastSentWarning() {
/*  130 */     return this.lastSentAttackMessage;
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendAttackWarning() {
/*  135 */     if (System.currentTimeMillis() - this.lastSentAttackMessage > 180000L) {
/*      */       
/*  137 */       this.lastSentAttackMessage = System.currentTimeMillis();
/*  138 */       Creature c = King.getOfficial(getKingdom(), 1502);
/*  139 */       if (c != null) {
/*      */         
/*  141 */         StringBuilder buf = new StringBuilder();
/*      */         
/*  143 */         String name = getName();
/*  144 */         c.getCommunicator().sendAlertServerMessage("Guard tower of " + name + " is under attack!", (byte)4);
/*  145 */         int tilex = (int)this.tower.getPosX() >> 2;
/*  146 */         int tiley = (int)this.tower.getPosY() >> 2;
/*  147 */         VolaTile t = Zones.getTileOrNull(tilex, tiley, this.tower.isOnSurface());
/*  148 */         if (t != null) {
/*      */           
/*  150 */           if (t.getVillage() != null) {
/*      */             
/*  152 */             buf.append("The ");
/*  153 */             buf.append(this.tower.getName());
/*  154 */             buf.append(" is in the settlement of ");
/*  155 */             buf.append(t.getVillage().getName());
/*  156 */             buf.append(". ");
/*      */           } 
/*  158 */           VolaTile ct = c.getCurrentTile();
/*  159 */           if (ct != null) {
/*      */             
/*  161 */             int ctx = ct.tilex;
/*  162 */             int cty = ct.tiley;
/*  163 */             int mindist = Math.max(Math.abs(tilex - ctx), Math.abs(tiley - cty));
/*  164 */             int dir = MethodsCreatures.getDir(c, tilex, tiley);
/*  165 */             String direction = MethodsCreatures.getLocationStringFor(c.getStatus().getRotation(), dir, "you");
/*  166 */             buf.append(EndGameItems.getDistanceString(mindist, this.tower.getName(), direction, true));
/*      */           } 
/*  168 */           c.getCommunicator().sendAlertServerMessage(buf.toString());
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean isMyTower(Creature guard) {
/*  181 */     if (guard.getKingdomId() == this.tower.getAuxData()) {
/*      */       Iterator<Creature> it;
/*  183 */       for (it = this.guards.iterator(); it.hasNext();) {
/*      */         
/*  185 */         if (it.next() == guard)
/*  186 */           return true; 
/*      */       } 
/*  188 */       for (it = this.freeGuards.iterator(); it.hasNext();) {
/*      */         
/*  190 */         if (it.next() == guard)
/*  191 */           return true; 
/*      */       } 
/*      */     } 
/*  194 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void destroy() {
/*  202 */     destroyGuards();
/*      */     
/*      */     try {
/*  205 */       deleteTower();
/*      */     }
/*  207 */     catch (IOException iox) {
/*      */       
/*  209 */       logger.log(Level.WARNING, iox.getMessage(), iox);
/*      */     } 
/*  211 */     this.tower = null;
/*      */   }
/*      */ 
/*      */   
/*      */   void destroyGuards() {
/*  216 */     this.guards.clear();
/*  217 */     for (Iterator<Creature> it = this.freeGuards.iterator(); it.hasNext(); ) {
/*      */       
/*  219 */       Creature g = it.next();
/*      */       
/*      */       try {
/*  222 */         destroyGuard(g);
/*      */       }
/*  224 */       catch (Exception e) {
/*      */         
/*  226 */         logger.log(Level.WARNING, "Problem destroying guard: " + g + " for tower: " + this + " due to " + e
/*  227 */             .getMessage(), e);
/*      */       } 
/*      */     } 
/*  230 */     this.freeGuards.clear();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void destroyGuard(Creature guard) throws IOException {
/*  240 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/*  242 */       logger.finer("Destroying guard " + guard);
/*      */     }
/*  244 */     Connection dbcon = null;
/*  245 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  248 */       dbcon = DbConnector.getZonesDbCon();
/*  249 */       ps = dbcon.prepareStatement("DELETE FROM TOWERGUARDS WHERE CREATUREID=?");
/*  250 */       ps.setLong(1, guard.getWurmId());
/*  251 */       ps.executeUpdate();
/*      */     }
/*  253 */     catch (SQLException ex) {
/*      */       
/*  255 */       logger.log(Level.INFO, "Failed to delete tower creature " + guard.getWurmId(), ex);
/*      */     }
/*      */     finally {
/*      */       
/*  259 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  260 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean hasLiveGuards() {
/*  266 */     return (this.guards.size() > 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void returnGuard(Creature guard) throws IOException {
/*  276 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/*  278 */       logger.finer("Returning guard " + guard);
/*      */     }
/*  280 */     this.guards.remove(guard);
/*  281 */     if (!this.freeGuards.contains(guard))
/*      */     {
/*  283 */       if (this.guards.size() < getMaxGuards()) {
/*      */         
/*  285 */         this.freeGuards.add(guard);
/*  286 */         Connection dbcon = null;
/*  287 */         PreparedStatement ps = null;
/*      */         
/*      */         try {
/*  290 */           dbcon = DbConnector.getZonesDbCon();
/*  291 */           ps = dbcon.prepareStatement("UPDATE TOWERGUARDS SET RETURNED=? WHERE CREATUREID=?");
/*  292 */           ps.setBoolean(1, true);
/*  293 */           ps.setLong(2, guard.getWurmId());
/*  294 */           ps.executeUpdate();
/*      */         }
/*  296 */         catch (SQLException ex) {
/*      */           
/*  298 */           logger.log(Level.INFO, "Failed to return guard for " + this.tower.getWurmId());
/*  299 */           throw new IOException("Failed to return guard for " + this.tower.getWurmId(), ex);
/*      */         }
/*      */         finally {
/*      */           
/*  303 */           DbUtilities.closeDatabaseObjects(ps, null);
/*  304 */           DbConnector.returnConnection(dbcon);
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/*  309 */         guard.destroy();
/*  310 */         destroyGuard(guard);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void setGuardInWorld(Creature guard) {
/*  321 */     VolaTile t = Zones.getTileOrNull((int)this.tower.getPosX() >> 2, (int)this.tower.getPosY() >> 2, this.tower.isOnSurface());
/*  322 */     if (t != null) {
/*      */       
/*  324 */       Fence[] fences = t.getFencesForLevel(0);
/*  325 */       if (fences != null)
/*      */       {
/*  327 */         for (Fence f : fences)
/*  328 */           f.destroy(); 
/*      */       }
/*      */     } 
/*  331 */     t = Zones.getTileOrNull(((int)this.tower.getPosX() >> 2) + 1, (int)this.tower.getPosY() >> 2, this.tower.isOnSurface());
/*  332 */     if (t != null) {
/*      */       
/*  334 */       Fence[] fences = t.getFencesForLevel(0);
/*  335 */       for (Fence f : fences) {
/*      */         
/*  337 */         if (!f.isHorizontal())
/*  338 */           f.destroy(); 
/*      */       } 
/*      */     } 
/*  341 */     t = Zones.getTileOrNull((int)this.tower.getPosX() >> 2, ((int)this.tower.getPosY() >> 2) + 1, this.tower.isOnSurface());
/*  342 */     if (t != null) {
/*      */       
/*  344 */       Fence[] fences = t.getFencesForLevel(0);
/*  345 */       for (Fence f : fences) {
/*      */         
/*  347 */         if (f.isHorizontal())
/*  348 */           f.destroy(); 
/*      */       } 
/*      */     } 
/*  351 */     guard.setPositionX(this.tower.getPosX());
/*  352 */     guard.setPositionY(this.tower.getPosY());
/*  353 */     guard.setPositionZ(this.tower.getPosZ());
/*  354 */     guard.setRotation(1.0F + Server.rand.nextFloat() * 359.0F);
/*  355 */     guard.setLayer(this.tower.isOnSurface() ? 0 : -1, false);
/*      */     
/*      */     try {
/*  358 */       guard.respawn();
/*  359 */       Zone zone = Zones.getZone((int)this.tower.getPosX() >> 2, (int)this.tower.getPosY() >> 2, this.tower.isOnSurface());
/*  360 */       zone.addCreature(guard.getWurmId());
/*  361 */       guard.savePosition(zone.getId());
/*      */     }
/*  363 */     catch (NoSuchZoneException nsz) {
/*      */       
/*  365 */       logger.log(Level.WARNING, "Guard: " + guard.getWurmId() + ": " + nsz.getMessage(), (Throwable)nsz);
/*      */     }
/*  367 */     catch (NoSuchCreatureException nsc) {
/*      */       
/*  369 */       logger.log(Level.WARNING, "Guard: " + guard.getWurmId() + ": " + nsc.getMessage(), (Throwable)nsc);
/*      */     }
/*  371 */     catch (NoSuchPlayerException nsp) {
/*      */       
/*  373 */       logger.log(Level.WARNING, "Guard: " + guard.getWurmId() + ": " + nsp.getMessage(), (Throwable)nsp);
/*      */     }
/*  375 */     catch (Exception ex) {
/*      */       
/*  377 */       logger.log(Level.WARNING, "Failed to return village guard: " + ex.getMessage(), ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void activateGuard(Creature guard) throws IOException {
/*  388 */     this.freeGuards.remove(guard);
/*  389 */     if (!this.guards.contains(guard))
/*  390 */       this.guards.add(guard); 
/*  391 */     guard.setGuardTower(this);
/*  392 */     setGuardInWorld(guard);
/*  393 */     Connection dbcon = null;
/*  394 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  397 */       dbcon = DbConnector.getZonesDbCon();
/*  398 */       ps = dbcon.prepareStatement("UPDATE TOWERGUARDS SET RETURNED=? WHERE CREATUREID=?");
/*  399 */       ps.setBoolean(1, false);
/*  400 */       ps.setLong(2, guard.getWurmId());
/*  401 */       ps.executeUpdate();
/*      */     }
/*  403 */     catch (SQLException ex) {
/*      */       
/*  405 */       logger.log(Level.INFO, "Failed to activate guard for " + this.tower.getWurmId());
/*  406 */       throw new IOException("Failed to activate guard for " + this.tower.getWurmId(), ex);
/*      */     }
/*      */     finally {
/*      */       
/*  410 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  411 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean alertGuards(Creature caller) {
/*  417 */     boolean helps = false;
/*      */     
/*  419 */     long target1 = -10L;
/*  420 */     long target2 = -10L;
/*  421 */     if (caller.getReputation() >= 0 || caller.getKingdomTemplateId() == 3)
/*      */     {
/*  423 */       if (this.guards.size() > 0) {
/*      */         
/*  425 */         int tpx = getTower().getTileX();
/*  426 */         int tpy = getTower().getTileY();
/*  427 */         int posz = (int)getTower().getPosZ();
/*  428 */         Creature callerTarg = caller.getTarget();
/*      */         
/*  430 */         for (Creature g : this.guards) {
/*      */           
/*  432 */           boolean hasTarget = false;
/*  433 */           if (g.target == -10L) {
/*      */             
/*  435 */             if (callerTarg != null)
/*      */             {
/*  437 */               if (callerTarg.isWithinTileDistanceTo(tpx, tpy, posz, 20))
/*      */               {
/*  439 */                 if (target2 == -10L)
/*      */                 {
/*  441 */                   if (callerTarg.getAttitude(g) == 2)
/*      */                   {
/*  443 */                     if (callerTarg.currentKingdom == getKingdom()) {
/*      */                       
/*  445 */                       g.setTarget(callerTarg.getWurmId(), false);
/*  446 */                       if (g.target == callerTarg.getWurmId()) {
/*      */                         
/*  448 */                         if (target1 == -10L) {
/*      */                           
/*  450 */                           target1 = callerTarg.getWurmId();
/*  451 */                           yellHunt(g, callerTarg, false);
/*      */                         }
/*  453 */                         else if (target2 == -10L) {
/*      */                           
/*  455 */                           target2 = callerTarg.getWurmId();
/*  456 */                           yellHunt(g, callerTarg, true);
/*      */                         }
/*      */                         else {
/*      */                           
/*  460 */                           logger.log(Level.INFO, "This shouldn't happen? Three targets when yelling.");
/*      */                         } 
/*  462 */                         yellHunt(g, callerTarg, true);
/*  463 */                         hasTarget = true;
/*  464 */                         helps = true;
/*      */                       } 
/*      */                     } 
/*      */                   }
/*      */                 }
/*      */               }
/*      */             }
/*  471 */             if (!hasTarget)
/*      */             {
/*  473 */               if (caller.opponent != null)
/*      */               {
/*  475 */                 if (caller.opponent.isWithinTileDistanceTo(tpx, tpy, posz, 20))
/*      */                 {
/*  477 */                   if (target2 == -10L)
/*      */                   {
/*  479 */                     if (caller.opponent.getAttitude(g) == 2)
/*      */                     {
/*  481 */                       if (caller.opponent.currentKingdom == getKingdom()) {
/*      */                         
/*  483 */                         g.setTarget(caller.opponent.getWurmId(), false);
/*  484 */                         if (g.target == caller.opponent.getWurmId()) {
/*      */                           
/*  486 */                           if (target1 == -10L) {
/*      */                             
/*  488 */                             target1 = caller.opponent.getWurmId();
/*  489 */                             yellHunt(g, caller.opponent, false);
/*      */                           }
/*  491 */                           else if (target2 == -10L) {
/*      */                             
/*  493 */                             target2 = caller.opponent.getWurmId();
/*  494 */                             yellHunt(g, caller.opponent, true);
/*      */                           }
/*      */                           else {
/*      */                             
/*  498 */                             logger.log(Level.INFO, "This shouldn't happen? Three targets when yelling.");
/*      */                             
/*  500 */                             yellHunt(g, caller.opponent, true);
/*      */                           } 
/*  502 */                           hasTarget = true;
/*  503 */                           helps = true;
/*      */                         } 
/*      */                       } 
/*      */                     }
/*      */                   }
/*      */                 }
/*      */               }
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  519 */     return helps;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void spawnSoldier(Item target, byte kingdom) {
/*      */     try {
/*  526 */       Creature c = Creature.doNew(7, target
/*  527 */           .getPosX() - 8.0F + Server.rand.nextFloat() * 16.0F, target.getPosY() - 8.0F + Server.rand.nextFloat() * 16.0F, Server.rand
/*  528 */           .nextFloat() * 360.0F, 0, LoginHandler.raiseFirstLetter(target.getName() + " guard"), 
/*  529 */           Server.rand.nextBoolean() ? 1 : 0, kingdom);
/*  530 */       c.checkForEnemies(true);
/*      */     }
/*  532 */     catch (Exception e) {
/*      */ 
/*      */       
/*  535 */       logger.log(Level.WARNING, e.getMessage(), e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void spawnCommander(Item target, byte kingdom) {
/*      */     try {
/*  543 */       String name = target.getName() + " lieutenant";
/*  544 */       boolean captain = Server.rand.nextBoolean();
/*  545 */       if (captain)
/*  546 */         name = target.getName() + " captain"; 
/*  547 */       Creature c = Creature.doNew(8, target
/*  548 */           .getPosX() - 8.0F + Server.rand.nextFloat() * 16.0F, target.getPosY() - 8.0F + Server.rand.nextFloat() * 16.0F, Server.rand
/*  549 */           .nextFloat() * 360.0F, 0, LoginHandler.raiseFirstLetter(name), 
/*  550 */           Server.rand.nextBoolean() ? 1 : 0, kingdom);
/*      */       
/*  552 */       if (captain) {
/*      */         
/*  554 */         Skills s = c.getSkills();
/*      */         
/*      */         try {
/*  557 */           Skill bc = s.getSkill(104);
/*  558 */           bc.setKnowledge(bc.getKnowledge() + 10.0D, false);
/*  559 */           Skill bs = s.getSkill(103);
/*  560 */           bs.setKnowledge(bs.getKnowledge() + 10.0D, false);
/*  561 */           Skill bst = s.getSkill(102);
/*  562 */           bst.setKnowledge(bst.getKnowledge() + 10.0D, false);
/*  563 */           Skill mst = s.getSkill(101);
/*  564 */           mst.setKnowledge(mst.getKnowledge() + 10.0D, false);
/*      */         }
/*  566 */         catch (NoSuchSkillException nss) {
/*      */           
/*  568 */           logger.log(Level.WARNING, c.getWurmId() + ": " + nss.getMessage());
/*      */         } 
/*      */       } 
/*  571 */       c.checkForEnemies(true);
/*      */     }
/*  573 */     catch (Exception e) {
/*      */ 
/*      */       
/*  576 */       logger.log(Level.WARNING, e.getMessage(), e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void yellHunt(Creature guard, Creature target, boolean aiding) {
/*  584 */     String toYell = "";
/*  585 */     if (!aiding) {
/*  586 */       switch (guard.getKingdomId()) {
/*      */         
/*      */         case 3:
/*  589 */           toYell = yellHotsHunter(guard, target);
/*      */           break;
/*      */         case 1:
/*  592 */           toYell = yellJennHunter(guard, target);
/*      */           break;
/*      */         case 2:
/*  595 */           toYell = yellMolrHunter(guard, target);
/*      */           break;
/*      */         case 4:
/*  598 */           toYell = yellFreedomHunter(guard, target);
/*      */           break;
/*      */         default:
/*  601 */           toYell = yellGenericHunter(guard, target);
/*      */           break;
/*      */       } 
/*      */     } else {
/*  605 */       toYell = yellAidHunter(guard, target);
/*  606 */     }  guard.say(toYell);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final String yellAidHunter(Creature guard, Creature target) {
/*  611 */     StringBuilder sb = new StringBuilder();
/*  612 */     int random = rand.nextInt(10);
/*  613 */     if (random < 1) {
/*      */       
/*  615 */       sb.append("Coming for ");
/*  616 */       if (!target.isPlayer()) {
/*  617 */         sb.append("the ");
/*      */       }
/*  619 */     } else if (random < 2) {
/*      */       
/*  621 */       sb.append("I'll help with ");
/*  622 */       if (!target.isPlayer()) {
/*  623 */         sb.append("the ");
/*      */       }
/*  625 */     } else if (random < 3) {
/*      */       
/*  627 */       sb.append("Joining in on ");
/*  628 */       if (!target.isPlayer()) {
/*  629 */         sb.append("the ");
/*      */       }
/*  631 */     } else if (random < 4) {
/*      */       
/*  633 */       sb.append("Beware of me as well, ");
/*      */     }
/*  635 */     else if (random < 5) {
/*      */       
/*  637 */       sb.append("Whoa! Here I come, ");
/*      */     }
/*  639 */     else if (random < 6) {
/*      */       
/*  641 */       sb.append("Now we are two, ");
/*      */     }
/*  643 */     else if (random < 7) {
/*      */       
/*  645 */       sb.append("You better believe it, ");
/*      */     }
/*  647 */     else if (random < 8) {
/*      */       
/*  649 */       sb.append("I come as well, ");
/*      */     }
/*  651 */     else if (random < 9) {
/*      */       
/*  653 */       sb.append("I also found ");
/*  654 */       if (!target.isPlayer()) {
/*  655 */         sb.append("the ");
/*      */       }
/*  657 */     } else if (random < 10) {
/*      */       
/*  659 */       sb.append("I see ");
/*  660 */       if (!target.isPlayer()) {
/*  661 */         sb.append("the ");
/*      */       }
/*      */     } 
/*  664 */     if (target.isPlayer()) {
/*  665 */       sb.append(target.getName());
/*      */     } else {
/*  667 */       sb.append(target.getName().toLowerCase());
/*  668 */     }  sb.append(".");
/*  669 */     return sb.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   public static final String yellHotsHunter(Creature guard, Creature target) {
/*  674 */     StringBuilder sb = new StringBuilder();
/*  675 */     int random = rand.nextInt(10);
/*  676 */     if (random < 1) {
/*      */       
/*  678 */       sb.append("You're one ugly bastard, ");
/*      */     }
/*  680 */     else if (random < 2) {
/*      */       
/*  682 */       sb.append("This will be fun, ");
/*      */     }
/*  684 */     else if (random < 3) {
/*      */       
/*  686 */       sb.append("I will enjoy killing you, ");
/*      */     }
/*  688 */     else if (random < 4) {
/*      */       
/*  690 */       sb.append("Eat my wrath, ");
/*      */     }
/*  692 */     else if (random < 5) {
/*      */       
/*  694 */       sb.append("I will shred you, ");
/*      */     }
/*  696 */     else if (random < 6) {
/*      */       
/*  698 */       sb.append("You will look bad torn to pieces, ");
/*      */     }
/*  700 */     else if (random < 7) {
/*      */       
/*  702 */       sb.append("Your corpse will rot away in silence, ");
/*      */     }
/*  704 */     else if (random < 8) {
/*      */       
/*  706 */       sb.append("I will drink your blood, ");
/*      */     }
/*  708 */     else if (random < 9) {
/*      */       
/*  710 */       sb.append("Die, die and die again, ");
/*      */     }
/*  712 */     else if (random < 10) {
/*      */       
/*  714 */       sb.append("Prepare to be exterminated, ");
/*      */     } 
/*  716 */     if (target.isPlayer()) {
/*  717 */       sb.append(target.getName());
/*      */     } else {
/*  719 */       sb.append(target.getName().toLowerCase());
/*  720 */     }  sb.append("!");
/*  721 */     return sb.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   public static final String yellJennHunter(Creature guard, Creature target) {
/*  726 */     StringBuilder sb = new StringBuilder();
/*  727 */     int random = rand.nextInt(10);
/*  728 */     if (random < 1) {
/*      */       
/*  730 */       sb.append("I'll hunt down ");
/*  731 */       if (!target.isPlayer()) {
/*  732 */         sb.append("this ");
/*      */       }
/*  734 */     } else if (random < 2) {
/*      */       
/*  736 */       sb.append("I'll take care of ");
/*  737 */       if (!target.isPlayer()) {
/*  738 */         sb.append("the ");
/*      */       }
/*  740 */     } else if (random < 3) {
/*      */       
/*  742 */       sb.append("You will soon be history, ");
/*      */     }
/*  744 */     else if (random < 4) {
/*      */       
/*  746 */       sb.append("Goodbye, ");
/*      */     }
/*  748 */     else if (random < 5) {
/*      */       
/*  750 */       sb.append("Quick, help me dispatch ");
/*  751 */       if (!target.isPlayer()) {
/*  752 */         sb.append("this ");
/*      */       }
/*  754 */     } else if (random < 6) {
/*      */       
/*  756 */       sb.append("I found ");
/*  757 */       if (!target.isPlayer()) {
/*  758 */         sb.append("the ");
/*      */       }
/*  760 */     } else if (random < 7) {
/*      */       
/*  762 */       sb.append("No soup for you, ");
/*      */     }
/*  764 */     else if (random < 8) {
/*      */       
/*  766 */       sb.append("Let me handle ");
/*  767 */       if (!target.isPlayer()) {
/*  768 */         sb.append("this ");
/*      */       }
/*  770 */     } else if (random < 9) {
/*      */       
/*  772 */       sb.append("Here is ");
/*  773 */       if (!target.isPlayer()) {
/*  774 */         sb.append("the ");
/*      */       }
/*  776 */     } else if (random < 10) {
/*      */       
/*  778 */       sb.append("This will hurt some, ");
/*      */     } 
/*  780 */     if (target.isPlayer()) {
/*  781 */       sb.append(target.getName());
/*      */     } else {
/*  783 */       sb.append(target.getName().toLowerCase());
/*  784 */     }  sb.append("!");
/*  785 */     return sb.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   public static final String yellMolrHunter(Creature guard, Creature target) {
/*  790 */     StringBuilder sb = new StringBuilder();
/*  791 */     int random = rand.nextInt(10);
/*  792 */     if (random < 1) {
/*      */       
/*  794 */       sb.append("By the fires of Magranon! Die, ");
/*      */     }
/*  796 */     else if (random < 2) {
/*      */       
/*  798 */       sb.append("I will rip you apart, ");
/*      */     }
/*  800 */     else if (random < 3) {
/*      */       
/*  802 */       sb.append("I will crush you, ");
/*      */     }
/*  804 */     else if (random < 4) {
/*      */       
/*  806 */       sb.append("Prepare to die, ");
/*      */     }
/*  808 */     else if (random < 5) {
/*      */       
/*  810 */       sb.append("This will hurt badly, ");
/*      */     }
/*  812 */     else if (random < 6) {
/*      */       
/*  814 */       sb.append("You receive no mercy, ");
/*      */     }
/*  816 */     else if (random < 7) {
/*      */       
/*  818 */       sb.append("Bleed, ");
/*      */     }
/*  820 */     else if (random < 8) {
/*      */       
/*  822 */       sb.append("This will be your last breath, ");
/*      */     }
/*  824 */     else if (random < 9) {
/*      */       
/*  826 */       sb.append("Pain will be served, ");
/*      */     }
/*  828 */     else if (random < 10) {
/*      */       
/*  830 */       sb.append("This is the end, ");
/*      */     } 
/*  832 */     if (target.isPlayer()) {
/*  833 */       sb.append(target.getName());
/*      */     } else {
/*  835 */       sb.append(target.getName().toLowerCase());
/*  836 */     }  sb.append("!");
/*  837 */     return sb.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   public static final String yellFreedomHunter(Creature guard, Creature target) {
/*  842 */     StringBuilder sb = new StringBuilder();
/*  843 */     int random = rand.nextInt(10);
/*  844 */     if (random < 1) {
/*      */       
/*  846 */       sb.append("Goodbye, ");
/*      */     }
/*  848 */     else if (random < 2) {
/*      */       
/*  850 */       sb.append("I have to slay you now, ");
/*      */     }
/*  852 */     else if (random < 3) {
/*      */       
/*  854 */       sb.append("Forgive me, ");
/*      */     }
/*  856 */     else if (random < 4) {
/*      */       
/*  858 */       sb.append("Stop that immediately, ");
/*      */     }
/*  860 */     else if (random < 5) {
/*      */       
/*  862 */       sb.append("I have to hurt you now, ");
/*      */     }
/*  864 */     else if (random < 6) {
/*      */       
/*  866 */       sb.append("Flee, ");
/*      */     }
/*  868 */     else if (random < 7) {
/*      */       
/*  870 */       sb.append("Run quickly now, ");
/*      */     }
/*  872 */     else if (random < 8) {
/*      */       
/*  874 */       sb.append("It is my duty to inform you that this is the end, ");
/*      */     }
/*  876 */     else if (random < 9) {
/*      */       
/*  878 */       sb.append("I will terminate ");
/*      */     }
/*  880 */     else if (random < 10) {
/*      */       
/*  882 */       sb.append("My pleasure, ");
/*      */     } 
/*  884 */     if (target.isPlayer()) {
/*  885 */       sb.append(target.getName());
/*      */     } else {
/*  887 */       sb.append(target.getName().toLowerCase());
/*  888 */     }  sb.append("!");
/*  889 */     return sb.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   public static final String yellGenericHunter(Creature guard, Creature target) {
/*  894 */     StringBuilder sb = new StringBuilder();
/*  895 */     int random = rand.nextInt(10);
/*  896 */     if (random < 1) {
/*      */       
/*  898 */       sb.append("I'll hunt down ");
/*  899 */       if (!target.isPlayer()) {
/*  900 */         sb.append("this ");
/*      */       }
/*  902 */     } else if (random < 2) {
/*      */       
/*  904 */       sb.append("I'll take care of ");
/*  905 */       if (!target.isPlayer()) {
/*  906 */         sb.append("the ");
/*      */       }
/*  908 */     } else if (random < 3) {
/*      */       
/*  910 */       sb.append("Prepare to meet your maker, ");
/*      */     }
/*  912 */     else if (random < 4) {
/*      */       
/*  914 */       sb.append("Goodbye, ");
/*      */     }
/*  916 */     else if (random < 5) {
/*      */       
/*  918 */       sb.append("This is the end, ");
/*      */     }
/*  920 */     else if (random < 6) {
/*      */       
/*  922 */       sb.append("I found ");
/*  923 */       if (!target.isPlayer()) {
/*  924 */         sb.append("this ");
/*      */       }
/*  926 */     } else if (random < 7) {
/*      */       
/*  928 */       sb.append("I will terminate you, ");
/*      */     }
/*  930 */     else if (random < 8) {
/*      */       
/*  932 */       sb.append("I attack ");
/*  933 */       if (!target.isPlayer()) {
/*  934 */         sb.append("this ");
/*      */       }
/*  936 */     } else if (random < 9) {
/*      */       
/*  938 */       sb.append("I will hunt ");
/*  939 */       if (!target.isPlayer()) {
/*  940 */         sb.append("the ");
/*      */       }
/*  942 */     } else if (random < 10) {
/*      */       
/*  944 */       sb.append("I get ");
/*  945 */       if (!target.isPlayer())
/*  946 */         sb.append("the "); 
/*      */     } 
/*  948 */     if (target.isPlayer()) {
/*  949 */       sb.append(target.getName());
/*      */     } else {
/*  951 */       sb.append(target.getName().toLowerCase());
/*  952 */     }  sb.append("!");
/*  953 */     return sb.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void newGuard(Creature guard) throws IOException {
/*  963 */     guard.setGuardTower(this);
/*  964 */     this.guards.add(guard);
/*  965 */     Connection dbcon = null;
/*  966 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  969 */       dbcon = DbConnector.getZonesDbCon();
/*  970 */       ps = dbcon.prepareStatement("INSERT INTO TOWERGUARDS(TOWERID,CREATUREID) VALUES(?,?)");
/*  971 */       ps.setLong(1, this.tower.getWurmId());
/*  972 */       ps.setLong(2, guard.getWurmId());
/*  973 */       ps.executeUpdate();
/*      */     }
/*  975 */     catch (SQLException ex) {
/*      */       
/*  977 */       logger.log(Level.INFO, "Failed to insert guard for " + this.tower.getWurmId());
/*  978 */       throw new IOException("Failed to insert guard for " + this.tower.getWurmId(), ex);
/*      */     }
/*      */     finally {
/*      */       
/*  982 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  983 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void deleteTower() throws IOException {
/*  993 */     Connection dbcon = null;
/*  994 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  997 */       dbcon = DbConnector.getZonesDbCon();
/*  998 */       ps = dbcon.prepareStatement("DELETE FROM TOWERGUARDS WHERE TOWERID=?");
/*  999 */       ps.setLong(1, this.tower.getWurmId());
/* 1000 */       ps.executeUpdate();
/*      */     }
/* 1002 */     catch (SQLException ex) {
/*      */       
/* 1004 */       logger.log(Level.INFO, "Failed to delete tower " + this.tower.getWurmId());
/* 1005 */       throw new IOException("Failed to delete tower " + this.tower.getWurmId(), ex);
/*      */     }
/*      */     finally {
/*      */       
/* 1009 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1010 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void poll() {
/* 1019 */     pollGuards();
/* 1020 */     this.tower.attackEnemies(true);
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getGuardCount() {
/* 1025 */     return this.guards.size();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getMaxGuards() {
/* 1034 */     if (Features.Feature.TOWER_CHAINING.isEnabled() && !this.tower.isChained())
/* 1035 */       return 0; 
/* 1036 */     return Math.min(5 + this.tower.getRarity(), (int)this.tower.getQualityLevel() / 10);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getMaxPossibleGuards() {
/* 1044 */     return Math.min(5 + this.tower.getRarity(), (int)this.tower.getQualityLevel() / 10);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void pollGuards() {
/* 1052 */     if (this.guards.size() < getMaxGuards()) {
/*      */ 
/*      */       
/* 1055 */       Village v = Villages.getVillageWithPerimeterAt(getTower().getTileX(), getTower().getTileY(), true);
/* 1056 */       if (v != null && getKingdom() != v.kingdom)
/*      */         return; 
/* 1058 */       byte sex = 0;
/* 1059 */       if (Server.rand.nextInt(2) == 0)
/* 1060 */         sex = 1; 
/* 1061 */       int templateId = 34;
/* 1062 */       byte templateKingdom = this.tower.getAuxData();
/* 1063 */       Kingdom kingdom = Kingdoms.getKingdom(this.tower.getAuxData());
/* 1064 */       if (kingdom != null)
/* 1065 */         templateKingdom = kingdom.getTemplate(); 
/* 1066 */       if (templateKingdom == 3) {
/*      */         
/* 1068 */         templateId = 35;
/*      */       }
/* 1070 */       else if (templateKingdom == 2) {
/*      */         
/* 1072 */         templateId = 36;
/*      */       }
/* 1074 */       else if (templateKingdom == 4) {
/*      */         
/* 1076 */         templateId = 67;
/*      */       } 
/*      */       
/*      */       try {
/* 1080 */         if (this.freeGuards.isEmpty()) {
/*      */           
/* 1082 */           Kingdom k = Kingdoms.getKingdom(this.tower.getAuxData());
/* 1083 */           if (k.getId() != 0) {
/*      */             
/* 1085 */             spawnGuard(templateId, "tower guard", false);
/*      */           }
/* 1087 */           else if (this.tower.getTemplateId() == 996) {
/*      */             
/* 1089 */             spawnGuard(67, "Peacekeeper", false);
/*      */           } 
/*      */         } else {
/*      */           
/* 1093 */           Creature toReturn = this.freeGuards.removeFirst();
/* 1094 */           activateGuard(toReturn);
/* 1095 */           if (logger.isLoggable(Level.FINER))
/*      */           {
/* 1097 */             logger.finer("Activating " + toReturn.getWurmId());
/*      */           }
/*      */         } 
/* 1100 */       } catch (Exception ex) {
/*      */         
/* 1102 */         logger.log(Level.WARNING, "Problem while polling guards for tower: " + this.tower + ", " + ex.getMessage(), ex);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getBashAlertDamage() {
/* 1112 */     return 90;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void checkBashDamage(float oldDam, float newDam) {
/* 1121 */     for (byte i = 1; i <= 3; i = (byte)(i + 1)) {
/*      */       
/* 1123 */       if (oldDam < (30 * i) && newDam >= (30 * i)) {
/*      */         
/* 1125 */         spawnBashWave(i);
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/*      */     
/* 1131 */     if (oldDam < getBashAlertDamage() && newDam >= getBashAlertDamage()) {
/* 1132 */       Players.getInstance().broadCastBashInfo(getTower(), getName() + " is under attack.");
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getTowerSpawnMod() {
/* 1141 */     return Math.round(getTower().getQualityLevel() / 30.0F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getTowerBashSpawnCount(int spawnMod, byte waveStrength) {
/* 1151 */     return Math.max(1, spawnMod) * waveStrength;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void spawnGuard(int templateId, String name, boolean offSetSpawn) {
/*      */     try {
/* 1164 */       byte sex = (Server.rand.nextInt(2) == 0) ? 1 : 0;
/* 1165 */       Kingdom k = Kingdoms.getKingdom(this.tower.getAuxData());
/* 1166 */       if (k.getId() != 0) {
/*      */         
/* 1168 */         byte xOffSet = 0;
/* 1169 */         byte yOffSet = 0;
/* 1170 */         if (offSetSpawn) {
/*      */           
/* 1172 */           if (Server.rand.nextBoolean())
/* 1173 */             xOffSet = Server.rand.nextBoolean() ? 1 : -1; 
/* 1174 */           if (Server.rand.nextBoolean())
/* 1175 */             yOffSet = Server.rand.nextBoolean() ? 1 : -1; 
/*      */         } 
/* 1177 */         String creatureName = k.getName() + " " + name;
/* 1178 */         Creature newc = Creature.doNew(templateId, this.tower.getPosX() + xOffSet, this.tower.getPosY() + yOffSet, Server.rand
/* 1179 */             .nextInt(360), this.tower.isOnSurface() ? 0 : -1, creatureName, sex, this.tower.getAuxData());
/*      */         
/* 1181 */         newGuard(newc);
/* 1182 */         if (logger.isLoggable(Level.FINER))
/*      */         {
/* 1184 */           logger.finer("WT Created guard " + newc.getName() + " now=" + this.guards.size() + " max=" + 
/* 1185 */               getMaxGuards());
/*      */         }
/*      */       } 
/* 1188 */     } catch (Exception ex) {
/*      */       
/* 1190 */       logger.log(Level.WARNING, "Problem while spawning guard for tower: " + this.tower + ", " + ex.getMessage(), ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void spawnBashWave(byte waveStrength) {
/* 1200 */     byte sex = 0;
/* 1201 */     int templateId = 34;
/* 1202 */     byte templateKingdom = this.tower.getAuxData();
/* 1203 */     Kingdom kingdom = Kingdoms.getKingdom(this.tower.getAuxData());
/* 1204 */     if (kingdom != null)
/* 1205 */       templateKingdom = kingdom.getTemplate(); 
/* 1206 */     if (templateKingdom == 3) {
/*      */       
/* 1208 */       templateId = 35;
/*      */     }
/* 1210 */     else if (templateKingdom == 2) {
/*      */       
/* 1212 */       templateId = 36;
/*      */     }
/* 1214 */     else if (templateKingdom == 4) {
/*      */       
/* 1216 */       templateId = 67;
/*      */     } 
/*      */ 
/*      */     
/* 1220 */     if (this.guards.size() < getMaxPossibleGuards())
/*      */     {
/* 1222 */       for (int j = this.guards.size(); j < getMaxPossibleGuards(); j++)
/*      */       {
/* 1224 */         spawnGuard(templateId, "tower guard", true);
/*      */       }
/*      */     }
/*      */     
/* 1228 */     int spawnCount = getTowerBashSpawnCount(getTowerSpawnMod(), waveStrength);
/* 1229 */     for (int i = 0; i < spawnCount; i++)
/*      */     {
/* 1231 */       spawnGuard(8, "Captain", true);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean hasNearbyAlliedTower(int tilex, int tiley, byte founderKingdom) {
/* 1246 */     GuardTower closest = null;
/* 1247 */     int minDist = Integer.MAX_VALUE;
/*      */     
/* 1249 */     for (GuardTower tower : Kingdoms.getTowers().values()) {
/*      */       
/* 1251 */       int distx = Math.abs(tower.getTower().getTileX() - tilex);
/* 1252 */       int disty = Math.abs(tower.getTower().getTileY() - tiley);
/*      */       
/* 1254 */       int tileDistance = Math.max(distx, disty);
/* 1255 */       if (tileDistance <= minDist) {
/*      */         
/* 1257 */         minDist = tileDistance;
/* 1258 */         closest = tower;
/*      */       } 
/*      */     } 
/* 1261 */     if (closest != null)
/*      */     {
/*      */ 
/*      */ 
/*      */       
/* 1266 */       return (minDist <= 100 && closest.getTower().getKingdom() == founderKingdom);
/*      */     }
/* 1268 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void canConstructTower(Creature performer, Item realTarget) throws NoSuchItemException {
/* 1281 */     if (!performer.isOnSurface() || !realTarget.isOnSurface()) {
/*      */       
/* 1283 */       performer.getCommunicator().sendAlertServerMessage("You can't construct the tower now; you can't build this below surface.");
/*      */ 
/*      */       
/* 1286 */       throw new NoSuchItemException("Below surface.");
/*      */     } 
/*      */ 
/*      */     
/* 1290 */     VolaTile targTile = Zones.getTileOrNull(realTarget.getTileX(), realTarget.getTileY(), true);
/* 1291 */     if (targTile != null)
/*      */     {
/* 1293 */       if (targTile.isTransition()) {
/*      */         
/* 1295 */         performer.getCommunicator().sendAlertServerMessage("You can't construct the tower here - the foundation is not stable enough.");
/*      */ 
/*      */         
/* 1298 */         throw new NoSuchItemException("On cave opening.");
/*      */       } 
/*      */     }
/*      */     
/* 1302 */     int tilex = realTarget.getTileX();
/* 1303 */     int tiley = realTarget.getTileY();
/* 1304 */     boolean onSurface = realTarget.isOnSurface();
/*      */ 
/*      */     
/* 1307 */     if (Terraforming.isTileUnderWater(1, tilex, tiley, onSurface)) {
/*      */       
/* 1309 */       performer.getCommunicator().sendAlertServerMessage("You can't construct the tower now; the ground is not solid here.");
/*      */ 
/*      */       
/* 1312 */       throw new NoSuchItemException("Too wet.");
/*      */     } 
/*      */     
/* 1315 */     int mindist = Kingdoms.minKingdomDist;
/*      */ 
/*      */     
/* 1318 */     if (Kingdoms.isTowerTooNear(tilex, tiley, onSurface, false)) {
/*      */       
/* 1320 */       performer.getCommunicator().sendAlertServerMessage("You can't construct the tower now; another tower is too near.");
/*      */ 
/*      */       
/* 1323 */       throw new NoSuchItemException("Too close to another tower.");
/*      */     } 
/* 1325 */     if (Features.Feature.TOWER_CHAINING.isEnabled() && 
/* 1326 */       !hasNearbyAlliedTower(tilex, tiley, performer.getKingdomId()) && performer
/* 1327 */       .getKingdomId() != 4) {
/*      */       
/* 1329 */       performer.getCommunicator().sendAlertServerMessage("You can't construct the tower now; it must be within range of another allied tower.");
/*      */ 
/*      */       
/* 1332 */       throw new NoSuchItemException("Not within range of an allied tower.");
/*      */     } 
/*      */     
/* 1335 */     if (!Zones.isKingdomBlocking(tilex - mindist, tiley - mindist, tilex + mindist, tiley + mindist, performer
/* 1336 */         .getKingdomId())) {
/*      */       
/* 1338 */       performer.getCommunicator().sendAlertServerMessage("You can't construct the tower now; another kingdom is too near.");
/*      */ 
/*      */       
/* 1341 */       throw new NoSuchItemException("Too close to another kingdom.");
/*      */     } 
/*      */     
/* 1344 */     if (!Servers.isThisAHomeServer()) {
/*      */ 
/*      */       
/* 1347 */       if (Terraforming.isTileModBlocked(performer, tilex, tiley, performer.isOnSurface())) {
/* 1348 */         throw new NoSuchItemException("Tile protected by the deities in this area.");
/*      */       }
/*      */       
/* 1351 */       for (Item targ : Items.getWarTargets()) {
/*      */         
/* 1353 */         int maxnorth = Math.max(0, tiley - 60);
/* 1354 */         int maxsouth = Math.min(Zones.worldTileSizeY, tiley + 60);
/* 1355 */         int maxwest = Math.max(0, tilex - 60);
/* 1356 */         int maxeast = Math.min(Zones.worldTileSizeX, tilex + 60);
/* 1357 */         if ((int)targ.getPosX() >> 2 > maxwest && (int)targ.getPosX() >> 2 < maxeast && 
/* 1358 */           (int)targ.getPosY() >> 2 < maxsouth && (int)targ.getPosY() >> 2 > maxnorth) {
/*      */           
/* 1360 */           performer.getCommunicator().sendSafeServerMessage("You cannot construct the tower here, since this is an active battle ground.");
/*      */           
/* 1362 */           throw new NoSuchItemException("Too close to a war target.");
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 1367 */       EndGameItem alt = EndGameItems.getEvilAltar();
/* 1368 */       if (alt != null) {
/*      */         
/* 1370 */         int maxnorth = Math.max(0, tiley - 100);
/* 1371 */         int maxsouth = Math.min(Zones.worldTileSizeY, tiley + 100);
/* 1372 */         int maxeast = Math.max(0, tilex - 100);
/* 1373 */         int maxwest = Math.min(Zones.worldTileSizeX, tilex + 100);
/* 1374 */         if (alt.getItem() != null)
/*      */         {
/* 1376 */           if ((int)alt.getItem().getPosX() >> 2 < maxwest && 
/* 1377 */             (int)alt.getItem().getPosX() >> 2 > maxeast && 
/* 1378 */             (int)alt.getItem().getPosY() >> 2 < maxsouth && 
/* 1379 */             (int)alt.getItem().getPosY() >> 2 > maxnorth)
/*      */           {
/* 1381 */             throw new NoSuchItemException("You cannot construct a tower here, since this is unholy ground.");
/*      */           }
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1388 */       alt = EndGameItems.getGoodAltar();
/* 1389 */       if (alt != null) {
/*      */         
/* 1391 */         int maxnorth = Math.max(0, tiley - 100);
/* 1392 */         int maxsouth = Math.min(Zones.worldTileSizeY, tiley + 100);
/* 1393 */         int maxeast = Math.max(0, tilex - 100);
/* 1394 */         int maxwest = Math.min(Zones.worldTileSizeX, tilex + 100);
/* 1395 */         if (alt.getItem() != null)
/*      */         {
/* 1397 */           if ((int)alt.getItem().getPosX() >> 2 < maxwest && 
/* 1398 */             (int)alt.getItem().getPosX() >> 2 > maxeast && 
/* 1399 */             (int)alt.getItem().getPosY() >> 2 < maxsouth && 
/* 1400 */             (int)alt.getItem().getPosY() >> 2 > maxnorth)
/*      */           {
/* 1402 */             throw new NoSuchItemException("You cannot construct a tower here, since this is holy ground.");
/*      */           }
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void load() {
/* 1419 */     Connection dbcon = null;
/* 1420 */     PreparedStatement ps = null;
/* 1421 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 1424 */       dbcon = DbConnector.getZonesDbCon();
/* 1425 */       ps = dbcon.prepareStatement("SELECT CREATUREID, RETURNED FROM TOWERGUARDS WHERE TOWERID=?");
/* 1426 */       ps.setLong(1, this.tower.getWurmId());
/* 1427 */       rs = ps.executeQuery();
/* 1428 */       while (rs.next()) {
/*      */         
/* 1430 */         long creatureid = rs.getLong("CREATUREID");
/* 1431 */         boolean returned = rs.getBoolean("RETURNED");
/*      */         
/*      */         try {
/* 1434 */           Creature guard = Creatures.getInstance().getCreature(creatureid);
/* 1435 */           if (logger.isLoggable(Level.FINER))
/*      */           {
/* 1437 */             logger.finer("GT Loaded " + guard.getName());
/*      */           }
/* 1439 */           if (!returned) {
/*      */             
/* 1441 */             if (!this.guards.contains(guard))
/* 1442 */               this.guards.add(guard); 
/* 1443 */             guard.setGuardTower(this);
/*      */             
/*      */             continue;
/*      */           } 
/* 1447 */           if (!this.freeGuards.contains(guard)) {
/* 1448 */             this.freeGuards.add(guard);
/*      */           }
/*      */         }
/* 1451 */         catch (NoSuchCreatureException nsc) {
/*      */           
/* 1453 */           Connection dbcon2 = null;
/* 1454 */           PreparedStatement ps2 = null;
/*      */           
/*      */           try {
/* 1457 */             logger.log(Level.WARNING, "Deleting from towerguards where creatureid=" + creatureid);
/* 1458 */             dbcon2 = DbConnector.getZonesDbCon();
/* 1459 */             ps2 = dbcon2.prepareStatement("DELETE FROM TOWERGUARDS WHERE CREATUREID=?");
/* 1460 */             ps2.setLong(1, creatureid);
/* 1461 */             ps2.executeUpdate();
/*      */           }
/* 1463 */           catch (SQLException ex) {
/*      */             
/* 1465 */             logger.log(Level.INFO, "Failed to delete tower creature " + creatureid, ex);
/*      */           }
/*      */           finally {
/*      */             
/* 1469 */             DbUtilities.closeDatabaseObjects(ps2, null);
/* 1470 */             DbConnector.returnConnection(dbcon2);
/*      */           }
/*      */         
/*      */         } 
/*      */       } 
/* 1475 */     } catch (SQLException sqx) {
/*      */       
/* 1477 */       logger.log(Level.WARNING, "Failed to load guards for tower with id " + this.tower.getWurmId(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1481 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 1482 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1489 */     long lWurmID = (this.tower != null) ? this.tower.getWurmId() : -1L;
/* 1490 */     return "GuardTower [WurmID: " + lWurmID + ", Kingdom: " + Kingdoms.getNameFor(getKingdom()) + ", #guards: " + this.guards
/* 1491 */       .size() + ", #freeGuards: " + this.freeGuards.size() + ", Item: " + this.tower + ']';
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\kingdom\GuardTower.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */