/*     */ package com.wurmonline.server.behaviours;
/*     */ 
/*     */ import com.wurmonline.server.FailedException;
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.ItemFactory;
/*     */ import com.wurmonline.server.items.NoSuchTemplateException;
/*     */ import com.wurmonline.server.questions.TextInputQuestion;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class InventoryBehaviour
/*     */   extends Behaviour
/*     */ {
/*     */   public InventoryBehaviour() {
/*  22 */     super((short)49);
/*     */   }
/*     */ 
/*     */   
/*  26 */   private static final Logger logger = Logger.getLogger(MethodsItems.class.getName());
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(Action act, Creature performer, Item target, short action, float counter) {
/*  31 */     if (action == 1) {
/*     */       
/*  33 */       performer.getCommunicator().sendNormalServerMessage(target.examine(performer));
/*  34 */       target.sendEnchantmentStrings(performer.getCommunicator());
/*  35 */       return true;
/*     */     } 
/*  37 */     if (action == 567) {
/*     */       
/*  39 */       addGroup(target, performer);
/*  40 */       return true;
/*     */     } 
/*  42 */     if (action == 59) {
/*     */       
/*  44 */       if (target.getTemplateId() != 824)
/*  45 */         return true; 
/*  46 */       renameGroup(target, performer);
/*  47 */       return true;
/*     */     } 
/*  49 */     if (action == 586) {
/*     */       
/*  51 */       removeGroup(target, performer);
/*  52 */       return true;
/*     */     } 
/*  54 */     if (action == 568 || action == 3) {
/*     */       
/*  56 */       openContainer(target, performer);
/*  57 */       return true;
/*     */     } 
/*  59 */     if (ManageMenu.isManageAction(performer, action))
/*     */     {
/*  61 */       return ManageMenu.action(act, performer, action, counter);
/*     */     }
/*     */     
/*  64 */     return super.action(act, performer, target, action, counter);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(Action act, Creature performer, Item source, Item target, short action, float counter) {
/*  70 */     if (action == 1)
/*     */     {
/*  72 */       return action(act, performer, target, action, counter);
/*     */     }
/*  74 */     if (action == 567 || action == 59 || action == 586)
/*     */     {
/*  76 */       return action(act, performer, target, action, counter);
/*     */     }
/*  78 */     if (action == 568 || action == 3)
/*     */     {
/*  80 */       return action(act, performer, target, action, counter);
/*     */     }
/*  82 */     if (ManageMenu.isManageAction(performer, action))
/*     */     {
/*  84 */       return ManageMenu.action(act, performer, action, counter);
/*     */     }
/*  86 */     if (action == 744 && source.canHaveInscription())
/*     */     {
/*  88 */       return PapyrusBehaviour.addToCookbook(act, performer, source, target, action, counter);
/*     */     }
/*     */     
/*  91 */     return super.action(act, performer, source, target, action, counter);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void removeGroup(Item group, Creature performer) {
/*  96 */     if (group.getTemplateId() != 824)
/*     */       return; 
/*  98 */     if ((group.getItemsAsArray()).length > 0) {
/*     */       
/* 100 */       performer.getCommunicator().sendNormalServerMessage("The group must be empty before you can remove it.");
/*     */       return;
/*     */     } 
/* 103 */     Items.destroyItem(group.getWurmId());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void addGroup(Item inventory, Creature performer) {
/* 109 */     if ((!inventory.isInventory() && !inventory.isInventoryGroup()) || inventory.getOwnerId() != performer.getWurmId()) {
/*     */       
/* 111 */       performer.getCommunicator().sendNormalServerMessage("You can only add groups to your inventory or other groups.");
/*     */       
/*     */       return;
/*     */     } 
/* 115 */     Item[] items = performer.getInventory().getItemsAsArray();
/* 116 */     int groupCount = 0;
/* 117 */     for (int i = 0; i < items.length; i++) {
/*     */       
/* 119 */       if (items[i].getTemplateId() == 824)
/* 120 */         groupCount++; 
/* 121 */       if (groupCount == 20)
/*     */         break; 
/*     */     } 
/* 124 */     if (groupCount >= 20) {
/*     */       
/* 126 */       performer.getCommunicator().sendNormalServerMessage("You can only have 20 groups.");
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*     */     try {
/* 132 */       Item group = ItemFactory.createItem(824, 100.0F, "");
/* 133 */       group.setName("Group");
/* 134 */       inventory.insertItem(group, true);
/* 135 */       renameGroup(group, performer);
/*     */     }
/* 137 */     catch (NoSuchTemplateException nst) {
/*     */       
/* 139 */       logger.log(Level.WARNING, nst.getMessage(), (Throwable)nst);
/*     */     }
/* 141 */     catch (FailedException fe) {
/*     */       
/* 143 */       logger.log(Level.WARNING, fe.getMessage(), (Throwable)fe);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void openContainer(Item group, Creature performer) {
/* 149 */     performer.getCommunicator().sendOpenInventoryContainer(group.getWurmId());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void renameGroup(Item group, Creature performer) {
/* 155 */     TextInputQuestion tiq = new TextInputQuestion(performer, "Setting name for group.", "Set the new name:", 1, group.getWurmId(), 20, false);
/*     */     
/* 157 */     tiq.setOldtext(group.getName());
/* 158 */     tiq.sendQuestion();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item target) {
/* 164 */     int tid = target.getTemplateId();
/* 165 */     List<ActionEntry> toReturn = super.getBehavioursFor(performer, target);
/* 166 */     if ((target.isInventory() || target.isInventoryGroup()) && target.getOwnerId() == performer.getWurmId())
/* 167 */       toReturn.add(Actions.actionEntrys[567]); 
/* 168 */     if (tid == 824 && target.getOwnerId() == performer.getWurmId()) {
/*     */       
/* 170 */       toReturn.add(Actions.actionEntrys[59]);
/* 171 */       toReturn.add(Actions.actionEntrys[586]);
/* 172 */       toReturn.add(Actions.actionEntrys[568]);
/*     */     } 
/* 174 */     toReturn.addAll(ManageMenu.getBehavioursFor(performer));
/* 175 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item target) {
/* 181 */     List<ActionEntry> toReturn = super.getBehavioursFor(performer, source, target);
/* 182 */     int tid = target.getTemplateId();
/* 183 */     if ((target.isInventory() || target.isInventoryGroup()) && target.getOwnerId() == performer.getWurmId())
/* 184 */       toReturn.add(Actions.actionEntrys[567]); 
/* 185 */     if (tid == 824 && target.getOwnerId() == performer.getWurmId()) {
/*     */       
/* 187 */       toReturn.add(Actions.actionEntrys[59]);
/* 188 */       toReturn.add(Actions.actionEntrys[586]);
/* 189 */       toReturn.add(Actions.actionEntrys[568]);
/*     */     } 
/* 191 */     toReturn.addAll(ManageMenu.getBehavioursFor(performer));
/* 192 */     if (target.isInventory() && source.canHaveInscription())
/*     */     {
/* 194 */       toReturn.addAll(PapyrusBehaviour.getPapyrusBehavioursFor(performer, source));
/*     */     }
/* 196 */     return toReturn;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\InventoryBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */