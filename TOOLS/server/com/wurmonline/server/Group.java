/*     */ package com.wurmonline.server;
/*     */ 
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.players.MapAnnotation;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Group
/*     */ {
/*     */   protected final Map<String, Creature> members;
/*     */   protected String name;
/*  45 */   protected static final Logger logger = Logger.getLogger(Group.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Group(String aName) {
/*  55 */     this.members = new HashMap<>();
/*  56 */     this.name = aName;
/*  57 */     if (logger.isLoggable(Level.FINER))
/*     */     {
/*  59 */       logger.finer("Creating a Group - Name: " + this.name);
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
/*     */   public String getName() {
/*  71 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setName(String newName) {
/*  76 */     this.name = newName;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Creature c) {
/*  81 */     return (this.members.get(c.getName()) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTeam() {
/*  86 */     return false;
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
/*     */   public final void addMember(String aName, Creature aMember) {
/*  99 */     if (!this.members.values().contains(aMember)) {
/* 100 */       this.members.put(aName, aMember);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void dropMember(String aName) {
/* 111 */     this.members.remove(aName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getNumberOfMembers() {
/* 121 */     return (this.members != null) ? this.members.size() : 0;
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
/*     */   public final void sendMessage(Message message) {
/* 133 */     for (Creature c : this.members.values()) {
/*     */       
/* 135 */       Creature sender = message.getSender();
/* 136 */       if (sender == null || !c.isIgnored(sender.getWurmId())) {
/* 137 */         c.getCommunicator().sendMessage(message);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public final void sendMapAnnotation(MapAnnotation[] annotations) {
/* 143 */     for (Creature c : this.members.values())
/*     */     {
/* 145 */       c.getCommunicator().sendMapAnnotations(annotations);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final void sendRemoveMapAnnotation(MapAnnotation annotation) {
/* 151 */     for (Creature c : this.members.values())
/*     */     {
/* 153 */       c.getCommunicator().sendRemoveMapAnnotation(annotation.getId(), annotation.getType(), annotation.getServer());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final void sendClearMapAnnotationsOfType(byte type) {
/* 159 */     for (Creature c : this.members.values())
/*     */     {
/* 161 */       c.getCommunicator().sendClearMapAnnotationsOfType(type);
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
/*     */   public final void broadCastSafe(String message) {
/* 174 */     broadCastSafe(message, (byte)0);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void broadCastSafe(String message, byte messageType) {
/* 179 */     for (Creature player : this.members.values())
/*     */     {
/* 181 */       player.getCommunicator().sendSafeServerMessage(message, messageType);
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
/*     */   public final void broadCastAlert(String message, byte messageType) {
/* 194 */     for (Creature player : this.members.values())
/*     */     {
/* 196 */       player.getCommunicator().sendAlertServerMessage(message, messageType);
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
/*     */   public final void broadCastNormal(String message) {
/* 209 */     for (Creature player : this.members.values())
/*     */     {
/* 211 */       player.getCommunicator().sendNormalServerMessage(message);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsOfflineMember(long wurmid) {
/* 217 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\Group.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */