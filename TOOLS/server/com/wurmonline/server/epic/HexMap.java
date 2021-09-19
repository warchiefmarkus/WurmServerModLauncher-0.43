/*      */ package com.wurmonline.server.epic;
/*      */ 
/*      */ import com.wurmonline.server.DbConnector;
/*      */ import com.wurmonline.server.Features;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.WurmId;
/*      */ import com.wurmonline.server.creatures.CreatureTemplate;
/*      */ import com.wurmonline.server.creatures.CreatureTemplateFactory;
/*      */ import com.wurmonline.server.creatures.CreatureTemplateIds;
/*      */ import com.wurmonline.server.creatures.NoSuchCreatureTemplateException;
/*      */ import com.wurmonline.server.deities.Deities;
/*      */ import com.wurmonline.server.deities.Deity;
/*      */ import com.wurmonline.server.players.PlayerInfoFactory;
/*      */ import com.wurmonline.server.utils.DbUtilities;
/*      */ import com.wurmonline.server.webinterface.WcEpicEvent;
/*      */ import com.wurmonline.server.webinterface.WcEpicKarmaCommand;
/*      */ import java.sql.Connection;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.util.LinkedList;
/*      */ import java.util.Map;
/*      */ import java.util.Random;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.annotation.Nullable;
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
/*      */ public class HexMap
/*      */   implements MiscConstants, CreatureTemplateIds, TimeConstants
/*      */ {
/*   61 */   private static final Logger logger = Logger.getLogger(HexMap.class.getName());
/*      */   
/*      */   public static Valrei VALREI;
/*      */   
/*      */   public static final String VALREINAME = "Valrei";
/*      */   
/*      */   private static final String UPDATE_ENTITY_CONTROLLER = "UPDATE CONTROLLERS SET CONTROLLER=? WHERE CREATURE=?";
/*      */   
/*      */   private static final String CREATE_ENTITY_CONTROLLER = "INSERT INTO CONTROLLERS (CREATURE) VALUES (?)";
/*      */   private static final String LOAD_ENTITY_CONTROLLERS = "SELECT * FROM CONTROLLERS";
/*      */   private static final String LOAD_ALL_VISITED_HEX = "SELECT * FROM VISITED";
/*   72 */   static final Random rand = new Random();
/*   73 */   private final Map<Integer, EpicMapListener> eventListeners = new ConcurrentHashMap<>();
/*      */   
/*   75 */   private final Map<Integer, MapHex> hexmap = new ConcurrentHashMap<>();
/*   76 */   private final Map<Long, LinkedList<Integer>> controllers = new ConcurrentHashMap<>();
/*   77 */   private final Map<Long, EpicEntity> entities = new ConcurrentHashMap<>();
/*      */   
/*      */   private final EpicScenario currentScenario;
/*      */   private final String name;
/*      */   
/*      */   HexMap(String _name) {
/*   83 */     this.name = _name;
/*   84 */     this.currentScenario = new EpicScenario();
/*   85 */     if (this.name.equals("Valrei")) {
/*   86 */       VALREI = (Valrei)this;
/*      */     } else {
/*   88 */       VALREI = new Valrei();
/*      */     } 
/*      */   }
/*      */   
/*      */   final String getName() {
/*   93 */     return this.name;
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
/*      */   public final MapHex getMapHex(int id) {
/*  105 */     return this.hexmap.get(Integer.valueOf(id));
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
/*      */   final MapHex getMapHex(Integer id) {
/*  117 */     return this.hexmap.get(id);
/*      */   }
/*      */ 
/*      */   
/*      */   MapHex getSpawnHex(EpicEntity entity) {
/*  122 */     for (MapHex hm : this.hexmap.values()) {
/*      */       
/*  124 */       if (hm.isSpawnFor(entity.getId()))
/*  125 */         return hm; 
/*      */     } 
/*  127 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   final void addEntity(EpicEntity entity) {
/*  133 */     if (entity.isPlayerGod()) {
/*      */       return;
/*      */     }
/*  136 */     this.entities.put(Long.valueOf(entity.getId()), entity);
/*      */   }
/*      */ 
/*      */   
/*      */   void destroyEntity(EpicEntity entity) {
/*  141 */     entity.dropAll(false);
/*  142 */     entity.setHexMap(null);
/*  143 */     removeEntity(entity);
/*  144 */     if (entity.getCarrier() != null)
/*  145 */       entity.setCarrier(null, true, false, false); 
/*  146 */     entity.deleteEntity();
/*      */   }
/*      */ 
/*      */   
/*      */   final void removeEntity(EpicEntity entity) {
/*  151 */     this.entities.remove(Long.valueOf(entity.getId()));
/*      */   }
/*      */ 
/*      */   
/*      */   public final void loadAllEntities() {
/*  156 */     if (this.entities.isEmpty()) {
/*  157 */       generateEntities();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void generateEntities() {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final MapHex getRandomHex() {
/*  170 */     int toget = rand.nextInt(this.hexmap.size());
/*  171 */     int x = 0;
/*  172 */     for (MapHex hm : this.hexmap.values()) {
/*      */       
/*  174 */       if (x == toget)
/*  175 */         return hm; 
/*  176 */       x++;
/*      */     } 
/*  178 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   final void addMapHex(MapHex mh) {
/*  183 */     this.hexmap.put(Integer.valueOf(mh.getId()), mh);
/*      */   }
/*      */ 
/*      */   
/*      */   public final EpicEntity[] getAllEntities() {
/*  188 */     return (EpicEntity[])this.entities.values().toArray((Object[])new EpicEntity[this.entities.size()]);
/*      */   }
/*      */ 
/*      */   
/*      */   public final void pollAllEntities(boolean testing) {
/*  193 */     EpicEntity[] entityArr = getAllEntities();
/*  194 */     for (EpicEntity entity : entityArr) {
/*      */       
/*  196 */       entity.poll();
/*  197 */       if (testing)
/*  198 */         entity.setHelped(true, false); 
/*  199 */       if (entity.checkWinCondition()) {
/*      */         break;
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   final int getReasonAndEffectInt() {
/*  208 */     return this.currentScenario.getReasonPlusEffect();
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
/*      */   final void win(EpicEntity entity, String collName, int nums) {
/*  220 */     setWinEffects(entity, collName, nums);
/*  221 */     checkSpecialMapWinCases(entity);
/*  222 */     nextScenario();
/*  223 */     WcEpicKarmaCommand.clearKarma();
/*  224 */     PlayerInfoFactory.resetScenarioKarma();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setWinEffects(EpicEntity entity, String collName, int nums) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setEntityHelped(long entityId, byte missionType, int missionDifficulty) {
/*  241 */     EpicEntity entity = getEntity(entityId);
/*  242 */     if (entity != null) {
/*      */       
/*  244 */       float current = 0.0F;
/*  245 */       switch (rand.nextInt(7)) {
/*      */         
/*      */         case 0:
/*  248 */           current = entity.getCurrentSkill(102);
/*  249 */           entity.setSkill(102, current + (100.0F - current) / ((500 + (7 - missionDifficulty) * 50) + rand
/*  250 */               .nextFloat() * ((7 - missionDifficulty) * 50)));
/*      */           break;
/*      */         case 1:
/*  253 */           current = entity.getCurrentSkill(103);
/*  254 */           entity.setSkill(103, current + (100.0F - current) / ((500 + (7 - missionDifficulty) * 50) + rand
/*  255 */               .nextFloat() * ((7 - missionDifficulty) * 50)));
/*      */           break;
/*      */         case 2:
/*  258 */           current = entity.getCurrentSkill(104);
/*  259 */           entity.setSkill(104, current + (100.0F - current) / ((500 + (7 - missionDifficulty) * 50) + rand
/*  260 */               .nextFloat() * ((7 - missionDifficulty) * 50)));
/*      */           break;
/*      */         case 3:
/*  263 */           current = entity.getCurrentSkill(100);
/*  264 */           entity.setSkill(100, current + (100.0F - current) / ((500 + (7 - missionDifficulty) * 50) + rand
/*  265 */               .nextFloat() * ((7 - missionDifficulty) * 50)));
/*      */           break;
/*      */         case 4:
/*  268 */           current = entity.getCurrentSkill(101);
/*  269 */           entity.setSkill(101, current + (100.0F - current) / ((500 + (7 - missionDifficulty) * 50) + rand
/*  270 */               .nextFloat() * ((7 - missionDifficulty) * 50)));
/*      */           break;
/*      */         case 5:
/*  273 */           current = entity.getCurrentSkill(105);
/*  274 */           entity.setSkill(105, current + (100.0F - current) / ((500 + (7 - missionDifficulty) * 50) + rand
/*  275 */               .nextFloat() * ((7 - missionDifficulty) * 50)));
/*      */           break;
/*      */         case 6:
/*  278 */           current = entity.getCurrentSkill(106);
/*  279 */           entity.setSkill(106, current + (100.0F - current) / ((500 + (7 - missionDifficulty) * 50) + rand
/*  280 */               .nextFloat() * ((7 - missionDifficulty) * 50)));
/*      */           break;
/*      */       } 
/*      */       
/*  284 */       entity.setHelped(true, false);
/*  285 */       long timeToLeave = entity.modifyTimeToLeave(-EpicMissionEnum.getTimeReductionForMission(missionType, missionDifficulty));
/*  286 */       if (timeToLeave < System.currentTimeMillis()) {
/*      */         
/*  288 */         int effect = Server.rand.nextInt(4) + 1;
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  293 */         WcEpicEvent wce = new WcEpicEvent(WurmId.getNextWCCommandId(), effect, entity.getId(), 0, 3, entity.getName() + "s followers now have the attention of the " + Effectuator.getSpiritType(effect) + " spirits.", false);
/*      */         
/*  295 */         wce.sendFromLoginServer();
/*  296 */         if (rand.nextInt(20) == 0) {
/*      */           
/*  298 */           int template = 72 + Server.rand.nextInt(6);
/*  299 */           setCreatureController(template, entity.getId());
/*      */           
/*      */           try {
/*  302 */             CreatureTemplate c = CreatureTemplateFactory.getInstance().getTemplate(template);
/*  303 */             broadCast(entity.getName() + " now controls the " + c.getName() + "s.");
/*      */           }
/*  305 */           catch (NoSuchCreatureTemplateException nst) {
/*      */             
/*  307 */             logger.log(Level.WARNING, nst.getMessage(), (Throwable)nst);
/*      */           } 
/*      */         } 
/*      */       } 
/*  311 */       entity.setShouldCreateMission(true, true);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void checkSpecialMapWinCases(EpicEntity winner) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void nextScenario() {
/*  330 */     this.currentScenario.saveScenario(false);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   final boolean winCondition(boolean isWurm, int currentCollectibles, boolean isAtSpawn, int currentHex) {
/*  336 */     if ((!this.currentScenario.isSpawnPointRequiredToWin() && currentHex == this.currentScenario.getHexNumRequiredToWin()) || (isAtSpawn && this.currentScenario
/*  337 */       .isSpawnPointRequiredToWin()))
/*      */     {
/*  339 */       if (isWurm) {
/*      */         
/*  341 */         if (currentCollectibles >= this.currentScenario.getCollectiblesForWurmToWin()) {
/*  342 */           return true;
/*      */         
/*      */         }
/*      */       }
/*  346 */       else if (currentCollectibles >= this.currentScenario.getCollectiblesToWin()) {
/*      */         
/*  348 */         return true;
/*      */       } 
/*      */     }
/*      */     
/*  352 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addDemigod(String _name, long id, long companion, float initialBStr, float initialBSta, float initialBCon, float initialML, float initialMS, float initialSS, float initialSD) {
/*  359 */     EpicEntity newDemi = new EpicEntity(this, id, _name, 7, -1.0F, -1.0F);
/*  360 */     boolean foundHex = false;
/*  361 */     while (!foundHex) {
/*      */       
/*  363 */       MapHex hex = getRandomHex();
/*  364 */       if (hex != null)
/*      */       {
/*  366 */         if (!hex.isSpawn()) {
/*      */           
/*  368 */           hex.setHomeEntityId(id);
/*  369 */           hex.setSpawnEntityId(id);
/*  370 */           newDemi.createEntity(hex.getId());
/*  371 */           foundHex = true;
/*  372 */           logger.log(Level.INFO, _name + " will spawn " + hex.getPrepositionString() + " " + hex.getName());
/*      */         } 
/*      */       }
/*      */     } 
/*      */     
/*  377 */     newDemi.addSkill(102, initialBStr);
/*  378 */     newDemi.addSkill(103, initialBSta);
/*  379 */     newDemi.addSkill(104, initialBCon);
/*  380 */     newDemi.addSkill(100, initialML);
/*  381 */     newDemi.addSkill(101, initialMS);
/*  382 */     newDemi.addSkill(105, initialSS);
/*  383 */     newDemi.addSkill(106, initialSD);
/*  384 */     newDemi.createAndSaveSkills();
/*      */     
/*  386 */     if (companion != 0L) {
/*      */       
/*  388 */       EpicEntity compa = getEntity(companion);
/*  389 */       if (compa != null) {
/*      */         
/*  391 */         newDemi.setCompanion(compa);
/*  392 */         compa.setDemigodPlusForEntity((byte)(compa.getDemigodsToAppoint() - 1));
/*      */       } 
/*      */     } 
/*  395 */     newDemi.spawn();
/*  396 */     if (Features.Feature.VALREI_MAP.isEnabled()) {
/*      */       
/*  398 */       ValreiMapData.updateFromEpicEntity(newDemi);
/*      */       
/*  400 */       ValreiMapData.lastPolled = System.currentTimeMillis() - 1860000L;
/*  401 */       ValreiMapData.lastUpdatedTime = System.currentTimeMillis() - 2460000L;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   EpicEntity getDemiGodFor(EpicEntity entity) {
/*  407 */     for (EpicEntity e : this.entities.values()) {
/*      */       
/*  409 */       if (e.isDemigod() && e.getCompanion() == entity)
/*  410 */         return e; 
/*      */     } 
/*  412 */     return null;
/*      */   }
/*      */   
/*      */   public boolean elevateDemigod(long deityNum) {
/*  416 */     return elevateDemigod(deityNum, null);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean elevateDemigod(long deityNum, @Nullable String name) {
/*  421 */     EpicEntity god = getEntity(deityNum);
/*  422 */     logger.log(Level.INFO, "Checking elev for " + deityNum);
/*  423 */     if (god != null) {
/*      */       
/*  425 */       logger.log(Level.INFO, "Checking elev at 2 for " + god.getId());
/*  426 */       EpicEntity e = getDemiGodFor(god);
/*  427 */       if (e != null) {
/*      */         
/*  429 */         logger.log(Level.INFO, "Found entity demigod " + e.getName() + ". Number is " + e.getId() + ".");
/*  430 */         if (name == null || e.getName().toLowerCase().equals(name.toLowerCase())) {
/*      */           
/*  432 */           Deity d = Deities.getDeity((int)e.getId());
/*  433 */           logger.log(Level.INFO, "Setting deity power " + d.getName() + " id=" + d.number);
/*  434 */           d.setPower((byte)3);
/*  435 */           e.setType(0);
/*      */           
/*  437 */           float rest = e.getInitialAttack() - 6.0F;
/*  438 */           float att = Math.min(6.0F, e.getInitialAttack());
/*  439 */           if (rest > 0.0F) {
/*  440 */             att += rest / 10.0F;
/*      */           }
/*  442 */           rest = e.getInitialVitality() - 6.0F;
/*  443 */           float vit = Math.min(6.0F, e.getInitialVitality());
/*  444 */           if (rest > 0.0F) {
/*  445 */             vit += rest / 10.0F;
/*      */           }
/*  447 */           Servers.ascend(d.getNumber(), d.name, e.getId(), (byte)(int)deityNum, d.sex, (byte)3, e
/*  448 */               .getCurrentSkill(102), e.getCurrentSkill(103), e
/*  449 */               .getCurrentSkill(104), e.getCurrentSkill(100), e.getCurrentSkill(101), e
/*  450 */               .getCurrentSkill(105), e.getCurrentSkill(106));
/*  451 */           return true;
/*      */         } 
/*      */         
/*  454 */         return false;
/*      */       } 
/*      */       
/*  457 */       return false;
/*      */     } 
/*      */     
/*  460 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   void generateRandomScenario() {
/*  465 */     EpicEntity.toggleXmlDump(false);
/*  466 */     destroyCollectables();
/*  467 */     destroySources();
/*  468 */     respawnEntities();
/*      */     
/*  470 */     int maxCollectables = 1 + rand.nextInt(10);
/*      */     
/*  472 */     int reasonAndEffect = getRandomReason();
/*  473 */     boolean spawnPoint = rand.nextBoolean();
/*  474 */     String firstPartOfName = generateFirstName();
/*  475 */     String secondPartOfName = generateSecondName();
/*      */     
/*  477 */     EpicEntity questHolder = getRandomEntityMonster();
/*  478 */     int hexNum = 0;
/*  479 */     if (!spawnPoint)
/*  480 */       hexNum = rand.nextInt(this.hexmap.size()) + 1; 
/*  481 */     if (reasonAndEffect == 15) {
/*      */       
/*  483 */       hexNum = 5;
/*  484 */       spawnPoint = false;
/*      */     } 
/*  486 */     String missionName = "";
/*  487 */     String instigator = getRandomInstigator();
/*  488 */     String hide = generateHideWord();
/*  489 */     String reasonString = getReason(reasonAndEffect, (maxCollectables > 1));
/*  490 */     String missionDescription = instigator + ' ' + hide + " the " + firstPartOfName + ' ' + secondPartOfName + '.' + ' ' + reasonString;
/*      */     
/*  492 */     if (maxCollectables == 1) {
/*      */       
/*  494 */       missionName = generateMissionName(firstPartOfName + ' ' + secondPartOfName, questHolder);
/*  495 */       if (questHolder != null)
/*      */       {
/*  497 */         if (rand.nextBoolean())
/*      */         {
/*  499 */           missionDescription = instigator + ' ' + hide + ' ' + questHolder.getName() + "'s " + firstPartOfName + ' ' + secondPartOfName + '.' + ' ' + reasonString;
/*      */         
/*      */         }
/*      */       }
/*      */     }
/*      */     else {
/*      */       
/*  506 */       if (rand.nextBoolean()) {
/*  507 */         missionName = generateMissionName(firstPartOfName + ' ' + secondPartOfName + "s", questHolder);
/*      */       } else {
/*  509 */         missionName = generateMissionName(secondPartOfName + "s", questHolder);
/*  510 */       }  if (rand.nextBoolean()) {
/*      */ 
/*      */         
/*  513 */         missionDescription = instigator + ' ' + hide + ' ' + questHolder.getName() + "'s " + getNameForNumber(maxCollectables) + ' ' + firstPartOfName + ' ' + secondPartOfName + "s" + '.' + ' ' + reasonString;
/*      */       }
/*      */       else {
/*      */         
/*  517 */         missionDescription = instigator + ' ' + hide + " the " + firstPartOfName + ' ' + secondPartOfName + "s" + '.' + ' ' + reasonString;
/*      */       } 
/*      */     } 
/*  520 */     missionDescription = missionDescription + ' ' + getMapSpecialWinEffect();
/*  521 */     int srcfrags = 2 + rand.nextInt(4);
/*      */     
/*  523 */     generateCollectables(maxCollectables, firstPartOfName + ' ' + secondPartOfName, 2);
/*  524 */     generateCollectables(srcfrags, "Source " + generateSecondName(), 1);
/*  525 */     setWinCondition(Math.max(1, maxCollectables / 2), maxCollectables, spawnPoint, hexNum, missionName, missionDescription, reasonAndEffect);
/*      */     
/*  527 */     EpicEntity.toggleXmlDump(true);
/*  528 */     EpicXmlWriter.dumpEntities(this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   String getMapSpecialWinEffect() {
/*  538 */     return "";
/*      */   }
/*      */ 
/*      */   
/*      */   int getRandomReason() {
/*  543 */     int num = rand.nextInt(21);
/*      */     
/*  545 */     if (num == 20)
/*  546 */       num = 20 + rand.nextInt(5); 
/*  547 */     return num;
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
/*      */   String getReason(int reasonId, boolean many) {
/*  559 */     return "Those are dangerous!";
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   String getRandomInstigator() {
/*  565 */     int r = rand.nextInt(20);
/*  566 */     switch (r)
/*      */     
/*      */     { case 0:
/*  569 */         firstPart = "The Morbid One";
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
/*  632 */         return firstPart;case 1: firstPart = "The vengeful Sea Spirits"; return firstPart;case 2: firstPart = "The mischievous Forest Spirits"; return firstPart;case 3: firstPart = "The immobile Frozen One"; return firstPart;case 4: firstPart = "The unfathomable Stargazer"; return firstPart;case 5: firstPart = "The mysterious Drakespirit"; return firstPart;case 6: firstPart = "The evil Deathcrawler"; return firstPart;case 7: firstPart = "Ethereal thunderstorms"; return firstPart;case 8: firstPart = "An emissary from the void"; return firstPart;case 9: firstPart = "A deadly starburst"; return firstPart;case 10: firstPart = "A heavy chaos eruption"; return firstPart;case 11: firstPart = "An unnatural meteor storm"; return firstPart;case 12: firstPart = "A sudden surge in source energy"; return firstPart;case 13: firstPart = "A physical storm of emotions"; return firstPart;case 14: firstPart = "A quake of world-shattering proportions"; return firstPart;case 15: firstPart = "The Shift"; return firstPart;case 16: firstPart = "An eruption of Fire Spirits from Firejaw"; return firstPart;case 17: firstPart = "Uttacha who left her depths in desperation"; return firstPart;case 18: firstPart = "A portal to Seris opened. The dead souls"; return firstPart;case 19: firstPart = "Demons from Sol"; return firstPart; }  String firstPart = ""; logger.warning("Somehow rand.nextInt(20) returned an int that was not between 0 and 19"); return firstPart;
/*      */   }
/*      */ 
/*      */   
/*      */   EpicEntity getRandomEntityMonster() {
/*  637 */     EpicEntity[] allArr = getAllEntities();
/*  638 */     LinkedList<EpicEntity> mons = new LinkedList<>();
/*  639 */     for (EpicEntity ep : allArr) {
/*      */       
/*  641 */       if (ep.isWurm() || ep.isDeity() || ep.isSentinelMonster() || ep.isAlly())
/*  642 */         mons.add(ep); 
/*      */     } 
/*  644 */     if (mons.size() > 0)
/*  645 */       return mons.get(rand.nextInt(mons.size())); 
/*  646 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   String generateHideWord() {
/*      */     String secondPart;
/*  652 */     int r = rand.nextInt(14);
/*  653 */     switch (r) {
/*      */       
/*      */       case 0:
/*  656 */         secondPart = "hid";
/*      */         break;
/*      */       case 1:
/*  659 */         secondPart = "scattered";
/*      */         break;
/*      */       case 2:
/*  662 */         secondPart = "dispersed";
/*      */         break;
/*      */       case 3:
/*  665 */         secondPart = "dug down";
/*      */         break;
/*      */       case 4:
/*  668 */         secondPart = "brought";
/*      */         break;
/*      */       case 5:
/*  671 */         secondPart = "stole";
/*      */         break;
/*      */       case 6:
/*  674 */         secondPart = "dropped";
/*      */         break;
/*      */       case 7:
/*  677 */         secondPart = "misplaced";
/*      */         break;
/*      */       case 8:
/*  680 */         secondPart = "invented";
/*      */         break;
/*      */       case 9:
/*  683 */         secondPart = "created";
/*      */         break;
/*      */       case 10:
/*  686 */         secondPart = "spread out";
/*      */         break;
/*      */       case 11:
/*  689 */         secondPart = "revealed the existance of";
/*      */         break;
/*      */       case 12:
/*  692 */         secondPart = "rained";
/*      */         break;
/*      */       case 13:
/*  695 */         secondPart = "separated";
/*      */         break;
/*      */       default:
/*  698 */         secondPart = "";
/*  699 */         logger.warning("Somehow rand.nextInt(14) returned an int that was not between 0 and 13"); break;
/*      */     } 
/*  701 */     int prep = rand.nextInt(4);
/*  702 */     if (prep == 0)
/*  703 */       return "has " + secondPart; 
/*  704 */     if (prep == 1)
/*  705 */       return "just " + secondPart; 
/*  706 */     if (prep == 2)
/*  707 */       return "recently " + secondPart; 
/*  708 */     return secondPart;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   String generateMissionName(String firstPart, EpicEntity questHolder) {
/*      */     String monsterName;
/*  716 */     if (questHolder != null) {
/*  717 */       monsterName = questHolder.getName();
/*      */     } else {
/*  719 */       monsterName = "The Spirits";
/*  720 */     }  int r = rand.nextInt(16);
/*  721 */     switch (r)
/*      */     
/*      */     { case 0:
/*  724 */         secondPart = "Hunt for the " + firstPart;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  775 */         return secondPart;case 1: secondPart = "Looking for " + firstPart; return secondPart;case 2: secondPart = "The lost " + firstPart; return secondPart;case 3: secondPart = "Quest of the " + firstPart; return secondPart;case 4: secondPart = "Revenge of " + monsterName; return secondPart;case 5: secondPart = monsterName + "'s hunt"; return secondPart;case 6: secondPart = monsterName + " lost"; return secondPart;case 7: secondPart = firstPart + " lost"; return secondPart;case 8: secondPart = monsterName + " in peril"; return secondPart;case 9: secondPart = monsterName + "'s mystery"; return secondPart;case 10: secondPart = monsterName + " fall"; return secondPart;case 11: secondPart = "The missing " + firstPart; return secondPart;case 12: secondPart = "Lost the " + firstPart; return secondPart;case 13: secondPart = "Who hid the " + firstPart; return secondPart;case 14: secondPart = monsterName + "'s " + firstPart; return secondPart;case 15: secondPart = monsterName + " and the " + firstPart; return secondPart; }  String secondPart = ""; logger.warning("Somehow rand.nextInt(16) returned an int that was not between 0 and 15"); return secondPart;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final void setWinCondition(int collectiblesRequired, int collectiblesRequiredForWurm, boolean atSpawnPointRequired, int hexNumRequired, String newScenarioName, String newScenarioQuest, int reasonAndEffect) {
/*  782 */     this.currentScenario.setCollectiblesToWin(collectiblesRequired);
/*  783 */     this.currentScenario.setCollectiblesForWurmToWin(collectiblesRequiredForWurm);
/*  784 */     this.currentScenario.setSpawnPointRequiredToWin(atSpawnPointRequired);
/*  785 */     if (this.currentScenario.isSpawnPointRequiredToWin()) {
/*  786 */       this.currentScenario.setHexNumRequiredToWin(0);
/*      */     } else {
/*  788 */       this.currentScenario.setHexNumRequiredToWin(hexNumRequired);
/*      */     } 
/*  790 */     this.currentScenario.setScenarioName(newScenarioName);
/*  791 */     this.currentScenario.setScenarioQuest(newScenarioQuest);
/*  792 */     this.currentScenario.setReasonPlusEffect(reasonAndEffect);
/*  793 */     logger.log(Level.INFO, this.currentScenario.getScenarioName() + ':');
/*  794 */     logger.log(Level.INFO, this.currentScenario.getScenarioQuest());
/*  795 */     this.currentScenario.saveScenario(true);
/*      */   }
/*      */ 
/*      */   
/*      */   public final void broadCast(String event) {
/*  800 */     for (EpicMapListener listener : this.eventListeners.values())
/*  801 */       listener.broadCastEpicEvent(event); 
/*  802 */     logger.log(Level.INFO, event);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final EpicScenario getCurrentScenario() {
/*  812 */     return this.currentScenario;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final String getScenarioQuestString() {
/*  820 */     return this.currentScenario.getScenarioQuest();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final String getScenarioName() {
/*  828 */     return this.currentScenario.getScenarioName();
/*      */   }
/*      */ 
/*      */   
/*      */   final void addAttackTo(long entityId, float addedValue) {
/*  833 */     EpicEntity entity = this.entities.get(Long.valueOf(entityId));
/*  834 */     if (entity != null) {
/*  835 */       entity.setAttack(entity.getAttack() + addedValue);
/*      */     }
/*      */   }
/*      */   
/*      */   final void addVitalityTo(long entityId, float addedValue) {
/*  840 */     EpicEntity entity = this.entities.get(Long.valueOf(entityId));
/*  841 */     if (entity != null) {
/*  842 */       entity.setVitality(entity.getVitality() + addedValue);
/*      */     }
/*      */   }
/*      */   
/*      */   public final EpicEntity getEntity(long eid) {
/*  847 */     return this.entities.get(Long.valueOf(eid));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   final void addEntity(String newEntityName, long newid, float attack, float vitality, long masterId, int deityType) {
/*  853 */     EpicEntity newent = new EpicEntity(this, newid, newEntityName, deityType, attack, vitality);
/*  854 */     EpicEntity masterEntity = this.entities.get(Long.valueOf(masterId));
/*  855 */     if (masterEntity != null) {
/*      */       
/*  857 */       MapHex mh = getSpawnHex(masterEntity);
/*  858 */       MapHex newSpawn = getMapHex(mh.getId() + ((masterId == 3L) ? 2 : 1));
/*  859 */       newSpawn.setSpawnEntityId(newid);
/*  860 */       newent.setCompanion(masterEntity);
/*  861 */       broadCast(newEntityName + " has joined the side of " + masterEntity.getName() + " on " + this.name + '.');
/*  862 */       broadCast(newEntityName + " set up home " + newSpawn.getPrepositionString() + newSpawn.getName() + '.');
/*      */     }
/*      */     else {
/*      */       
/*  866 */       boolean searching = true;
/*  867 */       while (searching) {
/*      */         
/*  869 */         logger.log(Level.INFO, "Looking for free spawnpoint for " + newEntityName);
/*  870 */         MapHex mh = getRandomHex();
/*  871 */         if (!mh.isSpawn()) {
/*      */           
/*  873 */           mh.setSpawnEntityId(newid);
/*  874 */           searching = false;
/*  875 */           broadCast(newEntityName + " has entered " + this.name + '.');
/*  876 */           broadCast(newEntityName + " set up home " + mh.getPrepositionString() + mh.getName() + '.');
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   final void broadCastEpicWinCondition(String _scenarioname, String _scenarioQuest) {
/*  884 */     for (EpicMapListener listener : this.eventListeners.values()) {
/*  885 */       listener.broadCastEpicWinCondition(_scenarioname, _scenarioQuest);
/*      */     }
/*      */   }
/*      */   
/*      */   public final void removeListener(EpicMapListener listener) {
/*  890 */     this.eventListeners.remove(Integer.valueOf(listener.hashCode()));
/*      */   }
/*      */ 
/*      */   
/*      */   public final void addListener(EpicMapListener listener) {
/*  895 */     this.eventListeners.put(Integer.valueOf(listener.hashCode()), listener);
/*      */   }
/*      */   
/*  898 */   private static Random nameRand = new Random();
/*      */ 
/*      */   
/*      */   public static final String generateFirstName(int randId) {
/*  902 */     nameRand.setSeed(randId);
/*  903 */     return getFirstNameForNumber(nameRand.nextInt(20));
/*      */   }
/*      */ 
/*      */   
/*      */   public static final String generateFirstName() {
/*  908 */     int r = rand.nextInt(20);
/*  909 */     return getFirstNameForNumber(r);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final String getFirstNameForNumber(int r) {
/*  918 */     switch (r)
/*      */     
/*      */     { case 0:
/*  921 */         firstPart = "Golden";
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
/*  984 */         return firstPart;case 1: firstPart = "Frozen"; return firstPart;case 2: firstPart = "Silvery"; return firstPart;case 3: firstPart = "Ornamented"; return firstPart;case 4: firstPart = "Shiny"; return firstPart;case 5: firstPart = "Beautiful"; return firstPart;case 6: firstPart = "Burning"; return firstPart;case 7: firstPart = "Fire"; return firstPart;case 8: firstPart = "Glowing"; return firstPart;case 9: firstPart = "Lustrous"; return firstPart;case 10: firstPart = "Charming"; return firstPart;case 11: firstPart = "Deadly"; return firstPart;case 12: firstPart = "Wild"; return firstPart;case 13: firstPart = "Soulstruck"; return firstPart;case 14: firstPart = "Black"; return firstPart;case 15: firstPart = "Shadow"; return firstPart;case 16: firstPart = "Rotten"; return firstPart;case 17: firstPart = "Marble"; return firstPart;case 18: firstPart = "Powerful"; return firstPart;case 19: firstPart = "Holy"; return firstPart; }  String firstPart = ""; logger.warning("Method argument was an int that was not between 0 and 19"); return firstPart;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static final String getNameForNumber(int number) {
/*  990 */     switch (number)
/*      */     
/*      */     { case 0:
/*  993 */         numString = "zero";
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
/* 1058 */         return numString;case 1: numString = "one"; return numString;case 2: numString = "two"; return numString;case 3: numString = "three"; return numString;case 4: numString = "four"; return numString;case 5: numString = "five"; return numString;case 6: numString = "six"; return numString;case 7: numString = "seven"; return numString;case 8: numString = "eight"; return numString;case 9: numString = "nine"; return numString;case 10: numString = "ten"; return numString;case 11: numString = "eleven"; return numString;case 12: numString = "twelve"; return numString;case 13: numString = "thirteen"; return numString;case 14: numString = "fourteen"; return numString;case 15: numString = "fifteen"; return numString;case 16: numString = "sixteen"; return numString;case 17: numString = "seventeen"; return numString;case 18: numString = "eighteen"; return numString;case 19: numString = "nineteen"; return numString;case 20: numString = "twenty"; return numString; }  String numString = number + ""; return numString;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final String generateSecondName(int randId) {
/* 1063 */     nameRand.setSeed(randId);
/* 1064 */     return getSecondNameForNumber(nameRand.nextInt(20));
/*      */   }
/*      */ 
/*      */   
/*      */   public static final String generateSecondName() {
/* 1069 */     int r = rand.nextInt(20);
/* 1070 */     return getSecondNameForNumber(r);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final String getSecondNameForNumber(int r) {
/* 1079 */     switch (r)
/*      */     
/*      */     { case 0:
/* 1082 */         secondPart = "Feather";
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
/* 1145 */         return secondPart;case 1: secondPart = "Token"; return secondPart;case 2: secondPart = "Totem"; return secondPart;case 3: secondPart = "Crystal"; return secondPart;case 4: secondPart = "Shard"; return secondPart;case 5: secondPart = "Opal"; return secondPart;case 6: secondPart = "Diamond"; return secondPart;case 7: secondPart = "Fragment"; return secondPart;case 8: secondPart = "Jar"; return secondPart;case 9: secondPart = "Quill"; return secondPart;case 10: secondPart = "Harp"; return secondPart;case 11: secondPart = "Orb"; return secondPart;case 12: secondPart = "Sceptre"; return secondPart;case 13: secondPart = "Spirit"; return secondPart;case 14: secondPart = "Jewel"; return secondPart;case 15: secondPart = "Corpse"; return secondPart;case 16: secondPart = "Eye"; return secondPart;case 17: secondPart = "Circlet"; return secondPart;case 18: secondPart = "Band"; return secondPart;case 19: secondPart = "Strand"; return secondPart; }  String secondPart = ""; logger.warning("Method argument was an int that was not between 0 and 19"); return secondPart;
/*      */   }
/*      */ 
/*      */   
/*      */   final void destroyCollectables() {
/* 1150 */     EpicEntity[] entityArr = getAllEntities();
/* 1151 */     for (EpicEntity e : entityArr) {
/*      */       
/* 1153 */       if (e.isCollectable())
/*      */       {
/* 1155 */         destroyEntity(e);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   final void destroySources() {
/* 1162 */     EpicEntity[] entityArr = getAllEntities();
/* 1163 */     for (EpicEntity e : entityArr) {
/*      */       
/* 1165 */       if (e.isSource())
/*      */       {
/* 1167 */         destroyEntity(e);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   final void respawnEntities() {
/* 1174 */     EpicEntity[] entityArr = getAllEntities();
/* 1175 */     for (EpicEntity e : entityArr) {
/*      */       
/* 1177 */       if (!e.isCollectable() && !e.isSource()) {
/*      */         
/* 1179 */         int numSteps = e.resetSteps();
/* 1180 */         logger.log(Level.INFO, e.getName() + " took " + numSteps + " steps.");
/* 1181 */         e.spawn();
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   final void generateCollectables(int nums, String cname, int type) {
/* 1188 */     for (int x = -1; x >= -nums; x--) {
/*      */       
/* 1190 */       int id = x;
/* 1191 */       if (type == 1)
/* 1192 */         id = -100 - x; 
/* 1193 */       EpicEntity collectable = new EpicEntity(this, id, cname, type);
/* 1194 */       MapHex hex = getRandomHex();
/* 1195 */       collectable.createEntity(0);
/* 1196 */       hex.addEntity(collectable);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   boolean doesEntityExist(int entityId) {
/* 1202 */     return getEntities().containsKey(Long.valueOf(entityId));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setImpossibleWinConditions() {
/* 1210 */     this.currentScenario.setCollectiblesToWin(100);
/* 1211 */     this.currentScenario.setCollectiblesForWurmToWin(100);
/* 1212 */     this.currentScenario.setSpawnPointRequiredToWin(false);
/* 1213 */     this.currentScenario.setHexNumRequiredToWin(0);
/* 1214 */     this.currentScenario.setScenarioName("");
/* 1215 */     this.currentScenario.setScenarioQuest("");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getCollictblesRequiredToWin() {
/* 1223 */     return this.currentScenario.getCollectiblesToWin();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getCollictblesRequiredForWurmToWin() {
/* 1231 */     return this.currentScenario.getCollectiblesForWurmToWin();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean isSpawnPointRequiredToWin() {
/* 1239 */     return this.currentScenario.isSpawnPointRequiredToWin();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   int getHexNumRequiredToWin() {
/* 1247 */     return this.currentScenario.getHexNumRequiredToWin();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   int getScenarioNumber() {
/* 1258 */     return this.currentScenario.getScenarioNumber();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void incrementScenarioNumber() {
/* 1268 */     this.currentScenario.incrementScenarioNumber();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Map<Long, EpicEntity> getEntities() {
/* 1278 */     return this.entities;
/*      */   }
/*      */ 
/*      */   
/*      */   void sendDemigodRequest(long deityNum, String dname) {
/* 1283 */     Servers.requestDemigod((byte)(int)deityNum, dname);
/*      */   }
/*      */ 
/*      */   
/*      */   boolean spawnCreatures(EpicEntity entity) {
/* 1288 */     boolean delayedSpawn = false;
/* 1289 */     LinkedList<Integer> creatureTemplates = this.controllers.get(Long.valueOf(entity.getId()));
/* 1290 */     if (creatureTemplates != null && creatureTemplates.size() > 0) {
/*      */       
/* 1292 */       Integer toSpawn = creatureTemplates.get(rand.nextInt(creatureTemplates.size()));
/*      */       
/*      */       try {
/*      */         String summonString;
/* 1296 */         CreatureTemplate ct = CreatureTemplateFactory.getInstance().getTemplate(toSpawn.intValue());
/*      */         
/* 1298 */         switch (Server.rand.nextInt(5)) {
/*      */           
/*      */           case 0:
/* 1301 */             summonString = "sends forth";
/*      */             break;
/*      */           case 1:
/* 1304 */             summonString = "summons";
/*      */             break;
/*      */           case 2:
/* 1307 */             summonString = "commands";
/*      */             break;
/*      */           case 3:
/* 1310 */             summonString = "brings";
/*      */             break;
/*      */           case 4:
/* 1313 */             summonString = "lets loose";
/*      */             break;
/*      */           default:
/* 1316 */             summonString = "summons";
/*      */             break;
/*      */         } 
/* 1319 */         if (toSpawn.intValue() == 75)
/* 1320 */           delayedSpawn = true; 
/* 1321 */         String effectDesc = entity.getName() + " " + summonString + " the " + ct.getName() + "s.";
/* 1322 */         WcEpicEvent wce = new WcEpicEvent(WurmId.getNextWCCommandId(), 0, entity.getId(), toSpawn.intValue(), 5, effectDesc, false);
/*      */         
/* 1324 */         wce.sendFromLoginServer();
/*      */         
/* 1326 */         wce.sendToServer(3);
/* 1327 */         broadCast(effectDesc);
/*      */       }
/* 1329 */       catch (NoSuchCreatureTemplateException nst) {
/*      */         
/* 1331 */         logger.log(Level.WARNING, nst.getMessage());
/*      */       } 
/*      */     } 
/* 1334 */     return delayedSpawn;
/*      */   }
/*      */ 
/*      */   
/*      */   final void loadControllers() {
/* 1339 */     Connection dbcon = null;
/* 1340 */     PreparedStatement ps = null;
/* 1341 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 1344 */       dbcon = DbConnector.getDeityDbCon();
/* 1345 */       ps = dbcon.prepareStatement("SELECT * FROM CONTROLLERS");
/* 1346 */       rs = ps.executeQuery();
/* 1347 */       int found = 0;
/*      */       
/* 1349 */       while (rs.next()) {
/*      */         
/* 1351 */         int creatureTemplateId = rs.getInt("CREATURE");
/* 1352 */         long controller = rs.getLong("CONTROLLER");
/* 1353 */         LinkedList<Integer> list = this.controllers.get(Long.valueOf(controller));
/* 1354 */         if (list == null)
/*      */         {
/* 1356 */           list = new LinkedList<>();
/*      */         }
/* 1358 */         list.add(Integer.valueOf(creatureTemplateId));
/* 1359 */         this.controllers.put(Long.valueOf(controller), list);
/* 1360 */         found++;
/*      */       } 
/* 1362 */       if (found == 0)
/*      */       {
/* 1364 */         createControlledCreatures();
/*      */       }
/*      */     }
/* 1367 */     catch (SQLException sqx) {
/*      */       
/* 1369 */       logger.log(Level.WARNING, "Problem loading entity controllers due to " + sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1373 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 1374 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   void createControlledCreatures() {
/* 1380 */     initializeCreatureController(72);
/* 1381 */     initializeCreatureController(73);
/* 1382 */     initializeCreatureController(74);
/* 1383 */     initializeCreatureController(75);
/* 1384 */     initializeCreatureController(76);
/* 1385 */     initializeCreatureController(77);
/*      */   }
/*      */ 
/*      */   
/*      */   final void initializeCreatureController(int creatureTemplateId) {
/* 1390 */     Connection dbcon = null;
/* 1391 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1394 */       dbcon = DbConnector.getDeityDbCon();
/* 1395 */       ps = dbcon.prepareStatement("INSERT INTO CONTROLLERS (CREATURE) VALUES (?)");
/* 1396 */       ps.setInt(1, creatureTemplateId);
/* 1397 */       ps.executeUpdate();
/*      */     }
/* 1399 */     catch (SQLException sqx) {
/*      */       
/* 1401 */       logger.log(Level.WARNING, "Problem creating entity controller for creature template " + creatureTemplateId + " due to " + sqx
/* 1402 */           .getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1406 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1407 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   final void setCreatureController(int creatureTemplateId, long controller) {
/* 1413 */     for (Map.Entry<Long, LinkedList<Integer>> me : this.controllers.entrySet()) {
/*      */       
/* 1415 */       LinkedList<Integer> creatures = me.getValue();
/* 1416 */       if (creatures.contains(Integer.valueOf(creatureTemplateId))) {
/*      */ 
/*      */         
/* 1419 */         if (((Long)me.getKey()).longValue() == controller) {
/*      */           return;
/*      */         }
/*      */         
/* 1423 */         creatures.remove(Integer.valueOf(creatureTemplateId));
/*      */         break;
/*      */       } 
/*      */     } 
/* 1427 */     LinkedList<Integer> list = this.controllers.get(Long.valueOf(controller));
/* 1428 */     if (list == null)
/*      */     {
/* 1430 */       list = new LinkedList<>();
/*      */     }
/* 1432 */     list.add(Integer.valueOf(creatureTemplateId));
/* 1433 */     this.controllers.put(Long.valueOf(controller), list);
/* 1434 */     Connection dbcon = null;
/* 1435 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1438 */       dbcon = DbConnector.getDeityDbCon();
/* 1439 */       ps = dbcon.prepareStatement("UPDATE CONTROLLERS SET CONTROLLER=? WHERE CREATURE=?");
/* 1440 */       ps.setLong(1, controller);
/* 1441 */       ps.setInt(2, creatureTemplateId);
/* 1442 */       ps.executeUpdate();
/*      */     }
/* 1444 */     catch (SQLException sqx) {
/*      */       
/* 1446 */       logger.log(Level.WARNING, "Problem updating entity controller for creature template " + creatureTemplateId + " due to " + sqx
/* 1447 */           .getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1451 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1452 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   final void loadVisitedHexes() {
/* 1458 */     logger.info("Starting to load visited hexes for " + this.name);
/* 1459 */     long start = System.nanoTime();
/*      */     
/* 1461 */     Connection dbcon = null;
/* 1462 */     PreparedStatement ps = null;
/* 1463 */     ResultSet rs = null;
/* 1464 */     int found = 0;
/*      */     
/*      */     try {
/* 1467 */       dbcon = DbConnector.getDeityDbCon();
/* 1468 */       ps = dbcon.prepareStatement("SELECT * FROM VISITED");
/* 1469 */       rs = ps.executeQuery();
/*      */       
/* 1471 */       while (rs.next())
/*      */       {
/* 1473 */         int hexid = rs.getInt("HEXID");
/* 1474 */         long entityId = rs.getLong("ENTITYID");
/* 1475 */         MapHex h = getMapHex(hexid);
/* 1476 */         EpicEntity e = getEntity(entityId);
/* 1477 */         if (e != null)
/* 1478 */           h.addVisitedBy(e, true); 
/* 1479 */         found++;
/*      */       }
/*      */     
/* 1482 */     } catch (SQLException sqx) {
/*      */       
/* 1484 */       logger.log(Level.WARNING, "Problem loading visited hexes due to " + sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1488 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 1489 */       DbConnector.returnConnection(dbcon);
/*      */       
/* 1491 */       long end = System.nanoTime();
/* 1492 */       logger.info("Loading " + found + " visited hexes took " + ((float)(end - start) / 1000000.0F) + " ms");
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\epic\HexMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */