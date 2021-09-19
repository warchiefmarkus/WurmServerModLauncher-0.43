/*      */ package com.wurmonline.server.items;
/*      */ 
/*      */ import com.wurmonline.communication.SocketConnection;
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.server.GeneralUtilities;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.LoginHandler;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.NoSuchEntryException;
/*      */ import com.wurmonline.server.behaviours.ActionEntry;
/*      */ import com.wurmonline.server.behaviours.Actions;
/*      */ import com.wurmonline.server.behaviours.BuildMaterial;
/*      */ import com.wurmonline.server.behaviours.CaveWallBehaviour;
/*      */ import com.wurmonline.server.behaviours.MethodsStructure;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.skills.Skill;
/*      */ import com.wurmonline.server.skills.SkillSystem;
/*      */ import com.wurmonline.server.structures.BridgePartEnum;
/*      */ import com.wurmonline.server.structures.Fence;
/*      */ import com.wurmonline.server.structures.RoofFloorEnum;
/*      */ import com.wurmonline.server.structures.Structure;
/*      */ import com.wurmonline.server.structures.Wall;
/*      */ import com.wurmonline.server.structures.WallEnum;
/*      */ import com.wurmonline.server.utils.StringUtil;
/*      */ import com.wurmonline.shared.constants.ProtoConstants;
/*      */ import com.wurmonline.shared.constants.StructureConstantsEnum;
/*      */ import com.wurmonline.shared.constants.WallConstants;
/*      */ import java.io.IOException;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Optional;
/*      */ import java.util.Set;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.annotation.Nonnull;
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
/*      */ public final class CreationWindowMethods
/*      */   implements ProtoConstants, MiscConstants
/*      */ {
/*   71 */   private static final Logger logger = Logger.getLogger(CreationWindowMethods.class.getName());
/*      */   
/*      */   private static final String CHARSET_ENCODING_FOR_COMMS = "UTF-8";
/*      */ 
/*      */   
/*      */   public static final boolean createWallBuildingBuffer(SocketConnection connection, @Nonnull Wall wall, @Nonnull Player player, long toolId) {
/*   77 */     Item tool = null;
/*      */ 
/*      */     
/*   80 */     if (toolId != -10L) {
/*      */       
/*   82 */       Optional<Item> optTool = Items.getItemOptional(toolId);
/*      */       
/*   84 */       if (!optTool.isPresent())
/*   85 */         return false; 
/*   86 */       tool = optTool.get();
/*      */     } 
/*      */ 
/*      */     
/*   90 */     WallEnum wallEnum = WallEnum.WALL_PLAN;
/*   91 */     wallEnum = WallEnum.getWall(wall.getType(), wall.getMaterial());
/*      */ 
/*      */     
/*   94 */     if (wallEnum == WallEnum.WALL_PLAN)
/*      */     {
/*   96 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  100 */     boolean sendNeededTool = (tool == null || !WallEnum.isCorrectTool(wallEnum, (Creature)player, tool));
/*      */ 
/*      */     
/*  103 */     ByteBuffer buffer = connection.getBuffer();
/*      */ 
/*      */     
/*  106 */     addPartialRequestHeader(buffer);
/*      */ 
/*      */     
/*  109 */     buffer.put((byte)1);
/*      */ 
/*      */     
/*  112 */     buffer.putShort((short)(sendNeededTool ? 2 : 1));
/*      */ 
/*      */     
/*  115 */     if (sendNeededTool)
/*      */     {
/*      */       
/*  118 */       if (!addToolsNeededForWall(buffer, wallEnum, player)) {
/*      */         
/*  120 */         connection.clearBuffer();
/*  121 */         return false;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*  126 */     addStringToBuffer(buffer, "Item(s) needed in inventory", false);
/*      */ 
/*      */     
/*  129 */     int[] needed = WallEnum.getMaterialsNeeded(wall);
/*      */ 
/*      */     
/*  132 */     buffer.putShort((short)(needed.length / 2));
/*      */ 
/*      */     
/*  135 */     for (int i = 0; i < needed.length; i += 2) {
/*      */       
/*  137 */       ItemTemplate template = getItemTemplate(needed[i]);
/*  138 */       if (template == null) {
/*      */         
/*  140 */         connection.clearBuffer();
/*  141 */         return false;
/*      */       } 
/*      */ 
/*      */       
/*  145 */       String name = getFenceMaterialName(template);
/*  146 */       addStringToBuffer(buffer, name, false);
/*      */ 
/*      */       
/*  149 */       buffer.putShort(template.getImageNumber());
/*      */ 
/*      */       
/*  152 */       short chance = (short)needed[i + 1];
/*  153 */       buffer.putShort(chance);
/*      */ 
/*      */       
/*  156 */       buffer.putShort(wallEnum.getActionId());
/*      */     } 
/*      */     
/*  159 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean addToolsNeededForWall(ByteBuffer buffer, WallEnum wallEnum, @Nonnull Player player) {
/*  166 */     addStringToBuffer(buffer, "Needed tool in crafting window", false);
/*      */ 
/*      */     
/*  169 */     List<Integer> list = WallEnum.getToolsForWall(wallEnum, (Creature)player);
/*      */ 
/*      */     
/*  172 */     buffer.putShort((short)list.size());
/*      */ 
/*      */     
/*  175 */     for (Integer tid : list) {
/*      */ 
/*      */       
/*  178 */       ItemTemplate template = getItemTemplate(tid.intValue());
/*      */ 
/*      */       
/*  181 */       if (template == null)
/*      */       {
/*      */         
/*  184 */         return false;
/*      */       }
/*      */ 
/*      */       
/*  188 */       String name = getFenceMaterialName(template);
/*  189 */       addStringToBuffer(buffer, name, false);
/*      */ 
/*      */       
/*  192 */       buffer.putShort(template.getImageNumber());
/*      */ 
/*      */       
/*  195 */       short chance = 1;
/*  196 */       buffer.putShort((short)1);
/*  197 */       buffer.putShort(wallEnum.getActionId());
/*      */     } 
/*  199 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final String getFenceMaterialName(ItemTemplate template) {
/*  204 */     if (template.getTemplateId() == 218)
/*  205 */       return "small iron " + template.getName(); 
/*  206 */     if (template.getTemplateId() == 217) {
/*  207 */       return "large iron " + template.getName();
/*      */     }
/*  209 */     return template.getName();
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
/*      */   public static final boolean createWallPlanBuffer(SocketConnection connection, @Nonnull Structure structure, @Nonnull Wall wall, @Nonnull Player player, long toolId) {
/*  226 */     if (toolId == -10L) {
/*  227 */       return false;
/*      */     }
/*      */     
/*  230 */     Optional<Item> optTool = Items.getItemOptional(toolId);
/*      */     
/*  232 */     if (!optTool.isPresent())
/*      */     {
/*  234 */       return false;
/*      */     }
/*  236 */     Item tool = optTool.get();
/*      */ 
/*      */     
/*  239 */     List<WallEnum> wallList = WallEnum.getWallsByTool((Creature)player, tool, structure.needsDoor(), 
/*  240 */         MethodsStructure.hasInsideFence(wall));
/*      */ 
/*      */     
/*  243 */     if (wallList.size() == 0) {
/*  244 */       return false;
/*      */     }
/*      */     
/*  247 */     ByteBuffer buffer = connection.getBuffer();
/*      */ 
/*      */     
/*  250 */     addPartialRequestHeader(buffer);
/*      */ 
/*      */     
/*  253 */     buffer.put((byte)0);
/*  254 */     buffer.putShort((short)1);
/*      */ 
/*      */     
/*  257 */     addStringToBuffer(buffer, "Walls", false);
/*      */ 
/*      */     
/*  260 */     buffer.putShort((short)wallList.size());
/*      */ 
/*      */     
/*  263 */     for (WallEnum en : wallList) {
/*      */ 
/*      */       
/*  266 */       addStringToBuffer(buffer, en.getName(), false);
/*      */ 
/*      */       
/*  269 */       buffer.putShort(en.getIcon());
/*      */ 
/*      */       
/*  272 */       boolean canBuild = WallEnum.canBuildWall(wall, en.getMaterial(), (Creature)player);
/*  273 */       short chance = (short)(canBuild ? 100 : 0);
/*  274 */       buffer.putShort(chance);
/*      */ 
/*      */       
/*  277 */       buffer.putShort(en.getActionId());
/*      */     } 
/*      */     
/*  280 */     return true;
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
/*      */   public static final boolean createCaveCladdingBuffer(SocketConnection connection, int tilex, int tiley, int tile, byte type, Player player, long toolId) {
/*  295 */     Item tool = null;
/*      */ 
/*      */     
/*  298 */     if (toolId != -10L) {
/*      */       
/*  300 */       Optional<Item> optTool = Items.getItemOptional(toolId);
/*      */       
/*  302 */       if (!optTool.isPresent())
/*  303 */         return false; 
/*  304 */       tool = optTool.get();
/*      */     } 
/*      */ 
/*      */     
/*  308 */     boolean sendNeededTool = (tool == null || !CaveWallBehaviour.isCorrectTool(type, (Creature)player, tool));
/*      */ 
/*      */     
/*  311 */     ByteBuffer buffer = connection.getBuffer();
/*      */ 
/*      */     
/*  314 */     addPartialRequestHeader(buffer);
/*      */ 
/*      */     
/*  317 */     buffer.put((byte)1);
/*      */ 
/*      */     
/*  320 */     buffer.putShort((short)(sendNeededTool ? 3 : 2));
/*      */ 
/*      */     
/*  323 */     if (sendNeededTool)
/*      */     {
/*      */       
/*  326 */       if (!addToolsNeededForWall(buffer, type, player)) {
/*      */         
/*  328 */         connection.clearBuffer();
/*  329 */         return false;
/*      */       } 
/*      */     }
/*      */     
/*  333 */     addStringToBuffer(buffer, "Item(s) needed in inventory", false);
/*      */ 
/*      */     
/*  336 */     short action = CaveWallBehaviour.actionFromWallType(type);
/*  337 */     int[] needed = CaveWallBehaviour.getMaterialsNeeded(tilex, tiley, type);
/*      */ 
/*      */     
/*  340 */     buffer.putShort((short)(needed.length / 2));
/*      */     
/*      */     int i;
/*  343 */     for (i = 0; i < needed.length; i += 2) {
/*      */       
/*  345 */       ItemTemplate template = getItemTemplate(needed[i]);
/*  346 */       if (template == null) {
/*      */         
/*  348 */         connection.clearBuffer();
/*  349 */         return false;
/*      */       } 
/*      */       
/*  352 */       String name = getFenceMaterialName(template);
/*  353 */       addStringToBuffer(buffer, name, false);
/*      */ 
/*      */       
/*  356 */       buffer.putShort(template.getImageNumber());
/*      */ 
/*      */       
/*  359 */       short chance = 1;
/*  360 */       buffer.putShort((short)1);
/*      */ 
/*      */       
/*  363 */       buffer.putShort(action);
/*      */     } 
/*  365 */     addStringToBuffer(buffer, "Total materials needed", false);
/*      */     
/*  367 */     if (needed.length == 1 && needed[0] == -1) {
/*  368 */       buffer.putShort((short)0);
/*      */     } else {
/*      */       
/*  371 */       buffer.putShort((short)(needed.length / 2));
/*  372 */       for (i = 0; i < needed.length; i += 2) {
/*      */         
/*  374 */         ItemTemplate template = getItemTemplate(needed[i]);
/*  375 */         String name = getFenceMaterialName(template);
/*  376 */         addStringToBuffer(buffer, name, false);
/*  377 */         buffer.putShort(template.getImageNumber());
/*  378 */         short chance = (short)needed[i + 1];
/*  379 */         buffer.putShort(chance);
/*  380 */         buffer.putShort(action);
/*      */       } 
/*      */     } 
/*      */     
/*  384 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean addToolsNeededForWall(ByteBuffer buffer, byte type, @Nonnull Player player) {
/*  390 */     addStringToBuffer(buffer, "Needed tool in crafting window", false);
/*      */ 
/*      */     
/*  393 */     List<Integer> list = CaveWallBehaviour.getToolsForType(type, (Creature)player);
/*      */ 
/*      */     
/*  396 */     buffer.putShort((short)list.size());
/*      */ 
/*      */     
/*  399 */     for (Integer tid : list) {
/*      */ 
/*      */       
/*  402 */       ItemTemplate template = getItemTemplate(tid.intValue());
/*      */ 
/*      */       
/*  405 */       if (template == null)
/*      */       {
/*      */         
/*  408 */         return false;
/*      */       }
/*      */ 
/*      */       
/*  412 */       String name = getFenceMaterialName(template);
/*  413 */       addStringToBuffer(buffer, name, false);
/*      */ 
/*      */       
/*  416 */       buffer.putShort(template.getImageNumber());
/*      */ 
/*      */       
/*  419 */       short chance = 1;
/*  420 */       buffer.putShort((short)1);
/*  421 */       buffer.putShort(CaveWallBehaviour.actionFromWallType(type));
/*      */     } 
/*  423 */     return true;
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
/*      */   public static final boolean createCaveReinforcedBuffer(SocketConnection connection, Player player, long toolId) {
/*  439 */     if (toolId == -10L) {
/*  440 */       return false;
/*      */     }
/*      */     
/*  443 */     Optional<Item> optTool = Items.getItemOptional(toolId);
/*      */     
/*  445 */     if (!optTool.isPresent())
/*      */     {
/*  447 */       return false;
/*      */     }
/*  449 */     Item tool = optTool.get();
/*      */ 
/*      */ 
/*      */     
/*  453 */     byte[] canMake = CaveWallBehaviour.getMaterialsFromToolType((Creature)player, tool);
/*      */ 
/*      */     
/*  456 */     if (canMake.length == 0) {
/*  457 */       return false;
/*      */     }
/*      */     
/*  460 */     ByteBuffer buffer = connection.getBuffer();
/*      */ 
/*      */     
/*  463 */     addPartialRequestHeader(buffer);
/*      */ 
/*      */     
/*  466 */     buffer.put((byte)0);
/*  467 */     buffer.putShort((short)1);
/*      */ 
/*      */     
/*  470 */     addStringToBuffer(buffer, "CaveWalls", false);
/*      */ 
/*      */     
/*  473 */     buffer.putShort((short)canMake.length);
/*      */ 
/*      */     
/*  476 */     for (byte type : canMake) {
/*      */       
/*  478 */       Tiles.Tile theTile = Tiles.getTile(type);
/*      */       
/*  480 */       addStringToBuffer(buffer, theTile.getName(), false);
/*      */ 
/*      */       
/*  483 */       buffer.putShort((short)theTile.getIconId());
/*      */ 
/*      */       
/*  486 */       boolean canBuild = CaveWallBehaviour.canCladWall(type, (Creature)player);
/*  487 */       short chance = (short)(canBuild ? 100 : 0);
/*  488 */       buffer.putShort(chance);
/*      */       
/*  490 */       buffer.putShort(CaveWallBehaviour.actionFromWallType(type));
/*      */     } 
/*  492 */     return true;
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
/*      */   public static final boolean createHedgeCreationBuffer(SocketConnection connection, @Nonnull Item sprout, long borderId, @Nonnull Player player) {
/*  508 */     StructureConstantsEnum hedgeType = Fence.getLowHedgeType(sprout.getMaterial());
/*      */ 
/*      */     
/*  511 */     if (hedgeType == StructureConstantsEnum.FENCE_PLAN_WOODEN) {
/*  512 */       return false;
/*      */     }
/*      */     
/*  515 */     int x = Tiles.decodeTileX(borderId);
/*  516 */     int y = Tiles.decodeTileY(borderId);
/*      */ 
/*      */     
/*  519 */     Tiles.TileBorderDirection dir = Tiles.decodeDirection(borderId);
/*      */ 
/*      */     
/*  522 */     Structure structure = MethodsStructure.getStructureOrNullAtTileBorder(x, y, dir, true);
/*      */ 
/*      */     
/*  525 */     if (structure != null) {
/*  526 */       return false;
/*      */     }
/*      */     
/*  529 */     if (!player.isOnSurface())
/*      */     {
/*  531 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  535 */     ByteBuffer buffer = connection.getBuffer();
/*      */ 
/*      */     
/*  538 */     addPartialRequestHeader(buffer);
/*      */ 
/*      */     
/*  541 */     buffer.put((byte)0);
/*      */ 
/*      */     
/*  544 */     buffer.putShort((short)1);
/*      */ 
/*      */     
/*  547 */     addStringToBuffer(buffer, "Hedges", false);
/*      */ 
/*      */     
/*  550 */     buffer.putShort((short)1);
/*      */ 
/*      */     
/*  553 */     String name = WallConstants.getName(hedgeType);
/*      */ 
/*      */     
/*  556 */     addStringToBuffer(buffer, name, false);
/*      */ 
/*      */     
/*  559 */     buffer.putShort((short)60);
/*      */ 
/*      */     
/*  562 */     Skill gardening = player.getSkills().getSkillOrLearn(10045);
/*      */ 
/*      */     
/*  565 */     short chance = (short)(int)gardening.getChance((1.0F + sprout.getDamage()), null, sprout.getQualityLevel());
/*  566 */     buffer.putShort(chance);
/*      */ 
/*      */     
/*  569 */     buffer.putShort(Actions.actionEntrys[186].getNumber());
/*      */     
/*  571 */     return true;
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
/*      */   public static final boolean createFlowerbedBuffer(SocketConnection connection, @Nonnull Item tool, long borderId, @Nonnull Player player) {
/*  587 */     StructureConstantsEnum flowerbedType = Fence.getFlowerbedType(tool.getTemplateId());
/*      */ 
/*      */     
/*  590 */     int x = Tiles.decodeTileX(borderId);
/*  591 */     int y = Tiles.decodeTileY(borderId);
/*      */ 
/*      */     
/*  594 */     Tiles.TileBorderDirection dir = Tiles.decodeDirection(borderId);
/*      */ 
/*      */     
/*  597 */     Structure structure = MethodsStructure.getStructureOrNullAtTileBorder(x, y, dir, true);
/*      */ 
/*      */     
/*  600 */     if (structure != null) {
/*  601 */       return false;
/*      */     }
/*      */     
/*  604 */     if (!player.isOnSurface())
/*      */     {
/*  606 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  610 */     ByteBuffer buffer = connection.getBuffer();
/*      */ 
/*      */     
/*  613 */     addPartialRequestHeader(buffer);
/*      */ 
/*      */     
/*  616 */     buffer.put((byte)0);
/*      */ 
/*      */     
/*  619 */     buffer.putShort((short)1);
/*      */ 
/*      */     
/*  622 */     addStringToBuffer(buffer, "Flowerbeds", false);
/*      */ 
/*      */     
/*  625 */     buffer.putShort((short)1);
/*      */ 
/*      */     
/*  628 */     String name = WallConstants.getName(flowerbedType);
/*      */ 
/*      */     
/*  631 */     addStringToBuffer(buffer, name, false);
/*      */ 
/*      */     
/*  634 */     buffer.putShort((short)60);
/*      */ 
/*      */     
/*  637 */     Skill gardening = player.getSkills().getSkillOrLearn(10045);
/*      */ 
/*      */     
/*  640 */     short chance = (short)(int)gardening.getChance((1.0F + tool.getDamage()), null, tool.getQualityLevel());
/*      */ 
/*      */     
/*  643 */     buffer.putShort(chance);
/*      */ 
/*      */     
/*  646 */     buffer.putShort(Actions.actionEntrys[563].getNumber());
/*      */     
/*  648 */     return true;
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
/*      */   public static final boolean createFenceListBuffer(SocketConnection connection, long borderId) {
/*  662 */     int x = Tiles.decodeTileX(borderId);
/*  663 */     int y = Tiles.decodeTileY(borderId);
/*      */ 
/*      */     
/*  666 */     Tiles.TileBorderDirection dir = Tiles.decodeDirection(borderId);
/*  667 */     int heightOffset = Tiles.decodeHeightOffset(borderId);
/*  668 */     boolean onSurface = true;
/*  669 */     boolean hasArch = false;
/*      */     
/*  671 */     if (MethodsStructure.doesTileBorderContainWallOrFence(x, y, heightOffset, dir, true, false)) {
/*  672 */       hasArch = true;
/*      */     }
/*      */     
/*  675 */     Structure structure = MethodsStructure.getStructureOrNullAtTileBorder(x, y, dir, true);
/*      */ 
/*      */     
/*  678 */     Map<String, List<ActionEntry>> fenceList = createFenceCreationList((structure != null), false, hasArch);
/*      */ 
/*      */ 
/*      */     
/*  682 */     if (Items.getMarker(x, y, true, 0, -10L) != null)
/*  683 */       return false; 
/*  684 */     if (dir == Tiles.TileBorderDirection.DIR_HORIZ && Items.getMarker(x + 1, y, true, 0, -10L) != null)
/*  685 */       return false; 
/*  686 */     if (dir == Tiles.TileBorderDirection.DIR_DOWN && Items.getMarker(x, y + 1, true, 0, -10L) != null) {
/*  687 */       return false;
/*      */     }
/*      */     
/*  690 */     if (fenceList.size() == 0) {
/*  691 */       return false;
/*      */     }
/*      */     
/*  694 */     ByteBuffer buffer = connection.getBuffer();
/*      */ 
/*      */     
/*  697 */     addPartialRequestHeader(buffer);
/*      */ 
/*      */     
/*  700 */     buffer.put((byte)0);
/*      */ 
/*      */     
/*  703 */     buffer.putShort((short)fenceList.size());
/*      */ 
/*      */     
/*  706 */     for (String category : fenceList.keySet()) {
/*      */ 
/*      */       
/*  709 */       addStringToBuffer(buffer, category, false);
/*      */ 
/*      */       
/*  712 */       List<ActionEntry> fences = fenceList.get(category);
/*      */ 
/*      */       
/*  715 */       buffer.putShort((short)fences.size());
/*      */ 
/*      */       
/*  718 */       for (ActionEntry ae : fences) {
/*      */ 
/*      */         
/*  721 */         StructureConstantsEnum type = Fence.getFencePlanType(ae.getNumber());
/*      */ 
/*      */         
/*  724 */         String name = WallConstants.getName(Fence.getFenceForPlan(type));
/*      */ 
/*      */         
/*  727 */         addStringToBuffer(buffer, name, false);
/*      */ 
/*      */         
/*  730 */         buffer.putShort((short)60);
/*      */ 
/*      */         
/*  733 */         short chance = 100;
/*  734 */         buffer.putShort((short)100);
/*      */ 
/*      */         
/*  737 */         buffer.putShort(ae.getNumber());
/*      */       } 
/*      */     } 
/*  740 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final Map<String, List<ActionEntry>> createFenceCreationList(boolean inStructure, boolean showAll, boolean borderHasArch) {
/*  746 */     Map<String, List<ActionEntry>> list = new HashMap<>();
/*      */     
/*  748 */     if (!inStructure || showAll)
/*  749 */       list.put("Log", new ArrayList<>()); 
/*  750 */     list.put("Plank", new ArrayList<>());
/*  751 */     list.put("Rope", new ArrayList<>());
/*  752 */     list.put("Shaft", new ArrayList<>());
/*  753 */     list.put("Woven", new ArrayList<>());
/*  754 */     list.put("Stone", new ArrayList<>());
/*  755 */     list.put("Iron", new ArrayList<>());
/*  756 */     list.put("Slate", new ArrayList<>());
/*  757 */     list.put("Rounded stone", new ArrayList<>());
/*  758 */     list.put("Pottery", new ArrayList<>());
/*  759 */     list.put("Sandstone", new ArrayList<>());
/*  760 */     list.put("Marble", new ArrayList<>());
/*      */ 
/*      */     
/*  763 */     if (!inStructure || showAll) {
/*      */ 
/*      */       
/*  766 */       ((List<ActionEntry>)list.get("Log")).add(Actions.actionEntrys[165]);
/*  767 */       ((List<ActionEntry>)list.get("Log")).add(Actions.actionEntrys[167]);
/*      */     } 
/*      */ 
/*      */     
/*  771 */     ((List<ActionEntry>)list.get("Plank")).add(Actions.actionEntrys[166]);
/*  772 */     ((List<ActionEntry>)list.get("Plank")).add(Actions.actionEntrys[168]);
/*  773 */     ((List<ActionEntry>)list.get("Plank")).add(Actions.actionEntrys[520]);
/*  774 */     ((List<ActionEntry>)list.get("Plank")).add(Actions.actionEntrys[528]);
/*      */ 
/*      */     
/*  777 */     if ((inStructure && !borderHasArch) || showAll)
/*      */     {
/*      */       
/*  780 */       ((List<ActionEntry>)list.get("Plank")).add(Actions.actionEntrys[516]);
/*      */     }
/*      */ 
/*      */     
/*  784 */     ((List<ActionEntry>)list.get("Rope")).add(Actions.actionEntrys[543]);
/*  785 */     ((List<ActionEntry>)list.get("Rope")).add(Actions.actionEntrys[544]);
/*      */ 
/*      */     
/*  788 */     ((List<ActionEntry>)list.get("Shaft")).add(Actions.actionEntrys[526]);
/*  789 */     ((List<ActionEntry>)list.get("Shaft")).add(Actions.actionEntrys[527]);
/*  790 */     ((List<ActionEntry>)list.get("Shaft")).add(Actions.actionEntrys[529]);
/*      */ 
/*      */     
/*  793 */     ((List<ActionEntry>)list.get("Woven")).add(Actions.actionEntrys[478]);
/*      */ 
/*      */     
/*  796 */     if (!inStructure || showAll)
/*  797 */       ((List<ActionEntry>)list.get("Stone")).add(Actions.actionEntrys[163]); 
/*  798 */     if (!inStructure || !borderHasArch || showAll)
/*  799 */       ((List<ActionEntry>)list.get("Stone")).add(Actions.actionEntrys[164]); 
/*  800 */     if ((!inStructure && !borderHasArch) || showAll) {
/*  801 */       ((List<ActionEntry>)list.get("Stone")).add(Actions.actionEntrys[654]);
/*      */     }
/*      */     
/*  804 */     ((List<ActionEntry>)list.get("Stone")).add(Actions.actionEntrys[541]);
/*  805 */     ((List<ActionEntry>)list.get("Stone")).add(Actions.actionEntrys[542]);
/*      */ 
/*      */     
/*  808 */     if ((inStructure && !borderHasArch) || showAll)
/*      */     {
/*      */       
/*  811 */       ((List<ActionEntry>)list.get("Stone")).add(Actions.actionEntrys[517]);
/*      */     }
/*      */ 
/*      */     
/*  815 */     ((List<ActionEntry>)list.get("Iron")).add(Actions.actionEntrys[477]);
/*  816 */     ((List<ActionEntry>)list.get("Iron")).add(Actions.actionEntrys[479]);
/*      */     
/*  818 */     if (!inStructure || !borderHasArch || showAll) {
/*      */ 
/*      */       
/*  821 */       ((List<ActionEntry>)list.get("Iron")).add(Actions.actionEntrys[545]);
/*  822 */       ((List<ActionEntry>)list.get("Iron")).add(Actions.actionEntrys[546]);
/*      */     } 
/*      */ 
/*      */     
/*  826 */     ((List<ActionEntry>)list.get("Iron")).add(Actions.actionEntrys[611]);
/*      */ 
/*      */     
/*  829 */     if (inStructure || showAll)
/*      */     {
/*      */       
/*  832 */       ((List<ActionEntry>)list.get("Iron")).add(Actions.actionEntrys[521]);
/*      */     }
/*      */     
/*  835 */     ((List<ActionEntry>)list.get("Slate")).add(Actions.actionEntrys[832]);
/*  836 */     ((List<ActionEntry>)list.get("Slate")).add(Actions.actionEntrys[833]);
/*  837 */     ((List<ActionEntry>)list.get("Slate")).add(Actions.actionEntrys[834]);
/*  838 */     if (!inStructure || !borderHasArch || showAll)
/*  839 */       ((List<ActionEntry>)list.get("Slate")).add(Actions.actionEntrys[870]); 
/*  840 */     if ((!inStructure && !borderHasArch) || showAll)
/*  841 */       ((List<ActionEntry>)list.get("Slate")).add(Actions.actionEntrys[871]); 
/*  842 */     if (!inStructure || !borderHasArch || showAll) {
/*      */       
/*  844 */       ((List<ActionEntry>)list.get("Slate")).add(Actions.actionEntrys[872]);
/*  845 */       ((List<ActionEntry>)list.get("Slate")).add(Actions.actionEntrys[873]);
/*      */     } 
/*  847 */     if ((inStructure && !borderHasArch) || showAll)
/*  848 */       ((List<ActionEntry>)list.get("Slate")).add(Actions.actionEntrys[874]); 
/*  849 */     ((List<ActionEntry>)list.get("Slate")).add(Actions.actionEntrys[875]);
/*      */     
/*  851 */     ((List<ActionEntry>)list.get("Rounded stone")).add(Actions.actionEntrys[835]);
/*  852 */     ((List<ActionEntry>)list.get("Rounded stone")).add(Actions.actionEntrys[836]);
/*  853 */     ((List<ActionEntry>)list.get("Rounded stone")).add(Actions.actionEntrys[837]);
/*  854 */     if (!inStructure || !borderHasArch || showAll)
/*  855 */       ((List<ActionEntry>)list.get("Rounded stone")).add(Actions.actionEntrys[876]); 
/*  856 */     if ((!inStructure && !borderHasArch) || showAll)
/*  857 */       ((List<ActionEntry>)list.get("Rounded stone")).add(Actions.actionEntrys[877]); 
/*  858 */     if (!inStructure || !borderHasArch || showAll) {
/*      */       
/*  860 */       ((List<ActionEntry>)list.get("Rounded stone")).add(Actions.actionEntrys[878]);
/*  861 */       ((List<ActionEntry>)list.get("Rounded stone")).add(Actions.actionEntrys[879]);
/*      */     } 
/*  863 */     if ((inStructure && !borderHasArch) || showAll)
/*  864 */       ((List<ActionEntry>)list.get("Rounded stone")).add(Actions.actionEntrys[880]); 
/*  865 */     ((List<ActionEntry>)list.get("Rounded stone")).add(Actions.actionEntrys[881]);
/*      */     
/*  867 */     ((List<ActionEntry>)list.get("Pottery")).add(Actions.actionEntrys[838]);
/*  868 */     ((List<ActionEntry>)list.get("Pottery")).add(Actions.actionEntrys[839]);
/*  869 */     ((List<ActionEntry>)list.get("Pottery")).add(Actions.actionEntrys[840]);
/*  870 */     if (!inStructure || !borderHasArch || showAll)
/*  871 */       ((List<ActionEntry>)list.get("Pottery")).add(Actions.actionEntrys[894]); 
/*  872 */     if ((!inStructure && !borderHasArch) || showAll)
/*  873 */       ((List<ActionEntry>)list.get("Pottery")).add(Actions.actionEntrys[895]); 
/*  874 */     if (!inStructure || !borderHasArch || showAll) {
/*      */       
/*  876 */       ((List<ActionEntry>)list.get("Pottery")).add(Actions.actionEntrys[896]);
/*  877 */       ((List<ActionEntry>)list.get("Pottery")).add(Actions.actionEntrys[897]);
/*      */     } 
/*  879 */     if ((inStructure && !borderHasArch) || showAll)
/*  880 */       ((List<ActionEntry>)list.get("Pottery")).add(Actions.actionEntrys[898]); 
/*  881 */     ((List<ActionEntry>)list.get("Pottery")).add(Actions.actionEntrys[899]);
/*      */     
/*  883 */     ((List<ActionEntry>)list.get("Sandstone")).add(Actions.actionEntrys[841]);
/*  884 */     ((List<ActionEntry>)list.get("Sandstone")).add(Actions.actionEntrys[842]);
/*  885 */     ((List<ActionEntry>)list.get("Sandstone")).add(Actions.actionEntrys[843]);
/*  886 */     if (!inStructure || !borderHasArch || showAll)
/*  887 */       ((List<ActionEntry>)list.get("Sandstone")).add(Actions.actionEntrys[882]); 
/*  888 */     if ((!inStructure && !borderHasArch) || showAll)
/*  889 */       ((List<ActionEntry>)list.get("Sandstone")).add(Actions.actionEntrys[883]); 
/*  890 */     if (!inStructure || !borderHasArch || showAll) {
/*      */       
/*  892 */       ((List<ActionEntry>)list.get("Sandstone")).add(Actions.actionEntrys[884]);
/*  893 */       ((List<ActionEntry>)list.get("Sandstone")).add(Actions.actionEntrys[885]);
/*      */     } 
/*  895 */     if ((inStructure && !borderHasArch) || showAll)
/*  896 */       ((List<ActionEntry>)list.get("Sandstone")).add(Actions.actionEntrys[886]); 
/*  897 */     ((List<ActionEntry>)list.get("Sandstone")).add(Actions.actionEntrys[887]);
/*      */     
/*  899 */     ((List<ActionEntry>)list.get("Marble")).add(Actions.actionEntrys[844]);
/*  900 */     ((List<ActionEntry>)list.get("Marble")).add(Actions.actionEntrys[845]);
/*  901 */     ((List<ActionEntry>)list.get("Marble")).add(Actions.actionEntrys[846]);
/*  902 */     if (!inStructure || !borderHasArch || showAll)
/*  903 */       ((List<ActionEntry>)list.get("Marble")).add(Actions.actionEntrys[900]); 
/*  904 */     if ((!inStructure && !borderHasArch) || showAll)
/*  905 */       ((List<ActionEntry>)list.get("Marble")).add(Actions.actionEntrys[901]); 
/*  906 */     if (!inStructure || !borderHasArch || showAll) {
/*      */       
/*  908 */       ((List<ActionEntry>)list.get("Marble")).add(Actions.actionEntrys[902]);
/*  909 */       ((List<ActionEntry>)list.get("Marble")).add(Actions.actionEntrys[903]);
/*      */     } 
/*  911 */     if ((inStructure && !borderHasArch) || showAll)
/*  912 */       ((List<ActionEntry>)list.get("Marble")).add(Actions.actionEntrys[904]); 
/*  913 */     ((List<ActionEntry>)list.get("Marble")).add(Actions.actionEntrys[905]);
/*      */     
/*  915 */     return list;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final List<ActionEntry> createCaveWallCreationList() {
/*  922 */     List<ActionEntry> list = new ArrayList<>();
/*      */     
/*  924 */     list.add(Actions.actionEntrys[856]);
/*  925 */     list.add(Actions.actionEntrys[857]);
/*  926 */     list.add(Actions.actionEntrys[858]);
/*  927 */     list.add(Actions.actionEntrys[859]);
/*  928 */     list.add(Actions.actionEntrys[860]);
/*  929 */     list.add(Actions.actionEntrys[861]);
/*  930 */     list.add(Actions.actionEntrys[862]);
/*      */     
/*  932 */     return list;
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
/*      */   public static final boolean createCreationListBuffer(SocketConnection connection, @Nonnull Item source, @Nonnull Item target, @Nonnull Player player) {
/*  947 */     Map<String, Map<CreationEntry, Integer>> map = GeneralUtilities.getCreationList(source, target, player);
/*      */     
/*  949 */     if (map.size() == 0) {
/*      */       
/*  951 */       Recipe recipe = Recipes.getRecipeFor(player.getWurmId(), (byte)2, source, target, true, false);
/*  952 */       if (recipe == null) {
/*  953 */         return false;
/*      */       }
/*  955 */       ByteBuffer byteBuffer = connection.getBuffer();
/*  956 */       addPartialRequestHeader(byteBuffer);
/*  957 */       byteBuffer.put((byte)0);
/*  958 */       byteBuffer.putShort((short)1);
/*  959 */       addStringToBuffer(byteBuffer, "Cooking", false);
/*  960 */       byteBuffer.putShort((short)1);
/*      */ 
/*      */       
/*  963 */       Item realSource = source;
/*  964 */       Item realTarget = target;
/*  965 */       if (recipe.hasActiveItem() && source != null && recipe.getActiveItem().getTemplateId() != realSource.getTemplateId()) {
/*      */         
/*  967 */         realSource = target;
/*  968 */         realTarget = source;
/*      */       } 
/*  970 */       ItemTemplate template = recipe.getResultTemplate(realTarget);
/*  971 */       if (template == null) {
/*      */         
/*  973 */         connection.clearBuffer();
/*  974 */         return false;
/*      */       } 
/*      */       
/*  977 */       addStringToBuffer(byteBuffer, recipe.getSubMenuName(realTarget), false);
/*  978 */       byteBuffer.putShort(template.getImageNumber());
/*  979 */       byteBuffer.putShort((short)(int)recipe.getChanceFor(realSource, realTarget, (Creature)player));
/*  980 */       byteBuffer.putShort(recipe.getMenuId());
/*      */       
/*  982 */       return true;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  987 */     ByteBuffer buffer = connection.getBuffer();
/*      */     
/*  989 */     addPartialRequestHeader(buffer);
/*      */     
/*  991 */     buffer.put((byte)0);
/*      */ 
/*      */     
/*  994 */     buffer.putShort((short)map.size());
/*      */     
/*  996 */     for (String category : map.keySet()) {
/*      */ 
/*      */       
/*  999 */       addStringToBuffer(buffer, category, false);
/*      */ 
/*      */       
/* 1002 */       Map<CreationEntry, Integer> entries = map.get(category);
/*      */ 
/*      */       
/* 1005 */       buffer.putShort((short)entries.size());
/*      */ 
/*      */       
/* 1008 */       if (!addCreationEntriesToPartialList(buffer, entries)) {
/*      */ 
/*      */         
/* 1011 */         connection.clearBuffer();
/* 1012 */         return false;
/*      */       } 
/*      */     } 
/*      */     
/* 1016 */     return true;
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
/*      */   public static final boolean createUnfinishedCreationListBuffer(SocketConnection connection, @Nonnull Item source, @Nonnull Player player) {
/* 1032 */     AdvancedCreationEntry entry = getAdvancedCreationEntry(source.getRealTemplateId());
/*      */     
/* 1034 */     if (entry == null) {
/* 1035 */       return false;
/*      */     }
/*      */     
/* 1038 */     List<String> itemNames = new ArrayList<>();
/* 1039 */     List<Integer> numberOfItemsNeeded = new ArrayList<>();
/* 1040 */     List<Short> icons = new ArrayList<>();
/*      */ 
/*      */     
/* 1043 */     if (!fillRequirmentsLists(entry, source, itemNames, numberOfItemsNeeded, icons)) {
/* 1044 */       return false;
/*      */     }
/*      */     
/* 1047 */     ByteBuffer buffer = connection.getBuffer();
/*      */ 
/*      */     
/* 1050 */     addPartialRequestHeader(buffer);
/*      */ 
/*      */     
/* 1053 */     buffer.put((byte)1);
/*      */ 
/*      */     
/* 1056 */     buffer.putShort((short)1);
/* 1057 */     String category = "Needed items";
/* 1058 */     addStringToBuffer(buffer, "Needed items", false);
/*      */ 
/*      */     
/* 1061 */     buffer.putShort((short)numberOfItemsNeeded.size());
/*      */     
/* 1063 */     for (int i = 0; i < numberOfItemsNeeded.size(); i++) {
/*      */ 
/*      */       
/* 1066 */       String itemName = itemNames.get(i);
/* 1067 */       addStringToBuffer(buffer, itemName, false);
/*      */ 
/*      */       
/* 1070 */       buffer.putShort(((Short)icons.get(i)).shortValue());
/*      */ 
/*      */       
/* 1073 */       short count = ((Integer)numberOfItemsNeeded.get(i)).shortValue();
/* 1074 */       buffer.putShort(count);
/*      */ 
/*      */       
/* 1077 */       buffer.putShort((short)0);
/*      */     } 
/* 1079 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final boolean fillRequirmentsLists(AdvancedCreationEntry entry, Item source, List<String> itemNames, List<Integer> numberOfItemsNeeded, List<Short> icons) {
/* 1087 */     CreationRequirement[] requirements = entry.getRequirements();
/*      */     
/* 1089 */     if (requirements.length == 0) {
/* 1090 */       return false;
/*      */     }
/* 1092 */     for (CreationRequirement requirement : requirements) {
/*      */ 
/*      */       
/* 1095 */       int remaining = requirement.getResourceNumber() - AdvancedCreationEntry.getStateForRequirement(requirement, source);
/*      */       
/* 1097 */       if (remaining > 0) {
/*      */ 
/*      */         
/* 1100 */         int templateNeeded = requirement.getResourceTemplateId();
/* 1101 */         ItemTemplate needed = getItemTemplate(templateNeeded);
/*      */         
/* 1103 */         if (needed == null) {
/* 1104 */           return false;
/*      */         }
/*      */         
/* 1107 */         itemNames.add(buildTemplateName(needed, null, (byte)0));
/* 1108 */         icons.add(Short.valueOf(needed.getImageNumber()));
/* 1109 */         numberOfItemsNeeded.add(Integer.valueOf(remaining));
/*      */       } 
/*      */     } 
/*      */     
/* 1113 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final AdvancedCreationEntry getAdvancedCreationEntry(int id) {
/*      */     try {
/* 1120 */       return CreationMatrix.getInstance().getAdvancedCreationEntry(id);
/*      */     }
/* 1122 */     catch (NoSuchEntryException nse) {
/*      */       
/* 1124 */       logger.log(Level.WARNING, "No advanced creation entry with id: " + id, (Throwable)nse);
/* 1125 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final boolean addCreationEntriesToPartialList(ByteBuffer buffer, Map<CreationEntry, Integer> entries) {
/* 1132 */     for (CreationEntry entry : entries.keySet()) {
/*      */ 
/*      */       
/* 1135 */       ItemTemplate template = getItemTemplate(entry.getObjectCreated());
/*      */ 
/*      */       
/* 1138 */       if (template == null) {
/* 1139 */         return false;
/*      */       }
/*      */       
/* 1142 */       String entryName = buildTemplateName(template, entry, (byte)0);
/*      */ 
/*      */       
/* 1145 */       addStringToBuffer(buffer, entryName, false);
/*      */ 
/*      */       
/* 1148 */       buffer.putShort(template.getImageNumber());
/*      */ 
/*      */       
/* 1151 */       short chance = ((Integer)entries.get(entry)).shortValue();
/* 1152 */       buffer.putShort(chance);
/*      */ 
/*      */       
/* 1155 */       buffer.putShort((short)(10000 + entry.getObjectCreated()));
/*      */     } 
/* 1157 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final ItemTemplate getItemTemplate(int templateId) {
/*      */     try {
/* 1164 */       return ItemTemplateFactory.getInstance().getTemplate(templateId);
/*      */     }
/* 1166 */     catch (NoSuchTemplateException nst) {
/*      */       
/* 1168 */       logger.log(Level.WARNING, "Unable to find item template with id: " + templateId, (Throwable)nst);
/* 1169 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static final String buildTemplateCaptionName(ItemTemplate toCreate, ItemTemplate source, ItemTemplate target) {
/* 1175 */     String nameFormat = "%s %s";
/* 1176 */     String materialFormat = "%s, %s";
/* 1177 */     String name = toCreate.getName();
/* 1178 */     String sourceMaterial = Item.getMaterialString(source.getMaterial());
/* 1179 */     String targetMaterial = Item.getMaterialString(target.getMaterial());
/* 1180 */     String createMaterial = Item.getMaterialString(toCreate.getMaterial());
/* 1181 */     if (toCreate.sizeString.length() > 0)
/* 1182 */       name = StringUtil.format("%s %s", new Object[] { toCreate.sizeString.trim(), name }); 
/* 1183 */     if (toCreate.isMetal()) {
/*      */       
/* 1185 */       if (!name.equals("lump") && !name.equals("sheet")) {
/*      */         
/* 1187 */         if (!source.isTool() && source.isMetal() && !sourceMaterial.equals("unknown")) {
/* 1188 */           name = StringUtil.format("%s, %s", new Object[] { name, sourceMaterial });
/* 1189 */         } else if (!target.isTool() && target.isMetal() && !targetMaterial.equals("unknown")) {
/* 1190 */           name = StringUtil.format("%s, %s", new Object[] { name, targetMaterial });
/*      */         } 
/* 1192 */       } else if (!createMaterial.equals("unknown")) {
/* 1193 */         name = StringUtil.format("%s %s", new Object[] { createMaterial, name });
/*      */       } 
/* 1195 */     } else if (toCreate.isLiquidCooking()) {
/*      */       
/* 1197 */       if (target.isFood()) {
/* 1198 */         name = StringUtil.format("%s, %s", new Object[] { name, target.getName() });
/*      */       }
/* 1200 */     } else if (toCreate.getTemplateId() == 74) {
/*      */       
/* 1202 */       if (!createMaterial.equals("unknown")) {
/* 1203 */         name = StringUtil.format("%s %s", new Object[] { createMaterial, name });
/*      */       }
/* 1205 */     } else if (toCreate.getTemplateId() == 891) {
/*      */       
/* 1207 */       name = StringUtil.format("%s %s", new Object[] { "wooden", toCreate.getName() });
/*      */     }
/* 1209 */     else if (toCreate.getTemplateId() == 404) {
/*      */       
/* 1211 */       name = StringUtil.format("%s %s", new Object[] { "stone", toCreate.getName() });
/*      */     }
/* 1213 */     else if (toCreate.isStone()) {
/*      */       
/* 1215 */       if (name.equals("shards") && !createMaterial.equals("unknown")) {
/* 1216 */         name = StringUtil.format("%s %s", new Object[] { createMaterial, name });
/* 1217 */       } else if (name.equals("altar")) {
/* 1218 */         name = StringUtil.format("%s %s", new Object[] { "stone", name });
/* 1219 */       } else if (toCreate.getTemplateId() == 593) {
/* 1220 */         name = StringUtil.format("%s %s", new Object[] { "stone", name });
/*      */       } 
/* 1222 */     } else if (toCreate.getTemplateId() == 322) {
/*      */       
/* 1224 */       if (name.equals("altar")) {
/* 1225 */         name = StringUtil.format("%s %s", new Object[] { "wooden", name });
/*      */       }
/* 1227 */     } else if (toCreate.getTemplateId() == 592) {
/* 1228 */       name = StringUtil.format("%s %s", new Object[] { "plank", name });
/*      */     } 
/* 1230 */     return name;
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
/*      */   private static final String buildTemplateName(ItemTemplate template, @Nullable CreationEntry entry, byte materialOverride) {
/* 1242 */     String nameFormat = "%s %s";
/* 1243 */     String materialFormat = "%s, %s";
/* 1244 */     String name = template.getName();
/* 1245 */     String material = Item.getMaterialString(template.getMaterial());
/* 1246 */     if (template.sizeString.length() > 0)
/* 1247 */       name = StringUtil.format("%s %s", new Object[] { template.sizeString.trim(), name }); 
/* 1248 */     if (template.isMetal() && (name.equals("lump") || name.equals("sheet"))) {
/* 1249 */       name = StringUtil.format("%s %s", new Object[] { material, name });
/* 1250 */     } else if (materialOverride != 0) {
/*      */       
/* 1252 */       name = StringUtil.format("%s, %s", new Object[] { name, Materials.convertMaterialByteIntoString(materialOverride) });
/*      */     }
/* 1254 */     else if (name.equals("barding")) {
/*      */       
/* 1256 */       if (template.isCloth()) {
/* 1257 */         name = StringUtil.format("%s %s", new Object[] { "cloth", name });
/* 1258 */       } else if (template.isMetal()) {
/* 1259 */         name = StringUtil.format("%s %s", new Object[] { "chain", name });
/*      */       } else {
/* 1261 */         name = StringUtil.format("%s %s", new Object[] { material, name });
/*      */       } 
/* 1263 */     } else if (name.equals("rock")) {
/*      */       
/* 1265 */       name = StringUtil.format("%s, %s", new Object[] { name, "iron" });
/*      */     }
/* 1267 */     else if (template.getTemplateId() == 216 || template.getTemplateId() == 215) {
/* 1268 */       name = StringUtil.format("%s, %s", new Object[] { name, material });
/* 1269 */     } else if (template.isStone()) {
/*      */       
/* 1271 */       if (name.equals("shards") && !material.equals("unknown")) {
/* 1272 */         name = StringUtil.format("%s, %s", new Object[] { name, material });
/*      */       }
/* 1274 */     } else if (name.equals("fur")) {
/*      */       
/* 1276 */       if (entry != null)
/*      */       {
/* 1278 */         if (entry.getObjectCreated() == 846)
/*      */         {
/* 1280 */           name = "black bear fur";
/*      */         }
/* 1282 */         else if (entry.getObjectCreated() == 847)
/*      */         {
/* 1284 */           name = "brown bear fur";
/*      */         }
/* 1286 */         else if (entry.getObjectCreated() == 849)
/*      */         {
/* 1288 */           name = "black wolf fur";
/*      */         }
/*      */       
/*      */       }
/* 1292 */     } else if (name.equals("pelt")) {
/*      */       
/* 1294 */       if (entry != null)
/*      */       {
/* 1296 */         if (entry.getObjectCreated() == 848)
/*      */         {
/* 1298 */           name = "mountain lion pelt";
/*      */         }
/*      */       }
/*      */     } 
/*      */     
/* 1303 */     return name;
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
/*      */   private static void addStringToBuffer(ByteBuffer buffer, String string, boolean shortLength) {
/* 1316 */     byte[] bytes = getEncodedBytesFromString(string);
/*      */     
/* 1318 */     if (!shortLength) {
/* 1319 */       buffer.put((byte)bytes.length);
/*      */     } else {
/* 1321 */       buffer.putShort((short)bytes.length);
/* 1322 */     }  buffer.put(bytes);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final byte[] getEncodedBytesFromString(String string) {
/*      */     try {
/* 1329 */       return string.getBytes("UTF-8");
/*      */     }
/* 1331 */     catch (UnsupportedEncodingException e) {
/*      */ 
/*      */       
/* 1334 */       logger.log(Level.WARNING, e.getMessage(), e);
/* 1335 */       return new byte[0];
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
/*      */   private static void addPartialRequestHeader(ByteBuffer buffer) {
/* 1347 */     buffer.put((byte)-46);
/* 1348 */     buffer.put((byte)0);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean sendAllCraftingRecipes(SocketConnection connection, @Nonnull Player player) {
/* 1353 */     RecipesListParameter params = new RecipesListParameter();
/* 1354 */     short numberOfEntries = buildCreationsList(params);
/*      */     
/* 1356 */     if (!sendCreationListCategories(connection, params, numberOfEntries)) {
/*      */ 
/*      */       
/* 1359 */       player.setLink(false);
/* 1360 */       return false;
/*      */     } 
/*      */     
/* 1363 */     if (!sendCreationRecipes(connection, player, params))
/*      */     {
/* 1365 */       return false;
/*      */     }
/*      */     
/* 1368 */     if (!sendFenceRecipes(connection, player, params))
/*      */     {
/* 1370 */       return false;
/*      */     }
/*      */     
/* 1373 */     if (!sendHedgeRecipes(connection, player, params))
/*      */     {
/* 1375 */       return false;
/*      */     }
/*      */     
/* 1378 */     if (!sendFlowerbedRecipes(connection, player, params))
/*      */     {
/* 1380 */       return false;
/*      */     }
/*      */     
/* 1383 */     if (!sendWallRecipes(connection, player, params))
/*      */     {
/* 1385 */       return false;
/*      */     }
/*      */     
/* 1388 */     if (!sendRoofFloorRecipes(connection, player, params))
/*      */     {
/* 1390 */       return false;
/*      */     }
/*      */     
/* 1393 */     if (!sendBridgePartRecipes(connection, player, params))
/*      */     {
/* 1395 */       return false;
/*      */     }
/*      */     
/* 1398 */     if (!sendCaveWallRecipes(connection, player, params)) {
/* 1399 */       return false;
/*      */     }
/* 1401 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final boolean sendRoofFloorRecipes(SocketConnection connection, @Nonnull Player player, RecipesListParameter params) {
/* 1408 */     for (String category : params.getRoofs_floors().keySet()) {
/*      */ 
/*      */       
/* 1411 */       List<RoofFloorEnum> entries = params.getRoofs_floors().get(category);
/* 1412 */       for (RoofFloorEnum entry : entries) {
/*      */         
/* 1414 */         int[] tools = RoofFloorEnum.getValidToolsForMaterial(entry.getMaterial());
/* 1415 */         for (int tool : tools) {
/*      */ 
/*      */           
/* 1418 */           ByteBuffer buffer = connection.getBuffer();
/*      */ 
/*      */           
/* 1421 */           addCreationRecipesMessageHeaders(buffer);
/*      */ 
/*      */           
/* 1424 */           addCategoryIdToBuffer(params, category, buffer);
/*      */ 
/*      */           
/* 1427 */           addRoofFloorRecipeInfoToBuffer(entry, buffer);
/*      */ 
/*      */           
/* 1430 */           ItemTemplate toolTemplate = getItemTemplate(tool);
/*      */           
/* 1432 */           if (toolTemplate == null) {
/*      */             
/* 1434 */             logger.log(Level.WARNING, "sendRoofFlorRecipes() No item template found with id: " + tool);
/* 1435 */             connection.clearBuffer();
/* 1436 */             return false;
/*      */           } 
/*      */ 
/*      */           
/* 1440 */           addRoofFloorToolInfoToBuffer(buffer, toolTemplate);
/*      */ 
/*      */           
/* 1443 */           addWallPlanInfoToBuffer(buffer, entry);
/*      */           
/* 1445 */           if (!addAdditionalMaterialsForRoofsFloors(buffer, entry)) {
/*      */             
/* 1447 */             connection.clearBuffer();
/* 1448 */             return false;
/*      */           } 
/*      */ 
/*      */           
/*      */           try {
/* 1453 */             connection.flush();
/*      */           }
/* 1455 */           catch (IOException ex) {
/*      */             
/* 1457 */             logger.log(Level.WARNING, "Failed to flush floor|roof recipes!", ex);
/* 1458 */             player.setLink(false);
/* 1459 */             return false;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1465 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final boolean sendBridgePartRecipes(SocketConnection connection, @Nonnull Player player, RecipesListParameter params) {
/* 1472 */     for (String category : params.getBridgeParts().keySet()) {
/*      */ 
/*      */       
/* 1475 */       List<BridgePartEnum> entries = params.getBridgeParts().get(category);
/* 1476 */       for (BridgePartEnum entry : entries) {
/*      */         
/* 1478 */         int[] tools = BridgePartEnum.getValidToolsForMaterial(entry.getMaterial());
/* 1479 */         for (int tool : tools) {
/*      */ 
/*      */           
/* 1482 */           ByteBuffer buffer = connection.getBuffer();
/*      */ 
/*      */           
/* 1485 */           addCreationRecipesMessageHeaders(buffer);
/*      */ 
/*      */           
/* 1488 */           addCategoryIdToBuffer(params, category, buffer);
/*      */ 
/*      */           
/* 1491 */           addBridgePartRecipeInfoToBuffer(entry, buffer);
/*      */ 
/*      */           
/* 1494 */           ItemTemplate toolTemplate = getItemTemplate(tool);
/*      */           
/* 1496 */           if (toolTemplate == null) {
/*      */             
/* 1498 */             logger.log(Level.WARNING, "sendRoofFlorRecipes() No item template found with id: " + tool);
/* 1499 */             connection.clearBuffer();
/* 1500 */             return false;
/*      */           } 
/*      */ 
/*      */           
/* 1504 */           addRoofFloorToolInfoToBuffer(buffer, toolTemplate);
/*      */ 
/*      */           
/* 1507 */           buffer.putShort((short)60);
/* 1508 */           addStringToBuffer(buffer, entry.getName() + " plan", true);
/*      */           
/* 1510 */           if (!addTotalMaterialsForBridgeParts(buffer, entry)) {
/*      */             
/* 1512 */             connection.clearBuffer();
/* 1513 */             return false;
/*      */           } 
/*      */ 
/*      */           
/*      */           try {
/* 1518 */             connection.flush();
/*      */           }
/* 1520 */           catch (IOException ex) {
/*      */             
/* 1522 */             logger.log(Level.WARNING, "Failed to flush bridge part recipes!", ex);
/* 1523 */             player.setLink(false);
/* 1524 */             return false;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1530 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final boolean addAdditionalMaterialsForRoofsFloors(ByteBuffer buffer, RoofFloorEnum entry) {
/* 1536 */     List<BuildMaterial> list = entry.getTotalMaterialsNeeded();
/* 1537 */     buffer.putShort((short)list.size());
/* 1538 */     for (BuildMaterial bMat : list) {
/*      */       
/* 1540 */       ItemTemplate mat = getItemTemplate(bMat.getTemplateId());
/* 1541 */       if (mat == null) {
/*      */         
/* 1543 */         logger.log(Level.WARNING, "Unable to find item template with id: " + bMat.getTemplateId());
/* 1544 */         return false;
/*      */       } 
/*      */       
/* 1547 */       buffer.putShort(mat.getImageNumber());
/*      */       
/* 1549 */       addStringToBuffer(buffer, buildTemplateName(mat, null, (byte)0), true);
/*      */       
/* 1551 */       buffer.putShort((short)bMat.getNeededQuantity());
/*      */     } 
/*      */     
/* 1554 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final boolean addTotalMaterialsForBridgeParts(ByteBuffer buffer, BridgePartEnum entry) {
/* 1560 */     List<BuildMaterial> list = entry.getTotalMaterialsNeeded();
/* 1561 */     buffer.putShort((short)list.size());
/* 1562 */     for (BuildMaterial bMat : list) {
/*      */       
/* 1564 */       ItemTemplate mat = getItemTemplate(bMat.getTemplateId());
/* 1565 */       if (mat == null) {
/*      */         
/* 1567 */         logger.log(Level.WARNING, "Unable to find item template with id: " + bMat.getTemplateId());
/* 1568 */         return false;
/*      */       } 
/*      */       
/* 1571 */       buffer.putShort(mat.getImageNumber());
/* 1572 */       addStringToBuffer(buffer, buildTemplateName(mat, null, (byte)0), true);
/* 1573 */       buffer.putShort((short)bMat.getNeededQuantity());
/*      */     } 
/*      */     
/* 1576 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void addRoofFloorToolInfoToBuffer(ByteBuffer buffer, ItemTemplate toolTemplate) {
/* 1586 */     buffer.putShort(toolTemplate.getImageNumber());
/*      */ 
/*      */     
/* 1589 */     addStringToBuffer(buffer, toolTemplate.getName(), true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void addRoofFloorRecipeInfoToBuffer(RoofFloorEnum entry, ByteBuffer buffer) {
/* 1599 */     buffer.putShort((short)60);
/*      */ 
/*      */     
/* 1602 */     addStringToBuffer(buffer, entry.getName(), true);
/*      */ 
/*      */     
/* 1605 */     addStringToBuffer(buffer, SkillSystem.getNameFor(entry.getNeededSkillNumber()), true);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static void addBridgePartRecipeInfoToBuffer(BridgePartEnum entry, ByteBuffer buffer) {
/* 1611 */     buffer.putShort((short)60);
/*      */ 
/*      */     
/* 1614 */     addStringToBuffer(buffer, entry.getName(), true);
/*      */ 
/*      */     
/* 1617 */     addStringToBuffer(buffer, SkillSystem.getNameFor(entry.getNeededSkillNumber()), true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final boolean sendWallRecipes(SocketConnection connection, @Nonnull Player player, RecipesListParameter params) {
/* 1624 */     for (String category : params.getWalls().keySet()) {
/*      */ 
/*      */       
/* 1627 */       List<WallEnum> entries = params.getWalls().get(category);
/*      */ 
/*      */       
/* 1630 */       for (WallEnum entry : entries) {
/*      */ 
/*      */         
/* 1633 */         List<Integer> tools = WallEnum.getToolsForWall(entry, null);
/*      */ 
/*      */         
/* 1636 */         for (Integer tool : tools) {
/*      */ 
/*      */           
/* 1639 */           ByteBuffer buffer = connection.getBuffer();
/*      */ 
/*      */           
/* 1642 */           addCreationRecipesMessageHeaders(buffer);
/*      */ 
/*      */           
/* 1645 */           addCategoryIdToBuffer(params, category, buffer);
/*      */ 
/*      */           
/* 1648 */           addWallInfoToBuffer(entry, buffer);
/*      */ 
/*      */           
/* 1651 */           ItemTemplate toolTemplate = getItemTemplate(tool.intValue());
/*      */           
/* 1653 */           if (toolTemplate == null) {
/*      */             
/* 1655 */             connection.clearBuffer();
/* 1656 */             logger.log(Level.WARNING, "Unable to find tool with id: " + tool.intValue());
/* 1657 */             return false;
/*      */           } 
/*      */ 
/*      */           
/* 1661 */           addWallToolIInfoToBuffer(buffer, toolTemplate);
/*      */ 
/*      */           
/* 1664 */           addWallPlanInfoToBuffer(buffer);
/*      */ 
/*      */           
/* 1667 */           if (!addAdditionalMaterialsForWall(buffer, entry)) {
/*      */             
/* 1669 */             connection.clearBuffer();
/* 1670 */             return false;
/*      */           } 
/*      */ 
/*      */           
/*      */           try {
/* 1675 */             connection.flush();
/*      */           }
/* 1677 */           catch (IOException iex) {
/*      */             
/* 1679 */             logger.log(Level.WARNING, "Failed to flush well recipe", iex);
/* 1680 */             player.setLink(false);
/* 1681 */             return false;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1687 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final boolean addAdditionalMaterialsForWall(ByteBuffer buffer, WallEnum entry) {
/* 1693 */     int[] needed = entry.getTotalMaterialsNeeded();
/*      */     
/* 1695 */     buffer.putShort((short)(needed.length / 2));
/* 1696 */     for (int i = 0; i < needed.length; i += 2) {
/*      */       
/* 1698 */       ItemTemplate mat = getItemTemplate(needed[i]);
/* 1699 */       if (mat == null)
/*      */       {
/* 1701 */         return false;
/*      */       }
/*      */       
/* 1704 */       buffer.putShort(mat.getImageNumber());
/* 1705 */       addStringToBuffer(buffer, buildTemplateName(mat, null, (byte)0), true);
/*      */       
/* 1707 */       buffer.putShort((short)needed[i + 1]);
/*      */     } 
/*      */     
/* 1710 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void addWallPlanInfoToBuffer(ByteBuffer buffer) {
/* 1719 */     buffer.putShort((short)60);
/*      */ 
/*      */     
/* 1722 */     addStringToBuffer(buffer, WallEnum.WALL_PLAN.getName(), true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void addWallPlanInfoToBuffer(ByteBuffer buffer, RoofFloorEnum entry) {
/* 1731 */     buffer.putShort((short)60);
/*      */     
/* 1733 */     String planString = entry.isFloor() ? "planned floor" : "planned roof";
/*      */ 
/*      */     
/* 1736 */     addStringToBuffer(buffer, planString, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void addWallToolIInfoToBuffer(ByteBuffer buffer, ItemTemplate toolTemplate) {
/* 1747 */     buffer.putShort(toolTemplate.getImageNumber());
/*      */ 
/*      */     
/* 1750 */     addStringToBuffer(buffer, toolTemplate.getName(), true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void addWallInfoToBuffer(WallEnum entry, ByteBuffer buffer) {
/* 1760 */     buffer.putShort((short)60);
/*      */ 
/*      */     
/* 1763 */     addStringToBuffer(buffer, entry.getName(), true);
/*      */ 
/*      */     
/* 1766 */     addStringToBuffer(buffer, 
/* 1767 */         SkillSystem.getNameFor(WallEnum.getSkillNumber(entry.getMaterial())), true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final boolean sendFlowerbedRecipes(SocketConnection connection, @Nonnull Player player, RecipesListParameter params) {
/* 1774 */     for (String category : params.getFlowerbeds().keySet()) {
/*      */ 
/*      */       
/* 1777 */       List<Short> entries = params.getFlowerbeds().get(category);
/*      */ 
/*      */       
/* 1780 */       for (Iterator<Short> iterator = entries.iterator(); iterator.hasNext(); ) { short entry = ((Short)iterator.next()).shortValue();
/*      */         
/* 1782 */         short bedType = entry;
/* 1783 */         String name = WallConstants.getName(StructureConstantsEnum.getEnumByValue(bedType));
/* 1784 */         int flowerType = Fence.getFlowerTypeByFlowerbedType(StructureConstantsEnum.getEnumByValue(bedType));
/*      */ 
/*      */         
/* 1787 */         ByteBuffer buffer = connection.getBuffer();
/*      */ 
/*      */         
/* 1790 */         addCreationRecipesMessageHeaders(buffer);
/*      */ 
/*      */         
/* 1793 */         addCategoryIdToBuffer(params, category, buffer);
/*      */ 
/*      */         
/* 1796 */         addFlowerbedInfoToBuffer(name, buffer);
/*      */ 
/*      */         
/* 1799 */         ItemTemplate flower = getItemTemplate(flowerType);
/*      */ 
/*      */         
/* 1802 */         if (flower == null) {
/*      */           
/* 1804 */           connection.clearBuffer();
/* 1805 */           return false;
/*      */         } 
/*      */         
/* 1808 */         addWallToolIInfoToBuffer(buffer, flower);
/*      */         
/* 1810 */         addTileBorderToBuffer(buffer);
/*      */         
/* 1812 */         if (!addAdditionalMaterialsForFlowerbed(buffer, flower)) {
/*      */           
/* 1814 */           connection.clearBuffer();
/* 1815 */           return false;
/*      */         } 
/*      */ 
/*      */         
/*      */         try {
/* 1820 */           connection.flush();
/*      */         }
/* 1822 */         catch (IOException ex) {
/*      */           
/* 1824 */           logger.log(Level.WARNING, "IO Exception when sending flowerbed recipes.", ex);
/* 1825 */           player.setLink(false);
/* 1826 */           return false;
/*      */         }  }
/*      */     
/*      */     } 
/*      */     
/* 1831 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final boolean addAdditionalMaterialsForFlowerbed(ByteBuffer buffer, ItemTemplate flower) {
/* 1837 */     int[] needed = { flower.getTemplateId(), 4, 22, 3, 218, 1, 26, 1 };
/*      */     
/* 1839 */     buffer.putShort((short)(needed.length / 2));
/* 1840 */     for (int i = 0; i < needed.length; i += 2) {
/*      */       
/* 1842 */       ItemTemplate mat = null;
/* 1843 */       if (needed[i] == flower.getTemplateId()) {
/*      */         
/* 1845 */         mat = flower;
/*      */       }
/*      */       else {
/*      */         
/* 1849 */         mat = getItemTemplate(needed[i]);
/* 1850 */         if (mat == null)
/*      */         {
/* 1852 */           return false;
/*      */         }
/*      */       } 
/*      */       
/* 1856 */       buffer.putShort((short)60);
/* 1857 */       addStringToBuffer(buffer, buildTemplateName(mat, null, (byte)0), true);
/* 1858 */       buffer.putShort((short)needed[i + 1]);
/*      */     } 
/*      */     
/* 1861 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void addFlowerbedInfoToBuffer(String name, ByteBuffer buffer) {
/* 1871 */     buffer.putShort((short)60);
/*      */ 
/*      */     
/* 1874 */     addStringToBuffer(buffer, name, true);
/*      */ 
/*      */     
/* 1877 */     addStringToBuffer(buffer, SkillSystem.getNameFor(10045), true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final boolean sendHedgeRecipes(SocketConnection connection, @Nonnull Player player, RecipesListParameter params) {
/* 1884 */     for (String category : params.getHedges().keySet()) {
/*      */ 
/*      */       
/* 1887 */       List<Short> entries = params.getHedges().get(category);
/*      */ 
/*      */       
/* 1890 */       for (Iterator<Short> iterator = entries.iterator(); iterator.hasNext(); ) { short entry = ((Short)iterator.next()).shortValue();
/*      */         
/* 1892 */         short hedgeType = entry;
/*      */ 
/*      */         
/* 1895 */         ByteBuffer buffer = connection.getBuffer();
/*      */ 
/*      */         
/* 1898 */         addCreationRecipesMessageHeaders(buffer);
/*      */ 
/*      */         
/* 1901 */         addCategoryIdToBuffer(params, category, buffer);
/*      */ 
/*      */         
/* 1904 */         addHedgeInfoToBuffer(StructureConstantsEnum.getEnumByValue(hedgeType), buffer);
/*      */         
/* 1906 */         ItemTemplate sprout = getItemTemplate(266);
/* 1907 */         if (sprout == null) {
/*      */           
/* 1909 */           connection.clearBuffer();
/* 1910 */           return false;
/*      */         } 
/*      */         
/* 1913 */         byte materialType = Fence.getMaterialForLowHedge(StructureConstantsEnum.getEnumByValue(hedgeType));
/* 1914 */         String materialString = Item.getMaterialString(materialType);
/*      */ 
/*      */         
/* 1917 */         addSproutInfoToBuffer(sprout, materialString, buffer);
/*      */ 
/*      */         
/* 1920 */         addTileBorderToBuffer(buffer);
/*      */         
/* 1922 */         addAdditionalMaterialsForHedge(buffer, sprout, materialString);
/*      */ 
/*      */         
/*      */         try {
/* 1926 */           connection.flush();
/*      */         }
/* 1928 */         catch (IOException ex) {
/*      */           
/* 1930 */           logger.log(Level.WARNING, "IO Exception when sending hedge recipes.", ex);
/* 1931 */           player.setLink(false);
/* 1932 */           return false;
/*      */         }  }
/*      */     
/*      */     } 
/*      */     
/* 1937 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final void addAdditionalMaterialsForHedge(ByteBuffer buffer, ItemTemplate template, String material) {
/* 1943 */     buffer.putShort((short)1);
/*      */     
/* 1945 */     buffer.putShort(template.getImageNumber());
/* 1946 */     addStringToBuffer(buffer, StringUtil.format("%s, %s", new Object[] { template.getName(), material }), true);
/* 1947 */     buffer.putShort((short)4);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void addSproutInfoToBuffer(ItemTemplate sprout, String material, ByteBuffer buffer) {
/* 1957 */     buffer.putShort(sprout.getImageNumber());
/*      */     
/* 1959 */     String sproutName = StringUtil.format("%s, %s", new Object[] { sprout.getName(), material });
/*      */ 
/*      */     
/* 1962 */     addStringToBuffer(buffer, sproutName, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void addHedgeInfoToBuffer(StructureConstantsEnum hedgeType, ByteBuffer buffer) {
/* 1972 */     buffer.putShort((short)60);
/*      */ 
/*      */     
/* 1975 */     addStringToBuffer(buffer, WallConstants.getName(hedgeType), true);
/*      */ 
/*      */     
/* 1978 */     addStringToBuffer(buffer, SkillSystem.getNameFor(10045), true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final boolean sendFenceRecipes(SocketConnection connection, @Nonnull Player player, RecipesListParameter params) {
/* 1985 */     for (String category : params.getFences().keySet()) {
/*      */ 
/*      */       
/* 1988 */       List<ActionEntry> entries = params.getFences().get(category);
/*      */ 
/*      */       
/* 1991 */       for (ActionEntry entry : entries) {
/*      */ 
/*      */         
/* 1994 */         StructureConstantsEnum originalFenceType = Fence.getFencePlanType(entry.getNumber());
/*      */         
/* 1996 */         StructureConstantsEnum fenceType = Fence.getFenceForPlan(originalFenceType);
/*      */ 
/*      */         
/* 1999 */         int[] correctTools = MethodsStructure.getCorrectToolsForBuildingFences();
/* 2000 */         for (int i = 0; i < correctTools.length; i++) {
/*      */ 
/*      */ 
/*      */           
/* 2004 */           ByteBuffer buffer = connection.getBuffer();
/*      */ 
/*      */           
/* 2007 */           addCreationRecipesMessageHeaders(buffer);
/*      */ 
/*      */           
/* 2010 */           addCategoryIdToBuffer(params, category, buffer);
/*      */ 
/*      */           
/* 2013 */           addCreatedFenceToBuffer(originalFenceType, fenceType, buffer);
/*      */ 
/*      */           
/* 2016 */           if (!addFenceToolToBuffer(buffer, correctTools[i])) {
/*      */             
/* 2018 */             connection.clearBuffer();
/* 2019 */             return false;
/*      */           } 
/*      */ 
/*      */           
/* 2023 */           addTileBorderToBuffer(buffer);
/*      */           
/* 2025 */           if (!addAdditionalMaterialsForFence(buffer, originalFenceType)) {
/*      */             
/* 2027 */             connection.clearBuffer();
/* 2028 */             return false;
/*      */           } 
/*      */ 
/*      */           
/*      */           try {
/* 2033 */             connection.flush();
/*      */           }
/* 2035 */           catch (IOException ex) {
/*      */             
/* 2037 */             logger.log(Level.WARNING, "IO Exception when sending fence recipes.", ex);
/* 2038 */             player.setLink(false);
/* 2039 */             return false;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 2045 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final boolean addAdditionalMaterialsForFence(ByteBuffer buffer, StructureConstantsEnum fence) {
/* 2050 */     int[] items = Fence.getItemTemplatesNeededForFenceTotal(fence);
/* 2051 */     if (items.length < 2) {
/* 2052 */       buffer.putShort((short)0);
/*      */     } else {
/*      */       
/* 2055 */       buffer.putShort((short)(items.length / 2));
/* 2056 */       for (int i = 0; i < items.length; i += 2) {
/*      */         
/* 2058 */         ItemTemplate mat = getItemTemplate(items[i]);
/* 2059 */         if (mat == null)
/*      */         {
/* 2061 */           return false;
/*      */         }
/*      */ 
/*      */         
/* 2065 */         buffer.putShort(mat.getImageNumber());
/*      */ 
/*      */         
/* 2068 */         addStringToBuffer(buffer, buildTemplateName(mat, null, (byte)0), true);
/*      */ 
/*      */         
/* 2071 */         buffer.putShort((short)items[i + 1]);
/*      */       } 
/*      */     } 
/*      */     
/* 2075 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void addTileBorderToBuffer(ByteBuffer buffer) {
/* 2084 */     buffer.putShort((short)60);
/* 2085 */     addStringToBuffer(buffer, "Tile Border", true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean addFenceToolToBuffer(ByteBuffer buffer, int toolId) {
/* 2095 */     ItemTemplate toolTemplate = getItemTemplate(toolId);
/*      */ 
/*      */     
/* 2098 */     if (toolTemplate == null) {
/*      */       
/* 2100 */       logger.log(Level.WARNING, "Unable to find tool template with id: " + toolId);
/* 2101 */       return false;
/*      */     } 
/*      */ 
/*      */     
/* 2105 */     buffer.putShort(toolTemplate.imageNumber);
/*      */ 
/*      */     
/* 2108 */     addStringToBuffer(buffer, toolTemplate.getName(), true);
/*      */     
/* 2110 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void addCreatedFenceToBuffer(StructureConstantsEnum originalFenceType, StructureConstantsEnum fenceType, ByteBuffer buffer) {
/* 2121 */     buffer.putShort((short)60);
/*      */ 
/*      */     
/* 2124 */     String fenceName = WallConstants.getName(fenceType);
/* 2125 */     addStringToBuffer(buffer, fenceName, true);
/*      */ 
/*      */     
/* 2128 */     int skillNumber = Fence.getSkillNumberNeededForFence(originalFenceType);
/* 2129 */     addStringToBuffer(buffer, SkillSystem.getNameFor(skillNumber), true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void addReinforcedWallToBuffer(ByteBuffer buffer) {
/* 2138 */     buffer.putShort((short)60);
/* 2139 */     addStringToBuffer(buffer, Tiles.Tile.TILE_CAVE_WALL_REINFORCED.getName(), true);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static void addCreatedReinforcedWallToBuffer(byte partCladType, byte cladType, ByteBuffer buffer) {
/* 2145 */     buffer.putShort((short)60);
/*      */ 
/*      */     
/* 2148 */     String fenceName = Tiles.getTile(cladType).getName();
/* 2149 */     addStringToBuffer(buffer, fenceName, true);
/*      */ 
/*      */     
/* 2152 */     int skillNumber = CaveWallBehaviour.getSkillNumberNeededForCladding((short)partCladType);
/* 2153 */     addStringToBuffer(buffer, SkillSystem.getNameFor(skillNumber), true);
/*      */   }
/*      */ 
/*      */   
/*      */   private static final boolean addAdditionalMaterialsForReinforcedWall(ByteBuffer buffer, short action) {
/* 2158 */     int[] items = CaveWallBehaviour.getMaterialsNeededTotal(action);
/* 2159 */     if (items.length < 2) {
/* 2160 */       buffer.putShort((short)0);
/*      */     } else {
/*      */       
/* 2163 */       buffer.putShort((short)(items.length / 2));
/* 2164 */       for (int i = 0; i < items.length; i += 2) {
/*      */         
/* 2166 */         ItemTemplate mat = getItemTemplate(items[i]);
/* 2167 */         if (mat == null)
/*      */         {
/* 2169 */           return false;
/*      */         }
/*      */ 
/*      */         
/* 2173 */         buffer.putShort(mat.getImageNumber());
/*      */ 
/*      */         
/* 2176 */         addStringToBuffer(buffer, buildTemplateName(mat, null, (byte)0), true);
/*      */ 
/*      */         
/* 2179 */         buffer.putShort((short)items[i + 1]);
/*      */       } 
/*      */     } 
/* 2182 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final boolean sendCreationRecipes(SocketConnection connection, @Nonnull Player player, RecipesListParameter params) {
/* 2189 */     for (String category : params.getCreationEntries().keySet()) {
/*      */ 
/*      */       
/* 2192 */       List<CreationEntry> entries = params.getCreationEntries().get(category);
/*      */ 
/*      */       
/* 2195 */       for (CreationEntry entry : entries) {
/*      */ 
/*      */         
/* 2198 */         ByteBuffer buffer = connection.getBuffer();
/*      */ 
/*      */         
/* 2201 */         addCreationRecipesMessageHeaders(buffer);
/*      */ 
/*      */         
/* 2204 */         addCategoryIdToBuffer(params, category, buffer);
/*      */ 
/*      */         
/* 2207 */         ItemTemplate created = getItemTemplate(entry.getObjectCreated());
/* 2208 */         ItemTemplate source = getItemTemplate(entry.getObjectSource());
/* 2209 */         ItemTemplate target = getItemTemplate(entry.getObjectTarget());
/*      */ 
/*      */         
/* 2212 */         if (created == null || source == null || target == null) {
/*      */           
/* 2214 */           connection.clearBuffer();
/* 2215 */           return false;
/*      */         } 
/*      */ 
/*      */         
/* 2219 */         addItemCreatedToRecipesBuffer(entry, buffer, created, source, target);
/*      */ 
/*      */         
/* 2222 */         addInitialItemUsedToRecipesBuffer(entry, buffer, source, entry.getObjectSourceMaterial());
/*      */ 
/*      */         
/* 2225 */         addInitialItemUsedToRecipesBuffer(entry, buffer, target, entry.getObjectTargetMaterial());
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2230 */         if (!addAditionalMaterialsForAdvancedEntries(buffer, entry)) {
/*      */           
/* 2232 */           connection.clearBuffer();
/* 2233 */           return false;
/*      */         } 
/*      */ 
/*      */         
/*      */         try {
/* 2238 */           connection.flush();
/*      */         }
/* 2240 */         catch (IOException iex) {
/*      */           
/* 2242 */           logger.log(Level.WARNING, "Failed to send creation entries to recipes list", iex);
/* 2243 */           player.setLink(false);
/* 2244 */           return false;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 2249 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final boolean addAditionalMaterialsForAdvancedEntries(ByteBuffer buffer, CreationEntry entry) {
/* 2255 */     if (entry instanceof AdvancedCreationEntry) {
/*      */       
/* 2257 */       AdvancedCreationEntry adv = (AdvancedCreationEntry)entry;
/*      */ 
/*      */       
/* 2260 */       CreationRequirement[] reqs = adv.getRequirements();
/*      */ 
/*      */       
/* 2263 */       buffer.putShort((short)reqs.length);
/*      */ 
/*      */       
/* 2266 */       for (CreationRequirement req : reqs)
/*      */       {
/* 2268 */         int id = req.getResourceTemplateId();
/* 2269 */         ItemTemplate mat = getItemTemplate(id);
/* 2270 */         if (mat == null)
/*      */         {
/* 2272 */           return false;
/*      */         }
/*      */ 
/*      */         
/* 2276 */         buffer.putShort(mat.getImageNumber());
/*      */ 
/*      */         
/* 2279 */         addStringToBuffer(buffer, buildTemplateName(mat, null, (byte)0), true);
/*      */ 
/*      */         
/* 2282 */         buffer.putShort((short)req.getResourceNumber());
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 2288 */       buffer.putShort((short)0);
/*      */     } 
/*      */     
/* 2291 */     return true;
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
/*      */   private static void addInitialItemUsedToRecipesBuffer(CreationEntry entry, ByteBuffer buffer, ItemTemplate item, byte materialOverride) {
/* 2303 */     buffer.putShort(item.getImageNumber());
/*      */ 
/*      */     
/* 2306 */     addStringToBuffer(buffer, buildTemplateName(item, entry, materialOverride), true);
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
/*      */   private static void addItemCreatedToRecipesBuffer(CreationEntry entry, ByteBuffer buffer, ItemTemplate created, ItemTemplate source, ItemTemplate target) {
/* 2320 */     buffer.putShort(created.getImageNumber());
/*      */ 
/*      */     
/* 2323 */     addStringToBuffer(buffer, buildTemplateCaptionName(created, source, target), true);
/*      */ 
/*      */     
/* 2326 */     String skillName = SkillSystem.getNameFor(entry.getPrimarySkill());
/* 2327 */     addStringToBuffer(buffer, skillName, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void addCategoryIdToBuffer(RecipesListParameter params, String category, ByteBuffer buffer) {
/* 2337 */     buffer.putShort(((Integer)params.getCategoryIds().get(category)).shortValue());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void addCreationRecipesMessageHeaders(ByteBuffer buffer) {
/* 2345 */     buffer.put((byte)-46);
/* 2346 */     buffer.put((byte)3);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final boolean sendCreationListCategories(SocketConnection connection, RecipesListParameter params, short numberOfEntries) {
/* 2354 */     ByteBuffer buffer = connection.getBuffer();
/*      */ 
/*      */     
/* 2357 */     addRecipesCategoryListMessageHeadersToBuffer(buffer);
/*      */ 
/*      */     
/* 2360 */     buffer.putShort((short)params.getTotalCategories());
/*      */ 
/*      */     
/* 2363 */     addCategoryToBuffer(buffer, params.getCreationEntries().keySet(), params.getCategoryIds());
/*      */ 
/*      */     
/* 2366 */     addCategoryToBuffer(buffer, params.getFences().keySet(), params.getCategoryIds());
/*      */ 
/*      */     
/* 2369 */     addCategoryToBuffer(buffer, params.getHedges().keySet(), params.getCategoryIds());
/*      */ 
/*      */     
/* 2372 */     addCategoryToBuffer(buffer, params.getFlowerbeds().keySet(), params.getCategoryIds());
/*      */ 
/*      */     
/* 2375 */     addCategoryToBuffer(buffer, params.getWalls().keySet(), params.getCategoryIds());
/*      */ 
/*      */     
/* 2378 */     addCategoryToBuffer(buffer, params.getRoofs_floors().keySet(), params.getCategoryIds());
/*      */ 
/*      */     
/* 2381 */     addCategoryToBuffer(buffer, params.getBridgeParts().keySet(), params.getCategoryIds());
/*      */ 
/*      */     
/* 2384 */     addCategoryToBuffer(buffer, params.getCaveWalls().keySet(), params.getCategoryIds());
/*      */ 
/*      */     
/* 2387 */     buffer.putShort(numberOfEntries);
/*      */ 
/*      */     
/*      */     try {
/* 2391 */       connection.flush();
/* 2392 */       return true;
/*      */     }
/* 2394 */     catch (IOException iex) {
/*      */       
/* 2396 */       logger.log(Level.WARNING, "An error occured while flushing the categories for the recipes list.", iex);
/* 2397 */       connection.clearBuffer();
/* 2398 */       return false;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void addCategoryToBuffer(ByteBuffer buffer, Set<String> categories, Map<String, Integer> categoryIds) {
/* 2406 */     for (String categoryName : categories) {
/*      */ 
/*      */       
/* 2409 */       buffer.putShort(((Integer)categoryIds.get(categoryName)).shortValue());
/* 2410 */       addStringToBuffer(buffer, categoryName, true);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void addRecipesCategoryListMessageHeadersToBuffer(ByteBuffer buffer) {
/* 2419 */     buffer.put((byte)-46);
/* 2420 */     buffer.put((byte)4);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static short addCraftingRecipesToRecipesList(RecipesListParameter params, CreationEntry[] toAdd, boolean isSimple) {
/* 2426 */     short numberOfEntries = 0;
/*      */ 
/*      */     
/* 2429 */     for (CreationEntry entry : toAdd) {
/*      */       
/* 2431 */       if (isSimple)
/*      */       {
/*      */         
/* 2434 */         if (CreationMatrix.getInstance().getAdvancedEntriesMap().containsKey(Integer.valueOf(entry.getObjectCreated())) || entry
/* 2435 */           .getObjectTarget() == 672) {
/*      */           continue;
/*      */         }
/*      */       }
/*      */ 
/*      */       
/* 2441 */       String categoryName = entry.getCategory().getCategoryName();
/*      */       
/* 2443 */       List<CreationEntry> entries = null;
/*      */       
/* 2445 */       if (!params.getCreationEntries().containsKey(categoryName))
/*      */       {
/*      */         
/* 2448 */         params.getCreationEntries().put(categoryName, new ArrayList<>());
/*      */       }
/*      */ 
/*      */       
/* 2452 */       assignCategoryId(categoryName, params);
/*      */ 
/*      */       
/* 2455 */       entries = params.getCreationEntries().get(categoryName);
/*      */ 
/*      */       
/* 2458 */       entries.add(entry);
/*      */       
/* 2460 */       numberOfEntries = (short)(numberOfEntries + 1);
/*      */       continue;
/*      */     } 
/* 2463 */     return numberOfEntries;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final short addFencesToCraftingRecipesList(RecipesListParameter param) {
/* 2469 */     Map<String, List<ActionEntry>> flist = createFenceCreationList(true, true, false);
/*      */ 
/*      */     
/* 2472 */     int[] cTools = MethodsStructure.getCorrectToolsForBuildingFences();
/*      */     
/* 2474 */     short numberOfEntries = 0;
/*      */ 
/*      */     
/* 2477 */     for (String name : flist.keySet()) {
/*      */       
/* 2479 */       String categoryName = StringUtil.format("%s %s", new Object[] { name, "fences" });
/*      */ 
/*      */       
/* 2482 */       if (!param.getFences().containsKey(categoryName))
/*      */       {
/*      */         
/* 2485 */         param.getFences().put(categoryName, new ArrayList<>());
/*      */       }
/*      */ 
/*      */       
/* 2489 */       assignCategoryId(categoryName, param);
/*      */ 
/*      */       
/* 2492 */       List<ActionEntry> entries = param.getFences().get(categoryName);
/*      */ 
/*      */       
/* 2495 */       for (ActionEntry entry : flist.get(name)) {
/*      */ 
/*      */         
/* 2498 */         entries.add(entry);
/*      */ 
/*      */         
/* 2501 */         numberOfEntries = (short)(numberOfEntries + cTools.length);
/*      */       } 
/*      */     } 
/*      */     
/* 2505 */     return numberOfEntries;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final short addGenericRecipesToList(Map<String, List<Short>> list, RecipesListParameter param, short[] toAdd, String categoryToAdd) {
/* 2511 */     short numberOfEntries = 0;
/*      */     
/* 2513 */     assignCategoryId(categoryToAdd, param);
/*      */ 
/*      */     
/* 2516 */     for (int i = 0; i < toAdd.length; i++) {
/*      */ 
/*      */       
/* 2519 */       if (!list.containsKey(categoryToAdd))
/*      */       {
/* 2521 */         list.put(categoryToAdd, new ArrayList<>());
/*      */       }
/* 2523 */       List<Short> entries = list.get(categoryToAdd);
/* 2524 */       entries.add(Short.valueOf(toAdd[i]));
/* 2525 */       numberOfEntries = (short)(numberOfEntries + 1);
/*      */     } 
/*      */     
/* 2528 */     return numberOfEntries;
/*      */   }
/*      */ 
/*      */   
/*      */   private static void assignCategoryId(String category, RecipesListParameter params) {
/* 2533 */     if (!params.getCategoryIds().containsKey(category))
/*      */     {
/* 2535 */       params.getCategoryIds().put(category, Integer.valueOf(params.getCategoryIdsSize() + 1));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static final short addWallsToTheCraftingList(RecipesListParameter param) {
/* 2541 */     short numberOfEntries = 0;
/* 2542 */     String wallsCategory = "Walls";
/*      */     
/* 2544 */     assignCategoryId("Walls", param);
/*      */     
/* 2546 */     for (WallEnum en : WallEnum.values()) {
/*      */       
/* 2548 */       if (en != WallEnum.WALL_PLAN) {
/*      */         
/* 2550 */         if (!param.getWalls().containsKey("Walls"))
/*      */         {
/* 2552 */           param.getWalls().put("Walls", new ArrayList<>());
/*      */         }
/* 2554 */         List<WallEnum> entries = param.getWalls().get("Walls");
/* 2555 */         entries.add(en);
/*      */         
/* 2557 */         numberOfEntries = (short)(numberOfEntries + WallEnum.getToolsForWall(en, null).size());
/*      */       } 
/*      */     } 
/* 2560 */     return numberOfEntries;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final short addBridgePartsToTheCraftingList(RecipesListParameter param) {
/* 2565 */     short numberOfEntries = 0;
/*      */     
/* 2567 */     for (BridgePartEnum en : BridgePartEnum.values()) {
/*      */       
/* 2569 */       if (en != BridgePartEnum.UNKNOWN) {
/*      */         
/* 2571 */         String typeName = StringUtil.toLowerCase(en.getMaterial().getName());
/*      */         
/* 2573 */         typeName = StringUtil.format("%s %s", new Object[] { "bridge,", typeName });
/* 2574 */         String categoryName = LoginHandler.raiseFirstLetter(typeName);
/*      */         
/* 2576 */         assignCategoryId(categoryName, param);
/*      */         
/* 2578 */         if (!param.getBridgeParts().containsKey(categoryName))
/*      */         {
/* 2580 */           param.getBridgeParts().put(categoryName, new ArrayList<>());
/*      */         }
/* 2582 */         List<BridgePartEnum> entries = param.getBridgeParts().get(categoryName);
/* 2583 */         entries.add(en);
/* 2584 */         numberOfEntries = (short)(numberOfEntries + (BridgePartEnum.getValidToolsForMaterial(en.getMaterial())).length);
/*      */       } 
/*      */     } 
/* 2587 */     return numberOfEntries;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final short addCaveWallsToTheCraftingList(RecipesListParameter param) {
/* 2592 */     String wallsCategory = "Cave walls";
/*      */     
/* 2594 */     assignCategoryId("Cave walls", param);
/*      */ 
/*      */     
/* 2597 */     List<ActionEntry> flist = createCaveWallCreationList();
/*      */     
/* 2599 */     short numberOfEntries = 0;
/*      */ 
/*      */     
/* 2602 */     if (!param.getCaveWalls().containsKey("Cave walls"))
/*      */     {
/* 2604 */       param.getCaveWalls().put("Cave walls", new ArrayList<>());
/*      */     }
/*      */     
/* 2607 */     List<ActionEntry> entries = param.getCaveWalls().get("Cave walls");
/*      */ 
/*      */     
/* 2610 */     for (ActionEntry entry : flist) {
/*      */ 
/*      */       
/* 2613 */       entries.add(entry);
/*      */ 
/*      */       
/* 2616 */       numberOfEntries = (short)(numberOfEntries + (CaveWallBehaviour.getCorrectToolsForCladding(entry.getNumber())).length);
/*      */     } 
/* 2618 */     return numberOfEntries;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final short addRoofsFloorsToTheCraftingList(RecipesListParameter param) {
/* 2623 */     short numberOfEntries = 0;
/*      */     
/* 2625 */     for (RoofFloorEnum en : RoofFloorEnum.values()) {
/*      */       
/* 2627 */       if (en != RoofFloorEnum.UNKNOWN) {
/*      */         
/* 2629 */         String typeName = en.getType().getName();
/* 2630 */         if (typeName.contains("opening")) {
/* 2631 */           typeName = StringUtil.format("%s %s%s", new Object[] { "floor", typeName, "s" });
/* 2632 */         } else if (typeName.contains("staircase,")) {
/* 2633 */           typeName = StringUtil.format("%s", new Object[] { typeName.replace("se,", "ses,") });
/*      */         } else {
/* 2635 */           typeName = StringUtil.format("%s%s", new Object[] { typeName, "s" });
/* 2636 */         }  String categoryName = LoginHandler.raiseFirstLetter(typeName);
/*      */         
/* 2638 */         assignCategoryId(categoryName, param);
/*      */         
/* 2640 */         if (!param.getRoofs_floors().containsKey(categoryName))
/*      */         {
/* 2642 */           param.getRoofs_floors().put(categoryName, new ArrayList<>());
/*      */         }
/* 2644 */         List<RoofFloorEnum> entries = param.getRoofs_floors().get(categoryName);
/* 2645 */         entries.add(en);
/* 2646 */         numberOfEntries = (short)(numberOfEntries + (RoofFloorEnum.getValidToolsForMaterial(en.getMaterial())).length);
/*      */       } 
/*      */     } 
/* 2649 */     return numberOfEntries;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final boolean sendCaveWallRecipes(SocketConnection connection, @Nonnull Player player, RecipesListParameter params) {
/* 2656 */     for (String category : params.getCaveWalls().keySet()) {
/*      */ 
/*      */       
/* 2659 */       List<ActionEntry> entries = params.getCaveWalls().get(category);
/*      */ 
/*      */       
/* 2662 */       for (ActionEntry entry : entries) {
/*      */ 
/*      */         
/* 2665 */         byte partCladType = CaveWallBehaviour.getPartReinforcedWallFromAction(entry.getNumber());
/*      */         
/* 2667 */         byte cladType = CaveWallBehaviour.getReinforcedWallFromAction(entry.getNumber());
/*      */ 
/*      */         
/* 2670 */         int[] correctTools = CaveWallBehaviour.getCorrectToolsForCladding(entry.getNumber());
/* 2671 */         for (int i = 0; i < correctTools.length; i++) {
/*      */ 
/*      */           
/* 2674 */           ByteBuffer buffer = connection.getBuffer();
/*      */ 
/*      */           
/* 2677 */           addCreationRecipesMessageHeaders(buffer);
/*      */ 
/*      */           
/* 2680 */           addCategoryIdToBuffer(params, category, buffer);
/*      */ 
/*      */           
/* 2683 */           addCreatedReinforcedWallToBuffer(partCladType, cladType, buffer);
/*      */ 
/*      */           
/* 2686 */           if (!addFenceToolToBuffer(buffer, correctTools[i])) {
/*      */             
/* 2688 */             connection.clearBuffer();
/* 2689 */             return false;
/*      */           } 
/*      */ 
/*      */           
/* 2693 */           addReinforcedWallToBuffer(buffer);
/*      */           
/* 2695 */           if (!addAdditionalMaterialsForReinforcedWall(buffer, entry.getNumber())) {
/*      */             
/* 2697 */             connection.clearBuffer();
/* 2698 */             return false;
/*      */           } 
/*      */ 
/*      */           
/*      */           try {
/* 2703 */             connection.flush();
/*      */           }
/* 2705 */           catch (IOException ex) {
/*      */             
/* 2707 */             logger.log(Level.WARNING, "IO Exception when sending fence recipes.", ex);
/* 2708 */             player.setLink(false);
/* 2709 */             return false;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/* 2714 */     return true;
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
/*      */   public static class RecipesListParameter
/*      */   {
/* 2757 */     private Map<String, List<CreationEntry>> creationEntries = new HashMap<>();
/* 2758 */     private Map<String, Integer> categoryIds = new HashMap<>();
/* 2759 */     private Map<String, List<ActionEntry>> fences = new HashMap<>();
/* 2760 */     private Map<String, List<Short>> hedges = new HashMap<>();
/* 2761 */     private Map<String, List<Short>> flowerbeds = new HashMap<>();
/* 2762 */     private Map<String, List<WallEnum>> walls = new HashMap<>();
/* 2763 */     private Map<String, List<RoofFloorEnum>> roofs_floors = new HashMap<>();
/* 2764 */     private Map<String, List<BridgePartEnum>> bridgeParts = new HashMap<>();
/* 2765 */     private Map<String, List<ActionEntry>> cavewalls = new HashMap<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Map<String, List<CreationEntry>> getCreationEntries() {
/* 2775 */       return this.creationEntries;
/*      */     }
/*      */ 
/*      */     
/*      */     public final int getCreationEntriesSize() {
/* 2780 */       return this.creationEntries.size();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Map<String, Integer> getCategoryIds() {
/* 2790 */       return this.categoryIds;
/*      */     }
/*      */ 
/*      */     
/*      */     public final int getCategoryIdsSize() {
/* 2795 */       return this.categoryIds.size();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Map<String, List<ActionEntry>> getFences() {
/* 2805 */       return this.fences;
/*      */     }
/*      */ 
/*      */     
/*      */     public final int getFencesSize() {
/* 2810 */       return this.fences.size();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Map<String, List<Short>> getHedges() {
/* 2820 */       return this.hedges;
/*      */     }
/*      */ 
/*      */     
/*      */     public final int getHedgesSize() {
/* 2825 */       return this.hedges.size();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Map<String, List<Short>> getFlowerbeds() {
/* 2835 */       return this.flowerbeds;
/*      */     }
/*      */ 
/*      */     
/*      */     public final int getFlowerbedsSize() {
/* 2840 */       return this.flowerbeds.size();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Map<String, List<WallEnum>> getWalls() {
/* 2850 */       return this.walls;
/*      */     }
/*      */ 
/*      */     
/*      */     public final int getWallsSize() {
/* 2855 */       return this.walls.size();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Map<String, List<RoofFloorEnum>> getRoofs_floors() {
/* 2865 */       return this.roofs_floors;
/*      */     }
/*      */ 
/*      */     
/*      */     public final int getRoofs_floorsSize() {
/* 2870 */       return this.roofs_floors.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public Map<String, List<BridgePartEnum>> getBridgeParts() {
/* 2875 */       return this.bridgeParts;
/*      */     }
/*      */ 
/*      */     
/*      */     public final int getBridgePartsSize() {
/* 2880 */       return this.bridgeParts.size();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Map<String, List<ActionEntry>> getCaveWalls() {
/* 2890 */       return this.cavewalls;
/*      */     }
/*      */ 
/*      */     
/*      */     public final int getCaveWallsSize() {
/* 2895 */       return this.cavewalls.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public final int getTotalCategories() {
/* 2900 */       return getCreationEntriesSize() + getFencesSize() + getHedgesSize() + 
/* 2901 */         getFlowerbedsSize() + getWallsSize() + getRoofs_floorsSize() + getBridgePartsSize() + 
/* 2902 */         getCaveWallsSize();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static final short buildCreationsList(RecipesListParameter param) {
/* 2908 */     short numberOfEntries = 0;
/*      */ 
/*      */     
/* 2911 */     numberOfEntries = (short)(numberOfEntries + addCraftingRecipesToRecipesList(param, 
/* 2912 */         CreationMatrix.getInstance().getSimpleEntries(), true));
/*      */ 
/*      */     
/* 2915 */     numberOfEntries = (short)(numberOfEntries + addCraftingRecipesToRecipesList(param, 
/* 2916 */         CreationMatrix.getInstance().getAdvancedEntries(), false));
/*      */ 
/*      */     
/* 2919 */     numberOfEntries = (short)(numberOfEntries + addFencesToCraftingRecipesList(param));
/*      */ 
/*      */     
/* 2922 */     numberOfEntries = (short)(numberOfEntries + addGenericRecipesToList(param.getHedges(), param, Fence.getAllLowHedgeTypes(), "Hedges"));
/*      */ 
/*      */     
/* 2925 */     numberOfEntries = (short)(numberOfEntries + addGenericRecipesToList(param.getFlowerbeds(), param, Fence.getAllFlowerbeds(), "Flowerbeds"));
/*      */ 
/*      */     
/* 2928 */     numberOfEntries = (short)(numberOfEntries + addWallsToTheCraftingList(param));
/*      */     
/* 2930 */     numberOfEntries = (short)(numberOfEntries + addRoofsFloorsToTheCraftingList(param));
/*      */     
/* 2932 */     numberOfEntries = (short)(numberOfEntries + addBridgePartsToTheCraftingList(param));
/*      */     
/* 2934 */     numberOfEntries = (short)(numberOfEntries + addCaveWallsToTheCraftingList(param));
/*      */     
/* 2936 */     return numberOfEntries;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\items\CreationWindowMethods.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */