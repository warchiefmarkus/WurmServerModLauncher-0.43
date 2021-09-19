/*      */ package com.wurmonline.server.questions;
/*      */ 
/*      */ import com.wurmonline.server.Features;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.players.PlayerInfoFactory;
/*      */ import com.wurmonline.server.villages.Citizen;
/*      */ import com.wurmonline.server.villages.DbVillageRole;
/*      */ import com.wurmonline.server.villages.NoSuchRoleException;
/*      */ import com.wurmonline.server.villages.NoSuchVillageException;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.villages.VillageRole;
/*      */ import com.wurmonline.server.villages.VillageStatus;
/*      */ import com.wurmonline.server.villages.Villages;
/*      */ import java.io.IOException;
/*      */ import java.util.Arrays;
/*      */ import java.util.Properties;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import java.util.regex.Pattern;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class VillageRolesManageQuestion
/*      */   extends Question
/*      */   implements VillageStatus, TimeConstants
/*      */ {
/*   52 */   private static final Logger logger = Logger.getLogger(VillageRolesManageQuestion.class.getName());
/*      */   
/*      */   private static final int MAX_ROLES = 50;
/*      */   
/*      */   private static final String nameDesc = "label{text=\"Name of the role\"}";
/*      */   
/*      */   private static final String buildDesc = "label{text=\"Allows ability to plan, color and continue on buildings and fences.\"}";
/*      */   
/*      */   private static final String cutDesc = "label{text=\"Allows cutting down young trees, picking sprouts and planting new trees.\"}";
/*      */   
/*      */   private static final String cutOldDesc = "label{text=\"Allows cutting down old trees.\"}";
/*      */   
/*      */   private static final String destroyDesc = "label{text=\"Allows destroying of building and fence plans as well as finished buildings and plans. Also allows lockpicking of structures and removing lamp posts and signs.\"}";
/*      */   
/*      */   private static final String farmDesc = "label{text=\"Allows sowing, farming and harvesting farms. Also milking creatures.\"}";
/*      */   
/*      */   private static final String guardDesc = "label{text=\"Allows hiring guards to protect the settlement and uphold these rules.\"}";
/*      */   
/*      */   private static final String inviteDesc = "label{text=\"Allows ability to invite non-citizens to join the settlement.\"}";
/*      */   
/*      */   private static final String manageDesc = "label{text=\"Allows adding to and modifying these roles via this interface, as well as changing settings and setting reputations and disbanding the settlement.\"}";
/*      */   
/*      */   private static final String mineDesc = "label{text=\"Allows mining in general.\"}";
/*      */   
/*      */   private static final String digDesc = "label{text=\"Allows ability to dig, pack, pave, level and flatten.\"}";
/*      */   private static final String expandDesc = "label{text=\"Allows ability to expand the size of the settlement.\"}";
/*      */   private static final String passDesc = "label{text=\"Allows passing through all gates, locked or not.\"}";
/*      */   private static final String lockDesc = "label{text=\"Allows ability to unlock and relock gates inside the settlement.\"}";
/*      */   private static final String atkDesc = "label{text=\"Allows ability to attack citizens and allies of the settlement.\"}";
/*      */   private static final String atknonDesc = "label{text=\"Allows ability to attack non citizens.\"}";
/*      */   private static final String fishDesc = "label{text=\"Allows ability to fish on deed.\"}";
/*      */   private static final String pullDesc = "label{text=\"Allows ability to push, pull, turn items. Note that pull and push may require pickup permission as well\"}";
/*      */   private static final String dipDesc = "label{text=\"Allows ability to form and break alliances with other settlements, as well as declare war and make peace.\"}";
/*      */   private static final String mapDesc = "label{text=\"Allows ability to add and remove village annotations on the map.\"}";
/*      */   private static final String leadDesc = "label{text=\"Allows leading creatures.\"}";
/*      */   private static final String pickupDesc = "label{text=\"Allows ability to pickup item on deed.\"}";
/*      */   private static final String tameDesc = "label{text=\"Allows ability to Tame on deed.\"}";
/*      */   private static final String loadDesc = "label{text=\"Allows ability to Load on deed (needs Pickup as well).\"}";
/*      */   private static final String butcherDesc = "label{text=\"Allows ability to Butcher on deed.\"}";
/*      */   private static final String attachDesc = "label{text=\"Allows ability to Attach Locks on deed.\"}";
/*      */   private static final String pickLocksDesc = "label{text=\"Allows lockpicking of structures.\"}";
/*      */   private static final String deedDesc = "label{text=\"Make the role apply to a certain settlement only. Useful in alliances.\"}";
/*      */   private static final String deleteDesc = "label{text=\"If you check that box the role will disappear.\"}";
/*      */   private int roleId;
/*      */   private final String gmBack;
/*      */   private final int gmRowsPerPage;
/*      */   private final byte gmCurrentPage;
/*      */   
/*      */   public VillageRolesManageQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
/*  101 */     this(aResponder, aTitle, aQuestion, aTarget, -10, "", 50, (byte)0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public VillageRolesManageQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget, int aRoleId, String aGMBack, int rowsPerPage, byte iPage) {
/*  107 */     super(aResponder, aTitle, aQuestion, 105, aTarget);
/*  108 */     this.roleId = aRoleId;
/*  109 */     this.gmBack = aGMBack;
/*  110 */     this.gmCurrentPage = iPage;
/*  111 */     this.gmRowsPerPage = rowsPerPage;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void answer(Properties answers) {
/*  120 */     setAnswer(answers);
/*  121 */     Village village = getVillage();
/*  122 */     if (village == null) {
/*      */       return;
/*      */     }
/*  125 */     for (String key : getAnswer().stringPropertyNames()) {
/*      */       
/*  127 */       if (key.equals("close")) {
/*      */         return;
/*      */       }
/*      */ 
/*      */       
/*  132 */       if (key.equals("reset")) {
/*      */         
/*  134 */         village.resetRoles();
/*  135 */         getResponder().getCommunicator().sendNormalServerMessage("All roles were reset. Any other changes you tried to make were discarded.");
/*      */         
/*      */         return;
/*      */       } 
/*  139 */       if (key.startsWith("change")) {
/*      */ 
/*      */         
/*  142 */         int id = Integer.parseInt(key.substring(6));
/*      */         
/*      */         try {
/*  145 */           VillageRole vr = village.getRole(id);
/*  146 */           String newName = getAnswer().getProperty("name");
/*  147 */           if (QuestionParser.containsIllegalCharacters(newName)) {
/*  148 */             getResponder().getCommunicator().sendSafeServerMessage("The role " + newName + " contains illegal letters. Please select another name."); continue;
/*      */           } 
/*  150 */           if (newName.length() < 3) {
/*  151 */             getResponder().getCommunicator().sendSafeServerMessage("The role " + newName + " contains less than 3 letters. Please select another name.");
/*      */             continue;
/*      */           } 
/*  154 */           vr.setName(newName);
/*      */         }
/*  156 */         catch (IOException iox) {
/*      */           
/*  158 */           logger.log(Level.WARNING, "Failed to change mayor role " + id + ":" + iox.getMessage(), iox);
/*  159 */           getResponder().getCommunicator().sendSafeServerMessage("Failed to change the name of the mayor role. Please contact administration.");
/*      */         
/*      */         }
/*  162 */         catch (NoSuchRoleException nsre) {
/*      */           
/*  164 */           logger.log(Level.WARNING, nsre.getMessage(), (Throwable)nsre);
/*  165 */           getResponder().getCommunicator().sendSafeServerMessage("Failed to find the mayor role. Please contact administration.");
/*      */         } 
/*      */         continue;
/*      */       } 
/*  169 */       if (key.equals("add")) {
/*      */         
/*  171 */         int id = -1;
/*      */ 
/*      */         
/*  174 */         VillageRolesManageQuestion vrmq = new VillageRolesManageQuestion(getResponder(), getTitle(), getQuestion(), getTarget(), -1, this.gmBack, this.gmRowsPerPage, this.gmCurrentPage);
/*  175 */         vrmq.sendQuestion(); continue;
/*      */       } 
/*  177 */       if (key.startsWith("edit")) {
/*      */         
/*  179 */         int id = Integer.parseInt(key.substring(4));
/*      */ 
/*      */         
/*  182 */         VillageRolesManageQuestion vrmq = new VillageRolesManageQuestion(getResponder(), getTitle(), getQuestion(), getTarget(), id, this.gmBack, this.gmRowsPerPage, this.gmCurrentPage);
/*  183 */         vrmq.sendQuestion(); continue;
/*      */       } 
/*  185 */       if (key.startsWith("show")) {
/*      */         
/*  187 */         int id = Integer.parseInt(key.substring(4));
/*      */ 
/*      */         
/*  190 */         VillageRolesManageQuestion vrmq = new VillageRolesManageQuestion(getResponder(), getTitle(), getQuestion(), getTarget(), id, this.gmBack, this.gmRowsPerPage, this.gmCurrentPage);
/*  191 */         vrmq.sendQuestion(); continue;
/*      */       } 
/*  193 */       if (key.equals("GMTool")) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  199 */         byte qType = 0;
/*  200 */         byte qSubType = 0;
/*  201 */         long wId = -10L;
/*  202 */         String sSearch = "";
/*  203 */         String sBack = "";
/*  204 */         String[] backs = this.gmBack.split(Pattern.quote("|"));
/*  205 */         if (backs.length > 0) {
/*      */ 
/*      */           
/*  208 */           StringBuilder buf = new StringBuilder();
/*  209 */           if (backs.length > 1) {
/*      */             
/*  211 */             buf.append(backs[0]);
/*  212 */             for (int s = 1; s < backs.length - 1; s++) {
/*  213 */               buf.append("|" + backs[s]);
/*      */             }
/*      */           } 
/*  216 */           String[] lparts = backs[backs.length - 1].split(",");
/*  217 */           qType = Byte.parseByte(lparts[0]);
/*  218 */           qSubType = Byte.parseByte(lparts[1]);
/*  219 */           wId = Long.parseLong(lparts[2]);
/*      */ 
/*      */           
/*  222 */           if (lparts.length > 3)
/*  223 */             sSearch = lparts[3]; 
/*  224 */           sBack = buf.toString();
/*      */         } else {
/*      */           return;
/*      */         } 
/*  228 */         GmTool gt = new GmTool(getResponder(), qType, qSubType, wId, sSearch, sBack, this.gmRowsPerPage, this.gmCurrentPage);
/*  229 */         gt.sendQuestion(); continue;
/*      */       } 
/*  231 */       if (key.equals("save")) {
/*      */         
/*  233 */         int oldRoleId = this.roleId;
/*  234 */         String roleName = getAnswer().getProperty("roleName");
/*      */         
/*  236 */         if (QuestionParser.containsIllegalCharacters(roleName)) {
/*      */           
/*  238 */           getResponder().getCommunicator().sendSafeServerMessage("The role " + roleName + " contains illegal letters. Please select another name.");
/*      */           
/*      */           return;
/*      */         } 
/*  242 */         if (roleName.length() < 3) {
/*      */           
/*  244 */           getResponder().getCommunicator().sendSafeServerMessage("The role " + roleName + " contains less than 3 letters. Please select another name.");
/*      */           
/*      */           return;
/*      */         } 
/*  248 */         if (this.roleId == -1) {
/*      */ 
/*      */           
/*  251 */           String externalSettlement = getAnswer().getProperty("externalSettlement");
/*  252 */           String individualPlayer = getAnswer().getProperty("individualPlayer");
/*      */           
/*  254 */           int villageAppliedTo = 0;
/*  255 */           if (externalSettlement != null && externalSettlement.length() > 0) {
/*      */             
/*  257 */             if (externalSettlement.equalsIgnoreCase(getResponder().getCitizenVillage().getName())) {
/*      */               
/*  259 */               getResponder().getCommunicator().sendAlertServerMessage("Use normal roles instead of the village applied option for your own settlement.");
/*      */ 
/*      */               
/*      */               return;
/*      */             } 
/*      */ 
/*      */             
/*      */             try {
/*  267 */               Village vill = Villages.getVillage(externalSettlement);
/*  268 */               villageAppliedTo = vill.getId();
/*      */             }
/*  270 */             catch (NoSuchVillageException nsv) {
/*      */               
/*  272 */               getResponder().getCommunicator().sendAlertServerMessage("No village found with the name \"" + externalSettlement + "\".");
/*      */               
/*      */               return;
/*      */             } 
/*      */           } 
/*      */           
/*  278 */           long playerAppliedTo = -10L;
/*  279 */           if (individualPlayer != null && individualPlayer.length() > 0) {
/*      */             
/*  281 */             if (villageAppliedTo > 0) {
/*      */               
/*  283 */               getResponder().getCommunicator().sendAlertServerMessage("Cannot have external village and individual player in same role. So defaulting to specified village.");
/*      */ 
/*      */               
/*      */               return;
/*      */             } 
/*      */ 
/*      */             
/*  290 */             long pid = PlayerInfoFactory.getWurmId(individualPlayer);
/*  291 */             if (pid == -10L) {
/*      */               
/*  293 */               getResponder().getCommunicator().sendAlertServerMessage("Player \"" + individualPlayer + "\" not found.");
/*      */               
/*      */               return;
/*      */             } 
/*  297 */             if (village.isCitizen(pid)) {
/*      */               
/*  299 */               getResponder().getCommunicator().sendAlertServerMessage("Use normal roles instead of the individual player option for your own settlement citizens.");
/*      */               
/*      */               return;
/*      */             } 
/*      */             
/*  304 */             playerAppliedTo = pid;
/*      */           } 
/*      */ 
/*      */ 
/*      */           
/*      */           try {
/*  310 */             DbVillageRole newrole = new DbVillageRole(village.getId(), roleName, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, (byte)0, villageAppliedTo, false, false, false, false, false, false, false, false, false, playerAppliedTo, 0, 0, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  316 */             village.addRole((VillageRole)newrole);
/*  317 */             this.roleId = newrole.id;
/*  318 */             getResponder().getCommunicator().sendSafeServerMessage("The role \"" + roleName + "\" has been created.");
/*      */           }
/*  320 */           catch (IOException iox) {
/*      */             
/*  322 */             logger.log(Level.WARNING, "Failed to create role \"" + roleName + "\":" + iox.getMessage(), iox);
/*  323 */             getResponder().getCommunicator().sendSafeServerMessage("Failed to create the role \"" + roleName + "\". Please contact administration.");
/*      */             
/*      */             return;
/*      */           } 
/*      */         } 
/*      */         
/*      */         try {
/*  330 */           VillageRole vr = village.getRole(this.roleId);
/*  331 */           String oldName = vr.getName();
/*      */ 
/*      */ 
/*      */           
/*  335 */           boolean aCitizen = (vr.getStatus() == 3 || (vr.getStatus() == 0 && vr.getVillageAppliedTo() == 0 && vr.getPlayerAppliedTo() == -10L));
/*      */           
/*  337 */           if (!oldName.equals(roleName)) {
/*  338 */             vr.setName(roleName);
/*      */           }
/*  340 */           vr.setCanBrand((Boolean.parseBoolean(getAnswer().getProperty("brand")) && aCitizen));
/*  341 */           vr.setCanBreed(Boolean.parseBoolean(getAnswer().getProperty("breed")));
/*  342 */           vr.setCanButcher(Boolean.parseBoolean(getAnswer().getProperty("butcher")));
/*  343 */           vr.setCanGroom(Boolean.parseBoolean(getAnswer().getProperty("groom")));
/*  344 */           vr.setCanLead(Boolean.parseBoolean(getAnswer().getProperty("lead")));
/*  345 */           vr.setCanMilkShear(Boolean.parseBoolean(getAnswer().getProperty("milk")));
/*  346 */           vr.setCanSacrifice(Boolean.parseBoolean(getAnswer().getProperty("sacrifice")));
/*  347 */           vr.setCanTame(Boolean.parseBoolean(getAnswer().getProperty("tame")));
/*      */           
/*  349 */           vr.setCanBuild(Boolean.parseBoolean(getAnswer().getProperty("build")));
/*  350 */           if (vr.getStatus() != 1)
/*  351 */             vr.setCanDestroyAnyBuilding(Boolean.parseBoolean(getAnswer().getProperty("destroyAnyBuildings"))); 
/*  352 */           vr.setCanDestroyFence(Boolean.parseBoolean(getAnswer().getProperty("destroyFence")));
/*  353 */           vr.setCanDestroyItems(Boolean.parseBoolean(getAnswer().getProperty("destroyIteme")));
/*  354 */           vr.setCanPickLocks((Boolean.parseBoolean(getAnswer().getProperty("pickLocks")) && aCitizen));
/*  355 */           vr.setCanPlanBuildings(Boolean.parseBoolean(getAnswer().getProperty("planBuildings")));
/*      */           
/*  357 */           vr.setCanCultivate(Boolean.parseBoolean(getAnswer().getProperty("cultivate")));
/*  358 */           vr.setCanDigResource(Boolean.parseBoolean(getAnswer().getProperty("digResource")));
/*  359 */           vr.setCanPack(Boolean.parseBoolean(getAnswer().getProperty("pack")));
/*  360 */           vr.setCanTerraform(Boolean.parseBoolean(getAnswer().getProperty("terraform")));
/*      */           
/*  362 */           vr.setCanHarvestFields(Boolean.parseBoolean(getAnswer().getProperty("harvest")));
/*  363 */           vr.setCanSowFields(Boolean.parseBoolean(getAnswer().getProperty("sow")));
/*  364 */           vr.setCanTendFields(Boolean.parseBoolean(getAnswer().getProperty("tend")));
/*      */           
/*  366 */           vr.setCanChopDownAllTrees(Boolean.parseBoolean(getAnswer().getProperty("chopAllTrees")));
/*  367 */           vr.setCanChopDownOldTrees(Boolean.parseBoolean(getAnswer().getProperty("chopOldTrees")));
/*  368 */           vr.setCanCutGrass(Boolean.parseBoolean(getAnswer().getProperty("cutGrass")));
/*  369 */           vr.setCanHarvestFruit(Boolean.parseBoolean(getAnswer().getProperty("harvestFruit")));
/*  370 */           vr.setCanMakeLawn(Boolean.parseBoolean(getAnswer().getProperty("makeLawn")));
/*  371 */           vr.setCanPickSprouts(Boolean.parseBoolean(getAnswer().getProperty("pickSprouts")));
/*  372 */           vr.setCanPlantFlowers(Boolean.parseBoolean(getAnswer().getProperty("plantFlowers")));
/*  373 */           vr.setCanPlantSprouts(Boolean.parseBoolean(getAnswer().getProperty("plantSprouts")));
/*  374 */           vr.setCanPrune(Boolean.parseBoolean(getAnswer().getProperty("prune")));
/*      */           
/*  376 */           vr.setCanAttackCitizens(Boolean.parseBoolean(getAnswer().getProperty("attackCitizens")));
/*  377 */           vr.setCanAttackNonCitizens(Boolean.parseBoolean(getAnswer().getProperty("attackNonCitizens")));
/*  378 */           vr.setCanCastDeitySpells(Boolean.parseBoolean(getAnswer().getProperty("deitySpells")));
/*  379 */           vr.setCanCastSorcerySpells(Boolean.parseBoolean(getAnswer().getProperty("sorcerySpells")));
/*  380 */           vr.setCanForageBotanize(Boolean.parseBoolean(getAnswer().getProperty("forage")));
/*  381 */           vr.setCanPassGates(Boolean.parseBoolean(getAnswer().getProperty("passGates")));
/*  382 */           vr.setCanPlaceMerchants(Boolean.parseBoolean(getAnswer().getProperty("placeMerchants")));
/*  383 */           vr.setCanPave(Boolean.parseBoolean(getAnswer().getProperty("pave")));
/*  384 */           vr.setCanUseMeditationAbility(Boolean.parseBoolean(getAnswer().getProperty("meditationAbilities")));
/*      */           
/*  386 */           vr.setCanAttachLocks((Boolean.parseBoolean(getAnswer().getProperty("attachLocks")) && aCitizen));
/*  387 */           vr.setCanDrop(Boolean.parseBoolean(getAnswer().getProperty("drop")));
/*  388 */           vr.setCanImproveRepair(Boolean.parseBoolean(getAnswer().getProperty("improve")));
/*  389 */           vr.setCanLoad(Boolean.parseBoolean(getAnswer().getProperty("load")));
/*  390 */           vr.setCanPickup(Boolean.parseBoolean(getAnswer().getProperty("pickup")));
/*  391 */           vr.setCanPickupPlanted(Boolean.parseBoolean(getAnswer().getProperty("pickupPlanted")));
/*  392 */           vr.setCanPlantItem(Boolean.parseBoolean(getAnswer().getProperty("plantItem")));
/*  393 */           vr.setCanPullPushTurn(Boolean.parseBoolean(getAnswer().getProperty("pullPushTurn")));
/*  394 */           vr.setCanUnload(Boolean.parseBoolean(getAnswer().getProperty("unload")));
/*      */           
/*  396 */           vr.setCanMineFloor(Boolean.parseBoolean(getAnswer().getProperty("mineFloor")));
/*  397 */           vr.setCanMineIron(Boolean.parseBoolean(getAnswer().getProperty("mineIron")));
/*  398 */           vr.setCanMineOther(Boolean.parseBoolean(getAnswer().getProperty("mineOther")));
/*  399 */           vr.setCanMineRock(Boolean.parseBoolean(getAnswer().getProperty("mineRock")));
/*  400 */           vr.setCanSurface(Boolean.parseBoolean(getAnswer().getProperty("surface")));
/*  401 */           vr.setCanTunnel(Boolean.parseBoolean(getAnswer().getProperty("tunnel")));
/*  402 */           vr.setCanReinforce(Boolean.parseBoolean(getAnswer().getProperty("reinforce")));
/*      */           
/*  404 */           if (aCitizen) {
/*      */             
/*  406 */             vr.SetCanPerformActionsOnAlliedDeeds(Boolean.parseBoolean(getAnswer().getProperty("helpAllied")));
/*  407 */             vr.setCanDiplomat(Boolean.parseBoolean(getAnswer().getProperty("diplomat")));
/*  408 */             vr.setCanInviteCitizens(Boolean.parseBoolean(getAnswer().getProperty("inviteCitizens")));
/*  409 */             vr.setCanManageAllowedObjects(Boolean.parseBoolean(getAnswer().getProperty("manageAllowedObjects")));
/*  410 */             vr.setCanManageCitizenRoles(Boolean.parseBoolean(getAnswer().getProperty("manageCitizenRoles")));
/*  411 */             vr.setCanManageGuards(Boolean.parseBoolean(getAnswer().getProperty("manageGuards")));
/*  412 */             vr.setCanManageMap(Boolean.parseBoolean(getAnswer().getProperty("manageMap")));
/*  413 */             vr.setCanManageReputations(Boolean.parseBoolean(getAnswer().getProperty("manageReputations")));
/*  414 */             vr.setCanManageRoles(Boolean.parseBoolean(getAnswer().getProperty("manageRoles")));
/*  415 */             vr.setCanManageSettings(Boolean.parseBoolean(getAnswer().getProperty("manageSettings")));
/*  416 */             vr.setCanConfigureTwitter(Boolean.parseBoolean(getAnswer().getProperty("twitter")));
/*  417 */             vr.setCanResizeSettlement(Boolean.parseBoolean(getAnswer().getProperty("resize")));
/*      */           } 
/*      */           
/*  420 */           vr.save();
/*  421 */           if (oldRoleId != -1)
/*      */           {
/*  423 */             getResponder().getCommunicator().sendSafeServerMessage("The role \"" + roleName + "\" has been updated to your specifications.");
/*      */           
/*      */           }
/*      */         }
/*  427 */         catch (NoSuchRoleException nsre) {
/*      */           
/*  429 */           logger.log(Level.WARNING, nsre.getMessage(), (Throwable)nsre);
/*  430 */           getResponder().getCommunicator().sendSafeServerMessage("Failed to find the role. Make sure it still exists.");
/*      */         
/*      */         }
/*  433 */         catch (IOException ioe) {
/*      */           
/*  435 */           logger.log(Level.WARNING, ioe.getMessage(), ioe);
/*  436 */           getResponder().getCommunicator().sendSafeServerMessage("Failed to save the role.");
/*      */         } 
/*      */         continue;
/*      */       } 
/*  440 */       if (key.equals("back")) {
/*      */ 
/*      */ 
/*      */         
/*  444 */         VillageRolesManageQuestion vrmq = new VillageRolesManageQuestion(getResponder(), getTitle(), getQuestion(), getTarget(), -10, this.gmBack, this.gmRowsPerPage, this.gmCurrentPage);
/*  445 */         vrmq.sendQuestion(); continue;
/*      */       } 
/*  447 */       if (key.equals("remove")) {
/*      */ 
/*      */         
/*      */         try {
/*  451 */           VillageRole vr = village.getRole(this.roleId);
/*  452 */           if (vr.getStatus() == 3) {
/*  453 */             getResponder().getCommunicator().sendNormalServerMessage("There must always exist a citizen role. You cannot delete it.");
/*      */           }
/*      */           else {
/*      */             
/*  457 */             String name = vr.getName();
/*  458 */             village.removeRole(vr);
/*  459 */             vr.delete();
/*  460 */             getResponder().getCommunicator().sendNormalServerMessage("You have removed the role '" + name + "'.");
/*      */           }
/*      */         
/*      */         }
/*  464 */         catch (NoSuchRoleException nsre) {
/*      */ 
/*      */           
/*  467 */           logger.log(Level.WARNING, nsre.getMessage(), (Throwable)nsre);
/*      */         }
/*  469 */         catch (IOException iox) {
/*      */           
/*  471 */           logger.log(Level.WARNING, "Failed to change role " + this.roleId + ":" + iox.getMessage(), iox);
/*  472 */           getResponder().getCommunicator().sendSafeServerMessage("Failed to change the role. Please contact administration.");
/*      */         } 
/*      */         return;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendQuestion() {
/*  483 */     Village village = getVillage();
/*      */     
/*  485 */     if (village == null) {
/*      */       return;
/*      */     }
/*  488 */     if (this.roleId != -10) {
/*  489 */       roleShow(village, this.roleId);
/*      */     } else {
/*  491 */       roleList(village);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void roleShow(Village village, int rid) {
/*  497 */     VillageRole citRole = null;
/*  498 */     if (this.gmBack.length() == 0) {
/*      */       
/*  500 */       Citizen cit = village.getCitizen(getResponder().getWurmId());
/*  501 */       if (cit == null) {
/*      */         
/*  503 */         getResponder().getCommunicator().sendNormalServerMessage("You are not a citizen here");
/*      */         return;
/*      */       } 
/*  506 */       citRole = cit.getRole();
/*      */     } 
/*  508 */     VillageRole villRole = null;
/*      */ 
/*      */     
/*      */     try {
/*  512 */       if (rid != -1) {
/*  513 */         villRole = village.getRole(rid);
/*      */       }
/*  515 */     } catch (NoSuchRoleException nsre) {
/*      */       
/*  517 */       logger.log(Level.WARNING, nsre.getMessage(), (Throwable)nsre);
/*  518 */       getResponder().getCommunicator().sendSafeServerMessage("Failed to find the role. Please contact administration.");
/*      */       
/*      */       return;
/*      */     } 
/*  522 */     roleShow(getResponder(), getId(), citRole, villRole, this.gmBack);
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
/*      */   public static void roleShow(Creature performer, int qId, VillageRole citRole, VillageRole villRole, String gmBack) {
/*  534 */     String myRole1 = " ";
/*  535 */     String myRole2 = " ";
/*      */     
/*  537 */     if (citRole == null) {
/*      */       
/*  539 */       myRole1 = "";
/*  540 */       myRole2 = "INFO: Readonly";
/*      */     }
/*  542 */     else if (villRole == null) {
/*      */       
/*  544 */       myRole1 = "INFO: You can only add the permissions";
/*  545 */       myRole2 = " to a new role if your role has them.";
/*      */     }
/*  547 */     else if (villRole.getId() == citRole.getId()) {
/*      */       
/*  549 */       myRole1 = "WARNING: This is your own role!";
/*  550 */       myRole2 = "You can only disable permissions.";
/*      */     }
/*      */     else {
/*      */       
/*  554 */       myRole1 = "INFO: You can only change the permissions";
/*  555 */       myRole2 = " that your role has.";
/*      */     } 
/*      */ 
/*      */     
/*  559 */     boolean iCanBrand = false;
/*  560 */     boolean iCanBreed = false;
/*  561 */     boolean iCanButcher = false;
/*  562 */     boolean iCanGroom = false;
/*  563 */     boolean iCanLead = false;
/*  564 */     boolean iCanMilkShear = false;
/*  565 */     boolean iCanSacrifice = false;
/*  566 */     boolean iCanTame = false;
/*      */     
/*  568 */     boolean iCanBuild = false;
/*  569 */     boolean iCanDestroyFence = false;
/*  570 */     boolean iCanDestroyItems = false;
/*  571 */     boolean iCanPickLocks = false;
/*  572 */     boolean iCanPlanBuildings = false;
/*      */     
/*  574 */     boolean iCanCultivate = false;
/*  575 */     boolean iCanDigResource = false;
/*  576 */     boolean iCanPack = false;
/*  577 */     boolean iCanTerraform = false;
/*      */     
/*  579 */     boolean iCanHarvestFields = false;
/*  580 */     boolean iCanSowFields = false;
/*  581 */     boolean iCanTendFields = false;
/*      */     
/*  583 */     boolean iCanChopAllTrees = false;
/*  584 */     boolean iCanChopOldTrees = false;
/*  585 */     boolean iCanCutGrass = false;
/*  586 */     boolean iCanHarvestFruit = false;
/*  587 */     boolean iCanMakeLawn = false;
/*  588 */     boolean iCanPickSprouts = false;
/*  589 */     boolean iCanPlantFlowers = false;
/*  590 */     boolean iCanPlantSprouts = false;
/*  591 */     boolean iCanPrune = false;
/*      */     
/*  593 */     boolean iCanAttackCitizens = false;
/*  594 */     boolean iCanAttackNonCitizens = false;
/*  595 */     boolean iCanCastDeitySpells = false;
/*  596 */     boolean iCanCastSorcerySpells = false;
/*  597 */     boolean iCanForageBotanize = false;
/*  598 */     boolean iCanPassGates = false;
/*  599 */     boolean iCanPlaceMerchants = false;
/*  600 */     boolean iCanPave = false;
/*  601 */     boolean iCanUseMeditationAbilities = false;
/*      */     
/*  603 */     boolean iCanAttachLocks = false;
/*  604 */     boolean iCanDrop = false;
/*  605 */     boolean iCanImproveRepair = false;
/*  606 */     boolean iCanLoad = false;
/*  607 */     boolean iCanPickup = false;
/*  608 */     boolean iCanPickupPlanted = false;
/*  609 */     boolean iCanPullPushTurn = false;
/*  610 */     boolean iCanUnload = false;
/*  611 */     boolean iCanPlantItem = false;
/*      */     
/*  613 */     boolean iCanMineFloor = false;
/*  614 */     boolean iCanMineIron = false;
/*  615 */     boolean iCanMineOther = false;
/*  616 */     boolean iCanMineRock = false;
/*  617 */     boolean iCanSurface = false;
/*  618 */     boolean iCanTunnel = false;
/*  619 */     boolean iCanReinforce = false;
/*      */     
/*  621 */     boolean iCanHelpAllied = false;
/*  622 */     boolean iCanDiplomat = false;
/*  623 */     boolean iCanDestroyAnyBuilding = false;
/*  624 */     boolean iCanInviteCitizens = false;
/*  625 */     boolean iCanManageAllowedObjects = false;
/*  626 */     boolean iCanManageCitizenRoles = false;
/*  627 */     boolean iCanManageGuards = false;
/*  628 */     boolean iCanManageMap = false;
/*  629 */     boolean iCanManageReputations = false;
/*  630 */     boolean iCanManageRoles = false;
/*  631 */     boolean iCanManageSettings = false;
/*  632 */     boolean iCanConfigureTwitter = false;
/*  633 */     boolean iCanResizeSettlement = false;
/*      */     
/*  635 */     if (citRole != null) {
/*      */ 
/*      */       
/*  638 */       iCanBrand = citRole.mayBrand();
/*  639 */       iCanBreed = citRole.mayBreed();
/*  640 */       iCanButcher = citRole.mayButcher();
/*  641 */       iCanGroom = citRole.mayGroom();
/*  642 */       iCanLead = citRole.mayLead();
/*  643 */       iCanMilkShear = citRole.mayMilkAndShear();
/*  644 */       iCanSacrifice = citRole.maySacrifice();
/*  645 */       iCanTame = citRole.mayTame();
/*      */       
/*  647 */       iCanBuild = citRole.mayBuild();
/*  648 */       iCanDestroyAnyBuilding = citRole.mayDestroyAnyBuilding();
/*  649 */       iCanDestroyFence = citRole.mayDestroyFences();
/*  650 */       iCanDestroyItems = citRole.mayDestroyItems();
/*  651 */       iCanPickLocks = citRole.mayPickLocks();
/*  652 */       iCanPlanBuildings = citRole.mayPlanBuildings();
/*      */       
/*  654 */       iCanCultivate = citRole.mayCultivate();
/*  655 */       iCanDigResource = citRole.mayDigResources();
/*  656 */       iCanPack = citRole.mayPack();
/*  657 */       iCanTerraform = citRole.mayTerraform();
/*      */       
/*  659 */       iCanHarvestFields = citRole.mayHarvestFields();
/*  660 */       iCanSowFields = citRole.maySowFields();
/*  661 */       iCanTendFields = citRole.mayTendFields();
/*      */       
/*  663 */       iCanChopAllTrees = citRole.mayChopDownAllTrees();
/*  664 */       iCanChopOldTrees = citRole.mayChopDownOldTrees();
/*  665 */       iCanCutGrass = citRole.mayCutGrass();
/*  666 */       iCanHarvestFruit = citRole.mayHarvestFruit();
/*  667 */       iCanMakeLawn = citRole.mayMakeLawn();
/*  668 */       iCanPickSprouts = citRole.mayPickSprouts();
/*  669 */       iCanPlantFlowers = citRole.mayPlantFlowers();
/*  670 */       iCanPlantSprouts = citRole.mayPlantSprouts();
/*  671 */       iCanPrune = citRole.mayPrune();
/*      */       
/*  673 */       iCanAttackCitizens = citRole.mayAttackCitizens();
/*  674 */       iCanAttackNonCitizens = citRole.mayAttackNonCitizens();
/*  675 */       iCanCastDeitySpells = citRole.mayCastDeitySpells();
/*  676 */       iCanCastSorcerySpells = citRole.mayCastSorcerySpells();
/*  677 */       iCanForageBotanize = citRole.mayForageAndBotanize();
/*  678 */       iCanPassGates = citRole.mayPassGates();
/*  679 */       iCanPlaceMerchants = citRole.mayPlaceMerchants();
/*  680 */       iCanPave = citRole.mayPave();
/*  681 */       iCanUseMeditationAbilities = citRole.mayUseMeditationAbilities();
/*      */       
/*  683 */       iCanAttachLocks = citRole.mayAttachLock();
/*  684 */       iCanDrop = citRole.mayDrop();
/*  685 */       iCanImproveRepair = citRole.mayImproveAndRepair();
/*  686 */       iCanLoad = citRole.mayLoad();
/*  687 */       iCanPickup = citRole.mayPickup();
/*  688 */       iCanPickupPlanted = citRole.mayPickupPlanted();
/*  689 */       iCanPullPushTurn = citRole.mayPushPullTurn();
/*  690 */       iCanUnload = citRole.mayUnload();
/*  691 */       iCanPlantItem = citRole.mayPlantItem();
/*      */       
/*  693 */       iCanMineFloor = citRole.mayMineFloor();
/*  694 */       iCanMineIron = citRole.mayMineIronVeins();
/*  695 */       iCanMineOther = citRole.mayMineOtherVeins();
/*  696 */       iCanMineRock = citRole.mayMineRock();
/*  697 */       iCanSurface = citRole.mayMineSurface();
/*  698 */       iCanTunnel = citRole.mayTunnel();
/*  699 */       iCanReinforce = citRole.mayReinforce();
/*      */       
/*  701 */       iCanHelpAllied = citRole.mayPerformActionsOnAlliedDeeds();
/*  702 */       iCanDiplomat = citRole.isDiplomat();
/*  703 */       iCanInviteCitizens = citRole.mayInviteCitizens();
/*  704 */       iCanManageAllowedObjects = citRole.mayManageAllowedObjects();
/*  705 */       iCanManageCitizenRoles = citRole.mayManageCitizenRoles();
/*  706 */       iCanManageGuards = citRole.mayManageGuards();
/*  707 */       iCanManageMap = citRole.mayManageMap();
/*  708 */       iCanManageReputations = citRole.mayManageReputations();
/*  709 */       iCanManageRoles = citRole.mayManageRoles();
/*  710 */       iCanManageSettings = citRole.mayManageSettings();
/*  711 */       iCanConfigureTwitter = citRole.mayConfigureTwitter();
/*  712 */       iCanResizeSettlement = citRole.mayResizeSettlement();
/*      */     } 
/*      */     
/*  715 */     String roleDesc = "new role";
/*  716 */     String roleName = "";
/*  717 */     String defaultRole = "";
/*  718 */     byte role = 0;
/*  719 */     boolean hasExternalSettlement = true;
/*  720 */     boolean hasIndividualPlayer = true;
/*  721 */     String externalSettlement = "";
/*  722 */     String individualPlayer = "";
/*      */ 
/*      */     
/*  725 */     boolean brand = false;
/*  726 */     boolean breed = false;
/*  727 */     boolean butcher = false;
/*  728 */     boolean groom = false;
/*  729 */     boolean lead = false;
/*  730 */     boolean milkShear = false;
/*  731 */     boolean sacrifice = false;
/*  732 */     boolean tame = false;
/*      */     
/*  734 */     boolean build = false;
/*  735 */     boolean destroyFence = false;
/*  736 */     boolean destroyItems = false;
/*  737 */     boolean pickLocks = false;
/*  738 */     boolean planBuildings = false;
/*      */     
/*  740 */     boolean cultivate = false;
/*  741 */     boolean digResource = false;
/*  742 */     boolean pack = false;
/*  743 */     boolean terraform = false;
/*      */     
/*  745 */     boolean harvestFields = false;
/*  746 */     boolean sowFields = false;
/*  747 */     boolean tendFields = false;
/*      */     
/*  749 */     boolean chopAllTrees = false;
/*  750 */     boolean chopOldTrees = false;
/*  751 */     boolean cutGrass = false;
/*  752 */     boolean harvestFruit = false;
/*  753 */     boolean makeLawn = false;
/*  754 */     boolean pickSprouts = false;
/*  755 */     boolean plantFlowers = false;
/*  756 */     boolean plantSprouts = false;
/*  757 */     boolean prune = false;
/*      */     
/*  759 */     boolean attackCitizens = false;
/*  760 */     boolean attackNonCitizens = false;
/*  761 */     boolean deitySpells = false;
/*  762 */     boolean sorcerySpells = false;
/*  763 */     boolean forageBotanize = false;
/*  764 */     boolean passGates = false;
/*  765 */     boolean placeMerchants = false;
/*  766 */     boolean pave = false;
/*  767 */     boolean meditationAbilities = false;
/*      */     
/*  769 */     boolean attachLocks = false;
/*  770 */     boolean drop = false;
/*  771 */     boolean improveRepair = false;
/*  772 */     boolean load = false;
/*  773 */     boolean pickup = false;
/*  774 */     boolean pickupPlanted = false;
/*  775 */     boolean pullPushTurn = false;
/*  776 */     boolean unload = false;
/*  777 */     boolean plantItem = false;
/*      */     
/*  779 */     boolean mineFloor = false;
/*  780 */     boolean mineIron = false;
/*  781 */     boolean mineOther = false;
/*  782 */     boolean mineRock = false;
/*  783 */     boolean surface = false;
/*  784 */     boolean tunnel = false;
/*  785 */     boolean reinforce = false;
/*      */     
/*  787 */     boolean helpAllied = false;
/*  788 */     boolean diplomat = false;
/*  789 */     boolean destroyAnyBuilding = false;
/*  790 */     boolean inviteCitizens = false;
/*  791 */     boolean manageCitizenRoles = false;
/*  792 */     boolean manageAllowedObjects = false;
/*  793 */     boolean manageGuards = false;
/*  794 */     boolean manageMap = false;
/*  795 */     boolean manageReputations = false;
/*  796 */     boolean manageRoles = false;
/*  797 */     boolean manageSettings = false;
/*  798 */     boolean configureTwitter = false;
/*  799 */     boolean resizeSettlement = false;
/*      */     
/*  801 */     if (villRole != null) {
/*      */       
/*  803 */       roleName = villRole.getName();
/*  804 */       roleDesc = "role '" + roleName + "'";
/*  805 */       role = villRole.getStatus();
/*      */       
/*  807 */       switch (role) {
/*      */         
/*      */         case 3:
/*  810 */           defaultRole = "Citizen";
/*      */           break;
/*      */         case 2:
/*  813 */           defaultRole = "Mayor";
/*      */           break;
/*      */         case 4:
/*  816 */           defaultRole = "Guard";
/*      */           break;
/*      */         case 6:
/*  819 */           defaultRole = "Wagoner";
/*      */           break;
/*      */         case 5:
/*  822 */           defaultRole = "Ally";
/*      */           break;
/*      */         case 1:
/*  825 */           defaultRole = "Non-Citizen";
/*  826 */           iCanDestroyAnyBuilding = false;
/*      */           break;
/*      */       } 
/*  829 */       hasExternalSettlement = false;
/*  830 */       externalSettlement = "n/a";
/*  831 */       if (role == 0 && villRole.getVillageAppliedTo() != 0) {
/*      */         
/*      */         try {
/*      */           
/*  835 */           Village external = Villages.getVillage(villRole.getVillageAppliedTo());
/*  836 */           externalSettlement = external.getName();
/*  837 */           hasExternalSettlement = true;
/*      */         }
/*  839 */         catch (NoSuchVillageException e) {
/*      */ 
/*      */ 
/*      */           
/*  843 */           externalSettlement = "Missing Settlement";
/*      */         } 
/*      */       }
/*  846 */       if (role == 0 && villRole.getPlayerAppliedTo() != -10L) {
/*      */         
/*  848 */         individualPlayer = PlayerInfoFactory.getPlayerName(villRole.getPlayerAppliedTo());
/*  849 */         hasIndividualPlayer = true;
/*      */       }
/*      */       else {
/*      */         
/*  853 */         hasIndividualPlayer = false;
/*  854 */         individualPlayer = "n/a";
/*      */       } 
/*      */ 
/*      */       
/*  858 */       brand = villRole.mayBrand();
/*  859 */       breed = villRole.mayBreed();
/*  860 */       butcher = villRole.mayButcher();
/*  861 */       groom = villRole.mayGroom();
/*  862 */       lead = villRole.mayLead();
/*  863 */       milkShear = villRole.mayMilkAndShear();
/*  864 */       sacrifice = villRole.maySacrifice();
/*  865 */       tame = villRole.mayTame();
/*      */       
/*  867 */       build = villRole.mayBuild();
/*  868 */       destroyAnyBuilding = villRole.mayDestroyAnyBuilding();
/*  869 */       destroyFence = villRole.mayDestroyFences();
/*  870 */       destroyItems = villRole.mayDestroyItems();
/*  871 */       pickLocks = villRole.mayPickLocks();
/*  872 */       planBuildings = villRole.mayPlanBuildings();
/*      */       
/*  874 */       cultivate = villRole.mayCultivate();
/*  875 */       digResource = villRole.mayDigResources();
/*  876 */       pack = villRole.mayPack();
/*  877 */       terraform = villRole.mayTerraform();
/*      */       
/*  879 */       harvestFields = villRole.mayHarvestFields();
/*  880 */       sowFields = villRole.maySowFields();
/*  881 */       tendFields = villRole.mayTendFields();
/*      */       
/*  883 */       chopAllTrees = villRole.mayChopDownAllTrees();
/*  884 */       chopOldTrees = villRole.mayChopDownOldTrees();
/*  885 */       cutGrass = villRole.mayCutGrass();
/*  886 */       harvestFruit = villRole.mayHarvestFruit();
/*  887 */       makeLawn = villRole.mayMakeLawn();
/*  888 */       pickSprouts = villRole.mayPickSprouts();
/*  889 */       plantFlowers = villRole.mayPlantFlowers();
/*  890 */       plantSprouts = villRole.mayPlantSprouts();
/*  891 */       prune = villRole.mayPrune();
/*      */       
/*  893 */       attackCitizens = villRole.mayAttackCitizens();
/*  894 */       attackNonCitizens = villRole.mayAttackNonCitizens();
/*  895 */       deitySpells = villRole.mayCastDeitySpells();
/*  896 */       sorcerySpells = villRole.mayCastSorcerySpells();
/*  897 */       forageBotanize = villRole.mayForageAndBotanize();
/*  898 */       passGates = villRole.mayPassGates();
/*  899 */       placeMerchants = villRole.mayPlaceMerchants();
/*  900 */       pave = villRole.mayPave();
/*  901 */       meditationAbilities = villRole.mayUseMeditationAbilities();
/*      */       
/*  903 */       attachLocks = villRole.mayAttachLock();
/*  904 */       drop = villRole.mayDrop();
/*  905 */       improveRepair = villRole.mayImproveAndRepair();
/*  906 */       load = villRole.mayLoad();
/*  907 */       pickup = villRole.mayPickup();
/*  908 */       pickupPlanted = villRole.mayPickupPlanted();
/*  909 */       pullPushTurn = villRole.mayPushPullTurn();
/*  910 */       unload = villRole.mayUnload();
/*  911 */       plantItem = villRole.mayPlantItem();
/*      */       
/*  913 */       mineFloor = villRole.mayMineFloor();
/*  914 */       mineIron = villRole.mayMineIronVeins();
/*  915 */       mineOther = villRole.mayMineOtherVeins();
/*  916 */       mineRock = villRole.mayMineRock();
/*  917 */       surface = villRole.mayMineSurface();
/*  918 */       tunnel = villRole.mayTunnel();
/*  919 */       reinforce = villRole.mayReinforce();
/*      */       
/*  921 */       helpAllied = villRole.mayPerformActionsOnAlliedDeeds();
/*  922 */       diplomat = villRole.isDiplomat();
/*  923 */       inviteCitizens = villRole.mayInviteCitizens();
/*  924 */       manageAllowedObjects = villRole.mayManageAllowedObjects();
/*  925 */       manageCitizenRoles = villRole.mayManageCitizenRoles();
/*  926 */       manageGuards = villRole.mayManageGuards();
/*  927 */       manageMap = villRole.mayManageMap();
/*  928 */       manageReputations = villRole.mayManageReputations();
/*  929 */       manageRoles = villRole.mayManageRoles();
/*  930 */       manageSettings = villRole.mayManageSettings();
/*  931 */       configureTwitter = villRole.mayConfigureTwitter();
/*  932 */       resizeSettlement = villRole.mayResizeSettlement();
/*      */       
/*  934 */       if ((role == 0 && villRole.getVillageAppliedTo() != 0) || role == 1 || role == 5 || role == 4 || role == 6) {
/*      */ 
/*      */         
/*  937 */         iCanPickLocks = false;
/*  938 */         iCanAttachLocks = false;
/*  939 */         iCanBrand = false;
/*      */       } 
/*      */     } 
/*      */     
/*  943 */     String destroyAllPopup = "";
/*  944 */     if (!Servers.localServer.PVPSERVER)
/*      */     {
/*  946 */       destroyAllPopup = ";confirm=\"The Destroy Any Buildings option enables theft\";question=\"Are sure you want this " + roleDesc + " to be able to destroy buildings since it enables theft?\"";
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  951 */     String citOnlyColour = ";color=\"127,127,255\"";
/*  952 */     String popupColour = ";color=\"255,127,127\"";
/*  953 */     boolean citTypeRole = (role == 2 || role == 3 || (role == 0 && !hasExternalSettlement && !hasIndividualPlayer) || villRole == null);
/*      */ 
/*      */     
/*  956 */     StringBuilder buf = new StringBuilder();
/*      */     
/*  958 */     buf.append("border{border{null;null;varray{rescale=\"true\";table{rows=\"2\";cols=\"3\";label{type='bold';text=\"Role Name \"};" + ((citRole != null) ? "input{maxchars=\"20\";id=\"roleName\";" : "label{") + "text=\"" + roleName + "\"}label{text=\"" + myRole1 + "\"}");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  967 */     if (hasExternalSettlement) {
/*  968 */       buf.append("label{type=\"bold\";text=\"Settlement \"};" + (
/*  969 */           (externalSettlement.length() == 0) ? "input{maxchars=\"40\";id=\"externalSettlement\";" : "label{") + "text=\"" + externalSettlement + "\"}");
/*      */     } else {
/*      */       
/*  972 */       buf.append("label{text=\" \"}label{text=\" \"}");
/*      */     } 
/*  974 */     buf.append("label{text=\"" + myRole2 + "\"}");
/*  975 */     if (hasIndividualPlayer) {
/*  976 */       buf.append("label{type=\"bold\";text=\"Player \"};" + (
/*  977 */           (individualPlayer.length() == 0) ? "input{maxchars=\"40\";id=\"individualPlayer\";" : "label{") + "text=\"" + individualPlayer + "\"}");
/*      */     } else {
/*      */       
/*  980 */       buf.append("label{text=\" \"}label{text=\" \"}");
/*      */     } 
/*      */     
/*  983 */     if (citRole != null && villRole != null && role == 0) {
/*  984 */       buf.append("harray{label{text=\"  \"};button{text=\"Remove role\";id=\"remove\";confirm=\"You are about to remove this role.\";question=\"Are you sure you want to do that?\"}}");
/*      */ 
/*      */     
/*      */     }
/*  988 */     else if (defaultRole.length() > 0) {
/*  989 */       buf.append("label{type=\"bolditalic\";text=\"This is the default " + defaultRole + " role.\"}");
/*      */     } else {
/*  991 */       buf.append("label{text=\" \"}");
/*  992 */     }  buf.append("};");
/*  993 */     buf.append("};varray{right{harray{label{text=\" \"};" + ((citRole != null || gmBack
/*      */ 
/*      */         
/*  996 */         .length() > 0) ? "button{text=\"Back\";id=\"back\"};" : "") + "label{text=\" \"};button{text=\"Close\";id=\"close\"}}}label{text=\" \"}right{harray{label{text=\" \"};" + ((citRole != null) ? "button{text=\"Save\";id=\"save\"}" : "") + "}}};null;");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1005 */     buf.append("}null;scroll{vertical=\"true\";horizontal=\"false\";varray{rescale=\"true\";passthrough{id=\"id\";text=\"" + qId + "\"}");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1011 */     String blank = "image{src=\"img.gui.bridge.blank\";size=\"" + (Servers.isThisAPvpServer() ? " 240" : "220") + ",2\";text=\"\"}";
/*      */     
/* 1013 */     buf.append("label{type=\"bold\";text=\"Animals\"}");
/* 1014 */     buf.append("table{rows=\"3\";cols=\"3\";");
/* 1015 */     buf.append(permission("brand", brand, "Brand", iCanBrand, "Allows branding of animals on deed - Only for Citizen type roles.", ";color=\"127,127,255\"", "never"));
/*      */     
/* 1017 */     buf.append(permission("breed", breed, "Breed", iCanBreed, "Allows breeding of animals on deed.", "", "always"));
/*      */     
/* 1019 */     buf.append(permission("butcher", butcher, "Butcher", iCanButcher, "Allows butchering on deed.", "", "always"));
/*      */     
/* 1021 */     buf.append(permission("groom", groom, "Groom", iCanGroom, "Allows grooming of animals on deed.", "", "always"));
/*      */     
/* 1023 */     buf.append(permission("lead", lead, "Lead", iCanLead, "Allows leading of animals on deed.", "", "always"));
/*      */     
/* 1025 */     buf.append(permission("milk", milkShear, "Milk / Shear", iCanMilkShear, "Allows milking of cows and shearing of sheep.", "", "always"));
/*      */     
/* 1027 */     buf.append(permission("sacrifice", sacrifice, "Sacrifice", iCanSacrifice, "Allows sacrificing of animals on deed.", "", "always"));
/*      */     
/* 1029 */     buf.append(permission("tame", tame, "Tame / Charm / Dominate", iCanTame, "Allows taming of animals on deed. Charming and Domination of animals requires this and 'Cast Deity Spells' privilege.", "", "always"));
/*      */     
/* 1031 */     buf.append("label{text=\"\"};");
/* 1032 */     buf.append(blank + blank + blank);
/* 1033 */     buf.append("}");
/*      */     
/* 1035 */     buf.append("label{type=\"bold\";text=\"Construction\"}");
/* 1036 */     buf.append("table{rows=\"3\";cols=\"3\";");
/* 1037 */     buf.append(permission("build", build, "Build", iCanBuild, "Allows ability to color and continue on buildings and build fences and place mine doors.", "", "noGuards"));
/*      */     
/* 1039 */     buf.append(permission("destroyAnyBuildings", destroyAnyBuilding, "Destroy Any Buildings", iCanDestroyAnyBuilding, "Allows demolition of any building, not available for non-citizen role.", ";color=\"255,127,127\"" + destroyAllPopup, "noGuards"));
/*      */     
/* 1041 */     buf.append(permission("destroyFence", destroyFence, "Destroy Fences", iCanDestroyFence, "Allows destruction of fences and gates.", "", "noGuards"));
/*      */     
/* 1043 */     buf.append(permission("destroyIteme", destroyItems, "Destroy Items", iCanDestroyItems, "Allows destruction of items.", "", "noGuards"));
/*      */     
/* 1045 */     buf.append(permission("pickLocks", pickLocks, "Pick Locks", iCanPickLocks, "Allows lockpicking of buildings, items and gates. Also requires Pickup permission to lockpick items. - Only for Citizen type roles.", ";color=\"127,127,255\"", "noGuards"));
/*      */     
/* 1047 */     buf.append(permission("planBuildings", planBuildings, "Plan Buildings", iCanPlanBuildings, "Allows planning of buildings.", "", "noGuards"));
/*      */     
/* 1049 */     buf.append(blank + blank + blank);
/* 1050 */     buf.append("}");
/*      */     
/* 1052 */     buf.append("label{type=\"bold\";text=\"Digging\"}");
/* 1053 */     buf.append("table{rows=\"3\";cols=\"3\";");
/* 1054 */     buf.append(permission("cultivate", cultivate, "Cultivate", iCanCultivate, "Allows cultivating of packed tiles.", "", "noGuards"));
/*      */     
/* 1056 */     buf.append(permission("digResource", digResource, "Dig Clay / Tar / Peat / Moss", iCanDigResource, "Allows digging of resource tiles.", "", "always"));
/*      */     
/* 1058 */     buf.append(permission("pack", pack, "Pack", iCanPack, "Allows packing of tiles.", "", "noGuards"));
/*      */     
/* 1060 */     buf.append(permission("terraform", terraform, "Terraform", iCanTerraform, "Allows terraforming (digging, levelling and flattening).", "", "both"));
/*      */     
/* 1062 */     buf.append("label{text=\"\"};");
/* 1063 */     buf.append("label{text=\"\"};");
/* 1064 */     buf.append(blank + blank + blank);
/* 1065 */     buf.append("}");
/*      */     
/* 1067 */     buf.append("label{type=\"bold\";text=\"Farming\"}");
/* 1068 */     buf.append("table{rows=\"3\";cols=\"3\";");
/* 1069 */     buf.append(permission("harvest", harvestFields, "Harvest Fields", iCanHarvestFields, "Allows harvesting of fields.", "", "always"));
/*      */     
/* 1071 */     buf.append(permission("sow", sowFields, "Sow Fields", iCanSowFields, "Allows sowing of fields.", "", "always"));
/*      */     
/* 1073 */     buf.append(permission("tend", tendFields, "Tend Fields", iCanTendFields, "Allows tending of fields.", "", "always"));
/*      */     
/* 1075 */     buf.append(blank + blank + blank);
/* 1076 */     buf.append("}");
/*      */     
/* 1078 */     buf.append("label{type=\"bold\";text=\"Forestry / Gardening\"}");
/* 1079 */     buf.append("table{rows=\"3\";cols=\"3\";");
/* 1080 */     buf.append(permission("chopAllTrees", chopAllTrees, "Chop Down Trees and Bushes", iCanChopAllTrees, "Allows chopping down of all trees and bushes except the very old and overaged trees.", "", "always"));
/*      */     
/* 1082 */     buf.append(permission("chopOldTrees", chopOldTrees, "Chop Down V.Old Trees", iCanChopOldTrees, "Allows chopping down of Very Old and Overaged trees. Also allows 'Wild Growth' spell on hedges, but that requires 'Cast Deity Spells' privilege as well.", "", "always"));
/*      */ 
/*      */     
/* 1085 */     buf.append(permission("cutGrass", cutGrass, "Cut Grass", iCanCutGrass, "Allows cutting of grass.", "", "always"));
/*      */     
/* 1087 */     buf.append(permission("harvestFruit", harvestFruit, "Harvest Fruit", iCanHarvestFruit, "Allows harvesting from trees and bushes.", "", "always"));
/*      */     
/* 1089 */     buf.append(permission("makeLawn", makeLawn, "Make Lawn", iCanMakeLawn, "Allows ability to make lawn.", "", "always"));
/*      */     
/* 1091 */     buf.append(permission("pickSprouts", pickSprouts, "Pick Sprouts", iCanPickSprouts, "Allows picking of sprouts from tress and bushes and also allows picking of flowers.", "", "always"));
/*      */     
/* 1093 */     buf.append(permission("plantFlowers", plantFlowers, "Plant Flowers", iCanPlantFlowers, "Allows planting of flowers, mixed grass and thatch.", "", "always"));
/*      */     
/* 1095 */     buf.append(permission("plantSprouts", plantSprouts, "Plant Sprouts", iCanPlantSprouts, "Allows planting of sprouts including hedges.", "", "always"));
/*      */     
/* 1097 */     buf.append(permission("prune", prune, "Prune", iCanPrune, "Allows pruning of trees and bushes including hedges.", "", "always"));
/*      */     
/* 1099 */     buf.append(blank + blank + blank);
/* 1100 */     buf.append("}");
/*      */     
/* 1102 */     buf.append("label{type=\"bold\";text=\"General\"}");
/* 1103 */     buf.append("table{rows=\"3\";cols=\"3\";");
/* 1104 */     buf.append(permission("attackCitizens", attackCitizens, "Attack Citizens", iCanAttackCitizens, "Allows attacking of citizens and allies of the settlement.", "", "always"));
/*      */     
/* 1106 */     buf.append(permission("attackNonCitizens", attackNonCitizens, "Attack Non Citizens", iCanAttackNonCitizens, "Allows attacking of non citizens which includes animals.", "", "always"));
/*      */     
/* 1108 */     buf.append(permission("deitySpells", deitySpells, "Cast Deity Spells", iCanCastDeitySpells, "Allows use of deity spells on deed.", "", "always"));
/*      */     
/* 1110 */     buf.append(permission("sorcerySpells", sorcerySpells, "Cast Sorcery Spells", iCanCastSorcerySpells, "Allows use of sorcery spells on deed.", "", "always"));
/*      */     
/* 1112 */     buf.append(permission("forage", forageBotanize, "Forage / Botanize / Investigate", iCanForageBotanize, "Allows foraging, botanizing and investigating on deed.", "", "always"));
/*      */     
/* 1114 */     buf.append(permission("passGates", passGates, "May Pass Gates", iCanPassGates, "Allows passing of gates that don't have any permissions set.", "", "never"));
/*      */     
/* 1116 */     if (Features.Feature.WAGONER.isEnabled()) {
/* 1117 */       buf.append(permission("placeMerchants", placeMerchants, "May Place NPCs", iCanPlaceMerchants, "Allows placing of Merchants, Traders and Wagoners (citizen only).", "", "never"));
/*      */     } else {
/*      */       
/* 1120 */       buf.append(permission("placeMerchants", placeMerchants, "May Place Merchants", iCanPlaceMerchants, "Allows placing of Merchants and Traders.", "", "never"));
/*      */     } 
/* 1122 */     buf.append(permission("pave", pave, "Pave", iCanPave, "Allows adding or removing of paving on deed.", "", "noGuards"));
/*      */     
/* 1124 */     buf.append(permission("meditationAbilities", meditationAbilities, "Use Meditation Abilities", iCanUseMeditationAbilities, "Allows use of meditation abilities on deed.", "", "always"));
/*      */ 
/*      */     
/* 1127 */     buf.append(blank + blank + blank);
/* 1128 */     buf.append("}");
/*      */     
/* 1130 */     buf.append("label{type=\"bold\";text=\"Item Management\"}");
/* 1131 */     buf.append("table{rows=\"3\";cols=\"3\";");
/* 1132 */     buf.append(permission("attachLocks", attachLocks, "Attach Locks", iCanAttachLocks, "Allows attaching locks to items on deed - Only for Citizen type roles.", ";color=\"127,127,255\"", "noGuards"));
/*      */     
/* 1134 */     buf.append(permission("drop", drop, "Drop", iCanDrop, "Allows dropping of items on deed.", "", "always"));
/*      */     
/* 1136 */     buf.append(permission("improve", improveRepair, "Improve / Repair", iCanImproveRepair, "Allows improving and repairing of items on deed.", "", "always"));
/*      */     
/* 1138 */     buf.append(permission("load", load, "Load", iCanLoad, "Allows loading of items on deed, also requires 'Pickup' permission, or 'Pickup Planted' permission.", "", "noGuards"));
/*      */ 
/*      */     
/* 1141 */     buf.append(permission("pickup", pickup, "Pickup", iCanPickup, "Allows picking up of items on deed.", "", "always"));
/*      */     
/* 1143 */     buf.append(permission("pickupPlanted", pickupPlanted, "Pickup Planted Items", iCanPickupPlanted, "Allows picking up of planted items on deed, also requires 'Pickup' permission.", "", "noGuards"));
/*      */     
/* 1145 */     buf.append(permission("plantItem", plantItem, "Plant Item", iCanPlantItem, "Allows planting and securing of items on deed.", "", "noGuards"));
/*      */     
/* 1147 */     buf.append(permission("pullPushTurn", pullPushTurn, "Pull / Push / Turn", iCanPullPushTurn, "Allows pull, push and turning of items on deed.", "", "noGuards"));
/*      */     
/* 1149 */     buf.append(permission("unload", unload, "UnLoad", iCanUnload, "Allows unloading of items on deed.", "", "noGuards"));
/*      */ 
/*      */     
/* 1152 */     buf.append(blank + blank + blank);
/* 1153 */     buf.append("}");
/*      */     
/* 1155 */     buf.append("label{type=\"bold\";text=\"Mining\"}");
/* 1156 */     buf.append("table{rows=\"3\";cols=\"3\";");
/* 1157 */     buf.append(permission("reinforce", reinforce, "Add/Remove Reinforcement", iCanReinforce, "Allows adding and removing of reinforced walls (needs Mine Rock) and floors (needs Mine Floor) on deed. Also used to control Disintegrate.", "", "never"));
/*      */ 
/*      */     
/* 1160 */     buf.append(permission("mineFloor", mineFloor, "Mine Floor", iCanMineFloor, "Allows mining of floors and ceilings, including levelling and flattening in caves on deed.", "", "noGuards"));
/*      */     
/* 1162 */     buf.append(permission("mineIron", mineIron, "Mine Iron Veins", iCanMineIron, "Allows mining of Iron veins on deed.", "", "noGuards"));
/*      */     
/* 1164 */     buf.append(permission("mineOther", mineOther, "Mine Other Veins", iCanMineOther, "Allows mining of other veins on deed.", "", "noGuards"));
/*      */     
/* 1166 */     buf.append(permission("mineRock", mineRock, "Mine Rock", iCanMineRock, "Allows mining of rock in caves on deed.", "", "noGuards"));
/*      */     
/* 1168 */     buf.append(permission("surface", surface, "Surface Mining", iCanSurface, "Allows surface mining on deed.", "", "noGuards"));
/*      */     
/* 1170 */     buf.append(permission("tunnel", tunnel, "Tunnel", iCanTunnel, "Allows making tunnel entrances on deed.", "", "noGuards"));
/*      */     
/* 1172 */     buf.append("label{text=\"\"}");
/* 1173 */     buf.append("label{text=\"\"}");
/* 1174 */     buf.append(blank + blank + blank);
/* 1175 */     buf.append("}");
/*      */     
/* 1177 */     if (citTypeRole) {
/*      */       
/* 1179 */       buf.append("harray{label{;color=\"127,127,255\"type=\"bold\";text=\"Settlement Management\"}");
/* 1180 */       if (villRole == null)
/* 1181 */         buf.append("label{;color=\"127,127,255\"text=\" - Only for Citizen type roles.\"}"); 
/* 1182 */       buf.append("}");
/* 1183 */       buf.append("table{rows=\"3\";cols=\"3\";");
/* 1184 */       buf.append(permission("helpAllied", helpAllied, "Allow Actions on Allied Deeds", iCanHelpAllied, "Allows Actions on allied deeds using their ally role, if not set then player will use their non-citizen role.", ";color=\"127,127,255\"", ""));
/*      */ 
/*      */       
/* 1187 */       buf.append(permission("inviteCitizens", inviteCitizens, "Invite Citizens", iCanInviteCitizens, "Allows ability to invite non-citizens to join the settlement including using recruitment boards.", ";color=\"127,127,255\"", ""));
/*      */ 
/*      */       
/* 1190 */       buf.append(permission("manageAllowedObjects", manageAllowedObjects, "Manage Allowed Objects", iCanManageAllowedObjects, "Allows ability to manage allowed objects, that's buildings, gates and mine doors that have the 'Settlement May Manage' flag set.", ";color=\"127,127,255\"", ""));
/*      */ 
/*      */       
/* 1193 */       buf.append(permission("manageCitizenRoles", manageCitizenRoles, "Manage Citizen Roles", iCanManageCitizenRoles, "Allows ability to assign roles to citizens.", ";color=\"127,127,255\"", ""));
/*      */       
/* 1195 */       buf.append(permission("manageGuards", manageGuards, "Manage Guards", iCanManageGuards, "Allows hiring guards to protect the settlement and uphold these rules.", ";color=\"127,127,255\"", ""));
/*      */       
/* 1197 */       buf.append(permission("manageMap", manageMap, "Manage Map", iCanManageMap, "Allows ability to add and remove village annotations on the map. Updating alliance annotations requires the manage politics permission as well.", ";color=\"127,127,255\"", ""));
/*      */ 
/*      */       
/* 1200 */       buf.append(permission("diplomat", diplomat, "Manage Politics", iCanDiplomat, "Allows ability to form and break alliances with other settlements, as well as declare war and make peace.", ";color=\"127,127,255\"", ""));
/*      */ 
/*      */       
/* 1203 */       buf.append(permission("manageReputations", manageReputations, "Manage Reputations", iCanManageReputations, "Allows ability to manage reputations.", ";color=\"127,127,255\"", ""));
/*      */       
/* 1205 */       buf.append(permission("manageRoles", manageRoles, "Manage Roles", iCanManageRoles, "Allows managing of permissions in any role in which you have the corresponding permission in your role.", ";color=\"127,127,255\"", ""));
/*      */ 
/*      */       
/* 1208 */       buf.append(permission("manageSettings", manageSettings, "Manage Settings", iCanManageSettings, "Allows changing settlement settings.", ";color=\"127,127,255\"", ""));
/*      */       
/* 1210 */       buf.append(permission("twitter", configureTwitter, "May Configure Twitter", iCanConfigureTwitter, "Allows ability to configure twitter settings.", ";color=\"127,127,255\"", ""));
/*      */       
/* 1212 */       buf.append(permission("resize", resizeSettlement, "Resize Settlement", iCanResizeSettlement, "Allows ability to change the size of the settlement.", ";color=\"127,127,255\"", ""));
/*      */ 
/*      */ 
/*      */       
/* 1216 */       buf.append(blank + blank + blank);
/* 1217 */       buf.append("}");
/*      */     } 
/*      */     
/* 1220 */     buf.append("}};null;null;null;null;}");
/* 1221 */     performer.getCommunicator().sendBml(560, 400, true, true, buf.toString(), 200, 200, 200, "Role Management");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static String permission(String sid, boolean selected, String text, boolean enabled, String hover, String extra, String enemyIcon) {
/* 1227 */     String icon = "";
/*      */     
/* 1229 */     if (Servers.isThisAPvpServer() && enemyIcon.length() != 0)
/*      */     {
/* 1231 */       switch (enemyIcon) {
/*      */         
/*      */         case "always":
/* 1234 */           icon = "image{src=\"img.gui.circle.green\";size=\"16,16\";text=\"Should always be doable by enemy.\"}";
/*      */           break;
/*      */         case "never":
/* 1237 */           icon = "image{src=\"img.gui.circle.red\";size=\"16,16\";text=\"Should never be doable by enemy.\"}";
/*      */           break;
/*      */         case "both":
/* 1240 */           icon = "image{src=\"img.gui.circle.both\";size=\"16,16\";text=\"Should only be doable by enemy if guards are dead. Except dig which is always doable.\"}";
/*      */           break;
/*      */         case "noGuards":
/* 1243 */           icon = "image{src=\"img.gui.circle.yellow\";size=\"16,16\";text=\"Should only be doable by enemy if guards are dead.\"}";
/*      */           break;
/*      */       } 
/*      */     }
/* 1247 */     if (enabled) {
/* 1248 */       return "harray{checkbox{id=\"" + sid + "\";selected=\"" + selected + "\";text=\"" + text + "  \"" + extra + ";hover=\"" + hover + "\"};" + icon + "};";
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1256 */     return "harray{image{src=\"img.gui." + (selected ? "vsmall" : "xsmall") + "\";size=\"16,16\";text=\"" + hover + "\"}label{text=\"" + text + "\"" + extra + ";hover=\"" + hover + "\"};" + icon + "};";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void roleList(Village village) {
/* 1266 */     VillageRole[] roles = village.getRoles();
/* 1267 */     Arrays.sort((Object[])roles);
/* 1268 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 1270 */     if (this.gmBack.length() == 0) {
/*      */       
/* 1272 */       String buttons = "button{text=\"Close\";id=\"close\"}";
/* 1273 */       if (roles.length < 50) {
/* 1274 */         buttons = "button{text=\"Add role\";id=\"add\"};label{text=\" \"};" + buttons;
/*      */       }
/* 1276 */       buf.append("border{border{null;null;label{type='bold';text=\"Manage Roles for " + village
/*      */ 
/*      */ 
/*      */           
/* 1280 */           .getName() + "\"};harray{" + buttons + "};harray{button{text=\"Reset all roles\";id=\"reset\";confirm=\"You are about to reset all the roles.\";question=\"Are you sure you want to do that?\"};label{text=\"Everyone except the mayor will have the Citizen role.\"}}}null;scroll{vertical=\"true\";horizontal=\"false\";varray{rescale=\"true\";passthrough{id=\"id\";text=\"" + 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1288 */           getId() + "\"}");
/*      */       
/* 1290 */       if (village.isDisbanding()) {
/*      */         
/* 1292 */         long timeleft = village.getDisbanding() - System.currentTimeMillis();
/* 1293 */         String times = Server.getTimeFor(timeleft);
/* 1294 */         buf.append("text{type=\"bold\";text=\"This settlement is disbanding\"}");
/*      */         
/* 1296 */         if (timeleft > 0L) {
/* 1297 */           buf.append("text{type=\"bold\";text=\"Eta: " + times + ".\"};text{text=\"\"};");
/*      */         } else {
/* 1299 */           buf.append("text{type=\"bold\";text=\"Eta:  any minute now.\"};text{text=\"\"};");
/*      */         } 
/* 1301 */       }  buf.append("text{text=\"\"}");
/* 1302 */       buf.append("table{rows=\"3\";cols=\"3\";");
/* 1303 */       boolean hasGuardRole = false;
/* 1304 */       boolean hasWagonerRole = false; int x;
/* 1305 */       for (x = 0; x < roles.length; x++) {
/*      */         
/* 1307 */         if (roles[x].getStatus() == 2) {
/*      */           
/* 1309 */           buf.append("harray{button{text=\"Change Name\";id=\"change" + roles[x].getId() + "\"};label{text=\" \"}}");
/* 1310 */           buf.append("input{maxchars=\"20\";id=\"name\";text=\"" + roles[x]
/* 1311 */               .getName() + "\";onenter=\"change" + roles[x]
/* 1312 */               .getId() + "\"};label{text=\"Default Mayor Role\"}");
/*      */         } 
/*      */         
/* 1315 */         if (roles[x].getStatus() == 4)
/* 1316 */           hasGuardRole = true; 
/* 1317 */         if (roles[x].getStatus() == 6)
/* 1318 */           hasWagonerRole = true; 
/*      */       } 
/* 1320 */       if (hasGuardRole) {
/*      */         
/* 1322 */         buf.append("label{text=\" \"}");
/* 1323 */         buf.append("label{text=\"Guard\"}");
/* 1324 */         buf.append("label{text=\"Default Guard Role\"}");
/*      */       } 
/* 1326 */       for (x = 0; x < roles.length; x++) {
/*      */         
/* 1328 */         if (roles[x].getStatus() != 2 && roles[x].getStatus() != 4 && roles[x].getStatus() != 6) {
/*      */           
/* 1330 */           buf.append("right{harray{button{text=\"Edit\";id=\"edit" + roles[x].getId() + "\"};label{text=\" \"}}}");
/* 1331 */           buf.append("label{text=\"" + roles[x].getName() + "\"}");
/* 1332 */           switch (roles[x].getStatus()) {
/*      */             
/*      */             case 1:
/* 1335 */               buf.append("label{text=\"Default Non-Citizen Role\"}");
/*      */               break;
/*      */             case 3:
/* 1338 */               buf.append("label{text=\"Default Citizen Role\"}");
/*      */               break;
/*      */             case 5:
/* 1341 */               buf.append("label{text=\"Default Allied Role\"}");
/*      */               break;
/*      */             default:
/* 1344 */               if (roles[x].getVillageAppliedTo() > 0) {
/* 1345 */                 buf.append("label{text=\"External Village\"}"); break;
/* 1346 */               }  if (roles[x].getPlayerAppliedTo() != -10L) {
/* 1347 */                 buf.append("label{text=\"Individual Player\"}"); break;
/*      */               } 
/* 1349 */               buf.append("label{text=\"\"}"); break;
/*      */           } 
/*      */         } 
/*      */       } 
/* 1353 */       if (hasWagonerRole) {
/*      */         
/* 1355 */         buf.append("label{text=\" \"}");
/* 1356 */         buf.append("label{text=\"Wagoner\"}");
/* 1357 */         buf.append("label{text=\"Default Wagoner Role\"}");
/*      */       } 
/* 1359 */       buf.append("}");
/* 1360 */       buf.append("text{text=\"\"}");
/* 1361 */       buf.append("text{text=\"If the checkbox is checked, it means the role WILL BE able to perform the task.\"}");
/* 1362 */       if (Servers.isThisAPvpServer())
/* 1363 */         buf.append("text{type=\"bold\";text=\"Note that you will need at least 1 guard to enforce these rules on deed!\"}"); 
/* 1364 */       buf.append("text{text=\"If you want to change the name, simply type another name there.\"}");
/* 1365 */       buf.append("text{text=\" \"}");
/* 1366 */       buf.append("text{type=\"bold\";text=\"Note! The role name may contain the following letters: \"}");
/* 1367 */       buf.append("text{text=\"a-z,A-Z,', and -\"}text{text=\"\"}");
/* 1368 */       buf.append("text{text=\"The non-citizens role means those who are not citizens. Normally they shouldn't be able to do anything at all.\"}");
/*      */       
/* 1370 */       buf.append("text{text=\"The citizen role is the default role people get when they become citizens.\"}");
/* 1371 */       buf.append("text{text=\"\"}");
/*      */       
/* 1373 */       buf.append("}};null;null;null;null;}");
/* 1374 */       getResponder().getCommunicator().sendBml(450, 400, true, true, buf.toString(), 200, 200, 200, this.title);
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */       
/* 1380 */       buf.append("border{border{null;null;label{type='bold';text=\"Show Roles for " + village
/*      */ 
/*      */ 
/*      */           
/* 1384 */           .getName() + "\"};harray{button{text=\"Close\";id=\"close\"}};harray{button{text=\"Back to GM Tool\";id=\"GMTool\"}};}null;scroll{vertical=\"true\";horizontal=\"false\";varray{rescale=\"true\";passthrough{id=\"id\";text=\"" + 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1389 */           getId() + "\"}");
/*      */       
/* 1391 */       if (village.isDisbanding()) {
/*      */         
/* 1393 */         long timeleft = village.getDisbanding() - System.currentTimeMillis();
/* 1394 */         String times = Server.getTimeFor(timeleft);
/* 1395 */         buf.append("text{type=\"bold\";text=\"This settlement is disbanding\"}");
/*      */         
/* 1397 */         if (timeleft > 0L) {
/* 1398 */           buf.append("text{type=\"bold\";text=\"Eta: " + times + ".\"};text{text=\"\"};");
/*      */         } else {
/* 1400 */           buf.append("text{type=\"bold\";text=\"Eta:  any minute now.\"};text{text=\"\"};");
/*      */         } 
/* 1402 */       }  buf.append("text{text=\"\"}");
/* 1403 */       buf.append("table{rows=\"3\";cols=\"3\";");
/* 1404 */       int guardRoleId = -1;
/* 1405 */       int wagonerRoleId = -1; int x;
/* 1406 */       for (x = 0; x < roles.length; x++) {
/*      */         
/* 1408 */         if (roles[x].getStatus() == 2) {
/*      */           
/* 1410 */           buf.append("harray{button{text=\"Show\";id=\"show" + roles[x].getId() + "\"};label{text=\" \"}}");
/* 1411 */           buf.append("label{text=\"" + roles[x].getName() + "\"}label{text=\"Default Mayor Role\"}");
/*      */         } 
/*      */         
/* 1414 */         if (roles[x].getStatus() == 4)
/* 1415 */           guardRoleId = roles[x].getId(); 
/* 1416 */         if (roles[x].getStatus() == 6)
/* 1417 */           wagonerRoleId = roles[x].getId(); 
/*      */       } 
/* 1419 */       if (guardRoleId > -1) {
/*      */         
/* 1421 */         buf.append("harray{button{text=\"Show\";id=\"show" + guardRoleId + "\"};label{text=\" \"}}");
/* 1422 */         buf.append("label{text=\"Guard\"}label{text=\"Default Guard Role\"}");
/*      */       } 
/*      */       
/* 1425 */       for (x = 0; x < roles.length; x++) {
/*      */         
/* 1427 */         if (roles[x].getStatus() != 2 && roles[x].getStatus() != 4 && roles[x].getStatus() != 6) {
/*      */           
/* 1429 */           buf.append("right{harray{button{text=\"Show\";id=\"show" + roles[x].getId() + "\"};label{text=\" \"}}}");
/* 1430 */           buf.append("label{text=\"" + roles[x].getName() + "\"}");
/* 1431 */           switch (roles[x].getStatus()) {
/*      */             
/*      */             case 1:
/* 1434 */               buf.append("label{text=\"Default Non-Citizen Role\"}");
/*      */               break;
/*      */             case 3:
/* 1437 */               buf.append("label{text=\"Default Citizen Role\"}");
/*      */               break;
/*      */             case 5:
/* 1440 */               buf.append("label{text=\"Default Allied Role\"}");
/*      */               break;
/*      */             default:
/* 1443 */               if (roles[x].getVillageAppliedTo() > 0) {
/* 1444 */                 buf.append("label{text=\"External Village\"}"); break;
/* 1445 */               }  if (roles[x].getPlayerAppliedTo() != -10L) {
/* 1446 */                 buf.append("label{text=\"Individual Player\"}"); break;
/*      */               } 
/* 1448 */               buf.append("label{text=\"\"}"); break;
/*      */           } 
/*      */         } 
/*      */       } 
/* 1452 */       if (wagonerRoleId > -1) {
/*      */         
/* 1454 */         buf.append("harray{button{text=\"Show\";id=\"show" + wagonerRoleId + "\"};label{text=\" \"}}");
/* 1455 */         buf.append("label{text=\"Wagoner\"}label{text=\"Default Wagoner Role\"}");
/*      */       } 
/*      */       
/* 1458 */       buf.append("}");
/* 1459 */       buf.append("text{text=\"\"}");
/* 1460 */       buf.append("}};null;null;null;null;}");
/* 1461 */       getResponder().getCommunicator().sendBml(250, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void roleMatrix(Village village) {
/* 1467 */     VillageRole[] roles = village.getRoles();
/* 1468 */     Arrays.sort((Object[])roles);
/* 1469 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 1471 */     buf.append(getBmlHeaderWithScroll());
/* 1472 */     if (village.isDisbanding()) {
/*      */       
/* 1474 */       long timeleft = village.getDisbanding() - System.currentTimeMillis();
/* 1475 */       String times = Server.getTimeFor(timeleft);
/* 1476 */       buf.append("text{type=\"bold\";text=\"This settlement is disbanding\"}");
/*      */       
/* 1478 */       if (timeleft > 0L) {
/* 1479 */         buf.append("text{type=\"bold\";text=\"Eta: " + times + ".\"};text{text=\"\"};");
/*      */       } else {
/* 1481 */         buf.append("text{type=\"bold\";text=\"Eta:  any minute now.\"};text{text=\"\"};");
/*      */       } 
/* 1483 */     }  buf.append("text{type=\"bold\";text=\"Set the permissions for the different roles.\"}");
/* 1484 */     buf.append("text{text=\"If the checkbox is checked, it means the role WILL BE able to perform the task.\"}");
/* 1485 */     if (Servers.isThisAPvpServer())
/* 1486 */       buf.append("text{type=\"bold\";text=\"Note that you will need at least 1 guard to enforce these rules on deed!\"}"); 
/* 1487 */     buf.append("text{text=\"If you want to change the name, simply type another name there.\"}text{text=\" \"}");
/* 1488 */     buf.append("text{type=\"bold\";text=\"Note! The role name may contain the following letters: \"}");
/* 1489 */     buf.append("text{text=\"a-z,A-Z,', and -\"}text{text=\"\"}");
/* 1490 */     buf.append("text{text=\"The non-citizens role means those who are not citizens. Normally they shouldn't be able to do anything at all.\"}");
/* 1491 */     buf.append("text{text=\"The citizen role is the default role people get when they become citizens.\"}");
/*      */     
/* 1493 */     buf.append("checkbox{id=\"resetall\";selected=\"false\";text=\"Check this box to reset all roles. Everyone except the mayor will have the Citizen role. Any other changes made below will be discarded.\"};text{text=\"\"}");
/*      */     
/* 1495 */     boolean hasGuardRole = false;
/* 1496 */     for (int x = 0; x < roles.length; x++) {
/*      */       
/* 1498 */       if (roles[x].getStatus() == 2)
/* 1499 */         buf.append("harray{label{text=\"Change mayor role name here \"};input{maxchars=\"20\";id=\"" + roles[x].getId() + "name\"; text=\"" + roles[x].getName() + "\"}}"); 
/* 1500 */       if (roles[x].getStatus() == 4) {
/* 1501 */         hasGuardRole = true;
/*      */       }
/*      */     } 
/* 1504 */     if (roles.length < 50) {
/* 1505 */       buf.append("text{text=\"If you want to create a new role, just change the name 'newrole' and set the appropriate values.\"}");
/*      */     } else {
/* 1507 */       buf.append("text{text=\"You have reached the maximum number of roles for your settlement, which is 50. Delete one if you need more.\"}");
/*      */     } 
/* 1509 */     buf.append("text{text=\"\"}");
/*      */ 
/*      */     
/* 1512 */     int cols = roles.length + ((roles.length < 50) ? 1 : 0) + (hasGuardRole ? 0 : 1);
/* 1513 */     buf.append("table{rows=\"21\";cols=\"" + cols + "\";");
/*      */     
/* 1515 */     buf.append("label{maxchars=\"20\";text=\"Role name\"};"); int i;
/* 1516 */     for (i = 0; i < roles.length; i++) {
/* 1517 */       if (roles[i].getStatus() != 2 && roles[i].getStatus() != 4)
/* 1518 */         buf.append("harray{input{maxchars=\"20\";id=\"" + roles[i].getId() + "name\"; text=\"" + roles[i].getName() + "\"};label{text=\" \"}}"); 
/* 1519 */     }  if (roles.length < 50)
/* 1520 */       buf.append("input{maxchars=\"20\";id=\"newname\";text=\"newrole\"}"); 
/* 1521 */     buf.append("label{text=\"Name of the role\"}");
/*      */     
/* 1523 */     buf.append("label{text=\"Build\"};");
/* 1524 */     for (i = 0; i < roles.length; i++) {
/*      */       
/* 1526 */       if (roles[i].getStatus() != 2 && roles[i].getStatus() != 4)
/* 1527 */         buf.append("checkbox{id=\"" + roles[i].getId() + "build\"" + (
/* 1528 */             roles[i].mayBuild() ? ";selected='true'" : "") + "}"); 
/*      */     } 
/* 1530 */     if (roles.length < 50)
/* 1531 */       buf.append("checkbox{id=\"newbuild\"}"); 
/* 1532 */     buf.append("label{text=\"Allows ability to plan, color and continue on buildings and fences.\"}");
/*      */     
/* 1534 */     buf.append("label{text=\"Forestry\"};");
/* 1535 */     for (i = 0; i < roles.length; i++) {
/*      */       
/* 1537 */       if (roles[i].getStatus() != 2 && roles[i].getStatus() != 4)
/* 1538 */         buf.append("checkbox{id=\"" + roles[i].getId() + "cut\"" + (
/* 1539 */             roles[i].mayCuttrees() ? ";selected='true'" : "") + "}"); 
/*      */     } 
/* 1541 */     if (roles.length < 50)
/* 1542 */       buf.append("checkbox{id=\"newcut\"}"); 
/* 1543 */     buf.append("label{text=\"Allows cutting down young trees, picking sprouts and planting new trees.\"}");
/*      */     
/* 1545 */     buf.append("label{text=\"Cut old trees\"};");
/* 1546 */     for (i = 0; i < roles.length; i++) {
/*      */       
/* 1548 */       if (roles[i].getStatus() != 2 && roles[i].getStatus() != 4)
/* 1549 */         buf.append("checkbox{id=\"" + roles[i].getId() + "cutold\"" + (
/* 1550 */             roles[i].mayCutOldTrees() ? ";selected='true'" : "") + "}"); 
/*      */     } 
/* 1552 */     if (roles.length < 50)
/* 1553 */       buf.append("checkbox{id=\"newcutold\"}"); 
/* 1554 */     buf.append("label{text=\"Allows cutting down old trees.\"}");
/*      */     
/* 1556 */     buf.append("label{text=\"Destroy buildings\"};");
/* 1557 */     for (i = 0; i < roles.length; i++) {
/*      */       
/* 1559 */       if (roles[i].getStatus() != 2 && roles[i].getStatus() != 4)
/* 1560 */         buf.append("checkbox{id=\"" + roles[i].getId() + "dest\"" + (
/* 1561 */             roles[i].mayDestroy() ? ";selected='true'" : "") + "}"); 
/*      */     } 
/* 1563 */     if (roles.length < 50)
/* 1564 */       buf.append("checkbox{id=\"newdest\"}"); 
/* 1565 */     buf.append("label{text=\"Allows destroying of building and fence plans as well as finished buildings and plans. Also allows lockpicking of structures and removing lamp posts and signs.\"}");
/*      */     
/* 1567 */     buf.append("label{text=\"Farm\"};");
/* 1568 */     for (i = 0; i < roles.length; i++) {
/*      */       
/* 1570 */       if (roles[i].getStatus() != 2 && roles[i].getStatus() != 4)
/* 1571 */         buf.append("checkbox{id=\"" + roles[i].getId() + "farm\"" + (
/* 1572 */             roles[i].mayFarm() ? ";selected='true'" : "") + "}"); 
/*      */     } 
/* 1574 */     if (roles.length < 50)
/* 1575 */       buf.append("checkbox{id=\"newfarm\"}"); 
/* 1576 */     buf.append("label{text=\"Allows sowing, farming and harvesting farms. Also milking creatures.\"}");
/*      */     
/* 1578 */     buf.append("label{text=\"Hire guards\"};");
/* 1579 */     for (i = 0; i < roles.length; i++) {
/*      */       
/* 1581 */       if (roles[i].getStatus() != 2 && roles[i].getStatus() != 4)
/* 1582 */         buf.append("checkbox{id=\"" + roles[i].getId() + "hire\"" + (
/* 1583 */             roles[i].mayHire() ? ";selected='true'" : "") + "}"); 
/*      */     } 
/* 1585 */     if (roles.length < 50)
/* 1586 */       buf.append("checkbox{id=\"newhire\"}"); 
/* 1587 */     buf.append("label{text=\"Allows hiring guards to protect the settlement and uphold these rules.\"}");
/*      */     
/* 1589 */     buf.append("label{text=\"Citizens\"};");
/* 1590 */     for (i = 0; i < roles.length; i++) {
/*      */       
/* 1592 */       if (roles[i].getStatus() != 2 && roles[i].getStatus() != 4)
/* 1593 */         buf.append("checkbox{id=\"" + roles[i].getId() + "inv\"" + (
/* 1594 */             roles[i].mayInviteCitizens() ? ";selected='true'" : "") + "}"); 
/*      */     } 
/* 1596 */     if (roles.length < 50)
/* 1597 */       buf.append("checkbox{id=\"newinv\"}"); 
/* 1598 */     buf.append("label{text=\"Allows ability to invite non-citizens to join the settlement.\"}");
/*      */     
/* 1600 */     buf.append("label{text=\"Manage roles\"};");
/* 1601 */     for (i = 0; i < roles.length; i++) {
/*      */       
/* 1603 */       if (roles[i].getStatus() != 2 && roles[i].getStatus() != 4)
/* 1604 */         buf.append("checkbox{id=\"" + roles[i].getId() + "man\"" + (
/* 1605 */             roles[i].mayManageRoles() ? ";selected='true'" : "") + "}"); 
/*      */     } 
/* 1607 */     if (roles.length < 50)
/* 1608 */       buf.append("checkbox{id=\"newman\"}"); 
/* 1609 */     buf.append("label{text=\"Allows adding to and modifying these roles via this interface, as well as changing settings and setting reputations and disbanding the settlement.\"}");
/*      */     
/* 1611 */     buf.append("label{text=\"Mine\"};");
/* 1612 */     for (i = 0; i < roles.length; i++) {
/*      */       
/* 1614 */       if (roles[i].getStatus() != 2 && roles[i].getStatus() != 4)
/* 1615 */         buf.append("checkbox{id=\"" + roles[i].getId() + "mine\"" + (
/* 1616 */             roles[i].mayMine() ? ";selected='true'" : "") + "}"); 
/*      */     } 
/* 1618 */     if (roles.length < 50)
/* 1619 */       buf.append("checkbox{id=\"newmine\"}"); 
/* 1620 */     buf.append("label{text=\"Allows mining in general.\"}");
/*      */     
/* 1622 */     buf.append("label{text=\"Terraform\"};");
/* 1623 */     for (i = 0; i < roles.length; i++) {
/*      */       
/* 1625 */       if (roles[i].getStatus() != 2 && roles[i].getStatus() != 4)
/* 1626 */         buf.append("checkbox{id=\"" + roles[i].getId() + "terra\"" + (
/* 1627 */             roles[i].mayTerraform() ? ";selected='true'" : "") + "}"); 
/*      */     } 
/* 1629 */     if (roles.length < 50)
/* 1630 */       buf.append("checkbox{id=\"newterra\"}"); 
/* 1631 */     buf.append("label{text=\"Allows ability to dig, pack, pave, level and flatten.\"}");
/*      */     
/* 1633 */     buf.append("label{text=\"Expand\"};");
/* 1634 */     for (i = 0; i < roles.length; i++) {
/*      */       
/* 1636 */       if (roles[i].getStatus() != 2 && roles[i].getStatus() != 4)
/* 1637 */         buf.append("checkbox{id=\"" + roles[i].getId() + "expa\"" + (
/* 1638 */             roles[i].mayExpand() ? ";selected='true'" : "") + "}"); 
/*      */     } 
/* 1640 */     if (roles.length < 50)
/* 1641 */       buf.append("checkbox{id=\"newexpa\"}"); 
/* 1642 */     buf.append("label{text=\"Allows ability to expand the size of the settlement.\"}");
/*      */     
/* 1644 */     buf.append("label{text=\"Pass gates\"};");
/* 1645 */     for (i = 0; i < roles.length; i++) {
/*      */       
/* 1647 */       if (roles[i].getStatus() != 2 && roles[i].getStatus() != 4) {
/* 1648 */         buf.append("checkbox{id=\"" + roles[i].getId() + "pass\"" + ";selected='true'" + "}");
/*      */       }
/*      */     } 
/* 1651 */     if (roles.length < 50)
/* 1652 */       buf.append("checkbox{id=\"newpass\"}"); 
/* 1653 */     buf.append("label{text=\"Allows passing through all gates, locked or not.\"}");
/*      */     
/* 1655 */     buf.append("label{text=\"Lock gates\"};");
/* 1656 */     for (i = 0; i < roles.length; i++) {
/*      */       
/* 1658 */       if (roles[i].getStatus() != 2 && roles[i].getStatus() != 4)
/* 1659 */         buf.append("checkbox{id=\"" + roles[i].getId() + "lock\"" + (
/* 1660 */             roles[i].mayLockFences() ? ";selected='true'" : "") + "}"); 
/*      */     } 
/* 1662 */     if (roles.length < 50)
/* 1663 */       buf.append("checkbox{id=\"newlock\"}"); 
/* 1664 */     buf.append("label{text=\"Allows ability to unlock and relock gates inside the settlement.\"}");
/*      */     
/* 1666 */     buf.append("label{text=\"Attack citizens\"};");
/* 1667 */     for (i = 0; i < roles.length; i++) {
/*      */       
/* 1669 */       if (roles[i].getStatus() != 2 && roles[i].getStatus() != 4)
/* 1670 */         buf.append("checkbox{id=\"" + roles[i].getId() + "attcitiz\"" + (
/* 1671 */             roles[i].mayAttackCitizens() ? ";selected='true'" : "") + "}"); 
/*      */     } 
/* 1673 */     if (roles.length < 50)
/* 1674 */       buf.append("checkbox{id=\"newattcitiz\"}"); 
/* 1675 */     buf.append("label{text=\"Allows ability to attack citizens and allies of the settlement.\"}");
/*      */     
/* 1677 */     buf.append("label{text=\"Attack non-citizens\"};");
/* 1678 */     for (i = 0; i < roles.length; i++) {
/*      */       
/* 1680 */       if (roles[i].getStatus() != 2 && roles[i].getStatus() != 4)
/* 1681 */         buf.append("checkbox{id=\"" + roles[i].getId() + "attnoncitiz\"" + (
/* 1682 */             roles[i].mayAttackNonCitizens() ? ";selected='true'" : "") + "}"); 
/*      */     } 
/* 1684 */     if (roles.length < 50)
/* 1685 */       buf.append("checkbox{id=\"newattnoncitiz\"}"); 
/* 1686 */     buf.append("label{text=\"Allows ability to attack non citizens.\"}");
/*      */     
/* 1688 */     buf.append("label{text=\"Fish\"};");
/* 1689 */     for (i = 0; i < roles.length; i++) {
/*      */       
/* 1691 */       if (roles[i].getStatus() != 2 && roles[i].getStatus() != 4)
/* 1692 */         buf.append("checkbox{id=\"" + roles[i].getId() + "fish\"" + (
/* 1693 */             roles[i].mayFish() ? ";selected='true'" : "") + "}"); 
/*      */     } 
/* 1695 */     if (roles.length < 50)
/* 1696 */       buf.append("checkbox{id=\"newfish\"}"); 
/* 1697 */     buf.append("label{text=\"Allows ability to fish on deed.\"}");
/*      */     
/* 1699 */     buf.append("label{text=\"Push,Pull,Turn\"};");
/* 1700 */     for (i = 0; i < roles.length; i++) {
/*      */       
/* 1702 */       if (roles[i].getStatus() != 2 && roles[i].getStatus() != 4)
/* 1703 */         buf.append("checkbox{id=\"" + roles[i].getId() + "pushpullturn\"" + (
/* 1704 */             roles[i].mayPushPullTurn() ? ";selected='true'" : "") + "}"); 
/*      */     } 
/* 1706 */     if (roles.length < 50)
/* 1707 */       buf.append("checkbox{id=\"newpushpullturn\"}"); 
/* 1708 */     buf.append("label{text=\"Allows ability to push, pull, turn items. Note that pull and push may require pickup permission as well\"}");
/*      */     
/* 1710 */     buf.append("label{text=\"Diplomat\"};");
/* 1711 */     for (i = 0; i < roles.length; i++) {
/*      */       
/* 1713 */       if (roles[i].getStatus() != 2 && roles[i].getStatus() != 4)
/* 1714 */         if (roles[i].getStatus() != 1) {
/* 1715 */           buf.append("checkbox{id=\"" + roles[i].getId() + "diplomat\"" + (
/* 1716 */               roles[i].isDiplomat() ? ";selected='true'" : "") + "}");
/*      */         } else {
/* 1718 */           buf.append("label{text=\"N/A\"}");
/*      */         }  
/* 1720 */     }  if (roles.length < 50)
/* 1721 */       buf.append("checkbox{id=\"newdiplomat\"}"); 
/* 1722 */     buf.append("label{text=\"Allows ability to form and break alliances with other settlements, as well as declare war and make peace.\"}");
/*      */     
/* 1724 */     buf.append("label{text=\"Map\"};");
/* 1725 */     for (i = 0; i < roles.length; i++) {
/*      */       
/* 1727 */       if (roles[i].getStatus() != 2 && roles[i].getStatus() != 4)
/* 1728 */         if (roles[i].getStatus() == 3 || roles[i].getStatus() == 0) {
/* 1729 */           buf.append("checkbox{id=\"" + roles[i].getId() + "map\"" + (
/* 1730 */               roles[i].mayUpdateMap() ? ";selected='true'" : "") + "}");
/*      */         } else {
/* 1732 */           buf.append("label{text=\"N/A\"}");
/*      */         }  
/* 1734 */     }  if (roles.length < 50)
/* 1735 */       buf.append("checkbox{id=\"newmayupdatemap\"}"); 
/* 1736 */     buf.append("label{text=\"Allows ability to add and remove village annotations on the map.\"}");
/*      */ 
/*      */     
/* 1739 */     buf.append("label{text=\"Lead\"};");
/* 1740 */     for (i = 0; i < roles.length; i++) {
/*      */       
/* 1742 */       if (roles[i].getStatus() != 2 && roles[i].getStatus() != 4)
/* 1743 */         buf.append("checkbox{id=\"" + roles[i].getId() + "lead\"" + (
/* 1744 */             roles[i].mayLead() ? ";selected='true'" : "") + "}"); 
/*      */     } 
/* 1746 */     if (roles.length < 50)
/* 1747 */       buf.append("checkbox{id=\"newmayLead\"}"); 
/* 1748 */     buf.append("label{text=\"Allows leading creatures.\"}");
/*      */ 
/*      */     
/* 1751 */     buf.append("label{text=\"Pickup\"};");
/* 1752 */     for (i = 0; i < roles.length; i++) {
/*      */       
/* 1754 */       if (roles[i].getStatus() != 2 && roles[i].getStatus() != 4)
/* 1755 */         buf.append("checkbox{id=\"" + roles[i].getId() + "pickup\"" + (
/* 1756 */             roles[i].mayPickup() ? ";selected='true'" : "") + "}"); 
/*      */     } 
/* 1758 */     if (roles.length < 50)
/* 1759 */       buf.append("checkbox{id=\"newmayPickup\"}"); 
/* 1760 */     buf.append("label{text=\"Allows ability to pickup item on deed.\"}");
/*      */ 
/*      */     
/* 1763 */     buf.append("label{text=\"Tame\"};");
/* 1764 */     for (i = 0; i < roles.length; i++) {
/*      */       
/* 1766 */       if (roles[i].getStatus() != 2 && roles[i].getStatus() != 4)
/* 1767 */         buf.append("checkbox{id=\"" + roles[i].getId() + "tame\"" + (
/* 1768 */             roles[i].mayTame() ? ";selected='true'" : "") + "}"); 
/*      */     } 
/* 1770 */     if (roles.length < 50)
/* 1771 */       buf.append("checkbox{id=\"newmayTame\"}"); 
/* 1772 */     buf.append("label{text=\"Allows ability to Tame on deed.\"}");
/*      */ 
/*      */     
/* 1775 */     buf.append("label{text=\"Load\"};");
/* 1776 */     for (i = 0; i < roles.length; i++) {
/*      */       
/* 1778 */       if (roles[i].getStatus() != 2 && roles[i].getStatus() != 4)
/* 1779 */         buf.append("checkbox{id=\"" + roles[i].getId() + "load\"" + (
/* 1780 */             roles[i].mayLoad() ? ";selected='true'" : "") + "}"); 
/*      */     } 
/* 1782 */     if (roles.length < 50)
/* 1783 */       buf.append("checkbox{id=\"newmayLoad\"}"); 
/* 1784 */     buf.append("label{text=\"Allows ability to Load on deed (needs Pickup as well).\"}");
/*      */ 
/*      */     
/* 1787 */     buf.append("label{text=\"Butcher\"};");
/* 1788 */     for (i = 0; i < roles.length; i++) {
/*      */       
/* 1790 */       if (roles[i].getStatus() != 2 && roles[i].getStatus() != 4)
/* 1791 */         buf.append("checkbox{id=\"" + roles[i].getId() + "butcher\"" + (
/* 1792 */             roles[i].mayButcher() ? ";selected='true'" : "") + "}"); 
/*      */     } 
/* 1794 */     if (roles.length < 50)
/* 1795 */       buf.append("checkbox{id=\"newmayButcher\"}"); 
/* 1796 */     buf.append("label{text=\"Allows ability to Butcher on deed.\"}");
/*      */ 
/*      */     
/* 1799 */     buf.append("label{text=\"Attach Lock\"};");
/* 1800 */     for (i = 0; i < roles.length; i++) {
/*      */       
/* 1802 */       if (roles[i].getStatus() != 2 && roles[i].getStatus() != 4)
/* 1803 */         buf.append("checkbox{id=\"" + roles[i].getId() + "attachLock\"" + (
/* 1804 */             roles[i].mayAttachLock() ? ";selected='true'" : "") + "}"); 
/*      */     } 
/* 1806 */     if (roles.length < 50)
/* 1807 */       buf.append("checkbox{id=\"newmayAttachLock\"}"); 
/* 1808 */     buf.append("label{text=\"Allows ability to Attach Locks on deed.\"}");
/*      */ 
/*      */     
/* 1811 */     buf.append("label{text=\"Pick Locks\"};");
/* 1812 */     for (i = 0; i < roles.length; i++) {
/*      */       
/* 1814 */       if (roles[i].getStatus() != 2 && roles[i].getStatus() != 4)
/* 1815 */         if (roles[i].getStatus() != 1) {
/* 1816 */           buf.append("checkbox{id=\"" + roles[i].getId() + "pickLocks\"" + (
/* 1817 */               roles[i].mayPickLocks() ? ";selected='true'" : "") + "}");
/*      */         } else {
/* 1819 */           buf.append("label{text=\"N/A\"}");
/*      */         }  
/* 1821 */     }  if (roles.length < 50)
/* 1822 */       buf.append("checkbox{id=\"newmayPickLocks\"}"); 
/* 1823 */     buf.append("label{text=\"Allows lockpicking of structures.\"}");
/*      */     
/* 1825 */     buf.append("label{text=\"Settlement\"};");
/* 1826 */     for (i = 0; i < roles.length; i++) {
/*      */       
/* 1828 */       if (roles[i].getStatus() == 0) {
/*      */         
/* 1830 */         int villId = roles[i].getVillageAppliedTo();
/*      */         
/*      */         try {
/* 1833 */           Village villapplied = Villages.getVillage(villId);
/* 1834 */           buf.append("harray{input{maxchars=\"40\";id=\"" + roles[i].getId() + "Applied\"; text=\"" + villapplied
/* 1835 */               .getName() + "\"};label{text=\" \"}}");
/*      */         }
/* 1837 */         catch (NoSuchVillageException nsv) {
/*      */           
/* 1839 */           buf.append("label{text=\"N/A\"}");
/*      */         }
/*      */       
/* 1842 */       } else if (roles[i].getStatus() != 2 && roles[i].getStatus() != 4) {
/* 1843 */         buf.append("label{text=\"N/A\"}");
/*      */       } 
/* 1845 */     }  if (roles.length < 50)
/* 1846 */       buf.append("input{maxchars=\"40\";id=\"newappliedTo\";text=\"\"}"); 
/* 1847 */     buf.append("label{text=\"Make the role apply to a certain settlement only. Useful in alliances.\"}");
/*      */     
/* 1849 */     buf.append("label{text=\"DELETE\"};");
/* 1850 */     for (i = 0; i < roles.length; i++) {
/*      */       
/* 1852 */       if (roles[i].getStatus() == 0) {
/* 1853 */         buf.append("checkbox{id=\"" + roles[i].getId() + "delete\"selected=\"false\"}");
/*      */       }
/* 1855 */       else if (roles[i].getStatus() != 2 && roles[i].getStatus() != 4) {
/* 1856 */         buf.append("label{text=\"N/A\"}");
/*      */       } 
/* 1858 */     }  if (roles.length < 50)
/* 1859 */       buf.append("label{text=\" \"}"); 
/* 1860 */     buf.append("label{text=\"If you check that box the role will disappear.\"}");
/*      */     
/* 1862 */     buf.append("}");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1888 */     buf.append(createAnswerButton3());
/* 1889 */     getResponder().getCommunicator().sendBml(700, 400, true, true, buf.toString(), 200, 200, 200, this.title);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Village getVillage() {
/*      */     try {
/* 1896 */       Village village = null;
/* 1897 */       if (this.gmBack.length() > 0) {
/*      */ 
/*      */         
/*      */         try {
/* 1901 */           village = Villages.getVillage((int)this.target);
/*      */         }
/* 1903 */         catch (NoSuchVillageException e) {
/*      */ 
/*      */           
/* 1906 */           getResponder().getCommunicator().sendNormalServerMessage("Cannot find the village ... most odd!.");
/*      */         } 
/*      */         
/* 1909 */         return village;
/*      */       } 
/*      */ 
/*      */       
/* 1913 */       if (this.target == -10L) {
/*      */         
/* 1915 */         village = getResponder().getCitizenVillage();
/* 1916 */         if (village == null)
/*      */         {
/* 1918 */           getResponder().getCommunicator().sendNormalServerMessage("You are not a citizen of any village (on this server).");
/*      */         }
/*      */         
/* 1921 */         return village;
/*      */       } 
/*      */ 
/*      */       
/* 1925 */       Item deed = Items.getItem(this.target);
/* 1926 */       int villageId = deed.getData2();
/* 1927 */       return Villages.getVillage(villageId);
/*      */     
/*      */     }
/* 1930 */     catch (NoSuchItemException nsi) {
/*      */       
/* 1932 */       logger.log(Level.WARNING, "Failed to locate village deed with id " + this.target, (Throwable)nsi);
/* 1933 */       getResponder().getCommunicator().sendNormalServerMessage("Failed to locate the deed item for that request. Please contact administration.");
/*      */       
/* 1935 */       return null;
/*      */     }
/* 1937 */     catch (NoSuchVillageException nsv) {
/*      */       
/* 1939 */       logger.log(Level.WARNING, "Failed to locate village for deed with id " + this.target, (Throwable)nsv);
/* 1940 */       getResponder().getCommunicator().sendNormalServerMessage("Failed to locate the village for that request. Please contact administration.");
/*      */       
/* 1942 */       return null;
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\VillageRolesManageQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */