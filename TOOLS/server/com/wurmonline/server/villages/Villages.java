/*      */ package com.wurmonline.server.villages;
/*      */ 
/*      */ import com.wurmonline.math.TilePos;
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.server.Constants;
/*      */ import com.wurmonline.server.DbConnector;
/*      */ import com.wurmonline.server.FailedException;
/*      */ import com.wurmonline.server.Features;
/*      */ import com.wurmonline.server.HistoryManager;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.NoSuchPlayerException;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.WurmId;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.CreatureTemplate;
/*      */ import com.wurmonline.server.creatures.CreatureTemplateFactory;
/*      */ import com.wurmonline.server.creatures.Creatures;
/*      */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*      */ import com.wurmonline.server.creatures.NoSuchCreatureTemplateException;
/*      */ import com.wurmonline.server.creatures.Offspring;
/*      */ import com.wurmonline.server.economy.Change;
/*      */ import com.wurmonline.server.economy.MonetaryConstants;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.ItemFactory;
/*      */ import com.wurmonline.server.items.NoSuchTemplateException;
/*      */ import com.wurmonline.server.kingdom.InfluenceChain;
/*      */ import com.wurmonline.server.kingdom.Kingdom;
/*      */ import com.wurmonline.server.kingdom.Kingdoms;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.utils.DbUtilities;
/*      */ import com.wurmonline.server.zones.Den;
/*      */ import com.wurmonline.server.zones.Dens;
/*      */ import com.wurmonline.server.zones.FocusZone;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import com.wurmonline.shared.util.StringUtilities;
/*      */ import java.awt.Rectangle;
/*      */ import java.io.IOException;
/*      */ import java.sql.Connection;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashSet;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.annotation.Nonnull;
/*      */ import javax.annotation.Nullable;
/*      */ import javax.annotation.concurrent.GuardedBy;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class Villages
/*      */   implements VillageStatus, MiscConstants, MonetaryConstants, TimeConstants
/*      */ {
/*   79 */   private static final ConcurrentHashMap<Integer, Village> villages = new ConcurrentHashMap<>();
/*      */   
/*   81 */   private static final ConcurrentHashMap<Long, DeadVillage> deadVillages = new ConcurrentHashMap<>();
/*      */ 
/*      */   
/*   84 */   private static Logger logger = Logger.getLogger(Villages.class.getName());
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String LOAD_VILLAGES = "SELECT * FROM VILLAGES WHERE DISBANDED=0";
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String LOAD_DEAD_VILLAGES = "SELECT * FROM VILLAGES WHERE DISBANDED=1";
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String CREATE_DEAD_VILLAGE = "INSERT INTO VILLAGES (NAME,FOUNDER,MAYOR,CREATIONDATE,STARTX,ENDX,STARTY,ENDY,DEEDID,LASTLOGIN,KINGDOM,DISBAND,DISBANDED,DEVISE) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String LOAD_WARS = "SELECT * FROM VILLAGEWARS";
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String LOAD_WAR_DECLARATIONS = "SELECT * FROM VILLAGEWARDECLARATIONS";
/*      */ 
/*      */   
/*      */   @GuardedBy("ALLIANCES_RW_LOCK")
/*  108 */   private static final Set<Alliance> alliances = new HashSet<>();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  113 */   private static final ReentrantReadWriteLock ALLIANCES_RW_LOCK = new ReentrantReadWriteLock();
/*      */ 
/*      */   
/*      */   @GuardedBy("WARS_RW_LOCK")
/*  117 */   private static final Set<Object> wars = new HashSet();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  122 */   private static final ReentrantReadWriteLock WARS_RW_LOCK = new ReentrantReadWriteLock();
/*      */ 
/*      */   
/*  125 */   public static long TILE_UPKEEP = 20L;
/*      */   
/*  127 */   public static String TILE_UPKEEP_STRING = (new Change(TILE_UPKEEP)).getChangeString();
/*      */   
/*  129 */   public static long TILE_COST = 100L;
/*      */   
/*  131 */   public static String TILE_COST_STRING = (new Change(TILE_COST)).getChangeString();
/*      */   
/*  133 */   public static long GUARD_COST = ((Servers.localServer.isChallengeOrEpicServer() ? 3 : 2) * 10000);
/*      */   
/*  135 */   public static String GUARD_COST_STRING = (new Change(GUARD_COST)).getChangeString();
/*      */   
/*  137 */   public static long GUARD_UPKEEP = ((Servers.localServer.isChallengeOrEpicServer() ? 3 : 1) * 10000);
/*      */   
/*  139 */   public static String GUARD_UPKEEP_STRING = (new Change(GUARD_UPKEEP)).getChangeString();
/*      */   
/*  141 */   public static long PERIMETER_COST = 50L;
/*      */   
/*  143 */   public static String PERIMETER_COST_STRING = (new Change(PERIMETER_COST)).getChangeString();
/*      */   
/*  145 */   public static long PERIMETER_UPKEEP = 5L;
/*      */   
/*  147 */   public static String PERIMETER_UPKEEP_STRING = (new Change(PERIMETER_UPKEEP)).getChangeString();
/*      */   
/*  149 */   public static long MINIMUM_UPKEEP = 10000L;
/*      */   
/*  151 */   public static String MINIMUM_UPKEEP_STRING = (new Change(MINIMUM_UPKEEP)).getChangeString();
/*      */   
/*  153 */   private static long lastPolledVillageFaith = System.currentTimeMillis();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Village getVillage(int id) throws NoSuchVillageException {
/*  165 */     Village toReturn = villages.get(Integer.valueOf(id));
/*  166 */     if (toReturn == null)
/*  167 */       throw new NoSuchVillageException("No village with id " + id); 
/*  168 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public static Village getVillage(String name) throws NoSuchVillageException {
/*  173 */     for (Village v : villages.values()) {
/*      */       
/*  175 */       if (v.getName().equalsIgnoreCase(name))
/*  176 */         return v; 
/*      */     } 
/*  178 */     throw new NoSuchVillageException("No village with name " + name);
/*      */   }
/*      */ 
/*      */   
/*      */   public static Village getVillage(@Nonnull TilePos tilePos, boolean surfaced) {
/*  183 */     return getVillage(tilePos.x, tilePos.y, surfaced);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Village getVillage(int tilex, int tiley, boolean surfaced) {
/*  189 */     for (Village village : villages.values()) {
/*      */       
/*  191 */       if (village.covers(tilex, tiley))
/*  192 */         return village; 
/*      */     } 
/*  194 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Village getVillagePlus(int tilex, int tiley, boolean surfaced, int extra) {
/*  200 */     for (Village village : villages.values()) {
/*      */       
/*  202 */       if (village.coversPlus(tilex, tiley, extra))
/*  203 */         return village; 
/*      */     } 
/*  205 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isNameOk(String villageName, int ignoreVillageId) {
/*  210 */     for (Village village : villages.values()) {
/*      */       
/*  212 */       if (village.id != ignoreVillageId && village.getName().equals(villageName))
/*  213 */         return false; 
/*      */     } 
/*  215 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isNameOk(String villageName) {
/*  220 */     return isNameOk(villageName, -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Village createVillage(int startx, int endx, int starty, int endy, int tokenx, int tokeny, String villageName, Creature founder, long deedid, boolean surfaced, boolean democracy, String devise, boolean permanent, byte spawnKingdom, int initialPerimeter) throws NoSuchItemException, IOException, NoSuchCreatureException, NoSuchPlayerException, NoSuchRoleException, FailedException {
/*  230 */     if (!isNameOk(villageName))
/*  231 */       throw new FailedException("The name " + villageName + " already exists. Please select another."); 
/*  232 */     Village toReturn = null;
/*      */ 
/*      */     
/*  235 */     Item deed = Items.getItem(deedid);
/*  236 */     if (deed.getTemplateId() == 862) {
/*      */       
/*  238 */       deed.setDamage(0.0F);
/*  239 */       deed.setTemplateId(663);
/*      */       
/*  241 */       deed.setData1(100);
/*      */     } 
/*  243 */     toReturn = new DbVillage(startx, endx, starty, endy, villageName, founder, deedid, surfaced, democracy, devise, permanent, spawnKingdom, initialPerimeter);
/*      */ 
/*      */     
/*  246 */     toReturn.addCitizen(founder, toReturn.getRoleForStatus((byte)2));
/*      */     
/*  248 */     toReturn.initialize();
/*      */     
/*      */     try {
/*  251 */       Item token = createVillageToken(toReturn, tokenx, tokeny);
/*  252 */       toReturn.setTokenId(token.getWurmId());
/*      */     }
/*  254 */     catch (NoSuchTemplateException nst) {
/*      */       
/*  256 */       logger.log(Level.WARNING, nst.getMessage(), (Throwable)nst);
/*      */     }
/*  258 */     catch (FailedException fe) {
/*      */       
/*  260 */       logger.log(Level.WARNING, fe.getMessage(), (Throwable)fe);
/*      */     } 
/*  262 */     deed.setData2(toReturn.getId());
/*  263 */     villages.put(Integer.valueOf(toReturn.getId()), toReturn);
/*  264 */     toReturn.createInitialUpkeepPlan();
/*  265 */     toReturn.addHistory(founder.getName(), "founded");
/*  266 */     HistoryManager.addHistory(founder.getName(), "founded " + villageName, false);
/*  267 */     founder.achievement(170);
/*      */     
/*  269 */     if (Features.Feature.TOWER_CHAINING.isEnabled()) {
/*      */       
/*  271 */       InfluenceChain chain = InfluenceChain.getInfluenceChain(toReturn.kingdom);
/*  272 */       InfluenceChain.addTokenToChain(toReturn.kingdom, toReturn.getToken());
/*      */     } 
/*      */     
/*  275 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   static void removeVillage(int id) {
/*  280 */     Village v = villages.remove(Integer.valueOf(id));
/*  281 */     if (v != null) {
/*      */ 
/*      */ 
/*      */       
/*  285 */       DeadVillage dv = new DeadVillage(v.getDeedId(), v.getStartX(), v.getStartY(), v.getEndX(), v.getEndY(), v.getName(), v.getFounderName(), (v.getMayor() != null) ? v.getMayor().getName() : "Unknown", v.getCreationDate(), System.currentTimeMillis(), System.currentTimeMillis(), v.kingdom);
/*  286 */       deadVillages.put(Long.valueOf(v.getDeedId()), dv);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean mayCreateTokenOnTile(boolean surfaced, int tilex, int tiley) {
/*  292 */     VolaTile tile = Zones.getTileOrNull(tilex, tiley, surfaced);
/*  293 */     if (tile == null)
/*      */     {
/*  295 */       return true;
/*      */     }
/*      */ 
/*      */     
/*  299 */     if (tile.getStructure() == null)
/*      */     {
/*  301 */       return true;
/*      */     }
/*      */     
/*  304 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static Item createTokenOnTile(Village village, int tilex, int tiley) throws NoSuchTemplateException, FailedException {
/*  310 */     VolaTile tile = Zones.getTileOrNull(tilex, tiley, village.isOnSurface());
/*  311 */     if (tile == null) {
/*      */       
/*  313 */       Item token = ItemFactory.createItem(236, 99.0F, ((tilex << 2) + 2), ((tiley << 2) + 2), 180.0F, village
/*  314 */           .isOnSurface(), (byte)0, -10L, null);
/*  315 */       token.setData2(village.getId());
/*  316 */       return token;
/*      */     } 
/*      */ 
/*      */     
/*  320 */     if (tile.getStructure() == null) {
/*      */       
/*  322 */       Item token = ItemFactory.createItem(236, 99.0F, ((tilex << 2) + 2), ((tiley << 2) + 2), 180.0F, village
/*  323 */           .isOnSurface(), (byte)0, -10L, null);
/*  324 */       token.setData2(village.getId());
/*  325 */       return token;
/*      */     } 
/*      */     
/*  328 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static Item createVillageToken(Village village, int tokenx, int tokeny) throws NoSuchTemplateException, FailedException {
/*  334 */     int size = village.endx - village.startx;
/*  335 */     Item token = createTokenOnTile(village, tokenx, tokeny);
/*  336 */     if (token != null)
/*  337 */       return token;  int x;
/*  338 */     for (x = -1; x <= 1; x++) {
/*      */       
/*  340 */       for (int y = -1; y <= 1; y++) {
/*      */         
/*  342 */         token = createTokenOnTile(village, tokenx + x, tokeny + y);
/*  343 */         if (token != null) {
/*  344 */           return token;
/*      */         }
/*      */       } 
/*      */     } 
/*  348 */     for (x = -size / 2; x <= size / 2; x++) {
/*      */       
/*  350 */       for (int y = -size / 2; y <= size / 2; y++) {
/*      */         
/*  352 */         token = createTokenOnTile(village, tokenx + x, tokeny + y);
/*  353 */         if (token != null)
/*  354 */           return token; 
/*      */       } 
/*      */     } 
/*  357 */     throw new FailedException("Failed to locate a good spot for the token item.");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final String isFocusZoneBlocking(int sizeW, int sizeE, int sizeN, int sizeS, int tokenx, int tokeny, int desiredPerimeter, boolean surfaced) {
/*  364 */     int startpx = Zones.safeTileX(tokenx - sizeW - 5 - desiredPerimeter);
/*  365 */     int startpy = Zones.safeTileY(tokeny - sizeN - 5 - desiredPerimeter);
/*  366 */     int endpy = Zones.safeTileX(tokeny + sizeS + 1 + 5 + desiredPerimeter);
/*  367 */     int endpx = Zones.safeTileY(tokenx + sizeE + 1 + 5 + desiredPerimeter);
/*  368 */     Rectangle bounds = new Rectangle(startpx, startpy, endpx - startpx, endpy - startpy);
/*  369 */     StringBuilder toReturn = new StringBuilder();
/*  370 */     FocusZone[] fzs = FocusZone.getAllZones();
/*  371 */     for (FocusZone focusz : fzs) {
/*      */       
/*  373 */       if (focusz.isNonPvP() || focusz.isPvP()) {
/*      */ 
/*      */         
/*  376 */         Rectangle focusRect = new Rectangle(focusz.getStartX(), focusz.getStartY(), focusz.getEndX() - focusz.getStartX(), focusz.getEndY() - focusz.getStartY());
/*  377 */         if (focusRect.intersects(bounds))
/*      */         {
/*  379 */           toReturn.append(focusz.getName() + " is within the planned area. ");
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  384 */     if (toReturn.toString().length() > 0)
/*  385 */       toReturn.append("Settling there is no longer allowed."); 
/*  386 */     return toReturn.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   public static final Set<Village> getVillagesWithin(int startX, int startY, int endX, int endY) {
/*  391 */     Rectangle bounds = new Rectangle(startX, startY, endX - startX, endY - startY);
/*  392 */     Rectangle perimRect = bounds;
/*  393 */     Set<Village> toReturn = new HashSet<>();
/*  394 */     for (Village village : villages.values()) {
/*      */       
/*  396 */       perimRect = new Rectangle(village.startx, village.starty, village.getDiameterX(), village.getDiameterY());
/*  397 */       if (perimRect.intersects(bounds))
/*  398 */         toReturn.add(village); 
/*      */     } 
/*  400 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Map<Village, String> canFoundVillage(int sizeW, int sizeE, int sizeN, int sizeS, int tokenx, int tokeny, int desiredPerimeter, boolean surfaced, @Nullable Village original, Creature founder) {
/*  408 */     int startpx = Zones.safeTileX(tokenx - sizeW - 5 - desiredPerimeter);
/*  409 */     int startpy = Zones.safeTileY(tokeny - sizeN - 5 - desiredPerimeter);
/*  410 */     int endpy = Zones.safeTileX(tokeny + sizeS + 1 + 5 + desiredPerimeter);
/*  411 */     int endpx = Zones.safeTileY(tokenx + sizeE + 1 + 5 + desiredPerimeter);
/*  412 */     Rectangle bounds = new Rectangle(startpx, startpy, endpx - startpx, endpy - startpy);
/*  413 */     Rectangle perimRect = bounds;
/*      */     
/*  415 */     Map<Village, String> decliners = new Hashtable<>();
/*  416 */     boolean allianceOnly = (Servers.localServer.PVPSERVER && !Servers.localServer.isChallengeOrEpicServer());
/*      */ 
/*      */     
/*  419 */     Rectangle allianceBounds = allianceOnly ? new Rectangle(Zones.safeTileX(startpx - 100), Zones.safeTileY(startpy - 100), endpx - startpx + 200, endpy - startpy + 200) : bounds;
/*      */ 
/*      */     
/*  422 */     boolean accept = false;
/*  423 */     boolean prohibited = false;
/*  424 */     for (Village village : villages.values()) {
/*      */       
/*  426 */       if (village != original) {
/*      */         
/*  428 */         int mindist = 5 + village.getPerimeterSize();
/*      */         
/*  430 */         perimRect = new Rectangle(village.startx - mindist, village.starty - mindist, village.getDiameterX() + mindist * 2, village.getDiameterY() + mindist * 2);
/*  431 */         if (perimRect.intersects(bounds)) {
/*      */           
/*  433 */           prohibited = true;
/*  434 */           decliners.put(village, "has perimeter within the planned settlement or its perimeter."); continue;
/*      */         } 
/*  436 */         if (allianceOnly && original == null)
/*      */         {
/*  438 */           if (perimRect.intersects(allianceBounds))
/*      */           {
/*  440 */             if (founder != null) {
/*      */               
/*  442 */               if (founder.getCitizenVillage() != null && (founder.getCitizenVillage() == village || village
/*  443 */                 .isAlly(founder))) {
/*      */                 
/*  445 */                 accept = true; continue;
/*      */               } 
/*  447 */               if (founder.getCitizenVillage() == null || founder.getCitizenVillage() != village || 
/*  448 */                 !village.isAlly(founder))
/*      */               {
/*  450 */                 decliners.put(village, "requires " + founder.getName() + " to be a citizen or ally.");
/*      */               }
/*      */             } 
/*      */           }
/*      */         }
/*      */       } 
/*      */     } 
/*  457 */     if (prohibited == true || !accept) {
/*  458 */       return decliners;
/*      */     }
/*  460 */     return new Hashtable<>();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Village getVillageWithPerimeterAt(int tilex, int tiley, boolean surfaced) {
/*  509 */     for (Village village : villages.values()) {
/*      */       
/*  511 */       int mindist = 5 + village.getPerimeterSize();
/*  512 */       Rectangle perimRect = new Rectangle(village.startx - mindist, village.starty - mindist, village.endx - village.startx + 1 + mindist * 2, village.endy - village.starty + 1 + mindist * 2);
/*      */       
/*  514 */       if (perimRect.contains(tilex, tiley))
/*      */       {
/*  516 */         return village;
/*      */       }
/*      */     } 
/*  519 */     return null;
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
/*      */   public static Village doesNotAllowAction(Creature performer, int action, int tilex, int tiley, boolean surfaced) {
/*  540 */     if (!Servers.localServer.HOMESERVER) {
/*  541 */       return null;
/*      */     }
/*      */     
/*  544 */     if (performer.getKingdomId() != Servers.localServer.KINGDOM) {
/*  545 */       return null;
/*      */     }
/*      */     
/*  548 */     if (performer.getPower() > 1) {
/*  549 */       return null;
/*      */     }
/*      */     
/*  552 */     if (performer.getKingdomTemplateId() == 3) {
/*  553 */       return null;
/*      */     }
/*      */     
/*  556 */     VolaTile t = Zones.getTileOrNull(tilex, tiley, surfaced);
/*  557 */     if (t != null)
/*      */     {
/*  559 */       if (t.getVillage() != null) {
/*  560 */         return null;
/*      */       }
/*      */     }
/*      */     
/*  564 */     Village v = getVillageWithPerimeterAt(tilex, tiley, surfaced);
/*  565 */     if (v != null && !v.isCitizen(performer) && !v.isAlly(performer))
/*  566 */       return v; 
/*  567 */     return null;
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
/*      */   public static final Village doesNotAllowBuildAction(Creature performer, int action, int tilex, int tiley, boolean surfaced) {
/*  586 */     if (performer.getPower() > 1) {
/*  587 */       return null;
/*      */     }
/*      */     
/*  590 */     VolaTile t = Zones.getTileOrNull(tilex, tiley, surfaced);
/*  591 */     if (t != null) {
/*      */       
/*  593 */       Village village = t.getVillage();
/*  594 */       if (village != null) {
/*      */         
/*  596 */         VillageRole role = village.getRoleFor(performer);
/*  597 */         if (role != null) {
/*      */           
/*  599 */           if (role.mayBuild()) {
/*  600 */             return null;
/*      */           }
/*  602 */           return village;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  607 */     Village v = getVillageWithPerimeterAt(tilex, tiley, surfaced);
/*  608 */     if (v != null && !v.isCitizen(performer) && !v.isAlly(performer)) {
/*  609 */       return v;
/*      */     }
/*  611 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Item isAltarOnDeed(int sizeW, int sizeE, int sizeN, int sizeS, int tokenx, int tokeny, boolean surfaced) {
/*  622 */     int startx = Math.max(0, tokenx - sizeW);
/*  623 */     int starty = Math.max(0, tokeny - sizeN);
/*  624 */     int endy = Math.min((1 << Constants.meshSize) - 1, tokeny + sizeS);
/*  625 */     int endx = Math.min((1 << Constants.meshSize) - 1, tokenx + sizeE);
/*  626 */     for (int x = startx; x <= endx; x++) {
/*      */       
/*  628 */       for (int y = starty; y <= endy; y++) {
/*      */         
/*  630 */         VolaTile t = Zones.getTileOrNull(x, y, surfaced);
/*  631 */         if (t != null) {
/*      */           
/*  633 */           Item[] items = t.getItems();
/*  634 */           for (int i = 0; i < items.length; i++) {
/*      */             
/*  636 */             if (!items[i].isUnfinished())
/*      */             {
/*  638 */               if (items[i].isNonDeedable() || (items[i].isRoyal() && items[i].isNoTake()) || (items[i]
/*  639 */                 .isEpicTargetItem() && Servers.localServer.PVPSERVER))
/*  640 */                 return items[i]; 
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*  646 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object isAggOnDeed(@Nullable Village currVill, Creature responder, int sizeW, int sizeE, int sizeN, int sizeS, int tokenx, int tokeny, boolean surfaced) {
/*  652 */     int startx = Math.max(0, tokenx - sizeW);
/*  653 */     int starty = Math.max(0, tokeny - sizeN);
/*  654 */     int endy = Zones.safeTileY(tokeny + sizeS);
/*  655 */     int endx = Zones.safeTileX(tokenx + sizeE);
/*  656 */     for (int x = startx; x <= endx; x++) {
/*      */       
/*  658 */       for (int y = starty; y <= endy; y++) {
/*      */         
/*  660 */         Den den = Dens.getDen(x, y);
/*  661 */         if (den != null) {
/*      */           
/*      */           try {
/*      */ 
/*      */             
/*  666 */             CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(den.getTemplateId());
/*  667 */             if (responder.getPower() >= 2) {
/*  668 */               responder.getCommunicator().sendSafeServerMessage(template
/*  669 */                   .getName() + " Den found at " + x + "," + y + ".");
/*      */             }
/*  671 */             if (!template.isUnique() || Creatures.getInstance().creatureWithTemplateExists(den.getTemplateId()))
/*      */             {
/*      */ 
/*      */ 
/*      */               
/*  676 */               return den;
/*      */             }
/*  678 */           } catch (NoSuchCreatureTemplateException nst) {
/*      */             
/*  680 */             logger.log(Level.WARNING, den.getTemplateId() + ":" + nst.getMessage(), (Throwable)nst);
/*  681 */             if (responder.getPower() >= 2) {
/*  682 */               responder.getCommunicator().sendSafeServerMessage("Den with unknown template ID: " + den.getTemplateId() + " found at " + x + ", " + y + ".");
/*      */             } else {
/*  684 */               responder.getCommunicator().sendSafeServerMessage("An invalid creature den was found. Please use /support to ask a GM for help to deal with this issue.");
/*      */             } 
/*  686 */             return den;
/*      */           } 
/*      */         }
/*  689 */         VolaTile t = Zones.getTileOrNull(x, y, surfaced);
/*  690 */         if (t != null)
/*      */         {
/*  692 */           if (currVill == null || t.getVillage() != currVill) {
/*      */             
/*  694 */             Creature[] crets = t.getCreatures();
/*  695 */             for (int i = 0; i < crets.length; i++) {
/*      */               
/*  697 */               if ((crets[i].getAttitude(responder) == 2 && (crets[i]
/*  698 */                 .getBaseCombatRating() > 5.0F || crets[i].isPlayer())) || crets[i].isUnique()) {
/*      */                 
/*  700 */                 if (responder.getPower() >= 2)
/*  701 */                   responder.getCommunicator().sendSafeServerMessage(crets[i]
/*  702 */                       .getName() + " agro Creature found at " + x + "," + y + "."); 
/*  703 */                 return crets[i];
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/*  710 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean canExpandVillage(int size, Item token) throws NoSuchVillageException {
/*  715 */     Village vill = getVillage(token.getData2());
/*  716 */     int tilex = vill.getStartX();
/*  717 */     int tiley = vill.getStartY();
/*  718 */     boolean surfaced = vill.isOnSurface();
/*  719 */     int startx = Math.max(0, tilex - size);
/*  720 */     int starty = Math.max(0, tiley - size);
/*  721 */     int endx = Math.min((1 << Constants.meshSize) - 1, tilex + size);
/*      */     
/*  723 */     int endy = Math.min((1 << Constants.meshSize) - 1, tiley + size);
/*  724 */     for (int x = startx; x <= endx; x += 5) {
/*      */       
/*  726 */       for (int y = starty; y <= endy; y += 5) {
/*      */         
/*  728 */         Village check = Zones.getVillage(x, y, surfaced);
/*  729 */         if (check != null && !check.equals(vill))
/*  730 */           return false; 
/*      */       } 
/*      */     } 
/*  733 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void generateDeadVillage(Player performer, boolean sendFeedback) throws IOException {
/*  738 */     int centerX = -1;
/*  739 */     int centerY = -1;
/*  740 */     boolean gotLocation = false;
/*  741 */     while (!gotLocation) {
/*      */       
/*  743 */       int testX = Server.rand.nextInt((int)(Zones.worldTileSizeX * 0.8F)) + (int)(Zones.worldTileSizeX * 0.1F);
/*  744 */       int testY = Server.rand.nextInt((int)(Zones.worldTileSizeY * 0.8F)) + (int)(Zones.worldTileSizeY * 0.1F);
/*  745 */       if (Tiles.decodeHeight(Server.surfaceMesh.getTile(testX, testY)) <= 0) {
/*      */         continue;
/*      */       }
/*  748 */       centerX = testX;
/*  749 */       centerY = testY;
/*      */       
/*  751 */       gotLocation = true;
/*      */     } 
/*      */     
/*  754 */     int sizeX = Server.rand.nextInt(30) * ((Server.rand.nextInt(4) == 0) ? 3 : 1) + 5;
/*  755 */     int sizeY = Math.max(sizeX / 4, Math.min(sizeX * 4, Server.rand.nextInt(30) * ((Server.rand.nextInt(4) == 0) ? 3 : 1) + 5));
/*  756 */     sizeY = Math.max(5, sizeY);
/*      */     
/*  758 */     int startx = centerX - sizeX;
/*  759 */     int starty = centerY - sizeY;
/*  760 */     int endx = centerX + sizeX;
/*  761 */     int endy = centerY + sizeY;
/*  762 */     String name = StringUtilities.raiseFirstLetterOnly(generateGenericVillageName());
/*  763 */     String founderName = StringUtilities.raiseFirstLetterOnly(Server.rand.nextBoolean() ? Offspring.getRandomFemaleName() : Offspring.getRandomMaleName());
/*  764 */     String mayorName = StringUtilities.raiseFirstLetterOnly(Server.rand.nextBoolean() ? founderName : (Server.rand.nextBoolean() ? 
/*  765 */         Offspring.getRandomFemaleName() : Offspring.getRandomMaleName()));
/*  766 */     long creationDate = System.currentTimeMillis() - 2419200000L * Server.rand.nextInt(60);
/*  767 */     long deedid = WurmId.getNextItemId();
/*  768 */     long disbandDate = (long)Math.min((float)(System.currentTimeMillis() - 2419200000L), 
/*  769 */         Math.max((float)(creationDate + 2419200000L), (float)creationDate + (float)(System.currentTimeMillis() - creationDate) * Server.rand.nextFloat()));
/*  770 */     long lastLogin = Math.max(creationDate + 2419200000L, disbandDate - 2419200000L * Server.rand.nextInt(6));
/*  771 */     byte kingdom = Servers.localServer.HOMESERVER ? Servers.localServer.KINGDOM : (byte)(Server.rand.nextInt(4) + 1);
/*      */ 
/*      */ 
/*      */     
/*  775 */     Connection dbcon = null;
/*  776 */     PreparedStatement ps = null;
/*  777 */     ResultSet rs = null;
/*      */     
/*      */     try {
/*  780 */       dbcon = DbConnector.getZonesDbCon();
/*  781 */       ps = dbcon.prepareStatement("INSERT INTO VILLAGES (NAME,FOUNDER,MAYOR,CREATIONDATE,STARTX,ENDX,STARTY,ENDY,DEEDID,LASTLOGIN,KINGDOM,DISBAND,DISBANDED,DEVISE) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)", 2);
/*  782 */       ps.setString(1, name);
/*  783 */       ps.setString(2, founderName);
/*  784 */       ps.setString(3, mayorName);
/*  785 */       ps.setLong(4, creationDate);
/*  786 */       ps.setInt(5, startx);
/*  787 */       ps.setInt(6, endx);
/*  788 */       ps.setInt(7, starty);
/*  789 */       ps.setInt(8, endy);
/*  790 */       ps.setLong(9, deedid);
/*  791 */       ps.setLong(10, lastLogin);
/*  792 */       ps.setByte(11, kingdom);
/*  793 */       ps.setLong(12, disbandDate);
/*  794 */       ps.setBoolean(13, true);
/*  795 */       ps.setString(14, "A settlement like no other.");
/*  796 */       ps.executeUpdate();
/*      */     }
/*  798 */     catch (SQLException sqx) {
/*      */       
/*  800 */       throw new IOException(sqx);
/*      */     }
/*      */     finally {
/*      */       
/*  804 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*  805 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */ 
/*      */     
/*  809 */     DeadVillage dv = new DeadVillage(deedid, startx, starty, endx, endy, name, founderName, mayorName, creationDate, disbandDate, lastLogin, kingdom);
/*      */     
/*  811 */     deadVillages.put(Long.valueOf(deedid), dv);
/*      */     
/*  813 */     performer.sendToLoggers("Generated a dead village at " + centerX + "," + centerY + ".");
/*  814 */     if (sendFeedback) {
/*  815 */       performer.getCommunicator().sendNormalServerMessage("Dead Village \"" + name + "\" created at " + centerX + "," + centerY + ".");
/*      */     }
/*      */   }
/*      */   
/*      */   private static String generateGenericVillageName() {
/*  820 */     ArrayList<String> genericEndings = new ArrayList<>();
/*  821 */     addAllStrings(genericEndings, new String[] { " Village", " Isle", " Island", " Mountain", " Plains", " Estate", " Beach", " Homestead", " Valley", " Forest", " Farm", " Castle" });
/*      */     
/*  823 */     ArrayList<String> genericSuffix = new ArrayList<>();
/*  824 */     addAllStrings(genericSuffix, new String[] { "ford", "borough", "ington", "ton", "stead", "chester", "dale", "ham", "ing", "mouth", "port" });
/*      */     
/*  826 */     String toReturn = "";
/*  827 */     switch (Server.rand.nextInt(3)) {
/*      */       
/*      */       case 0:
/*  830 */         toReturn = toReturn + Offspring.getRandomMaleName();
/*      */         break;
/*      */       case 1:
/*  833 */         toReturn = toReturn + Offspring.getRandomFemaleName();
/*      */         break;
/*      */       case 2:
/*  836 */         toReturn = toReturn + Offspring.getRandomGenericName();
/*      */         break;
/*      */     } 
/*      */     
/*  840 */     if (Server.rand.nextInt(3) == 0) {
/*      */       
/*  842 */       toReturn = toReturn + (String)genericSuffix.get(Server.rand.nextInt(genericSuffix.size()));
/*  843 */       if (Server.rand.nextBoolean()) {
/*  844 */         toReturn = toReturn + (String)genericEndings.get(Server.rand.nextInt(genericEndings.size()));
/*      */       }
/*      */     } else {
/*  847 */       toReturn = toReturn + (String)genericEndings.get(Server.rand.nextInt(genericEndings.size()));
/*      */     } 
/*  849 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   private static void addAllStrings(ArrayList<String> toAddTo, String... names) {
/*  854 */     for (String s : names) {
/*  855 */       toAddTo.add(s);
/*      */     }
/*      */   }
/*      */   
/*      */   public static void loadDeadVillages() throws IOException {
/*  860 */     logger.info("Loading dead villages.");
/*      */ 
/*      */     
/*  863 */     long start = System.nanoTime();
/*  864 */     Connection dbcon = null;
/*  865 */     PreparedStatement ps = null;
/*  866 */     ResultSet rs = null;
/*      */     
/*      */     try {
/*  869 */       dbcon = DbConnector.getZonesDbCon();
/*  870 */       ps = dbcon.prepareStatement("SELECT * FROM VILLAGES WHERE DISBANDED=1");
/*  871 */       rs = ps.executeQuery();
/*  872 */       while (rs.next())
/*      */       {
/*  874 */         int startx = rs.getInt("STARTX");
/*  875 */         int starty = rs.getInt("STARTY");
/*  876 */         int endx = rs.getInt("ENDX");
/*  877 */         int endy = rs.getInt("ENDY");
/*  878 */         String name = rs.getString("NAME");
/*  879 */         String founderName = rs.getString("FOUNDER");
/*  880 */         String mayorName = rs.getString("MAYOR");
/*  881 */         long creationDate = rs.getLong("CREATIONDATE");
/*  882 */         long deedid = rs.getLong("DEEDID");
/*  883 */         long disband = rs.getLong("DISBAND");
/*  884 */         long lastLogin = rs.getLong("LASTLOGIN");
/*  885 */         byte kingdom = rs.getByte("KINGDOM");
/*      */         
/*  887 */         DeadVillage dv = new DeadVillage(deedid, startx, starty, endx, endy, name, founderName, mayorName, creationDate, disband, lastLogin, kingdom);
/*      */         
/*  889 */         deadVillages.put(Long.valueOf(deedid), dv);
/*      */       }
/*      */     
/*  892 */     } catch (SQLException sqx) {
/*      */       
/*  894 */       throw new IOException(sqx);
/*      */     }
/*      */     finally {
/*      */       
/*  898 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*  899 */       DbConnector.returnConnection(dbcon);
/*  900 */       long end = System.nanoTime();
/*  901 */       logger.info("Loaded " + deadVillages.size() + " dead villages from the database took " + ((float)(end - start) / 1000000.0F) + " ms");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void loadVillages() throws IOException {
/*  909 */     logger.info("Loading villages.");
/*      */ 
/*      */     
/*  912 */     long start = System.nanoTime();
/*  913 */     Connection dbcon = null;
/*  914 */     PreparedStatement ps = null;
/*  915 */     ResultSet rs = null;
/*      */ 
/*      */     
/*      */     try {
/*  919 */       dbcon = DbConnector.getZonesDbCon();
/*  920 */       ps = dbcon.prepareStatement("SELECT * FROM VILLAGES WHERE DISBANDED=0");
/*  921 */       rs = ps.executeQuery();
/*  922 */       while (rs.next()) {
/*      */         
/*  924 */         int id = rs.getInt("ID");
/*  925 */         int startx = rs.getInt("STARTX");
/*  926 */         int starty = rs.getInt("STARTY");
/*  927 */         int endx = rs.getInt("ENDX");
/*  928 */         int endy = rs.getInt("ENDY");
/*  929 */         String name = rs.getString("NAME");
/*  930 */         String founderName = rs.getString("FOUNDER");
/*  931 */         String mayorName = rs.getString("MAYOR");
/*  932 */         long creationDate = rs.getLong("CREATIONDATE");
/*  933 */         long deedid = rs.getLong("DEEDID");
/*  934 */         boolean surfaced = rs.getBoolean("SURFACED");
/*  935 */         String devise = rs.getString("DEVISE");
/*  936 */         boolean democracy = rs.getBoolean("DEMOCRACY");
/*  937 */         boolean homestead = rs.getBoolean("HOMESTEAD");
/*  938 */         long tokenid = rs.getLong("TOKEN");
/*  939 */         long disband = rs.getLong("DISBAND");
/*  940 */         long disbander = rs.getLong("DISBANDER");
/*  941 */         long lastLogin = rs.getLong("LASTLOGIN");
/*  942 */         byte kingdom = rs.getByte("KINGDOM");
/*  943 */         long upkeep = rs.getLong("UPKEEP");
/*  944 */         byte settings = rs.getByte("MAYPICKUP");
/*  945 */         boolean acceptsHomesteads = rs.getBoolean("ACCEPTSHOMESTEADS");
/*  946 */         int maxcitizens = rs.getInt("MAXCITIZENS");
/*  947 */         boolean perma = rs.getBoolean("PERMANENT");
/*  948 */         byte spawnKingdom = rs.getByte("SPAWNKINGDOM");
/*  949 */         boolean merchants = rs.getBoolean("MERCHANTS");
/*  950 */         int perimeterTiles = rs.getInt("PERIMETER");
/*  951 */         boolean aggros = rs.getBoolean("AGGROS");
/*      */         
/*  953 */         String consumerKeyToUse = rs.getString("TWITKEY");
/*  954 */         String consumerSecretToUse = rs.getString("TWITSECRET");
/*  955 */         String applicationToken = rs.getString("TWITAPP");
/*  956 */         String applicationSecret = rs.getString("TWITAPPSECRET");
/*  957 */         boolean twitChat = rs.getBoolean("TWITCHAT");
/*  958 */         boolean twitEnabled = rs.getBoolean("TWITENABLE");
/*  959 */         float faithWar = rs.getFloat("FAITHWAR");
/*  960 */         float faithHeal = rs.getFloat("FAITHHEAL");
/*  961 */         float faithCreate = rs.getFloat("FAITHCREATE");
/*  962 */         byte spawnSituation = rs.getByte("SPAWNSITUATION");
/*  963 */         int allianceNumber = rs.getInt("ALLIANCENUMBER");
/*  964 */         short wins = rs.getShort("HOTAWINS");
/*  965 */         long lastChangedName = rs.getLong("NAMECHANGED");
/*  966 */         int villageRep = rs.getInt("VILLAGEREP");
/*      */         
/*  968 */         String motd = rs.getString("MOTD");
/*      */         
/*  970 */         Village toAdd = new DbVillage(id, startx, endx, starty, endy, name, founderName, mayorName, deedid, surfaced, democracy, devise, creationDate, homestead, tokenid, disband, disbander, lastLogin, kingdom, upkeep, settings, acceptsHomesteads, merchants, maxcitizens, perma, spawnKingdom, perimeterTiles, aggros, consumerKeyToUse, consumerSecretToUse, applicationToken, applicationSecret, twitChat, twitEnabled, faithWar, faithHeal, faithCreate, spawnSituation, allianceNumber, wins, lastChangedName, motd);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  976 */         toAdd.villageReputation = villageRep;
/*  977 */         villages.put(Integer.valueOf(id), toAdd);
/*  978 */         Kingdoms.getKingdom(kingdom).setExistsHere(true);
/*  979 */         toAdd.loadRoles();
/*      */ 
/*      */         
/*  982 */         toAdd.loadVillageMapAnnotations();
/*  983 */         toAdd.loadVillageRecruitees();
/*      */         
/*  985 */         toAdd.plan = new DbGuardPlan(id);
/*      */         
/*  987 */         if (logger.isLoggable(Level.FINE))
/*      */         {
/*  989 */           logger.fine("Loaded Village ID: " + id + ": " + toAdd);
/*      */         }
/*      */       } 
/*      */       
/*  993 */       for (Village toAdd : villages.values())
/*      */       {
/*      */         
/*  996 */         toAdd.initialize();
/*  997 */         toAdd.addGates();
/*  998 */         toAdd.addMineDoors();
/*      */         
/* 1000 */         toAdd.loadReputations();
/* 1001 */         toAdd.plan.fixGuards();
/* 1002 */         toAdd.checkForEnemies();
/* 1003 */         toAdd.loadHistory();
/*      */       }
/*      */     
/* 1006 */     } catch (SQLException sqx) {
/*      */       
/* 1008 */       throw new IOException(sqx);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*      */     finally {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1019 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 1020 */       DbConnector.returnConnection(dbcon);
/* 1021 */       long end = System.nanoTime();
/* 1022 */       logger.info("Loaded " + villages.size() + " villages from the database took " + ((float)(end - start) / 1000000.0F) + " ms");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void loadCitizens() {
/* 1030 */     logger.info("Loading villages citizens.");
/* 1031 */     for (Village toAdd : villages.values())
/*      */     {
/* 1033 */       toAdd.loadCitizens();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void loadGuards() {
/* 1039 */     logger.info("Loading villages guards.");
/* 1040 */     for (Village toAdd : villages.values())
/*      */     {
/* 1042 */       toAdd.loadGuards();
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
/*      */   static final void createWar(Village villone, Village villtwo) {
/* 1055 */     VillageWar newWar = new DbVillageWar(villone, villtwo);
/* 1056 */     newWar.save();
/* 1057 */     villone.startWar(newWar, true);
/* 1058 */     villtwo.startWar(newWar, false);
/* 1059 */     HistoryManager.addHistory("", villone.getName() + " and " + villtwo.getName() + " goes to war.");
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
/*      */   public static final void declareWar(Village villone, Village villtwo) {
/* 1071 */     WarDeclaration newWar = new WarDeclaration(villone, villtwo);
/*      */     
/* 1073 */     villone.addWarDeclaration(newWar);
/* 1074 */     villtwo.addWarDeclaration(newWar);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void declarePeace(Creature performer, Creature accepter, Village villone, Village villtwo) {
/* 1080 */     villone.declarePeace(performer, accepter, villtwo, true);
/* 1081 */     villtwo.declarePeace(performer, accepter, villone, false);
/*      */     
/* 1083 */     VillageWar[] wararr = getWars();
/* 1084 */     for (int x = 0; x < wararr.length; x++) {
/*      */       
/* 1086 */       if ((wararr[x].getVillone() == villone && wararr[x].getVilltwo() == villtwo) || (wararr[x]
/* 1087 */         .getVilltwo() == villone && wararr[x].getVillone() == villtwo))
/*      */       {
/* 1089 */         removeAndDeleteVillageWar(wararr[x]);
/*      */       }
/*      */     } 
/*      */     
/* 1093 */     HistoryManager.addHistory("", villone.getName() + " and " + villtwo.getName() + " make peace.");
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
/*      */   private static boolean removeAndDeleteVillageWar(VillageWar aVillageWar) {
/* 1105 */     boolean lVillageWarExisted = false;
/* 1106 */     if (aVillageWar != null) {
/*      */       
/* 1108 */       WARS_RW_LOCK.writeLock().lock();
/*      */       
/*      */       try {
/* 1111 */         lVillageWarExisted = wars.remove(aVillageWar);
/* 1112 */         aVillageWar.delete();
/*      */       }
/*      */       finally {
/*      */         
/* 1116 */         WARS_RW_LOCK.writeLock().unlock();
/*      */       } 
/*      */     } 
/* 1119 */     return lVillageWarExisted;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final VillageWar[] getWars() {
/* 1128 */     WARS_RW_LOCK.readLock().lock();
/*      */     
/*      */     try {
/* 1131 */       return wars.<VillageWar>toArray(new VillageWar[wars.size()]);
/*      */     }
/*      */     finally {
/*      */       
/* 1135 */       WARS_RW_LOCK.readLock().unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final Alliance[] getAlliances() {
/* 1145 */     ALLIANCES_RW_LOCK.readLock().lock();
/*      */     
/*      */     try {
/* 1148 */       return alliances.<Alliance>toArray(new Alliance[alliances.size()]);
/*      */     }
/*      */     finally {
/*      */       
/* 1152 */       ALLIANCES_RW_LOCK.readLock().unlock();
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
/*      */   public static final void loadWars() throws IOException {
/* 1164 */     logger.log(Level.INFO, "Loading all wars.");
/*      */ 
/*      */     
/* 1167 */     long start = System.nanoTime();
/* 1168 */     Connection dbcon = null;
/* 1169 */     PreparedStatement ps = null;
/* 1170 */     ResultSet rs = null;
/* 1171 */     WARS_RW_LOCK.writeLock().lock();
/*      */     
/*      */     try {
/* 1174 */       dbcon = DbConnector.getZonesDbCon();
/* 1175 */       ps = dbcon.prepareStatement("SELECT * FROM VILLAGEWARS");
/* 1176 */       rs = ps.executeQuery();
/* 1177 */       int aid = -10;
/* 1178 */       while (rs.next()) {
/*      */         
/*      */         try
/*      */         {
/* 1182 */           aid = rs.getInt("ID");
/* 1183 */           Village villone = getVillage(rs.getInt("VILLONE"));
/* 1184 */           Village villtwo = getVillage(rs.getInt("VILLTWO"));
/* 1185 */           VillageWar war = new DbVillageWar(villone, villtwo);
/* 1186 */           villone.addWar(war);
/* 1187 */           villtwo.addWar(war);
/* 1188 */           wars.add(war);
/* 1189 */           if (logger.isLoggable(Level.FINE))
/*      */           {
/* 1191 */             logger.fine("Loaded War ID: " + aid + ": " + war);
/*      */           }
/*      */         }
/* 1194 */         catch (NoSuchVillageException nsv)
/*      */         {
/* 1196 */           logger.log(Level.WARNING, "Failed to load war with id " + aid + "!");
/*      */         }
/*      */       
/*      */       } 
/* 1200 */     } catch (SQLException sqx) {
/*      */       
/* 1202 */       throw new IOException(sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1206 */       WARS_RW_LOCK.writeLock().unlock();
/* 1207 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 1208 */       DbConnector.returnConnection(dbcon);
/* 1209 */       long end = System.nanoTime();
/* 1210 */       logger.info("Loaded " + wars.size() + " wars from the database took " + ((float)(end - start) / 1000000.0F) + " ms");
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
/*      */   public static final void loadWarDeclarations() throws IOException {
/* 1226 */     Connection dbcon = null;
/* 1227 */     PreparedStatement ps = null;
/* 1228 */     ResultSet rs = null;
/* 1229 */     WARS_RW_LOCK.writeLock().lock();
/*      */     
/*      */     try {
/* 1232 */       dbcon = DbConnector.getZonesDbCon();
/* 1233 */       ps = dbcon.prepareStatement("SELECT * FROM VILLAGEWARDECLARATIONS");
/* 1234 */       rs = ps.executeQuery();
/* 1235 */       int aid = -10;
/* 1236 */       while (rs.next()) {
/*      */         
/*      */         try
/*      */         {
/* 1240 */           aid = rs.getInt("ID");
/* 1241 */           Village villone = getVillage(rs.getInt("VILLONE"));
/* 1242 */           Village villtwo = getVillage(rs.getInt("VILLTWO"));
/* 1243 */           long time = rs.getLong("DECLARETIME");
/* 1244 */           WarDeclaration war = new WarDeclaration(villone, villtwo, time);
/* 1245 */           villone.addWarDeclaration(war);
/* 1246 */           villtwo.addWarDeclaration(war);
/* 1247 */           wars.add(war);
/*      */         }
/* 1249 */         catch (NoSuchVillageException nsv)
/*      */         {
/* 1251 */           logger.log(Level.WARNING, "Failed to load war with id " + aid + "!");
/*      */         }
/*      */       
/*      */       } 
/* 1255 */     } catch (SQLException sqx) {
/*      */       
/* 1257 */       throw new IOException(sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1261 */       WARS_RW_LOCK.writeLock().unlock();
/* 1262 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 1263 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Village getVillageForCreature(Creature creature) {
/* 1270 */     if (creature == null)
/* 1271 */       return null; 
/* 1272 */     for (Village village : villages.values()) {
/*      */       
/* 1274 */       if (village.isCitizen(creature))
/* 1275 */         return village; 
/*      */     } 
/* 1277 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public static Village getVillageForCreature(long wid) {
/* 1282 */     if (wid == -10L)
/* 1283 */       return null; 
/* 1284 */     for (Village village : villages.values()) {
/*      */       
/* 1286 */       if (village.isCitizen(wid))
/* 1287 */         return village; 
/*      */     } 
/* 1289 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public static long getVillageMoney() {
/* 1294 */     long toReturn = 0L;
/* 1295 */     for (Village village : villages.values()) {
/*      */       
/* 1297 */       if (village.plan != null)
/* 1298 */         toReturn += village.plan.moneyLeft; 
/*      */     } 
/* 1300 */     return toReturn;
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
/*      */   public static final int getSizeForDeed(int templateId) {
/* 1312 */     if (templateId == 237 || templateId == 234)
/* 1313 */       return 5; 
/* 1314 */     if (templateId == 211 || templateId == 253)
/* 1315 */       return 10; 
/* 1316 */     if (templateId == 238)
/* 1317 */       return 15; 
/* 1318 */     if (templateId == 239 || templateId == 254) {
/* 1319 */       return 20;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1324 */     if (templateId == 242)
/*      */     {
/* 1326 */       return 50;
/*      */     }
/*      */     
/* 1329 */     if (templateId == 244)
/*      */     {
/* 1331 */       return 100; } 
/* 1332 */     if (templateId == 245) {
/* 1333 */       return 200;
/*      */     }
/* 1335 */     return 5;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final Village[] getVillages() {
/* 1344 */     Village[] toReturn = new Village[0];
/* 1345 */     if (villages != null)
/* 1346 */       toReturn = (Village[])villages.values().toArray((Object[])new Village[villages.size()]); 
/* 1347 */     return toReturn;
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
/*      */   public static int getNumberOfVillages() {
/* 1361 */     return villages.size();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void poll() {
/* 1369 */     long now = System.currentTimeMillis();
/* 1370 */     Village[] aVillages = getVillages();
/* 1371 */     boolean lowerFaith = (System.currentTimeMillis() - lastPolledVillageFaith > 86400000L);
/*      */     
/* 1373 */     for (int x = 0; x < aVillages.length; x++)
/*      */     {
/* 1375 */       aVillages[x].poll(now, lowerFaith);
/*      */     }
/* 1377 */     if (lowerFaith)
/*      */     {
/* 1379 */       lastPolledVillageFaith = System.currentTimeMillis();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static final Village getCapital(byte kingdom) {
/* 1385 */     Village[] vills = getVillages();
/* 1386 */     for (int x = 0; x < vills.length; x++) {
/*      */       
/* 1388 */       if ((vills[x]).kingdom == kingdom && vills[x].isCapital())
/*      */       {
/* 1390 */         return vills[x];
/*      */       }
/*      */     } 
/* 1393 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final Village getFirstVillageForKingdom(byte kingdom) {
/* 1398 */     Village[] vills = getVillages();
/* 1399 */     for (int x = 0; x < vills.length; x++) {
/*      */       
/* 1401 */       if ((vills[x]).kingdom == kingdom)
/*      */       {
/* 1403 */         return vills[x];
/*      */       }
/*      */     } 
/* 1406 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final Village getFirstPermanentVillageForKingdom(byte kingdom) {
/* 1411 */     Village[] vills = getVillages();
/* 1412 */     for (int x = 0; x < vills.length; x++) {
/*      */       
/* 1414 */       if ((vills[x]).kingdom == kingdom && (vills[x]).isPermanent)
/*      */       {
/* 1416 */         return vills[x];
/*      */       }
/*      */     } 
/* 1419 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final Village[] getPermanentVillagesForKingdom(byte kingdom) {
/* 1424 */     ConcurrentHashMap<Integer, Village> permVills = new ConcurrentHashMap<>();
/* 1425 */     for (Village village : villages.values()) {
/*      */       
/* 1427 */       if (village.isPermanent && village.kingdom == kingdom)
/* 1428 */         permVills.put(Integer.valueOf(village.getId()), village); 
/*      */     } 
/* 1430 */     return (Village[])permVills.values().toArray((Object[])new Village[permVills.size()]);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean wasLastVillage(Village village) {
/* 1435 */     Village[] vills = getVillages();
/* 1436 */     for (int x = 0; x < vills.length; x++) {
/*      */       
/* 1438 */       if (village.getId() != vills[x].getId() && (vills[x]).kingdom == village.kingdom)
/*      */       {
/* 1440 */         return false;
/*      */       }
/*      */     } 
/* 1443 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void convertTowers() {
/* 1448 */     Village[] vills = getVillages();
/*      */     int x;
/* 1450 */     for (x = 0; x < vills.length; x++)
/*      */     {
/* 1452 */       vills[x].convertTowersWithinDistance(150);
/*      */     }
/*      */     
/* 1455 */     for (x = 0; x < vills.length; x++)
/*      */     {
/* 1457 */       vills[x].convertTowersWithinPerimeter();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static final Village[] getPermanentVillages(byte kingdomChecked) {
/* 1463 */     Set<Village> toReturn = new HashSet<>();
/*      */     
/* 1465 */     Kingdom kingd = Kingdoms.getKingdom(kingdomChecked);
/* 1466 */     if (kingd != null)
/*      */     {
/* 1468 */       for (Village v : villages.values()) {
/*      */         
/* 1470 */         if (v.kingdom == kingdomChecked)
/*      */         {
/* 1472 */           if (v.isPermanent || (v.isCapital() && kingd.isCustomKingdom()))
/* 1473 */             toReturn.add(v); 
/*      */         }
/*      */       } 
/*      */     }
/* 1477 */     return toReturn.<Village>toArray(new Village[toReturn.size()]);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final Village[] getKosVillagesFor(long playerId) {
/* 1482 */     Set<Village> toReturn = new HashSet<>();
/* 1483 */     for (Village v : villages.values()) {
/*      */       
/* 1485 */       Reputation rep = v.getReputationObject(playerId);
/* 1486 */       if (rep != null)
/*      */       {
/* 1488 */         toReturn.add(v);
/*      */       }
/*      */     } 
/* 1491 */     return toReturn.<Village>toArray(new Village[toReturn.size()]);
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static final Village getVillageFor(Item waystone) {
/* 1497 */     for (Village village : villages.values()) {
/*      */       
/* 1499 */       if (village.coversPlus(waystone.getTileX(), waystone.getTileY(), 2))
/*      */       {
/* 1501 */         return village;
/*      */       }
/*      */     } 
/* 1504 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final ArrayList<DeadVillage> getDeadVillagesFor(int tilex, int tiley) {
/* 1509 */     return getDeadVillagesNear(tilex, tiley, 0);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final ArrayList<DeadVillage> getDeadVillagesNear(int tilex, int tiley, int range) {
/* 1514 */     ArrayList<DeadVillage> toReturn = new ArrayList<>();
/* 1515 */     for (DeadVillage dv : deadVillages.values()) {
/*      */       
/* 1517 */       if (dv.getStartX() - range > tilex || dv.getEndX() + range < tilex || dv
/* 1518 */         .getStartY() - range > tiley || dv.getEndY() + range < tiley) {
/*      */         continue;
/*      */       }
/* 1521 */       toReturn.add(dv);
/*      */     } 
/* 1523 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final DeadVillage getDeadVillage(long deadVillageId) {
/* 1528 */     return deadVillages.get(Long.valueOf(deadVillageId));
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\villages\Villages.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */