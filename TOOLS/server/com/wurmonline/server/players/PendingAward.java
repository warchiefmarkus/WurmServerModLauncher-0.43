/*    */ package com.wurmonline.server.players;
/*    */ 
/*    */ import com.wurmonline.server.Servers;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PendingAward
/*    */ {
/* 34 */   private static Logger logger = Logger.getLogger(PendingAward.class.getName());
/*    */   
/*    */   long wurmid;
/*    */   String name;
/* 38 */   private static final ConcurrentHashMap<String, PendingAward> pendingAwards = new ConcurrentHashMap<>();
/*    */   
/*    */   int days;
/*    */   int months;
/*    */   
/*    */   public PendingAward(long _wurmid, String _name, int _days, int _months) {
/* 44 */     this.wurmid = _wurmid;
/* 45 */     this.name = _name;
/* 46 */     this.days = _days;
/* 47 */     this.months = _months;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public final void award() {
/* 53 */     PlayerInfo pinf = PlayerInfoFactory.getPlayerInfoWithWurmId(this.wurmid);
/*    */     
/* 55 */     if (pinf != null) {
/*    */       
/* 57 */       if (pinf.currentServer == Servers.localServer.id) {
/*    */         
/* 59 */         if (pinf.awards == null) {
/*    */           
/* 61 */           pinf.awards = new Awards(this.wurmid, this.days, 0, 0, 0, 0, 0L, 0, 0, true);
/* 62 */           for (int m = 0; m < this.months; m++) {
/*    */             
/* 64 */             pinf.awards.setMonthsPaidSinceReset(pinf.awards.getMonthsPaidSinceReset() + 1);
/* 65 */             pinf.awards.setMonthsPaidInARow(pinf.awards.getMonthsPaidInARow() + 1);
/* 66 */             AwardLadder.award(pinf, true);
/*    */           } 
/* 68 */           pinf.awards.setLastTickedDay(System.currentTimeMillis());
/* 69 */           pinf.awards.update();
/*    */         }
/*    */         else {
/*    */           
/* 73 */           int monthsMissed = this.months - pinf.awards.getMonthsPaidSinceReset();
/* 74 */           if (monthsMissed > 0) {
/*    */             
/* 76 */             for (int m = 0; m < monthsMissed; m++) {
/*    */               
/* 78 */               pinf.awards.setMonthsPaidSinceReset(pinf.awards.getMonthsPaidSinceReset() + 1);
/* 79 */               pinf.awards.setMonthsPaidInARow(pinf.awards.getMonthsPaidInARow() + 1);
/* 80 */               AwardLadder.award(pinf, true);
/*    */             } 
/* 82 */             pinf.awards.setLastTickedDay(System.currentTimeMillis());
/* 83 */             pinf.awards.update();
/*    */           } 
/*    */         } 
/*    */       } else {
/*    */         
/* 88 */         logger.log(Level.INFO, this.wurmid + " " + this.name + " is on server " + pinf.currentServer + " and not here when being awarded " + this.months + " months, " + this.days + " days.");
/*    */       }
/*    */     
/*    */     } else {
/*    */       
/* 93 */       logger.log(Level.INFO, this.wurmid + " " + this.name + " no PlayerInfo when being awarded " + this.months + " months, " + this.days + " days.");
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\PendingAward.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */