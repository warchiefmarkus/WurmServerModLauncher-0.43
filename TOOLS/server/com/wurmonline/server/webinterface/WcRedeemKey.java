/*    */ package com.wurmonline.server.webinterface;
/*    */ 
/*    */ import com.wurmonline.server.NoSuchPlayerException;
/*    */ import com.wurmonline.server.Players;
/*    */ import com.wurmonline.server.WurmId;
/*    */ import com.wurmonline.server.players.Player;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WcRedeemKey
/*    */   extends WebCommand
/*    */ {
/* 44 */   private static final Logger logger = Logger.getLogger(WcRedeemKey.class.getName());
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public WcRedeemKey(long playerId, String coupon, byte reply) {
/* 50 */     super(WurmId.getNextWCCommandId(), (short)26);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public WcRedeemKey(long aId, byte[] aData) {
/* 56 */     super(aId, (short)26, aData);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean autoForward() {
/* 67 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   byte[] encode() {
/* 74 */     byte[] barr = null;
/*    */     
/* 76 */     return barr;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute() {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static final Player getRedeemingPlayer(long wurmId) {
/*    */     try {
/* 89 */       return Players.getInstance().getPlayer(wurmId);
/*    */     }
/* 91 */     catch (NoSuchPlayerException e) {
/*    */       
/* 93 */       logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/* 94 */       return null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\webinterface\WcRedeemKey.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */