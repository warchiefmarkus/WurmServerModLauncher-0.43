/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.players.Friend;
/*     */ import com.wurmonline.server.players.PermissionsByPlayer;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.structures.NoSuchStructureException;
/*     */ import com.wurmonline.server.structures.Structure;
/*     */ import com.wurmonline.server.structures.Structures;
/*     */ import java.io.IOException;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
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
/*     */ public final class StructureManagement
/*     */   extends Question
/*     */ {
/*  45 */   private static final Logger logger = Logger.getLogger(StructureManagement.class.getName());
/*     */   
/*     */   private Player player;
/*     */   private Structure structure;
/*  49 */   private Friend[] friends = new Friend[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private PermissionsByPlayer[] guests;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StructureManagement(Creature aResponder, String aTitle, String aQuestion, int aType, long aTarget) {
/*  64 */     super(aResponder, aTitle, aQuestion, aType, aTarget);
/*  65 */     this.player = (Player)getResponder();
/*     */     
/*     */     try {
/*  68 */       this.structure = Structures.getStructure(this.target);
/*  69 */       this.friends = this.player.getFriends();
/*  70 */       this.guests = this.structure.getPermissionsPlayerList().getPermissionsByPlayer();
/*     */     }
/*  72 */     catch (NoSuchStructureException e) {
/*     */       
/*  74 */       logger.log(Level.INFO, getResponder().getWurmId() + " tried to manage structure with id " + this.target + " but no structure was found.");
/*     */       return;
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
/*     */   public void answer(Properties answers) {
/*  88 */     setAnswer(answers);
/*  89 */     QuestionParser.parseStructureManagement(this);
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
/* 100 */     StringBuilder buf = new StringBuilder(getBmlHeader());
/* 101 */     buf.append("text{type='bold';text='Add guest:'}");
/* 102 */     if (this.friends.length > 0) {
/*     */       
/* 104 */       Map<String, Long> map = new TreeMap<>();
/* 105 */       for (int x = 0; x < this.friends.length; x++) {
/*     */ 
/*     */         
/*     */         try {
/* 109 */           boolean add = true;
/* 110 */           for (int g = 0; g < this.guests.length; g++) {
/*     */             
/* 112 */             if (this.friends[x].getFriendId() == this.guests[g].getPlayerId()) {
/*     */               
/* 114 */               add = false;
/*     */               break;
/*     */             } 
/*     */           } 
/* 118 */           if (add) {
/* 119 */             map.put(Players.getInstance().getNameFor(this.friends[x].getFriendId()), new Long(this.friends[x].getFriendId()));
/*     */           }
/* 121 */         } catch (NoSuchPlayerException noSuchPlayerException) {
/*     */ 
/*     */         
/*     */         }
/* 125 */         catch (IOException iox) {
/*     */           
/* 127 */           getResponder().getCommunicator().sendAlertServerMessage("There was a problem when handling your request. Please contact an administrator.");
/*     */           
/* 129 */           logger.log(Level.WARNING, "Got ioexception when looking for player with id " + this.friends[x], iox);
/*     */         } 
/*     */       } 
/* 132 */       Set<Map.Entry<String, Long>> entries = map.entrySet();
/* 133 */       for (Iterator<Map.Entry<String, Long>> it = entries.iterator(); it.hasNext(); ) {
/*     */         
/* 135 */         Map.Entry<String, Long> e = it.next();
/* 136 */         buf.append("checkbox{id='f" + ((Long)e.getValue()).longValue() + "';text='" + (String)e.getKey() + "'}");
/*     */       } 
/*     */     } else {
/*     */       
/* 140 */       buf.append("text{type='bold';text='No friends to add.'}");
/* 141 */     }  buf.append("text{type='bold';text='Remove guest:'}");
/*     */     
/* 143 */     Map<String, Long> fset = new TreeMap<>();
/* 144 */     if (this.guests.length > 0)
/*     */     {
/* 146 */       for (int x = 0; x < this.guests.length; x++) {
/*     */ 
/*     */         
/*     */         try {
/* 150 */           if (this.guests[x].getPlayerId() != -20L && this.guests[x]
/* 151 */             .getPlayerId() != -30L && this.guests[x]
/* 152 */             .getPlayerId() != -40L) {
/* 153 */             fset.put(Players.getInstance().getNameFor(this.guests[x].getPlayerId()), Long.valueOf(this.guests[x].getPlayerId()));
/*     */           }
/* 155 */         } catch (NoSuchPlayerException noSuchPlayerException) {
/*     */ 
/*     */         
/*     */         }
/* 159 */         catch (IOException iox) {
/*     */           
/* 161 */           getResponder().getCommunicator().sendAlertServerMessage("There was a problem when handling your request. Please contact an administrator.");
/*     */           
/* 163 */           logger.log(Level.WARNING, "Got ioexception when looking for player with id " + this.guests[x], iox);
/*     */         } 
/*     */       } 
/*     */     }
/* 167 */     if (fset.isEmpty()) {
/* 168 */       buf.append("text{type='bold';text='No guests to remove.'}");
/*     */     } else {
/*     */       
/* 171 */       Set<Map.Entry<String, Long>> entries = fset.entrySet();
/* 172 */       for (Iterator<Map.Entry<String, Long>> it = entries.iterator(); it.hasNext(); ) {
/*     */         
/* 174 */         Map.Entry<String, Long> e = it.next();
/* 175 */         buf.append("checkbox{id='g" + ((Long)e.getValue()).longValue() + "';text='" + (String)e.getKey() + "'}");
/*     */       } 
/*     */     } 
/* 178 */     buf.append("text{type='bold';text='Lock structure:'}");
/*     */     
/* 180 */     if (!this.structure.isLockable())
/*     */     {
/* 182 */       buf.append("text{type='bold';text='WARNING! Not all doors have locks.'}");
/*     */     }
/*     */     
/* 185 */     if (this.structure.isLocked()) {
/* 186 */       buf.append("checkbox{id='unlock';text='Unlock all doors'}");
/*     */     } else {
/* 188 */       buf.append("checkbox{id='lock';text='Lock all doors'}");
/*     */     } 
/* 190 */     if (getResponder().getCitizenVillage() != null) {
/*     */       
/* 192 */       buf.append("checkbox{id='allowVillagers';text='Allow citizens to enter';selected=\"" + (
/* 193 */           this.structure.allowsCitizens() ? 1 : 0) + "\"}");
/* 194 */       buf.append("checkbox{id='allowAllies';text='Allow allies to enter';selected=\"" + (
/* 195 */           this.structure.allowsAllies() ? 1 : 0) + "\"}");
/* 196 */       buf.append("checkbox{id='allowKingdom';text='Allow kingdom to enter';selected=\"" + (
/* 197 */           this.structure.allowsKingdom() ? 1 : 0) + "\"}");
/*     */     } 
/* 199 */     buf.append("text{type='bold';text='Change name:'}");
/* 200 */     buf.append("input{maxchars='40'; id='sname'; text=\"" + this.structure.getName() + "\"}");
/* 201 */     buf.append("text{type='italic';text='Note! The name may contain the following letters: '}");
/* 202 */     buf.append("text{type='italic';text=\"a-z,A-Z,', and -\"}");
/* 203 */     buf.append("text{text=''}checkbox{id='demolish';text='Destroy this structure';selected='false';confirm=\"You are about to demolish this building" + (
/*     */ 
/*     */         
/* 206 */         this.structure.hasBridgeEntrance() ? " and connected bridge(s)" : "") + ".\";question=\"Are you sure you want to do that?\"}text{text=''}text{text=''}");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 211 */     buf.append(createAnswerButton2());
/*     */     
/* 213 */     getResponder().getCommunicator().sendBml(300, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\StructureManagement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */