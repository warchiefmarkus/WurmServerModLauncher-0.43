/*      */ package com.wurmonline.server.items;
/*      */ 
/*      */ import com.wurmonline.server.Constants;
/*      */ import com.wurmonline.server.DbConnector;
/*      */ import com.wurmonline.server.Features;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.NoSuchPlayerException;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.WurmCalendar;
/*      */ import com.wurmonline.server.WurmId;
/*      */ import com.wurmonline.server.behaviours.Vehicle;
/*      */ import com.wurmonline.server.behaviours.Vehicles;
/*      */ import com.wurmonline.server.combat.ArmourTemplate;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.Delivery;
/*      */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*      */ import com.wurmonline.server.effects.Effect;
/*      */ import com.wurmonline.server.effects.EffectFactory;
/*      */ import com.wurmonline.server.utils.DbUtilities;
/*      */ import com.wurmonline.server.utils.ItemDamageDatabaseUpdatable;
/*      */ import com.wurmonline.server.utils.ItemDamageDatabaseUpdater;
/*      */ import com.wurmonline.server.utils.ItemLastOwnerDatabaseUpdatable;
/*      */ import com.wurmonline.server.utils.ItemLastOwnerDatabaseUpdater;
/*      */ import com.wurmonline.server.utils.ItemOwnerDatabaseUpdatable;
/*      */ import com.wurmonline.server.utils.ItemOwnerDatabaseUpdater;
/*      */ import com.wurmonline.server.utils.ItemParentDatabaseUpdatable;
/*      */ import com.wurmonline.server.utils.ItemParentDatabaseUpdater;
/*      */ import com.wurmonline.server.utils.WurmDbUpdatable;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import com.wurmonline.shared.constants.ItemMaterials;
/*      */ import java.io.IOException;
/*      */ import java.sql.Connection;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashSet;
/*      */ import java.util.Optional;
/*      */ import java.util.Set;
/*      */ import java.util.StringTokenizer;
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
/*      */ public final class DbItem
/*      */   extends Item
/*      */   implements ItemTypes, MiscConstants, ItemMaterials
/*      */ {
/*   70 */   private static final Logger logger = Logger.getLogger(DbItem.class.getName());
/*      */ 
/*      */ 
/*      */   
/*   74 */   private static PreparedStatement lastmPS = null;
/*      */ 
/*      */ 
/*      */   
/*   78 */   private static int lastmPSCount = 0;
/*      */ 
/*      */ 
/*      */   
/*   82 */   public static int overallLastmPSCount = 0;
/*      */   
/*   84 */   private static PreparedStatement lastDmgPS = null;
/*      */   
/*   86 */   private static int lastDmgPSCount = 0;
/*      */ 
/*      */ 
/*      */   
/*   90 */   public static int overallDmgPSCount = 0;
/*      */ 
/*      */   
/*      */   private DbStrings dbstrings;
/*      */   
/*   95 */   private static final ItemDamageDatabaseUpdater itemDamageDatabaseUpdater = new ItemDamageDatabaseUpdater("Item Database Damage Updater", Constants.numberOfDbItemDamagesToUpdateEachTime);
/*      */ 
/*      */   
/*   98 */   private static final ItemOwnerDatabaseUpdater itemOwnerDatabaseUpdater = new ItemOwnerDatabaseUpdater("Item Database Owner Updater", Constants.numberOfDbItemOwnersToUpdateEachTime);
/*      */ 
/*      */   
/*  101 */   private static final ItemLastOwnerDatabaseUpdater itemLastOwnerDatabaseUpdater = new ItemLastOwnerDatabaseUpdater("Item Database Last Owner Updater", Constants.numberOfDbItemOwnersToUpdateEachTime);
/*      */ 
/*      */   
/*  104 */   private static final ItemParentDatabaseUpdater itemParentDatabaseUpdater = new ItemParentDatabaseUpdater("Item Database Parent Updater", Constants.numberOfDbItemOwnersToUpdateEachTime);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   DbItem(long wurmId, String _name, ItemTemplate _template, float _qualityLevel, byte _material, byte aRarity, long bridgeId, String _creator) throws IOException {
/*  111 */     super(wurmId, _name, _template, _qualityLevel, _material, aRarity, bridgeId, _creator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   DbItem(String _name, ItemTemplate _template, float _qualityLevel, float x, float y, float z, float rot, byte _material, byte aRarity, long bridgeId, String _creator) throws IOException {
/*  118 */     super(_name, _template, _qualityLevel, x, y, z, rot, _material, aRarity, bridgeId, _creator);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public DbItem(long aId) throws Exception {
/*  124 */     this.id = aId;
/*  125 */     load(false);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public DbItem(long aId, boolean frozen) throws Exception {
/*  131 */     this.id = aId;
/*  132 */     load(frozen);
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
/*      */   public DbItem(long wurmId, String aName, short aPlace, ItemTemplate aTemplate, float aQualityLevel, String aCreator) throws IOException {
/*  153 */     this(wurmId, aName, aTemplate, aQualityLevel, (byte)1, (byte)0, -10L, aCreator);
/*  154 */     setPlace(aPlace);
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
/*      */   public DbItem(long wid, ItemTemplate templ, String nam, long last, float ql, float origQl, int sizex, int sizey, int sizez, float posx, float posy, float posz, float rot, long parentid, long ownerid, int zoneid, float dam, int w, byte mat, long lid, short plac, int pric, short temper, String desc, byte blesser, byte enchant, boolean bank, long lastOwnerId, byte auxdata, long created, byte createState, int rTemplate, boolean wornArmour, int _color, int _color2, boolean _female, boolean _mailed, boolean _transferred, String _creator, boolean _hidden, byte mailedTimes, byte rarebyte, long bridgeId, int aSettings, boolean _placedOnParent, DbStrings dbStrings) {
/*      */     try {
/*  170 */       this.id = wid;
/*  171 */       this.template = templ;
/*  172 */       this.dbstrings = dbStrings;
/*  173 */       this.name = nam;
/*  174 */       this.lastMaintained = Math.min(WurmCalendar.currentTime, last);
/*  175 */       this.qualityLevel = ql;
/*  176 */       this.originalQualityLevel = origQl;
/*      */ 
/*      */       
/*  179 */       int[] sizes = { sizex, sizey, sizez };
/*  180 */       Arrays.sort(sizes);
/*  181 */       this.sizeX = sizes[0];
/*  182 */       this.sizeY = sizes[1];
/*  183 */       this.sizeZ = sizes[2];
/*  184 */       this.posX = posx;
/*  185 */       this.posY = posy;
/*  186 */       this.posZ = posz;
/*  187 */       this.rotation = rot;
/*  188 */       this.parentId = parentid;
/*  189 */       this.ownerId = ownerid;
/*  190 */       this.zoneId = zoneid;
/*  191 */       this.damage = dam;
/*      */       
/*  193 */       this.price = pric;
/*  194 */       this.weight = w;
/*  195 */       this.material = mat;
/*  196 */       this.lockid = lid;
/*  197 */       this.place = plac;
/*  198 */       this.temperature = temper;
/*  199 */       this.description = desc;
/*  200 */       this.bless = blesser;
/*  201 */       this.enchantment = enchant;
/*  202 */       this.banked = bank;
/*  203 */       this.lastOwner = lastOwnerId;
/*  204 */       this.auxbyte = auxdata;
/*  205 */       this.creationDate = created;
/*  206 */       this.creationState = createState;
/*  207 */       this.realTemplate = rTemplate;
/*  208 */       this.wornAsArmour = wornArmour;
/*  209 */       this.color = _color;
/*  210 */       this.color2 = _color2;
/*  211 */       this.female = _female;
/*  212 */       this.mailed = _mailed;
/*  213 */       this.mailTimes = mailedTimes;
/*  214 */       this.transferred = _transferred;
/*  215 */       this.creator = _creator;
/*  216 */       this.hidden = _hidden;
/*  217 */       this.rarity = rarebyte;
/*  218 */       this.onBridge = bridgeId;
/*  219 */       setSettings(aSettings);
/*  220 */       this.placedOnParent = _placedOnParent;
/*  221 */       if (templ.hasData())
/*      */       {
/*  223 */         this.data = Items.getItemData(this.id);
/*      */       }
/*  225 */       if (templ.canHaveInscription())
/*      */       {
/*  227 */         this.inscription = Items.getItemInscriptionData(this.id);
/*      */       }
/*  229 */       setOwnerStuff(templ);
/*      */       
/*  231 */       if (templ.isLock()) {
/*  232 */         loadKeys();
/*      */       }
/*  234 */       if (templ.getTemplateId() == 74 && this.temperature > 200) {
/*  235 */         this.temperature = 10000;
/*      */       }
/*  237 */       if (templ.getTemplateId() == 1172)
/*  238 */         setInternalVolumeFromAuxByte(); 
/*  239 */       if (getSpellEffects() != null)
/*      */       {
/*  241 */         if (getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_GLOW) > 1.0F)
/*      */         {
/*  243 */           setLightOverride(true);
/*  244 */           setIsAlwaysLit(true);
/*      */         }
/*      */         else
/*      */         {
/*  248 */           setLightOverride(false);
/*  249 */           setIsAlwaysLit(false);
/*      */         }
/*      */       
/*      */       }
/*  253 */     } catch (Exception ex) {
/*      */       
/*  255 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*      */     } 
/*  257 */     if (this.template.isRecycled && this.banked && this.ownerId == -10L) {
/*  258 */       Itempool.addRecycledItem(this);
/*      */     } else {
/*  260 */       Items.putItem(this);
/*      */     } 
/*  262 */     ItemFactory.createContainerRestrictions(this);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setOwnerStuff(ItemTemplate templ) {
/*  268 */     if (this.ownerId != -10L) {
/*      */       
/*      */       try {
/*      */         
/*  272 */         Creature owner = Server.getInstance().getCreature(this.ownerId);
/*      */ 
/*      */ 
/*      */         
/*  276 */         if (templ.isBodyPart()) {
/*      */           
/*  278 */           if (getAuxData() == 100) {
/*  279 */             owner.addCarriedWeight(getWeightGrams());
/*      */           }
/*      */         } else {
/*  282 */           owner.addCarriedWeight(getWeightGrams());
/*  283 */         }  if (isKey())
/*  284 */           owner.addKey(this, true); 
/*  285 */         if (this.wornAsArmour)
/*      */         {
/*  287 */           ArmourTemplate armour = ArmourTemplate.getArmourTemplate(this.template.templateId);
/*  288 */           if (armour != null)
/*      */           {
/*  290 */             float moveModChange = armour.getMoveModifier();
/*  291 */             if (Features.Feature.METALLIC_ITEMS.isEnabled()) {
/*      */               
/*  293 */               moveModChange *= ArmourTemplate.getMaterialMovementModifier(getMaterial());
/*      */             }
/*  295 */             else if (Servers.localServer.isChallengeOrEpicServer()) {
/*      */               
/*  297 */               if (getMaterial() == 57 || getMaterial() == 67) {
/*  298 */                 moveModChange *= 0.9F;
/*  299 */               } else if (getMaterial() == 56) {
/*  300 */                 moveModChange *= 0.95F;
/*      */               } 
/*      */             } 
/*  303 */             (owner.getMovementScheme()).armourMod.setModifier((owner.getMovementScheme()).armourMod.getModifier() - moveModChange);
/*      */           }
/*      */         
/*      */         }
/*      */       
/*      */       }
/*  309 */       catch (NoSuchCreatureException noSuchCreatureException) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*  315 */       catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void clearBatches() {
/*      */     try {
/*  327 */       if (lastmPS != null) {
/*      */         
/*  329 */         int[] x = lastmPS.executeBatch();
/*  330 */         logger.log(Level.INFO, "Saved last maintained batch size " + x.length);
/*  331 */         DbUtilities.closeDatabaseObjects(lastmPS, null);
/*  332 */         lastmPS = null;
/*  333 */         lastmPSCount = 0;
/*      */       } 
/*  335 */       if (lastDmgPS != null)
/*      */       {
/*  337 */         int[] x = lastDmgPS.executeBatch();
/*  338 */         logger.log(Level.INFO, "Saved last damage batch size " + x.length);
/*  339 */         DbUtilities.closeDatabaseObjects(lastDmgPS, null);
/*  340 */         lastDmgPS = null;
/*  341 */         lastDmgPSCount = 0;
/*      */       }
/*      */     
/*  344 */     } catch (Exception iox) {
/*      */       
/*  346 */       logger.log(Level.WARNING, iox.getMessage(), iox);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean exists(Connection dbcon) {
/*  352 */     PreparedStatement ps = null;
/*  353 */     ResultSet rs = null;
/*      */     
/*      */     try {
/*  356 */       ps = dbcon.prepareStatement(this.dbstrings.loadItem());
/*  357 */       ps.setLong(1, this.id);
/*  358 */       rs = ps.executeQuery();
/*  359 */       return rs.next();
/*      */     }
/*  361 */     catch (SQLException ex) {
/*      */       
/*  363 */       logger.log(Level.WARNING, "Failed to check if item exists.", ex);
/*      */     }
/*      */     finally {
/*      */       
/*  367 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*  368 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*  370 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void create(float aQualityLevel, long aCreationDate) throws IOException {
/*  376 */     Connection dbcon = null;
/*  377 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  380 */       this.dbstrings = getDbStrings(this.template.getTemplateId());
/*  381 */       dbcon = DbConnector.getItemDbCon();
/*      */       
/*  383 */       ps = dbcon.prepareStatement(this.dbstrings.createItem());
/*  384 */       ps.setLong(1, this.id);
/*  385 */       ps.setInt(2, this.template.getTemplateId());
/*  386 */       ps.setString(3, this.name);
/*  387 */       ps.setFloat(4, aQualityLevel);
/*  388 */       ps.setFloat(5, this.originalQualityLevel);
/*  389 */       this.lastMaintained = aCreationDate;
/*  390 */       this.creationDate = aCreationDate;
/*  391 */       ps.setLong(6, aCreationDate);
/*  392 */       ps.setLong(7, -10L);
/*  393 */       this.sizeX = this.template.getSizeX();
/*  394 */       this.sizeY = this.template.getSizeY();
/*  395 */       this.sizeZ = this.template.getSizeZ();
/*  396 */       ps.setInt(8, this.sizeX);
/*  397 */       ps.setInt(9, this.sizeY);
/*  398 */       ps.setInt(10, this.sizeZ);
/*  399 */       ps.setInt(11, -10);
/*  400 */       ps.setFloat(12, 0.0F);
/*  401 */       ps.setFloat(13, 1.0F);
/*  402 */       ps.setLong(14, this.parentId);
/*  403 */       ps.setInt(15, this.template.getWeightGrams());
/*  404 */       ps.setByte(16, this.material);
/*  405 */       ps.setLong(17, this.lockid);
/*  406 */       ps.setString(18, this.description);
/*  407 */       ps.setLong(19, aCreationDate);
/*  408 */       ps.setByte(20, this.rarity);
/*  409 */       ps.setString(21, this.creator);
/*  410 */       ps.setLong(22, this.onBridge);
/*  411 */       ps.setInt(23, getSettings().getPermissions());
/*  412 */       ps.executeUpdate();
/*  413 */       if (isLock()) {
/*  414 */         createLock();
/*      */       }
/*  416 */     } catch (SQLException sqex) {
/*      */       
/*  418 */       logger.log(Level.WARNING, "Failed to create/update item with id " + this.id, sqex);
/*      */     }
/*      */     finally {
/*      */       
/*  422 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  423 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void load() throws Exception {
/*  430 */     load(false);
/*      */   }
/*      */ 
/*      */   
/*      */   public void load(boolean frozen) throws Exception {
/*  435 */     Connection dbcon = null;
/*  436 */     PreparedStatement ps = null;
/*  437 */     ResultSet rs = null;
/*      */     
/*      */     try {
/*  440 */       if (frozen) {
/*  441 */         this.dbstrings = FrozenItemDbStrings.getInstance();
/*      */       } else {
/*  443 */         this.dbstrings = getDbStringsByWurmId(this.id);
/*  444 */       }  dbcon = DbConnector.getItemDbCon();
/*  445 */       ps = dbcon.prepareStatement(this.dbstrings.loadItem());
/*  446 */       ps.setLong(1, this.id);
/*  447 */       rs = ps.executeQuery();
/*  448 */       if (rs.next()) {
/*      */         
/*  450 */         this.template = ItemTemplateFactory.getInstance().getTemplate(rs.getInt("TEMPLATEID"));
/*  451 */         this.lastMaintained = rs.getLong("LASTMAINTAINED");
/*  452 */         this.qualityLevel = rs.getFloat("QUALITYLEVEL");
/*  453 */         this.originalQualityLevel = rs.getFloat("ORIGINALQUALITYLEVEL");
/*  454 */         this.sizeX = rs.getInt("SIZEX");
/*  455 */         this.sizeY = rs.getInt("SIZEY");
/*  456 */         this.sizeZ = rs.getInt("SIZEZ");
/*  457 */         this.posX = rs.getFloat("POSX");
/*  458 */         this.posY = rs.getFloat("POSY");
/*  459 */         this.posZ = rs.getFloat("POSZ");
/*  460 */         this.rotation = rs.getFloat("ROTATION");
/*  461 */         this.parentId = rs.getLong("PARENTID");
/*  462 */         this.ownerId = rs.getLong("OWNERID");
/*  463 */         this.lastOwner = rs.getLong("LASTOWNERID");
/*  464 */         this.zoneId = rs.getInt("ZONEID");
/*  465 */         this.name = rs.getString("NAME");
/*  466 */         this.damage = rs.getFloat("DAMAGE");
/*  467 */         this.weight = rs.getInt("WEIGHT");
/*  468 */         this.material = rs.getByte("MATERIAL");
/*  469 */         this.lockid = rs.getLong("LOCKID");
/*  470 */         this.place = rs.getShort("PLACE");
/*  471 */         this.price = rs.getInt("PRICE");
/*  472 */         this.temperature = rs.getShort("TEMPERATURE");
/*  473 */         this.description = rs.getString("DESCRIPTION");
/*  474 */         this.bless = rs.getByte("BLESS");
/*  475 */         this.enchantment = rs.getByte("ENCHANT");
/*  476 */         this.banked = rs.getBoolean("BANKED");
/*  477 */         this.auxbyte = rs.getByte("AUXDATA");
/*  478 */         this.color = rs.getInt("COLOR");
/*  479 */         this.color2 = rs.getInt("COLOR2");
/*  480 */         this.female = rs.getBoolean("FEMALE");
/*  481 */         this.mailed = rs.getBoolean("MAILED");
/*  482 */         this.hidden = rs.getBoolean("HIDDEN");
/*  483 */         this.realTemplate = rs.getInt("REALTEMPLATE");
/*  484 */         this.creationState = rs.getByte("CREATIONSTATE");
/*  485 */         this.creationDate = rs.getLong("CREATIONDATE");
/*  486 */         this.wornAsArmour = rs.getBoolean("WORNARMOUR");
/*  487 */         this.transferred = rs.getBoolean("TRANSFERRED");
/*  488 */         this.creator = rs.getString("CREATOR");
/*  489 */         this.mailTimes = rs.getByte("MAILTIMES");
/*  490 */         this.onBridge = rs.getLong("ONBRIDGE");
/*  491 */         this.rarity = rs.getByte("RARITY");
/*  492 */         setSettings(rs.getInt("SETTINGS"));
/*      */         
/*  494 */         DbUtilities.closeDatabaseObjects(ps, rs);
/*  495 */         logger.log(Level.WARNING, this.name + " this load should not happen anymore. " + this.id + ".", new Exception());
/*      */       }
/*      */       else {
/*      */         
/*  499 */         DbUtilities.closeDatabaseObjects(ps, rs);
/*  500 */         throw new NoSuchItemException("No item with id " + this.id);
/*      */       } 
/*  502 */       if (hasData()) {
/*  503 */         this.data = Items.getItemData(this.id);
/*      */       }
/*  505 */       if (canHaveInscription()) {
/*  506 */         this.inscription = Items.getItemInscriptionData(this.id);
/*      */       }
/*  508 */       if (this.ownerId != -10L) {
/*      */         
/*      */         try {
/*      */           
/*  512 */           Creature owner = Server.getInstance().getCreature(this.ownerId);
/*      */           
/*  514 */           if (isBodyPart()) {
/*      */             
/*  516 */             if (getAuxData() == 100) {
/*  517 */               owner.addCarriedWeight(getWeightGrams());
/*      */             }
/*      */           } else {
/*  520 */             owner.addCarriedWeight(getWeightGrams());
/*      */           } 
/*  522 */           if (isKey()) {
/*  523 */             owner.addKey(this, true);
/*      */           }
/*      */         }
/*  526 */         catch (NoSuchCreatureException noSuchCreatureException) {
/*      */ 
/*      */ 
/*      */         
/*      */         }
/*  531 */         catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  537 */       if (isLock()) {
/*  538 */         loadKeys();
/*      */       }
/*  540 */       if (this.template.getTemplateId() == 74 && this.temperature > 200)
/*  541 */         this.temperature = 10000; 
/*  542 */       if (isHollow()) {
/*  543 */         this.items = getItems();
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*      */ 
/*      */     
/*      */     }
/*  551 */     catch (SQLException ex) {
/*      */       
/*  553 */       logger.log(Level.WARNING, "Failed to load item with id " + this.id, ex);
/*      */     
/*      */     }
/*      */     finally {
/*      */       
/*  558 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*  559 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void loadEffects() {
/*  566 */     EffectFactory.getInstance().getEffectsFor(this);
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
/*      */   public void loadKeys() {
/*  584 */     Connection dbcon = null;
/*  585 */     PreparedStatement ps = null;
/*  586 */     ResultSet rs = null;
/*      */     
/*      */     try {
/*  589 */       dbcon = DbConnector.getItemDbCon();
/*  590 */       ps = dbcon.prepareStatement(this.dbstrings.getLock());
/*  591 */       ps.setLong(1, this.id);
/*  592 */       rs = ps.executeQuery();
/*  593 */       if (rs.next()) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  600 */         this.locked = rs.getBoolean("LOCKED");
/*      */       }
/*      */       else {
/*      */         
/*  604 */         createLock();
/*      */       } 
/*  606 */       getKeys();
/*      */     
/*      */     }
/*  609 */     catch (SQLException ex) {
/*      */       
/*  611 */       logger.log(Level.WARNING, "Failed to load keys for lock with id " + this.id, ex);
/*      */     
/*      */     }
/*      */     finally {
/*      */       
/*  616 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*  617 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void createLock() {
/*  623 */     Connection dbcon = null;
/*  624 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  627 */       dbcon = DbConnector.getItemDbCon();
/*  628 */       ps = dbcon.prepareStatement(this.dbstrings.createLock());
/*      */       
/*  630 */       ps.setLong(1, this.id);
/*  631 */       ps.setBoolean(2, this.locked);
/*  632 */       ps.executeUpdate();
/*      */     }
/*  634 */     catch (SQLException ex) {
/*      */       
/*  636 */       logger.log(Level.WARNING, "Failed to save keys for lock with id " + this.id, ex);
/*      */     
/*      */     }
/*      */     finally {
/*      */       
/*  641 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  642 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void addNewKey(long keyId) {
/*  649 */     Connection dbcon = null;
/*  650 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  653 */       dbcon = DbConnector.getItemDbCon();
/*  654 */       ps = dbcon.prepareStatement(this.dbstrings.addKey());
/*  655 */       ps.setLong(1, this.id);
/*  656 */       ps.setLong(2, keyId);
/*  657 */       ps.executeUpdate();
/*      */     }
/*  659 */     catch (SQLException ex) {
/*      */       
/*  661 */       logger.log(Level.WARNING, "Failed to add key for lock with id " + this.id, ex);
/*      */     }
/*      */     finally {
/*      */       
/*  665 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  666 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void getKeys() {
/*  672 */     Connection dbcon = null;
/*  673 */     PreparedStatement ps = null;
/*  674 */     ResultSet rs = null;
/*      */     
/*      */     try {
/*  677 */       this.keys = new HashSet<>();
/*  678 */       dbcon = DbConnector.getItemDbCon();
/*  679 */       ps = dbcon.prepareStatement(this.dbstrings.getKeys());
/*  680 */       ps.setLong(1, this.id);
/*  681 */       rs = ps.executeQuery();
/*  682 */       while (rs.next())
/*      */       {
/*      */         
/*  685 */         this.keys.add(new Long(rs.getLong("KEYID")));
/*      */       
/*      */       }
/*      */     }
/*  689 */     catch (SQLException ex) {
/*      */       
/*  691 */       logger.log(Level.WARNING, "Failed to load keys for lock with id " + this.id, ex);
/*      */     
/*      */     }
/*      */     finally {
/*      */       
/*  696 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  697 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void removeNewKey(long keyId) {
/*  704 */     Connection dbcon = null;
/*  705 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  708 */       dbcon = DbConnector.getItemDbCon();
/*  709 */       ps = dbcon.prepareStatement(this.dbstrings.removeKey());
/*  710 */       ps.setLong(1, keyId);
/*  711 */       ps.setLong(2, this.id);
/*  712 */       ps.executeUpdate();
/*      */     }
/*  714 */     catch (SQLException ex) {
/*      */       
/*  716 */       logger.log(Level.WARNING, "Failed to remove key for lock with id " + this.id, ex);
/*      */     }
/*      */     finally {
/*      */       
/*  720 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  721 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean lockExists(Connection dbcon) {
/*  727 */     PreparedStatement ps = null;
/*  728 */     ResultSet rs = null;
/*      */     
/*      */     try {
/*  731 */       ps = dbcon.prepareStatement(this.dbstrings.getLock());
/*  732 */       ps.setLong(1, this.id);
/*  733 */       rs = ps.executeQuery();
/*  734 */       return rs.next();
/*      */     }
/*  736 */     catch (SQLException ex) {
/*      */       
/*  738 */       logger.log(Level.WARNING, "Failed to check if lock exists:", ex);
/*      */     }
/*      */     finally {
/*      */       
/*  742 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*      */     } 
/*  744 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLockId(long lid) {
/*  750 */     if (lid != this.lockid) {
/*      */       
/*  752 */       Connection dbcon = null;
/*  753 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/*  756 */         this.lockid = lid;
/*  757 */         dbcon = DbConnector.getItemDbCon();
/*  758 */         ps = dbcon.prepareStatement(this.dbstrings.setLockId());
/*  759 */         ps.setLong(1, this.lockid);
/*  760 */         ps.setLong(2, this.id);
/*  761 */         ps.executeUpdate();
/*      */       }
/*  763 */       catch (SQLException sqx) {
/*      */         
/*  765 */         logger.log(Level.WARNING, "Failed to save item " + this.id, sqx);
/*      */       }
/*      */       finally {
/*      */         
/*  769 */         DbUtilities.closeDatabaseObjects(ps, null);
/*  770 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public long getLockId() {
/*  778 */     return this.lockid;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setZoneId(int zid, boolean isOnSurface) {
/*  784 */     this.surfaced = isOnSurface;
/*  785 */     if (isHollow())
/*      */     {
/*  787 */       if (this.items != null)
/*  788 */         for (Item item : this.items)
/*      */         {
/*  790 */           item.setSurfaced(this.surfaced);
/*      */         } 
/*      */     }
/*  793 */     if (zid != this.zoneId) {
/*      */       
/*  795 */       Connection dbcon = null;
/*  796 */       PreparedStatement ps = null;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/*  802 */         this.zoneId = zid;
/*  803 */         dbcon = DbConnector.getItemDbCon();
/*  804 */         ps = dbcon.prepareStatement(this.dbstrings.setZoneId());
/*  805 */         ps.setInt(1, this.zoneId);
/*  806 */         ps.setLong(2, this.id);
/*  807 */         ps.executeUpdate();
/*      */       }
/*  809 */       catch (SQLException sqx) {
/*      */         
/*  811 */         logger.log(Level.WARNING, "Failed to set Zone ID to " + this.zoneId + " for item " + this.id, sqx);
/*      */       }
/*      */       finally {
/*      */         
/*  815 */         DbUtilities.closeDatabaseObjects(ps, null);
/*  816 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int getZoneId() {
/*  824 */     if (this.parentId != -10L)
/*      */     {
/*  826 */       if (Items.isItemLoaded(this.parentId)) {
/*      */         
/*      */         try {
/*      */           
/*  830 */           Item parent = Items.getItem(this.parentId);
/*  831 */           return parent.getZoneId();
/*      */         }
/*  833 */         catch (NoSuchItemException nsi) {
/*      */           
/*  835 */           logger.log(Level.WARNING, "This REALLY shouldn't happen! parentId: " + this.parentId, (Throwable)nsi);
/*      */         } 
/*      */       }
/*      */     }
/*  839 */     return this.zoneId;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setParentId(long pid, boolean isOnSurface) {
/*  845 */     this.surfaced = isOnSurface;
/*  846 */     if (this.parentId != pid) {
/*      */       
/*  848 */       if (isCoin() && getValue() >= 1000000) {
/*  849 */         logger.log(Level.INFO, "COINLOG PID " + pid + ", " + getWurmId() + " owner " + this.ownerId + " banked " + this.banked + " mailed=" + this.mailed, new Exception());
/*      */       }
/*  851 */       Connection dbcon = null;
/*  852 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/*  855 */         if (pid == -10L) {
/*      */           
/*  857 */           if (this.watchers != null)
/*      */           {
/*  859 */             for (Creature watcher : this.watchers) {
/*      */               
/*  861 */               watcher.getCommunicator().sendRemoveFromInventory(this);
/*  862 */               watcher.getCommunicator().sendCloseInventoryWindow(getWurmId());
/*      */             } 
/*      */           }
/*  865 */           this.watchers = null;
/*      */         } else {
/*      */ 
/*      */           
/*      */           try {
/*      */             
/*  871 */             Item parent = Items.getItem(pid);
/*  872 */             if (this.ownerId != parent.getOwnerId()) {
/*      */               
/*  874 */               if (parent.getPosX() != getPosX() || parent.getPosY() != getPosY()) {
/*  875 */                 setPosXYZ(parent.getPosX(), parent.getPosY(), parent.getPosZ());
/*      */               }
/*  877 */               for (Item i : getItems()) {
/*      */                 
/*  879 */                 if (i.isPlacedOnParent() && i.isHollow())
/*      */                 {
/*  881 */                   if (i.getWatcherSet() != null)
/*      */                   {
/*  883 */                     for (Creature watcher : i.getWatcherSet()) {
/*  884 */                       watcher.getCommunicator().sendCloseInventoryWindow(i.getWurmId());
/*      */                     }
/*      */                   }
/*      */                 }
/*      */               } 
/*  889 */             } else if (recursiveParentCheck() != null && getTopParentOrNull().getTemplateId() != 0) {
/*      */               
/*  891 */               if (this.watchers != null)
/*      */               {
/*  893 */                 for (Creature watcher : this.watchers)
/*      */                 {
/*  895 */                   watcher.getCommunicator().sendRemoveFromInventory(this);
/*  896 */                   watcher.getCommunicator().sendCloseInventoryWindow(getWurmId());
/*      */                 }
/*      */               
/*      */               }
/*      */             } 
/*  901 */           } catch (NoSuchItemException noSuchItemException) {}
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  909 */         this.parentId = pid;
/*      */         
/*  911 */         if (WurmId.getType(this.parentId) != 6)
/*      */         {
/*  913 */           if (Constants.useScheduledExecutorToUpdateItemParentInDatabase)
/*      */           {
/*  915 */             ItemParentDatabaseUpdatable lUpdatable = new ItemParentDatabaseUpdatable(this.id, this.parentId, this.dbstrings.setParentId());
/*  916 */             itemParentDatabaseUpdater.addToQueue((WurmDbUpdatable)lUpdatable);
/*      */           }
/*      */           else
/*      */           {
/*  920 */             dbcon = DbConnector.getItemDbCon();
/*  921 */             ps = dbcon.prepareStatement(this.dbstrings.setParentId());
/*  922 */             ps.setLong(1, pid);
/*  923 */             ps.setLong(2, this.id);
/*  924 */             ps.executeUpdate();
/*      */           }
/*      */         
/*      */         }
/*  928 */       } catch (SQLException sqx) {
/*      */         
/*  930 */         logger.log(Level.WARNING, "Failed to set parentId to " + pid + " for item " + this.id, sqx);
/*      */       }
/*      */       finally {
/*      */         
/*  934 */         DbUtilities.closeDatabaseObjects(ps, null);
/*  935 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public long getParentId() {
/*  943 */     return this.parentId;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTemplateId(int tid) {
/*  949 */     if (this.template != null)
/*      */     {
/*  951 */       if (this.template.getTemplateId() != tid) {
/*      */         
/*      */         try {
/*      */           
/*  955 */           boolean skipNameUpdate = isUnenchantedTurret();
/*  956 */           this.template = ItemTemplateFactory.getInstance().getTemplate(tid);
/*  957 */           if (this.template.getMaterial() != 0 && (this.template.isTransmutable || this.material == 0))
/*      */           {
/*  959 */             setMaterial(this.template.getMaterial()); } 
/*  960 */           if (this.template.isDragonArmour) {
/*      */             
/*  962 */             if (this.name.startsWith("unfinished")) {
/*      */               
/*  964 */               StringTokenizer st = new StringTokenizer(this.name);
/*  965 */               st.nextToken();
/*  966 */               String n = st.nextToken();
/*  967 */               while (st.hasMoreTokens())
/*  968 */                 n = n + " " + st.nextToken(); 
/*  969 */               setName(n, !skipNameUpdate);
/*      */             } else {
/*      */               
/*  972 */               setName(ItemFactory.generateName(this.template, getMaterial()), !skipNameUpdate);
/*      */             } 
/*      */           } else {
/*  975 */             setName(ItemFactory.generateName(this.template, getMaterial()), !skipNameUpdate);
/*  976 */           }  setSizes(this.template.getSizeX(), this.template.getSizeY(), this.template.getSizeZ());
/*      */         }
/*  978 */         catch (NoSuchTemplateException nst) {
/*      */           
/*  980 */           logger.log(Level.WARNING, "Tried to set item " + this.id + " to templateid " + tid + " which doesn't exist.", (Throwable)nst);
/*      */         } 
/*      */       }
/*      */     }
/*      */     
/*  985 */     Connection dbcon = null;
/*  986 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  989 */       dbcon = DbConnector.getItemDbCon();
/*  990 */       ps = dbcon.prepareStatement(this.dbstrings.setTemplateId());
/*  991 */       ps.setInt(1, tid);
/*  992 */       ps.setLong(2, this.id);
/*  993 */       ps.executeUpdate();
/*      */     }
/*  995 */     catch (SQLException sqx) {
/*      */       
/*  997 */       logger.log(Level.WARNING, "Failed to set templateId to " + tid + " for item " + this.id, sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1001 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1002 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/* 1004 */     updatePos();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int getTemplateId() {
/* 1010 */     if (this.template != null)
/* 1011 */       return this.template.getTemplateId(); 
/* 1012 */     int toReturn = -10;
/* 1013 */     Connection dbcon = null;
/* 1014 */     PreparedStatement ps = null;
/* 1015 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 1018 */       dbcon = DbConnector.getItemDbCon();
/* 1019 */       ps = dbcon.prepareStatement(this.dbstrings.getTemplateId());
/* 1020 */       ps.setLong(1, this.id);
/* 1021 */       rs = ps.executeQuery();
/* 1022 */       if (rs.next())
/*      */       {
/* 1024 */         toReturn = rs.getInt("TEMPLATEID");
/*      */       }
/*      */     }
/* 1027 */     catch (SQLException sqx) {
/*      */       
/* 1029 */       logger.log(Level.WARNING, "Failed to get template ID for item " + this.id, sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1033 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 1034 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/* 1036 */     return toReturn;
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
/*      */   public boolean setInscription(String aInscription, String inscriber) {
/* 1048 */     return setInscription(aInscription, inscriber, 0);
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
/*      */   public boolean setInscription(String aInscription, String inscriber, int penColour) {
/* 1060 */     if (this.inscription == null) {
/* 1061 */       this.inscription = new InscriptionData(this.id, "", inscriber, penColour);
/*      */     }
/* 1063 */     if (this.inscription.getInscription().compareTo(aInscription) != 0) {
/*      */       
/* 1065 */       this.inscription.setInscription(aInscription);
/* 1066 */       saveInscription();
/*      */       
/* 1068 */       if (this.watchers != null) {
/*      */         
/* 1070 */         for (Creature watcher : this.watchers)
/*      */         {
/* 1072 */           watcher.getCommunicator().sendUpdateInventoryItem(this);
/*      */         }
/*      */       }
/* 1075 */       else if (this.zoneId > 0 && this.parentId == -10L) {
/*      */         
/* 1077 */         VolaTile t = Zones.getTileOrNull(getTileX(), getTileY(), isOnSurface());
/* 1078 */         if (t != null)
/*      */         {
/* 1080 */           t.renameItem(this);
/*      */         }
/*      */       } 
/* 1083 */       return true;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1089 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   boolean saveInscription() {
/* 1095 */     if (this.inscription != null) {
/*      */       
/* 1097 */       Connection dbcon = null;
/* 1098 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 1101 */         dbcon = DbConnector.getItemDbCon();
/* 1102 */         if (inscriptionExists(dbcon)) {
/*      */           
/* 1104 */           ps = dbcon.prepareStatement(this.dbstrings.setInscription());
/* 1105 */           ps.setString(1, this.inscription.getInscription());
/* 1106 */           ps.setLong(2, this.id);
/* 1107 */           ps.executeUpdate();
/* 1108 */           DbUtilities.closeDatabaseObjects(ps, null);
/*      */         } else {
/*      */           
/* 1111 */           createInscriptionDataEntry(dbcon);
/*      */         } 
/* 1113 */       } catch (SQLException sqx) {
/*      */         
/* 1115 */         logger.log(Level.WARNING, "Failed to save item " + this.id, sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 1119 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1120 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/* 1122 */       return true;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1129 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public InscriptionData getInscription() {
/* 1135 */     return this.inscription;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean setDescription(String newdesc) {
/* 1141 */     newdesc = newdesc.substring(0, Math.min(255, newdesc.length()));
/* 1142 */     if (!this.description.equals(newdesc)) {
/*      */       
/* 1144 */       Connection dbcon = null;
/* 1145 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 1148 */         this.description = newdesc;
/* 1149 */         dbcon = DbConnector.getItemDbCon();
/* 1150 */         ps = dbcon.prepareStatement(this.dbstrings.setDescription());
/* 1151 */         ps.setString(1, this.description);
/* 1152 */         ps.setLong(2, this.id);
/* 1153 */         ps.executeUpdate();
/* 1154 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1155 */         if (this.watchers != null)
/*      */         {
/* 1157 */           for (Creature watcher : this.watchers)
/*      */           {
/* 1159 */             watcher.getCommunicator().sendUpdateInventoryItem(this);
/*      */           }
/*      */         }
/* 1162 */         else if (this.zoneId > 0 && this.parentId == -10L)
/*      */         {
/* 1164 */           VolaTile t = Zones.getTileOrNull(getTileX(), getTileY(), isOnSurface());
/* 1165 */           if (t != null)
/*      */           {
/* 1167 */             t.renameItem(this);
/*      */           }
/*      */         }
/*      */       
/* 1171 */       } catch (SQLException sqx) {
/*      */         
/* 1173 */         logger.log(Level.WARNING, "Failed to set Description to '" + this.description + "' for item " + this.id, sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 1177 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1178 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/* 1180 */       return true;
/*      */     } 
/* 1182 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setName(String newName) {
/* 1188 */     setName(newName, true);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setName(String newname, boolean sendUpdate) {
/* 1194 */     newname = newname.substring(0, Math.min(39, newname.length()));
/* 1195 */     if (!this.name.equals(newname)) {
/*      */       
/* 1197 */       Connection dbcon = null;
/* 1198 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 1201 */         this.name = newname;
/* 1202 */         dbcon = DbConnector.getItemDbCon();
/* 1203 */         ps = dbcon.prepareStatement(this.dbstrings.setName());
/* 1204 */         ps.setString(1, this.name);
/* 1205 */         ps.setLong(2, this.id);
/* 1206 */         ps.executeUpdate();
/* 1207 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1208 */         if (sendUpdate)
/*      */         {
/* 1210 */           if (this.watchers != null)
/*      */           {
/* 1212 */             for (Creature watcher : this.watchers)
/*      */             {
/* 1214 */               watcher.getCommunicator().sendUpdateInventoryItem(this);
/*      */             }
/*      */           }
/* 1217 */           else if (this.zoneId > 0 && this.parentId == -10L)
/*      */           {
/* 1219 */             VolaTile t = Zones.getTileOrNull(getTileX(), getTileY(), isOnSurface());
/* 1220 */             if (t != null)
/*      */             {
/* 1222 */               t.renameItem(this);
/*      */             }
/*      */           }
/*      */         
/*      */         }
/* 1227 */       } catch (SQLException sqx) {
/*      */         
/* 1229 */         logger.log(Level.WARNING, "Failed to set name to '" + this.name + "' for item " + this.id, sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 1233 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1234 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String getDescription() {
/* 1242 */     if (this.template.descIsExam)
/* 1243 */       return ""; 
/* 1244 */     if (getTemplateId() == 1309 && isSealedByPlayer())
/* 1245 */       return Delivery.getContainerDescription(getWurmId()); 
/* 1246 */     return this.description;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPlace(short pl) {
/* 1252 */     if (pl != this.place) {
/*      */       
/* 1254 */       Connection dbcon = null;
/* 1255 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 1258 */         this.place = pl;
/* 1259 */         dbcon = DbConnector.getItemDbCon();
/* 1260 */         ps = dbcon.prepareStatement(this.dbstrings.setPlace());
/* 1261 */         ps.setShort(1, this.place);
/* 1262 */         ps.setLong(2, this.id);
/* 1263 */         ps.executeUpdate();
/*      */       }
/* 1265 */       catch (SQLException sqx) {
/*      */         
/* 1267 */         logger.log(Level.WARNING, "Failed to save item " + this.id + " and place: " + pl, sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 1271 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1272 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public short getPlace() {
/* 1280 */     return this.place;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setOriginalQualityLevel(float qlevel) {
/* 1286 */     if (qlevel != this.originalQualityLevel) {
/*      */       
/* 1288 */       this.originalQualityLevel = qlevel;
/* 1289 */       Connection dbcon = null;
/* 1290 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 1293 */         dbcon = DbConnector.getItemDbCon();
/* 1294 */         ps = dbcon.prepareStatement(this.dbstrings.setOriginalQualityLevel());
/* 1295 */         ps.setFloat(1, this.originalQualityLevel);
/* 1296 */         ps.setLong(2, this.id);
/* 1297 */         ps.executeUpdate();
/*      */       }
/* 1299 */       catch (SQLException sqx) {
/*      */         
/* 1301 */         logger.log(Level.WARNING, "Failed to set original QL to " + this.originalQualityLevel + " for item " + this.id, sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 1305 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1306 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public float getOriginalQualityLevel() {
/* 1314 */     return this.originalQualityLevel;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean setQualityLevel(float qlevel) {
/* 1320 */     boolean decayed = false;
/* 1321 */     if (qlevel != this.qualityLevel || qlevel <= 0.0F) {
/*      */       
/* 1323 */       boolean belowQL10 = false;
/* 1324 */       if (isBoat() && getCurrentQualityLevel() < 10.0F)
/* 1325 */         belowQL10 = true; 
/* 1326 */       this.qualityLevel = Math.min(100.0F, qlevel);
/* 1327 */       if (!checkDecay()) {
/*      */         
/* 1329 */         if (this.parentId != -10L) {
/*      */           
/* 1331 */           if (this.watchers != null)
/*      */           {
/* 1333 */             for (Creature watcher : this.watchers)
/*      */             {
/* 1335 */               watcher.getCommunicator().sendUpdateInventoryItem(this);
/*      */             
/*      */             }
/*      */           
/*      */           }
/*      */         }
/* 1341 */         else if (isUseOnGroundOnly() && !isDomainItem() && 
/* 1342 */           !isKingdomMarker() && 
/* 1343 */           !hideAddToCreationWindow() && !isNoDrop()) {
/*      */           
/* 1345 */           if (getTopParent() == getWurmId())
/*      */           {
/* 1347 */             if (this.watchers != null)
/*      */             {
/* 1349 */               for (Creature watcher : this.watchers)
/*      */               {
/* 1351 */                 watcher.getCommunicator().sendUpdateGroundItem(this);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/* 1356 */         else if (isUnfinished()) {
/*      */           
/* 1358 */           if (this.watchers != null)
/*      */           {
/* 1360 */             for (Creature watcher : this.watchers)
/*      */             {
/* 1362 */               watcher.getCommunicator().sendUpdateGroundItem(this);
/*      */             }
/*      */           }
/*      */         } 
/*      */         
/* 1367 */         Connection dbcon = null;
/* 1368 */         PreparedStatement ps = null;
/*      */         
/*      */         try {
/* 1371 */           dbcon = DbConnector.getItemDbCon();
/* 1372 */           ps = dbcon.prepareStatement(this.dbstrings.setQualityLevel());
/* 1373 */           ps.setFloat(1, this.qualityLevel);
/* 1374 */           ps.setLong(2, this.id);
/* 1375 */           ps.executeUpdate();
/*      */         }
/* 1377 */         catch (SQLException sqx) {
/*      */           
/* 1379 */           logger.log(Level.WARNING, "Failed to set QL to " + this.qualityLevel + " for item " + this.id, sqx);
/*      */         }
/*      */         finally {
/*      */           
/* 1383 */           DbUtilities.closeDatabaseObjects(ps, null);
/* 1384 */           DbConnector.returnConnection(dbcon);
/*      */         } 
/*      */         
/* 1387 */         if (belowQL10 && getCurrentQualityLevel() > 10.0F)
/*      */         {
/* 1389 */           if (isBoat())
/* 1390 */             updateIfGroundItem(); 
/*      */         }
/* 1392 */         if (getTemplate().getInitialContainers() != null)
/*      */         {
/*      */ 
/*      */           
/* 1396 */           for (Item item : getItemsAsArray())
/*      */           {
/* 1398 */             item.setQualityLevel(qlevel);
/*      */           }
/*      */         }
/*      */       } else {
/*      */         
/* 1403 */         decayed = true;
/*      */       } 
/* 1405 */     }  return decayed;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public float getQualityLevel() {
/* 1411 */     return this.qualityLevel;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLastMaintained(long last) {
/* 1417 */     if (last != this.lastMaintained) {
/*      */       
/*      */       try {
/*      */         
/* 1421 */         this.lastMaintained = last;
/*      */         
/* 1423 */         if (lastmPS == null) {
/*      */           
/* 1425 */           Connection dbcon = DbConnector.getItemDbCon();
/*      */           
/* 1427 */           if (Server.getInstance().isPS()) {
/* 1428 */             lastmPS = dbcon.prepareStatement(this.dbstrings.setLastMaintainedOld());
/*      */           } else {
/* 1430 */             lastmPS = dbcon.prepareStatement(this.dbstrings.setLastMaintained());
/*      */           } 
/*      */         } 
/* 1433 */         lastmPS.setLong(1, this.lastMaintained);
/* 1434 */         lastmPS.setLong(2, this.id);
/* 1435 */         lastmPS.addBatch();
/* 1436 */         overallLastmPSCount++;
/* 1437 */         lastmPSCount++;
/* 1438 */         this.template.maintUpdates++;
/*      */         
/* 1440 */         if (lastmPSCount > 700)
/*      */         {
/*      */ 
/*      */ 
/*      */           
/* 1445 */           long checkms = System.currentTimeMillis();
/*      */           
/* 1447 */           lastmPS.executeBatch();
/* 1448 */           DbUtilities.closeDatabaseObjects(lastmPS, null);
/* 1449 */           lastmPS = null;
/* 1450 */           if (System.currentTimeMillis() - checkms > 300L || logger.isLoggable(Level.FINEST)) {
/* 1451 */             logger.log(Level.WARNING, "SaveItemLastMaintained batch took " + (System.currentTimeMillis() - checkms) + " ms for " + lastmPSCount + " updates.");
/*      */           }
/*      */           
/* 1454 */           lastmPSCount = 0;
/*      */         }
/*      */       
/* 1457 */       } catch (SQLException sqx) {
/*      */         
/* 1459 */         logger.log(Level.WARNING, "Failed to set lastMaintained to " + this.lastMaintained + " for item " + this.id, sqx);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getLastMaintained() {
/* 1469 */     return this.lastMaintained;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean setOwnerId(long newOwnerId) {
/* 1475 */     if (this.ownerId != newOwnerId)
/*      */     {
/* 1477 */       if (Constants.useScheduledExecutorToUpdateItemOwnerInDatabase) {
/*      */         
/* 1479 */         this.ownerId = newOwnerId;
/* 1480 */         ItemOwnerDatabaseUpdatable lUpdatable = new ItemOwnerDatabaseUpdatable(this.id, this.ownerId, this.dbstrings.setOwnerId());
/* 1481 */         itemOwnerDatabaseUpdater.addToQueue((WurmDbUpdatable)lUpdatable);
/*      */       }
/*      */       else {
/*      */         
/* 1485 */         Connection dbcon = null;
/* 1486 */         PreparedStatement ps = null;
/*      */         
/*      */         try {
/* 1489 */           this.ownerId = newOwnerId;
/* 1490 */           dbcon = DbConnector.getItemDbCon();
/* 1491 */           ps = dbcon.prepareStatement(this.dbstrings.setOwnerId());
/* 1492 */           ps.setLong(1, this.ownerId);
/* 1493 */           ps.setLong(2, this.id);
/* 1494 */           ps.executeUpdate();
/*      */         }
/* 1496 */         catch (SQLException sqx) {
/*      */           
/* 1498 */           logger.log(Level.WARNING, "Failed to set ownerId to " + this.ownerId + " for item " + this.id, sqx);
/* 1499 */           return false;
/*      */         }
/*      */         finally {
/*      */           
/* 1503 */           DbUtilities.closeDatabaseObjects(ps, null);
/* 1504 */           DbConnector.returnConnection(dbcon);
/*      */         } 
/*      */       } 
/*      */     }
/* 1508 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLastOwnerId(long oid) {
/* 1514 */     if (this.lastOwner != oid)
/*      */     {
/* 1516 */       if (Constants.useScheduledExecutorToUpdateItemLastOwnerInDatabase) {
/*      */         
/* 1518 */         this.lastOwner = oid;
/* 1519 */         ItemLastOwnerDatabaseUpdatable lUpdatable = new ItemLastOwnerDatabaseUpdatable(this.id, this.lastOwner, this.dbstrings.setLastOwnerId());
/* 1520 */         itemLastOwnerDatabaseUpdater.addToQueue((WurmDbUpdatable)lUpdatable);
/*      */         
/* 1522 */         if (this.template.getInitialContainers() != null)
/*      */         {
/*      */           
/* 1525 */           for (Item ic : getItems()) {
/*      */             
/* 1527 */             if (ic.getLastOwnerId() != this.lastOwner) {
/* 1528 */               ic.setLastOwnerId(this.lastOwner);
/*      */             }
/*      */           } 
/*      */         }
/*      */       } else {
/*      */         
/* 1534 */         Connection dbcon = null;
/* 1535 */         PreparedStatement ps = null;
/*      */         
/*      */         try {
/* 1538 */           this.lastOwner = oid;
/* 1539 */           dbcon = DbConnector.getItemDbCon();
/* 1540 */           ps = dbcon.prepareStatement(this.dbstrings.setLastOwnerId());
/* 1541 */           ps.setLong(1, this.lastOwner);
/* 1542 */           ps.setLong(2, this.id);
/* 1543 */           ps.executeUpdate();
/* 1544 */           if (this.template.getInitialContainers() != null)
/*      */           {
/*      */             
/* 1547 */             for (Item ic : getItems()) {
/*      */               
/* 1549 */               if (ic.getLastOwnerId() != this.lastOwner) {
/* 1550 */                 ic.setLastOwnerId(this.lastOwner);
/*      */               }
/*      */             } 
/*      */           }
/* 1554 */         } catch (SQLException sqx) {
/*      */           
/* 1556 */           logger.log(Level.WARNING, "Failed to set last ownerId to " + this.lastOwner + " for item " + this.id, sqx);
/*      */         }
/*      */         finally {
/*      */           
/* 1560 */           DbUtilities.closeDatabaseObjects(ps, null);
/* 1561 */           DbConnector.returnConnection(dbcon);
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public long getOwnerId() {
/* 1570 */     return this.ownerId;
/*      */   }
/*      */ 
/*      */   
/*      */   public void maybeUpdateKeepnetPos() {
/* 1575 */     if (getExtra() != -1L)
/*      */     {
/* 1577 */       if (getTemplateId() == 491 || getTemplateId() == 490) {
/*      */         
/* 1579 */         Optional<Item> keepNet = Items.getItemOptional(getExtra());
/* 1580 */         if (keepNet.isPresent()) {
/*      */           
/* 1582 */           boolean switchLayers = (((Item)keepNet.get()).isOnSurface() != isOnSurface());
/* 1583 */           ((Item)keepNet.get()).setPos(this.posX, this.posY, this.posZ, this.rotation, this.onBridge);
/* 1584 */           for (Item subItem : ((Item)keepNet.get()).getItems()) {
/* 1585 */             subItem.setPos(this.posX, this.posY, this.posZ, this.rotation, this.onBridge);
/*      */           }
/* 1587 */           if (switchLayers) {
/* 1588 */             ((Item)keepNet.get()).setSurfaced(isOnSurface());
/*      */           }
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void setPos(float _posX, float _posY, float _posZ, float _rot, long bridgeId) {
/* 1597 */     if (this.posX != _posX || this.posY != _posY || this.posZ != _posZ || this.rotation != _rot || this.onBridge != bridgeId) {
/*      */       
/* 1599 */       this.posX = _posX;
/* 1600 */       this.posY = _posY;
/* 1601 */       this.posZ = _posZ;
/* 1602 */       this.rotation = _rot;
/* 1603 */       this.onBridge = bridgeId;
/*      */       
/* 1605 */       savePosition();
/*      */     } 
/* 1607 */     maybeUpdateKeepnetPos();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPosXYZRotation(float _posX, float _posY, float _posZ, float _rot) {
/* 1613 */     if (this.posX != _posX || this.posY != _posY || this.posZ != _posZ || this.rotation != _rot) {
/*      */       
/* 1615 */       Connection dbcon = null;
/* 1616 */       PreparedStatement ps = null;
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/* 1621 */         this.posX = _posX;
/* 1622 */         this.posY = _posY;
/* 1623 */         this.posZ = _posZ;
/* 1624 */         this.rotation = _rot;
/* 1625 */         dbcon = DbConnector.getItemDbCon();
/* 1626 */         ps = dbcon.prepareStatement(this.dbstrings.setPosXYZRotation());
/* 1627 */         ps.setFloat(1, this.posX);
/* 1628 */         ps.setFloat(2, this.posY);
/* 1629 */         ps.setFloat(3, this.posZ);
/* 1630 */         ps.setFloat(4, this.rotation);
/* 1631 */         ps.setLong(5, this.id);
/* 1632 */         ps.executeUpdate();
/* 1633 */         if (this.effects != null)
/*      */         {
/* 1635 */           for (Effect effect : this.effects)
/*      */           {
/* 1637 */             effect.setPosX(this.posX);
/* 1638 */             effect.setPosY(this.posY);
/* 1639 */             effect.setPosZ(this.posZ);
/*      */           }
/*      */         
/*      */         }
/* 1643 */       } catch (SQLException sqx) {
/*      */         
/* 1645 */         if (Server.getMillisToShutDown() == Long.MIN_VALUE)
/* 1646 */           Server.getInstance().startShutdown(5, "The server lost connection to the database. Shutting down "); 
/* 1647 */         logger.log(Level.WARNING, "Failed to save item " + this.id, sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 1651 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1652 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/* 1655 */     maybeUpdateKeepnetPos();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPosXYZ(float _posX, float _posY, float _posZ) {
/* 1661 */     if (this.posX != _posX || this.posY != _posY || this.posZ != _posZ) {
/*      */       
/* 1663 */       Connection dbcon = null;
/* 1664 */       PreparedStatement ps = null;
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/* 1669 */         this.posX = _posX;
/* 1670 */         this.posY = _posY;
/* 1671 */         this.posZ = _posZ;
/* 1672 */         dbcon = DbConnector.getItemDbCon();
/* 1673 */         ps = dbcon.prepareStatement(this.dbstrings.setPosXYZ());
/* 1674 */         ps.setFloat(1, this.posX);
/* 1675 */         ps.setFloat(2, this.posY);
/* 1676 */         ps.setFloat(3, this.posZ);
/* 1677 */         ps.setLong(4, this.id);
/* 1678 */         ps.executeUpdate();
/* 1679 */         if (this.effects != null)
/*      */         {
/* 1681 */           for (Effect effect : this.effects)
/*      */           {
/* 1683 */             effect.setPosX(this.posX);
/* 1684 */             effect.setPosY(this.posY);
/* 1685 */             effect.setPosZ(this.posZ);
/*      */           }
/*      */         
/*      */         }
/* 1689 */       } catch (SQLException sqx) {
/*      */         
/* 1691 */         if (Server.getMillisToShutDown() == Long.MIN_VALUE)
/* 1692 */           Server.getInstance().startShutdown(5, "The server lost connection to the database. Shutting down "); 
/* 1693 */         logger.log(Level.WARNING, "Failed to save item " + this.id, sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 1697 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1698 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/* 1701 */     maybeUpdateKeepnetPos();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPosXY(float _posX, float _posY) {
/* 1707 */     if (this.posX != _posX || this.posY != _posY) {
/*      */       
/* 1709 */       Connection dbcon = null;
/* 1710 */       PreparedStatement ps = null;
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/* 1715 */         this.posX = _posX;
/* 1716 */         this.posY = _posY;
/* 1717 */         dbcon = DbConnector.getItemDbCon();
/* 1718 */         ps = dbcon.prepareStatement(this.dbstrings.setPosXY());
/* 1719 */         ps.setFloat(1, this.posX);
/* 1720 */         ps.setFloat(2, this.posY);
/* 1721 */         ps.setLong(3, this.id);
/* 1722 */         ps.executeUpdate();
/* 1723 */         if (this.effects != null)
/*      */         {
/* 1725 */           for (Effect effect : this.effects)
/*      */           {
/* 1727 */             effect.setPosX(this.posX);
/* 1728 */             effect.setPosY(this.posY);
/*      */           }
/*      */         
/*      */         }
/* 1732 */       } catch (SQLException sqx) {
/*      */         
/* 1734 */         if (Server.getMillisToShutDown() == Long.MIN_VALUE)
/* 1735 */           Server.getInstance().startShutdown(5, "The server lost connection to the database. Shutting down "); 
/* 1736 */         logger.log(Level.WARNING, "Failed to save item " + this.id, sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 1740 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1741 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/* 1744 */     maybeUpdateKeepnetPos();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPosX(float _posX) {
/* 1750 */     if (this.posX != _posX) {
/*      */       
/* 1752 */       Connection dbcon = null;
/* 1753 */       PreparedStatement ps = null;
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/* 1758 */         this.posX = _posX;
/* 1759 */         dbcon = DbConnector.getItemDbCon();
/* 1760 */         ps = dbcon.prepareStatement(this.dbstrings.setPosX());
/* 1761 */         ps.setFloat(1, this.posX);
/* 1762 */         ps.setLong(2, this.id);
/* 1763 */         ps.executeUpdate();
/* 1764 */         if (this.effects != null)
/*      */         {
/* 1766 */           for (Effect effect : this.effects)
/*      */           {
/* 1768 */             effect.setPosX(this.posX);
/*      */           }
/*      */         }
/*      */       }
/* 1772 */       catch (SQLException sqx) {
/*      */         
/* 1774 */         if (Server.getMillisToShutDown() == Long.MIN_VALUE)
/* 1775 */           Server.getInstance().startShutdown(5, "The server lost connection to the database. Shutting down "); 
/* 1776 */         logger.log(Level.WARNING, "Failed to save item " + this.id, sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 1780 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1781 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/* 1784 */     maybeUpdateKeepnetPos();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPosY(float posy) {
/* 1790 */     if (this.posY != posy) {
/*      */       
/* 1792 */       Connection dbcon = null;
/* 1793 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 1796 */         this.posY = posy;
/* 1797 */         dbcon = DbConnector.getItemDbCon();
/* 1798 */         ps = dbcon.prepareStatement(this.dbstrings.setPosY());
/* 1799 */         ps.setFloat(1, this.posY);
/* 1800 */         ps.setLong(2, this.id);
/* 1801 */         ps.executeUpdate();
/* 1802 */         if (this.effects != null)
/*      */         {
/* 1804 */           for (Effect effect : this.effects)
/*      */           {
/* 1806 */             effect.setPosY(this.posY);
/*      */           }
/*      */         }
/*      */       }
/* 1810 */       catch (SQLException sqx) {
/*      */         
/* 1812 */         logger.log(Level.WARNING, "Failed to save item " + this.id, sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 1816 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1817 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/* 1820 */     maybeUpdateKeepnetPos();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPosZ(float posz) {
/* 1826 */     if (isFloating())
/*      */     {
/* 1828 */       posz = Math.max(0.0F, posz);
/*      */     }
/* 1830 */     if (this.posZ != posz) {
/*      */       
/* 1832 */       Connection dbcon = null;
/* 1833 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 1836 */         this.posZ = posz;
/*      */ 
/*      */         
/* 1839 */         dbcon = DbConnector.getItemDbCon();
/* 1840 */         ps = dbcon.prepareStatement(this.dbstrings.setPosZ());
/*      */         
/* 1842 */         ps.setFloat(1, this.posZ);
/* 1843 */         ps.setLong(2, this.id);
/* 1844 */         ps.executeUpdate();
/*      */         
/* 1846 */         if (this.effects != null)
/*      */         {
/* 1848 */           for (Effect effect : this.effects)
/*      */           {
/* 1850 */             effect.setPosZ(this.posZ);
/*      */           }
/*      */         }
/*      */       }
/* 1854 */       catch (SQLException sqx) {
/*      */         
/* 1856 */         logger.log(Level.WARNING, "Failed to save item " + this.id, sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 1860 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1861 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/* 1864 */     maybeUpdateKeepnetPos();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setRotation(float rot) {
/* 1870 */     if (this.rotation != rot) {
/*      */       
/* 1872 */       Connection dbcon = null;
/* 1873 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 1876 */         this.rotation = ladderRotate(rot);
/* 1877 */         dbcon = DbConnector.getItemDbCon();
/* 1878 */         ps = dbcon.prepareStatement(this.dbstrings.setRotation());
/* 1879 */         ps.setFloat(1, this.rotation);
/* 1880 */         ps.setLong(2, this.id);
/* 1881 */         ps.executeUpdate();
/* 1882 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1883 */         if (isWind() && getParentId() == -10L)
/*      */         {
/* 1885 */           if (isOnSurface()) {
/*      */             
/* 1887 */             VolaTile t = Zones.getTileOrNull(getTileX(), getTileY(), isOnSurface());
/* 1888 */             if (t != null) {
/* 1889 */               t.sendRotate(this, this.rotation);
/*      */             }
/*      */           } 
/*      */         }
/* 1893 */       } catch (SQLException sqx) {
/*      */         
/* 1895 */         logger.log(Level.WARNING, "Failed to save item " + this.id, sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 1899 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1900 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTransferred(boolean trans) {
/* 1908 */     if (this.transferred != trans) {
/*      */       
/* 1910 */       Connection dbcon = null;
/* 1911 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 1914 */         this.transferred = trans;
/* 1915 */         dbcon = DbConnector.getItemDbCon();
/* 1916 */         ps = dbcon.prepareStatement(this.dbstrings.setTransferred());
/* 1917 */         ps.setBoolean(1, this.transferred);
/* 1918 */         ps.setLong(2, this.id);
/* 1919 */         ps.executeUpdate();
/*      */       }
/* 1921 */       catch (SQLException sqx) {
/*      */         
/* 1923 */         logger.log(Level.WARNING, "Failed to save item " + this.id, sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 1927 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1928 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void savePosition() {
/* 1939 */     Connection dbcon = null;
/* 1940 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1943 */       dbcon = DbConnector.getItemDbCon();
/* 1944 */       ps = dbcon.prepareStatement(this.dbstrings.savePos());
/* 1945 */       ps.setFloat(1, this.posX);
/* 1946 */       ps.setFloat(2, this.posY);
/* 1947 */       ps.setFloat(3, this.posZ);
/* 1948 */       ps.setFloat(4, this.rotation);
/* 1949 */       ps.setLong(5, this.onBridge);
/* 1950 */       ps.setLong(6, this.id);
/* 1951 */       ps.executeUpdate();
/*      */     }
/* 1953 */     catch (SQLException sqx) {
/*      */       
/* 1955 */       logger.log(Level.WARNING, "Failed to save item " + this.id, sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1959 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1960 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public float getRotation() {
/* 1967 */     return this.rotation;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void checkSaveDamage() {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean setDamage(float dam) {
/* 1979 */     float modifier = 1.0F;
/* 1980 */     float difference = dam - this.damage;
/* 1981 */     if (difference > 0.0F)
/*      */     {
/* 1983 */       if (getSpellEffects() != null) {
/*      */         
/* 1985 */         modifier = getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_DAMAGETAKEN);
/* 1986 */         difference *= modifier;
/*      */       } 
/*      */     }
/*      */     
/* 1990 */     return setDamage(this.damage + difference, false);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean setDamage(float dam, boolean overrideIndestructible) {
/* 1996 */     boolean destroyed = false;
/* 1997 */     if (!overrideIndestructible && isIndestructible() && !isHugeAltar())
/* 1998 */       return false; 
/* 1999 */     this.lastMaintained = WurmCalendar.currentTime;
/* 2000 */     if (!isBodyPartAttached())
/*      */     {
/* 2002 */       if (dam != this.damage || dam >= 100.0F) {
/*      */         
/*      */         try {
/*      */           
/* 2006 */           boolean belowQL10 = false;
/* 2007 */           if (isBoat() && getCurrentQualityLevel() < 10.0F)
/* 2008 */             belowQL10 = true; 
/* 2009 */           boolean updateModel = false;
/* 2010 */           if (isVisibleDecay())
/*      */           {
/* 2012 */             if (dam >= 50.0F && this.damage < 50.0F) {
/* 2013 */               updateModel = true;
/* 2014 */             } else if (dam < 50.0F && this.damage >= 50.0F) {
/* 2015 */               updateModel = true;
/* 2016 */             } else if (dam < 25.0F && this.damage >= 25.0F) {
/* 2017 */               updateModel = true;
/* 2018 */             } else if (dam >= 25.0F && this.damage < 25.0F) {
/* 2019 */               updateModel = true;
/*      */             } 
/*      */           }
/* 2022 */           this.damage = Math.max(0.0F, dam);
/* 2023 */           if (!checkDecay()) {
/*      */             
/* 2025 */             if (this.parentId == -10L) {
/*      */               
/* 2027 */               if (isUseOnGroundOnly() && !isDomainItem() && 
/* 2028 */                 !isKingdomMarker() && 
/* 2029 */                 !hideAddToCreationWindow() && !isNoDrop())
/*      */               {
/* 2031 */                 if (getTopParent() == getWurmId())
/*      */                 {
/* 2033 */                   if (this.watchers != null)
/*      */                   {
/* 2035 */                     for (Creature watcher : this.watchers)
/*      */                     {
/* 2037 */                       watcher.getCommunicator().sendUpdateGroundItem(this);
/*      */                     }
/*      */                   }
/*      */                 }
/*      */               }
/* 2042 */               else if (isUnfinished())
/*      */               {
/* 2044 */                 if (this.watchers != null)
/*      */                 {
/* 2046 */                   for (Creature watcher : this.watchers)
/*      */                   {
/* 2048 */                     watcher.getCommunicator().sendUpdateGroundItem(this);
/*      */                   }
/*      */                 }
/*      */               }
/* 2052 */               else if (updateModel)
/*      */               {
/* 2054 */                 updateModelNameOnGroundItem();
/*      */               }
/*      */             
/* 2057 */             } else if (this.parentId != -10L) {
/*      */               
/* 2059 */               if (this.watchers != null)
/*      */               {
/* 2061 */                 for (Creature watcher : this.watchers)
/*      */                 {
/* 2063 */                   watcher.getCommunicator().sendUpdateInventoryItem(this);
/*      */                 }
/*      */               }
/*      */             }
/* 2067 */             else if (updateModel) {
/*      */               
/* 2069 */               updateModelNameOnGroundItem();
/*      */             }
/*      */           
/*      */           } else {
/*      */             
/* 2074 */             destroyed = true;
/* 2075 */           }  if (!destroyed)
/*      */           {
/* 2077 */             if (Constants.useScheduledExecutorToUpdateItemDamageInDatabase) {
/*      */ 
/*      */               
/* 2080 */               ItemDamageDatabaseUpdatable lUpdatable = new ItemDamageDatabaseUpdatable(this.id, this.damage, this.lastMaintained, this.dbstrings.setDamageOld());
/* 2081 */               itemDamageDatabaseUpdater.addToQueue((WurmDbUpdatable)lUpdatable);
/* 2082 */               overallDmgPSCount++;
/*      */             }
/*      */             else {
/*      */               
/* 2086 */               if (lastDmgPS == null) {
/*      */                 
/* 2088 */                 Connection dbcon = DbConnector.getItemDbCon();
/* 2089 */                 if (Server.getInstance().isPS()) {
/* 2090 */                   lastDmgPS = dbcon.prepareStatement(this.dbstrings.setDamageOld());
/*      */                 } else {
/* 2092 */                   lastDmgPS = dbcon.prepareStatement(this.dbstrings.setDamage());
/*      */                 } 
/*      */               } 
/* 2095 */               lastDmgPS.setFloat(1, this.damage);
/* 2096 */               lastDmgPS.setLong(2, this.lastMaintained);
/*      */               
/* 2098 */               lastDmgPS.setLong(3, this.id);
/* 2099 */               lastDmgPS.addBatch();
/* 2100 */               lastDmgPSCount++;
/* 2101 */               overallDmgPSCount++;
/*      */               
/* 2103 */               this.template.damUpdates++;
/* 2104 */               if (lastDmgPSCount > 700) {
/*      */                 
/* 2106 */                 long checkms = System.currentTimeMillis();
/* 2107 */                 lastDmgPS.executeBatch();
/* 2108 */                 DbUtilities.closeDatabaseObjects(lastDmgPS, null);
/* 2109 */                 lastDmgPS = null;
/* 2110 */                 if (System.currentTimeMillis() - checkms > 300L || logger.isLoggable(Level.FINEST)) {
/* 2111 */                   logger.log(Level.WARNING, "SaveItemDamage batch took " + (
/* 2112 */                       System.currentTimeMillis() - checkms) + " ms for " + lastDmgPSCount + " updates.");
/*      */                 }
/* 2114 */                 lastDmgPSCount = 0;
/*      */               } 
/*      */             } 
/*      */             
/* 2118 */             if (belowQL10 && getCurrentQualityLevel() > 10.0F)
/*      */             {
/* 2120 */               if (isBoat()) {
/* 2121 */                 updateModelNameOnGroundItem();
/*      */               }
/*      */             }
/* 2124 */             if (isPlanted() && !isRoadMarker() && (getDamage() > 70.0F || getCurrentQualityLevel() < 10.0F)) {
/*      */ 
/*      */               
/* 2127 */               VolaTile vt = Zones.getTileOrNull(getTileX(), getTileY(), this.surfaced);
/* 2128 */               if (vt == null || vt.getVillage() == null) {
/*      */                 
/* 2130 */                 setIsPlanted(false);
/* 2131 */                 logger.info("Item " + this.id + " just unplanted itself.");
/*      */               } 
/*      */             } 
/* 2134 */             if (this.damage > 0.0F) {
/* 2135 */               setIsFresh(false);
/*      */             }
/* 2137 */             if (isBoat() && Vehicles.getVehicle(this).getPilotId() != -10L)
/*      */             {
/* 2139 */               Vehicle boat = Vehicles.getVehicle(this);
/*      */               try {
/* 2141 */                 Players.getInstance().getPlayer(boat.getPilotId()).getMovementScheme().addMountSpeed((short)boat.calculateNewBoatSpeed(false));
/* 2142 */               } catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */             }
/*      */           
/*      */           }
/*      */         
/*      */         }
/* 2148 */         catch (SQLException sqx) {
/*      */           
/* 2150 */           logger.log(Level.WARNING, "Failed to save item " + this.id, sqx);
/*      */         } 
/*      */       }
/*      */     }
/* 2154 */     return destroyed;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public float getDamage() {
/* 2160 */     return this.damage;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLocked(boolean lock) {
/* 2166 */     if (lock != this.locked) {
/*      */       
/* 2168 */       Connection dbcon = null;
/* 2169 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 2172 */         this.locked = lock;
/*      */         
/* 2174 */         dbcon = DbConnector.getItemDbCon();
/* 2175 */         ps = dbcon.prepareStatement(this.dbstrings.setLocked());
/* 2176 */         ps.setBoolean(1, this.locked);
/* 2177 */         ps.setLong(2, this.id);
/*      */         
/* 2179 */         ps.executeUpdate();
/*      */       }
/* 2181 */       catch (SQLException sqx) {
/*      */         
/* 2183 */         logger.log(Level.WARNING, "Failed to save item " + this.id, sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 2187 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 2188 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTemperature(short temp) {
/* 2196 */     if (isFood())
/* 2197 */       temp = (short)Math.min(3500, temp); 
/* 2198 */     if (this.temperature != temp) {
/*      */       
/* 2200 */       Connection dbcon = null;
/* 2201 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 2204 */         boolean flag = isOnFire();
/*      */         
/* 2206 */         int diff = this.temperature - temp;
/* 2207 */         if (diff > 400 || diff < -400) {
/* 2208 */           this.tempChange = 400;
/*      */         
/*      */         }
/* 2211 */         else if (diff > 0) {
/*      */           
/* 2213 */           this.tempChange = (byte)Math.min(400, this.tempChange + diff);
/*      */         }
/*      */         else {
/*      */           
/* 2217 */           this.tempChange = (byte)Math.max(-400, this.tempChange + diff);
/*      */         } 
/*      */         
/* 2220 */         this.temperature = temp;
/* 2221 */         boolean flag2 = isOnFire();
/* 2222 */         if ((isFood() && (this.tempChange >= 100 || this.tempChange <= -100)) || this.tempChange == 400 || this.tempChange == -400) {
/*      */           
/* 2224 */           this.tempChange = 0;
/* 2225 */           dbcon = DbConnector.getItemDbCon();
/* 2226 */           ps = dbcon.prepareStatement(this.dbstrings.setTemperature());
/* 2227 */           ps.setShort(1, temp);
/* 2228 */           ps.setLong(2, this.id);
/*      */           
/* 2230 */           ps.executeUpdate();
/* 2231 */           DbUtilities.closeDatabaseObjects(ps, null);
/*      */         } 
/*      */         
/* 2234 */         if ((isLight() || isFire() || getTemplateId() == 178 || 
/* 2235 */           getTemplateId() == 889 || 
/* 2236 */           getTemplateId() == 180 || 
/* 2237 */           getTemplateId() == 1178 || 
/* 2238 */           getTemplateId() == 1301 || 
/* 2239 */           getTemplateId() == 1243) && flag != flag2)
/*      */         {
/*      */ 
/*      */           
/* 2243 */           if (flag)
/*      */           {
/* 2245 */             if (this.parentId == -10L) {
/*      */               
/* 2247 */               VolaTile t = Zones.getTileOrNull(getTileX(), getTileY(), isOnSurface());
/* 2248 */               if (t != null)
/*      */               {
/* 2250 */                 t.renameItem(this);
/* 2251 */                 t.removeLightSource(this);
/*      */               }
/*      */             
/*      */             } else {
/*      */               
/* 2256 */               notifyWatchersTempChange();
/*      */               
/*      */               try {
/* 2259 */                 if (getParent() != null && getParent().getTemplate().hasViewableSubItems() && (
/* 2260 */                   !getParent().getTemplate().isContainerWithSubItems() || isPlacedOnParent())) {
/*      */                   
/* 2262 */                   VolaTile vt = Zones.getTileOrNull(getParent().getTileX(), getParent().getTileY(), getParent().isOnSurface());
/* 2263 */                   if (vt != null)
/*      */                   {
/* 2265 */                     vt.renameItem(this);
/* 2266 */                     vt.removeLightSource(this);
/*      */                   }
/*      */                 
/*      */                 } 
/* 2270 */               } catch (NoSuchItemException noSuchItemException) {}
/*      */             
/*      */             }
/*      */ 
/*      */           
/*      */           }
/* 2276 */           else if (flag2)
/*      */           {
/* 2278 */             if (this.parentId == -10L) {
/*      */               
/* 2280 */               VolaTile t = Zones.getTileOrNull(getTileX(), getTileY(), isOnSurface());
/* 2281 */               if (t != null)
/*      */               {
/* 2283 */                 t.renameItem(this);
/* 2284 */                 t.addLightSource(this);
/*      */               }
/*      */             
/*      */             } else {
/*      */               
/* 2289 */               notifyWatchersTempChange();
/*      */               
/*      */               try {
/* 2292 */                 if (getParent() != null && getParent().getTemplate().hasViewableSubItems() && (
/* 2293 */                   !getParent().getTemplate().isContainerWithSubItems() || isPlacedOnParent())) {
/*      */                   
/* 2295 */                   VolaTile vt = Zones.getTileOrNull(getParent().getTileX(), getParent().getTileY(), getParent().isOnSurface());
/* 2296 */                   if (vt != null)
/*      */                   {
/* 2298 */                     vt.renameItem(this);
/* 2299 */                     vt.addLightSource(this);
/*      */                   }
/*      */                 
/*      */                 } 
/* 2303 */               } catch (NoSuchItemException noSuchItemException) {}
/*      */             
/*      */             }
/*      */           
/*      */           }
/*      */         
/*      */         }
/*      */       }
/* 2311 */       catch (SQLException sqx) {
/*      */         
/* 2313 */         logger.log(Level.WARNING, "Failed to save item " + this.id, sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 2317 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 2318 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getLocked() {
/* 2327 */     return this.locked;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addItem(Item item, boolean loading) {
/* 2334 */     if (item.getTemplateId() == 1392)
/*      */     {
/* 2336 */       for (Item i : getItemsAsArray()) {
/*      */         
/* 2338 */         if (i.getTemplateId() == 1392 && i.getRealTemplateId() == item.getRealTemplateId()) {
/* 2339 */           Items.destroyItem(i.getWurmId());
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/* 2344 */     if (item != null) {
/*      */       
/* 2346 */       if (this.items == null)
/* 2347 */         this.items = new HashSet<>(); 
/* 2348 */       this.items.add(item);
/* 2349 */       item.setSurfaced(this.surfaced);
/* 2350 */       if (!loading) {
/* 2351 */         updateParents();
/*      */       }
/* 2353 */       if (getTemplate().getContainerRestrictions() != null) {
/*      */         
/* 2355 */         Item[] existingItems = getItemsAsArray();
/* 2356 */         for (ContainerRestriction cRest : getTemplate().getContainerRestrictions()) {
/*      */           
/* 2358 */           if (cRest.doesItemOverrideSlot(item))
/*      */           {
/* 2360 */             for (Item i : existingItems) {
/* 2361 */               if (i.getTemplateId() == 1392 && i.getRealTemplateId() == cRest.getEmptySlotTemplateId()) {
/* 2362 */                 Items.destroyItem(i.getWurmId());
/*      */               }
/*      */             } 
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } else {
/* 2369 */       logger.warning("Ignored attempt to add a null item to " + this);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void removeItem(Item item) {
/* 2376 */     if (this.items != null)
/*      */     {
/*      */       
/* 2379 */       if (item != null) {
/*      */         
/* 2381 */         this.items.remove(item);
/*      */       }
/*      */       else {
/*      */         
/* 2385 */         logger.warning("Ignored attempt to remove a null item from " + this);
/*      */       } 
/*      */     }
/* 2388 */     updateParents();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<Item> getItems() {
/* 2394 */     if (this.items == null)
/*      */     {
/* 2396 */       this.items = new HashSet<>();
/*      */     }
/* 2398 */     return this.items;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Item[] getItemsAsArray() {
/* 2404 */     if (this.items == null)
/* 2405 */       return emptyItems; 
/* 2406 */     return this.items.<Item>toArray(new Item[this.items.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setSizeX(int sizex) {
/* 2412 */     if (sizex != this.sizeX) {
/*      */       
/* 2414 */       Connection dbcon = null;
/* 2415 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 2418 */         if (!isLiquid()) {
/* 2419 */           sizex = Math.min(this.template.getSizeX() * 4, sizex);
/*      */         }
/* 2421 */         this.sizeX = sizex;
/* 2422 */         dbcon = DbConnector.getItemDbCon();
/* 2423 */         ps = dbcon.prepareStatement(this.dbstrings.setSizeX());
/* 2424 */         ps.setInt(1, this.sizeX);
/* 2425 */         ps.setLong(2, this.id);
/* 2426 */         ps.executeUpdate();
/*      */       }
/* 2428 */       catch (SQLException sqx) {
/*      */         
/* 2430 */         logger.log(Level.WARNING, "Failed to save item " + this.id, sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 2434 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 2435 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int getSizeX() {
/* 2443 */     float modifier = 1.0F;
/* 2444 */     if (getSpellEffects() != null)
/*      */     {
/* 2446 */       modifier = getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_SIZE);
/*      */     }
/*      */     
/* 2449 */     return (int)(this.sizeX * modifier);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setSizeY(int sizey) {
/* 2455 */     if (sizey != this.sizeY) {
/*      */       
/* 2457 */       Connection dbcon = null;
/* 2458 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 2461 */         if (!isLiquid()) {
/* 2462 */           sizey = Math.min(this.template.getSizeY() * 4, sizey);
/*      */         }
/* 2464 */         this.sizeY = sizey;
/* 2465 */         dbcon = DbConnector.getItemDbCon();
/* 2466 */         ps = dbcon.prepareStatement(this.dbstrings.setSizeY());
/* 2467 */         ps.setInt(1, this.sizeY);
/* 2468 */         ps.setLong(2, this.id);
/* 2469 */         ps.executeUpdate();
/*      */       }
/* 2471 */       catch (SQLException sqx) {
/*      */         
/* 2473 */         logger.log(Level.WARNING, "Failed to save item " + this.id, sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 2477 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 2478 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int getSizeY() {
/* 2486 */     float modifier = 1.0F;
/* 2487 */     if (getSpellEffects() != null)
/*      */     {
/* 2489 */       modifier = getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_SIZE);
/*      */     }
/*      */     
/* 2492 */     return (int)(this.sizeY * modifier);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setSizeZ(int sizez) {
/* 2498 */     if (this.sizeZ != sizez) {
/*      */       
/* 2500 */       Connection dbcon = null;
/* 2501 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 2504 */         if (!isLiquid()) {
/* 2505 */           sizez = Math.min(this.template.getSizeZ() * 4, sizez);
/*      */         }
/* 2507 */         this.sizeZ = sizez;
/* 2508 */         dbcon = DbConnector.getItemDbCon();
/* 2509 */         ps = dbcon.prepareStatement(this.dbstrings.setSizeZ());
/* 2510 */         ps.setInt(1, this.sizeZ);
/* 2511 */         ps.setLong(2, this.id);
/* 2512 */         ps.executeUpdate();
/*      */       }
/* 2514 */       catch (SQLException sqx) {
/*      */         
/* 2516 */         logger.log(Level.WARNING, "Failed to save item " + this.id, sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 2520 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 2521 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int getSizeZ() {
/* 2529 */     float modifier = 1.0F;
/* 2530 */     if (getSpellEffects() != null)
/*      */     {
/* 2532 */       modifier = getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_SIZE);
/*      */     }
/*      */     
/* 2535 */     return (int)(this.sizeZ * modifier);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean setWeight(int w, boolean destroyOnWeightZero) {
/* 2541 */     return setWeight(w, destroyOnWeightZero, true);
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
/*      */   public boolean setWeight(int w, boolean destroyOnWeightZero, boolean updateOwner) {
/* 2553 */     if (this.weight != w) {
/*      */       
/* 2555 */       Connection dbcon = null;
/* 2556 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 2559 */         if (destroyOnWeightZero && w <= 0) {
/*      */           
/* 2561 */           Items.destroyItem(this.id);
/* 2562 */           return true;
/*      */         } 
/*      */ 
/*      */         
/* 2566 */         if (this.ownerId != -10L)
/*      */         {
/* 2568 */           if (updateOwner)
/*      */             
/*      */             try {
/* 2571 */               Creature owner = Server.getInstance().getCreature(this.ownerId);
/*      */               
/* 2573 */               if (isBodyPart()) {
/*      */                 
/* 2575 */                 if (getAuxData() == 100)
/*      */                 {
/* 2577 */                   if (!owner.removeCarriedWeight(this.weight)) {
/* 2578 */                     logger.log(Level.WARNING, getName() + " removed " + this.weight + " and added " + w, new Exception());
/*      */                   }
/*      */                   
/* 2581 */                   owner.addCarriedWeight(w);
/*      */                 }
/*      */               
/*      */               } else {
/*      */                 
/* 2586 */                 if (!owner.removeCarriedWeight(this.weight)) {
/* 2587 */                   logger.log(Level.WARNING, getName() + " removed " + this.weight + " and added " + w, new Exception());
/*      */                 }
/*      */                 
/* 2590 */                 owner.addCarriedWeight(w);
/*      */               }
/*      */             
/* 2593 */             } catch (NoSuchCreatureException noSuchCreatureException) {
/*      */ 
/*      */             
/* 2596 */             } catch (NoSuchPlayerException nsp) {
/*      */               
/* 2598 */               logger.log(Level.WARNING, "Creature doesn't exist although it says so." + nsp.getMessage(), (Throwable)nsp);
/*      */             }  
/*      */         }
/* 2601 */         if (isCombine() && !isLiquid()) {
/*      */           
/* 2603 */           double modi = Math.min(4.0D, 
/* 2604 */               Math.pow(w, 0.3333333333333333D) / Math.pow(this.template.getWeightGrams(), 0.3333333333333333D));
/* 2605 */           setSizeZ(Math.max(1, (int)(this.template.getSizeZ() * modi)));
/* 2606 */           setSizeY(Math.max(1, (int)(this.template.getSizeY() * modi)));
/* 2607 */           setSizeX(Math.max(1, (int)(this.template.getSizeX() * modi)));
/*      */         } 
/* 2609 */         this.weight = w;
/*      */         
/* 2611 */         if (isBulkItem())
/*      */         {
/* 2613 */           setDescription("" + getBulkNums() + "x");
/*      */         }
/*      */         
/* 2616 */         if (this.parentId != -10L) {
/*      */           
/* 2618 */           updateParents();
/*      */ 
/*      */         
/*      */         }
/* 2622 */         else if (isUseOnGroundOnly() && !isDomainItem() && 
/* 2623 */           !isKingdomMarker() && 
/* 2624 */           !hideAddToCreationWindow() && !isNoDrop()) {
/*      */           
/* 2626 */           if (getTopParent() == getWurmId())
/*      */           {
/* 2628 */             if (this.watchers != null)
/*      */             {
/* 2630 */               for (Creature watcher : this.watchers)
/*      */               {
/* 2632 */                 watcher.getCommunicator().sendUpdateGroundItem(this);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/* 2637 */         else if (isUnfinished()) {
/*      */           
/* 2639 */           if (this.watchers != null)
/*      */           {
/* 2641 */             for (Creature watcher : this.watchers)
/*      */             {
/* 2643 */               watcher.getCommunicator().sendUpdateGroundItem(this);
/*      */             }
/*      */           }
/*      */         } 
/*      */         
/* 2648 */         dbcon = DbConnector.getItemDbCon();
/* 2649 */         ps = dbcon.prepareStatement(this.dbstrings.setWeight());
/* 2650 */         ps.setInt(1, this.weight);
/* 2651 */         ps.setLong(2, this.id);
/* 2652 */         ps.executeUpdate();
/*      */       
/*      */       }
/* 2655 */       catch (SQLException sqx) {
/*      */         
/* 2657 */         logger.log(Level.WARNING, "Failed to save item " + this.id, sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 2661 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 2662 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/* 2665 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int getWeightGrams() {
/* 2671 */     if (getSpellEffects() == null) {
/* 2672 */       return this.weight;
/*      */     }
/* 2674 */     return (int)(this.weight * getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_WEIGHT));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setData1(int d1) {
/* 2680 */     if (this.data == null)
/* 2681 */       this.data = new ItemData(this.id, -1, -1, -1, -1); 
/* 2682 */     if (this.data.data1 != d1) {
/*      */       
/* 2684 */       Connection dbcon = null;
/* 2685 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 2688 */         this.data.data1 = d1;
/* 2689 */         dbcon = DbConnector.getItemDbCon();
/* 2690 */         if (dataExists(dbcon)) {
/*      */           
/* 2692 */           ps = dbcon.prepareStatement(this.dbstrings.updateData1());
/* 2693 */           ps.setInt(1, this.data.data1);
/* 2694 */           ps.setLong(2, this.id);
/* 2695 */           ps.executeUpdate();
/* 2696 */           DbUtilities.closeDatabaseObjects(ps, null);
/*      */         } else {
/*      */           
/* 2699 */           createDataEntry(dbcon);
/*      */         } 
/* 2701 */       } catch (SQLException sqx) {
/*      */         
/* 2703 */         logger.log(Level.WARNING, "Failed to save item " + this.id, sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 2707 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 2708 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int getData1() {
/* 2716 */     if (this.data != null) {
/* 2717 */       return this.data.data1;
/*      */     }
/* 2719 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setData2(int d2) {
/* 2725 */     if (this.data == null)
/* 2726 */       this.data = new ItemData(this.id, -1, -1, -1, -1); 
/* 2727 */     if (this.data.data2 != d2) {
/*      */       
/* 2729 */       Connection dbcon = null;
/* 2730 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 2733 */         this.data.data2 = d2;
/* 2734 */         dbcon = DbConnector.getItemDbCon();
/* 2735 */         if (dataExists(dbcon)) {
/*      */           
/* 2737 */           ps = dbcon.prepareStatement(this.dbstrings.updateData2());
/* 2738 */           ps.setInt(1, this.data.data2);
/* 2739 */           ps.setLong(2, this.id);
/* 2740 */           ps.executeUpdate();
/* 2741 */           DbUtilities.closeDatabaseObjects(ps, null);
/*      */         } else {
/*      */           
/* 2744 */           createDataEntry(dbcon);
/*      */         } 
/* 2746 */       } catch (SQLException sqx) {
/*      */         
/* 2748 */         logger.log(Level.WARNING, "Failed to save item " + this.id, sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 2752 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 2753 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int getData2() {
/* 2761 */     if (this.data != null) {
/* 2762 */       return this.data.data2;
/*      */     }
/* 2764 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setData(int d1, int d2) {
/* 2770 */     if (this.data == null)
/* 2771 */       this.data = new ItemData(this.id, -1, -1, -1, -1); 
/* 2772 */     if (this.data.data1 != d1 || this.data.data2 != d2) {
/*      */       
/* 2774 */       Connection dbcon = null;
/* 2775 */       PreparedStatement ps = null;
/*      */ 
/*      */       
/*      */       try {
/* 2779 */         this.data.data1 = d1;
/* 2780 */         this.data.data2 = d2;
/* 2781 */         dbcon = DbConnector.getItemDbCon();
/* 2782 */         if (dataExists(dbcon)) {
/* 2783 */           ps = dbcon.prepareStatement(this.dbstrings.updateAllData());
/*      */         } else {
/* 2785 */           ps = dbcon.prepareStatement(this.dbstrings.createData());
/* 2786 */         }  ps.setInt(1, this.data.data1);
/* 2787 */         ps.setInt(2, this.data.data2);
/* 2788 */         ps.setInt(3, this.data.extra1);
/* 2789 */         ps.setInt(4, this.data.extra2);
/* 2790 */         ps.setLong(5, this.id);
/* 2791 */         ps.executeUpdate();
/*      */       }
/* 2793 */       catch (SQLException sqx) {
/*      */         
/* 2795 */         logger.log(Level.WARNING, "Failed to save item " + this.id, sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 2799 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 2800 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setExtra1(int e1) {
/* 2808 */     if (this.data == null)
/* 2809 */       this.data = new ItemData(this.id, -1, -1, -1, -1); 
/* 2810 */     if (this.data.extra1 != e1) {
/*      */       
/* 2812 */       Connection dbcon = null;
/* 2813 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 2816 */         this.data.extra1 = e1;
/* 2817 */         dbcon = DbConnector.getItemDbCon();
/* 2818 */         if (dataExists(dbcon)) {
/*      */           
/* 2820 */           ps = dbcon.prepareStatement(this.dbstrings.updateExtra1());
/* 2821 */           ps.setInt(1, this.data.extra1);
/* 2822 */           ps.setLong(2, this.id);
/* 2823 */           ps.executeUpdate();
/* 2824 */           DbUtilities.closeDatabaseObjects(ps, null);
/*      */         } else {
/*      */           
/* 2827 */           createDataEntry(dbcon);
/*      */         } 
/* 2829 */       } catch (SQLException sqx) {
/*      */         
/* 2831 */         logger.log(Level.WARNING, "Failed to save item " + this.id, sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 2835 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 2836 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int getExtra1() {
/* 2844 */     if (this.data != null) {
/* 2845 */       return this.data.extra1;
/*      */     }
/* 2847 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setExtra2(int e2) {
/* 2853 */     if (this.data == null)
/* 2854 */       this.data = new ItemData(this.id, -1, -1, -1, -1); 
/* 2855 */     if (this.data.extra2 != e2) {
/*      */       
/* 2857 */       Connection dbcon = null;
/* 2858 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 2861 */         this.data.extra2 = e2;
/* 2862 */         dbcon = DbConnector.getItemDbCon();
/* 2863 */         if (dataExists(dbcon)) {
/*      */           
/* 2865 */           ps = dbcon.prepareStatement(this.dbstrings.updateExtra2());
/* 2866 */           ps.setInt(1, this.data.extra2);
/* 2867 */           ps.setLong(2, this.id);
/* 2868 */           ps.executeUpdate();
/* 2869 */           DbUtilities.closeDatabaseObjects(ps, null);
/*      */         } else {
/*      */           
/* 2872 */           createDataEntry(dbcon);
/*      */         } 
/* 2874 */       } catch (SQLException sqx) {
/*      */         
/* 2876 */         logger.log(Level.WARNING, "Failed to save item " + this.id, sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 2880 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 2881 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int getExtra2() {
/* 2889 */     if (this.data != null) {
/* 2890 */       return this.data.extra2;
/*      */     }
/* 2892 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setExtra(int e1, int e2) {
/* 2898 */     if (this.data == null)
/* 2899 */       this.data = new ItemData(this.id, -1, -1, -1, -1); 
/* 2900 */     if (this.data.extra1 != e1 || this.data.extra2 != e2) {
/*      */       
/* 2902 */       Connection dbcon = null;
/* 2903 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 2906 */         this.data.extra1 = e1;
/* 2907 */         this.data.extra2 = e2;
/* 2908 */         dbcon = DbConnector.getItemDbCon();
/* 2909 */         if (dataExists(dbcon)) {
/* 2910 */           ps = dbcon.prepareStatement(this.dbstrings.updateAllData());
/*      */         } else {
/* 2912 */           ps = dbcon.prepareStatement(this.dbstrings.createData());
/* 2913 */         }  ps.setInt(1, this.data.data1);
/* 2914 */         ps.setInt(2, this.data.data2);
/* 2915 */         ps.setInt(3, this.data.extra1);
/* 2916 */         ps.setInt(4, this.data.extra2);
/* 2917 */         ps.setLong(5, this.id);
/* 2918 */         ps.executeUpdate();
/*      */       }
/* 2920 */       catch (SQLException sqx) {
/*      */         
/* 2922 */         logger.log(Level.WARNING, "Failed to save item " + this.id, sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 2926 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 2927 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAllData(int d1, int d2, int e1, int e2) {
/* 2935 */     if (this.data == null)
/* 2936 */       this.data = new ItemData(this.id, -1, -1, -1, -1); 
/* 2937 */     if (this.data.data1 != d1 || this.data.data2 != d2 || this.data.extra1 != e1 || this.data.extra2 != e2) {
/*      */       
/* 2939 */       Connection dbcon = null;
/* 2940 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 2943 */         this.data.data1 = d1;
/* 2944 */         this.data.data2 = d2;
/* 2945 */         this.data.extra1 = e1;
/* 2946 */         this.data.extra2 = e2;
/* 2947 */         dbcon = DbConnector.getItemDbCon();
/* 2948 */         if (dataExists(dbcon)) {
/* 2949 */           ps = dbcon.prepareStatement(this.dbstrings.updateAllData());
/*      */         } else {
/* 2951 */           ps = dbcon.prepareStatement(this.dbstrings.createData());
/* 2952 */         }  ps.setInt(1, this.data.data1);
/* 2953 */         ps.setInt(2, this.data.data2);
/* 2954 */         ps.setInt(3, this.data.extra1);
/* 2955 */         ps.setInt(4, this.data.extra2);
/* 2956 */         ps.setLong(5, this.id);
/* 2957 */         ps.executeUpdate();
/*      */       }
/* 2959 */       catch (SQLException sqx) {
/*      */         
/* 2961 */         logger.log(Level.WARNING, "Failed to save item " + this.id, sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 2965 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 2966 */         DbConnector.returnConnection(dbcon);
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
/*      */   private boolean inscriptionExists(Connection dbcon) {
/* 2990 */     PreparedStatement ps = null;
/* 2991 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 2994 */       ps = dbcon.prepareStatement(this.dbstrings.getInscription());
/* 2995 */       ps.setLong(1, this.id);
/* 2996 */       rs = ps.executeQuery();
/* 2997 */       return rs.next();
/*      */     }
/* 2999 */     catch (SQLException sqx) {
/*      */       
/* 3001 */       logger.log(Level.WARNING, "Failed to save item " + this.id, sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 3005 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*      */     } 
/* 3007 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean dataExists(Connection dbcon) {
/* 3012 */     PreparedStatement ps = null;
/* 3013 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 3016 */       ps = dbcon.prepareStatement(this.dbstrings.getData());
/* 3017 */       ps.setLong(1, this.id);
/* 3018 */       rs = ps.executeQuery();
/* 3019 */       return rs.next();
/*      */     }
/* 3021 */     catch (SQLException sqx) {
/*      */       
/* 3023 */       logger.log(Level.WARNING, "Failed to save item " + this.id, sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 3027 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*      */     } 
/* 3029 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private void createInscriptionDataEntry(Connection dbcon) {
/* 3034 */     PreparedStatement ps = null;
/* 3035 */     if (this.inscription == null) {
/* 3036 */       this.inscription = new InscriptionData(this.id, "", "", 0);
/*      */     }
/*      */     try {
/* 3039 */       ps = dbcon.prepareStatement(this.dbstrings.createInscription());
/* 3040 */       ps.setLong(1, this.id);
/* 3041 */       ps.setString(2, this.inscription.getInscription());
/* 3042 */       ps.setString(3, this.inscription.getInscriber());
/* 3043 */       ps.setInt(4, this.inscription.getPenColour());
/* 3044 */       ps.executeUpdate();
/*      */     }
/* 3046 */     catch (SQLException sqx) {
/*      */       
/* 3048 */       logger.log(Level.WARNING, "Failed to save inscription data for item " + this.id, sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 3052 */       DbUtilities.closeDatabaseObjects(ps, null);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void createDataEntry(Connection dbcon) {
/* 3058 */     PreparedStatement ps = null;
/* 3059 */     if (this.data == null) {
/* 3060 */       this.data = new ItemData(this.id, -1, -1, -1, -1);
/*      */     }
/*      */     try {
/* 3063 */       ps = dbcon.prepareStatement(this.dbstrings.createData());
/* 3064 */       ps.setInt(1, this.data.data1);
/* 3065 */       ps.setInt(2, this.data.data2);
/* 3066 */       ps.setInt(3, this.data.extra1);
/* 3067 */       ps.setInt(4, this.data.extra2);
/* 3068 */       ps.setLong(5, this.id);
/* 3069 */       ps.executeUpdate();
/*      */     }
/* 3071 */     catch (SQLException sqx) {
/*      */       
/* 3073 */       logger.log(Level.WARNING, "Failed to save item " + this.id, sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 3077 */       DbUtilities.closeDatabaseObjects(ps, null);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getMaterial() {
/* 3084 */     if (getTemplateId() == 1307 && getData1() > 0 && getRealTemplate() != null) {
/*      */       
/* 3086 */       if (this.material != 0) {
/* 3087 */         return this.material;
/*      */       }
/* 3089 */       return getRealTemplate().getMaterial();
/*      */     } 
/* 3091 */     if (getTemplateId() == 1307) {
/* 3092 */       return 0;
/*      */     }
/* 3094 */     if (this.material == 0) {
/* 3095 */       return this.template.getMaterial();
/*      */     }
/* 3097 */     return this.material;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMaterial(byte mat) {
/* 3103 */     if (this.material != mat) {
/*      */       
/* 3105 */       Connection dbcon = null;
/* 3106 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 3109 */         this.material = mat;
/* 3110 */         dbcon = DbConnector.getItemDbCon();
/* 3111 */         ps = dbcon.prepareStatement(this.dbstrings.setMaterial());
/* 3112 */         ps.setByte(1, this.material);
/* 3113 */         ps.setLong(2, this.id);
/* 3114 */         ps.executeUpdate();
/*      */       }
/* 3116 */       catch (SQLException sqx) {
/*      */         
/* 3118 */         logger.log(Level.WARNING, "Failed to save item " + this.id, sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 3122 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 3123 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBanked(boolean bank) {
/* 3131 */     if (this.banked != bank) {
/*      */       
/* 3133 */       Connection dbcon = null;
/* 3134 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 3137 */         this.banked = bank;
/* 3138 */         dbcon = DbConnector.getItemDbCon();
/* 3139 */         ps = dbcon.prepareStatement(this.dbstrings.setBanked());
/* 3140 */         ps.setBoolean(1, this.banked);
/* 3141 */         ps.setLong(2, this.id);
/* 3142 */         ps.executeUpdate();
/*      */       }
/* 3144 */       catch (SQLException sqx) {
/*      */         
/* 3146 */         logger.log(Level.WARNING, "Failed to save item " + this.id, sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 3150 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 3151 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void bless(int blesser) {
/* 3159 */     if (this.bless == 0) {
/*      */       
/* 3161 */       Connection dbcon = null;
/* 3162 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 3165 */         this.bless = (byte)blesser;
/* 3166 */         dbcon = DbConnector.getItemDbCon();
/* 3167 */         ps = dbcon.prepareStatement(this.dbstrings.setBless());
/* 3168 */         ps.setByte(1, this.bless);
/* 3169 */         ps.setLong(2, this.id);
/* 3170 */         ps.executeUpdate();
/*      */       }
/* 3172 */       catch (SQLException sqx) {
/*      */         
/* 3174 */         logger.log(Level.WARNING, "Failed to save item " + this.id, sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 3178 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 3179 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void enchant(byte ench) {
/* 3187 */     if (this.enchantment != ench) {
/*      */       
/* 3189 */       Connection dbcon = null;
/* 3190 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 3193 */         this.enchantment = ench;
/* 3194 */         dbcon = DbConnector.getItemDbCon();
/* 3195 */         ps = dbcon.prepareStatement(this.dbstrings.setEnchant());
/* 3196 */         ps.setByte(1, this.enchantment);
/* 3197 */         ps.setLong(2, this.id);
/* 3198 */         ps.executeUpdate();
/*      */       }
/* 3200 */       catch (SQLException sqx) {
/*      */         
/* 3202 */         logger.log(Level.WARNING, "Failed to save item " + this.id, sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 3206 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 3207 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPrice(int newPrice) {
/* 3215 */     if (this.price != newPrice) {
/*      */       
/* 3217 */       Connection dbcon = null;
/* 3218 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 3221 */         this.price = newPrice;
/* 3222 */         dbcon = DbConnector.getItemDbCon();
/* 3223 */         ps = dbcon.prepareStatement(this.dbstrings.setPrice());
/* 3224 */         ps.setInt(1, this.price);
/* 3225 */         ps.setLong(2, this.id);
/* 3226 */         ps.executeUpdate();
/*      */       }
/* 3228 */       catch (SQLException sqx) {
/*      */         
/* 3230 */         logger.log(Level.WARNING, "Failed to save item " + this.id, sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 3234 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 3235 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAuxData(byte auxdata) {
/* 3243 */     if (this.auxbyte != auxdata) {
/*      */       
/* 3245 */       Connection dbcon = null;
/* 3246 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 3249 */         this.auxbyte = auxdata;
/* 3250 */         dbcon = DbConnector.getItemDbCon();
/* 3251 */         ps = dbcon.prepareStatement(this.dbstrings.setAuxData());
/* 3252 */         ps.setByte(1, this.auxbyte);
/* 3253 */         ps.setLong(2, this.id);
/* 3254 */         ps.executeUpdate();
/*      */       }
/* 3256 */       catch (SQLException sqx) {
/*      */         
/* 3258 */         logger.log(Level.WARNING, "Failed to save item " + this.id, sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 3262 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 3263 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/* 3265 */       if (isFood() || isAlcohol())
/*      */       {
/* 3267 */         if (this.watchers != null) {
/*      */           
/* 3269 */           for (Creature watcher : this.watchers)
/*      */           {
/* 3271 */             watcher.getCommunicator().sendUpdateInventoryItem(this);
/*      */           }
/*      */         }
/* 3274 */         else if (this.zoneId > 0 && this.parentId == -10L) {
/*      */           
/* 3276 */           VolaTile t = Zones.getTileOrNull(getTileX(), getTileY(), isOnSurface());
/* 3277 */           if (t != null)
/*      */           {
/* 3279 */             t.renameItem(this);
/*      */           }
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCreationState(byte newState) {
/* 3289 */     if (this.creationState != newState) {
/*      */       
/* 3291 */       Connection dbcon = null;
/* 3292 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 3295 */         this.creationState = newState;
/* 3296 */         dbcon = DbConnector.getItemDbCon();
/* 3297 */         ps = dbcon.prepareStatement(this.dbstrings.setCreationState());
/* 3298 */         ps.setByte(1, newState);
/* 3299 */         ps.setLong(2, this.id);
/* 3300 */         ps.executeUpdate();
/*      */       }
/* 3302 */       catch (SQLException sqx) {
/*      */         
/* 3304 */         logger.log(Level.WARNING, "Failed to save item " + this.id, sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 3308 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 3309 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setRealTemplate(int rTemplate) {
/* 3317 */     if (this.realTemplate != rTemplate) {
/*      */       
/* 3319 */       Connection dbcon = null;
/* 3320 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 3323 */         this.realTemplate = rTemplate;
/* 3324 */         dbcon = DbConnector.getItemDbCon();
/* 3325 */         ps = dbcon.prepareStatement(this.dbstrings.setRealTemplate());
/* 3326 */         ps.setInt(1, this.realTemplate);
/* 3327 */         ps.setLong(2, this.id);
/* 3328 */         ps.executeUpdate();
/*      */       }
/* 3330 */       catch (SQLException sqx) {
/*      */         
/* 3332 */         logger.log(Level.WARNING, "Failed to save item " + this.id, sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 3336 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 3337 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFemale(boolean _female) {
/* 3345 */     if (this.female != _female) {
/*      */       
/* 3347 */       Connection dbcon = null;
/* 3348 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 3351 */         this.female = _female;
/* 3352 */         dbcon = DbConnector.getItemDbCon();
/* 3353 */         ps = dbcon.prepareStatement(this.dbstrings.setFemale());
/* 3354 */         ps.setBoolean(1, this.female);
/* 3355 */         ps.setLong(2, this.id);
/* 3356 */         ps.executeUpdate();
/*      */       }
/* 3358 */       catch (SQLException sqx) {
/*      */         
/* 3360 */         logger.log(Level.WARNING, "Failed to save item " + this.id, sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 3364 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 3365 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCreator(String _creator) {
/* 3373 */     if (isNamed() && _creator != null && _creator.length() > 0 && (this.creator == null || !this.creator.equals(_creator))) {
/*      */       
/* 3375 */       this.creator = _creator.substring(0, Math.min(_creator.length(), this.creatorMaxLength));
/* 3376 */       Connection dbcon = null;
/* 3377 */       PreparedStatement ps = null;
/* 3378 */       if (this.creator.equals("0")) {
/* 3379 */         logger.log(Level.INFO, "Creator set to 0 at ", new Exception());
/*      */       }
/*      */       try {
/* 3382 */         dbcon = DbConnector.getItemDbCon();
/* 3383 */         ps = dbcon.prepareStatement(this.dbstrings.setCreator());
/* 3384 */         ps.setString(1, this.creator);
/* 3385 */         ps.setLong(2, this.id);
/* 3386 */         ps.executeUpdate();
/*      */       }
/* 3388 */       catch (SQLException sqx) {
/*      */         
/* 3390 */         logger.log(Level.WARNING, "Failed to save item " + this.id, sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 3394 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 3395 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setColor(int _color) {
/* 3403 */     if (this.color != _color)
/*      */     {
/* 3405 */       setColors(_color, this.color2);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setColor2(int _color2) {
/* 3412 */     if (this.color2 != _color2)
/*      */     {
/* 3414 */       setColors(this.color, _color2);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private void setColors(int _color, int _color2) {
/* 3420 */     Connection dbcon = null;
/* 3421 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 3424 */       this.color = _color;
/* 3425 */       this.color2 = _color2;
/* 3426 */       dbcon = DbConnector.getItemDbCon();
/* 3427 */       ps = dbcon.prepareStatement(this.dbstrings.setColor());
/* 3428 */       ps.setInt(1, this.color);
/* 3429 */       ps.setInt(2, this.color2);
/* 3430 */       ps.setLong(3, this.id);
/* 3431 */       ps.executeUpdate();
/*      */     }
/* 3433 */     catch (SQLException sqx) {
/*      */       
/* 3435 */       logger.log(Level.WARNING, "Failed to save item " + this.id, sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 3439 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 3440 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/* 3442 */     if (getParentId() != -10L) {
/*      */       
/* 3444 */       if (this.watchers != null)
/*      */       {
/* 3446 */         for (Creature watcher : this.watchers)
/*      */         {
/* 3448 */           watcher.getCommunicator().sendUpdateInventoryItem(this);
/*      */         }
/*      */       }
/*      */     }
/*      */     else {
/*      */       
/* 3454 */       updateIfGroundItem();
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
/*      */   void setWornAsArmour(boolean wornArmour, long newOwner) {
/* 3468 */     if (this.wornAsArmour != wornArmour) {
/*      */ 
/*      */       
/* 3471 */       Connection dbcon = null;
/* 3472 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 3475 */         this.wornAsArmour = wornArmour;
/* 3476 */         dbcon = DbConnector.getItemDbCon();
/* 3477 */         ps = dbcon.prepareStatement(this.dbstrings.setWornAsArmour());
/* 3478 */         ps.setBoolean(1, this.wornAsArmour);
/* 3479 */         ps.setLong(2, this.id);
/* 3480 */         ps.executeUpdate();
/*      */       }
/* 3482 */       catch (SQLException sqx) {
/*      */         
/* 3484 */         logger.log(Level.WARNING, "Failed to save item " + this.id, sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 3488 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 3489 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/* 3491 */       if (this.wornAsArmour) {
/*      */ 
/*      */         
/*      */         try {
/* 3495 */           Creature creature = Server.getInstance().getCreature(newOwner);
/* 3496 */           ArmourTemplate armour = ArmourTemplate.getArmourTemplate(this.template.templateId);
/*      */           
/* 3498 */           if (armour != null)
/*      */           {
/*      */ 
/*      */             
/* 3502 */             float moveModChange = armour.getMoveModifier();
/* 3503 */             if (Features.Feature.METALLIC_ITEMS.isEnabled()) {
/*      */               
/* 3505 */               moveModChange *= ArmourTemplate.getMaterialMovementModifier(getMaterial());
/*      */             }
/* 3507 */             else if (Servers.localServer.isChallengeOrEpicServer()) {
/*      */               
/* 3509 */               if (getMaterial() == 57 || getMaterial() == 67) {
/* 3510 */                 moveModChange *= 0.9F;
/* 3511 */               } else if (getMaterial() == 56) {
/* 3512 */                 moveModChange *= 0.95F;
/*      */               } 
/*      */             } 
/* 3515 */             (creature.getMovementScheme()).armourMod.setModifier((creature.getMovementScheme()).armourMod.getModifier() - moveModChange);
/*      */ 
/*      */             
/* 3518 */             if (armour.getLimitFactor() != creature.getArmourLimitingFactor())
/*      */             {
/*      */               
/* 3521 */               creature.recalcLimitingFactor(this);
/*      */             
/*      */             }
/*      */           
/*      */           }
/*      */ 
/*      */         
/*      */         }
/* 3529 */         catch (NoSuchPlayerException nsp) {
/*      */           
/* 3531 */           logger.log(Level.WARNING, "Worn armour on unknown player: ", (Throwable)nsp);
/*      */         }
/* 3533 */         catch (NoSuchCreatureException cnf) {
/*      */           
/* 3535 */           logger.log(Level.WARNING, "Worn armour on unknown creature: ", (Throwable)cnf);
/*      */         } 
/*      */       } else {
/*      */ 
/*      */         
/*      */         try {
/*      */           
/* 3542 */           Creature creature = Server.getInstance().getCreature(getOwnerId());
/* 3543 */           ArmourTemplate armour = ArmourTemplate.getArmourTemplate(this.template.templateId);
/* 3544 */           if (armour != null)
/*      */           {
/*      */ 
/*      */             
/* 3548 */             float moveModChange = armour.getMoveModifier();
/* 3549 */             if (Features.Feature.METALLIC_ITEMS.isEnabled()) {
/*      */               
/* 3551 */               moveModChange *= ArmourTemplate.getMaterialMovementModifier(getMaterial());
/*      */             }
/* 3553 */             else if (Servers.localServer.isChallengeOrEpicServer()) {
/*      */               
/* 3555 */               if (getMaterial() == 57 || getMaterial() == 67) {
/* 3556 */                 moveModChange *= 0.9F;
/* 3557 */               } else if (getMaterial() == 56) {
/* 3558 */                 moveModChange *= 0.95F;
/*      */               } 
/*      */             } 
/* 3561 */             (creature.getMovementScheme()).armourMod.setModifier((creature.getMovementScheme()).armourMod.getModifier() + moveModChange);
/*      */ 
/*      */             
/* 3564 */             creature.recalcLimitingFactor(null);
/*      */           
/*      */           }
/*      */ 
/*      */         
/*      */         }
/* 3570 */         catch (NoSuchPlayerException nsp) {
/*      */           
/* 3572 */           logger.log(Level.WARNING, "Worn armour on unknown player: ", (Throwable)nsp);
/*      */         }
/* 3574 */         catch (NoSuchCreatureException cnf) {
/*      */           
/* 3576 */           logger.log(Level.WARNING, "Worn armour on unknown creature: ", (Throwable)cnf);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void clear(long wurmId, String _creator, float posx, float posy, float posz, float _rot, String _desc, String _name, float _qualitylevel, byte _material, byte aRarity, long bridgeId) {
/* 3587 */     this.id = wurmId;
/* 3588 */     this.creator = _creator;
/* 3589 */     this.posX = posx;
/* 3590 */     this.posY = posy;
/* 3591 */     this.posZ = posz;
/* 3592 */     this.description = _desc;
/* 3593 */     this.name = _name;
/* 3594 */     this.qualityLevel = _qualitylevel;
/* 3595 */     this.originalQualityLevel = this.qualityLevel;
/* 3596 */     this.rotation = _rot;
/* 3597 */     this.zoneId = -10;
/* 3598 */     this.parentId = -10L;
/* 3599 */     this.auxbyte = 0;
/* 3600 */     this.sizeX = this.template.getSizeX();
/* 3601 */     this.sizeY = this.template.getSizeY();
/* 3602 */     this.sizeZ = this.template.getSizeZ();
/* 3603 */     this.weight = this.template.getWeightGrams();
/* 3604 */     this.lastMaintained = WurmCalendar.currentTime;
/* 3605 */     this.creationDate = WurmCalendar.currentTime;
/* 3606 */     this.banked = false;
/* 3607 */     this.damage = 0.0F;
/* 3608 */     this.enchantment = 0;
/* 3609 */     this.data = null;
/* 3610 */     this.color = -1;
/* 3611 */     this.color2 = -1;
/* 3612 */     this.temperature = 200;
/* 3613 */     this.creator = "";
/* 3614 */     this.isBusy = false;
/* 3615 */     this.material = _material;
/* 3616 */     this.bless = 0;
/* 3617 */     this.mailed = false;
/* 3618 */     this.mailTimes = 0;
/* 3619 */     this.rarity = aRarity;
/* 3620 */     this.onBridge = bridgeId;
/* 3621 */     this.creationState = 0;
/* 3622 */     this.hatching = false;
/* 3623 */     this.ownerId = -10L;
/* 3624 */     this.lastOwner = -10L;
/* 3625 */     this.realTemplate = -10;
/* 3626 */     if (isNamed() && _creator != null && _creator.length() > 0)
/*      */     {
/* 3628 */       this.creator = _creator.substring(0, Math.min(_creator.length(), this.creatorMaxLength));
/*      */     }
/*      */     
/* 3631 */     Connection dbcon = null;
/* 3632 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 3635 */       dbcon = DbConnector.getItemDbCon();
/*      */       
/* 3637 */       ps = dbcon.prepareStatement(this.dbstrings.clearItem());
/*      */       
/* 3639 */       ps.setString(1, this.name);
/* 3640 */       ps.setString(2, this.description);
/* 3641 */       ps.setFloat(3, this.qualityLevel);
/* 3642 */       ps.setFloat(4, this.originalQualityLevel);
/* 3643 */       ps.setLong(5, this.lastMaintained);
/* 3644 */       ps.setByte(6, this.enchantment);
/* 3645 */       ps.setBoolean(7, this.banked);
/* 3646 */       ps.setInt(8, this.sizeX);
/* 3647 */       ps.setInt(9, this.sizeY);
/* 3648 */       ps.setInt(10, this.sizeZ);
/* 3649 */       ps.setInt(11, this.zoneId);
/* 3650 */       ps.setFloat(12, this.damage);
/* 3651 */       ps.setLong(13, this.parentId);
/* 3652 */       ps.setFloat(14, this.rotation);
/* 3653 */       ps.setInt(15, this.weight);
/* 3654 */       ps.setFloat(16, this.posX);
/* 3655 */       ps.setFloat(17, this.posY);
/* 3656 */       ps.setFloat(18, this.posZ);
/* 3657 */       ps.setString(19, this.creator);
/* 3658 */       ps.setByte(20, this.auxbyte);
/* 3659 */       ps.setInt(21, this.color);
/* 3660 */       ps.setInt(22, this.color2);
/* 3661 */       ps.setShort(23, this.temperature);
/* 3662 */       ps.setLong(24, this.creationDate);
/* 3663 */       ps.setByte(25, this.material);
/* 3664 */       ps.setByte(26, this.bless);
/* 3665 */       ps.setByte(27, this.rarity);
/* 3666 */       ps.setByte(28, this.creationState);
/* 3667 */       ps.setLong(29, this.id);
/* 3668 */       ps.executeUpdate();
/*      */     }
/* 3670 */     catch (SQLException sqex) {
/*      */       
/* 3672 */       logger.log(Level.WARNING, "Failed to create/update item with id " + this.id, sqex);
/*      */     }
/*      */     finally {
/*      */       
/* 3676 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 3677 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMailed(boolean _mailed) {
/* 3685 */     if (this.mailed != _mailed) {
/*      */       
/* 3687 */       Connection dbcon = null;
/* 3688 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 3691 */         this.mailed = _mailed;
/* 3692 */         dbcon = DbConnector.getItemDbCon();
/* 3693 */         ps = dbcon.prepareStatement(this.dbstrings.setMailed());
/* 3694 */         ps.setBoolean(1, this.mailed);
/* 3695 */         ps.setLong(2, this.id);
/* 3696 */         ps.executeUpdate();
/*      */       }
/* 3698 */       catch (SQLException sqx) {
/*      */         
/* 3700 */         logger.log(Level.WARNING, "Failed to save item " + this.id, sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 3704 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 3705 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setHidden(boolean _hidden) {
/* 3713 */     if (this.hidden != _hidden) {
/*      */       
/* 3715 */       Connection dbcon = null;
/* 3716 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 3719 */         this.hidden = _hidden;
/* 3720 */         dbcon = DbConnector.getItemDbCon();
/* 3721 */         ps = dbcon.prepareStatement(this.dbstrings.setHidden());
/* 3722 */         ps.setBoolean(1, this.hidden);
/* 3723 */         ps.setLong(2, this.id);
/* 3724 */         ps.executeUpdate();
/*      */       }
/* 3726 */       catch (SQLException sqx) {
/*      */         
/* 3728 */         logger.log(Level.WARNING, "Failed to save item " + this.id, sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 3732 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 3733 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void savePermissions() {
/* 3741 */     Connection dbcon = null;
/* 3742 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 3745 */       dbcon = DbConnector.getItemDbCon();
/* 3746 */       ps = dbcon.prepareStatement(this.dbstrings.setSettings());
/* 3747 */       ps.setInt(1, getSettings().getPermissions());
/* 3748 */       ps.setLong(2, this.id);
/* 3749 */       ps.executeUpdate();
/*      */     }
/* 3751 */     catch (SQLException sqx) {
/*      */       
/* 3753 */       logger.log(Level.WARNING, "Failed to save permissions for item " + this.id, sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 3757 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 3758 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setDbStrings(DbStrings newDbStrings) {
/* 3765 */     this.dbstrings = newDbStrings;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public DbStrings getDbStrings() {
/* 3771 */     return this.dbstrings;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ItemDamageDatabaseUpdater getItemDamageDatabaseUpdater() {
/* 3781 */     return itemDamageDatabaseUpdater;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ItemOwnerDatabaseUpdater getItemOwnerDatabaseUpdater() {
/* 3791 */     return itemOwnerDatabaseUpdater;
/*      */   }
/*      */ 
/*      */   
/*      */   public static ItemLastOwnerDatabaseUpdater getItemLastOwnerDatabaseUpdater() {
/* 3796 */     return itemLastOwnerDatabaseUpdater;
/*      */   }
/*      */ 
/*      */   
/*      */   public static ItemParentDatabaseUpdater getItemParentDatabaseUpdater() {
/* 3801 */     return itemParentDatabaseUpdater;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMailTimes(byte times) {
/* 3810 */     if (this.mailTimes != times) {
/*      */       
/* 3812 */       Connection dbcon = null;
/* 3813 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 3816 */         this.mailTimes = times;
/* 3817 */         dbcon = DbConnector.getItemDbCon();
/* 3818 */         ps = dbcon.prepareStatement(this.dbstrings.setMailTimes());
/* 3819 */         ps.setByte(1, this.mailTimes);
/* 3820 */         ps.setLong(2, this.id);
/* 3821 */         ps.executeUpdate();
/*      */       }
/* 3823 */       catch (SQLException sqx) {
/*      */         
/* 3825 */         logger.log(Level.WARNING, "Failed to save item " + this.id, sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 3829 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 3830 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void moveToFreezer() {
/* 3838 */     Connection dbcon = null;
/* 3839 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 3842 */       dbcon = DbConnector.getItemDbCon();
/* 3843 */       ps = dbcon.prepareStatement(this.dbstrings.freeze());
/* 3844 */       ps.setLong(1, this.id);
/* 3845 */       ps.executeUpdate();
/*      */     }
/* 3847 */     catch (SQLException sqx) {
/*      */       
/* 3849 */       logger.log(Level.WARNING, "Failed to freeze item " + this.id, sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 3853 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 3854 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void returnFromFreezer() {
/* 3861 */     Connection dbcon = null;
/* 3862 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 3865 */       dbcon = DbConnector.getItemDbCon();
/* 3866 */       ps = dbcon.prepareStatement(this.dbstrings.thaw());
/* 3867 */       ps.setLong(1, this.id);
/* 3868 */       ps.executeUpdate();
/*      */     }
/* 3870 */     catch (SQLException sqx) {
/*      */       
/* 3872 */       logger.log(Level.WARNING, "Failed to unfreeze item " + this.id, sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 3876 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 3877 */       DbConnector.returnConnection(dbcon);
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
/*      */   public boolean setRarity(byte newRarity) {
/* 3889 */     if (newRarity != this.rarity) {
/*      */       
/* 3891 */       this.rarity = newRarity;
/* 3892 */       Connection dbcon = null;
/* 3893 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 3896 */         dbcon = DbConnector.getItemDbCon();
/* 3897 */         ps = dbcon.prepareStatement(this.dbstrings.setRarity());
/* 3898 */         ps.setByte(1, this.rarity);
/* 3899 */         ps.setLong(2, this.id);
/* 3900 */         ps.executeUpdate();
/* 3901 */         if (this.watchers != null)
/*      */         {
/* 3903 */           for (Creature watcher : this.watchers)
/*      */           {
/* 3905 */             watcher.getCommunicator().sendUpdateInventoryItem(this);
/*      */           }
/*      */         }
/* 3908 */         else if (this.zoneId > 0 && this.parentId == -10L)
/*      */         {
/* 3910 */           VolaTile t = Zones.getTileOrNull(getTileX(), getTileY(), isOnSurface());
/* 3911 */           if (t != null)
/*      */           {
/* 3913 */             t.renameItem(this);
/*      */           }
/*      */         }
/*      */       
/* 3917 */       } catch (SQLException sqx) {
/*      */         
/* 3919 */         logger.log(Level.WARNING, "Failed to set rarity " + this.rarity + " for item " + this.id, sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 3923 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 3924 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/* 3926 */       return true;
/*      */     } 
/* 3928 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPlacedOnParent(boolean onParent) {
/* 3934 */     if (this.placedOnParent != onParent) {
/*      */       
/* 3936 */       this.placedOnParent = onParent;
/* 3937 */       Connection dbcon = null;
/* 3938 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 3941 */         dbcon = DbConnector.getItemDbCon();
/* 3942 */         ps = dbcon.prepareStatement("UPDATE ITEMS SET PLACEDONPARENT=? WHERE WURMID=?");
/* 3943 */         ps.setBoolean(1, this.placedOnParent);
/* 3944 */         ps.setLong(2, this.id);
/* 3945 */         ps.executeUpdate();
/*      */       }
/* 3947 */       catch (SQLException sqx) {
/*      */         
/* 3949 */         logger.log(Level.WARNING, "Failed to set placedOnParent " + this.placedOnParent + " for item " + this.id, sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 3953 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 3954 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void deleteInDatabase() {
/* 3962 */     Connection dbcon = null;
/* 3963 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 3966 */       dbcon = DbConnector.getItemDbCon();
/* 3967 */       ps = dbcon.prepareStatement(this.dbstrings.deleteItem());
/* 3968 */       ps.setLong(1, this.id);
/* 3969 */       ps.executeUpdate();
/*      */     }
/* 3971 */     catch (SQLException sqx) {
/*      */       
/* 3973 */       logger.log(Level.WARNING, "Failed to delete item " + this.id, sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 3977 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 3978 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isItem() {
/* 3985 */     return true;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\items\DbItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */