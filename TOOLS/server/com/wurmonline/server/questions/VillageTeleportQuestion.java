/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.endgames.EndGameItem;
/*     */ import com.wurmonline.server.endgames.EndGameItems;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.villages.Village;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import java.util.Properties;
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
/*     */ public final class VillageTeleportQuestion
/*     */   extends Question
/*     */ {
/*  39 */   private static final Logger logger = Logger.getLogger(VillageTeleportQuestion.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int floorLevel;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VillageTeleportQuestion(Creature aResponder, String aTitle, String aQuestion, int aType, long aTarget) {
/*  52 */     super(aResponder, aTitle, aQuestion, 111, aTarget);
/*     */     
/*  54 */     this.floorLevel = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public VillageTeleportQuestion(Creature responder) {
/*  59 */     super(responder, "Village Teleport", "", 111, responder.getWurmId());
/*  60 */     this.floorLevel = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties aAnswers) {
/*  71 */     boolean teleport = (aAnswers.getProperty("teleport") != null && aAnswers.getProperty("teleport").equals("true"));
/*  72 */     this.floorLevel = getResponder().getFloorLevel();
/*  73 */     if (getResponder().isDead()) {
/*     */       
/*  75 */       getResponder().getCommunicator().sendNormalServerMessage("The dead can't teleport.");
/*     */       return;
/*     */     } 
/*  78 */     if (this.floorLevel != 0) {
/*     */       
/*  80 */       getResponder().getCommunicator().sendNormalServerMessage("You need to be on ground level to teleport.");
/*     */       return;
/*     */     } 
/*  83 */     Item[] inventoryItems = getResponder().getInventory().getAllItems(true);
/*  84 */     for (Item lInventoryItem : inventoryItems) {
/*     */       
/*  86 */       if (lInventoryItem.isArtifact()) {
/*     */         
/*  88 */         getResponder().getCommunicator().sendNormalServerMessage("The " + lInventoryItem
/*  89 */             .getName() + " hums and disturbs the weave. You can not teleport right now.");
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*  94 */     Item[] bodyItems = getResponder().getBody().getBodyItem().getAllItems(true);
/*  95 */     for (Item lInventoryItem : bodyItems) {
/*     */       
/*  97 */       if (lInventoryItem.isArtifact()) {
/*     */         
/*  99 */         getResponder().getCommunicator().sendNormalServerMessage("The " + lInventoryItem
/* 100 */             .getName() + " hums and disturbs the weave. You can not teleport right now.");
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 105 */     if (teleport && this.floorLevel == 0) {
/*     */       
/* 107 */       if (getResponder().getEnemyPresense() > 0 || getResponder().isFighting()) {
/*     */         
/* 109 */         getResponder().getCommunicator().sendNormalServerMessage("There are enemies in the vicinity. You fail to focus.");
/*     */         
/*     */         return;
/*     */       } 
/* 113 */       if (getResponder().getCitizenVillage() == null) {
/*     */         
/* 115 */         getResponder().getCommunicator().sendNormalServerMessage("You need to be citizen in a village to teleport home.");
/*     */         
/*     */         return;
/*     */       } 
/* 119 */       if (getResponder().isOnPvPServer() && 
/* 120 */         Zones.isWithinDuelRing(getResponder().getTileX(), getResponder().getTileY(), true) != null) {
/*     */         
/* 122 */         getResponder().getCommunicator().sendNormalServerMessage("The magic of the duelling ring interferes. You can not teleport here.");
/*     */         
/*     */         return;
/*     */       } 
/* 126 */       if (getResponder().isInPvPZone()) {
/*     */         
/* 128 */         getResponder().getCommunicator().sendNormalServerMessage("The magic of the pvp zone interferes. You can not teleport here.");
/*     */         
/*     */         return;
/*     */       } 
/* 132 */       if (Servers.localServer.PVPSERVER && EndGameItems.getEvilAltar() != null) {
/*     */         
/* 134 */         EndGameItem egi = EndGameItems.getEvilAltar();
/*     */         
/* 136 */         if (getResponder().isWithinDistanceTo(egi.getItem().getPosX(), egi.getItem().getPosY(), egi
/* 137 */             .getItem().getPosZ(), 50.0F)) {
/*     */ 
/*     */           
/* 140 */           getResponder().getCommunicator().sendNormalServerMessage("The magic of this place interferes. You can not teleport here.");
/*     */ 
/*     */           
/*     */           return;
/*     */         } 
/* 145 */       } else if (Servers.localServer.PVPSERVER && EndGameItems.getGoodAltar() != null) {
/*     */         
/* 147 */         EndGameItem egi = EndGameItems.getGoodAltar();
/*     */         
/* 149 */         if (getResponder().isWithinDistanceTo(egi.getItem().getPosX(), egi.getItem().getPosY(), egi
/* 150 */             .getItem().getPosZ(), 50.0F)) {
/*     */ 
/*     */           
/* 153 */           getResponder().getCommunicator().sendNormalServerMessage("The magic of this place interferes. You can not teleport here.");
/*     */           
/*     */           return;
/*     */         } 
/*     */       } 
/* 158 */       Village village = getResponder().getCitizenVillage();
/* 159 */       if (village != null) {
/*     */         
/* 161 */         getResponder().setTeleportPoints((short)village.getTokenX(), (short)village.getTokenY(), 0, 0);
/* 162 */         if (getResponder().startTeleporting())
/*     */         {
/* 164 */           getResponder().getCommunicator().sendTeleport(false);
/* 165 */           getResponder().teleport();
/* 166 */           ((Player)getResponder()).setUsedFreeVillageTeleport();
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 171 */         logger.log(Level.WARNING, getResponder().getName() + " tried to teleport to null settlement!");
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/* 184 */     this.floorLevel = getResponder().getFloorLevel();
/* 185 */     if (this.floorLevel == 0) {
/*     */       
/* 187 */       Village village = getResponder().getCitizenVillage();
/* 188 */       if (village != null)
/*     */       {
/* 190 */         String villageName = village.getName();
/* 191 */         StringBuilder buf = new StringBuilder();
/* 192 */         buf.append(getBmlHeader());
/*     */         
/* 194 */         buf.append("text{type=\"bold\";text=\"Teleport to settlement " + villageName + ":\"}");
/* 195 */         buf.append("text{text=\"\"}");
/* 196 */         buf.append("text{text=\"You have to option to teleport directly to the village token of your new village.\"}");
/* 197 */         buf.append("text{text=\"\"}");
/* 198 */         buf.append("text{type=\"bold\";text=\"You can only do this once per character.\"}");
/* 199 */         buf.append("text{text=\"\"}");
/*     */         
/* 201 */         buf.append("text{text=\"Do you want to teleport to " + villageName + "?\"}");
/*     */         
/* 203 */         buf.append("radio{ group=\"teleport\"; id=\"true\";text=\"Yes\"}");
/* 204 */         buf.append("radio{ group=\"teleport\"; id=\"false\";text=\"No\";selected=\"true\"}");
/*     */         
/* 206 */         buf.append(createOkAnswerButton());
/* 207 */         getResponder().getCommunicator().sendBml(300, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */       }
/*     */       else
/*     */       {
/* 211 */         logger.log(Level.WARNING, getResponder().getName() + " tried to teleport to null settlement!");
/* 212 */         getResponder().getCommunicator().sendNormalServerMessage("Failed to locate the settlement for the teleportation. Please contact administration.");
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 218 */       Village village = getResponder().getCitizenVillage();
/* 219 */       if (village != null) {
/*     */         
/* 221 */         String villageName = village.getName();
/* 222 */         StringBuilder buf = new StringBuilder();
/* 223 */         buf.append(getBmlHeader());
/*     */         
/* 225 */         buf.append("text{type=\"bold\";text=\"Teleport to settlement " + villageName + ":\"}");
/* 226 */         buf.append("text{text=\"\"}");
/* 227 */         buf.append("text{text=\"You have to option to teleport directly to the village token of your new village.\"}");
/* 228 */         buf.append("text{text=\"\"}");
/* 229 */         buf.append("text{type=\"bold\";text=\"You can only do this once per character.\"}");
/* 230 */         buf.append("text{text=\"\"}");
/* 231 */         buf.append("text{type=\"bold\";text=\"You need to be on ground level in order to teleport to your village.\"}");
/* 232 */         buf.append("text{type=\"bold\";text=\"Once on ground level write /vteleport in the chat to teleport.\"}");
/*     */         
/* 234 */         buf.append(createOkAnswerButton());
/* 235 */         getResponder().getCommunicator().sendBml(300, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */       }
/*     */       else {
/*     */         
/* 239 */         logger.log(Level.WARNING, getResponder().getName() + " tried to teleport to null settlement!");
/* 240 */         getResponder().getCommunicator().sendNormalServerMessage("Failed to locate the settlement for the teleportation. Please contact administration.");
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\VillageTeleportQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */