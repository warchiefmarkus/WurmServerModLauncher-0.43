/*     */ package com.wurmonline.server.banks;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public final class Banks
/*     */   implements TimeConstants
/*     */ {
/*  47 */   private static final Map<Long, Bank> banks = new HashMap<>();
/*     */   
/*     */   private static final String LOADBANKS = "SELECT * FROM BANKS";
/*     */   
/*     */   private static final String ISBANKED = "SELECT EXISTS(SELECT 1 FROM BANKS_ITEMS WHERE ITEMID=?) AS ISBANKED";
/*     */   private static final String BANKID = "SELECT BANKID FROM BANKS_ITEMS WHERE ITEMID=?";
/*     */   private static final String OWNEROFBANK = "SELECT OWNER FROM BANKS WHERE WURMID=?";
/*  54 */   private static final Logger logger = Logger.getLogger(Banks.class.getName());
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
/*     */   private static final void addBank(Bank bank) {
/*  73 */     banks.put(new Long(bank.owner), bank);
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
/*     */   public static final Bank getBank(long owner) {
/*  85 */     Bank bank = banks.get(new Long(owner));
/*  86 */     return bank;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int getNumberOfBanks() {
/*  97 */     return banks.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void poll(long now) {
/* 107 */     if (banks != null && !banks.isEmpty()) {
/*     */ 
/*     */ 
/*     */       
/* 111 */       boolean MULTI_THREADED_BANK_POLL = false;
/* 112 */       int NUMBER_OF_BANK_POLL_TASKS = 10;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 120 */       for (Bank bank : banks.values())
/*     */       {
/* 122 */         bank.poll(now);
/*     */       
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 128 */       logger.log(Level.FINE, "No banks to poll");
/*     */     } 
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
/*     */   public static boolean startBank(long owner, int size, int currentVillage) {
/* 149 */     if (banks.containsKey(new Long(owner)))
/*     */     {
/* 151 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 155 */     Bank bank = new Bank(owner, size, currentVillage);
/* 156 */     addBank(bank);
/* 157 */     return true;
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
/*     */   public static void loadAllBanks() {
/* 170 */     Connection dbcon = null;
/* 171 */     PreparedStatement ps = null;
/* 172 */     ResultSet rs = null;
/* 173 */     int loadedBanks = 0;
/* 174 */     long start = System.nanoTime();
/*     */ 
/*     */     
/*     */     try {
/* 178 */       dbcon = DbConnector.getEconomyDbCon();
/* 179 */       ps = dbcon.prepareStatement("SELECT * FROM BANKS");
/* 180 */       rs = ps.executeQuery();
/*     */       
/* 182 */       while (rs.next())
/*     */       {
/* 184 */         long wurmid = rs.getLong("WURMID");
/* 185 */         long owner = rs.getLong("OWNER");
/* 186 */         long lastpolled = rs.getLong("LASTPOLLED");
/* 187 */         long startedMove = rs.getLong("STARTEDMOVE");
/* 188 */         int size = rs.getInt("SIZE");
/* 189 */         int currentVillage = rs.getInt("CURRENTVILLAGE");
/* 190 */         int targetVillage = rs.getInt("TARGETVILLAGE");
/* 191 */         addBank(new Bank(wurmid, owner, size, lastpolled, startedMove, currentVillage, targetVillage));
/* 192 */         loadedBanks++;
/*     */       }
/*     */     
/* 195 */     } catch (SQLException sqx) {
/*     */       
/* 197 */       logger.log(Level.WARNING, "Failed to load banks, SqlState: " + sqx.getSQLState() + ", ErrorCode: " + sqx.getErrorCode(), sqx);
/* 198 */       Exception lNext = sqx.getNextException();
/* 199 */       if (lNext != null)
/*     */       {
/* 201 */         logger.log(Level.WARNING, "Failed to load banks, Next Exception", lNext);
/*     */       }
/*     */     }
/*     */     finally {
/*     */       
/* 206 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 207 */       DbConnector.returnConnection(dbcon);
/*     */       
/* 209 */       long end = System.nanoTime();
/* 210 */       logger.info("Loaded " + loadedBanks + " banks from database took " + ((float)(end - start) / 1000000.0F) + " ms");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long itemInBank(long itemID) {
/* 222 */     long inBank = 0L;
/*     */ 
/*     */     
/* 225 */     Connection dbcon = null;
/* 226 */     PreparedStatement ps = null;
/* 227 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 230 */       dbcon = DbConnector.getEconomyDbCon();
/* 231 */       ps = dbcon.prepareStatement("SELECT BANKID FROM BANKS_ITEMS WHERE ITEMID=?");
/* 232 */       ps.setLong(1, itemID);
/* 233 */       rs = ps.executeQuery();
/* 234 */       while (rs.next()) {
/* 235 */         inBank = rs.getLong("BANKID");
/*     */       }
/*     */     }
/* 238 */     catch (SQLException sqx) {
/*     */       
/* 240 */       logger.log(Level.WARNING, "Failed execute ISBANKED, SqlState: " + sqx
/* 241 */           .getSQLState() + ", ErrorCode: " + sqx.getErrorCode(), sqx);
/* 242 */       Exception lNext = sqx.getNextException();
/* 243 */       if (lNext != null)
/*     */       {
/* 245 */         logger.log(Level.WARNING, "Failed to execute ISBANKED, Next Exception", lNext);
/*     */       }
/*     */     }
/*     */     finally {
/*     */       
/* 250 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 251 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */     
/* 254 */     return inBank;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final boolean isItemBanked(long itemID) {
/* 259 */     return !(itemInBank(itemID) == 0L);
/*     */   }
/*     */ 
/*     */   
/*     */   public static final long ownerOfBank(long bankID) {
/* 264 */     long ownerid = -10L;
/*     */ 
/*     */ 
/*     */     
/* 268 */     Connection dbcon = null;
/* 269 */     PreparedStatement ps = null;
/* 270 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 273 */       dbcon = DbConnector.getEconomyDbCon();
/* 274 */       ps = dbcon.prepareStatement("SELECT OWNER FROM BANKS WHERE WURMID=?");
/* 275 */       ps.setLong(1, bankID);
/* 276 */       rs = ps.executeQuery();
/* 277 */       while (rs.next()) {
/* 278 */         ownerid = rs.getLong("OWNER");
/*     */       }
/*     */     }
/* 281 */     catch (SQLException sqx) {
/*     */       
/* 283 */       logger.log(Level.WARNING, "Failed execute ISBANKED, SqlState: " + sqx
/* 284 */           .getSQLState() + ", ErrorCode: " + sqx.getErrorCode(), sqx);
/* 285 */       Exception lNext = sqx.getNextException();
/* 286 */       if (lNext != null)
/*     */       {
/* 288 */         logger.log(Level.WARNING, "Failed to execute ISBANKED, Next Exception", lNext);
/*     */       }
/*     */     }
/*     */     finally {
/*     */       
/* 293 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 294 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */ 
/*     */     
/* 298 */     return ownerid;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\banks\Banks.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */