/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.players.Friend;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.players.PlayerInfoFactory;
/*     */ import com.wurmonline.server.players.PlayerState;
/*     */ import java.util.Arrays;
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
/*     */ public final class ManageFriends
/*     */   extends Question
/*     */ {
/*  36 */   private static final Logger logger = Logger.getLogger(ManageFriends.class.getName());
/*     */ 
/*     */   
/*     */   private final Friend[] friends;
/*     */ 
/*     */   
/*     */   private final Player player;
/*     */ 
/*     */   
/*     */   private static final String line = "label{type=\"bold\";text=\"- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -\"}";
/*     */ 
/*     */ 
/*     */   
/*     */   public ManageFriends(Creature aResponder) {
/*  50 */     super(aResponder, aResponder.getName() + "'s List of Friends", "Manage Your List of Friends", 118, -10L);
/*  51 */     this.player = (Player)getResponder();
/*  52 */     this.friends = this.player.getFriends();
/*  53 */     Arrays.sort((Object[])this.friends);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties aAnswer) {
/*  63 */     setAnswer(aAnswer);
/*  64 */     if (this.type == 0) {
/*     */       
/*  66 */       logger.log(Level.INFO, "Received answer for a question with NOQUESTION.");
/*     */       return;
/*     */     } 
/*  69 */     if (this.type == 118) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  74 */       for (int i = 0; i < this.friends.length; i++) {
/*     */         
/*  76 */         String remove = aAnswer.getProperty("rem" + i);
/*  77 */         if (remove != null && remove.equalsIgnoreCase("true")) {
/*     */           
/*  79 */           this.player.removeFriend(this.friends[i].getFriendId());
/*  80 */           this.player.removeMeFromFriendsList(this.friends[i].getFriendId(), this.friends[i].getName());
/*     */           
/*  82 */           ManageFriends mf = new ManageFriends(getResponder());
/*  83 */           mf.sendQuestion();
/*     */           return;
/*     */         } 
/*     */       } 
/*  87 */       String reply = aAnswer.getProperty("reply");
/*  88 */       if (reply != null && reply.equalsIgnoreCase("true")) {
/*     */         
/*  90 */         String cat = aAnswer.getProperty("cat");
/*  91 */         String category = Friend.Category.catFromInt(Integer.parseInt(cat)).name();
/*  92 */         String wffn = this.player.waitingForFriend();
/*  93 */         if (wffn.length() > 0) {
/*     */           
/*  95 */           this.player.getCommunicator().addFriend(wffn, category);
/*     */           
/*  97 */           ManageFriends mf = new ManageFriends(getResponder());
/*  98 */           mf.sendQuestion();
/*     */           
/*     */           return;
/*     */         } 
/* 102 */         this.player.getCommunicator().sendNormalServerMessage("Too slow! Noone is waiting for a reply anymore.");
/*     */       } 
/*     */       
/* 105 */       String add = aAnswer.getProperty("add");
/* 106 */       if (add != null && add.equalsIgnoreCase("true")) {
/*     */         
/* 108 */         String addname = aAnswer.getProperty("addname");
/* 109 */         String cat = aAnswer.getProperty("addcat");
/* 110 */         String category = Friend.Category.catFromInt(Integer.parseInt(cat)).name();
/*     */         
/* 112 */         if (addname.length() < 3) {
/* 113 */           this.player.getCommunicator().sendNormalServerMessage("Name is too short");
/*     */         } else {
/* 115 */           this.player.getCommunicator().addFriend(addname, category);
/*     */         } 
/* 117 */         ManageFriends mf = new ManageFriends(getResponder());
/* 118 */         mf.sendQuestion();
/*     */         
/*     */         return;
/*     */       } 
/* 122 */       String update = aAnswer.getProperty("update");
/* 123 */       if (update != null && update.equalsIgnoreCase("true")) {
/*     */         
/* 125 */         boolean didChange = false;
/* 126 */         for (int j = 0; j < this.friends.length; j++) {
/*     */           
/* 128 */           String cat = aAnswer.getProperty("cat" + j);
/* 129 */           String note = aAnswer.getProperty("note" + j);
/* 130 */           if (cat != null) {
/*     */             
/* 132 */             byte catId = Byte.parseByte(cat);
/* 133 */             if (this.friends[j].getCatId() != catId || !this.friends[j].getNote().equals(note)) {
/*     */               
/* 135 */               ((Player)getResponder()).updateFriendData(this.friends[j].getFriendId(), catId, note);
/* 136 */               if (this.friends[j].getCatId() != catId)
/* 137 */                 getResponder().getCommunicator().sendNormalServerMessage(this.friends[j].getName() + " is now in your category " + 
/*     */                     
/* 139 */                     Friend.Category.catFromInt(catId).name() + "."); 
/* 140 */               if (!this.friends[j].getNote().equals(note))
/* 141 */                 getResponder().getCommunicator().sendNormalServerMessage("You added a note for " + this.friends[j]
/* 142 */                     .getName() + "."); 
/* 143 */               didChange = true;
/*     */             } 
/*     */           } 
/*     */         } 
/* 147 */         if (!didChange) {
/* 148 */           getResponder().getCommunicator().sendNormalServerMessage("You decide not to do anything.");
/*     */         }
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
/* 162 */     StringBuilder buf = new StringBuilder();
/* 163 */     boolean notFound = false;
/* 164 */     buf.append(getBmlHeader());
/* 165 */     buf.append("text{text=\"\"};");
/*     */     
/* 167 */     int row = 0;
/* 168 */     String blank = "image{src=\"img.gui.bridge.blank\";size=\"200,1\";text=\"\"}";
/* 169 */     for (Friend friend : this.friends) {
/*     */ 
/*     */       
/* 172 */       PlayerState pState = PlayerInfoFactory.getPlayerState(friend.getFriendId());
/* 173 */       String pName = "Not found";
/* 174 */       int cat = friend.getCatId();
/*     */       
/* 176 */       if (pState == null) {
/*     */ 
/*     */         
/* 179 */         notFound = true;
/*     */       }
/*     */       else {
/*     */         
/* 183 */         pName = pState.getPlayerName();
/*     */       } 
/* 185 */       buf.append("harray{varray{image{src=\"img.gui.bridge.blank\";size=\"200,1\";text=\"\"}label{text=\"" + pName + "\"}};radio{group=\"cat" + row + "\";id=\"3\";selected=\"" + ((cat == 3) ? 1 : 0) + "\";text=\"Trusted \";hover=\"Trusted\"}radio{group=\"cat" + row + "\";id=\"2\";selected=\"" + ((cat == 2) ? 1 : 0) + "\";text=\"Friend \";hover=\"Friend\"}radio{group=\"cat" + row + "\";id=\"1\";selected=\"" + ((cat == 1) ? 1 : 0) + "\";text=\"Contact \";hover=\"Contact\"}radio{group=\"cat" + row + "\";id=\"0\";selected=\"" + ((cat == 0) ? 1 : 0) + "\";text=\"Other \";hover=\"Other\"}harray{label{text=\" \"};button{id=\"rem" + row + "\";text=\"Remove\";confirm=\"You are about to remove " + pName + "  from your friends list.\";question=\"Do you really want to do that?\";hover=\"remove " + pName + " from your friends list\"}}}");
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
/* 200 */       buf.append("input{maxchars=\"40\";id=\"note" + row + "\";text=\"" + friend.getNote() + "\"};");
/* 201 */       row++;
/*     */     } 
/*     */     
/* 204 */     buf.append("text{text=\"\"};");
/*     */     
/* 206 */     buf.append("harray{button{text=\"Update Friends\";id=\"update\"}};");
/* 207 */     buf.append("text{text=\"\"};");
/* 208 */     if (notFound) {
/* 209 */       buf.append("label{text=\"'Not Found' could be the result of a server being offline.\"};");
/*     */     }
/* 211 */     buf.append("label{text=\"Note 'Remove' is immediate, but does double check.\"};");
/* 212 */     buf.append("label{type=\"bold\";text=\"- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -\"}");
/*     */     
/* 214 */     String wffn = this.player.waitingForFriend();
/* 215 */     if (wffn.length() != 0) {
/*     */       
/* 217 */       if (this.player.askingFriend()) {
/* 218 */         buf.append("text{text=\"You are still waiting for a response from " + wffn + ".\"};");
/*     */       } else {
/*     */         
/* 221 */         buf.append("label{text=\"" + wffn + " is waiting for you to add them to their list of friends. \"};");
/* 222 */         buf.append("harray{button{text=\"Send Reply\";id=\"reply\"};label{text=\" and add them to \"};dropdown{id=\"cat\";default=\"0\";options=\"Other,Contacts,Friends,Trusted\"}label{text=\" category.\"}};");
/*     */       
/*     */       }
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 230 */       buf.append("harray{button{text=\"Send Request\";id=\"add\"};label{text=\" to \"};input{maxchars=\"40\";id=\"addname\";onenter=\"add\"};label{text=\" so can add them to your \"};dropdown{id=\"addcat\";default=\"0\";options=\"Other,Contacts,Friends,Trusted\"}label{text=\" category.\"}};");
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 237 */     buf.append("text{text=\"\"}");
/* 238 */     buf.append("}};null;null;}");
/*     */     
/* 240 */     getResponder().getCommunicator().sendBml(500, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\ManageFriends.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */