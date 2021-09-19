/*     */ package com.wurmonline.server.behaviours;
/*     */ 
/*     */ import com.wurmonline.server.NoSuchItemException;
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*     */ import com.wurmonline.server.items.InscriptionData;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.questions.SimplePopup;
/*     */ import com.wurmonline.server.questions.TextInputQuestion;
/*     */ import com.wurmonline.server.zones.NoSuchZoneException;
/*     */ import com.wurmonline.server.zones.VolaTile;
/*     */ import com.wurmonline.server.zones.Zone;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import java.util.List;
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
/*     */ public final class GravestoneBehaviour
/*     */   extends ItemBehaviour
/*     */ {
/*     */   public GravestoneBehaviour() {
/*  42 */     super((short)48);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(Action act, Creature performer, Item target, short action, float counter) {
/*  48 */     if (action == 506)
/*  49 */       return readInscription(performer, target); 
/*  50 */     if (action == 177 || action == 178 || action == 181 || action == 99) {
/*     */ 
/*     */       
/*  53 */       if (canManipulateGrave(target, performer))
/*     */       {
/*  55 */         return MethodsItems.moveItem(performer, target, counter, action, act);
/*     */       }
/*     */ 
/*     */       
/*  59 */       performer.getCommunicator().sendNormalServerMessage("You may not push, pull or turn that item.");
/*  60 */       return true;
/*     */     } 
/*     */     
/*  63 */     if (action == 1) {
/*     */       
/*  65 */       performer.getCommunicator().sendNormalServerMessage(target.examine(performer));
/*  66 */       target.sendEnchantmentStrings(performer.getCommunicator());
/*  67 */       sendInscription(performer, target);
/*  68 */       return true;
/*     */     } 
/*     */     
/*  71 */     return super.action(act, performer, target, action, counter);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(Action act, Creature performer, Item source, Item target, short action, float counter) {
/*  77 */     if (action == 505)
/*  78 */       return inscribe(performer, source, target); 
/*  79 */     if (action == 506)
/*  80 */       return action(act, performer, target, action, counter); 
/*  81 */     if (action == 192)
/*     */     
/*  83 */     { if (target.creationState != 0) {
/*     */         
/*  85 */         int tid = MethodsItems.getItemForImprovement(target.getMaterial(), target.creationState);
/*  86 */         if (source.getTemplateId() == tid) {
/*  87 */           return MethodsItems.polishItem(act, performer, source, target, counter);
/*     */         }
/*     */       } else {
/*  90 */         return MethodsItems.improveItem(act, performer, source, target, counter);
/*     */       }  }
/*  92 */     else { if (action == 83 || action == 180) {
/*     */         
/*  94 */         if (performer.mayDestroy(target) || performer.getPower() >= 2) {
/*  95 */           return MethodsItems.destroyItem(action, performer, source, target, false, counter);
/*     */         }
/*  97 */         return true;
/*     */       } 
/*  99 */       if (action == 177 || action == 178 || action == 181 || action == 99 || action == 1)
/*     */       {
/* 101 */         return action(act, performer, target, action, counter); } 
/* 102 */       if (action == 179) {
/*     */         
/* 104 */         summon(performer, source, target);
/* 105 */         return true;
/*     */       } 
/* 107 */       if (action == 91) {
/*     */         
/* 109 */         if ((source.getTemplateId() == 176 || source.getTemplateId() == 315) && performer
/* 110 */           .getPower() >= 2) {
/*     */           
/* 112 */           float nut = (50 + Server.rand.nextInt(49)) / 100.0F;
/* 113 */           performer.getStatus().refresh(nut, false);
/*     */         } 
/* 115 */         return true;
/*     */       } 
/* 117 */       if (action == 503) {
/*     */         
/* 119 */         if (performer.getPower() >= 2)
/*     */         {
/* 121 */           if (source.getTemplateId() == 176 || source.getTemplateId() == 315)
/*     */           {
/* 123 */             Methods.sendCreateZone(performer);
/*     */           }
/*     */         }
/*     */       } else {
/*     */         
/* 128 */         return super.action(act, performer, source, target, action, counter);
/*     */       }  }
/* 130 */      return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private static final boolean canManipulateGrave(Item grave, Creature performer) {
/* 135 */     if (grave.lastOwner == performer.getWurmId())
/* 136 */       return true; 
/* 137 */     if (performer.getPower() >= 2) {
/* 138 */       return true;
/*     */     }
/*     */     
/* 141 */     VolaTile t = Zones.getTileOrNull(grave.getTileX(), grave.getTileY(), grave.isOnSurface());
/* 142 */     if (t != null)
/*     */     {
/* 144 */       if (t.getVillage() != null && t.getVillage().isCitizen(performer)) {
/* 145 */         return true;
/*     */       }
/*     */     }
/* 148 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private static final boolean inscribe(Creature performer, Item chisel, Item gravestone) {
/* 153 */     if (chisel == null) {
/*     */ 
/*     */       
/* 156 */       performer.getCommunicator().sendNormalServerMessage("You fumble with the " + chisel + " but you cannot figure out how it works.");
/*     */       
/* 158 */       return true;
/*     */     } 
/* 160 */     InscriptionData inscriptionData = gravestone.getInscription();
/* 161 */     if (!gravestone.canHaveInscription()) {
/*     */       
/* 163 */       performer.getCommunicator().sendNormalServerMessage("You cannot inscribe on that!");
/* 164 */       return true;
/*     */     } 
/*     */     
/* 167 */     if (inscriptionData != null)
/*     */     {
/* 169 */       if (inscriptionData.hasBeenInscribed()) {
/*     */         
/* 171 */         performer.getCommunicator().sendNormalServerMessage("This " + gravestone
/* 172 */             .getName() + " has already been inscribed by " + inscriptionData.getInscriber() + ".");
/* 173 */         return true;
/*     */       } 
/*     */     }
/*     */     
/* 177 */     int numberOfChars = (int)(gravestone.getQualityLevel() * 2.0F);
/*     */ 
/*     */ 
/*     */     
/* 181 */     TextInputQuestion tiq = new TextInputQuestion(performer, "Inscribing a message on " + gravestone.getName() + ".", "Inscribing is an irreversible process. Enter your important message here:", 2, gravestone.getWurmId(), numberOfChars, false);
/*     */     
/* 183 */     Server.getInstance().broadCastAction(performer
/* 184 */         .getName() + " starts to inscribe with " + chisel.getName() + " on " + gravestone.getNameWithGenus() + ".", performer, 5);
/*     */     
/* 186 */     tiq.sendQuestion();
/* 187 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private static final boolean readInscription(Creature performer, Item gravestone) {
/* 192 */     InscriptionData inscriptionData = gravestone.getInscription();
/* 193 */     if (inscriptionData != null) {
/*     */ 
/*     */       
/* 196 */       SimplePopup pp = new SimplePopup(performer, gravestone.getName(), inscriptionData.getInscription());
/* 197 */       performer.getCommunicator().sendNormalServerMessage("You read the " + gravestone.getName() + ".");
/* 198 */       pp.sendQuestion("Close");
/*     */     }
/*     */     else {
/*     */       
/* 202 */       performer.getCommunicator().sendNormalServerMessage("There was no inscription to read.");
/*     */     } 
/* 204 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void summon(Creature performer, Item wand, Item target) {
/* 209 */     int stid = wand.getTemplateId();
/* 210 */     if ((stid == 176 || stid == 315) && performer.getPower() >= 2) {
/*     */       
/*     */       try {
/*     */         
/* 214 */         Zone currZone = Zones.getZone((int)target.getPosX() >> 2, (int)target.getPosY() >> 2, target
/* 215 */             .isOnSurface());
/* 216 */         currZone.removeItem(target);
/* 217 */         target.putItemInfrontof(performer);
/*     */       }
/* 219 */       catch (NoSuchZoneException nsz) {
/*     */         
/* 221 */         performer.getCommunicator().sendNormalServerMessage("Failed to locate the zone for that item. Failed to summon.");
/*     */       
/*     */       }
/* 224 */       catch (NoSuchCreatureException nsc) {
/*     */         
/* 226 */         performer.getCommunicator().sendNormalServerMessage("Failed to locate the creature for that request.. you! Failed to summon.");
/*     */       
/*     */       }
/* 229 */       catch (NoSuchItemException nsi) {
/*     */         
/* 231 */         performer.getCommunicator().sendNormalServerMessage("Failed to locate the item for that request! Failed to summon.");
/*     */       
/*     */       }
/* 234 */       catch (NoSuchPlayerException nsp) {
/*     */         
/* 236 */         performer.getCommunicator().sendNormalServerMessage("Failed to locate the creature for that request.. you! Failed to summon.");
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void sendInscription(Creature performer, Item gravestone) {
/* 244 */     InscriptionData inscriptionData = gravestone.getInscription();
/* 245 */     if (inscriptionData != null) {
/*     */       
/* 247 */       String inscription = inscriptionData.getInscription();
/* 248 */       if (inscription.length() > 0) {
/*     */         
/* 250 */         performer.getCommunicator().sendNormalServerMessage("There is an inscription carved into the gravestone.");
/* 251 */         performer.getCommunicator().sendNormalServerMessage(inscription);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item target) {
/* 259 */     List<ActionEntry> toReturn = super.getBehavioursFor(performer, target);
/*     */     
/* 261 */     if (target.getTemplateId() == 822) {
/*     */       
/* 263 */       InscriptionData inscriptionData = target.getInscription();
/* 264 */       if (inscriptionData != null && inscriptionData.hasBeenInscribed())
/* 265 */         toReturn.add(Actions.actionEntrys[506]); 
/*     */     } 
/* 267 */     if (toReturn.contains(Actions.actionEntrys[59]) && !canManipulateGrave(target, performer))
/*     */     {
/* 269 */       toReturn.remove(Actions.actionEntrys[59]);
/*     */     }
/* 271 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item target) {
/* 277 */     List<ActionEntry> toReturn = super.getBehavioursFor(performer, source, target);
/* 278 */     if (target.getTemplateId() == 822) {
/*     */       
/* 280 */       InscriptionData inscriptionData = target.getInscription();
/* 281 */       if (source.getTemplateId() == 97)
/*     */       {
/* 283 */         if (target.canHaveInscription())
/*     */         {
/* 285 */           if (inscriptionData == null || !inscriptionData.hasBeenInscribed() || performer
/* 286 */             .getPower() >= 2) {
/* 287 */             toReturn.add(Actions.actionEntrys[505]);
/*     */           }
/*     */         }
/*     */       }
/* 291 */       if (inscriptionData != null && inscriptionData.hasBeenInscribed())
/* 292 */         toReturn.add(Actions.actionEntrys[506]); 
/*     */     } 
/* 294 */     if (toReturn.contains(Actions.actionEntrys[59]) && !canManipulateGrave(target, performer))
/*     */     {
/* 296 */       toReturn.remove(Actions.actionEntrys[59]);
/*     */     }
/* 298 */     return toReturn;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\GravestoneBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */