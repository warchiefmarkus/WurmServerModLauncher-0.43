/*      */ package com.wurmonline.server.behaviours;
/*      */ 
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.WurmCalendar;
/*      */ import com.wurmonline.server.creatures.Communicator;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.ItemTemplate;
/*      */ import com.wurmonline.server.items.ItemTemplateFactory;
/*      */ import com.wurmonline.server.items.ItemTypes;
/*      */ import com.wurmonline.server.items.NoSuchTemplateException;
/*      */ import com.wurmonline.server.items.WurmColor;
/*      */ import com.wurmonline.server.players.Permissions;
/*      */ import com.wurmonline.server.players.PermissionsHistories;
/*      */ import com.wurmonline.server.players.PermissionsPlayerList;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.players.PlayerInfo;
/*      */ import com.wurmonline.server.players.PlayerInfoFactory;
/*      */ import com.wurmonline.server.questions.ManageObjectList;
/*      */ import com.wurmonline.server.questions.ManagePermissions;
/*      */ import com.wurmonline.server.questions.MissionManager;
/*      */ import com.wurmonline.server.questions.PermissionsHistory;
/*      */ import com.wurmonline.server.skills.NoSuchSkillException;
/*      */ import com.wurmonline.server.skills.Skill;
/*      */ import com.wurmonline.server.skills.Skills;
/*      */ import com.wurmonline.server.structures.Door;
/*      */ import com.wurmonline.server.structures.DoorSettings;
/*      */ import com.wurmonline.server.structures.Fence;
/*      */ import com.wurmonline.server.structures.FenceGate;
/*      */ import com.wurmonline.server.structures.NoSuchLockException;
/*      */ import com.wurmonline.server.structures.Structure;
/*      */ import com.wurmonline.server.tutorial.MissionTriggers;
/*      */ import com.wurmonline.server.utils.StringUtil;
/*      */ import com.wurmonline.server.utils.logging.TileEvent;
/*      */ import com.wurmonline.server.villages.NoSuchVillageException;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.villages.Villages;
/*      */ import com.wurmonline.server.zones.NoSuchZoneException;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.Zone;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import com.wurmonline.shared.constants.FenceConstants;
/*      */ import com.wurmonline.shared.constants.StructureConstantsEnum;
/*      */ import com.wurmonline.shared.constants.StructureStateEnum;
/*      */ import edu.umd.cs.findbugs.annotations.Nullable;
/*      */ import java.io.IOException;
/*      */ import java.util.Collections;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.annotation.Nonnull;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ final class FenceBehaviour
/*      */   extends Behaviour
/*      */   implements FenceConstants, ItemTypes, MiscConstants
/*      */ {
/*   85 */   private static final Logger logger = Logger.getLogger(FenceBehaviour.class.getName());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   FenceBehaviour() {
/*   92 */     super((short)22);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   FenceBehaviour(short type) {
/*  102 */     super(type);
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
/*      */   @Nonnull
/*      */   public List<ActionEntry> getBehavioursFor(@Nonnull Creature performer, @Nonnull Item subject, @Nonnull Fence target) {
/*  119 */     List<ActionEntry> toReturn = super.getBehavioursFor(performer, target);
/*      */     
/*  121 */     long targetId = target.getId();
/*  122 */     FenceGate gate = FenceGate.getFenceGate(targetId);
/*  123 */     int templateId = subject.getTemplateId();
/*  124 */     if (!target.isFinished()) {
/*      */       
/*  126 */       toReturn.add(Actions.actionEntrys[171]);
/*  127 */       toReturn.add(Actions.actionEntrys[607]);
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  132 */       if (target.isItemRepair(subject) && !target.isFlowerbed()) {
/*      */         
/*  134 */         if (target.getDamage() > 0.0F)
/*      */         {
/*  136 */           if ((!Servers.localServer.challengeServer || performer.getEnemyPresense() <= 0) && 
/*  137 */             !target.isNoRepair())
/*      */           {
/*  139 */             if (!toReturn.contains(Actions.actionEntrys[193]))
/*  140 */               toReturn.add(Actions.actionEntrys[193]); 
/*      */           }
/*      */         }
/*  143 */         if (target.getQualityLevel() < 100.0F && !target.isNoImprove() && target.getDamage() == 0.0F)
/*      */         {
/*  145 */           if (!toReturn.contains(Actions.actionEntrys[192])) {
/*  146 */             toReturn.add(Actions.actionEntrys[192]);
/*      */           }
/*      */         }
/*  149 */       } else if (templateId == 676) {
/*      */         
/*  151 */         if (subject.getOwnerId() == performer.getWurmId()) {
/*  152 */           toReturn.add(Actions.actionEntrys[472]);
/*      */         }
/*  154 */       } else if (subject.isContainerLiquid() && target.isFlowerbed()) {
/*      */         
/*  156 */         Item[] items = subject.getItemsAsArray();
/*  157 */         for (Item item : items) {
/*      */           
/*  159 */           if (item.getTemplateId() == 128) {
/*      */             
/*  161 */             toReturn.add(Actions.actionEntrys[565]);
/*      */             break;
/*      */           } 
/*      */         } 
/*      */       } 
/*  166 */       if (target.isFinished()) {
/*      */         
/*  168 */         VolaTile fenceTile = Zones.getTileOrNull(target.getTileX(), target.getTileY(), target.isOnSurface());
/*  169 */         Structure structure = (fenceTile == null) ? null : fenceTile.getStructure();
/*      */         
/*  171 */         if (structure == null || MethodsStructure.mayModifyStructure(performer, structure, fenceTile, (short)683))
/*      */         {
/*  173 */           if (target.isStoneFence() && (subject.getTemplateId() == 130 || (subject
/*  174 */             .isWand() && performer.getPower() >= 4))) {
/*  175 */             toReturn.add(new ActionEntry((short)847, "Render fence", "rendering"));
/*  176 */           } else if (target.isPlasteredFence() && (subject.getTemplateId() == 1115 || (subject
/*  177 */             .isWand() && performer.getPower() >= 4))) {
/*  178 */             toReturn.add(new ActionEntry((short)847, "Remove render", "removing"));
/*      */           } 
/*      */         }
/*      */       } 
/*  182 */       if (!performer.isGuest() && !target.isIndestructible()) {
/*      */         
/*  184 */         Skills skills = performer.getSkills();
/*  185 */         Skill str = skills.getSkillOrLearn(102);
/*  186 */         if (str.getKnowledge(0.0D) > 21.0D)
/*      */         {
/*  188 */           if (!target.isRubble()) {
/*  189 */             toReturn.add(Actions.actionEntrys[172]);
/*      */           }
/*      */         }
/*      */       } 
/*      */       
/*  194 */       if (subject.isColor() && !target.isNotPaintable()) {
/*  195 */         toReturn.add(Actions.actionEntrys[231]);
/*      */       }
/*  197 */       if (target.isDoor()) {
/*  198 */         toReturn.addAll(getBehavioursForGate(performer, subject, target, gate));
/*      */       }
/*  200 */       if (subject.isTrellis() && performer.getFloorLevel() == 0) {
/*      */         
/*  202 */         toReturn.add(new ActionEntry((short)-3, "Plant", "Plant options"));
/*  203 */         toReturn.add(Actions.actionEntrys[746]);
/*  204 */         toReturn.add(new ActionEntry((short)176, "In center", "planting"));
/*  205 */         toReturn.add(Actions.actionEntrys[747]);
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  210 */     if (MethodsStructure.isCorrectToolForBuilding(performer, templateId)) {
/*      */       
/*  212 */       if (!target.isHedge() && !target.isFinished())
/*      */       {
/*  214 */         toReturn.add(Actions.actionEntrys[170]);
/*      */       }
/*      */     }
/*  217 */     else if (templateId == 267) {
/*      */       
/*  219 */       if (target.isHedge()) {
/*      */         
/*  221 */         toReturn.add(Actions.actionEntrys[373]);
/*  222 */         if (performer.getPower() > 0)
/*  223 */           toReturn.add(Actions.actionEntrys[188]); 
/*      */       } 
/*      */     } 
/*  226 */     if (target.isHedge() && (subject.isWeaponSlash() || subject.getTemplateId() == 24))
/*      */     {
/*  228 */       toReturn.add(Actions.actionEntrys[96]);
/*      */     }
/*      */     
/*  231 */     if ((templateId == 315 || templateId == 176) && performer
/*  232 */       .getPower() >= 2) {
/*      */ 
/*      */       
/*  235 */       toReturn.add(Actions.actionEntrys[180]);
/*      */       
/*  237 */       if (target.getDamage() > 0.0F)
/*      */       {
/*  239 */         if (!Servers.localServer.challengeServer || performer.getEnemyPresense() <= 0)
/*      */         {
/*  241 */           toReturn.add(Actions.actionEntrys[193]);
/*      */         }
/*      */       }
/*      */       
/*  245 */       if (target.getQualityLevel() < 100.0F)
/*      */       {
/*  247 */         toReturn.add(Actions.actionEntrys[192]);
/*      */       }
/*      */       
/*  250 */       if (templateId == 176 && Servers.isThisATestServer() && !target.isMagic())
/*      */       {
/*  252 */         toReturn.add(Actions.actionEntrys[581]);
/*      */       }
/*      */       
/*  255 */       toReturn.add(Actions.actionEntrys[684]);
/*  256 */       if (target.isHedge())
/*      */       {
/*  258 */         toReturn.add(Actions.actionEntrys[373]);
/*  259 */         toReturn.add(Actions.actionEntrys[188]);
/*  260 */         toReturn.add(Actions.actionEntrys[96]);
/*      */       }
/*      */     
/*      */     }
/*  264 */     else if (templateId == 441) {
/*      */       
/*  266 */       if (target.getColor() != -1 && !target.isNotPaintable())
/*      */       {
/*  268 */         toReturn.add(Actions.actionEntrys[232]);
/*      */       }
/*      */     } 
/*      */     
/*  272 */     if ((MissionTriggers.getMissionTriggersWith(templateId, 473, targetId)).length > 0)
/*      */     {
/*  274 */       toReturn.add(Actions.actionEntrys[473]);
/*      */     }
/*  276 */     if ((MissionTriggers.getMissionTriggersWith(templateId, 474, targetId)).length > 0)
/*      */     {
/*  278 */       toReturn.add(Actions.actionEntrys[474]);
/*      */     }
/*      */ 
/*      */     
/*  282 */     addEmotes(toReturn);
/*      */ 
/*      */     
/*  285 */     addWarStuff(toReturn, performer, target);
/*      */     
/*  287 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void addWarStuff(@Nonnull List<ActionEntry> toReturn, @Nonnull Creature performer, @Nonnull Fence fence) {
/*  294 */     Village targVill = fence.getVillage();
/*  295 */     Village village = performer.getCitizenVillage();
/*      */     
/*  297 */     if (village == null || !village.mayDoDiplomacy(performer) || targVill == null) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/*  302 */     if (village == targVill) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/*  307 */     boolean atPeace = village.mayDeclareWarOn(targVill);
/*  308 */     if (atPeace) {
/*      */       
/*  310 */       toReturn.add(new ActionEntry((short)-1, "Village", "Village options", emptyIntArr));
/*  311 */       toReturn.add(Actions.actionEntrys[209]);
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
/*      */   @Nonnull
/*      */   public List<ActionEntry> getBehavioursFor(@Nonnull Creature performer, @Nonnull Fence target) {
/*  326 */     List<ActionEntry> toReturn = super.getBehavioursFor(performer, target);
/*      */     
/*  328 */     FenceGate gate = FenceGate.getFenceGate(target.getId());
/*      */     
/*  330 */     if (!target.isFinished()) {
/*  331 */       toReturn.add(Actions.actionEntrys[607]);
/*  332 */     } else if (target.isDoor()) {
/*  333 */       toReturn.addAll(getBehavioursForGate(performer, (Item)null, target, gate));
/*      */     } 
/*  335 */     addEmotes(toReturn);
/*      */     
/*  337 */     return toReturn;
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
/*      */   public boolean action(@Nonnull Action act, @Nonnull Creature performer, @Nonnull Item source, boolean onSurface, @Nonnull Fence target, short action, float counter) {
/*  356 */     boolean done = true;
/*  357 */     FenceGate gate = FenceGate.getFenceGate(target.getId());
/*      */     
/*  359 */     Communicator comm = performer.getCommunicator();
/*  360 */     switch (action)
/*      */     
/*      */     { 
/*      */       case 1:
/*  364 */         if (source.getTemplateId() == 176 && performer.getPower() >= 2) {
/*      */           
/*  366 */           done = true;
/*  367 */           action(act, performer, onSurface, target, action, counter);
/*  368 */           comm.sendNormalServerMessage("Startx=" + target
/*  369 */               .getTileX() + ", Starty=" + target.getTileY() + " dir=" + target
/*  370 */               .getDir());
/*      */         } else {
/*      */           
/*  373 */           done = action(act, performer, onSurface, target, action, counter);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  937 */         return done;case 607: comm.sendAddFenceToCreationWindow(target, -10L); return true;case 209: done = action(act, performer, onSurface, target, action, counter); return done;case 170: if (target.getLayer() != performer.getLayer() && Servers.isThisAPvpServer()) { performer.getCommunicator().sendNormalServerMessage("You cannot continue that, you are too far away."); return true; }  if (MethodsStructure.isCorrectToolForBuilding(performer, source.getTemplateId())) { done = MethodsStructure.continueFence(performer, target, source, counter, action, act); if (done) if (!target.isFinished()) { comm.sendAddFenceToCreationWindow(target, target.getId()); } else { comm.sendRemoveFromCreationWindow(target.getId()); }   }  return done;case 581: if (source.getTemplateId() == 176 && Servers.isThisATestServer()) { decay(target, performer); done = true; } else { done = super.action(act, performer, onSurface, target, action, counter); }  return done;case 565: if (target.isFlowerbed()) { done = waterFlower(act, performer, source, target, counter); } else { done = super.action(act, performer, onSurface, target, action, counter); }  return done;case 373: done = Terraforming.pruneHedge(act, performer, source, target, onSurface, counter); return done;case 188: if (performer.getPower() > 0) if (!target.isHighHedge() && target.getType() != StructureConstantsEnum.HEDGE_FLOWER1_LOW && target.getType() != StructureConstantsEnum.HEDGE_FLOWER3_MEDIUM) { target.setDamage(0.0F); target.setType(StructureConstantsEnum.getEnumByValue((short)(byte)((target.getType()).value + 1))); try { target.save(); VolaTile tile = Zones.getTileOrNull(target.getTileX(), target.getTileY(), onSurface); if (tile != null) tile.updateFence(target);  } catch (IOException iox) { logger.log(Level.WARNING, iox.getMessage(), iox); }  TileEvent.log(target.getTileX(), target.getTileY(), 0, performer.getWurmId(), 188); } else { comm.sendNormalServerMessage("You can't grow that hedge any further, clown."); }   return done;case 96: if (target.isHedge() && (source.isWeaponSlash() || source.getTemplateId() == 24 || source.isWand())) done = Terraforming.chopHedge(act, performer, source, target, onSurface, counter);  return done;case 171: done = MethodsStructure.removeFencePlan(performer, source, target, counter, action, act); return done;case 193: if (!target.isFlowerbed()) { if ((!Servers.localServer.challengeServer || performer.getEnemyPresense() <= 0) && !target.isNoRepair()) { done = MethodsStructure.repairFence(act, performer, source, target, counter); } else { done = true; }  } else { done = super.action(act, performer, onSurface, target, action, counter); }  return done;case 192: if (target.isFinished() && target.isItemRepair(source) && !target.isNoImprove()) { done = MethodsStructure.improveFence(act, performer, source, target, counter); } else { done = true; }  return done;case 172: done = true; if (!target.isRubble() && !target.isIndestructible()) try { Skill str = performer.getSkills().getSkill(102); if (str.getKnowledge(0.0D) > 21.0D) done = MethodsStructure.destroyFence(action, performer, source, target, false, counter);  } catch (NoSuchSkillException nss) { logger.log(Level.WARNING, "Weird, " + performer.getName() + " has no strength!"); }   return done;case 173: done = true; if (!target.isIndestructible()) try { Skill str = performer.getSkills().getSkill(102); if (str.getKnowledge(0.0D) > 21.0D) done = MethodsStructure.destroyFence(action, performer, source, target, true, counter);  } catch (NoSuchSkillException nss) { logger.log(Level.WARNING, "Weird, " + performer.getName() + " has no strength!"); }   return done;case 231: if (target.isFinished()) { if (target.isNotPaintable() || !Methods.isActionAllowed(performer, action)) { comm.sendNormalServerMessage("You are not allowed to paint this fence."); return true; }  done = MethodsStructure.colorFence(performer, source, target, act); } else { comm.sendNormalServerMessage("Finish the wall first."); return true; }  return done;case 232: if (target.isNotPaintable() || !Methods.isActionAllowed(performer, action)) { comm.sendNormalServerMessage("You are not allowed to remove the paint from this wall."); return true; }  done = MethodsStructure.removeColor(performer, source, target, act); return done;case 180: if (performer.getPower() >= 2) { MethodsStructure.instaDestroyFence(performer, target); } else { done = super.action(act, performer, onSurface, target, action, counter); }  return done;case 161: if (source.isLock() && source.isLocked()) { comm.sendNormalServerMessage("The " + source.getName() + " is already in use."); return true; }  if (source.getTemplateId() != 252) { done = super.action(act, performer, onSurface, target, action, counter); } else if (target.isDoor()) { if (!target.isFinished() || target.isNotLockable()) { comm.sendNormalServerMessage("This fence is not finished yet. Attach the lock when it is finished."); } else { if (gate == null) { logger.log(Level.WARNING, "No gate found for fence with id " + target.getId()); return true; }  try { long lockid = gate.getLockId(); if (lockid == source.getWurmId()) { comm.sendNormalServerMessage("You may not attach the lock to this gate twice. Are you crazy or supernatural?"); return true; }  } catch (NoSuchLockException noSuchLockException) {} if (!((Methods.isActionAllowed(performer, action) && gate.mayAttachLock(performer)) ? 1 : 0)) { comm.sendNormalServerMessage("You may not attach the lock to this gate as you do not have permission to do so."); return true; }  boolean insta = (Servers.isThisATestServer() && performer.getPower() > 0); Village village = null; Skill carpentry = performer.getSkills().getSkillOrLearn(1005); int time = 10; done = false; if (counter == 1.0F) { time = (int)Math.max(100.0D, (100.0D - carpentry.getKnowledge(source, 0.0D)) * 5.0D); try { performer.getCurrentAction().setTimeLeft(time); } catch (NoSuchActionException nsa) { logger.log(Level.INFO, "This action does not exist?", (Throwable)nsa); }  comm.sendNormalServerMessage("You start to attach the lock."); Server.getInstance().broadCastAction(performer.getName() + " starts to attach a lock.", performer, 5); performer.sendActionControl(Actions.actionEntrys[161].getVerbString(), true, time); } else { try { time = performer.getCurrentAction().getTimeLeft(); } catch (NoSuchActionException nsa) { logger.log(Level.INFO, "This action does not exist?", (Throwable)nsa); }  if (counter * 10.0F > time || insta) { carpentry.skillCheck((100.0F - source.getCurrentQualityLevel()), 0.0D, false, counter); long parentId = source.getParentId(); if (parentId != -10L) try { Items.getItem(parentId).dropItem(source.getWurmId(), true); } catch (NoSuchItemException nsi) { logger.log(Level.INFO, performer.getName() + " tried to build with nonexistant nail."); }   done = true; try { village = gate.getVillage(); long lockid = gate.getLockId(); if (lockid != source.getLockId()) try { Item oldlock = Items.getItem(lockid); if (village != null) oldlock.removeKey(village.getDeedId());  oldlock.setLocked(false); performer.getInventory().insertItem(oldlock, true); } catch (NoSuchItemException nsy) { logger.log(Level.WARNING, "Weird. Lock id exists, but not the item.", (Throwable)nsy); }   } catch (NoSuchLockException noSuchLockException) {} if (village != null) source.addKey(village.getDeedId());  if (source.getLastOwnerId() != performer.getWurmId()) logger.log(Level.INFO, "Weird. Lock has wrong last owner.");  source.setLastOwnerId(performer.getWurmId()); gate.setLock(source.getWurmId()); source.setLocked(true); PermissionsHistories.addHistoryEntry(gate.getWurmId(), System.currentTimeMillis(), performer.getWurmId(), performer.getName(), "Attached lock to gate"); comm.sendNormalServerMessage("You attach the lock to the gate."); Server.getInstance().broadCastAction(performer.getName() + " attaches a lock to the gate.", performer, 5); if (village != null) { gate.setIsManaged(true, (Player)performer); gate.addGuest(performer.getWurmId(), DoorSettings.DoorPermissions.PASS.getValue()); }  }  }  }  }  return done;case 28: done = true; if (!target.isNotLockable()) try { if (gate != null) { Item lock = gate.getLock(); if (gate.mayLock(performer) || performer.hasKeyForLock(lock)) { lock.lock(); PermissionsHistories.addHistoryEntry(gate.getWurmId(), System.currentTimeMillis(), performer.getWurmId(), performer.getName(), "Locked gate"); comm.sendNormalServerMessage("You lock the gate."); Server.getInstance().broadCastAction(performer.getName() + " locks the gate.", performer, 5); }  } else { logger.log(Level.WARNING, "No gate found for fence with id " + target.getId()); return true; }  } catch (NoSuchLockException noSuchLockException) {}  return done;case 102: done = true; if (!target.isNotLockable()) try { if (gate != null) { Item lock = gate.getLock(); if (gate.mayLock(performer) || performer.hasKeyForLock(lock)) { lock.unlock(); PermissionsHistories.addHistoryEntry(gate.getWurmId(), System.currentTimeMillis(), performer.getWurmId(), performer.getName(), "Unlocked gate"); comm.sendNormalServerMessage("You unlock the gate."); Server.getInstance().broadCastAction(performer.getName() + " unlocks the gate.", performer, 5); }  } else { logger.log(Level.WARNING, "No gate found for fence with id " + target.getId()); return true; }  } catch (NoSuchLockException noSuchLockException) {}  return done;case 101: done = true; if (!target.isNotLockpickable()) if (gate != null) done = MethodsStructure.picklock(performer, source, (Door)gate, gate.getFence().getName(), counter, act);   return done;case 472: done = true; if (source.getTemplateId() == 676 && source.getOwnerId() == performer.getWurmId()) { MissionManager m = new MissionManager(performer, "Manage missions", "Select action", target.getId(), target.getName(), source.getWurmId()); m.sendQuestion(); }  return done;case 667: if (gate != null && (gate.mayManage(performer) || gate.isActualOwner(performer.getWurmId()))) { ManagePermissions mp = new ManagePermissions(performer, ManageObjectList.Type.GATE, (PermissionsPlayerList.ISettings)FenceGate.getFenceGate(target.getId()), false, -10L, false, null, ""); mp.sendQuestion(); }  done = true; return done;case 691: if (gate != null && gate.maySeeHistory(performer)) { PermissionsHistory ph = new PermissionsHistory(performer, target.getId()); ph.sendQuestion(); }  return done;case 684: done = true; if ((source.getTemplateId() == 315 || source.getTemplateId() == 176) && performer.getPower() >= 2) { Methods.sendItemRestrictionManagement(performer, (Permissions.IAllow)target, target.getId()); } else { logger.log(Level.WARNING, performer.getName() + " hacking the protocol by trying to set the restrictions of " + target + ", counter: " + counter + '!'); }  return done;case 176: case 746: case 747: if (source.isTrellis()) { done = Terraforming.plantTrellis(performer, source, target.getTileX(), target.getTileY(), onSurface, target.getDir(), action, counter, act); } else { done = true; }  return done;case 847: if (target.isStoneFence() && (source.getTemplateId() == 130 || (source.isWand() && performer.getPower() >= 4))) return toggleRenderFence(performer, source, target, act, counter);  if (target.isPlasteredFence() && (source.getTemplateId() == 1115 || (source.isWand() && performer.getPower() >= 4))) return toggleRenderFence(performer, source, target, act, counter);  break; }  done = super.action(act, performer, onSurface, target, action, counter); return done;
/*      */   }
/*      */ 
/*      */   
/*      */   static final boolean toggleRenderFence(Creature performer, Item tool, Fence fence, Action act, float counter) {
/*  942 */     boolean insta = (tool.isWand() && performer.getPower() >= 4);
/*  943 */     VolaTile fenceTile = getFenceTile(fence);
/*  944 */     if (fenceTile == null) {
/*  945 */       return true;
/*      */     }
/*  947 */     Structure structure = fenceTile.getStructure();
/*  948 */     if (!insta && structure != null && !MethodsStructure.mayModifyStructure(performer, structure, fenceTile, (short)683)) {
/*      */       
/*  950 */       performer.getCommunicator().sendNormalServerMessage("You are not allowed to modify the structure.");
/*      */       
/*  952 */       return true;
/*      */     } 
/*  954 */     if (!Methods.isActionAllowed(performer, (short)116, fenceTile.getTileX(), fenceTile.getTileY()))
/*      */     {
/*  956 */       return true;
/*      */     }
/*  958 */     if (fence.isStoneFence() && !insta)
/*      */     {
/*      */       
/*  961 */       if (tool.getWeightGrams() < 5000) {
/*      */         
/*  963 */         performer.getCommunicator().sendNormalServerMessage("It takes 5kg of " + tool
/*  964 */             .getName() + " to render the " + fence.getName() + ".");
/*  965 */         return true;
/*      */       } 
/*      */     }
/*  968 */     int time = 40;
/*  969 */     if (counter == 1.0F) {
/*      */       
/*  971 */       String render = fence.isStoneFence() ? "render" : "remove the render from";
/*  972 */       String rendering = fence.isStoneFence() ? "rending" : "removing the render from";
/*  973 */       act.setTimeLeft(time);
/*  974 */       performer.sendActionControl(rendering + " the fence", true, time);
/*  975 */       performer.getCommunicator().sendNormalServerMessage(StringUtil.format("You start to " + render + " the %s.", new Object[] { fence.getName() }));
/*  976 */       Server.getInstance().broadCastAction(
/*  977 */           StringUtil.format("%s starts to " + render + " the %s.", new Object[] { performer.getName(), fence.getName() }), performer, 5);
/*  978 */       return false;
/*      */     } 
/*      */ 
/*      */     
/*  982 */     time = act.getTimeLeft();
/*      */     
/*  984 */     if (counter * 10.0F > time || insta) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  993 */       String render = fence.isStoneFence() ? "render" : "remove the render from";
/*  994 */       String renders = fence.isStoneFence() ? "renders" : "removes the render from";
/*  995 */       performer.getCommunicator().sendNormalServerMessage(StringUtil.format("You " + render + " the %s.", new Object[] { fence.getName() }));
/*  996 */       Server.getInstance().broadCastAction(
/*  997 */           StringUtil.format("%s " + renders + " the %s.", new Object[] { performer.getName(), fence.getName() }), performer, 5);
/*  998 */       if (fence.isStoneFence() && !insta)
/*      */       {
/* 1000 */         tool.setWeight(tool.getWeightGrams() - 5000, true);
/*      */       }
/*      */       
/* 1003 */       if (fence.getType() == StructureConstantsEnum.FENCE_STONE) {
/* 1004 */         fence.setType(StructureConstantsEnum.FENCE_RENDERED);
/* 1005 */       } else if (fence.getType() == StructureConstantsEnum.FENCE_IRON) {
/* 1006 */         fence.setType(StructureConstantsEnum.FENCE_RENDERED_IRON);
/* 1007 */       } else if (fence.getType() == StructureConstantsEnum.FENCE_IRON_GATE) {
/* 1008 */         fence.setType(StructureConstantsEnum.FENCE_RENDERED_IRON_GATE);
/* 1009 */       } else if (fence.getType() == StructureConstantsEnum.FENCE_RENDERED) {
/* 1010 */         fence.setType(StructureConstantsEnum.FENCE_STONE);
/* 1011 */       } else if (fence.getType() == StructureConstantsEnum.FENCE_RENDERED_IRON) {
/* 1012 */         fence.setType(StructureConstantsEnum.FENCE_IRON);
/* 1013 */       } else if (fence.getType() == StructureConstantsEnum.FENCE_RENDERED_IRON_GATE) {
/* 1014 */         fence.setType(StructureConstantsEnum.FENCE_IRON_GATE);
/*      */       
/*      */       }
/* 1017 */       else if (fence.getType() == StructureConstantsEnum.FENCE_IRON_HIGH) {
/* 1018 */         fence.setType(StructureConstantsEnum.FENCE_RENDERED_HIGH_IRON_FENCE);
/* 1019 */       } else if (fence.getType() == StructureConstantsEnum.FENCE_RENDERED_HIGH_IRON_FENCE) {
/* 1020 */         fence.setType(StructureConstantsEnum.FENCE_IRON_HIGH);
/*      */       }
/* 1022 */       else if (fence.getType() == StructureConstantsEnum.FENCE_RENDERED_HIGH_IRON_FENCE_GATE) {
/* 1023 */         fence.setType(StructureConstantsEnum.FENCE_IRON_GATE_HIGH);
/* 1024 */       } else if (fence.getType() == StructureConstantsEnum.FENCE_IRON_GATE_HIGH) {
/* 1025 */         fence.setType(StructureConstantsEnum.FENCE_RENDERED_HIGH_IRON_FENCE_GATE);
/*      */       }
/* 1027 */       else if (fence.getType() == StructureConstantsEnum.FENCE_RENDERED_PORTCULLIS) {
/* 1028 */         fence.setType(StructureConstantsEnum.FENCE_PORTCULLIS);
/* 1029 */       } else if (fence.getType() == StructureConstantsEnum.FENCE_PORTCULLIS) {
/* 1030 */         fence.setType(StructureConstantsEnum.FENCE_RENDERED_PORTCULLIS);
/*      */       }
/* 1032 */       else if (fence.getType() == StructureConstantsEnum.FENCE_RENDERED_STONE_PARAPET) {
/* 1033 */         fence.setType(StructureConstantsEnum.FENCE_STONE_PARAPET);
/* 1034 */       } else if (fence.getType() == StructureConstantsEnum.FENCE_STONE_PARAPET) {
/* 1035 */         fence.setType(StructureConstantsEnum.FENCE_RENDERED_STONE_PARAPET);
/*      */       }
/* 1037 */       else if (fence.getType() == StructureConstantsEnum.FENCE_RENDERED_CHAIN_FENCE) {
/* 1038 */         fence.setType(StructureConstantsEnum.FENCE_MEDIUM_CHAIN);
/* 1039 */       } else if (fence.getType() == StructureConstantsEnum.FENCE_MEDIUM_CHAIN) {
/* 1040 */         fence.setType(StructureConstantsEnum.FENCE_RENDERED_CHAIN_FENCE);
/*      */       }
/* 1042 */       else if (fence.getType() == StructureConstantsEnum.FENCE_RENDERED_TALL_STONE_WALL) {
/* 1043 */         fence.setType(StructureConstantsEnum.FENCE_STONEWALL_HIGH);
/* 1044 */       } else if (fence.getType() == StructureConstantsEnum.FENCE_STONEWALL_HIGH) {
/* 1045 */         fence.setType(StructureConstantsEnum.FENCE_RENDERED_TALL_STONE_WALL);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/* 1051 */         fence.save();
/*      */       }
/* 1053 */       catch (IOException e) {
/*      */ 
/*      */         
/* 1056 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/* 1058 */       fenceTile.updateFence(fence);
/*      */       
/* 1060 */       return true;
/*      */     } 
/* 1062 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   static final VolaTile getFenceTile(Fence fence) {
/* 1068 */     int tilex = fence.getStartX();
/* 1069 */     int tiley = fence.getStartY();
/* 1070 */     for (int xx = 1; xx >= -1; xx--) {
/*      */       
/* 1072 */       for (int yy = 1; yy >= -1; yy--) {
/*      */ 
/*      */         
/*      */         try {
/* 1076 */           Zone zone = Zones.getZone(tilex + xx, tiley + yy, fence.isOnSurface());
/* 1077 */           VolaTile tile = zone.getTileOrNull(tilex + xx, tiley + yy);
/* 1078 */           if (tile != null) {
/*      */             
/* 1080 */             Fence[] fences = tile.getFences();
/* 1081 */             for (int s = 0; s < fences.length; s++)
/*      */             {
/* 1083 */               if (fences[s].getId() == fence.getId())
/*      */               {
/* 1085 */                 return tile;
/*      */               }
/*      */             }
/*      */           
/*      */           } 
/* 1090 */         } catch (NoSuchZoneException noSuchZoneException) {}
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1096 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   private static void decay(Fence fence, Creature performer) {
/* 1101 */     if (fence.isMagic()) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/* 1106 */     long decayTime = 86400000L;
/* 1107 */     if (fence.isHedge())
/*      */     {
/* 1109 */       if (fence.isLowHedge()) {
/*      */         
/* 1111 */         decayTime *= 3L;
/*      */       }
/* 1113 */       else if (fence.isMediumHedge()) {
/*      */         
/* 1115 */         decayTime *= 10L;
/*      */       }
/*      */       else {
/*      */         
/* 1119 */         Village vill = fence.getVillage();
/* 1120 */         if (vill != null) {
/*      */           
/* 1122 */           if (vill.moreThanMonthLeft()) {
/*      */             
/* 1124 */             performer.getCommunicator().sendNormalServerMessage("There is more then a month left of upkeep, no decay will take place.");
/*      */             
/*      */             return;
/*      */           } 
/* 1128 */           if (vill.lessThanWeekLeft())
/*      */           {
/* 1130 */             decayTime *= (fence.isFlowerbed() ? 2L : 10L);
/*      */           
/*      */           }
/*      */         
/*      */         }
/* 1135 */         else if (Zones.getKingdom(fence.getTileX(), fence.getTileY()) == 0) {
/*      */           
/* 1137 */           decayTime = (long)((float)decayTime * 0.5F);
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/* 1143 */     fence.setLastUsed(WurmCalendar.currentTime - decayTime - 10L);
/* 1144 */     fence.poll(WurmCalendar.currentTime);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean waterFlower(@Nonnull Action act, @Nonnull Creature performer, @Nonnull Item waterSource, @Nonnull Fence flowerbed, float counter) {
/* 1153 */     int time = 0;
/* 1154 */     Item water = null;
/*      */     
/* 1156 */     for (Item item : waterSource.getItemsAsArray()) {
/*      */       
/* 1158 */       if (item.getTemplateId() == 128) {
/*      */         
/* 1160 */         water = item;
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/*      */     
/* 1166 */     Communicator comm = performer.getCommunicator();
/* 1167 */     if (water == null) {
/*      */       
/* 1169 */       comm.sendNormalServerMessage("You need water to water the flowerbed.", (byte)3);
/* 1170 */       return true;
/*      */     } 
/*      */     
/* 1173 */     if (water.getWeightGrams() < 100) {
/*      */       
/* 1175 */       comm.sendNormalServerMessage("You need more water in order to water the flowerbed.", (byte)3);
/* 1176 */       return true;
/*      */     } 
/*      */     
/* 1179 */     if (flowerbed.getDamage() == 0.0F) {
/*      */       
/* 1181 */       comm.sendNormalServerMessage("This flowerbed is in no need of watering.", (byte)3);
/* 1182 */       return true;
/*      */     } 
/*      */ 
/*      */     
/* 1186 */     Skill gardening = performer.getSkills().getSkillOrLearn(10045);
/*      */     
/* 1188 */     if (counter == 1.0F) {
/*      */       
/* 1190 */       time = Actions.getStandardActionTime(performer, gardening, waterSource, 0.0D);
/* 1191 */       act.setTimeLeft(time);
/* 1192 */       comm.sendNormalServerMessage("You start watering the flowerbed.");
/* 1193 */       Server.getInstance().broadCastAction(performer.getName() + " starts to water a flowerbed.", performer, 5);
/* 1194 */       performer.sendActionControl(Actions.actionEntrys[565].getVerbString(), true, time);
/* 1195 */       return false;
/*      */     } 
/*      */     
/* 1198 */     time = act.getTimeLeft();
/*      */     
/* 1200 */     if (counter * 10.0F <= time)
/*      */     {
/* 1202 */       return false;
/*      */     }
/*      */     
/* 1205 */     double power = gardening.skillCheck(15.0D, 0.0D, false, counter);
/* 1206 */     if (power > 0.0D) {
/*      */       
/* 1208 */       float dmgChange = 20.0F * (float)(power / 100.0D);
/* 1209 */       flowerbed.setDamage(Math.max(0.0F, flowerbed.getDamage() - dmgChange));
/* 1210 */       water.setWeight(water.getWeightGrams() - 100, true);
/* 1211 */       comm.sendNormalServerMessage("You successfully watered the flowerbed.");
/* 1212 */       return true;
/*      */     } 
/*      */     
/* 1215 */     int waterReduction = 100;
/* 1216 */     if (power >= -20.0D) {
/*      */       
/* 1218 */       comm.sendNormalServerMessage("You accidentally miss the flowerbed and pour the water on the ground instead.", (byte)3);
/*      */     
/*      */     }
/* 1221 */     else if (power > -50.0D && power < -20.0D) {
/*      */       
/* 1223 */       comm.sendNormalServerMessage("You spill water all over your clothes.", (byte)3);
/*      */     }
/*      */     else {
/*      */       
/* 1227 */       comm.sendNormalServerMessage("For some inexplicable reason you poured all of the water on the ground, how you thought it would help you will never know.");
/*      */       
/* 1229 */       waterReduction = Math.min(water.getWeightGrams(), 200);
/*      */     } 
/*      */     
/* 1232 */     water.setWeight(water.getWeightGrams() - waterReduction, true);
/*      */     
/* 1234 */     return true;
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
/*      */   public boolean action(@Nonnull Action act, @Nonnull Creature performer, boolean onSurface, @Nonnull Fence target, short action, float counter) {
/*      */     StructureStateEnum state;
/*      */     ManagePermissions mp;
/*      */     StructureConstantsEnum type;
/*      */     String toSend;
/* 1252 */     boolean done = false;
/* 1253 */     FenceGate gate = FenceGate.getFenceGate(target.getId());
/* 1254 */     Communicator comm = performer.getCommunicator();
/* 1255 */     switch (action) {
/*      */ 
/*      */ 
/*      */       
/*      */       case 1:
/* 1260 */         state = target.getState();
/* 1261 */         type = target.getType();
/*      */ 
/*      */         
/* 1264 */         if (target.isFinished()) {
/*      */           
/* 1266 */           toSend = getFinishedString(performer, target, gate, type);
/*      */         }
/*      */         else {
/*      */           
/* 1270 */           toSend = getUnfinishedString(target, state, type);
/*      */         } 
/* 1272 */         if (toSend.length() > 0) {
/*      */           
/* 1274 */           comm.sendNormalServerMessage(toSend);
/* 1275 */           comm.sendNormalServerMessage("QL=" + target.getQualityLevel() + ", dam=" + target.getDamage());
/*      */         } 
/* 1277 */         done = true;
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 607:
/* 1284 */         comm.sendAddFenceToCreationWindow(target, -10L);
/* 1285 */         done = true;
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 28:
/* 1292 */         done = true;
/* 1293 */         if (gate == null) {
/*      */           break;
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*      */         try {
/* 1300 */           Item lock = gate.getLock();
/*      */           
/* 1302 */           if (gate.mayLock(performer) || performer.hasKeyForLock(lock))
/*      */           {
/* 1304 */             lock.lock();
/* 1305 */             comm.sendNormalServerMessage("You lock the gate.");
/* 1306 */             Server.getInstance()
/* 1307 */               .broadCastAction(performer.getNameWithGenus() + " locks the gate.", performer, 5);
/*      */           }
/*      */         
/* 1310 */         } catch (NoSuchLockException noSuchLockException) {}
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 102:
/* 1320 */         done = true;
/*      */         
/*      */         try {
/* 1323 */           Item lock = gate.getLock();
/*      */           
/* 1325 */           if (gate.mayLock(performer) || performer.hasKeyForLock(lock))
/*      */           {
/* 1327 */             lock.unlock();
/* 1328 */             comm.sendNormalServerMessage("You unlock the gate.");
/* 1329 */             Server.getInstance()
/* 1330 */               .broadCastAction(performer.getNameWithGenus() + " unlocks the gate.", performer, 5);
/*      */           }
/*      */         
/* 1333 */         } catch (NoSuchLockException noSuchLockException) {}
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 209:
/* 1343 */         done = true;
/* 1344 */         if (performer.getCitizenVillage() == null) {
/*      */           
/* 1346 */           comm.sendAlertServerMessage("You are no longer a citizen of a village.");
/*      */           
/*      */           break;
/*      */         } 
/* 1350 */         if (target.getVillage() == null) {
/*      */           
/* 1352 */           comm.sendAlertServerMessage(target.getName() + " is no longer in a village.");
/*      */           
/*      */           break;
/*      */         } 
/* 1356 */         if (!performer.getCitizenVillage().mayDeclareWarOn(target.getVillage())) {
/*      */           
/* 1358 */           comm.sendAlertServerMessage(target.getName() + " is already at war with your village.");
/*      */           
/*      */           break;
/*      */         } 
/*      */         
/* 1363 */         Methods.sendWarDeclarationQuestion(performer, target.getVillage());
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 667:
/* 1370 */         done = true;
/*      */         
/* 1372 */         if (gate == null || (
/* 1373 */           !gate.mayManage(performer) && 
/* 1374 */           !gate.isActualOwner(performer.getWurmId()))) {
/*      */           break;
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1382 */         mp = new ManagePermissions(performer, ManageObjectList.Type.GATE, (PermissionsPlayerList.ISettings)FenceGate.getFenceGate(target.getId()), false, -10L, false, null, "");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1388 */         mp.sendQuestion();
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 691:
/* 1395 */         done = true;
/* 1396 */         if (gate != null && gate.maySeeHistory(performer)) {
/*      */           
/* 1398 */           PermissionsHistory ph = new PermissionsHistory(performer, target.getId());
/* 1399 */           ph.sendQuestion();
/*      */         } 
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 193:
/* 1406 */         comm.sendNormalServerMessage("'Repair' requires an active item.");
/* 1407 */         done = true;
/*      */         break;
/*      */     } 
/*      */ 
/*      */     
/* 1412 */     return done;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String getMaterialName(ItemTemplate template) {
/* 1420 */     switch (template.getTemplateId()) {
/*      */       
/*      */       case 218:
/* 1423 */         return "small iron " + template.getName();
/*      */       case 217:
/* 1425 */         return "large iron " + template.getName();
/*      */     } 
/* 1427 */     return template.getName();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private List<ActionEntry> getBehavioursForGate(Creature performer, @Nullable Item subject, Fence target, @Nonnull FenceGate gate) {
/* 1436 */     List<ActionEntry> toReturn = new LinkedList<>();
/* 1437 */     List<ActionEntry> permissions = new LinkedList<>();
/*      */ 
/*      */     
/* 1440 */     if (gate.mayManage(performer) || gate.isActualOwner(performer.getWurmId()))
/*      */     {
/* 1442 */       permissions.add(new ActionEntry((short)667, "Manage Gate", "managing permissions"));
/*      */     }
/*      */     
/* 1445 */     if (gate.maySeeHistory(performer))
/*      */     {
/* 1447 */       permissions.add(new ActionEntry((short)691, "History of Gate", "viewing"));
/*      */     }
/*      */     
/* 1450 */     if (!permissions.isEmpty()) {
/*      */       
/* 1452 */       if (permissions.size() > 1) {
/*      */         
/* 1454 */         Collections.sort(permissions);
/* 1455 */         toReturn.add(new ActionEntry((short)-permissions.size(), "Permissions", "viewing"));
/*      */       } 
/* 1457 */       toReturn.addAll(permissions);
/*      */     } 
/*      */ 
/*      */     
/*      */     try {
/* 1462 */       Item lock = gate.getLock();
/* 1463 */       if (!target.isNotLockable())
/*      */       {
/*      */         
/* 1466 */         if (gate.mayLock(performer) || performer.hasKeyForLock(lock))
/*      */         {
/* 1468 */           if (lock.isLocked()) {
/* 1469 */             toReturn.add(Actions.actionEntrys[102]);
/*      */           } else {
/* 1471 */             toReturn.add(Actions.actionEntrys[28]);
/*      */           }  } 
/*      */       }
/* 1474 */       if (performer.isWithinDistanceTo(target.getTileX(), target.getTileY(), 1))
/*      */       {
/* 1476 */         if (subject != null && subject.getTemplateId() == 463 && !target.isNotLockpickable())
/*      */         {
/* 1478 */           MethodsStructure.addLockPickEntry(performer, subject, (Door)gate, false, lock, toReturn);
/*      */         }
/*      */       }
/*      */     }
/* 1482 */     catch (NoSuchLockException noSuchLockException) {}
/*      */ 
/*      */ 
/*      */     
/* 1486 */     if (!target.isNotLockable() && subject != null && subject.getTemplateId() == 252)
/*      */     {
/* 1488 */       if (gate.mayAttachLock(performer))
/*      */       {
/* 1490 */         toReturn.add(Actions.actionEntrys[161]);
/*      */       }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1507 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   private static String getUnfinishedString(@Nonnull Fence target, StructureStateEnum state, StructureConstantsEnum type) {
/* 1513 */     String toSend = "You see an unfinished fence.";
/*      */     
/* 1515 */     switch (type) {
/*      */ 
/*      */       
/*      */       case FENCE_PLAN_PALISADE:
/*      */       case FENCE_PALISADE:
/* 1520 */         toSend = "You see an unfinished wooden palisade.";
/*      */         break;
/*      */       case FENCE_PLAN_STONEWALL:
/*      */       case FENCE_STONEWALL:
/*      */       case FENCE_STONE_PARAPET:
/* 1525 */         toSend = "You see an unfinished stone wall.";
/*      */         break;
/*      */       case FENCE_PLAN_STONE_PARAPET:
/* 1528 */         toSend = "You see an unfinished stone parapet.";
/*      */         break;
/*      */       case FENCE_PLAN_STONE_IRON_PARAPET:
/* 1531 */         toSend = "You see an unfinished stone and iron parapet.";
/*      */         break;
/*      */       case FENCE_PLAN_WOODEN_PARAPET:
/* 1534 */         toSend = "You see an unfinished wooden parapet.";
/*      */         break;
/*      */       case FENCE_PLAN_WOODEN:
/*      */       case FENCE_WOODEN:
/*      */       case FENCE_PLAN_WOODEN_CRUDE:
/* 1539 */         toSend = "You see an unfinished wooden fence.";
/*      */         break;
/*      */       case FENCE_PLAN_WOODEN_GATE:
/*      */       case FENCE_WOODEN_GATE:
/* 1543 */         toSend = "You see an unfinished wooden fence gate.";
/*      */         break;
/*      */       case FENCE_PLAN_PALISADE_GATE:
/*      */       case FENCE_PALISADE_GATE:
/* 1547 */         toSend = "You see an unfinished wooden palisade gate.";
/*      */         break;
/*      */       case FENCE_PLAN_WOODEN_GATE_CRUDE:
/*      */       case FENCE_WOODEN_CRUDE_GATE:
/* 1551 */         toSend = "You see an unfinished crude wooden fence gate.";
/*      */         break;
/*      */       case FENCE_PLAN_GARDESGARD_GATE:
/*      */       case FENCE_GARDESGARD_GATE:
/* 1555 */         toSend = "You see an unfinished wooden roundpole fence gate.";
/*      */         break;
/*      */       case FENCE_PLAN_STONEWALL_HIGH:
/*      */       case FENCE_STONEWALL_HIGH:
/* 1559 */         toSend = "You see an unfinished tall stone wall.";
/*      */         break;
/*      */       case FENCE_PLAN_MEDIUM_CHAIN:
/* 1562 */         toSend = "You see an unfinished chain fence.";
/*      */         break;
/*      */       case FENCE_PLAN_PORTCULLIS:
/* 1565 */         toSend = "You see an unfinished portcullis.";
/*      */         break;
/*      */       case FENCE_PLAN_IRON:
/* 1568 */         toSend = "You see an unfinished iron fence.";
/*      */         break;
/*      */       case FENCE_PLAN_IRON_GATE:
/* 1571 */         toSend = "You see an unfinished iron fence gate.";
/*      */         break;
/*      */       case FENCE_PLAN_WOVEN:
/* 1574 */         toSend = "You see an unfinished woven fence.";
/*      */         break;
/*      */       case FENCE_PLAN_STONE:
/* 1577 */         toSend = "You see an unfinished stone fence.";
/*      */         break;
/*      */       case FENCE_PLAN_CURB:
/* 1580 */         toSend = "You see an unfinished curb.";
/*      */         break;
/*      */       case FENCE_PLAN_ROPE_LOW:
/* 1583 */         toSend = "You see an unfinished low rope fence.";
/*      */         break;
/*      */       case FENCE_PLAN_ROPE_HIGH:
/* 1586 */         toSend = "You see an unfinished high rope fence.";
/*      */         break;
/*      */       case FENCE_PLAN_GARDESGARD_HIGH:
/* 1589 */         toSend = "You see an unfinished high roundpole fence.";
/*      */         break;
/*      */       case FENCE_PLAN_GARDESGARD_LOW:
/* 1592 */         toSend = "You see an unfinished low roundpole fence.";
/*      */         break;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1598 */     int[] tNeeded = Fence.getConstructionMaterialsNeededTotal(target);
/* 1599 */     if (tNeeded.length <= 0)
/*      */     {
/* 1601 */       return toSend;
/*      */     }
/*      */     
/* 1604 */     if (tNeeded[0] == -1) {
/*      */       
/* 1606 */       logger.log(Level.WARNING, "Weird. This shouldn't happen. The fence is finished, of type " + type + " and state " + state, new Exception());
/*      */ 
/*      */ 
/*      */       
/* 1610 */       return toSend;
/*      */     } 
/*      */ 
/*      */     
/*      */     try {
/* 1615 */       toSend = toSend + " Total materials needed ";
/* 1616 */       for (int x = 0; x < tNeeded.length - 1; x += 2) {
/*      */         
/* 1618 */         toSend = toSend + tNeeded[x + 1] + " ";
/* 1619 */         ItemTemplate template = ItemTemplateFactory.getInstance().getTemplate(tNeeded[x]);
/* 1620 */         if (!template.getName().endsWith("s") && tNeeded[x + 1] > 1) {
/*      */           
/* 1622 */           toSend = toSend + getMaterialName(template) + "s";
/*      */         }
/*      */         else {
/*      */           
/* 1626 */           toSend = toSend + getMaterialName(template);
/*      */         } 
/* 1628 */         if (x < tNeeded.length - 2)
/*      */         {
/* 1630 */           toSend = toSend + " and ";
/*      */         }
/*      */       } 
/* 1633 */       toSend = toSend + ".";
/* 1634 */       if (tNeeded.length > 2)
/*      */       {
/* 1636 */         toSend = toSend + " Current stage needs 1 ";
/* 1637 */         int[] materials = Fence.getItemTemplatesNeededForFence(target);
/* 1638 */         for (int i = 0; i < materials.length; i++) {
/*      */           
/* 1640 */           ItemTemplate template = ItemTemplateFactory.getInstance().getTemplate(materials[i]);
/*      */           
/* 1642 */           toSend = toSend + getMaterialName(template);
/* 1643 */           if (i < materials.length - 1)
/* 1644 */             toSend = toSend + " and "; 
/*      */         } 
/* 1646 */         toSend = toSend + ".";
/*      */       }
/*      */     
/* 1649 */     } catch (NoSuchTemplateException nst) {
/*      */       
/* 1651 */       logger.log(Level.WARNING, "Failed to locate template: " + nst.getMessage(), (Throwable)nst);
/*      */     } 
/*      */     
/* 1654 */     return toSend;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   private static String getFinishedString(@Nonnull Creature performer, @Nonnull Fence target, @Nullable FenceGate gate, StructureConstantsEnum type) {
/* 1663 */     String toSend = "";
/*      */     
/* 1665 */     Communicator comm = performer.getCommunicator();
/*      */     
/* 1667 */     toSend = getDescription(target, gate, type);
/*      */     
/* 1669 */     comm.sendNormalServerMessage(toSend);
/* 1670 */     toSend = "";
/* 1671 */     if (target.isGate()) {
/*      */       
/* 1673 */       sendGateDescription(target, gate, comm);
/*      */       
/* 1675 */       sendLockDescription(target, gate, performer, comm);
/*      */       
/* 1677 */       if (performer.getPower() > 0)
/*      */       {
/* 1679 */         comm.sendNormalServerMessage("State=" + target.getState() + " inner x=" + gate
/* 1680 */             .getInnerTile().getTileX() + ", " + gate
/* 1681 */             .getInnerTile().getTileY() + ", outer: " + gate
/* 1682 */             .getOuterTile().getTileX() + ", y=" + gate
/* 1683 */             .getOuterTile().getTileY());
/*      */       }
/*      */     } 
/*      */     
/* 1687 */     if (target.getColor() != -1)
/*      */     {
/* 1689 */       comm.sendNormalServerMessage("Colors: R=" + 
/* 1690 */           WurmColor.getColorRed(target.getColor()) + ", G=" + 
/* 1691 */           WurmColor.getColorGreen(target.getColor()) + ", B=" + 
/* 1692 */           WurmColor.getColorBlue(target.getColor()) + ".");
/*      */     }
/*      */     
/* 1695 */     toSend = "";
/*      */ 
/*      */ 
/*      */     
/* 1699 */     comm.sendNormalServerMessage("QL=" + target.getQualityLevel() + ", dam=" + target.getDamage());
/*      */     
/* 1701 */     return toSend;
/*      */   }
/*      */ 
/*      */   
/*      */   private static String getDescription(Fence target, FenceGate gate, StructureConstantsEnum type) {
/* 1706 */     String toSend = "";
/* 1707 */     if (!target.isGate()) {
/*      */       
/* 1709 */       toSend = getFenceDescription(type);
/*      */     }
/*      */     else {
/*      */       
/* 1713 */       toSend = getFenceGateDescription(gate, type);
/*      */     } 
/*      */     
/* 1716 */     if (target.isFlowerbed() && toSend.isEmpty())
/*      */     {
/* 1718 */       toSend = "A flowerbed filled with unknown flowers.";
/*      */     }
/*      */     
/* 1721 */     if (target.isLowHedge()) {
/*      */       
/* 1723 */       if (type != StructureConstantsEnum.HEDGE_FLOWER1_LOW) {
/*      */         
/* 1725 */         toSend = "This low hedge is growing steadily.";
/*      */       }
/*      */       else {
/*      */         
/* 1729 */         toSend = "This pretty lavender hedge will probably not grow further.";
/*      */       } 
/* 1731 */       return toSend;
/*      */     } 
/*      */     
/* 1734 */     if (target.isMediumHedge())
/*      */     {
/* 1736 */       return "This medium sized hedge is growing steadily.";
/*      */     }
/*      */     
/* 1739 */     if (target.isHighHedge())
/*      */     {
/* 1741 */       return "This hedge seems to be at peak height.";
/*      */     }
/*      */     
/* 1744 */     if (toSend.isEmpty()) {
/*      */       
/* 1746 */       toSend = "Unknown fence type.\n";
/* 1747 */       logger.log(Level.WARNING, "Missing fence description for type: " + type);
/*      */     } 
/*      */     
/* 1750 */     return toSend;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static void sendLockDescription(Fence target, FenceGate gate, Creature performer, Communicator comm) {
/*      */     try {
/* 1757 */       String toSend = "";
/* 1758 */       Item lock = gate.getLock();
/* 1759 */       String lockStrength = lock.getLockStrength();
/*      */       
/* 1761 */       comm.sendNormalServerMessage("You see a gate with a lock. The lock is of " + lockStrength + " quality.");
/*      */       
/* 1763 */       if (performer.getPower() >= 5)
/*      */       {
/* 1765 */         comm.sendNormalServerMessage("Lock WurmId=" + lock.getWurmId() + ", dam=" + lock.getDamage());
/*      */       }
/*      */       
/* 1768 */       if (gate.getLockCounter() > 0) {
/*      */         
/* 1770 */         comm.sendNormalServerMessage("The gate is picked open and will shut in " + gate.getLockCounterTime());
/*      */       }
/* 1772 */       else if (lock.isLocked()) {
/*      */         
/* 1774 */         toSend = toSend + "It is locked.";
/*      */       }
/*      */       else {
/*      */         
/* 1778 */         toSend = toSend + "It is unlocked.";
/*      */       } 
/*      */       
/* 1781 */       comm.sendNormalServerMessage(toSend);
/*      */       
/* 1783 */       if (performer.getPower() > 1)
/*      */       {
/* 1785 */         String ownerName = "unknown";
/* 1786 */         PlayerInfo info = PlayerInfoFactory.getPlayerInfoWithWurmId(lock.getLastOwnerId());
/* 1787 */         if (info != null)
/*      */         {
/* 1789 */           ownerName = info.getName();
/*      */         }
/* 1791 */         comm.sendNormalServerMessage("Last lock owner: " + ownerName);
/*      */       }
/*      */     
/* 1794 */     } catch (NoSuchLockException noSuchLockException) {}
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void sendGateDescription(Fence target, FenceGate gate, Communicator comm) {
/* 1802 */     String name = gate.getName();
/* 1803 */     String toSend = "";
/*      */     
/* 1805 */     toSend = toSend + "A plaque is attached to it:";
/* 1806 */     comm.sendNormalServerMessage(toSend);
/* 1807 */     comm.sendNormalServerMessage("-----------------");
/*      */     
/* 1809 */     toSend = "";
/* 1810 */     if (name.length() > 0) {
/*      */       
/* 1812 */       toSend = toSend + "Named: \"" + name + "\"";
/* 1813 */       comm.sendNormalServerMessage(toSend);
/*      */     } 
/*      */     
/* 1816 */     boolean showOwner = true;
/*      */     
/* 1818 */     if (gate.isManaged()) {
/*      */       
/*      */       try {
/*      */ 
/*      */         
/* 1823 */         Village vc = Villages.getVillage(gate.getVillageId());
/* 1824 */         comm.sendNormalServerMessage(vc.getMotto());
/* 1825 */         showOwner = false;
/*      */       }
/* 1827 */       catch (NoSuchVillageException e) {
/*      */ 
/*      */         
/* 1830 */         gate.setIsManaged(false, null);
/* 1831 */         target.savePermissions();
/*      */       } 
/*      */     }
/*      */     
/* 1835 */     if (showOwner) {
/*      */       
/* 1837 */       long ownerId = gate.getOwnerId();
/* 1838 */       if (ownerId != -10L) {
/*      */         
/* 1840 */         String owner = PlayerInfoFactory.getPlayerName(ownerId);
/* 1841 */         comm.sendNormalServerMessage("Owner:" + owner);
/*      */       } 
/*      */     } 
/*      */     
/* 1845 */     comm.sendNormalServerMessage("-----------------");
/*      */   }
/*      */ 
/*      */   
/*      */   private static String getFenceGateDescription(FenceGate gate, StructureConstantsEnum type) {
/* 1850 */     Village village = (gate != null) ? gate.getVillage() : null;
/* 1851 */     boolean noVillage = (village == null);
/* 1852 */     String villageName = noVillage ? null : village.getName();
/* 1853 */     String toSend = "";
/* 1854 */     switch (type) {
/*      */ 
/*      */       
/*      */       case FENCE_PALISADE_GATE:
/* 1858 */         if (noVillage) {
/* 1859 */           toSend = "You see a wooden palisade gate.\n"; break;
/*      */         } 
/* 1861 */         toSend = "You see a wooden palisade gate in the settlement of " + villageName + ".";
/*      */         break;
/*      */       case FENCE_WOODEN_GATE:
/* 1864 */         if (noVillage) {
/* 1865 */           toSend = "You see a wooden fence gate.\n"; break;
/*      */         } 
/* 1867 */         toSend = "You see a wooden fence gate in the settlement of " + villageName + ".\n";
/*      */         break;
/*      */       case FENCE_IRON_GATE:
/*      */       case FENCE_IRON_GATE_HIGH:
/*      */       case FENCE_SLATE_IRON_GATE:
/*      */       case FENCE_ROUNDED_STONE_IRON_GATE:
/*      */       case FENCE_POTTERY_IRON_GATE:
/*      */       case FENCE_SANDSTONE_IRON_GATE:
/*      */       case FENCE_RENDERED_IRON_GATE:
/*      */       case FENCE_MARBLE_IRON_GATE:
/*      */       case FENCE_SLATE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_ROUNDED_STONE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_SANDSTONE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_RENDERED_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_POTTERY_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_MARBLE_HIGH_IRON_FENCE_GATE:
/* 1883 */         if (noVillage) {
/* 1884 */           toSend = "You see an iron fence gate.\n"; break;
/*      */         } 
/* 1886 */         toSend = "You see an iron fence gate in the settlement of " + villageName + ".\n";
/*      */         break;
/*      */       case FENCE_WOODEN_CRUDE_GATE:
/* 1889 */         if (noVillage) {
/* 1890 */           toSend = "You see a crude wooden fence gate.\n"; break;
/*      */         } 
/* 1892 */         toSend = "You see a crude wooden fence gate in the settlement of " + villageName + ".\n";
/*      */         break;
/*      */       case FENCE_GARDESGARD_GATE:
/* 1895 */         if (noVillage) {
/* 1896 */           toSend = "You see a wooden roundpole fence gate.\n"; break;
/*      */         } 
/* 1898 */         toSend = "You see a wooden roundpole fence gate in the settlement of " + villageName + ".\n";
/*      */         break;
/*      */       case FENCE_PORTCULLIS:
/*      */       case FENCE_SLATE_PORTCULLIS:
/*      */       case FENCE_ROUNDED_STONE_PORTCULLIS:
/*      */       case FENCE_SANDSTONE_PORTCULLIS:
/*      */       case FENCE_RENDERED_PORTCULLIS:
/*      */       case FENCE_POTTERY_PORTCULLIS:
/*      */       case FENCE_MARBLE_PORTCULLIS:
/* 1907 */         if (noVillage) {
/* 1908 */           toSend = "You see a portcullis.\n"; break;
/*      */         } 
/* 1910 */         toSend = "You see a portcullis in the settlement of " + villageName + ".\n";
/*      */         break;
/*      */     } 
/*      */     
/* 1914 */     return toSend;
/*      */   }
/*      */ 
/*      */   
/*      */   private static String getFenceDescription(StructureConstantsEnum type) {
/* 1919 */     String toSend = "";
/* 1920 */     switch (type) {
/*      */ 
/*      */       
/*      */       case FENCE_PALISADE:
/* 1924 */         toSend = "You see a sturdy wooden palisade.";
/*      */         break;
/*      */       case FENCE_STONEWALL:
/* 1927 */         toSend = "You see a strong stone wall.";
/*      */         break;
/*      */       case FENCE_WOODEN:
/* 1930 */         toSend = "You see a wooden fence.";
/*      */         break;
/*      */       case FENCE_WOODEN_CRUDE:
/* 1933 */         toSend = "You see a crude wooden fence.";
/*      */         break;
/*      */       case FENCE_GARDESGARD_LOW:
/* 1936 */         toSend = "You see a low wooden roundpole fence.";
/*      */         break;
/*      */       case FENCE_GARDESGARD_HIGH:
/* 1939 */         toSend = "You see a high wooden roundpole fence";
/*      */         break;
/*      */       case FENCE_IRON:
/*      */       case FENCE_SLATE_IRON:
/*      */       case FENCE_ROUNDED_STONE_IRON:
/*      */       case FENCE_POTTERY_IRON:
/*      */       case FENCE_SANDSTONE_IRON:
/*      */       case FENCE_RENDERED_IRON:
/*      */       case FENCE_MARBLE_IRON:
/* 1948 */         toSend = "You see an iron fence.";
/*      */         break;
/*      */       case FENCE_IRON_HIGH:
/*      */       case FENCE_SLATE_HIGH_IRON_FENCE:
/*      */       case FENCE_ROUNDED_STONE_HIGH_IRON_FENCE:
/*      */       case FENCE_SANDSTONE_HIGH_IRON_FENCE:
/*      */       case FENCE_RENDERED_HIGH_IRON_FENCE:
/*      */       case FENCE_POTTERY_HIGH_IRON_FENCE:
/*      */       case FENCE_MARBLE_HIGH_IRON_FENCE:
/* 1957 */         toSend = "You see an high iron fence";
/*      */         break;
/*      */       case FENCE_WOVEN:
/* 1960 */         toSend = "This woven fence is purely a decoration and stops neither creature nor man.";
/*      */         break;
/*      */       case FENCE_WOODEN_PARAPET:
/* 1963 */         toSend = "You see a wooden parapet.";
/*      */         break;
/*      */       case FENCE_STONE_PARAPET:
/*      */       case FENCE_SANDSTONE_STONE_PARAPET:
/*      */       case FENCE_SLATE_STONE_PARAPET:
/*      */       case FENCE_ROUNDED_STONE_STONE_PARAPET:
/*      */       case FENCE_RENDERED_STONE_PARAPET:
/*      */       case FENCE_POTTERY_STONE_PARAPET:
/*      */       case FENCE_MARBLE_STONE_PARAPET:
/* 1972 */         toSend = "You see a strong stone parapet.";
/*      */         break;
/*      */       case FENCE_STONE_IRON_PARAPET:
/* 1975 */         toSend = "You see a strong parapet made from stone and iron.";
/*      */         break;
/*      */       case FENCE_MEDIUM_CHAIN:
/*      */       case FENCE_SLATE_CHAIN_FENCE:
/*      */       case FENCE_ROUNDED_STONE_CHAIN_FENCE:
/*      */       case FENCE_SANDSTONE_CHAIN_FENCE:
/*      */       case FENCE_RENDERED_CHAIN_FENCE:
/*      */       case FENCE_POTTERY_CHAIN_FENCE:
/*      */       case FENCE_MARBLE_CHAIN_FENCE:
/* 1984 */         toSend = "You see a chain fence.";
/*      */         break;
/*      */       case FENCE_STONEWALL_HIGH:
/*      */       case FENCE_SLATE_TALL_STONE_WALL:
/*      */       case FENCE_ROUNDED_STONE_TALL_STONE_WALL:
/*      */       case FENCE_SANDSTONE_TALL_STONE_WALL:
/*      */       case FENCE_RENDERED_TALL_STONE_WALL:
/*      */       case FENCE_POTTERY_TALL_STONE_WALL:
/*      */       case FENCE_MARBLE_TALL_STONE_WALL:
/* 1993 */         toSend = "You see a strong tall stone wall.";
/*      */         break;
/*      */       case FENCE_STONE:
/* 1996 */         toSend = "You see a stone fence.";
/*      */         break;
/*      */       case FENCE_SLATE:
/*      */       case FENCE_ROUNDED_STONE:
/*      */       case FENCE_POTTERY:
/*      */       case FENCE_SANDSTONE:
/*      */       case FENCE_RENDERED:
/*      */       case FENCE_MARBLE:
/* 2004 */         toSend = "You see a strong fence.";
/*      */         break;
/*      */       case FENCE_CURB:
/* 2007 */         toSend = "You see a curb.";
/*      */         break;
/*      */       case FENCE_ROPE_LOW:
/* 2010 */         toSend = "You see a low rope fence.";
/*      */         break;
/*      */       case FENCE_ROPE_HIGH:
/* 2013 */         toSend = "You see a high rope fence.";
/*      */         break;
/*      */       case FENCE_MAGIC_STONE:
/* 2016 */         toSend = "This stone wall is magic! You can see how it slowly crumbles as the weave disperses the Source.";
/*      */         break;
/*      */       case FENCE_MAGIC_FIRE:
/* 2019 */         toSend = "This wall of fire is magic! You can see how it slowly dissipates as the weave disperses the Source.";
/*      */         break;
/*      */       case FENCE_MAGIC_ICE:
/* 2022 */         toSend = "This ice wall is magic! You can see how it slowly melts as the weave disperses the Source.";
/*      */         break;
/*      */       case FLOWERBED_BLUE:
/* 2025 */         toSend = "A flowerbed filled with crooked but beautiful blue flowers.";
/*      */         break;
/*      */       case FLOWERBED_GREENISH_YELLOW:
/* 2028 */         toSend = "A flowerbed filled with greenish-yellow furry flowers.";
/*      */         break;
/*      */       case FLOWERBED_ORANGE_RED:
/* 2031 */         toSend = "A flowerbed filled with long-stemmed orange-red flowers with thick, pointy leaves.";
/*      */         break;
/*      */       case FLOWERBED_PURPLE:
/* 2034 */         toSend = "A flowerbed filled with purple fluffy flowers.";
/*      */         break;
/*      */       case FLOWERBED_WHITE:
/* 2037 */         toSend = "A flowerbed filled with thick-stemmed white flowers with long leaves.";
/*      */         break;
/*      */       case FLOWERBED_WHITE_DOTTED:
/* 2040 */         toSend = "A flowerbed filled with uncommon white-dotted flowers.";
/*      */         break;
/*      */       case FLOWERBED_YELLOW:
/* 2043 */         toSend = "A flowerbed filled with yellow prickly flowers.";
/*      */         break;
/*      */     } 
/*      */ 
/*      */     
/* 2048 */     return toSend;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\FenceBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */