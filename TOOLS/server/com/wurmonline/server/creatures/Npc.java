/*      */ package com.wurmonline.server.creatures;
/*      */ 
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.server.FailedException;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.Message;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.NoSuchPlayerException;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.WurmId;
/*      */ import com.wurmonline.server.behaviours.Action;
/*      */ import com.wurmonline.server.behaviours.ActionEntry;
/*      */ import com.wurmonline.server.behaviours.Behaviour;
/*      */ import com.wurmonline.server.behaviours.BehaviourDispatcher;
/*      */ import com.wurmonline.server.bodys.Wound;
/*      */ import com.wurmonline.server.creatures.ai.ChatManager;
/*      */ import com.wurmonline.server.creatures.ai.NoPathException;
/*      */ import com.wurmonline.server.creatures.ai.Path;
/*      */ import com.wurmonline.server.creatures.ai.PathFinder;
/*      */ import com.wurmonline.server.creatures.ai.PathTile;
/*      */ import com.wurmonline.server.deities.Deities;
/*      */ import com.wurmonline.server.deities.Deity;
/*      */ import com.wurmonline.server.epic.EpicMission;
/*      */ import com.wurmonline.server.epic.EpicServerStatus;
/*      */ import com.wurmonline.server.epic.Hota;
/*      */ import com.wurmonline.server.items.CreationEntry;
/*      */ import com.wurmonline.server.items.CreationMatrix;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.ItemFactory;
/*      */ import com.wurmonline.server.items.ItemTemplate;
/*      */ import com.wurmonline.server.items.ItemTemplateFactory;
/*      */ import com.wurmonline.server.items.NoSuchTemplateException;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.skills.NoSuchSkillException;
/*      */ import com.wurmonline.server.structures.Wall;
/*      */ import com.wurmonline.server.tutorial.MissionTrigger;
/*      */ import com.wurmonline.server.tutorial.MissionTriggers;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.villages.Villages;
/*      */ import com.wurmonline.server.zones.FocusZone;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import java.util.List;
/*      */ import java.util.Random;
/*      */ import javax.annotation.Nullable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Npc
/*      */   extends Creature
/*      */ {
/*   57 */   static final Random faceRandom = new Random();
/*   58 */   int lastX = 0; int lastY = 0;
/*      */   final ChatManager chatManager;
/*      */   LongTarget longTarget;
/*   61 */   int longTargetAttempts = 0;
/*   62 */   int passiveCounter = 0;
/*      */   
/*      */   int MAXSEED;
/*      */ 
/*      */   
/*      */   public Npc(CreatureTemplate aTemplate) throws Exception
/*      */   {
/*   69 */     super(aTemplate);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  112 */     this.MAXSEED = 100; this.chatManager = new ChatManager(this); } public Npc(long aId) throws Exception { super(aId); this.MAXSEED = 100; this.chatManager = new ChatManager(this); } public final ChatManager getChatManager() { return this.chatManager; } public final byte getKingdomId() { if (isAggHuman()) return 0;  return this.status.kingdom; } public Npc() throws Exception { this.MAXSEED = 100; this.chatManager = new ChatManager(this); }
/*      */   public final boolean isAggHuman() { return (this.status.modtype == 2); }
/*      */   public final void pollNPCChat() { getChatManager().checkChats(); }
/*  115 */   public final void pollNPC() { checkItemSpawn(); if (this.passiveCounter-- == 0) doSomething();  } private final void doSomething() { if (!isFighting())
/*      */     {
/*  117 */       if (this.target == -10L)
/*      */       {
/*  119 */         if (!capturePillar()) {
/*      */           
/*  121 */           if (!performLongTargetAction()) {
/*      */             
/*  123 */             if (getStatus().getPath() == null && Server.rand.nextBoolean())
/*      */             {
/*  125 */               startPathing(0);
/*  126 */               setPassiveCounter(120);
/*      */             }
/*      */             else
/*      */             {
/*  130 */               int seed = Server.rand.nextInt(this.MAXSEED);
/*  131 */               if (seed < 10) {
/*      */                 
/*  133 */                 if ((getStatus()).damage > 0) {
/*      */                   
/*  135 */                   Wound[] wounds = getBody().getWounds().getWounds();
/*  136 */                   if (wounds.length > 0) {
/*      */                     
/*  138 */                     Wound rand = wounds[Server.rand.nextInt(wounds.length)];
/*  139 */                     if (Server.rand.nextBoolean()) {
/*      */                       
/*  141 */                       rand.setBandaged(true);
/*  142 */                       if (Server.rand.nextBoolean()) {
/*  143 */                         rand.setHealeff((byte)(Server.rand.nextInt(70) + 30));
/*      */                       }
/*      */                     } else {
/*  146 */                       rand.heal();
/*      */                     } 
/*      */                   } 
/*  149 */                 }  setPassiveCounter(30);
/*      */               } 
/*  151 */               if (seed < 20) {
/*      */                 
/*  153 */                 Item[] allItems = getInventory().getAllItems(false);
/*  154 */                 if (allItems.length > 0) {
/*      */                   
/*  156 */                   Item rand = allItems[Server.rand.nextInt(allItems.length)];
/*      */                   
/*      */                   try {
/*  159 */                     if (rand.isFood()) {
/*  160 */                       BehaviourDispatcher.action(this, this.communicator, -10L, rand.getWurmId(), (short)182);
/*  161 */                     } else if (rand.isLiquid()) {
/*  162 */                       BehaviourDispatcher.action(this, this.communicator, -10L, rand.getWurmId(), (short)183);
/*      */                     } else {
/*  164 */                       BehaviourDispatcher.action(this, this.communicator, -10L, rand.getWurmId(), (short)118);
/*      */                     } 
/*  166 */                   } catch (Exception exception) {}
/*      */                 } 
/*      */ 
/*      */ 
/*      */                 
/*  171 */                 setPassiveCounter(30);
/*      */               } 
/*  173 */               if (seed < 30) {
/*      */                 
/*  175 */                 if (getCurrentTile() != null) {
/*      */                   
/*      */                   try {
/*      */                     
/*  179 */                     Item[] allItems = getInventory().getAllItems(false);
/*  180 */                     Item[] groundItems = getCurrentTile().getItems();
/*  181 */                     Item rand = null;
/*  182 */                     if (allItems.length > 0)
/*      */                     {
/*  184 */                       rand = allItems[Server.rand.nextInt(allItems.length)];
/*      */                     }
/*  186 */                     if (groundItems.length > 0) {
/*      */                       
/*  188 */                       Item targ = groundItems[Server.rand.nextInt(groundItems.length)];
/*  189 */                       if (Server.rand.nextBoolean() && getCurrentTile().getVillage() == null) {
/*      */                         
/*  191 */                         if (Server.rand.nextInt(4) == 0 && targ != null && targ.isHollow() && targ.testInsertItem(rand)) {
/*  192 */                           targ.insertItem(rand);
/*  193 */                         } else if (canCarry(targ.getWeightGrams())) {
/*      */                           
/*  195 */                           BehaviourDispatcher.action(this, this.communicator, -10L, targ.getWurmId(), (short)6);
/*      */                         }
/*  197 */                         else if (targ.isHollow()) {
/*      */                           
/*  199 */                           Item[] containeds = targ.getAllItems(false);
/*  200 */                           if (containeds.length > 0) {
/*      */                             
/*  202 */                             Item targ2 = containeds[Server.rand.nextInt(containeds.length)];
/*  203 */                             if (!targ2.isBodyPart() && !targ2.isNoTake()) {
/*      */                               
/*  205 */                               BehaviourDispatcher.action(this, this.communicator, -10L, targ2.getWurmId(), (short)6);
/*  206 */                               wearItems();
/*      */                             } 
/*      */                           } 
/*      */                         } 
/*      */                       } else {
/*      */                         
/*  212 */                         BehaviourDispatcher.action(this, this.communicator, -10L, targ.getWurmId(), (short)162);
/*      */                       } 
/*      */                     } else {
/*  215 */                       BehaviourDispatcher.action(this, this.communicator, -10L, rand.getWurmId(), (short)162);
/*      */                     } 
/*  217 */                     setPassiveCounter(30);
/*      */                   }
/*  219 */                   catch (Exception exception) {}
/*      */                 }
/*      */ 
/*      */ 
/*      */                 
/*  224 */                 setPassiveCounter(10);
/*      */               } 
/*  226 */               if (seed < 40) {
/*      */                 
/*  228 */                 if (getCurrentTile() != null) {
/*      */                   
/*      */                   try {
/*      */                     
/*  232 */                     Item[] allItems = getInventory().getAllItems(false);
/*  233 */                     Item[] groundItems = getCurrentTile().getItems();
/*  234 */                     Item rand = null;
/*  235 */                     if (allItems.length > 0)
/*      */                     {
/*  237 */                       rand = allItems[Server.rand.nextInt(allItems.length)];
/*      */                     }
/*  239 */                     if (groundItems.length > 0) {
/*      */                       
/*  241 */                       Item targ = groundItems[Server.rand.nextInt(groundItems.length)];
/*  242 */                       if (Server.rand.nextBoolean() && getCurrentTile().getVillage() == null) {
/*      */                         
/*  244 */                         if (Server.rand.nextInt(4) == 0 && targ != null && targ.isHollow() && targ.testInsertItem(rand)) {
/*  245 */                           targ.insertItem(rand);
/*  246 */                         } else if (canCarry(targ.getWeightGrams())) {
/*      */                           
/*  248 */                           BehaviourDispatcher.action(this, this.communicator, -10L, targ.getWurmId(), (short)6);
/*      */                         }
/*  250 */                         else if (targ.isHollow()) {
/*      */                           
/*  252 */                           Item[] containeds = targ.getAllItems(false);
/*  253 */                           if (containeds.length > 0) {
/*      */                             
/*  255 */                             Item targ2 = containeds[Server.rand.nextInt(containeds.length)];
/*  256 */                             if (!targ2.isBodyPart() && !targ2.isNoTake())
/*      */                             {
/*  258 */                               BehaviourDispatcher.action(this, this.communicator, -10L, targ2.getWurmId(), (short)6);
/*  259 */                               wearItems();
/*      */                             }
/*      */                           
/*      */                           } 
/*      */                         } 
/*      */                       } else {
/*      */                         
/*  266 */                         if (targ.isHollow()) {
/*      */                           
/*  268 */                           Item[] containeds = targ.getAllItems(false);
/*  269 */                           if (containeds.length > 0 && Server.rand.nextBoolean())
/*      */                           {
/*  271 */                             targ = containeds[Server.rand.nextInt(containeds.length)];
/*      */                           }
/*      */                         } 
/*  274 */                         Behaviour behaviour = Action.getBehaviour(targ.getWurmId(), isOnSurface());
/*  275 */                         BehaviourDispatcher.RequestParam param = BehaviourDispatcher.requestActionForItemsBodyIdsCoinIds(this, targ.getWurmId(), rand, behaviour);
/*  276 */                         List<ActionEntry> actions = param.getAvailableActions();
/*  277 */                         if (actions.size() > 0) {
/*      */                           
/*  279 */                           ActionEntry ae = actions.get(Server.rand.nextInt(actions.size()));
/*  280 */                           if (ae.getNumber() > 0) {
/*  281 */                             BehaviourDispatcher.action(this, this.communicator, rand.getWurmId(), targ.getWurmId(), ae.getNumber());
/*      */                           }
/*      */                         } 
/*      */                       } 
/*  285 */                     } else if (rand != null) {
/*  286 */                       BehaviourDispatcher.action(this, this.communicator, -10L, rand.getWurmId(), (short)162);
/*      */                     } 
/*  288 */                     setPassiveCounter(30);
/*      */                   }
/*  290 */                   catch (Exception exception) {}
/*      */                 }
/*      */ 
/*      */ 
/*      */                 
/*  295 */                 setPassiveCounter(10);
/*      */               } 
/*  297 */               if (seed < 50) {
/*      */                 
/*  299 */                 if (getCurrentTile() != null) {
/*      */                   
/*      */                   try {
/*      */                     
/*  303 */                     Item[] allItems = getInventory().getAllItems(false);
/*  304 */                     Item rand = null;
/*  305 */                     if (allItems.length > 0) {
/*      */                       
/*  307 */                       boolean abilused = false;
/*  308 */                       for (Item abil : allItems) {
/*      */                         
/*  310 */                         if (abil.isAbility())
/*      */                         {
/*  312 */                           if (Server.rand.nextBoolean()) {
/*      */                             
/*  314 */                             BehaviourDispatcher.action(this, this.communicator, -10L, abil.getWurmId(), (short)118);
/*  315 */                             abilused = true;
/*      */                             break;
/*      */                           } 
/*      */                         }
/*      */                       } 
/*  320 */                       if (!abilused) {
/*      */                         
/*  322 */                         rand = allItems[Server.rand.nextInt(allItems.length)];
/*  323 */                         if (Server.rand.nextInt(5) == 0 && !rand.isEpicTargetItem() && rand.isUnique() && !rand.isAbility() && !rand.isMagicStaff() && !rand.isRoyal()) {
/*      */                           
/*  325 */                           rand.putItemInfrontof(this);
/*      */                         }
/*  327 */                         else if (isOnSurface()) {
/*      */                           
/*  329 */                           long targTile = Tiles.getTileId(getTileX(), getTileY(), 0);
/*  330 */                           Behaviour behaviour = Action.getBehaviour(targTile, isOnSurface());
/*  331 */                           BehaviourDispatcher.RequestParam param = BehaviourDispatcher.requestActionForTiles(this, targTile, true, rand, behaviour);
/*  332 */                           List<ActionEntry> actions = param.getAvailableActions();
/*  333 */                           if (actions.size() > 0) {
/*      */                             
/*  335 */                             ActionEntry ae = actions.get(Server.rand.nextInt(actions.size()));
/*  336 */                             if (ae.getNumber() > 0) {
/*  337 */                               BehaviourDispatcher.action(this, this.communicator, (rand == null) ? -10L : rand.getWurmId(), targTile, ae.getNumber());
/*      */                             }
/*      */                           } 
/*      */                         } 
/*      */                       } 
/*  342 */                     } else if (isOnSurface()) {
/*      */                       
/*  344 */                       long targTile = Tiles.getTileId(getTileX(), getTileY(), 0);
/*  345 */                       Behaviour behaviour = Action.getBehaviour(targTile, isOnSurface());
/*  346 */                       BehaviourDispatcher.RequestParam param = BehaviourDispatcher.requestActionForTiles(this, targTile, true, null, behaviour);
/*  347 */                       List<ActionEntry> actions = param.getAvailableActions();
/*  348 */                       if (actions.size() > 0) {
/*      */                         
/*  350 */                         ActionEntry ae = actions.get(Server.rand.nextInt(actions.size()));
/*  351 */                         if (ae.getNumber() > 0)
/*  352 */                           BehaviourDispatcher.action(this, this.communicator, -10L, targTile, ae.getNumber()); 
/*      */                       } 
/*      */                     } 
/*  355 */                     setPassiveCounter(30);
/*      */                   }
/*  357 */                   catch (Exception exception) {}
/*      */                 }
/*      */ 
/*      */ 
/*      */                 
/*  362 */                 setPassiveCounter(10);
/*      */               } 
/*  364 */               if (seed < 70) {
/*      */                 
/*  366 */                 if (getCurrentTile() != null) {
/*      */                   
/*      */                   try {
/*      */                     
/*  370 */                     Item[] allItems = getInventory().getAllItems(false);
/*  371 */                     Item rand = null;
/*  372 */                     if (allItems.length > 0)
/*      */                     {
/*  374 */                       rand = allItems[Server.rand.nextInt(allItems.length)];
/*      */                     }
/*  376 */                     boolean found = false;
/*  377 */                     Creature[] crets = null;
/*  378 */                     for (int x = -2; x <= 2; x++) {
/*  379 */                       for (int y = -2; y <= 2; y++) {
/*      */                         
/*  381 */                         VolaTile t = Zones.getTileOrNull(Zones.safeTileX(getTileX() + x), Zones.safeTileY(getTileY() + y), isOnSurface());
/*  382 */                         if (t != null) {
/*      */                           
/*  384 */                           crets = t.getCreatures();
/*  385 */                           if (crets.length > 0) {
/*      */                             
/*  387 */                             Creature targC = crets[Server.rand.nextInt(crets.length)];
/*  388 */                             Behaviour behaviour = Action.getBehaviour(targC.getWurmId(), isOnSurface());
/*  389 */                             BehaviourDispatcher.RequestParam param = BehaviourDispatcher.requestActionForCreaturesPlayers(this, targC.getWurmId(), rand, 
/*  390 */                                 targC.isPlayer() ? 0 : 1, behaviour);
/*  391 */                             List<ActionEntry> actions = param.getAvailableActions();
/*  392 */                             if (actions.size() > 0) {
/*      */                               
/*  394 */                               ActionEntry ae = actions.get(Server.rand.nextInt(actions.size()));
/*  395 */                               if (ae.getNumber() > 0)
/*  396 */                                 BehaviourDispatcher.action(this, this.communicator, (rand == null) ? -10L : rand.getWurmId(), targC.getWurmId(), ae.getNumber()); 
/*  397 */                               setPassiveCounter(30);
/*  398 */                               found = true; break;
/*      */                             } 
/*      */                           } 
/*      */                         } 
/*      */                       } 
/*      */                     } 
/*  404 */                     if (!found) {
/*      */                       
/*  406 */                       long targTile = Tiles.getTileId(getTileX() - 1 + Server.rand.nextInt(2), getTileY() - 1 + Server.rand.nextInt(2), 0, isOnSurface());
/*  407 */                       Behaviour behaviour = Action.getBehaviour(targTile, isOnSurface());
/*  408 */                       BehaviourDispatcher.RequestParam param = BehaviourDispatcher.requestActionForTiles(this, targTile, true, rand, behaviour);
/*  409 */                       List<ActionEntry> actions = param.getAvailableActions();
/*  410 */                       if (actions.size() > 0) {
/*      */                         
/*  412 */                         ActionEntry ae = actions.get(Server.rand.nextInt(actions.size()));
/*  413 */                         if (ae.getNumber() > 0)
/*  414 */                           BehaviourDispatcher.action(this, this.communicator, (rand == null) ? -10L : rand.getWurmId(), targTile, ae.getNumber()); 
/*      */                       } 
/*      */                     } 
/*  417 */                     setPassiveCounter(30);
/*      */                   }
/*  419 */                   catch (Exception exception) {}
/*      */                 }
/*      */ 
/*      */ 
/*      */                 
/*  424 */                 setPassiveCounter(10);
/*      */               } 
/*  426 */               if (seed < 80) {
/*      */                 
/*  428 */                 Creature[] crets = null;
/*  429 */                 for (int x = -2; x <= 2; x++) {
/*  430 */                   for (int y = -2; y <= 2; y++) {
/*      */                     
/*  432 */                     VolaTile t = Zones.getTileOrNull(Zones.safeTileX(getTileX() + x), Zones.safeTileY(getTileY() + y), isOnSurface());
/*  433 */                     if (t != null) {
/*      */                       
/*  435 */                       crets = t.getCreatures();
/*  436 */                       if (crets.length > 0) {
/*      */                         
/*      */                         try {
/*      */                           
/*  440 */                           Creature targC = crets[Server.rand.nextInt(crets.length)];
/*  441 */                           Behaviour behaviour = Action.getBehaviour(targC.getWurmId(), isOnSurface());
/*  442 */                           BehaviourDispatcher.RequestParam param = BehaviourDispatcher.requestActionForCreaturesPlayers(this, targC.getWurmId(), null, 
/*  443 */                               targC.isPlayer() ? 0 : 1, behaviour);
/*  444 */                           List<ActionEntry> actions = param.getAvailableActions();
/*  445 */                           if (actions.size() > 0) {
/*      */                             
/*  447 */                             ActionEntry ae = actions.get(Server.rand.nextInt(actions.size()));
/*  448 */                             if ((!ae.isOffensive() || !isFriendlyKingdom(targC.getKingdomId())) && 
/*  449 */                               ae.getNumber() > 0) {
/*  450 */                               BehaviourDispatcher.action(this, this.communicator, -10L, targC.getWurmId(), ae.getNumber());
/*      */                             }
/*      */                             break;
/*      */                           } 
/*  454 */                         } catch (Exception exception) {}
/*      */                       }
/*      */                     } 
/*      */                   } 
/*      */                 } 
/*      */               } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/*      */               try {
/*  466 */                 Item[] allItems = getInventory().getAllItems(false);
/*      */                 
/*  468 */                 if (allItems.length > 2)
/*      */                 {
/*  470 */                   Item rand1 = allItems[Server.rand.nextInt(allItems.length)];
/*  471 */                   Item rand2 = allItems[Server.rand.nextInt(allItems.length)];
/*  472 */                   Behaviour behaviour = Action.getBehaviour(rand2.getWurmId(), isOnSurface());
/*  473 */                   BehaviourDispatcher.RequestParam param = BehaviourDispatcher.requestActionForItemsBodyIdsCoinIds(this, rand2.getWurmId(), rand1, behaviour);
/*  474 */                   List<ActionEntry> actions = param.getAvailableActions();
/*  475 */                   if (actions.size() > 0) {
/*      */                     
/*  477 */                     ActionEntry ae = actions.get(Server.rand.nextInt(actions.size()));
/*  478 */                     if (ae.getNumber() > 0)
/*  479 */                       BehaviourDispatcher.action(this, this.communicator, (rand1 == null) ? -10L : rand1.getWurmId(), rand2.getWurmId(), ae.getNumber()); 
/*      */                   } 
/*  481 */                   setPassiveCounter(30);
/*      */                 }
/*      */               
/*  484 */               } catch (Exception exception) {}
/*      */             
/*      */             }
/*      */           
/*      */           }
/*      */           else {
/*      */             
/*  491 */             setPassiveCounter(180);
/*      */           } 
/*      */         } else {
/*  494 */           setPassiveCounter(30);
/*      */         } 
/*      */       }
/*      */     } }
/*      */ 
/*      */   
/*      */   private void clearLongTarget() {
/*  501 */     this.longTarget = null;
/*  502 */     this.longTargetAttempts = 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isOnLongTargetTile() {
/*  507 */     if (getStatus() == null)
/*  508 */       return false; 
/*  509 */     return (this.longTarget.getTileX() == (int)this.status.getPositionX() >> 2 && this.longTarget.getTileY() == (int)this.status.getPositionY() >> 2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Path findPath(int targetX, int targetY, @Nullable PathFinder pathfinder) throws NoPathException {
/*  516 */     Path path = null;
/*  517 */     PathFinder pf = (pathfinder != null) ? pathfinder : new PathFinder();
/*  518 */     setPathfindcounter(getPathfindCounter() + 1);
/*  519 */     if (getPathfindCounter() < 10 || this.target != -10L || getPower() > 0) {
/*      */       
/*  521 */       path = pf.findPath(this, getTileX(), getTileY(), targetX, targetY, isOnSurface(), 20);
/*      */     } else {
/*      */       
/*  524 */       throw new NoPathException("No pathing now");
/*  525 */     }  if (path != null)
/*  526 */       setPathfindcounter(0); 
/*  527 */     return path;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean capturePillar() {
/*  532 */     if (getCitizenVillage() != null) {
/*      */       
/*  534 */       FocusZone hota = FocusZone.getHotaZone();
/*  535 */       if (hota != null && hota.covers(getTileX(), getTileY()))
/*      */       {
/*  537 */         for (Item i : getCurrentTile().getItems()) {
/*      */           
/*  539 */           if (i.getTemplateId() == 739)
/*      */           {
/*  541 */             if (i.getData1() != getCitizenVillage().getId()) {
/*      */ 
/*      */               
/*      */               try {
/*  545 */                 BehaviourDispatcher.action(this, this.communicator, -10L, i.getWurmId(), (short)504);
/*      */               }
/*  547 */               catch (Exception exception) {}
/*      */ 
/*      */ 
/*      */               
/*  551 */               return true;
/*      */             } 
/*      */           }
/*      */         } 
/*      */       }
/*      */     } 
/*  557 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean performLongTargetAction() {
/*  562 */     if (this.longTarget != null && this.longTarget.getMissionTrigger() > 0) {
/*      */       
/*  564 */       MissionTrigger trigger = MissionTriggers.getTriggerWithId(this.longTarget.getMissionTrigger());
/*  565 */       if (trigger != null)
/*      */       {
/*  567 */         if (Math.abs(this.longTarget.getTileX() - getTileX()) < 3 && Math.abs(this.longTarget.getTileY() - getTileY()) < 3)
/*      */         {
/*  569 */           Item found = null;
/*  570 */           if (trigger.getItemUsedId() > 0) {
/*      */             
/*  572 */             if (trigger.getOnActionPerformed() == 148) {
/*      */               
/*  574 */               CreationEntry ce = CreationMatrix.getInstance().getCreationEntry(trigger.getItemUsedId());
/*  575 */               if (ce != null && 
/*  576 */                 !ce.isAdvanced()) {
/*      */                 
/*      */                 try {
/*      */                   
/*  580 */                   found = ItemFactory.createItem(trigger.getItemUsedId(), 20.0F + Server.rand.nextFloat() * 20.0F, getName());
/*  581 */                   getInventory().insertItem(found, true);
/*  582 */                   if (found.getWeightGrams() > 20000) {
/*  583 */                     found.putItemInfrontof(this);
/*      */                   } else {
/*  585 */                     wearItems();
/*  586 */                   }  MissionTriggers.activateTriggers(this, found, 148, 0L, 1);
/*  587 */                   clearLongTarget();
/*  588 */                   return true;
/*      */                 }
/*  590 */                 catch (Exception exception) {}
/*      */               }
/*      */             } 
/*      */ 
/*      */ 
/*      */             
/*  596 */             for (Item item : getAllItems()) {
/*      */               
/*  598 */               if (item.getTemplateId() == trigger.getItemUsedId())
/*      */               {
/*  600 */                 found = item;
/*      */               }
/*      */             } 
/*  603 */             if (found == null) {
/*      */               
/*      */               try {
/*      */                 
/*  607 */                 found = ItemFactory.createItem(trigger.getItemUsedId(), 20.0F + Server.rand.nextFloat() * 20.0F, getName());
/*  608 */                 getInventory().insertItem(found, true);
/*      */               }
/*  610 */               catch (Exception exception) {}
/*      */             }
/*      */           } 
/*      */ 
/*      */ 
/*      */           
/*  616 */           if (WurmId.getType(trigger.getTarget()) == 1 || WurmId.getType(trigger.getTarget()) == 0) {
/*      */             
/*      */             try {
/*      */               
/*  620 */               Creature c = Server.getInstance().getCreature(trigger.getTarget());
/*  621 */               if (c == null || c.isDead())
/*      */               {
/*  623 */                 clearLongTarget();
/*  624 */                 return true;
/*      */               }
/*      */             
/*  627 */             } catch (NoSuchCreatureException nsc) {
/*      */               
/*  629 */               clearLongTarget();
/*  630 */               return true;
/*      */             }
/*  632 */             catch (NoSuchPlayerException nsp) {
/*      */               
/*  634 */               clearLongTarget();
/*  635 */               return true;
/*      */             } 
/*      */           }
/*  638 */           if (WurmId.getType(trigger.getTarget()) == 3 && trigger.getOnActionPerformed() == 492) {
/*      */             
/*  640 */             int tilenum = Server.surfaceMesh.getTile(getTileX(), getTileY());
/*  641 */             if (!Tiles.isTree(Tiles.decodeType(tilenum)))
/*  642 */               return true; 
/*  643 */             if (found == null)
/*      */             {
/*  645 */               for (Item axe : getBody().getBodyItem().getAllItems(false)) {
/*      */                 
/*  647 */                 if (axe.isWeaponAxe() || axe.isWeaponSlash())
/*      */                 {
/*  649 */                   found = axe;
/*      */                 }
/*      */               } 
/*      */             }
/*  653 */             if (found == null)
/*      */             {
/*  655 */               for (Item axe : getInventory().getAllItems(false)) {
/*      */                 
/*  657 */                 if (axe.isWeaponAxe() || axe.isWeaponSlash())
/*      */                 {
/*  659 */                   found = axe;
/*      */                 }
/*      */               } 
/*      */             }
/*  663 */             if (found == null) {
/*      */               
/*      */               try {
/*      */                 
/*  667 */                 found = ItemFactory.createItem(7, 10.0F, getName());
/*      */               }
/*  669 */               catch (Exception exception) {}
/*      */             }
/*      */ 
/*      */ 
/*      */             
/*  674 */             if ((found != null && found.isWeaponAxe()) || found.isWeaponSlash()) {
/*      */               
/*      */               try {
/*      */                 
/*  678 */                 BehaviourDispatcher.action(this, this.communicator, found.getWurmId(), trigger.getTarget(), (short)96);
/*      */               }
/*  680 */               catch (Exception exception) {}
/*      */             }
/*      */           } 
/*      */ 
/*      */ 
/*      */           
/*  686 */           MissionTriggers.activateTriggers(this, found, trigger.getOnActionPerformed(), trigger.getTarget(), 1);
/*      */           
/*  688 */           clearLongTarget();
/*  689 */           return true;
/*      */         }
/*      */       
/*      */       }
/*  693 */     } else if (this.longTarget != null) {
/*      */       
/*  695 */       if (isOnLongTargetTile()) {
/*      */         
/*  697 */         Item[] currentItems = getCurrentTile().getItems();
/*  698 */         for (Item current : currentItems) {
/*      */           
/*  700 */           if (current.isCorpse() && current.getLastOwnerId() == getWurmId()) {
/*      */             
/*  702 */             for (Item incorpse : current.getAllItems(false)) {
/*      */               
/*  704 */               if (!incorpse.isBodyPart())
/*      */               {
/*  706 */                 getInventory().insertItem(incorpse);
/*      */               }
/*      */             } 
/*  709 */             wearItems();
/*  710 */             Items.destroyItem(current.getWurmId());
/*      */           } 
/*      */         } 
/*  713 */         clearLongTarget();
/*  714 */         return true;
/*      */       } 
/*      */     } 
/*  717 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final PathTile getMoveTarget(int seed) {
/*  723 */     if (getStatus() == null) {
/*  724 */       return null;
/*      */     }
/*  726 */     float lPosX = this.status.getPositionX();
/*  727 */     float lPosY = this.status.getPositionY();
/*  728 */     boolean hasTarget = false;
/*  729 */     int tilePosX = (int)lPosX >> 2;
/*  730 */     int tilePosY = (int)lPosY >> 2;
/*  731 */     int tx = tilePosX;
/*  732 */     int ty = tilePosY;
/*  733 */     if (!isAggHuman() && getCitizenVillage() != null)
/*      */     {
/*  735 */       if (this.longTarget == null) {
/*      */         
/*  737 */         if (Server.rand.nextInt(100) == 0) {
/*      */           
/*  739 */           Player[] players = Players.getInstance().getPlayers();
/*  740 */           for (int x = 0; x < 10; x++) {
/*      */             
/*  742 */             Player p = players[Server.rand.nextInt(players.length)];
/*  743 */             if (p.isWithinDistanceTo(this, 200.0F) && p
/*  744 */               .getPower() == 0) {
/*      */               
/*  746 */               int tile = Server.surfaceMesh.getTile(tilePosX, tilePosY);
/*  747 */               if (!p.isOnSurface())
/*  748 */                 tile = Server.caveMesh.getTile(tilePosX, tilePosY); 
/*  749 */               this.longTarget = new LongTarget(p.getTileX(), p.getTileY(), tile, p.isOnSurface(), p.getFloorLevel(), this);
/*  750 */               if (p.isFriendlyKingdom(getKingdomId())) {
/*  751 */                 getChatManager().createAndSendMessage(p, "Oi.", false); break;
/*      */               } 
/*  753 */               getChatManager().createAndSendMessage(p, "Coming for you.", false);
/*      */               break;
/*      */             } 
/*      */           } 
/*      */         } 
/*  758 */         if (this.longTarget == null && Server.rand.nextInt(10) == 0) {
/*      */           
/*  760 */           Item[] allIts = Items.getAllItems();
/*  761 */           for (Item corpse : allIts) {
/*      */             
/*  763 */             if (corpse.getZoneId() > 0 && corpse.getTemplateId() == 272 && corpse.getLastOwnerId() == getWurmId() && corpse.getName().toLowerCase().contains(getName().toLowerCase())) {
/*      */               
/*  765 */               Item[] contained = corpse.getAllItems(false);
/*  766 */               if (contained.length > 4) {
/*      */                 
/*  768 */                 boolean surf = corpse.isOnSurface();
/*  769 */                 int tile = Server.surfaceMesh.getTile(corpse.getTileX(), corpse.getTileY());
/*  770 */                 if (!surf)
/*  771 */                   tile = Server.caveMesh.getTile(corpse.getTileX(), corpse.getTileY()); 
/*  772 */                 this.longTarget = new LongTarget(corpse.getTileX(), corpse.getTileY(), tile, surf, surf ? 0 : -1, this);
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*  777 */         if (this.longTarget == null && Server.rand.nextInt(10) == 0) {
/*      */           
/*  779 */           EpicMission[] ems = EpicServerStatus.getCurrentEpicMissions();
/*  780 */           for (EpicMission em : ems) {
/*      */             
/*  782 */             if (em.isCurrent()) {
/*      */               
/*  784 */               Deity deity = Deities.getDeity(em.getEpicEntityId());
/*  785 */               if (deity != null)
/*      */               {
/*  787 */                 if (deity.getFavoredKingdom() == getKingdomId())
/*      */                 {
/*  789 */                   for (MissionTrigger trig : MissionTriggers.getAllTriggers()) {
/*      */                     
/*  791 */                     if (trig.getMissionRequired() == em.getMissionId()) {
/*      */                       
/*  793 */                       long target = trig.getTarget();
/*      */                       
/*  795 */                       if (WurmId.getType(target) == 3 || WurmId.getType(target) == 17) {
/*      */                         
/*  797 */                         int x2 = Tiles.decodeTileX(target);
/*  798 */                         int y2 = Tiles.decodeTileY(target);
/*  799 */                         boolean surf = (WurmId.getType(target) == 3);
/*  800 */                         int tile = Server.surfaceMesh.getTile(x2, y2);
/*  801 */                         if (!surf)
/*  802 */                           tile = Server.caveMesh.getTile(x2, y2); 
/*  803 */                         this.longTarget = new LongTarget(x2, y2, tile, surf, surf ? 0 : -1, this);
/*      */                       }
/*  805 */                       else if (WurmId.getType(target) == 2) {
/*      */ 
/*      */                         
/*      */                         try {
/*  809 */                           Item i = Items.getItem(target);
/*  810 */                           int tile = Server.surfaceMesh.getTile(i.getTileX(), i.getTileY());
/*  811 */                           if (!i.isOnSurface())
/*  812 */                             tile = Server.caveMesh.getTile(i.getTileX(), i.getTileY()); 
/*  813 */                           this.longTarget = new LongTarget(i.getTileX(), i.getTileY(), tile, i.isOnSurface(), i.getFloorLevel(), this);
/*      */                         }
/*  815 */                         catch (NoSuchItemException noSuchItemException) {}
/*      */ 
/*      */ 
/*      */                       
/*      */                       }
/*  820 */                       else if (WurmId.getType(target) == 1 || 
/*  821 */                         WurmId.getType(target) == 0) {
/*      */ 
/*      */                         
/*      */                         try {
/*  825 */                           Creature c = Server.getInstance().getCreature(target);
/*  826 */                           int tile = Server.surfaceMesh.getTile(c.getTileX(), c.getTileY());
/*  827 */                           if (!c.isOnSurface())
/*  828 */                             tile = Server.caveMesh.getTile(c.getTileX(), c.getTileY()); 
/*  829 */                           this.longTarget = new LongTarget(c.getTileX(), c.getTileY(), tile, c.isOnSurface(), c.getFloorLevel(), this);
/*      */                         }
/*  831 */                         catch (NoSuchCreatureException noSuchCreatureException) {
/*      */ 
/*      */                         
/*      */                         }
/*  835 */                         catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */ 
/*      */ 
/*      */                       
/*      */                       }
/*  840 */                       else if (WurmId.getType(target) == 5) {
/*      */                         
/*  842 */                         int x = (int)(target >> 32L) & 0xFFFF;
/*  843 */                         int y = (int)(target >> 16L) & 0xFFFF;
/*      */                         
/*  845 */                         Wall wall = Wall.getWall(target);
/*  846 */                         if (wall != null) {
/*      */                           
/*  848 */                           int tile = Server.surfaceMesh.getTile(x, y);
/*  849 */                           this.longTarget = new LongTarget(x, y, tile, true, wall.getFloorLevel(), this);
/*      */                         } 
/*      */                       } 
/*  852 */                       if (this.longTarget != null) {
/*      */                         
/*  854 */                         this.longTarget.setMissionTrigger(trig.getId());
/*  855 */                         this.longTarget.setEpicMission(em.getMissionId());
/*  856 */                         this.longTarget.setMissionTarget(target);
/*      */                       } 
/*      */                     } 
/*      */                   } 
/*      */                 }
/*      */               }
/*      */             } 
/*      */           } 
/*      */         } 
/*  865 */         if (this.longTarget == null && Server.rand.nextInt(10) == 0) {
/*      */           
/*  867 */           if (getCitizenVillage() != null) {
/*      */             
/*  869 */             if (getTileRange(this, getCitizenVillage().getTokenX(), getCitizenVillage().getTokenY()) > 300.0D) {
/*      */               
/*  871 */               int tile = Server.surfaceMesh.getTile(getCitizenVillage().getTokenX(), getCitizenVillage().getTokenY());
/*  872 */               this.longTarget = new LongTarget(getCitizenVillage().getTokenX(), getCitizenVillage().getTokenY(), tile, true, 0, this);
/*      */             } 
/*      */           } else {
/*      */             
/*  876 */             for (Village v : Villages.getVillages()) {
/*      */               
/*  878 */               if (v.isPermanent && v.kingdom == getKingdomId())
/*      */               {
/*  880 */                 if (getTileRange(this, v.getTokenX(), v.getTokenY()) > 300.0D) {
/*      */                   
/*  882 */                   int tile = Server.surfaceMesh.getTile(v.getTokenX(), v.getTokenY());
/*  883 */                   this.longTarget = new LongTarget(v.getTokenX(), v.getTokenY(), tile, true, 0, this);
/*      */                 }  } 
/*      */             } 
/*      */           } 
/*  887 */           if (this.longTarget != null) {
/*      */             
/*  889 */             int seedh = Server.rand.nextInt(5);
/*  890 */             String mess = "Think I'll head home again...";
/*  891 */             switch (seedh) {
/*      */               
/*      */               case 0:
/*  894 */                 mess = "Time to go home!";
/*      */                 break;
/*      */               case 1:
/*  897 */                 mess = "Enough of this. Home Sweet Home.";
/*      */                 break;
/*      */               case 2:
/*  900 */                 mess = "Heading home. Are you coming?";
/*      */                 break;
/*      */               case 3:
/*  903 */                 mess = "I will go home now.";
/*      */                 break;
/*      */               case 4:
/*  906 */                 mess = "That's it. I'm going home.";
/*      */                 break;
/*      */               default:
/*  909 */                 mess = "Think I'll go home for a while.";
/*      */                 break;
/*      */             } 
/*      */             
/*  913 */             if (getCurrentTile() != null) {
/*      */               
/*  915 */               Message m = new Message(this, (byte)0, ":Local", "<" + getName() + "> " + mess);
/*  916 */               getCurrentTile().broadCastMessage(m);
/*      */             } 
/*      */           } 
/*      */         } 
/*  920 */         if (this.longTarget == null && Server.rand.nextInt(100) == 0)
/*      */         {
/*  922 */           if (getCitizenVillage() != null) {
/*      */             
/*  924 */             FocusZone hota = FocusZone.getHotaZone();
/*  925 */             if (hota != null)
/*      */             {
/*  927 */               if (!hota.covers(getTileX(), getTileY())) {
/*      */                 
/*  929 */                 int hx = hota.getStartX() + Server.rand.nextInt(hota.getEndX() - hota.getStartX());
/*  930 */                 int hy = hota.getStartY() + Server.rand.nextInt(hota.getEndY() - hota.getStartY());
/*  931 */                 int tile = Server.surfaceMesh.getTile(hx, hy);
/*  932 */                 this.longTarget = new LongTarget(hx, hy, tile, true, 0, this);
/*  933 */                 int seedh = Server.rand.nextInt(5);
/*  934 */                 String mess = "Think I'll go hunt for some pillars a bit...";
/*  935 */                 switch (seedh) {
/*      */                   
/*      */                   case 0:
/*  938 */                     mess = "Anyone in the Hunt of the Ancients is in trouble now!";
/*      */                     break;
/*      */                   case 1:
/*  941 */                     mess = "Going to check out what happens in the Hunt.";
/*      */                     break;
/*      */                   case 2:
/*  944 */                     mess = "Heading to join the Hunt. Coming with me?";
/*      */                     break;
/*      */                   case 3:
/*  947 */                     mess = "Going to head to the Hunt of the Ancients. You interested?";
/*      */                     break;
/*      */                   case 4:
/*  950 */                     mess = "I want to do some gloryhunting in the HOTA.";
/*      */                     break;
/*      */                   default:
/*  953 */                     mess = "Think I'll go join the hunt a bit...";
/*      */                     break;
/*      */                 } 
/*      */                 
/*  957 */                 if (getCurrentTile() != null) {
/*      */                   
/*  959 */                   Message m = new Message(this, (byte)0, ":Local", "<" + getName() + "> " + mess);
/*  960 */                   getCurrentTile().broadCastMessage(m);
/*      */                 } 
/*      */               } 
/*      */             }
/*      */           } 
/*      */         }
/*  966 */         if (this.longTarget != null) {
/*  967 */           return this.longTarget;
/*      */         }
/*      */       } else {
/*      */         
/*  971 */         boolean clear = false;
/*  972 */         if (this.longTarget.getCreatureTarget() != null && this.longTarget.getTileX() != this.longTarget.getCreatureTarget().getTileX())
/*  973 */           this.longTarget.setTileX(this.longTarget.getCreatureTarget().getTileX()); 
/*  974 */         if (this.longTarget.getCreatureTarget() != null && this.longTarget.getTileY() != this.longTarget.getCreatureTarget().getTileY())
/*  975 */           this.longTarget.setTileY(this.longTarget.getCreatureTarget().getTileY()); 
/*  976 */         if (this.longTarget.getEpicMission() > 0) {
/*      */           
/*  978 */           EpicMission em = EpicServerStatus.getEpicMissionForMission(this.longTarget.getEpicMission());
/*  979 */           if (em == null || !em.isCurrent() || em.isCompleted())
/*      */           {
/*  981 */             clear = true;
/*      */           }
/*      */         } 
/*  984 */         if (Math.abs(this.longTarget.getTileX() - tx) < 20 && Math.abs(this.longTarget.getTileY() - ty) < 20) {
/*      */           
/*  986 */           if (Math.abs(this.longTarget.getTileX() - tx) < 10 && Math.abs(this.longTarget.getTileY() - ty) < 10)
/*      */           {
/*  988 */             if (this.longTarget.getCreatureTarget() != null)
/*      */             {
/*  990 */               if (!this.longTarget.getCreatureTarget().isFriendlyKingdom(getKingdomId())) {
/*      */                 
/*  992 */                 setTarget(this.longTarget.getCreatureTarget().getWurmId(), false);
/*  993 */                 clear = true;
/*      */               } 
/*      */             }
/*      */           }
/*  997 */           if (isOnLongTargetTile() || this.longTargetAttempts++ > 50) {
/*      */             
/*  999 */             clear = true;
/*      */           } else {
/*      */             
/* 1002 */             return this.longTarget;
/*      */           } 
/* 1004 */         } else if (System.currentTimeMillis() - this.longTarget.getStartTime() > 3600000L) {
/* 1005 */           clear = true;
/* 1006 */         }  if (clear)
/* 1007 */           clearLongTarget(); 
/*      */       } 
/*      */     }
/* 1010 */     boolean flee = false;
/* 1011 */     if (this.target == -10L || this.fleeCounter > 0)
/*      */     {
/* 1013 */       if (isTypeFleeing() || this.fleeCounter > 0)
/*      */       {
/* 1015 */         if (isOnSurface())
/*      */         {
/* 1017 */           if (Server.rand.nextBoolean()) {
/*      */             
/* 1019 */             if (getCurrentTile() != null && getCurrentTile().getVillage() != null)
/*      */             {
/* 1021 */               Long[] crets = getVisionArea().getSurface().getCreatures();
/* 1022 */               for (Long lCret : crets)
/*      */               {
/*      */                 
/*      */                 try {
/* 1026 */                   Creature cret = Server.getInstance().getCreature(lCret.longValue());
/* 1027 */                   if (cret.getPower() == 0 && (cret
/* 1028 */                     .isPlayer() || cret.isAggHuman() || cret.isCarnivore() || cret.isMonster())) {
/*      */                     
/* 1030 */                     if (cret.getPosX() > getPosX()) {
/* 1031 */                       tilePosX -= Server.rand.nextInt(6);
/*      */                     } else {
/* 1033 */                       tilePosX += Server.rand.nextInt(6);
/* 1034 */                     }  if (cret.getPosY() > getPosY()) {
/* 1035 */                       tilePosY -= Server.rand.nextInt(6);
/*      */                     } else {
/* 1037 */                       tilePosY += Server.rand.nextInt(6);
/* 1038 */                     }  flee = true;
/*      */                     
/*      */                     break;
/*      */                   } 
/* 1042 */                 } catch (Exception exception) {}
/*      */               }
/*      */             
/*      */             }
/*      */           
/*      */           }
/*      */           else {
/*      */             
/* 1050 */             for (Player p : Players.getInstance().getPlayers()) {
/*      */               
/* 1052 */               if ((p.getPower() == 0 || Servers.localServer.testServer) && p.getVisionArea() != null && p
/* 1053 */                 .getVisionArea().getSurface() != null && p
/* 1054 */                 .getVisionArea().getSurface().containsCreature(this)) {
/*      */                 
/* 1056 */                 if (p.getPosX() > getPosX()) {
/* 1057 */                   tilePosX -= Server.rand.nextInt(6);
/*      */                 } else {
/* 1059 */                   tilePosX += Server.rand.nextInt(6);
/* 1060 */                 }  if (p.getPosY() > getPosY()) {
/* 1061 */                   tilePosY -= Server.rand.nextInt(6);
/*      */                 } else {
/* 1063 */                   tilePosY += Server.rand.nextInt(6);
/* 1064 */                 }  flee = true;
/*      */                 break;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         }
/*      */       }
/*      */     }
/* 1072 */     if (!flee && !hasTarget) {
/*      */       
/* 1074 */       VolaTile currTile = getCurrentTile();
/* 1075 */       if (currTile != null) {
/*      */         
/* 1077 */         int rand = Server.rand.nextInt(9);
/* 1078 */         int tpx = currTile.getTileX() + 4 - rand;
/* 1079 */         rand = Server.rand.nextInt(9);
/* 1080 */         int tpy = currTile.getTileY() + 4 - rand;
/* 1081 */         totx += currTile.getTileX() - tpx;
/* 1082 */         toty += currTile.getTileY() - tpy;
/*      */         
/* 1084 */         if (this.longTarget != null) {
/*      */           
/* 1086 */           if (Math.abs(this.longTarget.getTileX() - getTileX()) < 20) {
/*      */             
/* 1088 */             tpx = this.longTarget.getTileX();
/*      */           }
/*      */           else {
/*      */             
/* 1092 */             tpx = getTileX() + 5 + Server.rand.nextInt(6);
/* 1093 */             if (getTileX() > this.longTarget.getTileX())
/* 1094 */               tpx = getTileX() - 5 - Server.rand.nextInt(6); 
/*      */           } 
/* 1096 */           if (Math.abs(this.longTarget.getTileY() - getTileY()) < 20) {
/*      */             
/* 1098 */             tpy = this.longTarget.getTileY();
/*      */           }
/*      */           else {
/*      */             
/* 1102 */             tpy = getTileY() + 5 + Server.rand.nextInt(6);
/* 1103 */             if (getTileY() > this.longTarget.getTileY()) {
/* 1104 */               tpy = getTileY() - 5 - Server.rand.nextInt(6);
/*      */             }
/*      */           } 
/* 1107 */         } else if (getCitizenVillage() != null) {
/*      */           
/* 1109 */           FocusZone hota = FocusZone.getHotaZone();
/* 1110 */           if (hota != null)
/*      */           {
/* 1112 */             if (hota.covers(getTileX(), getTileY()))
/*      */             {
/* 1114 */               for (Item pillar : Hota.getHotaItems()) {
/*      */                 
/* 1116 */                 if (pillar.getTemplateId() == 739 && pillar.getZoneId() > 0)
/*      */                 {
/* 1118 */                   if (pillar.getData1() != getCitizenVillage().getId())
/*      */                   {
/* 1120 */                     if (getTileRange(this, pillar.getTileX(), pillar.getTileY()) < 20.0D) {
/*      */                       
/* 1122 */                       tpx = pillar.getTileX();
/* 1123 */                       tpy = pillar.getTileY();
/*      */                     } 
/*      */                   }
/*      */                 }
/*      */               } 
/*      */             }
/*      */           }
/*      */         } 
/* 1131 */         tpx = Zones.safeTileX(tpx);
/* 1132 */         tpy = Zones.safeTileY(tpy);
/* 1133 */         VolaTile t = Zones.getOrCreateTile(tpx, tpy, isOnSurface());
/* 1134 */         if (isOnSurface()) {
/*      */ 
/*      */           
/* 1137 */           boolean stepOnBridge = false;
/* 1138 */           if (Server.rand.nextInt(5) == 0)
/*      */           {
/* 1140 */             for (VolaTile stile : this.currentTile.getThisAndSurroundingTiles(1)) {
/*      */               
/* 1142 */               if (stile.getStructure() != null && stile.getStructure().isTypeBridge()) {
/*      */                 
/* 1144 */                 if (stile.getStructure().isHorizontal()) {
/*      */                   
/* 1146 */                   if (stile.getStructure().getMaxX() == stile.getTileX() || stile
/* 1147 */                     .getStructure().getMinX() == stile.getTileX())
/*      */                   {
/* 1149 */                     if (getTileY() == stile.getTileY()) {
/*      */                       
/* 1151 */                       tilePosX = stile.getTileX();
/* 1152 */                       tilePosY = stile.getTileY();
/* 1153 */                       stepOnBridge = true;
/*      */                       
/*      */                       break;
/*      */                     } 
/*      */                   }
/*      */                   continue;
/*      */                 } 
/* 1160 */                 if (stile.getStructure().getMaxY() == stile.getTileY() || stile
/* 1161 */                   .getStructure().getMinY() == stile.getTileY())
/*      */                 {
/* 1163 */                   if (getTileX() == stile.getTileX()) {
/*      */                     
/* 1165 */                     tilePosX = stile.getTileX();
/* 1166 */                     tilePosY = stile.getTileY();
/* 1167 */                     stepOnBridge = true;
/*      */                     
/*      */                     break;
/*      */                   } 
/*      */                 }
/*      */               } 
/*      */             } 
/*      */           }
/* 1175 */           if (!stepOnBridge)
/*      */           {
/* 1177 */             if (t == null || (t.getCreatures()).length < 3)
/*      */             {
/* 1179 */               tilePosX = tpx;
/* 1180 */               tilePosY = tpy;
/*      */             
/*      */             }
/*      */           
/*      */           }
/*      */         }
/* 1186 */         else if (t == null || (t.getCreatures()).length < 3) {
/*      */           
/* 1188 */           tilePosX = tpx;
/* 1189 */           tilePosY = tpy;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1195 */     Creature targ = getTarget();
/*      */     
/* 1197 */     if (targ != null) {
/*      */       
/* 1199 */       if (targ.getCultist() != null && targ.getCultist().hasFearEffect())
/*      */       {
/* 1201 */         setTarget(-10L, true);
/*      */       }
/*      */       
/* 1204 */       VolaTile currTile = targ.getCurrentTile();
/* 1205 */       if (currTile != null) {
/*      */         
/* 1207 */         tilePosX = currTile.tilex;
/* 1208 */         tilePosY = currTile.tiley;
/* 1209 */         if (seed == 100) {
/*      */           
/* 1211 */           tilePosX = currTile.tilex - 1 + Server.rand.nextInt(3);
/* 1212 */           tilePosY = currTile.tiley - 1 + Server.rand.nextInt(3);
/*      */         } 
/* 1214 */         int targGroup = targ.getGroupSize();
/* 1215 */         int myGroup = getGroupSize();
/*      */         
/* 1217 */         if (isOnSurface() != currTile.isOnSurface()) {
/*      */           
/* 1219 */           boolean changeLayer = false;
/* 1220 */           if ((getCurrentTile()).isTransition)
/*      */           {
/* 1222 */             changeLayer = true;
/*      */           }
/*      */           
/* 1225 */           VolaTile t = getCurrentTile();
/* 1226 */           if ((isAggHuman() || isHunter() || isDominated()) && (
/* 1227 */             !currTile.isGuarded() || (t != null && t.isGuarded())) && 
/* 1228 */             isWithinTileDistanceTo(currTile.getTileX(), currTile.getTileY(), 
/* 1229 */               (int)targ.getPositionZ(), this.template.getMaxHuntDistance())) {
/*      */             
/* 1231 */             if (!changeLayer) {
/*      */               
/* 1233 */               int[] tiles = { tilePosX, tilePosY };
/*      */               
/* 1235 */               if (isOnSurface()) {
/* 1236 */                 tiles = findRandomCaveEntrance(tiles);
/*      */               } else {
/* 1238 */                 tiles = findRandomCaveExit(tiles);
/* 1239 */               }  tilePosX = tiles[0];
/* 1240 */               tilePosY = tiles[1];
/*      */             } 
/*      */           } else {
/*      */             
/* 1244 */             setTarget(-10L, true);
/* 1245 */           }  if (changeLayer)
/*      */           {
/*      */             
/* 1248 */             if (!Tiles.isMineDoor(Tiles.decodeType(Server.surfaceMesh.getTile(tx, ty))) || 
/* 1249 */               MineDoorPermission.getPermission(tx, ty).mayPass(this))
/* 1250 */               setLayer(isOnSurface() ? -1 : 0, true); 
/*      */           }
/*      */         } 
/* 1253 */         if (targ.getCultist() != null && targ.getCultist().hasFearEffect()) {
/*      */           
/* 1255 */           if (Server.rand.nextBoolean()) {
/* 1256 */             tilePosX = Math.max(currTile.getTileX() + 10, getTileX());
/*      */           } else {
/* 1258 */             tilePosX = Math.min(currTile.getTileX() - 10, getTileX());
/* 1259 */           }  if (Server.rand.nextBoolean()) {
/* 1260 */             tilePosX = Math.max(currTile.getTileY() + 10, getTileY());
/*      */           } else {
/* 1262 */             tilePosX = Math.min(currTile.getTileY() - 10, getTileY());
/*      */           } 
/*      */         } else {
/*      */           
/* 1266 */           VolaTile t = getCurrentTile();
/* 1267 */           if (targGroup <= myGroup * getMaxGroupAttackSize() && (
/* 1268 */             isAggHuman() || isHunter()) && (
/* 1269 */             !currTile.isGuarded() || (t != null && t.isGuarded()))) {
/*      */             
/* 1271 */             if (isWithinTileDistanceTo(currTile.getTileX(), currTile.getTileY(), 
/* 1272 */                 (int)targ.getPositionZ(), this.template.getMaxHuntDistance())) {
/*      */ 
/*      */               
/* 1275 */               if (targ.getKingdomId() != 0 && 
/* 1276 */                 !isFriendlyKingdom(targ.getKingdomId()) && (
/* 1277 */                 isDefendKingdom() || (isAggWhitie() && targ
/* 1278 */                 .getKingdomTemplateId() != 3))) {
/*      */                 
/* 1280 */                 if (!isFighting())
/*      */                 {
/* 1282 */                   if (seed == 100)
/*      */                   {
/* 1284 */                     tilePosX = currTile.tilex - 1 + Server.rand.nextInt(3);
/* 1285 */                     tilePosY = currTile.tiley - 1 + Server.rand.nextInt(3);
/*      */                   }
/*      */                   else
/*      */                   {
/* 1289 */                     tilePosX = currTile.getTileX();
/* 1290 */                     tilePosY = currTile.getTileY();
/* 1291 */                     setTarget(targ.getWurmId(), false);
/*      */ 
/*      */                   
/*      */                   }
/*      */ 
/*      */ 
/*      */                 
/*      */                 }
/*      */               
/*      */               }
/* 1301 */               else if (seed == 100) {
/*      */                 
/* 1303 */                 tilePosX = currTile.tilex - 1 + Server.rand.nextInt(3);
/* 1304 */                 tilePosY = currTile.tiley - 1 + Server.rand.nextInt(3);
/*      */               }
/*      */               else {
/*      */                 
/* 1308 */                 tilePosX = currTile.getTileX();
/* 1309 */                 tilePosY = currTile.getTileY();
/*      */                 
/* 1311 */                 if (getSize() < 5 && targ
/* 1312 */                   .getBridgeId() != -10L && 
/* 1313 */                   getBridgeId() < 0L) {
/*      */                   
/* 1315 */                   int[] tiles = findBestBridgeEntrance(targ.getTileX(), targ
/* 1316 */                       .getTileY(), targ.getLayer(), targ
/* 1317 */                       .getBridgeId());
/* 1318 */                   if (tiles[0] > 0) {
/*      */                     
/* 1320 */                     tilePosX = tiles[0];
/* 1321 */                     tilePosY = tiles[1];
/*      */ 
/*      */ 
/*      */ 
/*      */                     
/* 1326 */                     if (getTileX() == tilePosX && getTileY() == tilePosY)
/*      */                     {
/* 1328 */                       tilePosX = currTile.tilex;
/* 1329 */                       tilePosY = currTile.tiley;
/*      */                     }
/*      */                   
/*      */                   } 
/* 1333 */                 } else if (getBridgeId() != targ.getBridgeId()) {
/*      */                   
/* 1335 */                   int[] tiles = findBestBridgeEntrance(targ.getTileX(), targ
/* 1336 */                       .getTileY(), targ.getLayer(), getBridgeId());
/* 1337 */                   if (tiles[0] > 0)
/*      */                   {
/* 1339 */                     tilePosX = tiles[0];
/* 1340 */                     tilePosY = tiles[1];
/*      */ 
/*      */ 
/*      */                     
/* 1344 */                     if (getTileX() == tilePosX && getTileY() == tilePosY)
/*      */                     {
/* 1346 */                       tilePosX = currTile.tilex;
/* 1347 */                       tilePosY = currTile.tiley;
/*      */                     }
/*      */                   
/*      */                   }
/*      */                 
/*      */                 } 
/*      */               } 
/* 1354 */             } else if (!isFighting()) {
/*      */               
/* 1356 */               setTarget(-10L, true);
/*      */             }
/*      */           
/* 1359 */           } else if (!isFighting()) {
/*      */             
/* 1361 */             setTarget(-10L, true);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/* 1366 */     if (tilePosX == tx && tilePosY == ty)
/* 1367 */       return null; 
/* 1368 */     tilePosX = Zones.safeTileX(tilePosX);
/* 1369 */     tilePosY = Zones.safeTileY(tilePosY);
/* 1370 */     if (!isOnSurface()) {
/*      */       
/* 1372 */       int tile = Server.caveMesh.getTile(tilePosX, tilePosY);
/* 1373 */       if (!Tiles.isSolidCave(Tiles.decodeType(tile)) && (
/* 1374 */         Tiles.decodeHeight(tile) > -getHalfHeightDecimeters() || isSwimming() || isSubmerged())) {
/* 1375 */         return new PathTile(tilePosX, tilePosY, tile, isOnSurface(), -1);
/*      */       }
/*      */     } else {
/*      */       
/* 1379 */       int tile = Server.surfaceMesh.getTile(tilePosX, tilePosY);
/* 1380 */       if (Tiles.decodeHeight(tile) > -getHalfHeightDecimeters() || isSwimming() || isSubmerged()) {
/* 1381 */         return new PathTile(tilePosX, tilePosY, tile, isOnSurface(), getFloorLevel());
/*      */       }
/*      */     } 
/* 1384 */     setTarget(-10L, true);
/* 1385 */     if (isDominated() && hasOrders())
/* 1386 */       removeOrder(getFirstOrder()); 
/* 1387 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   private final void setPassiveCounter(int counter) {
/* 1392 */     this.passiveCounter = counter;
/*      */   }
/*      */   
/*      */   private final void checkItemSpawn() {
/* 1396 */     if (this.lastX == 0)
/* 1397 */       this.lastX = getTileX(); 
/* 1398 */     if (this.lastY == 0) {
/* 1399 */       this.lastY = getTileY();
/*      */     }
/* 1401 */     if (this.lastX - getTileX() > 50 || this.lastY - getTileY() > 50) {
/*      */       
/* 1403 */       this.lastX = getTileX();
/* 1404 */       this.lastY = getTileY();
/* 1405 */       if (Server.rand.nextInt(10) == 0 && (getBody().getContainersAndWornItems()).length < 10) {
/*      */         
/*      */         try {
/*      */           
/* 1409 */           int templateId = Server.rand.nextInt(1437);
/* 1410 */           ItemTemplate template = ItemTemplateFactory.getInstance().getTemplate(templateId);
/* 1411 */           if (template.isArmour() || template.isWeapon())
/*      */           {
/* 1413 */             if (!template.isRoyal && !template.artifact) {
/*      */               
/*      */               try {
/* 1416 */                 Item toInsert = ItemFactory.createItem(templateId, Server.rand.nextFloat() * 80.0F + 20.0F, getName());
/* 1417 */                 getInventory().insertItem(toInsert, true);
/* 1418 */                 wearItems();
/*      */                 
/* 1420 */                 if (toInsert.getParentId() == getInventory().getWurmId()) {
/* 1421 */                   Items.destroyItem(toInsert.getWurmId());
/*      */                 }
/* 1423 */               } catch (FailedException failedException) {}
/*      */             
/*      */             }
/*      */           
/*      */           }
/*      */         }
/* 1429 */         catch (NoSuchTemplateException noSuchTemplateException) {}
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isMoveLocal() {
/* 1440 */     if (hasTrait(8))
/* 1441 */       return true; 
/* 1442 */     return this.template.isMoveLocal();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isSentinel() {
/* 1448 */     if (hasTrait(9))
/* 1449 */       return true; 
/* 1450 */     return this.template.isSentinel();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isMoveGlobal() {
/* 1456 */     if (hasTrait(1))
/* 1457 */       return true; 
/* 1458 */     return this.template.isMoveGlobal();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNpc() {
/* 1464 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public long getFace() {
/* 1470 */     faceRandom.setSeed(getWurmId());
/* 1471 */     return faceRandom.nextLong();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public float getSpeed() {
/* 1477 */     if (getVehicle() > -10L && WurmId.getType(getVehicle()) == 1)
/*      */     {
/* 1479 */       return 1.7F;
/*      */     }
/* 1481 */     return 1.1F;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isTypeFleeing() {
/* 1487 */     return ((getStatus()).modtype == 10 || (getStatus()).damage > 45000);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isRespawn() {
/* 1493 */     return !hasTrait(19);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isDominatable(Creature aDominator) {
/* 1499 */     if (getLeader() != null && getLeader() != aDominator)
/* 1500 */       return false; 
/* 1501 */     if (isRidden() || this.hitchedTo != null)
/* 1502 */       return false; 
/* 1503 */     return hasTrait(22);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getBaseCombatRating() {
/* 1513 */     double fskill = 1.0D;
/*      */     
/*      */     try {
/* 1516 */       fskill = this.skills.getSkill(1023).getKnowledge();
/*      */     }
/* 1518 */     catch (NoSuchSkillException nss) {
/*      */       
/* 1520 */       this.skills.learn(1023, 1.0F);
/* 1521 */       fskill = 1.0D;
/*      */     } 
/* 1523 */     if (getLoyalty() > 0.0F)
/* 1524 */       return (float)Math.max(1.0D, (isReborn() ? 0.7F : 0.5F) * fskill / 5.0D * this.status.getBattleRatingTypeModifier()) * Servers.localServer.getCombatRatingModifier(); 
/* 1525 */     return (float)Math.max(1.0D, fskill / 5.0D * this.status.getBattleRatingTypeModifier()) * Servers.localServer.getCombatRatingModifier();
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\Npc.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */