/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.MineDoorPermission;
/*     */ import com.wurmonline.server.creatures.MineDoorSettings;
/*     */ import com.wurmonline.server.players.PermissionsByPlayer;
/*     */ import com.wurmonline.server.villages.NoSuchVillageException;
/*     */ import com.wurmonline.server.villages.Village;
/*     */ import com.wurmonline.server.villages.Villages;
/*     */ import java.io.IOException;
/*     */ import java.util.Properties;
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
/*     */ public final class MineDoorQuestion
/*     */   extends Question
/*     */ {
/*  36 */   private PermissionsByPlayer[] permitted = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int tx;
/*     */ 
/*     */ 
/*     */   
/*     */   private final int ty;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MineDoorQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget, int tilex, int tiley) {
/*  51 */     super(aResponder, aTitle, aQuestion, 72, aTarget);
/*  52 */     this.tx = tilex;
/*  53 */     this.ty = tiley;
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
/*  64 */     MineDoorPermission md = MineDoorPermission.getPermission(this.tx, this.ty);
/*  65 */     this.permitted = md.getPermissionsPlayerList().getPermissionsByPlayer();
/*  66 */     boolean changed = false;
/*  67 */     int villid = -1;
/*  68 */     if (getResponder().getCitizenVillage() != null)
/*  69 */       villid = (getResponder().getCitizenVillage()).id; 
/*  70 */     int removed = 0;
/*  71 */     for (int x = 0; x < this.permitted.length; x++) {
/*     */       
/*  73 */       String str1 = "nperm" + x;
/*  74 */       String str2 = aAnswers.getProperty(str1);
/*  75 */       if (str2 != null && str2.equals("true")) {
/*     */         
/*  77 */         removed++;
/*  78 */         md.removeMDPerm(this.permitted[x].getPlayerId());
/*     */       } 
/*     */     } 
/*  81 */     if (removed > 0)
/*  82 */       getResponder().getCommunicator().sendNormalServerMessage("Removed " + removed + " existing permissions."); 
/*  83 */     String key = "vperm";
/*  84 */     String val = aAnswers.getProperty(key);
/*  85 */     if (val != null && val.equals("true")) {
/*     */       
/*  87 */       changed = (md.setVillageId(villid) || changed);
/*  88 */       getResponder().getCommunicator().sendNormalServerMessage("Added the right for your village to use the door.");
/*     */     }
/*  90 */     else if (md.getVillageId() == villid && villid >= 0) {
/*     */       
/*  92 */       changed = (md.setVillageId(-1) || changed);
/*  93 */       getResponder().getCommunicator().sendNormalServerMessage("Removed the right for your village to use the door.");
/*     */     } 
/*  95 */     key = "alliedperm";
/*  96 */     val = aAnswers.getProperty(key);
/*  97 */     if (val != null && val.equals("true")) {
/*     */       
/*  99 */       if (!md.isAllowAllies())
/*     */       {
/* 101 */         changed = (md.setAllowAllies(true) || changed);
/* 102 */         getResponder().getCommunicator().sendNormalServerMessage("Added the right for your alliance to use the door.");
/*     */       }
/*     */     
/* 105 */     } else if (md.isAllowAllies()) {
/*     */       
/* 107 */       changed = (md.setAllowAllies(false) || changed);
/* 108 */       getResponder().getCommunicator().sendNormalServerMessage("Removed the right for your alliance to use the door.");
/*     */     } 
/* 110 */     key = "allperm";
/* 111 */     val = aAnswers.getProperty(key);
/* 112 */     if (val != null && val.equals("true")) {
/*     */       
/* 114 */       if (!md.isAllowAll())
/*     */       {
/* 116 */         changed = (md.setAllowAll(true) || changed);
/* 117 */         getResponder().getCommunicator().sendNormalServerMessage("Added the right for anyone to use the door.");
/*     */       }
/*     */     
/* 120 */     } else if (md.isAllowAll()) {
/*     */       
/* 122 */       changed = (md.setAllowAll(false) || changed);
/* 123 */       getResponder().getCommunicator().sendNormalServerMessage("Removed the right for anyone to use the door.");
/*     */     } 
/*     */     
/* 126 */     key = "newperm";
/* 127 */     val = aAnswers.getProperty(key);
/* 128 */     if (val != null && val.length() > 0)
/*     */     {
/* 130 */       if (md.getPermissionsPlayerList().size() < md.getMaxAllowed()) {
/*     */         
/* 132 */         long mdid = Players.getInstance().getWurmIdByPlayerName(val);
/* 133 */         if (mdid > 0L) {
/*     */           
/* 135 */           byte kingdom = Players.getInstance().getKingdomForPlayer(mdid);
/* 136 */           if (kingdom == getResponder().getKingdomId()) {
/*     */             
/* 138 */             md.addMDPerm(mdid, MineDoorSettings.MinedoorPermissions.PASS.getValue());
/* 139 */             getResponder().getCommunicator().sendNormalServerMessage("Gave permission to " + val + ".");
/*     */           } else {
/*     */             
/* 142 */             getResponder().getCommunicator().sendNormalServerMessage("You may not permit the enemy " + val + " to enter the mine door.");
/*     */           } 
/*     */         } else {
/*     */           
/* 146 */           getResponder().getCommunicator().sendNormalServerMessage("There is no known player with the name " + val + ".");
/*     */         } 
/*     */       } else {
/*     */         
/* 150 */         getResponder().getCommunicator().sendNormalServerMessage("May not add new permissions at the moment. You only have 100 keys.");
/*     */       } 
/*     */     }
/*     */     
/* 154 */     key = "newcont";
/* 155 */     val = aAnswers.getProperty(key);
/* 156 */     if (val != null && val.length() > 0) {
/*     */       
/* 158 */       long mdid = Players.getInstance().getWurmIdByPlayerName(val);
/* 159 */       if (mdid > 0L) {
/*     */         
/* 161 */         byte kingdom = Players.getInstance().getKingdomForPlayer(mdid);
/* 162 */         if (kingdom == getResponder().getKingdomId()) {
/*     */           
/* 164 */           changed = (md.setController(mdid) || changed);
/* 165 */           getResponder().getCommunicator().sendNormalServerMessage("You gave control of the mine door to " + val + ".");
/*     */         }
/*     */         else {
/*     */           
/* 169 */           getResponder().getCommunicator().sendNormalServerMessage("You may not permit the enemy " + val + " to enter the mine door.");
/*     */         } 
/*     */       } else {
/*     */         
/* 173 */         getResponder().getCommunicator().sendNormalServerMessage("There is no known player with the name " + val + ".");
/*     */       } 
/*     */     } 
/* 176 */     key = "newname";
/* 177 */     val = aAnswers.getProperty(key);
/* 178 */     if (val != null) {
/* 179 */       changed = (md.setObjectName(val, getResponder()) || changed);
/*     */     }
/* 181 */     if (changed) {
/*     */       
/*     */       try {
/*     */         
/* 185 */         md.save();
/*     */       }
/* 187 */       catch (IOException iOException) {}
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
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/* 201 */     MineDoorPermission md = MineDoorPermission.getPermission(this.tx, this.ty);
/* 202 */     this.permitted = md.getPermissionsPlayerList().getPermissionsByPlayer();
/* 203 */     int villid = -1;
/* 204 */     if (getResponder().getCitizenVillage() != null)
/* 205 */       villid = (getResponder().getCitizenVillage()).id; 
/* 206 */     StringBuilder buf = new StringBuilder(getBmlHeader());
/* 207 */     buf.append("text{type='bold';text=\"Permissions to use mine door:\"}text{text=''}");
/* 208 */     String rvname = "none";
/* 209 */     if (villid >= 0) {
/*     */       
/*     */       try {
/*     */         
/* 213 */         rvname = Villages.getVillage(villid).getName();
/*     */       }
/* 215 */       catch (NoSuchVillageException noSuchVillageException) {}
/*     */     }
/*     */ 
/*     */     
/* 219 */     if (md.getVillageId() >= 0) {
/*     */ 
/*     */       
/*     */       try {
/* 223 */         Village cvill = Villages.getVillage(md.getVillageId());
/* 224 */         buf.append("text{text='Currently the village of " + cvill.getName() + " may use and manage the mine door.'}");
/*     */       }
/* 226 */       catch (NoSuchVillageException nsv) {
/*     */         
/* 228 */         md.setVillageId(-1);
/*     */         
/*     */         try {
/* 231 */           md.save();
/*     */         }
/* 233 */         catch (IOException iOException) {}
/*     */       } 
/*     */ 
/*     */       
/* 237 */       buf.append("checkbox{id='vperm';selected='" + ((md.getVillageId() == villid) ? 1 : 0) + "';text='Check here if you want everyone in your village (" + rvname + ") to be able to use and manage the mine door.'};");
/*     */ 
/*     */     
/*     */     }
/* 241 */     else if (villid >= 0) {
/*     */       
/* 243 */       buf.append("checkbox{id='vperm';selected='" + ((md.getVillageId() == villid) ? 1 : 0) + "';text='Check here if you want everyone in your village (" + rvname + ") to be able to use and manage the mine door.'};");
/*     */     } 
/*     */ 
/*     */     
/* 247 */     buf.append("checkbox{id='allperm';selected='" + md.isAllowAll() + "';text='Check here if you want everyone to be able to use the mine door.'};");
/*     */ 
/*     */     
/* 250 */     buf.append("checkbox{id='alliedperm';selected='" + md.isAllowAllies() + "';text='Check here if you want your allies to be able to use the mine door.'};");
/*     */     
/* 252 */     if (getResponder().getCitizenVillage() == null || getResponder().getCitizenVillage().getAllianceNumber() <= 0)
/*     */     {
/* 254 */       buf.append("text{text=\"Note that the alliance setting only has effect if your settlement actually is in an alliance.\"}");
/*     */     }
/*     */     
/* 257 */     if (this.permitted.length > 0) {
/*     */       
/* 259 */       buf.append("text{text='These are the people who may use and manage the mine door:'}");
/* 260 */       buf.append("table{rows='" + (this.permitted.length + 1) + "';cols='2';label{text='Remove'};label{text='Name'};");
/*     */ 
/*     */       
/* 263 */       for (int x = 0; x < this.permitted.length; x++) {
/*     */         
/* 265 */         String name = "unknown";
/*     */         
/*     */         try {
/* 268 */           name = Players.getInstance().getNameFor(this.permitted[x].getPlayerId());
/*     */         }
/* 270 */         catch (Exception exception) {}
/*     */ 
/*     */         
/* 273 */         buf.append("checkbox{id='nperm" + x + "';selected='false';text=''};label{text='" + name + "'};");
/*     */       } 
/* 275 */       buf.append("};");
/*     */     }
/* 277 */     else if (!md.isAllowAll()) {
/* 278 */       buf.append("text{text='No other people may use the mine door.'}");
/*     */     } 
/* 280 */     if (md.getPermissionsPlayerList().size() < md.getMaxAllowed()) {
/*     */       
/* 282 */       buf.append("text{text='Add new person:'};input{maxchars='40'; id='newperm'; text=''};");
/*     */     } else {
/*     */       
/* 285 */       buf.append("text{text='You have no more keys for the door to give away.'}");
/* 286 */     }  if (md.getController() == getResponder().getWurmId()) {
/*     */       
/* 288 */       buf.append("text{text='Only you may change the controller of this door.'}");
/* 289 */       buf.append("text{text='Change controller of this door to:'};input{maxchars='40'; id='newcont'; text=''};");
/* 290 */       buf.append("text{text=\"Rename this door\"};input{maxchars=\"40\"; id=\"newname\"; text=\"" + md.getObjectName() + "\"};");
/*     */     } 
/*     */     
/* 293 */     buf.append(createAnswerButton2());
/* 294 */     getResponder().getCommunicator().sendBml(400, 400, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\MineDoorQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */