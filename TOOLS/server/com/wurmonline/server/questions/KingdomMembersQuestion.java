/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.WurmCalendar;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.kingdom.Appointment;
/*     */ import com.wurmonline.server.kingdom.Appointments;
/*     */ import com.wurmonline.server.kingdom.King;
/*     */ import com.wurmonline.server.kingdom.Kingdom;
/*     */ import com.wurmonline.server.kingdom.Kingdoms;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.players.PlayerInfo;
/*     */ import com.wurmonline.server.players.PlayerInfoFactory;
/*     */ import com.wurmonline.server.villages.Village;
/*     */ import com.wurmonline.server.villages.Villages;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Properties;
/*     */ 
/*     */ 
/*     */ public class KingdomMembersQuestion
/*     */   extends Question
/*     */   implements QuestionTypes
/*     */ {
/*     */   private static final int WIDTH = 600;
/*     */   private static final int HEIGHT = 250;
/*     */   private static final boolean RESIZEABLE = true;
/*     */   private static final boolean CLOSEABLE = true;
/*  32 */   private static final int[] RGB = new int[] { 200, 200, 200 };
/*     */   
/*     */   private static final String kID = "kingdomId";
/*     */   
/*     */   private static final String selectGrp = "select1";
/*     */   
/*     */   private static final String winTitle = "Kingdom Members";
/*     */   
/*     */   private static final String question = "Member List";
/*     */   
/*     */   private static final int maxRows = 25;
/*     */   
/*     */   private static final String prev = "prev";
/*     */   private static final String next = "next";
/*     */   private static final String expel = "expel";
/*     */   private static final String appoint = "appoint";
/*     */   private static final String toExpel = "toexpel";
/*     */   private static final String confirmExpel = "confirmExpel";
/*     */   private static final String confirmAppoint = "confirmAppoint";
/*     */   private static final String gmtool = "gmtool";
/*     */   private static final String filterMe = "filterMe";
/*     */   private static final String filterText = "filterText";
/*     */   private static final String cyan = "66,200,200";
/*     */   private static final String green = "66,225,66";
/*     */   private static final String orange = "255,156,66";
/*     */   private static final String red = "255,66,66";
/*     */   private static final String white = "255,255,255";
/*     */   private static final String colSize = "200,16";
/*     */   private static final int cols = 3;
/*  61 */   private List<Kingdom> klist = new LinkedList<>();
/*  62 */   private int currentIndex = 0;
/*  63 */   private Kingdom kingdom = Kingdoms.getKingdom((byte)0);
/*     */   private Player player;
/*     */   private PlayerInfo playerInfo;
/*     */   private PlayerInfo[] members;
/*  67 */   private String filter = "*";
/*     */ 
/*     */ 
/*     */   
/*     */   public KingdomMembersQuestion(Creature aResponder, long aTarget, byte kingdomId) {
/*  72 */     super(aResponder, "Kingdom Members", "Member List", 131, aTarget);
/*  73 */     this.kingdom = Kingdoms.getKingdom(kingdomId);
/*     */   }
/*     */ 
/*     */   
/*     */   public KingdomMembersQuestion(Creature aResponder, long aTarget, String kingdomName, byte kingdomId) {
/*  78 */     super(aResponder, "Kingdom Members [" + kingdomName + "]", "Member List for " + kingdomName, 131, aTarget);
/*  79 */     this.kingdom = Kingdoms.getKingdom(kingdomId);
/*     */ 
/*     */     
/*  82 */     this.kingdom.loadAllMembers();
/*     */   }
/*     */ 
/*     */   
/*     */   public KingdomMembersQuestion(Creature aResponder, long aTarget, Kingdom kingdom) {
/*  87 */     super(aResponder, "Kingdom Members [" + kingdom.getName() + "]", "Member List for " + kingdom.getName(), 131, aTarget);
/*  88 */     this.kingdom = kingdom;
/*     */   }
/*     */ 
/*     */   
/*     */   KingdomMembersQuestion(KingdomMembersQuestion old, String aTitle, String aQuestion) {
/*  93 */     super(old.getResponder(), aTitle, aQuestion, 131, old.getTarget());
/*  94 */     this.kingdom = old.kingdom;
/*  95 */     this.player = old.player;
/*  96 */     this.members = old.members;
/*  97 */     this.klist = old.klist;
/*  98 */     this.currentIndex = old.currentIndex;
/*  99 */     this.filter = old.filter;
/* 100 */     this.playerInfo = old.playerInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/* 107 */     String val = answers.getProperty("filterMe");
/* 108 */     if (val != null) {
/* 109 */       val = answers.getProperty("filterText");
/* 110 */       if (val != null) {
/*     */         
/* 112 */         this.filter = val;
/* 113 */         KingdomMembersQuestion kmq = new KingdomMembersQuestion(this, this.title, getQuestion());
/* 114 */         kmq.sendQuestion();
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*     */     
/* 120 */     if (getResponder().getPower() > 0) {
/*     */       
/* 122 */       val = answers.getProperty("kingdomId");
/* 123 */       if (val != null) {
/*     */         
/* 125 */         int i = Integer.parseInt(val);
/* 126 */         if (i == 0)
/*     */           return; 
/* 128 */         this.kingdom = this.klist.get(i);
/* 129 */         if (this.kingdom == null) {
/*     */           return;
/*     */         }
/* 132 */         if (this.kingdom.getId() != 4) {
/*     */           
/* 134 */           KingdomMembersQuestion kmq = new KingdomMembersQuestion(getResponder(), this.target, this.kingdom);
/* 135 */           kmq.sendQuestion();
/*     */           return;
/*     */         } 
/*     */       } 
/* 139 */       val = answers.getProperty("gmtool");
/* 140 */       if (val != null) {
/*     */         
/* 142 */         val = answers.getProperty("select1");
/* 143 */         if (val == null)
/*     */           return; 
/* 145 */         GmTool gmt = new GmTool(getResponder(), Long.parseLong(val));
/* 146 */         getResponder().getCommunicator().sendNormalServerMessage("Starting GM Tool for '" + val + "'");
/* 147 */         gmt.sendQuestion();
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*     */     
/* 153 */     val = answers.getProperty("next");
/* 154 */     if (val != null) {
/*     */       
/* 156 */       KingdomMembersQuestion kmq = new KingdomMembersQuestion(getResponder(), this.target, this.kingdom);
/* 157 */       this.currentIndex += 25;
/* 158 */       kmq.members = this.members;
/* 159 */       kmq.sendMemberList();
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 164 */     val = answers.getProperty("prev");
/* 165 */     if (val != null) {
/*     */       
/* 167 */       KingdomMembersQuestion kmq = new KingdomMembersQuestion(getResponder(), this.target, this.kingdom);
/* 168 */       this.currentIndex -= 25;
/* 169 */       kmq.members = this.members;
/* 170 */       kmq.sendMemberList();
/*     */       
/*     */       return;
/*     */     } 
/* 174 */     if (King.isKing(getResponder().getWurmId(), this.kingdom.getId())) {
/*     */ 
/*     */       
/* 177 */       val = answers.getProperty("appoint");
/* 178 */       if (val != null) {
/*     */         
/* 180 */         val = answers.getProperty("select1");
/* 181 */         if (val == null)
/*     */           return; 
/* 183 */         long wurmId = Long.parseLong(val);
/* 184 */         this.player = Players.getInstance().getPlayerOrNull(wurmId);
/* 185 */         if (this.player == null) {
/*     */           
/* 187 */           getResponder().getCommunicator().sendNormalServerMessage("You can only appoint members that are online.", (byte)3);
/*     */           
/*     */           return;
/*     */         } 
/* 191 */         KingdomMembersQuestion kmq = new KingdomMembersQuestion(this, "Appoint " + this.player.getName(), "Appointing " + this.player.getName());
/* 192 */         kmq.sendAppointWindow();
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 197 */       val = answers.getProperty("expel");
/* 198 */       if (val != null) {
/*     */         
/* 200 */         val = answers.getProperty("select1");
/* 201 */         if (val == null)
/*     */           return; 
/* 203 */         long wurmId = Long.parseLong(val);
/* 204 */         if (wurmId == getResponder().getWurmId()) {
/*     */           
/* 206 */           getResponder().getCommunicator()
/* 207 */             .sendNormalServerMessage("You are King, you cannot expel yourself. You must remove your crown and abdicate your throne.");
/*     */           
/*     */           return;
/*     */         } 
/* 211 */         this.playerInfo = PlayerInfoFactory.getPlayerInfoWithWurmId(wurmId);
/* 212 */         if (this.playerInfo == null) {
/*     */           
/* 214 */           getResponder().getCommunicator().sendNormalServerMessage("Unable to find that player.", (byte)3);
/*     */           
/*     */           return;
/*     */         } 
/*     */         
/* 219 */         KingdomMembersQuestion kmq = new KingdomMembersQuestion(this, "Expel " + this.playerInfo.getName(), "Expelling " + this.playerInfo.getName());
/* 220 */         kmq.sendExpelConfirmation();
/*     */         
/*     */         return;
/*     */       } 
/* 224 */       val = answers.getProperty("confirmExpel");
/* 225 */       if (val != null) {
/*     */         
/* 227 */         val = answers.getProperty("toexpel");
/* 228 */         if (val.compareTo(this.playerInfo.getName()) == 0) {
/*     */           
/* 230 */           this.kingdom.expelMember(getResponder(), this.playerInfo.getName());
/*     */           
/*     */           return;
/*     */         } 
/*     */       } 
/* 235 */       val = answers.getProperty("confirmAppoint");
/* 236 */       if (val != null) {
/*     */         
/* 238 */         King king = King.getKing(this.kingdom.getId());
/* 239 */         if (king == null)
/*     */           return; 
/* 241 */         Appointments apps = Appointments.getAppointments(king.era);
/* 242 */         if (apps == null)
/*     */           return; 
/* 244 */         answers.forEach((k, v) -> handleAppointment(k.toString(), v.toString(), apps));
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void handleAppointment(String k, String v, Appointments apps) {
/* 252 */     if (k.toString().contains("confirmAppoint") || k.toString().contains("id")) {
/*     */       return;
/*     */     }
/* 255 */     int aId = Integer.valueOf(k).intValue();
/* 256 */     boolean isSet = Boolean.valueOf(v).booleanValue();
/* 257 */     Appointment a = apps.getAppointment(aId);
/* 258 */     if (a == null)
/*     */       return; 
/* 260 */     if (a.getType() == 2 && apps.officials[aId - 1500] == this.player.getWurmId()) {
/*     */       
/* 262 */       if (!isSet) {
/*     */         
/* 264 */         this.player.getCommunicator().sendNormalServerMessage("You are hereby notified that you have been removed of the office as " + a
/*     */             
/* 266 */             .getNameForGender(this.player.getSex()) + ".", (byte)2);
/* 267 */         getResponder().getCommunicator().sendNormalServerMessage("You vacate the office of " + a
/* 268 */             .getNameForGender((byte)0) + ".", (byte)2);
/* 269 */         apps.setOfficial(aId, 0L);
/*     */       } else {
/*     */         
/*     */         return;
/*     */       } 
/* 274 */     } else if (isSet) {
/* 275 */       this.player.addAppointment(a, getResponder());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/* 281 */     if (!Servers.localServer.PVPSERVER || this.kingdom.getId() == 4) {
/*     */       
/* 283 */       getResponder().getCommunicator().sendNormalServerMessage("You may only list members in kingdoms on a PvP server.");
/*     */       return;
/*     */     } 
/* 286 */     if (getResponder().getPower() <= 0) {
/*     */       
/* 288 */       if (this.kingdom.getId() != getResponder().getKingdomId()) {
/*     */         
/* 290 */         getResponder().getCommunicator().sendNormalServerMessage("You may only view members in your own kingdom.");
/*     */         return;
/*     */       } 
/* 293 */       if (!this.kingdom.isCustomKingdom()) {
/*     */         
/* 295 */         getResponder().getCommunicator().sendNormalServerMessage("Only custom kingdoms may view their member roster.");
/*     */         
/*     */         return;
/*     */       } 
/* 299 */     } else if (this.kingdom.getId() == 0 && getResponder().getPower() > 0) {
/*     */       
/* 301 */       sendKingdomList();
/*     */       return;
/*     */     } 
/* 304 */     sendMemberList();
/*     */   }
/*     */ 
/*     */   
/*     */   private void sendKingdomList() {
/* 309 */     StringBuilder buf = new StringBuilder(getHeader("Member List"));
/* 310 */     buf.append("harray{label{text='Select kingdom:'};");
/* 311 */     buf.append("dropdown{id='kingdomId'; options='None,");
/* 312 */     Kingdom[] kingdoms = Kingdoms.getAllKingdoms();
/*     */     
/* 314 */     this.klist.add(Kingdoms.getKingdom((byte)0));
/* 315 */     for (int i = 0; i < kingdoms.length; i++) {
/*     */       
/* 317 */       if (kingdoms[i].getId() != 4 && kingdoms[i].getId() != 0) {
/*     */         
/* 319 */         buf.append(kingdoms[i].getName() + ",");
/* 320 */         this.klist.add(kingdoms[i]);
/*     */       } 
/* 322 */     }  buf.append("'}} text{text='Note: Freedom is too large for member lists.'}; text{text=''};");
/* 323 */     buf.append(createAnswerButton2());
/* 324 */     getResponder().getCommunicator().sendBml(250, 150, true, true, buf.toString(), RGB[0], RGB[1], RGB[2], "Kingdom Members");
/*     */   }
/*     */   
/*     */   private PlayerInfo[] filterList(PlayerInfo[] memberArray) {
/* 328 */     ArrayList<PlayerInfo> memberList = new ArrayList<>();
/* 329 */     for (int x = 0; x < memberArray.length; x++) {
/* 330 */       if (PlayerInfoFactory.wildCardMatch(memberArray[x].getName(), this.filter))
/* 331 */         memberList.add(memberArray[x]); 
/*     */     } 
/* 333 */     memberList.sort((p1, p2) -> p1.getName().compareTo(p2.getName()));
/* 334 */     return memberList.<PlayerInfo>toArray(new PlayerInfo[memberList.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   private void sendMemberList() {
/* 339 */     if (this.kingdom == null || this.kingdom.getId() == 4) {
/*     */       
/* 341 */       getResponder().getCommunicator().sendNormalServerMessage("Unable to show member list for that kingdom.");
/*     */       
/*     */       return;
/*     */     } 
/* 345 */     this.members = filterList(this.kingdom.getAllMembers());
/* 346 */     if (this.members.length == 0) {
/*     */       
/* 348 */       getResponder().getCommunicator().sendNormalServerMessage("There are no members to list.");
/*     */       
/*     */       return;
/*     */     } 
/* 352 */     StringBuilder buf = new StringBuilder(getHeader(this.kingdom.getName()));
/* 353 */     int rows = (this.members.length - this.currentIndex < 25) ? (this.members.length - this.currentIndex) : 25;
/* 354 */     buf.append("harray{label{text=\"Filter by: \"}; input{maxchars=\"20\";id=\"filterText\";text=\"" + this.filter + "\"; onenter='" + "filterMe" + "'};button{text='Filter'; id='" + "filterMe" + "'};label{text=' (Use * as a wildcard)'};}");
/*     */     
/* 356 */     buf.append("text{text=''};");
/* 357 */     buf.append("table{rows='" + rows + "'; cols='5'; ");
/* 358 */     buf.append("label{text=''}; label{type='bold'; text='Name'}; label{type='bold'; text='Last On'}; label{type='bold'; text='Member Since'}; label{type='bold'; text='Village'}; ");
/*     */     
/* 360 */     for (int x = 0; x < rows; x++) {
/*     */       
/* 362 */       if (this.currentIndex + x >= this.members.length)
/*     */         break; 
/* 364 */       appendMember(x, buf);
/*     */     } 
/* 366 */     buf.append("}");
/* 367 */     buf.append("text{text=''};harray{label{type='bold'; text='Total Members: '};label{text='" + (this.kingdom.getAllMembers()).length + "'};label{text=''};label{type='bold'; text='Premium: '};label{text='" + this.kingdom
/* 368 */         .getPremiumMemberCount() + "'};}");
/* 369 */     createActionButtons(buf);
/*     */     
/* 371 */     buf.append(createPageAndCloseButtons(rows - 1));
/* 372 */     getResponder().getCommunicator().sendBml(600, 250 + rows * 13, true, true, buf.toString(), RGB[0], RGB[1], RGB[2], "Kingdom Members");
/*     */   }
/*     */   
/*     */   private final void appendMember(int x, StringBuilder buf) {
/* 376 */     boolean isOn = PlayerInfoFactory.isPlayerOnline((this.members[this.currentIndex + x]).wurmId);
/* 377 */     String lastOnString = Server.getTimeFor(System.currentTimeMillis() - this.members[this.currentIndex + x].getLastLogout());
/* 378 */     String memberSinceString = WurmCalendar.formatGmt((this.members[this.currentIndex + x]).lastChangedKindom);
/* 379 */     Village village = Villages.getVillageForCreature((this.members[this.currentIndex + x]).wurmId);
/* 380 */     String color = "255,255,255";
/* 381 */     String villageName = (village == null) ? "None" : village.getName();
/* 382 */     String hover = "";
/* 383 */     if (village != null && village.isMayor((this.members[this.currentIndex + x]).wurmId))
/* 384 */       villageName = villageName + " (Mayor)"; 
/* 385 */     if (King.isKing((this.members[this.currentIndex + x]).wurmId, this.kingdom.getId())) {
/*     */       
/* 387 */       King k = King.getKing(this.kingdom.getId());
/* 388 */       hover = "hover=\"" + k.getFullTitle() + "\";";
/* 389 */       color = "66,200,200";
/*     */     } 
/*     */     
/* 392 */     buf.append("radio{id='" + (this.members[this.currentIndex + x]).wurmId + "'; group='" + "select1" + "'};");
/* 393 */     buf.append("label{color='" + color + "'; " + hover + " text='" + this.members[this.currentIndex + x].getName() + "'};");
/* 394 */     if (isOn) {
/* 395 */       buf.append("label{type='bold'; color='66,225,66'; text='Online'};");
/*     */     } else {
/* 397 */       buf.append("label{text='" + lastOnString + "'};");
/*     */     } 
/* 399 */     buf.append("label{text='" + memberSinceString + "'};");
/* 400 */     buf.append("label{text='" + villageName + "'};");
/*     */   }
/*     */ 
/*     */   
/*     */   private void createActionButtons(StringBuilder buf) {
/* 405 */     if (getResponder().getPower() >= 2) {
/* 406 */       buf.append("text{text=''}; left{ harray{ text{text=''} button{text='GM Tool'; id='gmtool'};");
/* 407 */     } else if (this.kingdom.isCustomKingdom() && King.isKing(getResponder().getWurmId(), this.kingdom.getId())) {
/*     */       
/* 409 */       buf.append("text{text=''}; left{ harray{ text{text=''} button{text='Appoint'; id='appoint'}; ");
/* 410 */       if ((this.kingdom.getAllMembers()).length > 10 || Servers.localServer.testServer)
/* 411 */         buf.append("text{text=''} button{text='Expel'; id='expel'};"); 
/*     */     } 
/* 413 */     buf.append("}}");
/*     */   }
/*     */ 
/*     */   
/*     */   private final String createPageAndCloseButtons(int lRow) {
/* 418 */     if (this.members == null) {
/* 419 */       return "";
/*     */     }
/* 421 */     StringBuilder buf = new StringBuilder("}}null;right{ harray{");
/* 422 */     if (this.currentIndex + lRow > 26)
/* 423 */       buf.append("button{text='Previous'; id='prev'}; label{text=''};"); 
/* 424 */     if (this.currentIndex + lRow < this.members.length - 1)
/* 425 */       buf.append("button{text='Next'; id='next'}; label{text=''};"); 
/* 426 */     buf.append("button{text='Close'; id='close'}; label{text=''}; label{text=''};}}}");
/* 427 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private final String getHeader(String header) {
/* 432 */     return "border{center{header{text=\"" + header + "\"}};null;scroll{vertical=\"true\";horizontal=\"false\";varray{rescale=\"true\";passthrough{id=\"id\";text=\"" + this.id + "\"}";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void sendExpelConfirmation() {
/* 440 */     if (this.playerInfo == null)
/*     */       return; 
/* 442 */     StringBuilder buf = new StringBuilder(getHeader("Expel Member"));
/* 443 */     buf.append("text{text=''}center{header{text='" + this.playerInfo.getName() + "'};}text{text=''};");
/* 444 */     buf.append("center{label{text=\"Type the person's name exactly as shown to confirm.\"};}text{text=''};");
/* 445 */     buf.append("center{harray{label{text=''}input{id='toexpel'; maxchars='25'; text=''}label{text=''}}}text{text=''};");
/* 446 */     buf.append("harray{button{id='confirmExpel'; text='Confirm'};label{text=''};button{id='cancel'; text='Cancel'};}}}null;null;}");
/* 447 */     getResponder().getCommunicator().sendBml(320, 225, true, true, buf.toString(), RGB[0], RGB[1], RGB[2], "Kingdom Members");
/*     */   }
/*     */ 
/*     */   
/*     */   private void sendAppointWindow() {
/* 452 */     if (this.player == null)
/*     */       return; 
/* 454 */     if (!King.isKing(getResponder().getWurmId(), this.kingdom.getId())) {
/*     */       
/* 456 */       getResponder().getCommunicator().sendNormalServerMessage("Only the ruler may appoint subjects.");
/*     */       
/*     */       return;
/*     */     } 
/* 460 */     StringBuilder buf = new StringBuilder(getHeader("Appoint " + this.player.getName()));
/* 461 */     King king = King.getKing(this.kingdom.getId());
/* 462 */     Appointments a = Appointments.getAppointments(king.era);
/*     */     
/* 464 */     long timeLeft = a.getResetTimeRemaining();
/* 465 */     if (timeLeft <= 0L) {
/* 466 */       buf.append("center{varray{label{color=\"66,200,200\"; text=\"Titles and orders will refresh shortly.\"}}};");
/*     */     } else {
/* 468 */       buf.append("center{varray{label{color=\"66,200,200\"; text=\"Titles and orders will refresh in " + Server.getTimeFor(timeLeft) + ".\"}}}");
/* 469 */     }  buf.append("text{text=''};");
/*     */ 
/*     */     
/* 472 */     buf.append("table{rows='5';  cols='6'; label{text=''};label{size=\"200,16\"; text=''}; label{text=''};label{type='bold'; size=\"200,16\"; text='Titles'}; label{text=''};label{size=\"200,16\"; text=''};label{text=''};label{text=''};label{text=''};label{text=''};label{text=''};label{text=''};");
/*     */ 
/*     */     
/* 475 */     addTitleStrings(a, buf);
/* 476 */     buf.append("};text{text=''};");
/*     */ 
/*     */     
/* 479 */     buf.append("table{rows='4';  cols='6'; label{text=''};label{size=\"200,16\"; text=''}; label{text=''};label{type='bold'; size=\"200,16\"; text='Orders & Decorations'}; label{text=''};label{size=\"200,16\"; text=''};label{text=''};label{text=''};label{text=''};label{text=''};label{text=''};label{text=''};");
/*     */ 
/*     */     
/* 482 */     addOrderStrings(a, buf);
/* 483 */     buf.append("};text{text=''};");
/*     */ 
/*     */     
/* 486 */     buf.append("table{rows='6';  cols='6'; label{text=''};label{size=\"200,16\"; text=''}; label{text=''};label{type='bold'; size='200,16'; text='Offices'}; label{text=''};label{size=\"200,16\"; text=''};label{text=''};label{text=''};label{text=''};label{text=''};label{text=''};label{text=''};");
/*     */ 
/*     */     
/* 489 */     addOfficeStrings(a, buf);
/*     */     
/* 491 */     buf.append("};text{text=''};label{color='66,225,66'; text='Green indicates member already has this appointment'};");
/* 492 */     buf.append("label{color='255,156,66'; text='Orange indicates an office may be set but is occupied.'};");
/* 493 */     buf.append("label{color='255,66,66'; text='Red indicates appointment is on cool down.'};text{text=''};");
/*     */     
/* 495 */     buf.append("harray{button{id='confirmAppoint'; text='Appoint'};label{text=''};button{id='cancel'; text='Cancel'};}}}null;null;}");
/* 496 */     getResponder().getCommunicator().sendBml(620, 460, true, true, buf.toString(), RGB[0], RGB[1], RGB[2], "Kingdom Members");
/*     */   }
/*     */   
/*     */   private void addTitleStrings(Appointments a, StringBuilder buf) {
/*     */     int x;
/* 501 */     for (x = 0; x < a.availableTitles.length; x++) {
/*     */       
/* 503 */       if (Appointments.getMaxAppointment(this.kingdom.getId(), x) != 0) {
/*     */         
/* 505 */         Appointment t = a.getAppointment(x);
/* 506 */         if (this.player.hasAppointment(t.getId())) {
/*     */           
/* 508 */           buf.append("label{text=''}; label{color='66,225,66'; text=\"" + t.getNameForGender(this.player.getSex()) + "\"};");
/*     */         }
/* 510 */         else if (a.getAvailTitlesForId(x) > 0) {
/*     */           
/* 512 */           buf.append("checkbox{id='" + x + "'}; label{text=\"" + t.getNameForGender(this.player.getSex()) + "(" + a.getAvailTitlesForId(x) + ")\"};");
/*     */         }
/*     */         else {
/*     */           
/* 516 */           buf.append("label{text=''}; label{color='255,66,66'; text=\"" + t.getNameForGender(this.player.getSex()) + "\"};");
/*     */         } 
/*     */       } 
/* 519 */     }  if (a.availableTitles.length % 3 != 0)
/*     */     {
/* 521 */       for (x = 0; x < 3 - a.availableTitles.length % 3; x++)
/*     */       {
/* 523 */         buf.append("label{text=''};");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void addOrderStrings(Appointments a, StringBuilder buf) {
/*     */     int x;
/* 530 */     for (x = 0; x < a.availableOrders.length; x++) {
/*     */       
/* 532 */       if (Appointments.getMaxAppointment(this.kingdom.getId(), x) != 0) {
/*     */         
/* 534 */         int oId = x + 30;
/* 535 */         Appointment o = a.getAppointment(oId);
/*     */         
/* 537 */         if (this.player.hasAppointment(o.getId())) {
/*     */           
/* 539 */           buf.append("label{text=''}; label{color='66,225,66'; text=\"" + o.getNameForGender(this.player.getSex()) + "\"};");
/*     */         }
/* 541 */         else if (a.getAvailOrdersForId(oId) > 0) {
/*     */           
/* 543 */           buf.append("checkbox{id='" + oId + "'}; label{text=\"" + o.getNameForGender(this.player.getSex()) + "(" + a.getAvailOrdersForId(oId) + ")\"};");
/*     */         }
/*     */         else {
/*     */           
/* 547 */           buf.append("label{text=''}; label{color='255,66,66'; text=\"" + o.getNameForGender(this.player.getSex()) + "\"};");
/*     */         } 
/*     */       } 
/* 550 */     }  if (a.availableOrders.length % 3 != 0)
/*     */     {
/* 552 */       for (x = 0; x < 3 - a.availableOrders.length % 3; x++)
/*     */       {
/* 554 */         buf.append("label{text=''};");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void addOfficeStrings(Appointments a, StringBuilder buf) {
/*     */     int x;
/* 561 */     for (x = 0; x < a.officials.length; x++) {
/*     */       
/* 563 */       int oId = x + 1500;
/* 564 */       Appointment o = a.getAppointment(oId);
/*     */       
/* 566 */       if (a.officials[x] == this.player.getWurmId()) {
/*     */         
/* 568 */         if (a.isOfficeSet(oId)) {
/* 569 */           buf.append("label{text=''}; label{color='66,225,66'; text=\"" + o.getNameForGender(this.player.getSex()) + "\"};");
/*     */         } else {
/*     */           
/* 572 */           String conf = "Are you sure you want to remove " + this.player.getName() + " from this office?";
/* 573 */           buf.append("checkbox{id='" + oId + "'; unconfirm=''; unquestion=\"" + conf + "\"; selected='true'}; label{color='" + "66,225,66" + "'; text=\"" + o
/* 574 */               .getNameForGender(this.player.getSex()) + "\"};");
/*     */         }
/*     */       
/*     */       }
/* 578 */       else if (a.isOfficeSet(oId)) {
/*     */         
/* 580 */         String oName = PlayerInfoFactory.getPlayerName(a.officials[x]);
/* 581 */         buf.append("label{text=''}; label{color='255,66,66'; hover=\"Current: " + oName + "\"; text=\"" + o
/* 582 */             .getNameForGender(this.player.getSex()) + "\"};");
/*     */       
/*     */       }
/* 585 */       else if (a.officials[x] > 0L) {
/*     */         
/* 587 */         String oName = PlayerInfoFactory.getPlayerName(a.officials[x]);
/* 588 */         String conf = "Are you sure you want to remove " + oName + " from this office?";
/* 589 */         buf.append("checkbox{id='" + oId + "'; hover=\"Current: " + oName + "\"; confirm=''; question=\"" + conf + "\"}; label{color='" + "255,156,66" + "'; hover=\"Current: " + oName + "\"; text=\"" + o
/*     */             
/* 591 */             .getNameForGender(this.player.getSex()) + "\"};");
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 596 */         buf.append("checkbox{id='" + oId + "'}; label{text=\"" + o.getNameForGender(this.player.getSex()) + "\"};");
/*     */       } 
/*     */     } 
/* 599 */     if (a.officials.length % 3 != 0)
/*     */     {
/* 601 */       for (x = 0; x < 3 - a.officials.length % 3; x++)
/*     */       {
/* 603 */         buf.append("label{text=''};");
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\KingdomMembersQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */