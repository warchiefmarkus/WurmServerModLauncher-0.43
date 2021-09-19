/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Groups;
/*     */ import com.wurmonline.server.NoSuchGroupException;
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.Team;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ public class TeamManagementQuestion
/*     */   extends Question
/*     */ {
/*     */   private final Creature invited;
/*     */   private final boolean mayInvite;
/*     */   private boolean founding;
/*     */   private boolean sendToResponder = true;
/*     */   private String teamName;
/*     */   private boolean managing = false;
/*     */   private boolean removing = false;
/*  48 */   private final List<Creature> memberlist = new LinkedList<>();
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
/*     */   public TeamManagementQuestion(Creature aResponder, String aTitle, String aQuestion, boolean _mayInvite, long aTarget, boolean manage, boolean removeSelf) throws NoSuchCreatureException, NoSuchPlayerException {
/*  64 */     super(aResponder, aTitle, aQuestion, 85, aTarget);
/*  65 */     this.invited = Server.getInstance().getCreature(aTarget);
/*     */     
/*  67 */     if (getResponder().getTeam() == null) {
/*  68 */       this.founding = true;
/*     */     } else {
/*  70 */       this.founding = false;
/*  71 */     }  this.mayInvite = _mayInvite;
/*  72 */     this.managing = manage;
/*  73 */     this.removing = removeSelf;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/*  84 */     setAnswer(answers);
/*     */     
/*  86 */     Creature asker = getResponder();
/*  87 */     if (this.sendToResponder) {
/*     */       
/*  89 */       if (this.managing) {
/*     */         
/*  91 */         String cancel = getAnswer().getProperty("cancel");
/*  92 */         if (cancel != null && cancel.equals("true")) {
/*     */           return;
/*     */         }
/*     */         
/*  96 */         String did = getAnswer().getProperty("did");
/*  97 */         if (did != null) {
/*     */           
/*     */           try {
/*     */             
/* 101 */             int x = Integer.parseInt(did);
/* 102 */             Creature c = this.memberlist.get(x);
/* 103 */             String kick = getAnswer().getProperty("kick");
/* 104 */             if (kick != null && kick.equals("true")) {
/*     */               
/* 106 */               if (getResponder().getTeam() == c.getTeam())
/*     */               {
/* 108 */                 getResponder().getCommunicator().sendNormalServerMessage("You remove " + c
/* 109 */                     .getName() + " from the team.");
/* 110 */                 c.setTeam(null, true);
/*     */               }
/*     */             
/*     */             } else {
/*     */               
/* 115 */               String appoint = getAnswer().getProperty("appoint");
/* 116 */               if (appoint != null && appoint.equals("true"))
/*     */               {
/* 118 */                 getResponder().getTeam().setNewLeader(c);
/*     */               }
/* 120 */               String invite = getAnswer().getProperty("invite");
/* 121 */               if (invite != null && invite.equals("true"))
/*     */               {
/* 123 */                 if (!c.isTeamLeader()) {
/* 124 */                   c.setMayInviteTeam(!c.mayInviteTeam());
/*     */                 }
/*     */               }
/*     */             } 
/* 128 */           } catch (NumberFormatException nf) {
/*     */             
/* 130 */             asker.getCommunicator().sendNormalServerMessage("Failed to parse " + did + " to a number.");
/*     */             return;
/*     */           } 
/*     */         }
/*     */         return;
/*     */       } 
/* 136 */       if (this.removing) {
/*     */         
/* 138 */         String cancel = getAnswer().getProperty("cancel");
/* 139 */         if (cancel != null && cancel.equals("true")) {
/*     */           return;
/*     */         }
/*     */         
/* 143 */         if (getResponder().getTeam() != null)
/* 144 */           getResponder().setTeam(null, true); 
/*     */         return;
/*     */       } 
/* 147 */       if (this.invited == null || !this.invited.hasLink()) {
/*     */         
/* 149 */         asker.getCommunicator().sendNormalServerMessage("The team invitation is lost - the creature can no longer be found.");
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 154 */         String cancel = getAnswer().getProperty("cancel");
/* 155 */         if (cancel != null && cancel.equals("true")) {
/*     */           
/* 157 */           asker.getCommunicator().sendNormalServerMessage("You decide to skip inviting for now.");
/*     */         
/*     */         }
/*     */         else {
/*     */           
/* 162 */           boolean _mayInvite = (getAnswer().getProperty("invite") != null && getAnswer().getProperty("invite").equals("true"));
/* 163 */           this.teamName = getAnswer().getProperty("teamname");
/* 164 */           if (this.teamName != null) {
/*     */             
/* 166 */             if (this.teamName.length() >= 21) {
/*     */               
/* 168 */               getResponder().getCommunicator().sendNormalServerMessage("Please select a shorter name. Max 21 characters.");
/*     */               
/*     */               return;
/*     */             } 
/* 172 */             if (QuestionParser.containsIllegalVillageCharacters(this.teamName)) {
/*     */               
/* 174 */               getResponder().getCommunicator().sendNormalServerMessage("The name " + this.teamName + " contains illegal characters. Please select another name.");
/*     */               
/*     */               return;
/*     */             } 
/*     */           } 
/*     */           
/* 180 */           if (this.teamName == null)
/*     */           {
/* 182 */             if (this.founding) {
/* 183 */               this.teamName = asker.getNamePossessive();
/* 184 */             } else if (asker.getTeam() != null) {
/* 185 */               this.teamName = asker.getTeam().getName();
/*     */             } 
/*     */           }
/*     */           try {
/* 189 */             Groups.getGroup(this.teamName);
/* 190 */             getResponder().getCommunicator().sendNormalServerMessage("The name " + this.teamName + " is already in use. Please select another name.");
/*     */ 
/*     */             
/*     */             return;
/* 194 */           } catch (NoSuchGroupException noSuchGroupException) {
/*     */ 
/*     */             
/*     */             try {
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 202 */               TeamManagementQuestion tj = new TeamManagementQuestion(asker, "Joining a team", "Do you want to join " + asker.getNamePossessive() + " team?", _mayInvite, this.target, false, false);
/* 203 */               tj.teamName = this.teamName;
/* 204 */               tj.sendToResponder = false;
/* 205 */               tj.sendQuestion();
/* 206 */               asker.getCommunicator().sendNormalServerMessage("You ask " + this.invited
/* 207 */                   .getName() + " to join your team " + this.teamName);
/*     */             }
/* 209 */             catch (Exception ex) {
/*     */               
/* 211 */               asker.getCommunicator().sendNormalServerMessage("The player could not be found.");
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } else {
/*     */       
/* 218 */       boolean join = (getAnswer().getProperty("join") != null && getAnswer().getProperty("join").equals("true"));
/* 219 */       if (this.invited == null || !this.invited.hasLink()) {
/*     */         
/* 221 */         asker.getCommunicator().sendNormalServerMessage("The team invitation is lost - the creature can no longer be found.");
/*     */       
/*     */       }
/* 224 */       else if (!join) {
/*     */         
/* 226 */         this.invited.getCommunicator().sendNormalServerMessage("You decline to join the team.");
/* 227 */         asker.getCommunicator().sendNormalServerMessage(this.invited.getName() + " declines to join the team.");
/*     */ 
/*     */       
/*     */       }
/* 231 */       else if (!this.founding && asker.getTeam() == null) {
/*     */         
/* 233 */         this.invited.getCommunicator().sendNormalServerMessage(asker.getName() + " is no longer part of a team.");
/* 234 */         asker.getCommunicator().sendNormalServerMessage("You are no longer part of a team.");
/*     */       }
/* 236 */       else if (!asker.mayInviteTeam()) {
/*     */         
/* 238 */         this.invited.getCommunicator().sendNormalServerMessage(asker
/* 239 */             .getName() + " may no longer invite to " + asker.getHisHerItsString() + " team.");
/* 240 */         asker.getCommunicator().sendNormalServerMessage("You may no longer invite to the team.");
/*     */       }
/*     */       else {
/*     */         
/* 244 */         if (this.founding) {
/*     */           
/* 246 */           getResponder().setTeam(new Team(this.teamName, getResponder()), true);
/* 247 */           getResponder().getCommunicator().sendNormalServerMessage("You form a team with " + this.invited
/* 248 */               .getName() + ".");
/*     */         } 
/* 250 */         this.invited.setTeam(getResponder().getTeam(), true);
/* 251 */         this.invited.setMayInviteTeam(this.mayInvite);
/* 252 */         this.invited.getCommunicator().sendNormalServerMessage("You join " + this.teamName + ".");
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
/*     */   public Creature getInvited() {
/* 264 */     return this.invited;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/* 270 */     StringBuilder buf = new StringBuilder();
/* 271 */     buf.append(getBmlHeader());
/* 272 */     if (this.sendToResponder) {
/*     */       
/* 274 */       if (this.managing) {
/*     */         
/* 276 */         buf.append("text{text=\"Here you may remove team members or appoint a new leader.\"};");
/* 277 */         Creature[] members = getResponder().getTeam().getMembers();
/*     */         
/* 279 */         Arrays.sort(members, new Comparator<Creature>()
/*     */             {
/*     */               
/*     */               public int compare(Creature o1, Creature o2)
/*     */               {
/* 284 */                 return o1.getName().compareTo(o2.getName());
/*     */               }
/*     */             });
/* 287 */         buf.append("harray{dropdown{id='did';options=\"");
/* 288 */         for (int x = 0; x < members.length; x++) {
/*     */           
/* 290 */           if (x > 0)
/* 291 */             buf.append(","); 
/* 292 */           this.memberlist.add(members[x]);
/* 293 */           buf.append(members[x].getName());
/* 294 */           if (members[x].mayInviteTeam())
/* 295 */             buf.append(" (invite)"); 
/*     */         } 
/* 297 */         buf.append("\"}};");
/* 298 */         buf
/* 299 */           .append("text{type='italic';text=\"The people who may invite to the team are listed with (invite) after their name.\"};");
/* 300 */         buf.append("text{text=\"\"};");
/*     */         
/* 302 */         buf.append("checkbox{  id='kick';text='Remove';selected='false'}");
/* 303 */         buf.append("checkbox{  id='appoint';text='Appoint leader';selected='false'}");
/* 304 */         buf.append("checkbox{  id='invite';text='Toggle may invite';selected='false'}");
/*     */         
/* 306 */         buf
/* 307 */           .append("harray {button{text='Cancel';id='cancel'};label{text=' ';id='spacedlxg'};button{text='Send';id='submit'}}}};null;null;}");
/*     */       }
/* 309 */       else if (this.removing) {
/*     */ 
/*     */         
/* 312 */         buf.append("text{text=\"Do you really want to leave the team?\"};");
/* 313 */         buf
/* 314 */           .append("harray {button{text='Cancel';id='cancel'};label{text=' ';id='spacedlxg'};button{text='Leave the team!';id='submit'}}}};null;null;}");
/*     */       }
/*     */       else {
/*     */         
/* 318 */         String tn = getResponder().getNamePossessive();
/* 319 */         boolean label = false;
/* 320 */         if (getResponder().getTeam() != null) {
/*     */           
/* 322 */           tn = getResponder().getTeam().getName();
/* 323 */           label = true;
/*     */         } 
/*     */         
/* 326 */         if (this.founding) {
/*     */           
/* 328 */           buf.append("text{text=\"Do you want to form a team with " + this.invited.getName() + "?\"}");
/* 329 */           buf.append("text{text=\"The main benefit of being in a team is a new chat window.\"}");
/* 330 */           if (!Servers.localServer.PVPSERVER) {
/*     */             
/* 332 */             buf.append("text{text=\"As part of a team you can also use each others tools and resources easier.\"}");
/* 333 */             buf.append("text{text=\"Other team members will be able to pick up your stuff close to you.\"}");
/*     */           } 
/*     */         } else {
/*     */           
/* 337 */           buf.append("text{text=\"Do you want to invite " + this.invited.getName() + " to " + tn + "?\"}");
/* 338 */         }  buf.append("text{text=\"\"};");
/*     */         
/* 340 */         if (!label) {
/*     */           
/* 342 */           buf.append("label{text=\"Select a name\"};");
/* 343 */           buf.append("input{id=\"teamname\";maxchars=\"20\";text=\"" + tn + "\"};");
/*     */         } 
/* 345 */         buf.append("label{text=\"Should " + this.invited.getName() + " be allowed to invite others to the team?\"};");
/* 346 */         buf.append("radio{ group='invite'; id='true';text='Yes';selected='true'}");
/* 347 */         buf.append("radio{ group='invite'; id='false';text='No'}");
/* 348 */         buf
/* 349 */           .append("harray {button{text='Cancel';id='cancel'};label{text=' ';id='spacedlxg'};button{text='Form team!';id='submit'}}}};null;null;}");
/*     */       } 
/* 351 */       getResponder().getCommunicator().sendBml(400, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */     }
/*     */     else {
/*     */       
/* 355 */       Team team = getResponder().getTeam();
/* 356 */       buf.append("text{text=\"You have been invited by " + getResponder().getName() + " to join " + 
/* 357 */           getResponder().getHisHerItsString() + " team " + this.teamName + ".\"}");
/* 358 */       buf.append("text{text=\"\"};");
/* 359 */       if (this.invited.getTeam() != null && this.invited.getTeam() != team)
/*     */       {
/* 361 */         buf.append("text{text=\"You will no longer be part of the team " + this.invited.getTeam().getName() + " if you accept.\"}");
/*     */       }
/*     */       
/* 364 */       buf.append("text{text=\"The main benefit of being in a team is a new chat window.\"}");
/* 365 */       if (!Servers.localServer.PVPSERVER) {
/*     */         
/* 367 */         buf.append("text{text=\"As part of a team you can also use each others tools and resources easier.\"}");
/* 368 */         buf.append("text{text=\"Other team members will be able to pick up your stuff close to you.\"}");
/*     */       } 
/* 370 */       buf.append("text{text=\"\"};");
/* 371 */       buf.append("text{text=\"Do you want to join " + this.teamName + "?\"}");
/* 372 */       buf.append("text{text=\"\"};");
/* 373 */       buf.append("radio{ group='join'; id='true';text='Yes'}");
/* 374 */       buf.append("radio{ group='join'; id='false';text='No';selected='true'}");
/* 375 */       buf.append(createAnswerButton2());
/*     */       
/* 377 */       getInvited().getCommunicator().sendBml(400, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\TeamManagementQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */