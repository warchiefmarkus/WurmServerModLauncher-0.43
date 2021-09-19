/*     */ package com.wurmonline.server.webinterface;
/*     */ 
/*     */ import com.wurmonline.server.LoginServerWebConnection;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.WurmId;
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
/*     */ public abstract class WebCommand
/*     */ {
/*     */   private final long id;
/*     */   private byte[] data;
/*     */   private final short type;
/*     */   public static final short WC_TYPE_NONE = 0;
/*     */   public static final short WC_TYPE_GM_MESSAGE = 1;
/*     */   public static final short WC_TYPE_SERVER_MESSAGE = 2;
/*     */   public static final short WC_TYPE_DEMOTION = 3;
/*     */   public static final short WC_TYPE_DELETION = 4;
/*     */   public static final short WC_TYPE_REFRESHPINF = 5;
/*     */   public static final short WC_TYPE_RESET = 6;
/*     */   public static final short WC_TYPE_KINGDOMINFO = 7;
/*     */   public static final short WC_TYPE_KINGDOMDELETE = 8;
/*     */   public static final short WC_TYPE_EPICEVENT = 9;
/*     */   public static final short WC_TYPE_EPICSTATUS = 10;
/*     */   public static final short WC_TYPE_EPICSCENARIO = 11;
/*     */   public static final short WC_TYPE_OPENEPIC = 12;
/*     */   public static final short WC_TYPE_KINGDOMCHAT = 13;
/*     */   public static final short WC_TYPE_GLOBALMODERATION = 14;
/*     */   public static final short WC_TYPE_GLOBALIGNORE = 15;
/*     */   public static final short WC_TYPE_KARMA = 16;
/*     */   public static final short WC_TYPE_GLOBALPM = 17;
/*     */   public static final short WC_TYPE_TICKET = 18;
/*     */   public static final short WC_TYPE_PLAYER_STATUS = 19;
/*     */   public static final short WC_TYPE_VOTING = 20;
/*     */   public static final short WC_TYPE_SPAWNS = 21;
/*     */   public static final short WC_TYPE_GLOBAL_ALARM = 22;
/*     */   public static final short WC_TYPE_CAHELPGROUP = 23;
/*     */   public static final short WC_TYPE_MGMT_MESSAGE = 24;
/*     */   public static final short WC_TYPE_ADD_FRIEND = 25;
/*     */   public static final short WC_TYPE_REDEEM_KEY = 26;
/*     */   public static final short WC_TYPE_VALREI_MAP_UPDATE = 27;
/*     */   public static final short WC_TYPE_TRADECHANNEL = 28;
/*     */   public static final short WC_TYPE_GV_HELP = 29;
/*     */   public static final short WC_TYPE_EXPEL_MEMBER = 30;
/*     */   public static final short WC_TYPE_TAB_LISTS = 31;
/*     */   public static final short WC_TYPE_TRELLO_HIGHWAY = 32;
/*     */   public static final short WC_TYPE_SET_POWER = 33;
/*     */   public static final short WC_TYPE_GET_HEROES = 34;
/*     */   public static final short WC_TYPE_TRELLO_DEATHS = 35;
/*     */   public static final short WC_DESTROY_CREATURE = 36;
/*     */   boolean isRestrictedEpic = false;
/*     */   
/*     */   WebCommand(long _id) {
/*  80 */     this.id = _id;
/*  81 */     this.type = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   WebCommand(long _id, short _type) {
/*  86 */     this.id = _id;
/*  87 */     this.type = _type;
/*     */   }
/*     */ 
/*     */   
/*     */   WebCommand(long _id, short _type, byte[] _data) {
/*  92 */     this.id = _id;
/*  93 */     this.type = _type;
/*  94 */     this.data = _data;
/*     */   }
/*     */ 
/*     */   
/*     */   public final short getType() {
/*  99 */     return this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEpicOnly() {
/* 104 */     return this.isRestrictedEpic;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final void setData(byte[] _data) {
/* 115 */     this.data = _data;
/*     */   }
/*     */ 
/*     */   
/*     */   public final byte[] getData() {
/* 120 */     return this.data;
/*     */   }
/*     */ 
/*     */   
/*     */   public final long getWurmId() {
/* 125 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract byte[] encode();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract boolean autoForward();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void execute();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final WebCommand createWebCommand(short wctype, long id, byte[] data) {
/* 147 */     switch (wctype) {
/*     */       
/*     */       case 1:
/* 150 */         return new WCGmMessage(id, data);
/*     */       case 3:
/* 152 */         return new WcDemotion(id, data);
/*     */       case 5:
/* 154 */         return new WcRefreshCommand(id, data);
/*     */       case 6:
/* 156 */         return new WcResetCommand(id, data);
/*     */       case 7:
/* 158 */         return new WcKingdomInfo(id, data);
/*     */       case 8:
/* 160 */         return new WcDeleteKingdom(id, data);
/*     */       case 9:
/* 162 */         return new WcEpicEvent(id, data);
/*     */       case 10:
/* 164 */         return new WcEpicStatusReport(id, data);
/*     */       case 11:
/* 166 */         return new WcCreateEpicMission(id, data);
/*     */       case 12:
/* 168 */         return new WcOpenEpicPortal(id, data);
/*     */       case 13:
/* 170 */         return new WcKingdomChat(id, data);
/*     */       case 14:
/* 172 */         return new WcGlobalModeration(id, data);
/*     */       case 15:
/* 174 */         return new WcGlobalIgnore(id, data);
/*     */       case 16:
/* 176 */         return new WcEpicKarmaCommand(id, data);
/*     */       case 17:
/* 178 */         return new WcGlobalPM(id, data);
/*     */       case 18:
/* 180 */         return new WcTicket(id, data);
/*     */       case 19:
/* 182 */         return new WcPlayerStatus(id, data);
/*     */       case 4:
/* 184 */         return new WcRemoveFriendship(id, data);
/*     */       case 20:
/* 186 */         return new WcVoting(id, data);
/*     */       case 21:
/* 188 */         return new WcSpawnPoints(id, data);
/*     */       case 22:
/* 190 */         return new WcGlobalAlarmMessage(id, data);
/*     */       case 23:
/* 192 */         return new WcCAHelpGroupMessage(id, data);
/*     */       case 24:
/* 194 */         return new WcMgmtMessage(id, data);
/*     */       case 25:
/* 196 */         return new WcAddFriend(id, data);
/*     */       case 26:
/* 198 */         return new WcRedeemKey(id, data);
/*     */       case 27:
/* 200 */         return new WCValreiMapUpdater(id, data);
/*     */       case 28:
/* 202 */         return new WcTradeChannel(id, data);
/*     */       case 29:
/* 204 */         return new WcGVHelpMessage(id, data);
/*     */       case 30:
/* 206 */         return new WcExpelMember(id, data);
/*     */       case 31:
/* 208 */         return new WcTabLists(id, data);
/*     */       case 32:
/* 210 */         return new WcTrelloHighway(id, data);
/*     */       case 33:
/* 212 */         return new WcSetPower(id, data);
/*     */       case 34:
/* 214 */         return new WcGetHeroes(id, data);
/*     */       case 35:
/* 216 */         return new WcTrelloDeaths(id, data);
/*     */       case 36:
/* 218 */         return new WcKillCommand(id, data);
/*     */     } 
/* 220 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getOriginServer() {
/* 226 */     return WurmId.getOrigin(this.id);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void sendToServer(int serverId) {
/* 234 */     encode();
/* 235 */     if (serverId == Servers.localServer.id) {
/*     */       
/* 237 */       execute();
/*     */     }
/*     */     else {
/*     */       
/* 241 */       LoginServerWebConnection lsw = new LoginServerWebConnection(serverId);
/* 242 */       lsw.sendWebCommand(this.type, this);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void sendToLoginServer() {
/* 251 */     encode();
/* 252 */     if (Servers.localServer.LOGINSERVER) {
/* 253 */       execute();
/*     */     } else {
/*     */       
/* 256 */       LoginServerWebConnection lsw = new LoginServerWebConnection();
/* 257 */       lsw.sendWebCommand(this.type, this);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void sendFromLoginServer() {
/* 266 */     encode();
/* 267 */     Servers.sendWebCommandToAllServers(this.type, this, isEpicOnly());
/*     */     
/* 269 */     if ((Servers.localServer.EPIC && isEpicOnly()) || getType() == 10 || getType() == 27)
/* 270 */       execute(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\webinterface\WebCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */