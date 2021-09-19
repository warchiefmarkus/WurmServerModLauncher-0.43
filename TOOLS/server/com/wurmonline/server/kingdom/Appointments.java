/*     */ package com.wurmonline.server.kingdom;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import com.wurmonline.server.zones.Zones;
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
/*     */ public class Appointments
/*     */   implements MiscConstants, TimeConstants
/*     */ {
/*  42 */   private static final Logger logger = Logger.getLogger(Appointments.class.getName());
/*  43 */   byte kingdom = 0;
/*     */   private final int era;
/*  45 */   static Appointments jenn = null;
/*  46 */   static Appointments hots = null;
/*  47 */   static Appointments molr = null;
/*  48 */   static Appointments none = null;
/*     */   
/*     */   private static final String CREATE_APPOINTMENTS = "insert into APPOINTMENTS ( ERA,KINGDOM, LASTCHECKED ) VALUES(?,?,?)";
/*     */   
/*     */   private static final String CREATE_OFFICES = "insert into OFFICES ( ERA ) VALUES(?)";
/*     */   
/*     */   private static final String RESET_APPOINTMENTS = "DELETE FROM APPOINTMENTS WHERE ERA=?";
/*     */   
/*     */   private static final String RESET_OFFICES = "DELETE FROM OFFICES WHERE ERA=?";
/*     */   private static final String GET_APPOINTMENTS = "select * FROM APPOINTMENTS WHERE ERA=?";
/*     */   private static final String GET_OFFICES = "select * FROM OFFICES WHERE ERA=?";
/*  59 */   private final Map<Integer, Appointment> appointments = new HashMap<>();
/*     */   
/*  61 */   private static final Map<Integer, Appointments> allAppointments = new HashMap<>();
/*  62 */   long lastChecked = 0L;
/*     */   
/*     */   static final int title1 = 0;
/*     */   
/*     */   static final int title2 = 1;
/*     */   static final int title3 = 2;
/*     */   static final int title4 = 3;
/*     */   static final int title5 = 4;
/*     */   static final int title6 = 5;
/*     */   static final int title7 = 6;
/*     */   static final int title8 = 7;
/*     */   static final int title9 = 8;
/*  74 */   public final int[] availableTitles = new int[9];
/*  75 */   private static final String[] titleDBStrings = new String[9]; public static final int order1 = 30;
/*     */   
/*     */   static {
/*     */     int x;
/*  79 */     for (x = 0; x < titleDBStrings.length; x++)
/*     */     {
/*  81 */       titleDBStrings[x] = "UPDATE APPOINTMENTS SET TITLE" + (x + 1) + "=? WHERE ERA=?";
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static final int order2 = 31;
/*     */   
/*     */   public static final int order3 = 32;
/*     */   public static final int order4 = 33;
/*     */   public static final int order5 = 34;
/*     */   public static final int order6 = 35;
/*     */   private static final int numappointments = 36;
/*  93 */   public final int[] availableOrders = new int[6];
/*  94 */   private static final String[] orderDBStrings = new String[6];
/*     */   
/*     */   public static final int official1 = 1500;
/*     */   public static final int official2 = 1501;
/*     */   public static final int official3 = 1502;
/*     */   public static final int official4 = 1503;
/*     */   public static final int official5 = 1504;
/*     */   public static final int official6 = 1505;
/*     */   
/*     */   static {
/* 104 */     for (x = 0; x < orderDBStrings.length; x++)
/*     */     {
/* 106 */       orderDBStrings[x] = "UPDATE APPOINTMENTS SET ORDER" + (x + 1) + "=? WHERE ERA=?";
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
/*     */   public static final int official7 = 1506;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int official8 = 1507;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int official9 = 1508;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int official10 = 1509;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int official11 = 1510;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int numOfficials = 11;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 168 */   public long[] officials = new long[11];
/* 169 */   private boolean[] officesSet = new boolean[11];
/* 170 */   private static final String[] officialDBStrings = new String[11];
/* 171 */   private static final String[] officeDBStrings = new String[11];
/*     */   
/*     */   static {
/* 174 */     for (x = 0; x < officialDBStrings.length; x++) {
/*     */       
/* 176 */       officialDBStrings[x] = "UPDATE APPOINTMENTS SET OFFICIAL" + (x + 1) + "=? WHERE ERA=?";
/* 177 */       officeDBStrings[x] = "UPDATE OFFICES SET OFFICIAL" + (x + 1) + "=? WHERE ERA=?";
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   Appointments(int _era, byte kdom, boolean current) {
/* 183 */     this.era = _era;
/* 184 */     this.kingdom = kdom;
/* 185 */     if (current) {
/*     */       
/* 187 */       if (this.kingdom == 1 && jenn == null)
/* 188 */         jenn = this; 
/* 189 */       if (this.kingdom == 3 && hots == null)
/* 190 */         hots = this; 
/* 191 */       if (this.kingdom == 2 && molr == null)
/* 192 */         molr = this; 
/*     */     } 
/* 194 */     logger.log(Level.INFO, "Loading era " + this.era + " for kingdom " + this.kingdom + " current=" + current);
/* 195 */     loadAppointments();
/* 196 */     addAppointments(this.era, this);
/* 197 */     Appointment.setAppointments(this);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void addAppointments(int era, Appointments app) {
/* 202 */     allAppointments.put(Integer.valueOf(era), app);
/*     */   }
/*     */ 
/*     */   
/*     */   static Appointments getCurrentAppointments(byte _kingdom) {
/* 207 */     if (_kingdom == 1)
/* 208 */       return jenn; 
/* 209 */     if (_kingdom == 3)
/* 210 */       return hots; 
/* 211 */     if (_kingdom == 2)
/* 212 */       return molr; 
/* 213 */     return none;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Appointments getAppointments(int era) {
/* 218 */     return allAppointments.get(Integer.valueOf(era));
/*     */   }
/*     */ 
/*     */   
/*     */   void addAppointment(Appointment app) {
/* 223 */     this.appointments.put(Integer.valueOf(app.getId()), app);
/*     */   }
/*     */ 
/*     */   
/*     */   public Appointment getAppointment(int _id) {
/* 228 */     return this.appointments.get(Integer.valueOf(_id));
/*     */   }
/*     */ 
/*     */   
/*     */   public Appointment getFinestAppointment(long data, long wurmid) {
/* 233 */     int highestlevel = 0;
/* 234 */     Appointment highest = null;
/* 235 */     Appointment tempapp = null;
/*     */     int x;
/* 237 */     for (x = 0; x < 36; x++) {
/*     */       
/* 239 */       if ((data >> x & 0x1L) == 1L) {
/*     */         
/* 241 */         tempapp = getAppointment(x);
/* 242 */         if (tempapp.getLevel() > highestlevel) {
/*     */           
/* 244 */           highestlevel = tempapp.getLevel();
/* 245 */           highest = tempapp;
/*     */         } 
/*     */       } 
/*     */     } 
/* 249 */     for (x = 0; x < this.officials.length; x++) {
/*     */       
/* 251 */       if (this.officials[x] == wurmid) {
/*     */         
/* 253 */         tempapp = getAppointment(x + 1500);
/* 254 */         if (tempapp.getLevel() >= highestlevel) {
/*     */           
/* 256 */           highestlevel = tempapp.getLevel();
/* 257 */           highest = tempapp;
/*     */         } 
/*     */       } 
/*     */     } 
/* 261 */     return highest;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAppointmentLevels(long data, long wurmid) {
/* 266 */     int levels = 0;
/*     */     int x;
/* 268 */     for (x = 0; x < 36; x++) {
/*     */       
/* 270 */       if ((data >> x & 0x1L) == 1L) {
/*     */         
/* 272 */         Appointment tempapp = getAppointment(x);
/* 273 */         levels += tempapp.getLevel();
/*     */       } 
/*     */     } 
/* 276 */     for (x = 0; x < this.officials.length; x++) {
/*     */       
/* 278 */       if (this.officials[x] == wurmid) {
/*     */         
/* 280 */         Appointment tempapp = getAppointment(x + 1500);
/* 281 */         levels += tempapp.getLevel();
/*     */       } 
/*     */     } 
/* 284 */     return levels;
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
/*     */   public String getTitles(long data, boolean male) {
/* 297 */     Set<Appointment> tempset = new HashSet<>();
/*     */     
/* 299 */     for (int x = 0; x < 36; x++) {
/*     */       
/* 301 */       if ((data >> x & 0x1L) == 1L) {
/*     */         
/* 303 */         Appointment tempapp = getAppointment(x);
/*     */ 
/*     */         
/* 306 */         if (tempapp != null && tempapp.getType() == 0)
/* 307 */           tempset.add(tempapp); 
/*     */       } 
/*     */     } 
/* 310 */     if (tempset.isEmpty()) {
/* 311 */       return "";
/*     */     }
/* 313 */     int tempcounter = 0;
/* 314 */     StringBuilder sb = new StringBuilder();
/* 315 */     for (Iterator<Appointment> it = tempset.iterator(); it.hasNext(); ) {
/*     */       
/* 317 */       if (male) {
/* 318 */         sb.append(((Appointment)it.next()).getMaleName());
/*     */       } else {
/* 320 */         sb.append(((Appointment)it.next()).getFemaleName());
/*     */       } 
/* 322 */       tempcounter++;
/* 323 */       if (tempcounter <= tempset.size() - 2) {
/* 324 */         sb.append(", "); continue;
/* 325 */       }  if (tempcounter == tempset.size() - 1)
/* 326 */         sb.append(" and "); 
/*     */     } 
/* 328 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getOrders(long data, boolean male) {
/* 333 */     Set<Appointment> tempset = new HashSet<>();
/*     */     
/* 335 */     for (int x = 30; x < 36; x++) {
/*     */       
/* 337 */       if ((data >> x & 0x1L) == 1L) {
/*     */         
/* 339 */         Appointment tempapp = getAppointment(x);
/* 340 */         if (tempapp.getType() == 1)
/* 341 */           tempset.add(tempapp); 
/*     */       } 
/*     */     } 
/* 344 */     if (tempset.isEmpty()) {
/* 345 */       return "";
/*     */     }
/* 347 */     int tempcounter = 0;
/* 348 */     StringBuilder sb = new StringBuilder();
/* 349 */     for (Iterator<Appointment> it = tempset.iterator(); it.hasNext(); ) {
/*     */       
/* 351 */       if (male) {
/* 352 */         sb.append(((Appointment)it.next()).getMaleName());
/*     */       } else {
/* 354 */         sb.append(((Appointment)it.next()).getFemaleName());
/*     */       } 
/* 356 */       tempcounter++;
/* 357 */       if (tempcounter <= tempset.size() - 2) {
/* 358 */         sb.append(", "); continue;
/* 359 */       }  if (tempcounter == tempset.size() - 1)
/* 360 */         sb.append(" and the "); 
/*     */     } 
/* 362 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAppointed(long wurmid) {
/* 367 */     for (int x = 0; x < this.officials.length; x++) {
/*     */       
/* 369 */       if (this.officials[x] == wurmid)
/*     */       {
/* 371 */         return true;
/*     */       }
/*     */     } 
/* 374 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getOffices(long wurmid, boolean male) {
/* 379 */     Set<Appointment> tempset = new HashSet<>();
/*     */     
/* 381 */     for (int x = 0; x < this.officials.length; x++) {
/*     */       
/* 383 */       if (this.officials[x] == wurmid) {
/*     */         
/* 385 */         Appointment tempapp = getAppointment(x + 1500);
/*     */         
/* 387 */         tempset.add(tempapp);
/*     */       } 
/*     */     } 
/* 390 */     if (tempset.isEmpty())
/* 391 */       return ""; 
/* 392 */     int tempcounter = 0;
/* 393 */     StringBuilder sb = new StringBuilder();
/* 394 */     for (Iterator<Appointment> it = tempset.iterator(); it.hasNext(); ) {
/*     */       
/* 396 */       if (male) {
/* 397 */         sb.append(((Appointment)it.next()).getMaleName());
/*     */       } else {
/* 399 */         sb.append(((Appointment)it.next()).getFemaleName());
/*     */       } 
/* 401 */       tempcounter++;
/* 402 */       if (tempcounter <= tempset.size() - 2) {
/* 403 */         sb.append(", "); continue;
/* 404 */       }  if (tempcounter == tempset.size() - 1)
/* 405 */         sb.append(" and "); 
/*     */     } 
/* 407 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAvailTitlesForId(int _id) {
/* 412 */     return this.availableTitles[_id];
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAvailOrdersForId(int _id) {
/* 417 */     return this.availableOrders[_id - 30];
/*     */   }
/*     */ 
/*     */   
/*     */   public long getOfficialForId(int _id) {
/* 422 */     return this.officials[_id - 1500];
/*     */   }
/*     */ 
/*     */   
/*     */   private void loadAppointments() {
/* 427 */     boolean existed = false;
/*     */ 
/*     */     
/* 430 */     if (this.era > 0) {
/*     */       
/* 432 */       Connection dbcon = null;
/* 433 */       PreparedStatement ps = null;
/* 434 */       ResultSet rs = null;
/*     */       
/*     */       try {
/* 437 */         dbcon = DbConnector.getZonesDbCon();
/* 438 */         ps = dbcon.prepareStatement("select * FROM APPOINTMENTS WHERE ERA=?");
/* 439 */         ps.setInt(1, this.era);
/* 440 */         rs = ps.executeQuery();
/* 441 */         if (rs.next()) {
/*     */           
/* 443 */           existed = true; int x;
/* 444 */           for (x = 0; x < this.availableOrders.length; x++)
/* 445 */             this.availableOrders[x] = rs.getInt("ORDER" + (x + 1)); 
/* 446 */           for (x = 0; x < this.availableTitles.length; x++)
/* 447 */             this.availableTitles[x] = rs.getInt("TITLE" + (x + 1)); 
/* 448 */           this.officials = new long[11];
/* 449 */           for (x = 0; x < this.officials.length; x++)
/* 450 */             this.officials[x] = rs.getLong("OFFICIAL" + (x + 1)); 
/* 451 */           this.lastChecked = rs.getLong("LASTCHECKED");
/* 452 */           logger.log(Level.INFO, "Loaded lastChecked for Era " + this.era + ". Last checked was " + ((
/*     */               
/* 454 */               System.currentTimeMillis() - this.lastChecked) / 3600000L) + " hours ago.");
/*     */         } 
/* 456 */         DbUtilities.closeDatabaseObjects(ps, rs);
/*     */         
/* 458 */         ps = dbcon.prepareStatement("select * FROM OFFICES WHERE ERA=?");
/* 459 */         ps.setInt(1, this.era);
/* 460 */         rs = ps.executeQuery();
/* 461 */         if (rs.next()) {
/*     */           
/* 463 */           this.officesSet = new boolean[11];
/* 464 */           for (int x = 0; x < this.officesSet.length; x++)
/* 465 */             this.officesSet[x] = rs.getBoolean("OFFICIAL" + (x + 1)); 
/*     */         } 
/* 467 */         DbUtilities.closeDatabaseObjects(ps, rs);
/* 468 */         if (!existed)
/*     */         {
/* 470 */           ps = dbcon.prepareStatement("insert into APPOINTMENTS ( ERA,KINGDOM, LASTCHECKED ) VALUES(?,?,?)");
/* 471 */           ps.setInt(1, this.era);
/* 472 */           ps.setByte(2, this.kingdom);
/* 473 */           ps.setLong(3, this.lastChecked);
/* 474 */           ps.executeUpdate();
/* 475 */           DbUtilities.closeDatabaseObjects(ps, null);
/*     */           
/* 477 */           ps = dbcon.prepareStatement("insert into OFFICES ( ERA ) VALUES(?)");
/* 478 */           ps.setInt(1, this.era);
/* 479 */           ps.executeUpdate();
/* 480 */           DbUtilities.closeDatabaseObjects(ps, null);
/*     */         }
/*     */       
/* 483 */       } catch (SQLException sqex) {
/*     */         
/* 485 */         logger.log(Level.WARNING, "Failed to load kingdom officials for era " + this.era + sqex.getMessage(), sqex);
/*     */       }
/*     */       finally {
/*     */         
/* 489 */         DbUtilities.closeDatabaseObjects(ps, rs);
/* 490 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int useTitle(int id) {
/* 497 */     int newAvail = this.availableTitles[id] = this.availableTitles[id] - 1;
/* 498 */     Connection dbcon = null;
/* 499 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 502 */       dbcon = DbConnector.getZonesDbCon();
/* 503 */       ps = dbcon.prepareStatement(titleDBStrings[id]);
/* 504 */       ps.setInt(1, newAvail);
/* 505 */       ps.setInt(2, this.era);
/* 506 */       ps.executeUpdate();
/*     */     }
/* 508 */     catch (SQLException sqex) {
/*     */       
/* 510 */       logger.log(Level.WARNING, "Failed to set avail titles for era " + this.era + " " + id + ", " + 
/*     */           
/* 512 */           Kingdoms.getNameFor(this.kingdom) + sqex
/* 513 */           .getMessage(), sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 517 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 518 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/* 520 */     return newAvail;
/*     */   }
/*     */ 
/*     */   
/*     */   public int useOrder(int id) {
/* 525 */     int newAvail = this.availableOrders[id - 30] = this.availableOrders[id - 30] - 1;
/* 526 */     Connection dbcon = null;
/* 527 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 530 */       dbcon = DbConnector.getZonesDbCon();
/* 531 */       ps = dbcon.prepareStatement(orderDBStrings[id - 30]);
/* 532 */       ps.setInt(1, newAvail);
/* 533 */       ps.setInt(2, this.era);
/* 534 */       ps.executeUpdate();
/*     */     }
/* 536 */     catch (SQLException sqex) {
/*     */       
/* 538 */       logger.log(Level.WARNING, "Failed to set avail orders for era " + this.era + " " + id + ", " + 
/*     */           
/* 540 */           Kingdoms.getNameFor(this.kingdom) + sqex
/* 541 */           .getMessage(), sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 545 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 546 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/* 548 */     return newAvail;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOfficeSet(int id) {
/* 553 */     return this.officesSet[id - 1500];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long setOfficial(int id, long wurmid) {
/* 561 */     long oldOfficial = this.officials[id - 1500];
/* 562 */     Connection dbcon = null;
/* 563 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 566 */       this.officials[id - 1500] = wurmid;
/*     */       
/* 568 */       dbcon = DbConnector.getZonesDbCon();
/* 569 */       ps = dbcon.prepareStatement(officialDBStrings[id - 1500]);
/* 570 */       ps.setLong(1, wurmid);
/* 571 */       ps.setInt(2, this.era);
/* 572 */       ps.executeUpdate();
/* 573 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 574 */       if (wurmid > 0L)
/*     */       {
/* 576 */         this.officesSet[id - 1500] = true;
/* 577 */         ps = dbcon.prepareStatement(officeDBStrings[id - 1500]);
/* 578 */         ps.setBoolean(1, true);
/* 579 */         ps.setInt(2, this.era);
/* 580 */         ps.executeUpdate();
/* 581 */         DbUtilities.closeDatabaseObjects(ps, null);
/*     */       }
/*     */     
/* 584 */     } catch (SQLException sqex) {
/*     */       
/* 586 */       logger.log(Level.WARNING, "Failed to set official era " + this.era + " " + wurmid + ", " + sqex.getMessage(), sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 590 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 591 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/* 593 */     return oldOfficial;
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetAppointments(byte kingdomId) {
/* 598 */     Connection dbcon = null;
/* 599 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 602 */       Zones.calculateZones(false);
/* 603 */       logger.log(Level.INFO, "Resetting lastChecked for Era " + this.era + ". Last checked was " + ((
/*     */           
/* 605 */           System.currentTimeMillis() - this.lastChecked) / 3600000L) + " hours ago.");
/* 606 */       this.lastChecked = System.currentTimeMillis();
/* 607 */       dbcon = DbConnector.getZonesDbCon();
/*     */ 
/*     */       
/* 610 */       ps = dbcon.prepareStatement("DELETE FROM APPOINTMENTS WHERE ERA=?");
/* 611 */       ps.setInt(1, this.era);
/* 612 */       ps.executeUpdate();
/* 613 */       DbUtilities.closeDatabaseObjects(ps, null);
/*     */ 
/*     */       
/* 616 */       ps = dbcon.prepareStatement("insert into APPOINTMENTS ( ERA,KINGDOM, LASTCHECKED ) VALUES(?,?,?)");
/* 617 */       ps.setInt(1, this.era);
/* 618 */       ps.setByte(2, this.kingdom);
/* 619 */       ps.setLong(3, this.lastChecked);
/* 620 */       ps.executeUpdate();
/* 621 */       DbUtilities.closeDatabaseObjects(ps, null);
/*     */ 
/*     */       
/* 624 */       ps = dbcon.prepareStatement("DELETE FROM OFFICES WHERE ERA=?");
/* 625 */       ps.setInt(1, this.era);
/* 626 */       ps.executeUpdate();
/* 627 */       DbUtilities.closeDatabaseObjects(ps, null);
/*     */       
/* 629 */       ps = dbcon.prepareStatement("insert into OFFICES ( ERA ) VALUES(?)");
/* 630 */       ps.setInt(1, this.era);
/* 631 */       ps.executeUpdate();
/* 632 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 633 */       this.officesSet = new boolean[11];
/*     */       
/* 635 */       resetOfficials();
/*     */ 
/*     */       
/* 638 */       float perc = Zones.getPercentLandForKingdom(kingdomId);
/*     */       
/* 640 */       resetOrders(perc);
/*     */ 
/*     */       
/* 643 */       resetTitles(perc);
/*     */     }
/* 645 */     catch (SQLException sqex) {
/*     */       
/* 647 */       logger.log(Level.WARNING, "Failed to reset appointments for era " + this.era + " kingdom " + 
/*     */           
/* 649 */           Kingdoms.getNameFor(kingdomId) + ", " + sqex
/* 650 */           .getMessage(), sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 654 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 655 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void resetOfficials() {
/* 661 */     Connection dbcon = null;
/* 662 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 665 */       dbcon = DbConnector.getZonesDbCon();
/* 666 */       for (int x = 0; x < this.officials.length; x++) {
/*     */         
/* 668 */         if (this.officials[x] > 0L)
/*     */         {
/* 670 */           ps = dbcon.prepareStatement(officialDBStrings[x]);
/* 671 */           ps.setLong(1, this.officials[x]);
/* 672 */           ps.setInt(2, this.era);
/* 673 */           ps.executeUpdate();
/* 674 */           DbUtilities.closeDatabaseObjects(ps, null);
/*     */         }
/*     */       
/*     */       } 
/* 678 */     } catch (SQLException sqex) {
/*     */       
/* 680 */       logger.log(Level.WARNING, "Failed to reset officials for era " + this.era + " kingdom " + Kingdoms.getNameFor(this.kingdom) + ", " + sqex
/* 681 */           .getMessage(), sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 685 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 686 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void resetOrders(float percentOwned) {
/* 692 */     percentOwned = Math.min(50.0F, percentOwned);
/* 693 */     if (Servers.localServer.HOMESERVER)
/* 694 */       percentOwned = 10.0F; 
/* 695 */     for (int a = 0; a < this.availableOrders.length; a++) {
/*     */       
/* 697 */       int avail = (int)((percentOwned + 10.0F) / Math.max(a, 1) * 5.0F);
/* 698 */       if (avail == 0)
/*     */         break; 
/* 700 */       this.availableOrders[a] = avail;
/*     */     } 
/* 702 */     Connection dbcon = null;
/* 703 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 706 */       dbcon = DbConnector.getZonesDbCon();
/* 707 */       for (int x = 0; x < this.availableOrders.length; x++) {
/*     */         
/* 709 */         if (this.availableOrders[x] > 0)
/*     */         {
/* 711 */           ps = dbcon.prepareStatement(orderDBStrings[x]);
/* 712 */           ps.setInt(1, this.availableOrders[x]);
/* 713 */           ps.setInt(2, this.era);
/* 714 */           ps.executeUpdate();
/* 715 */           DbUtilities.closeDatabaseObjects(ps, null);
/*     */         }
/*     */       
/*     */       } 
/* 719 */     } catch (SQLException sqex) {
/*     */       
/* 721 */       logger.log(Level.WARNING, "Failed to reset orders for era " + this.era + " kingdom " + Kingdoms.getNameFor(this.kingdom) + ", " + sqex
/* 722 */           .getMessage(), sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 726 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 727 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void resetTitles(float percentOwned) {
/* 733 */     percentOwned = Math.min(50.0F, percentOwned);
/* 734 */     if (Servers.localServer.HOMESERVER)
/* 735 */       percentOwned = 10.0F; 
/* 736 */     for (int a = 0; a < this.availableTitles.length; a++) {
/*     */       
/* 738 */       int avail = (int)((percentOwned + 10.0F) / Math.max(a, 1) * 5.0F);
/* 739 */       if (avail == 0)
/*     */         break; 
/* 741 */       this.availableTitles[a] = avail;
/*     */     } 
/* 743 */     Connection dbcon = null;
/* 744 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 747 */       dbcon = DbConnector.getZonesDbCon();
/* 748 */       for (int x = 0; x < this.availableTitles.length; x++) {
/*     */         
/* 750 */         if (this.availableTitles[x] > 0)
/*     */         {
/* 752 */           ps = dbcon.prepareStatement(titleDBStrings[x]);
/* 753 */           ps.setInt(1, this.availableTitles[x]);
/* 754 */           ps.setInt(2, this.era);
/* 755 */           ps.executeUpdate();
/* 756 */           DbUtilities.closeDatabaseObjects(ps, null);
/*     */         }
/*     */       
/*     */       } 
/* 760 */     } catch (SQLException sqex) {
/*     */       
/* 762 */       logger.log(Level.WARNING, "Failed to reset titles for era " + this.era + " kingdom " + Kingdoms.getNameFor(this.kingdom) + ", " + sqex
/* 763 */           .getMessage(), sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 767 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 768 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public long getResetTimeRemaining() {
/* 774 */     return this.lastChecked + 604800000L - System.currentTimeMillis();
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getMaxAppointment(byte kId, int aId) {
/* 779 */     float p = Zones.getPercentLandForKingdom(kId);
/* 780 */     p = Math.min(50.0F, p);
/* 781 */     if (Servers.localServer.HOMESERVER)
/* 782 */       p = 10.0F; 
/* 783 */     return (int)((p + 10.0F) / Math.max(aId, 1) * 5.0F);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\kingdom\Appointments.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */