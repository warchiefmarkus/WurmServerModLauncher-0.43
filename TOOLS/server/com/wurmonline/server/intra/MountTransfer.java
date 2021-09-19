/*     */ package com.wurmonline.server.intra;
/*     */ 
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ public final class MountTransfer
/*     */   implements MiscConstants, TimeConstants
/*     */ {
/*  32 */   private static final Map<Long, MountTransfer> transfers = new HashMap<>();
/*  33 */   private static final Map<Long, MountTransfer> transfersPerCreature = new HashMap<>();
/*  34 */   private final Map<Long, Integer> seats = new HashMap<>();
/*     */   
/*  36 */   private static final Logger logger = Logger.getLogger(MountTransfer.class.getName());
/*     */ 
/*     */   
/*     */   private final long vehicleid;
/*     */   
/*     */   private final long pilotid;
/*     */   
/*     */   private final long creationTime;
/*     */ 
/*     */   
/*     */   public MountTransfer(long vehicleId, long pilotId) {
/*  47 */     this.vehicleid = vehicleId;
/*  48 */     this.pilotid = pilotId;
/*  49 */     this.creationTime = System.currentTimeMillis();
/*  50 */     transfers.put(Long.valueOf(vehicleId), this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addToSeat(long wid, int seatid) {
/*  55 */     if (logger.isLoggable(Level.FINER))
/*     */     {
/*  57 */       logger.finer("Adding " + wid + ", seat=" + seatid);
/*     */     }
/*  59 */     this.seats.put(Long.valueOf(wid), Integer.valueOf(seatid));
/*  60 */     transfersPerCreature.put(Long.valueOf(wid), this);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSeatFor(long wurmid) {
/*  65 */     if (this.seats.keySet().contains(Long.valueOf(wurmid)))
/*  66 */       return ((Integer)this.seats.get(Long.valueOf(wurmid))).intValue(); 
/*  67 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(long wurmid) {
/*  72 */     if (logger.isLoggable(Level.FINER))
/*     */     {
/*  74 */       logger.finer("Removing " + wurmid);
/*     */     }
/*  76 */     this.seats.remove(Long.valueOf(wurmid));
/*  77 */     transfersPerCreature.remove(Long.valueOf(wurmid));
/*  78 */     if (this.seats.isEmpty())
/*     */     {
/*  80 */       clearAndRemove();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   long getCreationTime() {
/*  86 */     return this.creationTime;
/*     */   }
/*     */ 
/*     */   
/*     */   private void clearAndRemove() {
/*  91 */     for (Iterator<Long> seatIt = this.seats.keySet().iterator(); seatIt.hasNext();)
/*  92 */       transfersPerCreature.remove(seatIt.next()); 
/*  93 */     transfers.remove(Long.valueOf(this.vehicleid));
/*  94 */     this.seats.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getVehicleId() {
/*  99 */     return this.vehicleid;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getPilotId() {
/* 104 */     return this.pilotid;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final MountTransfer getTransferFor(long wurmid) {
/* 109 */     return transfersPerCreature.get(Long.valueOf(wurmid));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void pruneTransfers() {
/* 117 */     Set<MountTransfer> toRemove = new HashSet<>();
/*     */     
/* 119 */     for (Iterator<MountTransfer> it = transfers.values().iterator(); it.hasNext(); ) {
/*     */       
/* 121 */       MountTransfer mt = it.next();
/* 122 */       if (System.currentTimeMillis() - mt.getCreationTime() > 1800000L)
/* 123 */         toRemove.add(mt); 
/*     */     } 
/* 125 */     for (Iterator<MountTransfer> it2 = toRemove.iterator(); it2.hasNext();)
/*     */     {
/* 127 */       ((MountTransfer)it2.next()).clearAndRemove();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\intra\MountTransfer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */