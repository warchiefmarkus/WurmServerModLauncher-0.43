/*     */ package com.wurmonline.server;
/*     */ 
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public class Team
/*     */   extends Group
/*     */ {
/*  29 */   private Creature leader = null;
/*  30 */   private final Map<Long, Boolean> offlineMembers = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Team(String aName, Creature _leader) {
/*  38 */     super(aName);
/*  39 */     this.leader = _leader;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTeam() {
/*  45 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTeamLeader(Creature c) {
/*  50 */     return (c == this.leader);
/*     */   }
/*     */ 
/*     */   
/*     */   public Creature[] getMembers() {
/*  55 */     return (Creature[])this.members.values().toArray((Object[])new Creature[this.members.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void setNewLeader(Creature newLeader) {
/*  60 */     this.leader = newLeader;
/*     */     
/*  62 */     Message m = new Message(newLeader, (newLeader == this.leader) ? 14 : 13, "Team", newLeader.getName() + " has been appointed new leader.");
/*     */ 
/*     */     
/*  65 */     for (Creature c : this.members.values()) {
/*     */       
/*  67 */       c.getCommunicator().sendRemoveTeam(newLeader.getName());
/*  68 */       c.getCommunicator().sendAddTeam(newLeader.getName(), newLeader.getWurmId());
/*  69 */       c.getCommunicator().sendMessage(m);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void creatureJoinedTeam(Creature joined) {
/*  75 */     addMember(joined.getName(), joined);
/*  76 */     Message m = new Message(joined, (joined == this.leader) ? 14 : 13, "Team", "Welcome to team chat.");
/*  77 */     joined.getCommunicator().sendMessage(m);
/*  78 */     for (Creature c : this.members.values()) {
/*     */       
/*  80 */       c.getCommunicator().sendAddTeam(joined.getName(), joined.getWurmId());
/*  81 */       joined.getCommunicator().sendAddTeam(c.getName(), c.getWurmId());
/*     */     } 
/*  83 */     if (this.offlineMembers.containsKey(Long.valueOf(joined.getWurmId()))) {
/*     */       
/*  85 */       Boolean mayInvite = this.offlineMembers.remove(Long.valueOf(joined.getWurmId()));
/*  86 */       joined.setMayInviteTeam(mayInvite.booleanValue());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void creatureReconnectedTeam(Creature joined) {
/*  92 */     Message m = new Message(joined, (joined == this.leader) ? 14 : 13, "Team", "Welcome to team chat.");
/*  93 */     joined.getCommunicator().sendMessage(m);
/*  94 */     for (Creature c : this.members.values())
/*     */     {
/*  96 */       joined.getCommunicator().sendAddTeam(c.getName(), c.getWurmId());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final void creaturePartedTeam(Creature parted, boolean sendRemove) {
/* 102 */     for (Creature c : this.members.values()) {
/*     */       
/* 104 */       c.getCommunicator().sendRemoveTeam(parted.getName());
/* 105 */       if (sendRemove)
/* 106 */         parted.getCommunicator().sendRemoveTeam(c.getName()); 
/*     */     } 
/* 108 */     dropMember(parted.getName());
/* 109 */     if (this.members.size() == 1) {
/*     */       
/* 111 */       Creature[] s = getMembers();
/* 112 */       s[0].getCommunicator().sendNormalServerMessage("The team has dissolved.");
/* 113 */       s[0].setTeam(null, true);
/*     */     }
/* 115 */     else if (this.members.size() > 1) {
/*     */       
/* 117 */       if (parted == this.leader) {
/*     */         
/* 119 */         Creature[] s = getMembers();
/* 120 */         setNewLeader(s[0]);
/*     */ 
/*     */         
/* 123 */         if (!sendRemove) {
/* 124 */           this.offlineMembers.put(Long.valueOf(parted.getWurmId()), Boolean.valueOf(parted.mayInviteTeam()));
/*     */         }
/*     */       } 
/*     */     } else {
/*     */       
/* 129 */       Groups.removeGroup(this.name);
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
/*     */   
/*     */   public final void sendTeamMessage(Creature sender, Message message) {
/* 144 */     for (Creature c : this.members.values()) {
/*     */       
/* 146 */       if (!c.isIgnored(message.getSender().getWurmId())) {
/* 147 */         c.getCommunicator().sendMessage(message);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsOfflineMember(long wurmid) {
/* 154 */     return this.offlineMembers.keySet().contains(Long.valueOf(wurmid));
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
/*     */   public final void sendTeamMessage(Creature sender, String message) {
/* 168 */     Message m = new Message(sender, (sender == this.leader) ? 14 : 13, "Team", "<" + sender.getName() + "> " + message);
/*     */     
/* 170 */     for (Creature c : this.members.values()) {
/*     */       
/* 172 */       if (!c.isIgnored(m.getSender().getWurmId()))
/* 173 */         c.getCommunicator().sendMessage(m); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\Team.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */