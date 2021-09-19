/*     */ package com.wurmonline.server.players;
/*     */ 
/*     */ import com.wurmonline.server.utils.ProtocolUtilities;
/*     */ import java.util.Arrays;
/*     */ import java.util.concurrent.atomic.AtomicLong;
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
/*     */ final class PlayerMessage
/*     */ {
/*  32 */   private static final Logger logger = Logger.getLogger(PlayerMessage.class.getName());
/*     */   
/*     */   private final Long iPlayerId;
/*     */   
/*     */   private final byte[] iMessageBytes;
/*     */   
/*     */   private final String iDescription;
/*     */   
/*  40 */   private final long iMessageId = messageIdSequence.getAndIncrement();
/*     */   
/*  42 */   private static final AtomicLong messageIdSequence = new AtomicLong();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PlayerMessage(Long aPlayerId, byte[] aMessageBytes) {
/*  54 */     if (logger.isLoggable(Level.FINEST))
/*     */     {
/*  56 */       logger.finest("Constructor - PlayerId: " + aPlayerId + ", MessageBytes: " + Arrays.toString(aMessageBytes));
/*     */     }
/*  58 */     if (aPlayerId == null || aMessageBytes == null || aMessageBytes.length == 0)
/*     */     {
/*  60 */       throw new IllegalArgumentException("PlayerId and MessageBytes must be non-null andthe MessageBytes must conatin at least one byte");
/*     */     }
/*  62 */     this.iPlayerId = aPlayerId;
/*  63 */     this.iMessageBytes = aMessageBytes;
/*  64 */     this.iDescription = ProtocolUtilities.getDescriptionForCommand(this.iMessageBytes[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   long getMessageId() {
/*  74 */     return this.iMessageId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Long getPlayerId() {
/*  84 */     return this.iPlayerId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   byte[] getMessageBytes() {
/*  94 */     return this.iMessageBytes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String getDescription() {
/* 104 */     return this.iDescription;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object aObj) {
/* 115 */     boolean lEquals = false;
/* 116 */     if (aObj instanceof PlayerMessage) {
/*     */       
/* 118 */       PlayerMessage lMessage = (PlayerMessage)aObj;
/*     */ 
/*     */ 
/*     */       
/* 122 */       if (lMessage.iMessageId != this.iMessageId && lMessage.iPlayerId != null && lMessage.iPlayerId.equals(this.iPlayerId) && lMessage.iMessageBytes != null && 
/* 123 */         Arrays.equals(lMessage.iMessageBytes, this.iMessageBytes))
/*     */       {
/* 125 */         lEquals = true;
/*     */       }
/*     */     } 
/* 128 */     return lEquals;
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
/* 140 */     int lHashCode = (int)(this.iMessageId ^ this.iMessageId >>> 32L);
/* 141 */     if (this.iPlayerId != null)
/*     */     {
/* 143 */       lHashCode ^= this.iPlayerId.hashCode();
/*     */     }
/* 145 */     if (this.iMessageBytes != null)
/*     */     {
/* 147 */       lHashCode ^= Arrays.hashCode(this.iMessageBytes);
/*     */     }
/*     */     
/* 150 */     return lHashCode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 161 */     return "PlayerMessage [MessageId: " + this.iMessageId + ", PlayerId: " + this.iPlayerId + ", MessageBytes length: " + this.iMessageBytes.length + ", Description: " + this.iDescription + ']';
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\PlayerMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */