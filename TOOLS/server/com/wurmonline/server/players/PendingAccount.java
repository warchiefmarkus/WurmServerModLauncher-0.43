/*     */ package com.wurmonline.server.players;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.GeneralUtilities;
/*     */ import com.wurmonline.server.Mailer;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import com.wurmonline.server.webinterface.WebInterfaceImpl;
/*     */ import java.io.IOException;
/*     */ import java.net.URLEncoder;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PendingAccount
/*     */ {
/*     */   private static final String GET_ALL_PENDING_ACCOUNTS = "SELECT * FROM PENDINGACCOUNTS";
/*     */   private static final String CREATE_PENDING_ACCOUNT = "INSERT INTO PENDINGACCOUNTS(NAME,EMAIL,EXPIRATIONDATE,HASH) VALUES(?,?,?,?)";
/*     */   private static final String DELETE_PENDING_ACCOUNT = "DELETE FROM PENDINGACCOUNTS WHERE NAME=?";
/*  46 */   private static final Logger logger = Logger.getLogger(PendingAccount.class.getName());
/*     */   
/*  48 */   public static final Map<String, PendingAccount> accounts = new HashMap<>();
/*     */   
/*  50 */   public String accountName = "Unknown";
/*  51 */   public String emailAddress = "";
/*  52 */   public long expiration = 0L;
/*  53 */   public String password = "";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void loadAllPendingAccounts() throws IOException {
/*  61 */     long start = System.nanoTime();
/*  62 */     Connection dbcon = null;
/*  63 */     PreparedStatement ps = null;
/*  64 */     ResultSet rs = null;
/*     */     
/*     */     try {
/*  67 */       dbcon = DbConnector.getLoginDbCon();
/*  68 */       ps = dbcon.prepareStatement("SELECT * FROM PENDINGACCOUNTS");
/*  69 */       rs = ps.executeQuery();
/*  70 */       while (rs.next())
/*     */       {
/*  72 */         PendingAccount pacc = new PendingAccount();
/*  73 */         pacc.accountName = rs.getString("NAME");
/*  74 */         pacc.emailAddress = rs.getString("EMAIL");
/*  75 */         pacc.expiration = rs.getLong("EXPIRATIONDATE");
/*  76 */         pacc.password = rs.getString("HASH");
/*  77 */         if (System.currentTimeMillis() > pacc.expiration) {
/*  78 */           pacc.delete(dbcon); continue;
/*     */         } 
/*  80 */         addPendingAccount(pacc);
/*     */       }
/*     */     
/*  83 */     } catch (SQLException sqex) {
/*     */       
/*  85 */       logger.log(Level.WARNING, sqex.getMessage(), sqex);
/*  86 */       throw new IOException(sqex);
/*     */     }
/*     */     finally {
/*     */       
/*  90 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*  91 */       DbConnector.returnConnection(dbcon);
/*  92 */       long end = System.nanoTime();
/*  93 */       logger.info("Loaded " + accounts.size() + " pending accounts from the database took " + ((float)(end - start) / 1000000.0F) + " ms");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void delete() {
/* 100 */     Connection dbcon = null;
/*     */     
/*     */     try {
/* 103 */       dbcon = DbConnector.getLoginDbCon();
/* 104 */       delete(dbcon);
/*     */     }
/* 106 */     catch (SQLException sqex) {
/*     */       
/* 108 */       logger.log(Level.WARNING, "Failed to delete pending account " + this.accountName, sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 112 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void delete(Connection dbcon) {
/* 118 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 121 */       ps = dbcon.prepareStatement("DELETE FROM PENDINGACCOUNTS WHERE NAME=?");
/* 122 */       ps.setString(1, this.accountName);
/* 123 */       ps.executeUpdate();
/*     */     }
/* 125 */     catch (SQLException sqex) {
/*     */       
/* 127 */       logger.log(Level.WARNING, "Failed to delete pending account " + this.accountName, sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 131 */       DbUtilities.closeDatabaseObjects(ps, null);
/*     */     } 
/* 133 */     accounts.remove(this.accountName);
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean addPendingAccount(PendingAccount acc) {
/* 138 */     if (accounts.containsKey(acc.accountName)) {
/* 139 */       return false;
/*     */     }
/* 141 */     accounts.put(acc.accountName, acc);
/* 142 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean create() {
/* 147 */     Connection dbcon = null;
/* 148 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 151 */       dbcon = DbConnector.getLoginDbCon();
/* 152 */       ps = dbcon.prepareStatement("INSERT INTO PENDINGACCOUNTS(NAME,EMAIL,EXPIRATIONDATE,HASH) VALUES(?,?,?,?)");
/* 153 */       ps.setString(1, this.accountName);
/* 154 */       ps.setString(2, this.emailAddress);
/* 155 */       ps.setLong(3, this.expiration);
/* 156 */       ps.setString(4, this.password);
/* 157 */       ps.executeUpdate();
/*     */     }
/* 159 */     catch (SQLException sqex) {
/*     */       
/* 161 */       logger.log(Level.WARNING, "Failed to add pending account " + this.accountName, sqex);
/* 162 */       return false;
/*     */     }
/*     */     finally {
/*     */       
/* 166 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 167 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/* 169 */     return addPendingAccount(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean doesPlayerExist(String name) {
/* 174 */     return accounts.containsKey(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void poll() {
/* 179 */     Connection dbcon = null;
/*     */     
/*     */     try {
/* 182 */       dbcon = DbConnector.getLoginDbCon();
/* 183 */       PendingAccount[] paddarr = (PendingAccount[])accounts.values().toArray((Object[])new PendingAccount[accounts.size()]);
/* 184 */       for (int x = 0; x < paddarr.length; x++) {
/*     */         
/* 186 */         if ((paddarr[x]).expiration < System.currentTimeMillis()) {
/* 187 */           paddarr[x].delete(dbcon);
/*     */         }
/*     */       } 
/* 190 */     } catch (SQLException sqx) {
/*     */       
/* 192 */       logger.log(Level.WARNING, "Failed to delete pending accounts. " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 196 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static String[] getAccountsForEmail(String email) {
/* 202 */     Set<String> set = new HashSet<>();
/* 203 */     for (Iterator<PendingAccount> it = accounts.values().iterator(); it.hasNext(); ) {
/*     */       
/* 205 */       PendingAccount info = it.next();
/* 206 */       if (info.emailAddress.equals(email))
/* 207 */         set.add(info.accountName); 
/*     */     } 
/* 209 */     return set.<String>toArray(new String[set.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public static PendingAccount getAccount(String name) {
/* 214 */     return accounts.get(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void resendMails(String contains) {
/* 219 */     PendingAccount[] paddarr = (PendingAccount[])accounts.values().toArray((Object[])new PendingAccount[accounts.size()]);
/* 220 */     for (int x = 0; x < paddarr.length; x++) {
/*     */       
/* 222 */       if (contains == null || (paddarr[x]).emailAddress.contains(contains))
/*     */         
/*     */         try {
/* 225 */           String email = Mailer.getPhaseOneMail();
/* 226 */           email = email.replace("@pname", (paddarr[x]).accountName);
/* 227 */           email = email.replace("@email", URLEncoder.encode((paddarr[x]).emailAddress, "UTF-8"));
/* 228 */           email = email.replace("@expiration", GeneralUtilities.toGMTString((paddarr[x]).expiration));
/* 229 */           email = email.replace("@password", (paddarr[x]).password);
/*     */           
/* 231 */           Mailer.sendMail(WebInterfaceImpl.mailAccount, (paddarr[x]).emailAddress, "Wurm Online character creation request", email);
/*     */           
/* 233 */           logger.log(Level.INFO, "Resent " + (paddarr[x]).emailAddress + " for " + (paddarr[x]).accountName);
/*     */         }
/* 235 */         catch (Exception exception) {} 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\PendingAccount.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */