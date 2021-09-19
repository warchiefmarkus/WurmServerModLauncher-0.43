/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.GeneralUtilities;
/*     */ import com.wurmonline.server.LoginHandler;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.ItemSettings;
/*     */ import com.wurmonline.server.players.Friend;
/*     */ import com.wurmonline.server.players.Permissions;
/*     */ import com.wurmonline.server.players.PermissionsByPlayer;
/*     */ import com.wurmonline.server.players.PermissionsHistories;
/*     */ import com.wurmonline.server.players.PermissionsPlayerList;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.players.PlayerInfoFactory;
/*     */ import com.wurmonline.server.structures.StructureSettings;
/*     */ import com.wurmonline.server.utils.BMLBuilder;
/*     */ import com.wurmonline.server.villages.Citizen;
/*     */ import com.wurmonline.server.villages.Village;
/*     */ import edu.umd.cs.findbugs.annotations.Nullable;
/*     */ import java.awt.Color;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.BitSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
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
/*     */ public class ManagePermissions
/*     */   extends Question
/*     */ {
/*  66 */   private static final Logger logger = Logger.getLogger(ManagePermissions.class.getName());
/*     */   
/*     */   private static final String legalNameChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
/*     */   
/*     */   private static final String legalDescriptionChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ'- 1234567890.,+/!()";
/*     */   
/*     */   private Player player;
/*     */   
/*     */   private ManageObjectList.Type objectType;
/*     */   
/*     */   private PermissionsPlayerList.ISettings object;
/*     */   private boolean hasBackButton;
/*     */   private boolean parentHasBack;
/*     */   private ManageObjectList.Type parentType;
/*     */   private String error;
/*     */   private boolean hadManage = true;
/*     */   
/*     */   public ManagePermissions(Creature aResponder, ManageObjectList.Type aObjectType, PermissionsPlayerList.ISettings anObject, boolean canGoBack, long parent, boolean parentCanGoBack, @Nullable ManageObjectList.Type aParentType, String errorText) {
/*  84 */     super(aResponder, "Managing " + anObject.getObjectName(), "Manage Permissions", 119, parent);
/*     */     
/*  86 */     this.player = (Player)aResponder;
/*  87 */     this.objectType = aObjectType;
/*  88 */     this.object = anObject;
/*  89 */     this.hasBackButton = canGoBack;
/*  90 */     this.parentHasBack = parentCanGoBack;
/*  91 */     this.parentType = aParentType;
/*  92 */     this.error = errorText;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties aAnswers) {
/* 103 */     setAnswer(aAnswers);
/*     */     
/* 105 */     String sback = aAnswers.getProperty("back");
/* 106 */     if (sback != null && sback.equals("true")) {
/*     */       
/* 108 */       ManageObjectList mol = new ManageObjectList((Creature)this.player, this.parentType, this.target, this.parentHasBack, 1, "", true);
/* 109 */       mol.sendQuestion();
/*     */       return;
/*     */     } 
/* 112 */     String sclose = aAnswers.getProperty("close");
/* 113 */     if (sclose != null && sclose.equals("true")) {
/*     */       return;
/*     */     }
/*     */     
/* 117 */     if (this.object.isItem() && this.object.getTemplateId() == 272) {
/*     */       
/* 119 */       ManagePermissions managePermissions = new ManagePermissions((Creature)this.player, this.objectType, this.object, this.hasBackButton, this.target, this.parentHasBack, this.parentType, "Cannot modify corpse permissions.");
/*     */ 
/*     */       
/* 122 */       managePermissions.sendQuestion();
/*     */       return;
/*     */     } 
/* 125 */     boolean canChangeName = this.object.canChangeName((Creature)this.player);
/* 126 */     String newObjectName = aAnswers.getProperty("object");
/* 127 */     String newOwnerName = aAnswers.getProperty("owner");
/* 128 */     String newOwner = (newOwnerName != null) ? LoginHandler.raiseFirstLetter(newOwnerName) : "";
/* 129 */     boolean manage = Boolean.parseBoolean(aAnswers.getProperty("manage"));
/* 130 */     boolean manageChanged = false;
/*     */ 
/*     */     
/* 133 */     if (canChangeName && !newObjectName.equalsIgnoreCase(this.object.getObjectName()))
/*     */     {
/* 135 */       if (containsIllegalCharacters("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ'- 1234567890.,+/!()", newObjectName)) {
/*     */         
/* 137 */         ManagePermissions managePermissions = new ManagePermissions((Creature)this.player, this.objectType, this.object, this.hasBackButton, this.target, this.parentHasBack, this.parentType, "Illegal new object name '" + newObjectName + "'.");
/*     */ 
/*     */         
/* 140 */         managePermissions.sendQuestion();
/*     */         
/*     */         return;
/*     */       } 
/*     */     }
/* 145 */     long ownerId = -10L;
/* 146 */     if (newOwner.length() != 0 && !newOwner.equalsIgnoreCase(this.object.getOwnerName())) {
/*     */       
/* 148 */       if (containsIllegalCharacters("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", newOwner)) {
/*     */         
/* 150 */         String msg = "Illegal new owners name '" + newOwner + "'.";
/* 151 */         if (this.object.mayShowPermissions((Creature)this.player)) {
/*     */           
/* 153 */           ManagePermissions managePermissions = new ManagePermissions((Creature)this.player, this.objectType, this.object, this.hasBackButton, this.target, this.parentHasBack, this.parentType, msg);
/*     */           
/* 155 */           managePermissions.sendQuestion();
/*     */         } else {
/*     */           
/* 158 */           this.player.getCommunicator().sendNormalServerMessage(msg);
/*     */         }  return;
/*     */       } 
/* 161 */       if (!this.object.canChangeOwner(getResponder())) {
/*     */         
/* 163 */         String msg = "Not allowed to change owner.";
/* 164 */         if (this.object.mayShowPermissions((Creature)this.player)) {
/*     */           
/* 166 */           ManagePermissions managePermissions = new ManagePermissions((Creature)this.player, this.objectType, this.object, this.hasBackButton, this.target, this.parentHasBack, this.parentType, "Not allowed to change owner.");
/*     */           
/* 168 */           managePermissions.sendQuestion();
/*     */         } else {
/*     */           
/* 171 */           this.player.getCommunicator().sendNormalServerMessage("Not allowed to change owner.");
/*     */         }  return;
/*     */       } 
/* 174 */       ownerId = PlayerInfoFactory.getWurmId(newOwnerName);
/* 175 */       if (ownerId == -10L) {
/*     */         
/* 177 */         String msg = "Cannot find new owner '" + newOwnerName + "'.";
/* 178 */         if (this.object.mayShowPermissions((Creature)this.player)) {
/*     */           
/* 180 */           ManagePermissions managePermissions = new ManagePermissions((Creature)this.player, this.objectType, this.object, this.hasBackButton, this.target, this.parentHasBack, this.parentType, msg);
/*     */           
/* 182 */           managePermissions.sendQuestion();
/*     */         } else {
/*     */           
/* 185 */           this.player.getCommunicator().sendNormalServerMessage(msg);
/*     */         } 
/*     */         return;
/*     */       } 
/*     */     } 
/* 190 */     LinkedList<String> changes = new LinkedList<>();
/* 191 */     LinkedList<String> removed = new LinkedList<>();
/* 192 */     LinkedList<String> added = new LinkedList<>();
/* 193 */     LinkedList<String> updated = new LinkedList<>();
/* 194 */     if (!this.object.mayShowPermissions((Creature)this.player)) {
/*     */ 
/*     */ 
/*     */       
/* 198 */       if (newObjectName != null && !newObjectName.equalsIgnoreCase(this.object.getObjectName())) {
/*     */         
/* 200 */         if (!this.object.setObjectName(newObjectName, (Creature)this.player)) {
/*     */           
/* 202 */           this.player.getCommunicator().sendNormalServerMessage("Problem changing name.");
/*     */           return;
/*     */         } 
/* 205 */         changes.add("Name");
/*     */       } 
/* 207 */       if (ownerId != -10L) {
/*     */         
/* 209 */         if (!this.object.setNewOwner(ownerId)) {
/*     */           
/* 211 */           if (!changes.isEmpty())
/* 212 */             PermissionsHistories.addHistoryEntry(this.object.getWurmId(), System.currentTimeMillis(), this.player
/* 213 */                 .getWurmId(), this.player.getName(), "Changed " + (String)changes.getFirst()); 
/* 214 */           this.player.getCommunicator().sendNormalServerMessage("Problem changing name.");
/*     */           return;
/*     */         } 
/* 217 */         changes.add("Owner to '" + newOwner + "'");
/*     */       } 
/*     */       
/* 220 */       if (this.objectType == ManageObjectList.Type.DOOR && ownerId == -10L && this.object.getWarning().length() == 0)
/*     */       {
/* 222 */         if (this.object.isManaged() != manage) {
/*     */           
/* 224 */           this.object.setIsManaged(manage, this.player);
/* 225 */           manageChanged = true;
/*     */         } 
/*     */       }
/*     */       
/*     */       try {
/* 230 */         this.object.save();
/*     */       }
/* 232 */       catch (IOException e) {
/*     */         
/* 234 */         logger.log(Level.WARNING, e.getMessage(), e);
/*     */       } 
/*     */       
/* 237 */       if (!changes.isEmpty() || manageChanged) {
/*     */         
/* 239 */         StringBuilder stringBuilder = new StringBuilder();
/* 240 */         if (!changes.isEmpty())
/*     */         {
/* 242 */           stringBuilder.append("Changed " + String.join(", ", (Iterable)changes));
/*     */         }
/* 244 */         if (manageChanged) {
/*     */           
/* 246 */           if (stringBuilder.length() > 0)
/* 247 */             stringBuilder.append(", "); 
/* 248 */           if (manage) {
/* 249 */             stringBuilder.append("Ticked Controlled by Flag");
/*     */           } else {
/* 251 */             stringBuilder.append("UnTicked Controlled by Flag");
/*     */           } 
/* 253 */         }  PermissionsHistories.addHistoryEntry(this.object.getWurmId(), System.currentTimeMillis(), this.player
/* 254 */             .getWurmId(), this.player.getName(), stringBuilder.toString());
/* 255 */         this.player.getCommunicator().sendNormalServerMessage("You " + stringBuilder.toString());
/*     */         
/* 257 */         ManagePermissions managePermissions = new ManagePermissions((Creature)this.player, this.objectType, this.object, this.hasBackButton, this.target, this.parentHasBack, this.parentType, "");
/*     */         
/* 259 */         managePermissions.sendQuestion();
/*     */       } 
/*     */       return;
/*     */     } 
/* 263 */     int rows = Integer.parseInt(aAnswers.getProperty("rows"));
/*     */     
/* 265 */     if (rows > this.object.getMaxAllowed()) {
/*     */ 
/*     */ 
/*     */       
/* 269 */       ManagePermissions managePermissions = new ManagePermissions((Creature)this.player, this.objectType, this.object, this.hasBackButton, this.target, this.parentHasBack, this.parentType, "Too many allowed entries, Max is " + this.object.getMaxAllowed() + ".");
/* 270 */       managePermissions.sendQuestion();
/*     */       return;
/*     */     } 
/* 273 */     PermissionsPlayerList ppl = this.object.getPermissionsPlayerList();
/* 274 */     long[] pIds = new long[rows];
/* 275 */     int[] settings = new int[rows];
/* 276 */     int excludeBit = StructureSettings.StructurePermissions.EXCLUDE.getBit();
/* 277 */     if (rows > 0) {
/*     */       
/* 279 */       int cols = Integer.parseInt(aAnswers.getProperty("cols"));
/* 280 */       Permissions.IPermission[] values = this.objectType.getEnumValues();
/* 281 */       int[] bits = new int[cols];
/* 282 */       for (int x = 0; x < cols; x++)
/*     */       {
/* 284 */         bits[x] = values[x].getBit();
/*     */       }
/*     */       
/* 287 */       for (int row = 0; row < rows; row++) {
/*     */         
/* 289 */         pIds[row] = Long.parseLong(aAnswers.getProperty("r" + row));
/* 290 */         BitSet bitset = new BitSet(32);
/* 291 */         boolean otherThanExclude = false;
/* 292 */         for (int col = 0; col < cols; col++) {
/*     */           
/* 294 */           boolean flag = Boolean.parseBoolean(aAnswers.getProperty("r" + row + "c" + col));
/* 295 */           bitset.set(bits[col], flag);
/* 296 */           if (bits[col] != excludeBit && flag)
/* 297 */             otherThanExclude = true; 
/*     */         } 
/* 299 */         boolean wasExcluded = false;
/* 300 */         if (ppl.exists(pIds[row])) {
/* 301 */           wasExcluded = ppl.hasPermission(pIds[row], excludeBit);
/*     */         }
/* 303 */         if (wasExcluded && otherThanExclude) {
/*     */           
/* 305 */           bitset.clear(excludeBit);
/*     */         }
/* 307 */         else if (bitset.get(excludeBit)) {
/*     */ 
/*     */           
/* 310 */           bitset.clear();
/* 311 */           bitset.set(excludeBit);
/*     */         } 
/* 313 */         if (pIds[row] == -30L || pIds[row] == -20L || pIds[row] == -40L || pIds[row] == -50L) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 319 */           if (bitset.get(StructureSettings.StructurePermissions.MANAGE.getBit())) {
/* 320 */             bitset.clear(StructureSettings.StructurePermissions.MANAGE.getBit());
/*     */           }
/*     */         }
/*     */         else {
/*     */           
/* 325 */           if (bitset.get(StructureSettings.StructurePermissions.MANAGE.getBit()))
/*     */           {
/* 327 */             for (int i = 1; i < cols; i++) {
/*     */               
/* 329 */               if (bits[i] != excludeBit)
/* 330 */                 bitset.set(bits[i], true); 
/*     */             } 
/*     */           }
/* 333 */           if (this.object.isItem() && this.object.getTemplateId() == 1271)
/*     */           {
/*     */             
/* 336 */             if (bitset.get(ItemSettings.MessageBoardPermissions.MANAGE_NOTICES.getBit())) {
/*     */               
/* 338 */               for (int i = 2; i < cols; i++)
/*     */               {
/* 340 */                 if (bits[i] != excludeBit) {
/* 341 */                   bitset.set(bits[i], true);
/*     */                 }
/*     */               }
/*     */             
/* 345 */             } else if (bitset.get(ItemSettings.MessageBoardPermissions.MAY_POST_NOTICES.getBit())) {
/*     */               
/* 347 */               bitset.set(ItemSettings.MessageBoardPermissions.ACCESS_HOLD.getBit(), true);
/*     */             
/*     */             }
/* 350 */             else if (bitset.get(ItemSettings.MessageBoardPermissions.MAY_ADD_PMS.getBit())) {
/*     */               
/* 352 */               bitset.set(ItemSettings.MessageBoardPermissions.ACCESS_HOLD.getBit(), true);
/*     */             } 
/*     */           }
/*     */         } 
/* 356 */         settings[row] = GeneralUtilities.getIntSettingsFrom(bitset);
/*     */       } 
/*     */     } 
/*     */     
/* 360 */     if (canChangeName && !newObjectName.equalsIgnoreCase(this.object.getObjectName())) {
/*     */       
/* 362 */       if (!this.object.setObjectName(newObjectName, (Creature)this.player)) {
/*     */         
/* 364 */         ManagePermissions managePermissions = new ManagePermissions((Creature)this.player, this.objectType, this.object, this.hasBackButton, this.target, this.parentHasBack, this.parentType, "Problem changing name.");
/*     */         
/* 366 */         managePermissions.sendQuestion();
/*     */         return;
/*     */       } 
/* 369 */       changes.add("Name");
/*     */     } 
/* 371 */     if (ownerId != -10L) {
/*     */       
/* 373 */       if (!this.object.setNewOwner(ownerId)) {
/*     */         
/* 375 */         if (!changes.isEmpty())
/* 376 */           PermissionsHistories.addHistoryEntry(this.object.getWurmId(), System.currentTimeMillis(), this.player
/* 377 */               .getWurmId(), this.player.getName(), "Changed " + (String)changes.getFirst()); 
/* 378 */         ManagePermissions managePermissions = new ManagePermissions((Creature)this.player, this.objectType, this.object, this.hasBackButton, this.target, this.parentHasBack, this.parentType, "Problem changing owner.");
/*     */         
/* 380 */         managePermissions.sendQuestion();
/*     */         return;
/*     */       } 
/* 383 */       changes.add("Owner to '" + newOwner + "'");
/*     */     } 
/*     */     
/* 386 */     if (ownerId == -10L && this.object.isManaged() != manage) {
/*     */       
/* 388 */       this.object.setIsManaged(manage, this.player);
/* 389 */       manageChanged = true;
/*     */     } 
/*     */     
/*     */     try {
/* 393 */       this.object.save();
/*     */     }
/* 395 */     catch (IOException e) {
/*     */       
/* 397 */       logger.log(Level.WARNING, e.getMessage(), e);
/*     */     } 
/* 399 */     if (ownerId == -10L && !manageChanged) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 408 */       for (PermissionsByPlayer pbp : ppl.getPermissionsByPlayer()) {
/*     */         
/* 410 */         boolean found = false;
/* 411 */         for (long pId : pIds) {
/*     */           
/* 413 */           if (pbp.getPlayerId() == pId) {
/*     */             
/* 415 */             found = true;
/*     */             break;
/*     */           } 
/*     */         } 
/* 419 */         if (!found) {
/*     */           
/* 421 */           removed.add(pbp.getName());
/* 422 */           this.object.removeGuest(pbp.getPlayerId());
/*     */         } 
/*     */       } 
/*     */       
/* 426 */       Permissions.IPermission[] values = this.objectType.getEnumValues();
/* 427 */       String[] title = new String[values.length];
/* 428 */       int[] bits = new int[values.length]; int x;
/* 429 */       for (x = 0; x < values.length; x++) {
/*     */         
/* 431 */         title[x] = values[x].getHeader1() + " " + values[x].getHeader2();
/* 432 */         bits[x] = values[x].getBit();
/*     */       } 
/*     */       
/* 435 */       for (x = 0; x < pIds.length; x++) {
/*     */         
/* 437 */         int oldSettings = 0;
/* 438 */         if (ppl.exists(pIds[x])) {
/* 439 */           oldSettings = ppl.getPermissionsFor(pIds[x]).getPermissions();
/*     */         }
/* 441 */         LinkedList<String> perms = new LinkedList<>();
/* 442 */         for (int y = 0; y < 32; y++) {
/*     */           
/* 444 */           boolean oldBit = ((oldSettings >>> y & 0x1) == 1);
/* 445 */           boolean newBit = ((settings[x] >>> y & 0x1) == 1);
/* 446 */           if (oldBit != newBit) {
/*     */ 
/*     */ 
/*     */             
/* 450 */             int bit = -1;
/* 451 */             for (int j = 0; j < values.length; j++) {
/*     */               
/* 453 */               if (bits[j] == y) {
/*     */                 
/* 455 */                 bit = j;
/*     */                 break;
/*     */               } 
/*     */             } 
/* 459 */             if (bit != -1) {
/*     */               
/* 461 */               if (oldBit) {
/* 462 */                 perms.add("-" + title[bit]);
/*     */               } else {
/* 464 */                 perms.add("+" + title[bit]);
/*     */               }
/*     */             
/*     */             }
/* 468 */             else if (oldBit) {
/* 469 */               perms.add("-Bad Bit");
/*     */             } else {
/* 471 */               perms.add("+Bad Bit");
/*     */             } 
/*     */           } 
/*     */         } 
/* 475 */         String fields = "(" + String.join(", ", (Iterable)perms) + ")";
/* 476 */         if (ppl.exists(pIds[x])) {
/*     */ 
/*     */           
/* 479 */           if (oldSettings != settings[x])
/*     */           {
/*     */             
/* 482 */             this.object.addGuest(pIds[x], settings[x]);
/* 483 */             updated.add(PermissionsByPlayer.getPlayerOrGroupName(pIds[x]) + fields);
/*     */           }
/*     */         
/* 486 */         } else if (settings[x] != 0) {
/*     */           
/* 488 */           this.object.addGuest(pIds[x], settings[x]);
/* 489 */           added.add(PermissionsByPlayer.getPlayerOrGroupName(pIds[x]) + fields);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 494 */     if (this.hadManage && !this.object.mayShowPermissions((Creature)this.player)) {
/* 495 */       this.player.getCommunicator().sendHidePermissions();
/*     */     }
/* 497 */     StringBuilder buf = new StringBuilder();
/* 498 */     if (!changes.isEmpty())
/*     */     {
/* 500 */       buf.append("Changed " + String.join(", ", (Iterable)changes));
/*     */     }
/* 502 */     if (manageChanged) {
/*     */       
/* 504 */       if (buf.length() > 0)
/* 505 */         buf.append(", "); 
/* 506 */       if (this.objectType == ManageObjectList.Type.DOOR) {
/*     */         
/* 508 */         if (manage) {
/* 509 */           buf.append("Ticked Controlled by Flag");
/*     */         } else {
/* 511 */           buf.append("UnTicked Controlled by Flag");
/*     */         } 
/* 513 */       } else if (manage) {
/* 514 */         buf.append("Ticked Manage Flag");
/*     */       } else {
/* 516 */         buf.append("UnTicked Manage Flag");
/*     */       } 
/* 518 */     }  if (!added.isEmpty()) {
/*     */       
/* 520 */       if (buf.length() > 0)
/* 521 */         buf.append(", "); 
/* 522 */       buf.append("Added " + String.join(", ", (Iterable)added));
/*     */     } 
/* 524 */     if (!removed.isEmpty()) {
/*     */       
/* 526 */       if (buf.length() > 0)
/* 527 */         buf.append(", "); 
/* 528 */       buf.append("Removed " + String.join(", ", (Iterable)removed));
/*     */     } 
/* 530 */     if (!updated.isEmpty()) {
/*     */       
/* 532 */       if (buf.length() > 0)
/* 533 */         buf.append(", "); 
/* 534 */       buf.append("Updated " + String.join(", ", (Iterable)updated));
/*     */     } 
/* 536 */     if (buf.length() > 0) {
/*     */       
/* 538 */       String historyevent = buf.toString();
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
/* 550 */       if (buf.length() > 255)
/*     */       {
/*     */         
/* 553 */         historyevent = historyevent.substring(0, 250) + " ...";
/*     */       }
/* 555 */       PermissionsHistories.addHistoryEntry(this.object.getWurmId(), System.currentTimeMillis(), this.player
/* 556 */           .getWurmId(), this.player.getName(), historyevent);
/* 557 */       this.player.getCommunicator().sendNormalServerMessage("You " + historyevent);
/*     */     } 
/*     */ 
/*     */     
/* 561 */     ManagePermissions mp = new ManagePermissions((Creature)this.player, this.objectType, this.object, this.hasBackButton, this.target, this.parentHasBack, this.parentType, "");
/*     */     
/* 563 */     mp.sendQuestion();
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
/* 574 */     String objectName = this.object.getObjectName();
/* 575 */     boolean canChangeName = this.object.canChangeName((Creature)this.player);
/* 576 */     String ownerName = (this.player.getPower() > 1) ? this.object.getOwnerName() : "";
/* 577 */     boolean canChangeOwner = this.object.canChangeOwner((Creature)this.player);
/* 578 */     String warningText = this.object.getWarning();
/* 579 */     boolean isManaged = this.object.isManaged();
/* 580 */     boolean isManageEnabled = this.object.isManageEnabled(this.player);
/* 581 */     String mayManageText = this.object.mayManageText(this.player);
/* 582 */     String mayManageHover = this.object.mayManageHover(this.player);
/*     */     
/* 584 */     String messageOnTick = this.object.messageOnTick();
/* 585 */     String questionOnTick = this.object.questionOnTick();
/* 586 */     String messageUnTick = this.object.messageUnTick();
/* 587 */     String questionUnTick = this.object.questionUnTick();
/*     */     
/* 589 */     if (!this.object.mayShowPermissions((Creature)this.player)) {
/*     */       
/* 591 */       if (canChangeOwner || canChangeName || (isManageEnabled && this.objectType == ManageObjectList.Type.DOOR))
/*     */       {
/* 593 */         this.hadManage = false;
/* 594 */         String oldOwnerName = (this.player.getPower() > 1) ? ("(" + this.object.getOwnerName() + ") ") : "";
/* 595 */         String door = "";
/* 596 */         if (this.objectType == ManageObjectList.Type.DOOR)
/*     */         {
/* 598 */           door = "checkbox{id=\"manage\";text=\"" + mayManageText + "\";hover=\"" + mayManageHover + "\";confirm=\"" + messageOnTick + "\";question=\"" + questionOnTick + "\";unconfirm=\"" + messageUnTick + "\";unquestion=\"" + questionUnTick + "\";selected=\"" + isManaged + "\"}";
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 607 */         StringBuilder buf = new StringBuilder();
/*     */         
/* 609 */         buf.append("border{border{size=\"20,20\";null;null;label{type='bold';text=\"" + this.question + "\"};harray{" + (this.hasBackButton ? "button{text=\"Back\";id=\"back\"};label{text=\" \"};" : "") + "button{text=\"Close\";id=\"close\"};label{text=\" \"}};null;};harray{passthrough{id=\"id\";text=\"" + 
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
/* 620 */             getId() + "\"};label{text=\"\"}};varray{label{text=\"You are not allowed to manage permissions.\"};harray{label{text=\"Name:\"};" + (canChangeName ? ("input{id=\"object\";text=\"" + objectName + "\"};") : ("label{text=\"" + objectName + "\"};")) + "}" + (canChangeOwner ? ("harray{label{text=\"Change owner " + oldOwnerName + "to \"};input{id=\"owner\";text=\"\"}};") : ((this.objectType == ManageObjectList.Type.DOOR && this.object
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 630 */             .getWarning().length() == 0) ? door : "label{text=\" \"};")) + "};varray{label{text=\"\"};label{text=\"\"};harray{button{text=\"Apply Changes\";id=\"save\"};label{text=\" \"}}};" + (
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 639 */             (warningText.length() > 0) ? ("center{label{type=\"bold\";color=\"240,40,40\";text=\"" + warningText + "\"}};") : "null;") + "}");
/*     */ 
/*     */         
/* 642 */         BMLBuilder completePanel = BMLBuilder.createBMLBorderPanel(
/* 643 */             BMLBuilder.createBMLBorderPanel(null, null, 
/*     */ 
/*     */               
/* 646 */               BMLBuilder.createGenericBuilder()
/* 647 */               .addLabel(this.question, null, BMLBuilder.TextType.BOLD, null), 
/* 648 */               BMLBuilder.createHorizArrayNode(false)
/* 649 */               .addString(this.hasBackButton ? BMLBuilder.createButton("back", "Back") : BMLBuilder.createLabel(" "))
/* 650 */               .addButton("close", "Close")
/* 651 */               .addLabel(" "), null, 20, 20), 
/*     */ 
/*     */             
/* 654 */             BMLBuilder.createHorizArrayNode(false)
/* 655 */             .addPassthrough("id", Integer.toString(getId()))
/* 656 */             .addLabel(""), 
/* 657 */             BMLBuilder.createVertArrayNode(false)
/* 658 */             .addLabel("You are not allowed to manage permissions.")
/* 659 */             .addString(BMLBuilder.createHorizArrayNode(false)
/* 660 */               .addLabel("Name:")
/* 661 */               .addString(canChangeName ? BMLBuilder.createInput("object", objectName) : BMLBuilder.createLabel(objectName))
/* 662 */               .toString())
/* 663 */             .addString(canChangeOwner ? 
/* 664 */               BMLBuilder.createHorizArrayNode(false)
/* 665 */               .addLabel("Change owner " + oldOwnerName + "to ")
/* 666 */               .addInput("owner", "", 0, 0)
/* 667 */               .toString() : ((this.objectType == ManageObjectList.Type.DOOR && this.object
/* 668 */               .getWarning().length() == 0) ? 
/* 669 */               BMLBuilder.createGenericBuilder().addCheckbox("manage", mayManageText, questionOnTick, messageOnTick, questionUnTick, messageUnTick, mayManageHover, isManaged, true, null)
/*     */               
/* 671 */               .toString() : 
/* 672 */               BMLBuilder.createLabel(" "))), 
/* 673 */             BMLBuilder.createVertArrayNode(false)
/* 674 */             .addLabel("")
/* 675 */             .addLabel("")
/* 676 */             .addString(BMLBuilder.createHorizArrayNode(false)
/* 677 */               .addButton("save", "Apply Changes")
/* 678 */               .addLabel(" ")
/* 679 */               .toString()), 
/* 680 */             (warningText.length() > 0) ? 
/* 681 */             BMLBuilder.createCenteredNode(
/* 682 */               BMLBuilder.createGenericBuilder()
/* 683 */               .addLabel(warningText, null, BMLBuilder.TextType.BOLD, Color.RED)) : null);
/*     */         
/* 685 */         int ht = (warningText.length() > 0) ? 150 : 125;
/* 686 */         getResponder().getCommunicator().sendBml(320, ht, true, true, buf.toString(), 200, 200, 200, this.objectType
/* 687 */             .getTitle() + " - Manage Permissions");
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 692 */       if (this.error.length() > 0) {
/*     */ 
/*     */ 
/*     */         
/* 696 */         this.player.getCommunicator().sendPermissionsApplyChangesFailed(getId(), this.error);
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 701 */       String mySettlement = (this.player.getCitizenVillage() != null) ? "Citizens of my deed" : "";
/*     */ 
/*     */       
/* 704 */       String allowAlliesText = this.object.getAllianceName();
/* 705 */       String allowCitizensText = this.object.getSettlementName();
/* 706 */       String allowKingdomText = this.object.getKingdomName();
/* 707 */       String allowEveryoneText = this.object.canAllowEveryone() ? "\"Everyone\"" : "";
/* 708 */       String allowRolePermissionText = this.object.getRolePermissionName();
/*     */       
/* 710 */       Permissions.IPermission[] values = this.objectType.getEnumValues();
/*     */       
/* 712 */       String[] header1 = new String[values.length];
/* 713 */       String[] header2 = new String[values.length];
/* 714 */       String[] hover = new String[values.length];
/*     */       
/* 716 */       int[] bits = new int[values.length];
/* 717 */       for (int x = 0; x < values.length; x++) {
/*     */         
/* 719 */         header1[x] = values[x].getHeader1();
/* 720 */         header2[x] = values[x].getHeader2();
/* 721 */         hover[x] = values[x].getHover();
/* 722 */         bits[x] = values[x].getBit();
/*     */       } 
/*     */       
/* 725 */       PermissionsPlayerList allowedList = this.object.getPermissionsPlayerList();
/*     */       
/* 727 */       String[] permittedNames = new String[allowedList.size()];
/* 728 */       long[] permittedIds = new long[allowedList.size()];
/* 729 */       boolean[][] allowed = new boolean[allowedList.size()][header1.length];
/*     */       
/* 731 */       int count = 0;
/* 732 */       PermissionsByPlayer[] pbpList = allowedList.getPermissionsByPlayer();
/* 733 */       Arrays.sort((Object[])pbpList);
/*     */       
/* 735 */       for (PermissionsByPlayer pbp : pbpList) {
/*     */         
/* 737 */         long playerId = pbp.getPlayerId();
/* 738 */         permittedNames[count] = pbp.getName();
/* 739 */         permittedIds[count] = playerId;
/* 740 */         for (int bit = 0; bit < bits.length; bit++)
/*     */         {
/* 742 */           allowed[count][bit] = pbp.hasPermission(bits[bit]);
/*     */         }
/* 744 */         count++;
/*     */       } 
/*     */       
/* 747 */       Friend[] friendsList = this.player.getFriends();
/* 748 */       Arrays.sort((Object[])friendsList);
/*     */       
/* 750 */       List<Long> trusted = new ArrayList<>();
/* 751 */       List<Long> friends = new ArrayList<>();
/* 752 */       List<Long> citizens = new ArrayList<>();
/*     */       
/* 754 */       for (count = 0; count < friendsList.length; count++) {
/*     */         
/* 756 */         if (friendsList[count].getCategory().getCatId() == Friend.Category.Trusted.getCatId() && 
/* 757 */           !allowedList.exists(friendsList[count].getFriendId()))
/* 758 */           trusted.add(Long.valueOf(friendsList[count].getFriendId())); 
/* 759 */         if (friendsList[count].getCategory().getCatId() == Friend.Category.Friends.getCatId() && 
/* 760 */           !allowedList.exists(friendsList[count].getFriendId())) {
/* 761 */           friends.add(Long.valueOf(friendsList[count].getFriendId()));
/*     */         }
/*     */       } 
/* 764 */       playerIdName[] trustedIdNames = new playerIdName[trusted.size()];
/* 765 */       for (count = 0; count < trusted.size(); count++)
/* 766 */         trustedIdNames[count] = new playerIdName(((Long)trusted.get(count)).longValue()); 
/* 767 */       Arrays.sort((Object[])trustedIdNames);
/*     */       
/* 769 */       long[] trustedIds = new long[trusted.size()];
/* 770 */       String[] trustedNames = new String[trusted.size()];
/* 771 */       for (count = 0; count < trusted.size(); count++) {
/*     */         
/* 773 */         trustedIds[count] = trustedIdNames[count].getWurmId();
/* 774 */         trustedNames[count] = trustedIdNames[count].getName();
/*     */       } 
/*     */       
/* 777 */       playerIdName[] friendIdNames = new playerIdName[friends.size()];
/* 778 */       for (count = 0; count < friends.size(); count++)
/* 779 */         friendIdNames[count] = new playerIdName(((Long)friends.get(count)).longValue()); 
/* 780 */       Arrays.sort((Object[])friendIdNames);
/*     */       
/* 782 */       long[] friendIds = new long[friends.size()];
/* 783 */       String[] friendNames = new String[friends.size()];
/* 784 */       for (count = 0; count < friends.size(); count++) {
/*     */         
/* 786 */         friendIds[count] = friendIdNames[count].getWurmId();
/* 787 */         friendNames[count] = friendIdNames[count].getName();
/*     */       } 
/*     */       
/* 790 */       Village village = this.player.getCitizenVillage();
/* 791 */       if (village != null)
/*     */       {
/* 793 */         for (Citizen c : village.getCitizens()) {
/*     */           
/* 795 */           if (!allowedList.exists(c.wurmId) && c.isPlayer())
/* 796 */             citizens.add(Long.valueOf(c.wurmId)); 
/*     */         } 
/*     */       }
/* 799 */       playerIdName[] citizenIdNames = new playerIdName[citizens.size()];
/* 800 */       for (count = 0; count < citizens.size(); count++)
/* 801 */         citizenIdNames[count] = new playerIdName(((Long)citizens.get(count)).longValue()); 
/* 802 */       Arrays.sort((Object[])citizenIdNames);
/*     */       
/* 804 */       long[] citizenIds = new long[citizens.size()];
/* 805 */       String[] citizenNames = new String[citizens.size()];
/* 806 */       for (count = 0; count < citizens.size(); count++) {
/*     */         
/* 808 */         citizenIds[count] = citizenIdNames[count].getWurmId();
/* 809 */         citizenNames[count] = citizenIdNames[count].getName();
/*     */       } 
/*     */       
/* 812 */       this.player.getCommunicator().sendShowPermissions(getId(), this.hasBackButton, this.objectType.getTitle(), objectName, ownerName, canChangeName, canChangeOwner, isManaged, isManageEnabled, mayManageText, mayManageHover, warningText, messageOnTick, questionOnTick, messageUnTick, questionUnTick, allowAlliesText, allowCitizensText, allowKingdomText, allowEveryoneText, allowRolePermissionText, header1, header2, hover, trustedNames, trustedIds, friendNames, friendIds, mySettlement, citizenNames, citizenIds, permittedNames, permittedIds, allowed);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final boolean containsIllegalCharacters(String legalChars, String name) {
/* 823 */     char[] chars = name.toCharArray();
/*     */     
/* 825 */     for (char lC : chars) {
/*     */       
/* 827 */       if (legalChars.indexOf(lC) < 0)
/* 828 */         return true; 
/*     */     } 
/* 830 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class playerIdName
/*     */     implements Comparable<playerIdName>
/*     */   {
/*     */     final long id;
/*     */ 
/*     */     
/*     */     final String name;
/*     */ 
/*     */ 
/*     */     
/*     */     playerIdName(long aId) {
/* 846 */       this.id = aId;
/* 847 */       this.name = PermissionsByPlayer.getPlayerOrGroupName(this.id);
/*     */     }
/*     */ 
/*     */     
/*     */     public long getWurmId() {
/* 852 */       return this.id;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getName() {
/* 857 */       return this.name;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int compareTo(playerIdName pin) {
/* 868 */       return getName().compareTo(pin.getName());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\ManagePermissions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */