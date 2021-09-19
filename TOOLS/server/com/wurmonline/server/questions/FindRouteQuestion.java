/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.highways.HighwayFinder;
/*     */ import com.wurmonline.server.highways.Routes;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.villages.NoSuchVillageException;
/*     */ import com.wurmonline.server.villages.Village;
/*     */ import com.wurmonline.server.villages.Villages;
/*     */ import com.wurmonline.server.zones.VirtualZone;
/*     */ import com.wurmonline.server.zones.VolaTile;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import com.wurmonline.shared.util.StringUtilities;
/*     */ import java.util.Arrays;
/*     */ import java.util.Properties;
/*     */ import java.util.StringTokenizer;
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
/*     */ 
/*     */ public class FindRouteQuestion
/*     */   extends Question
/*     */ {
/*  50 */   private static final Logger logger = Logger.getLogger(FindRouteQuestion.class.getName());
/*  51 */   public String villageName = "";
/*     */   
/*     */   private Village[] villages;
/*     */   private Player player;
/*     */   
/*     */   public FindRouteQuestion(Creature aResponder, Item waystone) {
/*  57 */     super(aResponder, "Find a route", "Find a route", 139, waystone.getWurmId());
/*  58 */     if (aResponder.isPlayer()) {
/*  59 */       this.player = (Player)getResponder();
/*     */     } else {
/*  61 */       this.player = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/*  67 */     setAnswer(answers);
/*  68 */     if (this.type == 0) {
/*     */       
/*  70 */       logger.log(Level.INFO, "Received answer for a question with NOQUESTION.");
/*     */       return;
/*     */     } 
/*  73 */     if (this.type == 139) {
/*     */       
/*  75 */       Village village = null;
/*     */       
/*  77 */       this.villageName = getAnswer().getProperty("vname");
/*  78 */       this.villageName = this.villageName.replaceAll("\"", "");
/*  79 */       this.villageName = this.villageName.trim();
/*  80 */       if (this.villageName.length() > 3) {
/*     */ 
/*     */         
/*  83 */         this.villageName = StringUtilities.raiseFirstLetter(this.villageName);
/*  84 */         StringTokenizer tokens = new StringTokenizer(this.villageName);
/*  85 */         String newName = tokens.nextToken();
/*  86 */         while (tokens.hasMoreTokens())
/*  87 */           newName = newName + " " + StringUtilities.raiseFirstLetter(tokens.nextToken()); 
/*  88 */         this.villageName = newName;
/*     */       } 
/*     */       
/*  91 */       if (!this.villageName.isEmpty()) {
/*     */ 
/*     */         
/*     */         try {
/*     */ 
/*     */           
/*  97 */           village = Villages.getVillage(this.villageName);
/*  98 */           if ((Routes.getNodesFor(village)).length == 0) {
/*     */             
/* 100 */             this.player.getCommunicator().sendNormalServerMessage("Unable to find connected waystones in " + this.villageName);
/*     */             
/*     */             return;
/*     */           } 
/* 104 */         } catch (NoSuchVillageException e) {
/*     */           
/* 106 */           this.player.getCommunicator().sendNormalServerMessage("Unable to find a village with that name: " + this.villageName);
/*     */ 
/*     */           
/*     */           return;
/*     */         } 
/*     */       } else {
/* 112 */         String clear = getAnswer().getProperty("clear");
/* 113 */         if (clear != null && clear.equals("true")) {
/*     */           
/* 115 */           this.player.setHighwayPath("", null);
/* 116 */           for (Item waystone : Items.getWaystones()) {
/*     */             
/* 118 */             VolaTile vt = Zones.getTileOrNull(waystone.getTileX(), waystone.getTileY(), waystone.isOnSurface());
/*     */             
/* 120 */             if (vt != null)
/*     */             {
/* 122 */               for (VirtualZone vz : vt.getWatchers()) {
/*     */ 
/*     */                 
/*     */                 try {
/* 126 */                   if (vz.getWatcher().getWurmId() == this.player.getWurmId()) {
/*     */                     
/* 128 */                     this.player.getCommunicator().sendWaystoneData(waystone);
/*     */                     
/*     */                     break;
/*     */                   } 
/* 132 */                 } catch (Exception e) {
/*     */                   
/* 134 */                   logger.log(Level.WARNING, e.getMessage(), e);
/*     */                 } 
/*     */               } 
/*     */             }
/*     */           } 
/*     */           return;
/*     */         } 
/* 141 */         String villno = getAnswer().getProperty("vill");
/* 142 */         int vno = Integer.parseInt(villno);
/* 143 */         if (this.villages.length == 0 || vno > this.villages.length) {
/*     */           
/* 145 */           this.player.getCommunicator().sendNormalServerMessage("No village selected!");
/*     */           
/*     */           return;
/*     */         } 
/*     */         
/* 150 */         village = this.villages[vno];
/* 151 */         this.villageName = village.getName();
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 158 */       if (village.equals(this.player.getCurrentVillage())) {
/*     */         
/* 160 */         this.player.getCommunicator().sendNormalServerMessage("You are already in that village.");
/*     */         return;
/*     */       } 
/* 163 */       HighwayFinder.queueHighwayFinding((Creature)this.player, Routes.getNode(this.target), village, (byte)0);
/*     */ 
/*     */       
/* 166 */       this.player.achievement(524);
/*     */       return;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/* 174 */     if (this.player == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 179 */     StringBuilder buf = new StringBuilder(getBmlHeader());
/* 180 */     int height = 220;
/* 181 */     if (this.player.getHighwayPathDestination().length() > 0) {
/*     */       
/* 183 */       buf.append("harray{label{text=\"Already heading to: " + this.player
/* 184 */           .getHighwayPathDestination() + "  \"}button{id=\"clear\";text=\"Clear route\"};}");
/*     */       
/* 186 */       buf.append("label{text=\"\"}");
/* 187 */       height += 50;
/*     */     } 
/*     */     
/* 190 */     this.villages = Routes.getVillages(this.target);
/* 191 */     buf.append("harray{label{text=\"Find a route to village \"};dropdown{id=\"vill\";options=\"");
/* 192 */     if (this.villages.length == 0) {
/* 193 */       buf.append("None");
/*     */     } else {
/*     */       
/* 196 */       Arrays.sort((Object[])this.villages);
/* 197 */       for (int i = 0; i < this.villages.length; i++) {
/*     */         
/* 199 */         if (i > 0)
/* 200 */           buf.append(","); 
/* 201 */         buf.append(this.villages[i].getName());
/*     */       } 
/*     */     } 
/* 204 */     buf.append("\"}}");
/* 205 */     buf.append("text{text=\"You may also specify a village name here to get a route to it.\"}");
/* 206 */     buf.append("harray{input{maxchars=\"40\";id=\"vname\";text=\"\"}}");
/* 207 */     buf.append("text{text=\"Note: The village must have a waystone in it, and be connected to the highway system.\"}");
/*     */     
/* 209 */     buf.append("label{text=\"\"}");
/* 210 */     buf.append(createAnswerButton2());
/* 211 */     getResponder().getCommunicator().sendBml(400, height, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\FindRouteQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */