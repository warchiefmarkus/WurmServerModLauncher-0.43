/*      */ package com.wurmonline.server.gui.propertysheet;
/*      */ 
/*      */ import coffee.keenan.network.helpers.address.AddressHelper;
/*      */ import com.wurmonline.server.Constants;
/*      */ import com.wurmonline.server.ServerEntry;
/*      */ import com.wurmonline.server.ServerProperties;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.steam.SteamHandler;
/*      */ import java.net.InetAddress;
/*      */ import java.util.HashSet;
/*      */ import java.util.Objects;
/*      */ import java.util.Optional;
/*      */ import java.util.Random;
/*      */ import java.util.Set;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javafx.beans.property.SimpleObjectProperty;
/*      */ import javafx.beans.value.ObservableValue;
/*      */ import javafx.collections.FXCollections;
/*      */ import javafx.collections.ListChangeListener;
/*      */ import javafx.collections.ObservableList;
/*      */ import javafx.scene.Node;
/*      */ import javafx.scene.control.Alert;
/*      */ import javafx.scene.control.ButtonType;
/*      */ import javafx.scene.layout.Priority;
/*      */ import javafx.scene.layout.VBox;
/*      */ import javafx.util.Callback;
/*      */ import org.controlsfx.control.PropertySheet;
/*      */ import org.controlsfx.property.editor.DefaultPropertyEditorFactory;
/*      */ import org.controlsfx.property.editor.PropertyEditor;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class ServerPropertySheet
/*      */   extends VBox
/*      */ {
/*   70 */   private static final Logger logger = Logger.getLogger(ServerPropertySheet.class.getName());
/*      */   private ServerEntry current;
/*      */   private PropertySheet propertySheet;
/*      */   private final ObservableList<PropertySheet.Item> list;
/*      */   
/*      */   private enum PropertyType {
/*   76 */     SERVERID, EXTSERVERIP, EXTSERVERPORT, INTIP, INTPORT, MAXPLAYERS, SKILLGAINRATE, ACTIONTIMER, RMIPORT, RMI_REG_PORT, PVPSERVER, HOMESERVER, SPAWNPOINTJENNX, SPAWNPOINTJENNY, SPAWNPOINTMOLX, SPAWNPOINTMOLY, SPAWNPOINTLIBX, SPAWNPOINTLIBY,
/*   77 */     KINGDOM, INTRASERVERPASSWORD, TESTSERVER, NAME, LOGINSERVER, ISPAYMENT, TWITTERCONSUMERKEY, TWITTERCONSUMERSECRET, TWITTERAPPTOKEN, TWITTERAPPSECRET, MAINTAINING, HOTADELAY, MAXCREATURES, PERCENTAGG, RANDOMSPAWNS, SKBASIC, SKFIGHT, SKMIND, SKOVERALL, SKBC, EPIC, CRMOD, STEAMPW, UPKEEP, MAXDEED, FREEDEEDS, TRADERMAX, TRADERINIT, TUNNELING, BREEDING, FIELDGROWTH, KINGSMONEY,
/*   78 */     MOTD, NPCS, STEAMQUERYPORT, TREEGROWTH, ADMIN_PWD, ENDGAMEITEMS, SPY_PREVENTION, NEWBIE_FRIENDLY, ENABLE_PNP_PORT_FORWARD;
/*      */   }
/*      */ 
/*      */   
/*   82 */   private final String categoryServerSettings = "1: Server Settings";
/*   83 */   private final String categoryAdvanceSettings = "2: Advance Server Settings";
/*   84 */   private final String categoryTweaks = "3: Gameplay Tweaks";
/*   85 */   private final String categoryTwitter = "4: Twitter Settings";
/*   86 */   private final String categoryMaintenance = "5: Maintenance";
/*      */ 
/*      */   
/*   89 */   private final String categoryOtherServerSettings = "1: Server Settings";
/*      */   
/*   91 */   private Set<PropertyType> changedProperties = new HashSet<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   class CustomPropertyItem
/*      */     implements PropertySheet.Item
/*      */   {
/*      */     private ServerPropertySheet.PropertyType type;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private String category;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private String name;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private String description;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean editable = true;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Object value;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Object minValue;
/*      */ 
/*      */ 
/*      */     
/*      */     private Object maxValue;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     CustomPropertyItem(ServerPropertySheet.PropertyType aType, String aCategory, String aName, String aDescription, boolean aEditable, Object aValue) {
/*  140 */       this.type = aType;
/*  141 */       this.category = aCategory;
/*  142 */       this.name = aName;
/*  143 */       this.description = aDescription;
/*  144 */       this.editable = aEditable;
/*  145 */       this.value = aValue;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     CustomPropertyItem(ServerPropertySheet.PropertyType aType, String aCategory, String aName, String aDescription, boolean aEditable, Object aValue, Object aMinValue) {
/*  152 */       this(aType, aCategory, aName, aDescription, aEditable, aValue);
/*  153 */       this.minValue = aMinValue;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     CustomPropertyItem(ServerPropertySheet.PropertyType aType, String aCategory, String aName, String aDescription, boolean aEditable, Object aValue, Object aMinValue, Object aMaxValue) {
/*  159 */       this(aType, aCategory, aName, aDescription, aEditable, aValue, aMinValue);
/*  160 */       this.maxValue = aMaxValue;
/*      */     }
/*      */ 
/*      */     
/*      */     public ServerPropertySheet.PropertyType getPropertyType() {
/*  165 */       return this.type;
/*      */     }
/*      */ 
/*      */     
/*      */     public Class<?> getType() {
/*  170 */       return this.value.getClass();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public String getCategory() {
/*  176 */       return this.category;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public String getName() {
/*  182 */       return this.name;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public String getDescription() {
/*  188 */       return this.description;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Optional<Class<? extends PropertyEditor<?>>> getPropertyEditorClass() {
/*  200 */       return super.getPropertyEditorClass();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isEditable() {
/*  206 */       return this.editable;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Object getValue() {
/*  212 */       return this.value;
/*      */     }
/*      */ 
/*      */     
/*      */     public Object getMinValue() {
/*  217 */       return this.minValue;
/*      */     }
/*      */ 
/*      */     
/*      */     public Object getMaxValue() {
/*  222 */       return this.maxValue;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void setValue(Object aValue) {
/*  228 */       Object newValue = aValue;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  243 */       if (!this.value.equals(newValue))
/*      */       {
/*  245 */         ServerPropertySheet.this.changedProperties.add(this.type);
/*      */       }
/*  247 */       this.value = newValue;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Optional<ObservableValue<? extends Object>> getObservableValue() {
/*  253 */       return (Optional)Optional.of(new SimpleObjectProperty(this.value));
/*      */     }
/*      */   }
/*      */   
/*      */   boolean saveNewGui = false;
/*      */   
/*      */   boolean saveSpawns = false;
/*      */   
/*      */   boolean saveTwitter = false;
/*      */   
/*      */   boolean changedId = false;
/*      */   
/*  265 */   int oldId = 0;
/*      */   private static final String PASSWORD_CHARS = "abcdefgijkmnopqrstwxyzABCDEFGHJKLMNPQRSTWXYZ23456789";
/*      */   
/*      */   public final String save() {
/*  269 */     String toReturn = "";
/*  270 */     boolean saveAtAll = false;
/*  271 */     for (CustomPropertyItem item : (CustomPropertyItem[])this.list.toArray((Object[])new CustomPropertyItem[this.list.size()])) {
/*      */       
/*  273 */       if (this.changedProperties.contains(item.getPropertyType()) || this.current.isCreating) {
/*      */         
/*  275 */         saveAtAll = true; try {
/*      */           boolean upnpSetting; boolean newSetting; boolean loadEGI; boolean spy; boolean newbie; short sqp;
/*      */           String pwd;
/*  278 */           switch (item.getPropertyType()) {
/*      */ 
/*      */             
/*      */             case SERVERID:
/*  282 */               if (this.current.isLocal) {
/*      */                 
/*  284 */                 this.changedId = true;
/*  285 */                 this.oldId = this.current.id;
/*      */               } 
/*  287 */               this.current.id = ((Integer)item.getValue()).intValue();
/*  288 */               this.saveNewGui = true;
/*      */               break;
/*      */             case ENABLE_PNP_PORT_FORWARD:
/*  291 */               upnpSetting = ((Boolean)item.getValue()).booleanValue();
/*  292 */               if (upnpSetting != ServerProperties.getBoolean("ENABLE_PNP_PORT_FORWARD", Constants.enablePnpPortForward)) {
/*      */                 
/*  294 */                 ServerProperties.setValue("ENABLE_PNP_PORT_FORWARD", Boolean.toString(upnpSetting));
/*  295 */                 ServerProperties.checkProperties();
/*      */               } 
/*      */               break;
/*      */             case EXTSERVERIP:
/*  299 */               this.current.EXTERNALIP = item.getValue().toString();
/*  300 */               this.saveNewGui = true;
/*      */               break;
/*      */             case EXTSERVERPORT:
/*  303 */               this.current.EXTERNALPORT = item.getValue().toString();
/*  304 */               this.saveNewGui = true;
/*      */               break;
/*      */             case INTIP:
/*  307 */               this.current.INTRASERVERADDRESS = item.getValue().toString();
/*  308 */               this.saveNewGui = true;
/*      */               break;
/*      */             case INTPORT:
/*  311 */               this.current.INTRASERVERPORT = item.getValue().toString();
/*  312 */               this.saveNewGui = true;
/*      */               break;
/*      */             case MAXPLAYERS:
/*  315 */               this.current.pLimit = ((Integer)item.getValue()).intValue();
/*  316 */               this.saveNewGui = true;
/*      */               break;
/*      */             case SKILLGAINRATE:
/*  319 */               this.current.setSkillGainRate(((Float)item.getValue()).floatValue());
/*  320 */               this.saveNewGui = true;
/*      */               break;
/*      */             case ACTIONTIMER:
/*  323 */               this.current.setActionTimer(((Float)item.getValue()).floatValue());
/*  324 */               this.saveNewGui = true;
/*      */               break;
/*      */             case RMIPORT:
/*  327 */               this.current.RMI_PORT = ((Integer)item.getValue()).intValue();
/*  328 */               this.saveNewGui = true;
/*      */               break;
/*      */             case RMI_REG_PORT:
/*  331 */               this.current.REGISTRATION_PORT = ((Integer)item.getValue()).intValue();
/*  332 */               this.saveNewGui = true;
/*      */               break;
/*      */             case PVPSERVER:
/*  335 */               this.current.PVPSERVER = ((Boolean)item.getValue()).booleanValue();
/*  336 */               this.saveNewGui = true;
/*      */               break;
/*      */             case EPIC:
/*  339 */               this.current.EPIC = ((Boolean)item.getValue()).booleanValue();
/*  340 */               this.saveNewGui = true;
/*      */               break;
/*      */             case HOMESERVER:
/*  343 */               this.current.HOMESERVER = ((Boolean)item.getValue()).booleanValue();
/*  344 */               this.saveNewGui = true;
/*      */               break;
/*      */             case SPAWNPOINTJENNX:
/*  347 */               this.current.SPAWNPOINTJENNX = ((Integer)item.getValue()).intValue();
/*  348 */               this.saveSpawns = true;
/*      */               break;
/*      */             case SPAWNPOINTJENNY:
/*  351 */               this.current.SPAWNPOINTJENNY = ((Integer)item.getValue()).intValue();
/*  352 */               this.saveSpawns = true;
/*      */               break;
/*      */             case SPAWNPOINTMOLX:
/*  355 */               this.current.SPAWNPOINTMOLX = ((Integer)item.getValue()).intValue();
/*  356 */               this.saveSpawns = true;
/*      */               break;
/*      */             case SPAWNPOINTMOLY:
/*  359 */               this.current.SPAWNPOINTMOLY = ((Integer)item.getValue()).intValue();
/*  360 */               this.saveSpawns = true;
/*      */               break;
/*      */             case SPAWNPOINTLIBX:
/*  363 */               this.current.SPAWNPOINTLIBX = ((Integer)item.getValue()).intValue();
/*  364 */               this.saveSpawns = true;
/*      */               break;
/*      */             case SPAWNPOINTLIBY:
/*  367 */               this.current.SPAWNPOINTLIBY = ((Integer)item.getValue()).intValue();
/*  368 */               this.saveSpawns = true;
/*      */               break;
/*      */             case KINGDOM:
/*  371 */               this.current.KINGDOM = ((Byte)item.getValue()).byteValue();
/*  372 */               this.saveNewGui = true;
/*      */               break;
/*      */             case INTRASERVERPASSWORD:
/*  375 */               this.current.INTRASERVERPASSWORD = item.getValue().toString();
/*  376 */               this.saveNewGui = true;
/*      */               break;
/*      */             case TESTSERVER:
/*  379 */               this.current.testServer = ((Boolean)item.getValue()).booleanValue();
/*  380 */               this.saveNewGui = true;
/*      */               break;
/*      */             case NPCS:
/*  383 */               newSetting = ((Boolean)item.getValue()).booleanValue();
/*  384 */               if (newSetting != ServerProperties.getBoolean("NPCS", Constants.loadNpcs)) {
/*      */                 
/*  386 */                 ServerProperties.setValue("NPCS", Boolean.toString(newSetting));
/*  387 */                 ServerProperties.checkProperties();
/*      */               } 
/*      */               break;
/*      */             case ENDGAMEITEMS:
/*  391 */               loadEGI = ((Boolean)item.getValue()).booleanValue();
/*  392 */               if (loadEGI != ServerProperties.getBoolean("ENDGAMEITEMS", Constants.loadEndGameItems)) {
/*      */                 
/*  394 */                 ServerProperties.setValue("ENDGAMEITEMS", Boolean.toString(loadEGI));
/*  395 */                 ServerProperties.checkProperties();
/*      */               } 
/*      */               break;
/*      */             case SPY_PREVENTION:
/*  399 */               spy = ((Boolean)item.getValue()).booleanValue();
/*  400 */               if (spy != ServerProperties.getBoolean("SPYPREVENTION", Constants.enableSpyPrevention)) {
/*      */                 
/*  402 */                 ServerProperties.setValue("SPYPREVENTION", Boolean.toString(spy));
/*  403 */                 ServerProperties.checkProperties();
/*      */               } 
/*      */               break;
/*      */             case NEWBIE_FRIENDLY:
/*  407 */               newbie = ((Boolean)item.getValue()).booleanValue();
/*  408 */               if (newbie != ServerProperties.getBoolean("NEWBIEFRIENDLY", Constants.isNewbieFriendly)) {
/*      */                 
/*  410 */                 ServerProperties.setValue("NEWBIEFRIENDLY", Boolean.toString(newbie));
/*  411 */                 ServerProperties.checkProperties();
/*      */               } 
/*      */               break;
/*      */             case STEAMQUERYPORT:
/*  415 */               sqp = ((Short)item.getValue()).shortValue();
/*  416 */               if (sqp != ServerProperties.getShort("STEAMQUERYPORT", sqp)) {
/*      */                 
/*  418 */                 ServerProperties.setValue("STEAMQUERYPORT", Short.toString(sqp));
/*  419 */                 ServerProperties.checkProperties();
/*      */               } 
/*      */               break;
/*      */             case ADMIN_PWD:
/*  423 */               pwd = (String)item.getValue();
/*  424 */               if (!pwd.equals(ServerProperties.getString("ADMINPASSWORD", pwd))) {
/*      */                 
/*  426 */                 ServerProperties.setValue("ADMINPASSWORD", pwd);
/*  427 */                 ServerProperties.checkProperties();
/*      */               } 
/*      */               break;
/*      */             case NAME:
/*  431 */               this.current.name = item.getValue().toString();
/*  432 */               this.saveNewGui = true;
/*      */               break;
/*      */             case LOGINSERVER:
/*  435 */               this.current.LOGINSERVER = ((Boolean)item.getValue()).booleanValue();
/*  436 */               this.saveNewGui = true;
/*      */               break;
/*      */             case ISPAYMENT:
/*  439 */               this.current.ISPAYMENT = false;
/*  440 */               this.saveNewGui = true;
/*      */               break;
/*      */             case TWITTERCONSUMERKEY:
/*  443 */               this.current.setConsumerKeyToUse(item.getValue().toString());
/*  444 */               this.saveTwitter = true;
/*      */               break;
/*      */             case TWITTERCONSUMERSECRET:
/*  447 */               this.current.setConsumerSecret(item.getValue().toString());
/*  448 */               this.saveTwitter = true;
/*      */               break;
/*      */             case TWITTERAPPTOKEN:
/*  451 */               this.current.setApplicationToken(item.getValue().toString());
/*  452 */               this.saveTwitter = true;
/*      */               break;
/*      */             case TWITTERAPPSECRET:
/*  455 */               this.current.setApplicationSecret(item.getValue().toString());
/*  456 */               this.saveTwitter = true;
/*      */               break;
/*      */             case MAINTAINING:
/*  459 */               this.current.maintaining = ((Boolean)item.getValue()).booleanValue();
/*      */               break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*      */             case MAXCREATURES:
/*  472 */               this.current.maxCreatures = ((Integer)item.getValue()).intValue();
/*  473 */               this.saveNewGui = true;
/*      */               break;
/*      */             case PERCENTAGG:
/*  476 */               this.current.percentAggCreatures = ((Float)item.getValue()).floatValue();
/*  477 */               this.saveNewGui = true;
/*      */               break;
/*      */             case HOTADELAY:
/*  480 */               this.current.setHotaDelay(((Integer)item.getValue()).intValue());
/*  481 */               this.saveNewGui = true;
/*      */               break;
/*      */             case RANDOMSPAWNS:
/*  484 */               this.current.randomSpawns = ((Boolean)item.getValue()).booleanValue();
/*  485 */               this.saveNewGui = true;
/*      */               break;
/*      */             case SKBASIC:
/*  488 */               this.current.setSkillbasicval(((Float)item.getValue()).floatValue());
/*  489 */               this.saveNewGui = true;
/*      */               break;
/*      */             case SKFIGHT:
/*  492 */               this.current.setSkillfightval(((Float)item.getValue()).floatValue());
/*  493 */               this.saveNewGui = true;
/*      */               break;
/*      */             case SKMIND:
/*  496 */               this.current.setSkillmindval(((Float)item.getValue()).floatValue());
/*  497 */               this.saveNewGui = true;
/*      */               break;
/*      */             case SKOVERALL:
/*  500 */               this.current.setSkilloverallval(((Float)item.getValue()).floatValue());
/*  501 */               this.saveNewGui = true;
/*      */               break;
/*      */             case SKBC:
/*  504 */               this.current.setSkillbcval(((Float)item.getValue()).floatValue());
/*  505 */               this.saveNewGui = true;
/*      */               break;
/*      */             case CRMOD:
/*  508 */               this.current.setCombatRatingModifier(((Float)item.getValue()).floatValue());
/*  509 */               this.saveNewGui = true;
/*      */               break;
/*      */             case STEAMPW:
/*  512 */               this.current.setSteamServerPassword(item.getValue().toString());
/*  513 */               this.saveNewGui = true;
/*      */               break;
/*      */             case UPKEEP:
/*  516 */               this.current.setUpkeep(((Boolean)item.getValue()).booleanValue());
/*  517 */               this.saveNewGui = true;
/*      */               break;
/*      */             case MAXDEED:
/*  520 */               this.current.setMaxDeedSize(((Integer)item.getValue()).intValue());
/*  521 */               this.saveNewGui = true;
/*      */               break;
/*      */             case FREEDEEDS:
/*  524 */               this.current.setFreeDeeds(((Boolean)item.getValue()).booleanValue());
/*  525 */               this.saveNewGui = true;
/*      */               break;
/*      */             
/*      */             case TRADERMAX:
/*  529 */               this.current.setTraderMaxIrons(((Integer)item.getValue()).intValue() * 10000);
/*  530 */               this.saveNewGui = true;
/*      */               break;
/*      */             
/*      */             case TRADERINIT:
/*  534 */               this.current.setInitialTraderIrons(((Integer)item.getValue()).intValue() * 10000);
/*  535 */               this.saveNewGui = true;
/*      */               break;
/*      */             case TUNNELING:
/*  538 */               this.current.setTunnelingHits(((Integer)item.getValue()).intValue());
/*  539 */               this.saveNewGui = true;
/*      */               break;
/*      */             case BREEDING:
/*  542 */               this.current.setBreedingTimer(((Long)item.getValue()).longValue());
/*  543 */               this.saveNewGui = true;
/*      */               break;
/*      */             case FIELDGROWTH:
/*  546 */               this.current.setFieldGrowthTime((long)(((Float)item.getValue()).floatValue() * 3600.0F * 1000.0F));
/*  547 */               this.saveNewGui = true;
/*      */               break;
/*      */             case TREEGROWTH:
/*  550 */               this.current.treeGrowth = ((Integer)item.getValue()).intValue();
/*  551 */               this.saveNewGui = true;
/*      */               break;
/*      */             
/*      */             case KINGSMONEY:
/*  555 */               this.current.setKingsmoneyAtRestart(((Integer)item.getValue()).intValue() * 10000);
/*  556 */               this.saveNewGui = true;
/*      */               break;
/*      */             case MOTD:
/*  559 */               this.current.setMotd(item.getValue().toString());
/*  560 */               this.saveNewGui = true;
/*      */               break;
/*      */           } 
/*      */         
/*  564 */         } catch (Exception ex) {
/*      */           
/*  566 */           saveAtAll = false;
/*  567 */           toReturn = toReturn + "Invalid value " + item.getCategory() + ": " + item.getValue() + ". ";
/*  568 */           logger.log(Level.INFO, "Error " + ex.getMessage(), ex);
/*      */         } 
/*      */       } 
/*      */     } 
/*  572 */     if (toReturn.length() == 0)
/*      */     {
/*  574 */       if (saveAtAll) {
/*      */         
/*  576 */         if (this.current.isCreating) {
/*      */           
/*  578 */           toReturn = "New server saved";
/*  579 */           Servers.registerServer(this.current.id, this.current.getName(), this.current.HOMESERVER, this.current.SPAWNPOINTJENNX, this.current.SPAWNPOINTJENNY, this.current.SPAWNPOINTLIBX, this.current.SPAWNPOINTLIBY, this.current.SPAWNPOINTMOLX, this.current.SPAWNPOINTMOLY, this.current.INTRASERVERADDRESS, this.current.INTRASERVERPORT, this.current.INTRASERVERPASSWORD, this.current.EXTERNALIP, this.current.EXTERNALPORT, this.current.LOGINSERVER, this.current.KINGDOM, this.current.ISPAYMENT, this.current
/*      */ 
/*      */ 
/*      */               
/*  583 */               .getConsumerKey(), this.current.getConsumerSecret(), this.current
/*  584 */               .getApplicationToken(), this.current.getApplicationSecret(), false, this.current.testServer, this.current.randomSpawns);
/*      */         }
/*      */         else {
/*      */           
/*  588 */           toReturn = "Properties saved";
/*  589 */         }  if (this.saveNewGui) {
/*      */           
/*  591 */           logger.log(Level.INFO, "Saved using new method.");
/*  592 */           this.saveNewGui = false;
/*  593 */           if (this.changedId) {
/*      */             
/*  595 */             this.current.saveNewGui(this.oldId);
/*  596 */             this.current.movePlayersFromId(this.oldId);
/*  597 */             this.changedId = false;
/*  598 */             Servers.moveServerId(this.current, this.oldId);
/*  599 */             this.oldId = 0;
/*      */           } else {
/*      */             
/*  602 */             this.current.saveNewGui(this.current.id);
/*      */           } 
/*  604 */         }  if (this.saveTwitter && !this.current.isCreating) {
/*      */           
/*  606 */           if (this.current.saveTwitter()) {
/*  607 */             logger.log(Level.INFO, "Saved twitter settings. The server will attempt to tweet.");
/*      */           } else {
/*  609 */             logger.log(Level.INFO, "Saved twitter settings. The server will not tweet.");
/*  610 */           }  this.saveTwitter = false;
/*      */         } 
/*  612 */         if (this.saveSpawns) {
/*      */           
/*  614 */           this.current.updateSpawns();
/*  615 */           logger.log(Level.INFO, "Saved new spawn points.");
/*  616 */           this.saveSpawns = false;
/*      */         } 
/*  618 */         saveAtAll = false;
/*  619 */         this.changedProperties.clear();
/*  620 */         this.current.isCreating = false;
/*      */       } 
/*      */     }
/*      */     
/*  624 */     return toReturn;
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
/*      */   class ServerPropertySheetChangeListener<CustomPropertyItem>
/*      */     implements ListChangeListener
/*      */   {
/*      */     public void onChanged(ListChangeListener.Change c) {
/*  642 */       System.out.println("Change occurred: " + c);
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
/*      */   public static final String generateRandomPassword() {
/*  656 */     Random rand = new Random();
/*      */     
/*  658 */     int length = rand.nextInt(3) + 6;
/*  659 */     char[] password = new char[length];
/*      */     
/*  661 */     for (int x = 0; x < length; x++) {
/*      */       
/*  663 */       int randDecimalAsciiVal = rand.nextInt("abcdefgijkmnopqrstwxyzABCDEFGHJKLMNPQRSTWXYZ23456789".length());
/*  664 */       password[x] = "abcdefgijkmnopqrstwxyzABCDEFGHJKLMNPQRSTWXYZ23456789".charAt(randDecimalAsciiVal);
/*      */     } 
/*      */     
/*  667 */     return String.valueOf(password);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final short getNewServerId() {
/*  672 */     Random random = new Random();
/*  673 */     short newRand = 0;
/*  674 */     HashSet<Integer> usedNumbers = new HashSet<>();
/*  675 */     for (ServerEntry entry : Servers.getAllServers())
/*      */     {
/*  677 */       usedNumbers.add(Integer.valueOf(entry.id));
/*      */     }
/*  679 */     int tries = 0;
/*  680 */     int max = 30000;
/*  681 */     usedNumbers.add(Integer.valueOf(0));
/*  682 */     while (usedNumbers.contains(Integer.valueOf(newRand)) && tries < max) {
/*      */       
/*  684 */       newRand = (short)random.nextInt(32767);
/*  685 */       if (!usedNumbers.contains(Integer.valueOf(newRand)))
/*      */         break; 
/*  687 */       tries++;
/*      */     } 
/*  689 */     return newRand;
/*      */   }
/*      */ 
/*      */   
/*      */   public ServerPropertySheet(ServerEntry entry) {
/*  694 */     this.current = entry;
/*  695 */     this.list = FXCollections.observableArrayList();
/*      */     
/*  697 */     if (entry == null)
/*      */       return; 
/*  699 */     if (entry.isLocal) {
/*      */       
/*  701 */       initializeLocalServer(entry);
/*      */     }
/*      */     else {
/*      */       
/*  705 */       initializeNonLocalServer(entry);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void initializeLocalServer(ServerEntry entry) {
/*  714 */     this.saveNewGui = false;
/*  715 */     this.saveSpawns = false;
/*  716 */     this.saveTwitter = false;
/*  717 */     this.list.add(new CustomPropertyItem(PropertyType.NAME, "1: Server Settings", "Server Name", "Name", true, entry.name));
/*      */     
/*  719 */     if (entry.id == 0) {
/*      */       
/*  721 */       this.changedId = true;
/*  722 */       this.oldId = 0;
/*  723 */       entry.id = getNewServerId();
/*  724 */       this.list.add(new CustomPropertyItem(PropertyType.SERVERID, "2: Advance Server Settings", "Server ID", "The unique ID in the cluster", true, 
/*  725 */             Integer.valueOf(entry.id)));
/*  726 */       this.changedProperties.add(PropertyType.SERVERID);
/*  727 */       this.saveNewGui = true;
/*      */     } else {
/*      */       
/*  730 */       this.list.add(new CustomPropertyItem(PropertyType.SERVERID, "2: Advance Server Settings", "Server ID", "The unique ID in the cluster", true, 
/*  731 */             Integer.valueOf(entry.id)));
/*      */     } 
/*      */ 
/*      */     
/*  735 */     this.list.add(new CustomPropertyItem(PropertyType.ENABLE_PNP_PORT_FORWARD, "1: Server Settings", "Auto port-forward", "Uses PNP to set up port-forwarding on your router", true, Boolean.valueOf(ServerProperties.getBoolean("ENABLE_PNP_PORT_FORWARD", Constants.enablePnpPortForward))));
/*  736 */     if (entry.EXTERNALIP == null || entry.EXTERNALIP.equals(""))
/*      */     {
/*  738 */       if (entry.isLocal) {
/*      */         
/*      */         try {
/*      */           
/*  742 */           entry.EXTERNALIP = InetAddress.getLocalHost().getHostAddress();
/*  743 */           this.changedProperties.add(PropertyType.EXTSERVERIP);
/*  744 */           this.saveNewGui = true;
/*      */         }
/*  746 */         catch (Exception ex) {
/*      */           
/*  748 */           logger.log(Level.INFO, ex.getMessage());
/*      */         } 
/*      */       }
/*      */     }
/*  752 */     this.list.add(new CustomPropertyItem(PropertyType.EXTSERVERIP, "1: Server Settings", "Server External IP Address", "IP Address", true, entry.EXTERNALIP));
/*      */     
/*  754 */     this.list.add(new CustomPropertyItem(PropertyType.EXTSERVERPORT, "1: Server Settings", "Server External IP Port", "IP Port number", true, entry.EXTERNALPORT));
/*      */     
/*  756 */     if ((entry.INTRASERVERADDRESS == null || entry.INTRASERVERADDRESS.equals("")) && entry.isLocal)
/*      */       
/*      */       try {
/*  759 */         entry.INTRASERVERADDRESS = InetAddress.getLoopbackAddress().getHostAddress();
/*  760 */         this.changedProperties.add(PropertyType.INTIP);
/*  761 */         this.saveNewGui = true;
/*      */       }
/*  763 */       catch (Exception ex) {
/*      */         
/*  765 */         logger.log(Level.INFO, ex.getMessage());
/*      */       }  
/*  767 */     this.list.add(new CustomPropertyItem(PropertyType.INTIP, "2: Advance Server Settings", "Server Internal IP Address", "IP Address", true, entry.INTRASERVERADDRESS));
/*      */     
/*  769 */     this.list.add(new CustomPropertyItem(PropertyType.INTPORT, "2: Advance Server Settings", "Server Internal IP Port", "IP Port number", true, entry.INTRASERVERPORT));
/*      */     
/*  771 */     this.list.add(new CustomPropertyItem(PropertyType.RMI_REG_PORT, "2: Advance Server Settings", "RMI Registration Port", "IP Port number", true, 
/*  772 */           Integer.valueOf(entry.REGISTRATION_PORT)));
/*  773 */     this.list.add(new CustomPropertyItem(PropertyType.RMIPORT, "2: Advance Server Settings", "RMI Port", "IP Port number", true, 
/*  774 */           Integer.valueOf(entry.RMI_PORT)));
/*  775 */     this.list.add(new CustomPropertyItem(PropertyType.STEAMQUERYPORT, "2: Advance Server Settings", "Steam Query Port", "A port Steam uses for queries about connections. Standard is 27016", true, 
/*  776 */           Short.valueOf(ServerProperties.getShort("STEAMQUERYPORT", SteamHandler.steamQueryPort))));
/*      */     
/*  778 */     if ((entry.INTRASERVERPASSWORD == null || entry.INTRASERVERPASSWORD.equals("")) && entry.isLocal) {
/*      */       
/*  780 */       entry.INTRASERVERPASSWORD = generateRandomPassword();
/*  781 */       this.changedProperties.add(PropertyType.INTRASERVERPASSWORD);
/*  782 */       this.saveNewGui = true;
/*      */     } 
/*  784 */     this.list.add(new CustomPropertyItem(PropertyType.INTRASERVERPASSWORD, "2: Advance Server Settings", "Intra server password", "Server Cross-Communication password. Used for connecting servers to eachother.", true, entry.INTRASERVERPASSWORD));
/*      */     
/*  786 */     this.list.add(new CustomPropertyItem(PropertyType.STEAMPW, "1: Server Settings", "Server password", "Server Password. If set, players need to provide this in order to connect to your server", true, entry
/*  787 */           .getSteamServerPassword()));
/*  788 */     this.list.add(new CustomPropertyItem(PropertyType.MAXPLAYERS, "1: Server Settings", "Maximum number of players", "Maximum number of players on this server", true, 
/*  789 */           Integer.valueOf(entry.pLimit)));
/*  790 */     this.list.add(new CustomPropertyItem(PropertyType.PVPSERVER, "1: Server Settings", "Allow PvP", "Allowing Player Versus Player combat and theft", true, 
/*  791 */           Boolean.valueOf(entry.PVPSERVER)));
/*  792 */     this.list.add(new CustomPropertyItem(PropertyType.EPIC, "1: Server Settings", "Epic settings", "Faster skillgain, missions affect Valrei", true, 
/*  793 */           Boolean.valueOf(entry.EPIC)));
/*  794 */     this.list.add(new CustomPropertyItem(PropertyType.HOMESERVER, "1: Server Settings", "Home Server", "A Home server can only have settlements from one kingdom", true, 
/*      */           
/*  796 */           Boolean.valueOf(entry.HOMESERVER)));
/*  797 */     this.list.add(new CustomPropertyItem(PropertyType.KINGDOM, "1: Server Settings", "Home Server Kingdom", "Which kingdom the Home server should have, 0 = No kingdom, 1 = Jenn-Kellon, 2 = Mol Rehan, 3 = Horde of the Summoned, 4 = Freedom Isles", true, 
/*  798 */           Byte.valueOf(entry.KINGDOM)));
/*  799 */     this.list.add(new CustomPropertyItem(PropertyType.LOGINSERVER, "2: Advance Server Settings", "Login Server", "The login server is the central cluster node responsible for bank accounts and cross communication", true, 
/*  800 */           Boolean.valueOf(entry.LOGINSERVER)));
/*      */ 
/*      */     
/*  803 */     this.list.add(new CustomPropertyItem(PropertyType.TESTSERVER, "2: Advance Server Settings", "Test Server", "Some special settings and debug options", true, 
/*  804 */           Boolean.valueOf(entry.testServer)));
/*  805 */     this.list.add(new CustomPropertyItem(PropertyType.NPCS, "2: Advance Server Settings", "Npcs", "Whether npcs should be loaded", true, 
/*  806 */           Boolean.valueOf(ServerProperties.getBoolean("NPCS", Constants.loadNpcs))));
/*  807 */     this.list.add(new CustomPropertyItem(PropertyType.ENDGAMEITEMS, "2: Advance Server Settings", "End Game Items", "Whether artifacts and huge altars should be loaded", true, 
/*  808 */           Boolean.valueOf(ServerProperties.getBoolean("ENDGAMEITEMS", Constants.loadEndGameItems))));
/*  809 */     this.list.add(new CustomPropertyItem(PropertyType.SPY_PREVENTION, "2: Advance Server Settings", "PVP Spy Prevention", "Prevents multiple IPs from different kingdoms from logging in at the same time.", true, 
/*  810 */           Boolean.valueOf(ServerProperties.getBoolean("SPYPREVENTION", Constants.enableSpyPrevention))));
/*  811 */     this.list.add(new CustomPropertyItem(PropertyType.NEWBIE_FRIENDLY, "2: Advance Server Settings", "Newbie Friendly", "Prevents harder creatures from spawning.", true, 
/*  812 */           Boolean.valueOf(ServerProperties.getBoolean("NEWBIEFRIENDLY", Constants.isNewbieFriendly))));
/*      */     
/*  814 */     this.list.add(new CustomPropertyItem(PropertyType.RANDOMSPAWNS, "2: Advance Server Settings", "Random spawn points", "Enable random spawn points for new players", true, 
/*  815 */           Boolean.valueOf(entry.randomSpawns)));
/*  816 */     this.list.add(new CustomPropertyItem(PropertyType.SPAWNPOINTJENNX, "2: Advance Server Settings", "Spawnpoint x", "Where players generally spawn, tile x", true, 
/*  817 */           Integer.valueOf(entry.SPAWNPOINTJENNX)));
/*  818 */     this.list.add(new CustomPropertyItem(PropertyType.SPAWNPOINTJENNY, "2: Advance Server Settings", "Spawnpoint y", "Where players generally spawn, tile y", true, 
/*  819 */           Integer.valueOf(entry.SPAWNPOINTJENNY)));
/*  820 */     this.list.add(new CustomPropertyItem(PropertyType.SPAWNPOINTMOLX, "2: Advance Server Settings", "Kingdom 2 Spawnpoint x", "Where kingdom 2 players spawn, tile x", true, 
/*  821 */           Integer.valueOf(entry.SPAWNPOINTMOLX)));
/*  822 */     this.list.add(new CustomPropertyItem(PropertyType.SPAWNPOINTMOLY, "2: Advance Server Settings", "Kingdom 2 Spawnpoint y", "Where kingdom 2 players spawn, tile y", true, 
/*  823 */           Integer.valueOf(entry.SPAWNPOINTMOLY)));
/*  824 */     this.list.add(new CustomPropertyItem(PropertyType.SPAWNPOINTLIBX, "2: Advance Server Settings", "Kingdom 3 Spawnpoint x", "Where kingdom 3 players spawn, tile x", true, 
/*  825 */           Integer.valueOf(entry.SPAWNPOINTLIBX)));
/*  826 */     this.list.add(new CustomPropertyItem(PropertyType.SPAWNPOINTLIBY, "2: Advance Server Settings", "Kingdom 3 Spawnpoint y", "Where kingdom 3 players spawn, tile y", true, 
/*  827 */           Integer.valueOf(entry.SPAWNPOINTLIBY)));
/*  828 */     this.list.add(new CustomPropertyItem(PropertyType.MOTD, "1: Server Settings", "Message of the day", "A message to display upon login", true, entry
/*  829 */           .getMotd()));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  834 */     this.list.add(new CustomPropertyItem(PropertyType.TWITTERCONSUMERKEY, "4: Twitter Settings", "Consumer key", "Consumer key", true, entry
/*  835 */           .getConsumerKey()));
/*  836 */     this.list.add(new CustomPropertyItem(PropertyType.TWITTERCONSUMERSECRET, "4: Twitter Settings", "Consumer secret", "Consumer secret", true, entry
/*  837 */           .getConsumerSecret()));
/*  838 */     this.list.add(new CustomPropertyItem(PropertyType.TWITTERAPPTOKEN, "4: Twitter Settings", "Application token", "Application token", true, entry
/*  839 */           .getApplicationToken()));
/*  840 */     this.list.add(new CustomPropertyItem(PropertyType.TWITTERAPPSECRET, "4: Twitter Settings", "Application secret", "Application secret", true, entry
/*  841 */           .getApplicationSecret()));
/*      */     
/*  843 */     this.list.add(new CustomPropertyItem(PropertyType.SKILLGAINRATE, "3: Gameplay Tweaks", "Skill gain rate multiplier", "Multiplies the server skill gain rate. Higher means faster skill gain. It's not exact depending on a number of factors.", true, 
/*      */           
/*  845 */           Float.valueOf(this.current.getSkillGainRate()), Float.valueOf(0.01F)));
/*  846 */     this.list.add(new CustomPropertyItem(PropertyType.SKBASIC, "3: Gameplay Tweaks", "Characteristics start value", "Start value of Characteristics such as body strength, stamina and soul depth", true, 
/*  847 */           Float.valueOf(this.current
/*  848 */             .getSkillbasicval()), Float.valueOf(1.0F), Float.valueOf(100.0F)));
/*  849 */     this.list.add(new CustomPropertyItem(PropertyType.SKMIND, "3: Gameplay Tweaks", "Mind Logic skill start value", "Start value of Mind Logic Characteristic (used for controlling vehicles)", true, 
/*  850 */           Float.valueOf(this.current
/*  851 */             .getSkillmindval()), Float.valueOf(1.0F), Float.valueOf(100.0F)));
/*  852 */     this.list.add(new CustomPropertyItem(PropertyType.SKBC, "3: Gameplay Tweaks", "Body Control skill start value", "Start value of Mind Logic Characteristic (used for controlling mounts)", true, 
/*  853 */           Float.valueOf(this.current
/*  854 */             .getSkillbcval()), Float.valueOf(1.0F), Float.valueOf(100.0F)));
/*  855 */     this.list.add(new CustomPropertyItem(PropertyType.SKFIGHT, "3: Gameplay Tweaks", "Fight skill start value", "Affects start value of the overall fighting skill", true, 
/*  856 */           Float.valueOf(this.current.getSkillfightval()), Float.valueOf(1.0F), Float.valueOf(100.0F)));
/*  857 */     this.list.add(new CustomPropertyItem(PropertyType.SKOVERALL, "3: Gameplay Tweaks", "Overall skill start value", "Start value of all other skills", true, 
/*  858 */           Float.valueOf(this.current.getSkilloverallval()), Float.valueOf(1.0F), Float.valueOf(100.0F)));
/*  859 */     this.list.add(new CustomPropertyItem(PropertyType.CRMOD, "3: Gameplay Tweaks", "Player combat rating modifier", "Modifies player combat power versus creatures", true, 
/*  860 */           Float.valueOf(this.current.getCombatRatingModifier())));
/*  861 */     this.list.add(new CustomPropertyItem(PropertyType.ACTIONTIMER, "3: Gameplay Tweaks", "Action speed multiplier", "Divides the max standard time an action takes. Higher makes actions faster.", true, 
/*  862 */           Float.valueOf(this.current.getActionTimer()), Float.valueOf(0.01F)));
/*  863 */     this.list.add(new CustomPropertyItem(PropertyType.HOTADELAY, "3: Gameplay Tweaks", "Hota Delay", "The time in minutes between Hunt Of The Ancients rounds", true, 
/*  864 */           Integer.valueOf(this.current.getHotaDelay())));
/*  865 */     this.list.add(new CustomPropertyItem(PropertyType.MAXCREATURES, "3: Gameplay Tweaks", "Max Creatures", "Max creatures", true, 
/*  866 */           Integer.valueOf(this.current.maxCreatures)));
/*  867 */     this.list.add(new CustomPropertyItem(PropertyType.PERCENTAGG, "3: Gameplay Tweaks", "Aggressive Creatures, %", "Approximate max number of aggressive creatures in per cent of all creatures", true, 
/*      */           
/*  869 */           Float.valueOf(this.current.percentAggCreatures), Float.valueOf(0.0F)));
/*  870 */     this.list.add(new CustomPropertyItem(PropertyType.UPKEEP, "3: Gameplay Tweaks", "Settlement upkeep enabled", "If settlements require upkeep money", true, 
/*  871 */           Boolean.valueOf(this.current.isUpkeep())));
/*      */ 
/*      */     
/*  874 */     this.list.add(new CustomPropertyItem(PropertyType.FREEDEEDS, "3: Gameplay Tweaks", "No deeding costs", "If deeding is free and costs no money", true, 
/*  875 */           Boolean.valueOf(this.current.isFreeDeeds())));
/*      */     
/*  877 */     this.list.add(new CustomPropertyItem(PropertyType.TRADERMAX, "3: Gameplay Tweaks", "Trader max money in silver", "The max amount of money a trader will receive from the pool", true, 
/*  878 */           Integer.valueOf(this.current.getTraderMaxIrons() / 10000)));
/*      */     
/*  880 */     this.list.add(new CustomPropertyItem(PropertyType.TRADERINIT, "3: Gameplay Tweaks", "Trader initial money in silver", "The initial amount of money a trader will receive from the pool", true, 
/*  881 */           Integer.valueOf(this.current.getInitialTraderIrons() / 10000)));
/*  882 */     this.list.add(new CustomPropertyItem(PropertyType.TUNNELING, "3: Gameplay Tweaks", "Minimum mining hits required", "The minimum number of times you need to mine before a wall disappears", true, 
/*  883 */           Integer.valueOf(this.current.getTunnelingHits())));
/*  884 */     this.list.add(new CustomPropertyItem(PropertyType.BREEDING, "3: Gameplay Tweaks", "Breeding time modifier", "A modifier which makes breeding faster the higher it is.", true, 
/*  885 */           Long.valueOf(this.current.getBreedingTimer())));
/*  886 */     this.list.add(new CustomPropertyItem(PropertyType.FIELDGROWTH, "3: Gameplay Tweaks", "Field growth timer, hour", "The number hours between field growth checks", true, 
/*  887 */           Float.valueOf((float)this.current.getFieldGrowthTime() / 1000.0F / 3600.0F), Float.valueOf(0.01F)));
/*  888 */     this.list.add(new CustomPropertyItem(PropertyType.TREEGROWTH, "3: Gameplay Tweaks", "Tree spread odds", "Odds of new trees or mushrooms appearing, as determined by a 1 in <Tree Growth> chance. When set to 0, tree growth is prevented.", true, 
/*  889 */           Integer.valueOf(this.current.treeGrowth), Integer.valueOf(0)));
/*      */     
/*  891 */     this.list.add(new CustomPropertyItem(PropertyType.KINGSMONEY, "3: Gameplay Tweaks", "Money pool in silver", "This is the amount of money that will be in the money pool after server restart.", true, 
/*  892 */           Integer.valueOf(this.current.getKingsmoneyAtRestart() / 10000)));
/*      */     
/*  894 */     this.list.add(new CustomPropertyItem(PropertyType.MAINTAINING, "5: Maintenance", "Maintenance", "Start in maintenance mode", true, 
/*  895 */           Boolean.valueOf(false)));
/*      */     
/*  897 */     this.list.add(new CustomPropertyItem(PropertyType.ADMIN_PWD, "1: Server Settings", "Admin Password", "Password used to unlock the ability to change game tweaks from within the game.", true, 
/*  898 */           ServerProperties.getString("ADMINPASSWORD", "")));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  903 */     if (this.saveNewGui) {
/*      */       
/*  905 */       this.saveNewGui = false;
/*  906 */       if (entry.isCreating) {
/*  907 */         save();
/*  908 */       } else if (this.changedId) {
/*      */         
/*  910 */         this.current.saveNewGui(this.oldId);
/*  911 */         this.current.movePlayersFromId(this.oldId);
/*  912 */         this.changedId = false;
/*  913 */         Servers.moveServerId(this.current, this.oldId);
/*  914 */         this.oldId = 0;
/*      */       } else {
/*      */         
/*  917 */         entry.saveNewGui(entry.id);
/*  918 */       }  this.changedProperties.clear();
/*  919 */       System.out.println("Saved new server");
/*      */     } 
/*  921 */     final SimpleObjectProperty<Callback<PropertySheet.Item, PropertyEditor<?>>> propertyEditorFactory = new SimpleObjectProperty(this, "propertyEditor", new DefaultPropertyEditorFactory());
/*  922 */     this.propertySheet = new PropertySheet(this.list);
/*  923 */     this.propertySheet.setPropertyEditorFactory(new Callback<PropertySheet.Item, PropertyEditor<?>>()
/*      */         {
/*      */ 
/*      */           
/*      */           public PropertyEditor<?> call(PropertySheet.Item param)
/*      */           {
/*  929 */             if (param instanceof ServerPropertySheet.CustomPropertyItem) {
/*      */               
/*  931 */               ServerPropertySheet.CustomPropertyItem pi = (ServerPropertySheet.CustomPropertyItem)param;
/*  932 */               if (pi.type == ServerPropertySheet.PropertyType.ACTIONTIMER || pi
/*  933 */                 .getValue().getClass() == Float.class)
/*      */               {
/*      */                 
/*  936 */                 return new FormattedFloatEditor(param);
/*      */               }
/*      */             } 
/*  939 */             if (propertyEditorFactory.get() == null)
/*  940 */               throw new NullPointerException("Null!"); 
/*  941 */             return (PropertyEditor)((Callback)propertyEditorFactory.get()).call(param);
/*      */           }
/*      */         });
/*  944 */     this.propertySheet.setMode(PropertySheet.Mode.CATEGORY);
/*  945 */     VBox.setVgrow((Node)this.propertySheet, Priority.ALWAYS);
/*  946 */     getChildren().add(this.propertySheet);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void initializeNonLocalServer(ServerEntry entry) {
/*  956 */     this.saveNewGui = false;
/*  957 */     this.saveSpawns = false;
/*  958 */     this.saveTwitter = false;
/*  959 */     this.list.add(new CustomPropertyItem(PropertyType.NAME, "1: Server Settings", "Server Name", "Name", true, entry.name));
/*      */     
/*  961 */     if (entry.id == 0) {
/*      */       
/*  963 */       this.changedId = true;
/*  964 */       this.oldId = 0;
/*  965 */       entry.id = getNewServerId();
/*  966 */       this.list.add(new CustomPropertyItem(PropertyType.SERVERID, "1: Server Settings", "Server ID", "The unique ID in the cluster", true, 
/*  967 */             Integer.valueOf(entry.id)));
/*  968 */       this.changedProperties.add(PropertyType.SERVERID);
/*  969 */       this.saveNewGui = true;
/*      */     } else {
/*      */       
/*  972 */       this.list.add(new CustomPropertyItem(PropertyType.SERVERID, "1: Server Settings", "Server ID", "The unique ID in the cluster", true, 
/*  973 */             Integer.valueOf(entry.id)));
/*  974 */     }  this.list.add(new CustomPropertyItem(PropertyType.ENABLE_PNP_PORT_FORWARD, "1: Server Settings", "Auto port-forward", "Uses PNP to set up port-forwarding on your router", true, Boolean.valueOf(ServerProperties.getBoolean("ENABLE_PNP_PORT_FORWARD", Constants.enablePnpPortForward))));
/*  975 */     if (entry.EXTERNALIP == null || entry.EXTERNALIP.equals(""))
/*      */     {
/*  977 */       if (entry.isLocal) {
/*      */         
/*      */         try
/*      */         {
/*  981 */           entry.EXTERNALIP = InetAddress.getLocalHost().getHostAddress();
/*  982 */           this.changedProperties.add(PropertyType.EXTSERVERIP);
/*  983 */           this.saveNewGui = true;
/*      */         }
/*  985 */         catch (Exception ex)
/*      */         {
/*  987 */           logger.log(Level.INFO, ex.getMessage());
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/*  992 */         entry.EXTERNALIP = ((InetAddress)Objects.<InetAddress>requireNonNull(AddressHelper.getFirstValidAddress())).getHostAddress();
/*  993 */         this.changedProperties.add(PropertyType.EXTSERVERIP);
/*  994 */         this.saveNewGui = true;
/*      */       } 
/*      */     }
/*  997 */     this.list.add(new CustomPropertyItem(PropertyType.EXTSERVERIP, "1: Server Settings", "Server External IP Address", "IP Address", true, entry.EXTERNALIP));
/*      */     
/*  999 */     this.list.add(new CustomPropertyItem(PropertyType.EXTSERVERPORT, "1: Server Settings", "Server External IP Port", "IP Port number", true, entry.EXTERNALPORT));
/*      */     
/* 1001 */     if ((entry.INTRASERVERADDRESS == null || entry.INTRASERVERADDRESS.equals("")) && entry.isLocal)
/*      */       
/*      */       try {
/* 1004 */         entry.INTRASERVERADDRESS = InetAddress.getLocalHost().getHostAddress();
/* 1005 */         this.changedProperties.add(PropertyType.INTIP);
/* 1006 */         this.saveNewGui = true;
/*      */       }
/* 1008 */       catch (Exception ex) {
/*      */         
/* 1010 */         logger.log(Level.INFO, ex.getMessage());
/*      */       }  
/* 1012 */     this.list.add(new CustomPropertyItem(PropertyType.INTIP, "1: Server Settings", "Server Internal IP Address", "IP Address", true, entry.INTRASERVERADDRESS));
/*      */     
/* 1014 */     this.list.add(new CustomPropertyItem(PropertyType.INTPORT, "1: Server Settings", "Server Internal IP Port", "IP Port number", true, entry.INTRASERVERPORT));
/*      */     
/* 1016 */     this.list.add(new CustomPropertyItem(PropertyType.RMI_REG_PORT, "1: Server Settings", "RMI Registration Port", "IP Port number", true, 
/* 1017 */           Integer.valueOf(entry.REGISTRATION_PORT)));
/* 1018 */     this.list.add(new CustomPropertyItem(PropertyType.RMIPORT, "1: Server Settings", "RMI Port", "IP Port number", true, 
/* 1019 */           Integer.valueOf(entry.RMI_PORT)));
/*      */     
/* 1021 */     if ((entry.INTRASERVERPASSWORD == null || entry.INTRASERVERPASSWORD.equals("")) && entry.isLocal) {
/*      */       
/* 1023 */       entry.INTRASERVERPASSWORD = generateRandomPassword();
/* 1024 */       this.changedProperties.add(PropertyType.INTRASERVERPASSWORD);
/* 1025 */       this.saveNewGui = true;
/*      */     } 
/* 1027 */     this.list.add(new CustomPropertyItem(PropertyType.INTRASERVERPASSWORD, "1: Server Settings", "Intra server password", "Server Cross-Communication password. Used for connecting servers to eachother.", true, entry.INTRASERVERPASSWORD));
/*      */ 
/*      */     
/* 1030 */     this.list.add(new CustomPropertyItem(PropertyType.LOGINSERVER, "1: Server Settings", "Login Server", "The login server is the central cluster node responsible for bank accounts and cross communication", true, 
/* 1031 */           Boolean.valueOf(entry.LOGINSERVER)));
/* 1032 */     this.list.add(new CustomPropertyItem(PropertyType.PVPSERVER, "1: Server Settings", "Allows PvP", "Allowing Player Versus Player combat and theft", true, 
/* 1033 */           Boolean.valueOf(entry.PVPSERVER)));
/* 1034 */     this.list.add(new CustomPropertyItem(PropertyType.HOMESERVER, "1: Server Settings", "Home Server", "A Home server can only have settlements from one kingdom", true, 
/*      */           
/* 1036 */           Boolean.valueOf(entry.HOMESERVER)));
/* 1037 */     this.list.add(new CustomPropertyItem(PropertyType.KINGDOM, "1: Server Settings", "Home Server Kingdom", "Which kingdom the Home server should have, 0 = No kingdom, 1 = Jenn-Kellon, 2 = Mol Rehan, 3 = Horde of the Summoned, 4 = Freedom Isles", true, 
/* 1038 */           Byte.valueOf(entry.KINGDOM)));
/*      */     
/* 1040 */     if (this.saveNewGui) {
/*      */       
/* 1042 */       this.saveNewGui = false;
/* 1043 */       if (entry.isCreating) {
/* 1044 */         save();
/* 1045 */       } else if (this.changedId) {
/*      */         
/* 1047 */         this.current.saveNewGui(this.oldId);
/* 1048 */         this.current.movePlayersFromId(this.oldId);
/* 1049 */         this.changedId = false;
/* 1050 */         Servers.moveServerId(this.current, this.oldId);
/* 1051 */         this.oldId = 0;
/*      */       } else {
/*      */         
/* 1054 */         entry.saveNewGui(entry.id);
/* 1055 */       }  this.changedProperties.clear();
/* 1056 */       System.out.println("Saved new server");
/*      */     } 
/* 1058 */     this.propertySheet = new PropertySheet(this.list);
/* 1059 */     this.propertySheet.setMode(PropertySheet.Mode.CATEGORY);
/* 1060 */     VBox.setVgrow((Node)this.propertySheet, Priority.ALWAYS);
/* 1061 */     getChildren().add(this.propertySheet);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setReadOnly() {
/* 1066 */     if (this.propertySheet != null) {
/*      */       
/* 1068 */       this.propertySheet.setMode(PropertySheet.Mode.NAME);
/* 1069 */       this.propertySheet.setDisable(true);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean haveChanges() {
/* 1075 */     if (this.changedProperties.isEmpty())
/*      */     {
/* 1077 */       return false;
/*      */     }
/*      */     
/* 1080 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void AskIfSave() {
/* 1085 */     Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
/* 1086 */     alert.setTitle("Unsaved changes!");
/* 1087 */     alert.setHeaderText("There are unsaved changes in the local server tab");
/* 1088 */     alert.setContentText("Do you want to save the changes?");
/*      */     
/* 1090 */     Optional<ButtonType> result = alert.showAndWait();
/* 1091 */     if (result.get() == ButtonType.OK)
/*      */     {
/* 1093 */       save();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public final ServerEntry getCurrentServerEntry() {
/* 1099 */     return this.current;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\gui\propertysheet\ServerPropertySheet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */