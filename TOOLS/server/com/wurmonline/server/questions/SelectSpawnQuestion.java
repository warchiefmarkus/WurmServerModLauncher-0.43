/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.LoginHandler;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.epic.ValreiMapData;
/*     */ import com.wurmonline.server.intra.IntraServerConnection;
/*     */ import com.wurmonline.server.kingdom.Kingdom;
/*     */ import com.wurmonline.server.kingdom.Kingdoms;
/*     */ import com.wurmonline.server.players.Achievements;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.players.Spawnpoint;
/*     */ import com.wurmonline.shared.constants.PlayerOnlineStatus;
/*     */ import java.io.IOException;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Properties;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ public class SelectSpawnQuestion
/*     */   extends Question
/*     */ {
/*  26 */   private static final Logger logger = Logger.getLogger(SelectSpawnQuestion.class.getName());
/*     */   
/*     */   final String welcomeMess;
/*     */   boolean unDead = false;
/*  30 */   private final LinkedList<Kingdom> availKingdoms = new LinkedList<>();
/*     */   
/*     */   public SelectSpawnQuestion(Player aResponder, String aTitle, String aQuestion, long aTarget, String message, boolean Undead) {
/*  33 */     super((Creature)aResponder, aTitle, aQuestion, 134, aTarget);
/*  34 */     this.welcomeMess = message;
/*  35 */     this.unDead = Undead;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/*     */     try {
/*  42 */       Player player = (Player)getResponder();
/*  43 */       byte kingdom = 4;
/*  44 */       if (Servers.localServer.HOMESERVER) {
/*  45 */         kingdom = Servers.localServer.KINGDOM;
/*     */       } else {
/*     */         
/*     */         try {
/*  49 */           String did = answers.getProperty("kingdomid");
/*  50 */           int index = Integer.parseInt(did);
/*  51 */           Kingdom k = getAvailKingdoms().get(index);
/*     */           
/*  53 */           kingdom = (k == null) ? 0 : k.getId();
/*     */         }
/*  55 */         catch (Exception ex) {
/*     */           
/*  57 */           logger.log(Level.INFO, ex.getMessage(), ex);
/*     */         } 
/*  59 */       }  boolean male = true;
/*     */       
/*     */       try {
/*  62 */         male = Boolean.parseBoolean(answers.getProperty("male"));
/*     */       }
/*  64 */       catch (Exception ex) {
/*  65 */         logger.log(Level.INFO, ex.getMessage(), ex);
/*     */       } 
/*  67 */       if (!male)
/*  68 */         player.setSex((byte)1, false); 
/*  69 */       player.setKingdomId(kingdom, true);
/*  70 */       player.setBlood(IntraServerConnection.calculateBloodFromKingdom(kingdom));
/*  71 */       float posX = (Servers.localServer.SPAWNPOINTJENNX * 4 + Server.rand.nextInt(10));
/*  72 */       float posY = (Servers.localServer.SPAWNPOINTJENNY * 4 + Server.rand.nextInt(10));
/*     */       
/*  74 */       int r = Server.rand.nextInt(3);
/*     */       
/*  76 */       float rot = Server.rand.nextInt(360);
/*     */       
/*  78 */       if (this.unDead) {
/*     */         
/*  80 */         kingdom = 0;
/*  81 */         float[] txty = Player.findRandomSpawnX(false, false);
/*  82 */         posX = txty[0];
/*  83 */         posY = txty[1];
/*     */       }
/*     */       else {
/*     */         
/*  87 */         if (Servers.localServer.KINGDOM != 0) {
/*     */           
/*  89 */           kingdom = Servers.localServer.KINGDOM;
/*     */ 
/*     */         
/*     */         }
/*  93 */         else if (r == 1) {
/*     */           
/*  95 */           posX = (Servers.localServer.SPAWNPOINTMOLX * 4 + Server.rand.nextInt(10));
/*  96 */           posY = (Servers.localServer.SPAWNPOINTMOLY * 4 + Server.rand.nextInt(10));
/*     */         }
/*  98 */         else if (r == 2) {
/*     */           
/* 100 */           posX = (Servers.localServer.SPAWNPOINTLIBX * 4 + Server.rand.nextInt(10));
/* 101 */           posY = (Servers.localServer.SPAWNPOINTLIBY * 4 + Server.rand.nextInt(10));
/*     */         } 
/*     */         
/* 104 */         if (Servers.localServer.randomSpawns) {
/*     */           
/* 106 */           float[] txty = Player.findRandomSpawnX(true, true);
/* 107 */           posX = txty[0];
/* 108 */           posY = txty[1];
/*     */         } 
/*     */       } 
/* 111 */       Spawnpoint sp = LoginHandler.getInitialSpawnPoint(kingdom);
/* 112 */       if (sp != null) {
/*     */         
/* 114 */         posX = (sp.tilex * 4 + Server.rand.nextInt(10));
/* 115 */         posY = (sp.tiley * 4 + Server.rand.nextInt(10));
/*     */       } 
/* 117 */       player.setPositionX(posX);
/* 118 */       player.setPositionY(posY);
/* 119 */       player.setRotation(rot);
/* 120 */       LoginHandler.putOutsideWall(player);
/* 121 */       if (player.isOnSurface()) {
/*     */         
/* 123 */         LoginHandler.putOutsideHouse(player, false);
/* 124 */         LoginHandler.putOutsideFence(player);
/*     */       } 
/* 126 */       player.setTeleportPoints(posX, posY, 0, 0);
/* 127 */       player.startTeleporting();
/*     */ 
/*     */ 
/*     */       
/* 131 */       Players.getInstance().sendConnectInfo(player, " has logged in.", player.getLastLogin(), PlayerOnlineStatus.ONLINE, true);
/*     */       
/* 133 */       Players.getInstance().addToGroups(player);
/*     */       
/* 135 */       Server.getInstance().startSendingFinals(player);
/* 136 */       player.getCommunicator().sendMapInfo();
/* 137 */       Achievements.sendAchievementList((Creature)player);
/* 138 */       Players.loadAllPrivatePOIForPlayer(player);
/* 139 */       player.sendAllMapAnnotations();
/* 140 */       ValreiMapData.sendAllMapData(player);
/* 141 */       player.resetLastSentToolbelt();
/* 142 */     } catch (IOException e) {
/*     */       
/* 144 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/* 152 */     StringBuilder buf = new StringBuilder();
/* 153 */     buf.append(getBmlHeader());
/*     */     
/* 155 */     buf.append("text{text=\"\"}text{text=\"\"}");
/* 156 */     boolean selected = Server.rand.nextBoolean();
/* 157 */     buf.append("radio{ group='male'; id='false';text='Female';selected='" + (!selected ? 1 : 0) + "'}");
/* 158 */     buf.append("radio{ group='male'; id='true';text='Male';selected='" + selected + "'}");
/* 159 */     if (!Servers.localServer.HOMESERVER) {
/*     */       
/* 161 */       buf.append("text{text=\"\"}text{text=\"\"}");
/* 162 */       buf.append("text{text=\"Please select kingdom.\"}text{text=\"\"}");
/* 163 */       buf.append("harray{label{text='Kingdom: '};dropdown{id='kingdomid';options=\"");
/* 164 */       Kingdom[] kingdoms = Kingdoms.getAllKingdoms();
/* 165 */       for (int x = 0; x < kingdoms.length; x++) {
/*     */         
/* 167 */         if (kingdoms[x].getId() != 0 && kingdoms[x].existsHere() && (!kingdoms[x].isCustomKingdom() || kingdoms[x].acceptsTransfers())) {
/*     */           
/* 169 */           this.availKingdoms.add(kingdoms[x]);
/* 170 */           buf.append(kingdoms[x].getName());
/* 171 */           buf.append(",");
/*     */         } 
/*     */       } 
/* 174 */       buf.append(",None\"}}");
/*     */     } 
/* 176 */     buf.append(createAnswerButton2());
/* 177 */     getResponder().getCommunicator().sendBml(300, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   List<Kingdom> getAvailKingdoms() {
/* 187 */     return this.availKingdoms;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\SelectSpawnQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */