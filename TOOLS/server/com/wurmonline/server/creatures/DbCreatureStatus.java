/*      */ package com.wurmonline.server.creatures;
/*      */ 
/*      */ import com.wurmonline.server.DbConnector;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.WurmCalendar;
/*      */ import com.wurmonline.server.WurmId;
/*      */ import com.wurmonline.server.behaviours.Seat;
/*      */ import com.wurmonline.server.behaviours.Vehicle;
/*      */ import com.wurmonline.server.behaviours.Vehicles;
/*      */ import com.wurmonline.server.bodys.BodyFactory;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.kingdom.Kingdoms;
/*      */ import com.wurmonline.server.structures.NoSuchStructureException;
/*      */ import com.wurmonline.server.structures.Structure;
/*      */ import com.wurmonline.server.structures.Structures;
/*      */ import com.wurmonline.server.utils.DbUtilities;
/*      */ import com.wurmonline.shared.constants.CounterTypes;
/*      */ import java.io.IOException;
/*      */ import java.sql.Connection;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.Statement;
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
/*      */ public final class DbCreatureStatus
/*      */   extends CreatureStatus
/*      */   implements CounterTypes
/*      */ {
/*   50 */   private static final Logger logger = Logger.getLogger(DbCreatureStatus.class.getName());
/*      */   
/*      */   private static final String getPlayerStatus = "select * from PLAYERS where WURMID=?";
/*      */   
/*      */   private static final String getCreatureStatus = "select * from CREATURES where WURMID=?";
/*      */   
/*      */   private static final String savePlayerStatus = "update PLAYERS set TEMPLATENAME=?, SEX=?, CENTIMETERSHIGH=?, CENTIMETERSLONG=?, CENTIMETERSWIDE=?, INVENTORYID=?, BODYID=?, BUILDINGID=?, HUNGER=?, THIRST=?, STAMINA=?,KINGDOM=?,FAT=?,STEALTH=?,DETECTIONSECS=?,TRAITS=?,NUTRITION=?,CALORIES=?,CARBS=?,FATS=?,PROTEINS=? where WURMID=?";
/*      */   
/*      */   private static final String saveCreatureStatus = "update CREATURES set NAME=?, TEMPLATENAME=?,SEX=?, CENTIMETERSHIGH=?, CENTIMETERSLONG=?, CENTIMETERSWIDE=?, INVENTORYID=?, BODYID=?, BUILDINGID=?, HUNGER=?, THIRST=?, STAMINA=?,KINGDOM=?,FAT=?,STEALTH=?,DETECTIONSECS=?,TRAITS=?,NUTRITION=?,PETNAME=?,AGE=? where WURMID=?";
/*      */   
/*      */   private static final String createPlayerStatus = "insert into PLAYERS (TEMPLATENAME, SEX,CENTIMETERSHIGH, CENTIMETERSLONG, CENTIMETERSWIDE, INVENTORYID,BODYID,BUILDINGID,HUNGER, THIRST, STAMINA,KINGDOM,FAT,STEALTH,DETECTIONSECS,TRAITS,NUTRITION,CALORIES,CARBS,FATS,PROTEINS,WURMID ) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
/*      */   
/*      */   private static final String createCreatureStatus = "insert into CREATURES (NAME, TEMPLATENAME, SEX,CENTIMETERSHIGH, CENTIMETERSLONG, CENTIMETERSWIDE, INVENTORYID,BODYID, BUILDINGID,HUNGER,THIRST, STAMINA,KINGDOM,FAT,STEALTH,DETECTIONSECS,TRAITS,NUTRITION,PETNAME, AGE, WURMID) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
/*      */   
/*      */   private static final String SET_PLAYER_KINGDOM = "update PLAYERS set KINGDOM=? WHERE WURMID=?";
/*      */   
/*      */   private static final String SET_PLAYER_INVENTORYID = "update PLAYERS set INVENTORYID=? WHERE WURMID=?";
/*      */   
/*      */   private static final String SET_CREATURE_INVENTORYID = "update CREATURES set INVENTORYID=? WHERE WURMID=?";
/*      */   
/*      */   private static final String SET_CREATURE_KINGDOM = "update CREATURES set KINGDOM=? WHERE WURMID=?";
/*      */   
/*      */   private static final String SET_DEAD_CREATURE = "update CREATURES set DEAD=? WHERE WURMID=?";
/*      */   
/*      */   private static final String SET_DEAD_PLAYER = "update PLAYERS set DEAD=? WHERE WURMID=?";
/*      */   
/*      */   private static final String SET_AGE_CREATURE = "update CREATURES set AGE=?,LASTPOLLEDAGE=? WHERE WURMID=?";
/*      */   private static final String SET_AGE_PLAYER = "update PLAYERS set AGE=?,LASTPOLLEDAGE=? WHERE WURMID=?";
/*      */   private static final String SET_FAT_CREATURE = "update CREATURES set FAT=? WHERE WURMID=?";
/*      */   private static final String SET_FAT_PLAYER = "update PLAYERS set FAT=? WHERE WURMID=?";
/*      */   private static final String SET_DOMINATOR = "update CREATURES set DOMINATOR=? WHERE WURMID=?";
/*      */   private static final String SET_REBORN = "update CREATURES set REBORN=? WHERE WURMID=?";
/*      */   private static final String SET_LOYALTY = "update CREATURES set LOYALTY=? WHERE WURMID=?";
/*      */   private static final String SET_OFFLINE = "update CREATURES set OFFLINE=? WHERE WURMID=?";
/*      */   private static final String SET_STAYONLINE = "update CREATURES set STAYONLINE=? WHERE WURMID=?";
/*      */   private static final String SET_CREATURE_TYPE = "update CREATURES set TYPE=? WHERE WURMID=?";
/*      */   private static final String SET_PLAYER_TYPE = "update PLAYERS set TYPE=? WHERE WURMID=?";
/*      */   private static final String SET_CREATURE_NAME = "update CREATURES set NAME=? WHERE WURMID=?";
/*      */   private static final String SET_CREATURE_INHERITANCE = "update CREATURES set TRAITS=?,MOTHER=?,FATHER=? WHERE WURMID=?";
/*      */   private static final String SET_PLAYER_INHERITANCE = "update PLAYERS set TRAITS=?,MOTHER=?,FATHER=? WHERE WURMID=?";
/*      */   private static final String SET_LASTPOLLEDLOYALTY = "update CREATURES set LASTPOLLEDLOYALTY=? WHERE WURMID=?";
/*      */   private static final String SET_PLDETECTIONSECS = "update PLAYERS set DETECTIONSECS=? WHERE WURMID=?";
/*      */   private static final String SET_LASTGROOMED = "update CREATURES set LASTGROOMED=? WHERE WURMID=?";
/*      */   private static final String SET_CDISEASE = "update CREATURES set DISEASE=? WHERE WURMID=?";
/*      */   private static final String SET_VEHICLE = "update CREATURES set VEHICLE=?,SEAT_TYPE=? WHERE WURMID=?";
/*      */   private static final String SET_PDISEASE = "update PLAYERS set DISEASE=? WHERE WURMID=?";
/*      */   private static final String ISLOADED = "update CREATURES set ISLOADED=? WHERE WURMID=?";
/*      */   private static final String SET_NEWAGE = "update CREATURES set AGE=? WHERE WURMID=?";
/*      */   
/*      */   public DbCreatureStatus(Creature aCreature, float posx, float posy, float aRot, int aLayer) throws Exception {
/*  100 */     super(aCreature, posx, posy, aRot, aLayer);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean save() throws IOException {
/*  106 */     long now = System.nanoTime();
/*      */     
/*  108 */     long id = this.statusHolder.getWurmId();
/*  109 */     this.inventoryId = this.statusHolder.getInventory().getWurmId();
/*  110 */     if (this.bodyId <= 0L) {
/*  111 */       this.bodyId = this.body.getId();
/*      */     }
/*  113 */     Connection dbcon = null;
/*  114 */     boolean toReturn = true;
/*      */     
/*      */     try {
/*  117 */       if (WurmId.getType(this.statusHolder.getWurmId()) == 0)
/*      */       {
/*  119 */         dbcon = DbConnector.getPlayerDbCon();
/*  120 */         toReturn = savePlayerStatus(id, dbcon);
/*      */       }
/*      */       else
/*      */       {
/*  124 */         dbcon = DbConnector.getCreatureDbCon();
/*  125 */         toReturn = saveCreatureStatus(id, dbcon);
/*      */       }
/*      */     
/*  128 */     } catch (SQLException sqex) {
/*      */       
/*  130 */       logger.log(Level.WARNING, "Failed to save status for creature with id " + id, sqex);
/*  131 */       throw new IOException("Failed to save status for creature with id " + id);
/*      */     }
/*      */     finally {
/*      */       
/*  135 */       dbcon = null;
/*  136 */       if (logger.isLoggable(Level.FINER)) {
/*      */         
/*  138 */         float lElapsedTime = (float)(System.nanoTime() - now) / 1000000.0F;
/*  139 */         if (lElapsedTime > 10.0F || (logger.isLoggable(Level.FINEST) && lElapsedTime > 1.0F))
/*      */         {
/*  141 */           logger.finer("Saving Status for " + (
/*  142 */               (WurmId.getType(this.statusHolder.getWurmId()) == 0) ? " player id, " : " Creature id, ") + this.statusHolder
/*  143 */               .getWurmId() + "," + this.statusHolder.getName() + ", which took " + lElapsedTime + " millis.");
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  148 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean saveCreatureStatus(long id, Connection dbcon) throws SQLException {
/*  159 */     if (isChanged()) {
/*      */       PreparedStatement ps;
/*      */ 
/*      */       
/*  163 */       if (isStatusExists()) {
/*  164 */         ps = dbcon.prepareStatement("update CREATURES set NAME=?, TEMPLATENAME=?,SEX=?, CENTIMETERSHIGH=?, CENTIMETERSLONG=?, CENTIMETERSWIDE=?, INVENTORYID=?, BODYID=?, BUILDINGID=?, HUNGER=?, THIRST=?, STAMINA=?,KINGDOM=?,FAT=?,STEALTH=?,DETECTIONSECS=?,TRAITS=?,NUTRITION=?,PETNAME=?,AGE=? where WURMID=?");
/*      */       } else {
/*      */         
/*  167 */         ps = dbcon.prepareStatement("insert into CREATURES (NAME, TEMPLATENAME, SEX,CENTIMETERSHIGH, CENTIMETERSLONG, CENTIMETERSWIDE, INVENTORYID,BODYID, BUILDINGID,HUNGER,THIRST, STAMINA,KINGDOM,FAT,STEALTH,DETECTIONSECS,TRAITS,NUTRITION,PETNAME, AGE, WURMID) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
/*  168 */         setStatusExists(true);
/*      */       } 
/*  170 */       ps.setString(1, this.statusHolder.name);
/*  171 */       ps.setString(2, this.template.getName());
/*  172 */       ps.setByte(3, this.sex);
/*  173 */       if (this.body.getCentimetersHigh() == 0)
/*  174 */         this.body.setCentimetersHigh(this.template.getCentimetersHigh()); 
/*  175 */       ps.setShort(4, this.body.getCentimetersHigh());
/*  176 */       if (this.body.getCentimetersLong() == 0)
/*  177 */         this.body.setCentimetersLong(this.template.getCentimetersLong()); 
/*  178 */       ps.setShort(5, this.body.getCentimetersLong());
/*  179 */       if (this.body.getCentimetersWide() == 0)
/*  180 */         this.body.setCentimetersWide(this.template.getCentimetersWide()); 
/*  181 */       ps.setShort(6, this.body.getCentimetersWide());
/*  182 */       ps.setLong(7, this.inventoryId);
/*  183 */       ps.setLong(8, this.bodyId);
/*  184 */       ps.setLong(9, this.buildingId);
/*  185 */       ps.setShort(10, (short)this.hunger);
/*  186 */       ps.setShort(11, (short)this.thirst);
/*  187 */       ps.setShort(12, (short)this.stamina);
/*  188 */       ps.setByte(13, this.kingdom);
/*  189 */       ps.setInt(14, this.fat);
/*  190 */       ps.setBoolean(15, this.stealth);
/*  191 */       ps.setShort(16, (short)this.detectInvisCounter);
/*  192 */       ps.setLong(17, getTraitBits());
/*  193 */       ps.setFloat(18, this.nutrition);
/*  194 */       ps.setString(19, this.statusHolder.petName);
/*  195 */       ps.setShort(20, (short)this.age);
/*  196 */       ps.setLong(21, id);
/*  197 */       ps.executeUpdate();
/*  198 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  199 */       setChanged(false);
/*  200 */       return true;
/*      */     } 
/*  202 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean savePlayerStatus(long id, Connection dbcon) throws SQLException {
/*  213 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  216 */       if (isStatusExists()) {
/*  217 */         ps = dbcon.prepareStatement("update PLAYERS set TEMPLATENAME=?, SEX=?, CENTIMETERSHIGH=?, CENTIMETERSLONG=?, CENTIMETERSWIDE=?, INVENTORYID=?, BODYID=?, BUILDINGID=?, HUNGER=?, THIRST=?, STAMINA=?,KINGDOM=?,FAT=?,STEALTH=?,DETECTIONSECS=?,TRAITS=?,NUTRITION=?,CALORIES=?,CARBS=?,FATS=?,PROTEINS=? where WURMID=?");
/*      */       } else {
/*      */         
/*  220 */         logger.log(Level.INFO, "Creating new status");
/*  221 */         ps = dbcon.prepareStatement("insert into PLAYERS (TEMPLATENAME, SEX,CENTIMETERSHIGH, CENTIMETERSLONG, CENTIMETERSWIDE, INVENTORYID,BODYID,BUILDINGID,HUNGER, THIRST, STAMINA,KINGDOM,FAT,STEALTH,DETECTIONSECS,TRAITS,NUTRITION,CALORIES,CARBS,FATS,PROTEINS,WURMID ) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
/*  222 */         this.stamina = 65535;
/*      */         
/*  224 */         setStatusExists(true);
/*      */       } 
/*  226 */       ps.setString(1, this.template.getName());
/*  227 */       ps.setByte(2, this.sex);
/*  228 */       ps.setShort(3, this.body.getCentimetersHigh());
/*  229 */       ps.setShort(4, this.body.getCentimetersLong());
/*  230 */       ps.setShort(5, this.body.getCentimetersWide());
/*  231 */       ps.setLong(6, this.inventoryId);
/*  232 */       ps.setLong(7, this.bodyId);
/*  233 */       ps.setLong(8, this.buildingId);
/*  234 */       ps.setShort(9, (short)this.hunger);
/*  235 */       ps.setShort(10, (short)this.thirst);
/*  236 */       ps.setShort(11, (short)this.stamina);
/*  237 */       ps.setByte(12, this.kingdom);
/*  238 */       ps.setByte(13, this.fat);
/*  239 */       ps.setBoolean(14, this.stealth);
/*  240 */       ps.setShort(15, (short)this.detectInvisCounter);
/*  241 */       ps.setLong(16, getTraitBits());
/*  242 */       ps.setFloat(17, this.nutrition);
/*  243 */       ps.setFloat(18, this.calories);
/*  244 */       ps.setFloat(19, this.carbs);
/*  245 */       ps.setFloat(20, this.fats);
/*  246 */       ps.setFloat(21, this.proteins);
/*  247 */       ps.setLong(22, id);
/*  248 */       ps.executeUpdate();
/*      */     }
/*      */     finally {
/*      */       
/*  252 */       DbUtilities.closeDatabaseObjects(ps, null);
/*      */     } 
/*  254 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void load() throws Exception {
/*  265 */     long id = this.statusHolder.getWurmId();
/*  266 */     Connection dbcon = null;
/*  267 */     String loadString = "select * from CREATURES where WURMID=?";
/*  268 */     setPosition(CreaturePos.getPosition(id));
/*  269 */     PreparedStatement ps = null;
/*  270 */     ResultSet rs = null;
/*      */     
/*      */     try {
/*  273 */       if (WurmId.getType(id) == 0) {
/*      */         
/*  275 */         if (logger.isLoggable(Level.FINEST))
/*      */         {
/*  277 */           logger.finest("Loading a player - id:" + id);
/*      */         }
/*  279 */         loadString = "select * from PLAYERS where WURMID=?";
/*  280 */         dbcon = DbConnector.getPlayerDbCon();
/*      */       }
/*      */       else {
/*      */         
/*  284 */         if (logger.isLoggable(Level.FINEST))
/*      */         {
/*  286 */           logger.finest("Loading a creature - id:" + id);
/*      */         }
/*  288 */         dbcon = DbConnector.getCreatureDbCon();
/*      */       } 
/*  290 */       ps = dbcon.prepareStatement(loadString);
/*  291 */       ps.setLong(1, id);
/*  292 */       rs = ps.executeQuery();
/*  293 */       if (rs.next())
/*      */       {
/*      */         
/*  296 */         String templateName = rs.getString("TEMPLATENAME");
/*  297 */         this.template = CreatureTemplateFactory.getInstance().getTemplate(templateName);
/*  298 */         this.statusHolder.template = this.template;
/*  299 */         this.bodyId = rs.getLong("BODYID");
/*  300 */         this.body = BodyFactory.getBody(this.statusHolder, this.template.getBodyType(), this.template.getCentimetersHigh(), this.template
/*  301 */             .getCentimetersLong(), this.template.getCentimetersWide());
/*  302 */         this.body.setCentimetersLong(rs.getShort("CENTIMETERSLONG"));
/*  303 */         this.body.setCentimetersHigh(rs.getShort("CENTIMETERSHIGH"));
/*  304 */         this.body.setCentimetersWide(rs.getShort("CENTIMETERSWIDE"));
/*  305 */         this.sex = rs.getByte("SEX");
/*  306 */         this.modtype = rs.getByte("TYPE");
/*  307 */         String name = rs.getString("NAME");
/*  308 */         this.statusHolder.setName(name);
/*  309 */         this.inventoryId = rs.getLong("INVENTORYID");
/*  310 */         this.stamina = rs.getShort("STAMINA") & 0xFFFF;
/*  311 */         this.hunger = rs.getShort("HUNGER") & 0xFFFF;
/*  312 */         this.thirst = rs.getShort("THIRST") & 0xFFFF;
/*  313 */         this.buildingId = rs.getLong("BUILDINGID");
/*  314 */         this.kingdom = rs.getByte("KINGDOM");
/*  315 */         this.dead = rs.getBoolean("DEAD");
/*  316 */         this.stealth = rs.getBoolean("STEALTH");
/*  317 */         this.age = rs.getInt("AGE");
/*  318 */         this.fat = rs.getByte("FAT");
/*  319 */         this.lastPolledAge = rs.getLong("LASTPOLLEDAGE");
/*  320 */         this.statusHolder.dominator = rs.getLong("DOMINATOR");
/*  321 */         this.reborn = rs.getBoolean("REBORN");
/*  322 */         this.loyalty = rs.getFloat("LOYALTY");
/*  323 */         this.lastPolledLoyalty = rs.getLong("LASTPOLLEDLOYALTY");
/*  324 */         this.detectInvisCounter = rs.getShort("DETECTIONSECS");
/*  325 */         this.traits = rs.getLong("TRAITS");
/*      */         
/*  327 */         if (this.traits != 0L)
/*  328 */           setTraitBits(this.traits); 
/*  329 */         this.mother = rs.getLong("MOTHER");
/*  330 */         this.father = rs.getLong("FATHER");
/*  331 */         this.nutrition = rs.getFloat("NUTRITION");
/*  332 */         this.disease = rs.getByte("DISEASE");
/*      */         
/*  334 */         if (WurmId.getType(id) == 0) {
/*      */           
/*  336 */           this.calories = rs.getFloat("CALORIES");
/*  337 */           this.carbs = rs.getFloat("CARBS");
/*  338 */           this.fats = rs.getFloat("FATS");
/*  339 */           this.proteins = rs.getFloat("PROTEINS");
/*      */         } 
/*  341 */         if (this.buildingId != -10L) {
/*      */           
/*      */           try {
/*      */             
/*  345 */             Structure struct = Structures.getStructure(this.buildingId);
/*  346 */             if (!struct.isFinalFinished()) {
/*  347 */               this.statusHolder.setStructure(struct);
/*      */             } else {
/*  349 */               this.buildingId = -10L;
/*      */             } 
/*  351 */           } catch (NoSuchStructureException nss) {
/*      */             
/*  353 */             this.buildingId = -10L;
/*  354 */             logger.log(Level.INFO, "Could not find structure for " + this.statusHolder.getName());
/*  355 */             this.statusHolder.setStructure(null);
/*      */           } 
/*      */         }
/*  358 */         if (WurmId.getType(id) == 1) {
/*      */           
/*  360 */           this.lastGroomed = rs.getLong("LASTGROOMED");
/*  361 */           this.offline = rs.getBoolean("OFFLINE");
/*  362 */           this.stayOnline = rs.getBoolean("STAYONLINE");
/*      */         } 
/*  364 */         this.statusHolder.calculateSize();
/*  365 */         long hitchedTo = rs.getLong("VEHICLE");
/*  366 */         if (hitchedTo > 0L) {
/*      */           
/*      */           try {
/*      */             
/*  370 */             Item vehicle = Items.getItem(hitchedTo);
/*  371 */             Vehicle vehic = Vehicles.getVehicle(vehicle);
/*  372 */             if (vehic.addDragger(this.statusHolder))
/*      */             {
/*  374 */               this.statusHolder.setHitched(vehic, true);
/*  375 */               Seat driverseat = vehic.getPilotSeat();
/*  376 */               float _r = (-vehicle.getRotation() + 180.0F) * 3.1415927F / 180.0F;
/*  377 */               float _s = (float)Math.sin(_r);
/*  378 */               float _c = (float)Math.cos(_r);
/*  379 */               float xo = _s * -driverseat.offx - _c * -driverseat.offy;
/*  380 */               float yo = _c * -driverseat.offx + _s * -driverseat.offy;
/*  381 */               float nPosX = getPositionX() - xo;
/*  382 */               float nPosY = getPositionY() - yo;
/*  383 */               float nPosZ = getPositionZ() - driverseat.offz;
/*      */               
/*  385 */               setPositionX(nPosX);
/*  386 */               setPositionY(nPosY);
/*  387 */               setRotation(-vehicle.getRotation() + 180.0F);
/*  388 */               this.statusHolder.getMovementScheme().setPosition(getPositionX(), 
/*  389 */                   getPositionY(), nPosZ, 
/*  390 */                   getRotation(), this.statusHolder.getLayer());
/*      */             }
/*      */           
/*  393 */           } catch (NoSuchItemException nsi) {
/*      */             
/*  395 */             logger.log(Level.INFO, "Item " + hitchedTo + " missing for hitched " + id + " " + name);
/*      */           } 
/*      */         }
/*  398 */         setStatusExists(true);
/*      */       }
/*      */     
/*  401 */     } catch (Exception sqex) {
/*      */       
/*  403 */       logger.log(Level.WARNING, "Failed to load status for creature with id " + id, sqex);
/*  404 */       throw new IOException("Failed to load status for creature with id " + id);
/*      */     }
/*      */     finally {
/*      */       
/*  408 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*  409 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static long getInventoryIdFor(long creatureId) {
/*  415 */     Connection dbcon = null;
/*  416 */     PreparedStatement ps = null;
/*  417 */     ResultSet rs = null;
/*  418 */     long toReturn = -10L;
/*      */     
/*      */     try {
/*  421 */       String selectString = "select * from PLAYERS where WURMID=?";
/*  422 */       if (WurmId.getType(creatureId) == 1) {
/*      */         
/*  424 */         selectString = "select * from CREATURES where WURMID=?";
/*  425 */         dbcon = DbConnector.getCreatureDbCon();
/*      */       } else {
/*      */         
/*  428 */         dbcon = DbConnector.getPlayerDbCon();
/*  429 */       }  ps = dbcon.prepareStatement(selectString);
/*  430 */       ps.setLong(1, creatureId);
/*  431 */       rs = ps.executeQuery();
/*  432 */       if (rs.next())
/*      */       {
/*  434 */         toReturn = rs.getLong("INVENTORYID");
/*      */       }
/*      */     }
/*  437 */     catch (SQLException sqx) {
/*      */       
/*  439 */       logger.log(Level.WARNING, "Creature has no inventoryitem?" + creatureId, sqx);
/*      */     }
/*      */     finally {
/*      */       
/*  443 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*  444 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*  446 */     return toReturn;
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
/*      */   public void savePosition(long id, boolean player, int zoneid, boolean immediately) throws IOException {
/*  468 */     if (getPosition() == null) {
/*      */       
/*  470 */       logger.log(Level.WARNING, "Position is null for " + id + " at ", new Exception());
/*      */       return;
/*      */     } 
/*  473 */     long now = System.nanoTime();
/*      */     
/*      */     try {
/*  476 */       if (player)
/*      */       {
/*  478 */         getPosition().savePlayerPosition(zoneid, immediately);
/*      */       }
/*      */       else
/*      */       {
/*  482 */         getPosition().saveCreaturePosition(zoneid, immediately);
/*      */       }
/*      */     
/*  485 */     } catch (SQLException sqex) {
/*      */       
/*  487 */       logger.log(Level.WARNING, "Failed to save status for creature/player with id " + id, sqex);
/*  488 */       if (Server.getMillisToShutDown() == Long.MIN_VALUE)
/*  489 */         Server.getInstance().startShutdown(5, "The server lost connection to the database. Shutting down "); 
/*  490 */       throw new IOException("Failed to save status for creature/player with id " + id, sqex);
/*      */     }
/*      */     finally {
/*      */       
/*  494 */       if (logger.isLoggable(Level.FINER)) {
/*      */         
/*  496 */         float lElapsedTime = (float)(System.nanoTime() - now) / 1000000.0F;
/*  497 */         if (lElapsedTime > 10.0F || (logger.isLoggable(Level.FINEST) && lElapsedTime > 1.0F))
/*      */         {
/*  499 */           logger.finer("Saving Position for " + (player ? " player id, " : " Creature id, ") + this.statusHolder
/*  500 */               .getWurmId() + "," + this.statusHolder.getName() + ", which took " + lElapsedTime + " millis.");
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setKingdom(byte kingd) throws IOException {
/*  510 */     boolean send = (this.kingdom != 0);
/*  511 */     if (this.kingdom != kingd && this.statusHolder.isPlayer())
/*  512 */       Kingdoms.getKingdom(this.kingdom).removeMember(this.statusHolder.getWurmId()); 
/*  513 */     this.kingdom = kingd;
/*  514 */     if (this.statusHolder.isPlayer() && this.statusHolder.getPower() == 0)
/*  515 */       Kingdoms.getKingdom(this.kingdom).addMember(this.statusHolder.getWurmId()); 
/*  516 */     Connection dbcon = null;
/*  517 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  520 */       if (WurmId.getType(this.statusHolder.getWurmId()) == 0) {
/*      */         
/*  522 */         dbcon = DbConnector.getPlayerDbCon();
/*  523 */         ps = dbcon.prepareStatement("update PLAYERS set KINGDOM=? WHERE WURMID=?");
/*      */       }
/*      */       else {
/*      */         
/*  527 */         dbcon = DbConnector.getCreatureDbCon();
/*  528 */         ps = dbcon.prepareStatement("update CREATURES set KINGDOM=? WHERE WURMID=?");
/*      */       } 
/*  530 */       ps.setByte(1, this.kingdom);
/*  531 */       ps.setLong(2, this.statusHolder.getWurmId());
/*  532 */       ps.executeUpdate();
/*  533 */       if (send)
/*      */       {
/*  535 */         this.statusHolder.sendAttitudeChange();
/*  536 */         this.statusHolder.refreshVisible();
/*      */       }
/*      */     
/*  539 */     } catch (SQLException sqex) {
/*      */       
/*  541 */       logger.log(Level.WARNING, this.statusHolder.getWurmId() + " " + sqex.getMessage(), sqex);
/*  542 */       throw new IOException("Failed to set kingdom for " + this.statusHolder.getWurmId() + " to " + Kingdoms.getNameFor(kingd) + ".");
/*      */     
/*      */     }
/*      */     finally {
/*      */       
/*  547 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  548 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*  550 */     if (this.statusHolder.isPlayer()) {
/*  551 */       Players.getInstance().registerNewKingdom(this.statusHolder.getKingdomId(), this.kingdom);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void setInventoryId(long newInventoryId) throws IOException {
/*  557 */     this.inventoryId = newInventoryId;
/*  558 */     Connection dbcon = null;
/*  559 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  562 */       dbcon = DbConnector.getPlayerDbCon();
/*  563 */       if (WurmId.getType(this.statusHolder.getWurmId()) == 0) {
/*      */         
/*  565 */         ps = dbcon.prepareStatement("update PLAYERS set INVENTORYID=? WHERE WURMID=?");
/*      */       }
/*      */       else {
/*      */         
/*  569 */         dbcon = DbConnector.getCreatureDbCon();
/*  570 */         ps = dbcon.prepareStatement("update CREATURES set INVENTORYID=? WHERE WURMID=?");
/*      */       } 
/*  572 */       ps.setLong(1, this.inventoryId);
/*  573 */       ps.setLong(2, this.statusHolder.getWurmId());
/*  574 */       ps.executeUpdate();
/*      */ 
/*      */     
/*      */     }
/*  578 */     catch (SQLException sqex) {
/*      */       
/*  580 */       logger.log(Level.WARNING, this.statusHolder.getWurmId() + " " + sqex.getMessage(), sqex);
/*  581 */       throw new IOException("Failed to set inventory id for " + this.statusHolder.getWurmId() + " to " + this.inventoryId + ".");
/*      */     }
/*      */     finally {
/*      */       
/*  585 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  586 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDead(boolean isdead) throws IOException {
/*  593 */     this.dead = isdead;
/*  594 */     Connection dbcon = null;
/*  595 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  598 */       if (this.statusHolder.isPlayer()) {
/*      */         
/*  600 */         dbcon = DbConnector.getPlayerDbCon();
/*  601 */         ps = dbcon.prepareStatement("update PLAYERS set DEAD=? WHERE WURMID=?");
/*      */       }
/*      */       else {
/*      */         
/*  605 */         dbcon = DbConnector.getCreatureDbCon();
/*  606 */         ps = dbcon.prepareStatement("update CREATURES set DEAD=? WHERE WURMID=?");
/*      */       } 
/*  608 */       ps.setBoolean(1, this.dead);
/*  609 */       ps.setLong(2, this.statusHolder.getWurmId());
/*  610 */       ps.executeUpdate();
/*      */     }
/*  612 */     catch (SQLException sqex) {
/*      */       
/*  614 */       logger.log(Level.WARNING, this.statusHolder.getWurmId() + " " + sqex.getMessage(), sqex);
/*  615 */       throw new IOException("Failed to set dead to " + isdead + " for " + this.statusHolder.getWurmId() + '.');
/*      */     }
/*      */     finally {
/*      */       
/*  619 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  620 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateAge(int newAge) throws IOException {
/*  627 */     this.age = newAge;
/*  628 */     Connection dbcon = null;
/*  629 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  632 */       this.lastPolledAge = WurmCalendar.currentTime;
/*  633 */       if (this.statusHolder.isPlayer()) {
/*      */         
/*  635 */         dbcon = DbConnector.getPlayerDbCon();
/*  636 */         ps = dbcon.prepareStatement("update PLAYERS set AGE=?,LASTPOLLEDAGE=? WHERE WURMID=?");
/*      */       }
/*      */       else {
/*      */         
/*  640 */         dbcon = DbConnector.getCreatureDbCon();
/*  641 */         ps = dbcon.prepareStatement("update CREATURES set AGE=?,LASTPOLLEDAGE=? WHERE WURMID=?");
/*      */       } 
/*  643 */       ps.setShort(1, (short)this.age);
/*  644 */       ps.setLong(2, this.lastPolledAge);
/*  645 */       ps.setLong(3, this.statusHolder.getWurmId());
/*  646 */       ps.executeUpdate();
/*      */     }
/*  648 */     catch (SQLException sqex) {
/*      */       
/*  650 */       logger.log(Level.WARNING, "Problem setting age of creature ID " + this.statusHolder.getWurmId() + " to " + this.age + ", lastPolledAge: " + this.lastPolledAge + " " + sqex
/*  651 */           .getMessage(), sqex);
/*  652 */       throw new IOException("Failed to set age to " + this.age + " for " + this.statusHolder.getWurmId() + '.');
/*      */     }
/*      */     finally {
/*      */       
/*  656 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  657 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateFat() throws IOException {
/*  664 */     Connection dbcon = null;
/*  665 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  668 */       if (this.statusHolder.isPlayer()) {
/*      */         
/*  670 */         dbcon = DbConnector.getPlayerDbCon();
/*  671 */         ps = dbcon.prepareStatement("update PLAYERS set FAT=? WHERE WURMID=?");
/*      */       }
/*      */       else {
/*      */         
/*  675 */         dbcon = DbConnector.getCreatureDbCon();
/*  676 */         ps = dbcon.prepareStatement("update CREATURES set FAT=? WHERE WURMID=?");
/*      */       } 
/*  678 */       ps.setByte(1, this.fat);
/*  679 */       ps.setLong(2, this.statusHolder.getWurmId());
/*  680 */       ps.executeUpdate();
/*      */     }
/*  682 */     catch (SQLException sqex) {
/*      */       
/*  684 */       logger.log(Level.WARNING, "Failed to set fat to " + this.fat + " for " + this.statusHolder
/*  685 */           .getWurmId() + " " + sqex.getMessage(), sqex);
/*  686 */       throw new IOException("Failed to set fat to " + this.fat + " for " + this.statusHolder.getWurmId() + '.');
/*      */     }
/*      */     finally {
/*      */       
/*  690 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  691 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDominator(long dominator) {
/*  698 */     Connection dbcon = null;
/*  699 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  702 */       dbcon = DbConnector.getCreatureDbCon();
/*  703 */       ps = dbcon.prepareStatement("update CREATURES set DOMINATOR=? WHERE WURMID=?");
/*  704 */       ps.setLong(1, dominator);
/*  705 */       ps.setLong(2, this.statusHolder.getWurmId());
/*  706 */       ps.executeUpdate();
/*      */     }
/*  708 */     catch (SQLException sqex) {
/*      */       
/*  710 */       logger.log(Level.WARNING, "Failed to set dominator to " + dominator + " for " + this.statusHolder.getWurmId() + " " + sqex
/*  711 */           .getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/*  715 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  716 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setReborn(boolean reb) {
/*  723 */     this.reborn = reb;
/*      */     
/*  725 */     Connection dbcon = null;
/*  726 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  729 */       dbcon = DbConnector.getCreatureDbCon();
/*  730 */       ps = dbcon.prepareStatement("update CREATURES set REBORN=? WHERE WURMID=?");
/*      */       
/*  732 */       ps.setBoolean(1, this.reborn);
/*  733 */       ps.setLong(2, this.statusHolder.getWurmId());
/*  734 */       ps.executeUpdate();
/*      */     }
/*  736 */     catch (SQLException sqex) {
/*      */       
/*  738 */       logger.log(Level.WARNING, "Failed to set reborn to " + this.reborn + " for " + this.statusHolder
/*  739 */           .getWurmId() + " " + sqex.getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/*  743 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  744 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLoyalty(float _loyalty) {
/*  751 */     _loyalty = Math.min(100.0F, _loyalty);
/*  752 */     _loyalty = Math.max(0.0F, _loyalty);
/*      */     
/*  754 */     if (_loyalty != this.loyalty) {
/*      */       
/*  756 */       this.loyalty = _loyalty;
/*  757 */       Connection dbcon = null;
/*  758 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/*  761 */         dbcon = DbConnector.getCreatureDbCon();
/*  762 */         ps = dbcon.prepareStatement("update CREATURES set LOYALTY=? WHERE WURMID=?");
/*      */         
/*  764 */         ps.setFloat(1, this.loyalty);
/*  765 */         ps.setLong(2, this.statusHolder.getWurmId());
/*  766 */         ps.executeUpdate();
/*      */       }
/*  768 */       catch (SQLException sqex) {
/*      */         
/*  770 */         logger.log(Level.WARNING, "Failed to set loyalty to " + this.loyalty + " for " + this.statusHolder.getWurmId() + " " + sqex
/*  771 */             .getMessage(), sqex);
/*      */       }
/*      */       finally {
/*      */         
/*  775 */         DbUtilities.closeDatabaseObjects(ps, null);
/*  776 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLastPolledLoyalty() {
/*  784 */     this.lastPolledLoyalty = System.currentTimeMillis();
/*  785 */     Connection dbcon = null;
/*  786 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  789 */       dbcon = DbConnector.getCreatureDbCon();
/*  790 */       ps = dbcon.prepareStatement("update CREATURES set LASTPOLLEDLOYALTY=? WHERE WURMID=?");
/*  791 */       ps.setLong(1, this.lastPolledLoyalty);
/*  792 */       ps.setLong(2, this.statusHolder.getWurmId());
/*  793 */       ps.executeUpdate();
/*      */     }
/*  795 */     catch (SQLException sqex) {
/*      */       
/*  797 */       logger.log(Level.WARNING, "Failed to set lastPolledLoyalty to " + this.lastPolledLoyalty + " for " + this.statusHolder
/*      */           
/*  799 */           .getWurmId() + " " + sqex
/*  800 */           .getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/*  804 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  805 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDetectionSecs() {
/*  812 */     if (this.statusHolder.isPlayer()) {
/*      */       
/*  814 */       Connection dbcon = null;
/*  815 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/*  818 */         dbcon = DbConnector.getPlayerDbCon();
/*  819 */         ps = dbcon.prepareStatement("update PLAYERS set DETECTIONSECS=? WHERE WURMID=?");
/*  820 */         ps.setShort(1, (short)this.detectInvisCounter);
/*  821 */         ps.setLong(2, this.statusHolder.getWurmId());
/*  822 */         ps.executeUpdate();
/*      */       }
/*  824 */       catch (SQLException sqex) {
/*      */         
/*  826 */         logger.log(Level.WARNING, "Failed to set detectInvisCounter to " + this.detectInvisCounter + " for " + this.statusHolder
/*  827 */             .getWurmId() + " " + sqex
/*  828 */             .getMessage(), sqex);
/*      */       }
/*      */       finally {
/*      */         
/*  832 */         DbUtilities.closeDatabaseObjects(ps, null);
/*  833 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setOffline(boolean _offline) {
/*  841 */     this.offline = _offline;
/*  842 */     Connection dbcon = null;
/*  843 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  846 */       dbcon = DbConnector.getCreatureDbCon();
/*  847 */       ps = dbcon.prepareStatement("update CREATURES set OFFLINE=? WHERE WURMID=?");
/*  848 */       ps.setBoolean(1, this.offline);
/*  849 */       ps.setLong(2, this.statusHolder.getWurmId());
/*  850 */       ps.executeUpdate();
/*      */     }
/*  852 */     catch (SQLException sqex) {
/*      */       
/*  854 */       logger.log(Level.WARNING, "Failed to set offline to " + this.offline + " for " + this.statusHolder
/*  855 */           .getWurmId() + " " + sqex.getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/*  859 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  860 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean setStayOnline(boolean _stayOnline) {
/*  867 */     this.stayOnline = _stayOnline;
/*  868 */     Connection dbcon = null;
/*  869 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  872 */       dbcon = DbConnector.getCreatureDbCon();
/*  873 */       ps = dbcon.prepareStatement("update CREATURES set STAYONLINE=? WHERE WURMID=?");
/*  874 */       ps.setBoolean(1, this.stayOnline);
/*  875 */       ps.setLong(2, this.statusHolder.getWurmId());
/*  876 */       ps.executeUpdate();
/*      */     }
/*  878 */     catch (SQLException sqex) {
/*      */       
/*  880 */       logger.log(Level.WARNING, "Failed to set stayOnline to " + this.stayOnline + " for " + this.statusHolder.getWurmId() + " " + sqex
/*  881 */           .getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/*  885 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  886 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*  888 */     return this.stayOnline;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setType(byte newtype) {
/*  894 */     this.modtype = newtype;
/*  895 */     if (this.modtype == 11)
/*  896 */       this.disease = 1; 
/*  897 */     Connection dbcon = null;
/*  898 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  901 */       dbcon = DbConnector.getCreatureDbCon();
/*  902 */       if (this.statusHolder.isPlayer()) {
/*      */         
/*  904 */         dbcon = DbConnector.getPlayerDbCon();
/*  905 */         ps = dbcon.prepareStatement("update PLAYERS set TYPE=? WHERE WURMID=?");
/*      */       }
/*      */       else {
/*      */         
/*  909 */         ps = dbcon.prepareStatement("update CREATURES set TYPE=? WHERE WURMID=?");
/*      */       } 
/*      */       
/*  912 */       ps.setByte(1, this.modtype);
/*  913 */       ps.setLong(2, this.statusHolder.getWurmId());
/*  914 */       ps.executeUpdate();
/*      */     }
/*  916 */     catch (SQLException sqex) {
/*      */       
/*  918 */       logger.log(Level.WARNING, "Failed to set type to " + this.modtype + " for " + this.statusHolder
/*  919 */           .getWurmId() + " " + sqex.getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/*  923 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  924 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setInheritance(long _traits, long _mother, long _father) throws IOException {
/*  931 */     this.traits = _traits;
/*  932 */     this.mother = _mother;
/*  933 */     this.father = _father;
/*  934 */     Connection dbcon = null;
/*  935 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  938 */       dbcon = DbConnector.getCreatureDbCon();
/*  939 */       if (this.statusHolder.isPlayer()) {
/*      */         
/*  941 */         dbcon = DbConnector.getPlayerDbCon();
/*  942 */         ps = dbcon.prepareStatement("update PLAYERS set TRAITS=?,MOTHER=?,FATHER=? WHERE WURMID=?");
/*      */       }
/*      */       else {
/*      */         
/*  946 */         ps = dbcon.prepareStatement("update CREATURES set TRAITS=?,MOTHER=?,FATHER=? WHERE WURMID=?");
/*      */       } 
/*      */       
/*  949 */       ps.setLong(1, this.traits);
/*  950 */       ps.setLong(2, this.mother);
/*  951 */       ps.setLong(3, this.father);
/*  952 */       ps.setLong(4, this.statusHolder.getWurmId());
/*  953 */       ps.executeUpdate();
/*      */     }
/*  955 */     catch (SQLException sqex) {
/*      */       
/*  957 */       logger.log(Level.WARNING, "Failed to set type to " + this.modtype + " for " + this.statusHolder
/*  958 */           .getWurmId() + " " + sqex.getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/*  962 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  963 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void saveCreatureName(String name) {
/*  970 */     Connection dbcon = null;
/*  971 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  974 */       dbcon = DbConnector.getCreatureDbCon();
/*  975 */       ps = dbcon.prepareStatement("update CREATURES set NAME=? WHERE WURMID=?");
/*      */       
/*  977 */       ps.setString(1, name);
/*  978 */       ps.setLong(2, this.statusHolder.getWurmId());
/*  979 */       ps.executeUpdate();
/*      */     }
/*  981 */     catch (SQLException sqex) {
/*      */       
/*  983 */       logger.log(Level.WARNING, "Failed to save name for " + this.statusHolder
/*  984 */           .getName() + " to " + name + " ," + this.statusHolder.getWurmId() + " " + sqex
/*  985 */           .getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/*  989 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  990 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLastGroomed(long _lastGroomed) {
/*  997 */     this.lastGroomed = _lastGroomed;
/*  998 */     Connection dbcon = null;
/*  999 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1002 */       dbcon = DbConnector.getCreatureDbCon();
/* 1003 */       ps = dbcon.prepareStatement("update CREATURES set LASTGROOMED=? WHERE WURMID=?");
/*      */       
/* 1005 */       ps.setLong(1, this.lastGroomed);
/* 1006 */       ps.setLong(2, this.statusHolder.getWurmId());
/* 1007 */       ps.executeUpdate();
/*      */     }
/* 1009 */     catch (SQLException sqex) {
/*      */       
/* 1011 */       logger.log(Level.WARNING, "Failed to set lastgroomed for " + this.statusHolder
/*      */           
/* 1013 */           .getName() + " ," + this.statusHolder.getWurmId() + " " + sqex
/* 1014 */           .getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/* 1018 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1019 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setDisease(byte _disease) {
/* 1026 */     this.disease = _disease;
/* 1027 */     Connection dbcon = null;
/* 1028 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1031 */       dbcon = DbConnector.getCreatureDbCon();
/* 1032 */       if (this.statusHolder.isPlayer()) {
/*      */         
/* 1034 */         dbcon = DbConnector.getPlayerDbCon();
/* 1035 */         ps = dbcon.prepareStatement("update PLAYERS set DISEASE=? WHERE WURMID=?");
/*      */       }
/*      */       else {
/*      */         
/* 1039 */         ps = dbcon.prepareStatement("update CREATURES set DISEASE=? WHERE WURMID=?");
/*      */       } 
/*      */       
/* 1042 */       ps.setByte(1, this.disease);
/* 1043 */       ps.setLong(2, this.statusHolder.getWurmId());
/* 1044 */       ps.executeUpdate();
/*      */     }
/* 1046 */     catch (SQLException sqex) {
/*      */       
/* 1048 */       logger.log(Level.WARNING, "Failed to set disease for " + this.statusHolder.getWurmId() + " " + sqex.getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/* 1052 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1053 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setVehicle(long vehicleId, byte seatType) {
/* 1060 */     if (!this.statusHolder.isPlayer()) {
/*      */       
/* 1062 */       Connection dbcon = null;
/* 1063 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 1066 */         dbcon = DbConnector.getCreatureDbCon();
/* 1067 */         ps = dbcon.prepareStatement("update CREATURES set VEHICLE=?,SEAT_TYPE=? WHERE WURMID=?");
/* 1068 */         ps.setLong(1, vehicleId);
/* 1069 */         ps.setByte(2, seatType);
/* 1070 */         ps.setLong(3, this.statusHolder.getWurmId());
/* 1071 */         ps.executeUpdate();
/*      */       }
/* 1073 */       catch (SQLException sqex) {
/*      */         
/* 1075 */         logger.log(Level.WARNING, "Failed to set hitched to for " + this.statusHolder.getWurmId() + " " + sqex.getMessage(), sqex);
/*      */       
/*      */       }
/*      */       finally {
/*      */         
/* 1080 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1081 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setLoaded(int loadstate, long cretID) {
/* 1092 */     Connection dbcon = null;
/* 1093 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1096 */       dbcon = DbConnector.getCreatureDbCon();
/* 1097 */       ps = dbcon.prepareStatement("update CREATURES set ISLOADED=? WHERE WURMID=?");
/* 1098 */       ps.setInt(1, loadstate);
/* 1099 */       ps.setLong(2, cretID);
/* 1100 */       ps.executeUpdate();
/*      */     }
/* 1102 */     catch (SQLException sqex) {
/*      */       
/* 1104 */       logger.log(Level.WARNING, "Failed to set loadstate to for " + cretID + " " + sqex.getMessage(), sqex);
/*      */     
/*      */     }
/*      */     finally {
/*      */       
/* 1109 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1110 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int getIsLoaded(long cretID) {
/* 1120 */     Statement stmt = null;
/* 1121 */     ResultSet rs = null;
/* 1122 */     int isLoaded = 0;
/*      */     
/*      */     try {
/* 1125 */       Connection dbcon = DbConnector.getCreatureDbCon();
/* 1126 */       stmt = dbcon.createStatement();
/* 1127 */       rs = stmt.executeQuery("select * from CREATURES where WURMID=" + cretID + "");
/* 1128 */       if (rs.next())
/*      */       {
/* 1130 */         isLoaded = rs.getInt("ISLOADED");
/*      */       }
/*      */     }
/* 1133 */     catch (SQLException ex) {
/*      */       
/* 1135 */       ex.printStackTrace();
/*      */     }
/*      */     finally {
/*      */       
/* 1139 */       DbUtilities.closeDatabaseObjects(stmt, rs);
/*      */     } 
/* 1141 */     return isLoaded;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\DbCreatureStatus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */