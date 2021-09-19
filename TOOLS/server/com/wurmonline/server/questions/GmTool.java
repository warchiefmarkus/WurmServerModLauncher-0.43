/*      */ package com.wurmonline.server.questions;
/*      */ 
/*      */ import com.wurmonline.mesh.BushData;
/*      */ import com.wurmonline.mesh.FoliageAge;
/*      */ import com.wurmonline.mesh.GrassData;
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.mesh.TreeData;
/*      */ import com.wurmonline.server.Features;
/*      */ import com.wurmonline.server.HistoryEvent;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.LoginServerWebConnection;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.NoSuchPlayerException;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.ServerEntry;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.WurmCalendar;
/*      */ import com.wurmonline.server.WurmId;
/*      */ import com.wurmonline.server.banks.Bank;
/*      */ import com.wurmonline.server.banks.BankSlot;
/*      */ import com.wurmonline.server.banks.BankUnavailableException;
/*      */ import com.wurmonline.server.banks.Banks;
/*      */ import com.wurmonline.server.behaviours.Crops;
/*      */ import com.wurmonline.server.behaviours.Seat;
/*      */ import com.wurmonline.server.behaviours.Vehicle;
/*      */ import com.wurmonline.server.behaviours.Vehicles;
/*      */ import com.wurmonline.server.bodys.Body;
/*      */ import com.wurmonline.server.creatures.AnimalSettings;
/*      */ import com.wurmonline.server.creatures.Brand;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.Creatures;
/*      */ import com.wurmonline.server.creatures.MineDoorPermission;
/*      */ import com.wurmonline.server.creatures.MineDoorSettings;
/*      */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*      */ import com.wurmonline.server.creatures.Traits;
/*      */ import com.wurmonline.server.creatures.Wagoner;
/*      */ import com.wurmonline.server.deities.Deities;
/*      */ import com.wurmonline.server.deities.Deity;
/*      */ import com.wurmonline.server.economy.Change;
/*      */ import com.wurmonline.server.economy.Economy;
/*      */ import com.wurmonline.server.economy.Shop;
/*      */ import com.wurmonline.server.epic.ValreiFightHistory;
/*      */ import com.wurmonline.server.epic.ValreiFightHistoryManager;
/*      */ import com.wurmonline.server.highways.MethodsHighways;
/*      */ import com.wurmonline.server.highways.Node;
/*      */ import com.wurmonline.server.highways.Route;
/*      */ import com.wurmonline.server.highways.Routes;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.ItemSettings;
/*      */ import com.wurmonline.server.items.ItemTemplate;
/*      */ import com.wurmonline.server.items.ItemTemplateFactory;
/*      */ import com.wurmonline.server.items.Materials;
/*      */ import com.wurmonline.server.items.NoSuchTemplateException;
/*      */ import com.wurmonline.server.items.Recipe;
/*      */ import com.wurmonline.server.items.Recipes;
/*      */ import com.wurmonline.server.items.WurmColor;
/*      */ import com.wurmonline.server.items.WurmMail;
/*      */ import com.wurmonline.server.kingdom.Kingdom;
/*      */ import com.wurmonline.server.kingdom.Kingdoms;
/*      */ import com.wurmonline.server.players.Cults;
/*      */ import com.wurmonline.server.players.Friend;
/*      */ import com.wurmonline.server.players.Permissions;
/*      */ import com.wurmonline.server.players.PermissionsByPlayer;
/*      */ import com.wurmonline.server.players.PermissionsHistories;
/*      */ import com.wurmonline.server.players.PermissionsHistoryEntry;
/*      */ import com.wurmonline.server.players.PermissionsPlayerList;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.players.PlayerInfo;
/*      */ import com.wurmonline.server.players.PlayerInfoFactory;
/*      */ import com.wurmonline.server.players.PlayerState;
/*      */ import com.wurmonline.server.players.Titles;
/*      */ import com.wurmonline.server.skills.NoSuchSkillException;
/*      */ import com.wurmonline.server.skills.Skill;
/*      */ import com.wurmonline.server.skills.SkillSystem;
/*      */ import com.wurmonline.server.skills.SkillTemplate;
/*      */ import com.wurmonline.server.skills.Skills;
/*      */ import com.wurmonline.server.skills.SkillsFactory;
/*      */ import com.wurmonline.server.structures.BridgePart;
/*      */ import com.wurmonline.server.structures.BridgePartEnum;
/*      */ import com.wurmonline.server.structures.Door;
/*      */ import com.wurmonline.server.structures.DoorSettings;
/*      */ import com.wurmonline.server.structures.Fence;
/*      */ import com.wurmonline.server.structures.FenceGate;
/*      */ import com.wurmonline.server.structures.Floor;
/*      */ import com.wurmonline.server.structures.NoSuchLockException;
/*      */ import com.wurmonline.server.structures.NoSuchStructureException;
/*      */ import com.wurmonline.server.structures.RoofFloorEnum;
/*      */ import com.wurmonline.server.structures.Structure;
/*      */ import com.wurmonline.server.structures.StructureSettings;
/*      */ import com.wurmonline.server.structures.Structures;
/*      */ import com.wurmonline.server.structures.Wall;
/*      */ import com.wurmonline.server.villages.Citizen;
/*      */ import com.wurmonline.server.villages.NoSuchVillageException;
/*      */ import com.wurmonline.server.villages.Reputation;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.villages.Villages;
/*      */ import com.wurmonline.server.zones.NoSuchZoneException;
/*      */ import com.wurmonline.server.zones.Rift;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.Zone;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import com.wurmonline.shared.constants.PlayerOnlineStatus;
/*      */ import com.wurmonline.shared.constants.ValreiConstants;
/*      */ import com.wurmonline.shared.exceptions.WurmServerException;
/*      */ import com.wurmonline.shared.util.StringUtilities;
/*      */ import java.io.IOException;
/*      */ import java.rmi.RemoteException;
/*      */ import java.text.DecimalFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Comparator;
/*      */ import java.util.Date;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import java.util.regex.Pattern;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class GmTool
/*      */   extends Question
/*      */ {
/*  161 */   private static final Logger logger = Logger.getLogger(GmTool.class.getName());
/*      */   private final byte displayType;
/*      */   private final byte displaySubType;
/*      */   private final long wurmId;
/*      */   private final String backString;
/*      */   private final byte currentPage;
/*      */   private final String searchString;
/*      */   private final int rowsPerPage;
/*  169 */   private final List<Player> playerlist = new LinkedList<>();
/*  170 */   private final List<Village> villagelist = new LinkedList<>();
/*      */   
/*      */   private static final String red = "color=\"255,127,127\"";
/*      */   private static final String green = "color=\"127,255,127\"";
/*      */   private static final String orange = "color=\"255,177,40\"";
/*  175 */   final DecimalFormat df = new DecimalFormat("#.##");
/*      */   
/*      */   public static final byte TYPE_VILLAGEID = 0;
/*      */   
/*      */   public static final byte TYPE_WURMID = 1;
/*      */   
/*      */   public static final byte TYPE_SEARCH = 2;
/*      */   
/*      */   public static final byte TYPE_DEITYID = 3;
/*      */   
/*      */   public static final byte TYPE_SERVERID = 4;
/*      */   
/*      */   public static final byte TYPE_HIGHWAYID = 5;
/*      */   
/*      */   public static final byte TYPE_WAGONERID = 6;
/*      */   
/*      */   private static final byte GO_BACK = 0;
/*      */   
/*      */   private static final byte WURM_ID = 1;
/*      */   
/*      */   private static final byte VILLAGE_ID = 2;
/*      */   
/*      */   private static final byte TILE_XY = 3;
/*      */   
/*      */   private static final byte PLAYER_NAME = 4;
/*      */   
/*      */   private static final byte STRING_WURMID = 5;
/*      */   
/*      */   private static final byte STRING_XY = 6;
/*      */   
/*      */   private static final byte PREV_PAGE = 7;
/*      */   
/*      */   private static final byte NEXT_PAGE = 8;
/*      */   private static final byte PLAYER_DROPDOWN = 9;
/*      */   private static final byte VILLAGE_DROPDOWN = 10;
/*      */   private static final byte SEARCH = 11;
/*      */   private static final byte TELEPORT_XY = 12;
/*      */   private static final byte DEITY_ID = 13;
/*      */   private static final byte SERVER_ID = 14;
/*      */   private static final byte HIGHWAY_ID = 15;
/*      */   private static final byte WAGONER_ID = 16;
/*      */   public static final byte SUMMARY = 1;
/*      */   private static final byte GUESTS = 2;
/*      */   private static final byte HISTORY = 3;
/*      */   private static final byte CITIZENS = 4;
/*      */   private static final byte ALLIES = 5;
/*      */   private static final byte KOS = 6;
/*      */   private static final byte ROLES = 7;
/*      */   private static final byte BRANDED = 8;
/*      */   private static final byte SETTINGS = 9;
/*      */   private static final byte BONUS = 4;
/*      */   private static final byte AFFINITY = 6;
/*      */   private static final byte PRIESTS = 7;
/*      */   private static final byte FOLLOWERS = 8;
/*      */   private static final byte ALTARS = 9;
/*      */   private static final byte VALREIFIGHTS = 10;
/*      */   private static final byte FRIENDS = 4;
/*      */   private static final byte SKILLS = 5;
/*      */   private static final byte BANK = 6;
/*      */   private static final byte TITLES = 7;
/*      */   private static final byte INVENTORY = 8;
/*      */   private static final byte BODY = 9;
/*      */   private static final byte CARING_FOR = 10;
/*      */   private static final byte MAIL_SENT = 11;
/*      */   private static final byte MAIL_WAITING = 12;
/*      */   private static final byte HISTORY_IP = 13;
/*      */   private static final byte HISTORY_EMAIL = 14;
/*      */   private static final byte IGNORING = 15;
/*      */   private static final byte CORPSES = 16;
/*      */   private static final byte REFERRS = 17;
/*      */   private static final byte KOSLIST = 18;
/*      */   private static final byte BUILDINGS = 19;
/*      */   private static final byte CARTS = 20;
/*      */   private static final byte SHIPS = 21;
/*      */   private static final byte MINEDOORS = 22;
/*      */   private static final byte GATES = 23;
/*      */   private static final byte PATH = 24;
/*      */   private static final byte TILES = 5;
/*      */   private static final byte WALLS = 6;
/*      */   private static final byte FLOORS = 7;
/*      */   private static final byte BRIDGE_PARTS = 8;
/*      */   private static final byte CREATURES = 9;
/*      */   private static final byte FENCES = 10;
/*      */   private static final byte ITEMS = 11;
/*      */   private static final byte TRAITS = 12;
/*      */   private static final byte MAIL_ITEMS = 13;
/*      */   private static final byte GM_SIGNS = 14;
/*      */   private static final byte SERVERS = 15;
/*      */   private static final byte TRADERS = 16;
/*      */   private static final byte NAMED_RECIPIES = 17;
/*      */   private static final byte RIFTS = 18;
/*      */   private static final byte ROUTELIST = 19;
/*      */   private static final byte ROUTESUMMARY = 20;
/*      */   private static final byte NODELIST = 21;
/*      */   private static final byte NODESUMMARY = 22;
/*      */   private static final byte CATSEYES = 23;
/*      */   private static final byte AVATARS = 24;
/*      */   private static final byte WAGONERLIST = 25;
/*      */   private static final byte KINGDOMS = 26;
/*      */   public static final byte SEARCH_BY_EMAIL = 1;
/*      */   public static final byte SEARCH_BY_IP = 2;
/*      */   
/*      */   public GmTool(Creature aResponder, long aTargetId) {
/*  278 */     super(aResponder, "InGame Web Interface lookalike", "----- " + 
/*  279 */         makeTitle((byte)1, (byte)1, aTargetId, "") + " -----", 101, 
/*  280 */         findWand(aResponder));
/*  281 */     this.displayType = 1;
/*  282 */     this.displaySubType = 1;
/*  283 */     this.wurmId = aTargetId;
/*  284 */     this.searchString = "";
/*  285 */     this.backString = "";
/*  286 */     this.currentPage = 0;
/*  287 */     this.rowsPerPage = 50;
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
/*      */   public GmTool(Creature aResponder, byte aType, byte aSubType, long aId, String aSearch, String aBack, int aRowsPerPage, byte aPage) {
/*  299 */     super(aResponder, "GM Tool", "----- " + 
/*  300 */         makeTitle(aType, aSubType, aId, aSearch) + " -----", 101, 
/*  301 */         findWand(aResponder));
/*  302 */     this.displayType = aType;
/*  303 */     this.displaySubType = aSubType;
/*  304 */     this.wurmId = aId;
/*  305 */     this.searchString = aSearch;
/*  306 */     this.backString = aBack;
/*  307 */     this.currentPage = aPage;
/*  308 */     this.rowsPerPage = aRowsPerPage;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static long findWand(Creature performer) {
/*  314 */     int wand = (performer.getPower() >= 4) ? 176 : 315;
/*  315 */     Item[] inv = performer.getInventory().getAllItems(false);
/*  316 */     for (Item item : inv) {
/*      */       
/*  318 */       if (item.getTemplateId() == wand)
/*  319 */         return item.getWurmId(); 
/*      */     } 
/*  321 */     return -10L;
/*      */   }
/*      */ 
/*      */   
/*      */   static String makeTitle(byte aType, byte aSubType, long aId, String aSearch) {
/*  326 */     String strType = "Village";
/*  327 */     if (aType == 0) {
/*      */       
/*  329 */       if (aSubType == 1) {
/*  330 */         strType = "Summary";
/*  331 */       } else if (aSubType == 4) {
/*  332 */         strType = "Citizens";
/*  333 */       } else if (aSubType == 5) {
/*  334 */         strType = "Allies";
/*  335 */       } else if (aSubType == 6) {
/*  336 */         strType = "KOS list";
/*  337 */       } else if (aSubType == 7) {
/*  338 */         strType = "Roles";
/*  339 */       } else if (aSubType == 8) {
/*  340 */         strType = "Branded animals";
/*  341 */       } else if (aSubType == 3) {
/*  342 */         strType = "History";
/*  343 */       } else if (aSubType == 9) {
/*  344 */         strType = "Settings";
/*  345 */       }  return strType + " of Village with Id:" + aId;
/*      */     } 
/*  347 */     if (aType == 3) {
/*      */       
/*  349 */       if (aId == -10L)
/*  350 */         return "List of deities"; 
/*  351 */       if (aSubType == 1) {
/*  352 */         strType = "Summary";
/*  353 */       } else if (aSubType == 4) {
/*  354 */         strType = "Bonuses";
/*  355 */       } else if (aSubType == 6) {
/*  356 */         strType = "Affinities";
/*  357 */       } else if (aSubType == 7) {
/*  358 */         strType = "Priests";
/*  359 */       } else if (aSubType == 8) {
/*  360 */         strType = "Followers";
/*  361 */       } else if (aSubType == 9) {
/*  362 */         strType = "Altars";
/*  363 */       } else if (aSubType == 10) {
/*  364 */         return "Valrei Fight with FightId: " + aId;
/*  365 */       }  return strType + " of Deity with Id:" + aId;
/*      */     } 
/*  367 */     if (aType == 4) {
/*      */       
/*  369 */       if (aSubType == 15 || (aSubType == 1 && aId == -10L)) {
/*  370 */         strType = "Server List";
/*  371 */       } else if (aSubType == 1) {
/*  372 */         strType = "Summary of Server with id:" + aId;
/*  373 */       } else if (aSubType == 5) {
/*  374 */         strType = "Top Skills";
/*  375 */       } else if (aSubType == 16) {
/*  376 */         strType = "Traders";
/*  377 */       } else if (aSubType == 17) {
/*  378 */         strType = "Named Recipes";
/*  379 */       } else if (aSubType == 18) {
/*  380 */         strType = "Rifts";
/*  381 */       } else if (aSubType == 19) {
/*  382 */         strType = "Highway Routes";
/*  383 */       } else if (aSubType == 21) {
/*  384 */         strType = "Highway Nodes";
/*  385 */       } else if (aSubType == 24) {
/*  386 */         strType = "Avatars";
/*  387 */       } else if (aSubType == 25) {
/*  388 */         strType = "Wagoners";
/*  389 */       } else if (aSubType == 26) {
/*  390 */         strType = "Kingdoms";
/*  391 */       }  return strType;
/*      */     } 
/*  393 */     if (aType == 5) {
/*      */       
/*  395 */       if (aSubType == 20) {
/*  396 */         strType = "Summary of Highway Route with id:" + aId;
/*  397 */       } else if (aSubType == 19) {
/*  398 */         strType = "Highway Routes";
/*  399 */       } else if (aSubType == 22) {
/*  400 */         strType = "Summary of Highway Node with id:" + aId;
/*  401 */       } else if (aSubType == 21) {
/*  402 */         strType = "Highway Nodes";
/*  403 */       } else if (aSubType == 23) {
/*  404 */         strType = "Catseyes on Route with id:" + aId;
/*      */       } else {
/*  406 */         strType = "Highway Route with id:" + aId;
/*  407 */       }  return strType;
/*      */     } 
/*  409 */     if (aType == 6) {
/*      */       
/*  411 */       if (aSubType == 25) {
/*  412 */         strType = "Wagoner list";
/*  413 */       } else if (aSubType == 1) {
/*  414 */         strType = "Summary of Wagoner with id:" + aId;
/*  415 */       }  return strType;
/*      */     } 
/*  417 */     if (aType == 1) {
/*      */       
/*  419 */       int idType = WurmId.getType(aId);
/*  420 */       strType = "(" + Integer.toString(idType) + ")";
/*  421 */       if (idType == 0) {
/*      */         
/*  423 */         if (aSubType == 1) {
/*  424 */           strType = "Summary";
/*  425 */         } else if (aSubType == 4) {
/*  426 */           strType = "Friends";
/*  427 */         } else if (aSubType == 5) {
/*  428 */           strType = "Skills";
/*  429 */         } else if (aSubType == 6) {
/*  430 */           strType = "Bank";
/*  431 */         } else if (aSubType == 7) {
/*  432 */           strType = "Titles";
/*  433 */         } else if (aSubType == 8) {
/*  434 */           strType = "Inventory";
/*  435 */         } else if (aSubType == 9) {
/*  436 */           strType = "Body";
/*  437 */         } else if (aSubType == 10) {
/*  438 */           strType = "Caring for";
/*  439 */         } else if (aSubType == 11) {
/*  440 */           strType = "Mail Sent";
/*  441 */         } else if (aSubType == 12) {
/*  442 */           strType = "Mail Waiting";
/*  443 */         } else if (aSubType == 13) {
/*  444 */           strType = "IP History";
/*  445 */         } else if (aSubType == 14) {
/*  446 */           strType = "Email History";
/*  447 */         } else if (aSubType == 15) {
/*  448 */           strType = "Ignore List";
/*  449 */         } else if (aSubType == 16) {
/*  450 */           strType = "Corpses";
/*  451 */         } else if (aSubType == 17) {
/*  452 */           strType = "Referrs";
/*  453 */         } else if (aSubType == 18) {
/*  454 */           strType = "On Kos";
/*  455 */         } else if (aSubType == 19) {
/*  456 */           strType = "Owned Buildings";
/*  457 */         } else if (aSubType == 20) {
/*  458 */           strType = "Owned Carts";
/*  459 */         } else if (aSubType == 21) {
/*  460 */           strType = "Owned Ships";
/*  461 */         } else if (aSubType == 22) {
/*  462 */           strType = "Owned Minedoors";
/*  463 */         } else if (aSubType == 23) {
/*  464 */           strType = "Owned Gates";
/*  465 */         } else if (aSubType == 24) {
/*  466 */           strType = "Highway Route";
/*      */         } 
/*  468 */         return strType + " of Player with Id:" + aId;
/*      */       } 
/*  470 */       if (idType == 1) {
/*      */         
/*  472 */         if (aSubType == 1) {
/*  473 */           strType = "Summary";
/*  474 */         } else if (aSubType == 2) {
/*  475 */           strType = "Permissionss";
/*  476 */         } else if (aSubType == 3) {
/*  477 */           strType = "History";
/*  478 */         } else if (aSubType == 8) {
/*  479 */           strType = "Inventory";
/*  480 */         } else if (aSubType == 9) {
/*  481 */           strType = "Body";
/*  482 */         } else if (aSubType == 12) {
/*  483 */           strType = "Traits";
/*  484 */         }  return strType + " of Creature with Id:" + aId;
/*      */       } 
/*  486 */       if (idType == 2) {
/*      */         
/*  488 */         if (aSubType == 1) {
/*  489 */           strType = "Summary of Item";
/*  490 */         } else if (aSubType == 2) {
/*  491 */           strType = "Permissionss";
/*  492 */         } else if (aSubType == 3) {
/*  493 */           strType = "History";
/*  494 */         } else if (aSubType == 11) {
/*  495 */           strType = "Items in Container";
/*  496 */         }  return strType + " with Id:" + aId;
/*      */       } 
/*  498 */       if (idType == 3) {
/*      */         
/*  500 */         if (aSubType == 1) {
/*  501 */           strType = "Summary";
/*  502 */         } else if (aSubType == 2) {
/*  503 */           strType = "Permissionss";
/*  504 */         } else if (aSubType == 3) {
/*  505 */           strType = "History";
/*  506 */         } else if (aSubType == 9) {
/*  507 */           strType = "Creatures";
/*  508 */         } else if (aSubType == 6) {
/*  509 */           strType = "Walls";
/*  510 */         } else if (aSubType == 10) {
/*  511 */           strType = "Fences";
/*  512 */         } else if (aSubType == 11) {
/*  513 */           strType = "Items";
/*  514 */         } else if (aSubType == 7) {
/*  515 */           strType = "Floors";
/*  516 */         } else if (aSubType == 8) {
/*  517 */           strType = "Bridge Part";
/*  518 */         } else if (aSubType == 13) {
/*  519 */           strType = "Items in Mail";
/*  520 */         } else if (aSubType == 14) {
/*  521 */           return "GM Signs";
/*  522 */         }  return strType + " on Tile with Id:" + aId;
/*      */       } 
/*  524 */       if (idType == 4) {
/*      */         
/*  526 */         if (aSubType == 1) {
/*  527 */           strType = "Summary";
/*  528 */         } else if (aSubType == 2) {
/*  529 */           strType = "Guests";
/*  530 */         } else if (aSubType == 3) {
/*  531 */           strType = "History";
/*  532 */         } else if (aSubType == 5) {
/*  533 */           strType = "Tiles";
/*  534 */         } else if (aSubType == 6) {
/*  535 */           strType = "Walls";
/*  536 */         } else if (aSubType == 10) {
/*  537 */           strType = "Fences";
/*  538 */         } else if (aSubType == 7) {
/*  539 */           strType = "Floors";
/*  540 */         } else if (aSubType == 8) {
/*  541 */           strType = "Bridge parts";
/*  542 */         }  return strType + " of Structure with Id:" + aId;
/*      */       } 
/*  544 */       if (idType == 5) {
/*      */         
/*  546 */         if (aSubType == 1) {
/*  547 */           strType = "Summary";
/*      */         }
/*  549 */         else if (aSubType == 2) {
/*  550 */           strType = "Permissionss";
/*  551 */         } else if (aSubType == 3) {
/*  552 */           strType = "History";
/*  553 */         }  return strType + " of Walls with Id:" + aId;
/*      */       } 
/*  555 */       if (idType == 6)
/*  556 */         return "Temp Items with Id:" + aId; 
/*  557 */       if (idType == 7) {
/*      */         
/*  559 */         if (aSubType == 1) {
/*  560 */           strType = "Summary";
/*      */         }
/*  562 */         else if (aSubType == 2) {
/*  563 */           strType = "Permissionss";
/*  564 */         } else if (aSubType == 3) {
/*  565 */           strType = "History";
/*  566 */         }  return strType + " of Fence with Id:" + aId;
/*      */       } 
/*  568 */       if (idType == 8 || idType == 32)
/*  569 */         return "Wounds with Id:" + aId; 
/*  570 */       if (idType == 9)
/*  571 */         return "Creature Skill with Id:" + aId; 
/*  572 */       if (idType == 10)
/*  573 */         return "Player Skill with Id:" + aId; 
/*  574 */       if (idType == 31)
/*  575 */         return "Temporary Skill with Id:" + aId; 
/*  576 */       if (idType == 11)
/*  577 */         return "Template Skill with Id:" + aId; 
/*  578 */       if (idType == 12)
/*  579 */         return "Tile Border with Id:" + aId; 
/*  580 */       if (idType == 13)
/*  581 */         return "Banks with Id:" + aId; 
/*  582 */       if (idType == 14)
/*  583 */         return "Planets with Id:" + aId; 
/*  584 */       if (idType == 15)
/*  585 */         return "Spells with Id:" + aId; 
/*  586 */       if (idType == 16)
/*  587 */         return "Plans with Id:" + aId; 
/*  588 */       if (idType == 17) {
/*      */         
/*  590 */         if (aSubType == 1) {
/*  591 */           strType = "Summary";
/*  592 */         } else if (aSubType == 9) {
/*  593 */           strType = "Creatures";
/*  594 */         }  return strType + " of Cave Tile with Id:" + aId;
/*      */       } 
/*  596 */       if (idType == 18)
/*  597 */         return "Skill Ids with Id:" + aId; 
/*  598 */       if (idType == 19)
/*  599 */         return "Body Ids with Id:" + aId; 
/*  600 */       if (idType == 20)
/*  601 */         return "Coin Ids with Id:" + aId; 
/*  602 */       if (idType == 21)
/*  603 */         return "WC Commands with Id:" + aId; 
/*  604 */       if (idType == 22)
/*  605 */         return "Mission Performed with Id:" + aId; 
/*  606 */       if (idType == 23)
/*  607 */         return "Floors with Id:" + aId; 
/*  608 */       if (idType == 28) {
/*  609 */         return "Bridge part with Id:" + aId;
/*      */       }
/*  611 */       return "Unknown type(" + idType + ") with Id:" + aId;
/*      */     } 
/*  613 */     if (aType == 2) {
/*      */       
/*  615 */       if (aSubType == 1)
/*  616 */         return "Searching by email " + aSearch; 
/*  617 */       if (aSubType == 2) {
/*  618 */         return "Searching by IP " + aSearch;
/*      */       }
/*  620 */       return "Unknown search type (" + aSubType + ") for " + aSearch;
/*      */     } 
/*      */     
/*  623 */     return "Unknown type(" + aType + ") with Id:" + aId + " and Search " + aSearch;
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
/*  634 */     setAnswer(aAnswer);
/*  635 */     if (this.type == 0) {
/*      */       
/*  637 */       logger.log(Level.INFO, "Received answer for a question with NOQUESTION.");
/*      */       return;
/*      */     } 
/*  640 */     if (this.type == 101)
/*      */     {
/*  642 */       if (getResponder().getPower() >= 2) {
/*      */         
/*  644 */         String sAction = aAnswer.getProperty("tid");
/*  645 */         byte iAction = 0;
/*  646 */         byte qType = 1;
/*  647 */         byte qSubType = 1;
/*  648 */         long wId = this.wurmId;
/*  649 */         String sSearch = "";
/*  650 */         byte iPage = 0;
/*  651 */         String strback = aAnswer.getProperty("back");
/*  652 */         String strcurrent = aAnswer.getProperty("current");
/*  653 */         String strRowsPerPage = aAnswer.getProperty("rpp");
/*  654 */         int tableRowsPerPage = Integer.parseInt(strRowsPerPage);
/*      */ 
/*      */         
/*  657 */         if (strback.length() > 0) {
/*      */ 
/*      */           
/*  660 */           String[] backs = strback.split(Pattern.quote("|"));
/*  661 */           if (backs.length >= 5) {
/*  662 */             String sBack = backs[1] + "|" + backs[2] + "|" + backs[3] + "|" + backs[4] + "|" + strcurrent;
/*      */           } else {
/*  664 */             String sBack = strback + "|" + strcurrent;
/*      */           } 
/*      */         } else {
/*  667 */           String sBack = strcurrent;
/*      */         } 
/*  669 */         String[] parts = sAction.split(",");
/*      */         
/*  671 */         if (parts.length > 0) {
/*      */           String str;
/*  673 */           iAction = Byte.parseByte(parts[0]);
/*  674 */           if (iAction == 7) {
/*      */             
/*  676 */             iPage = (byte)(this.currentPage - 1);
/*  677 */             str = strback;
/*  678 */             qType = this.displayType;
/*  679 */             qSubType = this.displaySubType;
/*  680 */             sSearch = this.searchString;
/*      */           }
/*  682 */           else if (iAction == 8) {
/*      */             
/*  684 */             iPage = (byte)(this.currentPage + 1);
/*  685 */             str = strback;
/*  686 */             qType = this.displayType;
/*  687 */             qSubType = this.displaySubType;
/*  688 */             sSearch = this.searchString;
/*      */           }
/*  690 */           else if (iAction == 0) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  696 */             String[] backs = strback.split(Pattern.quote("|"));
/*  697 */             if (backs.length > 0) {
/*      */ 
/*      */ 
/*      */ 
/*      */               
/*  702 */               StringBuilder buf = new StringBuilder();
/*  703 */               if (backs.length > 1) {
/*      */                 
/*  705 */                 buf.append(backs[0]);
/*  706 */                 for (int s = 1; s < backs.length - 1; s++) {
/*  707 */                   buf.append("|" + backs[s]);
/*      */                 }
/*      */               } 
/*  710 */               String[] lparts = backs[backs.length - 1].split(",");
/*  711 */               qType = Byte.parseByte(lparts[0]);
/*  712 */               qSubType = Byte.parseByte(lparts[1]);
/*  713 */               wId = Long.parseLong(lparts[2]);
/*      */ 
/*      */               
/*  716 */               if (lparts.length > 3)
/*  717 */                 sSearch = lparts[3]; 
/*  718 */               str = buf.toString();
/*      */             } else {
/*      */               
/*      */               return;
/*      */             } 
/*  723 */           } else if (iAction == 1) {
/*      */ 
/*      */             
/*  726 */             wId = Long.parseLong(parts[1]);
/*  727 */             qSubType = Byte.parseByte(parts[2]);
/*      */           }
/*  729 */           else if (iAction == 2) {
/*      */ 
/*      */             
/*  732 */             qType = 0;
/*  733 */             wId = Long.parseLong(parts[1]);
/*  734 */             qSubType = Byte.parseByte(parts[2]);
/*      */           }
/*  736 */           else if (iAction == 13) {
/*      */ 
/*      */             
/*  739 */             qType = 3;
/*  740 */             wId = Long.parseLong(parts[1]);
/*  741 */             qSubType = Byte.parseByte(parts[2]);
/*      */           }
/*  743 */           else if (iAction == 14) {
/*      */ 
/*      */             
/*  746 */             qType = 4;
/*  747 */             wId = Long.parseLong(parts[1]);
/*  748 */             qSubType = Byte.parseByte(parts[2]);
/*      */           }
/*  750 */           else if (iAction == 15) {
/*      */ 
/*      */             
/*  753 */             qType = 5;
/*  754 */             wId = Long.parseLong(parts[1]);
/*  755 */             qSubType = Byte.parseByte(parts[2]);
/*      */           }
/*  757 */           else if (iAction == 16) {
/*      */ 
/*      */             
/*  760 */             qType = 6;
/*  761 */             wId = Long.parseLong(parts[1]);
/*  762 */             qSubType = Byte.parseByte(parts[2]);
/*      */           }
/*  764 */           else if (iAction == 3) {
/*      */ 
/*      */             
/*  767 */             int tilex = Integer.parseInt(parts[1]);
/*  768 */             int tiley = Integer.parseInt(parts[2]);
/*  769 */             boolean surfaced = Boolean.parseBoolean(parts[3]);
/*      */ 
/*      */             
/*  772 */             wId = Tiles.getTileId(tilex, tiley, 0, surfaced);
/*      */           } else {
/*  774 */             if (iAction == 12) {
/*      */ 
/*      */               
/*  777 */               int tilex = Integer.parseInt(parts[1]);
/*  778 */               int tiley = Integer.parseInt(parts[2]);
/*  779 */               boolean surfaced = Boolean.parseBoolean(parts[3]);
/*      */               
/*      */               try {
/*  782 */                 Item wand = Items.getItem(this.target);
/*  783 */                 getResponder().setTeleportLayer(surfaced ? 0 : -1);
/*  784 */                 if (!surfaced)
/*      */                 {
/*  786 */                   if (Tiles.isSolidCave(Tiles.decodeType(Server.caveMesh.getTile(tilex, tiley))))
/*      */                   {
/*  788 */                     getResponder().getCommunicator().sendNormalServerMessage("The tile " + tilex + ", " + tiley + " is solid cave.");
/*      */                   }
/*      */                 }
/*      */                 
/*  792 */                 wand.setData(tilex, tiley);
/*      */                 
/*  794 */                 getResponder().getCommunicator().sendNormalServerMessage("You quietly mumble: " + tilex + ", " + tiley + " surfaced=" + surfaced);
/*      */               
/*      */               }
/*  797 */               catch (NoSuchItemException e) {
/*      */                 
/*  799 */                 getResponder().getCommunicator().sendNormalServerMessage("Cannot find wand");
/*      */               } 
/*      */               
/*      */               return;
/*      */             } 
/*      */             
/*  805 */             if (iAction == 4) {
/*      */ 
/*      */ 
/*      */               
/*  809 */               String pname = aAnswer.getProperty("pname");
/*  810 */               if (pname != null)
/*      */               {
/*  812 */                 PlayerInfo playerInfo = PlayerInfoFactory.createPlayerInfo(pname);
/*      */                 
/*      */                 try {
/*  815 */                   playerInfo.load();
/*      */                 }
/*  817 */                 catch (IOException iox) {
/*      */                   
/*  819 */                   getResponder().getCommunicator().sendNormalServerMessage("Failed to load data for the player with name " + pname + ".");
/*      */                   
/*      */                   return;
/*      */                 } 
/*  823 */                 if (playerInfo == null || playerInfo.wurmId <= 0L) {
/*      */ 
/*      */                   
/*  826 */                   long[] info = { Servers.localServer.id, -1L };
/*      */                   
/*  828 */                   LoginServerWebConnection lsw = new LoginServerWebConnection();
/*      */                   
/*      */                   try {
/*  831 */                     info = lsw.getCurrentServer(pname, -1L);
/*      */                   }
/*  833 */                   catch (Exception e) {
/*      */                     
/*  835 */                     info = new long[] { -1L, -1L };
/*      */                   } 
/*      */                   
/*  838 */                   if (info[0] == -1L) {
/*  839 */                     getResponder().getCommunicator().sendNormalServerMessage("Player with name " + pname + " not found anywhere!.");
/*      */                   } else {
/*      */                     
/*  842 */                     getResponder().getCommunicator().sendNormalServerMessage("Player with name " + pname + " has never been on this server, but is currently on server " + info[0] + ", their WurmId is " + info[1] + ".");
/*      */                   } 
/*      */                   
/*      */                   return;
/*      */                 } 
/*      */                 
/*  848 */                 wId = playerInfo.wurmId;
/*      */               }
/*      */             
/*  851 */             } else if (iAction == 5) {
/*      */ 
/*      */               
/*  854 */               String strWurmId = aAnswer.getProperty("wurmid");
/*      */               
/*  856 */               if (strWurmId != null && strWurmId.length() > 0) {
/*      */ 
/*      */                 
/*      */                 try {
/*  860 */                   wId = Long.parseLong(strWurmId);
/*      */                 }
/*  862 */                 catch (Exception e) {
/*      */                   
/*  864 */                   getResponder().getCommunicator().sendNormalServerMessage("Wurm Id is not a number!");
/*      */ 
/*      */                   
/*      */                   return;
/*      */                 } 
/*      */               } else {
/*  870 */                 getResponder().getCommunicator().sendNormalServerMessage("name missing");
/*      */                 
/*      */                 return;
/*      */               } 
/*  874 */             } else if (iAction == 6) {
/*      */ 
/*      */               
/*  877 */               String strtilex = aAnswer.getProperty("tilex");
/*  878 */               String strtiley = aAnswer.getProperty("tiley");
/*  879 */               String strtilesurface = aAnswer.getProperty("tilesurface");
/*  880 */               int tilex = Integer.parseInt(strtilex);
/*  881 */               int tiley = Integer.parseInt(strtiley);
/*  882 */               boolean surfaced = (Integer.parseInt(strtilesurface) == 0);
/*      */               
/*  884 */               wId = Tiles.getTileId(tilex, tiley, 0, surfaced);
/*      */             }
/*  886 */             else if (iAction == 9) {
/*      */               
/*  888 */               String pid = aAnswer.getProperty("playerid");
/*  889 */               int listid = Integer.parseInt(pid);
/*      */               
/*  891 */               Player p = this.playerlist.get(listid);
/*  892 */               if (p == null) {
/*      */                 
/*  894 */                 getResponder().getCommunicator().sendNormalServerMessage("No player found.");
/*      */                 return;
/*      */               } 
/*  897 */               wId = p.getWurmId();
/*      */             }
/*  899 */             else if (iAction == 10) {
/*      */               
/*  901 */               String vid = aAnswer.getProperty("villid");
/*  902 */               int vidd = Integer.parseInt(vid);
/*  903 */               Village vill = this.villagelist.get(vidd);
/*  904 */               if (vill == null) {
/*      */                 
/*  906 */                 getResponder().getCommunicator().sendNormalServerMessage("No village found.");
/*      */                 return;
/*      */               } 
/*  909 */               qType = 0;
/*  910 */               wId = vill.id;
/*      */             }
/*  912 */             else if (iAction == 11) {
/*      */               
/*  914 */               qType = 2;
/*  915 */               qSubType = Byte.parseByte(parts[1]);
/*  916 */               if (qSubType == 1) {
/*  917 */                 sSearch = aAnswer.getProperty("searchemail");
/*      */               } else {
/*  919 */                 sSearch = aAnswer.getProperty("searchip");
/*      */               } 
/*  921 */               if (sSearch.length() < 1) {
/*      */                 
/*  923 */                 getResponder().getCommunicator().sendNormalServerMessage("Nothing to search for!");
/*      */ 
/*      */                 
/*      */                 return;
/*      */               } 
/*      */             } else {
/*      */               return;
/*      */             } 
/*      */           } 
/*      */           
/*  933 */           GmTool gt = new GmTool(getResponder(), qType, qSubType, wId, sSearch, str, tableRowsPerPage, iPage);
/*  934 */           gt.sendQuestion();
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
/*      */   public void sendQuestion() {
/*  949 */     String mess = "Running GM Tool for " + makeTitle(this.displayType, this.displaySubType, this.wurmId, this.searchString);
/*  950 */     logger.log(Level.INFO, getResponder().getName() + " " + mess);
/*  951 */     long startTime = System.currentTimeMillis();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  960 */     getResponder().getCommunicator().sendNormalServerMessage(mess);
/*      */     
/*  962 */     StringBuilder buf = new StringBuilder();
/*  963 */     buf.append("border{center{text{type='bold';text=\"" + this.question + "\"}};null;scroll{vertical=\"true\";horizontal=\"true\";varray{passthrough{id=\"id\";text=\"" + 
/*      */         
/*  965 */         getId() + "\"}");
/*  966 */     if (getResponder().getPower() >= 2) {
/*      */       
/*      */       try {
/*      */         
/*  970 */         buf.append("passthrough{id=\"back\";text=\"" + this.backString + "\"}");
/*  971 */         buf.append("passthrough{id=\"current\";text=\"" + this.displayType + "," + this.displaySubType + "," + this.wurmId + "," + this.searchString + "\"}");
/*      */         
/*  973 */         PlayerInfo pInfo = null;
/*      */ 
/*      */         
/*  976 */         if (this.displayType == 0) {
/*      */           
/*      */           try
/*      */           {
/*  980 */             Village aVillage = Villages.getVillage((int)this.wurmId);
/*  981 */             String vd = villageDetails(aVillage);
/*  982 */             if (vd.length() == 0)
/*      */               return; 
/*  984 */             buf.append(vd);
/*      */           }
/*  986 */           catch (NoSuchVillageException e1)
/*      */           {
/*  988 */             logger.log(Level.WARNING, e1.getMessage(), (Throwable)e1);
/*  989 */             getResponder().getCommunicator().sendNormalServerMessage("Village not found");
/*      */           }
/*      */         
/*  992 */         } else if (this.displayType == 3) {
/*      */           
/*  994 */           if (this.wurmId == -10L) {
/*  995 */             buf.append(deityDetails((Deity)null, this.wurmId));
/*      */           } else {
/*      */             
/*  998 */             Deity deity = Deities.getDeity((int)this.wurmId);
/*  999 */             if (deity != null || this.displaySubType == 10) {
/* 1000 */               buf.append(deityDetails(deity, this.wurmId));
/*      */             } else {
/* 1002 */               buf.append("label{text=\"no such deity\"}");
/*      */             } 
/*      */           } 
/* 1005 */         } else if (this.displayType == 4) {
/*      */           
/* 1007 */           if (this.wurmId != -10L) {
/*      */             
/* 1009 */             ServerEntry sentry = Servers.getServerWithId((int)this.wurmId);
/* 1010 */             if (sentry != null) {
/* 1011 */               buf.append(serverDetails(sentry));
/*      */             } else {
/* 1013 */               buf.append("label{text=\"no such server\"}");
/*      */             } 
/*      */           } else {
/* 1016 */             buf.append(serverDetails((ServerEntry)null));
/*      */           } 
/* 1018 */         } else if (this.displayType == 5) {
/*      */ 
/*      */           
/* 1021 */           if (this.wurmId != -10L) {
/*      */             
/* 1023 */             if (this.displaySubType == 23 || this.displaySubType == 20) {
/*      */               
/* 1025 */               Route route = Routes.getRoute((int)this.wurmId);
/* 1026 */               if (route != null) {
/* 1027 */                 buf.append(highwayDetails(route, (Node)null));
/*      */               } else {
/* 1029 */                 buf.append("label{text=\"no such route\"}");
/*      */               } 
/*      */             } else {
/*      */               
/* 1033 */               Node node = Routes.getNode(this.wurmId);
/* 1034 */               if (node != null) {
/* 1035 */                 buf.append(highwayDetails((Route)null, node));
/*      */               } else {
/* 1037 */                 buf.append("label{text=\"no such node\"}");
/*      */               }
/*      */             
/*      */             } 
/*      */           } else {
/*      */             
/* 1043 */             buf.append(highwayDetails((Route)null, (Node)null));
/*      */           }
/*      */         
/* 1046 */         } else if (this.displayType == 6) {
/*      */ 
/*      */           
/* 1049 */           if (this.wurmId != -10L)
/*      */           {
/*      */ 
/*      */ 
/*      */             
/* 1054 */             Wagoner wagoner = Wagoner.getWagoner(this.wurmId);
/* 1055 */             if (wagoner != null) {
/* 1056 */               buf.append(wagonerDetails(wagoner));
/*      */             } else {
/* 1058 */               buf.append("label{text=\"no such wagoner\"}");
/*      */             }
/*      */           
/*      */           }
/*      */           else
/*      */           {
/* 1064 */             buf.append(wagonerDetails((Wagoner)null));
/*      */           }
/*      */         
/* 1067 */         } else if (this.displayType == 2) {
/*      */           
/* 1069 */           if (this.displaySubType == 1) {
/*      */             
/* 1071 */             PlayerInfo[] pInfos = PlayerInfoFactory.getPlayerInfosWithEmail(this.searchString);
/*      */             
/* 1073 */             Arrays.sort((Object[])pInfos);
/* 1074 */             buf.append(playerInfoTable(pInfos));
/*      */           }
/* 1076 */           else if (this.displaySubType == 2) {
/*      */             
/* 1078 */             PlayerInfo[] pInfos = PlayerInfoFactory.getPlayerInfosWithIpAddress(this.searchString);
/*      */             
/* 1080 */             Arrays.sort((Object[])pInfos);
/* 1081 */             buf.append(playerInfoTable(pInfos));
/*      */           } else {
/*      */             
/* 1084 */             buf.append("label{text=\"Not done (yet)\"};");
/*      */           } 
/*      */         } else {
/*      */           
/* 1088 */           int idType = WurmId.getType(this.wurmId);
/*      */           
/* 1090 */           if (idType == 0) {
/*      */             
/* 1092 */             pInfo = PlayerInfoFactory.getPlayerInfoWithWurmId(this.wurmId);
/*      */             
/* 1094 */             if (pInfo != null)
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 1100 */               buf.append(playerDetails(pInfo));
/*      */             }
/*      */           }
/* 1103 */           else if (idType == 4) {
/*      */             
/* 1105 */             Structure lStructure = Structures.getStructure(this.wurmId);
/* 1106 */             if (lStructure != null && lStructure.isTypeHouse()) {
/* 1107 */               buf.append(buildingDetails(lStructure));
/* 1108 */             } else if (lStructure != null && lStructure.isTypeBridge()) {
/* 1109 */               buf.append(bridgeDetails(lStructure));
/*      */             } else {
/* 1111 */               buf.append("label{text=\"Error - No such Structure\"};");
/*      */             } 
/* 1113 */           } else if (idType == 2) {
/* 1114 */             buf.append(itemDetails());
/* 1115 */           } else if (idType == 3 || idType == 17) {
/* 1116 */             buf.append(tileDetails(idType));
/* 1117 */           } else if (idType == 5) {
/* 1118 */             buf.append(wallDetails());
/* 1119 */           } else if (idType == 1) {
/* 1120 */             buf.append(creatureDetails());
/* 1121 */           } else if (idType == 7) {
/* 1122 */             buf.append(fenceDetails());
/* 1123 */           } else if (idType == 20) {
/* 1124 */             buf.append(itemDetails());
/* 1125 */           } else if (idType == 19) {
/* 1126 */             buf.append(itemDetails());
/* 1127 */           } else if (idType == 23) {
/* 1128 */             buf.append(tileDetails(idType));
/* 1129 */           } else if (idType == 28) {
/* 1130 */             buf.append(tileDetails(idType));
/*      */           } else {
/* 1132 */             buf.append("label{text=\"Not done (yet)\"};");
/*      */           } 
/*      */         } 
/*      */         
/* 1136 */         buf.append("label{type=\"bold\";text=\"--------------------------- Table rows per page -------------------\"}");
/* 1137 */         buf.append("harray{");
/* 1138 */         buf.append("radio{group=\"rpp\";id=\"25\"" + ((this.rowsPerPage == 25) ? ";selected=\"true\"" : "") + "};label{text=\"25\"};");
/* 1139 */         buf.append("radio{group=\"rpp\";id=\"50\"" + ((this.rowsPerPage == 50) ? ";selected=\"true\"" : "") + "};label{text=\"50\"};");
/* 1140 */         buf.append("radio{group=\"rpp\";id=\"100\"" + ((this.rowsPerPage == 100) ? ";selected=\"true\"" : "") + "};label{text=\"100\"};");
/* 1141 */         buf.append("radio{group=\"rpp\";id=\"250\"" + ((this.rowsPerPage == 250) ? ";selected=\"true\"" : "") + "};label{text=\"250\"};");
/* 1142 */         buf.append("radio{group=\"rpp\";id=\"500\"" + ((this.rowsPerPage == 500) ? ";selected=\"true\"" : "") + "};label{text=\"500\"};");
/* 1143 */         buf.append("radio{group=\"rpp\";id=\"1000\"" + ((this.rowsPerPage == 1000) ? ";selected=\"true\"" : "") + "};label{text=\"1000\"};");
/* 1144 */         buf.append("}");
/* 1145 */         buf.append("label{type=\"bold\";text=\"--------------------------- Options ------------------------------------\"}");
/*      */         
/* 1147 */         Player[] players = Players.getInstance().getPlayers();
/*      */         
/* 1149 */         Arrays.sort((Object[])players);
/*      */         
/* 1151 */         Village[] vills = Villages.getVillages();
/*      */         
/* 1153 */         Arrays.sort((Object[])vills);
/*      */         
/* 1155 */         int rows = 6;
/* 1156 */         if (this.backString.length() > 0) {
/* 1157 */           rows++;
/*      */         }
/* 1159 */         buf.append("table{rows=\"" + rows + "\";cols=\"3\";");
/*      */         
/* 1161 */         if (this.backString.length() > 0) {
/* 1162 */           buf.append("radio{group=\"tid\";id=\"0\"};label{text=\"Back\"};label{text=\"\"};");
/*      */         }
/*      */ 
/*      */         
/* 1166 */         buf.append("radio{group=\"tid\";id=\"4\"};label{text=\"Player by Name\"};input{id=\"pname\";maxchars=\"32\";text=\"\"};");
/*      */ 
/*      */ 
/*      */         
/* 1170 */         buf.append("radio{group=\"tid\";id=\"9\"};label{text=\"Online Player\"};dropdown{id=\"playerid\";default=\"0\";options=\"");
/*      */         
/*      */         int x;
/* 1173 */         for (x = 0; x < players.length; x++) {
/*      */           
/* 1175 */           if (x > 0)
/* 1176 */             buf.append(","); 
/* 1177 */           this.playerlist.add(players[x]);
/* 1178 */           buf.append(players[x].getName());
/*      */         } 
/* 1180 */         buf.append("\"};");
/*      */         
/* 1182 */         buf.append("radio{group=\"tid\";id=\"10\"};label{text=\"Village\"};dropdown{id=\"villid\";default=\"0\";options=\"");
/*      */ 
/*      */         
/* 1185 */         for (x = 0; x < vills.length; x++) {
/*      */           
/* 1187 */           if (x > 0)
/* 1188 */             buf.append(","); 
/* 1189 */           this.villagelist.add(vills[x]);
/* 1190 */           buf.append(vills[x].getName());
/*      */         } 
/* 1192 */         buf.append("\"};");
/*      */         
/* 1194 */         buf.append("radio{group=\"tid\";id=\"5\";selected=\"true\"};label{text=\"Wurm Id\"};input{id=\"wurmid\";maxchars=\"20\";text=\"" + this.wurmId + "\"}");
/*      */ 
/*      */ 
/*      */         
/* 1198 */         buf.append("radio{group=\"tid\";id=\"6\"};label{text=\"Coord (X,Y,surface)\"};harray{label{text=\"(\"};input{id=\"tilex\";maxchars=\"5\";text=\"-1\"};label{text=\",\"};input{id=\"tiley\";maxchars=\"5\";text=\"-1\"};label{text=\",\"};dropdown{id=\"tilesurface\";options=\"true,false\"};label{text=\")\"};}");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1205 */         buf.append("radio{group=\"tid\";id=\"11,1\";};label{text=\"Email search\"};input{id=\"searchemail\";maxchars=\"60\";text=\"" + ((pInfo != null) ? pInfo.emailAddress : "") + "\"}");
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1210 */         buf.append("radio{group=\"tid\";id=\"11,2\";};label{text=\"IP search\"};input{id=\"searchip\";maxchars=\"30\";text=\"" + ((pInfo != null) ? pInfo
/*      */             
/* 1212 */             .getIpaddress() : "") + "\"}");
/*      */         
/* 1214 */         buf.append("}");
/*      */         
/* 1216 */         buf.append("label{text=\"\"};");
/* 1217 */         buf.append("label{text=\"Query time: " + (System.currentTimeMillis() - startTime) + "ms\"};");
/* 1218 */         buf.append("label{text=\"\"};");
/* 1219 */         buf.append(createAnswerButton2());
/*      */         
/* 1221 */         getResponder().getCommunicator().sendBml(600, 500, true, true, buf.toString(), 200, 200, 200, this.title);
/*      */       }
/* 1223 */       catch (Exception e) {
/*      */ 
/*      */         
/* 1226 */         logger.log(Level.WARNING, this.wurmId + ": " + e.getMessage(), e);
/* 1227 */         getResponder().getCommunicator().sendNormalServerMessage("Exception:" + e.getMessage());
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private String deityDetails(Deity aDeity, long id) {
/* 1234 */     StringBuilder buf = new StringBuilder();
/* 1235 */     if (aDeity == null && id == -10L) {
/*      */       
/* 1237 */       if (this.displaySubType == 10) {
/* 1238 */         buf.append(deityValreiFightsList());
/*      */       } else {
/* 1240 */         buf.append(deityList());
/*      */       } 
/* 1242 */     } else if (this.displaySubType == 1) {
/* 1243 */       buf.append(deitySummary(aDeity));
/* 1244 */     } else if (this.displaySubType == 4) {
/* 1245 */       buf.append(deityBonuses(aDeity));
/* 1246 */     } else if (this.displaySubType == 6) {
/* 1247 */       buf.append(deityAffinities(aDeity));
/* 1248 */     } else if (this.displaySubType == 7) {
/* 1249 */       buf.append(deityPriests(aDeity));
/* 1250 */     } else if (this.displaySubType == 8) {
/* 1251 */       buf.append(deityFollowers(aDeity));
/* 1252 */     } else if (this.displaySubType == 9) {
/* 1253 */       buf.append(deityAltars(aDeity));
/* 1254 */     } else if (this.displaySubType == 10) {
/* 1255 */       buf.append(deityValreiFightDetails(id));
/*      */     } 
/*      */     
/* 1258 */     buf.append("label{type=\"bold\";text=\"------------------------------ Links --------------------------------------\"}");
/* 1259 */     buf.append("table{rows=\"4\";cols=\"4\";");
/* 1260 */     buf.append("radio{group=\"tid\";id=\"13,-10,1\"};label{text=\"List of Deities" + ((aDeity == null) ? " (Showing)" : "") + "\"};");
/*      */     
/* 1262 */     buf.append("radio{group=\"tid\";id=\"13,-10,10\"};label{text=\"List of Valrei Fights" + ((aDeity == null && this.displaySubType == 10) ? " (Showing)" : "") + "\"};");
/*      */     
/* 1264 */     if (aDeity != null) {
/*      */       
/* 1266 */       int numbPriests = (PlayerInfoFactory.getActivePriestsForDeity(aDeity.getNumber())).length;
/* 1267 */       int numbFollowers = (PlayerInfoFactory.getActiveFollowersForDeity(aDeity.getNumber())).length;
/* 1268 */       int numbAltars = (Zones.getAltars(aDeity.getNumber())).length;
/*      */       
/* 1270 */       buf.append("radio{group=\"tid\";id=\"13," + this.wurmId + "," + '\001' + "\"};label{text=\"Summary" + ((this.displaySubType == 1) ? " (Showing)" : "") + "\"};");
/*      */       
/* 1272 */       if (getResponder().getPower() >= 4) {
/*      */         
/* 1274 */         buf.append("radio{group=\"tid\";id=\"13," + this.wurmId + "," + '\004' + "\"};label{text=\"Bonus" + ((this.displaySubType == 4) ? " (Showing)" : "") + "\"};");
/*      */         
/* 1276 */         buf.append("radio{group=\"tid\";id=\"13," + this.wurmId + "," + '\006' + "\"};label{text=\"Affinities" + ((this.displaySubType == 6) ? " (Showing)" : "") + "\"};");
/*      */       } 
/*      */       
/* 1279 */       buf.append(((numbPriests == 0) ? "label{text=\"\"}" : ("radio{group=\"tid\";id=\"13," + this.wurmId + "," + '\007' + "\"};")) + "label{text=\"" + numbPriests + " Active Priests" + ((this.displaySubType == 7) ? " (Showing)" : "") + "\"};");
/*      */ 
/*      */ 
/*      */       
/* 1283 */       buf.append(((numbFollowers == 0) ? "label{text=\"\"}" : ("radio{group=\"tid\";id=\"13," + this.wurmId + "," + '\b' + "\"};")) + "label{text=\"" + numbFollowers + " Active Followers" + ((this.displaySubType == 8) ? " (Showing)" : "") + "\"};");
/*      */ 
/*      */ 
/*      */       
/* 1287 */       buf.append(((numbAltars == 0) ? "label{text=\"\"}" : ("radio{group=\"tid\";id=\"13," + this.wurmId + "," + '\t' + "\"};")) + "label{text=\"" + numbAltars + " Altars" + ((this.displaySubType == 9) ? " (Showing)" : "") + "\"};");
/*      */     } 
/*      */ 
/*      */     
/* 1291 */     buf.append("label{text=\"\"};label{text=\"\"};");
/* 1292 */     buf.append("}");
/* 1293 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String deityList() {
/* 1298 */     StringBuilder buf = new StringBuilder();
/* 1299 */     Deity[] deities = Deities.getDeities();
/* 1300 */     buf.append(deityTable(deities));
/* 1301 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String deityValreiFightsList() {
/* 1306 */     StringBuilder buf = new StringBuilder();
/* 1307 */     buf.append(deityValreiFightsListTable(ValreiFightHistoryManager.getInstance().getAllFights()));
/* 1308 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String deityValreiFightDetails(long fightId) {
/* 1313 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 1315 */     ValreiFightHistory fight = ValreiFightHistoryManager.getInstance().getFight(fightId);
/* 1316 */     if (fight == null) {
/*      */       
/* 1318 */       buf.append("label{text=\"Invalid fight id: " + fightId + "\"};");
/*      */     }
/*      */     else {
/*      */       
/* 1322 */       buf.append("table{rows=\"1\";cols=\"4\";");
/*      */       
/* 1324 */       buf.append("label{text=\"\"};label{text=\"Fight Id\"};label{text=\"\"};label{text=\"" + fightId + "\"};");
/*      */ 
/*      */       
/* 1327 */       for (int i = 0; i <= fight.getTotalActions(); i++)
/*      */       {
/* 1329 */         buf.append("label{text=\"\"};label{text=\"" + ValreiConstants.getFightActionName(fight.getFightAction(i)) + "\"};label{text=\"\"};label{text=\"" + 
/* 1330 */             ValreiConstants.getFightActionSummary(fight.getFightAction(i)) + "\"};");
/*      */       }
/*      */       
/* 1333 */       buf.append("}");
/*      */     } 
/*      */     
/* 1336 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String deitySummary(Deity aDeity) {
/* 1341 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 1343 */     buf.append("table{rows=\"1\";cols=\"4\";");
/*      */     
/* 1345 */     buf.append("label{text=\"\"};label{text=\"Name\"};label{text=\"\"};label{text=\"" + aDeity
/*      */ 
/*      */         
/* 1348 */         .getName() + "\"};");
/*      */     
/* 1350 */     buf.append("label{text=\"\"};label{text=\"Followers\"};label{text=\"\"};label{text=\"" + aDeity
/*      */ 
/*      */         
/* 1353 */         .getActiveFollowers() + "\"};");
/*      */     
/* 1355 */     buf.append("label{text=\"\"};label{text=\"Alignment\"};label{text=\"\"};label{text=\"" + aDeity
/*      */ 
/*      */         
/* 1358 */         .getAlignment() + "\"};");
/*      */     
/* 1360 */     long pid = aDeity.getBestHelper(false);
/* 1361 */     if (pid == -10L) {
/* 1362 */       buf.append("label{text=\"\"};label{text=\"Best Helper\"};label{text=\"\"};label{type=\"italic\";text=\"Noone!\"};");
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */       
/* 1368 */       PlayerInfo pInfo = PlayerInfoFactory.getPlayerInfoWithWurmId(pid);
/* 1369 */       String pName = (pInfo == null) ? "unknown" : pInfo.getName();
/* 1370 */       buf.append("label{text=\"\"};label{text=\"Best Helper\"};label{text=\"\"};label{text=\"" + pName + "\"};");
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1376 */     buf.append("label{text=\"\"};label{text=\"Average Faith\"};label{text=\"\"};label{text=\"" + aDeity
/*      */ 
/*      */         
/* 1379 */         .getFaithPerFollower() + "\"};");
/*      */     
/* 1381 */     buf.append("label{text=\"\"};label{text=\"Favor\"};label{text=\"\"};label{text=\"" + aDeity
/*      */ 
/*      */         
/* 1384 */         .getFavor() + "\"};");
/*      */     
/* 1386 */     buf.append("label{text=\"\"};label{text=\"Favored Kingdom\"};label{text=\"\"};label{text=\"" + 
/*      */ 
/*      */         
/* 1389 */         Kingdoms.getNameFor(aDeity.getFavoredKingdom()) + " (" + aDeity
/* 1390 */         .getFavoredKingdom() + ")\"};");
/*      */     
/* 1392 */     buf.append("label{text=\"\"};label{text=\"Gender\"};label{text=\"\"};label{text=\"" + (
/*      */ 
/*      */         
/* 1395 */         (aDeity.getSex() == 0) ? "Male" : "Female") + "\"};");
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 1400 */       ItemTemplate it = ItemTemplateFactory.getInstance().getTemplate(aDeity.getHolyItem());
/* 1401 */       buf.append("label{text=\"\"};label{text=\"Holy item?\"};label{text=\"\"};label{text=\"" + it
/*      */ 
/*      */           
/* 1404 */           .getName() + "\"};");
/*      */     }
/* 1406 */     catch (NoSuchTemplateException e) {
/*      */       
/* 1408 */       buf.append("label{text=\"\"};label{text=\"Holy item?\"};label{text=\"\"};label{text=\"unknown (" + aDeity
/*      */ 
/*      */           
/* 1411 */           .getHolyItem() + ")\"};");
/*      */     } 
/*      */     
/* 1414 */     buf.append("label{text=\"\"};label{text=\"Mountain God?\"};label{text=\"\"};label{" + 
/*      */ 
/*      */         
/* 1417 */         showBoolean(aDeity.isMountainGod()) + "};");
/*      */     
/* 1419 */     buf.append("label{text=\"\"};label{text=\"Water God?\"};label{text=\"\"};label{" + 
/*      */ 
/*      */         
/* 1422 */         showBoolean(aDeity.isWaterGod()) + "};");
/*      */     
/* 1424 */     buf.append("label{text=\"\"};label{text=\"Forest God?\"};label{text=\"\"};label{" + 
/*      */ 
/*      */         
/* 1427 */         showBoolean(aDeity.isForestGod()) + "};");
/*      */     
/* 1429 */     buf.append("label{text=\"\"};label{text=\"Hate God?\"};label{text=\"\"};label{" + 
/*      */ 
/*      */         
/* 1432 */         showBoolean(aDeity.isHateGod()) + "};");
/*      */     
/* 1434 */     buf.append("label{text=\"\"};label{text=\"Alignment\"};label{text=\"\"};label{text=\"" + aDeity
/*      */ 
/*      */         
/* 1437 */         .getAlignment() + "\"};");
/*      */     
/* 1439 */     buf.append("}");
/* 1440 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String deityBonuses(Deity aDeity) {
/* 1445 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 1447 */     buf.append("table{rows=\"1\";cols=\"4\";");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1464 */     buf.append("label{text=\"\"};label{text=\"Build Wall Bonus\"};label{text=\"\"};label{text=\"" + aDeity
/*      */ 
/*      */         
/* 1467 */         .getBuildWallBonus() + "\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1479 */     buf.append("}");
/* 1480 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String deityAffinities(Deity aDeity) {
/* 1485 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 1487 */     buf.append("table{rows=\"1\";cols=\"4\";");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1499 */     buf.append("label{text=\"\"};label{text=\"Meat Affinity?\"};label{text=\"\"};label{" + 
/*      */ 
/*      */         
/* 1502 */         showBoolean(aDeity.isMeatAffinity()) + "};");
/*      */     
/* 1504 */     buf.append("label{text=\"\"};label{text=\"Allows Butchering?\"};label{text=\"\"};label{" + 
/*      */ 
/*      */         
/* 1507 */         showBoolean(aDeity.isAllowsButchering()) + "};");
/*      */     
/* 1509 */     buf.append("label{text=\"\"};label{text=\"Road Protector?\"};label{text=\"\"};label{" + 
/*      */ 
/*      */         
/* 1512 */         showBoolean(aDeity.isRoadProtector()) + "};");
/*      */     
/* 1514 */     buf.append("label{text=\"\"};label{text=\"Item Protector?\"};label{text=\"\"};label{" + 
/*      */ 
/*      */         
/* 1517 */         showBoolean(aDeity.isItemProtector()) + "};");
/*      */     
/* 1519 */     buf.append("label{text=\"\"};label{text=\"Warrior?\"};label{text=\"\"};label{" + 
/*      */ 
/*      */         
/* 1522 */         showBoolean(aDeity.isWarrior()) + "};");
/*      */     
/* 1524 */     buf.append("label{text=\"\"};label{text=\"Death Protector?\"};label{text=\"\"};label{" + 
/*      */ 
/*      */         
/* 1527 */         showBoolean(aDeity.isDeathProtector()) + "};");
/*      */     
/* 1529 */     buf.append("label{text=\"\"};label{text=\"Death Item Protector?\"};label{text=\"\"};label{" + 
/*      */ 
/*      */         
/* 1532 */         showBoolean(aDeity.isDeathItemProtector()) + "};");
/*      */     
/* 1534 */     buf.append("label{text=\"\"};label{text=\"Metal Affinity?\"};label{text=\"\"};label{" + 
/*      */ 
/*      */         
/* 1537 */         showBoolean(aDeity.isMetalAffinity()) + "};");
/*      */     
/* 1539 */     buf.append("label{text=\"\"};label{text=\"Repairer?\"};label{text=\"\"};label{" + 
/*      */ 
/*      */         
/* 1542 */         showBoolean(aDeity.isRepairer()) + "};");
/*      */     
/* 1544 */     buf.append("label{text=\"\"};label{text=\"Learner?\"};label{text=\"\"};label{" + 
/*      */ 
/*      */         
/* 1547 */         showBoolean(aDeity.isLearner()) + "};");
/*      */     
/* 1549 */     buf.append("label{text=\"\"};label{text=\"Wood Affinity?\"};label{text=\"\"};label{" + 
/*      */ 
/*      */         
/* 1552 */         showBoolean(aDeity.isWoodAffinity()) + "};");
/*      */     
/* 1554 */     buf.append("label{text=\"\"};label{text=\"Befriend Creature?\"};label{text=\"\"};label{" + 
/*      */ 
/*      */         
/* 1557 */         showBoolean(aDeity.isBefriendCreature()) + "};");
/*      */     
/* 1559 */     buf.append("label{text=\"\"};label{text=\"Stamina Bonus?\"};label{text=\"\"};label{" + 
/*      */ 
/*      */         
/* 1562 */         showBoolean(aDeity.isStaminaBonus()) + "};");
/*      */     
/* 1564 */     buf.append("label{text=\"\"};label{text=\"Food Bonus?\"};label{text=\"\"};label{" + 
/*      */ 
/*      */         
/* 1567 */         showBoolean(aDeity.isFoodBonus()) + "};");
/*      */     
/* 1569 */     buf.append("label{text=\"\"};label{text=\"Healer?\"};label{text=\"\"};label{" + 
/*      */ 
/*      */         
/* 1572 */         showBoolean(aDeity.isHealer()) + "};");
/*      */     
/* 1574 */     buf.append("label{text=\"\"};label{text=\"Clay Affinity?\"};label{text=\"\"};label{" + 
/*      */ 
/*      */         
/* 1577 */         showBoolean(aDeity.isClayAffinity()) + "};");
/*      */     
/* 1579 */     buf.append("label{text=\"\"};label{text=\"Cloth Affinity?\"};label{text=\"\"};label{" + 
/*      */ 
/*      */         
/* 1582 */         showBoolean(aDeity.isClothAffinity()) + "};");
/*      */     
/* 1584 */     buf.append("label{text=\"\"};label{text=\"Food Affinity?\"};label{text=\"\"};label{" + 
/*      */ 
/*      */         
/* 1587 */         showBoolean(aDeity.isFoodAffinity()) + "};");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1594 */     buf.append("}");
/* 1595 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String deityPriests(Deity deity) {
/* 1600 */     StringBuilder buf = new StringBuilder();
/* 1601 */     PlayerInfo[] priests = PlayerInfoFactory.getActivePriestsForDeity(deity.getNumber());
/* 1602 */     buf.append(deityFollowersTable(priests, deity.getName(), "active priests"));
/* 1603 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String deityFollowers(Deity deity) {
/* 1608 */     StringBuilder buf = new StringBuilder();
/* 1609 */     PlayerInfo[] followers = PlayerInfoFactory.getActiveFollowersForDeity(deity.getNumber());
/* 1610 */     buf.append(deityFollowersTable(followers, deity.getName(), "active followers"));
/* 1611 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String deityAltars(Deity deity) {
/* 1616 */     StringBuilder buf = new StringBuilder();
/* 1617 */     Item[] altars = Zones.getAltars(deity.getNumber());
/* 1618 */     buf.append(deityAltarsTable(altars, deity.getName()));
/* 1619 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String serverDetails(ServerEntry aServer) {
/* 1624 */     ServerEntry[] servers = Servers.getAllServers();
/* 1625 */     Shop[] shops = Economy.getTraders();
/* 1626 */     Recipe[] recipes = Recipes.getNamedRecipes();
/* 1627 */     Creature[] avatars = Creatures.getInstance().getAvatars();
/* 1628 */     Kingdom[] kingdoms = Kingdoms.getAllKingdoms();
/* 1629 */     StringBuilder buf = new StringBuilder();
/* 1630 */     if (this.displaySubType == 15 || (this.displaySubType == 1 && aServer == null)) {
/* 1631 */       buf.append(serversTable(servers));
/* 1632 */     } else if (this.displaySubType == 1) {
/* 1633 */       buf.append(serverSummary(aServer));
/* 1634 */     } else if (this.displaySubType == 5) {
/* 1635 */       buf.append(serverTopSkills());
/* 1636 */     } else if (this.displaySubType == 16) {
/* 1637 */       buf.append(traderTable(shops));
/* 1638 */     } else if (this.displaySubType == 17) {
/* 1639 */       buf.append(recipeTable(recipes));
/* 1640 */     } else if (this.displaySubType == 18) {
/* 1641 */       buf.append(riftsTable());
/* 1642 */     } else if (this.displaySubType == 19) {
/* 1643 */       buf.append(routesList());
/* 1644 */     } else if (this.displaySubType == 21) {
/* 1645 */       buf.append(nodesList());
/* 1646 */     } else if (this.displaySubType == 24) {
/* 1647 */       buf.append(avatarsTable(avatars));
/* 1648 */     } else if (this.displaySubType == 26) {
/* 1649 */       buf.append(kingdomsTable(kingdoms));
/*      */     } 
/*      */     
/* 1652 */     buf.append("label{type=\"bold\";text=\"------------------------------ Links --------------------------------------\"}");
/* 1653 */     buf.append("table{rows=\"3\";cols=\"6\";");
/* 1654 */     buf.append("radio{group=\"tid\";id=\"14," + this.wurmId + "," + '\001' + "\"};label{text=\"Summary" + ((this.displaySubType == 1) ? " (Showing)" : "") + "\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1659 */     buf.append("radio{group=\"tid\";id=\"14,-10,15\"};label{text=\"" + servers.length + " Servers" + ((this.displaySubType == 15) ? " (Showing)" : "") + "\"};");
/*      */     
/* 1661 */     buf.append("radio{group=\"tid\";id=\"14,-10,16\"};label{text=\"" + shops.length + " Traders" + ((this.displaySubType == 16) ? " (Showing)" : "") + "\"};");
/*      */     
/* 1663 */     buf.append("radio{group=\"tid\";id=\"14,-10,17\"};label{text=\"" + recipes.length + " Named Recipes" + ((this.displaySubType == 17) ? " (Showing)" : "") + "\"};");
/*      */     
/* 1665 */     buf.append("radio{group=\"tid\";id=\"14,-10,18\"};label{text=\"Rift List" + ((this.displaySubType == 18) ? " (Showing)" : "") + "\"};");
/*      */     
/* 1667 */     buf.append("radio{group=\"tid\";id=\"14,-10,24\"};label{text=\"" + avatars.length + " Avatars" + ((this.displaySubType == 24) ? " (Showing)" : "") + "\"};");
/*      */     
/* 1669 */     buf.append("radio{group=\"tid\";id=\"14,-10,26\"};label{text=\"" + kingdoms.length + " Kingdoms" + ((this.displaySubType == 26) ? " (Showing)" : "") + "\"};");
/*      */     
/* 1671 */     buf.append("label{text=\"\"};label{text=\"\"};");
/* 1672 */     buf.append("label{text=\"\"};label{text=\"\"};");
/*      */     
/* 1674 */     if (Features.Feature.HIGHWAYS.isEnabled()) {
/*      */       
/* 1676 */       buf.append("radio{group=\"tid\";id=\"15,-10,19\"};label{text=\"Highway Routes" + ((this.displaySubType == 19) ? " (Showing)" : "") + "\"};");
/*      */       
/* 1678 */       buf.append("radio{group=\"tid\";id=\"15,-10,21\"};label{text=\"Highway Nodes" + ((this.displaySubType == 21) ? " (Showing)" : "") + "\"};");
/*      */       
/* 1680 */       if (Features.Feature.WAGONER.isEnabled()) {
/* 1681 */         buf.append("radio{group=\"tid\";id=\"16,-10,25\"};label{text=\"Wagoner List" + ((this.displaySubType == 25) ? " (Showing)" : "") + "\"};");
/*      */       } else {
/*      */         
/* 1684 */         buf.append("label{text=\"\"};label{text=\"\"};");
/*      */       } 
/* 1686 */     }  buf.append("}");
/* 1687 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String serverSummary(ServerEntry aServer) {
/* 1692 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 1694 */     buf.append("table{rows=\"1\";cols=\"4\";");
/*      */     
/* 1696 */     buf.append("label{text=\"\"};label{text=\"Name\"};label{text=\"\"};label{text=\"" + aServer
/*      */ 
/*      */         
/* 1699 */         .getName() + "\"};");
/*      */     
/* 1701 */     buf.append("label{text=\"\"};label{text=\"Abbreviation\"};label{text=\"\"};label{text=\"" + aServer
/*      */ 
/*      */         
/* 1704 */         .getAbbreviation() + "\"};");
/*      */     
/* 1706 */     buf.append("label{text=\"\"};label{text=\"Map Name\"};label{text=\"\"};label{text=\"" + aServer.mapname + "\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1711 */     buf.append("label{text=\"\"};label{text=\"Kingdom\"};label{text=\"\"};label{text=\"" + 
/*      */ 
/*      */         
/* 1714 */         Kingdoms.getNameFor(aServer.getKingdom()) + " (" + aServer
/* 1715 */         .getKingdom() + ")\"};");
/*      */     
/* 1717 */     buf.append("label{text=\"\"};label{text=\"Max Creatures\"};label{text=\"\"};label{text=\"" + aServer.maxCreatures + "\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1722 */     buf.append("label{text=\"\"};label{text=\"Max Typed Creatures\"};label{text=\"\"};label{text=\"" + aServer.maxTypedCreatures + "\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1727 */     buf.append("label{text=\"\"};label{text=\"Mesh Size\"};label{text=\"\"};label{text=\"" + aServer.meshSize + "\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1732 */     buf.append("label{text=\"\"};label{text=\"Agg Creature %\"};label{text=\"\"};label{text=\"" + aServer.percentAggCreatures + "\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1737 */     buf.append("label{text=\"\"};label{text=\"CA Help Group\"};label{text=\"\"};label{text=\"" + aServer
/*      */ 
/*      */         
/* 1740 */         .getCAHelpGroup() + "\"};");
/*      */     
/* 1742 */     buf.append("label{text=\"\"};label{text=\"Last Spawned Unique\"};label{text=\"\"};label{text=\"" + aServer
/*      */ 
/*      */         
/* 1745 */         .getLastSpawnedUnique() + "\"};");
/*      */     
/* 1747 */     buf.append("label{text=\"\"};label{text=\"Motd\"};label{text=\"\"};label{text=\"" + aServer
/*      */ 
/*      */         
/* 1750 */         .getMotd() + "\"};");
/*      */     
/* 1752 */     buf.append("label{text=\"\"};label{text=\"Available?\"};label{text=\"\"};label{" + 
/*      */ 
/*      */         
/* 1755 */         showBoolean(aServer.isAvailable(getResponder().getPower(), getResponder().isPaying())) + "};");
/*      */     
/* 1757 */     buf.append("label{text=\"\"};label{text=\"Challenge Server?\"};label{text=\"\"};label{" + 
/*      */ 
/*      */         
/* 1760 */         showBoolean(aServer.challengeServer) + "};");
/*      */     
/* 1762 */     buf.append("label{text=\"\"};label{text=\"Entry Server?\"};label{text=\"\"};label{" + 
/*      */ 
/*      */         
/* 1765 */         showBoolean(aServer.entryServer) + "};");
/*      */     
/* 1767 */     buf.append("label{text=\"\"};label{text=\"EPIC?\"};label{text=\"\"};label{" + 
/*      */ 
/*      */         
/* 1770 */         showBoolean(aServer.EPIC) + "};");
/*      */     
/* 1772 */     buf.append("label{text=\"\"};label{text=\"HOMESERVER?\"};label{text=\"\"};label{" + 
/*      */ 
/*      */         
/* 1775 */         showBoolean(aServer.HOMESERVER) + "};");
/*      */     
/* 1777 */     buf.append("label{text=\"\"};label{text=\"Is Local?\"};label{text=\"\"};label{" + 
/*      */ 
/*      */         
/* 1780 */         showBoolean(aServer.isLocal) + "};");
/*      */     
/* 1782 */     buf.append("label{text=\"\"};label{text=\"Is Chaos Server?\"};label{text=\"\"};label{" + 
/*      */ 
/*      */         
/* 1785 */         showBoolean(aServer.isChaosServer()) + "};");
/*      */     
/* 1787 */     buf.append("label{text=\"\"};label{text=\"Is Free Deeds?\"};label{text=\"\"};label{" + 
/*      */ 
/*      */         
/* 1790 */         showBoolean(aServer.isFreeDeeds()) + "};");
/*      */     
/* 1792 */     buf.append("label{text=\"\"};label{text=\"Is Full?\"};label{text=\"\"};label{" + 
/*      */ 
/*      */         
/* 1795 */         showBoolean(aServer.isFull()) + "};");
/*      */     
/* 1797 */     buf.append("label{text=\"\"};label{text=\"Is Payment?\"};label{text=\"\"};label{" + 
/*      */ 
/*      */         
/* 1800 */         showBoolean(aServer.ISPAYMENT) + "};");
/*      */     
/* 1802 */     buf.append("label{text=\"\"};label{text=\"PvP Server?\"};label{text=\"\"};label{" + 
/*      */ 
/*      */         
/* 1805 */         showBoolean(aServer.PVPSERVER) + "};");
/*      */     
/* 1807 */     buf.append("label{text=\"\"};label{text=\"Drive on...\"};label{text=\"\"};label{text=\"" + (
/*      */ 
/*      */         
/* 1810 */         Features.Feature.DRIVE_ON_LEFT.isEnabled() ? "Left" : "Right") + "\"};");
/*      */     
/* 1812 */     ServerEntry se = Servers.loginServer;
/* 1813 */     buf.append(((se != null) ? ("radio{group=\"tid\";id=\"14," + se.getId() + "," + '\001' + "\"};") : "label{text=\"\"};") + "label{text=\"Login Server\"};label{text=\"\"};label{text=\"" + ((se != null) ? se
/*      */ 
/*      */ 
/*      */         
/* 1817 */         .getName() : "none") + "\"};");
/*      */     
/* 1819 */     se = aServer.serverNorth;
/* 1820 */     buf.append(((se != null) ? ("radio{group=\"tid\";id=\"14," + se.getId() + "," + '\001' + "\"};") : "label{text=\"\"};") + "label{text=\"North Server\"};label{text=\"\"};label{text=\"" + ((se != null) ? se
/*      */ 
/*      */ 
/*      */         
/* 1824 */         .getName() : "none") + "\"};");
/*      */     
/* 1826 */     se = aServer.serverWest;
/* 1827 */     buf.append(((se != null) ? ("radio{group=\"tid\";id=\"14," + se.getId() + "," + '\001' + "\"};") : "label{text=\"\"};") + "label{text=\"West Server\"};label{text=\"\"};label{text=\"" + ((se != null) ? se
/*      */ 
/*      */ 
/*      */         
/* 1831 */         .getName() : "none") + "\"};");
/*      */     
/* 1833 */     se = aServer.serverEast;
/* 1834 */     buf.append(((se != null) ? ("radio{group=\"tid\";id=\"14," + se.getId() + "," + '\001' + "\"};") : "label{text=\"\"};") + "label{text=\"East Server\"};label{text=\"\"};label{text=\"" + ((se != null) ? se
/*      */ 
/*      */ 
/*      */         
/* 1838 */         .getName() : "none") + "\"};");
/*      */     
/* 1840 */     se = aServer.serverSouth;
/* 1841 */     buf.append(((se != null) ? ("radio{group=\"tid\";id=\"14," + se.getId() + "," + '\001' + "\"};") : "label{text=\"\"};") + "label{text=\"South Server\"};label{text=\"\"};label{text=\"" + ((se != null) ? se
/*      */ 
/*      */ 
/*      */         
/* 1845 */         .getName() : "none") + "\"};");
/*      */ 
/*      */     
/* 1848 */     buf.append("}");
/* 1849 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String serverTopSkills() {
/* 1856 */     StringBuilder buf = new StringBuilder();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1926 */     buf.append("label{text=\"Dont use - Causes too much lag\"};");
/* 1927 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private int getParentSkillId(Skill skill) {
/* 1932 */     int[] needed = skill.getDependencies();
/*      */     
/* 1934 */     int parentSkillId = 0;
/* 1935 */     if (needed.length > 0) {
/* 1936 */       parentSkillId = needed[0];
/*      */     }
/* 1938 */     if (parentSkillId != 0) {
/*      */       
/* 1940 */       int parentType = SkillSystem.getTypeFor(parentSkillId);
/* 1941 */       if (parentType == 0)
/* 1942 */         parentSkillId = 0; 
/*      */     } 
/* 1944 */     return parentSkillId;
/*      */   }
/*      */ 
/*      */   
/*      */   private String highwayDetails(Route route, Node node) {
/* 1949 */     StringBuilder buf = new StringBuilder();
/* 1950 */     if (this.displaySubType == 19) {
/* 1951 */       buf.append(routesList());
/* 1952 */     } else if (this.displaySubType == 20) {
/* 1953 */       buf.append(routeSummary(route));
/* 1954 */     } else if (this.displaySubType == 23) {
/* 1955 */       buf.append(routeCatseyes(route));
/* 1956 */     } else if (this.displaySubType == 21) {
/* 1957 */       buf.append(nodesList());
/* 1958 */     } else if (this.displaySubType == 22) {
/* 1959 */       buf.append(nodeSummary(node));
/* 1960 */     }  int ncats = 0;
/* 1961 */     if (route != null) {
/* 1962 */       ncats = (route.getCatseyes()).length;
/*      */     }
/*      */     
/* 1965 */     buf.append("label{type=\"bold\";text=\"------------------------------ Links --------------------------------------\"}");
/* 1966 */     buf.append("table{rows=\"3\";cols=\"6\";");
/* 1967 */     buf.append("radio{group=\"tid\";id=\"15,-10,19\"};label{text=\"Route List" + ((this.displaySubType == 19) ? " (Showing)" : "") + "\"};");
/*      */     
/* 1969 */     if (route != null) {
/*      */       
/* 1971 */       buf.append("radio{group=\"tid\";id=\"15," + this.wurmId + "," + '\024' + "\"};label{text=\"Route Summary" + ((this.displaySubType == 20) ? " (Showing)" : "") + "\"};");
/*      */       
/* 1973 */       buf.append("radio{group=\"tid\";id=\"15," + this.wurmId + "," + '\027' + "\"};label{text=\"" + ncats + " Route Catseyes" + ((this.displaySubType == 23) ? " (Showing)" : "") + "\"};");
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 1978 */       buf.append("label{text=\"\"};label{text=\"Route Summary\"};");
/* 1979 */       buf.append("label{text=\"\"};label{text=\"Route Catseyes\"};");
/*      */     } 
/* 1981 */     buf.append("radio{group=\"tid\";id=\"15,-10,21\"};label{text=\"Node List" + ((this.displaySubType == 21) ? " (Showing)" : "") + "\"};");
/*      */     
/* 1983 */     if (node != null) {
/*      */       
/* 1985 */       buf.append("radio{group=\"tid\";id=\"15," + this.wurmId + "," + '\026' + "\"};label{text=\"Node Summary" + ((this.displaySubType == 22) ? " (Showing)" : "") + "\"};");
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 1990 */       buf.append("label{text=\"\"};label{text=\"Node Summary\"};");
/*      */     } 
/* 1992 */     buf.append("label{text=\"\"};label{text=\"\"};");
/* 1993 */     buf.append("}");
/* 1994 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String routeSummary(Route route) {
/* 1999 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 2001 */     buf.append("table{rows=\"1\";cols=\"4\";");
/*      */     
/* 2003 */     buf.append("radio{group=\"tid\";id=\"1," + route.getStartNode().getWaystone().getWurmId() + "," + '\001' + "\"}label{text=\"Start Waystone\"};label{text=\"\"};label{text=\"" + route
/*      */ 
/*      */         
/* 2006 */         .getStartNode().getWaystone().getWurmId() + "\"};");
/*      */     
/* 2008 */     buf.append("label{text=\"\"};label{text=\"Start Dir\"};label{text=\"\"};label{text=\"" + 
/*      */ 
/*      */         
/* 2011 */         MethodsHighways.getLinkAsString(route.getDirection()) + " (" + route.getDirection() + ")\"};");
/*      */     
/* 2013 */     int stx = route.getStartNode().getWaystone().getTileX();
/* 2014 */     int sty = route.getStartNode().getWaystone().getTileY();
/* 2015 */     boolean soS = route.getStartNode().getWaystone().isOnSurface();
/* 2016 */     buf.append("radio{group=\"tid\";id=\"3," + stx + "," + sty + "," + soS + "\"};label{text=\"Coord (X,Y,surface)\"};radio{group=\"tid\";id=\"" + '\f' + "," + stx + "," + sty + "," + soS + "\"};label{text=\"(" + stx + "," + sty + "," + soS + ")\"}");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2021 */     buf.append("radio{group=\"tid\";id=\"15," + this.wurmId + "," + '\027' + "\"};label{text=\"Catseyes\"};label{text=\"\"};label{text=\"" + (route
/*      */ 
/*      */         
/* 2024 */         .getCatseyes()).length + " Catseyes\"};");
/*      */     
/* 2026 */     Node node = route.getEndNode();
/* 2027 */     if (node != null) {
/*      */       
/* 2029 */       buf.append("radio{group=\"tid\";id=\"1," + node.getWaystone().getWurmId() + "," + '\001' + "\"}label{text=\"End Waystone\"};label{text=\"\"};label{text=\"" + node
/*      */ 
/*      */           
/* 2032 */           .getWaystone().getWurmId() + "\"};");
/*      */       
/* 2034 */       int etx = route.getEndNode().getWaystone().getTileX();
/* 2035 */       int ety = route.getEndNode().getWaystone().getTileY();
/* 2036 */       boolean eoS = route.getEndNode().getWaystone().isOnSurface();
/* 2037 */       buf.append("radio{group=\"tid\";id=\"3," + etx + "," + ety + "," + eoS + "\"};label{text=\"Coord (X,Y,surface)\"};radio{group=\"tid\";id=\"" + '\f' + "," + etx + "," + ety + "," + eoS + "\"};label{text=\"(" + etx + "," + ety + "," + eoS + ")\"}");
/*      */ 
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */       
/* 2044 */       buf.append("label{text=\"\"};label{text=\"End Waystone\"};label{text=\"\"};label{type=\"italic\"text=\"missing\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2049 */       buf.append("label{text=\"\"};label{text=\"Coord (X,Y,surface)\"};label{text=\"\"};label{type=\"italic\"text=\"missing\"};");
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2055 */     Route reverseRoute = route.getOppositeRoute();
/* 2056 */     if (reverseRoute == null) {
/*      */       
/* 2058 */       buf.append("label{text=\"\"};label{text=\"Reverse Route\"};label{text=\"\"};label{type=\"italic\"text=\"missing\"};");
/*      */ 
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */       
/* 2065 */       buf.append("radio{group=\"tid\";id=\"15," + reverseRoute.getId() + "," + '\024' + "\"};label{text=\"Reverse Route\"};label{text=\"\"};label{" + "color=\"255,177,40\"" + "text=\"R" + reverseRoute
/*      */ 
/*      */           
/* 2068 */           .getId() + "\"};");
/*      */     } 
/*      */     
/* 2071 */     buf.append("label{text=\"\"};label{text=\"Dist\"};label{text=\"\"};label{text=\"" + route
/*      */ 
/*      */         
/* 2074 */         .getDistance() + "\"};");
/*      */     
/* 2076 */     buf.append("label{text=\"\"};label{text=\"Cost\"};label{text=\"\"};label{text=\"" + route
/*      */ 
/*      */         
/* 2079 */         .getCost() + "\"};");
/*      */     
/* 2081 */     buf.append("}");
/* 2082 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String routeCatseyes(Route route) {
/* 2087 */     StringBuilder buf = new StringBuilder();
/* 2088 */     Item[] catseyes = route.getCatseyes();
/* 2089 */     buf.append(itemsTable(catseyes));
/* 2090 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String nodeSummary(Node node) {
/* 2095 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 2097 */     buf.append("table{rows=\"1\";cols=\"4\";");
/*      */     
/* 2099 */     buf.append("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\001' + "\"}label{text=\"Waystone\"};label{text=\"\"};label{text=\"" + node
/*      */ 
/*      */         
/* 2102 */         .getWaystone().getWurmId() + "\"};");
/*      */     
/* 2104 */     int ntx = node.getWaystone().getTileX();
/* 2105 */     int nty = node.getWaystone().getTileY();
/* 2106 */     boolean noS = node.getWaystone().isOnSurface();
/* 2107 */     buf.append("radio{group=\"tid\";id=\"3," + ntx + "," + nty + "," + noS + "\"};label{text=\"Coord (X,Y,surface)\"};radio{group=\"tid\";id=\"" + '\f' + "," + ntx + "," + nty + "," + noS + "\"};label{text=\"(" + ntx + "," + nty + "," + noS + ")\"}");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2112 */     buf.append(nodeDir(node, (byte)1));
/* 2113 */     buf.append(nodeDir(node, (byte)2));
/* 2114 */     buf.append(nodeDir(node, (byte)4));
/* 2115 */     buf.append(nodeDir(node, (byte)8));
/* 2116 */     buf.append(nodeDir(node, (byte)16));
/* 2117 */     buf.append(nodeDir(node, (byte)32));
/* 2118 */     buf.append(nodeDir(node, (byte)64));
/* 2119 */     buf.append(nodeDir(node, -128));
/*      */     
/* 2121 */     buf.append("}");
/* 2122 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String nodeDir(Node node, byte dir) {
/* 2127 */     StringBuilder buf = new StringBuilder();
/*      */ 
/*      */     
/* 2130 */     Route route = node.getRoute(dir);
/* 2131 */     if (route != null) {
/*      */       
/* 2133 */       buf.append("radio{group=\"tid\";id=\"15," + route.getId() + "," + '\024' + "\"};");
/* 2134 */       buf.append("label{text=\"" + MethodsHighways.getLinkDirString(dir) + "\"};");
/* 2135 */       buf.append("label{text=\"\"};");
/* 2136 */       buf.append("label{text=\"Route " + route.getId() + "\"};");
/*      */     }
/*      */     else {
/*      */       
/* 2140 */       buf.append("label{text=\"\"};");
/* 2141 */       buf.append("label{text=\"" + MethodsHighways.getLinkDirString(dir) + "\"};");
/* 2142 */       buf.append("label{text=\"\"};");
/* 2143 */       buf.append("label{type=\"italic\"text=\"none\"};");
/*      */     } 
/* 2145 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String wagonerDetails(Wagoner wagoner) {
/* 2150 */     StringBuilder buf = new StringBuilder();
/* 2151 */     if (wagoner == null || this.displaySubType == 25) {
/* 2152 */       buf.append(wagonerList());
/* 2153 */     } else if (this.displaySubType == 1) {
/* 2154 */       buf.append(wagonerSummary(wagoner));
/*      */     } 
/*      */     
/* 2157 */     buf.append("label{type=\"bold\";text=\"------------------------------ Links --------------------------------------\"}");
/* 2158 */     buf.append("table{rows=\"3\";cols=\"6\";");
/* 2159 */     buf.append("radio{group=\"tid\";id=\"16,-10,25\"};label{text=\"Wagoner List" + ((wagoner == null || this.displaySubType == 25) ? " (Showing)" : "") + "\"};");
/*      */     
/* 2161 */     buf.append("label{text=\"\"};label{text=\"Wagoner Summary" + ((this.displaySubType == 1) ? " (Showing)" : "") + "\"};");
/*      */     
/* 2163 */     buf.append("label{text=\"\"};label{text=\"\"};");
/* 2164 */     buf.append("}");
/* 2165 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String wagonerSummary(Wagoner wagoner) {
/* 2170 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 2172 */     buf.append("table{rows=\"1\";cols=\"4\";");
/*      */     
/* 2174 */     buf.append("radio{group=\"tid\";id=\"16," + wagoner.getWurmId() + "," + '\001' + "\"}label{text=\"Id\"};label{text=\"\"};label{text=\"" + wagoner
/*      */ 
/*      */         
/* 2177 */         .getWurmId() + "\"};");
/*      */     
/* 2179 */     buf.append("label{text=\"\"};label{text=\"Name\"};label{text=\"\"};label{text=\"" + wagoner
/*      */ 
/*      */         
/* 2182 */         .getName() + "\"};");
/*      */     
/* 2184 */     Creature creature = wagoner.getCreature();
/* 2185 */     if (creature != null) {
/*      */       
/* 2187 */       int posX = creature.getTileX();
/* 2188 */       int posY = creature.getTileY();
/* 2189 */       boolean onS = creature.isOnSurface();
/* 2190 */       buf.append("radio{group=\"tid\";id=\"3," + posX + "," + posY + "," + onS + "\"};label{text=\"Current Coord (X,Y,surface)\"};radio{group=\"tid\";id=\"" + '\f' + "," + posX + "," + posY + "," + onS + "\"};label{text=\"(" + posX + "," + posY + "," + onS + ")\"}");
/*      */ 
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */       
/* 2197 */       buf.append("label{text=\"\"};label{text=\"Current Coord\"};label{text=\"\"};label{text=\"missing creature\"};");
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2203 */     buf.append("label{text=\"\"};label{text=\"State\"};label{text=\"\"};label{text=\"" + wagoner
/*      */ 
/*      */         
/* 2206 */         .getStateName() + "\"};");
/*      */ 
/*      */     
/*      */     try {
/* 2210 */       Item home = Items.getItem(wagoner.getHomeWaystoneId());
/* 2211 */       int posX = home.getTileX();
/* 2212 */       int posY = home.getTileY();
/* 2213 */       boolean onS = home.isOnSurface();
/* 2214 */       buf.append("radio{group=\"tid\";id=\"3," + posX + "," + posY + "," + onS + "\"};label{text=\"Home Coord (X,Y,surface)\"};radio{group=\"tid\";id=\"" + '\f' + "," + posX + "," + posY + "," + onS + "\"};label{text=\"(" + posX + "," + posY + "," + onS + ")\"}");
/*      */ 
/*      */ 
/*      */     
/*      */     }
/* 2219 */     catch (NoSuchItemException e) {
/*      */       
/* 2221 */       buf.append("label{text=\"\"};label{text=\"Home Coord\"};label{text=\"\"};label{text=\"missing waystone (" + wagoner
/*      */ 
/*      */           
/* 2224 */           .getHomeWaystoneId() + ")\"};");
/*      */     } 
/*      */     
/* 2227 */     buf.append("}");
/* 2228 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String villageDetails(Village aVillage) {
/* 2233 */     StringBuilder buf = new StringBuilder();
/* 2234 */     int numbCitizens = (aVillage.getCitizens()).length;
/* 2235 */     int numbAllies = (aVillage.getAllies()).length;
/* 2236 */     int numbKos = (aVillage.getReputations()).length;
/* 2237 */     int numbBranded = (Creatures.getInstance().getBranded(aVillage.id)).length;
/* 2238 */     int numbRoles = (aVillage.getRoles()).length;
/*      */     
/* 2240 */     if (this.displaySubType == 1)
/* 2241 */     { buf.append(villageSummary(aVillage)); }
/* 2242 */     else if (this.displaySubType == 4)
/* 2243 */     { buf.append(villageCitizens(aVillage)); }
/* 2244 */     else if (this.displaySubType == 5)
/* 2245 */     { buf.append(villageAllies(aVillage)); }
/* 2246 */     else if (this.displaySubType == 6)
/* 2247 */     { buf.append(villageKos(aVillage)); }
/* 2248 */     else { if (this.displaySubType == 7) {
/*      */ 
/*      */         
/* 2251 */         VillageRolesManageQuestion vrmq = new VillageRolesManageQuestion(getResponder(), "Show roles", "Showing roles and titles.", aVillage.getId(), -10, this.backString, this.rowsPerPage, this.currentPage);
/*      */         
/* 2253 */         vrmq.sendQuestion();
/* 2254 */         return "";
/*      */       } 
/* 2256 */       if (this.displaySubType == 8) {
/* 2257 */         buf.append(villageBranded(aVillage));
/* 2258 */       } else if (this.displaySubType == 3) {
/* 2259 */         buf.append(villageHistory(aVillage));
/* 2260 */       } else if (this.displaySubType == 9) {
/* 2261 */         buf.append(villageSettings(aVillage));
/*      */       }  }
/*      */     
/* 2264 */     buf.append("label{type=\"bold\";text=\"------------------------------ Links --------------------------------------\"}");
/*      */     
/* 2266 */     buf.append("table{rows=\"4\";cols=\"4\";");
/* 2267 */     buf.append("radio{group=\"tid\";id=\"2," + this.wurmId + "," + '\001' + "\"};label{text=\"Summary" + ((this.displaySubType == 1) ? " (Showing)" : "") + "\"};");
/*      */     
/* 2269 */     buf.append(((numbCitizens == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"2," + this.wurmId + "," + '\004' + "\"};")) + "label{text=\"" + numbCitizens + " Citizens" + ((this.displaySubType == 4) ? " (Showing)" : "") + "\"};");
/*      */ 
/*      */ 
/*      */     
/* 2273 */     buf.append(((numbAllies == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"2," + this.wurmId + "," + '\005' + "\"};")) + "label{text=\"" + numbAllies + " Allies" + ((this.displaySubType == 5) ? " (Showing)" : "") + "\"};");
/*      */ 
/*      */     
/* 2276 */     buf.append(((numbKos == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"2," + this.wurmId + "," + '\006' + "\"};")) + "label{text=\"" + numbKos + " KOS" + ((this.displaySubType == 6) ? " (Showing)" : "") + "\"};");
/*      */ 
/*      */ 
/*      */     
/* 2280 */     buf.append(((numbBranded == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"2," + this.wurmId + "," + '\b' + "\"};")) + "label{text=\"" + numbBranded + " Branded animals" + ((this.displaySubType == 8) ? " (Showing)" : "") + "\"};");
/*      */ 
/*      */ 
/*      */     
/* 2284 */     buf.append(((aVillage.getHistorySize() == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"2," + this.wurmId + "," + '\003' + "\"};")) + "label{text=\"" + aVillage
/*      */         
/* 2286 */         .getHistorySize() + " History" + ((this.displaySubType == 3) ? " (Showing)" : "") + "\"};");
/*      */ 
/*      */     
/* 2289 */     buf.append("radio{group=\"tid\";id=\"2," + this.wurmId + "," + '\007' + "\"};label{text=\"" + numbRoles + " Roles" + ((this.displaySubType == 7) ? " (Showing)" : "") + "\"};");
/*      */     
/* 2291 */     buf.append("radio{group=\"tid\";id=\"2," + this.wurmId + "," + '\t' + "\"};label{text=\"Settings" + ((this.displaySubType == 9) ? " (Showing)" : "") + "\"};");
/*      */ 
/*      */     
/* 2294 */     buf.append("}");
/* 2295 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String villageSummary(Village aVillage) {
/* 2300 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 2302 */     buf.append("table{rows=\"16\";cols=\"4\";");
/*      */     
/* 2304 */     buf.append("label{text=\"\"};label{text=\"Name\"};label{text=\"\"};label{text=\"" + aVillage
/*      */ 
/*      */         
/* 2307 */         .getName() + "\"};");
/*      */     
/* 2309 */     buf.append("label{text=\"\"};label{text=\"Motto\"};label{text=\"\"};label{text=\"" + aVillage
/*      */ 
/*      */         
/* 2312 */         .getMotto() + "\"};");
/*      */     
/* 2314 */     buf.append("radio{group=\"tid\";id=\"1," + aVillage.getDeedId() + "," + '\001' + "\"};label{text=\"Deed Id\"};label{text=\"\"};label{text=\"" + aVillage
/*      */ 
/*      */         
/* 2317 */         .getDeedId() + "\"};");
/*      */     
/* 2319 */     buf.append("label{text=\"\"};label{text=\"Permenent?\"};label{text=\"\"};label{text=\"" + aVillage.isPermanent + "\"}");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2324 */     buf.append("label{text=\"\"};label{text=\"Location\"};label{text=\"\"};label{text=\"" + 
/*      */ 
/*      */         
/* 2327 */         Kingdoms.getNameFor(aVillage.kingdom) + " (" + aVillage.kingdom + ")\"};");
/*      */ 
/*      */     
/* 2330 */     int tokenx = aVillage.getTokenX();
/* 2331 */     int tokeny = aVillage.getTokenY();
/* 2332 */     int sizeWest = tokenx - aVillage.getStartX();
/* 2333 */     int sizeEast = aVillage.getEndX() - tokenx;
/* 2334 */     int sizeNorth = tokeny - aVillage.getStartY();
/* 2335 */     int sizeSouth = aVillage.getEndY() - tokeny;
/*      */     
/* 2337 */     buf.append("label{text=\"\"};label{text=\"Size E-W\"};label{text=\"\"};label{text=\"E:" + sizeEast + " W:" + sizeWest + " (X:" + aVillage
/*      */ 
/*      */         
/* 2340 */         .getDiameterX() + ")\"};");
/*      */     
/* 2342 */     buf.append("label{text=\"\"};label{text=\"Size N-S\"};label{text=\"\"};label{text=\"N:" + sizeNorth + " S:" + sizeSouth + " (Y:" + aVillage
/*      */ 
/*      */         
/* 2345 */         .getDiameterY() + ")\"};");
/*      */     
/* 2347 */     buf.append("label{text=\"\"};label{text=\"Perimeter\"};label{text=\"\"};label{text=\"" + (5 + aVillage
/*      */ 
/*      */         
/* 2350 */         .getPerimeterSize()) + "\"};");
/*      */     
/* 2352 */     PlayerInfo founderInfo = PlayerInfoFactory.createPlayerInfo(aVillage.getFounderName());
/* 2353 */     buf.append("radio{group=\"tid\";id=\"1," + founderInfo.wurmId + "," + '\001' + "\"};label{text=\"Founder\"};label{text=\"\"};label{text=\"" + aVillage
/*      */ 
/*      */         
/* 2356 */         .getFounderName() + "\"};");
/*      */     
/* 2358 */     PlayerInfo mayorInfo = PlayerInfoFactory.createPlayerInfo(aVillage.mayorName);
/* 2359 */     buf.append("radio{group=\"tid\";id=\"1," + mayorInfo.wurmId + "," + '\001' + "\"};label{text=\"Mayor\"};label{text=\"\"};label{text=\"" + aVillage.mayorName + "\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2364 */     if (aVillage.isCapital()) {
/* 2365 */       buf.append("label{text=\"\"};label{text=\"Capital of\"};label{text=\"\"};label{text=\"" + 
/*      */ 
/*      */           
/* 2368 */           Kingdoms.getNameFor(aVillage.kingdom) + "(" + aVillage.kingdom + ")\"}");
/*      */     } else {
/*      */       
/* 2371 */       buf.append("label{text=\"\"};label{text=\"Capital of\"};label{text=\"\"};label{type=\"italic\";text=\"nothing\"}");
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 2376 */     if (aVillage.disband > 0L) {
/*      */       
/* 2378 */       buf.append("label{text=\"\"};label{text=\"Disbanding in\"};label{text=\"\"};label{text=\"" + 
/*      */ 
/*      */           
/* 2381 */           Server.getTimeFor(aVillage.disband - System.currentTimeMillis()) + "\"};");
/*      */       
/* 2383 */       PlayerInfo bInfo = PlayerInfoFactory.getPlayerInfoWithWurmId(aVillage.disbander);
/* 2384 */       if (bInfo != null) {
/* 2385 */         buf.append("radio{group=\"tid\";id=\"1," + bInfo.wurmId + "," + '\001' + "\"};label{text=\"Disbander\"};label{text=\"\"};label{text=\"" + bInfo
/*      */ 
/*      */             
/* 2388 */             .getName() + "\"};");
/*      */       } else {
/* 2390 */         buf.append("label{text=\"\"};label{text=\"Disbander\"};label{text=\"\"};label{type=\"italic\";text=\"unknown\"};");
/*      */       
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 2397 */       buf.append("label{text=\"\"};label{text=\"Disbanding in\"};label{text=\"\"};label{type=\"italic\";text=\"N/A\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2402 */       buf.append("label{text=\"\"};label{text=\"Disbander\"};label{text=\"\"};label{type=\"italic\";text=\"N/A\"};");
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2408 */     if (aVillage.guards != null) {
/* 2409 */       buf.append("label{text=\"\"};label{text=\"Guards\"};label{text=\"\"};label{text=\"" + aVillage.guards
/*      */ 
/*      */           
/* 2412 */           .size() + "\"}");
/*      */     } else {
/* 2414 */       buf.append("label{text=\"\"};label{text=\"Guards\"};label{text=\"\"};label{type=\"italic\";text=\"none\"}");
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 2421 */       short[] sp = aVillage.getTokenCoords();
/* 2422 */       Integer tx = Integer.valueOf(sp[0]);
/* 2423 */       Integer ty = Integer.valueOf(sp[1]);
/* 2424 */       buf.append("radio{group=\"tid\";id=\"3," + tx + "," + ty + ",true\"};label{text=\"Token Coord (X,Y,surface)\"};radio{group=\"tid\";id=\"" + '\f' + "," + tx + "," + ty + ",true\"};label{text=\"(" + tx + "," + ty + ",true)\"}");
/*      */ 
/*      */ 
/*      */     
/*      */     }
/* 2429 */     catch (NoSuchItemException e) {
/*      */ 
/*      */       
/* 2432 */       buf.append("label{text=\"\"};label{text=\"Token Coord (X,Y,surface)\"};label{text=\"\"};label{type=\"italic\";text=\"unknown\"}");
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 2437 */     buf.append("label{text=\"\"};label{text=\"in Alliance\"};label{text=\"\"};label{text=\"" + aVillage
/*      */ 
/*      */         
/* 2440 */         .getAllianceName() + "\"}");
/*      */     
/* 2442 */     buf.append("}");
/* 2443 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String villageCitizens(Village aVillage) {
/* 2448 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 2450 */     Citizen[] citizens = aVillage.getCitizens();
/* 2451 */     if (citizens.length > 0) {
/*      */       
/* 2453 */       Arrays.sort((Object[])citizens);
/* 2454 */       buf.append("label{text=\"total # of citizens:" + citizens.length + "\"};");
/*      */       
/* 2456 */       int pages = (citizens.length - 1) / this.rowsPerPage;
/* 2457 */       int start = this.currentPage * this.rowsPerPage;
/* 2458 */       int last = (start + this.rowsPerPage < citizens.length) ? (start + this.rowsPerPage) : citizens.length;
/* 2459 */       String nav = makeNav(this.currentPage, pages);
/*      */       
/* 2461 */       if (pages > 0) {
/* 2462 */         buf.append(nav);
/*      */       }
/* 2464 */       if (last > start) {
/*      */         
/* 2466 */         buf.append("table{rows=\"" + (last - start + 1) + "\";cols=\"4\";label{text=\"\"};label{text=\"Wurm Id\"};label{text=\"Name\"};label{text=\"Role\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2471 */         for (int x = start; x < last; x++) {
/*      */           
/* 2473 */           Citizen citizen = citizens[x];
/* 2474 */           buf.append("radio{group=\"tid\";id=\"1," + citizen.getId() + "," + '\001' + "\"};label{text=\"" + citizen
/* 2475 */               .getId() + "\"};label{text=\"" + citizen
/* 2476 */               .getName() + "\"};label{text=\"" + citizen
/* 2477 */               .getRole().getName() + "\"};");
/*      */         } 
/* 2479 */         buf.append("}");
/*      */       } else {
/*      */         
/* 2482 */         buf.append("label{text=\"Nothing to show\"}");
/*      */       } 
/* 2484 */       if (pages > 0) {
/* 2485 */         buf.append(nav);
/*      */       }
/*      */     } else {
/* 2488 */       buf.append("label{text=\"" + aVillage.getName() + " has no citizens!\"};");
/* 2489 */     }  return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String villageAllies(Village aVillage) {
/* 2494 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 2496 */     Village[] allies = aVillage.getAllies();
/* 2497 */     Arrays.sort((Object[])allies);
/* 2498 */     buf.append("label{text=\"total # of allies:" + allies.length + " in " + aVillage.getAllianceName() + "\"};");
/*      */     
/* 2500 */     int pages = (allies.length - 1) / this.rowsPerPage;
/* 2501 */     int start = this.currentPage * this.rowsPerPage;
/* 2502 */     int last = (start + this.rowsPerPage < allies.length) ? (start + this.rowsPerPage) : allies.length;
/* 2503 */     String nav = "harray{" + ((this.currentPage > 0) ? "radio{group=\"tid\";id=\"7\";text=\"Prev \"};" : "") + "label{text=\"Page " + (this.currentPage + 1) + " of " + (pages + 1) + " \"};" + ((this.currentPage < pages) ? "radio{group=\"tid\";id=\"8\";text=\"Next\"};" : "") + "}";
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2508 */     if (pages > 0) {
/* 2509 */       buf.append(nav);
/*      */     }
/* 2511 */     if (allies.length > 0) {
/*      */       
/* 2513 */       if (last > start) {
/*      */         
/* 2515 */         buf.append("table{rows=\"" + (last - start + 1) + "\";cols=\"2\";label{text=\"\"};label{text=\"Name\"};");
/*      */ 
/*      */         
/* 2518 */         for (int x = start; x < last; x++) {
/*      */           
/* 2520 */           Village allie = allies[x];
/* 2521 */           buf.append("radio{group=\"tid\";id=\"2," + allie.getId() + "," + '\001' + "\"};label{text=\"" + allie
/* 2522 */               .getName() + "\"};");
/*      */         } 
/* 2524 */         buf.append("}");
/*      */       } else {
/*      */         
/* 2527 */         buf.append("label{text=\"Nothing to show\"}");
/*      */       } 
/*      */     } else {
/* 2530 */       buf.append("label{text=\"No Allies\"};");
/*      */     } 
/* 2532 */     if (pages > 0) {
/* 2533 */       buf.append(nav);
/*      */     }
/* 2535 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String villageKos(Village aVillage) {
/* 2540 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 2542 */     Reputation[] reputations = aVillage.getReputations();
/* 2543 */     Arrays.sort((Object[])reputations);
/* 2544 */     buf.append("label{text=\"total # of players:" + reputations.length + "\"};");
/*      */     
/* 2546 */     int pages = (reputations.length - 1) / this.rowsPerPage;
/* 2547 */     int start = this.currentPage * this.rowsPerPage;
/* 2548 */     int last = (start + this.rowsPerPage < reputations.length) ? (start + this.rowsPerPage) : reputations.length;
/* 2549 */     String nav = "harray{" + ((this.currentPage > 0) ? "radio{group=\"tid\";id=\"7\";text=\"Prev \"};" : "") + "label{text=\"Page " + (this.currentPage + 1) + " of " + (pages + 1) + " \"};" + ((this.currentPage < pages) ? "radio{group=\"tid\";id=\"8\";text=\"Next\"};" : "") + "}";
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2554 */     if (pages > 0) {
/* 2555 */       buf.append(nav);
/*      */     }
/* 2557 */     if (last > start) {
/*      */       
/* 2559 */       buf.append("table{rows=\"" + (last - start + 1) + "\";cols=\"5\";label{text=\"\"};label{text=\"Wurm Id\"};label{text=\"Name\"};label{text=\"Reputation\"};label{text=\"Permanent\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2565 */       for (int x = start; x < last; x++) {
/*      */         
/* 2567 */         Reputation rep = reputations[x];
/* 2568 */         long playerWurmId = rep.getWurmId();
/*      */ 
/*      */         
/*      */         try {
/* 2572 */           buf.append("radio{group=\"tid\";id=\"1," + playerWurmId + "," + '\001' + "\"};label{text=\"" + playerWurmId + "\"};label{text=\"" + rep
/*      */               
/* 2574 */               .getNameFor() + "\"};label{text=\"" + rep
/* 2575 */               .getValue() + "\"};label{text=\"" + rep
/* 2576 */               .isPermanent() + "\"};");
/*      */         }
/* 2578 */         catch (NoSuchPlayerException e) {
/*      */           
/* 2580 */           buf.append("radio{group=\"tid\";id=\"1," + playerWurmId + "," + '\001' + "\"};label{text=\"" + playerWurmId + "\"};label{text=\"Player not found\"};label{text=\"" + rep
/*      */ 
/*      */               
/* 2583 */               .getValue() + "\"};label{text=\"" + rep
/* 2584 */               .isPermanent() + "\"};");
/*      */         } 
/*      */       } 
/* 2587 */       buf.append("}");
/*      */     }
/* 2589 */     else if (reputations.length == 0) {
/* 2590 */       buf.append("label{text=\"No one on KOS\"}");
/*      */     } else {
/* 2592 */       buf.append("label{text=\"Nothing to show\"}");
/*      */     } 
/* 2594 */     if (pages > 0) {
/* 2595 */       buf.append(nav);
/*      */     }
/* 2597 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String villageSettings(Village aVillage) {
/* 2602 */     StringBuilder buf = new StringBuilder();
/* 2603 */     buf.append("label{text=\"Settlement is a " + (aVillage.isDemocracy() ? "PERMANENT " : "non-") + "democracy.\"}");
/* 2604 */     buf.append("label{text=\"Guards " + (aVillage.allowsAggCreatures() ? "DO" : "dont") + " ignore aggressive creatures.\"}");
/*      */     
/* 2606 */     buf.append("label{text=\"" + (aVillage.unlimitedCitizens ? "UNLIMITED" : ("Max " + aVillage.getMaxCitizens())) + " citizens.\"}");
/*      */     
/* 2608 */     buf.append("label{text=\"Highways are " + (aVillage.isHighwayAllowed() ? "" : "NOT ") + "allowed.\"}");
/* 2609 */     buf.append("label{text=\"Kos is " + (aVillage.isKosAllowed() ? "" : "NOT ") + "allowed.\"}");
/* 2610 */     buf.append("label{text=\"Players " + (aVillage.isHighwayFound() ? "can" : "cannot") + " find route to village.\"}");
/* 2611 */     buf.append("label{text=\"Currently " + (aVillage.hasHighway() ? "has" : "does not have") + " a highway.\"}");
/*      */     
/* 2613 */     if (aVillage.getMotd().length() > 0) {
/* 2614 */       buf.append("label{text=\"MOTD: " + aVillage.getMotd() + "\";}");
/*      */     } else {
/* 2616 */       buf.append("label{text=\"No MOTD set.\"}");
/* 2617 */     }  return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String villageBranded(Village aVillage) {
/* 2622 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 2624 */     Creature[] creatures = Creatures.getInstance().getBranded(aVillage.id);
/* 2625 */     if (creatures.length > 0) {
/* 2626 */       buf.append(creaturesTable(creatures));
/*      */     } else {
/* 2628 */       buf.append("label{text=\"Nothing Branded by this vilage\"};");
/* 2629 */     }  return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String villageHistory(Village aVillage) {
/* 2634 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 2636 */     HistoryEvent[] histories = aVillage.getHistoryEvents();
/* 2637 */     Arrays.sort((Object[])histories);
/*      */     
/* 2639 */     if (histories.length > 0) {
/* 2640 */       buf.append(villageHistoryTable(histories));
/*      */     } else {
/* 2642 */       buf.append("label{text=\"" + aVillage.getName() + " has no history!?\"};");
/* 2643 */     }  return buf.toString();
/*      */   }
/*      */   
/*      */   private String playerDetails(PlayerInfo pInfo) {
/*      */     Map<String, Byte> referrals;
/* 2648 */     StringBuilder buf = new StringBuilder(8000);
/*      */     
/* 2650 */     Player p = null;
/* 2651 */     int numbBody = 0;
/* 2652 */     int numbInventory = 0;
/* 2653 */     Skills skills = null;
/* 2654 */     Bank bank = null;
/*      */     
/*      */     try {
/* 2657 */       p = getPlayerFromInfo(pInfo);
/* 2658 */       numbBody = (p.getBody().getAllItems()).length;
/* 2659 */       numbInventory = (p.getInventory().getItemsAsArray()).length;
/*      */     }
/* 2661 */     catch (Exception exception) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2670 */     int numbSkills = 0;
/*      */     
/*      */     try {
/* 2673 */       if (p != null) {
/* 2674 */         skills = p.getSkills();
/*      */       } else {
/*      */         
/* 2677 */         skills = SkillsFactory.createSkills(this.wurmId);
/* 2678 */         skills.load();
/*      */       } 
/* 2680 */       Skill[] skillarr = skills.getSkills();
/* 2681 */       numbSkills = skillarr.length;
/*      */     }
/* 2683 */     catch (Exception exception) {}
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2688 */     int numbBankItems = 0;
/*      */     
/*      */     try {
/* 2691 */       bank = Banks.getBank(this.wurmId);
/* 2692 */       if (bank != null) {
/*      */         
/* 2694 */         BankSlot[] slots = bank.slots;
/* 2695 */         if (slots != null)
/*      */         {
/* 2697 */           for (int x = 0; x < slots.length; x++) {
/*      */             
/* 2699 */             if (slots[x] != null) {
/* 2700 */               numbBankItems++;
/*      */             }
/*      */           } 
/*      */         }
/*      */       } 
/* 2705 */     } catch (Exception exception) {}
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2710 */     if (Servers.isRealLoginServer()) {
/* 2711 */       referrals = PlayerInfoFactory.getReferrers(pInfo.wurmId);
/*      */     } else {
/* 2713 */       referrals = (new LoginServerWebConnection()).getReferrers(getResponder(), pInfo.wurmId);
/*      */     } 
/* 2715 */     Village[] kosList = Villages.getKosVillagesFor(pInfo.wurmId);
/* 2716 */     int numbCaring = (Creatures.getInstance().getProtectedCreaturesFor(pInfo.wurmId)).length;
/* 2717 */     int numbTitles = (pInfo.getTitles()).length;
/* 2718 */     int numbFriends = (pInfo.getFriends()).length;
/* 2719 */     Set<WurmMail> sent = WurmMail.getMailsSendBy(this.wurmId);
/* 2720 */     Set<WurmMail> waiting = WurmMail.getMailsFor(this.wurmId);
/* 2721 */     int numbSent = sent.size();
/* 2722 */     int numbWaiting = waiting.size();
/* 2723 */     int numbHistoryIP = pInfo.historyIPStart.size();
/* 2724 */     int numbHistoryEmail = pInfo.historyEmail.size();
/* 2725 */     int numbIgnoring = (pInfo.getIgnored()).length;
/* 2726 */     int numbReferrals = (referrals == null) ? 0 : referrals.size();
/* 2727 */     int numbKos = kosList.length;
/* 2728 */     Structure[] buildings = new Structure[0];
/* 2729 */     List<Item> corpses = new ArrayList<>();
/* 2730 */     List<Item> carts = new ArrayList<>();
/* 2731 */     List<Item> ships = new ArrayList<>();
/*      */ 
/*      */     
/* 2734 */     MineDoorPermission[] minedoors = new MineDoorPermission[0];
/* 2735 */     FenceGate[] gates = new FenceGate[0];
/* 2736 */     List<Route> path = null;
/* 2737 */     if (p != null) {
/*      */ 
/*      */       
/* 2740 */       Items.getOwnedCorpsesCartsShipsFor(p, corpses, carts, ships);
/* 2741 */       buildings = Structures.getOwnedBuildingFor(p);
/* 2742 */       minedoors = MineDoorPermission.getOwnedMinedoorsFor(p);
/* 2743 */       gates = FenceGate.getOwnedGatesFor(p);
/* 2744 */       path = p.getHighwayPath();
/*      */     } 
/* 2746 */     int numbCorpses = corpses.size();
/* 2747 */     int numbBuildings = buildings.length;
/* 2748 */     int numbCarts = carts.size();
/* 2749 */     int numbShips = ships.size();
/* 2750 */     int numbMinedoors = minedoors.length;
/* 2751 */     int numbGates = gates.length;
/*      */     
/* 2753 */     if (this.displaySubType == 1) {
/* 2754 */       buf.append(playerSummary(pInfo, p));
/* 2755 */     } else if (this.displaySubType == 4) {
/* 2756 */       buf.append(playerFriends(pInfo));
/* 2757 */     } else if (this.displaySubType == 5) {
/* 2758 */       buf.append(playerSkills(skills));
/* 2759 */     } else if (this.displaySubType == 6) {
/* 2760 */       buf.append(playerBank(bank));
/* 2761 */     } else if (this.displaySubType == 7) {
/* 2762 */       buf.append(playerTitles(pInfo));
/* 2763 */     } else if (this.displaySubType == 8) {
/* 2764 */       buf.append(playerInventory(pInfo, p));
/* 2765 */     } else if (this.displaySubType == 9) {
/* 2766 */       buf.append(playerBody(pInfo, p));
/* 2767 */     } else if (this.displaySubType == 10) {
/* 2768 */       buf.append(playerCaringFor(pInfo));
/* 2769 */     } else if (this.displaySubType == 11) {
/* 2770 */       buf.append(playerMail(pInfo, "from", sent));
/* 2771 */     } else if (this.displaySubType == 12) {
/* 2772 */       buf.append(playerMail(pInfo, "to", waiting));
/* 2773 */     } else if (this.displaySubType == 13) {
/* 2774 */       buf.append(playerHistoryIP(pInfo));
/* 2775 */     } else if (this.displaySubType == 14) {
/* 2776 */       buf.append(playerHistoryEmail(pInfo));
/* 2777 */     } else if (this.displaySubType == 15) {
/* 2778 */       buf.append(playerIgnoreList(pInfo));
/* 2779 */     } else if (this.displaySubType == 16) {
/* 2780 */       buf.append(playerCorpses(pInfo));
/* 2781 */     } else if (this.displaySubType == 17) {
/* 2782 */       buf.append(playerReferrs(referrals, pInfo));
/* 2783 */     } else if (this.displaySubType == 18) {
/* 2784 */       buf.append(kosVillageTable(kosList, pInfo));
/* 2785 */     } else if (this.displaySubType == 19) {
/* 2786 */       buf.append(playerBuildings(p, buildings));
/* 2787 */     } else if (this.displaySubType == 20) {
/* 2788 */       buf.append(playerItems("carts", p, carts.<Item>toArray(new Item[carts.size()])));
/* 2789 */     } else if (this.displaySubType == 21) {
/* 2790 */       buf.append(playerItems("ships", p, ships.<Item>toArray(new Item[ships.size()])));
/* 2791 */     } else if (this.displaySubType == 22) {
/* 2792 */       buf.append(playerMinedoors(p, minedoors));
/* 2793 */     } else if (this.displaySubType == 23) {
/* 2794 */       buf.append(playerGates(p, gates));
/* 2795 */     } else if (this.displaySubType == 24) {
/* 2796 */       buf.append(playerPath(p, path));
/*      */     } 
/*      */     
/* 2799 */     buf.append("label{type=\"bold\";text=\"------------------------------ Links --------------------------------------\"}");
/*      */     
/* 2801 */     buf.append("table{rows=\"3\";cols=\"6\";");
/* 2802 */     buf.append("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\001' + "\"};label{text=\"Summary" + ((this.displaySubType == 1) ? " (Showing)" : "") + "\"};");
/*      */     
/* 2804 */     buf.append(((numbFriends == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\004' + "\"};")) + "label{text=\"" + numbFriends + " Friends" + ((this.displaySubType == 4) ? " (Showing)" : "") + "\"};");
/*      */ 
/*      */     
/* 2807 */     buf.append(((numbSkills == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\005' + "\"};")) + "label{text=\"" + numbSkills + " Skills" + ((this.displaySubType == 5) ? " (Showing)" : "") + "\"};");
/*      */ 
/*      */ 
/*      */     
/* 2811 */     buf.append(((numbBankItems == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\006' + "\"};")) + "label{text=\"" + numbBankItems + " Bank Items" + ((this.displaySubType == 6) ? " (Showing)" : "") + "\"};");
/*      */ 
/*      */     
/* 2814 */     buf.append(((numbTitles == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\007' + "\"};")) + "label{text=\"" + numbTitles + " Titles" + ((this.displaySubType == 7) ? " (Showing)" : "") + "\"};");
/*      */ 
/*      */     
/* 2817 */     buf.append(((numbCaring == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\n' + "\"};")) + "label{text=\"" + numbCaring + " Caring For" + ((this.displaySubType == 10) ? " (Showing)" : "") + "\"};");
/*      */ 
/*      */ 
/*      */     
/* 2821 */     buf.append(((numbSent == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\013' + "\"};")) + "label{text=\"" + numbSent + " Mail Sent" + ((this.displaySubType == 11) ? " (Showing)" : "") + "\"};");
/*      */ 
/*      */ 
/*      */     
/* 2825 */     buf.append(((numbWaiting == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\f' + "\"};")) + "label{text=\"" + numbWaiting + " Mail Waiting" + ((this.displaySubType == 12) ? " (Showing)" : "") + "\"};");
/*      */ 
/*      */ 
/*      */     
/* 2829 */     buf.append(((numbIgnoring == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\017' + "\"};")) + "label{text=\"" + numbIgnoring + " Ignored" + ((this.displaySubType == 15) ? " (Showing)" : "") + "\"};");
/*      */ 
/*      */ 
/*      */     
/* 2833 */     buf.append(((numbHistoryIP == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\r' + "\"};")) + "label{text=\"" + numbHistoryIP + " IP History" + ((this.displaySubType == 13) ? " (Showing)" : "") + "\"};");
/*      */ 
/*      */ 
/*      */     
/* 2837 */     buf.append(((numbHistoryEmail == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\016' + "\"};")) + "label{text=\"" + numbHistoryEmail + " Email History" + ((this.displaySubType == 14) ? " (Showing)" : "") + "\"};");
/*      */ 
/*      */     
/* 2840 */     buf.append(((numbCorpses == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\020' + "\"};")) + "label{text=\"" + numbCorpses + " Corpses" + ((this.displaySubType == 16) ? " (Showing)" : "") + "\"};");
/*      */ 
/*      */ 
/*      */     
/* 2844 */     buf.append(((numbReferrals == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\021' + "\"};")) + "label{text=\"" + numbReferrals + " Referrs" + ((this.displaySubType == 17) ? " (Showing)" : "") + "\"};");
/*      */ 
/*      */     
/* 2847 */     buf.append(((numbKos == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\022' + "\"};")) + "label{text=\"" + numbKos + " times on KOS" + ((this.displaySubType == 18) ? " (Showing)" : "") + "\"};");
/*      */ 
/*      */ 
/*      */     
/* 2851 */     if (p != null) {
/*      */       
/* 2853 */       buf.append(((numbInventory == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\b' + "\"};")) + "label{text=\"" + numbInventory + " Inventory" + ((this.displaySubType == 8) ? " (Showing)" : "") + "\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2858 */       buf.append(((numbBody == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\t' + "\"};")) + "label{text=\"" + numbBody + " Body" + ((this.displaySubType == 9) ? " (Showing)" : "") + "\"};");
/*      */ 
/*      */       
/* 2861 */       buf.append(((numbBuildings == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\023' + "\"};")) + "label{text=\"" + numbBuildings + " Owned Buildings" + ((this.displaySubType == 19) ? " (Showing)" : "") + "\"};");
/*      */ 
/*      */       
/* 2864 */       buf.append(((numbCarts == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\024' + "\"};")) + "label{text=\"" + numbCarts + " Owned Carts" + ((this.displaySubType == 20) ? " (Showing)" : "") + "\"};");
/*      */ 
/*      */ 
/*      */       
/* 2868 */       buf.append(((numbShips == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\025' + "\"};")) + "label{text=\"" + numbShips + " Owned Ships" + ((this.displaySubType == 21) ? " (Showing)" : "") + "\"};");
/*      */ 
/*      */       
/* 2871 */       buf.append(((numbMinedoors == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\026' + "\"};")) + "label{text=\"" + numbMinedoors + " Owned Minedoors" + ((this.displaySubType == 22) ? " (Showing)" : "") + "\"};");
/*      */ 
/*      */       
/* 2874 */       buf.append(((numbGates == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\027' + "\"};")) + "label{text=\"" + numbGates + " Owned Gates" + ((this.displaySubType == 23) ? " (Showing)" : "") + "\"};");
/*      */ 
/*      */ 
/*      */       
/* 2878 */       if (Features.Feature.HIGHWAYS.isEnabled()) {
/*      */         
/* 2880 */         buf.append(((path == null) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\030' + "\"};")) + "label{text=\"" + ((path == null) ? "No" : "Has") + " Highway Route" + ((this.displaySubType == 24) ? " (Showing)" : "") + "\"};");
/*      */ 
/*      */         
/* 2883 */         buf.append("label{text=\"\"};label{text=\"\"};");
/* 2884 */         buf.append("label{text=\"\"};label{text=\"\"};");
/*      */       } 
/*      */     } else {
/*      */       
/* 2888 */       buf.append("label{text=\"\"};label{text=\"\"};");
/*      */     } 
/* 2890 */     buf.append("}");
/*      */     
/* 2892 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String playerSummary(PlayerInfo pInfo, @Nullable Player p) {
/* 2897 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 2899 */     boolean thisServer = false;
/* 2900 */     String serverName = "";
/* 2901 */     boolean playerFound = false;
/* 2902 */     boolean playerExists = true;
/* 2903 */     PlayerInfo localInfo = PlayerInfoFactory.getPlayerInfoWithWurmId(this.wurmId);
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 2908 */       if (localInfo != null) {
/* 2909 */         localInfo.load();
/*      */       }
/* 2911 */       if (localInfo == null || localInfo.currentServer != Servers.localServer.id)
/*      */       {
/* 2913 */         LoginServerWebConnection lsw = new LoginServerWebConnection();
/*      */         
/*      */         try {
/* 2916 */           long[] wurmids = { this.wurmId };
/*      */           
/* 2918 */           PlayerState loginState = new PlayerState((byte[])lsw.getPlayerStates(wurmids).get(Long.valueOf(this.wurmId)));
/*      */           
/* 2920 */           if (loginState != null && loginState.getServerId() >= 0)
/*      */           {
/* 2922 */             playerFound = true;
/* 2923 */             serverName = loginState.getServerName();
/*      */           
/*      */           }
/*      */           else
/*      */           {
/* 2928 */             playerExists = false;
/*      */           }
/*      */         
/* 2931 */         } catch (Exception e) {
/*      */ 
/*      */           
/* 2934 */           logger.log(Level.WARNING, e.getMessage(), e);
/*      */         }
/*      */       
/*      */       }
/*      */       else
/*      */       {
/* 2940 */         thisServer = true;
/* 2941 */         serverName = Servers.localServer.getName();
/* 2942 */         playerFound = true;
/*      */       }
/*      */     
/* 2945 */     } catch (IOException e) {
/*      */ 
/*      */       
/* 2948 */       logger.log(Level.WARNING, e.getMessage(), e);
/*      */     }
/* 2950 */     catch (Exception e) {
/*      */ 
/*      */       
/* 2953 */       logger.log(Level.WARNING, "CatchAll:" + e.getMessage(), e);
/*      */     } 
/*      */     
/* 2956 */     String pStatus = "On Server " + serverName;
/* 2957 */     String pColour = "66,66,225";
/* 2958 */     if (playerFound) {
/*      */ 
/*      */       
/* 2961 */       if (thisServer) {
/*      */         try
/*      */         {
/*      */           
/* 2965 */           Players.getInstance().getPlayer(pInfo.getPlayerId());
/* 2966 */           serverName = Servers.localServer.getName();
/* 2967 */           pStatus = "Online";
/* 2968 */           pColour = "66,225,66";
/*      */         }
/* 2970 */         catch (NoSuchPlayerException e)
/*      */         {
/* 2972 */           pStatus = "Offline";
/* 2973 */           pColour = "255,66,66";
/*      */         }
/*      */       
/*      */       }
/* 2977 */     } else if (!playerExists) {
/* 2978 */       buf.append("label{text=\"Unknown Player!\"};");
/*      */     } else {
/* 2980 */       buf.append("label{text=\"Player not found\"};");
/*      */     } 
/* 2982 */     if (pInfo != null) {
/*      */       
/* 2984 */       String aState = pStatus;
/* 2985 */       if (!thisServer) {
/* 2986 */         aState = "Wrong Server!";
/*      */       }
/* 2988 */       buf.append("table{rows=\"35\";cols=\"4\";");
/*      */       
/* 2990 */       buf.append("label{text=\"\"};label{text=\"Name\"};label{text=\"\"};label{text=\"" + pInfo
/*      */ 
/*      */           
/* 2993 */           .getName() + "\"};");
/*      */       
/* 2995 */       buf.append("label{text=\"\"};label{text=\"Wurm Id\"};label{text=\"\"};label{text=\"" + this.wurmId + "\"}");
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3000 */       buf.append("label{text=\"\"};label{text=\"Steam Id\"};label{text=\"\"};label{text=\"" + pInfo
/*      */ 
/*      */           
/* 3003 */           .getSteamId() + "\"}");
/*      */       
/* 3005 */       buf.append("label{text=\"\"};label{text=\"Current Server\"};label{text=\"\"};label{text=\"" + serverName + "\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3010 */       buf.append("label{text=\"\"};label{text=\"Status\"};label{text=\"\"};label{color=\"" + pColour + "\";text=\"" + pStatus + "\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3015 */       Village citizenVillage = Villages.getVillageForCreature(this.wurmId);
/*      */       
/* 3017 */       if (citizenVillage != null) {
/*      */         
/* 3019 */         Citizen citiz = citizenVillage.getCitizen(this.wurmId);
/* 3020 */         buf.append("radio{group=\"tid\";id=\"2," + citizenVillage.getId() + "," + '\001' + "\"};label{text=\"CitizenVillage\"};label{text=\"\"};label{text=\"" + citizenVillage
/*      */ 
/*      */             
/* 3023 */             .getName() + "\"};");
/*      */         
/* 3025 */         buf.append("label{text=\"\"};label{text=\"CitizenRole\"};label{text=\"\"};label{text=\"" + citiz
/*      */ 
/*      */             
/* 3028 */             .getRole().getName() + "\"};");
/*      */       }
/*      */       else {
/*      */         
/* 3032 */         buf.append("label{text=\"\"};label{text=\"CitizenVillage\"};label{text=\"\"};label{type=\"italic\";text=\"None\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3037 */         buf.append("label{text=\"\"};label{text=\"CitizenRole\"};label{text=\"\"};label{type=\"italic\";text=\"N/A\"};");
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3043 */       buf.append("label{text=\"\"};label{text=\"Alignment\"};label{text=\"\"};label{text=\"" + new Float(pInfo
/*      */ 
/*      */             
/* 3046 */             .getAlignment()) + "\"};");
/*      */       
/* 3048 */       buf.append("label{text=\"\"};label{text=\"Reputation\"};label{text=\"\"};label{text=\"" + pInfo
/*      */ 
/*      */           
/* 3051 */           .getReputation() + "\"};");
/*      */       
/* 3053 */       buf.append("label{text=\"\"};label{text=\"Karma\"};label{text=\"\"};label{text=\"" + pInfo
/*      */ 
/*      */           
/* 3056 */           .getKarma() + "\"};");
/*      */       
/* 3058 */       buf.append("label{text=\"\"};label{text=\"Sleep Bonus\"};label{text=\"\"};label{text=\"" + pInfo
/*      */ 
/*      */           
/* 3061 */           .getSleepLeft() + " (" + (pInfo.isSleepFrozen() ? "Frozen" : "Active") + ")\"};");
/*      */       
/* 3063 */       buf.append("label{text=\"\"};label{text=\"Battle Rank\"};label{text=\"\"};label{text=\"" + 
/*      */ 
/*      */           
/* 3066 */           Integer.valueOf(pInfo.getRank()) + "\"};");
/*      */       
/* 3068 */       if (pInfo.getDeity() != null) {
/*      */         
/* 3070 */         buf.append("radio{group=\"tid\";id=\"13," + pInfo.getDeity().getNumber() + "," + '\001' + "\"};label{text=\"Deity\"};label{text=\"\"};label{text=\"" + 
/*      */ 
/*      */             
/* 3073 */             (pInfo.getDeity()).name + "\"};");
/*      */       }
/*      */       else {
/*      */         
/* 3077 */         buf.append("label{text=\"\"};label{text=\"Deity\"};label{text=\"\"};label{type=\"italic\";text=\"None\"};");
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3083 */       buf.append("label{text=\"\"};label{text=\"Faith\"};label{text=\"\"};label{text=\"" + new Float(pInfo
/*      */ 
/*      */             
/* 3086 */             .getFaith()) + "\"};");
/*      */       
/* 3088 */       buf.append("label{text=\"\"};label{text=\"Favor\"};label{text=\"\"};label{text=\"" + new Float(pInfo
/*      */ 
/*      */             
/* 3091 */             .getFavor()) + "\"};");
/*      */       
/* 3093 */       if (p != null && p.getCultist() != null) {
/*      */         
/* 3095 */         buf.append("label{text=\"\"};label{text=\"Meditation Path\"};label{text=\"\"};label{text=\"" + 
/*      */ 
/*      */             
/* 3098 */             Cults.getPathNameFor(p.getCultist().getPath()) + " (ID: " + p
/* 3099 */             .getCultist().getPath() + ")\"};");
/* 3100 */         buf.append("label{text=\"\"};label{text=\"Path Level\"};label{text=\"\"};label{text=\"" + 
/*      */ 
/*      */             
/* 3103 */             Cults.getNameForLevel(p.getCultist().getPath(), p.getCultist().getLevel()) + " (" + p
/* 3104 */             .getCultist().getLevel() + ")\"};");
/* 3105 */         buf.append("label{text=\"\"};label{text=\"Last Meditated\"};label{text=\"\"};label{text=\"" + 
/*      */ 
/*      */             
/* 3108 */             WurmCalendar.formatGmt(p.getCultist().getLastMeditated()) + ")\"};");
/* 3109 */         buf.append("label{text=\"\"};label{text=\"Last Path Gained\"};label{text=\"\"};label{text=\"" + 
/*      */ 
/*      */             
/* 3112 */             WurmCalendar.formatGmt(p.getCultist().getLastReceivedLevel()) + ")\"};");
/* 3113 */         buf.append("label{text=\"\"};label{text=\"Last Enlightened\"};label{text=\"\"};label{text=\"" + 
/*      */ 
/*      */             
/* 3116 */             WurmCalendar.formatGmt(p.getCultist().getLastEnlightened()) + ")\"};");
/*      */       }
/*      */       else {
/*      */         
/* 3120 */         buf.append("label{text=\"\"};label{text=\"Meditation Path\"};label{text=\"\"};label{type=\"italic\";text=\"None\"};");
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3126 */       buf.append("label{text=\"\"};label{text=\"Gender\"};label{text=\"\"};");
/*      */ 
/*      */ 
/*      */       
/* 3130 */       if (p != null)
/* 3131 */       { if (p.getSex() == 0) {
/* 3132 */           buf.append("label{text=\"Male\"}");
/*      */         } else {
/* 3134 */           buf.append("label{text=\"Female\"}");
/*      */         }  }
/* 3136 */       else { buf.append("label{type=\"italic\";text=\"(" + aState + ")\"}"); }
/*      */       
/* 3138 */       buf.append("label{text=\"\"};label{text=\"Money in Bank\"};label{text=\"\"};label{text=\"" + 
/*      */ 
/*      */           
/* 3141 */           Long.valueOf(pInfo.money) + "\"};");
/*      */       
/* 3143 */       buf.append("label{text=\"\"};label{text=\"Account Creation Date\"};label{text=\"\"};label{text=\"" + new Date(pInfo.creationDate) + "\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3148 */       buf.append("label{text=\"\"};label{text=\"Premium Account Expiry\"};label{text=\"\"};label{text=\"" + new Date(pInfo
/*      */ 
/*      */             
/* 3151 */             .getPaymentExpire()) + "\"};");
/*      */       
/* 3153 */       if (p != null) {
/*      */         
/* 3155 */         int posX = (int)p.getStatus().getPositionX() >> 2;
/* 3156 */         int posY = (int)p.getStatus().getPositionY() >> 2;
/* 3157 */         boolean onSurface = p.getStatus().isOnSurface();
/*      */         
/* 3159 */         buf.append("radio{group=\"tid\";id=\"3," + posX + "," + posY + "," + onSurface + "\"};label{text=\"Coord (X,Y,surface)\"};radio{group=\"tid\";id=\"" + '\f' + "," + posX + "," + posY + "," + onSurface + "\"};label{text=\"(" + posX + "," + posY + "," + onSurface + ")\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3164 */         buf.append("label{text=\"\"};label{text=\"Actual (X,Y,Z)\"};label{text=\"\"};label{text=\"(" + p
/*      */ 
/*      */             
/* 3167 */             .getPosX() + "," + p.getPosY() + "," + p.getPositionZ() + ")\"}");
/*      */         
/* 3169 */         Village currentVillage = Villages.getVillage(posX, posY, true);
/* 3170 */         Village perimVillage = Villages.getVillageWithPerimeterAt(posX, posY, true);
/*      */         
/* 3172 */         if (currentVillage != null) {
/*      */           
/* 3174 */           buf.append("radio{group=\"tid\";id=\"2," + currentVillage.getId() + "," + '\001' + "\"};label{text=\"In Village\"};label{text=\"\"};label{text=\"" + currentVillage
/*      */ 
/*      */               
/* 3177 */               .getName() + "\"};");
/*      */           
/* 3179 */           buf.append("label{text=\"\"};label{text=\"In Kingdom\"};label{text=\"\"};label{text=\"" + 
/*      */ 
/*      */               
/* 3182 */               Kingdoms.getNameFor(currentVillage.kingdom) + "(" + currentVillage.kingdom + ")\"};");
/*      */         
/*      */         }
/* 3185 */         else if (perimVillage != null) {
/*      */           
/* 3187 */           buf.append("radio{group=\"tid\";id=\"2," + perimVillage.getId() + "," + '\001' + "\"};label{text=\"In Perimeter\"};label{text=\"\"};label{text=\"" + perimVillage
/*      */ 
/*      */               
/* 3190 */               .getName() + "\"};");
/*      */           
/* 3192 */           buf.append("label{text=\"\"};label{text=\"In Kingdom\"};label{text=\"\"};label{text=\"" + 
/*      */ 
/*      */               
/* 3195 */               Kingdoms.getNameFor(perimVillage.kingdom) + "(" + perimVillage.kingdom + ")\"};");
/*      */         
/*      */         }
/* 3198 */         else if (p.currentKingdom != 0) {
/*      */           
/* 3200 */           buf.append("label{text=\"\"};label{text=\"In Village\"};label{text=\"\"};label{type=\"italic\";text=\"none\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 3205 */           buf.append("label{text=\"\"};label{text=\"In Kingdom\"};label{text=\"\"};label{text=\"" + 
/*      */ 
/*      */               
/* 3208 */               Kingdoms.getNameFor(p.currentKingdom) + "(" + p.currentKingdom + ")\"};");
/*      */         
/*      */         }
/*      */         else {
/*      */           
/* 3213 */           buf.append("label{text=\"\"};label{text=\"In Village\"};label{text=\"\"};label{type=\"italic\";text=\"unknown\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 3218 */           buf.append("label{text=\"\"};label{text=\"In Kingdom\"};label{text=\"\"};label{type=\"italic\";text=\"unknown (0)\"};");
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/* 3223 */         buf.append(addBridge(p.getBridgeId()));
/*      */         
/* 3225 */         buf.append("label{text=\"\"};label{text=\"Floor Level\"};label{text=\"\"};label{text=\"" + p
/*      */ 
/*      */             
/* 3228 */             .getFloorLevel() + "\"};");
/*      */         
/* 3230 */         if (p.getBestCompass() != null) {
/* 3231 */           buf.append("radio{group=\"tid\";id=\"1," + p.getBestCompass().getWurmId() + "," + '\001' + "\"};label{text=\"Best Compass\"};label{text=\"\"};label{text=\"" + p
/*      */ 
/*      */               
/* 3234 */               .getBestCompass().getWurmId() + "\"};");
/*      */         } else {
/* 3236 */           buf.append("label{text=\"\"};label{text=\"Best Compass\"};label{text=\"\"};label{text=\"None\"};");
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/* 3241 */         if (p.getBestBeeSmoker() != null) {
/* 3242 */           buf.append("radio{group=\"tid\";id=\"1," + p.getBestBeeSmoker().getWurmId() + "," + '\001' + "\"};label{text=\"Best Bee Smoker\"};label{text=\"\"};label{text=\"" + p
/*      */ 
/*      */               
/* 3245 */               .getBestBeeSmoker().getWurmId() + "\"};");
/*      */         } else {
/* 3247 */           buf.append("label{text=\"\"};label{text=\"Best Bee Smoker\"};label{text=\"\"};label{text=\"None\"};");
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/* 3252 */         if (p.getBestTackleBox() != null) {
/* 3253 */           buf.append("radio{group=\"tid\";id=\"1," + p.getBestTackleBox().getWurmId() + "," + '\001' + "\"};label{text=\"Best Tackle Box\"};label{text=\"\"};label{text=\"" + p
/*      */ 
/*      */               
/* 3256 */               .getBestTackleBox().getWurmId() + "\"};");
/*      */         } else {
/* 3258 */           buf.append("label{text=\"\"};label{text=\"Best Tackle Box\"};label{text=\"\"};label{text=\"None\"};");
/*      */         
/*      */         }
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 3265 */         buf.append("label{text=\"\"};label{text=\"Coord (X,Y,surface)\"};label{text=\"\"};label{type=\"italic\";text=\"(" + aState + ")\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3270 */         buf.append("label{text=\"\"};label{text=\"In Village\"};label{text=\"\"};label{type=\"italic\";text=\"(" + aState + ")\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3275 */         buf.append("label{text=\"\"};label{text=\"In Kingdom\"};label{text=\"\"};label{type=\"italic\";text=\"(" + aState + ")\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3280 */         buf.append("label{text=\"\"};label{text=\"On Bridge\"};label{text=\"\"};label{type=\"italic\";text=\"Unknown\"};");
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 3285 */       buf.append("label{text=\"\"};label{text=\"Banned\"};label{text=\"\"};label{text=\"" + pInfo
/*      */ 
/*      */           
/* 3288 */           .isBanned() + "\"};");
/*      */       
/* 3290 */       if (pInfo.isBanned()) {
/*      */         
/* 3292 */         buf.append("label{text=\"\"};label{text=\"IPBan reason\"};label{text=\"\"};label{text=\"" + pInfo.banreason + "\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3297 */         buf.append("label{text=\"\"};label{text=\"IPBan expires in\"};label{text=\"\"};label{text=\"" + 
/*      */ 
/*      */             
/* 3300 */             Server.getTimeFor(pInfo.banexpiry - System.currentTimeMillis()) + "\"};");
/*      */       } 
/*      */       
/* 3303 */       buf.append("label{text=\"\"};label{text=\"Warnings\"};label{text=\"\"};label{text=\"" + 
/*      */ 
/*      */           
/* 3306 */           String.valueOf(pInfo.getWarnings()) + "\"};");
/*      */       
/* 3308 */       buf.append("label{text=\"\"};label{text=\"Muted\"};label{text=\"\"};label{text=\"" + pInfo
/*      */ 
/*      */           
/* 3311 */           .isMute() + "\"};");
/*      */       
/* 3313 */       if (pInfo.isMute()) {
/*      */         
/* 3315 */         buf.append("label{text=\"\"};label{text=\"Mute reason\"};label{text=\"\"};label{text=\"" + pInfo.mutereason + "\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3320 */         buf.append("label{text=\"\"};label{text=\"Mute expires in\"};label{text=\"\"};label{text=\"" + 
/*      */ 
/*      */             
/* 3323 */             Server.getTimeFor(pInfo.muteexpiry - System.currentTimeMillis()) + "\"};");
/*      */       } 
/*      */       
/* 3326 */       buf.append("label{text=\"\"};label{text=\"IP Address\"};label{text=\"\"};label{text=\"" + pInfo
/*      */ 
/*      */           
/* 3329 */           .getIpaddress() + "\"};");
/*      */       
/* 3331 */       buf.append("label{text=\"\"};label{text=\"Email Address\"};label{text=\"\"};label{text=\"" + pInfo.emailAddress + "\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3336 */       if (p != null) {
/*      */         
/* 3338 */         buf.append("label{text=\"\"};label{text=\"Player Kingdom\"};label{text=\"\"};label{text=\"" + 
/*      */ 
/*      */             
/* 3341 */             Kingdoms.getNameFor(p.getKingdomId()) + "(" + p
/* 3342 */             .getKingdomId() + ")\"};");
/*      */         
/* 3344 */         byte kingdom = Kingdoms.getKingdomTemplateFor(p.getKingdomId());
/* 3345 */         buf.append("label{text=\"\"};label{text=\"Player Kingdom Template\"};label{text=\"\"};label{text=\"" + 
/*      */ 
/*      */             
/* 3348 */             Kingdoms.getNameFor(kingdom) + "(" + kingdom + ")\"};");
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 3353 */         buf.append("label{text=\"\"};label{text=\"Player Kingdom\"};label{text=\"\"};label{type=\"italic\";text=\"(" + aState + ")\"};");
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3359 */       buf.append("label{text=\"\"};label{text=\"Last login\"};label{text=\"\"};label{text=\"" + pInfo
/*      */ 
/*      */           
/* 3362 */           .getLastLogin() + " (" + new Date(pInfo.getLastLogin()) + ")\"};");
/*      */       
/* 3364 */       buf.append("label{text=\"\"};label{text=\"Last logout\"};label{text=\"\"};label{text=\"" + pInfo
/*      */ 
/*      */           
/* 3367 */           .getLastLogout() + " (" + new Date(pInfo.getLastLogout()) + ")\"};");
/*      */       
/* 3369 */       PlayerState ps = PlayerInfoFactory.getPlayerState(this.wurmId);
/* 3370 */       if (ps == null) {
/*      */         
/* 3372 */         buf.append("label{text=\"\"};label{text=\"state login\"};label{text=\"\"};label{text=\"not found\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3377 */         buf.append("label{text=\"\"};label{text=\"state logout\"};label{text=\"\"};label{text=\"not found\"};");
/*      */ 
/*      */       
/*      */       }
/*      */       else {
/*      */ 
/*      */         
/* 3384 */         buf.append("label{text=\"\"};label{text=\"state login\"};label{text=\"\"};label{text=\"" + ps
/*      */ 
/*      */             
/* 3387 */             .getLastLogin() + " (" + new Date(ps.getLastLogin()) + ")\"};");
/*      */         
/* 3389 */         buf.append("label{text=\"\"};label{text=\"state logout\"};label{text=\"\"};label{text=\"" + ps
/*      */ 
/*      */             
/* 3392 */             .getLastLogout() + " (" + new Date(ps.getLastLogout()) + ")\"};");
/*      */       } 
/*      */       
/* 3395 */       buf.append("label{text=\"\"};label{text=\"PlayingTime\"};label{text=\"\"};label{text=\"" + 
/*      */ 
/*      */           
/* 3398 */           Server.getTimeFor(pInfo.playingTime) + "\"};");
/*      */       
/* 3400 */       if (pInfo.title != null) {
/* 3401 */         buf.append("label{text=\"\"};label{text=\"First Title\"};label{text=\"\"};label{text=\"" + pInfo.title
/*      */ 
/*      */             
/* 3404 */             .getName(pInfo.isMale()) + "\"};");
/*      */       } else {
/* 3406 */         buf.append("label{text=\"\"};label{text=\"First Title\"};label{text=\"\"};label{type=\"italic\";text=\"{none}\"};");
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 3411 */       if (pInfo.secondTitle != null) {
/* 3412 */         buf.append("label{text=\"\"};label{text=\"Second Title\"};label{text=\"\"};label{text=\"" + pInfo.secondTitle
/*      */ 
/*      */             
/* 3415 */             .getName(pInfo.isMale()) + "\"};");
/*      */       } else {
/* 3417 */         buf.append("label{text=\"\"};label{text=\"Second Title\"};label{text=\"\"};label{type=\"italic\";text=\"{none}\"};");
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 3422 */       buf.append("label{text=\"\"};label{text=\"Priest\"};label{text=\"\"};label{text=\"" + pInfo.isPriest + "\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3427 */       if (p != null) {
/*      */         
/* 3429 */         if (p.hasPet())
/*      */         {
/* 3431 */           Creature pet = p.getPet();
/* 3432 */           if (pet != null) {
/* 3433 */             buf.append("radio{group=\"tid\";id=\"1," + pet.getWurmId() + "," + '\001' + "\"};label{text=\"Pet\"};label{text=\"\"};label{text=\"" + pet
/*      */ 
/*      */                 
/* 3436 */                 .getName() + "\"};");
/*      */           } else {
/* 3438 */             buf.append("label{text=\"\"};label{text=\"Pet\"};label{text=\"\"};label{type=\"italic\";text=\"Not found\"};");
/*      */           }
/*      */         
/*      */         }
/*      */         else
/*      */         {
/* 3444 */           buf.append("label{text=\"\"};label{text=\"Pet\"};label{text=\"\"};label{type=\"italic\";text=\"No\"};");
/*      */         }
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 3450 */         buf.append("label{text=\"\"};label{text=\"Pet\"};label{text=\"\"};label{type=\"italic\";text=\"{unknown}\"};");
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 3455 */       if (p != null) {
/*      */         
/* 3457 */         buf.append("label{text=\"\"};label{text=\"Thirst\"};label{text=\"\"};label{text=\"" + p
/*      */ 
/*      */             
/* 3460 */             .getStatus().getThirst() + "\"};");
/*      */         
/* 3462 */         buf.append("label{text=\"\"};label{text=\"Hunger\"};label{text=\"\"};label{text=\"" + p
/*      */ 
/*      */             
/* 3465 */             .getStatus().getHunger() + "\"};");
/*      */         
/* 3467 */         buf.append("label{text=\"\"};label{text=\"Damage\"};label{text=\"\"};label{text=\"" + 
/*      */ 
/*      */             
/* 3470 */             (p.getStatus()).damage + "\"};");
/*      */         
/* 3472 */         buf.append("label{text=\"\"};label{text=\"Nutrition\"};label{text=\"\"};label{text=\"" + p
/*      */ 
/*      */             
/* 3475 */             .getStatus().getNutritionlevel() + "\"};");
/*      */         
/* 3477 */         buf.append("label{text=\"\"};label{text=\"CCFP Values\"};label{text=\"\"};label{text=\"" + 
/*      */ 
/*      */             
/* 3480 */             (int)p.getStatus().getCalories() + ", " + 
/* 3481 */             (int)p.getStatus().getCarbs() + ", " + 
/* 3482 */             (int)p.getStatus().getFats() + ", " + 
/* 3483 */             (int)p.getStatus().getProteins() + "\"};");
/*      */         
/* 3485 */         buf.append("label{text=\"\"};label{text=\"CCFP Percentages\"};label{text=\"\"};label{text=\"" + 
/*      */ 
/*      */             
/* 3488 */             (int)p.getStatus().getCaloriesAsPercent() + "%, " + 
/* 3489 */             (int)p.getStatus().getCarbsAsPercent() + "%, " + 
/* 3490 */             (int)p.getStatus().getFatsAsPercent() + "%, " + 
/* 3491 */             (int)p.getStatus().getProteinsAsPercent() + "%\"};");
/*      */       } 
/*      */       
/* 3494 */       buf.append("label{text=\"\"};label{text=\"Chat no PMs\"};label{text=\"\"};label{text=\"" + pInfo
/*      */ 
/*      */           
/* 3497 */           .isFlagSet(1) + "\"};");
/*      */       
/* 3499 */       buf.append("label{text=\"\"};label{text=\"Chat x-Kingdom\"};label{text=\"\"};label{text=\"" + pInfo
/*      */ 
/*      */           
/* 3502 */           .isFlagSet(2) + "\"};");
/*      */       
/* 3504 */       buf.append("label{text=\"\"};label{text=\"Chat x-Server\"};label{text=\"\"};label{text=\"" + pInfo
/*      */ 
/*      */           
/* 3507 */           .isFlagSet(3) + "\"};");
/*      */       
/* 3509 */       buf.append("label{text=\"\"};label{text=\"Chat Friends\"};label{text=\"\"};label{text=\"" + pInfo
/*      */ 
/*      */           
/* 3512 */           .isFlagSet(4) + "\"};");
/*      */       
/* 3514 */       buf.append("label{text=\"\"};label{text=\"CCFP Hidden\"};label{text=\"\"};label{text=\"" + pInfo
/*      */ 
/*      */           
/* 3517 */           .isFlagSet(52) + "\"};");
/*      */       
/* 3519 */       buf.append("label{text=\"\"};label{text=\"New Affinity calc\"};label{text=\"\"};label{text=\"" + pInfo
/*      */ 
/*      */           
/* 3522 */           .isFlagSet(53) + "\"};");
/*      */ 
/*      */       
/* 3525 */       Recipe[] recipes = Recipes.getNamedRecipesFor(pInfo.getName());
/* 3526 */       StringBuilder buf2 = new StringBuilder();
/* 3527 */       if (recipes.length == 0) {
/* 3528 */         buf2.append("none");
/*      */       } else {
/*      */         
/* 3531 */         for (Recipe recipe : recipes) {
/*      */           
/* 3533 */           if (buf2.length() > 0)
/* 3534 */             buf2.append(','); 
/* 3535 */           buf2.append(recipe.getName() + " (" + recipe.getRecipeId() + ")");
/*      */         } 
/*      */       } 
/* 3538 */       buf.append("label{text=\"\"};label{text=\"Their Recipe\"};label{text=\"\"};label{text=\"" + buf2
/*      */ 
/*      */           
/* 3541 */           .toString() + "\"};");
/*      */       
/* 3543 */       if (pInfo.referrer <= 0L) {
/* 3544 */         buf.append("label{text=\"\"};label{text=\"Refered\"};label{text=\"\"};label{text=\"Noone\"};");
/*      */       
/*      */       }
/*      */       else {
/*      */ 
/*      */         
/* 3550 */         PlayerState rstate = PlayerInfoFactory.getPlayerState(pInfo.referrer);
/* 3551 */         buf.append("radio{group=\"tid\";id=\"1," + pInfo.referrer + "," + '\001' + "\"};label{text=\"Refered\"};label{text=\"\"};label{text=\"" + ((rstate != null) ? rstate
/*      */ 
/*      */             
/* 3554 */             .getPlayerName() : ("" + pInfo.referrer)) + "\"};");
/*      */       } 
/* 3556 */       buf.append("}");
/*      */     } 
/* 3558 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String playerBuildings(Player player, Structure[] buildings) {
/* 3563 */     StringBuilder buf = new StringBuilder();
/* 3564 */     Arrays.sort(buildings, new Comparator<Structure>()
/*      */         {
/*      */           
/*      */           public int compare(Structure param1, Structure param2)
/*      */           {
/* 3569 */             return param1.getObjectName().compareTo(param2.getObjectName());
/*      */           }
/*      */         });
/* 3572 */     if (buildings.length > 0) {
/*      */       
/* 3574 */       buf.append(buildingsTable(player, buildings));
/*      */     } else {
/*      */       
/* 3577 */       buf.append("label{text=\"not owner of any buildings.\"};");
/*      */     } 
/* 3579 */     buf.append("label{text=\"\"};");
/* 3580 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String playerMinedoors(Player player, MineDoorPermission[] mineDoors) {
/* 3585 */     StringBuilder buf = new StringBuilder();
/* 3586 */     Arrays.sort(mineDoors, new Comparator<MineDoorPermission>()
/*      */         {
/*      */           
/*      */           public int compare(MineDoorPermission param1, MineDoorPermission param2)
/*      */           {
/* 3591 */             return param1.getObjectName().compareTo(param2.getObjectName());
/*      */           }
/*      */         });
/* 3594 */     if (mineDoors.length > 0) {
/*      */       
/* 3596 */       buf.append(minedoorsTable(player, mineDoors));
/*      */     } else {
/*      */       
/* 3599 */       buf.append("label{text=\"not owner of any mine doors.\"};");
/*      */     } 
/* 3601 */     buf.append("label{text=\"\"};");
/* 3602 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String playerGates(Player player, FenceGate[] gates) {
/* 3607 */     StringBuilder buf = new StringBuilder();
/* 3608 */     Arrays.sort(gates, new Comparator<FenceGate>()
/*      */         {
/*      */           
/*      */           public int compare(FenceGate param1, FenceGate param2)
/*      */           {
/* 3613 */             if (param1.getFloorLevel() == param2.getFloorLevel()) {
/*      */               
/* 3615 */               int comp = param1.getTypeName().compareTo(param2.getTypeName());
/* 3616 */               if (comp == 0) {
/* 3617 */                 return param1.getObjectName().compareTo(param2.getObjectName());
/*      */               }
/* 3619 */               return comp;
/*      */             } 
/* 3621 */             if (param1.getFloorLevel() < param2.getFloorLevel()) {
/* 3622 */               return 1;
/*      */             }
/* 3624 */             return -1;
/*      */           }
/*      */         });
/* 3627 */     if (gates.length > 0) {
/*      */       
/* 3629 */       buf.append(gatesTable(player, gates));
/*      */     } else {
/*      */       
/* 3632 */       buf.append("label{text=\"not owner of any gates.\"};");
/*      */     } 
/* 3634 */     buf.append("label{text=\"\"};");
/* 3635 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String playerPath(Player player, List<Route> path) {
/* 3640 */     StringBuilder buf = new StringBuilder();
/* 3641 */     if (path != null && !path.isEmpty()) {
/* 3642 */       buf.append(pathTable(player, path));
/*      */     } else {
/* 3644 */       buf.append("label{text=\"no highway routing found.\"};");
/*      */     } 
/* 3646 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String playerItems(String itemtype, Player player, Item[] carts) {
/* 3651 */     StringBuilder buf = new StringBuilder();
/* 3652 */     Arrays.sort(carts, new Comparator<Item>()
/*      */         {
/*      */           
/*      */           public int compare(Item param1, Item param2)
/*      */           {
/* 3657 */             return param1.getObjectName().compareTo(param2.getObjectName());
/*      */           }
/*      */         });
/* 3660 */     if (carts.length > 0) {
/*      */       
/* 3662 */       buf.append(itemsTable(itemtype, player, carts));
/*      */     } else {
/*      */       
/* 3665 */       buf.append("label{text=\"not owner of any " + itemtype + ".\"};");
/*      */     } 
/* 3667 */     buf.append("label{text=\"\"};");
/* 3668 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String playerFriends(PlayerInfo pInfo) {
/* 3673 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 3675 */     Friend[] lfriends = pInfo.getFriends();
/* 3676 */     Arrays.sort((Object[])lfriends);
/* 3677 */     if (lfriends.length > 0) {
/*      */       
/* 3679 */       buf.append(friendsTable(lfriends));
/*      */     } else {
/*      */       
/* 3682 */       buf.append("label{text=\"" + pInfo.getName() + " has no friends :(\"};");
/*      */     } 
/* 3684 */     buf.append("label{text=\"\"};");
/* 3685 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String playerSkills(Skills skills) {
/* 3690 */     StringBuilder buf = new StringBuilder();
/*      */ 
/*      */     
/*      */     try {
/* 3694 */       Collection<SkillTemplate> temps = SkillSystem.templates.values();
/* 3695 */       SkillTemplate[] templates = temps.<SkillTemplate>toArray(new SkillTemplate[temps.size()]);
/* 3696 */       buf.append(skillsTable(templates, skills));
/*      */     }
/* 3698 */     catch (Exception iox) {
/*      */       
/* 3700 */       logger.log(Level.WARNING, this.wurmId + ": " + iox.getMessage(), iox);
/* 3701 */       buf.append("label{text=\"Error getting Skills\"}");
/* 3702 */       buf.append("label{text=\"Exception:\"};");
/* 3703 */       buf.append("label{text=\"" + iox.getMessage() + "\"};");
/*      */     } 
/* 3705 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String playerBank(Bank bank) {
/* 3710 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 3712 */     if (bank != null) {
/*      */ 
/*      */       
/*      */       try {
/* 3716 */         Village vill = bank.getCurrentVillage();
/* 3717 */         if (vill != null) {
/* 3718 */           buf.append("label{text=\"Bank is in " + vill.getName() + ".\"}");
/*      */         } else {
/* 3720 */           buf.append("label{text=\"Bank is in an unavailable village\"}");
/*      */         } 
/* 3722 */       } catch (BankUnavailableException bua) {
/*      */         
/* 3724 */         buf.append("label{text=\"Bank is not in a village?\"}");
/*      */       } 
/* 3726 */       buf.append("label{text=\"\"};");
/*      */       
/* 3728 */       BankSlot[] slots = bank.slots;
/* 3729 */       if (slots != null) {
/*      */ 
/*      */         
/* 3732 */         Set<Item> allItems = new HashSet<>();
/* 3733 */         for (int x = 0; x < slots.length; x++) {
/*      */           
/* 3735 */           if (slots[x] != null)
/* 3736 */             allItems.add(slots[x].getItem()); 
/*      */         } 
/* 3738 */         Item[] litems = allItems.<Item>toArray(new Item[allItems.size()]);
/*      */         
/* 3740 */         if (litems.length > 0) {
/* 3741 */           buf.append(itemsTable(litems));
/*      */         } else {
/* 3743 */           buf.append("label{text=\"Nothing found in  Bank\"}");
/*      */         } 
/*      */       } else {
/* 3746 */         buf.append("label{text=\"No Bank Slots?\"}");
/*      */       } 
/*      */     } else {
/* 3749 */       buf.append("label{text=\"No Bank found\"}");
/* 3750 */     }  return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String playerTitles(PlayerInfo pInfo) {
/* 3755 */     StringBuilder buf = new StringBuilder();
/*      */ 
/*      */     
/*      */     try {
/* 3759 */       Titles.Title[] titlearr = pInfo.getTitles();
/* 3760 */       if (titlearr.length > 0) {
/*      */         
/* 3762 */         buf.append("label{text=\"# of items:" + titlearr.length + "\"};");
/* 3763 */         Arrays.sort(titlearr, new Comparator<Titles.Title>()
/*      */             {
/*      */               
/*      */               public int compare(Titles.Title t1, Titles.Title t2)
/*      */               {
/* 3768 */                 return t1.getName().compareTo(t2.getName());
/*      */               }
/*      */             });
/*      */         
/* 3772 */         buf.append("table{rows=\"" + (titlearr.length + 1) + "\";cols=\"2\";label{text=\"\"};label{text=\"Name\"};");
/*      */         
/* 3774 */         for (int x = 0; x < titlearr.length; x++)
/*      */         {
/* 3776 */           buf.append("label{text=\"\"};label{text=\"" + titlearr[x]
/* 3777 */               .getName(false) + "\"};");
/*      */         }
/* 3779 */         buf.append("}");
/*      */       } else {
/*      */         
/* 3782 */         buf.append("label{text=\"No Titles found\"}");
/*      */       } 
/* 3784 */     } catch (Exception iox) {
/*      */       
/* 3786 */       logger.log(Level.WARNING, this.wurmId + ": " + iox.getMessage(), iox);
/* 3787 */       buf.append("label{text=\"Error getting Titles\"}");
/* 3788 */       buf.append("label{text=\"Exception:\"};");
/* 3789 */       buf.append("label{text=\"" + iox.getMessage() + "\"};");
/*      */     } 
/* 3791 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String playerInventory(PlayerInfo pInfo, Player p) {
/* 3796 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 3798 */     if (p != null) {
/*      */       
/* 3800 */       Item inventory = p.getInventory();
/*      */       
/* 3802 */       if (inventory != null) {
/*      */ 
/*      */         
/* 3805 */         Item[] litems = inventory.getItemsAsArray();
/* 3806 */         if (litems.length > 0) {
/* 3807 */           buf.append(itemsTable(litems));
/*      */         } else {
/* 3809 */           buf.append("label{text=\"No Items found.\"}");
/*      */         } 
/*      */       } else {
/* 3812 */         buf.append("label{text=\"No Inventory found.\"}");
/*      */       } 
/*      */     } else {
/* 3815 */       buf.append("label{text=\"Player not found.\"}");
/*      */     } 
/* 3817 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String playerBody(PlayerInfo pInfo, Player p) {
/* 3822 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 3824 */     if (p != null) {
/*      */ 
/*      */       
/* 3827 */       Body lBody = p.getBody();
/*      */       
/* 3829 */       if (lBody != null) {
/*      */         
/* 3831 */         Item[] litems = lBody.getAllItems();
/* 3832 */         if (litems.length > 0) {
/* 3833 */           buf.append(itemsTable(litems));
/*      */         } else {
/* 3835 */           buf.append("label{text=\"No Items found\"}");
/*      */         } 
/*      */       } else {
/* 3838 */         buf.append("label{text=\"Body not found.\"}");
/*      */       } 
/*      */     } else {
/* 3841 */       buf.append("label{text=\"Player not found.\"}");
/* 3842 */     }  return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String playerCaringFor(PlayerInfo pInfo) {
/* 3847 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 3849 */     Creature[] creatures = Creatures.getInstance().getProtectedCreaturesFor(pInfo.wurmId);
/* 3850 */     if (creatures.length > 0) {
/* 3851 */       buf.append(creaturesTable(creatures));
/*      */     } else {
/* 3853 */       buf.append("label{text=\"Nothing Cared for by " + pInfo.getName() + "\"};");
/* 3854 */     }  return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String playerMail(PlayerInfo pInfo, String fromto, Set<WurmMail> mails) {
/* 3859 */     StringBuilder buf = new StringBuilder();
/* 3860 */     WurmMail[] mailList = mails.<WurmMail>toArray(new WurmMail[mails.size()]);
/* 3861 */     Arrays.sort((Object[])mailList);
/* 3862 */     if (mailList.length > 0) {
/* 3863 */       buf.append(mailTable(mailList));
/*      */     } else {
/* 3865 */       buf.append("label{text=\"No Mail " + fromto + pInfo.getName() + "\"};");
/* 3866 */     }  return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String playerHistoryIP(PlayerInfo pInfo) {
/* 3871 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 3873 */     buf.append("label{type=\"italic\";text=\"First entry may have wrong date.\"}");
/*      */     
/* 3875 */     buf.append("table{rows=\"" + pInfo.historyIPStart.size() + "\";cols=\"3\";label{type=\"bold\";text=\"IP\"};label{type=\"bold\";text=\"First Used\"};label{type=\"bold\";text=\"Last Used\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3880 */     for (Map.Entry<String, Long> entry : (Iterable<Map.Entry<String, Long>>)pInfo.historyIPStart.entrySet())
/*      */     {
/* 3882 */       buf.append("label{text=\"" + (String)entry.getKey() + "\"};label{text=\"" + new Date(((Long)entry
/* 3883 */             .getValue()).longValue()) + "\"};label{text=\"" + new Date(((Long)pInfo.historyIPLast
/* 3884 */             .get(entry.getKey())).longValue()) + "\"};");
/*      */     }
/* 3886 */     buf.append("}");
/* 3887 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String playerHistoryEmail(PlayerInfo pInfo) {
/* 3892 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 3894 */     buf.append("label{type=\"italic\";text=\"First entry may have wrong date.\"}");
/*      */     
/* 3896 */     buf.append("table{rows=\"" + pInfo.historyEmail.size() + "\";cols=\"2\";label{type=\"bold\";text=\"IP\"};label{type=\"bold\";text=\"First Used\"};");
/*      */ 
/*      */ 
/*      */     
/* 3900 */     for (Map.Entry<String, Long> entry : (Iterable<Map.Entry<String, Long>>)pInfo.historyEmail.entrySet())
/*      */     {
/* 3902 */       buf.append("label{text=\"" + (String)entry.getKey() + "\"};label{text=\"" + new Date(((Long)entry
/* 3903 */             .getValue()).longValue()) + "\"};");
/*      */     }
/* 3905 */     buf.append("}");
/* 3906 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String playerIgnoreList(PlayerInfo pInfo) {
/* 3911 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 3913 */     long[] ignoring = pInfo.getIgnored();
/* 3914 */     Arrays.sort(ignoring);
/* 3915 */     if (ignoring.length > 0) {
/*      */       
/* 3917 */       buf.append(playersTable(ignoring));
/*      */     } else {
/*      */       
/* 3920 */       buf.append("label{text=\"" + pInfo.getName() + " is not ignoring anyone.\"};");
/*      */     } 
/* 3922 */     buf.append("label{text=\"\"};");
/* 3923 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String playerCorpses(PlayerInfo pInfo) {
/* 3928 */     StringBuilder buf = new StringBuilder();
/* 3929 */     Set<Item> corpses = findCorpses(pInfo);
/*      */     
/* 3931 */     if (corpses.size() > 0) {
/* 3932 */       buf.append(corpseTable(corpses, pInfo));
/*      */     } else {
/* 3934 */       buf.append("label{text=\"No corpses found for " + pInfo.getName() + ".\"};");
/*      */     } 
/* 3936 */     buf.append("label{text=\"\"};");
/* 3937 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String playerReferrs(Map<String, Byte> referrals, PlayerInfo pInfo) {
/* 3942 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 3944 */     if (referrals == null) {
/* 3945 */       buf.append("label{text=\"Error contacting login server.\"};");
/* 3946 */     } else if (referrals.size() > 0) {
/*      */       
/* 3948 */       buf.append("table{rows=\"" + (referrals.size() + 1) + "\";cols=\"2\";");
/* 3949 */       buf.append("label{type=\"bold\";text=\"Name\"};label{type=\"bold\"text=\"Type\"}");
/*      */       
/* 3951 */       for (Iterator<Map.Entry<String, Byte>> it = referrals.entrySet().iterator(); it.hasNext(); ) {
/*      */         
/* 3953 */         Map.Entry<String, Byte> entry = it.next();
/* 3954 */         String name = entry.getKey();
/* 3955 */         byte referralType = ((Byte)entry.getValue()).byteValue();
/* 3956 */         if (referralType == 0) {
/* 3957 */           buf.append("label{text=\"" + name + "\"};label{text=\"Undecided\"}"); continue;
/*      */         } 
/* 3959 */         if (referralType == 1) {
/* 3960 */           buf.append("label{text=\"" + name + "\"};label{text=\"3 silver\"}");
/*      */           continue;
/*      */         } 
/* 3963 */         buf.append("label{text=\"" + name + "\"};label{text=\"20 days\"}");
/*      */       } 
/*      */       
/* 3966 */       buf.append("}");
/*      */     } else {
/*      */       
/* 3969 */       buf.append("label{text=\"No referrals found for " + pInfo.getName() + ".\"};");
/*      */     } 
/* 3971 */     buf.append("label{text=\"\"};");
/* 3972 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Player getPlayerFromInfo(PlayerInfo pInfo) throws Exception {
/*      */     try {
/* 3980 */       return Players.getInstance().getPlayer(pInfo.getPlayerId());
/*      */     }
/* 3982 */     catch (NoSuchPlayerException e2) {
/*      */ 
/*      */       
/* 3985 */       Player p = new Player(pInfo);
/* 3986 */       p.getBody().createBodyParts();
/* 3987 */       p.checkBodyInventoryConsistency();
/* 3988 */       p.loadSkills();
/* 3989 */       Items.loadAllItemsForCreature((Creature)p, p.getStatus().getInventoryId());
/*      */ 
/*      */       
/* 3992 */       return p;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private String buildingDetails(Structure lBuilding) {
/* 3998 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 4000 */     if (this.displaySubType == 1) {
/* 4001 */       buf.append(buildingSummary(lBuilding));
/* 4002 */     } else if (this.displaySubType == 2) {
/* 4003 */       buf.append(showGuests(StructureSettings.StructurePermissions.getPermissions(), lBuilding.getPermissionsPlayerList()));
/* 4004 */     } else if (this.displaySubType == 3) {
/* 4005 */       buf.append(showHistory(lBuilding.getWurmId()));
/* 4006 */     } else if (this.displaySubType == 5) {
/* 4007 */       buf.append(buildingTiles(lBuilding));
/* 4008 */     } else if (this.displaySubType == 6) {
/* 4009 */       buf.append(buildingWalls(lBuilding));
/* 4010 */     } else if (this.displaySubType == 7) {
/* 4011 */       buf.append(buildingFloors(lBuilding));
/*      */     } 
/*      */     
/* 4014 */     buf.append("label{type=\"bold\";text=\"--------------- Links -------------------\"}");
/*      */     
/* 4016 */     buf.append("table{rows=\"2\";cols=\"4\";");
/* 4017 */     buf.append("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\001' + "\"};label{text=\"Summary" + ((this.displaySubType == 1) ? " (Showing)" : "") + "\"};");
/*      */     
/* 4019 */     buf.append((((lBuilding.getStructureTiles()).length == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\005' + "\"};")) + "label{text=\"" + (lBuilding
/*      */         
/* 4021 */         .getStructureTiles()).length + " Tiles" + ((this.displaySubType == 5) ? " (Showing)" : "") + "\"};");
/*      */ 
/*      */     
/* 4024 */     buf.append(((lBuilding.getPermissionsPlayerList().size() == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\002' + "\"};")) + "label{text=\"" + lBuilding
/*      */         
/* 4026 */         .getPermissionsPlayerList().size() + " Guests" + ((this.displaySubType == 2) ? " (Showing)" : "") + "\"};");
/*      */     
/* 4028 */     buf.append((((PermissionsHistories.getPermissionsHistoryFor(lBuilding.getWurmId()).getHistoryEvents()).length == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\003' + "\"};")) + "label{text=\"" + (
/*      */         
/* 4030 */         PermissionsHistories.getPermissionsHistoryFor(lBuilding.getWurmId()).getHistoryEvents()).length + " History" + ((this.displaySubType == 3) ? " (Showing)" : "") + "\"};");
/*      */ 
/*      */     
/* 4033 */     buf.append((((lBuilding.getWalls()).length == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\006' + "\"};")) + "label{text=\"" + (lBuilding
/*      */         
/* 4035 */         .getWalls()).length + " Walls" + ((this.displaySubType == 6) ? " (Showing)" : "") + "\"};");
/*      */     
/* 4037 */     buf.append((((lBuilding.getFloors()).length == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\007' + "\"};")) + "label{text=\"" + (lBuilding
/*      */         
/* 4039 */         .getFloors()).length + " Floors" + ((this.displaySubType == 7) ? " (Showing)" : "") + "\"};");
/*      */ 
/*      */     
/* 4042 */     buf.append("label{text=\"\"};label{text=\"\"};");
/* 4043 */     buf.append("}");
/*      */     
/* 4045 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String buildingSummary(Structure lBuilding) {
/* 4050 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 4052 */     buf.append("table{rows=\"18\";cols=\"4\";");
/*      */     
/* 4054 */     buf.append("label{text=\"\"};label{text=\"Wurm Id\"};label{text=\"\"};label{text=\"" + this.wurmId + "\"}");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4059 */     buf.append("label{text=\"\"};label{text=\"Name\"};label{text=\"\"};label{text=\"" + lBuilding
/*      */ 
/*      */         
/* 4062 */         .getName() + "\"};");
/*      */     
/* 4064 */     if (lBuilding.getWritId() == -10L) {
/*      */       
/* 4066 */       buf.append("label{text=\"\"};label{text=\"WritID\"};label{text=\"\"};label{text=\"" + lBuilding
/*      */ 
/*      */           
/* 4069 */           .getWritId() + "\"};");
/*      */     }
/*      */     else {
/*      */       
/* 4073 */       buf.append("radio{group=\"tid\";id=\"1," + lBuilding.getWritId() + "," + '\001' + "\"};label{text=\"WritID\"};label{text=\"\"};label{text=\"" + lBuilding
/*      */ 
/*      */           
/* 4076 */           .getWritId() + "\"};");
/*      */     } 
/*      */     
/* 4079 */     PlayerInfo pInfo = PlayerInfoFactory.getPlayerInfoWithWurmId(lBuilding.getOwnerId());
/* 4080 */     if (pInfo != null) {
/* 4081 */       buf.append("radio{group=\"tid\";id=\"1," + pInfo.wurmId + "," + '\001' + "\"};label{text=\"Owner Name\"};label{text=\"\"};label{text=\"" + pInfo
/*      */ 
/*      */           
/* 4084 */           .getName() + "\"};");
/*      */     } else {
/* 4086 */       buf.append("label{text=\"\"};label{text=\"OwnerID\"};label{text=\"\"};label{text=\"" + lBuilding
/*      */ 
/*      */           
/* 4089 */           .getOwnerId() + "\"};");
/*      */     } 
/* 4091 */     int sx = lBuilding.getCenterX();
/* 4092 */     int sy = lBuilding.getCenterY();
/* 4093 */     boolean onSurface = lBuilding.isOnSurface();
/* 4094 */     buf.append("radio{group=\"tid\";id=\"3," + sx + "," + sy + "," + onSurface + "\"};label{text=\"Center (X,Y,surface)\"};radio{group=\"tid\";id=\"" + '\f' + "," + sx + "," + sy + "," + onSurface + "\"};label{text=\"(" + sx + "," + sy + "," + onSurface + ")\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4099 */     buf.append("label{text=\"\"};label{text=\"Creation Date\"};label{text=\"\"};label{text=\"" + new Date(lBuilding
/*      */ 
/*      */           
/* 4102 */           .getCreationDate()) + "\"};");
/*      */     
/* 4104 */     buf.append("label{text=\"\"};label{text=\"Door Count\"};label{text=\"\"};label{text=\"" + lBuilding
/*      */ 
/*      */         
/* 4107 */         .getDoors() + "\"};");
/*      */     
/* 4109 */     buf.append("label{text=\"\"};label{text=\"Final Finished\"};label{text=\"\"};label{text=\"" + lBuilding
/*      */ 
/*      */         
/* 4112 */         .isFinalFinished() + "\"};");
/*      */     
/* 4114 */     buf.append("label{text=\"\"};label{text=\"Finalized\"};label{text=\"\"};label{text=\"" + lBuilding
/*      */ 
/*      */         
/* 4117 */         .isFinalized() + "\"};");
/*      */     
/* 4119 */     buf.append("label{text=\"\"};label{text=\"Finished\"};label{text=\"\"};label{text=\"" + lBuilding
/*      */ 
/*      */         
/* 4122 */         .isFinished() + "\"};");
/*      */     
/* 4124 */     buf.append("label{text=\"\"};label{text=\"Limit\"};label{text=\"\"};label{text=\"" + lBuilding
/*      */ 
/*      */         
/* 4127 */         .getLimit() + "\"};");
/*      */     
/* 4129 */     buf.append("label{text=\"\"};label{text=\"Lockable\"};label{text=\"\"};label{text=\"" + lBuilding
/*      */ 
/*      */         
/* 4132 */         .isLockable() + "\"};");
/*      */     
/* 4134 */     buf.append("label{text=\"\"};label{text=\"Locked\"};label{text=\"\"};label{text=\"" + lBuilding
/*      */ 
/*      */         
/* 4137 */         .isLocked() + "\"};");
/*      */     
/* 4139 */     int maxx = lBuilding.getMaxX();
/* 4140 */     int maxy = lBuilding.getMaxY();
/* 4141 */     buf.append("radio{group=\"tid\";id=\"3," + maxx + "," + maxy + "," + onSurface + "\"};label{text=\"Max (X,Y,surface)\"};radio{group=\"tid\";id=\"" + '\f' + "," + maxx + "," + maxy + "," + onSurface + "\"};label{text=\"(" + maxx + "," + maxy + "," + onSurface + ")\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4146 */     int minx = lBuilding.getMinX();
/* 4147 */     int miny = lBuilding.getMinY();
/* 4148 */     buf.append("radio{group=\"tid\";id=\"3," + minx + "," + miny + "," + onSurface + "\"};label{text=\"Min (X,Y,surface)\"};radio{group=\"tid\";id=\"" + '\f' + "," + minx + "," + miny + "," + onSurface + "\"};label{text=\"(" + minx + "," + miny + "," + onSurface + ")\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4153 */     buf.append("label{text=\"\"};label{text=\"Roof\"};label{text=\"\"};label{text=\"" + lBuilding
/*      */ 
/*      */         
/* 4156 */         .getRoof() + "\"};");
/*      */     
/* 4158 */     buf.append("label{text=\"\"};label{text=\"Size\"};label{text=\"\"};label{text=\"" + lBuilding
/*      */ 
/*      */         
/* 4161 */         .getSize() + "\"};");
/*      */     
/* 4163 */     buf.append("label{text=\"\"};label{text=\"HasWalls\"};label{text=\"\"};label{text=\"" + lBuilding
/*      */ 
/*      */         
/* 4166 */         .hasWalls() + "\"};");
/*      */     
/* 4168 */     buf.append("label{text=\"\"};label{text=\"Planner\"};label{text=\"\"};label{text=\"" + lBuilding
/*      */ 
/*      */         
/* 4171 */         .getPlanner() + "\"};");
/*      */     
/* 4173 */     Village v = lBuilding.getVillage();
/* 4174 */     buf.append("label{text=\"\"};label{text=\"In Settlement\"};label{text=\"\"};label{text=\"" + ((v != null) ? v
/*      */ 
/*      */         
/* 4177 */         .getName() : "-") + "\"};");
/*      */     
/* 4179 */     buf.append("}");
/* 4180 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String showGuests(Permissions.IPermission[] ips, PermissionsPlayerList ppl) {
/* 4185 */     StringBuilder buf = new StringBuilder();
/* 4186 */     PermissionsByPlayer[] lGuests = ppl.getPermissionsByPlayer();
/* 4187 */     if (lGuests.length > 0) {
/*      */       
/* 4189 */       buf.append(PermissionsByPlayerTable(ips, lGuests));
/*      */     } else {
/*      */       
/* 4192 */       buf.append("label{text=\"no guests\"};");
/* 4193 */     }  return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String showHistory(long objectId) {
/* 4198 */     StringBuilder buf = new StringBuilder();
/* 4199 */     PermissionsHistoryEntry[] lHistory = PermissionsHistories.getPermissionsHistoryFor(objectId).getHistoryEvents();
/* 4200 */     if (lHistory.length > 0) {
/* 4201 */       buf.append(historyTable(lHistory));
/*      */     } else {
/* 4203 */       buf.append("label{text=\"no guests\"};");
/* 4204 */     }  return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String buildingTiles(Structure lBuilding) {
/* 4209 */     StringBuilder buf = new StringBuilder();
/* 4210 */     VolaTile[] tiles = lBuilding.getStructureTiles();
/* 4211 */     if (tiles.length > 0) {
/* 4212 */       buf.append(tilesTable(tiles));
/*      */     } else {
/* 4214 */       buf.append("label{text=\"No Tiles found\"}");
/* 4215 */     }  return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String buildingWalls(Structure lBuilding) {
/* 4220 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 4222 */     Wall[] lWalls = lBuilding.getWalls();
/* 4223 */     if (lWalls.length > 0) {
/* 4224 */       buf.append(wallsTable(lWalls, -1, -1));
/*      */     } else {
/* 4226 */       buf.append("label{text=\"No Walls found\"}");
/* 4227 */     }  return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String buildingFloors(Structure lBuilding) {
/* 4232 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 4234 */     Floor[] lFloors = lBuilding.getFloors();
/* 4235 */     if (lFloors.length > 0) {
/* 4236 */       buf.append(floorsTable(lFloors));
/*      */     } else {
/* 4238 */       buf.append("label{text=\"No Floors found\"}");
/* 4239 */     }  return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String bridgeDetails(Structure lBridge) {
/* 4244 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 4246 */     if (this.displaySubType == 1) {
/* 4247 */       buf.append(bridgeSummary(lBridge));
/* 4248 */     } else if (this.displaySubType == 5) {
/* 4249 */       buf.append(bridgeTiles(lBridge));
/* 4250 */     } else if (this.displaySubType == 8) {
/* 4251 */       buf.append(bridgeParts(lBridge));
/*      */     } 
/*      */     
/* 4254 */     buf.append("label{type=\"bold\";text=\"--------------- Links -------------------\"}");
/*      */     
/* 4256 */     buf.append("table{rows=\"2\";cols=\"4\";");
/* 4257 */     buf.append("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\001' + "\"};label{text=\"Summary" + ((this.displaySubType == 1) ? " (Showing)" : "") + "\"};");
/*      */     
/* 4259 */     buf.append((((lBridge.getBridgeParts()).length == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\b' + "\"};")) + "label{text=\"" + (lBridge
/*      */         
/* 4261 */         .getBridgeParts()).length + " Bridge parts" + ((this.displaySubType == 8) ? " (Showing)" : "") + "\"};");
/*      */ 
/*      */     
/* 4264 */     buf.append((((lBridge.getStructureTiles()).length == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\005' + "\"};")) + "label{text=\"" + (lBridge
/*      */         
/* 4266 */         .getStructureTiles()).length + " Tiles" + ((this.displaySubType == 5) ? " (Showing)" : "") + "\"};");
/*      */     
/* 4268 */     buf.append("label{text=\"\"};label{text=\"\"};");
/* 4269 */     buf.append("}");
/*      */     
/* 4271 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String bridgeSummary(Structure lBridge) {
/* 4276 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 4278 */     buf.append("table{rows=\"10\";cols=\"4\";");
/*      */     
/* 4280 */     buf.append("label{text=\"\"};label{text=\"Wurm Id\"};label{text=\"\"};label{text=\"" + this.wurmId + "\"}");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4285 */     buf.append("label{text=\"\"};label{text=\"Name\"};label{text=\"\"};label{text=\"" + lBridge
/*      */ 
/*      */         
/* 4288 */         .getName() + "\"};");
/*      */     
/* 4290 */     buf.append("label{text=\"\"};label{text=\"Creation Date\"};label{text=\"\"};label{text=\"" + new Date(lBridge
/*      */ 
/*      */           
/* 4293 */           .getCreationDate()) + "\"};");
/*      */     
/* 4295 */     PlayerInfo pInfo = PlayerInfoFactory.getPlayerInfoWithWurmId(lBridge.getOwnerId());
/* 4296 */     if (pInfo != null) {
/* 4297 */       buf.append("radio{group=\"tid\";id=\"1," + pInfo.wurmId + "," + '\001' + "\"};label{text=\"Owner Name\"};label{text=\"\"};label{text=\"" + pInfo
/*      */ 
/*      */           
/* 4300 */           .getName() + "\"};");
/*      */     } else {
/* 4302 */       buf.append("label{text=\"\"};label{text=\"OwnerID\"};label{text=\"\"};label{text=\"" + lBridge
/*      */ 
/*      */           
/* 4305 */           .getOwnerId() + "\"};");
/*      */     } 
/* 4307 */     buf.append("label{text=\"\"};label{text=\"Final Finished\"};label{text=\"\"};label{text=\"" + lBridge
/*      */ 
/*      */         
/* 4310 */         .isFinalFinished() + "\"};");
/*      */     
/* 4312 */     buf.append("label{text=\"\"};label{text=\"Finalized\"};label{text=\"\"};label{text=\"" + lBridge
/*      */ 
/*      */         
/* 4315 */         .isFinalized() + "\"};");
/*      */     
/* 4317 */     buf.append("label{text=\"\"};label{text=\"Finished\"};label{text=\"\"};label{text=\"" + lBridge
/*      */ 
/*      */         
/* 4320 */         .isFinished() + "\"};");
/*      */     
/* 4322 */     int maxx = lBridge.getMaxX();
/* 4323 */     int maxy = lBridge.getMaxY();
/* 4324 */     buf.append("radio{group=\"tid\";id=\"3," + maxx + "," + maxy + ",true\"};label{text=\"Max (X,Y,surface)\"};radio{group=\"tid\";id=\"" + '\f' + "," + maxx + "," + maxy + ",true\"};label{text=\"(" + maxx + "," + maxy + ",true)\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4329 */     int minx = lBridge.getMinX();
/* 4330 */     int miny = lBridge.getMinY();
/* 4331 */     buf.append("radio{group=\"tid\";id=\"3," + minx + "," + miny + ",true\"};label{text=\"Min (X,Y,surface)\"};radio{group=\"tid\";id=\"" + '\f' + "," + minx + "," + miny + ",true\"};label{text=\"(" + minx + "," + miny + ",true)\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4336 */     buf.append("label{text=\"\"};label{text=\"Size\"};label{text=\"\"};label{text=\"" + lBridge
/*      */ 
/*      */         
/* 4339 */         .getSize() + "\"};");
/*      */     
/* 4341 */     buf.append("label{text=\"\"};label{text=\"Planner\"};label{text=\"\"};label{text=\"" + lBridge
/*      */ 
/*      */         
/* 4344 */         .getPlanner() + "\"};");
/*      */     
/* 4346 */     buf.append("}");
/* 4347 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String bridgeTiles(Structure lBridge) {
/* 4352 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 4354 */     VolaTile[] tiles = lBridge.getStructureTiles();
/* 4355 */     if (tiles.length > 0) {
/* 4356 */       buf.append(tilesTable(tiles));
/*      */     } else {
/* 4358 */       buf.append("label{text=\"No Tiles found\"}");
/* 4359 */     }  return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String bridgeParts(Structure lBridge) {
/* 4364 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 4366 */     BridgePart[] lBridgeParts = lBridge.getBridgeParts();
/* 4367 */     if (lBridgeParts.length > 0) {
/* 4368 */       buf.append(bridgePartsTable(lBridgeParts, -1, -1));
/*      */     } else {
/* 4370 */       buf.append("label{text=\"No Bridge Parts found\"}");
/* 4371 */     }  return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String tileDetails(int aIdType) {
/* 4376 */     StringBuilder buf = new StringBuilder();
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 4381 */       int tilex = Tiles.decodeTileX(this.wurmId);
/* 4382 */       int tiley = Tiles.decodeTileY(this.wurmId);
/* 4383 */       boolean isOnSurface = (aIdType != 17);
/* 4384 */       if (aIdType == 23) {
/*      */         
/* 4386 */         Floor floor = RoofFloorEnum.getFloorOrRoofFromId(this.wurmId);
/* 4387 */         isOnSurface = floor.isOnSurface();
/*      */       } 
/* 4389 */       if (aIdType == 28)
/*      */       {
/* 4391 */         isOnSurface = (Tiles.decodeLayer(this.wurmId) == 0);
/*      */       }
/* 4393 */       WurmMail[] inMail = WurmMail.getAllMail();
/*      */       
/* 4395 */       Zone zone = Zones.getZone(tilex, tiley, isOnSurface);
/*      */       
/* 4397 */       VolaTile tile = zone.getOrCreateTile(tilex, tiley);
/* 4398 */       int numbCreatures = (tile.getCreatures()).length;
/* 4399 */       int numbWalls = (tile.getWalls()).length;
/* 4400 */       int numbFences = (tile.getAllFences()).length;
/* 4401 */       int numbItems = (tile.getItems()).length;
/* 4402 */       int numbDeities = (Deities.getDeities()).length;
/*      */       
/* 4404 */       Structure lStructure = tile.getStructure();
/* 4405 */       int numbFloors = (lStructure == null) ? 0 : (lStructure.getFloorsAtTile(tilex, tiley, 0, 9999)).length;
/* 4406 */       int numbBridgeParts = (tile.getBridgeParts()).length;
/* 4407 */       MineDoorPermission minedoor = MineDoorPermission.getPermission(tilex, tiley);
/* 4408 */       Item[] gmSigns = Items.getGMSigns();
/*      */       
/* 4410 */       if (this.displaySubType == 1) {
/* 4411 */         buf.append(tileSummary(tile, aIdType, tilex, tiley, isOnSurface));
/* 4412 */       } else if (this.displaySubType == 9) {
/* 4413 */         buf.append(tileCreatures(tile));
/* 4414 */       } else if (this.displaySubType == 6) {
/* 4415 */         buf.append(tileWalls(tile));
/* 4416 */       } else if (this.displaySubType == 10) {
/* 4417 */         buf.append(tileFences(tile));
/* 4418 */       } else if (this.displaySubType == 11) {
/* 4419 */         buf.append(tileItems(tile));
/* 4420 */       } else if (this.displaySubType == 7) {
/* 4421 */         buf.append(tileFloors(tile));
/* 4422 */       } else if (this.displaySubType == 8) {
/* 4423 */         buf.append(tileBridgeParts(tile));
/* 4424 */       } else if (this.displaySubType == 13) {
/* 4425 */         buf.append(mailItems(inMail));
/* 4426 */       } else if (this.displaySubType == 14) {
/* 4427 */         buf.append(gmSignsTable(gmSigns));
/* 4428 */       } else if (minedoor != null && this.displaySubType == 2) {
/* 4429 */         buf.append(showGuests(MineDoorSettings.MinedoorPermissions.getPermissions(), minedoor.getPermissionsPlayerList()));
/* 4430 */       } else if (minedoor != null && this.displaySubType == 3) {
/* 4431 */         buf.append(showHistory(minedoor.getWurmId()));
/*      */       } 
/* 4433 */       buf.append("label{type=\"bold\";text=\"------------------------------ Links --------------------------------------\"}");
/*      */       
/* 4435 */       buf.append("table{rows=\"3\";cols=\"6\";");
/* 4436 */       buf.append("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\001' + "\"};label{text=\"Summary" + ((this.displaySubType == 1) ? " (Showing)" : "") + "\"};");
/*      */       
/* 4438 */       buf.append(((numbCreatures == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\t' + "\"};")) + "label{text=\"" + numbCreatures + " Creatures" + ((this.displaySubType == 9) ? " (Showing)" : "") + "\"};");
/*      */ 
/*      */ 
/*      */       
/* 4442 */       buf.append(((numbWalls == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\006' + "\"};")) + "label{text=\"" + numbWalls + " Walls" + ((this.displaySubType == 6) ? " (Showing)" : "") + "\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 4447 */       buf.append(((numbFences == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\n' + "\"};")) + "label{text=\"" + numbFences + " Fences" + ((this.displaySubType == 10) ? " (Showing)" : "") + "\"};");
/*      */ 
/*      */ 
/*      */       
/* 4451 */       buf.append(((numbItems == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\013' + "\"};")) + "label{text=\"" + numbItems + " Items" + ((this.displaySubType == 11) ? " (Showing)" : "") + "\"};");
/*      */ 
/*      */ 
/*      */       
/* 4455 */       buf.append(((numbFloors == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\007' + "\"};")) + "label{text=\"" + numbFloors + " Floors" + ((this.displaySubType == 7) ? " (Showing)" : "") + "\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 4460 */       buf.append(((numbBridgeParts == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\b' + "\"};")) + "label{text=\"" + numbBridgeParts + " Bridge Parts" + ((this.displaySubType == 8) ? " (Showing)" : "") + "\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/* 4466 */         buf.append(((inMail.length == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\r' + "\"};")) + "label{text=\"" + inMail.length + " Mail Items" + ((this.displaySubType == 13) ? " (Showing)" : "") + "\"};");
/*      */ 
/*      */ 
/*      */       
/*      */       }
/* 4471 */       catch (Exception e) {
/*      */         
/* 4473 */         buf.append("label{text=\"error\"};label{text=\"" + e.getMessage() + "\"};");
/*      */       } 
/* 4475 */       buf.append(((gmSigns.length == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\016' + "\"};")) + "label{text=\"" + gmSigns.length + " GM Signs" + ((this.displaySubType == 14) ? " (Showing)" : "") + "\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 4480 */       buf.append("radio{group=\"tid\";id=\"14,-10,1\"};label{text=\"Servers\"};");
/*      */ 
/*      */       
/* 4483 */       buf.append("radio{group=\"tid\";id=\"13,-10,1\"};label{text=\"" + numbDeities + " Deities\"};");
/*      */ 
/*      */ 
/*      */       
/* 4487 */       if (minedoor != null) {
/*      */         
/* 4489 */         buf.append(((minedoor.getPermissionsPlayerList().size() == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\002' + "\"};")) + "label{text=\"" + minedoor
/*      */             
/* 4491 */             .getPermissionsPlayerList().size() + " Minedoor Guests" + ((this.displaySubType == 2) ? " (Showing)" : "") + "\"};");
/*      */         
/* 4493 */         buf.append((((PermissionsHistories.getPermissionsHistoryFor(minedoor.getWurmId()).getHistoryEvents()).length == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\003' + "\"};")) + "label{text=\"" + (
/*      */             
/* 4495 */             PermissionsHistories.getPermissionsHistoryFor(minedoor.getWurmId()).getHistoryEvents()).length + " Minedoor History" + ((this.displaySubType == 3) ? " (Showing)" : "") + "\"};");
/*      */       } 
/*      */ 
/*      */       
/* 4499 */       if (Features.Feature.HIGHWAYS.isEnabled()) {
/*      */         
/* 4501 */         buf.append("radio{group=\"tid\";id=\"15,-10,19\"};label{text=\"Highway Routes" + ((this.displaySubType == 19) ? " (Showing)" : "") + "\"};");
/*      */         
/* 4503 */         buf.append("radio{group=\"tid\";id=\"15,-10,21\"};label{text=\"Highway Nodes" + ((this.displaySubType == 21) ? " (Showing)" : "") + "\"};");
/*      */       } 
/*      */ 
/*      */       
/* 4507 */       buf.append("label{text=\"\"};label{text=\"\"};");
/* 4508 */       buf.append("label{text=\"\"};label{text=\"\"};");
/*      */       
/* 4510 */       buf.append("}");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/* 4518 */     catch (NoSuchZoneException e) {
/*      */       
/* 4520 */       logger.log(Level.WARNING, "Error: " + e.getMessage(), (Throwable)e);
/* 4521 */       buf.append("label{text=\"Error - No such zone\"};");
/* 4522 */       buf.append("label{text=\"Exception:\"};");
/* 4523 */       buf.append("label{text=\"" + e.getMessage() + "\"};");
/*      */     }
/* 4525 */     catch (RuntimeException e) {
/*      */       
/* 4527 */       logger.log(Level.WARNING, "Error: " + e.getMessage(), e);
/* 4528 */       buf.append("label{text=\"Exception:\"};");
/* 4529 */       buf.append("label{text=\"" + e.getMessage() + "\"};");
/*      */     }
/* 4531 */     catch (Exception e) {
/*      */       
/* 4533 */       logger.log(Level.WARNING, "Error: " + e.getMessage(), e);
/* 4534 */       buf.append("label{text=\"(Catch all) Exception:\"};");
/* 4535 */       buf.append("label{text=\"" + e.getMessage() + "\"};");
/*      */     } 
/* 4537 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private String tileSummary(VolaTile tile, int aIdType, int tilex, int tiley, boolean isOnSurface) {
/*      */     int resource, meshtile;
/* 4544 */     StringBuilder buf = new StringBuilder();
/*      */ 
/*      */ 
/*      */     
/* 4548 */     if (isOnSurface) {
/*      */       
/* 4550 */       resource = Server.getWorldResource(tilex, tiley);
/* 4551 */       meshtile = Server.surfaceMesh.getTile(tilex, tiley);
/*      */     }
/*      */     else {
/*      */       
/* 4555 */       resource = Server.getCaveResource(tilex, tiley);
/* 4556 */       meshtile = Server.caveMesh.getTile(tilex, tiley);
/*      */     } 
/* 4558 */     byte tileType = Tiles.decodeType(meshtile);
/* 4559 */     byte data = Tiles.decodeData(meshtile);
/*      */     
/* 4561 */     int rows = 7;
/*      */     
/* 4563 */     Structure lStructure = tile.getStructure();
/* 4564 */     if (lStructure != null) {
/* 4565 */       rows += 2;
/*      */     }
/* 4567 */     Village lVillage = tile.getVillage();
/* 4568 */     Village perimVillage = null;
/* 4569 */     if (lVillage != null) {
/* 4570 */       rows++;
/*      */     } else {
/*      */       
/* 4573 */       perimVillage = Villages.getVillageWithPerimeterAt(tile.getTileX(), tile.getTileY(), true);
/*      */     } 
/* 4575 */     if (perimVillage != null) {
/* 4576 */       rows++;
/*      */     }
/* 4578 */     buf.append("table{rows=\"" + rows + "\";cols=\"4\";");
/*      */     
/* 4580 */     buf.append("label{text=\"\"};label{text=\"Wurm Id\"};label{text=\"\"};label{text=\"" + this.wurmId + "\"}");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4585 */     buf.append("label{text=\"\"};label{text=\"Layer\"};label{text=\"\"};label{text=\"" + tile
/*      */ 
/*      */         
/* 4588 */         .getLayer() + "\"};");
/*      */     
/* 4590 */     buf.append("label{text=\"\"};label{text=\"Tile type\"};label{text=\"\"};label{text=\"" + 
/*      */ 
/*      */         
/* 4593 */         Tiles.getTile(tileType).getName() + "(" + (tileType & 0xFF) + ")\"};");
/*      */ 
/*      */     
/* 4596 */     if (aIdType == 23) {
/*      */       
/* 4598 */       Floor floor = RoofFloorEnum.getFloorOrRoofFromId(this.wurmId);
/*      */       
/* 4600 */       buf.append("label{text=\"\"};label{text=\"Floor type\"};label{text=\"\"};label{text=\"" + floor
/*      */ 
/*      */           
/* 4603 */           .getType().getName() + " (" + floor.getType().getCode() + ")\"};");
/*      */       
/* 4605 */       buf.append("label{text=\"\"};label{text=\"Floor material\"};label{text=\"\"};label{text=\"" + floor
/*      */ 
/*      */           
/* 4608 */           .getMaterial().getName() + " (" + floor.getMaterial().getCode() + ")\"};");
/*      */       
/* 4610 */       buf.append("label{text=\"\"};label{text=\"Floor QL\"};label{text=\"\"};label{text=\"" + floor
/*      */ 
/*      */           
/* 4613 */           .getCurrentQL() + "\"};");
/*      */       
/* 4615 */       buf.append("label{text=\"\"};label{text=\"Floor Damage\"};label{text=\"\"};label{text=\"" + floor
/*      */ 
/*      */           
/* 4618 */           .getDamage() + "\"};");
/*      */       
/* 4620 */       buf.append("label{text=\"\"};label{text=\"Floor Dir\"};label{text=\"\"};label{text=\"" + floor
/*      */ 
/*      */           
/* 4623 */           .getDir() + "\"};");
/*      */       
/* 4625 */       buf.append("label{text=\"\"};label{text=\"Floor Level\"};label{text=\"\"};label{text=\"" + floor
/*      */ 
/*      */           
/* 4628 */           .getFloorLevel() + "\"};");
/*      */       
/* 4630 */       buf.append("label{text=\"\"};label{text=\"Floor Height Offset\"};label{text=\"\"};label{text=\"" + floor
/*      */ 
/*      */           
/* 4633 */           .getHeightOffset() + "\"};");
/*      */     }
/* 4635 */     else if (aIdType == 28) {
/*      */       
/* 4637 */       BridgePart bridgePart = BridgePartEnum.getBridgePartFromId(this.wurmId);
/*      */       
/* 4639 */       buf.append("label{text=\"\"};label{text=\"Bridge Part type\"};label{text=\"\"};label{text=\"" + bridgePart
/*      */ 
/*      */           
/* 4642 */           .getType().getName() + " (" + bridgePart.getType().getCode() + ")\"};");
/*      */       
/* 4644 */       buf.append("label{text=\"\"};label{text=\"Bridge Part material\"};label{text=\"\"};label{text=\"" + bridgePart
/*      */ 
/*      */           
/* 4647 */           .getMaterial().getName() + " (" + bridgePart.getMaterial().getCode() + ")\"};");
/*      */       
/* 4649 */       buf.append("label{text=\"\"};label{text=\"Bridge Part QL\"};label{text=\"\"};label{text=\"" + bridgePart
/*      */ 
/*      */           
/* 4652 */           .getCurrentQL() + "\"};");
/*      */       
/* 4654 */       buf.append("label{text=\"\"};label{text=\"Bridge Part Damage\"};label{text=\"\"};label{text=\"" + bridgePart
/*      */ 
/*      */           
/* 4657 */           .getDamage() + "\"};");
/*      */       
/* 4659 */       buf.append("label{text=\"\"};label{text=\"Bridge Part Dir\"};label{text=\"\"};label{text=\"" + bridgePart
/*      */ 
/*      */           
/* 4662 */           .getDir() + "\"};");
/*      */       
/* 4664 */       buf.append("label{text=\"\"};label{text=\"Bridge Part Slope\"};label{text=\"\"};label{text=\"" + bridgePart
/*      */ 
/*      */           
/* 4667 */           .getSlope() + "\"};");
/*      */       
/* 4669 */       buf.append("label{text=\"\"};label{text=\"Bridge Part Height\"};label{text=\"\"};label{text=\"" + bridgePart
/*      */ 
/*      */           
/* 4672 */           .getHeight() + " (" + bridgePart.getHeight() + ")\"};");
/*      */       
/* 4674 */       String roadSurface = "default ";
/* 4675 */       if (bridgePart.getRoadType() != 0) {
/*      */         
/* 4677 */         Tiles.Tile roadTile = Tiles.getTile(bridgePart.getRoadType());
/* 4678 */         roadSurface = roadTile.getDesc() + " ";
/*      */       } 
/* 4680 */       buf.append("label{text=\"\"};label{text=\"Bridge Part Road\"};label{text=\"\"};label{text=\"" + roadSurface + "(" + bridgePart
/*      */ 
/*      */           
/* 4683 */           .getRoadType() + ")\"};");
/*      */       
/* 4685 */       buf.append("label{text=\"\"};label{text=\"Last Maintained\"};label{text=\"\"};label{text=\"" + new Date(bridgePart
/*      */ 
/*      */             
/* 4688 */             .getLastUsed()) + "\"};");
/*      */     }
/*      */     else {
/*      */       
/* 4692 */       if (Tiles.getTile(tileType).isBush() || Tiles.getTile(tileType).isTree()) {
/* 4693 */         buf.append("label{text=\"\"};label{text=\"Resource:Damage\"};label{text=\"\"};label{text=\"" + resource + "\"};");
/*      */ 
/*      */       
/*      */       }
/* 4697 */       else if (tileType == Tiles.Tile.TILE_FIELD.getIntId() || tileType == Tiles.Tile.TILE_FIELD2.getIntId()) {
/* 4698 */         buf.append("label{text=\"\"};label{text=\"Resource:Farmed|Chance\"};label{text=\"\"};label{text=\"" + (resource >>> 11 & 0x1F) + "|" + (resource & 0x3FF) + "(Max:" + 'Ͽ' + ")\"};");
/*      */ 
/*      */ 
/*      */       
/*      */       }
/* 4703 */       else if (tileType == Tiles.Tile.TILE_CLAY.getIntId()) {
/* 4704 */         buf.append("label{text=\"\"};label{text=\"Resource:Dug count\"};label{text=\"\"};label{text=\"" + resource + "\"};");
/*      */ 
/*      */       
/*      */       }
/* 4708 */       else if (tileType == Tiles.Tile.TILE_MINE_DOOR_WOOD.getIntId() || tileType == Tiles.Tile.TILE_MINE_DOOR_STONE.getIntId() || tileType == Tiles.Tile.TILE_MINE_DOOR_GOLD
/* 4709 */         .getIntId() || tileType == Tiles.Tile.TILE_MINE_DOOR_SILVER.getIntId() || tileType == Tiles.Tile.TILE_MINE_DOOR_STEEL
/* 4710 */         .getIntId()) {
/* 4711 */         buf.append("label{text=\"\"};label{text=\"Resource:Door Strength\"};label{text=\"\"};label{text=\"" + resource + "\"};");
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 4716 */         buf.append("label{text=\"\"};label{text=\"Resource:\"};label{text=\"\"};label{text=\"" + resource + "\"};");
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 4722 */       if (tileType == Tiles.Tile.TILE_GRASS.getIntId()) {
/* 4723 */         buf.append("label{text=\"\"};label{text=\"Data:Growth|Flower\"};label{text=\"\"};label{text=\"" + 
/*      */ 
/*      */             
/* 4726 */             GrassData.GrowthStage.decodeTileData(data).name().toLowerCase() + "|" + 
/* 4727 */             GrassData.FlowerType.decodeTileData(data).name().toLowerCase() + "\"};");
/* 4728 */       } else if (tileType == Tiles.Tile.TILE_KELP.getIntId() || tileType == Tiles.Tile.TILE_REED.getIntId()) {
/* 4729 */         buf.append("label{text=\"\"};label{text=\"Data:Growth\"};label{text=\"\"};label{text=\"" + 
/*      */ 
/*      */             
/* 4732 */             GrassData.GrowthStage.decodeTileData(data).name().toLowerCase() + "\"};");
/* 4733 */       } else if (tileType == Tiles.Tile.TILE_TREE.getIntId() || tileType == Tiles.Tile.TILE_ENCHANTED_TREE.getIntId() || tileType == Tiles.Tile.TILE_MYCELIUM_TREE
/* 4734 */         .getIntId()) {
/* 4735 */         buf.append("label{text=\"\"};label{text=\"Data:Age|Type\"};label{text=\"\"};label{text=\"" + 
/*      */ 
/*      */             
/* 4738 */             FoliageAge.getFoliageAge(data).getAgeName() + "(" + (data >>> 4 & 0xF) + ")|" + 
/* 4739 */             TreeData.getTypeName((byte)(data & 0xF)) + "(" + (data & 0xF) + ")\"};");
/* 4740 */       } else if (tileType == Tiles.Tile.TILE_BUSH.getIntId() || tileType == Tiles.Tile.TILE_ENCHANTED_BUSH.getIntId() || tileType == Tiles.Tile.TILE_MYCELIUM_BUSH
/* 4741 */         .getIntId()) {
/* 4742 */         buf.append("label{text=\"\"};label{text=\"Data:Age|Type\"};label{text=\"\"};label{text=\"" + 
/*      */ 
/*      */             
/* 4745 */             FoliageAge.getFoliageAge(data).getAgeName() + "(" + (data >>> 4 & 0xF) + ")|" + 
/* 4746 */             BushData.getTypeName((byte)(data & 0xF)) + "(" + (data & 0xF) + ")\"};");
/* 4747 */       } else if (Tiles.getTile(tileType).isTree() || Tiles.getTile(tileType).isBush()) {
/*      */ 
/*      */         
/* 4750 */         buf.append("label{text=\"\"};label{text=\"Data:Age|hasFruit|inCentre|GrassLength\"};label{text=\"\"};label{text=\"" + 
/*      */ 
/*      */             
/* 4753 */             FoliageAge.getFoliageAge(data).getAgeName() + "(" + (data >>> 4 & 0xF) + ")|" + (((data & 0x8) == 0) ? "no Fruit" : "has Fruit") + "|" + (((data & 0x4) == 0) ? "Natural" : "In Centre") + "|" + 
/*      */ 
/*      */             
/* 4756 */             GrassData.GrowthTreeStage.fromInt(data & 0x3).name() + "\"};");
/*      */       
/*      */       }
/* 4759 */       else if (tileType == Tiles.Tile.TILE_FIELD.getIntId() || tileType == Tiles.Tile.TILE_FIELD2.getIntId()) {
/* 4760 */         buf.append("label{text=\"\"};label{text=\"Data:Tended|Age|Crop\"};label{text=\"\"};label{text=\"" + 
/*      */ 
/*      */             
/* 4763 */             Crops.decodeFieldState(data) + "|" + 
/* 4764 */             Crops.decodeFieldAge(data) + "|" + 
/* 4765 */             Crops.getCropName(Crops.getCropNumber(tileType, data)) + "(" + (data & 0xF) + ")\"};");
/*      */       } else {
/*      */         
/* 4768 */         buf.append("label{text=\"\"};label{text=\"Data:\"};label{text=\"\"};label{text=\"" + data + "\"};");
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 4774 */     if (lStructure != null) {
/*      */       
/* 4776 */       buf.append("label{text=\"\"};label{text=\"Structure ID\"};label{text=\"\"};label{text=\"" + lStructure
/*      */ 
/*      */           
/* 4779 */           .getWurmId() + "\"};");
/*      */       
/* 4781 */       buf.append("radio{group=\"tid\";id=\"1," + lStructure.getWurmId() + "," + '\001' + "\"};label{text=\"Structure Name\"};label{text=\"\"};label{text=\"" + lStructure
/*      */ 
/*      */           
/* 4784 */           .getName() + "\"};");
/*      */     } 
/*      */     
/* 4787 */     buf.append("label{text=\"\"};label{text=\"Kingdom\"};label{text=\"\"};label{text=\"" + 
/*      */ 
/*      */         
/* 4790 */         Kingdoms.getNameFor(tile.getKingdom()) + "(" + tile
/* 4791 */         .getKingdom() + ")\"};");
/*      */ 
/*      */     
/* 4794 */     if (lVillage != null) {
/*      */       
/* 4796 */       buf.append("radio{group=\"tid\";id=\"2," + lVillage.getId() + "," + '\001' + "\"};label{text=\"Village Name\"};label{text=\"\"};label{text=\"" + lVillage
/*      */ 
/*      */           
/* 4799 */           .getName() + "\"};");
/*      */     }
/* 4801 */     else if (perimVillage != null) {
/*      */       
/* 4803 */       buf.append("radio{group=\"tid\";id=\"2," + perimVillage.getId() + "," + '\001' + "\"};label{text=\"Perimeter Village Name\"};label{text=\"\"};label{text=\"" + perimVillage
/*      */ 
/*      */           
/* 4806 */           .getName() + "\"};");
/*      */     } 
/*      */     
/* 4809 */     int tx = tile.getTileX();
/* 4810 */     int ty = tile.getTileY();
/* 4811 */     buf.append("radio{group=\"tid\";id=\"3," + tx + "," + ty + "," + isOnSurface + "\"};label{text=\"Coord (X,Y,surface)\"};radio{group=\"tid\";id=\"" + '\f' + "," + tx + "," + ty + "," + isOnSurface + "\"};label{text=\"(" + tx + "," + ty + "," + isOnSurface + ")\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4816 */     if (isOnSurface) {
/*      */       
/* 4818 */       for (int x = 0; x <= 1; x++)
/*      */       {
/* 4820 */         for (int y = 0; y <= 1; y++)
/*      */         {
/* 4822 */           meshtile = Server.surfaceMesh.getTile(tilex + x, tiley + y);
/*      */           
/* 4824 */           short tileHeight = Tiles.decodeHeight(meshtile);
/* 4825 */           int rockTile = Server.rockMesh.getTile(tilex + x, tiley + y);
/* 4826 */           short rockHeight = Tiles.decodeHeight(rockTile);
/*      */           
/* 4828 */           buf.append("label{text=\"\"};label{text=\"" + ((y == 0) ? "N" : "S") + ((x == 0) ? "W" : "E") + " Surface,Rock Heights\"};label{text=\"\"};label{text=\"" + tileHeight + "," + rockHeight + "\"};");
/*      */         
/*      */         }
/*      */ 
/*      */       
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 4838 */       for (int x = 0; x <= 1; x++) {
/*      */         
/* 4840 */         for (int y = 0; y <= 1; y++) {
/*      */           
/* 4842 */           int currtile = Server.caveMesh.getTile(tilex + x, tiley + y);
/* 4843 */           short currHeight = Tiles.decodeHeight(currtile);
/* 4844 */           short cceil = (short)(Tiles.decodeData(currtile) & 0xFF);
/*      */           
/* 4846 */           buf.append("label{text=\"\"};label{text=\"" + ((y == 0) ? "N" : "S") + ((x == 0) ? "W" : "E") + " Cave floor, Ceiling gap (Ceiling height)\"};label{text=\"\"};label{text=\"" + currHeight + "," + cceil + " (" + (currHeight + cceil) + ")\"};");
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4855 */     buf.append("}");
/* 4856 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String tileCreatures(VolaTile tile) {
/* 4861 */     StringBuilder buf = new StringBuilder();
/*      */ 
/*      */     
/*      */     try {
/* 4865 */       Creature[] lCreatures = tile.getCreatures();
/* 4866 */       if (lCreatures.length > 0) {
/* 4867 */         buf.append(creaturesTable(lCreatures));
/*      */       } else {
/* 4869 */         buf.append("label{text=\"No Creatures found\"}");
/*      */       } 
/* 4871 */     } catch (Exception e1) {
/*      */       
/* 4873 */       logger.log(Level.WARNING, e1.getMessage(), e1);
/* 4874 */       buf.append("label{text=\"Error reading Creatures\"}");
/* 4875 */       buf.append("label{text=\"Exception:\"};");
/* 4876 */       buf.append("label{text=\"" + e1.getMessage() + "\"};");
/*      */     } 
/* 4878 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String tileWalls(VolaTile tile) {
/* 4883 */     StringBuilder buf = new StringBuilder();
/* 4884 */     buf.append("label{type=\"italic\";text=\"Only returns walls if tile is in a structure (atm)\"}");
/*      */ 
/*      */     
/*      */     try {
/* 4888 */       Wall[] lWalls = tile.getWalls();
/* 4889 */       if (lWalls.length > 0) {
/* 4890 */         buf.append(wallsTable(lWalls, tile.getTileX(), tile.getTileY()));
/*      */       } else {
/* 4892 */         buf.append("label{text=\"No Walls found\"}");
/*      */       } 
/* 4894 */     } catch (Exception e1) {
/*      */       
/* 4896 */       logger.log(Level.WARNING, e1.getMessage(), e1);
/* 4897 */       buf.append("label{text=\"Error reading Walls\"}");
/* 4898 */       buf.append("label{text=\"Exception:\"};");
/* 4899 */       buf.append("label{text=\"" + e1.getMessage() + "\"};");
/*      */     } 
/* 4901 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String tileFences(VolaTile tile) {
/* 4906 */     StringBuilder buf = new StringBuilder();
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 4911 */       Fence[] lFences = tile.getAllFences();
/* 4912 */       if (lFences.length > 0) {
/* 4913 */         buf.append(fencesTable(lFences, tile.getTileX(), tile.getTileY()));
/*      */       } else {
/* 4915 */         buf.append("label{text=\"No Fences found\"}");
/*      */       } 
/* 4917 */     } catch (Exception e1) {
/*      */       
/* 4919 */       logger.log(Level.WARNING, e1.getMessage(), e1);
/* 4920 */       buf.append("label{text=\"Error reading Fences\"}");
/* 4921 */       buf.append("label{text=\"Exception:\"};");
/* 4922 */       buf.append("label{text=\"" + e1.getMessage() + "\"};");
/*      */     } 
/* 4924 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String tileItems(VolaTile tile) {
/* 4929 */     StringBuilder buf = new StringBuilder();
/*      */ 
/*      */     
/*      */     try {
/* 4933 */       Item[] litems = tile.getItems();
/* 4934 */       if (litems.length > 0) {
/* 4935 */         buf.append(itemsTable(litems));
/*      */       } else {
/* 4937 */         buf.append("label{text=\"No Items found\"}");
/*      */       } 
/* 4939 */     } catch (Exception e1) {
/*      */       
/* 4941 */       logger.log(Level.WARNING, e1.getMessage(), e1);
/* 4942 */       buf.append("label{text=\"Error reading Items\"}");
/* 4943 */       buf.append("label{text=\"Exception:\"};");
/* 4944 */       buf.append("label{text=\"" + e1.getMessage() + "\"};");
/*      */     } 
/* 4946 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String tileFloors(VolaTile tile) {
/* 4951 */     StringBuilder buf = new StringBuilder();
/* 4952 */     Structure lStructure = tile.getStructure();
/*      */ 
/*      */     
/*      */     try {
/* 4956 */       Floor[] lfloors = lStructure.getFloorsAtTile(tile.getTileX(), tile.getTileY(), 0, 9999);
/* 4957 */       if (lfloors.length > 0) {
/* 4958 */         buf.append(floorsTable(lfloors));
/*      */       } else {
/* 4960 */         buf.append("label{text=\"No floors found\"}");
/*      */       } 
/* 4962 */     } catch (Exception e1) {
/*      */       
/* 4964 */       logger.log(Level.WARNING, e1.getMessage(), e1);
/* 4965 */       buf.append("label{text=\"Error reading Floors\"}");
/* 4966 */       buf.append("label{text=\"Exception:\"};");
/* 4967 */       buf.append("label{text=\"" + e1.getMessage() + "\"};");
/*      */     } 
/* 4969 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String tileBridgeParts(VolaTile tile) {
/* 4974 */     StringBuilder buf = new StringBuilder();
/*      */ 
/*      */     
/*      */     try {
/* 4978 */       BridgePart[] lbridgeParts = tile.getBridgeParts();
/* 4979 */       if (lbridgeParts.length > 0) {
/* 4980 */         buf.append(bridgePartTable(lbridgeParts));
/*      */       } else {
/* 4982 */         buf.append("label{text=\"No bridge parts found\"}");
/*      */       } 
/* 4984 */     } catch (Exception e1) {
/*      */       
/* 4986 */       logger.log(Level.WARNING, e1.getMessage(), e1);
/* 4987 */       buf.append("label{text=\"Error reading Bridge Parts\"}");
/* 4988 */       buf.append("label{text=\"Exception:\"};");
/* 4989 */       buf.append("label{text=\"" + e1.getMessage() + "\"};");
/*      */     } 
/* 4991 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String creatureDetails() {
/* 4996 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 4998 */     Creature creature = null;
/* 4999 */     Item inventory = null;
/* 5000 */     Body body = null;
/* 5001 */     int numbInventory = 0;
/* 5002 */     int numbBody = 0;
/* 5003 */     int numbTraits = 0;
/*      */     
/*      */     try {
/* 5006 */       creature = Creatures.getInstance().getCreature(this.wurmId);
/*      */       
/* 5008 */       inventory = creature.getInventory();
/* 5009 */       numbInventory = inventory.getItems().size();
/* 5010 */       body = creature.getBody();
/* 5011 */       numbBody = (body.getAllItems()).length;
/* 5012 */       numbTraits = creature.getStatus().traitsCount();
/* 5013 */       if (this.displaySubType == 1) {
/* 5014 */         buf.append(creatureSummary(creature));
/* 5015 */       } else if (this.displaySubType == 2) {
/* 5016 */         buf.append(showGuests(AnimalSettings.Animal2Permissions.getPermissions(), creature.getPermissionsPlayerList()));
/* 5017 */       } else if (this.displaySubType == 3) {
/* 5018 */         buf.append(showHistory(creature.getWurmId()));
/* 5019 */       } else if (this.displaySubType == 8) {
/* 5020 */         buf.append(containerItems(inventory));
/* 5021 */       } else if (this.displaySubType == 9) {
/* 5022 */         buf.append(containerItems(body.getBodyItem()));
/*      */       } else {
/*      */         
/* 5025 */         buf.append(creatureTraits(creature));
/*      */       } 
/* 5027 */     } catch (NoSuchCreatureException e) {
/*      */       
/* 5029 */       buf.append("label{text=\"no creature found with that Id\"}");
/*      */     } 
/*      */     
/* 5032 */     buf.append("label{type=\"bold\";text=\"------------------------------ Links --------------------------------------\"}");
/*      */     
/* 5034 */     buf.append("table{rows=\"2\";cols=\"4\";");
/* 5035 */     buf.append("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\001' + "\"};label{text=\"Summary" + ((this.displaySubType == 1) ? " (Showing)" : "") + "\"};");
/*      */     
/* 5037 */     if (creature != null) {
/*      */       
/* 5039 */       buf.append(((creature.getPermissionsPlayerList().size() == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\002' + "\"};")) + "label{text=\"" + creature
/*      */           
/* 5041 */           .getPermissionsPlayerList().size() + " Guests" + ((this.displaySubType == 2) ? " (Showing)" : "") + "\"};");
/*      */ 
/*      */       
/* 5044 */       buf.append((((PermissionsHistories.getPermissionsHistoryFor(creature.getWurmId()).getHistoryEvents()).length == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\003' + "\"};")) + "label{text=\"" + (
/*      */           
/* 5046 */           PermissionsHistories.getPermissionsHistoryFor(creature.getWurmId()).getHistoryEvents()).length + " History" + ((this.displaySubType == 3) ? " (Showing)" : "") + "\"};");
/*      */     } 
/*      */     
/* 5049 */     if (inventory != null) {
/* 5050 */       buf.append(((numbInventory == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\b' + "\"};")) + "label{text=\"" + numbInventory + " Inventory" + ((this.displaySubType == 8) ? " (Showing)" : "") + "\"};");
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 5055 */       buf.append("label{text=\"error\"};label{text=\"inventory\"};");
/*      */     } 
/* 5057 */     if (body != null) {
/* 5058 */       buf.append(((numbBody == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\t' + "\"};")) + "label{text=\"" + numbBody + " Body" + ((this.displaySubType == 9) ? " (Showing)" : "") + "\"};");
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 5063 */       buf.append("label{text=\"error\"};label{text=\"body\"};");
/* 5064 */     }  buf.append(((numbTraits == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\f' + "\"};")) + "label{text=\"" + numbTraits + " Traits" + ((this.displaySubType == 12) ? " (Showing)" : "") + "\"};");
/*      */ 
/*      */ 
/*      */     
/* 5068 */     buf.append("label{text=\"\"};label{text=\"\"};");
/* 5069 */     buf.append("}");
/*      */     
/* 5071 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String creatureSummary(Creature creature) {
/* 5076 */     StringBuilder buf = new StringBuilder();
/* 5077 */     int rows = 6;
/* 5078 */     if (creature.isCaredFor()) {
/* 5079 */       rows++;
/*      */     }
/* 5081 */     Brand brand = Creatures.getInstance().getBrand(this.wurmId);
/* 5082 */     if (brand != null) {
/* 5083 */       rows++;
/*      */     }
/* 5085 */     Shop shop = null;
/* 5086 */     if (creature.isNpcTrader()) {
/*      */       
/* 5088 */       shop = Economy.getEconomy().getShop(creature);
/* 5089 */       if (shop != null) {
/*      */         
/* 5091 */         rows += 2;
/* 5092 */         if (shop != null && shop.isPersonal()) {
/* 5093 */           rows++;
/*      */         }
/*      */       } 
/*      */     } 
/* 5097 */     buf.append("table{rows=\"" + rows + "\";cols=\"4\";");
/*      */     
/* 5099 */     buf.append("label{text=\"\"};label{text=\"Wurm Id\"};label{text=\"\"};label{text=\"" + this.wurmId + "\"}");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5104 */     buf.append("label{text=\"\"};label{text=\"Name\"};label{text=\"\"};label{text=\"" + creature
/*      */ 
/*      */         
/* 5107 */         .getName() + "\"}");
/*      */     
/* 5109 */     buf.append("label{text=\"\"};label{text=\"Template Name\"};label{text=\"\"};label{text=\"" + creature
/*      */ 
/*      */         
/* 5112 */         .getTemplate().getName() + "\"}");
/*      */     
/* 5114 */     int cx = creature.getTileX();
/* 5115 */     int cy = creature.getTileY();
/* 5116 */     boolean cs = creature.isOnSurface();
/* 5117 */     buf.append("radio{group=\"tid\";id=\"3," + cx + "," + cy + "," + cs + "\"};label{text=\"Coords (X,Y,surface)\"};radio{group=\"tid\";id=\"" + '\f' + "," + cx + "," + cy + "," + cs + "\"};label{text=\"(" + cx + "," + cy + "," + cs + ")\"}");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5122 */     buf.append("label{text=\"\"};label{text=\"Actual (X,Y,Z)\"};label{text=\"\"};label{text=\"(" + creature
/*      */ 
/*      */         
/* 5125 */         .getPosX() + "," + creature.getPosY() + "," + creature.getPositionZ() + ")\"}");
/*      */     
/* 5127 */     buf.append("label{text=\"\"};label{text=\"Rotation\"};label{text=\"\"};label{text=\"(" + creature
/*      */ 
/*      */         
/* 5130 */         .getStatus().getRotation() + ")\"}");
/*      */     
/* 5132 */     buf.append("label{text=\"\"};label{text=\"Sex\"};label{text=\"\"};");
/*      */ 
/*      */     
/* 5135 */     if (creature.getSex() == 0) {
/* 5136 */       buf.append("label{text=\"Male\"}");
/* 5137 */     } else if (creature.getSex() == 1) {
/* 5138 */       buf.append("label{text=\"Female\"}");
/*      */     } else {
/* 5140 */       buf.append("label{type=\"italic\";text=\"Unknown!\"}");
/*      */     } 
/* 5142 */     if (brand != null) {
/*      */       
/*      */       try {
/*      */ 
/*      */         
/* 5147 */         Village brandVillage = Villages.getVillage((int)brand.getBrandId());
/* 5148 */         buf.append("radio{group=\"tid\";id=\"2," + brandVillage.getId() + "," + '\001' + "\"};label{text=\"Branded by Village\"};label{text=\"\"};label{text=\"" + brandVillage
/*      */ 
/*      */             
/* 5151 */             .getName() + "\"}");
/*      */       }
/* 5153 */       catch (NoSuchVillageException e) {
/*      */         
/* 5155 */         buf.append("label{text=\"\"};label{text=\"Branded by Village Id\"};label{text=\"\"};label{text=\"" + brand
/*      */ 
/*      */             
/* 5158 */             .getBrandId() + "\"}");
/*      */       } 
/*      */     }
/*      */     
/* 5162 */     buf.append("label{text=\"\"};label{text=\"Age\"};label{text=\"\"};label{text=\"" + creature
/*      */ 
/*      */         
/* 5165 */         .getStatus().getAgeString() + " (" + (creature.getStatus()).age + ")\"}");
/*      */     
/* 5167 */     buf.append("label{text=\"\"};label{text=\"Carried Weight\"};label{text=\"\"};label{text=\"" + creature
/*      */ 
/*      */         
/* 5170 */         .getCarriedWeight() + "\"}");
/*      */     
/* 5172 */     buf.append("label{text=\"\"};label{text=\"Kingdom\"};label{text=\"\"};label{text=\"" + 
/*      */ 
/*      */         
/* 5175 */         Kingdoms.getNameFor(creature.getKingdomId()) + " (" + creature
/* 5176 */         .getKingdomId() + ")\"}");
/*      */     
/* 5178 */     if (creature.isCaredFor()) {
/*      */       
/* 5180 */       PlayerInfo pInfo = PlayerInfoFactory.getPlayerInfoWithWurmId(creature.getCareTakerId());
/* 5181 */       if (pInfo != null) {
/* 5182 */         buf.append("radio{group=\"tid\";id=\"1," + pInfo.wurmId + "," + '\001' + "\"};label{text=\"Cared For By Name\"};label{text=\"\"};label{text=\"" + pInfo
/*      */ 
/*      */             
/* 5185 */             .getName() + "\"};");
/*      */       } else {
/* 5187 */         buf.append("label{text=\"\"};label{text=\"Cared For By Id\"};label{text=\"\"};label{text=\"" + creature
/*      */ 
/*      */             
/* 5190 */             .getCareTakerId() + "\"};");
/*      */       } 
/*      */     } 
/* 5193 */     buf.append(addBridge(creature.getBridgeId()));
/*      */     
/* 5195 */     Vehicle hitch = creature.getHitched();
/* 5196 */     if (hitch != null) {
/*      */       
/* 5198 */       buf.append("radio{group=\"tid\";id=\"1," + hitch.wurmid + "," + '\001' + "\"};label{text=\"Hitched To\"};label{text=\"\"};label{text=\"" + hitch.name
/*      */ 
/*      */           
/* 5201 */           .replace("\"", "'") + "\"}");
/*      */     }
/* 5203 */     else if (creature.vehicle != -10L) {
/*      */       
/* 5205 */       Vehicle vehic = null;
/* 5206 */       String stype = "";
/* 5207 */       if (WurmId.getType(creature.vehicle) == 1) {
/*      */ 
/*      */         
/*      */         try {
/* 5211 */           Creature creat = Server.getInstance().getCreature(creature.vehicle);
/* 5212 */           vehic = Vehicles.getVehicle(creat);
/* 5213 */           Seat seat = vehic.getSeatFor(creature.getWurmId());
/* 5214 */           stype = (seat.getType() == 0) ? "Commander of" : "Passenger of";
/*      */         }
/* 5216 */         catch (NoSuchPlayerException|NoSuchCreatureException e) {
/*      */ 
/*      */           
/* 5219 */           logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*      */         } 
/*      */       } else {
/*      */ 
/*      */         
/*      */         try {
/*      */           
/* 5226 */           Item vehicle = Items.getItem(creature.vehicle);
/* 5227 */           vehic = Vehicles.getVehicle(vehicle);
/* 5228 */           Seat seat = vehic.getSeatFor(creature.getWurmId());
/* 5229 */           stype = (seat.getType() == 0) ? "Commander of" : (vehicle.isChair() ? "Sitting on" : "Passenger of");
/*      */         }
/* 5231 */         catch (NoSuchItemException e) {
/*      */ 
/*      */           
/* 5234 */           logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*      */         } 
/*      */       } 
/* 5237 */       if (stype.length() > 0)
/* 5238 */         buf.append("radio{group=\"tid\";id=\"1," + vehic.wurmid + "," + '\001' + "\"};label{text=\"" + stype + "\"};label{text=\"\"};label{text=\"" + vehic.name
/*      */ 
/*      */             
/* 5241 */             .replace("\"", "'") + "\"}"); 
/*      */     } 
/* 5243 */     if (shop != null) {
/*      */       
/* 5245 */       buf.append("label{text=\"\"};label{text=\"# items\"};label{text=\"\"};label{text=\"" + shop
/*      */ 
/*      */           
/* 5248 */           .getNumberOfItems() + "\"}");
/*      */       
/* 5250 */       long millis = shop.howLongEmpty();
/* 5251 */       long secsAll = TimeUnit.MILLISECONDS.toSeconds(millis);
/* 5252 */       long minsAll = TimeUnit.SECONDS.toMinutes(secsAll);
/* 5253 */       long hoursAll = TimeUnit.MINUTES.toHours(minsAll);
/* 5254 */       long days = TimeUnit.HOURS.toDays(hoursAll);
/* 5255 */       long secs = secsAll - TimeUnit.MINUTES.toSeconds(minsAll);
/* 5256 */       long mins = minsAll - TimeUnit.HOURS.toMinutes(hoursAll);
/* 5257 */       long hours = hoursAll - TimeUnit.DAYS.toHours(days);
/*      */       
/* 5259 */       buf.append("label{text=\"\"};label{text=\"When Empty\"};label{text=\"\"};label{text=\"" + days + " days, " + hours + " hours, " + mins + " mins and " + secs + " secs ago\"}");
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 5264 */       if (shop.isPersonal()) {
/*      */         
/* 5266 */         Item item = Items.findMerchantContractFromId(creature.getWurmId());
/*      */         
/* 5268 */         if (item == null) {
/* 5269 */           buf.append("label{text=\"\"};label{text=\"Contract Id\"};label{text=\"\"};label{text=\"Not Found\"}");
/*      */         
/*      */         }
/*      */         else {
/*      */           
/* 5274 */           buf.append("radio{group=\"tid\";id=\"1," + item.getWurmId() + "," + '\001' + "\"};label{text=\"Contract Id\"};label{text=\"\"};label{text=\"" + item
/*      */ 
/*      */               
/* 5277 */               .getName() + "\"}");
/*      */         } 
/*      */       } 
/*      */     } 
/* 5281 */     buf.append("label{text=\"\"};label{text=\"Hunger\"};label{text=\"\"};label{text=\"" + creature
/*      */ 
/*      */         
/* 5284 */         .getStatus().getHunger() + "\"};");
/*      */     
/* 5286 */     buf.append("label{text=\"\"};label{text=\"Thirst\"};label{text=\"\"};label{text=\"" + creature
/*      */ 
/*      */         
/* 5289 */         .getStatus().getThirst() + "\"};");
/*      */     
/* 5291 */     buf.append("label{text=\"\"};label{text=\"Nutrition\"};label{text=\"\"};label{text=\"" + creature
/*      */ 
/*      */         
/* 5294 */         .getStatus().getNutritionlevel() + "\"};");
/*      */     
/* 5296 */     buf.append("}");
/* 5297 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String creatureTraits(Creature creature) {
/* 5302 */     StringBuilder buf = new StringBuilder();
/* 5303 */     for (int x = 0; x < 64; x++) {
/*      */       
/* 5305 */       String colour = "";
/* 5306 */       if (Traits.isTraitNegative(x)) {
/* 5307 */         colour = "color=\"255,66,66\";";
/* 5308 */       } else if (!Traits.isTraitNeutral(x)) {
/* 5309 */         colour = "color=\"66,255,66\";";
/*      */       } 
/* 5311 */       if (creature.getStatus().isTraitBitSet(x) && Traits.getTraitString(x).length() > 0) {
/* 5312 */         buf.append("label{" + colour + "text=\"" + Traits.getTraitString(x) + "\"};");
/* 5313 */       } else if (x == 15 && creature.getStatus().isTraitBitSet(x)) {
/* 5314 */         buf.append("label{" + colour + "text=\"Colour: " + StringUtilities.raiseFirstLetterOnly(creature.getColourName(x)) + "\"};");
/* 5315 */       } else if (x == 16 && creature.getStatus().isTraitBitSet(x)) {
/* 5316 */         buf.append("label{" + colour + "text=\"Colour: " + StringUtilities.raiseFirstLetterOnly(creature.getColourName(x)) + "\"};");
/* 5317 */       } else if (x == 17 && creature.getStatus().isTraitBitSet(x)) {
/* 5318 */         buf.append("label{" + colour + "text=\"Colour: " + StringUtilities.raiseFirstLetterOnly(creature.getColourName(x)) + "\"};");
/* 5319 */       } else if (x == 18 && creature.getStatus().isTraitBitSet(x)) {
/* 5320 */         buf.append("label{" + colour + "text=\"Colour: " + StringUtilities.raiseFirstLetterOnly(creature.getColourName(x)) + "\"};");
/* 5321 */       } else if (x == 24 && creature.getStatus().isTraitBitSet(x)) {
/* 5322 */         buf.append("label{" + colour + "text=\"Colour: " + StringUtilities.raiseFirstLetterOnly(creature.getColourName(x)) + "\"};");
/* 5323 */       } else if (x == 25 && creature.getStatus().isTraitBitSet(x)) {
/* 5324 */         buf.append("label{" + colour + "text=\"Colour: " + StringUtilities.raiseFirstLetterOnly(creature.getColourName(x)) + "\"};");
/* 5325 */       } else if (x == 23 && creature.getStatus().isTraitBitSet(x)) {
/* 5326 */         buf.append("label{" + colour + "text=\"Colour: " + StringUtilities.raiseFirstLetterOnly(creature.getColourName(x)) + "\"};");
/* 5327 */       } else if (x == 30 && creature.getStatus().isTraitBitSet(x)) {
/* 5328 */         buf.append("label{" + colour + "text=\"Colour: " + StringUtilities.raiseFirstLetterOnly(creature.getColourName(x)) + "\"};");
/* 5329 */       } else if (x == 31 && creature.getStatus().isTraitBitSet(x)) {
/* 5330 */         buf.append("label{" + colour + "text=\"Colour: " + StringUtilities.raiseFirstLetterOnly(creature.getColourName(x)) + "\"};");
/* 5331 */       } else if (x == 32 && creature.getStatus().isTraitBitSet(x)) {
/* 5332 */         buf.append("label{" + colour + "text=\"Colour: " + StringUtilities.raiseFirstLetterOnly(creature.getColourName(x)) + "\"};");
/* 5333 */       } else if (x == 33 && creature.getStatus().isTraitBitSet(x)) {
/* 5334 */         buf.append("label{" + colour + "text=\"Colour: " + StringUtilities.raiseFirstLetterOnly(creature.getColourName(x)) + "\"};");
/* 5335 */       } else if (x == 34 && creature.getStatus().isTraitBitSet(x)) {
/* 5336 */         buf.append("label{" + colour + "text=\"Colour: " + StringUtilities.raiseFirstLetterOnly(creature.getColourName(x)) + "\"};");
/*      */       } 
/* 5338 */     }  return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String itemDetails() {
/* 5343 */     StringBuilder buf = new StringBuilder();
/* 5344 */     Item item = null;
/*      */     
/*      */     try {
/* 5347 */       item = Items.getItem(this.wurmId);
/* 5348 */       if (this.displaySubType == 1) {
/* 5349 */         buf.append(itemSummary(item));
/* 5350 */       } else if (this.displaySubType == 2) {
/* 5351 */         buf.append(showGuests(ItemSettings.GMItemPermissions.getPermissions(), item.getPermissionsPlayerList()));
/* 5352 */       } else if (this.displaySubType == 3) {
/* 5353 */         buf.append(showHistory(item.getWurmId()));
/*      */       } else {
/* 5355 */         buf.append(containerItems(item));
/*      */       } 
/* 5357 */     } catch (NoSuchItemException e) {
/*      */       
/* 5359 */       logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/* 5360 */       buf.append("label{text=\"Error - no such Item\"}");
/* 5361 */       buf.append("label{text=\"Exception:\"};");
/* 5362 */       buf.append("label{text=\"" + e.getMessage() + "\"};");
/*      */     } 
/*      */     
/* 5365 */     buf.append("label{type=\"bold\";text=\"------------------------------ Links --------------------------------------\"}");
/* 5366 */     int rows = 1;
/* 5367 */     buf.append("table{rows=\"1\";cols=\"4\";");
/* 5368 */     buf.append("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\001' + "\"};label{text=\"Summary" + ((this.displaySubType == 1) ? " (Showing)" : "") + "\"};");
/*      */ 
/*      */     
/* 5371 */     if (item != null) {
/*      */       
/* 5373 */       buf.append(((item.getPermissionsPlayerList().size() == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\002' + "\"};")) + "label{text=\"" + item
/*      */           
/* 5375 */           .getPermissionsPlayerList().size() + " Guests" + ((this.displaySubType == 2) ? " (Showing)" : "") + "\"};");
/*      */ 
/*      */       
/* 5378 */       buf.append((((PermissionsHistories.getPermissionsHistoryFor(item.getWurmId()).getHistoryEvents()).length == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\003' + "\"};")) + "label{text=\"" + (
/*      */           
/* 5380 */           PermissionsHistories.getPermissionsHistoryFor(item.getWurmId()).getHistoryEvents()).length + " History" + ((this.displaySubType == 3) ? " (Showing)" : "") + "\"};");
/*      */       
/* 5382 */       buf.append((((item.getItemsAsArray()).length == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\013' + "\"};")) + "label{text=\"" + (item
/*      */           
/* 5384 */           .getItemsAsArray()).length + " Items" + ((this.displaySubType == 11) ? " (Showing)" : "") + "\"};");
/*      */     }
/*      */     else {
/*      */       
/* 5388 */       buf.append("label{text=\"error\"};label{text=\"\"};");
/*      */     } 
/* 5390 */     if (Features.Feature.HIGHWAYS.isEnabled()) {
/*      */       
/* 5392 */       buf.append("radio{group=\"tid\";id=\"15,-10,19\"};label{text=\"Highway Routes" + ((this.displaySubType == 19) ? " (Showing)" : "") + "\"};");
/*      */       
/* 5394 */       buf.append("radio{group=\"tid\";id=\"15,-10,21\"};label{text=\"Highway Nodes" + ((this.displaySubType == 21) ? " (Showing)" : "") + "\"};");
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 5399 */     buf.append("}");
/*      */     
/* 5401 */     return buf.toString();
/*      */   }
/*      */   
/*      */   private String itemSummary(Item item) {
/*      */     String whenisit;
/* 5406 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 5408 */     Item lock = null;
/* 5409 */     long lockid = -10L;
/* 5410 */     int rows = 17;
/*      */     
/* 5412 */     if (item.isVillageDeed()) {
/* 5413 */       rows += 2;
/*      */     }
/* 5415 */     if (item.isLockable()) {
/*      */       
/* 5417 */       rows++;
/* 5418 */       lockid = item.getLockId();
/*      */       
/*      */       try {
/* 5421 */         lock = Items.getItem(lockid);
/*      */       }
/* 5423 */       catch (NoSuchItemException nsi) {
/*      */         
/* 5425 */         lock = null;
/*      */       } 
/*      */     } 
/*      */     
/* 5429 */     if (item.getTemplateId() == 300) {
/* 5430 */       rows++;
/*      */     }
/* 5432 */     if (item.getTemplateId() == 166) {
/* 5433 */       rows++;
/*      */     }
/* 5435 */     buf.append("table{rows=\"" + rows + "\";cols=\"4\";");
/*      */     
/* 5437 */     buf.append("label{text=\"\"};label{text=\"Wurm Id\"};label{text=\"\"};label{text=\"" + this.wurmId + "\"}");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5442 */     buf.append("label{text=\"\"};label{text=\"Name\"};label{text=\"\"};" + 
/*      */ 
/*      */         
/* 5445 */         itemNameWithColorByRarity(item));
/*      */     
/* 5447 */     buf.append("label{text=\"\"};label{text=\"QL\"};label{text=\"\"};label{text=\"" + item
/*      */ 
/*      */         
/* 5450 */         .getQualityLevel() + "\"}");
/*      */     
/* 5452 */     buf.append("label{text=\"\"};label{text=\"DMG\"};label{text=\"\"};label{text=\"" + item
/*      */ 
/*      */         
/* 5455 */         .getDamage() + "\"}");
/*      */     
/* 5457 */     buf.append("label{text=\"\"};label{text=\"Size (X,Y,Z)\"};label{text=\"\"};label{text=\"(" + item
/*      */ 
/*      */         
/* 5460 */         .getSizeX() + "," + item.getSizeY() + "," + item.getSizeZ() + ")\"}");
/*      */     
/* 5462 */     if (item.getParentId() == -10L) {
/* 5463 */       buf.append("label{text=\"\"};label{text=\"Rotation (degrees)\"};label{text=\"\"};label{text=\"" + item
/*      */ 
/*      */           
/* 5466 */           .getRotation() + "\"}");
/*      */     }
/* 5468 */     if (item.getOwnerId() != -10L) {
/*      */       
/* 5470 */       PlayerInfo playerInfo = PlayerInfoFactory.getPlayerInfoWithWurmId(item.getOwnerId());
/* 5471 */       if (playerInfo != null) {
/* 5472 */         buf.append("radio{group=\"tid\";id=\"1," + playerInfo.wurmId + "," + '\001' + "\"};label{text=\"Owner Name\"};label{text=\"\"};label{text=\"" + playerInfo
/*      */ 
/*      */             
/* 5475 */             .getName() + "\"};");
/*      */       } else {
/* 5477 */         buf.append("label{text=\"\"};label{text=\"Owner ID\"};label{text=\"\"};label{text=\"" + item
/*      */ 
/*      */             
/* 5480 */             .getOwnerId() + "\"};");
/*      */       } 
/*      */     } else {
/* 5483 */       buf.append("label{text=\"\"};label{text=\"Owner\"};label{text=\"\"};label{type=\"italic\";text=\"On Ground\"};");
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5489 */     PlayerInfo pInfo = PlayerInfoFactory.getPlayerInfoWithWurmId(item.lastOwner);
/* 5490 */     if (pInfo != null) {
/* 5491 */       buf.append("radio{group=\"tid\";id=\"1," + pInfo.wurmId + "," + '\001' + "\"};label{text=\"Last Owner Name\"};label{text=\"\"};label{text=\"" + pInfo
/*      */ 
/*      */           
/* 5494 */           .getName() + "\"};");
/* 5495 */     } else if (item.lastOwner == -10L) {
/* 5496 */       buf.append("label{text=\"\"};label{text=\"Last OwnerID\"};label{text=\"\"};label{text=\"NoOne (" + item.lastOwner + ")\"};");
/*      */ 
/*      */     
/*      */     }
/* 5500 */     else if (WurmId.getType(item.lastOwner) == 1) {
/*      */       
/* 5502 */       Wagoner wagoner = Wagoner.getWagoner(item.lastOwner);
/* 5503 */       if (wagoner != null) {
/* 5504 */         buf.append("label{text=\"\"};label{text=\"Last OwnerID\"};label{text=\"\"};label{text=\"Wagoner:" + wagoner
/*      */ 
/*      */             
/* 5507 */             .getName() + " (" + item.lastOwner + ")\"};");
/*      */       } else {
/* 5509 */         buf.append("label{text=\"\"};label{text=\"Last OwnerID\"};label{text=\"\"};label{text=\"Creature:Unknown (" + item.lastOwner + ")\"};");
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 5515 */       buf.append("label{text=\"\"};label{text=\"Last OwnerID\"};label{text=\"\"};label{text=\"Unknown (" + item.lastOwner + ")\"};");
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5521 */     int posX = (int)item.getPosX() >> 2;
/* 5522 */     int posY = (int)item.getPosY() >> 2;
/* 5523 */     boolean isOnSurface = item.isOnSurface();
/* 5524 */     long bridgeId = item.getBridgeId();
/*      */     
/* 5526 */     buf.append("radio{group=\"tid\";id=\"3," + posX + "," + posY + "," + isOnSurface + "\"};label{text=\"Coord (X,Y,surface)\"};radio{group=\"tid\";id=\"" + '\f' + "," + posX + "," + posY + "," + isOnSurface + "\"};label{text=\"(" + posX + "," + posY + "," + isOnSurface + ")\"}");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5531 */     buf.append("label{text=\"\"};label{text=\"Actual (X,Y,Z)\"};label{text=\"\"};label{text=\"(" + item
/*      */ 
/*      */         
/* 5534 */         .getPosX() + "," + item.getPosY() + "," + item.getPosZ() + ")\"}");
/*      */     
/* 5536 */     buf.append(addBridge(bridgeId));
/*      */     
/* 5538 */     if (item.isVehicle() || item.isHitchTarget()) {
/*      */ 
/*      */ 
/*      */       
/* 5542 */       Vehicle vehicle = Vehicles.getVehicle(item);
/* 5543 */       if (vehicle == null) {
/* 5544 */         buf.append("label{text=\"\"};label{text=\"Vehicle\"};label{text=\"\"};label{type=\"italic\";text=\"Not Found\"}");
/*      */       
/*      */       }
/*      */       else {
/*      */ 
/*      */         
/* 5550 */         int hitchedCount = 0;
/* 5551 */         Seat[] hitches = vehicle.getHitched();
/* 5552 */         for (Seat seat : hitches) {
/*      */           
/* 5554 */           if (seat.isOccupied()) {
/*      */             
/* 5556 */             hitchedCount++;
/* 5557 */             long occupant = seat.getOccupant();
/*      */             
/*      */             try {
/* 5560 */               Creature cret = Server.getInstance().getCreature(occupant);
/* 5561 */               buf.append("radio{group=\"tid\";id=\"1," + occupant + "," + '\001' + "\"};label{text=\"Hitched " + hitchedCount + ":\"};label{text=\"\"};label{text=\"" + cret
/*      */ 
/*      */                   
/* 5564 */                   .getName() + "\"};");
/*      */             }
/* 5566 */             catch (NoSuchPlayerException|NoSuchCreatureException e) {
/*      */               
/* 5568 */               buf.append("label{text=\"\"};label{text=\"Hitched " + hitchedCount + ":\"};label{text=\"\"};label{type=\"italic\";text=\"Not Found " + occupant + "\"};");
/*      */             } 
/*      */           } 
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/* 5575 */         if (!item.isChair()) {
/*      */           
/* 5577 */           Creature dragger = Items.getDragger(item);
/* 5578 */           if (dragger != null) {
/*      */             
/* 5580 */             buf.append("radio{group=\"tid\";id=\"1," + dragger.getWurmId() + "," + '\001' + "\"};label{text=\"Dragger:\"};label{text=\"\"};label{text=\"" + dragger
/*      */ 
/*      */                 
/* 5583 */                 .getName() + "\"};");
/*      */           }
/*      */           else {
/*      */             
/* 5587 */             buf.append("label{text=\"\"};label{text=\"Dragger:\"};label{text=\"\"};label{type=\"italic\";text=\"None\"};");
/*      */           } 
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/* 5593 */         String driver = item.isBoat() ? "Commander" : "Driver";
/* 5594 */         String passenger = item.isChair() ? "Sitter" : "Passenger";
/* 5595 */         int passengerCount = 0;
/* 5596 */         Seat[] seats = vehicle.getSeats();
/* 5597 */         for (Seat seat : seats) {
/*      */           
/* 5599 */           if (seat.type == 0) {
/*      */             
/* 5601 */             if (seat.isOccupied())
/*      */             {
/* 5603 */               long occupant = seat.getOccupant();
/*      */               
/*      */               try {
/* 5606 */                 Creature cret = Server.getInstance().getCreature(occupant);
/* 5607 */                 buf.append("radio{group=\"tid\";id=\"1," + occupant + "," + '\001' + "\"};label{text=\"" + driver + ":\"};label{text=\"\"};label{text=\"" + cret
/*      */ 
/*      */                     
/* 5610 */                     .getName() + "\"};");
/*      */               }
/* 5612 */               catch (NoSuchPlayerException|NoSuchCreatureException e) {
/*      */                 
/* 5614 */                 buf.append("label{text=\"\"};label{text=\"" + driver + ":\"};label{text=\"\"};label{type=\"italic\";text=\"Not Found (" + occupant + ")\"};");
/*      */               
/*      */               }
/*      */             
/*      */             }
/*      */             else
/*      */             {
/* 5621 */               buf.append("label{text=\"\"};label{text=\"" + driver + ":\"};label{text=\"\"};label{type=\"italic\";text=\"None\"};");
/*      */             
/*      */             }
/*      */           
/*      */           }
/* 5626 */           else if (seat.type == 1) {
/*      */             
/* 5628 */             passengerCount++;
/* 5629 */             if (seat.isOccupied())
/*      */             {
/* 5631 */               long occupant = seat.getOccupant();
/*      */               
/*      */               try {
/* 5634 */                 Creature cret = Server.getInstance().getCreature(occupant);
/* 5635 */                 buf.append("radio{group=\"tid\";id=\"1," + occupant + "," + '\001' + "\"};label{text=\"Passenger " + passengerCount + ":\"};label{text=\"\"};label{text=\"" + cret
/*      */ 
/*      */                     
/* 5638 */                     .getName() + "\"};");
/*      */               }
/* 5640 */               catch (NoSuchPlayerException|NoSuchCreatureException e) {
/*      */                 
/* 5642 */                 buf.append("label{text=\"\"};label{text=\"" + passenger + " " + passengerCount + ":\"};label{text=\"\"};label{type=\"italic\";text=\"Not Found (" + occupant + ")\"};");
/*      */               
/*      */               }
/*      */             
/*      */             }
/*      */             else
/*      */             {
/* 5649 */               buf.append("label{text=\"\"};label{text=\"" + passenger + " " + passengerCount + ":\"};label{text=\"\"};label{type=\"italic\";text=\"None\"};");
/*      */             }
/*      */           
/*      */           }
/*      */           else {
/*      */             
/* 5655 */             buf.append("label{text=\"\"};label{text=\"Unknown:\"};label{text=\"\"};label{type=\"italic\";text=\"Seat type (" + seat.type + ")\"};");
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 5663 */     buf.append("label{text=\"\"};label{text=\"Creator\"};label{text=\"\"};label{text=\"" + item.creator + "\"}");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5668 */     buf.append("label{text=\"\"};label{text=\"Description\"};label{text=\"\"};label{text=\"" + item
/*      */ 
/*      */         
/* 5671 */         .getDescription() + "\"}");
/*      */     
/* 5673 */     buf.append("label{text=\"\"};label{text=\"Is busy?\"};label{text=\"\"};label{" + 
/*      */ 
/*      */         
/* 5676 */         showBoolean(item.isBusy()) + "}");
/*      */     
/* 5678 */     buf.append("label{text=\"\"};label{text=\"Material\"};label{text=\"\"};label{text=\"" + 
/*      */ 
/*      */         
/* 5681 */         Materials.convertMaterialByteIntoString(item.getMaterial()) + " (" + item.getMaterial() + ")\"}");
/*      */     
/* 5683 */     buf.append("label{text=\"\"};label{text=\"Item Template\"};label{text=\"\"};label{text=\"" + item
/*      */ 
/*      */         
/* 5686 */         .getTemplate().getName() + " (" + item.getTemplateId() + ")\"}");
/*      */     
/* 5688 */     int rt = item.getRealTemplateId();
/*      */     
/*      */     try {
/* 5691 */       ItemTemplate realTemplate = ItemTemplateFactory.getInstance().getTemplate(rt);
/* 5692 */       buf.append("label{text=\"\"};label{text=\"Real Template\"};label{text=\"\"};label{text=\"" + realTemplate
/*      */ 
/*      */           
/* 5695 */           .getName() + " (" + rt + ")\"}");
/*      */     }
/* 5697 */     catch (NoSuchTemplateException e1) {
/*      */ 
/*      */       
/* 5700 */       buf.append("label{text=\"\"};label{text=\"Real Template\"};label{text=\"\"};label{type=\"italics\";text=\"" + ((rt == -10) ? "None" : "Unknown") + " (" + rt + ")\"}");
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5706 */     buf.append("label{text=\"\"};label{text=\"Data\"};label{text=\"\"};label{" + (
/*      */ 
/*      */         
/* 5709 */         item.hasData() ? "" : "color=\"255,127,127\"") + "text=\"" + item.getData1() + "," + item.getData2() + " (" + item.getData() + ")\"}");
/*      */     
/* 5711 */     buf.append("label{text=\"\"};label{text=\"Extra\"};label{text=\"\"};label{" + (
/*      */ 
/*      */         
/* 5714 */         item.hasData() ? (item.hasExtraData() ? "" : "color=\"255,177,40\"") : "color=\"255,127,127\"") + "text=\"" + item.getExtra1() + "," + item.getExtra2() + " (" + item.getExtra() + ")\"}");
/*      */     
/* 5716 */     if (item.usesFoodState()) {
/*      */       
/* 5718 */       buf.append("label{text=\"\"};label{text=\"Food state\"};label{text=\"\"};label{type=\"italics\";text=\"" + item
/*      */ 
/*      */           
/* 5721 */           .getFoodAuxByteName(item.getTemplate(), true, true) + " (" + item
/* 5722 */           .getAuxData() + ")\"}");
/*      */     }
/* 5724 */     else if (item.isRoadMarker()) {
/*      */       
/* 5726 */       buf.append("label{text=\"\"};label{text=\"Links (AuxByte)\"};label{text=\"\"};label{type=\"italics\";text=\"" + 
/*      */ 
/*      */           
/* 5729 */           MethodsHighways.getLinkAsString(item.getAuxData()) + " (" + item
/* 5730 */           .getAuxData() + ")\"}");
/*      */     }
/*      */     else {
/*      */       
/* 5734 */       buf.append("label{text=\"\"};label{text=\"AuxByte\"};label{text=\"\"};label{type=\"italics\";text=\"" + item
/*      */ 
/*      */           
/* 5737 */           .getActualAuxData() + "\"}");
/*      */     } 
/* 5739 */     if (item.getBless() == null) {
/*      */       
/* 5741 */       buf.append("label{text=\"\"};label{text=\"Bless\"};label{text=\"\"};label{type=\"italics\";text=\"none\"}");
/*      */ 
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */       
/* 5748 */       buf.append("label{text=\"\"};label{text=\"Bless\"};label{text=\"\"};label{text=\"" + item
/*      */ 
/*      */           
/* 5751 */           .getBless().getName() + "\"}");
/*      */     } 
/*      */     
/* 5754 */     if (item.getColor() == -1) {
/* 5755 */       buf.append("label{text=\"\"};label{text=\"Colour\"};label{text=\"\"};label{text=\"not initialized (-1)\"}");
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 5760 */       buf.append("label{text=\"\"};label{text=\"Colour\"};label{text=\"\"};label{text=\"" + 
/*      */ 
/*      */           
/* 5763 */           WurmColor.getRGBDescription(item.getColor()) + " (" + item.getColor() + ")\"}");
/*      */     } 
/* 5765 */     if (item.supportsSecondryColor())
/*      */     {
/* 5767 */       if (item.getColor2() == -1) {
/* 5768 */         buf.append("label{text=\"\"};label{text=\"Secondary Colour\"};label{text=\"\"};label{text=\"not initialized (-1)\"}");
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 5773 */         buf.append("label{text=\"\"};label{text=\"Secondary Colour\"};label{text=\"\"};label{text=\"" + 
/*      */ 
/*      */             
/* 5776 */             WurmColor.getRGBDescription(item.getColor2()) + " (" + item.getColor2() + ")\"}");
/*      */       } 
/*      */     }
/* 5779 */     Item parent = null;
/*      */     
/*      */     try {
/* 5782 */       parent = item.getParent();
/*      */     }
/* 5784 */     catch (NoSuchItemException noSuchItemException) {}
/*      */ 
/*      */ 
/*      */     
/* 5788 */     if (parent != null) {
/* 5789 */       buf.append("radio{group=\"tid\";id=\"1," + item.getParentId() + "," + '\001' + "\"};label{text=\"Parent\"};label{text=\"\"};label{text=\"" + parent
/*      */ 
/*      */           
/* 5792 */           .getName() + "\"};");
/*      */     } else {
/* 5794 */       buf.append("label{text=\"\"};label{text=\"Parent\"};label{text=\"\"};label{type=\"italic\";text=\"no parent (" + item
/*      */ 
/*      */           
/* 5797 */           .getParentId() + ")\"};");
/*      */     } 
/*      */ 
/*      */     
/* 5801 */     String tempString = "";
/* 5802 */     switch (item.getTemperatureState(item.getTemperature())) {
/*      */       
/*      */       case -1:
/* 5805 */         tempString = "Frozen";
/*      */         break;
/*      */       case 1:
/* 5808 */         tempString = "Very warm";
/*      */         break;
/*      */       case 2:
/* 5811 */         tempString = "Hot";
/*      */         break;
/*      */       case 3:
/* 5814 */         tempString = "Boiling";
/*      */         break;
/*      */       case 4:
/* 5817 */         tempString = "Searing";
/*      */         break;
/*      */       case 5:
/* 5820 */         tempString = "Glowing";
/*      */         break;
/*      */       case 0:
/* 5823 */         tempString = "Room temperature";
/*      */         break;
/*      */       
/*      */       default:
/* 5827 */         tempString = "Unknown state";
/*      */         break;
/*      */     } 
/* 5830 */     buf.append("label{text=\"\"};label{text=\"Temperature\"};label{text=\"\"};label{text=\"" + tempString + " (" + (item
/*      */ 
/*      */         
/* 5833 */         .getTemperature() / 10.0F) + "C)\"}");
/*      */     
/* 5835 */     buf.append("label{text=\"\"};label{text=\"Weight (Grams)\"};label{text=\"\"};label{text=\"" + item
/*      */ 
/*      */         
/* 5838 */         .getWeightGrams() + "\"}");
/*      */     
/* 5840 */     if (item.isFood() || item.isLiquid()) {
/*      */       
/* 5842 */       buf.append("label{text=\"\"};label{text=\"CCFP Kg Values\"};label{text=\"\"};label{text=\"" + item
/*      */ 
/*      */           
/* 5845 */           .getCalories() + ", " + item
/* 5846 */           .getCarbs() + ", " + item
/* 5847 */           .getFats() + ", " + item
/* 5848 */           .getProteins() + "\"};");
/*      */       
/* 5850 */       buf.append("label{text=\"\"};label{text=\"CCFP by Weight\"};label{text=\"\"};label{text=\"" + 
/*      */ 
/*      */           
/* 5853 */           (int)item.getCaloriesByWeight() + ", " + 
/* 5854 */           (int)item.getCarbsByWeight() + ", " + 
/* 5855 */           (int)item.getFatsByWeight() + ", " + 
/* 5856 */           (int)item.getProteinsByWeight() + "\"};");
/*      */     } 
/*      */     
/* 5859 */     if (item.isVillageDeed()) {
/*      */       
/*      */       try {
/*      */         
/* 5863 */         Village aVillage = Villages.getVillage(item.getData2());
/* 5864 */         buf.append("radio{group=\"tid\";id=\"2," + aVillage.getId() + "," + '\001' + "\"};label{text=\"Village Name\"};label{text=\"\"};label{text=\"" + aVillage
/*      */ 
/*      */             
/* 5867 */             .getName() + "\"};");
/*      */         
/* 5869 */         buf.append("label{text=\"\"};label{text=\"Village Motto\"};label{text=\"\"};label{text=\"" + aVillage
/*      */ 
/*      */             
/* 5872 */             .getMotto() + "\"};");
/*      */       }
/* 5874 */       catch (NoSuchVillageException e) {
/*      */         
/* 5876 */         buf.append("label{text=\"\"};label{text=\"Village Name\"};label{text=\"\"};label{type=\"italic\";text=\"not found\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 5881 */         buf.append("label{text=\"\"};label{text=\"Village Motto\"};label{text=\"\"};label{type=\"italic\";text=\"not found\"};");
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 5887 */     long wurmcurrent = WurmCalendar.currentTime;
/* 5888 */     long wurmdiff = wurmcurrent - item.getLastMaintained();
/*      */     
/* 5890 */     if (wurmdiff < 0L) {
/* 5891 */       whenisit = " (Approx real time until that date: " + Server.getTimeFor(-wurmdiff / 8L) + ")";
/*      */     } else {
/* 5893 */       whenisit = " (Approx real time since that date: " + Server.getTimeFor(wurmdiff / 8L) + ")";
/*      */     } 
/* 5895 */     buf.append("label{text=\"\"};label{text=\"Last Maintained\"};label{text=\"\"};label{text=\"" + item
/*      */ 
/*      */         
/* 5898 */         .getLastMaintained() + whenisit + "\"};");
/*      */     
/* 5900 */     if (item.isLockable())
/*      */     {
/* 5902 */       if (lockid != -10L) {
/*      */         
/* 5904 */         if (lock == null) {
/* 5905 */           buf.append("radio{group=\"tid\";id=\"1," + lockid + "," + '\001' + "\"};label{text=\"Weird.\"};label{text=\"\"};label{text=\"Lock id exists, but not the lock!\"}");
/*      */ 
/*      */         
/*      */         }
/* 5909 */         else if (lock.isLocked()) {
/* 5910 */           buf.append("radio{group=\"tid\";id=\"1," + lock.getWurmId() + "," + '\001' + "\"};label{text=\"" + lock
/* 5911 */               .getName() + "\"};label{text=\"\"};label{text=\"Locked\"}");
/*      */         }
/*      */         else {
/*      */           
/* 5915 */           buf.append("radio{group=\"tid\";id=\"1," + lock.getWurmId() + "," + '\001' + "\"};label{text=\"" + lock
/* 5916 */               .getName() + "\"};label{text=\"\"};label{text=\"Unlocked\"}");
/*      */         }
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 5922 */         buf.append("label{text=\"\"};label{text=\"Gate\"};label{text=\"\"};label{text=\"No Lock\"}");
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5929 */     if (item.getTemplateId() == 300) {
/*      */       
/*      */       try {
/*      */         
/* 5933 */         Creature merchant = null;
/*      */         
/* 5935 */         Shop shop = null;
/* 5936 */         long merchantId = -1L;
/*      */         
/* 5938 */         merchantId = item.getData();
/* 5939 */         if (merchantId != -1L) {
/*      */           
/* 5941 */           merchant = Server.getInstance().getCreature(merchantId);
/* 5942 */           if (merchant.isNpcTrader()) {
/* 5943 */             shop = Economy.getEconomy().getShop(merchant);
/*      */           }
/* 5945 */           buf.append("radio{group=\"tid\";id=\"1," + merchantId + "," + '\001' + "\"};label{text=\"Merchant\"};label{text=\"\"};label{text=\"" + merchant
/*      */ 
/*      */               
/* 5948 */               .getName() + "\"}");
/*      */         } else {
/*      */           
/* 5951 */           buf.append("label{text=\"\"};label{text=\"Merchant\"};label{text=\"\"};label{type=\"italic\";text=\"None.\"}");
/*      */         
/*      */         }
/*      */       
/*      */       }
/* 5956 */       catch (NoSuchPlayerException e) {
/*      */         
/* 5958 */         buf.append("label{text=\"\"};label{text=\"Merchant\"};label{text=\"\"};label{type=\"italic\";text=\"is a player? Well it can't be found.\"}");
/*      */ 
/*      */ 
/*      */       
/*      */       }
/* 5963 */       catch (NoSuchCreatureException e) {
/*      */         
/* 5965 */         buf.append("label{text=\"\"};label{text=\"Merchant\"};label{text=\"\"};label{type=\"italic\";text=\"can't be found.\"}");
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5972 */     if (item.getTemplateId() == 166) {
/*      */       
/*      */       try {
/*      */         
/* 5976 */         Structure building = Structures.getStructureForWrit(item.getWurmId());
/* 5977 */         buf.append("radio{group=\"tid\";id=\"1," + building.getWurmId() + "," + '\001' + "\"};label{text=\"Building\"};label{text=\"\"};label{text=\"" + building
/*      */ 
/*      */             
/* 5980 */             .getName() + "\"}");
/*      */       }
/* 5982 */       catch (NoSuchStructureException e) {
/*      */ 
/*      */         
/* 5985 */         buf.append("label{text=\"\"};label{text=\"Building\"};label{text=\"\"};label{type=\"italic\";text=\"can't be found.\"}");
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5992 */     if (item.mailed) {
/*      */       
/* 5994 */       buf.append("label{text=\"\"};label{text=\"Mailed\"};label{text=\"\"};label{text=\"Yes\"}");
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 5999 */       WurmMail wm = WurmMail.getWurmMailForItem(item.getWurmId());
/* 6000 */       if (wm != null) {
/*      */         
/* 6002 */         ServerEntry se = Servers.getServerWithId(wm.getSourceserver());
/* 6003 */         String sourceServer = " (Unk)";
/* 6004 */         if (se != null) {
/* 6005 */           sourceServer = " (" + se.getAbbreviation() + ")";
/*      */         }
/* 6007 */         long sender = wm.getSender();
/* 6008 */         if (sender == -10L) {
/* 6009 */           buf.append("label{text=\"\"};label{text=\" Sender\"};label{text=\"\"};label{type=\"italic\";text=\"NOID\"}");
/*      */ 
/*      */         
/*      */         }
/*      */         else {
/*      */ 
/*      */           
/* 6016 */           PlayerState ps = PlayerInfoFactory.getPlayerState(sender);
/* 6017 */           if (ps == null) {
/* 6018 */             buf.append("label{text=\"\"};label{text=\" Sender\"};label{text=\"\"};label{type=\"italic\";text=\"" + sender + "?" + sourceServer + "\"}");
/*      */           
/*      */           }
/*      */           else {
/*      */             
/* 6023 */             buf.append("radio{group=\"tid\";id=\"1," + sender + "," + '\001' + "\"};label{text=\" Sender\"};label{text=\"\"};label{text=\"" + ps
/*      */ 
/*      */                 
/* 6026 */                 .getPlayerName() + sourceServer + "\"}");
/*      */           } 
/* 6028 */         }  long receiver = wm.getReceiver();
/* 6029 */         if (receiver == -10L) {
/* 6030 */           buf.append("label{text=\"\"};label{text=\" Receiver\"};label{text=\"\"};label{type=\"italic\";text=\"NOID\"}");
/*      */ 
/*      */         
/*      */         }
/*      */         else {
/*      */ 
/*      */           
/* 6037 */           PlayerState ps = PlayerInfoFactory.getPlayerState(receiver);
/* 6038 */           if (ps == null) {
/* 6039 */             buf.append("label{text=\"\"};label{text=\" Receiver\"};label{text=\"\"};label{type=\"italic\";text=\"" + receiver + "?\"}");
/*      */           
/*      */           }
/*      */           else {
/*      */             
/* 6044 */             buf.append("radio{group=\"tid\";id=\"1," + receiver + "," + '\001' + "\"};label{text=\" Receiver\"};label{text=\"\"};label{text=\"" + ps
/*      */ 
/*      */                 
/* 6047 */                 .getPlayerName() + "\"}");
/*      */           } 
/* 6049 */         }  buf.append("label{text=\"\"};label{text=\" Expires\"};label{text=\"\"};label{text=\"" + new Date(wm
/*      */ 
/*      */               
/* 6052 */               .getExpiration()) + "\"}");
/*      */         
/* 6054 */         buf.append("label{text=\"\"};label{text=\" Price\"};label{text=\"\"};label{text=\"" + (
/*      */ 
/*      */             
/* 6057 */             (wm.getType() == 0) ? "" : "COD ") + (new Change(wm
/* 6058 */               .getPrice())).getChangeShortString() + "\"}");
/*      */         
/* 6060 */         buf.append("label{text=\"\"};label{text=\" Rejected\"};label{text=\"\"};label{text=\"" + wm
/*      */ 
/*      */             
/* 6063 */             .isRejected() + "\"}");
/*      */       } else {
/*      */         
/* 6066 */         buf.append("label{text=\"\"};label{text=\"Mailed\"};label{text=\"\"};label{type=\"italic\";text=\"Mail not found\"}");
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 6072 */       buf.append("label{text=\"\"};label{text=\"Mailed\"};label{text=\"\"};label{text=\"No\"}");
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 6077 */     if (item.isLock()) {
/*      */       
/* 6079 */       boolean found = false;
/* 6080 */       if (item.getTemplateId() == 252) {
/*      */         
/* 6082 */         buf.append("label{text=\"\"};label{text=\"Attached To\"};label{text=\"\"};label{text=\"gate? TODO find gate\"};");
/*      */ 
/*      */ 
/*      */         
/* 6086 */         found = true;
/*      */       }
/* 6088 */       else if (item.getTemplateId() == 167) {
/*      */         
/* 6090 */         buf.append("label{text=\"\"};label{text=\"Attached To\"};label{text=\"\"};label{text=\"door? TODO find door\"};");
/*      */ 
/*      */ 
/*      */         
/* 6094 */         found = true;
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 6099 */         for (Item litem : Items.getAllItems()) {
/*      */           
/* 6101 */           if (litem.getLockId() == item.getWurmId() && !litem.isKey()) {
/*      */             
/* 6103 */             buf.append("radio{group=\"tid\";id=\"1," + litem.getWurmId() + "," + '\001' + "\"};label{text=\"Attached To\"};label{text=\"\"};" + 
/*      */ 
/*      */                 
/* 6106 */                 itemNameWithColorByRarity(litem));
/* 6107 */             found = true;
/*      */             break;
/*      */           } 
/*      */         } 
/*      */       } 
/* 6112 */       if (!found)
/*      */       {
/* 6114 */         buf.append("label{text=\"\"};label{text=\"Attached To\"};label{text=\"\"};label{text=\"nothing!\"};");
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 6120 */     if (item.isBed()) {
/*      */       
/* 6122 */       PlayerInfo pinf = PlayerInfoFactory.getPlayerSleepingInBed(item.getWurmId());
/* 6123 */       if (pinf != null) {
/*      */         
/* 6125 */         buf.append("radio{group=\"tid\";id=\"1," + pinf.wurmId + "," + '\001' + "\"};label{text=\"In Use by\"};label{text=\"\"};label{text=\"" + pinf
/*      */ 
/*      */             
/* 6128 */             .getName() + "\"};");
/*      */       }
/*      */       else {
/*      */         
/* 6132 */         buf.append("label{text=\"\"};label{text=\"Bed In Use by\"};label{text=\"\"};label{type=\"italics\";text=\"Noone!\"};");
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 6137 */       if (item.getWhenRented() != 0L) {
/*      */         
/* 6139 */         buf.append("label{text=\"\"};label{text=\"Bed When Rented\"};label{text=\"\"};label{text=\"" + new Date(item
/*      */ 
/*      */               
/* 6142 */               .getWhenRented()) + "\"};");
/* 6143 */         long pid = item.getData();
/* 6144 */         if (pid > 0L)
/*      */         {
/*      */           
/* 6147 */           buf.append("radio{group=\"tid\";id=\"1," + pid + "," + '\001' + "\"};label{text=\"Bed Rented by\"};label{text=\"\"};label{text=\"" + 
/*      */ 
/*      */               
/* 6150 */               PlayerInfoFactory.getPlayerName(pid) + "\"};");
/*      */         }
/*      */       } 
/*      */     } 
/* 6154 */     if (item.isRoadMarker()) {
/*      */       
/* 6156 */       Village inVillage = Villages.getVillage(posX, posY, true);
/* 6157 */       if (inVillage != null) {
/*      */         
/* 6159 */         buf.append("radio{group=\"tid\";id=\"2," + inVillage.getId() + "," + '\001' + "\"};label{text=\"In Village\"};label{text=\"\"};label{text=\"" + inVillage
/*      */ 
/*      */             
/* 6162 */             .getName() + "\"};");
/*      */       }
/*      */       else {
/*      */         
/* 6166 */         Village lVillage = Villages.getVillageFor(item);
/* 6167 */         if (lVillage != null) {
/*      */           
/* 6169 */           buf.append("radio{group=\"tid\";id=\"2," + lVillage.getId() + "," + '\001' + "\"};label{text=\"Village Border\";hover=\"In first 2 tiles of perimeter, where permissions apply.\"};label{text=\"\"};label{text=\"" + lVillage
/*      */ 
/*      */               
/* 6172 */               .getName() + "\"};");
/*      */         }
/*      */         else {
/*      */           
/* 6176 */           buf.append("label{text=\"\"};label{text=\"in Village\"};label{text=\"\"};label{text=\"none\"};");
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 6182 */       if (item.getTemplateId() == 1112) {
/*      */         
/* 6184 */         Node node = Routes.getNode(item);
/* 6185 */         if (node.getVillage() == null) {
/* 6186 */           buf.append("label{text=\"\"};label{text=\"Route Village\"};label{text=\"\"};label{text=\"none\"};");
/*      */         
/*      */         }
/*      */         else {
/*      */           
/* 6191 */           buf.append("radio{group=\"tid\";id=\"2," + node.getVillage().getId() + "," + '\001' + "\"};label{text=\"Route Village\";hover=\"Should be same as In Village above.\"};label{text=\"\"};label{text=\"" + node
/*      */ 
/*      */               
/* 6194 */               .getVillage().getName() + "\"};");
/*      */         } 
/*      */       } 
/* 6197 */     }  if (item.getTemplateId() == 272) {
/*      */ 
/*      */       
/* 6200 */       long wb = item.getWasBrandedTo();
/* 6201 */       Village wasBrandedByVilage = null;
/*      */       
/* 6203 */       if (wb != -10L) {
/*      */         
/*      */         try {
/*      */           
/* 6207 */           wasBrandedByVilage = Villages.getVillage((int)wb);
/*      */         }
/* 6209 */         catch (NoSuchVillageException e) {
/*      */           
/* 6211 */           wb = -10L;
/*      */         } 
/*      */       }
/* 6214 */       if (wb == -10L) {
/*      */         
/* 6216 */         buf.append("label{text=\"\"};label{text=\"Was Branded By\";hover=\"will be reset if there is a server reboot\"};label{text=\"\"};label{text=\"none\"};");
/*      */ 
/*      */       
/*      */       }
/*      */       else {
/*      */ 
/*      */         
/* 6223 */         buf.append("radio{group=\"tid\";id=\"2," + wasBrandedByVilage.getId() + "," + '\001' + "\"};label{text=\"Was Branded By\";hover=\"will be reset if there is a server reboot\"};label{text=\"\"};label{text=\"" + wasBrandedByVilage
/*      */ 
/*      */             
/* 6226 */             .getName() + "\"};");
/*      */       } 
/*      */     } 
/* 6229 */     buf.append("}");
/*      */     
/* 6231 */     if (lock != null)
/*      */     {
/* 6233 */       buf.append(keysTable(lock));
/*      */     }
/*      */     
/* 6236 */     if (item.isLock())
/*      */     {
/* 6238 */       buf.append(keysTable(item));
/*      */     }
/*      */     
/* 6241 */     if (item.isKey()) {
/*      */       
/*      */       try {
/*      */         
/* 6245 */         Item key = Items.getItem(item.getLockId());
/* 6246 */         buf.append("label{type=\"bold\";text=\"Associated Lock:\"}");
/* 6247 */         buf.append("table{rows=\"1\";cols=\"2\";");
/* 6248 */         buf.append("radio{group=\"tid\";id=\"1," + key.getWurmId() + "," + '\001' + "\"};" + 
/* 6249 */             itemNameWithColorByRarity(key));
/* 6250 */         buf.append("}");
/*      */       }
/* 6252 */       catch (NoSuchItemException noSuchItemException) {}
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 6258 */     buf.append("label{text=\"Creation date: " + WurmCalendar.getTimeFor(item.creationDate) + "\"}");
/* 6259 */     buf.append("label{type=\"bold\";text=\"Examine text:\"};" + item.getExamineAsBml(getResponder()));
/*      */     
/* 6261 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String containerItems(Item item) {
/* 6266 */     StringBuilder buf = new StringBuilder();
/*      */ 
/*      */     
/*      */     try {
/* 6270 */       Item[] litems = item.getItemsAsArray();
/* 6271 */       if (litems.length > 0) {
/* 6272 */         buf.append(itemsTable(litems));
/*      */       } else {
/* 6274 */         buf.append("label{text=\"No Items found\"}");
/*      */       } 
/* 6276 */     } catch (Exception e1) {
/*      */       
/* 6278 */       logger.log(Level.WARNING, e1.getMessage(), e1);
/* 6279 */       buf.append("label{text=\"Error reading Items\"}");
/* 6280 */       buf.append("label{text=\"Exception:\"};");
/* 6281 */       buf.append("label{text=\"" + e1.getMessage() + "\"};");
/*      */     } 
/* 6283 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String mailItems(WurmMail[] mail) {
/* 6288 */     StringBuilder buf = new StringBuilder();
/* 6289 */     if (mail.length > 0) {
/*      */       
/* 6291 */       Arrays.sort((Object[])mail);
/* 6292 */       buf.append(mailTable(mail));
/*      */     } else {
/*      */       
/* 6295 */       buf.append("label{text=\"No Mail Items found\"}");
/* 6296 */     }  return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String wallDetails() {
/* 6301 */     StringBuilder buf = new StringBuilder();
/* 6302 */     Wall wall = Wall.getWall(this.wurmId);
/* 6303 */     if (wall != null) {
/*      */       
/* 6305 */       Door door = wall.getDoor();
/* 6306 */       if (door != null && this.displaySubType == 2) {
/* 6307 */         buf.append(showGuests(DoorSettings.DoorPermissions.getPermissions(), door.getPermissionsPlayerList()));
/* 6308 */       } else if (door != null && this.displaySubType == 3) {
/* 6309 */         buf.append(showHistory(door.getWurmId()));
/*      */       } else {
/* 6311 */         buf.append(wallSummary(wall));
/*      */       } 
/* 6313 */       buf.append("label{type=\"bold\";text=\"------------------------------ Links --------------------------------------\"}");
/* 6314 */       int rows = 1;
/* 6315 */       buf.append("table{rows=\"1\";cols=\"4\";");
/* 6316 */       buf.append("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\001' + "\"};label{text=\"Summary" + ((this.displaySubType == 1) ? " (Showing)" : "") + "\"};");
/*      */       
/* 6318 */       if (door != null) {
/*      */         
/* 6320 */         buf.append(((door.getPermissionsPlayerList().size() == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\002' + "\"};")) + "label{text=\"" + door
/*      */             
/* 6322 */             .getPermissionsPlayerList().size() + " Guests" + ((this.displaySubType == 2) ? " (Showing)" : "") + "\"};");
/*      */         
/* 6324 */         buf.append((((PermissionsHistories.getPermissionsHistoryFor(door.getWurmId()).getHistoryEvents()).length == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\003' + "\"};")) + "label{text=\"" + (
/*      */             
/* 6326 */             PermissionsHistories.getPermissionsHistoryFor(door.getWurmId()).getHistoryEvents()).length + " History" + ((this.displaySubType == 3) ? " (Showing)" : "") + "\"};");
/*      */       } 
/*      */       
/* 6329 */       buf.append("label{text=\"\"};label{text=\"\"};");
/* 6330 */       buf.append("}");
/*      */     } else {
/*      */       
/* 6333 */       buf.append("label{text=\"no wall found with that Id\"}");
/*      */     } 
/* 6335 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String wallSummary(Wall wall) {
/* 6340 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 6342 */     buf.append("table{rows=\"9\";cols=\"4\";");
/*      */     
/* 6344 */     buf.append("label{text=\"\"};label{text=\"Wurm Id\"};label{text=\"\"};label{text=\"" + this.wurmId + "\"}");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 6349 */     buf.append("label{text=\"\"};label{text=\"Name\"};label{text=\"\"};label{text=\"" + wall
/*      */ 
/*      */         
/* 6352 */         .getName() + "\"}");
/*      */     
/* 6354 */     buf.append("label{text=\"\"};label{text=\"Building Id\"};label{text=\"\"};label{text=\"" + wall
/*      */ 
/*      */         
/* 6357 */         .getStructureId() + "\"}");
/*      */ 
/*      */     
/*      */     try {
/* 6361 */       buf.append("radio{group=\"tid\";id=\"1," + wall.getStructureId() + "," + '\001' + "\"};label{text=\"Building Name\"};label{text=\"\"};label{text=\"" + 
/*      */ 
/*      */           
/* 6364 */           Structures.getStructure(wall.getStructureId()).getName() + "\"}");
/*      */     }
/* 6366 */     catch (Exception e) {
/*      */       
/* 6368 */       buf.append("label{text=\"\"};label{text=\"Building Name\"};label{text=\"\"};label{type=\"italic\";text=\"Building not found!\"}");
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 6374 */     buf.append("label{text=\"\"};label{text=\"Last Maintained\"};label{text=\"\"};label{text=\"" + new Date(wall
/*      */ 
/*      */           
/* 6377 */           .getLastUsed()) + "\"};");
/*      */     
/* 6379 */     int wx = wall.getTileX();
/* 6380 */     int wy = wall.getTileY();
/* 6381 */     boolean ws = (wall.getLayer() >= 0);
/* 6382 */     buf.append("radio{group=\"tid\";id=\"3," + wx + "," + wy + "," + ws + "\"};label{text=\"Coords (X,Y,surface)\"};radio{group=\"tid\";id=\"" + '\f' + "," + wx + "," + wy + "," + ws + "\"};label{text=\"(" + wx + "," + wy + "," + ws + ")\"}");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 6387 */     buf.append("label{text=\"\"};label{text=\"Material\"};label{text=\"\"};label{text=\"" + wall
/*      */ 
/*      */         
/* 6390 */         .getMaterialString() + "\"}");
/*      */     
/* 6392 */     buf.append("label{text=\"\"};label{text=\"QL\"};label{text=\"\"};label{text=\"" + wall
/*      */ 
/*      */         
/* 6395 */         .getQualityLevel() + "\"}");
/*      */     
/* 6397 */     buf.append("label{text=\"\"};label{text=\"DMG\"};label{text=\"\"};label{text=\"" + wall
/*      */ 
/*      */         
/* 6400 */         .getDamage() + "\"}");
/*      */     
/* 6402 */     buf.append("label{text=\"\"};label{text=\"Indoors\"};label{text=\"\"};label{text=\"" + wall
/*      */ 
/*      */         
/* 6405 */         .isIndoor() + "\"}");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 6418 */     buf.append("label{text=\"\"};label{text=\"Floor Level\"};label{text=\"\"};label{text=\"" + wall
/*      */ 
/*      */         
/* 6421 */         .getFloorLevel() + "\"}");
/*      */     
/* 6423 */     buf.append("label{text=\"\"};label{text=\"Height Offset\"};label{text=\"\"};label{text=\"" + wall
/*      */ 
/*      */         
/* 6426 */         .getHeight() + "\"}");
/*      */     
/* 6428 */     buf.append("}");
/* 6429 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String fenceDetails() {
/* 6434 */     StringBuilder buf = new StringBuilder();
/* 6435 */     Fence fence = Fence.getFence(this.wurmId);
/* 6436 */     if (fence != null) {
/*      */       
/* 6438 */       FenceGate gate = null;
/* 6439 */       if (fence.isDoor())
/* 6440 */         gate = FenceGate.getFenceGate(this.wurmId); 
/* 6441 */       if (gate != null && this.displaySubType == 2) {
/* 6442 */         buf.append(showGuests(DoorSettings.GatePermissions.getPermissions(), gate.getPermissionsPlayerList()));
/* 6443 */       } else if (gate != null && this.displaySubType == 3) {
/* 6444 */         buf.append(showHistory(gate.getWurmId()));
/*      */       } else {
/* 6446 */         buf.append(fenceSummary(fence));
/* 6447 */       }  buf.append("label{type=\"bold\";text=\"------------------------------ Links --------------------------------------\"}");
/* 6448 */       int rows = 1;
/* 6449 */       buf.append("table{rows=\"1\";cols=\"4\";");
/* 6450 */       buf.append("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\001' + "\"};label{text=\"Summary" + ((this.displaySubType == 1) ? " (Showing)" : "") + "\"};");
/*      */       
/* 6452 */       if (gate != null) {
/*      */         
/* 6454 */         buf.append(((gate.getPermissionsPlayerList().size() == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\002' + "\"};")) + "label{text=\"" + gate
/*      */             
/* 6456 */             .getPermissionsPlayerList().size() + " Guests" + ((this.displaySubType == 2) ? " (Showing)" : "") + "\"};");
/*      */         
/* 6458 */         buf.append((((PermissionsHistories.getPermissionsHistoryFor(gate.getWurmId()).getHistoryEvents()).length == 0) ? "label{text=\"\"};" : ("radio{group=\"tid\";id=\"1," + this.wurmId + "," + '\003' + "\"};")) + "label{text=\"" + (
/*      */             
/* 6460 */             PermissionsHistories.getPermissionsHistoryFor(gate.getWurmId()).getHistoryEvents()).length + " History" + ((this.displaySubType == 3) ? " (Showing)" : "") + "\"};");
/*      */       } 
/*      */       
/* 6463 */       buf.append("label{text=\"\"};label{text=\"\"};");
/* 6464 */       buf.append("}");
/*      */     } else {
/*      */       
/* 6467 */       buf.append("label{text=\"no fence found with that Id\"}");
/* 6468 */     }  return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String fenceSummary(Fence fence) {
/* 6473 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 6475 */     FenceGate gate = null;
/* 6476 */     Item lock = null;
/* 6477 */     long lockid = -10L;
/* 6478 */     int rows = 7;
/*      */     
/* 6480 */     if (fence.isDoor()) {
/*      */       
/* 6482 */       gate = FenceGate.getFenceGate(fence.getId());
/* 6483 */       if (gate != null) {
/*      */         
/*      */         try {
/*      */           
/* 6487 */           rows++;
/* 6488 */           lockid = gate.getLockId();
/*      */           
/*      */           try {
/* 6491 */             lock = Items.getItem(lockid);
/*      */           }
/* 6493 */           catch (NoSuchItemException nsi) {
/*      */             
/* 6495 */             logger.log(Level.WARNING, "Weird. Lock id exists, but not the item.", (Throwable)nsi);
/*      */           }
/*      */         
/* 6498 */         } catch (NoSuchLockException noSuchLockException) {}
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 6505 */     buf.append("table{rows=\"" + rows + "\";cols=\"4\";");
/*      */     
/* 6507 */     buf.append("label{text=\"\"};label{text=\"Wurm Id\"};label{text=\"\"};label{text=\"" + this.wurmId + "\"}");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 6512 */     buf.append("label{text=\"\"};label{text=\"Name\"};label{text=\"\"};label{text=\"" + fence
/*      */ 
/*      */         
/* 6515 */         .getName() + "\"}");
/*      */     
/* 6517 */     buf.append("label{text=\"\"};label{text=\"On Border\"};label{text=\"\"};label{text=\"" + (
/*      */ 
/*      */         
/* 6520 */         fence.isHorizontal() ? "North" : "West") + "\"}");
/*      */     
/* 6522 */     int fx = fence.getTileX();
/* 6523 */     int fy = fence.getTileY();
/* 6524 */     boolean fs = (fence.getLayer() >= 0);
/* 6525 */     buf.append("radio{group=\"tid\";id=\"3," + fx + "," + fy + "," + fs + "\"};label{text=\"Coords (X,Y,surface)\"};radio{group=\"tid\";id=\"" + '\f' + "," + fx + "," + fy + "," + fs + "\"};label{text=\"(" + fx + "," + fy + "," + fs + ")\"}");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 6530 */     buf.append("label{text=\"\"};label{text=\"Finished?\"};label{text=\"\"};label{text=\"" + fence
/*      */ 
/*      */         
/* 6533 */         .isFinished() + "\"}");
/*      */     
/* 6535 */     buf.append("label{text=\"\"};label{text=\"QL\"};label{text=\"\"};label{text=\"" + fence
/*      */ 
/*      */         
/* 6538 */         .getQualityLevel() + "\"}");
/*      */     
/* 6540 */     buf.append("label{text=\"\"};label{text=\"DMG\"};label{text=\"\"};label{text=\"" + fence
/*      */ 
/*      */         
/* 6543 */         .getDamage() + "\"}");
/*      */     
/* 6545 */     buf.append("label{text=\"\"};label{text=\"FloorLevel\"};label{text=\"\"};label{text=\"" + fence
/*      */ 
/*      */         
/* 6548 */         .getFloorLevel() + "\"}");
/*      */     
/* 6550 */     buf.append("label{text=\"\"};label{text=\"Height Offset\"};label{text=\"\"};label{text=\"" + fence
/*      */ 
/*      */         
/* 6553 */         .getHeightOffset() + "\"}");
/*      */     
/* 6555 */     if (fence.isDoor())
/*      */     {
/* 6557 */       if (gate != null)
/*      */       {
/* 6559 */         if (lockid != -10L) {
/*      */           
/* 6561 */           if (lock == null) {
/* 6562 */             buf.append("label{text=\"\"};label{text=\"Gate\"};label{text=\"\"};label{text=\"Lock Missing (" + lockid + ")\"}");
/*      */ 
/*      */           
/*      */           }
/* 6566 */           else if (lock.isLocked()) {
/* 6567 */             buf.append("radio{group=\"tid\";id=\"1," + lock.getWurmId() + "," + '\001' + "\"};label{text=\"" + lock
/* 6568 */                 .getName() + "\"};label{text=\"\"};label{text=\"Locked\"}");
/*      */           }
/*      */           else {
/*      */             
/* 6572 */             buf.append("radio{group=\"tid\";id=\"1," + lock.getWurmId() + "," + '\001' + "\"};label{text=\"" + lock
/* 6573 */                 .getName() + "\"};label{text=\"\"};label{text=\"Unlocked\"}");
/*      */           }
/*      */         
/*      */         }
/*      */         else {
/*      */           
/* 6579 */           buf.append("label{text=\"\"};label{text=\"Gate\"};label{text=\"\"};label{text=\"No Lock\"}");
/*      */         } 
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 6586 */     buf.append("}");
/*      */     
/* 6588 */     if (lock != null) {
/* 6589 */       buf.append(keysTable(lock));
/*      */     }
/* 6591 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private Set<Item> findCorpses(PlayerInfo pinf) {
/* 6596 */     Set<Item> corpses = new HashSet<>();
/* 6597 */     Item[] its = Items.getAllItems();
/* 6598 */     for (int itx = 0; itx < its.length; itx++) {
/*      */       
/* 6600 */       if (its[itx].getTemplateId() == 272)
/*      */       {
/* 6602 */         if (its[itx].getName().equals("corpse of " + pinf.getName()))
/*      */         {
/* 6604 */           if (its[itx].getZoneId() > -1)
/*      */           {
/* 6606 */             corpses.add(its[itx]);
/*      */           }
/*      */         }
/*      */       }
/*      */     } 
/* 6611 */     return corpses;
/*      */   }
/*      */ 
/*      */   
/*      */   private String keysTable(Item lock) {
/* 6616 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 6618 */     long[] keyarr = lock.getKeyIds();
/* 6619 */     buf.append("label{type=\"bold\";text=\"total # of keys:" + keyarr.length + "\"};");
/* 6620 */     buf.append("table{rows=\"" + keyarr.length + "\";cols=\"2\";");
/*      */     
/* 6622 */     for (long lElement : keyarr) {
/*      */ 
/*      */       
/*      */       try {
/* 6626 */         Item key = Items.getItem(lElement);
/* 6627 */         buf.append("radio{group=\"tid\";id=\"1," + lElement + "," + '\001' + "\"};" + 
/* 6628 */             itemNameWithColorByRarity(key));
/*      */       }
/* 6630 */       catch (NoSuchItemException e) {
/*      */         
/* 6632 */         buf.append("radio{group=\"tid\";id=\"1," + lElement + "," + '\001' + "\"};label{text=\"(not found) " + lElement + "\"};");
/*      */       } 
/*      */     } 
/*      */     
/* 6636 */     buf.append("}");
/*      */     
/* 6638 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String playerInfoTable(PlayerInfo[] lPlayerInfos) {
/* 6643 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 6645 */     buf.append("label{text=\"total # of players:" + lPlayerInfos.length + "\"};");
/*      */     
/* 6647 */     int pages = (lPlayerInfos.length - 1) / this.rowsPerPage;
/* 6648 */     int start = this.currentPage * this.rowsPerPage;
/* 6649 */     int last = (start + this.rowsPerPage < lPlayerInfos.length) ? (start + this.rowsPerPage) : lPlayerInfos.length;
/* 6650 */     String nav = makeNav(this.currentPage, pages);
/*      */     
/* 6652 */     if (pages > 0) {
/* 6653 */       buf.append(nav);
/*      */     }
/* 6655 */     if (last > start) {
/*      */       
/* 6657 */       buf.append("table{rows=\"" + (last - start + 1) + "\";cols=\"6\";label{text=\"\"};label{text=\"WurmId\"};label{text=\"Name\"};label{text=\"Status\"};label{text=\"Server\"};label{text=\"Banned?\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 6664 */       for (int x = start; x < last; x++) {
/*      */         
/* 6666 */         PlayerInfo playerInfo = lPlayerInfos[x];
/* 6667 */         long playerWurmId = playerInfo.wurmId;
/* 6668 */         String pServer = "Unknown";
/* 6669 */         String pStatus = "Unknown";
/* 6670 */         String pColour = "";
/* 6671 */         PlayerState pState = PlayerInfoFactory.getPlayerState(playerWurmId);
/* 6672 */         if (pState != null)
/*      */         {
/* 6674 */           if (pState.getState() == PlayerOnlineStatus.OTHER_SERVER) {
/*      */             
/* 6676 */             pStatus = "Other";
/* 6677 */             pColour = "color=\"255,177,40\"";
/* 6678 */             pServer = pState.getServerName();
/*      */           }
/* 6680 */           else if (pState.getState() == PlayerOnlineStatus.ONLINE) {
/*      */             
/* 6682 */             pStatus = "Online";
/* 6683 */             pColour = "color=\"127,255,127\"";
/* 6684 */             pServer = pState.getServerName();
/*      */           }
/* 6686 */           else if (pState.getState() == PlayerOnlineStatus.OFFLINE) {
/*      */             
/* 6688 */             pStatus = "Offline";
/* 6689 */             pColour = "color=\"255,127,127\"";
/* 6690 */             pServer = pState.getServerName();
/*      */           } 
/*      */         }
/*      */         
/* 6694 */         buf.append("radio{group=\"tid\";id=\"1," + playerWurmId + "," + '\001' + "\"};label{text=\"" + playerWurmId + "\"};label{text=\"" + playerInfo
/*      */             
/* 6696 */             .getName() + "\"};label{" + pColour + "text=\"" + pStatus + "\"};label{text=\"" + pServer + "\"};label{text=\"" + playerInfo
/*      */ 
/*      */             
/* 6699 */             .isBanned() + "\"};");
/*      */       } 
/* 6701 */       buf.append("}");
/*      */     } else {
/*      */       
/* 6704 */       buf.append("label{text=\"Nothing to show\"}");
/*      */     } 
/* 6706 */     if (pages > 0) {
/* 6707 */       buf.append(nav);
/*      */     }
/* 6709 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String buildingsTable(Player player, Structure[] buildings) {
/* 6714 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 6716 */     buf.append("label{text=\"total # of owned buildings:" + buildings.length + "\"};");
/*      */     
/* 6718 */     int pages = (buildings.length - 1) / this.rowsPerPage;
/* 6719 */     int start = this.currentPage * this.rowsPerPage;
/* 6720 */     int last = (start + this.rowsPerPage < buildings.length) ? (start + this.rowsPerPage) : buildings.length;
/* 6721 */     String nav = makeNav(this.currentPage, pages);
/*      */     
/* 6723 */     if (pages > 0) {
/* 6724 */       buf.append(nav);
/*      */     }
/* 6726 */     if (last > start) {
/*      */       
/* 6728 */       buf.append("table{rows=\"" + (last - start + 1) + "\";cols=\"7\";label{text=\"\"};label{text=\"WurmId\"}label{text=\"Owner?\"}label{text=\"Doors have locks?\"}label{text=\"On Deed?\"}label{text=\"Deed Managed?\"}label{text=\"Name\"}");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 6737 */       for (int x = start; x < last; x++) {
/*      */         
/* 6739 */         Structure structure = buildings[x];
/*      */         
/* 6741 */         buf.append("radio{group=\"tid\";id=\"1," + structure.getWurmId() + "," + '\001' + "\"};label{text=\"" + structure
/* 6742 */             .getWurmId() + "\"};label{" + 
/* 6743 */             showBoolean(structure.isActualOwner(player.getWurmId())) + "};");
/*      */         
/* 6745 */         if ((structure.getAllDoors()).length == 0) {
/* 6746 */           buf.append("label{color=\"255,177,40\"text=\"No lockable doors.\"};");
/* 6747 */         } else if (structure.isLockable()) {
/* 6748 */           buf.append("label{color=\"127,255,127\"text=\"true\"};");
/*      */         } else {
/* 6750 */           buf.append("label{color=\"255,127,127\"text=\"Not all doors have locks.\"};");
/* 6751 */         }  buf.append("label{" + showBoolean((structure.getVillage() != null)) + "};label{" + 
/* 6752 */             showBoolean(structure.isManaged()) + "};label{text=\"" + structure
/* 6753 */             .getObjectName() + "\"};");
/*      */       } 
/*      */       
/* 6756 */       buf.append("}");
/*      */     } else {
/*      */       
/* 6759 */       buf.append("label{text=\"Nothing to show\"}");
/*      */     } 
/* 6761 */     if (pages > 0) {
/* 6762 */       buf.append(nav);
/*      */     }
/* 6764 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String minedoorsTable(Player player, MineDoorPermission[] mds) {
/* 6769 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 6771 */     buf.append("label{text=\"total # of owned minde doors:" + mds.length + "\"};");
/*      */     
/* 6773 */     int pages = (mds.length - 1) / this.rowsPerPage;
/* 6774 */     int start = this.currentPage * this.rowsPerPage;
/* 6775 */     int last = (start + this.rowsPerPage < mds.length) ? (start + this.rowsPerPage) : mds.length;
/* 6776 */     String nav = makeNav(this.currentPage, pages);
/*      */     
/* 6778 */     if (pages > 0) {
/* 6779 */       buf.append(nav);
/*      */     }
/* 6781 */     if (last > start) {
/*      */       
/* 6783 */       buf.append("table{rows=\"" + (last - start + 1) + "\";cols=\"7\";label{text=\"\"};label{text=\"TileId\"}label{text=\"Door Type\"}label{text=\"Owner?\"}label{text=\"On Deed?\"}label{text=\"Deed Managed?\"}label{text=\"Name\"}");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 6792 */       for (int x = start; x < last; x++) {
/*      */         
/* 6794 */         MineDoorPermission mineDoor = mds[x];
/*      */         
/* 6796 */         buf.append("radio{group=\"tid\";id=\"1," + mineDoor.getWurmId() + "," + '\001' + "\"};label{text=\"" + mineDoor
/* 6797 */             .getWurmId() + "\"};label{text=\"" + mineDoor
/* 6798 */             .getTypeName() + "\"};label{" + 
/* 6799 */             showBoolean(mineDoor.isActualOwner(player.getWurmId())) + "};label{" + 
/* 6800 */             showBoolean((mineDoor.getVillage() != null)) + "};label{" + 
/* 6801 */             showBoolean(mineDoor.isManaged()) + "};label{text=\"" + mineDoor
/* 6802 */             .getObjectName() + "\"};");
/*      */       } 
/*      */       
/* 6805 */       buf.append("}");
/*      */     } else {
/*      */       
/* 6808 */       buf.append("label{text=\"Nothing to show\"}");
/*      */     } 
/* 6810 */     if (pages > 0) {
/* 6811 */       buf.append(nav);
/*      */     }
/* 6813 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String gatesTable(Player player, FenceGate[] gates) {
/* 6818 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 6820 */     buf.append("label{text=\"total # of owned gates:" + gates.length + "\"};");
/*      */     
/* 6822 */     int pages = (gates.length - 1) / this.rowsPerPage;
/* 6823 */     int start = this.currentPage * this.rowsPerPage;
/* 6824 */     int last = (start + this.rowsPerPage < gates.length) ? (start + this.rowsPerPage) : gates.length;
/* 6825 */     String nav = makeNav(this.currentPage, pages);
/*      */     
/* 6827 */     if (pages > 0) {
/* 6828 */       buf.append(nav);
/*      */     }
/* 6830 */     if (last > start) {
/*      */       
/* 6832 */       buf.append("table{rows=\"" + (last - start + 1) + "\";cols=\"10\";label{text=\"\"};label{text=\"WurmId\"}label{text=\"Gate Type\"}label{text=\"Level\"}label{text=\"Has Lock?\"}label{text=\"Locked?\"}label{text=\"Owner?\"}label{text=\"On Deed?\"}label{text=\"Deed Managed?\"}label{text=\"Name\"}");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 6844 */       for (int x = start; x < last; x++) {
/*      */         
/* 6846 */         FenceGate gate = gates[x];
/*      */         
/* 6848 */         buf.append("radio{group=\"tid\";id=\"1," + gate.getWurmId() + "," + '\001' + "\"};label{text=\"" + gate
/* 6849 */             .getWurmId() + "\"};label{text=\"" + gate
/* 6850 */             .getTypeName() + "\"};label{text=\"" + gate
/* 6851 */             .getFloorLevel() + "\"};label{" + 
/* 6852 */             showBoolean(gate.hasLock()) + "};label{" + 
/* 6853 */             showBoolean(gate.isLocked()) + "};label{" + 
/* 6854 */             showBoolean(gate.isActualOwner(player.getWurmId())) + "};label{" + 
/* 6855 */             showBoolean((gate.getVillage() != null)) + "};label{" + 
/* 6856 */             showBoolean(gate.isManaged()) + "};label{text=\"" + gate
/* 6857 */             .getObjectName() + "\"};");
/*      */       } 
/*      */       
/* 6860 */       buf.append("}");
/*      */     } else {
/*      */       
/* 6863 */       buf.append("label{text=\"Nothing to show\"}");
/*      */     } 
/* 6865 */     if (pages > 0) {
/* 6866 */       buf.append(nav);
/*      */     }
/* 6868 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private String pathTable(Player player, List<Route> path) {
/* 6874 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 6876 */     buf.append("label{text=\"total # of Nodes:" + path.size() + "\"};");
/*      */     
/* 6878 */     int pages = (path.size() - 1) / this.rowsPerPage;
/* 6879 */     int start = this.currentPage * this.rowsPerPage;
/* 6880 */     int last = (start + this.rowsPerPage < path.size()) ? (start + this.rowsPerPage) : path.size();
/* 6881 */     String nav = makeNav(this.currentPage, pages);
/*      */     
/* 6883 */     if (pages > 0) {
/* 6884 */       buf.append(nav);
/*      */     }
/* 6886 */     if (last > start) {
/*      */       
/* 6888 */       buf.append("table{rows=\"" + (last - start + 1) + "\";cols=\"5\";label{text=\"\"};label{text=\"Node Id\"}label{text=\"\"};label{text=\"Coords\"}label{text=\"Dir\"}");
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 6893 */       for (int x = start; x < last; x++) {
/*      */         
/* 6895 */         Node startNode = ((Route)path.get(x)).getStartNode();
/*      */         
/* 6897 */         int posX = startNode.getWaystone().getTileX();
/* 6898 */         int posY = startNode.getWaystone().getTileY();
/* 6899 */         boolean onS = startNode.getWaystone().isOnSurface();
/*      */         
/* 6901 */         buf.append("radio{group=\"tid\";id=\"15," + startNode.getWaystone().getWurmId() + "," + '\026' + "\"};");
/* 6902 */         buf.append("label{text=\"" + startNode.getWaystone().getWurmId() + "\"};");
/* 6903 */         buf.append("radio{group=\"tid\";id=\"12," + posX + "," + posY + "," + onS + "\"};");
/* 6904 */         buf.append("label{text=\"(" + posX + "," + posY + "," + onS + ")\"}");
/*      */ 
/*      */ 
/*      */         
/* 6908 */         if (x < path.size() - 1) {
/*      */           
/* 6910 */           Node endNode = ((Route)path.get(x + 1)).getEndNode();
/* 6911 */           byte dir = startNode.getNodeDir(endNode);
/* 6912 */           buf.append("label{text=\"" + MethodsHighways.getLinkDirString(dir) + "\"}");
/*      */         } else {
/*      */           
/* 6915 */           buf.append("label{type=\"italics\";text=\"target\"}");
/*      */         } 
/* 6917 */       }  buf.append("}");
/*      */     } else {
/*      */       
/* 6920 */       buf.append("label{text=\"Nothing to show\"}");
/*      */     } 
/* 6922 */     if (pages > 0) {
/* 6923 */       buf.append(nav);
/*      */     }
/* 6925 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String itemsTable(String itemtype, Player player, Item[] items) {
/* 6930 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 6932 */     buf.append("label{text=\"total # of owned " + itemtype + ":" + items.length + "\"};");
/*      */     
/* 6934 */     int pages = (items.length - 1) / this.rowsPerPage;
/* 6935 */     int start = this.currentPage * this.rowsPerPage;
/* 6936 */     int last = (start + this.rowsPerPage < items.length) ? (start + this.rowsPerPage) : items.length;
/* 6937 */     String nav = makeNav(this.currentPage, pages);
/*      */     
/* 6939 */     if (pages > 0) {
/* 6940 */       buf.append(nav);
/*      */     }
/* 6942 */     if (last > start) {
/*      */       
/* 6944 */       buf.append("table{rows=\"" + (last - start + 1) + "\";cols=\"7\";label{text=\"\"};label{text=\"WurmId\"}label{text=\"Type\"}label{text=\"Owner?\"}label{text=\"Locked?\"}label{text=\"Name\"}label{text=\"Is busy?\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 6953 */       for (int x = start; x < last; x++) {
/*      */         
/* 6955 */         Item item = items[x];
/*      */         
/* 6957 */         buf.append("radio{group=\"tid\";id=\"1," + item.getWurmId() + "," + '\001' + "\"};label{text=\"" + item
/* 6958 */             .getWurmId() + "\"};" + 
/* 6959 */             ManageObjectList.addRariryColour(item, item.getTypeName()) + "label{" + 
/* 6960 */             showBoolean(item.isActualOwner(player.getWurmId())) + "};label{" + 
/* 6961 */             showBoolean(item.isLocked()) + "};label{text=\"" + item
/* 6962 */             .getObjectName() + "\"};label{" + 
/* 6963 */             showBoolean(item.isBusy()) + "};");
/*      */       } 
/*      */       
/* 6966 */       buf.append("}");
/*      */     } else {
/*      */       
/* 6969 */       buf.append("label{text=\"Nothing to show\"}");
/*      */     } 
/* 6971 */     if (pages > 0) {
/* 6972 */       buf.append(nav);
/*      */     }
/* 6974 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String friendsTable(Friend[] lFriends) {
/* 6979 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 6981 */     buf.append("label{text=\"total # of players:" + lFriends.length + "\"};");
/*      */     
/* 6983 */     int pages = (lFriends.length - 1) / this.rowsPerPage;
/* 6984 */     int start = this.currentPage * this.rowsPerPage;
/* 6985 */     int last = (start + this.rowsPerPage < lFriends.length) ? (start + this.rowsPerPage) : lFriends.length;
/* 6986 */     String nav = makeNav(this.currentPage, pages);
/*      */     
/* 6988 */     if (pages > 0) {
/* 6989 */       buf.append(nav);
/*      */     }
/* 6991 */     if (last > start) {
/*      */ 
/*      */       
/* 6994 */       long[] wurmids = new long[last - start];
/* 6995 */       for (int x = start; x < last; x++) {
/* 6996 */         wurmids[x - start] = lFriends[x].getFriendId();
/*      */       }
/*      */       
/*      */       try {
/* 7000 */         Map<Long, byte[]> playerStates = PlayerInfoFactory.getPlayerStates(wurmids);
/*      */         
/* 7002 */         buf.append("table{rows=\"" + (last - start + 1) + "\";cols=\"5\";label{text=\"\"};label{type=\"bold\";text=\"WurmId\"};label{type=\"bold\";text=\"Name\"};label{type=\"bold\";text=\"OnServer\"};label{type=\"bold\";text=\"Category\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 7009 */         for (int i = 0; i < wurmids.length; i++) {
/*      */           
/* 7011 */           PlayerState pState = new PlayerState(playerStates.get(Long.valueOf(wurmids[i])));
/* 7012 */           buf.append("radio{group=\"tid\";id=\"1," + wurmids[i] + "," + '\001' + "\"};label{text=\"" + pState
/* 7013 */               .getPlayerId() + "\"};label{text=\"" + pState
/* 7014 */               .getPlayerName() + "\"};label{text=\"" + pState
/* 7015 */               .getServerName() + "\"};label{text=\"" + lFriends[i]
/* 7016 */               .getCategory().name() + "\"};");
/*      */         } 
/* 7018 */         buf.append("}");
/*      */       }
/* 7020 */       catch (RemoteException rx) {
/*      */ 
/*      */         
/* 7023 */         logger.log(Level.WARNING, rx.getMessage(), rx);
/*      */       }
/* 7025 */       catch (WurmServerException wse) {
/*      */ 
/*      */         
/* 7028 */         logger.log(Level.WARNING, wse.getMessage(), (Throwable)wse);
/*      */       } 
/*      */     } else {
/*      */       
/* 7032 */       buf.append("label{text=\"Nothing to show\"}");
/*      */     } 
/* 7034 */     if (pages > 0) {
/* 7035 */       buf.append(nav);
/*      */     }
/* 7037 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String PermissionsByPlayerTable(Permissions.IPermission[] ips, PermissionsByPlayer[] lplayers) {
/* 7042 */     StringBuilder buf = new StringBuilder();
/* 7043 */     Arrays.sort((Object[])lplayers);
/* 7044 */     buf.append("label{text=\"total # of players:" + lplayers.length + "\"};");
/*      */     
/* 7046 */     int pages = (lplayers.length - 1) / this.rowsPerPage;
/* 7047 */     int start = this.currentPage * this.rowsPerPage;
/* 7048 */     int last = (start + this.rowsPerPage < lplayers.length) ? (start + this.rowsPerPage) : lplayers.length;
/* 7049 */     String nav = makeNav(this.currentPage, pages);
/*      */     
/* 7051 */     if (pages > 0) {
/* 7052 */       buf.append(nav);
/*      */     }
/* 7054 */     if (last > start) {
/*      */ 
/*      */       
/* 7057 */       long[] wurmids = new long[last - start];
/* 7058 */       for (int x = start; x < last; x++) {
/* 7059 */         wurmids[x - start] = lplayers[x].getPlayerId();
/*      */       }
/*      */       
/*      */       try {
/* 7063 */         Map<Long, byte[]> playerStates = PlayerInfoFactory.getPlayerStates(wurmids);
/*      */         
/* 7065 */         buf.append("table{rows=\"" + (last - start + 1) + "\";cols=\"" + (ips.length + 4) + "\";label{text=\"\"};label{text=\"\"};label{text=\"\"};label{type=\"bold\";text=\"On\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 7071 */         for (Permissions.IPermission perm : ips) {
/* 7072 */           buf.append("label{type=\"bold\";text=\"" + perm.getHeader1() + "\"};");
/*      */         }
/* 7074 */         buf.append("label{text=\"\"};label{type=\"bold\";text=\"WurmId\"};label{type=\"bold\";text=\"Name\"};label{type=\"bold\";text=\"Server\"};");
/*      */ 
/*      */ 
/*      */         
/* 7078 */         for (Permissions.IPermission perm : ips) {
/* 7079 */           buf.append("label{type=\"bold\";text=\"" + perm.getHeader2() + "\"};");
/*      */         }
/* 7081 */         for (int i = 0; i < wurmids.length; i++) {
/*      */           
/* 7083 */           PlayerState pState = new PlayerState(playerStates.get(Long.valueOf(wurmids[i])));
/* 7084 */           buf.append("radio{group=\"tid\";id=\"1," + wurmids[i] + "," + '\001' + "\"};label{text=\"" + pState
/* 7085 */               .getPlayerId() + "\"};label{text=\"" + 
/* 7086 */               PermissionsByPlayer.getPlayerOrGroupName(pState.getPlayerId()) + "\"};label{text=\"" + (
/* 7087 */               (pState.getPlayerId() < 0L) ? Servers.getLocalServerName() : pState.getServerName()) + "\"};");
/* 7088 */           for (Permissions.IPermission perm : ips)
/* 7089 */             buf.append("label{" + showBoolean(lplayers[start + i].hasPermission(perm.getBit())) + "};"); 
/*      */         } 
/* 7091 */         buf.append("}");
/*      */       }
/* 7093 */       catch (RemoteException rx) {
/*      */ 
/*      */         
/* 7096 */         logger.log(Level.WARNING, rx.getMessage(), rx);
/*      */       }
/* 7098 */       catch (WurmServerException wse) {
/*      */ 
/*      */         
/* 7101 */         logger.log(Level.WARNING, wse.getMessage(), (Throwable)wse);
/*      */       } 
/*      */     } else {
/*      */       
/* 7105 */       buf.append("label{text=\"Nothing to show\"}");
/*      */     } 
/* 7107 */     if (pages > 0) {
/* 7108 */       buf.append(nav);
/*      */     }
/* 7110 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String showBoolean(boolean flag) {
/* 7115 */     StringBuilder buf = new StringBuilder();
/* 7116 */     if (flag) {
/* 7117 */       buf.append("color=\"127,255,127\"");
/*      */     } else {
/* 7119 */       buf.append("color=\"255,127,127\"");
/* 7120 */     }  buf.append("text=\"" + flag + "\"");
/* 7121 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String playersTable(long[] lplayers) {
/* 7126 */     StringBuilder buf = new StringBuilder();
/* 7127 */     Arrays.sort(lplayers);
/* 7128 */     buf.append("label{text=\"total # of players:" + lplayers.length + "\"};");
/*      */     
/* 7130 */     int pages = (lplayers.length - 1) / this.rowsPerPage;
/* 7131 */     int start = this.currentPage * this.rowsPerPage;
/* 7132 */     int last = (start + this.rowsPerPage < lplayers.length) ? (start + this.rowsPerPage) : lplayers.length;
/* 7133 */     String nav = makeNav(this.currentPage, pages);
/*      */     
/* 7135 */     if (pages > 0) {
/* 7136 */       buf.append(nav);
/*      */     }
/* 7138 */     if (last > start) {
/*      */ 
/*      */       
/* 7141 */       long[] wurmids = new long[last - start];
/* 7142 */       for (int x = start; x < last; x++) {
/* 7143 */         wurmids[x - start] = lplayers[x];
/*      */       }
/*      */       
/*      */       try {
/* 7147 */         Map<Long, byte[]> playerStates = PlayerInfoFactory.getPlayerStates(wurmids);
/*      */         
/* 7149 */         buf.append("table{rows=\"" + (last - start + 1) + "\";cols=\"4\";label{text=\"\"};label{type=\"bold\";text=\"WurmId\"};label{type=\"bold\";text=\"Name\"};label{type=\"bold\";text=\"OnServer\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 7155 */         for (int i = 0; i < wurmids.length; i++) {
/*      */           
/* 7157 */           PlayerState pState = new PlayerState(playerStates.get(Long.valueOf(wurmids[i])));
/* 7158 */           buf.append("radio{group=\"tid\";id=\"1," + wurmids[i] + "," + '\001' + "\"};label{text=\"" + pState
/* 7159 */               .getPlayerId() + "\"};label{text=\"" + pState
/* 7160 */               .getPlayerName() + "\"};label{text=\"" + pState
/* 7161 */               .getServerName() + "\"};");
/*      */         } 
/* 7163 */         buf.append("}");
/*      */       }
/* 7165 */       catch (RemoteException rx) {
/*      */ 
/*      */         
/* 7168 */         logger.log(Level.WARNING, rx.getMessage(), rx);
/*      */       }
/* 7170 */       catch (WurmServerException wse) {
/*      */ 
/*      */         
/* 7173 */         logger.log(Level.WARNING, wse.getMessage(), (Throwable)wse);
/*      */       } 
/*      */     } else {
/*      */       
/* 7177 */       buf.append("label{text=\"Nothing to show\"}");
/*      */     } 
/* 7179 */     if (pages > 0) {
/* 7180 */       buf.append(nav);
/*      */     }
/* 7182 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String avatarsTable(Creature[] lcreatures) {
/* 7187 */     StringBuilder buf = new StringBuilder();
/* 7188 */     Arrays.sort((Object[])lcreatures);
/* 7189 */     buf.append("label{text=\"total # of avatars:" + lcreatures.length + "\"};");
/*      */     
/* 7191 */     int pages = (lcreatures.length - 1) / this.rowsPerPage;
/* 7192 */     int start = this.currentPage * this.rowsPerPage;
/* 7193 */     int last = (start + this.rowsPerPage < lcreatures.length) ? (start + this.rowsPerPage) : lcreatures.length;
/* 7194 */     String nav = makeNav(this.currentPage, pages);
/*      */     
/* 7196 */     if (pages > 0) {
/* 7197 */       buf.append(nav);
/*      */     }
/* 7199 */     if (last > start) {
/*      */       
/* 7201 */       buf.append("table{rows=\"" + (last - start + 1) + "\";cols=\"6\";label{text=\"\"};label{type=\"bold\";text=\"WurmId\"};label{type=\"bold\";text=\"Type\"};label{text=\"\"};label{type=\"bold\";text=\"Coords\"};label{type=\"bold\";text=\"FullName\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 7207 */       for (int x = start; x < last; x++) {
/*      */         
/* 7209 */         Creature lcreature = lcreatures[x];
/* 7210 */         int posX = lcreature.getTileX();
/* 7211 */         int posY = lcreature.getTileY();
/* 7212 */         boolean onS = lcreature.isOnSurface();
/* 7213 */         buf.append("radio{group=\"tid\";id=\"1," + lcreature.getWurmId() + "," + '\001' + "\"};label{text=\"" + lcreature
/* 7214 */             .getWurmId() + "\"};label{text=\"" + lcreature
/* 7215 */             .getTemplate().getName() + "\"};radio{group=\"tid\";id=\"" + '\f' + "," + posX + "," + posY + "," + onS + "\"};label{text=\"(" + posX + "," + posY + "," + onS + ")\"}label{text=\"" + lcreature
/*      */ 
/*      */             
/* 7218 */             .getName() + "\"};");
/*      */       } 
/* 7220 */       buf.append("}");
/*      */     } else {
/*      */       
/* 7223 */       buf.append("label{text=\"Nothing to show\"}");
/*      */     } 
/* 7225 */     if (pages > 0) {
/* 7226 */       buf.append(nav);
/*      */     }
/* 7228 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String kingdomsTable(Kingdom[] lkingdoms) {
/* 7233 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 7235 */     buf.append("label{text=\"total # of kingdoms:" + lkingdoms.length + "\"};");
/*      */     
/* 7237 */     int pages = (lkingdoms.length - 1) / this.rowsPerPage;
/* 7238 */     int start = this.currentPage * this.rowsPerPage;
/* 7239 */     int last = (start + this.rowsPerPage < lkingdoms.length) ? (start + this.rowsPerPage) : lkingdoms.length;
/* 7240 */     String nav = makeNav(this.currentPage, pages);
/*      */     
/* 7242 */     if (pages > 0) {
/* 7243 */       buf.append(nav);
/*      */     }
/* 7245 */     if (last > start) {
/*      */       
/* 7247 */       buf.append("table{rows=\"" + (last - start + 1) + "\";cols=\"11\";label{text=\"\"};label{type=\"bold\";text=\"Id\"};label{type=\"bold\";text=\"Name\"};label{type=\"bold\";text=\"Template\"};label{type=\"bold\";text=\"Suffix\"};label{type=\"bold\";text=\"Chat Name\"};label{type=\"bold\";text=\"Here?\"};label{type=\"bold\";text=\"1st Motto\"};label{type=\"bold\";text=\"2nd Motto\"};label{type=\"bold\";text=\"Accepts\"};label{type=\"bold\";text=\"WinPoints\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 7260 */       for (int x = start; x < last; x++) {
/*      */         
/* 7262 */         Kingdom lkingdom = lkingdoms[x];
/* 7263 */         String tempking = "";
/* 7264 */         switch (lkingdom.getTemplate()) {
/*      */           
/*      */           case 0:
/* 7267 */             tempking = "none";
/*      */             break;
/*      */           case 1:
/* 7270 */             tempking = "JK";
/*      */             break;
/*      */           case 2:
/* 7273 */             tempking = "MR";
/*      */             break;
/*      */           case 3:
/* 7276 */             tempking = "HOTS";
/*      */             break;
/*      */           case 4:
/* 7279 */             tempking = "Freedom";
/*      */             break;
/*      */           default:
/* 7282 */             tempking = "(" + lkingdom.getTemplate() + ")"; break;
/*      */         } 
/* 7284 */         buf.append("label{text=\"\"};label{text=\"" + lkingdom
/* 7285 */             .getId() + "\"};label{text=\"" + lkingdom
/* 7286 */             .getName() + "\"};label{text=\"" + tempking + "\"};label{text=\"" + lkingdom
/*      */             
/* 7288 */             .getSuffix() + "\"};label{text=\"" + lkingdom
/* 7289 */             .getChatName() + "\"};label{text=\"" + lkingdom
/* 7290 */             .existsHere() + "\"};label{text=\"" + lkingdom
/* 7291 */             .getFirstMotto() + "\"};label{text=\"" + lkingdom
/* 7292 */             .getSecondMotto() + "\"};label{text=\"" + lkingdom
/* 7293 */             .acceptsTransfers() + "\"};label{text=\"" + lkingdom
/* 7294 */             .getWinpoints() + "\"};");
/*      */       } 
/*      */       
/* 7297 */       buf.append("}");
/*      */     } else {
/*      */       
/* 7300 */       buf.append("label{text=\"Nothing to show\"}");
/*      */     } 
/* 7302 */     if (pages > 0) {
/* 7303 */       buf.append(nav);
/*      */     }
/* 7305 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String creaturesTable(Creature[] lcreatures) {
/* 7310 */     StringBuilder buf = new StringBuilder();
/* 7311 */     Arrays.sort((Object[])lcreatures);
/* 7312 */     buf.append("label{text=\"total # of creatures:" + lcreatures.length + "\"};");
/*      */     
/* 7314 */     int pages = (lcreatures.length - 1) / this.rowsPerPage;
/* 7315 */     int start = this.currentPage * this.rowsPerPage;
/* 7316 */     int last = (start + this.rowsPerPage < lcreatures.length) ? (start + this.rowsPerPage) : lcreatures.length;
/* 7317 */     String nav = makeNav(this.currentPage, pages);
/*      */     
/* 7319 */     if (pages > 0) {
/* 7320 */       buf.append(nav);
/*      */     }
/* 7322 */     if (last > start) {
/*      */       
/* 7324 */       buf.append("table{rows=\"" + (last - start + 1) + "\";cols=\"5\";label{text=\"\"};label{type=\"bold\";text=\"WurmId\"};label{type=\"bold\";text=\"Type\"};label{type=\"bold\";text=\"Name\"};label{type=\"bold\";text=\"FullName\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 7330 */       for (int x = start; x < last; x++) {
/*      */         
/* 7332 */         Creature lcreature = lcreatures[x];
/*      */         
/* 7334 */         buf.append("radio{group=\"tid\";id=\"1," + lcreature.getWurmId() + "," + '\001' + "\"};label{text=\"" + lcreature
/* 7335 */             .getWurmId() + "\"};label{text=\"" + lcreature
/* 7336 */             .getTemplate().getName() + "\"};label{text=\"" + lcreature
/* 7337 */             .getNameWithoutPrefixes() + "\"};label{text=\"" + lcreature
/* 7338 */             .getName() + "\"};");
/*      */       } 
/* 7340 */       buf.append("}");
/*      */     } else {
/*      */       
/* 7343 */       buf.append("label{text=\"Nothing to show\"}");
/*      */     } 
/* 7345 */     if (pages > 0) {
/* 7346 */       buf.append(nav);
/*      */     }
/* 7348 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String skillsTable(SkillTemplate[] templates, Skills skills) {
/* 7353 */     StringBuilder buf = new StringBuilder();
/* 7354 */     Arrays.sort((Object[])templates);
/*      */     
/* 7356 */     buf.append("label{type=\"italic\";text=\"Affinities are only shown for online players.\"};");
/* 7357 */     buf.append("label{text=\"total # of skills:" + templates.length + "\"};");
/*      */     
/* 7359 */     int pages = (templates.length - 1) / this.rowsPerPage;
/* 7360 */     int start = this.currentPage * this.rowsPerPage;
/* 7361 */     int last = (start + this.rowsPerPage < templates.length) ? (start + this.rowsPerPage) : templates.length;
/* 7362 */     String nav = makeNav(this.currentPage, pages);
/*      */     
/* 7364 */     if (pages > 0) {
/* 7365 */       buf.append(nav);
/*      */     }
/* 7367 */     if (last > start) {
/*      */       
/* 7369 */       buf.append("table{rows=\"" + (last - start + 1) + "\";cols=\"5\";label{type=\"bold\";text=\"\"};label{type=\"bold\";text=\"Name\"};label{type=\"bold\";text=\"Value\"};label{type=\"bold\";text=\"Value2\"};label{type=\"bold\";text=\"Highest\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 7375 */       for (int x = start; x < last; x++) {
/*      */         
/* 7377 */         int sk = templates[x].getNumber();
/*      */         
/*      */         try {
/* 7380 */           Skill skill = skills.getSkill(sk);
/* 7381 */           String affs = "******".substring(0, skill.affinity);
/* 7382 */           buf.append("label{text=\"\"};label{text=\"" + templates[x]
/* 7383 */               .getName() + " " + affs + "\"};label{text=\"" + new Float(skill
/* 7384 */                 .getKnowledge()) + "\"};label{text=\"" + new Float(skill
/* 7385 */                 .getKnowledge(0.0D)) + "\"};label{text=\"" + new Float(skill
/* 7386 */                 .getMinimumValue()) + "\"};");
/*      */         }
/* 7388 */         catch (NoSuchSkillException e) {
/*      */ 
/*      */           
/* 7391 */           buf.append("label{text=\"\"};label{text=\"" + templates[x]
/* 7392 */               .getName() + "\"};label{text=\"1\"};label{text=\"1\"};label{text=\"N/A\"};");
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 7398 */       buf.append("}");
/*      */     } else {
/*      */       
/* 7401 */       buf.append("label{text=\"No Skills found\"}");
/*      */     } 
/* 7403 */     if (pages > 0) {
/* 7404 */       buf.append(nav);
/*      */     }
/* 7406 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String tilesTable(VolaTile[] tiles) {
/* 7411 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 7413 */     buf.append("label{text=\"Total # of tiles:" + tiles.length + "\"};");
/*      */     
/* 7415 */     int pages = (tiles.length - 1) / this.rowsPerPage;
/* 7416 */     int start = this.currentPage * this.rowsPerPage;
/* 7417 */     int last = (start + this.rowsPerPage < tiles.length) ? (start + this.rowsPerPage) : tiles.length;
/* 7418 */     String nav = makeNav(this.currentPage, pages);
/*      */     
/* 7420 */     if (pages > 0) {
/* 7421 */       buf.append(nav);
/*      */     }
/* 7423 */     if (last > start) {
/*      */       
/* 7425 */       buf.append("table{rows=\"" + (last - start + 1) + "\";cols=\"5\";label{text=\"\"};label{type=\"bold\";text=\"Wurm Id\"};label{type=\"bold\";text=\"TileX\"};label{type=\"bold\";text=\"TileY\"};label{type=\"bold\";text=\"Type\"}");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 7431 */       for (int x = start; x < last; x++) {
/*      */         
/* 7433 */         VolaTile tile = tiles[x];
/* 7434 */         long Id = Tiles.getTileId(tile.tilex, tile.tiley, 0);
/* 7435 */         int stile = Server.surfaceMesh.getTile(tile.tilex, tile.tiley);
/* 7436 */         byte ttype = Tiles.decodeType(stile);
/* 7437 */         Tiles.Tile theTile = Tiles.getTile(ttype);
/*      */         
/* 7439 */         buf.append("radio{group=\"tid\";id=\"1," + Id + "," + '\001' + "\"};label{text=\"" + Id + "\"};label{text=\"" + tile.tilex + "\"};label{text=\"" + tile.tiley + "\"};label{text=\"" + theTile
/*      */ 
/*      */ 
/*      */             
/* 7443 */             .getDesc() + "\"};");
/*      */       } 
/* 7445 */       buf.append("}");
/*      */     } else {
/*      */       
/* 7448 */       buf.append("label{text=\"Nothing to show\"}");
/*      */     } 
/* 7450 */     if (pages > 0)
/* 7451 */       buf.append(nav); 
/* 7452 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String wallsTable(Wall[] lWalls, int tilex, int tiley) {
/* 7457 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 7459 */     buf.append("label{text=\"Total # of walls:" + lWalls.length + "\"};");
/*      */     
/* 7461 */     int pages = (lWalls.length - 1) / this.rowsPerPage;
/* 7462 */     int start = this.currentPage * this.rowsPerPage;
/* 7463 */     int last = (start + this.rowsPerPage < lWalls.length) ? (start + this.rowsPerPage) : lWalls.length;
/* 7464 */     String nav = makeNav(this.currentPage, pages);
/*      */     
/* 7466 */     if (pages > 0) {
/* 7467 */       buf.append(nav);
/*      */     }
/* 7469 */     if (last > start) {
/*      */       
/* 7471 */       buf.append("table{rows=\"" + (last - start + 1) + "\";cols=\"8\";label{text=\"\"};label{type=\"bold\";text=\"Wurm Id\"};label{type=\"bold\";text=\"QL\"};label{type=\"bold\";text=\"DMG\"};label{type=\"bold\";text=\"Border\"}label{type=\"bold\";text=\"Name\"};label{type=\"bold\";text=\"Level\"};label{type=\"bold\";text=\"Indoor\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 7480 */       for (int x = start; x < last; x++) {
/*      */         String sBorder;
/* 7482 */         Wall lWall = lWalls[x];
/*      */ 
/*      */         
/* 7485 */         if (tilex == lWall.getStartX() - 1) {
/* 7486 */           sBorder = "East";
/* 7487 */         } else if (tiley == lWall.getStartY() - 1) {
/* 7488 */           sBorder = "South";
/*      */         } else {
/* 7490 */           sBorder = lWall.isHorizontal() ? "North" : "West";
/*      */         } 
/* 7492 */         buf.append("radio{group=\"tid\";id=\"1," + lWall.getId() + "," + '\001' + "\"};label{text=\"" + lWall
/* 7493 */             .getId() + "\"};label{text=\"" + this.df
/* 7494 */             .format(lWall.getQualityLevel()) + "\"};label{text=\"" + this.df
/* 7495 */             .format(lWall.getDamage()) + "\"};label{text=\"" + sBorder + "\"}label{text=\"" + lWall
/*      */             
/* 7497 */             .getName() + "\"};label{text=\"" + lWall
/* 7498 */             .getFloorLevel() + "\"};label{" + 
/* 7499 */             showBoolean(lWall.isIndoor()) + "};");
/*      */       } 
/*      */       
/* 7502 */       buf.append("}");
/*      */     } else {
/*      */       
/* 7505 */       buf.append("label{text=\"Nothing to show\"}");
/*      */     } 
/* 7507 */     if (pages > 0)
/* 7508 */       buf.append(nav); 
/* 7509 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String floorsTable(Floor[] lFloors) {
/* 7514 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 7516 */     buf.append("label{text=\"Total # of floors:" + lFloors.length + "\"};");
/*      */     
/* 7518 */     int pages = (lFloors.length - 1) / this.rowsPerPage;
/* 7519 */     int start = this.currentPage * this.rowsPerPage;
/* 7520 */     int last = (start + this.rowsPerPage < lFloors.length) ? (start + this.rowsPerPage) : lFloors.length;
/* 7521 */     String nav = makeNav(this.currentPage, pages);
/*      */     
/* 7523 */     if (pages > 0) {
/* 7524 */       buf.append(nav);
/*      */     }
/* 7526 */     if (last > start) {
/*      */       
/* 7528 */       buf.append("table{rows=\"" + (last - start + 1) + "\";cols=\"7\";label{text=\"\"};label{type=\"bold\";text=\"Wurm Id\"};label{type=\"bold\";text=\"QL\"};label{type=\"bold\";text=\"DMG\"};label{type=\"bold\";text=\"Name\"};label{type=\"bold\";text=\"Level\"};label{type=\"bold\";text=\"Dir\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 7536 */       for (int x = start; x < last; x++) {
/*      */         
/* 7538 */         Floor lFloor = lFloors[x];
/*      */         
/* 7540 */         buf.append("radio{group=\"tid\";id=\"1," + lFloor.getId() + "," + '\001' + "\"};label{text=\"" + lFloor
/* 7541 */             .getId() + "\"};label{text=\"" + this.df
/* 7542 */             .format(lFloor.getQualityLevel()) + "\"};label{text=\"" + this.df
/* 7543 */             .format(lFloor.getDamage()) + "\"};label{text=\"" + lFloor
/* 7544 */             .getName() + "\"};label{text=\"" + lFloor
/* 7545 */             .getFloorLevel() + "\"};label{text=\"" + lFloor
/* 7546 */             .getDir() + "\"};");
/*      */       } 
/* 7548 */       buf.append("}");
/*      */     } else {
/*      */       
/* 7551 */       buf.append("label{text=\"Nothing to show\"}");
/*      */     } 
/* 7553 */     if (pages > 0)
/* 7554 */       buf.append(nav); 
/* 7555 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String bridgePartTable(BridgePart[] lBridgeParts) {
/* 7560 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 7562 */     buf.append("label{text=\"Total # of Bridge Parts:" + lBridgeParts.length + "\"};");
/*      */     
/* 7564 */     int pages = (lBridgeParts.length - 1) / this.rowsPerPage;
/* 7565 */     int start = this.currentPage * this.rowsPerPage;
/* 7566 */     int last = (start + this.rowsPerPage < lBridgeParts.length) ? (start + this.rowsPerPage) : lBridgeParts.length;
/* 7567 */     String nav = makeNav(this.currentPage, pages);
/*      */     
/* 7569 */     if (pages > 0) {
/* 7570 */       buf.append(nav);
/*      */     }
/* 7572 */     if (last > start) {
/*      */       
/* 7574 */       buf.append("table{rows=\"" + (last - start + 1) + "\";cols=\"7\";label{text=\"\"};label{type=\"bold\";text=\"Wurm Id\"};label{type=\"bold\";text=\"QL\"};label{type=\"bold\";text=\"DMG\"};label{type=\"bold\";text=\"Name\"};label{type=\"bold\";text=\"Dir\"};label{type=\"bold\";text=\"Slope\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 7582 */       for (int x = start; x < last; x++) {
/*      */         
/* 7584 */         BridgePart lBridgePart = lBridgeParts[x];
/*      */         
/* 7586 */         buf.append("radio{group=\"tid\";id=\"1," + lBridgePart.getId() + "," + '\001' + "\"};label{text=\"" + lBridgePart
/* 7587 */             .getId() + "\"};label{text=\"" + this.df
/* 7588 */             .format(lBridgePart.getQualityLevel()) + "\"};label{text=\"" + this.df
/* 7589 */             .format(lBridgePart.getDamage()) + "\"};label{text=\"" + lBridgePart
/* 7590 */             .getName() + "\"};label{text=\"" + lBridgePart
/* 7591 */             .getDir() + "\"};label{text=\"" + lBridgePart
/* 7592 */             .getSlope() + "\"};");
/*      */       } 
/* 7594 */       buf.append("}");
/*      */     } else {
/*      */       
/* 7597 */       buf.append("label{text=\"Nothing to show\"}");
/*      */     } 
/* 7599 */     if (pages > 0)
/* 7600 */       buf.append(nav); 
/* 7601 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String bridgePartsTable(BridgePart[] lBridgeParts, int tilex, int tiley) {
/* 7606 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 7608 */     buf.append("label{text=\"Total # of bridge parts:" + lBridgeParts.length + "\"};");
/*      */     
/* 7610 */     int pages = (lBridgeParts.length - 1) / this.rowsPerPage;
/* 7611 */     int start = this.currentPage * this.rowsPerPage;
/* 7612 */     int last = (start + this.rowsPerPage < lBridgeParts.length) ? (start + this.rowsPerPage) : lBridgeParts.length;
/* 7613 */     String nav = makeNav(this.currentPage, pages);
/*      */     
/* 7615 */     if (pages > 0) {
/* 7616 */       buf.append(nav);
/*      */     }
/* 7618 */     if (last > start) {
/*      */       
/* 7620 */       buf.append("table{rows=\"" + (last - start + 1) + "\";cols=\"5\";label{text=\"\"};label{type=\"bold\";text=\"Wurm Id\"};label{type=\"bold\";text=\"QL\"};label{type=\"bold\";text=\"DMG\"};label{type=\"bold\";text=\"Name\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 7626 */       for (int x = start; x < last; x++) {
/*      */         
/* 7628 */         BridgePart lBridgePart = lBridgeParts[x];
/*      */         
/* 7630 */         buf.append("radio{group=\"tid\";id=\"1," + lBridgePart.getId() + "," + '\001' + "\"};label{text=\"" + lBridgePart
/* 7631 */             .getId() + "\"};label{text=\"" + this.df
/* 7632 */             .format(lBridgePart.getQualityLevel()) + "\"};label{text=\"" + this.df
/* 7633 */             .format(lBridgePart.getDamage()) + "\"};label{text=\"" + lBridgePart
/* 7634 */             .getName() + "\"};");
/*      */       } 
/* 7636 */       buf.append("}");
/*      */     } else {
/*      */       
/* 7639 */       buf.append("label{text=\"Nothing to show\"}");
/*      */     } 
/* 7641 */     if (pages > 0)
/* 7642 */       buf.append(nav); 
/* 7643 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String fencesTable(Fence[] lFences, int tilex, int tiley) {
/* 7648 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 7650 */     buf.append("label{text=\"Total # of fences:" + lFences.length + "\"};");
/*      */     
/* 7652 */     int pages = (lFences.length - 1) / this.rowsPerPage;
/* 7653 */     int start = this.currentPage * this.rowsPerPage;
/* 7654 */     int last = (start + this.rowsPerPage < lFences.length) ? (start + this.rowsPerPage) : lFences.length;
/* 7655 */     String nav = makeNav(this.currentPage, pages);
/*      */     
/* 7657 */     if (pages > 0) {
/* 7658 */       buf.append(nav);
/*      */     }
/* 7660 */     if (last > start) {
/*      */       
/* 7662 */       buf.append("table{rows=\"" + (last - start + 1) + "\";cols=\"7\";label{text=\"\"};label{type=\"bold\";text=\"Wurm Id\"};label{type=\"bold\";text=\"QL\"};label{type=\"bold\";text=\"DMG\"};label{type=\"bold\";text=\"Border\"};label{type=\"bold\";text=\"Name\"};label{type=\"bold\";text=\"Level\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 7670 */       for (int x = start; x < last; x++) {
/*      */         String sBorder;
/* 7672 */         Fence lFence = lFences[x];
/*      */ 
/*      */         
/* 7675 */         if (tilex > -1 && tilex == lFence.getTileX() - 1) {
/* 7676 */           sBorder = "East";
/* 7677 */         } else if (tiley > -1 && tiley == lFence.getTileY() - 1) {
/* 7678 */           sBorder = "South";
/*      */         } else {
/* 7680 */           sBorder = lFence.isHorizontal() ? "North" : "West";
/*      */         } 
/* 7682 */         buf.append("radio{group=\"tid\";id=\"1," + lFence.getId() + "," + '\001' + "\"};label{text=\"" + lFence
/* 7683 */             .getId() + "\"};label{text=\"" + this.df
/* 7684 */             .format(lFence.getQualityLevel()) + "\"};label{text=\"" + this.df
/* 7685 */             .format(lFence.getDamage()) + "\"};label{text=\"" + sBorder + "\"};label{text=\"" + lFence
/*      */             
/* 7687 */             .getName() + "\"};label{text=\"" + lFence
/* 7688 */             .getFloorLevel() + "\"};");
/*      */       } 
/* 7690 */       buf.append("}");
/*      */     } else {
/*      */       
/* 7693 */       buf.append("label{text=\"Nothing to show\"}");
/*      */     } 
/* 7695 */     if (pages > 0)
/* 7696 */       buf.append(nav); 
/* 7697 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String gmSignsTable(Item[] litems) {
/* 7702 */     StringBuilder buf = new StringBuilder();
/* 7703 */     buf.append("label{text=\"total # of GM Signs:" + litems.length + "\"};");
/* 7704 */     Arrays.sort((Object[])litems);
/*      */     
/* 7706 */     int pages = (litems.length - 1) / this.rowsPerPage;
/* 7707 */     int start = this.currentPage * this.rowsPerPage;
/* 7708 */     int last = (start + this.rowsPerPage < litems.length) ? (start + this.rowsPerPage) : litems.length;
/* 7709 */     String nav = makeNav(this.currentPage, pages);
/*      */     
/* 7711 */     if (pages > 0) {
/* 7712 */       buf.append(nav);
/*      */     }
/* 7714 */     if (last > start) {
/*      */       
/* 7716 */       buf.append("table{rows=\"" + (last - start + 1) + "\";cols=\"7\";label{text=\"\"};label{type=\"bold\";text=\"Wurm Id\"};label{type=\"bold\";text=\"QL\"};label{type=\"bold\";text=\"DAM\"};label{text=\"\"};label{type=\"bold\";text=\"Coords\"};label{type=\"bold\";text=\"Description\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 7723 */       for (int x = start; x < last; x++) {
/*      */         
/* 7725 */         int posX = litems[x].getTileX();
/* 7726 */         int posY = litems[x].getTileY();
/* 7727 */         boolean onS = litems[x].isOnSurface();
/* 7728 */         buf.append("radio{group=\"tid\";id=\"1," + litems[x].getWurmId() + "," + '\001' + "\"};label{text=\"" + litems[x]
/* 7729 */             .getWurmId() + "\"};label{text=\"" + this.df
/* 7730 */             .format(litems[x].getQualityLevel()) + "\"};label{text=\"" + this.df
/* 7731 */             .format(litems[x].getDamage()) + "\"};radio{group=\"tid\";id=\"" + '\f' + "," + posX + "," + posY + "," + onS + "\"};label{text=\"(" + posX + "," + posY + "," + onS + ")\"}label{text=\"" + litems[x]
/*      */ 
/*      */             
/* 7734 */             .getDescription() + "\"}");
/*      */       } 
/* 7736 */       buf.append("}");
/*      */     } else {
/*      */       
/* 7739 */       buf.append("label{text=\"Nothing to show\"}");
/*      */     } 
/* 7741 */     if (pages > 0)
/* 7742 */       buf.append(nav); 
/* 7743 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String itemsTable(Item[] litems) {
/* 7748 */     StringBuilder buf = new StringBuilder();
/* 7749 */     buf.append("label{text=\"total # of items:" + litems.length + "\"};");
/* 7750 */     Arrays.sort((Object[])litems);
/*      */     
/* 7752 */     int pages = (litems.length - 1) / this.rowsPerPage;
/* 7753 */     int start = this.currentPage * this.rowsPerPage;
/* 7754 */     int last = (start + this.rowsPerPage < litems.length) ? (start + this.rowsPerPage) : litems.length;
/* 7755 */     String nav = makeNav(this.currentPage, pages);
/*      */     
/* 7757 */     if (pages > 0) {
/* 7758 */       buf.append(nav);
/*      */     }
/* 7760 */     if (last > start) {
/*      */       
/* 7762 */       buf.append("table{rows=\"" + (last - start + 1) + "\";cols=\"7\";label{text=\"\"};label{type=\"bold\";text=\"Wurm Id\"};label{type=\"bold\";text=\"QL\"};label{type=\"bold\";text=\"DAM\"};label{type=\"bold\";text=\"Name\"};label{type=\"bold\";text=\"Aux\"};label{type=\"bold\";text=\"Is busy?\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 7771 */       for (int x = start; x < last; x++) {
/*      */         
/* 7773 */         String aux = "" + litems[x].getAuxData();
/* 7774 */         if (litems[x].isRoadMarker())
/*      */         {
/* 7776 */           aux = MethodsHighways.getLinkAsString(litems[x].getAuxData()) + "(" + aux + ")";
/*      */         }
/* 7778 */         buf.append("radio{group=\"tid\";id=\"1," + litems[x].getWurmId() + "," + '\001' + "\"};label{text=\"" + litems[x]
/* 7779 */             .getWurmId() + "\"};label{text=\"" + this.df
/* 7780 */             .format(litems[x].getQualityLevel()) + "\"};label{text=\"" + this.df
/* 7781 */             .format(litems[x].getDamage()) + "\"};" + 
/* 7782 */             itemNameWithColorByRarity(litems[x]) + "label{text=\"" + aux + "\"};label{" + 
/*      */             
/* 7784 */             showBoolean(litems[x].isBusy()) + "};");
/*      */       } 
/*      */       
/* 7787 */       buf.append("}");
/*      */     } else {
/*      */       
/* 7790 */       buf.append("label{text=\"Nothing to show\"}");
/*      */     } 
/* 7792 */     if (pages > 0)
/* 7793 */       buf.append(nav); 
/* 7794 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String traderTable(Shop[] lShops) {
/* 7799 */     StringBuilder buf = new StringBuilder();
/* 7800 */     buf.append("label{text=\"total # of traders:" + lShops.length + "\"};");
/*      */ 
/*      */     
/* 7803 */     int pages = (lShops.length - 1) / this.rowsPerPage;
/* 7804 */     int start = this.currentPage * this.rowsPerPage;
/* 7805 */     int last = (start + this.rowsPerPage < lShops.length) ? (start + this.rowsPerPage) : lShops.length;
/* 7806 */     String nav = makeNav(this.currentPage, pages);
/*      */     
/* 7808 */     if (pages > 0) {
/* 7809 */       buf.append(nav);
/*      */     }
/* 7811 */     if (last > start) {
/*      */       
/* 7813 */       buf.append("table{rows=\"" + (last - start + 1) + "\";cols=\"8\";label{type=\"bold\";text=\"TP\"};label{type=\"bold\";text=\"Wurm Id\"};label{type=\"bold\";text=\"X\"};label{type=\"bold\";text=\"Y\"};label{type=\"bold\";text=\"Name\"};label{type=\"bold\";text=\"vTP\"};label{type=\"bold\";text=\"Village\"};label{type=\"bold\";text=\"Kingdom\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 7823 */       for (int x = start; x < last; x++) {
/*      */ 
/*      */         
/*      */         try {
/*      */           
/* 7828 */           Creature trader = Creatures.getInstance().getCreature(lShops[x].getWurmId());
/* 7829 */           if (trader.getCurrentVillage() == null)
/*      */           {
/* 7831 */             buf.append("radio{group=\"tid\";id=\"12," + trader.getTileX() + "," + trader.getTileY() + "," + trader.isOnSurface() + "\"};label{text=\"" + lShops[x]
/* 7832 */                 .getWurmId() + "\"};label{text=\"" + trader
/* 7833 */                 .getTileX() + "\"};label{text=\"" + trader
/* 7834 */                 .getTileY() + "\"};label{text=\"" + trader
/* 7835 */                 .getName() + "\"};label{text=\"\"};label{text=\"n/a\"};label{text=\"" + 
/*      */ 
/*      */                 
/* 7838 */                 Kingdoms.getNameFor(trader.getCurrentKingdom()) + "\"};");
/*      */           
/*      */           }
/*      */           else
/*      */           {
/* 7843 */             buf.append("radio{group=\"tid\";id=\"12," + trader.getTileX() + "," + trader.getTileY() + "," + trader.isOnSurface() + "\"};label{text=\"" + lShops[x]
/* 7844 */                 .getWurmId() + "\"};label{text=\"" + trader
/* 7845 */                 .getTileX() + "\"};label{text=\"" + trader
/* 7846 */                 .getTileY() + "\"};label{text=\"" + trader
/* 7847 */                 .getName() + "\"};radio{group=\"tid\";id=\"" + '\f' + "," + trader
/* 7848 */                 .getCurrentVillage().getTokenX() + "," + trader.getCurrentVillage().getTokenY() + ",true\"};label{text=\"" + trader
/* 7849 */                 .getCurrentVillage().getName() + "\"};label{text=\"" + 
/* 7850 */                 Kingdoms.getNameFor(trader.getCurrentKingdom()) + "\"};");
/*      */           }
/*      */         
/*      */         }
/* 7854 */         catch (NoSuchCreatureException nsc) {
/*      */           
/* 7856 */           buf.append("label{text=\"\"}label{text=\"" + lShops[x]
/* 7857 */               .getWurmId() + "\"};label{text=\"n/a\"};label{text=\"n/a\"};label{text=\"unknown\"};label{text=\"\"};label{text=\"n/a\"};label{text=\"n/a\"};");
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 7867 */       buf.append("}");
/*      */     } else {
/*      */       
/* 7870 */       buf.append("label{text=\"Nothing to show\"}");
/*      */     } 
/* 7872 */     if (pages > 0)
/* 7873 */       buf.append(nav); 
/* 7874 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String recipeTable(Recipe[] lRecipes) {
/* 7879 */     StringBuilder buf = new StringBuilder();
/* 7880 */     buf.append("label{text=\"total # of named recipes:" + lRecipes.length + "\"};");
/*      */ 
/*      */     
/* 7883 */     int pages = (lRecipes.length - 1) / this.rowsPerPage;
/* 7884 */     int start = this.currentPage * this.rowsPerPage;
/* 7885 */     int last = (start + this.rowsPerPage < lRecipes.length) ? (start + this.rowsPerPage) : lRecipes.length;
/* 7886 */     String nav = makeNav(this.currentPage, pages);
/*      */     
/* 7888 */     if (pages > 0) {
/* 7889 */       buf.append(nav);
/*      */     }
/* 7891 */     if (last > start) {
/*      */       
/* 7893 */       buf.append("table{rows=\"" + (last - start + 1) + "\";cols=\"4\";label{text=\"\"};label{type=\"bold\";text=\"Recipe Id\"};label{type=\"bold\";text=\"Recipe Name\"};label{type=\"bold\";text=\"Namer\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 7900 */       for (int x = start; x < last; x++) {
/*      */         
/* 7902 */         String namer = Recipes.getRecipeNamer(lRecipes[x].getRecipeId());
/* 7903 */         buf.append("label{text=\"\"};label{text=\"" + lRecipes[x]
/*      */             
/* 7905 */             .getRecipeId() + "\"};label{text=\"" + lRecipes[x]
/* 7906 */             .getRecipeName() + "\"};label{text=\"" + namer + "\"};");
/*      */       } 
/*      */ 
/*      */       
/* 7910 */       buf.append("}");
/*      */     } else {
/*      */       
/* 7913 */       buf.append("label{text=\"No recipes are named\"}");
/*      */     } 
/* 7915 */     if (pages > 0)
/* 7916 */       buf.append(nav); 
/* 7917 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String routesList() {
/* 7922 */     Route[] routes = Routes.getAllRoutes();
/* 7923 */     StringBuilder buf = new StringBuilder();
/* 7924 */     buf.append("label{text=\"total # of highway routes:" + routes.length + "\"};");
/*      */     
/* 7926 */     int pages = (routes.length - 1) / this.rowsPerPage;
/* 7927 */     int start = this.currentPage * this.rowsPerPage;
/* 7928 */     int last = (start + this.rowsPerPage < routes.length) ? (start + this.rowsPerPage) : routes.length;
/* 7929 */     String nav = makeNav(this.currentPage, pages);
/*      */     
/* 7931 */     if (pages > 0) {
/* 7932 */       buf.append(nav);
/*      */     }
/* 7934 */     if (last > start) {
/*      */       
/* 7936 */       buf.append("table{rows=\"" + (last - start + 1) + "\";cols=\"14\";label{text=\"\"};label{type=\"bold\";text=\"Id\"};label{text=\"\"};label{type=\"bold\";text=\"Coords\"};label{text=\"\"};label{type=\"bold\";text=\"Start node\"};label{text=\"\"};label{type=\"bold\";text=\"Catseyes\"};label{text=\"\"};label{type=\"bold\";text=\"Coords\"};label{text=\"\"};label{type=\"bold\";text=\"End waystone\"};label{type=\"bold\";text=\"Dist\"};label{type=\"bold\";text=\"Cost\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 7946 */       for (int x = start; x < last; x++) {
/*      */         
/* 7948 */         Node startNode = routes[x].getStartNode();
/* 7949 */         int posX = startNode.getWaystone().getTileX();
/* 7950 */         int posY = startNode.getWaystone().getTileY();
/* 7951 */         boolean onS = startNode.getWaystone().isOnSurface();
/* 7952 */         buf.append("radio{group=\"tid\";id=\"15," + routes[x].getId() + "," + '\024' + "\"};");
/* 7953 */         buf.append("label{text=\"" + routes[x].getId() + "\"};");
/* 7954 */         buf.append("radio{group=\"tid\";id=\"12," + posX + "," + posY + "," + onS + "\"};");
/* 7955 */         buf.append("label{text=\"(" + posX + "," + posY + "," + onS + ")\"}");
/* 7956 */         buf.append("radio{group=\"tid\";id=\"15," + startNode.getWaystone().getWurmId() + "," + '\026' + "\"};");
/* 7957 */         buf.append("label{text=\"" + startNode.getWaystone().getWurmId() + "\"};");
/* 7958 */         buf.append("radio{group=\"tid\";id=\"15," + routes[x].getId() + "," + '\027' + "\"};");
/* 7959 */         buf.append("label{text=\"" + (routes[x].getCatseyes()).length + " catseyes\"};");
/* 7960 */         Node endNode = routes[x].getEndNode();
/* 7961 */         if (endNode != null) {
/*      */           
/* 7963 */           int endX = endNode.getWaystone().getTileX();
/* 7964 */           int endY = endNode.getWaystone().getTileY();
/* 7965 */           boolean enS = endNode.getWaystone().isOnSurface();
/*      */           
/* 7967 */           buf.append("radio{group=\"tid\";id=\"12," + endX + "," + endY + "," + enS + "\"};");
/* 7968 */           buf.append("label{text=\"(" + endX + "," + endY + "," + enS + ")\"}");
/* 7969 */           buf.append("radio{group=\"tid\";id=\"15," + endNode.getWaystone().getWurmId() + "," + '\026' + "\"};");
/* 7970 */           buf.append("label{text=\"" + endNode.getWaystone().getWurmId() + "\"};");
/*      */         }
/*      */         else {
/*      */           
/* 7974 */           buf.append("label{text=\"\"};");
/* 7975 */           buf.append("label{text=\"\"};");
/* 7976 */           buf.append("label{text=\"\"};");
/* 7977 */           buf.append("label{text=\"missing\"};");
/*      */         } 
/* 7979 */         buf.append("label{text=\"" + this.df.format(routes[x].getDistance()) + "\"};");
/* 7980 */         buf.append("label{text=\"" + this.df.format(routes[x].getCost()) + "\"};");
/*      */       } 
/* 7982 */       buf.append("}");
/*      */     } else {
/*      */       
/* 7985 */       buf.append("label{text=\"No highway routes found\"}");
/*      */     } 
/* 7987 */     if (pages > 0)
/* 7988 */       buf.append(nav); 
/* 7989 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String wagonerList() {
/* 7994 */     Wagoner[] wagoners = Wagoner.getAllWagoners();
/* 7995 */     StringBuilder buf = new StringBuilder();
/* 7996 */     buf.append("label{text=\"total # of wagoners:" + wagoners.length + "\"};");
/*      */     
/* 7998 */     int pages = (wagoners.length - 1) / this.rowsPerPage;
/* 7999 */     int start = this.currentPage * this.rowsPerPage;
/* 8000 */     int last = (start + this.rowsPerPage < wagoners.length) ? (start + this.rowsPerPage) : wagoners.length;
/* 8001 */     String nav = makeNav(this.currentPage, pages);
/*      */     
/* 8003 */     if (pages > 0) {
/* 8004 */       buf.append(nav);
/*      */     }
/* 8006 */     if (last > start) {
/*      */       
/* 8008 */       buf.append("table{rows=\"" + (last - start + 1) + "\";cols=\"12\";label{text=\"\"};label{type=\"bold\";text=\"Wurm Id\"};label{text=\"\"};label{type=\"bold\";text=\"Name\"};label{text=\"\"};label{type=\"bold\";text=\"Current Coords\"};label{text=\"\"};label{type=\"bold\";text=\"State\"};label{text=\"\"};label{type=\"bold\";text=\"Camp Coords\"};label{text=\"\"};label{type=\"bold\";text=\"Doing Delivery\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 8016 */       for (int x = start; x < last; x++) {
/*      */         
/* 8018 */         int posX = -1;
/* 8019 */         int posY = -1;
/* 8020 */         boolean onS = false;
/* 8021 */         Creature creature = wagoners[x].getCreature();
/* 8022 */         if (creature != null) {
/*      */           
/* 8024 */           posX = creature.getTileX();
/* 8025 */           posY = creature.getTileY();
/* 8026 */           onS = creature.isOnSurface();
/*      */         } 
/*      */         
/* 8029 */         int campX = -1;
/* 8030 */         int campY = -1;
/* 8031 */         boolean campS = true;
/*      */         
/*      */         try {
/* 8034 */           Item home = Items.getItem(wagoners[x].getHomeWaystoneId());
/* 8035 */           campX = home.getTileX();
/* 8036 */           campY = home.getTileY();
/* 8037 */           campS = home.isOnSurface();
/*      */         }
/* 8039 */         catch (NoSuchItemException noSuchItemException) {}
/*      */ 
/*      */ 
/*      */         
/* 8043 */         buf.append("radio{group=\"tid\";id=\"16," + wagoners[x].getWurmId() + "," + '\001' + "\"};");
/* 8044 */         buf.append("label{text=\"" + wagoners[x].getWurmId() + "\"};");
/* 8045 */         buf.append("label{text=\"\"}");
/* 8046 */         buf.append("label{text=\"" + wagoners[x].getName() + "\"};");
/* 8047 */         buf.append("radio{group=\"tid\";id=\"12," + posX + "," + posY + "," + onS + "\"};");
/* 8048 */         buf.append("label{text=\"(" + posX + "," + posY + "," + onS + ")\"}");
/* 8049 */         buf.append("label{text=\"\"}");
/* 8050 */         buf.append("label{text=\"" + wagoners[x].getStateName() + "\"};");
/* 8051 */         if (campX == -1) {
/*      */           
/* 8053 */           buf.append("label{text=\"\"}");
/* 8054 */           buf.append("label{text=\"camp missing ?\"};");
/*      */         }
/*      */         else {
/*      */           
/* 8058 */           buf.append("radio{group=\"tid\";id=\"12," + campX + "," + campY + "," + campS + "\"};");
/* 8059 */           buf.append("label{text=\"(" + campX + "," + campY + "," + campS + ")\"}");
/*      */         } 
/* 8061 */         if (wagoners[x].getDeliveryId() == -10L) {
/*      */           
/* 8063 */           buf.append("label{text=\"\"}");
/* 8064 */           buf.append("label{text=\"none\"}");
/*      */         
/*      */         }
/*      */         else {
/*      */           
/* 8069 */           buf.append("label{text=\"\"}");
/* 8070 */           buf.append("label{text=\"" + wagoners[x].getDeliveryId() + "\"}");
/*      */         } 
/*      */       } 
/* 8073 */       buf.append("}");
/*      */     } else {
/*      */       
/* 8076 */       buf.append("label{text=\"No wagoners found\"}");
/*      */     } 
/* 8078 */     if (pages > 0)
/* 8079 */       buf.append(nav); 
/* 8080 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String nodesList() {
/* 8085 */     Node[] nodes = Routes.getAllNodes();
/* 8086 */     StringBuilder buf = new StringBuilder();
/* 8087 */     buf.append("label{text=\"total # of highway nodes:" + nodes.length + "\"};");
/*      */ 
/*      */     
/* 8090 */     int pages = (nodes.length - 1) / this.rowsPerPage;
/* 8091 */     int start = this.currentPage * this.rowsPerPage;
/* 8092 */     int last = (start + this.rowsPerPage < nodes.length) ? (start + this.rowsPerPage) : nodes.length;
/* 8093 */     String nav = makeNav(this.currentPage, pages);
/*      */     
/* 8095 */     if (pages > 0) {
/* 8096 */       buf.append(nav);
/*      */     }
/* 8098 */     if (last > start) {
/*      */       
/* 8100 */       buf.append("table{rows=\"" + (last - start + 1) + "\";cols=\"36\";label{text=\"\"};label{type=\"bold\";text=\"Highway Node\"};label{text=\"\"};label{type=\"bold\";text=\"Coords\"};label{text=\"\"};label{type=\"bold\";text=\"N\"};label{text=\"\"};label{text=\"\"};label{text=\"\"};label{type=\"bold\";text=\"NE\"};label{text=\"\"};label{text=\"\"};label{text=\"\"};label{type=\"bold\";text=\"E\"};label{text=\"\"};label{text=\"\"};label{text=\"\"};label{type=\"bold\";text=\"SE\"};label{text=\"\"};label{text=\"\"};label{text=\"\"};label{type=\"bold\";text=\"S\"};label{text=\"\"};label{text=\"\"};label{text=\"\"};label{type=\"bold\";text=\"SW\"};label{text=\"\"};label{text=\"\"};label{text=\"\"};label{type=\"bold\";text=\"W\"};label{text=\"\"};label{text=\"\"};label{text=\"\"};label{type=\"bold\";text=\"NW\"};label{text=\"\"};label{text=\"\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 8120 */       for (int x = start; x < last; x++) {
/*      */         
/* 8122 */         int posX = nodes[x].getWaystone().getTileX();
/* 8123 */         int posY = nodes[x].getWaystone().getTileY();
/* 8124 */         boolean onS = nodes[x].getWaystone().isOnSurface();
/* 8125 */         buf.append("radio{group=\"tid\";id=\"15," + nodes[x].getWaystone().getWurmId() + "," + '\026' + "\"};");
/* 8126 */         buf.append("label{text=\"" + nodes[x].getWaystone().getWurmId() + "\"};");
/* 8127 */         buf.append("radio{group=\"tid\";id=\"12," + posX + "," + posY + "," + onS + "\"};");
/* 8128 */         buf.append("label{text=\"(" + posX + "," + posY + "," + onS + ")\"}");
/*      */         
/* 8130 */         buf.append(nodeRow(nodes[x], (byte)1));
/* 8131 */         buf.append(nodeRow(nodes[x], (byte)2));
/* 8132 */         buf.append(nodeRow(nodes[x], (byte)4));
/* 8133 */         buf.append(nodeRow(nodes[x], (byte)8));
/* 8134 */         buf.append(nodeRow(nodes[x], (byte)16));
/* 8135 */         buf.append(nodeRow(nodes[x], (byte)32));
/* 8136 */         buf.append(nodeRow(nodes[x], (byte)64));
/* 8137 */         buf.append(nodeRow(nodes[x], -128));
/*      */       } 
/* 8139 */       buf.append("}");
/*      */     } else {
/*      */       
/* 8142 */       buf.append("label{text=\"No highway nodes found\"}");
/*      */     } 
/* 8144 */     if (pages > 0)
/* 8145 */       buf.append(nav); 
/* 8146 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String nodeRow(Node node, byte dir) {
/* 8151 */     StringBuilder buf = new StringBuilder();
/*      */ 
/*      */     
/* 8154 */     Route route = node.getRoute(dir);
/* 8155 */     if (route != null) {
/*      */ 
/*      */       
/* 8158 */       Route oppRoute = route.getOppositeRoute();
/* 8159 */       buf.append("radio{group=\"tid\";id=\"15," + route.getId() + "," + '\024' + "\"};");
/* 8160 */       buf.append("label{color=\"127,255,127\"text=\"R" + route.getId() + "\"};");
/* 8161 */       if (oppRoute == null)
/*      */       {
/* 8163 */         buf.append("label{text=\"\"};");
/* 8164 */         buf.append("label{text=\"\"};");
/*      */       }
/*      */       else
/*      */       {
/* 8168 */         buf.append("radio{group=\"tid\";id=\"15," + oppRoute.getId() + "," + '\024' + "\"};");
/* 8169 */         buf.append("label{color=\"255,177,40\"text=\"R" + oppRoute.getId() + "\"};");
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/* 8174 */       buf.append("label{text=\"\"};");
/* 8175 */       buf.append("label{text=\"\"};");
/* 8176 */       buf.append("label{text=\"\"};");
/* 8177 */       buf.append("label{text=\"\"};");
/*      */     } 
/* 8179 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String riftsTable() {
/* 8184 */     StringBuilder buf = new StringBuilder();
/* 8185 */     Rift activeRift = Rift.getActiveRift();
/* 8186 */     Rift lastRift = Rift.getLastRift();
/* 8187 */     int count = 0;
/* 8188 */     if (activeRift != null)
/* 8189 */       count++; 
/* 8190 */     if (lastRift != null)
/* 8191 */       count++; 
/* 8192 */     buf.append("label{text=\"total # of rifts:" + count + "\"};");
/*      */     
/* 8194 */     if (count > 0) {
/*      */       
/* 8196 */       buf.append("table{rows=\"1\";cols=\"7\";label{text=\"\"};label{type=\"bold\";text=\"Type\"};label{type=\"bold\";text=\"Id\"};label{type=\"bold\";text=\"\"};label{type=\"bold\";text=\"Coords\"};label{type=\"bold\";text=\"Started\"};label{type=\"bold\";text=\"Ended\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 8205 */       if (activeRift != null) {
/*      */         
/* 8207 */         buf.append(riftRow(activeRift));
/* 8208 */         if (lastRift != null && lastRift != activeRift) {
/* 8209 */           buf.append(riftRow(lastRift));
/*      */         }
/* 8211 */       } else if (lastRift != null) {
/* 8212 */         buf.append(riftRow(lastRift));
/* 8213 */       }  buf.append("}");
/*      */     } else {
/*      */       
/* 8216 */       buf.append("label{text=\"Nothing to show\"}");
/* 8217 */     }  return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String riftRow(Rift rift) {
/* 8222 */     int posX = (rift.getCenterPos()).x;
/* 8223 */     int posY = (rift.getCenterPos()).y;
/* 8224 */     String type = rift.isActive() ? "Active" : "Last";
/* 8225 */     StringBuilder buf = new StringBuilder();
/* 8226 */     buf.append("label{text=\"\"};label{text=\"" + type + "\"};label{text=\"" + rift
/*      */         
/* 8228 */         .getNumber() + "\"};radio{group=\"tid\";id=\"" + '\f' + "," + posX + "," + posY + ",true\"};label{text=\"(" + posX + "," + posY + ",true)\"}label{text=\"" + rift
/*      */ 
/*      */         
/* 8231 */         .getActivated().toString() + "\"};label{text=\"" + (
/* 8232 */         rift.isActive() ? "Running" : rift.getEnded().toString()) + "\"};");
/*      */     
/* 8234 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String serversTable(ServerEntry[] lServers) {
/* 8239 */     StringBuilder buf = new StringBuilder();
/* 8240 */     buf.append("label{text=\"total # of servers:" + lServers.length + "\"};");
/* 8241 */     Arrays.sort((Object[])lServers);
/*      */     
/* 8243 */     int pages = (lServers.length - 1) / this.rowsPerPage;
/* 8244 */     int start = this.currentPage * this.rowsPerPage;
/* 8245 */     int last = (start + this.rowsPerPage < lServers.length) ? (start + this.rowsPerPage) : lServers.length;
/* 8246 */     String nav = makeNav(this.currentPage, pages);
/*      */     
/* 8248 */     if (pages > 0) {
/* 8249 */       buf.append(nav);
/*      */     }
/* 8251 */     if (last > start) {
/*      */       
/* 8253 */       buf.append("table{rows=\"" + (last - start + 1) + "\";cols=\"11\";label{text=\"\"};label{type=\"bold\";text=\"Id\"};label{type=\"bold\";text=\"Name\"};label{type=\"bold\";text=\"Abbreviation\"};label{type=\"bold\";text=\"Local\"};label{type=\"bold\";text=\"PvP\"};label{type=\"bold\";text=\"Epic\"};label{type=\"bold\";text=\"Chaos\"};label{type=\"bold\";text=\"HomeServer\"};label{type=\"bold\";text=\"Chaos\"};label{type=\"bold\";text=\"Kingdom\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 8266 */       for (int x = start; x < last; x++)
/*      */       {
/* 8268 */         buf.append("radio{group=\"tid\";id=\"14," + lServers[x].getId() + "," + '\001' + "\"};label{text=\"" + lServers[x]
/* 8269 */             .getId() + "\"};label{text=\"" + lServers[x]
/* 8270 */             .getName() + "\"};label{text=\"" + lServers[x]
/* 8271 */             .getAbbreviation() + "\"};label{" + 
/* 8272 */             showBoolean((lServers[x]).isLocal) + "};label{" + 
/* 8273 */             showBoolean((lServers[x]).PVPSERVER) + "};label{" + 
/* 8274 */             showBoolean((lServers[x]).EPIC) + "};label{" + 
/* 8275 */             showBoolean(lServers[x].isChaosServer()) + "};label{" + 
/* 8276 */             showBoolean((lServers[x]).HOMESERVER) + "};label{" + 
/* 8277 */             showBoolean((lServers[x]).ISPAYMENT) + "};label{text=\"" + lServers[x]
/* 8278 */             .getKingdom() + "\"};");
/*      */       }
/*      */       
/* 8281 */       buf.append("}");
/*      */     } else {
/*      */       
/* 8284 */       buf.append("label{text=\"Nothing to show\"}");
/*      */     } 
/* 8286 */     if (pages > 0)
/* 8287 */       buf.append(nav); 
/* 8288 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String deityAltarsTable(Item[] altars, String aType) {
/* 8293 */     StringBuilder buf = new StringBuilder();
/* 8294 */     buf.append("label{text=\"total # of " + aType + " Altars:" + altars.length + "\"};");
/* 8295 */     Arrays.sort((Object[])altars);
/*      */     
/* 8297 */     int pages = (altars.length - 1) / this.rowsPerPage;
/* 8298 */     int start = this.currentPage * this.rowsPerPage;
/* 8299 */     int last = (start + this.rowsPerPage < altars.length) ? (start + this.rowsPerPage) : altars.length;
/* 8300 */     String nav = makeNav(this.currentPage, pages);
/*      */     
/* 8302 */     if (pages > 0) {
/* 8303 */       buf.append(nav);
/*      */     }
/* 8305 */     if (last > start) {
/*      */       
/* 8307 */       buf.append("table{rows=\"" + (last - start + 1) + "\";cols=\"6\";label{text=\"\"};label{type=\"bold\";text=\"Wurm Id\"};label{type=\"bold\";text=\"Name\"};label{type=\"bold\";text=\"\"};label{type=\"bold\";text=\"Coords\"};label{type=\"bold\";text=\"Owner\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 8314 */       for (int x = start; x < last; x++) {
/*      */         
/* 8316 */         Item item = altars[x];
/* 8317 */         int posX = item.getTileX();
/* 8318 */         int posY = item.getTileY();
/* 8319 */         boolean onSurface = item.isOnSurface();
/* 8320 */         String owner = "";
/* 8321 */         if (item.getOwnerId() != -10L) {
/*      */           
/* 8323 */           PlayerInfo pInfo = PlayerInfoFactory.getPlayerInfoWithWurmId(item.getOwnerId());
/* 8324 */           if (pInfo != null) {
/* 8325 */             owner = pInfo.getName();
/*      */           } else {
/* 8327 */             owner = "" + item.getOwnerId();
/*      */           } 
/*      */         } else {
/*      */           
/* 8331 */           PlayerInfo pInfo = PlayerInfoFactory.getPlayerInfoWithWurmId(item.lastOwner);
/* 8332 */           if (pInfo != null) {
/* 8333 */             owner = pInfo.getName();
/*      */           } else {
/* 8335 */             owner = "" + item.lastOwner;
/*      */           } 
/*      */         } 
/* 8338 */         buf.append("radio{group=\"tid\";id=\"1," + altars[x].getWurmId() + "," + '\001' + "\"};label{text=\"" + altars[x]
/* 8339 */             .getWurmId() + "\"};" + 
/* 8340 */             itemNameWithColorByRarity(altars[x]) + "radio{group=\"tid\";id=\"" + '\f' + "," + posX + "," + posY + "," + onSurface + "\"};label{text=\"(" + posX + "," + posY + "," + onSurface + ")\"}label{text=\"" + owner + "\"}");
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 8345 */       buf.append("}");
/*      */     } else {
/*      */       
/* 8348 */       buf.append("label{text=\"Nothing to show\"}");
/*      */     } 
/* 8350 */     if (pages > 0)
/* 8351 */       buf.append(nav); 
/* 8352 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String corpseTable(Set<Item> corpses, PlayerInfo pInfo) {
/* 8357 */     Item[] items = corpses.<Item>toArray(new Item[corpses.size()]);
/* 8358 */     StringBuilder buf = new StringBuilder();
/* 8359 */     buf.append("label{text=\"total # of Corpses of " + pInfo.getName() + ":" + corpses.size() + "\"};");
/*      */ 
/*      */     
/* 8362 */     int pages = (corpses.size() - 1) / this.rowsPerPage;
/* 8363 */     int start = this.currentPage * this.rowsPerPage;
/* 8364 */     int last = (start + this.rowsPerPage < corpses.size()) ? (start + this.rowsPerPage) : corpses.size();
/* 8365 */     String nav = makeNav(this.currentPage, pages);
/*      */     
/* 8367 */     if (pages > 0) {
/* 8368 */       buf.append(nav);
/*      */     }
/* 8370 */     if (last > start) {
/*      */       
/* 8372 */       buf.append("table{rows=\"" + (last - start + 1) + "\";cols=\"5\";label{text=\"\"};label{type=\"bold\";text=\"Wurm Id\"};label{type=\"bold\";text=\"Name\"};label{type=\"bold\";text=\"\"};label{type=\"bold\";text=\"Coords\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 8378 */       for (int x = start; x < last; x++) {
/*      */         
/* 8380 */         Item item = items[x];
/* 8381 */         int posX = item.getTileX();
/* 8382 */         int posY = item.getTileY();
/* 8383 */         boolean onSurface = item.isOnSurface();
/*      */         
/* 8385 */         buf.append("radio{group=\"tid\";id=\"1," + items[x].getWurmId() + "," + '\001' + "\"};label{text=\"" + items[x]
/* 8386 */             .getWurmId() + "\"};" + 
/* 8387 */             itemNameWithColorByRarity(items[x]) + "radio{group=\"tid\";id=\"" + '\f' + "," + posX + "," + posY + "," + onSurface + "\"};label{text=\"(" + posX + "," + posY + "," + onSurface + ")\"}");
/*      */       } 
/*      */ 
/*      */       
/* 8391 */       buf.append("}");
/*      */     } else {
/*      */       
/* 8394 */       buf.append("label{text=\"Nothing to show\"}");
/*      */     } 
/* 8396 */     if (pages > 0)
/* 8397 */       buf.append(nav); 
/* 8398 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String mailTable(WurmMail[] mails) {
/* 8403 */     StringBuilder buf = new StringBuilder();
/* 8404 */     buf.append("label{text=\"total # of items:" + mails.length + "\"};");
/*      */     
/* 8406 */     int pages = (mails.length - 1) / this.rowsPerPage;
/* 8407 */     int start = this.currentPage * this.rowsPerPage;
/* 8408 */     int last = (start + this.rowsPerPage < mails.length) ? (start + this.rowsPerPage) : mails.length;
/* 8409 */     String nav = makeNav(this.currentPage, pages);
/*      */     
/* 8411 */     if (pages > 0) {
/* 8412 */       buf.append(nav);
/*      */     }
/* 8414 */     if (last > start) {
/*      */       
/* 8416 */       buf.append("table{rows=\"" + (last - start + 1) + "\";cols=\"5\";label{text=\"\"};label{type=\"bold\";text=\"Sender\"};label{type=\"bold\";text=\"To\"};label{type=\"bold\";text=\"Name\"};label{type=\"bold\";text=\"Price\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 8422 */       for (int x = start; x < last; x++) {
/*      */         
/* 8424 */         PlayerState sps = PlayerInfoFactory.getPlayerState((mails[x]).sender);
/* 8425 */         PlayerState rps = PlayerInfoFactory.getPlayerState((mails[x]).receiver);
/* 8426 */         String sName = (sps != null) ? sps.getPlayerName() : ("(Unk:" + (mails[x]).sender + ")");
/* 8427 */         String rName = (rps != null) ? rps.getPlayerName() : ("(Unk:" + (mails[x]).receiver + ")");
/*      */ 
/*      */         
/*      */         try {
/* 8431 */           Item item = Items.getItem((mails[x]).itemId);
/* 8432 */           ServerEntry se = Servers.getServerWithId(mails[x].getSourceserver());
/* 8433 */           String sourceServer = " (Unk)";
/* 8434 */           if (se != null)
/* 8435 */             sourceServer = " (" + se.getAbbreviation() + ")"; 
/* 8436 */           buf.append("radio{group=\"tid\";id=\"1," + (mails[x]).itemId + "," + '\001' + "\"};label{text=\"" + sName + sourceServer + "\"};label{text=\"" + rName + "\"};" + 
/*      */ 
/*      */               
/* 8439 */               itemNameWithColorByRarity(item) + "label{text=\"" + (
/* 8440 */               (mails[x].getType() == 0) ? "" : "COD ") + (new Change(mails[x]
/* 8441 */                 .getPrice())).getChangeShortString() + "\"};");
/*      */         }
/* 8443 */         catch (NoSuchItemException e) {
/*      */           
/* 8445 */           logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/* 8446 */           buf.append("label{text=\"\"};label{text=\"" + sName + "\"};label{text=\"" + rName + "\"};label{type=\"italic\";text=\"" + (mails[x]).itemId + "\"};label{text=\"" + (
/*      */ 
/*      */ 
/*      */               
/* 8450 */               (mails[x].getType() == 0) ? "" : "COD ") + (new Change(mails[x]
/* 8451 */                 .getPrice())).getChangeShortString() + "\"};");
/*      */         } 
/*      */       } 
/* 8454 */       buf.append("}");
/*      */     } else {
/*      */       
/* 8457 */       buf.append("label{text=\"Nothing to show\"}");
/*      */     } 
/* 8459 */     if (pages > 0)
/* 8460 */       buf.append(nav); 
/* 8461 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String villageHistoryTable(HistoryEvent[] histories) {
/* 8466 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 8468 */     buf.append("label{text=\"# of history entries:" + histories.length + "\"};");
/*      */     
/* 8470 */     int pages = (histories.length - 1) / this.rowsPerPage;
/* 8471 */     int start = this.currentPage * this.rowsPerPage;
/* 8472 */     int last = (start + this.rowsPerPage < histories.length) ? (start + this.rowsPerPage) : histories.length;
/* 8473 */     String nav = makeNav(this.currentPage, pages);
/*      */     
/* 8475 */     if (pages > 0) {
/* 8476 */       buf.append(nav);
/*      */     }
/* 8478 */     if (last > start) {
/*      */       
/* 8480 */       buf.append("table{rows=\"" + (last - start + 1) + "\";cols=\"3\";label{text=\"Dated\"};label{text=\"Who\"};label{text=\"Event\"};");
/*      */ 
/*      */ 
/*      */       
/* 8484 */       for (int x = start; x < last; x++) {
/*      */         
/* 8486 */         HistoryEvent hEvent = histories[x];
/* 8487 */         buf.append("label{text=\"" + hEvent.getDate() + "\"};label{text=\"" + ((hEvent.performer == null) ? "" : hEvent.performer) + "\"};label{text=\"" + hEvent.event + "\"};");
/*      */       } 
/*      */ 
/*      */       
/* 8491 */       buf.append("}");
/*      */     } else {
/*      */       
/* 8494 */       buf.append("label{text=\"Nothing to show\"}");
/*      */     } 
/* 8496 */     if (pages > 0) {
/* 8497 */       buf.append(nav);
/*      */     }
/* 8499 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String historyTable(PermissionsHistoryEntry[] histories) {
/* 8504 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 8506 */     buf.append("label{text=\"# of history entries:" + histories.length + "\"};");
/*      */     
/* 8508 */     int pages = (histories.length - 1) / this.rowsPerPage;
/* 8509 */     int start = this.currentPage * this.rowsPerPage;
/* 8510 */     int last = (start + this.rowsPerPage < histories.length) ? (start + this.rowsPerPage) : histories.length;
/* 8511 */     String nav = makeNav(this.currentPage, pages);
/*      */     
/* 8513 */     if (pages > 0) {
/* 8514 */       buf.append(nav);
/*      */     }
/* 8516 */     if (last > start) {
/*      */       
/* 8518 */       buf.append("table{rows=\"" + (last - start + 1) + "\";cols=\"3\";label{text=\"Dated\"};label{text=\"Who\"};label{text=\"Event\"};");
/*      */ 
/*      */ 
/*      */       
/* 8522 */       for (int x = start; x < last; x++) {
/*      */         
/* 8524 */         PermissionsHistoryEntry hEvent = histories[x];
/* 8525 */         buf.append("label{text=\"" + hEvent.getDate() + "\"};label{text=\"" + ((hEvent.performer == null) ? "" : hEvent.performer) + "\"};label{text=\"" + hEvent.event + "\"};");
/*      */       } 
/*      */ 
/*      */       
/* 8529 */       buf.append("}");
/*      */     } else {
/*      */       
/* 8532 */       buf.append("label{text=\"Nothing to show\"}");
/*      */     } 
/* 8534 */     if (pages > 0) {
/* 8535 */       buf.append(nav);
/*      */     }
/* 8537 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String kosVillageTable(Village[] lvillages, PlayerInfo pInfo) {
/* 8542 */     StringBuilder buf = new StringBuilder();
/* 8543 */     Arrays.sort((Object[])lvillages);
/* 8544 */     buf.append("label{text=\"total # of villages:" + lvillages.length + "\"};");
/*      */     
/* 8546 */     int pages = (lvillages.length - 1) / this.rowsPerPage;
/* 8547 */     int start = this.currentPage * this.rowsPerPage;
/* 8548 */     int last = (start + this.rowsPerPage < lvillages.length) ? (start + this.rowsPerPage) : lvillages.length;
/* 8549 */     String nav = makeNav(this.currentPage, pages);
/*      */     
/* 8551 */     if (pages > 0) {
/* 8552 */       buf.append(nav);
/*      */     }
/* 8554 */     if (last > start) {
/*      */       
/* 8556 */       buf.append("table{rows=\"" + (last - start + 1) + "\";cols=\"3\";label{text=\"\"};label{type=\"bold\";text=\"Village\"};label{type=\"bold\";text=\"Reputation\"};");
/*      */ 
/*      */ 
/*      */       
/* 8560 */       for (int x = start; x < last; x++) {
/*      */         
/* 8562 */         Village lvillage = lvillages[x];
/* 8563 */         Reputation rep = lvillage.getReputationObject(pInfo.wurmId);
/*      */         
/* 8565 */         buf.append("radio{group=\"tid\";id=\"2," + lvillage.getId() + "," + '\001' + "\"};label{text=\"" + lvillage
/* 8566 */             .getName() + "\"};label{text=\"" + rep
/* 8567 */             .getValue() + "\"};");
/*      */       } 
/* 8569 */       buf.append("}");
/*      */     } else {
/*      */       
/* 8572 */       buf.append("label{text=\"Nothing to show\"}");
/*      */     } 
/* 8574 */     if (pages > 0) {
/* 8575 */       buf.append(nav);
/*      */     }
/* 8577 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String deityTable(Deity[] deities) {
/* 8582 */     StringBuilder buf = new StringBuilder();
/* 8583 */     buf.append("label{text=\"total # of Deities:" + deities.length + "\"};");
/*      */ 
/*      */     
/* 8586 */     int pages = (deities.length - 1) / this.rowsPerPage;
/* 8587 */     int start = this.currentPage * this.rowsPerPage;
/* 8588 */     int last = (start + this.rowsPerPage < deities.length) ? (start + this.rowsPerPage) : deities.length;
/* 8589 */     String nav = makeNav(this.currentPage, pages);
/*      */     
/* 8591 */     if (pages > 0) {
/* 8592 */       buf.append(nav);
/*      */     }
/* 8594 */     if (last > start) {
/*      */       
/* 8596 */       buf.append("table{rows=\"" + (last - start + 1) + "\";cols=\"9\";label{text=\"\"};label{type=\"bold\";text=\"Id\"};label{type=\"bold\";text=\"Name\"};label{text=\"\"};label{type=\"bold\";text=\"Priests\"};label{text=\"\"};label{type=\"bold\";text=\"Followers\"};label{text=\"\"};label{type=\"bold\";text=\"Altars\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 8608 */       for (int x = start; x < last; x++) {
/*      */         
/* 8610 */         int numbPriests = (PlayerInfoFactory.getActivePriestsForDeity(deities[x].getNumber())).length;
/* 8611 */         int numbFollowers = (PlayerInfoFactory.getActiveFollowersForDeity(deities[x].getNumber())).length;
/* 8612 */         int numbAltars = (Zones.getAltars(deities[x].getNumber())).length;
/*      */         
/* 8614 */         buf.append("radio{group=\"tid\";id=\"13," + deities[x].getNumber() + "," + '\001' + "\"};label{text=\"" + deities[x]
/* 8615 */             .getNumber() + "\"};label{text=\"" + deities[x]
/* 8616 */             .getName() + "\"}" + ((numbPriests == 0) ? "label{text=\"\"}" : ("radio{group=\"tid\";id=\"13," + deities[x]
/*      */             
/* 8618 */             .getNumber() + "," + '\007' + "\"};")) + "label{text=\"" + numbPriests + "\"}" + ((numbFollowers == 0) ? "label{text=\"\"}" : ("radio{group=\"tid\";id=\"13," + deities[x]
/*      */ 
/*      */             
/* 8621 */             .getNumber() + "," + '\b' + "\"};")) + "label{text=\"" + numbFollowers + "\"}" + ((numbAltars == 0) ? "label{text=\"\"}" : ("radio{group=\"tid\";id=\"13," + deities[x]
/*      */ 
/*      */             
/* 8624 */             .getNumber() + "," + '\t' + "\"};")) + "label{text=\"" + numbAltars + "\"}");
/*      */       } 
/*      */ 
/*      */       
/* 8628 */       buf.append("}");
/*      */     } else {
/*      */       
/* 8631 */       buf.append("label{text=\"Nothing to show\"}");
/*      */     } 
/* 8633 */     if (pages > 0)
/* 8634 */       buf.append(nav); 
/* 8635 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String deityValreiFightsListTable(ArrayList<ValreiFightHistory> fights) {
/* 8640 */     StringBuilder buf = new StringBuilder();
/* 8641 */     buf.append("label{text=\"total # of fights:" + fights.size() + "\"};");
/*      */ 
/*      */     
/* 8644 */     int pages = (fights.size() - 1) / this.rowsPerPage;
/* 8645 */     int start = this.currentPage * this.rowsPerPage;
/* 8646 */     int last = (start + this.rowsPerPage < fights.size()) ? (start + this.rowsPerPage) : fights.size();
/* 8647 */     String nav = makeNav(this.currentPage, pages);
/*      */     
/* 8649 */     if (pages > 0) {
/* 8650 */       buf.append(nav);
/*      */     }
/* 8652 */     if (last > start) {
/*      */       
/* 8654 */       buf.append("table{rows=\"" + (last - start + 1) + "\";cols=\"3\";label{text=\"\"};label{type=\"bold\";text=\"FightId\"};label{type=\"bold\";text=\"Preview\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 8660 */       for (int x = start; x < last; x++) {
/*      */         
/* 8662 */         ValreiFightHistory fight = fights.get(x);
/*      */         
/* 8664 */         buf.append("radio{group=\"tid\";id=\"13," + fight.getFightId() + "," + '\n' + "\"};label{text=\"" + fight
/* 8665 */             .getFightId() + "\"};label{text=\"" + fight
/* 8666 */             .getPreviewString() + "\"}");
/*      */       } 
/*      */       
/* 8669 */       buf.append("}");
/*      */     } else {
/*      */       
/* 8672 */       buf.append("label{text=\"Nothing to show\"}");
/*      */     } 
/* 8674 */     if (pages > 0)
/* 8675 */       buf.append(nav); 
/* 8676 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String deityFollowersTable(PlayerInfo[] followers, String deityName, String aType) {
/* 8681 */     StringBuilder buf = new StringBuilder();
/* 8682 */     buf.append("label{text=\"total # of " + deityName + " " + aType + ":" + followers.length + "\"};");
/* 8683 */     Arrays.sort((Object[])followers);
/*      */     
/* 8685 */     int pages = (followers.length - 1) / this.rowsPerPage;
/* 8686 */     int start = this.currentPage * this.rowsPerPage;
/* 8687 */     int last = (start + this.rowsPerPage < followers.length) ? (start + this.rowsPerPage) : followers.length;
/* 8688 */     String nav = makeNav(this.currentPage, pages);
/*      */     
/* 8690 */     if (pages > 0) {
/* 8691 */       buf.append(nav);
/*      */     }
/* 8693 */     if (last > start) {
/*      */       
/* 8695 */       buf.append("table{rows=\"" + (last - start + 1) + "\";cols=\"6\";label{text=\"\"};label{type=\"bold\";text=\"Wurm Id\"};label{type=\"bold\";text=\"Name\"};label{type=\"bold\";text=\"Priest\"};label{type=\"bold\";text=\"Faith\"};label{type=\"bold\";text=\"Favor\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 8703 */       for (int x = start; x < last; x++)
/*      */       {
/* 8705 */         buf.append("radio{group=\"tid\";id=\"1," + (followers[x]).wurmId + "," + '\001' + "\"};label{text=\"" + (followers[x]).wurmId + "\"};label{text=\"" + followers[x]
/*      */             
/* 8707 */             .getName() + "\"};label{" + 
/* 8708 */             showBoolean((followers[x]).isPriest) + "};label{text=\"" + followers[x]
/* 8709 */             .getFaith() + "\"}label{text=\"" + followers[x]
/* 8710 */             .getFavor() + "\"}");
/*      */       }
/*      */       
/* 8713 */       buf.append("}");
/*      */     } else {
/*      */       
/* 8716 */       buf.append("label{text=\"Nothing to show\"}");
/*      */     } 
/* 8718 */     if (pages > 0)
/* 8719 */       buf.append(nav); 
/* 8720 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String makeNav(int aCurrentPage, int aTotalPages) {
/* 8725 */     if (aTotalPages > 0) {
/* 8726 */       return "harray{" + ((aCurrentPage > 0) ? "radio{group=\"tid\";id=\"7\";text=\"Prev \"};" : "") + "label{text=\"Page " + (aCurrentPage + 1) + " of " + (aTotalPages + 1) + " \"};" + ((aCurrentPage < aTotalPages) ? "radio{group=\"tid\";id=\"8\";text=\"Next\"};" : "") + "}";
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 8732 */     return "";
/*      */   }
/*      */ 
/*      */   
/*      */   private String addBridge(long bridgeId) {
/* 8737 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 8739 */     if (bridgeId != -10L) {
/*      */       
/*      */       try
/*      */       {
/*      */         
/* 8744 */         Structure lStructure = Structures.getStructure(bridgeId);
/* 8745 */         if (lStructure != null && lStructure.isTypeBridge()) {
/* 8746 */           buf.append("radio{group=\"tid\";id=\"1," + bridgeId + "," + '\001' + "\"};label{text=\"On Bridge\"};label{text=\"\"};label{text=\"" + lStructure
/*      */ 
/*      */               
/* 8749 */               .getName() + "\"};");
/*      */         } else {
/* 8751 */           buf.append("label{text=\"\"};label{text=\"On Bridge\"};label{text=\"\"};label{text=\"Not a bridge? " + bridgeId + "\"};");
/*      */         
/*      */         }
/*      */       
/*      */       }
/* 8756 */       catch (NoSuchStructureException nsse)
/*      */       {
/* 8758 */         buf.append("label{text=\"\"};label{text=\"On Bridge\"};label{text=\"\"};label{text=\"Not Found " + bridgeId + "\"};");
/*      */       
/*      */       }
/*      */ 
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 8766 */       buf.append("label{text=\"\"};label{text=\"On Bridge\"};label{text=\"\"};label{text=\"No\"};");
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 8772 */     return buf.toString();
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\GmTool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */