/*     */ package com.wurmonline.server.behaviours;
/*     */ 
/*     */ import com.wurmonline.server.Features;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.Creatures;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.questions.KingdomHistory;
/*     */ import com.wurmonline.server.questions.KingdomStatusQuestion;
/*     */ import com.wurmonline.server.questions.ManageFriends;
/*     */ import com.wurmonline.server.questions.ManageObjectList;
/*     */ import com.wurmonline.server.questions.PlayerProfileQuestion;
/*     */ import com.wurmonline.server.questions.WagonerDeliveriesQuestion;
/*     */ import com.wurmonline.server.villages.Village;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
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
/*     */ public final class ManageMenu
/*     */   implements MiscConstants
/*     */ {
/*     */   static List<ActionEntry> getBehavioursFor(Creature performer) {
/*  64 */     List<ActionEntry> alist = new LinkedList<>();
/*  65 */     List<ActionEntry> slist = new LinkedList<>();
/*     */ 
/*     */     
/*  68 */     alist.add(new ActionEntry((short)663, "Animals", "managing"));
/*  69 */     alist.add(new ActionEntry((short)664, "Buildings", "managing"));
/*  70 */     alist.add(new ActionEntry((short)665, "Carts and Wagons", "managing"));
/*  71 */     if (Features.Feature.WAGONER.isEnabled())
/*  72 */       alist.add(Actions.actionEntrys[916]); 
/*  73 */     alist.add(Actions.actionEntrys[661]);
/*  74 */     alist.add(new ActionEntry((short)667, "Gates", "managing"));
/*  75 */     alist.add(new ActionEntry((short)364, "MineDoors", "managing"));
/*  76 */     alist.add(Actions.actionEntrys[566]);
/*  77 */     alist.add(Actions.actionEntrys[690]);
/*  78 */     if (performer.getCitizenVillage() != null) {
/*     */       
/*  80 */       Village village = performer.getCitizenVillage();
/*  81 */       slist.addAll(VillageTokenBehaviour.getSettlementMenu(performer, false, village, village));
/*  82 */       alist.addAll(slist);
/*     */     } 
/*  84 */     alist.add(new ActionEntry((short)668, "Ships", "managing"));
/*     */     
/*  86 */     if (Features.Feature.WAGONER.isEnabled() && (Creatures.getManagedWagonersFor((Player)performer, -1)).length > 0) {
/*  87 */       alist.add(Actions.actionEntrys[863]);
/*     */     }
/*  89 */     int sz = (slist.size() > 0) ? (alist.size() - slist.size() + 1) : alist.size();
/*  90 */     List<ActionEntry> toReturn = new LinkedList<>();
/*  91 */     toReturn.add(new ActionEntry((short)-sz, "Manage", "Manage"));
/*  92 */     toReturn.addAll(alist);
/*     */     
/*  94 */     return toReturn;
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
/*     */   static boolean isManageAction(Creature performer, short action) {
/* 106 */     if (action == 663)
/* 107 */       return true; 
/* 108 */     if (action == 664)
/* 109 */       return true; 
/* 110 */     if (action == 665)
/* 111 */       return true; 
/* 112 */     if (action == 687)
/* 113 */       return true; 
/* 114 */     if (action == 667)
/* 115 */       return true; 
/* 116 */     if (action == 364)
/* 117 */       return true; 
/* 118 */     if (action == 668)
/* 119 */       return true; 
/* 120 */     if (action == 669)
/* 121 */       return true; 
/* 122 */     if (action == 670)
/* 123 */       return true; 
/* 124 */     if (action == 690)
/* 125 */       return true; 
/* 126 */     if (action == 661)
/* 127 */       return true; 
/* 128 */     if (action == 566)
/* 129 */       return true; 
/* 130 */     if (action == 77)
/* 131 */       return true; 
/* 132 */     if (action == 71)
/* 133 */       return true; 
/* 134 */     if (action == 72)
/* 135 */       return true; 
/* 136 */     if (action == 355)
/* 137 */       return true; 
/* 138 */     if (action == 356)
/* 139 */       return true; 
/* 140 */     if (action == 80)
/* 141 */       return true; 
/* 142 */     if (action == 67)
/* 143 */       return true; 
/* 144 */     if (action == 68)
/* 145 */       return true; 
/* 146 */     if (action == 540)
/* 147 */       return true; 
/* 148 */     if (action == 66)
/* 149 */       return true; 
/* 150 */     if (action == 69)
/* 151 */       return true; 
/* 152 */     if (action == 70)
/* 153 */       return true; 
/* 154 */     if (action == 76)
/* 155 */       return true; 
/* 156 */     if (action == 481)
/* 157 */       return true; 
/* 158 */     if (action == 863)
/* 159 */       return true; 
/* 160 */     if (action == 916)
/* 161 */       return true; 
/* 162 */     if (action == 738)
/* 163 */       return true; 
/* 164 */     return false;
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
/*     */   static boolean action(Action act, Creature performer, short action, float counter) {
/* 178 */     if (action == 663) {
/*     */       
/* 180 */       ManageObjectList mol = new ManageObjectList(performer, ManageObjectList.Type.ANIMAL1);
/* 181 */       mol.sendQuestion();
/* 182 */       return true;
/*     */     } 
/* 184 */     if (action == 664) {
/*     */       
/* 186 */       ManageObjectList mol = new ManageObjectList(performer, ManageObjectList.Type.BUILDING);
/* 187 */       mol.sendQuestion();
/* 188 */       return true;
/*     */     } 
/* 190 */     if (action == 665) {
/*     */       
/* 192 */       ManageObjectList mol = new ManageObjectList(performer, ManageObjectList.Type.LARGE_CART);
/* 193 */       mol.sendQuestion();
/* 194 */       return true;
/*     */     } 
/* 196 */     if (action == 667) {
/*     */       
/* 198 */       ManageObjectList mol = new ManageObjectList(performer, ManageObjectList.Type.GATE);
/* 199 */       mol.sendQuestion();
/* 200 */       return true;
/*     */     } 
/* 202 */     if (action == 364) {
/*     */       
/* 204 */       ManageObjectList mol = new ManageObjectList(performer, ManageObjectList.Type.MINEDOOR);
/* 205 */       mol.sendQuestion();
/* 206 */       return true;
/*     */     } 
/* 208 */     if (action == 668) {
/*     */       
/* 210 */       ManageObjectList mol = new ManageObjectList(performer, ManageObjectList.Type.SHIP);
/* 211 */       mol.sendQuestion();
/* 212 */       return true;
/*     */     } 
/* 214 */     if (action == 863) {
/*     */       
/* 216 */       ManageObjectList mol = new ManageObjectList(performer, ManageObjectList.Type.WAGONER);
/* 217 */       mol.sendQuestion();
/* 218 */       return true;
/*     */     } 
/* 220 */     if (action == 916) {
/*     */       
/* 222 */       WagonerDeliveriesQuestion mdq = new WagonerDeliveriesQuestion(performer, -10L, true);
/* 223 */       mdq.sendQuestion();
/* 224 */       return true;
/*     */     } 
/* 226 */     if (action == 670 && performer.getCitizenVillage() != null) {
/*     */       
/* 228 */       Methods.sendManageUpkeep(performer, null);
/* 229 */       return true;
/*     */     } 
/* 231 */     if (action == 661) {
/*     */       
/* 233 */       ManageFriends mf = new ManageFriends(performer);
/* 234 */       mf.sendQuestion();
/* 235 */       return true;
/*     */     } 
/* 237 */     if (action == 566) {
/*     */       
/* 239 */       PlayerProfileQuestion kq = new PlayerProfileQuestion(performer);
/* 240 */       kq.sendQuestion();
/* 241 */       return true;
/*     */     } 
/* 243 */     if (action == 690) {
/*     */       
/* 245 */       ManageObjectList mol = new ManageObjectList(performer, ManageObjectList.Type.SEARCH);
/* 246 */       mol.sendQuestion();
/* 247 */       return true;
/*     */     } 
/* 249 */     Village village = performer.getCitizenVillage();
/* 250 */     if (action == 77) {
/*     */       
/* 252 */       Methods.sendVillageInfo(performer, null);
/* 253 */       return true;
/*     */     } 
/* 255 */     if (village != null && action == 71) {
/*     */       
/* 257 */       Methods.sendVillageHistory(performer, null);
/* 258 */       return true;
/*     */     } 
/* 260 */     if (village != null && action == 72) {
/*     */       
/* 262 */       Methods.sendAreaHistory(performer, null);
/* 263 */       return true;
/*     */     } 
/* 265 */     if (village != null && action == 355) {
/*     */ 
/*     */       
/* 268 */       KingdomStatusQuestion kq = new KingdomStatusQuestion(performer, "Kingdom status", "Kingdoms", performer.getWurmId());
/* 269 */       kq.sendQuestion();
/* 270 */       return true;
/*     */     } 
/* 272 */     if (village != null && action == 356) {
/*     */ 
/*     */       
/* 275 */       KingdomHistory kq = new KingdomHistory(performer, "Kingdom history", "History of the kingdoms", performer.getWurmId());
/* 276 */       kq.sendQuestion();
/* 277 */       return true;
/*     */     } 
/* 279 */     if (village != null && action == 80) {
/*     */       
/* 281 */       if (village.mayDoDiplomacy(performer)) {
/* 282 */         Methods.sendManageAllianceQuestion(performer, null);
/*     */       } else {
/* 284 */         performer.getCommunicator().sendNormalServerMessage("You may not perform diplomacy for " + village
/* 285 */             .getName() + ".");
/* 286 */       }  return true;
/*     */     } 
/* 288 */     if (village != null && action == 67) {
/*     */       
/* 290 */       if (village.isActionAllowed((short)67, performer)) {
/* 291 */         Methods.sendManageVillageGuardsQuestion(performer, null);
/*     */       } else {
/* 293 */         performer.getCommunicator().sendNormalServerMessage("You may not manage guards for " + village
/* 294 */             .getName() + ".");
/* 295 */       }  return true;
/*     */     } 
/* 297 */     if (village != null && action == 68) {
/*     */       
/* 299 */       if (village.isActionAllowed((short)68, performer)) {
/* 300 */         Methods.sendManageVillageSettingsQuestion(performer, null);
/*     */       } else {
/* 302 */         performer.getCommunicator().sendNormalServerMessage("You may not manage settings for " + village
/* 303 */             .getName() + ".");
/* 304 */       }  return true;
/*     */     } 
/* 306 */     if (village != null && action == 540) {
/*     */       
/* 308 */       if (village.isActionAllowed((short)540, performer)) {
/* 309 */         Methods.sendManageVillageRolesQuestion(performer, null);
/*     */       } else {
/* 311 */         performer.getCommunicator().sendNormalServerMessage("You may not manage roles for " + village
/* 312 */             .getName() + ".");
/* 313 */       }  return true;
/*     */     } 
/* 315 */     if (village != null && action == 66) {
/*     */       
/* 317 */       if (village.isActionAllowed((short)66, performer)) {
/* 318 */         Methods.sendManageVillageCitizensQuestion(performer, null);
/*     */       } else {
/* 320 */         performer.getCommunicator().sendNormalServerMessage("You may not manage citizens for " + village
/* 321 */             .getName() + ".");
/* 322 */       }  return true;
/*     */     } 
/* 324 */     if (village != null && action == 69) {
/*     */       
/* 326 */       short laction = 69;
/* 327 */       if (village.isActionAllowed((short)69, performer)) {
/* 328 */         Methods.sendReputationManageQuestion(performer, null);
/*     */       } else {
/* 330 */         performer.getCommunicator().sendNormalServerMessage("You may not manage reputations for " + village
/* 331 */             .getName() + ".");
/* 332 */       }  return true;
/*     */     } 
/* 334 */     if (village != null && action == 70) {
/*     */       
/* 336 */       short laction = 70;
/* 337 */       if (village.isActionAllowed((short)70, performer)) {
/* 338 */         Methods.sendManageVillageGatesQuestion(performer, null);
/*     */       } else {
/* 340 */         performer.getCommunicator().sendNormalServerMessage("You may not manage gates for " + village
/* 341 */             .getName() + ".");
/* 342 */       }  return true;
/*     */     } 
/* 344 */     if (village != null && action == 76) {
/*     */       
/* 346 */       if (village.isActionAllowed((short)76, performer)) {
/* 347 */         Methods.sendExpandVillageQuestion(performer, null);
/*     */       } else {
/* 349 */         performer.getCommunicator().sendNormalServerMessage("You may not resize the settlement " + village
/* 350 */             .getName() + ".");
/* 351 */       }  return true;
/*     */     } 
/* 353 */     if (village != null && action == 481) {
/*     */       
/* 355 */       short laction = 481;
/* 356 */       if (village.isActionAllowed((short)481, performer)) {
/* 357 */         Methods.sendConfigureTwitter(performer, -10L, true, village.getName());
/*     */       } else {
/* 359 */         performer.getCommunicator().sendNormalServerMessage("You may not configure twitter for " + village
/* 360 */             .getName() + ".");
/* 361 */       }  return true;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 369 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\ManageMenu.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */