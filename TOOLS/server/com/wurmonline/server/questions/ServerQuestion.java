/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.ServerEntry;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.WurmId;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.shared.constants.CounterTypes;
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
/*     */ public final class ServerQuestion
/*     */   extends Question
/*     */   implements CounterTypes
/*     */ {
/*  36 */   private final List<ServerEntry> serverEntries = new LinkedList<>();
/*  37 */   private final List<ServerEntry> transferEntries = new LinkedList<>();
/*     */ 
/*     */   
/*     */   public ServerQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
/*  41 */     super(aResponder, aTitle, aQuestion, 43, aTarget);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/*  47 */     setAnswer(answers);
/*  48 */     QuestionParser.parseServerQuestion(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/*  54 */     StringBuilder buf = new StringBuilder();
/*  55 */     buf.append(getBmlHeader());
/*  56 */     if (getResponder().getPower() > 0) {
/*     */       ServerEntry[] entries;
/*  58 */       if (WurmId.getType(this.target) == 0) {
/*     */         
/*     */         try {
/*     */           
/*  62 */           Player p = Players.getInstance().getPlayer(this.target);
/*  63 */           buf.append("text{text='Careful! This will send " + p.getName() + " to another server:'}");
/*     */         }
/*  65 */         catch (NoSuchPlayerException noSuchPlayerException) {}
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  71 */       if (Servers.localServer.entryServer || Servers.isThisATestServer()) {
/*  72 */         entries = Servers.getAllServers();
/*     */       } else {
/*  74 */         entries = Servers.getAllNeighbours();
/*  75 */       }  buf.append("harray{label{type=\"bold\";text='Transfer to: '}dropdown{id='transferTo';default='0';options='");
/*  76 */       buf.append("None");
/*     */       
/*  78 */       for (int x = 0; x < entries.length; x++) {
/*     */         
/*  80 */         if (entries[x].isAvailable(getResponder().getPower(), getResponder().isPaying())) {
/*     */           
/*  82 */           buf.append(",");
/*  83 */           this.transferEntries.add(entries[x]);
/*  84 */           buf.append((entries[x]).name);
/*     */         } 
/*     */       } 
/*  87 */       if (Servers.localServer.id != Servers.loginServer.id) {
/*     */         
/*  89 */         buf.append(",");
/*  90 */         this.transferEntries.add(Servers.loginServer);
/*  91 */         buf.append(Servers.loginServer.name);
/*     */       } 
/*  93 */       buf.append("'}};");
/*     */     } 
/*  95 */     if (getResponder().getPower() >= 5) {
/*     */       
/*  97 */       buf.append("text{text='Manage servers. Add neighbours and other servers.'}");
/*  98 */       Servers.loadAllServers(true);
/*  99 */       buf.append("text{text='Reloaded all servers from database.'}");
/* 100 */       buf.append("table{rows=\"1\";cols=\"6\";");
/* 101 */       buf.append("label{type=\"bolditalic\";text=\"Type\"}label{type=\"bolditalic\";text=\"Name\"}label{type=\"bolditalic\";text=\"PvP\"}label{type=\"bolditalic\";text=\"Epic\"}label{type=\"bolditalic\";text=\"Home\"}label{type=\"bolditalic\";text=\"Chaos\"}");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 107 */       buf.append(getServerEntryData("Current", Servers.localServer));
/* 108 */       if (Servers.localServer.serverNorth != null)
/* 109 */         buf.append(getServerEntryData("NORTH", Servers.localServer.serverNorth)); 
/* 110 */       if (Servers.localServer.serverEast != null)
/* 111 */         buf.append(getServerEntryData("EAST", Servers.localServer.serverEast)); 
/* 112 */       if (Servers.localServer.serverSouth != null)
/* 113 */         buf.append(getServerEntryData("SOUTH", Servers.localServer.serverSouth)); 
/* 114 */       if (Servers.localServer.serverWest != null)
/* 115 */         buf.append(getServerEntryData("WEST", Servers.localServer.serverWest)); 
/* 116 */       if (Servers.loginServer != null)
/* 117 */         buf.append(getServerEntryData("LOGIN", Servers.loginServer)); 
/* 118 */       buf.append("}");
/* 119 */       buf.append("text{text=\"\"}");
/* 120 */       ServerEntry[] entries = Servers.getAllServers();
/* 121 */       buf.append("harray{label{type=\"bold\";text='Add neighbour: '}dropdown{id='neighbourServer';default='0';options='");
/* 122 */       buf.append("None");
/*     */       int x;
/* 124 */       for (x = 0; x < entries.length; x++) {
/*     */         
/* 126 */         buf.append(",");
/* 127 */         this.serverEntries.add(entries[x]);
/* 128 */         buf.append((entries[x]).name);
/*     */       } 
/* 130 */       buf.append("'}dropdown{id='direction';options='NORTH,EAST,SOUTH,WEST'}}");
/*     */       
/* 132 */       buf.append("harray{label{type=\"bold\";text='Remove server entry: '}dropdown{id='deleteServer';default='0';options='None");
/*     */ 
/*     */       
/* 135 */       for (x = 0; x < entries.length; x++) {
/*     */         
/* 137 */         buf.append(",");
/* 138 */         buf.append((entries[x]).name);
/*     */       } 
/* 140 */       buf.append("'}}");
/*     */       
/* 142 */       buf.append("label{type='bold';text='Add server entry: '}");
/* 143 */       buf.append("harray{label{text='Server Id: '}input{maxchars='3'; id='addid'}}");
/* 144 */       buf.append("harray{label{text='Name: '}input{maxchars='20'; id='addname'}}");
/* 145 */       buf.append("checkbox{text='Home Server?';id='addhome';selected='true'}");
/* 146 */       buf.append("checkbox{text='Payment Server?';id='addpayment';selected='true'}");
/* 147 */       buf.append("checkbox{text='Login Server?';id='addlogin;selected='false'}");
/* 148 */       buf.append("harray{label{text='Kingdom: '}dropdown{id='addkingdom';default='0';options='NONE,Jenn-Kellon,Mol Rehan,Horde of the Summoned'}}");
/*     */ 
/*     */ 
/*     */       
/* 152 */       buf.append("harray{label{text='StartJennX'}input{maxchars='4'; id='addsjx'}}");
/* 153 */       buf.append("harray{label{text='StartJennY'}input{maxchars='4'; id='addsjy'}}");
/* 154 */       buf.append("harray{label{text='StartMolX'}input{maxchars='4'; id='addsmx'}}");
/* 155 */       buf.append("harray{label{text='StartMolY'}input{maxchars='4'; id='addsmy'}}");
/* 156 */       buf.append("harray{label{text='StartLibX'}input{maxchars='4'; id='addslx'}}");
/* 157 */       buf.append("harray{label{text='StartLibY'}input{maxchars='4'; id='addsly'}}");
/* 158 */       buf.append("harray{label{text='External Ip'}input{maxchars='20'; id='addextip'}}");
/* 159 */       buf.append("harray{label{text='External Port'}input{maxchars='5'; id='addextport'}}");
/* 160 */       buf.append("harray{label{text='Internal Ip'}input{maxchars='20'; id='addintip'}}");
/* 161 */       buf.append("harray{label{text='Internal Port'}input{maxchars='5'; id='addintport'}}");
/* 162 */       buf.append("harray{label{text='Internal Password'}input{maxchars='50'; id='addintpass'}}");
/*     */     } 
/* 164 */     buf.append(createAnswerButton2());
/* 165 */     getResponder().getCommunicator().sendBml(300, 400, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ 
/*     */   
/*     */   private String getServerEntryData(String serverType, ServerEntry server) {
/* 170 */     StringBuilder buf = new StringBuilder();
/* 171 */     buf.append("label{text=\"" + serverType + "\"}label{text=\"" + server.name + "\"}label{text=\"" + server.PVPSERVER + "\"}label{text=\"" + server.EPIC + "\"}label{text=\"" + server.HOMESERVER + "\"}label{text=\"" + server
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 176 */         .isChaosServer() + "\"}");
/* 177 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ServerEntry getServerEntry(int aPosition) {
/* 187 */     return this.serverEntries.get(aPosition);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ServerEntry getTransferEntry(int aPosition) {
/* 197 */     return this.transferEntries.get(aPosition);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\ServerQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */