/*     */ package com.wurmonline.server.intra;
/*     */ 
/*     */ import com.wurmonline.server.Servers;
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
/*     */ public abstract class IntraCommand
/*     */   implements IntraServerConnectionListener
/*     */ {
/*     */   long startTime;
/*     */   long timeOutAt;
/*  34 */   long timeOutTime = 10000L;
/*  35 */   private static int nums = 0;
/*  36 */   static int num = 0;
/*     */   
/*  38 */   public int pollTimes = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  43 */   static final Logger logger2 = Logger.getLogger("IntraServer");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   IntraCommand() {
/*  51 */     num = nums++;
/*     */     
/*  53 */     this.startTime = System.currentTimeMillis();
/*  54 */     this.timeOutAt = this.startTime + this.timeOutTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract boolean poll();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void commandExecuted(IntraClient paramIntraClient);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void commandFailed(IntraClient paramIntraClient);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void dataReceived(IntraClient paramIntraClient);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isThisLoginServer() {
/*  85 */     return Servers.isThisLoginServer();
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
/*     */   String getLoginServerIntraServerAddress() {
/*  97 */     return Servers.loginServer.INTRASERVERADDRESS;
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
/*     */   String getLoginServerIntraServerPort() {
/* 109 */     return Servers.loginServer.INTRASERVERPORT;
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
/*     */   String getLoginServerIntraServerPassword() {
/* 121 */     return Servers.loginServer.INTRASERVERPASSWORD;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\intra\IntraCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */