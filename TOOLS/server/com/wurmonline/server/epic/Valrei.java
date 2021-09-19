/*      */ package com.wurmonline.server.epic;
/*      */ 
/*      */ import com.wurmonline.server.DbConnector;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.WurmId;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.deities.Deities;
/*      */ import com.wurmonline.server.deities.Deity;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.players.PlayerInfo;
/*      */ import com.wurmonline.server.players.PlayerInfoFactory;
/*      */ import com.wurmonline.server.utils.DbUtilities;
/*      */ import com.wurmonline.server.webinterface.WCValreiMapUpdater;
/*      */ import com.wurmonline.server.webinterface.WcEpicEvent;
/*      */ import java.io.IOException;
/*      */ import java.sql.Connection;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.Map;
/*      */ import java.util.concurrent.ConcurrentHashMap;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Valrei
/*      */   extends HexMap
/*      */ {
/*   54 */   private static Logger logger = Logger.getLogger(Valrei.class.getName());
/*      */   
/*      */   private static final String LOAD_ENTITYDATA = "SELECT * FROM ENTITIES";
/*      */   
/*      */   private static final String LOAD_ENTITYSKILLS = "SELECT * FROM ENTITYSKILLS";
/*      */ 
/*      */   
/*      */   private final void loadEntityData() {
/*   62 */     logger.info("Starting to load Epic Entity Data for Valrei");
/*   63 */     long start = System.nanoTime();
/*      */ 
/*      */     
/*   66 */     loadControllers();
/*   67 */     Connection dbcon = null;
/*   68 */     PreparedStatement ps = null;
/*   69 */     ResultSet rs = null;
/*      */     
/*      */     try {
/*   72 */       dbcon = DbConnector.getDeityDbCon();
/*   73 */       ps = dbcon.prepareStatement("SELECT * FROM ENTITIES");
/*   74 */       rs = ps.executeQuery();
/*   75 */       int found = 0;
/*   76 */       Map<Long, Long> comps = new HashMap<>();
/*   77 */       Map<Long, Long> carrs = new HashMap<>();
/*      */       
/*   79 */       while (rs.next()) {
/*      */         
/*   81 */         long number = rs.getLong("ID");
/*   82 */         String name = rs.getString("NAME");
/*   83 */         long companionId = rs.getInt("COMPANION");
/*   84 */         byte demigodApps = rs.getByte("DEMIGODPLUS");
/*   85 */         int spawnPoint = rs.getInt("SPAWNPOINT");
/*   86 */         float attack = rs.getFloat("ATTACK");
/*   87 */         float vitality = rs.getFloat("VITALITY");
/*      */         
/*   89 */         float initialAttack = rs.getFloat("INATTACK");
/*   90 */         float initialVitality = rs.getFloat("INVITALITY");
/*   91 */         int type = rs.getInt("ENTITYTYPE");
/*   92 */         long carrier = rs.getLong("CARRIER");
/*   93 */         int currentHex = rs.getInt("CURRENTHEX");
/*   94 */         boolean helped = rs.getBoolean("HELPED");
/*   95 */         long entered = rs.getLong("ENTERED");
/*      */         
/*   97 */         long leaving = rs.getLong("LEAVING");
/*   98 */         int targetHex = rs.getInt("TARGETHEX");
/*   99 */         if (companionId > 0L)
/*  100 */           comps.put(Long.valueOf(number), Long.valueOf(companionId)); 
/*  101 */         EpicEntity e = new EpicEntity(this, number, name, type, initialAttack, initialVitality, helped, entered, leaving, targetHex);
/*      */         
/*  103 */         e.setAttack(attack, true);
/*  104 */         e.setVitality(vitality, true);
/*  105 */         if (e != null) {
/*      */           
/*  107 */           e.setDemigodsToAppoint(demigodApps);
/*      */           
/*  109 */           MapHex mh = getMapHex(spawnPoint);
/*  110 */           if (mh != null)
/*      */           {
/*  112 */             if (!mh.isSpawnFor(number) && 
/*  113 */               !mh.isSpawn()) {
/*  114 */               mh.setSpawnEntityId(number);
/*      */             }
/*      */           }
/*  117 */           if (carrier > 0L) {
/*      */             
/*  119 */             EpicEntity carr = getEntity(carrier);
/*  120 */             if (carr != null) {
/*      */               
/*  122 */               e.setCarrier(carr, true, true, false);
/*      */             } else {
/*      */               
/*  125 */               carrs.put(Long.valueOf(number), Long.valueOf(carrier));
/*      */             } 
/*      */           } 
/*  128 */           if (currentHex > 0 && !e.isPlayerGod()) {
/*      */             
/*  130 */             MapHex hex = getMapHex(currentHex);
/*  131 */             if (hex != null)
/*      */             {
/*  133 */               e.setMapHex(hex, true);
/*      */             }
/*      */           } 
/*      */         } 
/*  137 */         found++;
/*      */       } 
/*      */       
/*  140 */       for (Map.Entry<Long, Long> carr : carrs.entrySet()) {
/*      */         
/*  142 */         EpicEntity e = getEntity(((Long)carr.getKey()).longValue());
/*  143 */         if (e != null) {
/*      */           
/*  145 */           EpicEntity carrier = getEntity(((Long)carr.getValue()).longValue());
/*  146 */           if (carrier != null)
/*  147 */             e.setCarrier(carrier, true, true, false); 
/*      */         } 
/*      */       } 
/*  150 */       for (Map.Entry<Long, Long> coma : comps.entrySet()) {
/*      */         
/*  152 */         EpicEntity e = getEntity(((Long)coma.getKey()).longValue());
/*  153 */         if (e != null) {
/*      */           
/*  155 */           EpicEntity companion = getEntity(((Long)coma.getValue()).longValue());
/*  156 */           if (companion != null) {
/*      */             
/*  158 */             logger.log(Level.INFO, e.getName() + " setting companion " + companion.getName());
/*  159 */             e.setCompanion(companion, true);
/*      */           } 
/*      */         } 
/*      */       } 
/*  163 */       if (found == 0)
/*      */       {
/*  165 */         createEntities();
/*      */       }
/*      */     }
/*  168 */     catch (SQLException sqx) {
/*      */       
/*  170 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/*  174 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*  175 */       DbConnector.returnConnection(dbcon);
/*      */       
/*  177 */       long end = System.nanoTime();
/*  178 */       logger.info("Loading Epic Entity data for Valrei took " + ((float)(end - start) / 1000000.0F) + " ms");
/*      */     } 
/*      */     
/*  181 */     loadVisitedHexes();
/*      */   }
/*      */ 
/*      */   
/*      */   private final void loadEntitySkills() {
/*  186 */     logger.info("Starting to load Epic Entity Skill Data");
/*  187 */     long start = System.nanoTime();
/*      */     
/*  189 */     Connection dbcon = null;
/*  190 */     PreparedStatement ps = null;
/*  191 */     ResultSet rs = null;
/*      */     
/*      */     try {
/*  194 */       dbcon = DbConnector.getDeityDbCon();
/*  195 */       ps = dbcon.prepareStatement("SELECT * FROM ENTITYSKILLS");
/*  196 */       rs = ps.executeQuery();
/*      */       
/*  198 */       int found = 0;
/*  199 */       while (rs.next()) {
/*      */         
/*  201 */         long entityId = rs.getLong("ENTITYID");
/*  202 */         int skillId = rs.getInt("SKILLID");
/*  203 */         float defaultVal = rs.getFloat("DEFAULTVAL");
/*  204 */         float currentVal = rs.getFloat("CURRENTVAL");
/*      */         
/*  206 */         EpicEntity e = getEntity(entityId);
/*  207 */         if (e != null) {
/*      */           
/*  209 */           e.setSkill(skillId, defaultVal, currentVal);
/*  210 */           found++;
/*      */         } 
/*      */       } 
/*      */       
/*  214 */       if (found == 0) {
/*      */         
/*  216 */         setEntityDefaultSkills();
/*  217 */         for (EpicEntity e : getAllEntities()) {
/*  218 */           e.createAndSaveSkills();
/*      */         }
/*      */       } 
/*  221 */     } catch (SQLException sqx) {
/*      */       
/*  223 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/*  227 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*  228 */       DbConnector.returnConnection(dbcon);
/*      */       
/*  230 */       long end = System.nanoTime();
/*  231 */       logger.info("Loading Epic Entity Skill Data took " + ((float)(end - start) / 1000000.0F) + " ms");
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private final void createEntities() {
/*  237 */     logger.info("Starting to create Epic Entities for Valrei");
/*  238 */     long start = System.nanoTime();
/*      */     
/*  240 */     EpicEntity Fo = new EpicEntity(this, 1L, "Fo", 0, 6.0F, 7.0F);
/*  241 */     EpicEntity Vynora = new EpicEntity(this, 3L, "Vynora", 6, 6.0F, 7.0F);
/*  242 */     Fo.setCompanion(Vynora);
/*  243 */     Vynora.setCompanion(Fo);
/*  244 */     EpicEntity Magranon = new EpicEntity(this, 2L, "Magranon", 0, 6.0F, 7.0F);
/*  245 */     EpicEntity Libila = new EpicEntity(this, 4L, "Libila", 0, 6.0F, 7.0F);
/*      */     
/*  247 */     EpicEntity Nogump = new EpicEntity(this, 6L, "Nogump", 6, 4.0F, 4.0F);
/*      */     
/*  249 */     Nogump.setCompanion(Vynora);
/*  250 */     EpicEntity Walnut = new EpicEntity(this, 7L, "Walnut", 6, 6.0F, 4.0F);
/*  251 */     Walnut.setCompanion(Fo);
/*  252 */     EpicEntity Pharmakos = new EpicEntity(this, 8L, "Pharmakos", 6, 3.0F, 3.0F);
/*  253 */     Pharmakos.setCompanion(Libila);
/*  254 */     EpicEntity Jackal = new EpicEntity(this, 9L, "Jackal", 6, 5.0F, 3.0F);
/*  255 */     Jackal.setCompanion(Magranon);
/*      */ 
/*      */     
/*  258 */     EpicEntity DeathCrawler = new EpicEntity(this, 10L, "The Deathcrawler", 5, 6.0F, 3.0F);
/*      */ 
/*      */ 
/*      */     
/*  262 */     EpicEntity Scavenger = new EpicEntity(this, 11L, "The Scavenger", 5, 2.0F, 6.0F);
/*      */ 
/*      */ 
/*      */     
/*  266 */     EpicEntity DirtMaw = new EpicEntity(this, 12L, "The Dirtmaw Giant", 5, 5.0F, 4.0F);
/*      */ 
/*      */     
/*  269 */     setEntityDefaultSkills();
/*      */     
/*  271 */     EpicEntity[] allents = getAllEntities();
/*  272 */     for (EpicEntity e : allents) {
/*      */       
/*  274 */       MapHex mh = getSpawnHex(e);
/*  275 */       int sp = 0;
/*  276 */       if (mh != null)
/*  277 */         sp = mh.getId(); 
/*  278 */       e.createEntity(sp);
/*  279 */       EpicEntity comp = e.getCompanion();
/*  280 */       long cid = 0L;
/*  281 */       if (comp != null)
/*  282 */         cid = comp.getId(); 
/*  283 */       e.setCompanionForEntity(cid);
/*      */     } 
/*      */     
/*  286 */     long end = System.nanoTime();
/*  287 */     logger.info("Creating Epic Entities for Valrei took " + ((float)(end - start) / 1000000.0F) + " ms");
/*      */   }
/*      */ 
/*      */   
/*      */   public void setEntityDefaultSkills(EpicEntity entity) {
/*  292 */     if (entity == null) {
/*      */       return;
/*      */     }
/*  295 */     if (entity.getId() == 1L) {
/*      */       
/*  297 */       entity.addSkill(102, 60.0F);
/*  298 */       entity.addSkill(103, 45.0F);
/*  299 */       entity.addSkill(104, 55.0F);
/*  300 */       entity.addSkill(100, 45.0F);
/*  301 */       entity.addSkill(101, 50.0F);
/*  302 */       entity.addSkill(105, 40.0F);
/*  303 */       entity.addSkill(106, 65.0F);
/*      */     }
/*  305 */     else if (entity.getId() == 3L) {
/*      */       
/*  307 */       entity.addSkill(102, 40.0F);
/*  308 */       entity.addSkill(103, 50.0F);
/*  309 */       entity.addSkill(104, 45.0F);
/*  310 */       entity.addSkill(100, 65.0F);
/*  311 */       entity.addSkill(101, 45.0F);
/*  312 */       entity.addSkill(105, 60.0F);
/*  313 */       entity.addSkill(106, 55.0F);
/*      */     }
/*  315 */     else if (entity.getId() == 2L) {
/*      */       
/*  317 */       entity.addSkill(102, 65.0F);
/*  318 */       entity.addSkill(103, 60.0F);
/*  319 */       entity.addSkill(104, 50.0F);
/*  320 */       entity.addSkill(100, 40.0F);
/*  321 */       entity.addSkill(101, 45.0F);
/*  322 */       entity.addSkill(105, 55.0F);
/*  323 */       entity.addSkill(106, 45.0F);
/*      */     }
/*  325 */     else if (entity.getId() == 4L) {
/*      */       
/*  327 */       entity.addSkill(102, 50.0F);
/*  328 */       entity.addSkill(103, 65.0F);
/*  329 */       entity.addSkill(104, 45.0F);
/*  330 */       entity.addSkill(100, 60.0F);
/*  331 */       entity.addSkill(101, 40.0F);
/*  332 */       entity.addSkill(105, 45.0F);
/*  333 */       entity.addSkill(106, 55.0F);
/*      */     }
/*  335 */     else if (entity.getId() == 6L) {
/*      */       
/*  337 */       entity.addSkill(102, 45.0F);
/*  338 */       entity.addSkill(103, 30.0F);
/*  339 */       entity.addSkill(104, 40.0F);
/*  340 */       entity.addSkill(100, 25.0F);
/*  341 */       entity.addSkill(101, 30.0F);
/*  342 */       entity.addSkill(105, 50.0F);
/*  343 */       entity.addSkill(106, 35.0F);
/*      */     }
/*  345 */     else if (entity.getId() == 7L) {
/*      */       
/*  347 */       entity.addSkill(102, 50.0F);
/*  348 */       entity.addSkill(103, 40.0F);
/*  349 */       entity.addSkill(104, 45.0F);
/*  350 */       entity.addSkill(100, 30.0F);
/*  351 */       entity.addSkill(101, 25.0F);
/*  352 */       entity.addSkill(105, 35.0F);
/*  353 */       entity.addSkill(106, 30.0F);
/*      */     }
/*  355 */     else if (entity.getId() == 8L) {
/*      */       
/*  357 */       entity.addSkill(102, 30.0F);
/*  358 */       entity.addSkill(103, 35.0F);
/*  359 */       entity.addSkill(104, 30.0F);
/*  360 */       entity.addSkill(100, 45.0F);
/*  361 */       entity.addSkill(101, 40.0F);
/*  362 */       entity.addSkill(105, 50.0F);
/*  363 */       entity.addSkill(106, 25.0F);
/*      */     }
/*  365 */     else if (entity.getId() == 9L) {
/*      */       
/*  367 */       entity.addSkill(102, 40.0F);
/*  368 */       entity.addSkill(103, 30.0F);
/*  369 */       entity.addSkill(104, 50.0F);
/*  370 */       entity.addSkill(100, 35.0F);
/*  371 */       entity.addSkill(101, 45.0F);
/*  372 */       entity.addSkill(105, 25.0F);
/*  373 */       entity.addSkill(106, 30.0F);
/*      */     }
/*  375 */     else if (entity.getId() == 10L) {
/*      */       
/*  377 */       entity.addSkill(102, 60.0F);
/*  378 */       entity.addSkill(103, 25.0F);
/*  379 */       entity.addSkill(104, 45.0F);
/*  380 */       entity.addSkill(100, 20.0F);
/*  381 */       entity.addSkill(101, 30.0F);
/*  382 */       entity.addSkill(105, 50.0F);
/*  383 */       entity.addSkill(106, 40.0F);
/*      */     }
/*  385 */     else if (entity.getId() == 11L) {
/*      */       
/*  387 */       entity.addSkill(102, 40.0F);
/*  388 */       entity.addSkill(103, 60.0F);
/*  389 */       entity.addSkill(104, 30.0F);
/*  390 */       entity.addSkill(100, 50.0F);
/*  391 */       entity.addSkill(101, 25.0F);
/*  392 */       entity.addSkill(105, 45.0F);
/*  393 */       entity.addSkill(106, 20.0F);
/*      */     }
/*  395 */     else if (entity.getId() == 12L) {
/*      */       
/*  397 */       entity.addSkill(102, 50.0F);
/*  398 */       entity.addSkill(103, 40.0F);
/*  399 */       entity.addSkill(104, 45.0F);
/*  400 */       entity.addSkill(100, 25.0F);
/*  401 */       entity.addSkill(101, 20.0F);
/*  402 */       entity.addSkill(105, 60.0F);
/*  403 */       entity.addSkill(106, 30.0F);
/*      */     }
/*  405 */     else if (entity.getId() == 5L) {
/*      */       
/*  407 */       entity.addSkill(102, 70.0F);
/*  408 */       entity.addSkill(103, 70.0F);
/*  409 */       entity.addSkill(104, 70.0F);
/*  410 */       entity.addSkill(100, 60.0F);
/*  411 */       entity.addSkill(101, 60.0F);
/*  412 */       entity.addSkill(105, 50.0F);
/*  413 */       entity.addSkill(106, 50.0F);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private final void setEntityDefaultSkills() {
/*  419 */     EpicEntity foEntity = getEntity(1L);
/*  420 */     EpicEntity vynEntity = getEntity(3L);
/*  421 */     EpicEntity magEntity = getEntity(2L);
/*  422 */     EpicEntity libEntity = getEntity(4L);
/*  423 */     EpicEntity nogumpEntity = getEntity(6L);
/*  424 */     EpicEntity walnutEntity = getEntity(7L);
/*  425 */     EpicEntity pharEntity = getEntity(8L);
/*  426 */     EpicEntity jackalEntity = getEntity(9L);
/*  427 */     EpicEntity deathEntity = getEntity(10L);
/*  428 */     EpicEntity scavEntity = getEntity(11L);
/*  429 */     EpicEntity dirtEntity = getEntity(12L);
/*  430 */     EpicEntity wurmEntity = getEntity(5L);
/*      */     
/*  432 */     setEntityDefaultSkills(foEntity);
/*  433 */     setEntityDefaultSkills(vynEntity);
/*  434 */     setEntityDefaultSkills(magEntity);
/*  435 */     setEntityDefaultSkills(libEntity);
/*  436 */     setEntityDefaultSkills(nogumpEntity);
/*  437 */     setEntityDefaultSkills(walnutEntity);
/*  438 */     setEntityDefaultSkills(pharEntity);
/*  439 */     setEntityDefaultSkills(jackalEntity);
/*  440 */     setEntityDefaultSkills(deathEntity);
/*  441 */     setEntityDefaultSkills(scavEntity);
/*  442 */     setEntityDefaultSkills(dirtEntity);
/*  443 */     setEntityDefaultSkills(wurmEntity);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Valrei() {
/*  451 */     super("Valrei");
/*  452 */     logger.info("Creating Valrei");
/*      */     
/*  454 */     MapHex v1 = new MapHex(this, 1, "Faltersteps", 2.0F, 0);
/*  455 */     v1.addNearHexes(2, 3, 4, 54, 53, 55);
/*  456 */     v1.setPresenceStringOne(" is traversing the ");
/*  457 */     v1.setPrepositionString(" at the ");
/*  458 */     v1.setHomeEntityId(3L);
/*  459 */     MapHex v2 = new MapHex(this, 2, "Shaded Depths of Uttacha", 1.0F, 0);
/*  460 */     v2.addNearHexes(55, 3, 1, 5, 6, 54);
/*  461 */     v2.setPresenceStringOne(" is wallowing in the ");
/*  462 */     v2.setPrepositionString(" down in the ");
/*  463 */     v2.setHomeEntityId(3L);
/*  464 */     MapHex v3 = new MapHex(this, 3, "The Drown", 1.0F, 0);
/*  465 */     v3.addNearHexes(1, 2, 4, 6, 7, 8);
/*  466 */     v3.setSpawnEntityId(3L);
/*  467 */     v3.setPresenceStringOne(" is trying to survive in ");
/*  468 */     v3.setPrepositionString(" deep down in ");
/*  469 */     v3.setHomeEntityId(3L);
/*  470 */     MapHex v4 = new MapHex(this, 4, "Nogump The Dirty", 1.0F, 0);
/*  471 */     v4.addNearHexes(1, 3, 8, 9, 52, 53);
/*  472 */     v4.setPresenceStringOne(" is visiting ");
/*  473 */     v4.setPrepositionString(" at ");
/*  474 */     v4.setHomeEntityId(3L);
/*  475 */     v4.setSpawnEntityId(6L);
/*  476 */     MapHex v5 = new MapHex(this, 5, "Altars Of Contemplation", 1.0F, 4);
/*  477 */     v5.addNearHexes(2, 10, 11, 6, 55, 56);
/*  478 */     v5.setPresenceStringOne(" marvels at the ");
/*  479 */     v5.setPrepositionString(" at the ");
/*  480 */     v5.setHomeEntityId(3L);
/*  481 */     MapHex v6 = new MapHex(this, 6, "Brokeneyes", 3.0F, 0);
/*  482 */     v6.addNearHexes(2, 5, 11, 12, 7, 3);
/*  483 */     v6.setPresenceStringOne(" is climbing the ");
/*  484 */     v6.setPrepositionString(" at the ");
/*  485 */     v6.setHomeEntityId(3L);
/*  486 */     MapHex v7 = new MapHex(this, 7, "Firejaw", 3.0F, 2);
/*  487 */     v7.addNearHexes(3, 6, 12, 13, 14, 8);
/*  488 */     v7.setPresenceStringOne(" is trying to survive the ");
/*  489 */     v7.setPrepositionString(" at the ");
/*  490 */     v7.setHomeEntityId(3L);
/*  491 */     MapHex v8 = new MapHex(this, 8, "The Shift", 1.0F, 5);
/*  492 */     v8.addNearHexes(4, 3, 7, 14, 15, 9);
/*  493 */     v8.setPresenceStringOne(" is exploring ");
/*  494 */     v8.setHomeEntityId(3L);
/*  495 */     MapHex v9 = new MapHex(this, 9, "Valreis Worried Brow", 1.0F, 0);
/*  496 */     v9.addNearHexes(4, 8, 15, 16, 51, 52);
/*  497 */     v9.setPresenceStringOne(" lingers at ");
/*  498 */     v9.setHomeEntityId(3L);
/*  499 */     MapHex v10 = new MapHex(this, 10, "Plains Of Hidden Thoughts", 1.0F, 0);
/*  500 */     v10.addNearHexes(5, 11, 17, 55, 54, 56);
/*  501 */     v10.setPresenceStringOne(" wanders the ");
/*      */     
/*  503 */     v10.setPrepositionString(" on the ");
/*  504 */     MapHex v11 = new MapHex(this, 11, "Windswept Heights", 1.0F, 0);
/*  505 */     v11.addNearHexes(5, 10, 17, 18, 12, 6);
/*  506 */     v11.setPresenceStringOne(" crosses the ");
/*      */     
/*  508 */     v11.setPrepositionString(" on the ");
/*  509 */     MapHex v12 = new MapHex(this, 12, "Scary Old Trees", 1.0F, 0);
/*  510 */     v12.addNearHexes(6, 11, 18, 19, 13, 7);
/*  511 */     v12.setPresenceStringOne(" walks among the ");
/*      */     
/*  513 */     v12.setPrepositionString(" under the ");
/*  514 */     MapHex v13 = new MapHex(this, 13, "Really Bad Lands", 2.0F, 1);
/*  515 */     v13.addNearHexes(7, 12, 19, 20, 21, 14);
/*  516 */     v13.setPresenceStringOne(" is struggling in the ");
/*      */     
/*  518 */     v13.setPrepositionString(" in the ");
/*  519 */     v13.setSpawnEntityId(5L);
/*  520 */     v13.setHomeEntityId(5L);
/*  521 */     MapHex v14 = new MapHex(this, 14, "Beastwatch Range", 2.0F, 0);
/*  522 */     v14.addNearHexes(8, 7, 13, 21, 22, 15);
/*  523 */     v14.setPresenceStringOne(" is surveying at ");
/*      */     
/*  525 */     v14.setPrepositionString(" at ");
/*  526 */     MapHex v15 = new MapHex(this, 15, "The Nobody", 2.0F, 0);
/*  527 */     v15.addNearHexes(9, 8, 14, 22, 23, 16);
/*  528 */     v15.setPresenceStringOne(" visits ");
/*  529 */     v15.setPrepositionString(" at ");
/*  530 */     MapHex v16 = new MapHex(this, 16, "Who's There Forest", 1.0F, 0);
/*  531 */     v16.addNearHexes(9, 15, 23, 43, 51, 42);
/*  532 */     v16.setPresenceStringOne(" is wandering ");
/*      */     
/*  534 */     v16.setPrepositionString(" in ");
/*  535 */     MapHex v17 = new MapHex(this, 17, "Diamond Mines", 2.0F, 1);
/*  536 */     v17.addNearHexes(10, 24, 25, 18, 11, 50);
/*  537 */     v17.setPresenceStringOne(" is searching the ");
/*  538 */     v17.setHomeEntityId(2L);
/*  539 */     MapHex v18 = new MapHex(this, 18, "Jeopardy Hunt", 1.0F, 0);
/*  540 */     v18.addNearHexes(11, 17, 25, 26, 19, 12);
/*  541 */     v18.setPresenceStringOne(" is hunting at the ");
/*  542 */     v18.setHomeEntityId(2L);
/*  543 */     MapHex v19 = new MapHex(this, 19, "Dying Plateau", 1.0F, 0);
/*  544 */     v19.addNearHexes(12, 18, 26, 27, 20, 13);
/*  545 */     v19.setPresenceStringOne(" is traversing the ");
/*  546 */     MapHex v20 = new MapHex(this, 20, "Skyrisen Range", 2.0F, 0);
/*  547 */     v20.addNearHexes(13, 19, 27, 28, 29, 21);
/*  548 */     v20.setPresenceStringOne(" is climbing the ");
/*  549 */     MapHex v21 = new MapHex(this, 21, "Brittlerock Mountains", 2.0F, 0);
/*  550 */     v21.addNearHexes(14, 13, 20, 29, 30, 22);
/*  551 */     v21.setPresenceStringOne(" defies the thunderstorms at ");
/*  552 */     MapHex v22 = new MapHex(this, 22, "Loft Despair", 1.0F, 0);
/*  553 */     v22.addNearHexes(15, 14, 21, 30, 31, 23);
/*  554 */     v22.setPresenceStringOne(" stands at ");
/*  555 */     v22.setHomeEntityId(4L);
/*  556 */     v22.setSpawnEntityId(8L);
/*  557 */     MapHex v23 = new MapHex(this, 23, "The Dark Songs Forest", 1.0F, 0);
/*  558 */     v23.addNearHexes(16, 15, 22, 31, 32, 43);
/*  559 */     v23.setPresenceStringOne(" listens at ");
/*  560 */     v23.setHomeEntityId(4L);
/*  561 */     MapHex v24 = new MapHex(this, 24, "Jackal's Sanctuary", 1.0F, 0);
/*  562 */     v24.addNearHexes(17, 33, 25, 49, 50, 57);
/*  563 */     v24.setPresenceStringOne(" visits ");
/*  564 */     v24.setHomeEntityId(2L);
/*  565 */     v24.setSpawnEntityId(9L);
/*  566 */     MapHex v25 = new MapHex(this, 25, "Castle Glittercrown", 1.0F, 0);
/*  567 */     v25.addNearHexes(17, 24, 33, 34, 26, 18);
/*  568 */     v25.setPresenceStringOne(" visits ");
/*  569 */     v25.setSpawnEntityId(2L);
/*  570 */     v25.setHomeEntityId(2L);
/*  571 */     MapHex v26 = new MapHex(this, 26, "Spiritgathers", 1.0F, 3);
/*  572 */     v26.addNearHexes(18, 25, 34, 35, 27, 19);
/*  573 */     v26.setPresenceStringOne(" listens at ");
/*  574 */     v26.setHomeEntityId(2L);
/*  575 */     MapHex v27 = new MapHex(this, 27, "Weirdpeaks Fall", 2.0F, 0);
/*  576 */     v27.addNearHexes(19, 26, 35, 36, 28, 20);
/*  577 */     v27.setPresenceStringOne(" is traversing the ");
/*  578 */     MapHex v28 = new MapHex(this, 28, "Spring Valleys", 1.0F, 0);
/*  579 */     v28.addNearHexes(20, 27, 36, 37, 38, 29);
/*  580 */     v28.setPresenceStringOne(" wanders the ");
/*  581 */     MapHex v29 = new MapHex(this, 29, "Eaglespirit Glacier", 2.0F, 3);
/*  582 */     v29.addNearHexes(21, 20, 28, 38, 39, 30);
/*  583 */     v29.setPresenceStringOne(" is stuck in the ");
/*  584 */     MapHex v30 = new MapHex(this, 30, "Wintertree Hills", 1.0F, 0);
/*  585 */     v30.addNearHexes(22, 21, 29, 39, 40, 31);
/*  586 */     v30.setPresenceStringOne(" hunts the ");
/*  587 */     v30.setHomeEntityId(4L);
/*  588 */     v30.setSpawnEntityId(11L);
/*  589 */     MapHex v31 = new MapHex(this, 31, "Bloodsucker March", 2.0F, 2);
/*  590 */     v31.addNearHexes(23, 22, 30, 40, 41, 32);
/*  591 */     v31.setPresenceStringOne(" is stuck in the ");
/*  592 */     v31.setHomeEntityId(4L);
/*  593 */     MapHex v32 = new MapHex(this, 32, "Den Of The Deathcrawler", 1.0F, 0);
/*  594 */     v32.addNearHexes(23, 31, 41, 42, 43, 33);
/*  595 */     v32.setPresenceStringOne(" explores the ");
/*  596 */     v32.setHomeEntityId(4L);
/*  597 */     v32.setSpawnEntityId(10L);
/*  598 */     MapHex v33 = new MapHex(this, 33, "Saltwalk", 2.0F, 2);
/*  599 */     v33.addNearHexes(32, 24, 42, 34, 25, 41);
/*  600 */     v33.setPresenceStringOne(" survives the ");
/*  601 */     v33.setHomeEntityId(2L);
/*  602 */     MapHex v34 = new MapHex(this, 34, "Mount Creation", 2.0F, 0);
/*  603 */     v34.addNearHexes(33, 42, 43, 35, 26, 25);
/*  604 */     v34.setPresenceStringOne(" climbs ");
/*  605 */     v34.setHomeEntityId(2L);
/*  606 */     MapHex v35 = new MapHex(this, 35, "Golden Jungle", 2.0F, 2);
/*  607 */     v35.addNearHexes(34, 43, 44, 36, 27, 26);
/*  608 */     v35.setPresenceStringOne(" explores the ");
/*  609 */     v35.setHomeEntityId(2L);
/*  610 */     MapHex v36 = new MapHex(this, 36, "Mount Assami", 2.0F, 0);
/*  611 */     v36.addNearHexes(35, 44, 45, 37, 28, 27);
/*  612 */     v36.setPresenceStringOne(" avoids the rockfalls at ");
/*  613 */     MapHex v37 = new MapHex(this, 37, "Humid Hills", 1.0F, 0);
/*  614 */     v37.addNearHexes(36, 45, 46, 47, 38, 28);
/*  615 */     v37.setPresenceStringOne(" travels through the ");
/*      */     
/*  617 */     v37.setHomeEntityId(1L);
/*  618 */     MapHex v38 = new MapHex(this, 38, "Deadends", 2.0F, 1);
/*  619 */     v38.addNearHexes(28, 37, 47, 48, 39, 29);
/*  620 */     v38.setPresenceStringOne(" looks for a way through the ");
/*  621 */     MapHex v39 = new MapHex(this, 39, "Foulwater Ices", 2.0F, 0);
/*  622 */     v39.addNearHexes(29, 38, 48, 49, 40, 30);
/*  623 */     v39.setPresenceStringOne(" swims through the ");
/*  624 */     MapHex v40 = new MapHex(this, 40, "Rusty Daggers", 2.0F, 0);
/*  625 */     v40.addNearHexes(30, 39, 49, 50, 41, 31);
/*  626 */     v40.setPresenceStringOne(" walks the ");
/*  627 */     v40.setSpawnEntityId(4L);
/*  628 */     v40.setHomeEntityId(4L);
/*  629 */     MapHex v41 = new MapHex(this, 41, "Broken Fingernails", 2.0F, 0);
/*  630 */     v41.addNearHexes(31, 40, 50, 32, 24, 33);
/*  631 */     v41.setPresenceStringOne(" traverses the ");
/*  632 */     v41.setHomeEntityId(4L);
/*  633 */     MapHex v42 = new MapHex(this, 42, "Flamestrike Desert", 1.0F, 0);
/*  634 */     v42.addNearHexes(16, 23, 32, 33, 34, 43);
/*  635 */     v42.setPresenceStringOne(" explores the ");
/*  636 */     v42.setHomeEntityId(2L);
/*  637 */     MapHex v43 = new MapHex(this, 43, "Western Spurs", 2.0F, 0);
/*  638 */     v43.addNearHexes(42, 23, 51, 44, 35, 34);
/*  639 */     v43.setPresenceStringOne(" climbs the ");
/*  640 */     MapHex v44 = new MapHex(this, 44, "Drakespirit Gardens", 2.0F, 4);
/*  641 */     v44.addNearHexes(43, 51, 52, 45, 36, 35);
/*  642 */     v44.setPresenceStringOne(" visits the ");
/*  643 */     MapHex v45 = new MapHex(this, 45, "Jagged Rise", 1.0F, 2);
/*  644 */     v45.addNearHexes(44, 52, 53, 46, 37, 36);
/*  645 */     v45.setPresenceStringOne(" is traversing the ");
/*  646 */     v45.setHomeEntityId(1L);
/*  647 */     MapHex v46 = new MapHex(this, 46, "The Myriad", 1.0F, 0);
/*  648 */     v46.addNearHexes(45, 53, 54, 55, 47, 37);
/*  649 */     v46.setPresenceStringOne(" lingers in ");
/*  650 */     v46.setSpawnEntityId(1L);
/*  651 */     v46.setHomeEntityId(1L);
/*  652 */     MapHex v47 = new MapHex(this, 47, "Forest Of The Dreadwalkers", 1.0F, 0);
/*  653 */     v47.addNearHexes(37, 46, 55, 56, 48, 38);
/*  654 */     v47.setPresenceStringOne(" explores the ");
/*  655 */     v47.setHomeEntityId(1L);
/*  656 */     MapHex v48 = new MapHex(this, 48, "Misthollow Flats", 1.0F, 0);
/*  657 */     v48.addNearHexes(38, 47, 56, 57, 49, 39);
/*  658 */     v48.setPresenceStringOne(" is in the ");
/*  659 */     MapHex v49 = new MapHex(this, 49, "Home Of The Treekeeper", 1.0F, 0);
/*  660 */     v49.addNearHexes(39, 48, 57, 50, 40, 24);
/*  661 */     v49.setPresenceStringOne(" is visiting the ");
/*  662 */     v49.setSpawnEntityId(7L);
/*  663 */     MapHex v50 = new MapHex(this, 50, "The Fence", 2.0F, 1);
/*  664 */     v50.addNearHexes(40, 49, 41, 24, 33, 17);
/*  665 */     v50.setPresenceStringOne(" is trapped at ");
/*      */     
/*  667 */     v50.setPrepositionString(" at ");
/*  668 */     v50.setHomeEntityId(4L);
/*  669 */     MapHex v51 = new MapHex(this, 51, "Bleak Plains", 1.0F, 0);
/*  670 */     v51.addNearHexes(43, 44, 52, 16, 23, 9);
/*  671 */     v51.setPresenceStringOne(" is walking the ");
/*  672 */     MapHex v52 = new MapHex(this, 52, "Deforestation", 1.0F, 0);
/*  673 */     v52.addNearHexes(44, 51, 45, 53, 4, 9);
/*  674 */     v52.setPresenceStringOne(" is passing the ");
/*  675 */     v52.setHomeEntityId(1L);
/*  676 */     MapHex v53 = new MapHex(this, 53, "The Mawpits", 2.0F, 2);
/*  677 */     v53.addNearHexes(52, 45, 46, 54, 1, 4);
/*  678 */     v53.setPresenceStringOne(" is exploring the ");
/*  679 */     v53.setHomeEntityId(1L);
/*  680 */     MapHex v54 = new MapHex(this, 54, "Stompinggrounds", 1.0F, 0);
/*  681 */     v54.addNearHexes(1, 2, 53, 46, 55, 4);
/*  682 */     v54.setPresenceStringOne(" runs through ");
/*  683 */     v54.setHomeEntityId(1L);
/*  684 */     v54.setSpawnEntityId(12L);
/*  685 */     MapHex v55 = new MapHex(this, 55, "Glowing Shrubs", 1.0F, 0);
/*  686 */     v55.addNearHexes(46, 54, 47, 56, 5, 2);
/*  687 */     v55.setPresenceStringOne(" walks among the ");
/*  688 */     v55.setHomeEntityId(1L);
/*  689 */     v55.setPrepositionString(" among the ");
/*  690 */     MapHex v56 = new MapHex(this, 56, "Stargazers' Hollows", 1.0F, 0);
/*  691 */     v56.addNearHexes(47, 55, 48, 57, 5, 10);
/*  692 */     v56.setPresenceStringOne(" visits the ");
/*  693 */     v56.setHomeEntityId(1L);
/*  694 */     MapHex v57 = new MapHex(this, 57, "Withering Marble", 1.0F, 0);
/*  695 */     v57.addNearHexes(49, 48, 56, 10, 24, 5);
/*  696 */     v57.setPresenceStringOne(" walks the ");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void generateEntities() {
/*  707 */     loadEntityData();
/*  708 */     loadEntitySkills();
/*  709 */     if (!getCurrentScenario().loadCurrentScenario()) {
/*  710 */       nextScenario();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   String getMapSpecialWinEffect() {
/*  721 */     if (!doesEntityExist(5)) {
/*      */       
/*  723 */       if (getCollictblesRequiredToWin() > 0) {
/*  724 */         return "If Libila collects these, she will awake the Wurm!";
/*      */       }
/*  726 */       return "If Libila acquires it, she will awake the Wurm!";
/*      */     } 
/*  728 */     return "";
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
/*      */   void checkSpecialMapWinCases(EpicEntity winner) {
/*  740 */     if (winner.getId() == 4L)
/*      */     {
/*  742 */       if (!isWurmAwake()) {
/*      */         
/*  744 */         EpicEntity Wurm = new EpicEntity(this, 5L, "Wurm", 4, 10.0F, 10.0F);
/*  745 */         MapHex mh = getSpawnHex(Wurm);
/*  746 */         int sp = 0;
/*  747 */         if (mh != null)
/*  748 */           sp = mh.getId(); 
/*  749 */         Wurm.createEntity(sp);
/*  750 */         broadCast("Terror strikes the hearts of mortals as Libila has awoken the Wurm!");
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
/*      */   boolean isWurmAwake() {
/*  762 */     return doesEntityExist(5);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final void nextScenario() {
/*  773 */     getCurrentScenario().saveScenario(false);
/*      */     
/*  775 */     setImpossibleWinConditions();
/*  776 */     incrementScenarioNumber();
/*  777 */     switch (getScenarioNumber()) {
/*      */ 
/*      */       
/*      */       case 1:
/*  781 */         generateRandomScenario();
/*      */         return;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  792 */     generateRandomScenario();
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
/*      */   int getRandomReason() {
/*  806 */     return rand.nextInt(20);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   EpicEntity charmEnemyEntity(EpicEntity entity) {
/*  817 */     EpicEntity[] allents = getAllEntities();
/*  818 */     LinkedList<EpicEntity> allies = new LinkedList<>();
/*  819 */     for (EpicEntity e : allents) {
/*      */       
/*  821 */       if (e.isAlly() && e.getCompanion() != entity)
/*  822 */         allies.add(e); 
/*      */     } 
/*  824 */     EpicEntity stolen = null;
/*  825 */     if (allies.size() > 0) {
/*      */       
/*  827 */       stolen = allies.get(rand.nextInt(allies.size()));
/*  828 */       stolen.setCompanion(entity);
/*      */     } 
/*  830 */     return stolen;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   EpicEntity returnCharmedEnemyEntity(EpicEntity entity) {
/*  838 */     EpicEntity stolen = null;
/*  839 */     switch ((int)entity.getId()) {
/*      */       
/*      */       case 1:
/*  842 */         stolen = getEntity(7L);
/*      */         break;
/*      */       case 3:
/*  845 */         stolen = getEntity(6L);
/*      */         break;
/*      */       case 2:
/*  848 */         stolen = getEntity(9L);
/*      */         break;
/*      */       case 4:
/*  851 */         stolen = getEntity(8L);
/*      */         break;
/*      */     } 
/*      */ 
/*      */     
/*  856 */     if (stolen != null && stolen.getCompanion() != entity) {
/*      */       
/*  858 */       stolen.setCompanion(entity);
/*      */     } else {
/*      */       
/*  861 */       stolen = null;
/*  862 */     }  return stolen;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setWinEffects(EpicEntity entity, String collName, int nums) {
/*  882 */     for (EpicEntity e : getAllEntities()) {
/*      */       
/*  884 */       if (e.isDeity() && e != entity) {
/*      */         
/*  886 */         HashMap<Integer, EpicEntity.SkillVal> skills = e.getAllSkills();
/*  887 */         for (Iterator<Integer> iterator = skills.keySet().iterator(); iterator.hasNext(); ) { int id = ((Integer)iterator.next()).intValue();
/*      */           
/*  889 */           EpicEntity.SkillVal sv = skills.get(Integer.valueOf(id));
/*  890 */           if (sv.getCurrentVal() > sv.getDefaultVal()) {
/*  891 */             e.setSkill(id, sv.getCurrentVal() - (100.0F - sv.getCurrentVal()) / 100.0F);
/*      */           } }
/*      */       
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  899 */     MapHex gatherPlace = getSpawnHex(entity);
/*  900 */     if (getHexNumRequiredToWin() > 0) {
/*  901 */       gatherPlace = getMapHex(getHexNumRequiredToWin());
/*      */     }
/*  903 */     String gatherPlaceS = "Home";
/*  904 */     if (gatherPlace != null)
/*  905 */       gatherPlaceS = gatherPlace.getName(); 
/*  906 */     String collMultipleName = collName;
/*  907 */     if (nums > 1)
/*  908 */       collMultipleName = collName + "s"; 
/*  909 */     String toBroadCast = entity.getName() + " has gathered the " + collMultipleName + " required at " + gatherPlaceS + ".";
/*      */     
/*  911 */     broadCast(entity.getName() + " has gathered the " + collMultipleName + " required at " + gatherPlaceS + ".");
/*      */     
/*  913 */     applyDeityScenarioReward(entity, toBroadCast, collMultipleName);
/*      */     
/*  915 */     if (!entity.isWurm()) {
/*      */       
/*  917 */       Deity deity = Deities.getDeity((int)entity.getId());
/*  918 */       if (deity != null) {
/*      */         
/*  920 */         ConcurrentHashMap<Long, Float> allHelpers = deity.getHelpers();
/*  921 */         float maxHelperVal = 0.0F;
/*  922 */         for (Float f : allHelpers.values()) {
/*  923 */           if (f.floatValue() > maxHelperVal) {
/*  924 */             maxHelperVal = f.floatValue();
/*      */           }
/*      */         } 
/*  927 */         long tierOneWinner = getWinningHelper(allHelpers, 0.8F, 300);
/*  928 */         if (tierOneWinner > -10L) {
/*      */           
/*  930 */           float winnerVal = ((Float)allHelpers.get(Long.valueOf(tierOneWinner))).floatValue();
/*  931 */           int randomTomeId = 795 + Server.rand.nextInt(16);
/*  932 */           if (winnerVal >= maxHelperVal * 0.95F && 
/*  933 */             Server.rand.nextInt(50) == 0) {
/*  934 */             randomTomeId = (Server.rand.nextInt(5) == 0) ? 794 : 465;
/*      */           }
/*  936 */           sendWinnerItem(deity, tierOneWinner, randomTomeId, false);
/*  937 */           allHelpers.replace(Long.valueOf(tierOneWinner), Float.valueOf(0.0F));
/*      */ 
/*      */           
/*  940 */           if (randomTomeId == 794 || randomTomeId == 465) {
/*      */             return;
/*      */           }
/*      */         } 
/*      */         
/*  945 */         long tierTwoWinner = -10L;
/*  946 */         for (int i = 0; i < 3; i++) {
/*      */           
/*  948 */           tierTwoWinner = getWinningHelper(allHelpers, 0.5F, 300);
/*  949 */           if (tierTwoWinner > -10L) {
/*      */             
/*  951 */             int randomTomeId = 795 + Server.rand.nextInt(16);
/*  952 */             sendWinnerItem(deity, tierTwoWinner, randomTomeId, true);
/*  953 */             allHelpers.replace(Long.valueOf(tierTwoWinner), Float.valueOf(0.0F));
/*      */           } 
/*      */         } 
/*      */ 
/*      */         
/*  958 */         long tierThreeWinner = -10L;
/*  959 */         for (int j = 0; j < 5; j++) {
/*      */           
/*  961 */           tierThreeWinner = getWinningHelper(allHelpers, 0.0F, 300);
/*  962 */           if (tierThreeWinner > -10L) {
/*      */             
/*  964 */             boolean fragment = Server.rand.nextBoolean();
/*      */             
/*  966 */             int randomTomeId = fragment ? (795 + Server.rand.nextInt(16)) : 837;
/*  967 */             sendWinnerItem(deity, tierThreeWinner, randomTomeId, fragment);
/*  968 */             allHelpers.replace(Long.valueOf(tierThreeWinner), Float.valueOf(0.0F));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void applyDeityScenarioReward(EpicEntity entity, String toBroadCast, String collMultipleName) {
/*      */     EpicEntity wasStolen1, stolen;
/*      */     Iterator<Integer> iterator;
/*      */     int i;
/*      */     EpicEntity charmed;
/*      */     int additionalRoll, j;
/* 1013 */     String rest = "";
/* 1014 */     HashMap<Integer, EpicEntity.SkillVal> skills = entity.getAllSkills();
/* 1015 */     switch (getReasonAndEffectInt()) {
/*      */       
/*      */       case 0:
/* 1018 */         if (entity.isWurm()) {
/* 1019 */           rest = "The Wurm punishes you all!";
/*      */         } else {
/* 1021 */           rest = entity.getName() + " uses the " + collMultipleName + " aggressively.";
/* 1022 */         }  broadCast(rest);
/*      */         break;
/*      */       case 1:
/* 1025 */         if (entity.isWurm()) {
/* 1026 */           rest = "The Wurm swallows the " + collMultipleName + " whole!";
/*      */         } else {
/* 1028 */           rest = entity.getName() + " stores the " + collMultipleName + " in a secret place.";
/* 1029 */         }  broadCast(rest);
/*      */         break;
/*      */       case 2:
/* 1032 */         if (entity.isWurm()) {
/* 1033 */           rest = "The Wurm relishes in your pain!";
/*      */         } else {
/* 1035 */           rest = entity.getName() + " makes good use of the " + collMultipleName + ".";
/* 1036 */         }  broadCast(rest);
/*      */         break;
/*      */       case 3:
/*      */       case 4:
/* 1040 */         if (entity.isWurm()) {
/* 1041 */           rest = "The Wurm punishes you all!";
/*      */         } else {
/* 1043 */           rest = entity.getName() + " will use the " + collMultipleName + " instead.";
/* 1044 */         }  broadCast(rest);
/*      */         
/* 1046 */         wasStolen1 = returnCharmedEnemyEntity(entity);
/* 1047 */         if (wasStolen1 != null) {
/*      */           
/* 1049 */           String nrest = wasStolen1.getName() + " is convinced to return to the side of " + entity.getName() + ".";
/* 1050 */           broadCast(nrest);
/* 1051 */           rest = rest + " " + nrest;
/*      */         } 
/*      */         break;
/*      */       case 5:
/* 1055 */         if (entity.isWurm()) {
/* 1056 */           rest = "Wurm releases aggravated souls all over the lands.";
/*      */         } else {
/* 1058 */           rest = entity.getName() + " directs aggravated souls at the enemy.";
/* 1059 */         }  broadCast(rest);
/* 1060 */         setCreatureController(74, entity.getId());
/*      */         break;
/*      */       case 6:
/*      */       case 9:
/* 1064 */         if (entity.isWurm()) {
/* 1065 */           rest = "Wurm uses the power gained to influence Valrei!";
/*      */         } else {
/* 1067 */           rest = entity.getName() + " tries to gain influence on Valrei!";
/* 1068 */         }  broadCast(rest);
/*      */         
/* 1070 */         stolen = charmEnemyEntity(entity);
/* 1071 */         if (stolen != null) {
/*      */           
/* 1073 */           String nrest = stolen.getName() + " is charmed by the power of " + entity.getName() + " and switches sides!";
/* 1074 */           broadCast(nrest);
/* 1075 */           rest = rest + " " + nrest;
/*      */         } 
/*      */         break;
/*      */       case 7:
/* 1079 */         if (entity.isWurm()) {
/* 1080 */           rest = "The Wurm increases its power even more!";
/*      */         } else {
/*      */           
/* 1083 */           rest = "The power of the " + collMultipleName + " is used by " + entity.getName() + " to grow in power";
/* 1084 */           if (isWurmAwake()) {
/* 1085 */             rest = rest + " and weaken the Wurm.";
/*      */           } else {
/* 1087 */             rest = rest + ".";
/*      */           } 
/* 1089 */         }  broadCast(rest);
/*      */         
/* 1091 */         for (iterator = skills.keySet().iterator(); iterator.hasNext(); ) { int id = ((Integer)iterator.next()).intValue();
/*      */           
/* 1093 */           EpicEntity.SkillVal sv = skills.get(Integer.valueOf(id));
/* 1094 */           entity.setSkill(id, sv.getCurrentVal() + (100.0F - sv.getCurrentVal()) / 200.0F); }
/*      */         
/* 1096 */         if (isWurmAwake() && !entity.isWurm()) {
/*      */           
/* 1098 */           EpicEntity wurm = getEntity(5L);
/* 1099 */           if (wurm != null)
/* 1100 */             for (Iterator<Integer> iterator1 = skills.keySet().iterator(); iterator1.hasNext(); ) { int id = ((Integer)iterator1.next()).intValue();
/*      */               
/* 1102 */               EpicEntity.SkillVal sv = skills.get(Integer.valueOf(id));
/* 1103 */               wurm.setSkill(id, sv.getCurrentVal() - (100.0F - sv.getCurrentVal()) / 25.0F); }
/*      */              
/* 1105 */           rest = "Wurm is weakened.";
/* 1106 */           broadCast(rest);
/*      */         } 
/*      */         break;
/*      */       case 8:
/* 1110 */         if (entity.isWurm()) {
/* 1111 */           rest = "Those were needed to keep the Demons from Sol at bay!";
/*      */         } else {
/* 1113 */           rest = entity.getName() + " now controls the Demons from Sol!";
/* 1114 */         }  broadCast(rest);
/* 1115 */         setCreatureController(72, entity.getId());
/*      */         break;
/*      */       case 10:
/* 1118 */         rest = entity.getName() + " receives significant knowledge from using the " + collMultipleName + ".";
/* 1119 */         broadCast(rest);
/*      */         
/* 1121 */         entity.increaseRandomSkill(100.0F);
/* 1122 */         entity.increaseRandomSkill(100.0F);
/*      */         break;
/*      */       case 11:
/* 1125 */         rest = entity.getName() + " now controls the Eagle Spirits!";
/* 1126 */         broadCast(rest);
/* 1127 */         setCreatureController(77, entity.getId());
/*      */         break;
/*      */       case 12:
/*      */       case 14:
/* 1131 */         rest = "The deities are weakened as Valrei is struck with a mysterious disease.";
/* 1132 */         broadCast(rest);
/* 1133 */         diseaseAllBut(entity);
/*      */         break;
/*      */       case 13:
/* 1136 */         rest = entity.getName() + " now commands the Deathcrawler's minions!";
/* 1137 */         broadCast(rest);
/* 1138 */         setCreatureController(73, entity.getId());
/*      */         break;
/*      */       case 15:
/* 1141 */         rest = entity.getName() + " now commands the sons of Nogump!";
/* 1142 */         broadCast(rest);
/* 1143 */         setCreatureController(75, entity.getId());
/*      */         break;
/*      */       case 16:
/* 1146 */         rest = entity.getName() + " grows from the immense power of the " + collMultipleName + ".";
/* 1147 */         broadCast(rest);
/*      */         
/* 1149 */         for (i = 0; i < 4; i++)
/* 1150 */           entity.increaseRandomSkill(100.0F); 
/*      */         break;
/*      */       case 17:
/* 1153 */         rest = entity.getName() + " now controls the Spirit Drakes!";
/* 1154 */         broadCast(rest);
/* 1155 */         setCreatureController(76, entity.getId());
/*      */         break;
/*      */       case 18:
/* 1158 */         rest = entity.getName() + " attempts to disrupt Valrei with the newfound power from the " + collMultipleName + ".";
/* 1159 */         broadCast(rest);
/*      */         
/* 1161 */         for (i = 0; i < 4; i++) {
/* 1162 */           entity.increaseRandomSkill(100.0F);
/*      */         }
/* 1164 */         charmed = charmEnemyEntity(entity);
/* 1165 */         if (charmed != null) {
/*      */           
/* 1167 */           String nrest = charmed.getName() + " is charmed by the power of " + entity.getName() + " and switches sides!";
/* 1168 */           broadCast(nrest);
/* 1169 */           rest = rest + " " + nrest;
/*      */         } 
/*      */         break;
/*      */       
/*      */       case 19:
/* 1174 */         additionalRoll = rand.nextInt(5);
/* 1175 */         switch (additionalRoll) {
/*      */           
/*      */           case 0:
/*      */           case 1:
/* 1179 */             rest = entity.getName() + " unlocks their hidden potential from using the " + collMultipleName + ".";
/* 1180 */             broadCast(rest);
/* 1181 */             for (j = 0; j < 6; j++)
/* 1182 */               entity.increaseRandomSkill(75.0F); 
/*      */             break;
/*      */           case 2:
/*      */           case 3:
/* 1186 */             rest = entity.getName() + " reveals some true power from within the " + collMultipleName + ".";
/* 1187 */             broadCast(rest);
/* 1188 */             for (j = 0; j < 6; j++)
/* 1189 */               entity.increaseRandomSkill(50.0F); 
/*      */             break;
/*      */           case 4:
/* 1192 */             rest = entity.getName() + " will rule both heaven and earth and may promote an ally!";
/* 1193 */             broadCast(rest);
/* 1194 */             for (j = 0; j < 6; j++) {
/* 1195 */               entity.increaseRandomSkill(50.0F);
/*      */             }
/* 1197 */             crushAllBut(entity);
/*      */             
/* 1199 */             Effectuator.promoteImmortal(entity.getId());
/*      */             break;
/*      */         } 
/*      */ 
/*      */         
/*      */         break;
/*      */     } 
/*      */ 
/*      */     
/* 1208 */     WcEpicEvent wce = new WcEpicEvent(WurmId.getNextWCCommandId(), getReasonAndEffectInt(), entity.getId(), 0, 0, toBroadCast + " " + rest, true);
/*      */ 
/*      */     
/* 1211 */     wce.sendFromLoginServer();
/*      */   }
/*      */ 
/*      */   
/*      */   private void sendWinnerItem(Deity deity, long winnerId, int itemId, boolean fragment) {
/* 1216 */     PlayerInfo pinf = PlayerInfoFactory.getPlayerInfoWithWurmId(winnerId);
/* 1217 */     logger.log(Level.INFO, winnerId + " won the prize of " + itemId + " from " + deity.getName());
/* 1218 */     if (pinf != null) {
/*      */       
/* 1220 */       if (!pinf.loaded) {
/*      */         
/*      */         try {
/*      */           
/* 1224 */           pinf.load();
/*      */         }
/* 1226 */         catch (IOException iox) {
/*      */           
/* 1228 */           logger.log(Level.WARNING, iox.getMessage(), iox);
/*      */         } 
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1234 */       WcEpicEvent wcwin = new WcEpicEvent(WurmId.getNextWCCommandId(), 0, pinf.wurmId, itemId, fragment ? 5 : 4, deity.getName(), false);
/* 1235 */       wcwin.sendFromLoginServer();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private long getWinningHelper(ConcurrentHashMap<Long, Float> allHelpers, float tierPercentage, int ticketCost) {
/* 1241 */     HashMap<Long, Integer> ticketCounts = new HashMap<>();
/*      */     
/* 1243 */     float maxHelperVal = 0.0F;
/* 1244 */     for (Float f : allHelpers.values()) {
/* 1245 */       if (f.floatValue() > maxHelperVal)
/* 1246 */         maxHelperVal = f.floatValue(); 
/*      */     } 
/* 1248 */     int totalTickets = 0;
/* 1249 */     for (Long l : allHelpers.keySet()) {
/*      */       
/* 1251 */       if (((Float)allHelpers.get(l)).floatValue() >= maxHelperVal * tierPercentage) {
/*      */         
/* 1253 */         float totalTier = ((Float)allHelpers.get(l)).floatValue() - maxHelperVal * tierPercentage;
/* 1254 */         int ticketCount = (int)(totalTier / ticketCost);
/* 1255 */         totalTickets += ticketCount;
/* 1256 */         ticketCounts.put(l, Integer.valueOf(ticketCount));
/*      */       } 
/*      */     } 
/* 1259 */     if (totalTickets > 0) {
/*      */       
/* 1261 */       int currentTicket = 0;
/* 1262 */       long[] tickets = new long[totalTickets];
/* 1263 */       for (Long l : ticketCounts.keySet()) {
/* 1264 */         for (int i = 0; i < ((Integer)ticketCounts.get(l)).intValue(); i++)
/* 1265 */           tickets[currentTicket++] = l.longValue(); 
/*      */       } 
/* 1267 */       return tickets[Server.rand.nextInt(totalTickets)];
/*      */     } 
/*      */     
/* 1270 */     return -10L;
/*      */   }
/*      */ 
/*      */   
/*      */   private void diseaseAllBut(EpicEntity winner) {
/* 1275 */     logger.info("Disease all but the winning epic entity: " + winner.getName());
/*      */     
/* 1277 */     EpicEntity[] entits = getAllEntities();
/* 1278 */     for (EpicEntity entity : entits) {
/*      */       
/* 1280 */       if (entity != winner) {
/*      */         
/* 1282 */         if (entity.isDeity() || entity.isWurm())
/*      */         {
/* 1284 */           if (!entity.isFriend(winner)) {
/*      */             
/* 1286 */             HashMap<Integer, EpicEntity.SkillVal> skills = entity.getAllSkills();
/* 1287 */             for (Iterator<Integer> iterator = skills.keySet().iterator(); iterator.hasNext(); ) { int id = ((Integer)iterator.next()).intValue();
/*      */               
/* 1289 */               EpicEntity.SkillVal sv = skills.get(Integer.valueOf(id));
/* 1290 */               entity.setSkill(id, sv.getCurrentVal() - (100.0F - sv.getCurrentVal()) / 100.0F); }
/*      */           
/*      */           } 
/*      */         }
/* 1294 */         if (entity.isDemigod()) {
/*      */           
/* 1296 */           HashMap<Integer, EpicEntity.SkillVal> skills = entity.getAllSkills();
/* 1297 */           for (Iterator<Integer> iterator = skills.keySet().iterator(); iterator.hasNext(); ) { int id = ((Integer)iterator.next()).intValue();
/*      */             
/* 1299 */             EpicEntity.SkillVal sv = skills.get(Integer.valueOf(id));
/* 1300 */             entity.setSkill(id, sv.getCurrentVal() - (100.0F - sv.getCurrentVal()) / 100.0F); }
/*      */         
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void crushAllBut(EpicEntity winner) {
/* 1309 */     logger.info("Crushing all but the winning epic entity: " + winner.getName());
/*      */     
/* 1311 */     setCreatureController(74, winner.getId());
/* 1312 */     setCreatureController(76, winner.getId());
/* 1313 */     setCreatureController(77, winner.getId());
/* 1314 */     setCreatureController(75, winner.getId());
/* 1315 */     setCreatureController(73, winner.getId());
/* 1316 */     setCreatureController(72, winner.getId());
/*      */     
/* 1318 */     EpicEntity[] entits = getAllEntities();
/* 1319 */     for (EpicEntity entity : entits) {
/*      */       
/* 1321 */       if (entity != winner) {
/*      */         
/* 1323 */         if (entity.isDeity() || entity.isWurm())
/*      */         {
/* 1325 */           if (!entity.isFriend(winner)) {
/*      */             
/* 1327 */             HashMap<Integer, EpicEntity.SkillVal> skills = entity.getAllSkills();
/* 1328 */             for (Iterator<Integer> iterator = skills.keySet().iterator(); iterator.hasNext(); ) { int id = ((Integer)iterator.next()).intValue();
/*      */               
/* 1330 */               EpicEntity.SkillVal sv = skills.get(Integer.valueOf(id));
/* 1331 */               entity.setSkill(id, sv.getCurrentVal() - (100.0F - sv.getCurrentVal()) / 50.0F); }
/*      */           
/*      */           } 
/*      */         }
/* 1335 */         if (entity.isDemigod())
/*      */         {
/* 1337 */           if (!entity.isFriend(winner))
/*      */           {
/* 1339 */             if (rand.nextInt(15) == 0) {
/*      */               
/* 1341 */               broadCast(entity.getName() + " is put to eternal sleep and dismissed to the void!");
/* 1342 */               destroyEntity(entity);
/*      */             }
/*      */             else {
/*      */               
/* 1346 */               if (entity.getMapHex() != null) {
/* 1347 */                 broadCast(entity.getName() + " is spared and will stay in " + entity.getMapHex().getName() + " for now.");
/*      */               } else {
/*      */                 
/* 1350 */                 broadCast(entity.getName() + " is spared and will stay on Valrei for now.");
/*      */               } 
/* 1352 */               HashMap<Integer, EpicEntity.SkillVal> skills = entity.getAllSkills();
/* 1353 */               for (Iterator<Integer> iterator = skills.keySet().iterator(); iterator.hasNext(); ) { int id = ((Integer)iterator.next()).intValue();
/*      */                 
/* 1355 */                 EpicEntity.SkillVal sv = skills.get(Integer.valueOf(id));
/* 1356 */                 entity.setSkill(id, sv.getCurrentVal() - (100.0F - sv.getCurrentVal()) / 50.0F); }
/*      */             
/*      */             } 
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
/*      */   String getReason(int reasonId, boolean many) {
/*      */     String firstPart;
/* 1374 */     switch (reasonId)
/*      */     
/*      */     { case 0:
/* 1377 */         if (many) {
/* 1378 */           firstPart = "If we acquire these we will gain immense strength!";
/*      */         } else {
/* 1380 */           firstPart = "If we acquire this we will gain immense strength!";
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1503 */         return firstPart;case 1: if (many) { firstPart = "These can be used to deal a severe blow to our enemies!"; } else { firstPart = "It can be used to deal a severe blow to our enemies!"; }  return firstPart;case 2: if (many) { firstPart = "If we aquire these we will gain considerable power!"; } else { firstPart = "If we aquire this we will gain considerable power!"; }  return firstPart;case 3: if (many) { firstPart = "These must not fall into the claws of the Wurm!"; } else { firstPart = "This must not fall into the claws of the Wurm!"; }  return firstPart;case 4: if (many) { firstPart = "The power of these are not to be underestimated!"; } else { firstPart = "The power of this one is not to be underestimated!"; }  return firstPart;case 5: if (many) { firstPart = "They will contain soul traces of utter importance!"; } else { firstPart = "It will contain soul traces of utter importance!"; }  return firstPart;case 6: if (many) { firstPart = "Woe befalls our enemies if we lay our hands on them!"; } else { firstPart = "Woe befalls our enemies if we lay our hands on it!"; }  return firstPart;case 7: if (many) { firstPart = "Apparently, these can be used to diminish Wurm's power."; } else { firstPart = "Apparently, this can be used to diminish Wurm's power."; }  return firstPart;case 8: if (many) { firstPart = "These are needed to keep the Demons from Sol at bay!"; } else { firstPart = "This is needed to keep the Demons from Sol at bay!"; }  return firstPart;case 9: if (many) { firstPart = "We can use these to erupt our enemy lands!"; } else { firstPart = "We can use it to erupt our enemy lands!"; }  return firstPart;case 10: if (many) { firstPart = "These are said to be truly worth dying for."; } else { firstPart = "It is said that this one is truly worth dying for."; }  return firstPart;case 11: if (many) { firstPart = "These may cause mayhem if used by the wrong hands!"; } else { firstPart = "It may cause mayhem if used by the wrong hands!"; }  return firstPart;case 12: if (many) { firstPart = "Meteors will rain down on our enemies if we get hold of these!"; } else { firstPart = "Meteors will rain down on our enemies if we get hold of this!"; }  return firstPart;case 13: if (many) { firstPart = "These are used to protect us from the Deathcrawler's minions!"; } else { firstPart = "It is used to protect us from the Deathcrawler's minions!"; }  return firstPart;case 14: if (many) { firstPart = "Unless we recover these, we will be severely weakened!"; } else { firstPart = "Unless we recover this, we will be severely weakened!"; }  return firstPart;case 15: if (many) { firstPart = "We can create world-changing things with these!"; } else { firstPart = "We can create world-changing things with this one!"; }  return firstPart;case 16: if (many) { firstPart = "The insights gained from these now will be magnificent!"; } else { firstPart = "The insights gained from this now will be magnificent!"; }  return firstPart;case 17: if (many) { firstPart = "Great damage would befall us if they end up in enemy hands."; } else { firstPart = "Great damage would befall us if it ends up in enemy hands."; }  return firstPart;case 18: if (many) { firstPart = "Whoever finds these will rise in power!"; } else { firstPart = "Whoever finds this one will rise in power!"; }  return firstPart;case 19: if (many) { firstPart = "Immense power awaits for those who find these!"; } else { firstPart = "Immense power awaits for those who find this!"; }  return firstPart; }  if (many) { firstPart = "We cannot allow our enemies to get their hands on these!"; } else { firstPart = "We cannot allow our enemies to get their hands on this!"; }  return firstPart;
/*      */   }
/*      */ 
/*      */   
/*      */   public void testValreiFight(Creature performer) {
/* 1508 */     EpicEntity fighter1 = getEntity((Server.rand.nextInt(4) + 1));
/* 1509 */     EpicEntity fighter2 = getEntity((Server.rand.nextInt(4) + 1));
/* 1510 */     while (fighter2 == fighter1) {
/* 1511 */       fighter2 = getEntity((Server.rand.nextInt(4) + 1));
/*      */     }
/* 1513 */     int wins1 = 0;
/* 1514 */     int wins2 = 0;
/*      */     
/* 1516 */     for (int i = 0; i < 1000; i++) {
/*      */       
/* 1518 */       ValreiFight vFight = new ValreiFight(getSpawnHex(fighter1), fighter1, fighter2);
/* 1519 */       ValreiFightHistory fightHistory = vFight.completeFight(true);
/* 1520 */       if (fightHistory.getFightWinner() == fighter1.getId()) {
/* 1521 */         wins1++;
/*      */       } else {
/* 1523 */         wins2++;
/*      */       } 
/*      */     } 
/* 1526 */     performer.getCommunicator().sendNormalServerMessage("1000 fights completed between " + fighter1.getName() + " and " + fighter2.getName() + ".");
/* 1527 */     performer.getCommunicator().sendNormalServerMessage(fighter1.getName() + " won " + (wins1 / 10.0F) + "% of fights, " + fighter2
/* 1528 */         .getName() + " won the remaining " + (wins2 / 10.0F) + "%.");
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
/*      */   public void testSingleValreiFight(Creature performer, Item source) {
/* 1540 */     EpicEntity fighter1 = getEntity(source.getData1());
/* 1541 */     EpicEntity fighter2 = getEntity(source.getData2());
/*      */     
/* 1543 */     if (fighter1 == null) {
/* 1544 */       performer.getCommunicator().sendNormalServerMessage("Invalid entity id for fighter 1: " + source.getData1() + ". Set a valid entity in Data1 of the " + source
/* 1545 */           .getName() + ".");
/* 1546 */     } else if (fighter2 == null) {
/* 1547 */       performer.getCommunicator().sendNormalServerMessage("Invalid entity id for fighter 2: " + source.getData2() + ". Set a valid entity in Data2 of the " + source
/* 1548 */           .getName() + ".");
/* 1549 */     } else if (fighter1 == fighter2) {
/* 1550 */       performer.getCommunicator().sendNormalServerMessage("Cannot fight two entities that are the same. Pick a different second entity by setting the Data2 of the " + source
/* 1551 */           .getName() + ".");
/* 1552 */     }  if (fighter1 == null || fighter2 == null || fighter1 == fighter2) {
/*      */       return;
/*      */     }
/* 1555 */     ValreiFight vFight = new ValreiFight(getSpawnHex(fighter1), fighter1, fighter2);
/* 1556 */     ValreiFightHistory fightHistory = vFight.completeFight(false);
/* 1557 */     ValreiFightHistoryManager.getInstance().addFight(fightHistory.getFightId(), fightHistory);
/*      */     
/* 1559 */     if (Servers.localServer.LOGINSERVER) {
/*      */       
/* 1561 */       WCValreiMapUpdater updater = new WCValreiMapUpdater(WurmId.getNextWCCommandId(), (byte)5);
/* 1562 */       updater.sendFromLoginServer();
/*      */     } 
/*      */     
/* 1565 */     performer.getCommunicator().sendNormalServerMessage("Fight complete between " + fighter1.getName() + " and " + fighter2.getName() + ".");
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\epic\Valrei.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */