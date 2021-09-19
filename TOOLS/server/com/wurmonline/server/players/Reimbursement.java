/*     */ package com.wurmonline.server.players;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.economy.MonetaryConstants;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.io.IOException;
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
/*     */ 
/*     */ public final class Reimbursement
/*     */   implements MonetaryConstants
/*     */ {
/*  43 */   private String name = "";
/*  44 */   private String email = "";
/*  45 */   private String paypalEmail = "";
/*     */   
/*  47 */   private int daysLeft = 0;
/*  48 */   private int months = 0;
/*  49 */   private int silver = 0;
/*     */   private boolean titleAndBok = false;
/*     */   private boolean mBok = false;
/*  52 */   private static Logger reimblogger = Logger.getLogger("Reimbursements");
/*     */   
/*  54 */   private static final Logger logger = Logger.getLogger(Reimbursement.class.getName());
/*  55 */   private static Map<String, Reimbursement> reimbursements = new HashMap<>();
/*     */   
/*     */   private boolean deleted = false;
/*     */   
/*     */   private static final String LOAD_REIMB = "SELECT * FROM REIMB WHERE REIMBURSED=0";
/*     */   
/*     */   private static final String LOAD_SPECREIMB = "SELECT * FROM REIMB WHERE NAME=?";
/*     */   
/*     */   private static final String SET_REIMB = "UPDATE REIMB SET MONTHS=?, SILVER=?,TITLEBOK=?,DAYSLEFT=? WHERE NAME=?";
/*     */   
/*     */   private static final String UPDATE_REIMB = "UPDATE REIMB SET MONTHS=?, SILVER=?,TITLEBOK=?,DAYSLEFT=?, REIMBURSED=0 WHERE NAME=?";
/*     */   
/*     */   private static final String DELETE_REIMB = "UPDATE REIMB SET REIMBURSED=1 WHERE NAME=?";
/*     */   
/*     */   public static final String NOREIMBS = "text{text='You have no reimbursements pending.'}";
/*     */   
/*     */   public static final String nameString = "Name=";
/*     */   
/*     */   public static final String nameEndString = " - '}";
/*     */   
/*     */   public static final String keySilver = "silver";
/*     */   public static final String keyDays = "days";
/*     */   public static final String keyBok = "bok";
/*     */   public static final String keyMBok = "mbok";
/*     */   public static final String keyTrinket = "trinket";
/*     */   
/*     */   public static void loadAll() throws IOException {
/*  82 */     long start = System.nanoTime();
/*  83 */     Connection dbcon = null;
/*  84 */     PreparedStatement ps = null;
/*  85 */     ResultSet rs = null;
/*  86 */     if (Servers.localServer.id == Servers.loginServer.id) {
/*     */       
/*     */       try
/*     */       {
/*  90 */         reimbursements = new HashMap<>();
/*  91 */         dbcon = DbConnector.getPlayerDbCon();
/*  92 */         ps = dbcon.prepareStatement("SELECT * FROM REIMB WHERE REIMBURSED=0");
/*     */         
/*  94 */         rs = ps.executeQuery();
/*  95 */         while (rs.next())
/*     */         {
/*  97 */           Reimbursement r = new Reimbursement();
/*  98 */           r.name = rs.getString("NAME");
/*  99 */           r.email = rs.getString("EMAIL");
/* 100 */           r.paypalEmail = rs.getString("PAYPALEMAIL");
/* 101 */           r.months = rs.getInt("MONTHS");
/* 102 */           r.daysLeft = rs.getInt("DAYSLEFT");
/* 103 */           r.silver = rs.getInt("SILVER");
/* 104 */           r.titleAndBok = rs.getBoolean("TITLEBOK");
/* 105 */           r.mBok = rs.getBoolean("MBOK");
/* 106 */           reimbursements.put(r.name, r);
/*     */         
/*     */         }
/*     */       
/*     */       }
/* 111 */       catch (SQLException sqex)
/*     */       {
/* 113 */         logger.log(Level.WARNING, sqex.getMessage(), sqex);
/* 114 */         throw new IOException(sqex);
/*     */       }
/*     */       finally
/*     */       {
/* 118 */         DbUtilities.closeDatabaseObjects(ps, rs);
/* 119 */         DbConnector.returnConnection(dbcon);
/* 120 */         long end = System.nanoTime();
/* 121 */         logger.info("Loaded " + reimbursements.size() + " reimbursements from the database took " + ((float)(end - start) / 1000000.0F) + " ms");
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 127 */       logger.info("Did not load reimbursements from the database as this is not the login server, which has id: " + Servers.loginServer.id);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void loadReimb(String name) throws IOException {
/* 134 */     if (Servers.localServer.id == Servers.loginServer.id) {
/*     */       
/* 136 */       Connection dbcon = null;
/* 137 */       PreparedStatement ps = null;
/* 138 */       ResultSet rs = null;
/*     */       
/*     */       try {
/* 141 */         dbcon = DbConnector.getPlayerDbCon();
/* 142 */         ps = dbcon.prepareStatement("SELECT * FROM REIMB WHERE NAME=?");
/* 143 */         ps.setString(1, name);
/* 144 */         rs = ps.executeQuery();
/* 145 */         if (rs.next())
/*     */         {
/* 147 */           Reimbursement r = new Reimbursement();
/* 148 */           r.name = rs.getString("NAME");
/* 149 */           r.email = rs.getString("EMAIL");
/* 150 */           r.paypalEmail = rs.getString("PAYPALEMAIL");
/* 151 */           r.months = rs.getInt("MONTHS");
/* 152 */           r.daysLeft = rs.getInt("DAYSLEFT");
/* 153 */           r.silver = rs.getInt("SILVER");
/* 154 */           r.titleAndBok = rs.getBoolean("TITLEBOK");
/* 155 */           r.mBok = rs.getBoolean("MBOK");
/* 156 */           reimbursements.put(r.name, r);
/* 157 */           logger.log(Level.INFO, "Found " + r.name + ": " + r.silver + "s, " + r.months + "m, " + r.daysLeft + "d, bok=" + r.titleAndBok + ", mbok=" + r.mBok + ".");
/*     */         }
/*     */       
/*     */       }
/* 161 */       catch (SQLException sqex) {
/*     */         
/* 163 */         logger.log(Level.WARNING, sqex.getMessage(), sqex);
/* 164 */         throw new IOException(sqex);
/*     */       }
/*     */       finally {
/*     */         
/* 168 */         DbUtilities.closeDatabaseObjects(ps, rs);
/* 169 */         DbConnector.returnConnection(dbcon);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 174 */       logger.info("Did not load reimbursement " + name + " from the database as this is not the login server, which has id: " + Servers.loginServer.id);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String addReimb(String changerName, String name, int numMonths, int _silver, int _daysLeft, boolean setbok) {
/* 182 */     Reimbursement r = reimbursements.get(name);
/* 183 */     if (r == null) {
/*     */       
/*     */       try {
/*     */         
/* 187 */         loadReimb(name);
/* 188 */         r = reimbursements.get(name);
/*     */       }
/* 190 */       catch (IOException iox) {
/*     */         
/* 192 */         logger.log(Level.WARNING, Servers.localServer.name + " - error " + iox.getMessage(), iox);
/* 193 */         return Servers.localServer.name + " - error " + iox.getMessage();
/*     */       } 
/*     */     }
/* 196 */     if (r != null) {
/*     */       
/* 198 */       if (r.deleted) {
/*     */         
/* 200 */         r.months = 0;
/* 201 */         r.silver = 0;
/* 202 */         r.titleAndBok = false;
/* 203 */         r.daysLeft = 0;
/*     */       } 
/* 205 */       r.months += numMonths;
/* 206 */       r.silver += _silver;
/* 207 */       r.daysLeft += _daysLeft;
/* 208 */       if (!r.titleAndBok)
/* 209 */         r.titleAndBok = setbok; 
/* 210 */       reimblogger.log(Level.INFO, changerName + " added to " + name + ": " + numMonths + " m, " + _silver + " s, " + _daysLeft + " days, bok=" + setbok + ".");
/*     */ 
/*     */       
/*     */       try {
/* 214 */         r.update();
/*     */       }
/* 216 */       catch (IOException ex) {
/*     */         
/* 218 */         logger.log(Level.WARNING, ex.getMessage(), ex);
/* 219 */         return Servers.localServer.name + " - Error - problem saving to database. Player was awarded though and may withdraw anyway. " + ex
/*     */           
/* 221 */           .getMessage();
/*     */       } 
/* 223 */       return Servers.localServer.name + " - ok. " + name + " now has " + r.months + "m, " + r.silver + "s, " + r.daysLeft + "d, bok=" + r.titleAndBok + "\n";
/*     */     } 
/*     */     
/* 226 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean withDraw(String retriever, String name, String _email, int _months, int _silvers, boolean titlebok, int _daysLeft) {
/* 233 */     Reimbursement r = reimbursements.get(name);
/* 234 */     if (r != null)
/*     */     {
/* 236 */       if (r.email.toLowerCase().equals(_email.toLowerCase())) {
/*     */         
/* 238 */         if (r.withDraw(retriever, _months, _silvers, titlebok, _daysLeft))
/*     */         {
/* 240 */           reimblogger.log(Level.INFO, retriever + " withdrew from " + name + " " + _months + " months, " + _silvers + " silver, " + _daysLeft + " days" + (titlebok ? " and the title and bok" : "."));
/*     */ 
/*     */           
/* 243 */           return true;
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 248 */         logger.log(Level.WARNING, name + " does not match email: " + r.email.toLowerCase() + " with submitted email " + _email
/* 249 */             .toLowerCase());
/*     */       } 
/*     */     }
/* 252 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean awardPlayerSilver(String name, int silver) {
/* 257 */     PlayerInfo pinf = PlayerInfoFactory.createPlayerInfo(name);
/*     */     
/*     */     try {
/* 260 */       pinf.load();
/*     */     }
/* 262 */     catch (IOException iox) {
/*     */       
/* 264 */       logger.log(Level.WARNING, name + ", " + iox.getMessage(), iox);
/* 265 */       return false;
/*     */     } 
/* 267 */     if (pinf != null)
/*     */     {
/* 269 */       if (silver > 0) {
/*     */         
/*     */         try {
/*     */           
/* 273 */           PlayerInfoFactory.addMoneyToBank(pinf.wurmId, (10000 * silver), "Reimbursed " + pinf.getName());
/* 274 */           return true;
/*     */         }
/* 276 */         catch (Exception iox) {
/*     */           
/* 278 */           logger.log(Level.WARNING, name + ", silver=" + silver + "," + iox.getMessage(), iox);
/*     */         } 
/*     */       }
/*     */     }
/* 282 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean awardPlayerDays(String name, int days) {
/* 287 */     PlayerInfo pinf = PlayerInfoFactory.createPlayerInfo(name);
/*     */     
/*     */     try {
/* 290 */       pinf.load();
/*     */     }
/* 292 */     catch (IOException iox) {
/*     */       
/* 294 */       logger.log(Level.WARNING, name + ", " + iox.getMessage(), iox);
/* 295 */       return false;
/*     */     } 
/* 297 */     if (pinf != null)
/*     */     {
/* 299 */       if (days > 0) {
/*     */         
/*     */         try {
/*     */           
/* 303 */           PlayerInfoFactory.addPlayingTime(pinf.wurmId, 0, days, "Reimbursed " + pinf.getName());
/* 304 */           return true;
/*     */         }
/* 306 */         catch (Exception iox) {
/*     */           
/* 308 */           logger.log(Level.WARNING, name + ", days=" + days + "," + iox.getMessage(), iox);
/*     */         } 
/*     */       }
/*     */     }
/* 312 */     return false;
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
/*     */   private boolean withDraw(String aName, int _months, int _silvers, boolean titlebok, int _daysLeft) {
/* 325 */     logger.log(Level.INFO, aName + " Withdrawing " + _months + "m, " + _silvers + "s, bok=" + titlebok + ", " + _daysLeft + "d.");
/*     */     
/* 327 */     boolean awardedSilver = false;
/* 328 */     boolean awardedDays = false;
/* 329 */     if (!this.deleted)
/*     */     {
/* 331 */       if (_months > 0 || _silvers > 0 || titlebok || _daysLeft > 0) {
/*     */         
/* 333 */         if (_months < 0 || _silvers < 0 || _daysLeft < 0)
/* 334 */           return false; 
/* 335 */         if (_months > this.months || _silvers > this.silver || (titlebok == true && !this.titleAndBok) || _daysLeft > this.daysLeft) {
/* 336 */           return false;
/*     */         }
/*     */         
/* 339 */         _months = Math.min(5, _months);
/* 340 */         this.months -= _months;
/* 341 */         if (_silvers > 0)
/*     */         {
/* 343 */           if (awardPlayerSilver(aName, _silvers)) {
/*     */             
/* 345 */             awardedSilver = true;
/* 346 */             this.silver -= _silvers;
/*     */           } else {
/*     */             
/* 349 */             return false;
/*     */           }  } 
/* 351 */         if (_daysLeft > 0) {
/*     */           
/* 353 */           if (this.daysLeft < 30) {
/* 354 */             _daysLeft = this.daysLeft;
/*     */           } else {
/* 356 */             _daysLeft = Math.max(_daysLeft, 30);
/* 357 */           }  if (awardPlayerDays(aName, _daysLeft)) {
/*     */             
/* 359 */             awardedDays = true;
/* 360 */             this.daysLeft -= _daysLeft;
/*     */           } else {
/*     */             
/* 363 */             return false;
/*     */           } 
/* 365 */         }  if (titlebok)
/*     */         {
/* 367 */           this.titleAndBok = false;
/*     */         }
/* 369 */         if (!this.titleAndBok && this.months == 0 && this.silver == 0 && this.daysLeft == 0) {
/*     */ 
/*     */           
/*     */           try {
/* 373 */             delete();
/*     */           }
/* 375 */           catch (IOException iox) {
/*     */             
/* 377 */             if (titlebok)
/*     */             {
/* 379 */               this.titleAndBok = true;
/*     */             }
/* 381 */             this.months += _months;
/* 382 */             if (!awardedSilver)
/* 383 */               this.silver += _silvers; 
/* 384 */             if (!awardedDays)
/* 385 */               this.daysLeft += _daysLeft; 
/* 386 */             return false;
/*     */           } 
/*     */         } else {
/*     */ 
/*     */           
/*     */           try {
/*     */             
/* 393 */             save();
/*     */           }
/* 395 */           catch (IOException iox) {
/*     */             
/* 397 */             if (titlebok)
/* 398 */               this.titleAndBok = true; 
/* 399 */             this.months += _months;
/* 400 */             if (!awardedSilver)
/* 401 */               this.silver += _silvers; 
/* 402 */             if (!awardedDays)
/* 403 */               this.daysLeft += _daysLeft; 
/* 404 */             return false;
/*     */           } 
/*     */         } 
/*     */         
/* 408 */         return true;
/*     */       } 
/*     */     }
/* 411 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private void save() throws IOException {
/* 416 */     Connection dbcon = null;
/* 417 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 420 */       dbcon = DbConnector.getPlayerDbCon();
/* 421 */       ps = dbcon.prepareStatement("UPDATE REIMB SET MONTHS=?, SILVER=?,TITLEBOK=?,DAYSLEFT=? WHERE NAME=?");
/* 422 */       ps.setInt(1, this.months);
/* 423 */       ps.setInt(2, this.silver);
/*     */       
/* 425 */       ps.setBoolean(3, this.titleAndBok);
/* 426 */       ps.setInt(4, this.daysLeft);
/* 427 */       ps.setString(5, this.name);
/* 428 */       ps.executeUpdate();
/*     */     }
/* 430 */     catch (SQLException ex) {
/*     */       
/* 432 */       logger.log(Level.WARNING, "Failed to set reimbursed for " + this.name, ex);
/* 433 */       throw new IOException(ex);
/*     */     }
/*     */     finally {
/*     */       
/* 437 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 438 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void update() throws IOException {
/* 444 */     Connection dbcon = null;
/* 445 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 448 */       dbcon = DbConnector.getPlayerDbCon();
/* 449 */       ps = dbcon.prepareStatement("UPDATE REIMB SET MONTHS=?, SILVER=?,TITLEBOK=?,DAYSLEFT=?, REIMBURSED=0 WHERE NAME=?");
/* 450 */       ps.setInt(1, this.months);
/* 451 */       ps.setInt(2, this.silver);
/*     */       
/* 453 */       ps.setBoolean(3, this.titleAndBok);
/* 454 */       ps.setInt(4, this.daysLeft);
/* 455 */       ps.setString(5, this.name);
/* 456 */       ps.executeUpdate();
/*     */     }
/* 458 */     catch (SQLException ex) {
/*     */       
/* 460 */       logger.log(Level.WARNING, "Failed to set reimbursed for " + this.name, ex);
/* 461 */       throw new IOException(ex);
/*     */     }
/*     */     finally {
/*     */       
/* 465 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 466 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void delete() throws IOException {
/* 472 */     Connection dbcon = null;
/* 473 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 476 */       dbcon = DbConnector.getPlayerDbCon();
/* 477 */       ps = dbcon.prepareStatement("UPDATE REIMB SET REIMBURSED=1 WHERE NAME=?");
/*     */       
/* 479 */       ps.setString(1, this.name);
/* 480 */       ps.executeUpdate();
/* 481 */       this.deleted = true;
/* 482 */       reimblogger.log(Level.INFO, this.name + " Reimbursements unavailable.");
/*     */     }
/* 484 */     catch (SQLException ex) {
/*     */       
/* 486 */       logger.log(Level.WARNING, "Failed to delete reimbursed for " + this.name, ex);
/* 487 */       throw new IOException(ex);
/*     */     }
/*     */     finally {
/*     */       
/* 491 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 492 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static Reimbursement[] getReimbsForEmail(String _email) {
/* 498 */     Set<Reimbursement> set = new HashSet<>();
/* 499 */     for (Iterator<Reimbursement> it = reimbursements.values().iterator(); it.hasNext(); ) {
/*     */       
/* 501 */       Reimbursement r = it.next();
/* 502 */       if (!r.deleted && r.email.toLowerCase().equals(_email))
/*     */       {
/* 504 */         if (r.months > 0 || r.silver > 0 || r.titleAndBok || r.daysLeft > 0)
/* 505 */           set.add(r); 
/*     */       }
/*     */     } 
/* 508 */     return set.<Reimbursement>toArray(new Reimbursement[set.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getReimbursementInfo(String _email) {
/* 513 */     if (_email.length() == 0) {
/*     */       
/* 515 */       logger.log(Level.WARNING, "Cannot get reimbs for a player with an empty email");
/* 516 */       return "text{text='You have no reimbursements pending.'}";
/*     */     } 
/* 518 */     Reimbursement[] reimbs = getReimbsForEmail(_email.toLowerCase());
/* 519 */     logger.log(Level.INFO, "Trying to get reimbs for " + _email);
/* 520 */     if (reimbs.length > 0) {
/*     */       
/* 522 */       StringBuilder buf = new StringBuilder();
/* 523 */       buf.append("text{text='Read this carefully:'}text{text=''}");
/* 524 */       buf.append("text{text='If you have trinkets to withdraw, what you get will _depend on how many trinkets you withdraw_.'}text{text=''}");
/* 525 */       buf.append("text{text='If you withdraw 1 trinket many times you will end up with a lot of spyglasses.'}");
/* 526 */       buf.append("text{text='If you withdraw 2 trinkets many times you will end up with a lot of spyglasses and a lot of basic tools.'}");
/* 527 */       buf.append("text{text='This is the trinket ladder:'}");
/* 528 */       buf.append("text{text='1 trinket: Spyglass.'}");
/* 529 */       buf.append("text{text='2 trinkets: 1 Resurrection stone, Basic tools QL 30 and the above.'}");
/* 530 */       buf.append("text{text='3 trinkets: QL 30 full leather armour and ql 30 medium metal shield and the above.'}");
/* 531 */       buf.append("text{text='4 trinkets: QL 50 lantern and ql 70 compass and the above.'}");
/* 532 */       buf.append("text{text='5 trinkets: 3 resurrection stones and the above.'}");
/* 533 */       buf.append("text{text='You will not withdraw more than 5 trinkets at a time.'}");
/* 534 */       buf
/* 535 */         .append("text{type='italic';text='Note that within a few months the kingdom of Mol-Rehan may emerge. You may want to save some if you want to try a MR character.'}");
/* 536 */       buf.append("text{type='italic';text='If you have premium days left, you will withdraw at a minimum 30 days no matter what you type in the box.'}");
/* 537 */       buf.append("text{text='You have the following reimbursements available (you will withdraw the amount in the textbox):'}text{text=''}");
/* 538 */       for (int x = 0; x < reimbs.length; x++) {
/*     */         
/* 540 */         buf.append("text{type='bold';text='Name=" + (reimbs[x]).name + " - '}");
/* 541 */         if ((reimbs[x]).months > 0) {
/* 542 */           buf.append("harray{label{text='Trinkets:'};input{id='trinket" + (reimbs[x]).name + "'; text='" + (reimbs[x]).months + "'; maxchars='2'}}");
/*     */         }
/* 544 */         if ((reimbs[x]).silver > 0) {
/* 545 */           buf.append("harray{label{text='Silver:'};input{id='silver" + (reimbs[x]).name + "'; text='" + (reimbs[x]).silver + "'; maxchars='3'}}");
/*     */         }
/* 547 */         if ((reimbs[x]).daysLeft > 0) {
/*     */           
/* 549 */           buf.append("harray{label{text='Premium days:'};input{id='days" + (reimbs[x]).name + "'; text='" + (reimbs[x]).daysLeft + "'; maxchars='3'}}");
/*     */           
/* 551 */           if ((reimbs[x]).daysLeft > 30) {
/* 552 */             buf.append("harray{label{text='(Withdraw 0 or minimum 30!)'}}");
/*     */           } else {
/* 554 */             buf.append("harray{label{text='(Withdraw 0 or minimum " + (reimbs[x]).daysLeft + ")!'}}");
/*     */           } 
/* 556 */         }  if ((reimbs[x]).titleAndBok && (reimbs[x]).mBok) {
/* 557 */           buf.append("harray{label{text='Titles+MBoK:'};checkbox{id='mbok" + (reimbs[x]).name + "';text='Mark this if you want to retrieve the Ageless and Keeper titles and the Master BoK'}}");
/*     */         }
/* 559 */         else if ((reimbs[x]).titleAndBok) {
/* 560 */           buf.append("harray{label{text='Title+BoK:'};checkbox{id='bok" + (reimbs[x]).name + "';text='Mark this if you want to retrieve the Ageless title and the BoK'}}");
/*     */         } 
/* 562 */         buf.append("label{text='__________________________'};");
/*     */       } 
/* 564 */       return buf.toString();
/*     */     } 
/*     */     
/* 567 */     return "text{text='You have no reimbursements pending.'}";
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\Reimbursement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */