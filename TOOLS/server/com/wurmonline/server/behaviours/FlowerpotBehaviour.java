/*     */ package com.wurmonline.server.behaviours;
/*     */ 
/*     */ import com.wurmonline.server.FailedException;
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.ItemFactory;
/*     */ import com.wurmonline.server.items.ItemTemplate;
/*     */ import com.wurmonline.server.items.ItemTemplateFactory;
/*     */ import com.wurmonline.server.items.NoSuchTemplateException;
/*     */ import com.wurmonline.server.skills.NoSuchSkillException;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class FlowerpotBehaviour
/*     */   extends ItemBehaviour
/*     */ {
/*     */   public FlowerpotBehaviour() {
/*  46 */     super((short)47);
/*     */   }
/*     */ 
/*     */   
/*  50 */   private static final Logger logger = Logger.getLogger(FlowerpotBehaviour.class.getName());
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item target) {
/*  55 */     List<ActionEntry> toReturn = super.getBehavioursFor(performer, target);
/*  56 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item target) {
/*  62 */     List<ActionEntry> toReturn = super.getBehavioursFor(performer, source, target);
/*     */     
/*  64 */     if (source.isFlower() && (target
/*  65 */       .getTemplateId() == 813 || target.getTemplateId() == 1001)) {
/*     */       
/*  67 */       toReturn.add(Actions.actionEntrys[564]);
/*     */     }
/*  69 */     else if (source.isContainerLiquid()) {
/*     */       
/*  71 */       Item[] items = source.getItemsAsArray();
/*  72 */       for (int i = 0; i < items.length; i++) {
/*     */         
/*  74 */         if (items[i].getTemplateId() == 128) {
/*     */           
/*  76 */           toReturn.add(Actions.actionEntrys[565]);
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/*  82 */     return toReturn;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean action(Action act, Creature performer, Item target, short action, float counter) {
/*  87 */     boolean toReturn = super.action(act, performer, target, action, counter);
/*  88 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(Action act, Creature performer, Item source, Item target, short action, float counter) {
/*  94 */     if (action == 1)
/*  95 */       return action(act, performer, target, action, counter); 
/*  96 */     if (action == 564)
/*  97 */       return plantFlowerInPot(act, performer, source, target, counter); 
/*  98 */     if (action == 565) {
/*  99 */       return waterFlower(act, performer, source, target, counter);
/*     */     }
/* 101 */     return super.action(act, performer, source, target, action, counter);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final boolean waterFlower(Action act, Creature performer, Item waterSource, Item pot, float counter) {
/* 107 */     int time = 0;
/* 108 */     Skill gardening = null;
/* 109 */     Item water = null;
/*     */     
/* 111 */     Item[] items = waterSource.getItemsAsArray();
/* 112 */     for (int i = 0; i < items.length; i++) {
/*     */       
/* 114 */       if (items[i].getTemplateId() == 128) {
/*     */         
/* 116 */         water = items[i];
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 121 */     if (water == null) {
/*     */       
/* 123 */       performer.getCommunicator().sendNormalServerMessage("You need water to water the flowers.", (byte)3);
/* 124 */       return true;
/*     */     } 
/* 126 */     if (water.getWeightGrams() < 100) {
/*     */       
/* 128 */       performer.getCommunicator().sendNormalServerMessage("You need more water in order to water the flowers.", (byte)3);
/* 129 */       return true;
/*     */     } 
/*     */     
/* 132 */     if (pot.getDamage() == 0.0F) {
/*     */       
/* 134 */       performer.getCommunicator().sendNormalServerMessage("The flowers are in no need of watering.", (byte)3);
/* 135 */       return true;
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 140 */       gardening = performer.getSkills().getSkill(10045);
/*     */     }
/* 142 */     catch (NoSuchSkillException nse) {
/*     */       
/* 144 */       gardening = performer.getSkills().learn(10045, 1.0F);
/*     */     } 
/*     */     
/* 147 */     if (counter == 1.0F) {
/*     */       
/* 149 */       time = Actions.getStandardActionTime(performer, gardening, pot, 0.0D);
/* 150 */       act.setTimeLeft(time);
/* 151 */       performer.getCommunicator().sendNormalServerMessage("You start watering the flowers.");
/* 152 */       Server.getInstance().broadCastAction(performer.getName() + " starts to water some flowers.", performer, 5);
/* 153 */       performer.sendActionControl(Actions.actionEntrys[565].getVerbString(), true, time);
/*     */       
/* 155 */       return false;
/*     */     } 
/*     */     
/* 158 */     time = act.getTimeLeft();
/* 159 */     if (counter * 10.0F > time) {
/*     */       
/* 161 */       double power = gardening.skillCheck(15.0D, 0.0D, false, counter);
/* 162 */       if (power > 0.0D) {
/*     */         
/* 164 */         float dmgChange = 20.0F * (float)(power / 100.0D);
/* 165 */         pot.setDamage(Math.max(0.0F, pot.getDamage() - dmgChange));
/* 166 */         water.setWeight(water.getWeightGrams() - 100, true);
/* 167 */         performer.getCommunicator().sendNormalServerMessage("You successfully watered the flowers, they look healthier.", (byte)2);
/* 168 */         return true;
/*     */       } 
/*     */ 
/*     */       
/* 172 */       int waterReduction = 100;
/* 173 */       if (power >= -20.0D) {
/* 174 */         performer.getCommunicator().sendNormalServerMessage("You accidentally miss the pot and pour the water on the ground instead.", (byte)3);
/* 175 */       } else if (power > -50.0D && power < -20.0D) {
/* 176 */         performer.getCommunicator().sendNormalServerMessage("You spill water all over your clothes.", (byte)3);
/*     */       } else {
/*     */         
/* 179 */         performer.getCommunicator().sendNormalServerMessage("For some inexplicable reason you poured all of the water on the ground, how you thought it would help you will never know.");
/* 180 */         waterReduction = Math.min(water.getWeightGrams(), 200);
/*     */       } 
/*     */       
/* 183 */       water.setWeight(water.getWeightGrams() - waterReduction, true);
/*     */       
/* 185 */       return true;
/*     */     } 
/*     */     
/* 188 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int getFlowerpotIdFromFlower(Item flower, Item pot) {
/* 194 */     if (pot.getTemplateId() == 813) {
/*     */       
/* 196 */       int i = flower.getTemplateId();
/* 197 */       if (i == 498)
/* 198 */         return 814; 
/* 199 */       if (i == 499)
/* 200 */         return 818; 
/* 201 */       if (i == 500)
/* 202 */         return 816; 
/* 203 */       if (i == 501)
/* 204 */         return 817; 
/* 205 */       if (i == 502)
/* 206 */         return 815; 
/* 207 */       if (i == 503) {
/* 208 */         return 819;
/*     */       }
/* 210 */       return 820;
/*     */     } 
/*     */ 
/*     */     
/* 214 */     int templateId = flower.getTemplateId();
/* 215 */     if (templateId == 498)
/* 216 */       return 1002; 
/* 217 */     if (templateId == 499)
/* 218 */       return 1006; 
/* 219 */     if (templateId == 500)
/* 220 */       return 1004; 
/* 221 */     if (templateId == 501)
/* 222 */       return 1005; 
/* 223 */     if (templateId == 502)
/* 224 */       return 1003; 
/* 225 */     if (templateId == 503) {
/* 226 */       return 1007;
/*     */     }
/* 228 */     return 1008;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean plantFlowerInPot(Action act, Creature performer, Item flower, Item pot, float counter) {
/* 235 */     int time = 0;
/* 236 */     if (counter == 1.0F) {
/*     */       
/* 238 */       Skill gardening = null;
/*     */       
/*     */       try {
/* 241 */         gardening = performer.getSkills().getSkill(10045);
/*     */       }
/* 243 */       catch (NoSuchSkillException nss) {
/*     */         
/* 245 */         gardening = performer.getSkills().learn(10045, 1.0F);
/*     */       } 
/*     */       
/* 248 */       time = Actions.getStandardActionTime(performer, gardening, flower, 0.0D);
/* 249 */       act.setTimeLeft(time);
/* 250 */       performer.getCommunicator().sendNormalServerMessage("You start planting the flowers.");
/* 251 */       Server.getInstance().broadCastAction(performer.getName() + " starts to plant some flowers.", performer, 5);
/* 252 */       performer.sendActionControl(Actions.actionEntrys[564].getVerbString(), true, time);
/*     */       
/* 254 */       return false;
/*     */     } 
/*     */     
/* 257 */     time = act.getTimeLeft();
/* 258 */     if (counter * 10.0F > time) {
/*     */       
/* 260 */       float ql = flower.getQualityLevel() + pot.getQualityLevel();
/* 261 */       ql /= 2.0F;
/* 262 */       float dmg = flower.getDamage() + pot.getDamage();
/* 263 */       dmg /= 2.0F;
/* 264 */       Skill gardening = null;
/*     */       
/*     */       try {
/* 267 */         gardening = performer.getSkills().getSkill(10045);
/*     */       }
/* 269 */       catch (NoSuchSkillException nss) {
/*     */         
/* 271 */         gardening = performer.getSkills().learn(10045, 1.0F);
/*     */       } 
/*     */       
/*     */       try {
/* 275 */         int toCreate = getFlowerpotIdFromFlower(flower, pot);
/* 276 */         ItemTemplate template = ItemTemplateFactory.getInstance().getTemplate(toCreate);
/* 277 */         double power = gardening.skillCheck((template.getDifficulty() + dmg), ql, false, counter);
/* 278 */         if (power > 0.0D) {
/*     */ 
/*     */           
/*     */           try {
/* 282 */             Item newPot = ItemFactory.createItem(toCreate, pot.getQualityLevel(), pot.getRarity(), performer.getName());
/* 283 */             newPot.setDamage(pot.getDamage());
/* 284 */             newPot.setLastOwnerId(pot.getLastOwnerId());
/* 285 */             newPot.setDescription(pot.getDescription());
/* 286 */             Items.destroyItem(pot.getWurmId());
/* 287 */             performer.getInventory().insertItem(newPot, true);
/* 288 */             performer.getCommunicator().sendNormalServerMessage("You finished planting the flowers in the pot.");
/*     */           
/*     */           }
/* 291 */           catch (NoSuchTemplateException nst) {
/*     */             
/* 293 */             logger.log(Level.WARNING, nst.getMessage(), (Throwable)nst);
/*     */           }
/* 295 */           catch (FailedException fe) {
/*     */             
/* 297 */             logger.log(Level.WARNING, fe.getMessage(), (Throwable)fe);
/*     */           } 
/*     */         } else {
/*     */           
/* 301 */           performer.getCommunicator().sendNormalServerMessage("Sadly, the fragile flowers do not survive despite your best efforts.", (byte)3);
/*     */         } 
/* 303 */         Items.destroyItem(flower.getWurmId());
/*     */       }
/* 305 */       catch (NoSuchTemplateException nst) {
/*     */         
/* 307 */         logger.log(Level.WARNING, nst.getMessage(), (Throwable)nst);
/*     */       } 
/* 309 */       return true;
/*     */     } 
/* 311 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\FlowerpotBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */