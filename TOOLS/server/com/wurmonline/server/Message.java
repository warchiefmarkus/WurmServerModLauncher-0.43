/*     */ package com.wurmonline.server;
/*     */ 
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import javax.annotation.Nullable;
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
/*     */ public final class Message
/*     */   implements MiscConstants
/*     */ {
/*     */   public static final String rcsversion = "$Id: Message.java,v 1.5 2006-09-28 23:19:14 root Exp $";
/*     */   private final byte type;
/*     */   private final String window;
/*     */   public static final String windowWarn = "Warn";
/*     */   public static final String windowDebug = "Debug";
/*     */   public static final String windowRoads = "Roads";
/*     */   private final String message;
/*     */   public static final byte SAY = 0;
/*     */   public static final byte TELL = 3;
/*     */   public static final byte WHISPER = 2;
/*     */   public static final byte VILLAGE = 3;
/*     */   public static final byte GROUP = 4;
/*     */   public static final byte SHOUT = 5;
/*     */   public static final byte EMOTE = 6;
/*     */   public static final byte SERVERSAFE = 6;
/*     */   public static final byte SERVERNORMAL = 7;
/*     */   public static final byte SERVERALERT = 8;
/*     */   public static final byte MGMT = 9;
/*     */   public static final byte KINGDOM = 10;
/*     */   public static final byte DEV = 11;
/*     */   public static final byte CA = 12;
/*     */   public static final byte TEAM = 13;
/*     */   public static final byte TEAM_LEADER = 14;
/*     */   public static final byte ALLIANCE = 15;
/*     */   public static final byte GLOBKINGDOM = 16;
/*     */   public static final byte EVENT = 17;
/*     */   public static final byte TRADE = 18;
/*     */   public static final byte LEFTWIN = 0;
/*     */   public static final byte RIGHTWIN = 1;
/* 121 */   private long receiver = -10L;
/* 122 */   private long senderId = -10L;
/*     */   private Creature sender;
/* 124 */   private byte senderKingdom = 0;
/*     */   
/* 126 */   private int colorR = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getColorR() {
/* 135 */     return this.colorR;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setColorR(int aColorR) {
/* 146 */     this.colorR = aColorR;
/*     */   }
/*     */   
/* 149 */   private int colorG = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getColorG() {
/* 158 */     return this.colorG;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setColorG(int aColorG) {
/* 169 */     this.colorG = aColorG;
/*     */   }
/*     */   
/* 172 */   private int colorB = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getColorB() {
/* 181 */     return this.colorB;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setColorB(int aColorB) {
/* 192 */     this.colorB = aColorB;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Message(@Nullable Creature aSender, byte aType, String aWindow, String aMessage) {
/* 210 */     this.sender = aSender;
/* 211 */     this.type = aType;
/* 212 */     this.window = aWindow;
/* 213 */     this.message = aMessage;
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
/*     */   public Message(@Nullable Creature aSender, byte aType, String aWindow, String aMessage, int colourR, int colourG, int colourB) {
/* 238 */     this.sender = aSender;
/* 239 */     this.type = aType;
/* 240 */     this.window = aWindow;
/* 241 */     this.message = aMessage;
/* 242 */     this.colorR = colourR;
/* 243 */     this.colorG = colourG;
/* 244 */     this.colorB = colourB;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Creature getSender() {
/* 255 */     return this.sender;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSender(Creature newSender) {
/* 265 */     this.sender = newSender;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getType() {
/* 276 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessage() {
/* 287 */     return this.message;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getWindow() {
/* 298 */     if (this.type == 11)
/*     */     {
/* 300 */       if (this.window.equals("GM")) {
/*     */         
/* 302 */         if (this.message.contains(" movement too "))
/* 303 */           return "Warn"; 
/* 304 */         if (this.message.startsWith("<System> Debug:"))
/* 305 */           return "Debug"; 
/* 306 */         if (this.message.startsWith("<Roads> "))
/* 307 */           return "Roads"; 
/*     */       } 
/*     */     }
/* 310 */     return this.window;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRed() {
/* 329 */     if (this.colorR >= 0)
/* 330 */       return this.colorR; 
/* 331 */     if (this.sender != null && this.sender.hasColoredChat())
/* 332 */       return getCustomRed(); 
/* 333 */     if (this.type == 5)
/* 334 */       return 215; 
/* 335 */     if (this.type == 6)
/* 336 */       return 228; 
/* 337 */     if (this.type == 3 || this.type == 14)
/* 338 */       return 145; 
/* 339 */     if (this.type == 1)
/* 340 */       return 58; 
/* 341 */     if (isHelpChannel(this.window) && this.sender != null && (this.sender
/* 342 */       .isPlayerAssistant() || this.sender.mayMute() || this.sender.getPower() > 0)) {
/* 343 */       return 105;
/*     */     }
/* 345 */     return 255;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBlue() {
/* 364 */     if (this.colorB >= 0)
/* 365 */       return this.colorB; 
/* 366 */     if (this.sender != null && this.sender.hasColoredChat())
/* 367 */       return getCustomBlue(); 
/* 368 */     if (this.type == 5)
/* 369 */       return 39; 
/* 370 */     if (this.type == 6)
/* 371 */       return 138; 
/* 372 */     if (this.type == 3 || this.type == 14)
/* 373 */       return 158; 
/* 374 */     if (this.type == 1)
/* 375 */       return 239; 
/* 376 */     if (isHelpChannel(this.window) && this.sender != null && (this.sender
/* 377 */       .isPlayerAssistant() || this.sender.mayMute() || this.sender.getPower() > 0)) {
/* 378 */       return 210;
/*     */     }
/* 380 */     return 255;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getGreen() {
/* 400 */     if (this.colorG >= 0)
/* 401 */       return this.colorG; 
/* 402 */     if (this.sender != null && this.sender.hasColoredChat())
/* 403 */       return getCustomGreen(); 
/* 404 */     if (this.type == 5)
/* 405 */       return 168; 
/* 406 */     if (this.type == 6)
/* 407 */       return 244; 
/* 408 */     if (this.type == 3 || this.type == 14)
/* 409 */       return 255; 
/* 410 */     if (this.type == 1)
/* 411 */       return 163; 
/* 412 */     if (isHelpChannel(this.window) && this.sender != null && (this.sender
/* 413 */       .isPlayerAssistant() || this.sender.mayMute() || this.sender.getPower() > 0)) {
/* 414 */       return 231;
/*     */     }
/* 416 */     return 255;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isHelpChannel(String channelName) {
/* 421 */     return (channelName.equals("CA HELP") || channelName
/* 422 */       .equals("GV HELP") || channelName
/* 423 */       .equals("JK HELP") || channelName
/* 424 */       .equals("MR HELP") || channelName
/* 425 */       .equals("HOTS HELP"));
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
/*     */   public int getCustomRed() {
/* 437 */     if (this.sender != null)
/* 438 */       return this.sender.getCustomRedChat(); 
/* 439 */     return 255;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCustomGreen() {
/* 450 */     if (this.sender != null)
/* 451 */       return this.sender.getCustomGreenChat(); 
/* 452 */     return 140;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCustomBlue() {
/* 463 */     if (this.sender != null)
/* 464 */       return this.sender.getCustomBlueChat(); 
/* 465 */     return 0;
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
/*     */   public int hashCode() {
/* 477 */     int prime = 31;
/* 478 */     int result = 1;
/* 479 */     result = 31 * result + ((this.message == null) ? 0 : this.message.hashCode());
/* 480 */     result = 31 * result + ((this.sender == null) ? 0 : this.sender.hashCode());
/* 481 */     result = 31 * result + this.type;
/* 482 */     result = 31 * result + ((this.window == null) ? 0 : this.window.hashCode());
/* 483 */     return result;
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
/*     */   public boolean equals(Object obj) {
/* 495 */     if (this == obj)
/*     */     {
/* 497 */       return true;
/*     */     }
/* 499 */     if (obj == null || getClass() != obj.getClass())
/*     */     {
/* 501 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 505 */     Message other = (Message)obj;
/* 506 */     if (this.message == null) {
/*     */       
/* 508 */       if (other.message != null)
/* 509 */         return false; 
/*     */     } else {
/* 511 */       if (!this.message.equals(other.message))
/*     */       {
/* 513 */         return false;
/*     */       }
/* 515 */       if (this.sender == null) {
/*     */         
/* 517 */         if (other.sender != null)
/* 518 */           return false; 
/*     */       } else {
/* 520 */         if (!this.sender.equals(other.sender) || this.type != other.type)
/*     */         {
/* 522 */           return false;
/*     */         }
/* 524 */         if (this.window == null)
/*     */         
/* 526 */         { if (other.window != null) {
/* 527 */             return false;
/*     */           } }
/*     */         else
/*     */         
/* 531 */         { return this.window.equals(other.window); } 
/*     */       } 
/* 533 */     }  return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getSenderKingdom() {
/* 544 */     return this.senderKingdom;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSenderKingdom(byte aSenderKingdom) {
/* 555 */     this.senderKingdom = aSenderKingdom;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getReceiver() {
/* 565 */     return this.receiver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReceiver(long aReceiver) {
/* 576 */     this.receiver = aReceiver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getSenderId() {
/* 586 */     return this.senderId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSenderId(long aSenderId) {
/* 597 */     this.senderId = aSenderId;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\Message.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */