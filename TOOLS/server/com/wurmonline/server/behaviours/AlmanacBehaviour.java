/*     */ package com.wurmonline.server.behaviours;
/*     */ 
/*     */ import com.wurmonline.server.FailedException;
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.WurmId;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.FragmentUtilities;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.ItemFactory;
/*     */ import com.wurmonline.server.items.NoSuchTemplateException;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.questions.ShowArchReport;
/*     */ import com.wurmonline.server.questions.ShowHarvestableInfo;
/*     */ import com.wurmonline.server.questions.TextInputQuestion;
/*     */ import com.wurmonline.server.villages.DeadVillage;
/*     */ import com.wurmonline.server.villages.Villages;
/*     */ import com.wurmonline.server.zones.VolaTile;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.Nullable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AlmanacBehaviour
/*     */   extends ItemBehaviour
/*     */ {
/*  61 */   private static final Logger logger = Logger.getLogger(MarkerBehaviour.class.getName());
/*     */ 
/*     */   
/*     */   public AlmanacBehaviour() {
/*  65 */     super((short)57);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item target) {
/*  71 */     List<ActionEntry> toReturn = super.getBehavioursFor(performer, target);
/*  72 */     toReturn.addAll(getBehavioursForAlmanac(performer, null, target));
/*  73 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item target) {
/*  79 */     List<ActionEntry> toReturn = super.getBehavioursFor(performer, source, target);
/*  80 */     toReturn.addAll(getBehavioursForAlmanac(performer, source, target));
/*  81 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(Action act, Creature performer, Item target, short action, float counter) {
/*  87 */     boolean[] ans = almanacAction(act, performer, null, target, action, counter);
/*  88 */     if (ans[0])
/*  89 */       return ans[1]; 
/*  90 */     return super.action(act, performer, target, action, counter);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(Action act, Creature performer, Item source, Item target, short action, float counter) {
/*  96 */     boolean[] ans = almanacAction(act, performer, source, target, action, counter);
/*  97 */     if (ans[0])
/*  98 */       return ans[1]; 
/*  99 */     return super.action(act, performer, source, target, action, counter);
/*     */   }
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
/*     */   private List<ActionEntry> getBehavioursForAlmanac(Creature performer, @Nullable Item source, Item target) {
/* 112 */     List<ActionEntry> toReturn = new LinkedList<>();
/* 113 */     if (target.getTemplateId() == 1127) {
/*     */       
/* 115 */       toReturn.add(new ActionEntry((short)17, "Read Almanac summary", "reading summary"));
/* 116 */       toReturn.add(Actions.actionEntrys[854]);
/*     */     }
/* 118 */     else if (target.getTemplateId() == 1128) {
/*     */       
/* 120 */       toReturn.add(new ActionEntry((short)17, "Read " + target.getName() + " summary", "reading summary"));
/* 121 */       if (target.isEmpty(false)) {
/* 122 */         toReturn.add(Actions.actionEntrys[855]);
/*     */       }
/* 124 */     } else if (target.getTemplateId() == 1403) {
/*     */       
/* 126 */       if (target.getAuxBit(0) && target.getAuxBit(1) && target.getAuxBit(2) && target.getAuxBit(3))
/* 127 */         toReturn.add(new ActionEntry((short)118, "Get direction", "getting direction")); 
/* 128 */       if (target.getData() != -1L)
/* 129 */         toReturn.add(new ActionEntry((short)17, "Read report", "reading report")); 
/*     */     } 
/* 131 */     return toReturn;
/*     */   }
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
/*     */   public boolean[] almanacAction(Action act, Creature performer, @Nullable Item source, Item target, short action, float counter) {
/* 147 */     if (action == 17) {
/*     */       
/* 149 */       if (target.getTemplateId() == 1403) {
/*     */         
/* 151 */         ShowArchReport sar = new ShowArchReport(performer, target);
/* 152 */         sar.sendQuestion();
/*     */         
/* 154 */         return new boolean[] { true, true };
/*     */       } 
/*     */       
/* 157 */       ShowHarvestableInfo shi = new ShowHarvestableInfo(performer, target);
/* 158 */       shi.sendQuestion();
/* 159 */       return new boolean[] { true, true };
/*     */     } 
/* 161 */     if (target.getTemplateId() == 1127 && action == 854) {
/*     */ 
/*     */       
/*     */       try {
/*     */         
/* 166 */         Item newItem = ItemFactory.createItem(1128, 100.0F, null);
/* 167 */         if (target.insertItem(newItem)) {
/* 168 */           renameFolder(newItem, performer);
/*     */         } else {
/* 170 */           performer.getCommunicator().sendNormalServerMessage("no space to add folder into almanac.");
/*     */         } 
/* 172 */       } catch (FailedException e) {
/*     */         
/* 174 */         performer.getCommunicator().sendNormalServerMessage("Problem adding folder into almanac.");
/* 175 */         logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*     */       }
/* 177 */       catch (NoSuchTemplateException e) {
/*     */         
/* 179 */         performer.getCommunicator().sendNormalServerMessage("Problem adding folder into almanac.");
/* 180 */         logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*     */       } 
/* 182 */       return new boolean[] { true, true };
/*     */     } 
/* 184 */     if (target.getTemplateId() == 1128 && action == 59) {
/*     */       
/* 186 */       renameFolder(target, performer);
/* 187 */       return new boolean[] { true, true };
/*     */     } 
/* 189 */     if (target.getTemplateId() == 1128 && target.isEmpty(false) && action == 855) {
/*     */       
/* 191 */       performer.getCommunicator().sendNormalServerMessage("Removing empty folder (" + target.getName() + ") from almanac.");
/* 192 */       Items.destroyItem(target.getWurmId());
/* 193 */       return new boolean[] { true, true };
/*     */     } 
/* 195 */     if (target.getTemplateId() == 1403)
/*     */     {
/* 197 */       if (action == 118) {
/*     */         
/* 199 */         if (target.getData() != -1L && target.getAuxBit(0) && target.getAuxBit(1) && target.getAuxBit(2) && target.getAuxBit(3)) {
/*     */           
/* 201 */           DeadVillage dv = Villages.getDeadVillage(target.getData());
/* 202 */           if (dv != null) {
/*     */             
/* 204 */             if (Math.abs(dv.getCenterX() - performer.getTileX()) < 1 && Math.abs(dv.getCenterY() - performer.getTileY()) < 1) {
/*     */               
/* 206 */               int time = act.getTimeLeft();
/* 207 */               if (counter == 1.0F) {
/*     */                 
/* 209 */                 performer.getCommunicator().sendNormalServerMessage("You spot something glinting in the ground and start pulling it free...", (byte)2);
/*     */                 
/* 211 */                 Server.getInstance().broadCastAction(performer.getName() + " spots something in the ground.", performer, 5);
/* 212 */                 time = Actions.getVariableActionTime(performer, performer.getSkills().getSkillOrLearn(10069), null, 0.0D, 250, 100, 2500);
/* 213 */                 act.setTimeLeft(time);
/* 214 */                 performer.sendActionControl(act.getActionString(), true, act.getTimeLeft());
/* 215 */                 performer.getStatus().modifyStamina(-2500.0F);
/*     */               }
/* 217 */               else if (counter * 10.0F > time) {
/*     */                 
/* 219 */                 Item cache = FragmentUtilities.createVillageCache((Player)performer, target, dv, performer
/* 220 */                     .getSkills().getSkillOrLearn(10069));
/* 221 */                 if (cache != null) {
/*     */                   
/* 223 */                   cache.setPosXY((dv.getCenterX() * 4 + 2), (dv.getCenterY() * 4 + 2));
/* 224 */                   VolaTile t = Zones.getOrCreateTile(dv.getCenterX(), dv.getCenterY(), true);
/* 225 */                   t.addItem(cache, false, false);
/*     */                   
/* 227 */                   performer.getCommunicator().sendNormalServerMessage("As you discover a " + cache.getName() + " the report is crumpled up and ruined.", (byte)2);
/*     */                   
/* 229 */                   Server.getInstance().broadCastAction(performer.getName() + " pulls a " + cache.getName() + " from the ground.", performer, 5);
/* 230 */                   Items.destroyItem(target.getWurmId());
/*     */                   
/* 232 */                   performer.achievement(580);
/*     */                   
/* 234 */                   return new boolean[] { true, true };
/*     */                 } 
/*     */ 
/*     */                 
/* 238 */                 performer.getCommunicator().sendNormalServerMessage("An error occured. Please try again later.", (byte)2);
/* 239 */                 return new boolean[] { true, true };
/*     */               } 
/*     */ 
/*     */               
/* 243 */               return new boolean[] { true, false };
/*     */             } 
/*     */ 
/*     */             
/* 247 */             performer.getCommunicator().sendNormalServerMessage("Reading details from the report, " + dv.getDeedName() + " looks like it may have been " + dv
/* 248 */                 .getDistanceFrom(performer.getTileX(), performer.getTileY()) + " to the " + dv
/* 249 */                 .getDirectionFrom(performer.getTileX(), performer.getTileY()) + ".");
/*     */ 
/*     */ 
/*     */           
/*     */           }
/* 254 */           else if (WurmId.getOrigin(target.getData()) != Servers.localServer.getId()) {
/* 255 */             performer.getCommunicator().sendNormalServerMessage("The details of this report seem to lead you to distant lands.");
/*     */           } else {
/* 257 */             performer.getCommunicator().sendNormalServerMessage("You're unable to make sense of this report.");
/*     */           } 
/*     */           
/* 260 */           return new boolean[] { true, true };
/*     */         } 
/*     */       } else {
/* 263 */         if (action == 1) {
/*     */           
/* 265 */           if (target.getData() == -1L) {
/* 266 */             performer.getCommunicator().sendNormalServerMessage("A blank archaeological report. Investigating a tile and discovering information about a village will let you report it in this report.");
/*     */           } else {
/*     */             
/* 269 */             performer.getCommunicator().sendNormalServerMessage("An archaeological report of a village. Reading this will let you recall information that you have discovered about this village.");
/*     */           } 
/* 271 */           return new boolean[] { true, true };
/*     */         } 
/* 273 */         if (action == 7)
/*     */         {
/*     */           
/* 276 */           return new boolean[] { true, true }; } 
/*     */       } 
/*     */     }
/* 279 */     return new boolean[] { false, false };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void renameFolder(Item folder, Creature performer) {
/* 285 */     TextInputQuestion tiq = new TextInputQuestion(performer, "Setting name for folder.", "Set the new name:", 1, folder.getWurmId(), 20, false);
/*     */     
/* 287 */     tiq.setOldtext(folder.getName());
/* 288 */     tiq.sendQuestion();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\AlmanacBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */