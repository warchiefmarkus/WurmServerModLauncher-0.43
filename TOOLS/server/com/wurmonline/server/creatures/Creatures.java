/*      */ package com.wurmonline.server.creatures;
/*      */ 
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.server.Constants;
/*      */ import com.wurmonline.server.DbConnector;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.LoginHandler;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.NoSuchPlayerException;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.WurmCalendar;
/*      */ import com.wurmonline.server.WurmId;
/*      */ import com.wurmonline.server.behaviours.Seat;
/*      */ import com.wurmonline.server.behaviours.Vehicle;
/*      */ import com.wurmonline.server.behaviours.Vehicles;
/*      */ import com.wurmonline.server.bodys.BodyFactory;
/*      */ import com.wurmonline.server.deities.Deities;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.players.PlayerInfo;
/*      */ import com.wurmonline.server.players.PlayerInfoFactory;
/*      */ import com.wurmonline.server.skills.Skills;
/*      */ import com.wurmonline.server.structures.NoSuchStructureException;
/*      */ import com.wurmonline.server.structures.Structure;
/*      */ import com.wurmonline.server.structures.Structures;
/*      */ import com.wurmonline.server.tutorial.MissionTargets;
/*      */ import com.wurmonline.server.utils.DbUtilities;
/*      */ import com.wurmonline.server.villages.NoSuchRoleException;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.villages.Villages;
/*      */ import com.wurmonline.server.zones.Den;
/*      */ import com.wurmonline.server.zones.Dens;
/*      */ import com.wurmonline.server.zones.NoSuchZoneException;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import java.io.IOException;
/*      */ import java.sql.Connection;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.Statement;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.Timer;
/*      */ import java.util.TimerTask;
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
/*      */ public final class Creatures
/*      */   implements MiscConstants, CreatureTemplateIds, TimeConstants
/*      */ {
/*      */   private final Map<Long, Creature> creatures;
/*      */   private final Map<Long, Creature> offlineCreatures;
/*      */   private final ConcurrentHashMap<Long, Creature> avatars;
/*   82 */   private final Map<Long, Long> protectedCreatures = new ConcurrentHashMap<>();
/*   83 */   private final Map<String, Creature> npcs = new ConcurrentHashMap<>();
/*      */   private final Map<Integer, Integer> creaturesByType;
/*   85 */   private static Creatures instance = null;
/*      */ 
/*      */   
/*   88 */   private static Logger logger = Logger.getLogger(Creatures.class.getName());
/*      */   
/*      */   private static final String getAllCreatures = "SELECT * FROM CREATURES";
/*      */   
/*      */   private static final String COUNT_CREATURES = "SELECT COUNT(*) FROM CREATURES";
/*      */   
/*      */   private static final String DELETE_CREATURE = "DELETE FROM CREATURES WHERE WURMID=?";
/*      */   
/*      */   private static final String DELETE_CREATUREBODY = "DELETE FROM BODYPARTS WHERE OWNERID=?";
/*      */   
/*      */   private static final String DELETE_CREATURESKILLS = "DELETE FROM SKILLS WHERE OWNER=?";
/*      */   private static final String DELETE_CREATUREITEMS = "DELETE FROM ITEMS WHERE OWNERID=?";
/*      */   private static final String DELETE_CREATURE_SPLIT = "DELETE FROM CREATURES_BASE WHERE WURMID=?";
/*      */   private static final String DELETE_CREATURE_POS_SPLIT = "DELETE FROM CREATURES_POS WHERE WURMID=?";
/*      */   private static final String DELETE_PROT_CREATURE = "DELETE FROM PROTECTED WHERE WURMID=?";
/*      */   private static final String INSERT_PROT_CREATURE = "INSERT INTO PROTECTED (WURMID,PLAYERID) VALUES(?,?)";
/*      */   private static final String LOAD_PROT_CREATURES = "SELECT * FROM PROTECTED";
/*      */   private static final boolean fixColourTraits = false;
/*  106 */   private final Map<Long, Brand> brandedCreatures = new ConcurrentHashMap<>();
/*      */   
/*  108 */   private final Map<Long, Long> ledCreatures = new ConcurrentHashMap<>();
/*      */ 
/*      */   
/*      */   private static Map<Long, Creature> rideCreatures;
/*      */   
/*      */   private final Timer creaturePollThread;
/*      */   
/*      */   private final PollTimerTask pollTask;
/*      */   
/*  117 */   private int numberOfNice = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  122 */   private int numberOfAgg = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  127 */   private int numberOfTyped = 0;
/*  128 */   private int kingdomCreatures = 0;
/*  129 */   private static int destroyedCaveCrets = 0;
/*      */   private static boolean loading = false;
/*  131 */   private static int nums = 0;
/*      */   
/*  133 */   private static int seaMonsters = 0;
/*  134 */   private static int seaHunters = 0;
/*      */   
/*  136 */   private int currentCreature = 0;
/*      */   private Creature[] crets;
/*  138 */   public int numberOfZonesX = 64;
/*  139 */   private long totalTime = 0L;
/*  140 */   private long startTime = 0L;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean logCreaturePolls = false;
/*      */ 
/*      */ 
/*      */   
/*      */   public static Creatures getInstance() {
/*  149 */     if (instance == null)
/*  150 */       instance = new Creatures(); 
/*  151 */     return instance;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getNumberOfCreatures() {
/*  158 */     return this.creatures.size();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getNumberOfCreaturesWithTemplate(int templateChecked) {
/*  163 */     int toReturn = 0;
/*  164 */     for (Creature cret : this.creatures.values()) {
/*      */       
/*  166 */       if (cret.getTemplate().getTemplateId() == templateChecked)
/*      */       {
/*  168 */         toReturn++;
/*      */       }
/*      */     } 
/*  171 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void setLastLed(long creatureLed, long leader) {
/*  176 */     this.ledCreatures.put(Long.valueOf(creatureLed), Long.valueOf(leader));
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean wasLastLed(long potentialLeader, long creatureLed) {
/*  181 */     Long lastLeader = this.ledCreatures.get(Long.valueOf(creatureLed));
/*  182 */     if (lastLeader != null)
/*  183 */       return (lastLeader.longValue() == potentialLeader); 
/*  184 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void addBrand(Brand brand) {
/*  189 */     this.brandedCreatures.put(Long.valueOf(brand.getCreatureId()), brand);
/*      */   }
/*      */ 
/*      */   
/*      */   public final void setBrand(long creatureId, long brandid) {
/*  194 */     if (brandid <= 0L) {
/*  195 */       this.brandedCreatures.remove(Long.valueOf(creatureId));
/*      */     } else {
/*      */       
/*  198 */       Brand brand = this.brandedCreatures.get(Long.valueOf(creatureId));
/*  199 */       if (brand == null) {
/*  200 */         brand = new Brand(creatureId, System.currentTimeMillis(), brandid, false);
/*      */       } else {
/*  202 */         brand.setBrandId(brandid);
/*  203 */       }  this.brandedCreatures.put(Long.valueOf(creatureId), brand);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final Brand getBrand(long creatureId) {
/*  209 */     Brand brand = this.brandedCreatures.get(Long.valueOf(creatureId));
/*  210 */     return brand;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isBrandedBy(long creatureId, long brandId) {
/*  215 */     Brand brand = this.brandedCreatures.get(Long.valueOf(creatureId));
/*  216 */     if (brand != null)
/*  217 */       return (brand.getBrandId() == brandId); 
/*  218 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public final Creature[] getBranded(long villageId) {
/*  223 */     Map<Long, Brand> removeMap = new ConcurrentHashMap<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  229 */     Set<Creature> brandedSet = new HashSet<>();
/*  230 */     for (Brand b : this.brandedCreatures.values()) {
/*      */       
/*  232 */       if (b.getBrandId() == villageId) {
/*      */         
/*      */         try {
/*      */           
/*  236 */           brandedSet.add(getCreature(b.getCreatureId()));
/*      */         }
/*  238 */         catch (NoSuchCreatureException e) {
/*      */ 
/*      */           
/*  241 */           Long cid = new Long(b.getCreatureId());
/*  242 */           if (isCreatureOffline(cid)) {
/*      */             
/*  244 */             Creature creature = this.offlineCreatures.get(cid);
/*  245 */             brandedSet.add(creature);
/*      */             
/*      */             continue;
/*      */           } 
/*  249 */           removeMap.put(Long.valueOf(b.getCreatureId()), b);
/*      */         } 
/*      */       }
/*      */     } 
/*  253 */     return brandedSet.<Creature>toArray(new Creature[brandedSet.size()]);
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
/*      */   public void removeBrandingFor(int villageId) {
/*  268 */     for (Brand b : this.brandedCreatures.values()) {
/*      */       
/*  270 */       if (b.getBrandId() == villageId) {
/*  271 */         b.deleteBrand();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getNumberOfNice() {
/*  281 */     return this.numberOfNice;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getNumberOfAgg() {
/*  290 */     return this.numberOfAgg;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getNumberOfTyped() {
/*  299 */     return this.numberOfTyped;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getNumberOfKingdomCreatures() {
/*  308 */     return this.kingdomCreatures;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getNumberOfSeaMonsters() {
/*  317 */     return seaMonsters;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getNumberOfSeaHunters() {
/*  326 */     return seaHunters;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Creatures() {
/*  336 */     int numberOfCreaturesInDatabase = Math.max(getNumberOfCreaturesInDatabase(), 100);
/*  337 */     this.creatures = new ConcurrentHashMap<>(numberOfCreaturesInDatabase);
/*  338 */     this.avatars = new ConcurrentHashMap<>();
/*  339 */     this.creaturesByType = new ConcurrentHashMap<>(numberOfCreaturesInDatabase);
/*  340 */     this.offlineCreatures = new ConcurrentHashMap<>();
/*  341 */     this.creaturePollThread = new Timer();
/*  342 */     this.pollTask = new PollTimerTask();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void startPollTask() {}
/*      */ 
/*      */ 
/*      */   
/*      */   public final void shutDownPolltask() {
/*  353 */     this.pollTask.shutDown();
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendOfflineCreatures(Communicator c, boolean showOwner) {
/*  358 */     for (Creature cret : this.offlineCreatures.values()) {
/*      */       
/*  360 */       String dominatorName = " dominator=" + cret.dominator;
/*  361 */       if (showOwner) {
/*      */         
/*      */         try {
/*  364 */           PlayerInfo p = PlayerInfoFactory.getPlayerInfoWithWurmId(cret.dominator);
/*  365 */           if (p != null) {
/*  366 */             dominatorName = " dominator=" + p.getName();
/*      */           }
/*  368 */         } catch (Exception exception) {}
/*      */       
/*      */       }
/*      */       else {
/*      */         
/*  373 */         dominatorName = "";
/*  374 */       }  c.sendNormalServerMessage(cret.getName() + " at " + (cret.getPosX() / 4.0F) + ", " + (cret.getPosY() / 4.0F) + " loyalty " + cret
/*  375 */           .getLoyalty() + dominatorName);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCreatureDead(Creature dead) {
/*  381 */     long deadid = dead.getWurmId();
/*  382 */     for (Creature creature : this.creatures.values()) {
/*      */       
/*  384 */       if (creature.opponent == dead)
/*      */       {
/*  386 */         creature.setOpponent(null);
/*      */       }
/*  388 */       if (creature.target == deadid)
/*  389 */         creature.setTarget(-10L, true); 
/*  390 */       creature.removeTarget(deadid);
/*      */     } 
/*  392 */     Vehicles.removeDragger(dead);
/*      */   }
/*      */ 
/*      */   
/*      */   public void combatRound() {
/*  397 */     for (Creature lCreature : this.creatures.values())
/*      */     {
/*  399 */       lCreature.getCombatHandler().clearRound();
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
/*      */   private int getNumberOfCreaturesInDatabase() {
/*  411 */     Statement stmt = null;
/*  412 */     ResultSet rs = null;
/*  413 */     int numberOfCreatures = 0;
/*      */     
/*      */     try {
/*  416 */       Connection dbcon = DbConnector.getCreatureDbCon();
/*  417 */       stmt = dbcon.createStatement();
/*  418 */       rs = stmt.executeQuery("SELECT COUNT(*) FROM CREATURES");
/*  419 */       if (rs.next())
/*      */       {
/*  421 */         numberOfCreatures = rs.getInt(1);
/*      */       }
/*      */     }
/*  424 */     catch (SQLException e) {
/*      */       
/*  426 */       logger.log(Level.WARNING, "Failed to count creatures:" + e.getMessage(), e);
/*      */     }
/*      */     finally {
/*      */       
/*  430 */       DbUtilities.closeDatabaseObjects(stmt, rs);
/*      */     } 
/*  432 */     return numberOfCreatures;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private final void loadMoreStuff(Creature toReturn) {
/*      */     try {
/*  439 */       toReturn.getBody().createBodyParts();
/*  440 */       Items.loadAllItemsForNonPlayer(toReturn, toReturn.getStatus().getInventoryId());
/*  441 */       Village v = Villages.getVillageForCreature(toReturn);
/*  442 */       if (v == null && 
/*  443 */         toReturn.isNpcTrader())
/*      */       {
/*  445 */         if (toReturn.getName().startsWith("Trader")) {
/*      */           
/*  447 */           v = Villages.getVillage(toReturn.getTileX(), toReturn.getTileY(), true);
/*  448 */           if (v != null)
/*      */             
/*      */             try {
/*  451 */               logger.log(Level.INFO, "Adding " + toReturn
/*  452 */                   .getName() + " as citizen to " + v.getName());
/*  453 */               v.addCitizen(toReturn, v.getRoleForStatus((byte)3));
/*      */             }
/*  455 */             catch (IOException iox) {
/*      */               
/*  457 */               logger.log(Level.INFO, iox.getMessage());
/*      */             }
/*  459 */             catch (NoSuchRoleException nsx) {
/*      */               
/*  461 */               logger.log(Level.INFO, nsx.getMessage());
/*      */             }  
/*      */         } 
/*      */       }
/*  465 */       toReturn.setCitizenVillage(v);
/*      */       
/*  467 */       toReturn.postLoad();
/*      */       
/*  469 */       if (toReturn.getTemplate().getTemplateId() == 46 || toReturn
/*  470 */         .getTemplate().getTemplateId() == 47) {
/*      */         
/*  472 */         Zones.setHasLoadedChristmas(true);
/*  473 */         if (!WurmCalendar.isChristmas()) {
/*      */           
/*  475 */           permanentlyDelete(toReturn);
/*      */         }
/*  477 */         else if (toReturn.getTemplate().getTemplateId() == 46) {
/*      */           
/*  479 */           if (!Servers.localServer.HOMESERVER && toReturn.getKingdomId() == 2) {
/*  480 */             Zones.santaMolRehan = toReturn;
/*  481 */           } else if (Servers.localServer.HOMESERVER && toReturn.getKingdomId() == 4) {
/*  482 */             Zones.santas.put(Long.valueOf(toReturn.getWurmId()), toReturn);
/*      */           } else {
/*  484 */             Zones.santa = toReturn;
/*      */           } 
/*      */         } else {
/*  487 */           Zones.evilsanta = toReturn;
/*      */         } 
/*      */       } 
/*  490 */     } catch (Exception ex) {
/*      */       
/*  492 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final void initializeCreature(String templateName, ResultSet rs, Creature statusHolder) {
/*  499 */     long id = statusHolder.getWurmId();
/*      */     
/*  501 */     statusHolder.getStatus().setPosition(CreaturePos.getPosition(id));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  507 */       statusHolder.getStatus().setStatusExists(true);
/*  508 */       (statusHolder.getStatus()).template = CreatureTemplateFactory.getInstance().getTemplate(templateName);
/*  509 */       statusHolder.template = (statusHolder.getStatus()).template;
/*  510 */       (statusHolder.getStatus()).bodyId = rs.getLong("BODYID");
/*  511 */       (statusHolder.getStatus()).body = BodyFactory.getBody(statusHolder, (statusHolder.getStatus()).template.getBodyType(), 
/*  512 */           (statusHolder.getStatus()).template.getCentimetersHigh(), 
/*  513 */           (statusHolder.getStatus()).template.getCentimetersLong(), 
/*  514 */           (statusHolder.getStatus()).template.getCentimetersWide());
/*  515 */       (statusHolder.getStatus()).body.setCentimetersLong(rs.getShort("CENTIMETERSLONG"));
/*  516 */       (statusHolder.getStatus()).body.setCentimetersHigh(rs.getShort("CENTIMETERSHIGH"));
/*  517 */       (statusHolder.getStatus()).body.setCentimetersWide(rs.getShort("CENTIMETERSWIDE"));
/*  518 */       (statusHolder.getStatus()).sex = rs.getByte("SEX");
/*  519 */       (statusHolder.getStatus()).modtype = rs.getByte("TYPE");
/*  520 */       String name = rs.getString("NAME");
/*  521 */       statusHolder.setName(name);
/*  522 */       (statusHolder.getStatus()).inventoryId = rs.getLong("INVENTORYID");
/*  523 */       (statusHolder.getStatus()).stamina = rs.getShort("STAMINA") & 0xFFFF;
/*  524 */       (statusHolder.getStatus()).hunger = rs.getShort("HUNGER") & 0xFFFF;
/*  525 */       (statusHolder.getStatus()).thirst = rs.getShort("THIRST") & 0xFFFF;
/*  526 */       (statusHolder.getStatus()).buildingId = rs.getLong("BUILDINGID");
/*  527 */       (statusHolder.getStatus()).kingdom = rs.getByte("KINGDOM");
/*  528 */       (statusHolder.getStatus()).dead = rs.getBoolean("DEAD");
/*  529 */       (statusHolder.getStatus()).stealth = rs.getBoolean("STEALTH");
/*  530 */       (statusHolder.getStatus()).age = rs.getInt("AGE");
/*  531 */       (statusHolder.getStatus()).fat = rs.getByte("FAT");
/*  532 */       (statusHolder.getStatus()).lastPolledAge = rs.getLong("LASTPOLLEDAGE");
/*  533 */       statusHolder.dominator = rs.getLong("DOMINATOR");
/*  534 */       (statusHolder.getStatus()).reborn = rs.getBoolean("REBORN");
/*  535 */       (statusHolder.getStatus()).loyalty = rs.getFloat("LOYALTY");
/*  536 */       (statusHolder.getStatus()).lastPolledLoyalty = rs.getLong("LASTPOLLEDLOYALTY");
/*  537 */       (statusHolder.getStatus()).detectInvisCounter = rs.getShort("DETECTIONSECS");
/*  538 */       (statusHolder.getStatus()).traits = rs.getLong("TRAITS");
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  543 */       if ((statusHolder.getStatus()).traits != 0L)
/*  544 */         statusHolder.getStatus().setTraitBits((statusHolder.getStatus()).traits); 
/*  545 */       (statusHolder.getStatus()).mother = rs.getLong("MOTHER");
/*  546 */       (statusHolder.getStatus()).father = rs.getLong("FATHER");
/*  547 */       (statusHolder.getStatus()).nutrition = rs.getFloat("NUTRITION");
/*  548 */       (statusHolder.getStatus()).disease = rs.getByte("DISEASE");
/*      */       
/*  550 */       if ((statusHolder.getStatus()).buildingId != -10L) {
/*      */         
/*      */         try {
/*      */           
/*  554 */           Structure struct = Structures.getStructure((statusHolder.getStatus()).buildingId);
/*  555 */           if (!struct.isFinalFinished()) {
/*  556 */             statusHolder.setStructure(struct);
/*      */           } else {
/*  558 */             (statusHolder.getStatus()).buildingId = -10L;
/*      */           } 
/*  560 */         } catch (NoSuchStructureException nss) {
/*      */           
/*  562 */           (statusHolder.getStatus()).buildingId = -10L;
/*  563 */           logger.log(Level.INFO, "Could not find structure for " + statusHolder.getName());
/*  564 */           statusHolder.setStructure(null);
/*      */         } 
/*      */       }
/*  567 */       (statusHolder.getStatus()).lastGroomed = rs.getLong("LASTGROOMED");
/*  568 */       (statusHolder.getStatus()).offline = rs.getBoolean("OFFLINE");
/*  569 */       (statusHolder.getStatus()).stayOnline = rs.getBoolean("STAYONLINE");
/*  570 */       String petName = rs.getString("PETNAME");
/*  571 */       statusHolder.setPetName(petName);
/*      */       
/*  573 */       statusHolder.calculateSize();
/*  574 */       statusHolder.vehicle = rs.getLong("VEHICLE");
/*  575 */       statusHolder.seatType = rs.getByte("SEAT_TYPE");
/*  576 */       if (statusHolder.vehicle > 0L)
/*      */       {
/*      */         
/*  579 */         rideCreatures.put(Long.valueOf(id), statusHolder);
/*      */ 
/*      */       
/*      */       }
/*      */     
/*      */     }
/*  585 */     catch (Exception ex) {
/*      */       
/*  587 */       logger.log(Level.WARNING, "Failed to load creature " + id + " " + ex.getMessage(), ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public int loadAllCreatures() throws NoSuchCreatureException {
/*  593 */     Brand.loadAllBrands();
/*  594 */     loading = true;
/*  595 */     Offspring.loadAllOffspring();
/*  596 */     loadAllProtectedCreatures();
/*      */     
/*  598 */     long lNow2 = System.nanoTime();
/*  599 */     logger.info("Loading all skills for creatures");
/*      */     
/*      */     try {
/*  602 */       Skills.loadAllCreatureSkills();
/*      */     
/*      */     }
/*  605 */     catch (Exception ex) {
/*      */       
/*  607 */       logger.log(Level.INFO, "Failed Loading creature skills.", ex);
/*  608 */       System.exit(0);
/*      */     } 
/*  610 */     logger.log(Level.INFO, "Loaded creature skills. That took " + ((float)(System.nanoTime() - lNow2) / 1000000.0F));
/*  611 */     logger.info("Loading Creatures");
/*  612 */     long lNow = System.nanoTime();
/*  613 */     long cpS = 0L;
/*  614 */     long cpOne = 0L;
/*  615 */     long cpTwo = 0L;
/*  616 */     long cpThree = 0L;
/*  617 */     long cpFour = 0L;
/*  618 */     Creature toReturn = null;
/*  619 */     Connection dbcon = null;
/*  620 */     PreparedStatement ps = null;
/*  621 */     ResultSet rs = null;
/*  622 */     Set<Creature> toRemove = new HashSet<>();
/*  623 */     rideCreatures = new ConcurrentHashMap<>();
/*      */     
/*      */     try {
/*  626 */       dbcon = DbConnector.getCreatureDbCon();
/*  627 */       ps = dbcon.prepareStatement("SELECT * FROM CREATURES");
/*  628 */       rs = ps.executeQuery();
/*  629 */       while (rs.next()) {
/*      */         
/*  631 */         cpS = System.nanoTime();
/*      */         
/*      */         try {
/*  634 */           String templateName = rs.getString("TEMPLATENAME");
/*  635 */           if (templateName.equalsIgnoreCase("human") || templateName.equalsIgnoreCase("npc human")) {
/*  636 */             toReturn = new Npc(rs.getLong("WURMID"));
/*      */           } else {
/*  638 */             toReturn = new Creature(rs.getLong("WURMID"));
/*  639 */           }  initializeCreature(templateName, rs, toReturn);
/*  640 */           toReturn.loadTemplate();
/*  641 */           if (toReturn.isFish()) {
/*      */             
/*  643 */             logger.info("Fish removed " + toReturn.getName());
/*  644 */             permanentlyDelete(toReturn);
/*      */           }
/*  646 */           else if ((!toReturn.isUnique() && (toReturn.isOffline() || toReturn.isDominated()) && 
/*  647 */             !toReturn.isStayonline()) || (!Constants.loadNpcs && toReturn.isNpc())) {
/*      */             
/*  649 */             addOfflineCreature(toReturn);
/*  650 */             addCreature(toReturn, true);
/*  651 */             toRemove.add(toReturn);
/*      */           }
/*  653 */           else if (!addCreature(toReturn, false)) {
/*      */             
/*  655 */             permanentlyDelete(toReturn);
/*      */           } 
/*  657 */           cpOne += System.nanoTime() - cpS;
/*  658 */           cpS = System.nanoTime();
/*      */         
/*      */         }
/*  661 */         catch (Exception ex) {
/*      */           
/*  663 */           logger.log(Level.WARNING, "Failed to load creature: " + toReturn + "due to " + ex.getMessage(), ex);
/*      */         } 
/*      */       } 
/*      */       
/*  667 */       for (Creature rider : rideCreatures.values()) {
/*      */         
/*  669 */         long vehicleId = rider.vehicle;
/*  670 */         byte seatType = rider.seatType;
/*  671 */         rider.vehicle = -10L;
/*  672 */         rider.seatType = -1;
/*      */         
/*      */         try {
/*  675 */           Vehicle vehic = null;
/*  676 */           Item vehicle = null;
/*  677 */           Creature creature = null;
/*  678 */           if (WurmId.getType(vehicleId) == 1) {
/*      */             
/*  680 */             creature = Server.getInstance().getCreature(vehicleId);
/*  681 */             vehic = Vehicles.getVehicle(creature);
/*      */           }
/*      */           else {
/*      */             
/*  685 */             vehicle = Items.getItem(vehicleId);
/*  686 */             vehic = Vehicles.getVehicle(vehicle);
/*      */           } 
/*      */           
/*  689 */           if (vehic == null) {
/*      */             continue;
/*      */           }
/*  692 */           if (seatType == -1 || seatType == 2) {
/*      */             
/*  694 */             if (vehic.addDragger(rider)) {
/*      */               
/*  696 */               rider.setHitched(vehic, true);
/*  697 */               Seat driverseat = vehic.getPilotSeat();
/*  698 */               if (driverseat != null) {
/*      */                 
/*  700 */                 float _r = (-vehicle.getRotation() + 180.0F) * 3.1415927F / 180.0F;
/*  701 */                 float _s = (float)Math.sin(_r);
/*  702 */                 float _c = (float)Math.cos(_r);
/*  703 */                 float xo = _s * -driverseat.offx - _c * -driverseat.offy;
/*  704 */                 float yo = _c * -driverseat.offx + _s * -driverseat.offy;
/*  705 */                 float nPosX = rider.getStatus().getPositionX() - xo;
/*  706 */                 float nPosY = rider.getStatus().getPositionY() - yo;
/*  707 */                 float nPosZ = rider.getStatus().getPositionZ() - driverseat.offz;
/*      */                 
/*  709 */                 rider.getStatus().setPositionX(nPosX);
/*  710 */                 rider.getStatus().setPositionY(nPosY);
/*  711 */                 rider.getStatus().setRotation(-vehicle.getRotation() + 180.0F);
/*  712 */                 rider.getMovementScheme().setPosition(rider.getStatus().getPositionX(), rider
/*  713 */                     .getStatus().getPositionY(), nPosZ, rider
/*  714 */                     .getStatus().getRotation(), rider.getLayer());
/*      */               } 
/*      */             }  continue;
/*      */           } 
/*  718 */           if (seatType == 0 || seatType == 1)
/*      */           {
/*      */             
/*  721 */             for (int x = 0; x < vehic.seats.length; x++) {
/*      */               
/*  723 */               if (vehic.seats[x].getType() == seatType && (
/*  724 */                 !vehic.seats[x].isOccupied() || (vehic.seats[x]).occupant == rider.getWurmId())) {
/*      */                 
/*  726 */                 vehic.seats[x].occupy(vehic, rider);
/*  727 */                 if (seatType == 0) {
/*      */                   
/*  729 */                   vehic.pilotId = rider.getWurmId();
/*  730 */                   rider.setVehicleCommander(true);
/*      */                 } 
/*  732 */                 MountAction m = new MountAction(creature, vehicle, vehic, x, (seatType == 0), (vehic.seats[x]).offz);
/*  733 */                 rider.setMountAction(m);
/*  734 */                 rider.setVehicle(vehicleId, true, seatType);
/*      */                 
/*      */                 break;
/*      */               } 
/*      */             } 
/*      */           }
/*  740 */         } catch (NoSuchItemException|NoSuchPlayerException|NoSuchCreatureException nsi) {
/*      */           
/*  742 */           logger.log(Level.INFO, "Item " + vehicleId + " missing for hitched " + rider.getWurmId() + " " + rider.getName());
/*      */         } 
/*      */       } 
/*  745 */       rideCreatures = null;
/*  746 */       long lNow1 = System.nanoTime();
/*  747 */       logger.info("Loading all items for creatures");
/*      */       
/*  749 */       Items.loadAllCreatureItems();
/*  750 */       logger.log(Level.INFO, "Loaded creature items. That took " + ((float)(System.nanoTime() - lNow1) / 1000000.0F) + " ms for " + 
/*      */           
/*  752 */           Items.getNumItems() + " items and " + Items.getNumCoins() + " coins.");
/*  753 */       for (Creature creature : this.creatures.values()) {
/*      */         
/*  755 */         Skills.fillCreatureTempSkills(creature);
/*  756 */         loadMoreStuff(creature);
/*      */       } 
/*  758 */       for (Creature creature : toRemove)
/*      */       {
/*  760 */         loadMoreStuff(creature);
/*  761 */         removeCreature(creature);
/*      */ 
/*      */         
/*  764 */         (creature.getStatus()).offline = true;
/*      */       }
/*      */     
/*  767 */     } catch (SQLException sqx) {
/*      */       
/*  769 */       logger.log(Level.WARNING, "Failed to load creatures:" + sqx.getMessage(), sqx);
/*  770 */       throw new NoSuchCreatureException(sqx);
/*      */     }
/*      */     finally {
/*      */       
/*  774 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*  775 */       DbConnector.returnConnection(dbcon);
/*  776 */       logger.log(Level.INFO, "Loaded " + getNumberOfCreatures() + " creatures. Destroyed " + destroyedCaveCrets + ". That took " + (
/*  777 */           (float)(System.nanoTime() - lNow) / 1000000.0F) + " ms. CheckPoints cp1=" + ((float)cpOne / 1000000.0F) + ", cp2=" + 0.0F + ", cp3=" + 0.0F + ", cp4=" + 0.0F);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  782 */       logger.log(Level.INFO, "Loaded items for creature. CheckPoints cp1=" + (
/*  783 */           (float)Items.getCpOne() / 1000000.0F) + ", cp2=" + (
/*  784 */           (float)Items.getCpTwo() / 1000000.0F) + ", cp3=" + (
/*  785 */           (float)Items.getCpThree() / 1000000.0F) + ", cp4=" + (
/*      */           
/*  787 */           (float)Items.getCpFour() / 1000000.0F));
/*      */     } 
/*      */     
/*  790 */     loading = false;
/*  791 */     Items.clearCreatureLoadMap();
/*  792 */     Skills.clearCreatureLoadMap();
/*  793 */     Offspring.resetOffspringCounters();
/*  794 */     return getNumberOfCreatures();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean creatureWithTemplateExists(int templateId) {
/*  799 */     for (Creature lCreature : this.creatures.values()) {
/*      */       
/*  801 */       if (lCreature.template.getTemplateId() == templateId)
/*  802 */         return true; 
/*      */     } 
/*  804 */     return false;
/*      */   }
/*      */   
/*      */   public Creature getUniqueCreatureWithTemplate(int templateId) {
/*  808 */     List<Creature> foundCreatures = new ArrayList<>();
/*  809 */     for (Creature lCreature : this.creatures.values()) {
/*      */       
/*  811 */       if (lCreature.template.getTemplateId() == templateId) {
/*  812 */         foundCreatures.add(lCreature);
/*      */       }
/*      */     } 
/*  815 */     if (foundCreatures.size() == 0) return null; 
/*  816 */     if (foundCreatures.size() == 1) return foundCreatures.get(0); 
/*  817 */     throw new UnsupportedOperationException("Multiple creatures found");
/*      */   }
/*      */ 
/*      */   
/*      */   public Creature getCreature(long id) throws NoSuchCreatureException {
/*  822 */     Creature toReturn = null;
/*  823 */     Long cid = new Long(id);
/*  824 */     if (this.creatures.containsKey(cid)) {
/*  825 */       toReturn = this.creatures.get(cid);
/*      */     } else {
/*      */       
/*  828 */       throw new NoSuchCreatureException("No such creature for id: " + id);
/*      */     } 
/*  830 */     if (toReturn == null)
/*      */     {
/*      */ 
/*      */ 
/*      */       
/*  835 */       throw new NoSuchCreatureException("No creature with id " + id);
/*      */     }
/*      */     
/*  838 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Creature getCreatureOrNull(long id) {
/*      */     try {
/*  845 */       return getCreature(id);
/*      */     }
/*  847 */     catch (NoSuchCreatureException n) {
/*      */       
/*  849 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void removeTarget(long id) {
/*  855 */     for (Creature cret : this.creatures.values()) {
/*      */       
/*  857 */       if (cret.target == id)
/*  858 */         cret.setTarget(-10L, true); 
/*      */     } 
/*  860 */     for (Creature cret : this.offlineCreatures.values()) {
/*      */       
/*  862 */       if (cret.target == id) {
/*  863 */         cret.setTarget(-10L, true);
/*      */       }
/*      */     } 
/*  866 */     Player[] players = Players.getInstance().getPlayers();
/*  867 */     for (Player lPlayer : players) {
/*      */       
/*  869 */       if (lPlayer.target == id) {
/*  870 */         lPlayer.setTarget(-10L, true);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCreatureOffline(Creature creature) {
/*      */     try {
/*  878 */       Creature[] watchers = creature.getInventory().getWatchers();
/*  879 */       for (Creature lWatcher : watchers) {
/*  880 */         creature.getInventory().removeWatcher(lWatcher, true);
/*      */       }
/*  882 */     } catch (NoSuchCreatureException noSuchCreatureException) {
/*      */ 
/*      */     
/*      */     }
/*  886 */     catch (Exception nsc) {
/*      */       
/*  888 */       logger.log(Level.WARNING, creature.getName() + " " + nsc.getMessage(), nsc);
/*      */     } 
/*      */     
/*      */     try {
/*  892 */       Creature[] watchers = creature.getBody().getBodyItem().getWatchers();
/*  893 */       for (Creature lWatcher : watchers) {
/*  894 */         creature.getBody().getBodyItem().removeWatcher(lWatcher, true);
/*      */       }
/*  896 */     } catch (NoSuchCreatureException noSuchCreatureException) {
/*      */ 
/*      */     
/*      */     }
/*  900 */     catch (Exception nsc) {
/*      */       
/*  902 */       logger.log(Level.WARNING, creature.getName() + " " + nsc.getMessage(), nsc);
/*      */     } 
/*  904 */     creature.clearOrders();
/*  905 */     creature.setLeader(null);
/*  906 */     creature.destroyVisionArea();
/*  907 */     removeTarget(creature.getWurmId());
/*  908 */     removeCreature(creature);
/*  909 */     addOfflineCreature(creature);
/*  910 */     creature.setPathing(false, true);
/*  911 */     creature.setOffline(true);
/*      */ 
/*      */     
/*      */     try {
/*  915 */       creature.getStatus().savePosition(creature.getWurmId(), false, -10, true);
/*      */     }
/*  917 */     catch (IOException iox) {
/*      */       
/*  919 */       logger.log(Level.WARNING, iox.getMessage(), iox);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private final void saveCreatureProtected(long creatureId, long protector) {
/*  926 */     Connection dbcon = null;
/*  927 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  930 */       dbcon = DbConnector.getCreatureDbCon();
/*  931 */       ps = dbcon.prepareStatement("INSERT INTO PROTECTED (WURMID,PLAYERID) VALUES(?,?)");
/*  932 */       ps.setLong(1, creatureId);
/*  933 */       ps.setLong(2, protector);
/*  934 */       ps.executeUpdate();
/*      */     }
/*  936 */     catch (SQLException sqex) {
/*      */       
/*  938 */       logger.log(Level.WARNING, "Failed to insert creature protected " + creatureId, sqex);
/*      */     }
/*      */     finally {
/*      */       
/*  942 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  943 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private final void deleteCreatureProtected(long creatureId) {
/*  950 */     Connection dbcon = null;
/*  951 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  954 */       dbcon = DbConnector.getCreatureDbCon();
/*  955 */       ps = dbcon.prepareStatement("DELETE FROM PROTECTED WHERE WURMID=?");
/*  956 */       ps.setLong(1, creatureId);
/*  957 */       ps.executeUpdate();
/*      */     
/*      */     }
/*  960 */     catch (SQLException sqex) {
/*      */       
/*  962 */       logger.log(Level.WARNING, "Failed to delete creature protected " + creatureId, sqex);
/*      */     }
/*      */     finally {
/*      */       
/*  966 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  967 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private final void loadAllProtectedCreatures() {
/*  973 */     Connection dbcon = null;
/*  974 */     PreparedStatement ps = null;
/*  975 */     ResultSet rs = null;
/*      */     
/*      */     try {
/*  978 */       dbcon = DbConnector.getCreatureDbCon();
/*  979 */       ps = dbcon.prepareStatement("SELECT * FROM PROTECTED");
/*  980 */       rs = ps.executeQuery();
/*  981 */       while (rs.next())
/*      */       {
/*  983 */         this.protectedCreatures.put(Long.valueOf(rs.getLong("WURMID")), Long.valueOf(rs.getLong("PLAYERID")));
/*      */       }
/*      */     }
/*  986 */     catch (SQLException sqex) {
/*      */       
/*  988 */       logger.log(Level.WARNING, "Failed to load creatures protected.", sqex);
/*      */     }
/*      */     finally {
/*      */       
/*  992 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*  993 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getNumberOfCreaturesProtectedBy(long protector) {
/*  999 */     int numsToReturn = 0;
/* 1000 */     for (Long l : this.protectedCreatures.values()) {
/*      */       
/* 1002 */       if (l.longValue() == protector)
/* 1003 */         numsToReturn++; 
/*      */     } 
/* 1005 */     return numsToReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public final int setNoCreaturesProtectedBy(long protector) {
/* 1010 */     int numsToReturn = 0;
/* 1011 */     LinkedList<Long> toRemove = new LinkedList<>();
/* 1012 */     for (Map.Entry<Long, Long> l : this.protectedCreatures.entrySet()) {
/*      */       
/* 1014 */       if (((Long)l.getValue()).longValue() == protector)
/*      */       {
/* 1016 */         toRemove.add(l.getKey());
/*      */       }
/*      */     } 
/* 1019 */     for (Long l : toRemove) {
/*      */       
/* 1021 */       numsToReturn++;
/* 1022 */       deleteCreatureProtected(l.longValue());
/* 1023 */       this.protectedCreatures.remove(l);
/*      */     } 
/* 1025 */     return numsToReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void setCreatureProtected(Creature creature, long protector, boolean setProtected) {
/* 1030 */     if (setProtected) {
/*      */       
/* 1032 */       if (!this.protectedCreatures.containsKey(Long.valueOf(creature.getWurmId())))
/* 1033 */         saveCreatureProtected(creature.getWurmId(), protector); 
/* 1034 */       this.protectedCreatures.put(Long.valueOf(creature.getWurmId()), Long.valueOf(protector));
/*      */ 
/*      */     
/*      */     }
/* 1038 */     else if (this.protectedCreatures.containsKey(Long.valueOf(creature.getWurmId()))) {
/*      */       
/* 1040 */       deleteCreatureProtected(creature.getWurmId());
/* 1041 */       this.protectedCreatures.remove(Long.valueOf(creature.getWurmId()));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final long getCreatureProtectorFor(long wurmId) {
/* 1048 */     if (this.protectedCreatures.containsKey(Long.valueOf(wurmId)))
/* 1049 */       return ((Long)this.protectedCreatures.get(Long.valueOf(wurmId))).longValue(); 
/* 1050 */     return -10L;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Creature[] getProtectedCreaturesFor(long playerId) {
/* 1058 */     Set<Creature> protectedSet = new HashSet<>();
/* 1059 */     for (Map.Entry<Long, Long> entry : this.protectedCreatures.entrySet()) {
/*      */       
/* 1061 */       if (((Long)entry.getValue()).longValue() == playerId) {
/*      */         
/*      */         try {
/*      */           
/* 1065 */           protectedSet.add(getCreature(((Long)entry.getKey()).longValue()));
/*      */         }
/* 1067 */         catch (NoSuchCreatureException e) {
/*      */ 
/*      */           
/* 1070 */           logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*      */         } 
/*      */       }
/*      */     } 
/* 1074 */     return protectedSet.<Creature>toArray(new Creature[protectedSet.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isCreatureProtected(long wurmId) {
/* 1081 */     return this.protectedCreatures.containsKey(Long.valueOf(wurmId));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final long getCreatureProctector(Creature creature) {
/* 1091 */     Long whom = this.protectedCreatures.get(Long.valueOf(creature.getWurmId()));
/* 1092 */     if (whom != null)
/* 1093 */       return whom.longValue(); 
/* 1094 */     return -10L;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void pollOfflineCreatures() {
/* 1102 */     Set<Creature> toReturn = new HashSet<>();
/* 1103 */     Iterator<Creature> creatureIterator = this.offlineCreatures.values().iterator();
/*      */     
/* 1105 */     while (creatureIterator.hasNext()) {
/*      */       
/* 1107 */       Creature offline = creatureIterator.next();
/* 1108 */       if (offline.pollAge()) {
/*      */         
/* 1110 */         if (logger.isLoggable(Level.FINER))
/*      */         {
/* 1112 */           logger.finer(offline.getWurmId() + ", " + offline.getName() + " is dead.");
/*      */         }
/* 1114 */         creatureIterator.remove();
/*      */         
/*      */         continue;
/*      */       } 
/* 1118 */       offline.pollLoyalty();
/* 1119 */       if (offline.dominator == -10L && (!offline.isNpc() || Constants.loadNpcs == true)) {
/* 1120 */         toReturn.add(offline);
/*      */       }
/*      */     } 
/* 1123 */     for (Creature c : toReturn) {
/*      */ 
/*      */       
/*      */       try {
/* 1127 */         logger.log(Level.INFO, "Returning " + c.getName() + " from being offline due to no loyalty.");
/* 1128 */         loadOfflineCreature(c.getWurmId());
/*      */       }
/* 1130 */       catch (NoSuchCreatureException nsc) {
/*      */         
/* 1132 */         logger.log(Level.WARNING, nsc.getMessage());
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public Creature loadOfflineCreature(long creatureId) throws NoSuchCreatureException {
/* 1139 */     Long cid = new Long(creatureId);
/* 1140 */     if (isCreatureOffline(cid)) {
/*      */       
/* 1142 */       Creature creature = this.offlineCreatures.remove(cid);
/* 1143 */       creature.setOffline(false);
/* 1144 */       creature.setLeader(null);
/* 1145 */       creature.setCitizenVillage(Villages.getVillageForCreature(creature));
/* 1146 */       (creature.getStatus()).visible = true;
/*      */       
/*      */       try {
/* 1149 */         creature.createVisionArea();
/*      */       }
/* 1151 */       catch (Exception ex) {
/*      */         
/* 1153 */         logger.log(Level.WARNING, "Problem creating VisionArea for creature with id " + creatureId + "due to " + ex
/* 1154 */             .getMessage(), ex);
/*      */       } 
/* 1156 */       addCreature(creature, false);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1161 */       return creature;
/*      */     } 
/*      */     
/* 1164 */     throw new NoSuchCreatureException("No such creature with id " + creatureId);
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
/*      */   public boolean isCreatureOffline(Long aCreatureId) {
/* 1176 */     return this.offlineCreatures.containsKey(aCreatureId);
/*      */   }
/*      */ 
/*      */   
/*      */   public final long getPetId(long dominatorId) {
/* 1181 */     for (Creature c : this.offlineCreatures.values()) {
/*      */       
/* 1183 */       if (c.dominator == dominatorId)
/* 1184 */         return c.getWurmId(); 
/*      */     } 
/* 1186 */     for (Creature c : this.creatures.values()) {
/*      */       
/* 1188 */       if (c.dominator == dominatorId)
/* 1189 */         return c.getWurmId(); 
/*      */     } 
/* 1191 */     return -10L;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void returnCreaturesForPlayer(long playerId) {
/* 1202 */     Set<Creature> toLoad = new HashSet<>();
/* 1203 */     for (Creature c : this.offlineCreatures.values()) {
/*      */       
/* 1205 */       if (c.dominator == playerId) {
/*      */         
/* 1207 */         toLoad.add(c);
/* 1208 */         c.setLoyalty(0.0F);
/* 1209 */         c.setDominator(-10L);
/*      */       } 
/*      */     } 
/*      */     
/* 1213 */     for (Creature c : toLoad) {
/*      */ 
/*      */       
/*      */       try {
/* 1217 */         logger.log(Level.INFO, "Returning " + c.getName() + " from being offline due to no loyalty.");
/* 1218 */         loadOfflineCreature(c.getWurmId());
/*      */       }
/* 1220 */       catch (NoSuchCreatureException nsc) {
/*      */         
/* 1222 */         logger.log(Level.WARNING, nsc.getMessage());
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final Creature getNpc(String name) {
/* 1229 */     return this.npcs.get(LoginHandler.raiseFirstLetter(name));
/*      */   }
/*      */ 
/*      */   
/*      */   public final Npc[] getNpcs() {
/* 1234 */     return (Npc[])this.npcs.values().toArray((Object[])new Npc[this.npcs.size()]);
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
/*      */   public void removeCreature(Creature creature) {
/* 1258 */     if (creature.isNpc())
/* 1259 */       this.npcs.remove(creature.getName()); 
/* 1260 */     this.creatures.remove(new Long(creature.getWurmId()));
/* 1261 */     this.avatars.remove(new Long(creature.getWurmId()));
/* 1262 */     removeCreatureByType(creature.getTemplate().getTemplateId());
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean addCreature(Creature creature, boolean offline) {
/* 1267 */     return addCreature(creature, offline, true);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void sendToWorld(Creature creature) {
/*      */     try {
/* 1274 */       Zones.getZone(creature.getTileX(), creature.getTileY(), creature.isOnSurface()).addCreature(creature.getWurmId());
/*      */     }
/* 1276 */     catch (NoSuchCreatureException nex) {
/*      */       
/* 1278 */       logger.log(Level.WARNING, "Failed to add creature ID: " + creature.getWurmId() + " due to " + nex.getMessage(), (Throwable)nex);
/*      */     }
/* 1280 */     catch (NoSuchZoneException sex) {
/*      */       
/* 1282 */       logger.log(Level.WARNING, "Failed to add creature ID: " + creature.getWurmId() + " due to " + sex.getMessage(), (Throwable)sex);
/*      */     }
/* 1284 */     catch (NoSuchPlayerException nsp) {
/*      */       
/* 1286 */       logger.log(Level.WARNING, "Failed to add creature ID: " + creature.getWurmId() + " due to " + nsp.getMessage(), (Throwable)nsp);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   final void addCreatureByType(int creatureType) {
/* 1292 */     Integer val = this.creaturesByType.get(Integer.valueOf(creatureType));
/* 1293 */     if (val == null) {
/* 1294 */       this.creaturesByType.put(Integer.valueOf(creatureType), Integer.valueOf(1));
/*      */     } else {
/* 1296 */       this.creaturesByType.put(Integer.valueOf(creatureType), Integer.valueOf(val.intValue() + 1));
/*      */     } 
/*      */   }
/*      */   
/*      */   final void removeCreatureByType(int creatureType) {
/* 1301 */     Integer val = this.creaturesByType.get(Integer.valueOf(creatureType));
/* 1302 */     if (val == null || val.intValue() == 0) {
/* 1303 */       this.creaturesByType.put(Integer.valueOf(creatureType), Integer.valueOf(0));
/*      */     } else {
/* 1305 */       this.creaturesByType.put(Integer.valueOf(creatureType), Integer.valueOf(val.intValue() - 1));
/*      */     } 
/*      */   }
/*      */   
/*      */   public final int getCreatureByType(int creatureType) {
/* 1310 */     Integer val = this.creaturesByType.get(Integer.valueOf(creatureType));
/* 1311 */     if (val == null || val.intValue() == 0) {
/* 1312 */       return 0;
/*      */     }
/* 1314 */     return val.intValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public final Map<Integer, Integer> getCreatureTypeList() {
/* 1319 */     return this.creaturesByType;
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getOpenSpawnSlotsForCreatureType(int creatureType) {
/* 1324 */     int currentCount = getCreatureByType(creatureType);
/*      */ 
/*      */     
/*      */     try {
/* 1328 */       CreatureTemplate ctemplate = CreatureTemplateFactory.getInstance().getTemplate(creatureType);
/*      */       
/* 1330 */       int maxByPercent = (int)(Servers.localServer.maxCreatures * ctemplate.getMaxPercentOfCreatures());
/* 1331 */       int slotsOpenForPercent = Math.max(maxByPercent - currentCount, 0);
/* 1332 */       if (ctemplate.usesMaxPopulation()) {
/*      */         
/* 1334 */         int maxPop = ctemplate.getMaxPopulationOfCreatures();
/* 1335 */         int slotsByPopulation = Math.max(maxPop - currentCount, 0);
/* 1336 */         if (maxPop <= maxByPercent)
/*      */         {
/* 1338 */           return slotsByPopulation;
/*      */         }
/*      */ 
/*      */         
/* 1342 */         return slotsOpenForPercent;
/*      */       } 
/*      */ 
/*      */       
/* 1346 */       return slotsOpenForPercent;
/*      */     }
/* 1348 */     catch (NoSuchCreatureTemplateException e) {
/*      */       
/* 1350 */       logger.log(Level.WARNING, "Unable to find creature template with id: " + creatureType + ".", (Throwable)e);
/* 1351 */       return 0;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   boolean addCreature(Creature creature, boolean offline, boolean sendToWorld) {
/* 1357 */     this.creatures.put(new Long(creature.getWurmId()), creature);
/* 1358 */     if (creature.isNpc())
/* 1359 */       this.npcs.put(LoginHandler.raiseFirstLetter(creature.getName()), creature); 
/* 1360 */     if (creature.isAvatar())
/* 1361 */       this.avatars.put(new Long(creature.getWurmId()), creature); 
/* 1362 */     addCreatureByType(creature.getTemplate().getTemplateId());
/* 1363 */     if (!creature.isDead()) {
/*      */       
/*      */       try {
/*      */         
/* 1367 */         if (!creature.isOnSurface())
/*      */         {
/* 1369 */           if (Tiles.isSolidCave(Tiles.decodeType(Server.caveMesh.getTile(creature.getTileX(), creature.getTileY())))) {
/*      */             
/* 1371 */             creature.setLayer(0, false);
/* 1372 */             logger.log(Level.INFO, "Changed layer to surface for ID: " + creature
/* 1373 */                 .getWurmId() + " - " + creature.getName() + '.');
/*      */           } 
/*      */         }
/*      */         
/* 1377 */         if (!offline) {
/*      */           
/* 1379 */           if (!creature.isFloating())
/*      */           {
/* 1381 */             if (creature.isMonster() || creature.isAggHuman()) {
/* 1382 */               this.numberOfAgg++;
/*      */             } else {
/* 1384 */               this.numberOfNice++;
/*      */             }  } 
/* 1386 */           if ((creature.getStatus()).modtype > 0) {
/* 1387 */             this.numberOfTyped++;
/*      */           }
/* 1389 */           if (creature.isAggWhitie() || creature.isDefendKingdom())
/* 1390 */             this.kingdomCreatures++; 
/* 1391 */           if (creature.isFloating() && !creature.isSpiritGuard())
/*      */           {
/* 1393 */             if (creature.getTemplate().getTemplateId() == 70) {
/* 1394 */               seaMonsters++;
/*      */             } else {
/* 1396 */               seaHunters++;
/*      */             } 
/*      */           }
/*      */           
/* 1400 */           if (sendToWorld) {
/*      */ 
/*      */             
/* 1403 */             int numsOnTile = Zones.getZone(creature.getTileX(), creature.getTileY(), creature.isOnSurface()).addCreature(creature.getWurmId());
/* 1404 */             if (loading && numsOnTile > 2 && !creature.isHorse())
/*      */             {
/* 1406 */               if (!creature.isOnSurface() && !creature.isDominated() && !creature.isUnique() && 
/* 1407 */                 !creature.isSalesman() && !creature.isWagoner() && 
/* 1408 */                 !creature.hasTrait(63) && !creature.isHitched() && (creature.getBody().getAllItems()).length == 0 && 
/* 1409 */                 !creature.isBranded() && !creature.isCaredFor())
/*      */               {
/* 1411 */                 Zones.getZone(creature.getTileX(), creature.getTileY(), creature.isOnSurface())
/* 1412 */                   .deleteCreature(creature, true);
/* 1413 */                 logger.log(Level.INFO, "Destroying " + creature.getName() + ", " + creature.getWurmId() + " at cave " + creature
/* 1414 */                     .getTileX() + ", " + creature
/* 1415 */                     .getTileY() + " - overcrowded.");
/* 1416 */                 destroyedCaveCrets++;
/* 1417 */                 return false;
/*      */               }
/*      */             
/*      */             }
/*      */           } 
/*      */         } 
/* 1423 */       } catch (NoSuchCreatureException nex) {
/*      */         
/* 1425 */         logger.log(Level.WARNING, "Failed to add creature ID: " + creature.getWurmId() + " due to " + nex.getMessage(), (Throwable)nex);
/*      */         
/* 1427 */         this.creatures.remove(new Long(creature.getWurmId()));
/* 1428 */         this.avatars.remove(new Long(creature.getWurmId()));
/* 1429 */         removeCreatureByType(creature.getTemplate().getTemplateId());
/* 1430 */         return false;
/*      */       }
/* 1432 */       catch (NoSuchZoneException sex) {
/*      */         
/* 1434 */         logger.log(Level.WARNING, "Failed to add creature ID: " + creature.getWurmId() + " due to " + sex.getMessage(), (Throwable)sex);
/*      */         
/* 1436 */         this.creatures.remove(new Long(creature.getWurmId()));
/* 1437 */         this.avatars.remove(new Long(creature.getWurmId()));
/* 1438 */         removeCreatureByType(creature.getTemplate().getTemplateId());
/* 1439 */         return false;
/*      */       }
/* 1441 */       catch (NoSuchPlayerException nsp) {
/*      */         
/* 1443 */         logger.log(Level.WARNING, "Failed to add creature ID: " + creature.getWurmId() + " due to " + nsp.getMessage(), (Throwable)nsp);
/*      */         
/* 1445 */         this.creatures.remove(new Long(creature.getWurmId()));
/* 1446 */         this.avatars.remove(new Long(creature.getWurmId()));
/* 1447 */         removeCreatureByType(creature.getTemplate().getTemplateId());
/* 1448 */         return false;
/*      */       } 
/*      */     }
/* 1451 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isLoading() {
/* 1456 */     return loading;
/*      */   }
/*      */ 
/*      */   
/*      */   private void addOfflineCreature(Creature creature) {
/* 1461 */     this.offlineCreatures.put(new Long(creature.getWurmId()), creature);
/*      */ 
/*      */     
/* 1464 */     if (!creature.isDead() && !creature.isOnSurface())
/*      */     {
/* 1466 */       if (Tiles.isSolidCave(Tiles.decodeType(Server.caveMesh.getTile(creature.getTileX(), creature.getTileY())))) {
/*      */         
/* 1468 */         creature.setLayer(0, false);
/* 1469 */         logger.log(Level.INFO, "Changed layer to surface for ID: " + creature.getWurmId() + " - " + creature.getName() + '.');
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
/*      */   public Creature[] getCreatures() {
/* 1481 */     Creature[] toReturn = new Creature[this.creatures.size()];
/* 1482 */     return (Creature[])this.creatures.values().toArray((Object[])toReturn);
/*      */   }
/*      */ 
/*      */   
/*      */   public Creature[] getAvatars() {
/* 1487 */     Creature[] toReturn = new Creature[this.avatars.size()];
/* 1488 */     return (Creature[])this.avatars.values().toArray((Object[])toReturn);
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
/*      */   public void saveCreatures() {
/* 1518 */     Creature[] creatarr = getCreatures();
/* 1519 */     Exception error = null;
/* 1520 */     int numsSaved = 0;
/* 1521 */     for (Creature creature : creatarr) {
/*      */ 
/*      */       
/*      */       try {
/* 1525 */         if (creature.getStatus().save()) {
/* 1526 */           numsSaved++;
/*      */         }
/* 1528 */       } catch (IOException iox) {
/*      */         
/* 1530 */         error = iox;
/*      */       } 
/*      */     } 
/* 1533 */     logger.log(Level.INFO, "Saved " + numsSaved + " creature statuses.");
/* 1534 */     if (error != null) {
/* 1535 */       logger.log(Level.INFO, "An error occurred while saving creatures:" + error.getMessage(), error);
/*      */     }
/*      */   }
/*      */   
/*      */   void permanentlyDelete(Creature creature) {
/* 1540 */     removeCreature(creature);
/* 1541 */     if (!creature.isFloating())
/*      */     {
/* 1543 */       if (creature.isMonster() || creature.isAggHuman()) {
/* 1544 */         this.numberOfAgg--;
/*      */       } else {
/* 1546 */         this.numberOfNice--;
/*      */       }  } 
/* 1548 */     if ((creature.getStatus()).modtype > 0)
/* 1549 */       this.numberOfTyped--; 
/* 1550 */     if (creature.isAggWhitie() || creature.isDefendKingdom())
/* 1551 */       this.kingdomCreatures--; 
/* 1552 */     if (creature.isFloating())
/*      */     {
/* 1554 */       if (creature.getTemplate().getTemplateId() == 70) {
/* 1555 */         seaMonsters--;
/*      */       } else {
/* 1557 */         seaHunters--;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1563 */     Brand brand = getBrand(creature.getWurmId());
/* 1564 */     if (brand != null) {
/*      */       
/* 1566 */       brand.deleteBrand();
/* 1567 */       setBrand(creature.getWurmId(), 0L);
/*      */     } 
/*      */     
/* 1570 */     setCreatureProtected(creature, -10L, false);
/* 1571 */     CreaturePos.delete(creature.getWurmId());
/*      */ 
/*      */     
/* 1574 */     MissionTargets.destroyMissionTarget(creature.getWurmId(), true);
/* 1575 */     Connection dbcon = null;
/* 1576 */     PreparedStatement ps = null;
/* 1577 */     Connection dbcon2 = null;
/* 1578 */     PreparedStatement ps2 = null;
/*      */     
/*      */     try {
/* 1581 */       dbcon = DbConnector.getCreatureDbCon();
/* 1582 */       if (Constants.useSplitCreaturesTable) {
/*      */         
/* 1584 */         ps = dbcon.prepareStatement("DELETE FROM CREATURES_BASE WHERE WURMID=?");
/* 1585 */         ps.setLong(1, creature.getWurmId());
/* 1586 */         ps.executeUpdate();
/* 1587 */         DbUtilities.closeDatabaseObjects(ps, null);
/*      */         
/* 1589 */         ps = dbcon.prepareStatement("DELETE FROM CREATURES_POS WHERE WURMID=?");
/* 1590 */         ps.setLong(1, creature.getWurmId());
/* 1591 */         ps.executeUpdate();
/* 1592 */         DbUtilities.closeDatabaseObjects(ps, null);
/*      */         
/* 1594 */         ps = dbcon.prepareStatement("DELETE FROM SKILLS WHERE OWNER=?");
/* 1595 */         ps.setLong(1, creature.getWurmId());
/* 1596 */         ps.executeUpdate();
/* 1597 */         DbUtilities.closeDatabaseObjects(ps, null);
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 1602 */         ps = dbcon.prepareStatement("DELETE FROM CREATURES WHERE WURMID=?");
/* 1603 */         ps.setLong(1, creature.getWurmId());
/* 1604 */         ps.executeUpdate();
/* 1605 */         DbUtilities.closeDatabaseObjects(ps, null);
/*      */         
/* 1607 */         ps = dbcon.prepareStatement("DELETE FROM SKILLS WHERE OWNER=?");
/* 1608 */         ps.setLong(1, creature.getWurmId());
/* 1609 */         ps.executeUpdate();
/* 1610 */         DbUtilities.closeDatabaseObjects(ps, null);
/*      */       } 
/* 1612 */       dbcon2 = DbConnector.getItemDbCon();
/*      */       
/* 1614 */       ps2 = dbcon2.prepareStatement("DELETE FROM BODYPARTS WHERE OWNERID=?");
/* 1615 */       ps2.setLong(1, creature.getWurmId());
/* 1616 */       ps2.executeUpdate();
/* 1617 */       DbUtilities.closeDatabaseObjects(ps2, null);
/*      */       
/* 1619 */       ps2 = dbcon2.prepareStatement("DELETE FROM ITEMS WHERE OWNERID=?");
/* 1620 */       ps2.setLong(1, creature.getWurmId());
/* 1621 */       ps2.executeUpdate();
/* 1622 */       DbUtilities.closeDatabaseObjects(ps2, null);
/*      */     
/*      */     }
/* 1625 */     catch (SQLException sqex) {
/*      */       
/* 1627 */       logger.log(Level.WARNING, "Failed to delete creature " + creature, sqex);
/*      */     }
/*      */     finally {
/*      */       
/* 1631 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1632 */       DbUtilities.closeDatabaseObjects(ps2, null);
/* 1633 */       DbConnector.returnConnection(dbcon);
/* 1634 */       DbConnector.returnConnection(dbcon2);
/*      */     } 
/*      */     
/* 1637 */     if (creature.isUnique())
/*      */     {
/* 1639 */       if (creature.getTemplate() != null) {
/*      */         
/* 1641 */         Den d = Dens.getDen(Integer.valueOf(creature.getTemplate().getTemplateId()).intValue());
/* 1642 */         if (d != null)
/*      */         {
/* 1644 */           if (!getInstance().creatureWithTemplateExists(creature.getTemplate().getTemplateId())) {
/* 1645 */             Dens.deleteDen(creature.getTemplate().getTemplateId());
/*      */           }
/*      */         }
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   int resetGuardSkills() {
/* 1656 */     int count = 0;
/* 1657 */     for (Creature cret : this.creatures.values()) {
/*      */       
/* 1659 */       if (cret.isSpiritGuard()) {
/*      */         
/*      */         try {
/*      */           
/* 1663 */           cret.skills.delete();
/* 1664 */           cret.skills.clone(cret.getSkills().getSkills());
/* 1665 */           cret.skills.save();
/* 1666 */           count++;
/*      */         }
/* 1668 */         catch (Exception ex) {
/*      */           
/* 1670 */           logger.log(Level.WARNING, cret.getWurmId() + ":" + ex.getMessage(), ex);
/*      */         } 
/*      */       }
/*      */     } 
/* 1674 */     logger.log(Level.INFO, "Reset " + count + " guards skills.");
/* 1675 */     return count;
/*      */   }
/*      */ 
/*      */   
/*      */   final Creature[] getCreaturesWithName(String name) {
/* 1680 */     name = name.toLowerCase();
/* 1681 */     Set<Creature> toReturn = new HashSet<>();
/* 1682 */     for (Creature cret : this.creatures.values()) {
/*      */       
/* 1684 */       if (!((cret.getName().toLowerCase().indexOf(name) < 0) ? 1 : 0))
/*      */       {
/* 1686 */         toReturn.add(cret);
/*      */       }
/*      */     } 
/* 1689 */     return toReturn.<Creature>toArray(new Creature[toReturn.size()]);
/*      */   }
/*      */ 
/*      */   
/*      */   public Creature[] getHorsesWithName(String aName) {
/* 1694 */     String name = aName.toLowerCase();
/* 1695 */     Set<Creature> toReturn = new HashSet<>();
/* 1696 */     for (Creature cret : this.creatures.values()) {
/*      */       
/* 1698 */       if ((cret.getTemplate()).isHorse && 
/* 1699 */         !((cret.getName().toLowerCase().indexOf(name) < 0) ? 1 : 0))
/*      */       {
/* 1701 */         toReturn.add(cret);
/*      */       }
/*      */     } 
/* 1704 */     return toReturn.<Creature>toArray(new Creature[toReturn.size()]);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean shouldDestroy(Creature c) {
/* 1709 */     int tid = c.getTemplate().getTemplateId();
/* 1710 */     if (nums < 7000)
/*      */     {
/* 1712 */       if (tid == 15 || tid == 54 || tid == 25 || tid == 44 || tid == 52 || tid == 55 || tid == 10 || tid == 42 || tid == 12 || tid == 45 || tid == 48 || tid == 59 || tid == 13 || tid == 21) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1717 */         nums++;
/* 1718 */         return true;
/*      */       } 
/*      */     }
/* 1721 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void destroySwimmers() {
/* 1726 */     Creature[] crets = getInstance().getCreatures();
/* 1727 */     for (Creature lCret : crets) {
/*      */       
/* 1729 */       if (shouldDestroy(lCret)) {
/* 1730 */         lCret.destroy();
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public static void createLightAvengers() {
/* 1736 */     int numsa = 0;
/* 1737 */     while (numsa < 20) {
/*      */       
/* 1739 */       int x = Zones.safeTileX(Server.rand.nextInt(Zones.worldTileSizeX));
/* 1740 */       int y = Zones.safeTileY(Server.rand.nextInt(Zones.worldTileSizeY));
/* 1741 */       int t = Server.surfaceMesh.getTile(x, y);
/* 1742 */       if (Tiles.decodeHeight(t) > 0) {
/*      */         
/* 1744 */         byte deity = 1;
/* 1745 */         if (Tiles.decodeHeightAsFloat(t) > 100.0F)
/*      */         {
/* 1747 */           deity = 2;
/*      */         }
/*      */         
/*      */         try {
/* 1751 */           CreatureTemplate ctemplate = CreatureTemplateFactory.getInstance().getTemplate(68);
/*      */           
/* 1753 */           Creature cret = Creature.doNew(68, (x << 2) + 2.0F, (y << 2) + 2.0F, Server.rand
/* 1754 */               .nextInt(360), 0, "", ctemplate.getSex(), (byte)0);
/*      */           
/* 1756 */           cret.setDeity(Deities.getDeity(deity));
/*      */           
/* 1758 */           numsa++;
/*      */         }
/* 1760 */         catch (Exception ex) {
/*      */           
/* 1762 */           logger.log(Level.WARNING, ex.getMessage(), ex);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void togglePollTaskLog() {
/* 1770 */     setLog(!isLog());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void pollAllCreatures(int num) {
/* 1776 */     if (num == 1 || this.crets == null) {
/*      */       
/* 1778 */       if (this.crets != null) {
/*      */         
/* 1780 */         if (isLog() && this.totalTime > 0L) {
/* 1781 */           logger.log(Level.INFO, "Creatures polled " + this.crets.length + " Took " + this.totalTime);
/*      */         }
/* 1783 */         this.totalTime = 0L;
/*      */       } 
/* 1785 */       this.currentCreature = 0;
/* 1786 */       this.crets = getCreatures();
/* 1787 */       if (this.crets != null)
/*      */       {
/* 1789 */         for (Player creature : Players.getInstance().getPlayers()) {
/*      */ 
/*      */           
/*      */           try {
/* 1793 */             VolaTile t = creature.getCurrentTile();
/* 1794 */             if (creature.poll()) {
/*      */               
/* 1796 */               if (t != null) {
/* 1797 */                 t.deleteCreature((Creature)creature);
/*      */               }
/* 1799 */             } else if (creature.isDoLavaDamage() && 
/* 1800 */               creature.doLavaDamage()) {
/*      */               
/* 1802 */               if (t != null)
/* 1803 */                 t.deleteCreature((Creature)creature); 
/*      */             } 
/* 1805 */             if (creature.isDoAreaDamage())
/*      */             {
/* 1807 */               if (t != null && 
/* 1808 */                 t.doAreaDamage((Creature)creature))
/*      */               {
/* 1810 */                 if (t != null) {
/* 1811 */                   t.deleteCreature((Creature)creature);
/*      */                 }
/*      */               }
/*      */             }
/* 1815 */           } catch (Exception ex) {
/*      */             
/* 1817 */             logger.log(Level.INFO, ex.getMessage(), ex);
/* 1818 */             ex.printStackTrace();
/*      */           } 
/*      */         } 
/*      */       }
/*      */     } 
/* 1823 */     this.startTime = System.currentTimeMillis();
/* 1824 */     long start = System.currentTimeMillis();
/* 1825 */     int rest = 0;
/* 1826 */     if (num == this.numberOfZonesX)
/* 1827 */       rest = this.crets.length % this.numberOfZonesX; 
/* 1828 */     for (int x = this.currentCreature; x < rest + this.crets.length / this.numberOfZonesX * num; x++) {
/*      */       
/* 1830 */       this.currentCreature++;
/*      */       
/*      */       try {
/* 1833 */         VolaTile t = this.crets[x].getCurrentTile();
/* 1834 */         if (this.crets[x].poll()) {
/*      */           
/* 1836 */           if (t != null) {
/* 1837 */             t.deleteCreature(this.crets[x]);
/*      */           }
/* 1839 */         } else if (this.crets[x].isDoLavaDamage() && 
/* 1840 */           this.crets[x].doLavaDamage()) {
/*      */           
/* 1842 */           if (t != null)
/* 1843 */             t.deleteCreature(this.crets[x]); 
/*      */         } 
/* 1845 */         if (this.crets[x].isDoAreaDamage())
/*      */         {
/* 1847 */           if (t != null && 
/* 1848 */             t.doAreaDamage(this.crets[x]))
/*      */           {
/* 1850 */             if (t != null) {
/* 1851 */               t.deleteCreature(this.crets[x]);
/*      */             }
/*      */           }
/*      */         }
/* 1855 */       } catch (Exception ex) {
/*      */         
/* 1857 */         logger.log(Level.INFO, ex.getMessage(), ex);
/* 1858 */         ex.printStackTrace();
/*      */       } 
/*      */     } 
/* 1861 */     this.totalTime += System.currentTimeMillis() - start;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isLog() {
/* 1872 */     return this.logCreaturePolls;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLog(boolean log) {
/* 1883 */     this.logCreaturePolls = log;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final Creature[] getManagedAnimalsFor(Player player, int villageId, boolean includeAll) {
/* 1894 */     Set<Creature> animals = new HashSet<>();
/* 1895 */     if (villageId >= 0 && includeAll)
/*      */     {
/* 1897 */       for (Creature animal : getInstance().getBranded(villageId))
/* 1898 */         animals.add(animal); 
/*      */     }
/* 1900 */     for (Creature animal : (getInstance()).creatures.values()) {
/*      */       
/* 1902 */       long whom = getInstance().getCreatureProctector(animal);
/* 1903 */       if (whom == player.getWurmId()) {
/* 1904 */         animals.add(animal); continue;
/* 1905 */       }  if (animal.canManage((Creature)player) && !animal.isWagoner())
/* 1906 */         animals.add(animal); 
/*      */     } 
/* 1908 */     if (player.getPet() != null)
/* 1909 */       animals.add(player.getPet()); 
/* 1910 */     return animals.<Creature>toArray(new Creature[animals.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final Creature[] getManagedWagonersFor(Player player, int villageId) {
/* 1921 */     Set<Creature> animals = new HashSet<>();
/* 1922 */     if (!Servers.isThisAPvpServer())
/*      */     {
/* 1924 */       for (Map.Entry<Long, Wagoner> entry : Wagoner.getWagoners().entrySet()) {
/*      */         
/* 1926 */         if (((Wagoner)entry.getValue()).getVillageId() == villageId) {
/*      */           
/* 1928 */           Creature creature = ((Wagoner)entry.getValue()).getCreature();
/* 1929 */           if (creature != null) {
/* 1930 */             animals.add(creature);
/*      */           }
/*      */           continue;
/*      */         } 
/* 1934 */         Creature wagoner = ((Wagoner)entry.getValue()).getCreature();
/* 1935 */         if (wagoner != null && wagoner.canManage((Creature)player)) {
/* 1936 */           animals.add(wagoner);
/*      */         }
/*      */       } 
/*      */     }
/* 1940 */     return animals.<Creature>toArray(new Creature[animals.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final Set<Creature> getMayUseWagonersFor(Creature performer) {
/* 1950 */     Set<Creature> wagoners = new HashSet<>();
/* 1951 */     if (!Servers.isThisAPvpServer())
/*      */     {
/* 1953 */       for (Map.Entry<Long, Wagoner> entry : Wagoner.getWagoners().entrySet()) {
/*      */         
/* 1955 */         Wagoner wagoner = entry.getValue();
/* 1956 */         Creature creature = wagoner.getCreature();
/* 1957 */         if (wagoner.getVillageId() != -1 && creature != null && creature.mayUse(performer))
/* 1958 */           wagoners.add(creature); 
/*      */       } 
/*      */     }
/* 1961 */     return wagoners;
/*      */   }
/*      */ 
/*      */   
/*      */   private class PollTimerTask
/*      */     extends TimerTask
/*      */   {
/*      */     private boolean keeprunning = true;
/*      */     private boolean log = false;
/*      */     
/*      */     public final void shutDown() {
/* 1972 */       this.keeprunning = false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void run() {
/* 1979 */       if (this.keeprunning) {
/*      */         
/* 1981 */         int polled = 0;
/* 1982 */         int destroyed = 0;
/* 1983 */         int failedRemove = 0;
/* 1984 */         long start = System.currentTimeMillis();
/* 1985 */         for (Creature creature : (Creatures.getInstance()).creatures.values()) {
/*      */ 
/*      */           
/*      */           try {
/* 1989 */             VolaTile t = creature.getCurrentTile();
/* 1990 */             if (creature.poll()) {
/*      */               
/* 1992 */               destroyed++;
/* 1993 */               if (t != null) {
/* 1994 */                 t.deleteCreature(creature);
/*      */               } else {
/* 1996 */                 failedRemove++;
/*      */               } 
/* 1998 */             } else if (creature.isDoLavaDamage() && 
/* 1999 */               creature.doLavaDamage()) {
/*      */               
/* 2001 */               destroyed++;
/* 2002 */               if (t != null) {
/* 2003 */                 t.deleteCreature(creature);
/*      */               } else {
/* 2005 */                 failedRemove++;
/*      */               } 
/* 2007 */             }  if (creature.isDoAreaDamage());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 2017 */             polled++;
/*      */           }
/* 2019 */           catch (Exception ex) {
/*      */             
/* 2021 */             Creatures.logger.log(Level.INFO, ex.getMessage(), ex);
/*      */           } 
/*      */         } 
/* 2024 */         if (isLog()) {
/* 2025 */           Creatures.logger.log(Level.INFO, "PTT polled " + polled + " Took " + (System.currentTimeMillis() - start) + " destroyed=" + destroyed + " failed remove=" + failedRemove);
/*      */         }
/*      */         
/* 2028 */         for (Player creature : Players.getInstance().getPlayers()) {
/*      */           
/*      */           try
/*      */           {
/* 2032 */             VolaTile t = creature.getCurrentTile();
/* 2033 */             if (creature.poll()) {
/*      */               
/* 2035 */               destroyed++;
/* 2036 */               if (t != null) {
/* 2037 */                 t.deleteCreature((Creature)creature);
/*      */               } else {
/* 2039 */                 failedRemove++;
/*      */               } 
/* 2041 */             } else if (creature.isDoLavaDamage() && 
/* 2042 */               creature.doLavaDamage()) {
/*      */               
/* 2044 */               destroyed++;
/* 2045 */               if (t != null) {
/* 2046 */                 t.deleteCreature((Creature)creature);
/*      */               } else {
/* 2048 */                 failedRemove++;
/*      */               } 
/* 2050 */             }  if (creature.isDoAreaDamage());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 2060 */             polled++;
/*      */           }
/* 2062 */           catch (Exception ex)
/*      */           {
/* 2064 */             Creatures.logger.log(Level.INFO, ex.getMessage(), ex);
/*      */           }
/*      */         
/*      */         } 
/*      */       } else {
/*      */         
/* 2070 */         Creatures.logger.log(Level.INFO, "PollTimerTask shut down.");
/* 2071 */         cancel();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isLog() {
/* 2082 */       return this.log;
/*      */     }
/*      */     
/*      */     private PollTimerTask() {}
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\Creatures.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */