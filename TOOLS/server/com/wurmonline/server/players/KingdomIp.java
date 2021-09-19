/*     */ package com.wurmonline.server.players;
/*     */ 
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
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
/*     */ public class KingdomIp
/*     */   implements TimeConstants, MiscConstants
/*     */ {
/*     */   private long lastLogout;
/*     */   private final String ipaddress;
/*     */   private byte currentKingdom;
/*  46 */   private final long timeBetweenKingdomSwitches = 600000L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  52 */   private static final Map<String, KingdomIp> ips = new ConcurrentHashMap<>();
/*     */   
/*  54 */   private static int pruneCounter = 0;
/*     */ 
/*     */   
/*  57 */   private static final Logger logger = Logger.getLogger(KingdomIp.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public KingdomIp(String ipAddress, byte activeKingdom) {
/*  64 */     this.ipaddress = ipAddress;
/*  65 */     this.currentKingdom = activeKingdom;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void logoff() {
/*  70 */     if (!Players.existsPlayerWithIp(this.ipaddress))
/*     */     {
/*  72 */       this.lastLogout = System.currentTimeMillis();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static final KingdomIp[] getAllKips() {
/*  78 */     return (KingdomIp[])ips.values().toArray((Object[])new KingdomIp[ips.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void logon(byte newKingdom) {
/*  83 */     this.lastLogout = 0L;
/*  84 */     this.currentKingdom = newKingdom;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getIpAddress() {
/*  89 */     return this.ipaddress;
/*     */   }
/*     */ 
/*     */   
/*     */   public final byte getKingdom() {
/*  94 */     return this.currentKingdom;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void setKingdom(byte newKingdom) {
/*  99 */     this.currentKingdom = newKingdom;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final KingdomIp getKIP(String ipAddress) {
/* 104 */     if (ipAddress == null)
/* 105 */       return null; 
/* 106 */     return ips.get(ipAddress.replace("/", ""));
/*     */   }
/*     */ 
/*     */   
/*     */   public final long getLastLogout() {
/* 111 */     return this.lastLogout;
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
/*     */   public final long mayLogonKingdom(byte kingdomChecked) {
/* 123 */     if (kingdomChecked == this.currentKingdom) {
/* 124 */       return 1L;
/*     */     }
/*     */     
/* 127 */     if (this.lastLogout == 0L)
/* 128 */       return -1L; 
/* 129 */     if (System.currentTimeMillis() - this.lastLogout > 600000L) {
/* 130 */       return 1L;
/*     */     }
/* 132 */     return System.currentTimeMillis() - this.lastLogout;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final KingdomIp getKIP(String ipAddress, byte kingdom) {
/* 137 */     pruneCounter++;
/* 138 */     if (pruneCounter == 300) {
/*     */       
/* 140 */       pruneCounter = 0;
/* 141 */       for (KingdomIp kp : ips.values()) {
/*     */         
/* 143 */         if (kp.lastLogout > 0L) {
/*     */           
/* 145 */           if (System.currentTimeMillis() - kp.lastLogout > 3600000L) {
/*     */             
/* 147 */             logger.log(Level.INFO, "Pruning kip " + kp.getIpAddress());
/* 148 */             ips.remove(kp.getIpAddress());
/*     */           } 
/*     */           
/*     */           continue;
/*     */         } 
/* 153 */         if (!Players.existsPlayerWithIp(kp.getIpAddress())) {
/*     */           
/* 155 */           logger.log(Level.INFO, "Detected non existing address for logged on ip when pruning kip " + kp
/* 156 */               .getIpAddress());
/* 157 */           ips.remove(kp.getIpAddress());
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 162 */     KingdomIp kip = ips.get(ipAddress.replace("/", ""));
/* 163 */     if (kip == null)
/*     */     {
/* 165 */       if (kingdom != 0) {
/*     */         
/* 167 */         kip = new KingdomIp(ipAddress.replace("/", ""), kingdom);
/* 168 */         ips.put(ipAddress.replace("/", ""), kip);
/*     */       } 
/*     */     }
/*     */     
/* 172 */     return kip;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\KingdomIp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */