/*      */ package com.wurmonline.server.items;
/*      */ 
/*      */ import com.wurmonline.server.FailedException;
/*      */ import com.wurmonline.server.HistoryManager;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.behaviours.Action;
/*      */ import com.wurmonline.server.behaviours.Actions;
/*      */ import com.wurmonline.server.behaviours.MethodsItems;
/*      */ import com.wurmonline.server.behaviours.NoSuchActionException;
/*      */ import com.wurmonline.server.behaviours.Terraforming;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.deities.Deities;
/*      */ import com.wurmonline.server.endgames.EndGameItem;
/*      */ import com.wurmonline.server.endgames.EndGameItems;
/*      */ import com.wurmonline.server.epic.EpicMission;
/*      */ import com.wurmonline.server.epic.EpicServerStatus;
/*      */ import com.wurmonline.server.epic.EpicTargetItems;
/*      */ import com.wurmonline.server.epic.MissionHelper;
/*      */ import com.wurmonline.server.kingdom.GuardTower;
/*      */ import com.wurmonline.server.kingdom.Kingdom;
/*      */ import com.wurmonline.server.kingdom.Kingdoms;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.skills.NoSuchSkillException;
/*      */ import com.wurmonline.server.skills.Skill;
/*      */ import com.wurmonline.server.skills.SkillList;
/*      */ import com.wurmonline.server.skills.Skills;
/*      */ import com.wurmonline.server.tutorial.MissionTriggers;
/*      */ import com.wurmonline.server.utils.StringUtil;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import java.util.HashSet;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class AdvancedCreationEntry
/*      */   extends CreationEntry
/*      */ {
/*   69 */   private static final Logger logger = Logger.getLogger(AdvancedCreationEntry.class.getName());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Set<CreationRequirement> requirements;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AdvancedCreationEntry(int aPrimarySkill, int aObjectSource, int aObjectTarget, int aObjectCreated, boolean aDestroyTarget, boolean aUseCapacity, float aPercentageLost, int aMinTimeSeconds, boolean aDestroyBoth, boolean aCreateOnGround, CreationCategories aCategory) {
/*   93 */     super(aPrimarySkill, aObjectSource, aObjectTarget, aObjectCreated, aDestroyTarget, aUseCapacity, aPercentageLost, aMinTimeSeconds, aDestroyBoth, aCreateOnGround, aCategory);
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
/*      */   public AdvancedCreationEntry(int aPrimarySkill, int aObjectSource, int aObjectTarget, int aObjectCreated, boolean aDepleteSource, boolean aDepleteTarget, float aPercentageLost, boolean aDepleteBoth, boolean aCreateOnGround, CreationCategories aCategory) {
/*  116 */     super(aPrimarySkill, aObjectSource, aObjectTarget, aObjectCreated, aDepleteSource, aDepleteTarget, aDepleteBoth, aPercentageLost, aCreateOnGround, aCategory);
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
/*      */   public AdvancedCreationEntry(int aPrimarySkill, int aObjectSource, int aObjectTarget, int aObjectCreated, boolean aDepleteSource, boolean aDepleteTarget, float aPercentageLost, boolean aDepleteBoth, boolean aCreateOnGround, int aCustomCutOffChance, double aMinimumSkill, CreationCategories aCategory) {
/*  141 */     super(aPrimarySkill, aObjectSource, aObjectTarget, aObjectCreated, aDepleteSource, aDepleteTarget, aDepleteBoth, aPercentageLost, aCreateOnGround, aCustomCutOffChance, aMinimumSkill, aCategory);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAdvanced() {
/*  147 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CreationEntry cloneAndRevert() {
/*  155 */     CreationEntry toReturn = new AdvancedCreationEntry(getPrimarySkill(), getObjectTarget(), getObjectSource(), getObjectCreated(), isDestroyTarget(), isUseCapacity(), getPercentageLost(), getMinTimeSeconds(), isDestroyBoth(), isCreateOnGround(), getCategory());
/*  156 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public AdvancedCreationEntry addRequirement(CreationRequirement requirement) {
/*  161 */     if (this.requirements == null)
/*  162 */       this.requirements = new HashSet<>(); 
/*  163 */     this.requirements.add(requirement);
/*  164 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CreationRequirement[] getRequirements() {
/*  173 */     if (this.requirements == null) {
/*  174 */       return emptyReqs;
/*      */     }
/*  176 */     return this.requirements.<CreationRequirement>toArray(new CreationRequirement[this.requirements.size()]);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isRequirementFilled(CreationRequirement requirement, Creature creature) {
/*  181 */     boolean toReturn = false;
/*      */     
/*  183 */     return false;
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
/*      */   public boolean areRequirementsFilled(Item target) {
/*  214 */     CreationRequirement[] reqs = getRequirements();
/*  215 */     for (CreationRequirement lReq : reqs) {
/*  216 */       if (getStateForRequirement(lReq, target) < lReq.getResourceNumber())
/*  217 */         return false; 
/*  218 */     }  return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isItemNeeded(Item item, Item target) {
/*  223 */     CreationRequirement[] reqs = getRequirements();
/*  224 */     for (CreationRequirement lReq : reqs) {
/*      */       
/*  226 */       if (item.getTemplateId() == lReq.getResourceTemplateId())
/*      */       {
/*  228 */         return (getStateForRequirement(lReq, target) < lReq.getResourceNumber());
/*      */       }
/*      */     } 
/*  231 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public CreationRequirement getRequirementForItem(Item item) {
/*  236 */     CreationRequirement[] reqs = getRequirements();
/*  237 */     for (CreationRequirement lReq : reqs) {
/*      */       
/*  239 */       if (item.getTemplateId() == lReq.getResourceTemplateId())
/*  240 */         return lReq; 
/*      */     } 
/*  242 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean runThroughRequirement(CreationRequirement req, Creature performer, Item target) {
/*  247 */     boolean toReturn = false;
/*  248 */     int state = getStateForRequirement(req, target);
/*  249 */     if (state != req.getResourceNumber())
/*      */     {
/*  251 */       if (req.runOnce(performer))
/*  252 */         setStateForRequirement(req, ++state, target); 
/*      */     }
/*  254 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int getTemplateId(Item target) {
/*  259 */     if (target.realTemplate > 0)
/*  260 */       return target.realTemplate; 
/*  261 */     int data = target.getData1();
/*  262 */     int tid = data >> 16 & 0xFFFF;
/*  263 */     return tid;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void setTemplateId(Item target, int templateId) {
/*  268 */     int data = target.getData1();
/*  269 */     int reqOne = data & 0xFFFF;
/*  270 */     int toSet = templateId << 16;
/*  271 */     data = toSet + reqOne;
/*  272 */     target.setData1(data);
/*  273 */     target.setRealTemplate(templateId);
/*      */   }
/*      */ 
/*      */   
/*      */   public String getItemsLeft(Item target) {
/*  278 */     ItemTemplate template = null;
/*      */     
/*      */     try {
/*  281 */       template = ItemTemplateFactory.getInstance().getTemplate(this.objectCreated);
/*      */     }
/*  283 */     catch (NoSuchTemplateException nst) {
/*      */       
/*  285 */       logger.log(Level.WARNING, "No template with id " + this.objectCreated);
/*      */     } 
/*  287 */     CreationRequirement[] reqs = getRequirements();
/*  288 */     StringBuilder buf = new StringBuilder();
/*  289 */     buf.append("The ");
/*  290 */     buf.append((template != null) ? template.getName() : "");
/*  291 */     if (template != null && template.getName().charAt(template.getName().length() - 1) == 's') {
/*  292 */       buf.append(" need ");
/*      */     } else {
/*  294 */       buf.append(" needs ");
/*  295 */     }  int itemsNeeded = 0;
/*  296 */     for (CreationRequirement lReq : reqs) {
/*      */       
/*  298 */       int rest = lReq.getResourceNumber() - getStateForRequirement(lReq, target);
/*  299 */       if (rest > 0) {
/*      */         
/*  301 */         int templateNeeded = lReq.getResourceTemplateId();
/*  302 */         ItemTemplate needed = null;
/*      */         
/*      */         try {
/*  305 */           needed = ItemTemplateFactory.getInstance().getTemplate(templateNeeded);
/*      */         }
/*  307 */         catch (NoSuchTemplateException nst) {
/*      */           
/*  309 */           logger.log(Level.WARNING, "No template with id " + templateNeeded);
/*  310 */           return "You can't figure out what is needed to complete this object.";
/*      */         } 
/*      */ 
/*      */         
/*  314 */         if (itemsNeeded > 0)
/*  315 */           buf.append(", and "); 
/*  316 */         buf.append(rest);
/*  317 */         buf.append(" ");
/*  318 */         buf.append(needed.sizeString);
/*  319 */         if (needed.isMetal() && needed.getTemplateId() != 897)
/*      */         {
/*  321 */           if (needed.getMaterial() != 0) {
/*      */             
/*  323 */             buf.append(Item.getMaterialString(needed.getMaterial()));
/*  324 */             buf.append(" ");
/*      */           } else {
/*      */             
/*  327 */             buf.append("metal ");
/*      */           }  } 
/*  329 */         buf.append(needed.getName());
/*  330 */         itemsNeeded++;
/*      */       } 
/*      */     } 
/*  333 */     if (itemsNeeded == 0)
/*  334 */       buf.append("no more items"); 
/*  335 */     buf.append(" to be finished.");
/*  336 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   public static int getStateForRequirement(CreationRequirement req, Item target) {
/*  341 */     int data = -1;
/*  342 */     int numberDone = -1;
/*  343 */     if (req.getNumber() == 1) {
/*      */       
/*  345 */       data = target.getData1();
/*  346 */       data = Math.max(0, data);
/*  347 */       numberDone = data & 0xFFFF;
/*      */     }
/*  349 */     else if (req.getNumber() == 2) {
/*      */       
/*  351 */       data = target.getData2();
/*  352 */       data = Math.max(0, data);
/*  353 */       numberDone = data >> 16 & 0xFFFF;
/*      */     }
/*  355 */     else if (req.getNumber() == 3) {
/*      */       
/*  357 */       data = target.getData2();
/*  358 */       data = Math.max(0, data);
/*  359 */       numberDone = data & 0xFFFF;
/*      */     }
/*      */     else {
/*      */       
/*  363 */       return ItemRequirement.getStateForRequirement(req.getResourceTemplateId(), target.getWurmId());
/*      */     } 
/*  365 */     return numberDone;
/*      */   }
/*      */ 
/*      */   
/*      */   private static void setStateForRequirement(CreationRequirement req, int state, Item target) {
/*  370 */     int data = -1;
/*  371 */     if (req.getNumber() == 1) {
/*      */       
/*  373 */       data = target.getData1();
/*  374 */       int tid = data >> 16 & 0xFFFF;
/*  375 */       int toSet = state & 0xFFFF;
/*      */       
/*  377 */       data = ((tid & 0xFFFF) << 16) + toSet;
/*  378 */       target.setData1(data);
/*      */     }
/*  380 */     else if (req.getNumber() == 2) {
/*      */       
/*  382 */       data = target.getData2();
/*  383 */       int reqthree = data & 0xFFFF;
/*  384 */       int toSet = (state & 0xFFFF) << 16;
/*  385 */       data = reqthree + toSet;
/*  386 */       target.setData2(data);
/*      */     }
/*  388 */     else if (req.getNumber() == 3) {
/*      */       
/*  390 */       data = target.getData2();
/*  391 */       int reqtwo = data >> 16 & 0xFFFF;
/*  392 */       int toSet = state & 0xFFFF;
/*  393 */       data = ((reqtwo & 0xFFFF) << 16) + toSet;
/*  394 */       target.setData2(data);
/*      */     }
/*      */     else {
/*      */       
/*  398 */       ItemRequirement.setRequirements(target.getWurmId(), req.getResourceTemplateId(), state, true, false);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int getTotalNumberOfItems() {
/*  405 */     int toReturn = 0;
/*  406 */     if (this.requirements != null)
/*      */     {
/*  408 */       for (CreationRequirement req : this.requirements)
/*      */       {
/*  410 */         toReturn += req.getResourceNumber();
/*      */       }
/*      */     }
/*      */     
/*  414 */     toReturn++;
/*  415 */     if (this.depleteTarget && this.depleteSource) {
/*      */ 
/*      */       
/*  418 */       toReturn++;
/*      */     }
/*  420 */     else if (this.destroyBoth) {
/*  421 */       toReturn++;
/*  422 */     }  return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Item cont(Creature performer, Item source, long targetId, float counter) throws FailedException, NoSuchSkillException, NoSuchItemException, IllegalArgumentException {
/*  428 */     Item target = Items.getItem(targetId);
/*  429 */     Item realSource = source;
/*  430 */     Item realTarget = target;
/*  431 */     ItemTemplate template = null;
/*  432 */     boolean failed = false;
/*      */     
/*      */     try {
/*  435 */       template = ItemTemplateFactory.getInstance().getTemplate(this.objectCreated);
/*      */     }
/*  437 */     catch (NoSuchTemplateException nst) {
/*      */       
/*  439 */       logger.log(Level.WARNING, performer.getName() + " - no template with id " + this.objectCreated);
/*  440 */       performer.getCommunicator().sendAlertServerMessage("You cannot continue your work, since nobody knows how to create these any longer.");
/*      */       
/*  442 */       throw new NoSuchItemException("No such template");
/*      */     } 
/*      */     
/*  445 */     if (isRestrictedToDeityFollower())
/*      */     {
/*  447 */       if (performer.getDeity() == null || performer.getDeity().getTemplateDeity() != getDeityRestriction()) {
/*      */         
/*  449 */         String deityName = Deities.getDeityName(getDeityRestriction());
/*  450 */         String message = "You must be a follower of %s or one of their demigods to continue on this item.";
/*  451 */         performer.getCommunicator().sendNormalServerMessage(StringUtil.format("You must be a follower of %s or one of their demigods to continue on this item.", new Object[] { deityName }));
/*  452 */         throw new NoSuchItemException("Incorrect deity");
/*      */       } 
/*      */     }
/*      */     
/*  456 */     int templateId = -1;
/*  457 */     if (target.getTemplateId() == 179) {
/*      */       
/*  459 */       templateId = getTemplateId(target);
/*  460 */       if (templateId == this.objectCreated)
/*      */       {
/*  462 */         realTarget = target;
/*  463 */         realSource = source;
/*      */       }
/*      */     
/*  466 */     } else if (source.getTemplateId() == 179) {
/*      */       
/*  468 */       templateId = getTemplateId(source);
/*  469 */       if (templateId == this.objectCreated) {
/*      */         
/*  471 */         realTarget = source;
/*  472 */         realSource = target;
/*      */       } 
/*      */     } 
/*      */     
/*  476 */     CreationRequirement req = getRequirementForItem(realSource);
/*  477 */     if (req == null) {
/*      */       
/*  479 */       performer.getCommunicator().sendNormalServerMessage("This item will not benefit from adding " + realSource
/*  480 */           .getNameWithGenus() + ".");
/*  481 */       throw new NoSuchItemException("This item will not benefit from adding " + realSource.getNameWithGenus() + ".");
/*      */     } 
/*  483 */     int state = getStateForRequirement(req, realTarget);
/*  484 */     if (state >= req.getResourceNumber()) {
/*      */       
/*  486 */       performer.getCommunicator().sendNormalServerMessage("This item will not benefit from adding " + realSource
/*  487 */           .getNameWithGenus() + ".");
/*  488 */       throw new NoSuchItemException("This item will not benefit from adding " + realSource.getNameWithGenus() + ".");
/*      */     } 
/*  490 */     if (realTarget.getDamage() > 0.0F) {
/*      */       
/*  492 */       String message = "You must repair the " + realTarget.getName() + " before continuing.";
/*  493 */       performer.getCommunicator().sendNormalServerMessage(message);
/*  494 */       throw new NoSuchItemException(message);
/*      */     } 
/*  496 */     Skills skills = performer.getSkills();
/*  497 */     Skill primSkill = null;
/*  498 */     Action act = null;
/*      */     
/*      */     try {
/*  501 */       act = performer.getCurrentAction();
/*      */     }
/*  503 */     catch (NoSuchActionException nsa) {
/*      */       
/*  505 */       logger.log(Level.WARNING, "This action doesn't exist? " + performer.getName(), (Throwable)nsa);
/*  506 */       throw new NoSuchItemException("An error occured on the server. This action was not found. Please report.");
/*      */     } 
/*  508 */     float failSecond = Float.MAX_VALUE;
/*      */     
/*      */     try {
/*  511 */       primSkill = skills.getSkill(this.primarySkill);
/*      */     }
/*  513 */     catch (Exception ex) {
/*      */       
/*  515 */       primSkill = skills.learn(this.primarySkill, 1.0F);
/*      */     } 
/*  517 */     if (hasMinimumSkillRequirement() && primSkill.getKnowledge(0.0D) < getMinimumSkillRequirement()) {
/*      */       
/*  519 */       performer.getCommunicator().sendNormalServerMessage("You are not skilled enough to continue building the " + template
/*  520 */           .getName() + ".");
/*  521 */       throw new NoSuchItemException("Not skilled enough.");
/*      */     } 
/*  523 */     int time = 10;
/*      */     
/*  525 */     if (counter == 1.0F) {
/*      */       
/*  527 */       if (template.onePerTile || this.createOnGround)
/*      */       {
/*  529 */         if (!MethodsItems.mayDropOnTile(performer)) {
/*      */           
/*  531 */           performer.getCommunicator().sendNormalServerMessage("You cannot create that item here, since there is not enough space in front of you.");
/*      */           
/*  533 */           throw new NoSuchItemException("Already contains a large item.");
/*      */         } 
/*      */       }
/*  536 */       if (template.isOutsideOnly()) {
/*      */         
/*  538 */         VolaTile t = performer.getCurrentTile();
/*  539 */         if (t != null && 
/*  540 */           t.getStructure() != null)
/*      */         {
/*  542 */           if (t.getStructure().isTypeBridge()) {
/*      */             
/*  544 */             if (performer.getBridgeId() != -10L)
/*      */             {
/*  546 */               performer.getCommunicator().sendNormalServerMessage("You cannot create that item on a bridge.");
/*  547 */               throw new NoSuchItemException("Can't create on a bridge.");
/*      */             }
/*      */           
/*      */           } else {
/*      */             
/*  552 */             performer.getCommunicator().sendNormalServerMessage("You cannot create that item inside.");
/*  553 */             throw new NoSuchItemException("Can't create inside.");
/*      */           }
/*      */         
/*      */         }
/*  557 */       } else if (template.insideOnly) {
/*      */         
/*  559 */         VolaTile t = performer.getCurrentTile();
/*      */         
/*  561 */         if (t == null || t.getStructure() == null) {
/*      */           
/*  563 */           performer.getCommunicator().sendNormalServerMessage("You must create that item inside.");
/*  564 */           throw new NoSuchItemException("Must create inside.");
/*      */         } 
/*      */       } 
/*      */       
/*  568 */       if (template.isDomainItem())
/*      */       {
/*  570 */         if (performer.getDeity() == null) {
/*      */           
/*  572 */           performer.getCommunicator().sendAlertServerMessage("You cannot continue your work, since you lack the faith needed.", (byte)3);
/*      */           
/*  574 */           throw new NoSuchItemException("No deity.");
/*      */         } 
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  596 */       if (template.nonDeedable)
/*      */       {
/*  598 */         if (performer.getCurrentVillage() != null) {
/*      */ 
/*      */           
/*  601 */           performer.getCommunicator().sendAlertServerMessage("You can't continue the " + template
/*  602 */               .getName() + " now; you can't build this in a settlement.", (byte)3);
/*      */           
/*  604 */           throw new NoSuchItemException("In settlement.");
/*      */         } 
/*      */       }
/*  607 */       if (template.isGuardTower()) {
/*      */         
/*  609 */         GuardTower.canConstructTower(performer, realTarget);
/*      */       }
/*  611 */       else if (template.protectionTower) {
/*      */         
/*  613 */         if (!performer.isOnSurface() || !target.isOnSurface()) {
/*      */           
/*  615 */           performer.getCommunicator().sendAlertServerMessage("You can't continue the tower now; you can't build this below surface.");
/*      */ 
/*      */           
/*  618 */           throw new NoSuchItemException("Below surface.");
/*      */         } 
/*  620 */         VolaTile targTile = Zones.getTileOrNull(realTarget.getTileX(), realTarget.getTileY(), true);
/*  621 */         if (targTile != null)
/*      */         {
/*  623 */           if (targTile.isTransition()) {
/*      */             
/*  625 */             performer.getCommunicator().sendAlertServerMessage("You can't continue the tower here - the foundation is not stable enough.");
/*      */ 
/*      */             
/*  628 */             throw new NoSuchItemException("On cave opening.");
/*      */           } 
/*      */         }
/*  631 */         VolaTile tile = performer.getCurrentTile();
/*  632 */         if (tile != null) {
/*      */           
/*  634 */           if (Terraforming.isTileUnderWater(1, tile.tilex, tile.tiley, performer.isOnSurface())) {
/*      */             
/*  636 */             performer.getCommunicator().sendAlertServerMessage("You can't continue the tower now; the ground is not solid here.");
/*      */ 
/*      */             
/*  639 */             throw new NoSuchItemException("Too wet.");
/*      */           } 
/*      */           
/*  642 */           int mindist = Kingdoms.minKingdomDist;
/*  643 */           if (Zones.getKingdom(tile.tilex, tile.tiley) == performer.getKingdomId()) {
/*  644 */             mindist = 60;
/*      */           }
/*  646 */           if (Kingdoms.isTowerTooNear(tile.tilex, tile.tiley, tile.isOnSurface(), template.protectionTower)) {
/*      */             
/*  648 */             performer.getCommunicator().sendAlertServerMessage("You can't continue the tower now; another tower is too near.");
/*      */ 
/*      */             
/*  651 */             throw new NoSuchItemException("Too close to another tower.");
/*      */           } 
/*  653 */           if (!template.protectionTower && 
/*  654 */             !Zones.isKingdomBlocking(tile.tilex - mindist, tile.tiley - mindist, tile.tilex + mindist, tile.tiley + mindist, performer
/*  655 */               .getKingdomId())) {
/*      */             
/*  657 */             performer.getCommunicator().sendAlertServerMessage("You can't continue the tower now; another kingdom is too near.");
/*      */ 
/*      */             
/*  660 */             throw new NoSuchItemException("Too close to another kingdom.");
/*      */           } 
/*  662 */           if (!Servers.localServer.HOMESERVER) {
/*      */             
/*  664 */             if (Terraforming.isTileModBlocked(performer, tile.tilex, tile.tiley, performer.isOnSurface()))
/*  665 */               throw new NoSuchItemException("Tile protected by the deities in this area."); 
/*  666 */             for (Item targ : Items.getWarTargets()) {
/*      */               
/*  668 */               int maxnorth = Math.max(0, tile.tiley - 60);
/*  669 */               int maxsouth = Math.min(Zones.worldTileSizeY, tile.tiley + 60);
/*  670 */               int maxwest = Math.max(0, tile.tilex - 60);
/*  671 */               int maxeast = Math.min(Zones.worldTileSizeX, tile.tilex + 60);
/*  672 */               if ((int)targ.getPosX() >> 2 > maxwest && (int)targ.getPosX() >> 2 < maxeast && 
/*  673 */                 (int)targ.getPosY() >> 2 < maxsouth && (int)targ.getPosY() >> 2 > maxnorth) {
/*      */                 
/*  675 */                 performer.getCommunicator().sendSafeServerMessage("You cannot found the tower here, since this is an active battle ground.");
/*      */                 
/*  677 */                 throw new NoSuchItemException("Too close to a war target.");
/*      */               } 
/*      */             } 
/*  680 */             EndGameItem alt = EndGameItems.getEvilAltar();
/*  681 */             if (alt != null) {
/*      */               
/*  683 */               int maxnorth = Math.max(0, tile.tiley - 100);
/*  684 */               int maxsouth = Math.min(Zones.worldTileSizeY, tile.tiley + 100);
/*  685 */               int maxeast = Math.max(0, tile.tilex - 100);
/*  686 */               int maxwest = Math.min(Zones.worldTileSizeX, tile.tilex + 100);
/*  687 */               if (alt.getItem() != null)
/*      */               {
/*  689 */                 if ((int)alt.getItem().getPosX() >> 2 < maxwest && 
/*  690 */                   (int)alt.getItem().getPosX() >> 2 > maxeast && 
/*  691 */                   (int)alt.getItem().getPosY() >> 2 < maxsouth && 
/*  692 */                   (int)alt.getItem().getPosY() >> 2 > maxnorth)
/*      */                 {
/*  694 */                   throw new NoSuchItemException("You cannot place a tower here, since this is holy ground.");
/*      */                 }
/*      */               }
/*      */             } 
/*      */             
/*  699 */             alt = EndGameItems.getGoodAltar();
/*  700 */             if (alt != null) {
/*      */               
/*  702 */               int maxnorth = Math.max(0, tile.tiley - 100);
/*  703 */               int maxsouth = Math.min(Zones.worldTileSizeY, tile.tiley + 100);
/*  704 */               int maxeast = Math.max(0, tile.tilex - 100);
/*  705 */               int maxwest = Math.min(Zones.worldTileSizeX, tile.tilex + 100);
/*  706 */               if (alt.getItem() != null)
/*      */               {
/*  708 */                 if ((int)alt.getItem().getPosX() >> 2 < maxwest && 
/*  709 */                   (int)alt.getItem().getPosX() >> 2 > maxeast && 
/*  710 */                   (int)alt.getItem().getPosY() >> 2 < maxsouth && 
/*  711 */                   (int)alt.getItem().getPosY() >> 2 > maxnorth)
/*      */                 {
/*  713 */                   throw new NoSuchItemException("You cannot place a tower here, since this is holy ground.");
/*      */                 }
/*      */               }
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/*  722 */       Item parent = null;
/*      */       
/*      */       try {
/*  725 */         parent = realTarget.getParent();
/*      */       }
/*  727 */       catch (NoSuchItemException noSuchItemException) {}
/*      */ 
/*      */ 
/*      */       
/*  731 */       if (parent != null) {
/*      */ 
/*      */         
/*  734 */         if (parent.isNoWorkParent()) {
/*      */           
/*  736 */           performer.getCommunicator().sendNormalServerMessage("You can't work with the " + realTarget
/*  737 */               .getName() + " in the " + parent.getName() + ".");
/*  738 */           throw new NoSuchItemException("The " + realTarget.getName() + " can't be modified in the " + parent
/*  739 */               .getName() + ".");
/*      */         } 
/*      */         
/*  742 */         if (template.isHollow())
/*      */         {
/*  744 */           if (template.getSizeZ() >= parent.getSizeZ() || template.getSizeY() >= parent.getSizeY() || template
/*  745 */             .getSizeX() >= parent.getSizeX()) {
/*      */             
/*  747 */             performer.getCommunicator().sendNormalServerMessage("The " + realTarget
/*  748 */                 .getName() + " will not fit in the " + parent.getName() + ".");
/*      */ 
/*      */             
/*  751 */             throw new NoSuchItemException("The " + realTarget.getName() + " will not fit in the " + parent
/*  752 */                 .getName() + ".");
/*      */           } 
/*      */         }
/*      */       } 
/*      */       
/*      */       try {
/*  758 */         if (realSource.getTemplateId() == 9) {
/*      */           
/*  760 */           if (realSource.getWeightGrams(false) < realSource.getTemplate().getWeightGrams() * 0.7D) {
/*      */             
/*  762 */             performer.getCommunicator().sendNormalServerMessage("The " + realSource
/*  763 */                 .getName() + " you try to use contains too little material. You need a heavier " + realSource
/*      */                 
/*  765 */                 .getName() + ".");
/*  766 */             throw new NoSuchItemException("Too little material");
/*      */           } 
/*      */         } else {
/*  769 */           if (realSource.getWeightGrams(false) < realSource.getTemplate().getWeightGrams()) {
/*      */             
/*  771 */             if (realSource.isCombine()) {
/*  772 */               performer
/*  773 */                 .getCommunicator()
/*  774 */                 .sendNormalServerMessage("The " + realSource
/*      */                   
/*  776 */                   .getName() + " contains too little material. Please combine with other of the same kind or select another one.");
/*      */             } else {
/*      */               
/*  779 */               performer.getCommunicator().sendNormalServerMessage("The " + realSource
/*  780 */                   .getName() + " contains too little material. Please select another one.");
/*  781 */             }  throw new NoSuchItemException("Too little material");
/*      */           } 
/*  783 */           if (realTarget.getWeightGrams(false) < realTarget.getTemplate().getWeightGrams()) {
/*      */             
/*  785 */             if (realTarget.isCombine()) {
/*  786 */               performer.getCommunicator().sendNormalServerMessage("The " + realTarget
/*  787 */                   .getName() + " contains too little material to create " + template
/*  788 */                   .getNameWithGenus() + ". Please combine with other of the same kind or select another one.");
/*      */             } else {
/*      */               
/*  791 */               performer.getCommunicator().sendNormalServerMessage("The " + realTarget
/*  792 */                   .getName() + " contains too little material. Please select another one.");
/*  793 */             }  throw new NoSuchItemException("Too little material.");
/*      */           } 
/*  795 */         }  realTarget.setBusy(true);
/*  796 */         time = Actions.getItemCreationTime(200, performer, primSkill, this, realSource, realTarget, template
/*  797 */             .isMassProduction());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  804 */         act.setTimeLeft(time);
/*  805 */         double bonus = performer.getVillageSkillModifier();
/*  806 */         float alc = 0.0F;
/*  807 */         if (performer.isPlayer())
/*  808 */           alc = ((Player)performer).getAlcohol(); 
/*  809 */         float power = (float)primSkill.skillCheck((template.getDifficulty() + alc), realSource, bonus, true, 5.0F);
/*  810 */         if (performer.isRoyalSmith())
/*      */         {
/*  812 */           if (template.isMetal() && 
/*  813 */             power < 0.0F && power > -20.0F)
/*  814 */             power = (10 + Server.rand.nextInt(10)); 
/*      */         }
/*  816 */         if (power < 0.0F) {
/*      */           
/*  818 */           int sec = (int)(time * (100.0F - Math.abs(power)) / 100.0F) / 10;
/*  819 */           act.setFailSecond(sec);
/*  820 */           act.setPower(power);
/*      */         }
/*      */         else {
/*      */           
/*  824 */           act.setPower(power);
/*      */         } 
/*  826 */         performer.sendActionControl(Actions.actionEntrys[148]
/*  827 */             .getVerbString() + " " + template.getName(), true, time);
/*  828 */         if (realSource.isNoTake())
/*      */         {
/*  830 */           performer.getCommunicator().sendNormalServerMessage("You start to work with the " + template
/*  831 */               .getName() + " on the " + realSource.getName() + ".");
/*  832 */           Server.getInstance().broadCastAction(performer
/*  833 */               .getName() + " starts working with the " + template.getName() + " on the " + realSource
/*  834 */               .getName() + ".", performer, 5);
/*      */         }
/*      */         else
/*      */         {
/*  838 */           performer.getCommunicator().sendNormalServerMessage("You start to work with the " + realSource
/*  839 */               .getName() + " on the " + template.getName() + ".");
/*  840 */           Server.getInstance().broadCastAction(performer
/*  841 */               .getName() + " starts working with the " + realSource.getName() + " on the " + template
/*  842 */               .getName() + ".", performer, 5);
/*      */         }
/*      */       
/*      */       }
/*  846 */       catch (NoSuchTemplateException nst) {
/*      */         
/*  848 */         logger.log(Level.WARNING, "no template for creating " + this.objectCreated, (Throwable)nst);
/*  849 */         performer.getCommunicator().sendSafeServerMessage("You cannot create that item right now. Please contact administrators.");
/*      */         
/*  851 */         throw new NoSuchItemException("Failed to locate template");
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  856 */       time = act.getTimeLeft();
/*      */       
/*  858 */       if (act.mayPlaySound())
/*      */       {
/*  860 */         MethodsItems.sendImproveSound(performer, realSource, realTarget, this.primarySkill);
/*      */       }
/*      */     } 
/*  863 */     failSecond = act.getFailSecond();
/*  864 */     if (counter > failSecond || counter * 10.0F > time) {
/*      */       
/*  866 */       if (act.getRarity() != 0 || realSource.getRarity() > 0)
/*      */       {
/*  868 */         performer.playPersonalSound("sound.fx.drumroll");
/*      */       }
/*      */       
/*      */       try {
/*  872 */         ItemTemplate temp = ItemTemplateFactory.getInstance().getTemplate(this.objectCreated);
/*  873 */         if (temp.onePerTile || this.createOnGround)
/*      */         {
/*  875 */           if (!MethodsItems.mayDropOnTile(performer))
/*      */           {
/*  877 */             performer.getCommunicator().sendNormalServerMessage("You cannot create that item here, since there is not enough space in front of you.");
/*      */             
/*  879 */             throw new NoSuchItemException("Already contains a large item.");
/*      */           }
/*      */         
/*      */         }
/*  883 */       } catch (NoSuchTemplateException nst) {
/*      */         
/*  885 */         logger.log(Level.WARNING, "No itemtemplate for objectCreated=" + this.objectCreated, (Throwable)nst);
/*  886 */         throw new NoSuchItemException("No template.");
/*      */       } 
/*  888 */       float skillMultiplier = Math.max(1.0F, counter / 2.0F);
/*  889 */       if (Servers.localServer.isChallengeOrEpicServer())
/*      */       {
/*      */         
/*  892 */         if (primSkill.hasLowCreationGain())
/*  893 */           skillMultiplier /= 3.0F; 
/*      */       }
/*  895 */       primSkill.skillCheck(template.getDifficulty(), realSource, performer.getVillageSkillModifier(), false, skillMultiplier);
/*  896 */       Item newItem = null;
/*      */       
/*      */       try {
/*  899 */         float qlevel = 0.0F;
/*  900 */         double imbueEnhancement = 1.0D + 0.23047D * source.getSkillSpellImprovement(primSkill.getNumber()) / 100.0D;
/*      */         
/*  902 */         qlevel = (float)(act.getPower() * imbueEnhancement / getTotalNumberOfItems());
/*  903 */         performer.sendToLoggers("Power is " + act.getPower() + " total num items is " + getTotalNumberOfItems() + " = qlevel " + qlevel);
/*      */         
/*  905 */         boolean sourceIsStructure = (realSource.isNoTake() && realSource.getOwnerId() == -10L);
/*      */         
/*  907 */         act.setDestroyedItem(null);
/*  908 */         byte material = realSource.getMaterial();
/*      */         
/*  910 */         byte rarity = realTarget.getRarity();
/*  911 */         if (realSource.rarity > rarity && 
/*  912 */           Server.rand.nextInt(getTotalNumberOfItems()) == 0)
/*  913 */           rarity = realSource.rarity; 
/*  914 */         if (qlevel > 0.0F)
/*      */         {
/*  916 */           if (act.getRarity() > rarity && 
/*  917 */             Server.rand.nextInt(getTotalNumberOfItems()) == 0) {
/*  918 */             rarity = act.getRarity();
/*      */           }
/*  920 */           int weight = realSource.getWeightGrams();
/*  921 */           performer.sendToLoggers("trimming qlevel to min " + qlevel + " or " + (realSource
/*  922 */               .getCurrentQualityLevel() / getTotalNumberOfItems()));
/*  923 */           if (!realSource.isWool() && getTotalNumberOfItems() > 2) {
/*  924 */             qlevel = Math.min(qlevel, realSource.getCurrentQualityLevel() / getTotalNumberOfItems());
/*      */           } else {
/*  926 */             qlevel = Math.min(qlevel, realSource.getCurrentQualityLevel() / (getTotalNumberOfItems() + 6));
/*  927 */           }  if (template.isWood() && realSource.isWood())
/*  928 */             realTarget.setMaterial(realSource.getMaterial()); 
/*  929 */           if (!sourceIsStructure)
/*      */           {
/*  931 */             if (weight <= realSource.getTemplate().getWeightGrams()) {
/*  932 */               Items.destroyItem(realSource.getWurmId());
/*      */             } else {
/*      */               
/*  935 */               realSource.setWeight(weight - realSource.getTemplate().getWeightGrams(), false);
/*  936 */               weight = realSource.getTemplate().getWeightGrams();
/*      */             } 
/*      */           }
/*  939 */           setStateForRequirement(req, ++state, realTarget);
/*  940 */           if (realTarget.isEpicTargetItem() || getTotalNumberOfItems() > 1000) {
/*      */             
/*  942 */             MissionHelper helper = MissionHelper.getOrCreateHelper(performer.getWurmId());
/*  943 */             helper.increaseHelps(realTarget.getWurmId());
/*      */           } 
/*  945 */           boolean create = areRequirementsFilled(realTarget);
/*  946 */           float itq = realTarget.getCurrentQualityLevel();
/*      */           
/*  948 */           float dam = realTarget.getDamage();
/*  949 */           if (dam > 0.0F)
/*  950 */             realTarget.setDamage(dam - dam / getTotalNumberOfItems()); 
/*  951 */           if (create) {
/*      */             
/*  953 */             byte mat = template.getMaterial();
/*  954 */             if (realTarget.getTemplateId() == 179) {
/*  955 */               mat = realTarget.getMaterial();
/*  956 */             } else if (realSource.getTemplateId() == 179) {
/*  957 */               mat = realSource.getMaterial();
/*  958 */             }  if (template.isWood())
/*      */             {
/*  960 */               if (realTarget.isWood()) {
/*  961 */                 mat = realTarget.getMaterial();
/*  962 */               } else if (realSource.isWood()) {
/*  963 */                 mat = realSource.getMaterial();
/*      */               }  } 
/*  965 */             if (template.isMetal())
/*      */             {
/*  967 */               if (realTarget.isMetal()) {
/*  968 */                 mat = realTarget.getMaterial();
/*  969 */               } else if (realSource.isMetal()) {
/*  970 */                 mat = realSource.getMaterial();
/*      */               } 
/*      */             }
/*  973 */             int obc = this.objectCreated;
/*  974 */             if (obc == 384) {
/*      */               
/*  976 */               Kingdom K = Kingdoms.getKingdom(realTarget.getAuxData());
/*  977 */               int ktId = 1;
/*  978 */               if (K != null) {
/*      */                 
/*  980 */                 ktId = K.getTemplate();
/*  981 */                 if (ktId == 3) {
/*  982 */                   obc = 430;
/*  983 */                 } else if (ktId == 2) {
/*  984 */                   obc = 528;
/*  985 */                 } else if (ktId == 4) {
/*  986 */                   obc = 638;
/*      */                 } 
/*      */               } 
/*  989 */             }  float endQl = Math.min(Math.max(1.0F, itq + qlevel), 99.0F);
/*  990 */             newItem = ItemFactory.createItem(obc, endQl, mat, rarity, performer
/*  991 */                 .getName());
/*      */ 
/*      */             
/*  994 */             if (newItem.isDomainItem())
/*      */             {
/*  996 */               if (performer.getDeity() != null) {
/*      */                 
/*  998 */                 newItem.bless((performer.getDeity()).number);
/*  999 */                 newItem.setName(newItem.getName() + " of " + (performer.getDeity()).name);
/*      */               } 
/*      */             }
/* 1002 */             if (newItem.isKingdomMarker() || newItem.isEpicTargetItem() || newItem.isTent() || newItem
/* 1003 */               .isUseMaterialAndKingdom() || newItem.isProtectionTower())
/*      */             {
/* 1005 */               if (template.isEpicTargetItem) {
/* 1006 */                 newItem.setAuxData(performer.getKingdomTemplateId());
/*      */               } else {
/* 1008 */                 newItem.setAuxData(performer.getKingdomId());
/*      */               }  } 
/* 1010 */             if (newItem.getTemplateId() == 850)
/*      */             {
/* 1012 */               newItem.setData1(performer.getKingdomId());
/*      */             }
/* 1014 */             if (newItem.isVehicle() || newItem.getTemplate().doesCreateWithLock()) {
/*      */               
/* 1016 */               newItem.setAuxData(performer.getKingdomId());
/* 1017 */               if (newItem.isLockable()) {
/*      */ 
/*      */                 
/* 1020 */                 int lockTemplate = newItem.isBoat() ? 568 : 193;
/* 1021 */                 Item lock = ItemFactory.createItem(lockTemplate, 1.0F, (byte)11, (byte)0, performer
/* 1022 */                     .getName());
/* 1023 */                 lock.setLastOwnerId(performer.getWurmId());
/* 1024 */                 newItem.setLockId(lock.getWurmId());
/* 1025 */                 lock.setLocked(true);
/*      */               } 
/*      */             } 
/* 1028 */             if (newItem.isEpicTargetItem())
/*      */             {
/*      */ 
/*      */               
/* 1032 */               MissionHelper.moveGlobalMissionId(realTarget.getWurmId(), newItem.getWurmId());
/*      */             }
/* 1034 */             if (realTarget.getOwnerId() <= 0L)
/* 1035 */               newItem.setLastOwnerId(realTarget.lastOwner); 
/* 1036 */             if (realTarget.getDescription().length() > 0)
/* 1037 */               newItem.setDescription(realTarget.getDescription()); 
/* 1038 */             Items.destroyItem(realTarget.getWurmId());
/*      */ 
/*      */             
/* 1041 */             if (newItem.getTemplateId() == 186) {
/* 1042 */               performer.achievement(532);
/* 1043 */             } else if (newItem.getTemplateId() == 180 || newItem.getTemplateId() == 178) {
/* 1044 */               performer.achievement(533);
/* 1045 */             } else if (newItem.getTemplateId() == 539) {
/* 1046 */               performer.achievement(538);
/* 1047 */             } else if (newItem.isBoat()) {
/* 1048 */               performer.achievement(540);
/* 1049 */             } else if (newItem.getTemplateId() == 1029) {
/* 1050 */               performer.achievement(561);
/* 1051 */             } else if (newItem.isGuardTower()) {
/* 1052 */               performer.achievement(574);
/* 1053 */             } else if (newItem.getTemplateId() == 850) {
/* 1054 */               performer.achievement(583);
/*      */             }
/*      */           
/*      */           }
/* 1058 */           else if (!sourceIsStructure) {
/*      */             
/* 1060 */             if (realSource.isLiquid()) {
/*      */               
/* 1062 */               performer.getCommunicator().sendNormalServerMessage("You wash the " + template
/* 1063 */                   .getName() + " with " + realSource.getName() + ".");
/* 1064 */               Server.getInstance().broadCastAction(performer
/* 1065 */                   .getName() + " washes " + template.getNameWithGenus() + " with the " + realSource
/* 1066 */                   .getName() + ".", performer, 5);
/*      */             }
/* 1068 */             else if (realSource.isColorComponent() && isColouringCreation()) {
/*      */               
/* 1070 */               performer.getCommunicator().sendNormalServerMessage("You use the " + realSource
/* 1071 */                   .getName() + " as pigment to change the colour of the " + template
/* 1072 */                   .getName() + ".");
/* 1073 */               Server.getInstance().broadCastAction(performer
/* 1074 */                   .getName() + " uses " + realSource.getNameWithGenus() + " as pigment to change the colour of the " + template
/*      */                   
/* 1076 */                   .getName() + ".", performer, 5);
/*      */             }
/*      */             else {
/*      */               
/* 1080 */               performer.getCommunicator().sendNormalServerMessage("You attach the " + realSource
/* 1081 */                   .getName() + " to the " + template.getName() + ".");
/* 1082 */               Server.getInstance().broadCastAction(performer
/* 1083 */                   .getName() + " attaches " + realSource.getNameWithGenus() + " to the " + template
/* 1084 */                   .getName() + ".", performer, 5);
/*      */             } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1108 */             if (!getUseTempalateWeight())
/*      */             {
/* 1110 */               realTarget.setWeight(realTarget.getWeightGrams() + weight, false);
/*      */             }
/*      */             
/* 1113 */             performer.sendToLoggers("adds " + qlevel + " to " + itq);
/* 1114 */             realTarget.setQualityLevel(itq + qlevel);
/* 1115 */             if (rarity != realTarget.getRarity()) {
/*      */               
/* 1117 */               realTarget.setRarity(rarity);
/* 1118 */               if (rarity > 2) {
/* 1119 */                 performer.achievement(300);
/* 1120 */               } else if (rarity > 1) {
/* 1121 */                 performer.achievement(302);
/* 1122 */               } else if (rarity > 0) {
/* 1123 */                 performer.achievement(301);
/*      */               } 
/*      */             } 
/*      */           } else {
/*      */             
/* 1128 */             performer.getCommunicator().sendNormalServerMessage("You use the " + realSource
/* 1129 */                 .getName() + " on the " + template.getName() + ".");
/* 1130 */             Server.getInstance().broadCastAction(performer
/* 1131 */                 .getName() + " uses " + realSource.getNameWithGenus() + " on the " + template
/* 1132 */                 .getName() + ".", performer, 5);
/*      */           
/*      */           }
/*      */ 
/*      */         
/*      */         }
/* 1138 */         else if (!sourceIsStructure)
/*      */         {
/* 1140 */           boolean destroyed = false;
/*      */           
/* 1142 */           qlevel = Math.max(-90.0F, qlevel * getTotalNumberOfItems());
/* 1143 */           String mess = "You make a small mistake. The " + realSource.getName() + " is damaged a bit.";
/*      */           
/* 1145 */           float dam = -qlevel / 10.0F;
/*      */           
/* 1147 */           failed = true;
/* 1148 */           if (realSource.isRepairable())
/* 1149 */             dam /= 10.0F; 
/* 1150 */           destroyed = realSource.setDamage(realSource.getDamage() + dam);
/* 1151 */           if (destroyed) {
/*      */             
/* 1153 */             int itc = getScrapMaterial(material);
/* 1154 */             if (itc != -1) {
/*      */               
/* 1156 */               float qMod = 100.0F + qlevel;
/* 1157 */               int we = realSource.getWeightGrams();
/* 1158 */               if (we > 10)
/*      */               {
/* 1160 */                 newItem = ItemFactory.createItem(itc, qMod / 10.0F, null);
/*      */                 
/* 1162 */                 newItem.setSizes(realSource.getSizeX(), realSource.getSizeY(), realSource.getSizeZ());
/* 1163 */                 newItem.setWeight(we, false);
/* 1164 */                 newItem.setTemperature(realSource.getTemperature());
/* 1165 */                 newItem.setMaterial(material);
/*      */               }
/*      */             
/*      */             } 
/*      */           } else {
/*      */             
/* 1171 */             if (qlevel > -20.0F) {
/* 1172 */               mess = "You almost made it, but the " + realSource.getName() + " is damaged.";
/* 1173 */             } else if (qlevel > -40.0F) {
/* 1174 */               mess = "This could very well work next time, but the " + realSource.getName() + " receives some damage.";
/*      */             }
/* 1176 */             else if (qlevel > -60.0F) {
/* 1177 */               mess = "Too many problems solved in the wrong way damages the " + realSource.getName() + " severely.";
/*      */             }
/* 1179 */             else if (qlevel > -90.0F) {
/* 1180 */               mess = "You fail miserably with the " + realSource.getName() + ".";
/* 1181 */             }  failed = true;
/*      */           } 
/* 1183 */           performer.getCommunicator().sendNormalServerMessage(mess);
/* 1184 */           Server.getInstance().broadCastAction(performer
/* 1185 */               .getName() + " fails with the " + realSource.getName() + ".", performer, 5);
/*      */         }
/*      */         else
/*      */         {
/* 1189 */           performer.getCommunicator().sendNormalServerMessage("You fail to use the " + realSource
/* 1190 */               .getName() + " on the " + template.getName() + ".");
/* 1191 */           Server.getInstance().broadCastAction(performer
/* 1192 */               .getName() + " fails to use " + realSource.getNameWithGenus() + " on the " + template
/* 1193 */               .getName() + ".", performer, 5);
/*      */         }
/*      */       
/*      */       }
/* 1197 */       catch (Exception ex) {
/*      */         
/* 1199 */         logger.log(Level.WARNING, performer.getName() + " failed to create item.", ex);
/*      */       } 
/* 1201 */       performer.getStatus().modifyStamina(-counter * 1000.0F);
/* 1202 */       if (newItem != null) {
/*      */         
/* 1204 */         if (newItem.getTemplateId() == 899) {
/* 1205 */           performer.achievement(370);
/*      */         }
/* 1207 */         return newItem;
/*      */       } 
/* 1209 */       if (failed)
/*      */       {
/* 1211 */         throw new NoSuchItemException("Failed.");
/*      */       }
/*      */       
/* 1214 */       throw new NoSuchItemException("Not done yet.");
/*      */     } 
/* 1216 */     throw new FailedException("Failed skillcheck.");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void onEpicItemCreated(Creature performer, Item newItem, int _objectCreated, boolean generateName) {
/* 1222 */     if (generateName) {
/*      */       
/* 1224 */       String newname = "";
/* 1225 */       String firstPart = EpicTargetItems.getTypeNamePartString(_objectCreated);
/* 1226 */       String secondPart = EpicTargetItems.getSymbolNamePartString(performer);
/* 1227 */       if (Server.rand.nextBoolean()) {
/*      */         
/* 1229 */         if (_objectCreated == 716 || firstPart.toLowerCase().contains("way") || firstPart
/* 1230 */           .toLowerCase().contains("path") || firstPart
/* 1231 */           .toLowerCase().contains("passage") || firstPart
/* 1232 */           .toLowerCase().contains("gate") || firstPart
/* 1233 */           .toLowerCase().contains("door")) {
/* 1234 */           newname = firstPart + " To " + secondPart;
/*      */         } else {
/* 1236 */           newname = firstPart + " Of " + secondPart;
/*      */         }
/*      */       
/*      */       }
/* 1240 */       else if (Server.rand.nextBoolean()) {
/*      */         
/* 1242 */         if (!secondPart.endsWith("s")) {
/* 1243 */           newname = secondPart + "s " + firstPart;
/*      */         } else {
/* 1245 */           newname = secondPart + "' " + firstPart;
/*      */         } 
/*      */       } else {
/* 1248 */         newname = secondPart + " " + firstPart;
/*      */       } 
/* 1250 */       newItem.setName(newname);
/*      */     } 
/* 1252 */     if (EpicTargetItems.isEpicItemWithMission(newItem)) {
/*      */       
/* 1254 */       performer.getCommunicator().sendSafeServerMessage("The " + newItem.getName() + " is complete! It has become an Epic focus point!");
/* 1255 */       HistoryManager.addHistory(performer.getName(), "completes the " + newItem.getName() + "!");
/* 1256 */       MissionTriggers.activateTriggers(performer, newItem, 148, 0L, 1);
/* 1257 */       MissionHelper.addKarmaForItem(newItem.getWurmId());
/*      */     }
/*      */     else {
/*      */       
/* 1261 */       performer.getCommunicator().sendAlertServerMessage("The " + newItem.getName() + " is complete! However, it failed to fulfil the requirements to become an Epic focus point.");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void consume(Item realTarget, Creature performer, ItemTemplate template, Action act) {
/*      */     try {
/* 1270 */       boolean destroy = false;
/* 1271 */       if (this.useCapacity) {
/*      */         
/* 1273 */         int weight = realTarget.getWeightGrams();
/* 1274 */         int extraWeight = (int)(this.percentageLost / 100.0F * template.getWeightGrams());
/* 1275 */         destroy = (weight <= template.getWeightGrams() + extraWeight);
/* 1276 */         if (!destroy) {
/*      */           
/* 1278 */           realTarget.setWeight(weight - template.getWeightGrams() - extraWeight, false);
/* 1279 */           if (extraWeight > 0) {
/*      */             
/* 1281 */             byte material = realTarget.getMaterial();
/* 1282 */             int itc = getScrapMaterial(material);
/* 1283 */             if (itc != -1)
/*      */             {
/* 1285 */               if (extraWeight > 10) {
/*      */                 
/* 1287 */                 Item newItem = ItemFactory.createItem(itc, realTarget.getCurrentQualityLevel() / 10.0F, material, (byte)0, performer
/* 1288 */                     .getName());
/* 1289 */                 newItem.setWeight(extraWeight, true);
/* 1290 */                 newItem.setTemperature(realTarget.getTemperature());
/* 1291 */                 performer.getInventory().insertItem(newItem);
/*      */               } 
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 1299 */       if (this.destroyTarget || destroy || this.destroyBoth)
/*      */       {
/*      */         
/* 1302 */         if (destroy || act.getPower() > 0.0F)
/*      */         {
/* 1304 */           act.setDestroyedItem(realTarget);
/*      */         }
/*      */       }
/*      */     }
/* 1308 */     catch (Exception ex) {
/*      */       
/* 1310 */       logger.log(Level.WARNING, "Failed to delete items.", ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Item run(Creature performer, Item source, long targetId, float counter) throws FailedException, NoSuchSkillException, NoSuchItemException, IllegalArgumentException {
/* 1318 */     Item target = Items.getItem(targetId);
/* 1319 */     Item realSource = source;
/* 1320 */     Item realTarget = target;
/*      */     
/* 1322 */     if (isRestrictedToDeityFollower())
/*      */     {
/* 1324 */       if (performer.getDeity() == null || performer.getDeity().getTemplateDeity() != getDeityRestriction()) {
/*      */         
/* 1326 */         String deityName = Deities.getDeityName(getDeityRestriction());
/* 1327 */         String message = "You must be a follower of %s or one of their demigods to create this item.";
/* 1328 */         performer.getCommunicator().sendNormalServerMessage(StringUtil.format("You must be a follower of %s or one of their demigods to create this item.", new Object[] { deityName }));
/* 1329 */         throw new NoSuchItemException("Incorrect deity");
/*      */       } 
/*      */     }
/*      */     
/* 1333 */     if (performer.getVehicle() != -10L) {
/*      */       
/* 1335 */       performer.getCommunicator().sendNormalServerMessage("You need to be on solid ground to do that.");
/* 1336 */       throw new NoSuchItemException("Need to be on solid ground.");
/*      */     } 
/*      */ 
/*      */     
/*      */     try {
/* 1341 */       int chance = (int)getDifficultyFor(realSource, realTarget, performer);
/* 1342 */       if (chance == 0 || chance <= 5)
/*      */       {
/* 1344 */         performer.getCommunicator().sendNormalServerMessage("This is impossible, perhaps you are not skilled enough.");
/*      */         
/* 1346 */         throw new NoSuchItemException("Not enough skill.");
/*      */       }
/*      */     
/* 1349 */     } catch (NoSuchTemplateException nst) {
/*      */       
/* 1351 */       throw new NoSuchItemException(nst.getMessage(), nst);
/*      */     } 
/* 1353 */     boolean create = false;
/* 1354 */     if (source.getTemplateId() == this.objectSource && target.getTemplateId() == this.objectTarget) {
/* 1355 */       create = true;
/* 1356 */     } else if (source.getTemplateId() == this.objectTarget && target.getTemplateId() == this.objectSource) {
/*      */       
/* 1358 */       create = true;
/* 1359 */       realTarget = source;
/* 1360 */       realSource = target;
/*      */     } 
/* 1362 */     if (realSource == realTarget || realSource.getWurmId() == realTarget.getWurmId()) {
/*      */       
/* 1364 */       create = false;
/*      */       
/* 1366 */       performer.getCommunicator().sendNormalServerMessage("You try to create something out of folding " + realSource
/* 1367 */           .getName() + " around itself. Doesn't work.");
/* 1368 */       throw new NoSuchItemException("Can't create on itself.");
/*      */     } 
/* 1370 */     if (this.objectSourceMaterial != 0 && realSource.getMaterial() != this.objectSourceMaterial) {
/*      */       
/* 1372 */       create = false;
/* 1373 */       performer.getCommunicator().sendNormalServerMessage("Incorrect source material!");
/*      */       
/* 1375 */       throw new NoSuchItemException("Incorrect source material!");
/*      */     } 
/* 1377 */     if (this.objectTargetMaterial != 0 && realTarget.getMaterial() != this.objectTargetMaterial) {
/*      */       
/* 1379 */       create = false;
/* 1380 */       performer.getCommunicator().sendNormalServerMessage("Incorrect target material!");
/*      */       
/* 1382 */       throw new NoSuchItemException("Incorrect target material!");
/*      */     } 
/* 1384 */     if (realSource.getTemplateId() == 1344)
/*      */     {
/*      */       
/* 1387 */       if (!realSource.isEmpty(false)) {
/*      */         
/* 1389 */         create = false;
/* 1390 */         performer.getCommunicator().sendNormalServerMessage("Fishing pole must be empty to be able to make a fishing rod.");
/*      */         
/* 1392 */         throw new NoSuchItemException("Must be Empty!");
/*      */       } 
/*      */     }
/* 1395 */     if (realSource.getTemplateId() == 1409 && realSource.getItemCount() > 0) {
/*      */       
/* 1397 */       create = false;
/* 1398 */       performer.getCommunicator().sendNormalServerMessage("You should remove any pages from the book before using it in creation.");
/* 1399 */       throw new NoSuchItemException("Items inside book.");
/*      */     } 
/* 1401 */     if (create) {
/*      */       
/* 1403 */       ItemTemplate template = null;
/*      */       
/*      */       try {
/* 1406 */         template = ItemTemplateFactory.getInstance().getTemplate(this.objectCreated);
/* 1407 */         if (template.onePerTile || this.createOnGround)
/*      */         {
/* 1409 */           if (!MethodsItems.mayDropOnTile(performer)) {
/*      */             
/* 1411 */             performer.getCommunicator().sendNormalServerMessage("You cannot create that item here, since there is not enough space in front of you.");
/*      */             
/* 1413 */             throw new NoSuchItemException("Already contains a large item.");
/*      */           } 
/*      */         }
/* 1416 */         if (template.isDomainItem())
/*      */         {
/* 1418 */           if (performer.getDeity() == null) {
/*      */             
/* 1420 */             performer.getCommunicator().sendAlertServerMessage("You lack the faith needed to create " + template
/* 1421 */                 .getNameWithGenus() + ".");
/* 1422 */             throw new NoSuchItemException("No deity.");
/*      */           } 
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
/* 1444 */         if (template.nonDeedable)
/*      */         {
/* 1446 */           if (performer.getCurrentVillage() != null) {
/*      */ 
/*      */             
/* 1449 */             performer.getCommunicator().sendAlertServerMessage("You can't continue the " + template
/* 1450 */                 .getName() + " now; you can't build this in a settlement.", (byte)3);
/*      */             
/* 1452 */             throw new NoSuchItemException("In settlement.");
/*      */           } 
/*      */         }
/* 1455 */         if (template.isGuardTower()) {
/*      */ 
/*      */           
/* 1458 */           GuardTower.canConstructTower(performer, realTarget);
/*      */         }
/* 1460 */         else if (template.protectionTower) {
/*      */           
/* 1462 */           if (!performer.isOnSurface()) {
/*      */             
/* 1464 */             performer.getCommunicator().sendAlertServerMessage("You can't build the tower here; you can't build this below surface.", (byte)3);
/*      */ 
/*      */             
/* 1467 */             throw new NoSuchItemException("Below surface.");
/*      */           } 
/* 1469 */           float posX = performer.getStatus().getPositionX();
/* 1470 */           float posY = performer.getStatus().getPositionY();
/* 1471 */           float rot = performer.getStatus().getRotation();
/*      */           
/* 1473 */           float xPosMod = (float)Math.sin((rot * 0.017453292F)) * 2.0F;
/* 1474 */           float yPosMod = -((float)Math.cos((rot * 0.017453292F))) * 2.0F;
/* 1475 */           posX += xPosMod;
/* 1476 */           posY += yPosMod;
/*      */           
/* 1478 */           int placedX = (int)posX >> 2;
/* 1479 */           int placedY = (int)posY >> 2;
/* 1480 */           VolaTile targTile = Zones.getOrCreateTile(placedX, placedY, true);
/* 1481 */           if (targTile != null)
/*      */           {
/* 1483 */             if (targTile.isTransition()) {
/*      */               
/* 1485 */               performer.getCommunicator().sendAlertServerMessage("You can't continue the tower here - the foundation is not stable enough.", (byte)3);
/*      */               
/* 1487 */               throw new NoSuchItemException("On cave opening.");
/*      */             } 
/*      */           }
/* 1490 */           VolaTile tile = performer.getCurrentTile();
/* 1491 */           if (tile != null) {
/*      */             
/* 1493 */             if (Terraforming.isTileUnderWater(1, tile.tilex, tile.tiley, performer.isOnSurface())) {
/*      */               
/* 1495 */               performer.getCommunicator().sendAlertServerMessage("You can't build the tower here; the ground is not solid here.", (byte)3);
/*      */ 
/*      */               
/* 1498 */               throw new NoSuchItemException("Too wet.");
/*      */             } 
/*      */             
/* 1501 */             int mindist = Kingdoms.minKingdomDist;
/* 1502 */             if (Zones.getKingdom(tile.tilex, tile.tiley) == performer.getKingdomId()) {
/* 1503 */               mindist = 60;
/*      */             }
/* 1505 */             if (Kingdoms.isTowerTooNear(tile.tilex, tile.tiley, tile.isOnSurface(), template.protectionTower)) {
/*      */               
/* 1507 */               performer.getCommunicator().sendAlertServerMessage("You can't continue the tower now; another tower is too near.", (byte)3);
/*      */ 
/*      */               
/* 1510 */               throw new NoSuchItemException("Too close to another tower.");
/*      */             } 
/* 1512 */             if (!template.protectionTower && 
/* 1513 */               !Zones.isKingdomBlocking(tile.tilex - mindist, tile.tiley - mindist, tile.tilex + mindist, tile.tiley + mindist, performer
/* 1514 */                 .getKingdomId())) {
/*      */               
/* 1516 */               performer.getCommunicator().sendAlertServerMessage("You can't continue the tower now; another kingdom is too near.", (byte)3);
/*      */ 
/*      */               
/* 1519 */               throw new NoSuchItemException("Too close to another kingdom.");
/*      */             } 
/* 1521 */             if (!Servers.localServer.HOMESERVER) {
/*      */               
/* 1523 */               if (Terraforming.isTileModBlocked(performer, placedX, placedY, performer.isOnSurface()))
/* 1524 */                 throw new NoSuchItemException("Tile protected by the deities in this area."); 
/* 1525 */               for (Item targ : Items.getWarTargets()) {
/*      */                 
/* 1527 */                 int maxnorth = Math.max(0, tile.tiley - 60);
/* 1528 */                 int maxsouth = Math.min(Zones.worldTileSizeY, tile.tiley + 60);
/* 1529 */                 int maxwest = Math.max(0, tile.tilex - 60);
/* 1530 */                 int maxeast = Math.min(Zones.worldTileSizeX, tile.tilex + 60);
/* 1531 */                 if ((int)targ.getPosX() >> 2 > maxwest && (int)targ.getPosX() >> 2 < maxeast && 
/* 1532 */                   (int)targ.getPosY() >> 2 < maxsouth && (int)targ.getPosY() >> 2 > maxnorth) {
/*      */                   
/* 1534 */                   performer.getCommunicator().sendSafeServerMessage("You cannot found the tower here, since this is an active battle ground.");
/*      */                   
/* 1536 */                   throw new NoSuchItemException("Too close to a war target.");
/*      */                 } 
/*      */               } 
/* 1539 */               EndGameItem alt = EndGameItems.getEvilAltar();
/* 1540 */               if (alt != null) {
/*      */                 
/* 1542 */                 int maxnorth = Math.max(0, tile.tiley - 100);
/* 1543 */                 int maxsouth = Math.min(Zones.worldTileSizeY, tile.tiley + 100);
/* 1544 */                 int maxeast = Math.max(0, tile.tilex - 100);
/* 1545 */                 int maxwest = Math.min(Zones.worldTileSizeX, tile.tilex + 100);
/* 1546 */                 if (alt.getItem() != null)
/*      */                 {
/* 1548 */                   if ((int)alt.getItem().getPosX() >> 2 < maxwest && 
/* 1549 */                     (int)alt.getItem().getPosX() >> 2 > maxeast && 
/* 1550 */                     (int)alt.getItem().getPosY() >> 2 < maxsouth && 
/* 1551 */                     (int)alt.getItem().getPosY() >> 2 > maxnorth)
/*      */                   {
/* 1553 */                     throw new NoSuchItemException("You cannot place a tower here, since this is holy ground.");
/*      */                   }
/*      */                 }
/*      */               } 
/*      */ 
/*      */               
/* 1559 */               alt = EndGameItems.getGoodAltar();
/* 1560 */               if (alt != null)
/*      */               {
/* 1562 */                 int maxnorth = Math.max(0, tile.tiley - 100);
/* 1563 */                 int maxsouth = Math.min(Zones.worldTileSizeY, tile.tiley + 100);
/* 1564 */                 int maxeast = Math.max(0, tile.tilex - 100);
/* 1565 */                 int maxwest = Math.min(Zones.worldTileSizeX, tile.tilex + 100);
/* 1566 */                 if (alt.getItem() != null)
/*      */                 {
/* 1568 */                   if ((int)alt.getItem().getPosX() >> 2 < maxwest && 
/* 1569 */                     (int)alt.getItem().getPosX() >> 2 > maxeast && 
/* 1570 */                     (int)alt.getItem().getPosY() >> 2 < maxsouth && 
/* 1571 */                     (int)alt.getItem().getPosY() >> 2 > maxnorth)
/*      */                   {
/* 1573 */                     throw new NoSuchItemException("You cannot place a tower here, since this is holy ground.");
/*      */                   
/*      */                   }
/*      */                 }
/*      */               }
/*      */             
/*      */             }
/* 1580 */             else if (!Terraforming.isFlat(tile.getTileX(), tile.getTileY(), performer.isOnSurface(), 4)) {
/*      */               
/* 1582 */               performer.getCommunicator().sendAlertServerMessage("The area must be flat where you want to build this tower.", (byte)3);
/*      */ 
/*      */               
/* 1585 */               throw new NoSuchItemException("Not flat enough here.");
/*      */             } 
/*      */           } 
/*      */         } 
/*      */         
/* 1590 */         if (template.isEpicTargetItem && Servers.localServer.PVPSERVER) {
/*      */           
/* 1592 */           EpicMission mission = EpicServerStatus.getBuildMissionForTemplate(this.objectCreated);
/* 1593 */           if (mission == null) {
/*      */             
/* 1595 */             performer.getCommunicator().sendNormalServerMessage("There is no current mission to build this item.");
/* 1596 */             performer.sendToLoggers("Trying to start mission item without a valid mission being found.");
/* 1597 */             throw new NoSuchItemException("There is no current mission to build this item.");
/*      */           } 
/* 1599 */           if (mission != null && EpicTargetItems.getTargetItemPlacement(mission.getMissionId()) != performer.getGlobalMapPlacement()) {
/*      */             
/* 1601 */             int placement = EpicTargetItems.getTargetItemPlacement(mission.getMissionId());
/* 1602 */             performer.getCommunicator().sendNormalServerMessage(EpicTargetItems.getTargetItemPlacementString(placement));
/* 1603 */             performer.sendToLoggers("Trying to start mission item in " + MiscConstants.getDirectionString((byte)performer.getGlobalMapPlacement()) + ". " + 
/* 1604 */                 EpicTargetItems.getTargetItemPlacementString(placement));
/* 1605 */             if (performer.getPower() > 0)
/* 1606 */               performer.getCommunicator().sendNormalServerMessage("GMLOG: Currently in the " + 
/* 1607 */                   MiscConstants.getDirectionString((byte)performer.getGlobalMapPlacement()) + "."); 
/* 1608 */             throw new NoSuchItemException(EpicTargetItems.getTargetItemPlacementString(placement));
/*      */           } 
/* 1610 */           if (!EpicTargetItems.mayBuildEpicItem(this.objectCreated, performer.getTileX(), performer.getTileY(), performer
/* 1611 */               .isOnSurface(), performer, performer.getKingdomTemplateId()))
/*      */           {
/* 1613 */             performer.getCommunicator().sendNormalServerMessage(EpicTargetItems.getInstructionStringForKingdom(this.objectCreated, performer
/* 1614 */                   .getKingdomTemplateId()));
/* 1615 */             performer.sendToLoggers("Trying to start mission item but failed requirements. " + 
/* 1616 */                 EpicTargetItems.getInstructionStringForKingdom(this.objectCreated, performer.getKingdomTemplateId()));
/* 1617 */             throw new NoSuchItemException(EpicTargetItems.getInstructionStringForKingdom(this.objectCreated, performer
/* 1618 */                   .getKingdomTemplateId()));
/*      */           }
/*      */         
/*      */         } 
/* 1622 */       } catch (NoSuchTemplateException nst) {
/*      */         
/* 1624 */         logger.log(Level.WARNING, "no template for creating " + this.objectCreated, (Throwable)nst);
/* 1625 */         performer.getCommunicator().sendSafeServerMessage("You cannot create that item right now. Please contact administrators.");
/*      */         
/* 1627 */         throw new NoSuchItemException("Failed to locate template");
/*      */       } 
/* 1629 */       Skills skills = performer.getSkills();
/* 1630 */       Skill primSkill = null;
/* 1631 */       Skill secondarySkill = null;
/* 1632 */       Action act = null;
/*      */       
/*      */       try {
/* 1635 */         act = performer.getCurrentAction();
/*      */       }
/* 1637 */       catch (NoSuchActionException nsa) {
/*      */         
/* 1639 */         logger.log(Level.WARNING, "This action doesn't exist? " + performer.getName(), (Throwable)nsa);
/* 1640 */         throw new NoSuchItemException("An error occured on the server. This action was not found. Please report.");
/*      */       } 
/* 1642 */       float failSecond = Float.MAX_VALUE;
/*      */       
/*      */       try {
/* 1645 */         primSkill = skills.getSkill(this.primarySkill);
/*      */       }
/* 1647 */       catch (Exception ex) {
/*      */         
/* 1649 */         primSkill = skills.learn(this.primarySkill, 1.0F);
/*      */       } 
/*      */       
/*      */       try {
/* 1653 */         secondarySkill = skills.getSkill(realSource.getPrimarySkill());
/*      */       }
/* 1655 */       catch (Exception ex) {
/*      */ 
/*      */         
/*      */         try {
/* 1659 */           secondarySkill = skills.learn(realSource.getPrimarySkill(), 1.0F);
/*      */         }
/* 1661 */         catch (Exception exception) {}
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1667 */       int time = 10;
/* 1668 */       if (counter == 1.0F) {
/*      */         
/* 1670 */         int sourceWeightToRemove = getSourceWeightToRemove(realSource, realTarget, template, true);
/* 1671 */         int targetWeightToRemove = getTargetWeightToRemove(realSource, realTarget, template, true);
/*      */ 
/*      */ 
/*      */         
/* 1675 */         int extraTargetWeight = (getPercentageLost() > 0.0F) ? (int)(this.percentageLost / 100.0F * targetWeightToRemove) : 0;
/* 1676 */         checkSaneAmounts(realSource, sourceWeightToRemove, realTarget, targetWeightToRemove + extraTargetWeight, template, performer, true);
/*      */         
/* 1678 */         if (realTarget.isMetal())
/*      */         {
/* 1680 */           if (realSource.isMetal())
/*      */           {
/* 1682 */             if (SkillList.IsBlacksmithing(this.primarySkill))
/*      */             {
/* 1684 */               if (realTarget.getTemperature() < 3500) {
/*      */                 
/* 1686 */                 performer.getCommunicator().sendNormalServerMessage("The " + realTarget
/* 1687 */                     .getName() + " must be glowing hot to do this.");
/* 1688 */                 throw new NoSuchItemException("Too low temperature.");
/*      */               } 
/*      */             }
/*      */           }
/*      */         }
/*      */         
/* 1694 */         if (template.isOutsideOnly()) {
/*      */           
/* 1696 */           VolaTile tile = performer.getCurrentTile();
/* 1697 */           if (tile != null)
/*      */           {
/* 1699 */             if (tile.getStructure() != null)
/*      */             {
/* 1701 */               if (tile.getStructure().isTypeBridge()) {
/*      */                 
/* 1703 */                 if (performer.getBridgeId() != -10L)
/*      */                 {
/* 1705 */                   performer.getCommunicator().sendNormalServerMessage("You cannot create that on a bridge.");
/* 1706 */                   throw new NoSuchItemException("Can't create on a bridge.");
/*      */                 }
/*      */               
/*      */               } else {
/*      */                 
/* 1711 */                 performer.getCommunicator().sendNormalServerMessage("You cannot create that inside a building.");
/* 1712 */                 throw new NoSuchItemException("Can't create inside.");
/*      */               }
/*      */             
/*      */             }
/*      */           }
/* 1717 */         } else if (template.insideOnly) {
/*      */           
/* 1719 */           VolaTile t = performer.getCurrentTile();
/*      */           
/* 1721 */           if (t == null || t.getStructure() == null) {
/*      */             
/* 1723 */             performer.getCommunicator().sendNormalServerMessage("You must create that item inside.");
/* 1724 */             throw new NoSuchItemException("Must create inside.");
/*      */           } 
/*      */         } 
/*      */ 
/*      */         
/*      */         try {
/* 1730 */           time = Actions.getItemCreationTime(200, performer, primSkill, this, realSource, realTarget, template
/* 1731 */               .isMassProduction());
/*      */         }
/* 1733 */         catch (NoSuchTemplateException nst) {
/*      */           
/* 1735 */           logger.log(Level.WARNING, "No template when creating with " + realSource
/* 1736 */               .getName() + " and " + realTarget.getName() + "." + nst
/* 1737 */               .getMessage(), (Throwable)nst);
/* 1738 */           performer.getCommunicator().sendSafeServerMessage("You cannot create that item right now. Please contact administrators.");
/*      */           
/* 1740 */           throw new NoSuchItemException("No template.");
/*      */         } 
/*      */ 
/*      */         
/*      */         try {
/* 1745 */           performer.getCurrentAction().setTimeLeft(time);
/*      */         }
/* 1747 */         catch (NoSuchActionException nsa) {
/*      */           
/* 1749 */           logger.log(Level.INFO, "This action does not exist?", (Throwable)nsa);
/*      */         } 
/* 1751 */         realTarget.setBusy(true);
/* 1752 */         performer.sendActionControl(Actions.actionEntrys[148]
/* 1753 */             .getVerbString() + " " + template.getName(), true, time);
/* 1754 */         if (realSource.isNoTake()) {
/*      */           
/* 1756 */           performer.getCommunicator().sendNormalServerMessage("You start to work with the " + realTarget
/* 1757 */               .getName() + " on the " + realSource.getName() + ".");
/* 1758 */           Server.getInstance().broadCastAction(performer
/* 1759 */               .getName() + " starts working with the " + realTarget.getName() + " on the " + realSource
/* 1760 */               .getName() + ".", performer, 5);
/*      */         }
/*      */         else {
/*      */           
/* 1764 */           performer.getCommunicator().sendNormalServerMessage("You start to work with the " + realSource
/* 1765 */               .getName() + " on the " + realTarget.getName() + ".");
/* 1766 */           Server.getInstance().broadCastAction(performer
/* 1767 */               .getName() + " starts working with the " + realSource.getName() + " on the " + realTarget
/* 1768 */               .getName() + ".", performer, 5);
/*      */         } 
/*      */         
/* 1771 */         if (!this.depleteSource && realSource.isRepairable())
/* 1772 */           realSource.setDamage(realSource.getDamage() + 0.0025F * realSource.getDamageModifier()); 
/* 1773 */         if (!this.depleteTarget && realTarget.isRepairable()) {
/* 1774 */           realTarget.setDamage(realTarget.getDamage() + 0.0025F * realSource.getDamageModifier());
/*      */         }
/* 1776 */         double bonus = performer.getVillageSkillModifier();
/* 1777 */         if (secondarySkill != null)
/* 1778 */           bonus += secondarySkill.getKnowledge(realSource, 0.0D) / 10.0D; 
/* 1779 */         float alc = 0.0F;
/* 1780 */         if (performer.isPlayer())
/* 1781 */           alc = ((Player)performer).getAlcohol(); 
/* 1782 */         float power = (float)primSkill.skillCheck((template.getDifficulty() + alc), realSource, bonus, true, 1.0F);
/* 1783 */         if (power < 0.0F) {
/*      */           
/* 1785 */           int sec = (int)(time * (100.0F - Math.abs(power)) / 100.0F) / 10;
/* 1786 */           act.setFailSecond(sec);
/* 1787 */           act.setPower(power);
/*      */         }
/*      */         else {
/*      */           
/* 1791 */           double imbueEnhancement = 1.0D + 0.23047D * source.getSkillSpellImprovement(primSkill.getNumber()) / 100.0D;
/*      */           
/* 1793 */           act.setPower((float)(power * imbueEnhancement));
/*      */         } 
/*      */       } else {
/*      */ 
/*      */         
/*      */         try {
/*      */           
/* 1800 */           time = performer.getCurrentAction().getTimeLeft();
/*      */           
/* 1802 */           if (act.mayPlaySound())
/*      */           {
/* 1804 */             MethodsItems.sendImproveSound(performer, realSource, realTarget, this.primarySkill);
/*      */           }
/*      */         }
/* 1807 */         catch (NoSuchActionException nsa) {
/*      */           
/* 1809 */           logger.log(Level.INFO, "This action does not exist?", (Throwable)nsa);
/*      */         } 
/*      */       } 
/* 1812 */       failSecond = act.getFailSecond();
/* 1813 */       if (counter > failSecond || counter * 10.0F > time)
/*      */       {
/* 1815 */         if (act.getRarity() != 0 || (this.depleteSource && realSource.getRarity() > 0) || (this.depleteTarget && realTarget
/* 1816 */           .getRarity() > 0))
/*      */         {
/* 1818 */           performer.playPersonalSound("sound.fx.drumroll");
/*      */         }
/* 1820 */         double bonus = performer.getVillageSkillModifier();
/* 1821 */         float alc = 0.0F;
/* 1822 */         if (performer.isPlayer()) {
/* 1823 */           alc = ((Player)performer).getAlcohol();
/*      */         }
/*      */         
/* 1826 */         if (secondarySkill != null)
/*      */         {
/* 1828 */           bonus = Math.max(-10.0D, secondarySkill
/* 1829 */               .skillCheck((template.getDifficulty() + alc), 0.0D, false, Math.max(1.0F, counter / 10.0F)));
/*      */         }
/* 1831 */         float skillMultiplier = Math.max(1.0F, counter / 2.0F);
/* 1832 */         if (Servers.localServer.isChallengeOrEpicServer())
/*      */         {
/*      */           
/* 1835 */           if (primSkill.hasLowCreationGain())
/* 1836 */             skillMultiplier /= 3.0F; 
/*      */         }
/* 1838 */         primSkill.skillCheck((template.getDifficulty() + alc), realSource, bonus, false, skillMultiplier);
/*      */         
/* 1840 */         int sourceWeightToRemove = getSourceWeightToRemove(realSource, realTarget, template, true);
/* 1841 */         int targetWeightToRemove = getTargetWeightToRemove(realSource, realTarget, template, true);
/* 1842 */         int extraTargetWeight = (getPercentageLost() > 0.0F) ? (int)(this.percentageLost / 100.0F * targetWeightToRemove) : 0;
/*      */         
/* 1844 */         checkSaneAmounts(realSource, sourceWeightToRemove, realTarget, targetWeightToRemove + extraTargetWeight, template, performer, true);
/*      */         
/* 1846 */         Item newItem = null;
/*      */ 
/*      */         
/*      */         try {
/* 1850 */           float qlevel = act.getPower();
/*      */           
/* 1852 */           float itq = qlevel;
/* 1853 */           if (realTarget.getCurrentQualityLevel() < qlevel) {
/* 1854 */             itq = realTarget.getCurrentQualityLevel();
/*      */           }
/* 1856 */           byte material = template.getMaterial();
/*      */           
/* 1858 */           if (template.isWood())
/*      */           {
/* 1860 */             if (realTarget.isWood()) {
/* 1861 */               material = realTarget.getMaterial();
/* 1862 */             } else if (realSource.isWood()) {
/* 1863 */               material = realSource.getMaterial();
/*      */             }  } 
/* 1865 */           if (template.isMetal())
/*      */           {
/* 1867 */             if (realTarget.isMetal()) {
/* 1868 */               material = realTarget.getMaterial();
/* 1869 */             } else if (realSource.isMetal()) {
/* 1870 */               material = realSource.getMaterial();
/*      */             } 
/*      */           }
/* 1873 */           if (qlevel > 0.0F) {
/*      */ 
/*      */             
/* 1876 */             int reqs = 0;
/* 1877 */             if (this.requirements != null)
/*      */             {
/* 1879 */               for (CreationRequirement req : this.requirements) {
/*      */                 
/* 1881 */                 if (req instanceof ItemContainerRequirement || req instanceof ItemVicinityRequirement) {
/*      */                   continue;
/*      */                 }
/*      */ 
/*      */                 
/* 1886 */                 reqs++;
/*      */               } 
/*      */             }
/* 1889 */             byte rarity = 0;
/* 1890 */             if (act.getRarity() > rarity && 
/* 1891 */               Server.rand.nextInt(getTotalNumberOfItems()) == 0)
/* 1892 */               rarity = act.getRarity(); 
/* 1893 */             if (this.depleteSource && realSource.rarity > rarity && (
/* 1894 */               Server.rand.nextInt(getTotalNumberOfItems()) == 0 || realSource.getTemplateId() == 1344))
/* 1895 */               rarity = realSource.rarity; 
/* 1896 */             if (this.depleteTarget && realTarget.rarity > rarity && 
/* 1897 */               Server.rand.nextInt(getTotalNumberOfItems()) == 0)
/* 1898 */               rarity = realTarget.rarity; 
/* 1899 */             if (reqs > 0) {
/*      */               
/* 1901 */               itq /= getTotalNumberOfItems();
/* 1902 */               itq += Math.min(realSource.getCurrentQualityLevel(), qlevel) / getTotalNumberOfItems();
/* 1903 */               if (Item.getMaterialCreationBonus(material) > 0.0F) {
/*      */                 
/* 1905 */                 float leftToMax = 100.0F - itq;
/* 1906 */                 itq += leftToMax / 100.0F * Item.getMaterialCreationBonus(material);
/*      */               } 
/* 1908 */               newItem = ItemFactory.createItem(179, Math.max(1.0F, itq), material, rarity, performer
/* 1909 */                   .getName());
/* 1910 */               newItem.setData(0, 0);
/* 1911 */               setTemplateId(newItem, this.objectCreated);
/* 1912 */               newItem.setName("unfinished " + template.sizeString + template.getName());
/* 1913 */               if (template.kingdomMarker || template.isTent() || template.protectionTower) {
/*      */                 
/* 1915 */                 newItem.setAuxData(performer.getKingdomId());
/*      */               }
/* 1917 */               else if (template.isEpicTargetItem) {
/*      */                 
/* 1919 */                 newItem.setAuxData(performer.getKingdomTemplateId());
/*      */               } 
/* 1921 */               if (getTotalNumberOfItems() > 1000) {
/*      */                 
/* 1923 */                 MissionHelper helper = MissionHelper.getOrCreateHelper(performer.getWurmId());
/* 1924 */                 helper.increaseHelps(newItem.getWurmId());
/*      */               } 
/* 1926 */               if (!getUseTempalateWeight())
/*      */               {
/* 1928 */                 int weight = sourceWeightToRemove + targetWeightToRemove;
/* 1929 */                 newItem.setWeight(weight, false);
/*      */ 
/*      */               
/*      */               }
/* 1933 */               else if (newItem.getRealTemplate() != null)
/*      */               {
/* 1935 */                 newItem.setWeight(newItem.getRealTemplate().getWeightGrams(), false);
/*      */               }
/*      */               else
/*      */               {
/* 1939 */                 newItem.setWeight(newItem.getTemplate().getWeightGrams(), false);
/*      */               }
/*      */             
/*      */             }
/*      */             else {
/*      */               
/* 1945 */               newItem = ItemFactory.createItem(this.objectCreated, Math.max(1.0F, itq), material, rarity, performer
/* 1946 */                   .getName());
/*      */             } 
/*      */             
/* 1949 */             if (newItem.getRarity() > 2) {
/* 1950 */               performer.achievement(300);
/* 1951 */             } else if (newItem.getRarity() == 1) {
/* 1952 */               performer.achievement(301);
/* 1953 */             } else if (newItem.getRarity() == 2) {
/* 1954 */               performer.achievement(302);
/* 1955 */             }  int extraWeight = getExtraWeight(template);
/* 1956 */             if (sourceWeightToRemove > 0) {
/*      */               
/* 1958 */               if (sourceWeightToRemove < realSource.getWeightGrams()) {
/* 1959 */                 realSource.setWeight(realSource.getWeightGrams() - sourceWeightToRemove, true);
/*      */               } else {
/* 1961 */                 Items.destroyItem(realSource.getWurmId());
/*      */               } 
/* 1963 */             } else if (realSource.isRepairable()) {
/* 1964 */               realSource.setDamage(realSource.getDamage() + 0.004F * realSource.getDamageModifier());
/* 1965 */             }  if (targetWeightToRemove > 0) {
/*      */               
/* 1967 */               if (targetWeightToRemove + extraTargetWeight < realTarget.getWeightGrams()) {
/* 1968 */                 realTarget.setWeight(realTarget.getWeightGrams() - targetWeightToRemove + extraTargetWeight, true);
/*      */               } else {
/* 1970 */                 Items.destroyItem(realTarget.getWurmId());
/*      */               } 
/* 1972 */             } else if (realTarget.isRepairable()) {
/* 1973 */               realTarget.setDamage(realTarget.getDamage() + 0.004F * realTarget.getDamageModifier());
/* 1974 */             }  if (extraWeight > 10 && !realTarget.isLiquid()) {
/*      */               
/* 1976 */               int itc = getScrapMaterial(material);
/* 1977 */               if (itc != -1) {
/*      */                 try
/*      */                 {
/*      */                   
/* 1981 */                   Item scrap = ItemFactory.createItem(itc, realTarget
/* 1982 */                       .getCurrentQualityLevel() / 10.0F, material, (byte)0, performer.getName());
/* 1983 */                   scrap.setWeight(extraWeight, false);
/* 1984 */                   scrap.setTemperature(realTarget.getTemperature());
/* 1985 */                   performer.getInventory().insertItem(scrap, true);
/*      */                 }
/* 1987 */                 catch (NoSuchTemplateException nst)
/*      */                 {
/* 1989 */                   logger.log(Level.WARNING, performer
/* 1990 */                       .getName() + " tid= " + itc + ", " + nst.getMessage(), (Throwable)nst);
/*      */                 }
/*      */               
/*      */               }
/*      */             } 
/*      */           } else {
/*      */             
/* 1997 */             String verb = (template.getName().charAt(template.getName().length() - 1) == 's') ? " are " : " is ";
/* 1998 */             String mess = "You realize this was a meaningless effort. The " + template.getName() + verb + "useless.";
/* 1999 */             float targDam = realTarget.getDamage();
/* 2000 */             float sourceDam = realSource.getDamage();
/* 2001 */             if (qlevel > -20.0F) {
/*      */               
/* 2003 */               mess = "You almost made it, but the " + template.getName() + verb + "useless.";
/* 2004 */               targDam += (Server.rand.nextInt(1) + 1);
/* 2005 */               sourceDam += (Server.rand.nextInt(1) + 1);
/*      */             }
/* 2007 */             else if (qlevel > -40.0F) {
/*      */               
/* 2009 */               mess = "This could very well work next time, but the " + template.getName() + verb + "useless.";
/* 2010 */               targDam += (Server.rand.nextInt(3) + 1);
/* 2011 */               sourceDam += (Server.rand.nextInt(3) + 1);
/*      */             }
/* 2013 */             else if (qlevel > -60.0F) {
/*      */               
/* 2015 */               mess = "Too many problems solved in the wrong way makes the " + template.getName() + " useless.";
/* 2016 */               targDam += (Server.rand.nextInt(5) + 1);
/* 2017 */               sourceDam += (Server.rand.nextInt(5) + 1);
/*      */             }
/* 2019 */             else if (qlevel > -80.0F) {
/*      */               
/* 2021 */               mess = "You fail miserably with the " + template.getName() + ".";
/* 2022 */               targDam += (Server.rand.nextInt(7) + 1);
/* 2023 */               sourceDam += (Server.rand.nextInt(7) + 1);
/*      */             }
/*      */             else {
/*      */               
/* 2027 */               targDam += (Server.rand.nextInt(9) + 1);
/* 2028 */               sourceDam += (Server.rand.nextInt(9) + 1);
/*      */             } 
/* 2030 */             performer.getCommunicator().sendNormalServerMessage(mess);
/* 2031 */             Server.getInstance().broadCastAction(performer
/* 2032 */                 .getName() + " fails with the " + template.getName() + ".", performer, 5);
/* 2033 */             if (realTarget.getTemplateId() == 128) {
/*      */ 
/*      */               
/* 2036 */               int weight = realTarget.getWeightGrams();
/* 2037 */               realTarget.setWeight(weight - 100, true);
/*      */             }
/* 2039 */             else if (targDam > 100.0F) {
/*      */               
/* 2041 */               int itc = getScrapMaterial(material);
/* 2042 */               if (itc != -1) {
/*      */                 
/* 2044 */                 int weight = template.getWeightGrams();
/* 2045 */                 if (this.depleteTarget)
/* 2046 */                   weight = realTarget.getWeightGrams(); 
/* 2047 */                 if (weight > 10) {
/*      */                   
/* 2049 */                   newItem = ItemFactory.createItem(itc, realTarget.getCurrentQualityLevel() / 10.0F, null);
/* 2050 */                   newItem.setTemperature(realTarget.getTemperature());
/* 2051 */                   newItem.setWeight(weight, false);
/* 2052 */                   newItem.setMaterial(material);
/*      */                 } 
/*      */               } 
/* 2055 */               Items.destroyItem(realTarget.getWurmId());
/*      */             } else {
/*      */               
/* 2058 */               realTarget.setDamage(targDam);
/* 2059 */             }  if (!realSource.isRepairable())
/*      */             {
/* 2061 */               if (sourceDam > 100.0F) {
/*      */                 
/* 2063 */                 int itc = getScrapMaterial(realSource.getMaterial());
/* 2064 */                 if (itc != -1) {
/*      */                   
/* 2066 */                   int weight = realSource.getWeightGrams();
/* 2067 */                   if (this.destroyTarget || this.destroyBoth)
/* 2068 */                     weight = realSource.getWeightGrams(); 
/* 2069 */                   if (weight > 10) {
/*      */                     
/* 2071 */                     newItem = ItemFactory.createItem(itc, realSource.getCurrentQualityLevel() / 10.0F, null);
/* 2072 */                     newItem.setTemperature(realSource.getTemperature());
/* 2073 */                     newItem.setWeight(weight, false);
/* 2074 */                     newItem.setMaterial(realSource.getMaterial());
/*      */                   } 
/*      */                 } 
/* 2077 */                 Items.destroyItem(realSource.getWurmId());
/*      */               } else {
/*      */                 
/* 2080 */                 realSource.setDamage(sourceDam);
/*      */               } 
/*      */             }
/*      */           } 
/* 2084 */         } catch (Exception ex) {
/*      */           
/* 2086 */           logger.log(Level.WARNING, "Failed to create item.", ex);
/*      */         } 
/*      */         
/* 2089 */         performer.getStatus().modifyStamina(-counter * 1000.0F);
/* 2090 */         if (newItem != null) {
/* 2091 */           return newItem;
/*      */         }
/*      */         
/* 2094 */         throw new NoSuchItemException("Too low quality.");
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/* 2099 */       throw new NoSuchItemException("Illegal parameters for this entry: source=" + realSource.getTemplateId() + ", target=" + realTarget
/* 2100 */           .getTemplateId() + " when creating " + this.objectCreated);
/* 2101 */     }  throw new FailedException("Failed skillcheck.");
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\items\AdvancedCreationEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */