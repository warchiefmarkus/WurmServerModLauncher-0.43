/*      */ package com.wurmonline.server.questions;
/*      */ 
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.LoginHandler;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.WurmId;
/*      */ import com.wurmonline.server.behaviours.MethodsCreatures;
/*      */ import com.wurmonline.server.behaviours.Vehicle;
/*      */ import com.wurmonline.server.behaviours.Vehicles;
/*      */ import com.wurmonline.server.creatures.AnimalSettings;
/*      */ import com.wurmonline.server.creatures.Brand;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.Creatures;
/*      */ import com.wurmonline.server.creatures.DbCreatureStatus;
/*      */ import com.wurmonline.server.creatures.Delivery;
/*      */ import com.wurmonline.server.creatures.MineDoorPermission;
/*      */ import com.wurmonline.server.creatures.MineDoorSettings;
/*      */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*      */ import com.wurmonline.server.creatures.Wagoner;
/*      */ import com.wurmonline.server.endgames.EndGameItems;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.ItemSettings;
/*      */ import com.wurmonline.server.players.Permissions;
/*      */ import com.wurmonline.server.players.PermissionsPlayerList;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.players.PlayerInfoFactory;
/*      */ import com.wurmonline.server.structures.Door;
/*      */ import com.wurmonline.server.structures.DoorSettings;
/*      */ import com.wurmonline.server.structures.FenceGate;
/*      */ import com.wurmonline.server.structures.NoSuchStructureException;
/*      */ import com.wurmonline.server.structures.Structure;
/*      */ import com.wurmonline.server.structures.StructureSettings;
/*      */ import com.wurmonline.server.structures.Structures;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import java.util.Arrays;
/*      */ import java.util.Comparator;
/*      */ import java.util.HashSet;
/*      */ import java.util.Properties;
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
/*      */ public class ManageObjectList
/*      */   extends Question
/*      */ {
/*      */   public enum Type
/*      */   {
/*   66 */     ANIMAL0("Animal", AnimalSettings.Animal0Permissions.values()),
/*   67 */     ANIMAL1("Animal", AnimalSettings.Animal1Permissions.values()),
/*   68 */     ANIMAL2("Animal", AnimalSettings.Animal2Permissions.values()),
/*   69 */     WAGONER("Wagoner", AnimalSettings.WagonerPermissions.values()),
/*   70 */     DELIVERY("Wagoner", AnimalSettings.WagonerPermissions.values()),
/*   71 */     BUILDING("Building", StructureSettings.StructurePermissions.values()),
/*   72 */     LARGE_CART("Large Cart", ItemSettings.VehiclePermissions.values()),
/*   73 */     DOOR("Door", DoorSettings.DoorPermissions.values()),
/*   74 */     GATE("Gate", DoorSettings.GatePermissions.values()),
/*   75 */     MINEDOOR("Minedoor", MineDoorSettings.MinedoorPermissions.values()),
/*   76 */     SHIP("Ship", ItemSettings.VehiclePermissions.values()),
/*   77 */     WAGON("Wagon", ItemSettings.WagonPermissions.values()),
/*   78 */     SHIP_CARRIER("Ship Carrier", ItemSettings.ShipTransporterPermissions.values()),
/*   79 */     CREATURE_CARRIER("Creature Carrier", ItemSettings.CreatureTransporterPermissions.values()),
/*   80 */     SMALL_CART("Small Cart", ItemSettings.SmallCartPermissions.values()),
/*   81 */     ITEM("Item", ItemSettings.ItemPermissions.values()),
/*   82 */     BED("Bed", ItemSettings.BedPermissions.values()),
/*   83 */     MESSAGE_BOARD("Village Message Board", ItemSettings.MessageBoardPermissions.values()),
/*   84 */     CORPSE("Corpse", ItemSettings.CorpsePermissions.values()),
/*   85 */     SEARCH("Search", null),
/*   86 */     REPLY("Reply", null);
/*      */     
/*      */     private final String title;
/*      */     
/*      */     private final Permissions.IPermission[] enumValues;
/*      */     
/*      */     Type(String aTitle, Permissions.IPermission[] values) {
/*   93 */       this.title = aTitle;
/*   94 */       this.enumValues = values;
/*      */     }
/*      */ 
/*      */     
/*      */     public String getTitle() {
/*   99 */       return this.title;
/*      */     }
/*      */ 
/*      */     
/*      */     public Permissions.IPermission[] getEnumValues() {
/*  104 */       return this.enumValues;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*  109 */   private static final Logger logger = Logger.getLogger(ManageObjectList.class.getName());
/*      */   
/*      */   private static final String red = "color=\"255,127,127\"";
/*      */   private static final String green = "color=\"127,255,127\"";
/*      */   private static final String orange = "color=\"255,177,40\"";
/*      */   private final Player player;
/*      */   private final Type objectType;
/*      */   private final boolean fromList;
/*      */   private final int sortBy;
/*      */   private String searchName;
/*      */   private final boolean includeAll;
/*  120 */   private PermissionsPlayerList.ISettings[] objects = null;
/*      */   
/*      */   private boolean showingQueue = false;
/*      */   private boolean inQueue = true;
/*      */   private boolean waitAccept = false;
/*      */   private boolean inProgress = false;
/*      */   private boolean delivered = false;
/*      */   private boolean rejected = false;
/*      */   private boolean cancelled = false;
/*      */   
/*      */   public ManageObjectList(Creature aResponder, Type aType) {
/*  131 */     this(aResponder, aType, -10L, false, 1, "", true);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public ManageObjectList(Creature aResponder, Type aType, long parent, boolean wasFromList, int aSortBy, String searchFor, boolean aIncludeAll) {
/*  137 */     super(aResponder, getTitle(aResponder, aType, parent, wasFromList, searchFor), 
/*  138 */         getQuestion(aResponder, aType, parent, wasFromList, searchFor), 121, parent);
/*  139 */     this.player = (Player)getResponder();
/*  140 */     this.fromList = wasFromList;
/*  141 */     this.objectType = aType;
/*  142 */     this.sortBy = aSortBy;
/*  143 */     this.searchName = searchFor;
/*  144 */     this.includeAll = aIncludeAll;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ManageObjectList(Creature aResponder, Type aType, long parent, boolean wasFromList, int aSortBy, String searchFor, boolean aIncludeAll, boolean inqueue, boolean waitaccept, boolean inprogress, boolean delivered, boolean rejected, boolean cancelled) {
/*  152 */     super(aResponder, getTitle(aResponder, aType, parent, wasFromList, searchFor), 
/*  153 */         getQuestion(aResponder, aType, parent, wasFromList, searchFor), 121, parent);
/*  154 */     this.player = (Player)getResponder();
/*  155 */     this.fromList = wasFromList;
/*  156 */     this.objectType = aType;
/*  157 */     this.sortBy = aSortBy;
/*  158 */     this.searchName = searchFor;
/*  159 */     this.includeAll = aIncludeAll;
/*  160 */     this.showingQueue = (aType == Type.WAGONER);
/*  161 */     this.inQueue = inqueue;
/*  162 */     this.waitAccept = waitaccept;
/*  163 */     this.inProgress = inprogress;
/*  164 */     this.delivered = delivered;
/*  165 */     this.rejected = rejected;
/*  166 */     this.cancelled = cancelled;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static String getTitle(Creature aResponder, Type aType, long parent, boolean wasFromList, String lookingFor) {
/*  172 */     if (aType == Type.DOOR) {
/*      */       
/*      */       try {
/*      */         
/*  176 */         Structure structure = Structures.getStructure(parent);
/*  177 */         return structure.getName() + "'s List of doors";
/*      */       }
/*  179 */       catch (NoSuchStructureException e) {
/*      */ 
/*      */         
/*  182 */         return aResponder.getName() + "'s List of " + aType.getTitle() + "s";
/*      */       } 
/*      */     }
/*  185 */     if (aType == Type.SEARCH)
/*  186 */       return "Player Search"; 
/*  187 */     if (aType == Type.REPLY)
/*  188 */       return "Search Result for " + lookingFor; 
/*  189 */     if (aType == Type.SMALL_CART || aType == Type.LARGE_CART || aType == Type.WAGON || aType == Type.SHIP_CARRIER || aType == Type.CREATURE_CARRIER)
/*      */     {
/*  191 */       return aResponder.getName() + "'s List of Small Carts, Large Carts, Wagons and Carriers"; } 
/*  192 */     if (aType == Type.WAGONER && wasFromList) {
/*      */       
/*  194 */       Wagoner wagoner = Wagoner.getWagoner(parent);
/*  195 */       return "Wagoners " + wagoner.getName() + "'s Queue";
/*      */     } 
/*  197 */     return aResponder.getName() + "'s List of " + aType.getTitle() + "s";
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static String getQuestion(Creature aResponder, Type aType, long parent, boolean wasFromList, String lookingFor) {
/*  203 */     if (aType == Type.DOOR) {
/*      */       
/*      */       try {
/*      */         
/*  207 */         Structure structure = Structures.getStructure(parent);
/*  208 */         return "Manage List of Doors for " + structure.getName();
/*      */       }
/*  210 */       catch (NoSuchStructureException e) {
/*      */ 
/*      */         
/*  213 */         return "Manage Your List of " + aType.getTitle() + "s";
/*      */       } 
/*      */     }
/*  216 */     if (aType == Type.SEARCH)
/*  217 */       return "Player Search"; 
/*  218 */     if (aType == Type.REPLY)
/*  219 */       return "Search Result for " + lookingFor; 
/*  220 */     if (aType == Type.SMALL_CART || aType == Type.LARGE_CART || aType == Type.WAGON || aType == Type.SHIP_CARRIER || aType == Type.CREATURE_CARRIER)
/*      */     {
/*  222 */       return "Manage Your List of Small Carts, Large Carts, Wagons and Carriers"; } 
/*  223 */     if (aType == Type.WAGONER && wasFromList) {
/*      */       
/*  225 */       Wagoner wagoner = Wagoner.getWagoner(parent);
/*  226 */       return "Viewing " + wagoner.getName() + "'s Queue";
/*      */     } 
/*  228 */     return "Manage Your List of " + aType.getTitle() + "s";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void answer(Properties aAnswer) {
/*  239 */     setAnswer(aAnswer);
/*      */     
/*  241 */     boolean managePermissions = getBooleanProp("permissions");
/*  242 */     boolean manageDoors = getBooleanProp("doors");
/*  243 */     boolean back = getBooleanProp("back");
/*  244 */     boolean close = getBooleanProp("close");
/*  245 */     boolean search = getBooleanProp("search");
/*  246 */     boolean remall = getBooleanProp("remall");
/*  247 */     boolean findAnimal = getBooleanProp("find");
/*  248 */     boolean inc = getBooleanProp("inc");
/*  249 */     boolean exc = getBooleanProp("exc");
/*  250 */     boolean queue = getBooleanProp("queue");
/*  251 */     boolean viewDelivery = getBooleanProp("delivery");
/*      */     
/*  253 */     if (close)
/*      */       return; 
/*  255 */     if (inc) {
/*      */       
/*  257 */       ManageObjectList mol = new ManageObjectList((Creature)this.player, this.objectType, this.target, this.fromList, this.sortBy, this.searchName, true);
/*  258 */       mol.sendQuestion();
/*      */       return;
/*      */     } 
/*  261 */     if (exc) {
/*      */       
/*  263 */       ManageObjectList mol = new ManageObjectList((Creature)this.player, this.objectType, this.target, this.fromList, this.sortBy, this.searchName, false);
/*  264 */       mol.sendQuestion();
/*      */       return;
/*      */     } 
/*  267 */     if (back) {
/*      */       
/*  269 */       if (this.objectType == Type.DOOR) {
/*      */         
/*  271 */         ManageObjectList mol = new ManageObjectList((Creature)this.player, Type.BUILDING, this.target, false, 1, "", this.includeAll);
/*  272 */         mol.sendQuestion();
/*      */         return;
/*      */       } 
/*  275 */       if (this.objectType == Type.WAGONER) {
/*      */         
/*  277 */         ManageObjectList mol = new ManageObjectList((Creature)this.player, Type.WAGONER, this.target, false, 1, "", this.includeAll);
/*  278 */         mol.sendQuestion();
/*      */         return;
/*      */       } 
/*  281 */       if (this.objectType == Type.DELIVERY) {
/*      */         
/*  283 */         ManageObjectList mol = new ManageObjectList((Creature)this.player, Type.WAGONER, this.target, false, 1, "", this.includeAll);
/*  284 */         mol.sendQuestion2();
/*      */         return;
/*      */       } 
/*  287 */       if (this.objectType == Type.REPLY) {
/*      */         
/*  289 */         ManageObjectList mol = new ManageObjectList((Creature)this.player, Type.SEARCH);
/*  290 */         mol.sendQuestion();
/*      */         return;
/*      */       } 
/*      */     } 
/*  294 */     if (search) {
/*      */ 
/*      */       
/*  297 */       String who = aAnswer.getProperty("who");
/*  298 */       String lookingFor = LoginHandler.raiseFirstLetter(who);
/*  299 */       long lookId = PlayerInfoFactory.getWurmId(lookingFor);
/*      */       
/*  301 */       ManageObjectList mol = new ManageObjectList((Creature)this.player, Type.REPLY, lookId, true, 1, lookingFor, this.includeAll);
/*  302 */       mol.sendQuestion();
/*      */       return;
/*      */     } 
/*  305 */     if (remall)
/*      */     {
/*      */       
/*  308 */       for (PermissionsPlayerList.ISettings is : this.objects) {
/*      */         
/*  310 */         if (!is.isActualOwner(this.target)) {
/*      */           
/*  312 */           is.removeGuest(this.target);
/*  313 */           this.player.getCommunicator().sendNormalServerMessage("You removed " + this.searchName + " from " + is
/*  314 */               .getTypeName() + " (" + is.getObjectName() + ")");
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/*  319 */     if (managePermissions || manageDoors || findAnimal || queue || viewDelivery) {
/*      */ 
/*      */       
/*  322 */       String sel = aAnswer.getProperty("sel");
/*  323 */       long selId = Long.parseLong(sel);
/*  324 */       if (selId == -10L) {
/*      */         
/*  326 */         this.player.getCommunicator().sendNormalServerMessage("You decide to do nothing.");
/*      */         return;
/*      */       } 
/*  329 */       if (managePermissions) {
/*      */ 
/*      */         
/*  332 */         int ct = WurmId.getType(selId);
/*  333 */         if (ct == 1) {
/*      */ 
/*      */           
/*      */           try {
/*  337 */             Creature creature = Creatures.getInstance().getCreature(selId);
/*  338 */             if (creature.isWagoner())
/*      */             {
/*  340 */               ManagePermissions mp = new ManagePermissions((Creature)this.player, Type.WAGONER, (PermissionsPlayerList.ISettings)creature, true, this.target, false, this.objectType, "");
/*      */               
/*  342 */               mp.sendQuestion();
/*      */             
/*      */             }
/*      */             else
/*      */             {
/*  347 */               Vehicle vehicle = Vehicles.getVehicle(creature);
/*  348 */               if (vehicle == null)
/*      */               {
/*  350 */                 ManagePermissions mp = new ManagePermissions((Creature)this.player, Type.ANIMAL0, (PermissionsPlayerList.ISettings)creature, true, this.target, false, this.objectType, "");
/*      */                 
/*  352 */                 mp.sendQuestion();
/*      */ 
/*      */               
/*      */               }
/*  356 */               else if (vehicle.isUnmountable())
/*      */               {
/*  358 */                 ManagePermissions mp = new ManagePermissions((Creature)this.player, Type.ANIMAL0, (PermissionsPlayerList.ISettings)creature, true, this.target, false, this.objectType, "");
/*      */                 
/*  360 */                 mp.sendQuestion();
/*      */               }
/*  362 */               else if (vehicle.getMaxPassengers() == 0)
/*      */               {
/*  364 */                 ManagePermissions mp = new ManagePermissions((Creature)this.player, Type.ANIMAL1, (PermissionsPlayerList.ISettings)creature, true, this.target, false, this.objectType, "");
/*      */                 
/*  366 */                 mp.sendQuestion();
/*      */               }
/*      */               else
/*      */               {
/*  370 */                 ManagePermissions mp = new ManagePermissions((Creature)this.player, Type.ANIMAL2, (PermissionsPlayerList.ISettings)creature, true, this.target, false, this.objectType, "");
/*      */                 
/*  372 */                 mp.sendQuestion();
/*      */               }
/*      */             
/*      */             }
/*      */           
/*  377 */           } catch (NoSuchCreatureException nsce) {
/*      */             
/*  379 */             this.player.getCommunicator().sendNormalServerMessage("Cannot find animal, it was here a minute ago!");
/*  380 */             logger.log(Level.WARNING, "Cannot find animal, it was here a minute ago! Id:" + selId, (Throwable)nsce);
/*      */           }
/*      */         
/*  383 */         } else if (ct == 4) {
/*      */           
/*      */           try
/*      */           {
/*  387 */             Structure structure = Structures.getStructure(selId);
/*  388 */             ManagePermissions mp = new ManagePermissions((Creature)this.player, Type.BUILDING, (PermissionsPlayerList.ISettings)structure, true, this.target, false, this.objectType, "");
/*      */             
/*  390 */             mp.sendQuestion();
/*      */           }
/*  392 */           catch (NoSuchStructureException nsse)
/*      */           {
/*  394 */             this.player.getCommunicator().sendNormalServerMessage("Cannot find structure, it was here a minute ago!");
/*  395 */             logger.log(Level.WARNING, "Cannot find structure, it was here a minute ago! Id:" + selId, (Throwable)nsse);
/*      */           }
/*      */         
/*  398 */         } else if (ct == 5) {
/*      */           
/*      */           try
/*      */           {
/*  402 */             Structure structure = Structures.getStructure(this.target);
/*  403 */             for (Door door : structure.getAllDoors()) {
/*      */               
/*  405 */               if (door.getWurmId() == selId) {
/*      */                 
/*  407 */                 ManagePermissions mp = new ManagePermissions((Creature)this.player, Type.DOOR, (PermissionsPlayerList.ISettings)door, true, this.target, this.fromList, this.objectType, "");
/*      */                 
/*  409 */                 mp.sendQuestion();
/*      */                 return;
/*      */               } 
/*      */             } 
/*  413 */             this.player.getCommunicator().sendNormalServerMessage("Cannot find door, it was here a minute ago!");
/*  414 */             logger.log(Level.WARNING, "Cannot find door, it was here a minute ago! Id:" + selId);
/*      */           }
/*  416 */           catch (NoSuchStructureException nsse)
/*      */           {
/*  418 */             this.player.getCommunicator().sendNormalServerMessage("Cannot find structure, it was here a minute ago!");
/*  419 */             logger.log(Level.WARNING, "Cannot find structure, it was here a minute ago! Id:" + selId, (Throwable)nsse);
/*      */           }
/*      */         
/*  422 */         } else if (ct == 7) {
/*      */           
/*  424 */           FenceGate gate = FenceGate.getFenceGate(selId);
/*  425 */           if (gate != null) {
/*      */             
/*  427 */             ManagePermissions mp = new ManagePermissions((Creature)this.player, Type.GATE, (PermissionsPlayerList.ISettings)gate, true, this.target, this.fromList, this.objectType, "");
/*      */             
/*  429 */             mp.sendQuestion();
/*      */           } else {
/*      */             
/*  432 */             this.player.getCommunicator().sendNormalServerMessage("Cannot find gate, it was here a minute ago!");
/*      */           } 
/*  434 */         } else if (ct == 2) {
/*      */           
/*      */           try
/*      */           {
/*  438 */             Item item = Items.getItem(selId);
/*      */             
/*  440 */             Type itemType = Type.SHIP;
/*  441 */             if (item.getTemplateId() == 186) {
/*  442 */               itemType = Type.SMALL_CART;
/*  443 */             } else if (item.getTemplateId() == 539) {
/*  444 */               itemType = Type.LARGE_CART;
/*  445 */             } else if (item.getTemplateId() == 850) {
/*  446 */               itemType = Type.WAGON;
/*  447 */             } else if (item.getTemplateId() == 853) {
/*  448 */               itemType = Type.SHIP_CARRIER;
/*  449 */             } else if (item.getTemplateId() == 1410) {
/*  450 */               itemType = Type.CREATURE_CARRIER;
/*      */             } 
/*  452 */             ManagePermissions mp = new ManagePermissions((Creature)this.player, itemType, (PermissionsPlayerList.ISettings)item, true, this.target, this.fromList, this.objectType, "");
/*      */             
/*  454 */             mp.sendQuestion();
/*      */           }
/*  456 */           catch (NoSuchItemException e)
/*      */           {
/*  458 */             this.player.getCommunicator().sendNormalServerMessage("Cannot find vehicle, it was here a minute ago!");
/*      */           }
/*      */         
/*  461 */         } else if (ct == 3) {
/*      */           
/*  463 */           MineDoorPermission mineDoor = MineDoorPermission.getPermission(selId);
/*  464 */           if (mineDoor != null) {
/*      */             
/*  466 */             ManagePermissions mp = new ManagePermissions((Creature)this.player, Type.MINEDOOR, (PermissionsPlayerList.ISettings)mineDoor, true, this.target, this.fromList, this.objectType, "");
/*      */             
/*  468 */             mp.sendQuestion();
/*      */           } else {
/*      */             
/*  471 */             this.player.getCommunicator().sendNormalServerMessage("Cannot find minedoor, it was here a minute ago!");
/*      */           } 
/*      */         } else {
/*      */           
/*  475 */           this.player.getCommunicator().sendNormalServerMessage("Unknown object!");
/*      */         } 
/*      */         return;
/*      */       } 
/*  479 */       if (manageDoors) {
/*      */         
/*  481 */         ManageObjectList mol = new ManageObjectList((Creature)this.player, Type.DOOR, selId, true, 1, "", this.includeAll);
/*  482 */         mol.sendQuestion();
/*      */       }
/*  484 */       else if (findAnimal && !Servers.isThisAPvpServer()) {
/*      */ 
/*      */         
/*      */         try {
/*      */           
/*  489 */           Creature creature = Creatures.getInstance().getCreature(selId);
/*  490 */           int centerx = creature.getTileX();
/*  491 */           int centery = creature.getTileY();
/*  492 */           int dx = Math.abs(centerx - this.player.getTileX());
/*  493 */           int dy = Math.abs(centery - this.player.getTileY());
/*  494 */           int mindist = (int)Math.sqrt((dx * dx + dy * dy));
/*  495 */           int dir = MethodsCreatures.getDir((Creature)this.player, centerx, centery);
/*  496 */           if (DbCreatureStatus.getIsLoaded(creature.getWurmId()) == 0)
/*      */           {
/*  498 */             String direction = MethodsCreatures.getLocationStringFor(this.player.getStatus().getRotation(), dir, "you");
/*      */             
/*  500 */             String toReturn = EndGameItems.getDistanceString(mindist, creature.getName(), direction, false);
/*  501 */             this.player.getCommunicator().sendNormalServerMessage(toReturn);
/*      */           }
/*      */           else
/*      */           {
/*  505 */             this.player.getCommunicator().sendNormalServerMessage("This creature is loaded in a cage, or on another server.");
/*      */           }
/*      */         
/*      */         }
/*  509 */         catch (NoSuchCreatureException nsce) {
/*      */           
/*  511 */           this.player.getCommunicator().sendNormalServerMessage("Cannot find animal, it was here a minute ago!");
/*  512 */           logger.log(Level.WARNING, "Cannot find animal, it was here a minute ago! Id:" + selId, (Throwable)nsce);
/*      */         } 
/*      */       } else {
/*  515 */         if (queue && this.objectType == Type.WAGONER) {
/*      */           
/*  517 */           this.inQueue = getBooleanProp("inqueue");
/*  518 */           this.waitAccept = getBooleanProp("waitaccept");
/*  519 */           this.inProgress = getBooleanProp("inprogress");
/*  520 */           this.delivered = getBooleanProp("delivered");
/*  521 */           this.rejected = getBooleanProp("rejected");
/*  522 */           this.cancelled = getBooleanProp("cancelled");
/*      */           
/*  524 */           ManageObjectList manageObjectList = new ManageObjectList((Creature)this.player, Type.WAGONER, selId, true, 1, "", this.includeAll, this.inQueue, this.waitAccept, this.inProgress, this.delivered, this.rejected, this.cancelled);
/*      */           
/*  526 */           manageObjectList.sendQuestion2();
/*      */           
/*      */           return;
/*      */         } 
/*      */         
/*  531 */         this.inQueue = getBooleanProp("inqueue");
/*  532 */         this.waitAccept = getBooleanProp("waitaccept");
/*  533 */         this.inProgress = getBooleanProp("inprogress");
/*  534 */         this.delivered = getBooleanProp("delivered");
/*  535 */         this.rejected = getBooleanProp("rejected");
/*  536 */         this.cancelled = getBooleanProp("cancelled");
/*      */         
/*  538 */         ManageObjectList mol = new ManageObjectList((Creature)this.player, Type.DELIVERY, selId, true, 1, "", this.includeAll, this.inQueue, this.waitAccept, this.inProgress, this.delivered, this.rejected, this.cancelled);
/*      */         
/*  540 */         mol.sendQuestion3();
/*      */         
/*      */         return;
/*      */       } 
/*      */     } 
/*  545 */     for (String key : getAnswer().stringPropertyNames()) {
/*      */       
/*  547 */       if (key.startsWith("sort")) {
/*      */ 
/*      */         
/*  550 */         String sid = key.substring(4);
/*  551 */         int newSort = Integer.parseInt(sid);
/*      */         
/*  553 */         if (this.showingQueue) {
/*      */           
/*  555 */           ManageObjectList manageObjectList = new ManageObjectList((Creature)this.player, this.objectType, this.target, this.fromList, newSort, this.searchName, this.includeAll, this.inQueue, this.waitAccept, this.inProgress, this.delivered, this.rejected, this.cancelled);
/*      */           
/*  557 */           manageObjectList.sendQuestion2();
/*      */           return;
/*      */         } 
/*  560 */         if (this.objectType == Type.DELIVERY) {
/*      */           
/*  562 */           ManageObjectList manageObjectList = new ManageObjectList((Creature)this.player, this.objectType, this.target, this.fromList, newSort, this.searchName, this.includeAll, this.inQueue, this.waitAccept, this.inProgress, this.delivered, this.rejected, this.cancelled);
/*      */           
/*  564 */           manageObjectList.sendQuestion3();
/*      */           return;
/*      */         } 
/*  567 */         ManageObjectList mol = new ManageObjectList((Creature)this.player, this.objectType, this.target, this.fromList, newSort, this.searchName, this.includeAll);
/*  568 */         mol.sendQuestion();
/*      */         
/*      */         return;
/*      */       } 
/*      */     } 
/*  573 */     if (this.objectType == Type.BUILDING) {
/*      */       
/*  575 */       for (String key : getAnswer().stringPropertyNames()) {
/*      */         
/*  577 */         if (key.startsWith("demolish")) {
/*      */ 
/*      */           
/*  580 */           String sid = key.substring(8);
/*  581 */           long id = Long.parseLong(sid);
/*      */           
/*      */           try {
/*  584 */             Structure structure = Structures.getStructure(id);
/*  585 */             if (structure.isOnSurface())
/*  586 */               Zones.flash(structure.getCenterX(), structure.getCenterY(), false); 
/*  587 */             structure.totallyDestroy();
/*      */           }
/*  589 */           catch (NoSuchStructureException nsse) {
/*      */             
/*  591 */             this.player.getCommunicator().sendNormalServerMessage("Cannot find structure, it was here a minute ago!");
/*  592 */             logger.log(Level.WARNING, "Cannot find structure, it was here a minute ago! Id:" + id, (Throwable)nsse);
/*      */           }
/*      */         
/*      */         } 
/*      */       } 
/*  597 */     } else if (this.objectType == Type.WAGONER) {
/*      */       
/*  599 */       for (String key : getAnswer().stringPropertyNames()) {
/*      */         
/*  601 */         if (key.startsWith("dismiss")) {
/*      */ 
/*      */           
/*  604 */           String sid = key.substring(7);
/*  605 */           long id = Long.parseLong(sid);
/*      */           
/*      */           try {
/*  608 */             Creature creature = Creatures.getInstance().getCreature(id);
/*  609 */             Wagoner wagoner = creature.getWagoner();
/*  610 */             if (wagoner.getVillageId() == -1) {
/*  611 */               this.player.getCommunicator().sendNormalServerMessage("Wagoner is already dismissing!");
/*      */               continue;
/*      */             } 
/*  614 */             WagonerDismissQuestion wdq = new WagonerDismissQuestion(getResponder(), wagoner);
/*  615 */             wdq.sendQuestion();
/*      */           
/*      */           }
/*  618 */           catch (NoSuchCreatureException nsse) {
/*      */             
/*  620 */             this.player.getCommunicator().sendNormalServerMessage("Cannot find wagoner, it was here a minute ago!");
/*  621 */             logger.log(Level.WARNING, "Cannot find wagoner, it was here a minute ago! Id:" + id, (Throwable)nsse);
/*      */           }
/*      */         
/*      */         } 
/*      */       } 
/*  626 */     } else if (this.objectType == Type.ANIMAL0 || this.objectType == Type.ANIMAL1 || this.objectType == Type.ANIMAL2) {
/*      */       
/*  628 */       for (String key : getAnswer().stringPropertyNames()) {
/*      */         
/*  630 */         if (key.startsWith("uncarefor")) {
/*      */ 
/*      */           
/*  633 */           String sid = key.substring(9);
/*  634 */           long id = Long.parseLong(sid);
/*      */ 
/*      */           
/*      */           try {
/*  638 */             int tc = Creatures.getInstance().getNumberOfCreaturesProtectedBy(this.player.getWurmId());
/*  639 */             int max = this.player.getNumberOfPossibleCreatureTakenCareOf();
/*      */             
/*  641 */             Creature animal = Creatures.getInstance().getCreature(id);
/*      */             
/*  643 */             if (animal.getCareTakerId() == this.player.getWurmId()) {
/*      */               
/*  645 */               Creatures.getInstance().setCreatureProtected(animal, -10L, false);
/*  646 */               this.player.getCommunicator().sendNormalServerMessage("You let " + animal
/*  647 */                   .getName() + " go in order to care for other creatures. You may care for " + (max - tc + 1) + " more creatures.");
/*      */             
/*      */             }
/*      */             else {
/*      */               
/*  652 */               this.player.getCommunicator().sendNormalServerMessage("You are not caring for this animal!");
/*      */             } 
/*  654 */           } catch (NoSuchCreatureException nsce) {
/*      */ 
/*      */             
/*  657 */             logger.log(Level.WARNING, nsce.getMessage(), (Throwable)nsce);
/*      */           } 
/*      */         } 
/*  660 */         if (key.startsWith("unbrand")) {
/*      */ 
/*      */           
/*  663 */           String sid = key.substring(7);
/*  664 */           long id = Long.parseLong(sid);
/*      */ 
/*      */           
/*      */           try {
/*  668 */             Creature animal = Creatures.getInstance().getCreature(id);
/*  669 */             Brand brand = Creatures.getInstance().getBrand(animal.getWurmId());
/*  670 */             if (brand != null) {
/*      */ 
/*      */               
/*  673 */               if (animal.getBrandVillage() == this.player.getCitizenVillage()) {
/*      */                 
/*  675 */                 if (this.player.getCitizenVillage().isActionAllowed((short)484, (Creature)this.player)) {
/*      */                   
/*  677 */                   brand.deleteBrand();
/*  678 */                   if (animal.getVisionArea() != null) {
/*  679 */                     animal.getVisionArea().broadCastUpdateSelectBar(animal.getWurmId());
/*      */                   }
/*      */                 } else {
/*  682 */                   this.player.getCommunicator().sendNormalServerMessage("You need to have deed permission to remove a brand.");
/*      */                 } 
/*      */               } else {
/*  685 */                 this.player.getCommunicator().sendNormalServerMessage("You need to be in same village as the brand on the animal.");
/*      */               } 
/*      */             } else {
/*  688 */               this.player.getCommunicator().sendNormalServerMessage("That animal is not branded.");
/*      */             } 
/*  690 */           } catch (NoSuchCreatureException nsce) {
/*      */ 
/*      */             
/*  693 */             logger.log(Level.WARNING, nsce.getMessage(), (Throwable)nsce);
/*      */           } 
/*      */         } 
/*  696 */         if (key.startsWith("untame")) {
/*      */ 
/*      */           
/*  699 */           String sid = key.substring(6);
/*  700 */           long id = Long.parseLong(sid);
/*      */ 
/*      */           
/*      */           try {
/*  704 */             Creature animal = Creatures.getInstance().getCreature(id);
/*  705 */             if (animal.getDominator() == this.player) {
/*      */               
/*  707 */               if (DbCreatureStatus.getIsLoaded(animal.getWurmId()) == 1) {
/*      */                 
/*  709 */                 this.player.getCommunicator().sendNormalServerMessage("This animal is caged, remove it first.", (byte)3);
/*      */                 
/*      */                 return;
/*      */               } 
/*      */               
/*  714 */               Creature pet = this.player.getPet();
/*  715 */               if (animal.cantRideUntame()) {
/*      */ 
/*      */                 
/*  718 */                 assert pet != null;
/*  719 */                 Vehicle cret = Vehicles.getVehicleForId(pet.getWurmId());
/*  720 */                 if (cret != null)
/*      */                 {
/*  722 */                   cret.kickAll();
/*      */                 }
/*      */               } 
/*  725 */               animal.setDominator(-10L);
/*  726 */               this.player.setPet(-10L);
/*  727 */               this.player.getCommunicator().sendNormalServerMessage("You no longer have this animal tamed!");
/*      */               continue;
/*      */             } 
/*  730 */             this.player.getCommunicator().sendNormalServerMessage("You do not have this animal tamed!");
/*      */           }
/*  732 */           catch (NoSuchCreatureException nsce) {
/*      */ 
/*      */             
/*  735 */             logger.log(Level.WARNING, nsce.getMessage(), (Throwable)nsce);
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
/*      */   public void sendQuestion() {
/*  750 */     int width = 300;
/*  751 */     StringBuilder buf = new StringBuilder();
/*  752 */     String closeBtn = "harray{" + (((this.objectType == Type.DOOR || this.objectType == Type.REPLY) && this.fromList) ? "button{text=\"Back\";id=\"back\"};" : "") + "label{text=\" \"};button{text=\"Close\";id=\"close\"};label{text=\" \"}};";
/*      */ 
/*      */ 
/*      */     
/*  756 */     buf.append("border{border{size=\"20,20\";null;null;label{type='bold';text=\"" + this.question + "\"};" + closeBtn + "null;}null;scroll{vertical=\"true\";horizontal=\"false\";varray{rescale=\"true\";passthrough{id=\"id\";text=\"" + 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  765 */         getId() + "\"}");
/*  766 */     String extraButton = "";
/*      */     
/*  768 */     if (this.objectType == Type.SEARCH) {
/*      */ 
/*      */       
/*  771 */       buf.append("text{text=\"Allow searching for all objects that the player has permissions and you can manage.\"}");
/*  772 */       buf.append("harray{label{text=\"Look For Player \"}input{id=\"who\"}}");
/*  773 */       buf.append("text{text=\"\"}");
/*      */       
/*  775 */       buf.append("harray{button{text=\"Search\";id=\"search\";default=\"true\"}};");
/*      */       
/*  777 */       buf.append("}};null;null;}");
/*  778 */       getResponder().getCommunicator().sendBml(300, 160, true, true, buf.toString(), 200, 200, 200, this.title);
/*      */       return;
/*      */     } 
/*  781 */     if (this.objectType == Type.REPLY) {
/*      */ 
/*      */ 
/*      */       
/*  785 */       if (this.target == -10L) {
/*      */         
/*  787 */         buf.append("label{text=\"No such Player\"}");
/*  788 */         buf.append("}};null;null;}");
/*  789 */         getResponder().getCommunicator().sendBml(300, 150, true, true, buf.toString(), 200, 200, 200, this.title);
/*      */ 
/*      */         
/*      */         return;
/*      */       } 
/*      */       
/*  795 */       Village vill = getResponder().getCitizenVillage();
/*  796 */       int vid = (vill != null && vill.getRoleFor(getResponder()).mayManageAllowedObjects()) ? vill.getId() : -1;
/*      */       
/*  798 */       Set<PermissionsPlayerList.ISettings> result = new HashSet<>();
/*  799 */       this.objects = (PermissionsPlayerList.ISettings[])Creatures.getManagedAnimalsFor(this.player, vid, true);
/*  800 */       for (PermissionsPlayerList.ISettings is : this.objects) {
/*      */         
/*  802 */         if (is.isGuest(this.target))
/*  803 */           result.add(is); 
/*      */       } 
/*  805 */       this.objects = (PermissionsPlayerList.ISettings[])Structures.getManagedBuildingsFor(this.player, vid, true);
/*  806 */       for (PermissionsPlayerList.ISettings is : this.objects) {
/*      */         
/*  808 */         if (is.isGuest(this.target))
/*  809 */           result.add(is); 
/*      */       } 
/*  811 */       this.objects = (PermissionsPlayerList.ISettings[])FenceGate.getManagedGatesFor(this.player, vid, true);
/*  812 */       for (PermissionsPlayerList.ISettings is : this.objects) {
/*      */         
/*  814 */         if (is.isGuest(this.target))
/*  815 */           result.add(is); 
/*      */       } 
/*  817 */       this.objects = (PermissionsPlayerList.ISettings[])Items.getManagedCartsFor(this.player, true);
/*  818 */       for (PermissionsPlayerList.ISettings is : this.objects) {
/*      */         
/*  820 */         if (is.isGuest(this.target))
/*  821 */           result.add(is); 
/*      */       } 
/*  823 */       this.objects = (PermissionsPlayerList.ISettings[])MineDoorPermission.getManagedMineDoorsFor(this.player, vid, true);
/*  824 */       for (PermissionsPlayerList.ISettings is : this.objects) {
/*      */         
/*  826 */         if (is.isGuest(this.target))
/*  827 */           result.add(is); 
/*      */       } 
/*  829 */       this.objects = (PermissionsPlayerList.ISettings[])Items.getManagedShipsFor(this.player, true);
/*  830 */       for (PermissionsPlayerList.ISettings is : this.objects) {
/*      */         
/*  832 */         if (is.isGuest(this.target)) {
/*  833 */           result.add(is);
/*      */         }
/*      */       } 
/*  836 */       this.objects = result.<PermissionsPlayerList.ISettings>toArray(new PermissionsPlayerList.ISettings[result.size()]);
/*      */       
/*  838 */       buf.append("text{text=\"List of objects that player '" + this.searchName + "' has permissions for that you may manage.\"}");
/*  839 */       int absSortBy = Math.abs(this.sortBy);
/*  840 */       final int upDown = Integer.signum(this.sortBy);
/*      */ 
/*      */       
/*  843 */       buf.append("table{rows=\"1\";cols=\"5\";label{text=\"\"};" + 
/*      */           
/*  845 */           colHeader("Name", 1, this.sortBy) + 
/*  846 */           colHeader("Type", 2, this.sortBy) + 
/*  847 */           colHeader("Owner?", 3, this.sortBy) + "label{type=\"bold\";text=\"\"};");
/*      */ 
/*      */ 
/*      */       
/*  851 */       switch (absSortBy) {
/*      */         
/*      */         case 1:
/*  854 */           Arrays.sort(this.objects, new Comparator<PermissionsPlayerList.ISettings>()
/*      */               {
/*      */                 
/*      */                 public int compare(PermissionsPlayerList.ISettings param1, PermissionsPlayerList.ISettings param2)
/*      */                 {
/*  859 */                   return param1.getObjectName().compareTo(param2.getObjectName()) * upDown;
/*      */                 }
/*      */               });
/*      */           break;
/*      */         case 2:
/*  864 */           Arrays.sort(this.objects, new Comparator<PermissionsPlayerList.ISettings>()
/*      */               {
/*      */                 
/*      */                 public int compare(PermissionsPlayerList.ISettings param1, PermissionsPlayerList.ISettings param2)
/*      */                 {
/*  869 */                   return param1.getTypeName().compareTo(param2.getTypeName()) * upDown;
/*      */                 }
/*      */               });
/*      */           break;
/*      */         case 3:
/*  874 */           Arrays.sort(this.objects, new Comparator<PermissionsPlayerList.ISettings>()
/*      */               {
/*      */                 
/*      */                 public int compare(PermissionsPlayerList.ISettings param1, PermissionsPlayerList.ISettings param2)
/*      */                 {
/*  879 */                   if (param1.isActualOwner(ManageObjectList.this.target) == param2.isActualOwner(ManageObjectList.this.target))
/*  880 */                     return param1.getObjectName().compareTo(param2.getObjectName()) * upDown; 
/*  881 */                   if (param1.isActualOwner(ManageObjectList.this.target)) {
/*  882 */                     return -1 * upDown;
/*      */                   }
/*  884 */                   return 1 * upDown;
/*      */                 }
/*      */               });
/*      */           break;
/*      */       } 
/*      */       
/*  890 */       for (PermissionsPlayerList.ISettings object : this.objects)
/*      */       {
/*  892 */         buf.append("radio{group=\"sel\";id=\"" + object.getWurmId() + "\";text=\"\"}label{text=\"" + object
/*  893 */             .getObjectName() + "\"};label{text=\"" + object
/*  894 */             .getTypeName() + "\"};label{" + 
/*  895 */             showBoolean(object.isActualOwner(this.target)) + "};label{text=\"\"}");
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  900 */       buf.append("}");
/*      */       
/*  902 */       if (result.size() > 0) {
/*  903 */         extraButton = ";label{text=\"  \"};button{text=\"Remove all Permissions\";id=\"remall\"}";
/*      */       }
/*  905 */     } else if (this.objectType == Type.ANIMAL0 || this.objectType == Type.ANIMAL1 || this.objectType == Type.ANIMAL2) {
/*      */       
/*  907 */       buf.append(makeAnimalList());
/*  908 */       if (!Servers.isThisAPvpServer())
/*  909 */         extraButton = ";label{text=\"  \"};button{text=\"Give direction to\";id=\"find\"}"; 
/*  910 */       width = 550;
/*      */     }
/*  912 */     else if (this.objectType == Type.BUILDING) {
/*      */       
/*  914 */       buf.append(makeBuildingList());
/*  915 */       extraButton = ";label{text=\"  \"};button{text=\"Manage All Doors\";id=\"doors\"}";
/*  916 */       width = 500;
/*      */     }
/*  918 */     else if (this.objectType == Type.LARGE_CART || this.objectType == Type.SMALL_CART || this.objectType == Type.WAGON || this.objectType == Type.SHIP_CARRIER || this.objectType == Type.CREATURE_CARRIER) {
/*      */ 
/*      */       
/*  921 */       buf.append(makeLandVehicleList());
/*  922 */       width = 500;
/*      */     }
/*  924 */     else if (this.objectType == Type.DOOR) {
/*      */       
/*  926 */       buf.append(makeDoorList());
/*  927 */       width = 500;
/*      */     }
/*  929 */     else if (this.objectType == Type.GATE) {
/*      */       
/*  931 */       buf.append(makeGateList());
/*  932 */       width = 600;
/*      */     }
/*  934 */     else if (this.objectType == Type.MINEDOOR) {
/*      */       
/*  936 */       buf.append(makeMineDoorList());
/*  937 */       width = 400;
/*      */     }
/*  939 */     else if (this.objectType == Type.SHIP) {
/*      */       
/*  941 */       buf.append(makeShipList());
/*  942 */       width = 500;
/*      */     }
/*  944 */     else if (this.objectType == Type.WAGONER) {
/*      */       
/*  946 */       buf.append(makeWagonerList());
/*  947 */       width = 600;
/*      */     } 
/*      */ 
/*      */     
/*  951 */     buf.append("radio{group=\"sel\";id=\"-10\";selected=\"true\";text=\"None\"}");
/*  952 */     buf.append("text{text=\"\"}");
/*      */     
/*  954 */     buf.append("harray{button{text=\"Manage Permissions\";id=\"permissions\"}" + extraButton + "};");
/*  955 */     if (this.objectType == Type.WAGONER) {
/*      */ 
/*      */       
/*  958 */       buf.append("text{text=\"\"}");
/*  959 */       buf.append("harray{button{text=\"View Deliveries\";id=\"queue\"};label{text=\" filter by \"};checkbox{id=\"inqueue\";text=\"In queue  \"" + (this.inQueue ? ";selected=\"true\"" : "") + "};checkbox{id=\"waitaccept\";text=\"Waiting for accept  \"" + (this.waitAccept ? ";selected=\"true\"" : "") + "};checkbox{id=\"inprogress\";text=\"In Progress  \"" + (this.inProgress ? ";selected=\"true\"" : "") + "};checkbox{id=\"delivered\";text=\"Delivered  \"" + (this.delivered ? ";selected=\"true\"" : "") + "};checkbox{id=\"rejected\";text=\"Rejected  \"" + (this.rejected ? ";selected=\"true\"" : "") + "};checkbox{id=\"cancelled\";text=\"Cancelled  \"" + (this.cancelled ? ";selected=\"true\"" : "") + "};};");
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
/*  970 */     buf.append("}};null;null;}");
/*  971 */     getResponder().getCommunicator().sendBml(width, 400, true, true, buf.toString(), 200, 200, 200, this.title);
/*      */   }
/*      */ 
/*      */   
/*      */   private void sendQuestion2() {
/*  976 */     StringBuilder buf = new StringBuilder();
/*  977 */     String closeBtn = "harray{button{text=\"Back\";id=\"back\"};label{text=\" \"};button{text=\"Close\";id=\"close\"};label{text=\" \"}};";
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  982 */     buf.append("border{border{size=\"20,20\";null;null;label{type='bold';text=\"" + this.question + "\"};" + "harray{button{text=\"Back\";id=\"back\"};label{text=\" \"};button{text=\"Close\";id=\"close\"};label{text=\" \"}};" + "null;}null;scroll{vertical=\"true\";horizontal=\"false\";varray{rescale=\"true\";passthrough{id=\"id\";text=\"" + 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  991 */         getId() + "\"}");
/*      */     
/*  993 */     int absSortBy = Math.abs(this.sortBy);
/*  994 */     final int upDown = Integer.signum(this.sortBy);
/*      */     
/*  996 */     Delivery[] deliveries = Delivery.getDeliveriesFor(this.target, this.inQueue, this.waitAccept, this.inProgress, this.rejected, this.delivered);
/*      */     
/*  998 */     buf.append("table{rows=\"1\";cols=\"6\";label{text=\"\"};" + 
/*      */         
/* 1000 */         colHeader("id", 1, this.sortBy) + 
/* 1001 */         colHeader("Sender", 2, this.sortBy) + 
/* 1002 */         colHeader("Receiver", 3, this.sortBy) + 
/* 1003 */         colHeader("State", 4, this.sortBy) + "label{text=\"\"};");
/*      */ 
/*      */     
/* 1006 */     switch (absSortBy) {
/*      */       
/*      */       case 1:
/* 1009 */         Arrays.sort(deliveries, new Comparator<Delivery>()
/*      */             {
/*      */               
/*      */               public int compare(Delivery param1, Delivery param2)
/*      */               {
/* 1014 */                 long value1 = param1.getDeliveryId();
/* 1015 */                 long value2 = param2.getDeliveryId();
/* 1016 */                 if (value1 == value2)
/* 1017 */                   return 0; 
/* 1018 */                 if (value1 < value2) {
/* 1019 */                   return -1 * upDown;
/*      */                 }
/* 1021 */                 return 1 * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */       case 2:
/* 1026 */         Arrays.sort(deliveries, new Comparator<Delivery>()
/*      */             {
/*      */               
/*      */               public int compare(Delivery param1, Delivery param2)
/*      */               {
/* 1031 */                 return param1.getSenderName().compareTo(param2.getSenderName()) * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */       case 3:
/* 1036 */         Arrays.sort(deliveries, new Comparator<Delivery>()
/*      */             {
/*      */               
/*      */               public int compare(Delivery param1, Delivery param2)
/*      */               {
/* 1041 */                 return param1.getReceiverName().compareTo(param2.getReceiverName()) * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */       case 4:
/* 1046 */         Arrays.sort(deliveries, new Comparator<Delivery>()
/*      */             {
/*      */               
/*      */               public int compare(Delivery param1, Delivery param2)
/*      */               {
/* 1051 */                 return param1.getStateName().compareTo(param2.getStateName()) * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */     } 
/*      */     
/* 1057 */     for (Delivery delivery : deliveries)
/*      */     {
/* 1059 */       buf.append("radio{group=\"sel\";id=\"" + delivery.getDeliveryId() + "\";text=\"\"}label{text=\"" + delivery
/* 1060 */           .getDeliveryId() + "\"};label{text=\"" + delivery
/* 1061 */           .getSenderName() + "\"};label{text=\"" + delivery
/* 1062 */           .getReceiverName() + "\"};label{text=\"" + delivery
/* 1063 */           .getStateName() + "\"};label{text=\"  \"};");
/*      */     }
/*      */ 
/*      */     
/* 1067 */     buf.append("}");
/* 1068 */     buf.append("radio{group=\"sel\";id=\"-10\";selected=\"true\";text=\"None\"}");
/* 1069 */     buf.append("text{text=\"\"}");
/* 1070 */     buf.append("harray{button{text=\"View Delivery\";id=\"delivery\"};};");
/*      */ 
/*      */     
/* 1073 */     buf.append("}};null;null;}");
/* 1074 */     getResponder().getCommunicator().sendBml(400, 400, true, true, buf.toString(), 200, 200, 200, this.title);
/*      */   }
/*      */ 
/*      */   
/*      */   private void sendQuestion3() {
/* 1079 */     StringBuilder buf = new StringBuilder();
/* 1080 */     String closeBtn = "harray{button{text=\"Back\";id=\"back\"};label{text=\" \"};button{text=\"Close\";id=\"close\"};label{text=\" \"}};";
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1085 */     buf.append("border{border{size=\"20,20\";null;null;label{type='bold';text=\"" + this.question + "\"};" + "harray{button{text=\"Back\";id=\"back\"};label{text=\" \"};button{text=\"Close\";id=\"close\"};label{text=\" \"}};" + "null;}null;scroll{vertical=\"true\";horizontal=\"false\";varray{rescale=\"true\";passthrough{id=\"id\";text=\"" + 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1094 */         getId() + "\"}");
/*      */     
/* 1096 */     Delivery delivery = Delivery.getDelivery(this.target);
/* 1097 */     buf.append("table{rows=\"1\";cols=\"4\";");
/*      */     
/* 1099 */     buf.append("label{text=\"\"};label{text=\"Id\"};label{text=\"\"};label{text=\"" + delivery
/*      */ 
/*      */         
/* 1102 */         .getDeliveryId() + "\"};");
/*      */     
/* 1104 */     buf.append("label{text=\"\"};label{text=\"Sender\"};label{text=\"\"};label{text=\"" + delivery
/*      */ 
/*      */         
/* 1107 */         .getSenderName() + "\"};");
/*      */     
/* 1109 */     buf.append("label{text=\"\"};label{text=\"Receiver\"};label{text=\"\"};label{text=\"" + delivery
/*      */ 
/*      */         
/* 1112 */         .getReceiverName() + "\"};");
/*      */     
/* 1114 */     buf.append("label{text=\"\"};label{text=\"State\"};label{text=\"\"};label{text=\"" + delivery
/*      */ 
/*      */         
/* 1117 */         .getStateName() + "\"};");
/*      */     
/* 1119 */     buf.append("label{text=\"\"};label{text=\"Delivery setup\"};label{text=\"@\"};label{text=\"" + delivery
/*      */ 
/*      */         
/* 1122 */         .getStringWaitingForAccept() + "\"};");
/*      */ 
/*      */     
/* 1125 */     String reason = "Accepted";
/* 1126 */     switch (delivery.getState()) {
/*      */ 
/*      */       
/*      */       case 6:
/*      */       case 11:
/* 1131 */         reason = "Timed Out";
/*      */         break;
/*      */ 
/*      */       
/*      */       case 5:
/*      */       case 8:
/* 1137 */         reason = "Rejected";
/*      */         break;
/*      */ 
/*      */       
/*      */       case 9:
/*      */       case 10:
/* 1143 */         reason = "Cancelled";
/*      */         break;
/*      */     } 
/*      */     
/* 1147 */     buf.append("label{text=\"\"};label{text=\"" + reason + "\"};label{text=\"@\"};label{text=\"" + delivery
/*      */ 
/*      */         
/* 1150 */         .getStringAcceptedOrRejected() + "\"};");
/*      */     
/* 1152 */     buf.append("label{text=\"\"};label{text=\"Delivery started\"};label{text=\"@\"};label{text=\"" + delivery
/*      */ 
/*      */         
/* 1155 */         .getStringDeliveryStarted() + "\"};");
/*      */     
/* 1157 */     buf.append("label{text=\"\"};label{text=\"Crates picked up\"};label{text=\"@\"};label{text=\"" + delivery
/*      */ 
/*      */         
/* 1160 */         .getStringPickedUp() + "\"};");
/*      */     
/* 1162 */     buf.append("label{text=\"\"};label{text=\"Crates delivered\"};label{text=\"@\"};label{text=\"" + delivery
/*      */ 
/*      */         
/* 1165 */         .getStringDelivered() + "\"};");
/*      */     
/* 1167 */     buf.append("}");
/*      */     
/* 1169 */     buf.append("}};null;null;}");
/* 1170 */     getResponder().getCommunicator().sendBml(400, 400, true, true, buf.toString(), 200, 200, 200, this.title);
/*      */   }
/*      */ 
/*      */   
/*      */   private String makeAnimalList() {
/* 1175 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 1177 */     Village vill = getResponder().getCitizenVillage();
/* 1178 */     final int vid = (vill != null && vill.getRoleFor(getResponder()).mayManageAllowedObjects()) ? vill.getId() : -1;
/*      */     
/* 1180 */     buf.append("text{text=\"As well as the list containing any animals that you care for, and any tamed animals you have. It also includes any animals that are branded to your village that have 'Settlement may manage' Permission set to your village so long as you have the 'Manage Allowed Objects' settlement permission.\"}");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1185 */     buf.append("text{text=\"\"}");
/*      */     
/* 1187 */     Creature[] animals = Creatures.getManagedAnimalsFor(this.player, vid, this.includeAll);
/* 1188 */     int absSortBy = Math.abs(this.sortBy);
/* 1189 */     final int upDown = Integer.signum(this.sortBy);
/*      */ 
/*      */     
/* 1192 */     buf.append("table{rows=\"1\";cols=\"8\";label{text=\"\"};" + 
/*      */         
/* 1194 */         colHeader("Name", 1, this.sortBy) + 
/* 1195 */         colHeader("Animal Type", 2, this.sortBy) + 
/* 1196 */         colHeader("On Deed?", 3, this.sortBy) + 
/* 1197 */         colHeader("Hitched?", 4, this.sortBy) + 
/* 1198 */         colHeader("Cared For?", 5, this.sortBy) + 
/* 1199 */         colHeader("Branded?", 6, this.sortBy) + 
/* 1200 */         colHeader("Tamed?", 7, this.sortBy));
/*      */ 
/*      */     
/* 1203 */     switch (absSortBy) {
/*      */       
/*      */       case 1:
/* 1206 */         Arrays.sort(animals, new Comparator<Creature>()
/*      */             {
/*      */               
/*      */               public int compare(Creature param1, Creature param2)
/*      */               {
/* 1211 */                 return param1.getName().compareTo(param2.getName()) * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */       case 2:
/* 1216 */         Arrays.sort(animals, new Comparator<Creature>()
/*      */             {
/*      */               
/*      */               public int compare(Creature param1, Creature param2)
/*      */               {
/* 1221 */                 return param1.getTypeName().compareTo(param2.getTypeName()) * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */       case 3:
/* 1226 */         Arrays.sort(animals, new Comparator<Creature>()
/*      */             {
/*      */               
/*      */               public int compare(Creature param1, Creature param2)
/*      */               {
/* 1231 */                 if (param1.isOnDeed() == param2.isOnDeed())
/* 1232 */                   return param1.getName().compareTo(param2.getName()) * upDown; 
/* 1233 */                 if (param1.isOnDeed()) {
/* 1234 */                   return 1 * upDown;
/*      */                 }
/* 1236 */                 return -1 * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */       case 4:
/* 1241 */         Arrays.sort(animals, new Comparator<Creature>()
/*      */             {
/*      */               
/*      */               public int compare(Creature param1, Creature param2)
/*      */               {
/* 1246 */                 if (param1.isHitched() == param2.isHitched())
/* 1247 */                   return param1.getName().compareTo(param2.getName()) * upDown; 
/* 1248 */                 if (param1.isHitched()) {
/* 1249 */                   return 1 * upDown;
/*      */                 }
/* 1251 */                 return -1 * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */       case 5:
/* 1256 */         Arrays.sort(animals, new Comparator<Creature>()
/*      */             {
/*      */               
/*      */               public int compare(Creature param1, Creature param2)
/*      */               {
/* 1261 */                 if (param1.isCaredFor(ManageObjectList.this.player) == param2.isCaredFor(ManageObjectList.this.player))
/* 1262 */                   return param1.getName().compareTo(param2.getName()) * upDown; 
/* 1263 */                 if (param1.isCaredFor(ManageObjectList.this.player)) {
/* 1264 */                   return 1 * upDown;
/*      */                 }
/* 1266 */                 return -1 * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */       case 6:
/* 1271 */         Arrays.sort(animals, new Comparator<Creature>()
/*      */             {
/*      */               
/*      */               public int compare(Creature param1, Creature param2)
/*      */               {
/* 1276 */                 if (param1.isBrandedBy(vid) == param2.isBrandedBy(vid))
/* 1277 */                   return param1.getName().compareTo(param2.getName()) * upDown; 
/* 1278 */                 if (param1.isBrandedBy(vid)) {
/* 1279 */                   return 1 * upDown;
/*      */                 }
/* 1281 */                 return -1 * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */       case 7:
/* 1286 */         Arrays.sort(animals, new Comparator<Creature>()
/*      */             {
/*      */               
/*      */               public int compare(Creature param1, Creature param2)
/*      */               {
/* 1291 */                 if (param1.isDominated() == param2.isDominated())
/* 1292 */                   return param1.getName().compareTo(param2.getName()) * upDown; 
/* 1293 */                 if (param1.isDominated()) {
/* 1294 */                   return 1 * upDown;
/*      */                 }
/* 1296 */                 return -1 * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */     } 
/* 1301 */     for (Creature animal : animals) {
/*      */       
/* 1303 */       buf.append((animal.canHavePermissions() ? ("radio{group=\"sel\";id=\"" + animal.getWurmId() + "\";text=\"\"}") : "label{text=\"\"};") + "label{text=\"" + animal
/* 1304 */           .getName() + "\"};label{text=\"" + animal
/* 1305 */           .getTypeName() + "\"};" + (
/* 1306 */           animal.isBranded() ? ("label{" + showBoolean(animal.isOnDeed()) + "};") : "label{text=\"not branded\"};") + "label{" + 
/* 1307 */           showBoolean(animal.isHitched()) + "};");
/*      */       
/* 1309 */       if (animal.isCaredFor(this.player)) {
/* 1310 */         buf.append(unCareForButton(animal));
/*      */       } else {
/* 1312 */         buf.append("label{" + showBoolean((animal.getCareTakerId() != -10L)) + "};");
/* 1313 */       }  if (animal.isBranded() && animal.getBrandVillage() == this.player.getCitizenVillage() && this.player
/* 1314 */         .getCitizenVillage().isActionAllowed((short)484, (Creature)this.player)) {
/* 1315 */         buf.append(unBrandButton(animal));
/*      */       } else {
/* 1317 */         buf.append("label{" + showBoolean(animal.isBranded()) + "};");
/* 1318 */       }  if (animal.isDominated() && animal.getDominator() == this.player) {
/* 1319 */         buf.append(unTameButton(animal));
/*      */       } else {
/* 1321 */         buf.append("label{" + showBoolean(animal.isDominated()) + "};");
/*      */       } 
/* 1323 */     }  buf.append("}");
/* 1324 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String unCareForButton(Creature animal) {
/* 1329 */     StringBuilder buf = new StringBuilder();
/* 1330 */     buf.append("harray{button{text=\"Un-Care For\";id=\"uncarefor" + animal.getWurmId() + "\";confirm=\"You are about to un care for " + animal
/* 1331 */         .getName() + ".\";question=\"Do you really want to do that?\"}label{text=\" \"}}");
/*      */ 
/*      */     
/* 1334 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String unBrandButton(Creature animal) {
/* 1339 */     StringBuilder buf = new StringBuilder();
/* 1340 */     buf.append("harray{button{text=\"Un-Brand\";id=\"unbrand" + animal.getWurmId() + "\";confirm=\"You are about to remove the brand from " + animal
/* 1341 */         .getName() + ".\";question=\"Do you really want to do that?\"}label{text=\" \"}}");
/*      */ 
/*      */     
/* 1344 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String unTameButton(Creature animal) {
/* 1349 */     StringBuilder buf = new StringBuilder();
/* 1350 */     buf.append("harray{button{text=\"Un-Tame\";id=\"untame" + animal.getWurmId() + "\";confirm=\"You are about to un tame " + animal
/* 1351 */         .getName() + ".\";question=\"Do you really want to do that?\"}label{text=\" \"}}");
/*      */ 
/*      */     
/* 1354 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String makeBuildingList() {
/* 1359 */     StringBuilder buf = new StringBuilder();
/* 1360 */     buf.append("text{text=\"List includes any buildings that you are the owner of plus any buildings in your settlment that have 'Settlement may manage' Permission set to your village so long as you have the 'Manage Allowed Objects' settlement permission.\"}");
/*      */ 
/*      */     
/* 1363 */     buf.append("text{text=\"\"}");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1371 */     Village vill = getResponder().getCitizenVillage();
/* 1372 */     int vid = (vill != null && vill.getRoleFor(getResponder()).mayManageAllowedObjects()) ? vill.getId() : -1;
/*      */     
/* 1374 */     Structure[] structures = Structures.getManagedBuildingsFor(this.player, vid, this.includeAll);
/* 1375 */     int absSortBy = Math.abs(this.sortBy);
/* 1376 */     final int upDown = Integer.signum(this.sortBy);
/*      */     
/* 1378 */     buf.append("table{rows=\"1\";cols=\"7\";label{text=\"\"};" + 
/*      */         
/* 1380 */         colHeader("Name", 1, this.sortBy) + 
/* 1381 */         colHeader("Owner?", 2, this.sortBy) + 
/* 1382 */         colHeader("Doors have locks?", 3, this.sortBy) + 
/* 1383 */         colHeader("On Deed?", 4, this.sortBy) + 
/* 1384 */         colHeader("Deed Managed?", 5, this.sortBy) + "label{type=\"bold\";text=\"\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1389 */     switch (absSortBy) {
/*      */       
/*      */       case 1:
/* 1392 */         Arrays.sort(structures, new Comparator<Structure>()
/*      */             {
/*      */               
/*      */               public int compare(Structure param1, Structure param2)
/*      */               {
/* 1397 */                 return param1.getObjectName().compareTo(param2.getObjectName()) * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */       case 2:
/* 1402 */         Arrays.sort(structures, new Comparator<Structure>()
/*      */             {
/*      */               
/*      */               public int compare(Structure param1, Structure param2)
/*      */               {
/* 1407 */                 if (param1.isActualOwner(ManageObjectList.this.player.getWurmId()) == param2.isActualOwner(ManageObjectList.this.player.getWurmId()))
/* 1408 */                   return param1.getObjectName().compareTo(param2.getObjectName()) * upDown; 
/* 1409 */                 if (param1.isActualOwner(ManageObjectList.this.player.getWurmId())) {
/* 1410 */                   return -1 * upDown;
/*      */                 }
/* 1412 */                 return 1 * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */       case 3:
/* 1417 */         Arrays.sort(structures, new Comparator<Structure>()
/*      */             {
/*      */               
/*      */               public int compare(Structure param1, Structure param2)
/*      */               {
/* 1422 */                 int value1 = ((param1.getAllDoors()).length == 0) ? 0 : (param1.isLockable() ? 1 : 2);
/* 1423 */                 int value2 = ((param2.getAllDoors()).length == 0) ? 0 : (param2.isLockable() ? 1 : 2);
/* 1424 */                 if (value1 == value2)
/* 1425 */                   return param1.getObjectName().compareTo(param2.getObjectName()) * upDown; 
/* 1426 */                 if (value1 < value2) {
/* 1427 */                   return 1 * upDown;
/*      */                 }
/* 1429 */                 return -1 * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */       case 4:
/* 1434 */         Arrays.sort(structures, new Comparator<Structure>()
/*      */             {
/*      */               
/*      */               public int compare(Structure param1, Structure param2)
/*      */               {
/* 1439 */                 int value1 = (param1.getVillage() != null) ? param1.getVillage().getId() : 0;
/* 1440 */                 int value2 = (param2.getVillage() != null) ? param2.getVillage().getId() : 0;
/* 1441 */                 if (value1 == value2)
/* 1442 */                   return param1.getObjectName().compareTo(param2.getObjectName()) * upDown; 
/* 1443 */                 if (value1 < value2) {
/* 1444 */                   return 1 * upDown;
/*      */                 }
/* 1446 */                 return -1 * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */       case 5:
/* 1451 */         Arrays.sort(structures, new Comparator<Structure>()
/*      */             {
/*      */               
/*      */               public int compare(Structure param1, Structure param2)
/*      */               {
/* 1456 */                 if (param1.isManaged() == param2.isManaged())
/* 1457 */                   return param1.getObjectName().compareTo(param2.getObjectName()) * upDown; 
/* 1458 */                 if (param1.isManaged()) {
/* 1459 */                   return -1 * upDown;
/*      */                 }
/* 1461 */                 return 1 * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */     } 
/* 1466 */     for (Structure structure : structures) {
/*      */       
/* 1468 */       buf.append((structure.canHavePermissions() ? ("radio{group=\"sel\";id=\"" + structure.getWurmId() + "\";text=\"\"}") : "label{text=\"\"};") + "label{text=\"" + structure
/* 1469 */           .getObjectName() + "\"};label{" + 
/* 1470 */           showBoolean(structure.isActualOwner(this.player.getWurmId())) + "};");
/* 1471 */       if ((structure.getAllDoors()).length == 0) {
/* 1472 */         buf.append("label{color=\"255,177,40\"text=\"No lockable doors.\"};");
/* 1473 */       } else if (structure.isLockable()) {
/* 1474 */         buf.append("label{color=\"127,255,127\"text=\"true\"};");
/*      */       } else {
/* 1476 */         buf.append("label{color=\"255,127,127\"text=\"Not all doors have locks.\"};");
/* 1477 */       }  buf.append("label{" + showBoolean((structure.getVillage() != null)) + "};");
/* 1478 */       buf.append("label{" + showBoolean(structure.isManaged()) + "};");
/* 1479 */       if (structure.isOwner(this.player.getWurmId())) {
/* 1480 */         buf.append("harray{label{text=\" \"};button{text=\"Demolish\";id=\"demolish" + structure
/* 1481 */             .getWurmId() + "\";confirm=\"You are about to blow up the building '" + structure
/* 1482 */             .getObjectName() + "'.\";question=\"Do you really want to do that?\"}label{text=\" \"}}");
/*      */       }
/*      */       else {
/*      */         
/* 1486 */         buf.append("label{text=\" \"}");
/*      */       } 
/* 1488 */     }  buf.append("}");
/* 1489 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String makeDoorList() {
/* 1494 */     StringBuilder buf = new StringBuilder();
/*      */ 
/*      */     
/*      */     try {
/* 1498 */       Structure structure = Structures.getStructure(this.target);
/*      */       
/* 1500 */       buf.append("text{text=\"List includes all doors in this building if you are the owner, or any doors in this building that have the 'Building may manage' Permission so long as you have the 'Manage Permissions' building permission.\"}");
/*      */ 
/*      */       
/* 1503 */       buf.append("text{text=\"Note: Owner of the Door is the Owner of the bulding.\"}");
/* 1504 */       buf.append("text{text=\"\"}");
/* 1505 */       buf.append("text{type=\"bold\";text=\"List of Doors that you may manage in this building.\"}");
/*      */       
/* 1507 */       if (this.includeAll) {
/* 1508 */         buf.append(extraButton("Exclude Doors without locks", "exc"));
/*      */       } else {
/* 1510 */         buf.append(extraButton("Include Doors without locks", "inc"));
/*      */       } 
/* 1512 */       Door[] doors = structure.getAllDoors(this.includeAll);
/* 1513 */       int absSortBy = Math.abs(this.sortBy);
/* 1514 */       final int upDown = Integer.signum(this.sortBy);
/*      */       
/* 1516 */       buf.append("table{rows=\"1\";cols=\"7\";label{text=\"\"};" + 
/*      */           
/* 1518 */           colHeader("Name", 1, this.sortBy) + 
/* 1519 */           colHeader("Door Type", 2, this.sortBy) + 
/* 1520 */           colHeader("Level", 3, this.sortBy) + 
/* 1521 */           colHeader("Has Lock?", 4, this.sortBy) + 
/* 1522 */           colHeader("Locked?", 5, this.sortBy) + 
/* 1523 */           colHeader("Building Managed?", 6, this.sortBy));
/*      */ 
/*      */       
/* 1526 */       Arrays.sort(doors, new Comparator<Door>()
/*      */           {
/*      */             
/*      */             public int compare(Door param1, Door param2)
/*      */             {
/* 1531 */               if (param1.getFloorLevel() == param2.getFloorLevel()) {
/*      */                 
/* 1533 */                 int comp = param1.getTypeName().compareTo(param2.getTypeName());
/* 1534 */                 if (comp == 0) {
/* 1535 */                   return param1.getObjectName().compareTo(param2.getObjectName()) * upDown;
/*      */                 }
/* 1537 */                 return comp * upDown;
/*      */               } 
/* 1539 */               if (param1.getFloorLevel() < param2.getFloorLevel()) {
/* 1540 */                 return 1 * upDown;
/*      */               }
/* 1542 */               return -1 * upDown;
/*      */             }
/*      */           });
/* 1545 */       switch (absSortBy) {
/*      */         
/*      */         case 1:
/* 1548 */           Arrays.sort(doors, new Comparator<Door>()
/*      */               {
/*      */                 
/*      */                 public int compare(Door param1, Door param2)
/*      */                 {
/* 1553 */                   return param1.getObjectName().compareTo(param2.getObjectName()) * upDown;
/*      */                 }
/*      */               });
/*      */           break;
/*      */         case 2:
/* 1558 */           Arrays.sort(doors, new Comparator<Door>()
/*      */               {
/*      */                 
/*      */                 public int compare(Door param1, Door param2)
/*      */                 {
/* 1563 */                   return param1.getTypeName().compareTo(param2.getTypeName()) * upDown;
/*      */                 }
/*      */               });
/*      */           break;
/*      */         case 3:
/* 1568 */           Arrays.sort(doors, new Comparator<Door>()
/*      */               {
/*      */                 
/*      */                 public int compare(Door param1, Door param2)
/*      */                 {
/* 1573 */                   if (param1.getFloorLevel() == param2.getFloorLevel())
/* 1574 */                     return 0; 
/* 1575 */                   if (param1.getFloorLevel() < param2.getFloorLevel()) {
/* 1576 */                     return 1 * upDown;
/*      */                   }
/* 1578 */                   return -1 * upDown;
/*      */                 }
/*      */               });
/*      */           break;
/*      */         case 4:
/* 1583 */           Arrays.sort(doors, new Comparator<Door>()
/*      */               {
/*      */                 
/*      */                 public int compare(Door param1, Door param2)
/*      */                 {
/* 1588 */                   if (param1.hasLock() == param2.hasLock())
/* 1589 */                     return 0; 
/* 1590 */                   if (param1.hasLock()) {
/* 1591 */                     return -1 * upDown;
/*      */                   }
/* 1593 */                   return 1 * upDown;
/*      */                 }
/*      */               });
/*      */           break;
/*      */         case 5:
/* 1598 */           Arrays.sort(doors, new Comparator<Door>()
/*      */               {
/*      */                 
/*      */                 public int compare(Door param1, Door param2)
/*      */                 {
/* 1603 */                   if (param1.isLocked() == param2.isLocked())
/* 1604 */                     return 0; 
/* 1605 */                   if (param1.isLocked()) {
/* 1606 */                     return -1 * upDown;
/*      */                   }
/* 1608 */                   return 1 * upDown;
/*      */                 }
/*      */               });
/*      */           break;
/*      */         case 6:
/* 1613 */           Arrays.sort(doors, new Comparator<Door>()
/*      */               {
/*      */                 
/*      */                 public int compare(Door param1, Door param2)
/*      */                 {
/* 1618 */                   if (param1.isManaged() == param2.isManaged())
/* 1619 */                     return 0; 
/* 1620 */                   if (param1.isManaged()) {
/* 1621 */                     return -1 * upDown;
/*      */                   }
/* 1623 */                   return 1 * upDown;
/*      */                 }
/*      */               });
/*      */           break;
/*      */       } 
/* 1628 */       for (Door door : doors)
/*      */       {
/* 1630 */         buf.append((door.canHavePermissions() ? ("radio{group=\"sel\";id=\"" + door.getWurmId() + "\";text=\"\"}") : "label{text=\"\"}") + "label{text=\"" + door
/* 1631 */             .getObjectName() + "\"};label{text=\"" + door
/* 1632 */             .getTypeName() + "\"};label{text=\"" + door
/* 1633 */             .getFloorLevel() + "\"};label{" + 
/* 1634 */             showBoolean(door.hasLock()) + "};label{" + 
/* 1635 */             showBoolean(door.isLocked()) + "};label{" + 
/* 1636 */             showBoolean(door.isManaged()) + "};");
/*      */       }
/*      */       
/* 1639 */       buf.append("}");
/*      */     }
/* 1641 */     catch (NoSuchStructureException nsse) {
/*      */       
/* 1643 */       logger.log(Level.WARNING, "Cannot find structure, it was here a minute ago! Id:" + this.target, (Throwable)nsse);
/* 1644 */       buf.append("text{text=\"Cannot find structure, it was here a minute ago!\"}");
/*      */     } 
/* 1646 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String makeGateList() {
/* 1651 */     StringBuilder buf = new StringBuilder();
/* 1652 */     buf.append("text{text=\"As well as the list containing any gates that you are the owner of their lock it also includes any gate that have 'Settlement may manage' Permission set to your village so long as you have the 'Manage Allowed Objects' settlement permission.\"}");
/*      */ 
/*      */     
/* 1655 */     Village vill = getResponder().getCitizenVillage();
/* 1656 */     if (vill != null && vill.isMayor((Creature)this.player))
/*      */     {
/* 1658 */       buf.append("text{text=\"As you are a mayor, the list will have all gates on your deed.\"}");
/*      */     }
/* 1660 */     buf.append("text{text=\"\"}");
/*      */     
/* 1662 */     if (this.includeAll) {
/* 1663 */       buf.append(extraButton("Exclude Gates without locks", "exc"));
/*      */     } else {
/* 1665 */       buf.append(extraButton("Include Gates without locks", "inc"));
/*      */     } 
/* 1667 */     int vid = (vill != null && vill.getRoleFor(getResponder()).mayManageAllowedObjects()) ? vill.getId() : -1;
/*      */     
/* 1669 */     FenceGate[] gates = FenceGate.getManagedGatesFor(this.player, vid, this.includeAll);
/* 1670 */     int absSortBy = Math.abs(this.sortBy);
/* 1671 */     final int upDown = Integer.signum(this.sortBy);
/*      */     
/* 1673 */     buf.append("table{rows=\"1\";cols=\"9\";label{text=\"\"};" + 
/*      */         
/* 1675 */         colHeader("Name", 1, this.sortBy) + 
/* 1676 */         colHeader("Gate Type", 2, this.sortBy) + 
/* 1677 */         colHeader("Level", 3, this.sortBy) + 
/* 1678 */         colHeader("Has Lock?", 4, this.sortBy) + 
/* 1679 */         colHeader("Locked?", 5, this.sortBy) + 
/* 1680 */         colHeader("Owner?", 6, this.sortBy) + 
/* 1681 */         colHeader("On Deed?", 7, this.sortBy) + 
/* 1682 */         colHeader("Deed Managed?", 8, this.sortBy));
/*      */ 
/*      */ 
/*      */     
/* 1686 */     Arrays.sort(gates, new Comparator<FenceGate>()
/*      */         {
/*      */           
/*      */           public int compare(FenceGate param1, FenceGate param2)
/*      */           {
/* 1691 */             if (param1.getFloorLevel() == param2.getFloorLevel()) {
/*      */               
/* 1693 */               int comp = param1.getTypeName().compareTo(param2.getTypeName());
/* 1694 */               if (comp == 0) {
/* 1695 */                 return param1.getObjectName().compareTo(param2.getObjectName()) * upDown;
/*      */               }
/* 1697 */               return comp * upDown;
/*      */             } 
/* 1699 */             if (param1.getFloorLevel() < param2.getFloorLevel()) {
/* 1700 */               return 1 * upDown;
/*      */             }
/* 1702 */             return -1 * upDown;
/*      */           }
/*      */         });
/* 1705 */     switch (absSortBy) {
/*      */       
/*      */       case 1:
/* 1708 */         Arrays.sort(gates, new Comparator<FenceGate>()
/*      */             {
/*      */               
/*      */               public int compare(FenceGate param1, FenceGate param2)
/*      */               {
/* 1713 */                 return param1.getObjectName().compareTo(param2.getObjectName()) * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */       case 2:
/* 1718 */         Arrays.sort(gates, new Comparator<FenceGate>()
/*      */             {
/*      */               
/*      */               public int compare(FenceGate param1, FenceGate param2)
/*      */               {
/* 1723 */                 return param1.getTypeName().compareTo(param2.getTypeName()) * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */       case 3:
/* 1728 */         Arrays.sort(gates, new Comparator<FenceGate>()
/*      */             {
/*      */               
/*      */               public int compare(FenceGate param1, FenceGate param2)
/*      */               {
/* 1733 */                 if (param1.getFloorLevel() == param2.getFloorLevel())
/* 1734 */                   return 0; 
/* 1735 */                 if (param1.getFloorLevel() < param2.getFloorLevel()) {
/* 1736 */                   return 1 * upDown;
/*      */                 }
/* 1738 */                 return -1 * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */       case 4:
/* 1743 */         Arrays.sort(gates, new Comparator<FenceGate>()
/*      */             {
/*      */               
/*      */               public int compare(FenceGate param1, FenceGate param2)
/*      */               {
/* 1748 */                 if (param1.hasLock() == param2.hasLock())
/* 1749 */                   return 0; 
/* 1750 */                 if (param1.hasLock()) {
/* 1751 */                   return -1 * upDown;
/*      */                 }
/* 1753 */                 return 1 * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */       case 5:
/* 1758 */         Arrays.sort(gates, new Comparator<FenceGate>()
/*      */             {
/*      */               
/*      */               public int compare(FenceGate param1, FenceGate param2)
/*      */               {
/* 1763 */                 if (param1.isLocked() == param2.isLocked())
/* 1764 */                   return 0; 
/* 1765 */                 if (param1.isLocked()) {
/* 1766 */                   return -1 * upDown;
/*      */                 }
/* 1768 */                 return 1 * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */       case 6:
/* 1773 */         Arrays.sort(gates, new Comparator<FenceGate>()
/*      */             {
/*      */               
/*      */               public int compare(FenceGate param1, FenceGate param2)
/*      */               {
/* 1778 */                 if (param1.isActualOwner(ManageObjectList.this.player.getWurmId()) == param2.isActualOwner(ManageObjectList.this.player.getWurmId()))
/* 1779 */                   return param1.getObjectName().compareTo(param2.getObjectName()) * upDown; 
/* 1780 */                 if (param1.isActualOwner(ManageObjectList.this.player.getWurmId())) {
/* 1781 */                   return -1 * upDown;
/*      */                 }
/* 1783 */                 return 1 * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */       
/*      */       case 7:
/* 1789 */         Arrays.sort(gates, new Comparator<FenceGate>()
/*      */             {
/*      */               
/*      */               public int compare(FenceGate param1, FenceGate param2)
/*      */               {
/* 1794 */                 int value1 = (param1.getVillage() != null) ? param1.getVillage().getId() : 0;
/* 1795 */                 int value2 = (param2.getVillage() != null) ? param2.getVillage().getId() : 0;
/* 1796 */                 if (value1 == value2)
/* 1797 */                   return 0; 
/* 1798 */                 if (value1 < value2) {
/* 1799 */                   return -1 * upDown;
/*      */                 }
/* 1801 */                 return 1 * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */       case 8:
/* 1806 */         Arrays.sort(gates, new Comparator<FenceGate>()
/*      */             {
/*      */               
/*      */               public int compare(FenceGate param1, FenceGate param2)
/*      */               {
/* 1811 */                 if (param1.isManaged() == param2.isManaged())
/* 1812 */                   return 0; 
/* 1813 */                 if (param1.isManaged()) {
/* 1814 */                   return -1 * upDown;
/*      */                 }
/* 1816 */                 return 1 * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */     } 
/* 1821 */     for (FenceGate gate : gates)
/*      */     {
/* 1823 */       buf.append((gate.canHavePermissions() ? ("radio{group=\"sel\";id=\"" + gate.getWurmId() + "\";text=\"\"}") : "label{text=\"\"}") + "label{text=\"" + gate
/* 1824 */           .getObjectName() + "\"};label{text=\"" + gate
/* 1825 */           .getTypeName() + "\"};label{text=\"" + gate
/* 1826 */           .getFloorLevel() + "\"};label{" + 
/* 1827 */           showBoolean(gate.hasLock()) + "};label{" + 
/* 1828 */           showBoolean(gate.isLocked()) + "};label{" + 
/* 1829 */           showBoolean(gate.isActualOwner(this.player.getWurmId())) + "};label{" + 
/* 1830 */           showBoolean((gate.getVillage() != null)) + "};label{" + 
/* 1831 */           showBoolean(gate.isManaged()) + "};");
/*      */     }
/*      */     
/* 1834 */     buf.append("}");
/* 1835 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String makeLandVehicleList() {
/* 1840 */     StringBuilder buf = new StringBuilder();
/* 1841 */     buf.append("text{text=\"List contains the Small Carts, Large Carts, Wagons and Carriers that you can manage.\"}");
/* 1842 */     buf.append("text{text=\"\"}");
/* 1843 */     buf.append("text{type=\"bold\";text=\"List of Small Carts, Large Carts, Wagons and Carriers that you may manage.\"}");
/*      */     
/* 1845 */     if (this.includeAll) {
/* 1846 */       buf.append(extraButton("Exclude Vehicles without locks", "exc"));
/*      */     } else {
/* 1848 */       buf.append(extraButton("Include Vehicles without locks", "inc"));
/*      */     } 
/* 1850 */     Item[] items = Items.getManagedCartsFor(this.player, this.includeAll);
/* 1851 */     int absSortBy = Math.abs(this.sortBy);
/* 1852 */     final int upDown = Integer.signum(this.sortBy);
/*      */     
/* 1854 */     buf.append("table{rows=\"1\";cols=\"6\";label{text=\"\"};" + 
/*      */         
/* 1856 */         colHeader("Name", 1, this.sortBy) + 
/* 1857 */         colHeader("Type", 2, this.sortBy) + 
/* 1858 */         colHeader("Owner?", 3, this.sortBy) + 
/* 1859 */         colHeader("Locked?", 4, this.sortBy) + "label{type=\"bold\";text=\"\"};");
/*      */     
/* 1861 */     switch (absSortBy) {
/*      */       
/*      */       case 1:
/* 1864 */         Arrays.sort(items, new Comparator<Item>()
/*      */             {
/*      */               
/*      */               public int compare(Item param1, Item param2)
/*      */               {
/* 1869 */                 return param1.getObjectName().compareTo(param2.getObjectName()) * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */       case 2:
/* 1874 */         Arrays.sort(items, new Comparator<Item>()
/*      */             {
/*      */               
/*      */               public int compare(Item param1, Item param2)
/*      */               {
/* 1879 */                 return param1.getTypeName().compareTo(param2.getTypeName()) * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */       case 3:
/* 1884 */         Arrays.sort(items, new Comparator<Item>()
/*      */             {
/*      */               
/*      */               public int compare(Item param1, Item param2)
/*      */               {
/* 1889 */                 if (param1.isActualOwner(ManageObjectList.this.player.getWurmId()) == param2.isActualOwner(ManageObjectList.this.player.getWurmId()))
/* 1890 */                   return param1.getObjectName().compareTo(param2.getObjectName()) * upDown; 
/* 1891 */                 if (param1.isActualOwner(ManageObjectList.this.player.getWurmId())) {
/* 1892 */                   return -1 * upDown;
/*      */                 }
/* 1894 */                 return 1 * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */       case 4:
/* 1899 */         Arrays.sort(items, new Comparator<Item>()
/*      */             {
/*      */               
/*      */               public int compare(Item param1, Item param2)
/*      */               {
/* 1904 */                 if (param1.isLocked() == param2.isLocked())
/* 1905 */                   return param1.getObjectName().compareTo(param2.getObjectName()) * upDown; 
/* 1906 */                 if (param1.isLocked()) {
/* 1907 */                   return -1 * upDown;
/*      */                 }
/* 1909 */                 return 1 * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */     } 
/*      */     
/* 1915 */     for (Item item : items)
/*      */     {
/* 1917 */       buf.append((item.canHavePermissions() ? ("radio{group=\"sel\";id=\"" + item.getWurmId() + "\";text=\"\"}") : "label{text=\"\"}") + "label{text=\"" + item
/* 1918 */           .getObjectName() + "\"};" + 
/* 1919 */           addRariryColour(item, item.getTypeName()) + "label{" + 
/* 1920 */           showBoolean(item.isActualOwner(this.player.getWurmId())) + "};label{" + 
/* 1921 */           showBoolean(item.isLocked()) + "};label{text=\"\"};");
/*      */     }
/*      */     
/* 1924 */     buf.append("}");
/* 1925 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String makeMineDoorList() {
/* 1930 */     Village vill = getResponder().getCitizenVillage();
/* 1931 */     StringBuilder buf = new StringBuilder();
/* 1932 */     buf.append("text{text=\"As well as the list containing any mine doors that you are the owner of it also includes any mine doors that have 'Settlement may manage' Permission set to your village so long as you have the 'Manage Allowed Objects' settlement permission.\"}");
/*      */ 
/*      */     
/* 1935 */     if (vill != null && vill.isMayor((Creature)this.player))
/*      */     {
/* 1937 */       buf.append("text{text=\"As you are a mayor, the list will have all minedoors on your deed.\"}");
/*      */     }
/* 1939 */     buf.append("text{text=\"\"}");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1947 */     int vid = (vill != null && vill.getRoleFor(getResponder()).mayManageAllowedObjects()) ? vill.getId() : -1;
/*      */     
/* 1949 */     MineDoorPermission[] mineDoors = MineDoorPermission.getManagedMineDoorsFor(this.player, vid, this.includeAll);
/* 1950 */     int absSortBy = Math.abs(this.sortBy);
/* 1951 */     final int upDown = Integer.signum(this.sortBy);
/*      */     
/* 1953 */     buf.append("table{rows=\"1\";cols=\"7\";label{text=\"\"};" + 
/*      */         
/* 1955 */         colHeader("Name", 1, this.sortBy) + 
/* 1956 */         colHeader("Door Type", 2, this.sortBy) + 
/* 1957 */         colHeader("Owner?", 3, this.sortBy) + 
/* 1958 */         colHeader("On Deed?", 4, this.sortBy) + 
/* 1959 */         colHeader("Deed Managed?", 5, this.sortBy) + "label{type=\"bold\";text=\"\"};");
/*      */ 
/*      */     
/* 1962 */     switch (absSortBy) {
/*      */       
/*      */       case 1:
/* 1965 */         Arrays.sort(mineDoors, new Comparator<MineDoorPermission>()
/*      */             {
/*      */               
/*      */               public int compare(MineDoorPermission param1, MineDoorPermission param2)
/*      */               {
/* 1970 */                 return param1.getObjectName().compareTo(param2.getObjectName()) * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */       case 2:
/* 1975 */         Arrays.sort(mineDoors, new Comparator<MineDoorPermission>()
/*      */             {
/*      */               
/*      */               public int compare(MineDoorPermission param1, MineDoorPermission param2)
/*      */               {
/* 1980 */                 return param1.getObjectName().compareTo(param2.getObjectName()) * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */       case 3:
/* 1985 */         Arrays.sort(mineDoors, new Comparator<MineDoorPermission>()
/*      */             {
/*      */               
/*      */               public int compare(MineDoorPermission param1, MineDoorPermission param2)
/*      */               {
/* 1990 */                 if (param1.isActualOwner(ManageObjectList.this.player.getWurmId()) == param2.isActualOwner(ManageObjectList.this.player.getWurmId()))
/* 1991 */                   return param1.getObjectName().compareTo(param2.getObjectName()) * upDown; 
/* 1992 */                 if (param1.isActualOwner(ManageObjectList.this.player.getWurmId())) {
/* 1993 */                   return -1 * upDown;
/*      */                 }
/* 1995 */                 return 1 * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */       
/*      */       case 4:
/* 2001 */         Arrays.sort(mineDoors, new Comparator<MineDoorPermission>()
/*      */             {
/*      */               
/*      */               public int compare(MineDoorPermission param1, MineDoorPermission param2)
/*      */               {
/* 2006 */                 int value1 = (param1.getVillage() != null) ? param1.getVillage().getId() : 0;
/* 2007 */                 int value2 = (param2.getVillage() != null) ? param2.getVillage().getId() : 0;
/* 2008 */                 if (value1 == value2)
/* 2009 */                   return 0; 
/* 2010 */                 if (value1 < value2) {
/* 2011 */                   return -1 * upDown;
/*      */                 }
/* 2013 */                 return 1 * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */       case 5:
/* 2018 */         Arrays.sort(mineDoors, new Comparator<MineDoorPermission>()
/*      */             {
/*      */               
/*      */               public int compare(MineDoorPermission param1, MineDoorPermission param2)
/*      */               {
/* 2023 */                 if (param1.isManaged() == param2.isManaged())
/* 2024 */                   return param1.getObjectName().compareTo(param2.getObjectName()) * upDown; 
/* 2025 */                 if (param1.isManaged()) {
/* 2026 */                   return -1 * upDown;
/*      */                 }
/* 2028 */                 return 1 * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */     } 
/*      */     
/* 2034 */     for (MineDoorPermission mineDoor : mineDoors)
/*      */     {
/* 2036 */       buf.append((mineDoor.canHavePermissions() ? ("radio{group=\"sel\";id=\"" + mineDoor.getWurmId() + "\";text=\"\"}") : "label{text=\"\"}") + "label{text=\"" + mineDoor
/* 2037 */           .getObjectName() + "\"};label{text=\"" + mineDoor
/* 2038 */           .getTypeName() + "\"};label{" + 
/* 2039 */           showBoolean(mineDoor.isActualOwner(this.player.getWurmId())) + "};label{" + 
/* 2040 */           showBoolean((mineDoor.getVillage() != null)) + "};label{" + 
/* 2041 */           showBoolean(mineDoor.isManaged()) + "};label{text=\" \"}");
/*      */     }
/*      */     
/* 2044 */     buf.append("}");
/* 2045 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String makeShipList() {
/* 2050 */     StringBuilder buf = new StringBuilder();
/* 2051 */     buf.append("text{text=\"List contains the Ships that you can manage.\"}");
/* 2052 */     buf.append("text{text=\"\"}");
/* 2053 */     buf.append("text{type=\"bold\";text=\"Will have List of Ships that you may manage.\"}");
/*      */     
/* 2055 */     if (this.includeAll) {
/* 2056 */       buf.append(extraButton("Exclude ships without locks", "exc"));
/*      */     } else {
/* 2058 */       buf.append(extraButton("Include ships without locks", "inc"));
/*      */     } 
/* 2060 */     Item[] items = Items.getManagedShipsFor(this.player, this.includeAll);
/* 2061 */     int absSortBy = Math.abs(this.sortBy);
/* 2062 */     final int upDown = Integer.signum(this.sortBy);
/*      */     
/* 2064 */     buf.append("table{rows=\"1\";cols=\"6\";label{text=\"\"};" + 
/*      */         
/* 2066 */         colHeader("Name", 1, this.sortBy) + 
/* 2067 */         colHeader("Type", 2, this.sortBy) + 
/* 2068 */         colHeader("Owner?", 3, this.sortBy) + 
/* 2069 */         colHeader("Locked?", 4, this.sortBy) + "label{type=\"bold\";text=\"\"};");
/*      */ 
/*      */     
/* 2072 */     switch (absSortBy) {
/*      */       
/*      */       case 1:
/* 2075 */         Arrays.sort(items, new Comparator<Item>()
/*      */             {
/*      */               
/*      */               public int compare(Item param1, Item param2)
/*      */               {
/* 2080 */                 return param1.getObjectName().compareTo(param2.getObjectName()) * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */       case 2:
/* 2085 */         Arrays.sort(items, new Comparator<Item>()
/*      */             {
/*      */               
/*      */               public int compare(Item param1, Item param2)
/*      */               {
/* 2090 */                 return param1.getTypeName().compareTo(param2.getTypeName()) * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */       case 3:
/* 2095 */         Arrays.sort(items, new Comparator<Item>()
/*      */             {
/*      */               
/*      */               public int compare(Item param1, Item param2)
/*      */               {
/* 2100 */                 if (param1.isActualOwner(ManageObjectList.this.player.getWurmId()) == param2.isActualOwner(ManageObjectList.this.player.getWurmId()))
/* 2101 */                   return param1.getObjectName().compareTo(param2.getObjectName()) * upDown; 
/* 2102 */                 if (param1.isActualOwner(ManageObjectList.this.player.getWurmId())) {
/* 2103 */                   return -1 * upDown;
/*      */                 }
/* 2105 */                 return 1 * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */       case 4:
/* 2110 */         Arrays.sort(items, new Comparator<Item>()
/*      */             {
/*      */               
/*      */               public int compare(Item param1, Item param2)
/*      */               {
/* 2115 */                 if (param1.isLocked() == param2.isLocked())
/* 2116 */                   return param1.getObjectName().compareTo(param2.getObjectName()) * upDown; 
/* 2117 */                 if (param1.isLocked()) {
/* 2118 */                   return -1 * upDown;
/*      */                 }
/* 2120 */                 return 1 * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */     } 
/*      */     
/* 2126 */     for (Item item : items)
/*      */     {
/* 2128 */       buf.append((item.canHavePermissions() ? ("radio{group=\"sel\";id=\"" + item.getWurmId() + "\";text=\"\"}") : "label{text=\"\"}") + "label{text=\"" + item
/* 2129 */           .getObjectName() + "\"};" + 
/* 2130 */           addRariryColour(item, item.getTypeName()) + "label{" + 
/* 2131 */           showBoolean(item.isActualOwner(this.player.getWurmId())) + "};label{" + 
/* 2132 */           showBoolean(item.isLocked()) + "};label{text=\"\"};");
/*      */     }
/*      */     
/* 2135 */     buf.append("}");
/* 2136 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String makeWagonerList() {
/* 2141 */     StringBuilder buf = new StringBuilder();
/*      */ 
/*      */     
/* 2144 */     int vid = -1;
/*      */     
/* 2146 */     Creature[] animals = Creatures.getManagedWagonersFor(this.player, -1);
/* 2147 */     int absSortBy = Math.abs(this.sortBy);
/* 2148 */     final int upDown = Integer.signum(this.sortBy);
/*      */ 
/*      */     
/* 2151 */     buf.append("table{rows=\"1\";cols=\"6\";label{text=\"\"};" + 
/*      */         
/* 2153 */         colHeader("Name", 1, this.sortBy) + 
/* 2154 */         colHeader("State", 2, this.sortBy) + 
/* 2155 */         colHeader("Queue", 3, this.sortBy) + "label{text=\"\"};label{text=\"\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2160 */     switch (absSortBy) {
/*      */       
/*      */       case 1:
/* 2163 */         Arrays.sort(animals, new Comparator<Creature>()
/*      */             {
/*      */               
/*      */               public int compare(Creature param1, Creature param2)
/*      */               {
/* 2168 */                 return param1.getName().compareTo(param2.getName()) * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */       case 2:
/* 2173 */         Arrays.sort(animals, new Comparator<Creature>()
/*      */             {
/*      */               
/*      */               public int compare(Creature param1, Creature param2)
/*      */               {
/* 2178 */                 return param1.getWagoner().getStateName().compareTo(param2.getWagoner().getStateName()) * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */       case 3:
/* 2183 */         Arrays.sort(animals, new Comparator<Creature>()
/*      */             {
/*      */               
/*      */               public int compare(Creature param1, Creature param2)
/*      */               {
/* 2188 */                 int value1 = param1.getWagoner().getQueueLength();
/* 2189 */                 int value2 = param2.getWagoner().getQueueLength();
/* 2190 */                 if (value1 == value2)
/* 2191 */                   return 0; 
/* 2192 */                 if (value1 < value2) {
/* 2193 */                   return -1 * upDown;
/*      */                 }
/* 2195 */                 return 1 * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */     } 
/* 2200 */     for (Creature animal : animals) {
/*      */       
/* 2202 */       Wagoner wagoner = animal.getWagoner();
/* 2203 */       int queueLength = Delivery.getQueueLength(wagoner.getWurmId());
/* 2204 */       buf.append((
/* 2205 */           animal.canHavePermissions() ? ("radio{group=\"sel\";id=\"" + animal
/* 2206 */           .getWurmId() + "\";text=\"\"}") : "label{text=\"\"};") + "label{text=\"" + animal
/*      */           
/* 2208 */           .getName() + "\"};label{text=\"" + wagoner
/* 2209 */           .getStateName() + "\"};" + ((queueLength == 0) ? "label{text=\"empty\"};" : ("label{text=\"" + queueLength + "\"};")) + "label{text=\"  \"};");
/*      */ 
/*      */ 
/*      */       
/* 2213 */       if (animal.mayManage(getResponder())) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2222 */         if (wagoner.getVillageId() == -1) {
/* 2223 */           buf.append("label{\"Dismissing.\"};");
/*      */         } else {
/* 2225 */           buf.append(dismissButton(animal));
/*      */         } 
/*      */       } else {
/* 2228 */         buf.append("label{\"\"};");
/*      */       } 
/* 2230 */     }  buf.append("}");
/* 2231 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String dismissButton(Creature animal) {
/* 2236 */     StringBuilder buf = new StringBuilder();
/* 2237 */     buf.append("harray{button{text=\"Dismiss\";id=\"dismiss" + animal.getWurmId() + "\";}label{text=\" \"}}");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2242 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String extraButton(String txt, String id) {
/* 2247 */     StringBuilder buf = new StringBuilder();
/* 2248 */     buf.append("harray{label{text=\"Filter list:\"};button{text=\"" + txt + "\";id=\"" + id + "\"}};");
/*      */ 
/*      */ 
/*      */     
/* 2252 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   public static String addRariryColour(Item item, String name) {
/* 2257 */     StringBuilder buf = new StringBuilder();
/* 2258 */     if (item.getRarity() == 1) {
/* 2259 */       buf.append("label{color=\"66,153,225\";text=\"rare " + name + "\"};");
/* 2260 */     } else if (item.getRarity() == 2) {
/* 2261 */       buf.append("label{color=\"0,255,255\";text=\"supreme " + name + "\"};");
/* 2262 */     } else if (item.getRarity() == 3) {
/* 2263 */       buf.append("label{color=\"255,0,255\";text=\"fantastic " + name + "\"};");
/*      */     } else {
/* 2265 */       buf.append("label{text=\"" + name + "\"};");
/* 2266 */     }  return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String showBoolean(boolean flag) {
/* 2271 */     StringBuilder buf = new StringBuilder();
/* 2272 */     if (flag) {
/* 2273 */       buf.append("color=\"127,255,127\"");
/*      */     } else {
/* 2275 */       buf.append("color=\"255,127,127\"");
/* 2276 */     }  buf.append("text=\"" + flag + "\"");
/* 2277 */     return buf.toString();
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\ManageObjectList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */