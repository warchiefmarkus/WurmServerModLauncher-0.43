/*     */ package com.wurmonline.server.support;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.players.PlayerState;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import com.wurmonline.server.webinterface.WcTicket;
/*     */ import com.wurmonline.shared.constants.CounterTypes;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentLinkedDeque;
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
/*     */ public final class Tickets
/*     */   implements CounterTypes, TimeConstants
/*     */ {
/*  52 */   private static Logger logger = Logger.getLogger(Tickets.class.getName());
/*  53 */   private static final Map<Integer, Ticket> tickets = new ConcurrentHashMap<>();
/*  54 */   private static final ConcurrentLinkedDeque<TicketToSend> ticketsToSend = new ConcurrentLinkedDeque<>();
/*  55 */   private static final ConcurrentLinkedDeque<TicketToUpdate> ticketsToUpdate = new ConcurrentLinkedDeque<>();
/*     */ 
/*     */   
/*     */   private static final String LOADTICKETNOS = "SELECT * FROM TICKETNOS";
/*     */ 
/*     */   
/*     */   private static final String ADDTICKETNOS = "INSERT INTO TICKETNOS (PK,NEXTTICKETID,LASTTICKETID,NEXTBATCH) VALUES(0,?,?,?)";
/*     */ 
/*     */   
/*     */   private static final String UPDATETICKETNOS = "UPDATE TICKETNOS SET NEXTTICKETID=?, LASTTICKETID=?, NEXTBATCH=? WHERE PK=0";
/*     */ 
/*     */   
/*     */   private static final String LOADALLTICKETS = "SELECT * FROM TICKETS";
/*     */   
/*     */   private static final String LOADALLTICKETACTIONS = "SELECT * FROM TICKETACTIONS";
/*     */   
/*     */   private static final String PURGEBADACTIONS = "DELETE FROM TICKETACTIONS USING TICKETACTIONS LEFT JOIN TICKETS ON TICKETS.TICKETID = TICKETACTIONS.TICKETID WHERE TICKETS.TICKETID IS NULL";
/*     */   
/*  73 */   private static int nextTicketId = 0;
/*  74 */   private static int lastTicketId = 0;
/*  75 */   private static int nextBatch = 0;
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
/*     */   public static void loadTickets() {
/*  87 */     long start = System.nanoTime();
/*     */     
/*     */     try {
/*  90 */       dbLoadTicketNos();
/*  91 */       dbLoadAllTickets();
/*     */     }
/*  93 */     catch (Exception ex) {
/*     */       
/*  95 */       logger.log(Level.WARNING, "Problems loading Tickets", ex);
/*     */     } 
/*  97 */     float lElapsedTime = (float)(System.nanoTime() - start) / 1000000.0F;
/*  98 */     logger.log(Level.INFO, "Loaded " + tickets.size() + " Tickets. It took " + lElapsedTime + " millis.");
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
/*     */   public static Ticket addTicket(Ticket newTicket, boolean saveit) {
/* 111 */     if (tickets.containsKey(Integer.valueOf(newTicket.getTicketId()))) {
/*     */       
/* 113 */       Ticket oldTicket = tickets.get(Integer.valueOf(newTicket.getTicketId()));
/*     */       
/* 115 */       oldTicket.update(newTicket.isGlobal(), newTicket.getClosedDate(), newTicket.getStateCode(), newTicket
/* 116 */           .getLevelCode(), newTicket.getResponderName(), newTicket.getDescription(), newTicket
/* 117 */           .getRefFeedback(), newTicket.getAcknowledged());
/* 118 */       return oldTicket;
/*     */     } 
/*     */ 
/*     */     
/* 122 */     tickets.put(Integer.valueOf(newTicket.getTicketId()), newTicket);
/*     */     
/* 124 */     if (saveit) {
/* 125 */       newTicket.save();
/*     */     }
/* 127 */     return newTicket;
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
/*     */   public static long calcWurmId(int ticketId) {
/* 140 */     return ((ticketId << 8) + 25);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int decodeTicketId(long wurmId) {
/* 151 */     return (int)(wurmId >>> 8L & 0xFFFFFFFFFFFFFFFFL);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getNextTicketNo() {
/* 162 */     int nextTicketNo = nextTicketId;
/* 163 */     nextTicketId++;
/* 164 */     if (nextTicketId > lastTicketId) {
/*     */       
/* 166 */       nextTicketId = nextBatch;
/* 167 */       lastTicketId = nextTicketId + 9999;
/* 168 */       if (Servers.isThisLoginServer()) {
/*     */ 
/*     */         
/* 171 */         nextBatch += 10000;
/*     */       }
/*     */       else {
/*     */         
/* 175 */         nextBatch = 0;
/*     */ 
/*     */         
/* 178 */         WcTicket wt = new WcTicket(1);
/* 179 */         wt.sendToLoginServer();
/*     */       } 
/*     */     } 
/* 182 */     dbUpdateTicketNos();
/* 183 */     return nextTicketNo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getNextBatchNo() {
/* 193 */     if (Servers.isThisLoginServer()) {
/*     */       
/* 195 */       int nextBatchNo = nextBatch;
/* 196 */       nextBatch += 10000;
/* 197 */       dbUpdateTicketNos();
/* 198 */       return nextBatchNo;
/*     */     } 
/*     */ 
/*     */     
/* 202 */     return nextBatch;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void checkBatchNos() {
/* 211 */     if (nextTicketId == 0) {
/*     */ 
/*     */       
/* 214 */       WcTicket wt = new WcTicket(2);
/* 215 */       wt.sendToLoginServer();
/*     */     }
/* 217 */     else if (nextBatch == 0) {
/*     */ 
/*     */       
/* 220 */       WcTicket wt = new WcTicket(1);
/* 221 */       wt.sendToLoginServer();
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
/*     */   public static void setNextBatchNo(int newBatchNo, int secondBatchNo) {
/* 233 */     if (nextTicketId == 0) {
/*     */       
/* 235 */       nextTicketId = newBatchNo;
/* 236 */       lastTicketId = newBatchNo + 9999;
/*     */       
/* 238 */       if (nextBatch == 0) {
/* 239 */         nextBatch = secondBatchNo;
/*     */       
/*     */       }
/*     */     }
/* 243 */     else if (nextBatch == 0) {
/* 244 */       nextBatch = newBatchNo;
/*     */     } 
/* 246 */     dbUpdateTicketNos();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Ticket[] getTickets(Player player) {
/* 257 */     Map<Integer, Ticket> playerTickets = new HashMap<>();
/* 258 */     for (Map.Entry<Integer, Ticket> entry : tickets.entrySet()) {
/*     */       
/* 260 */       if (((Ticket)entry.getValue()).isTicketShownTo(player))
/*     */       {
/* 262 */         playerTickets.put(entry.getKey(), entry.getValue());
/*     */       }
/*     */     } 
/* 265 */     return (Ticket[])playerTickets.values().toArray((Object[])new Ticket[playerTickets.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void acknowledgeTicketUpdatesFor(Player player) {
/* 270 */     for (Map.Entry<Integer, Ticket> entry : tickets.entrySet())
/*     */     {
/* 272 */       ((Ticket)entry.getValue()).acknowledgeTicketUpdate(player);
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
/*     */   public static Ticket[] getTicketsChangedSince(long aDate) {
/* 284 */     Map<Integer, Ticket> playerTickets = new HashMap<>();
/* 285 */     for (Map.Entry<Integer, Ticket> entry : tickets.entrySet()) {
/*     */       
/* 287 */       if (((Ticket)entry.getValue()).hasTicketChangedSince(aDate))
/*     */       {
/* 289 */         playerTickets.put(entry.getKey(), entry.getValue());
/*     */       }
/*     */     } 
/* 292 */     return (Ticket[])playerTickets.values().toArray((Object[])new Ticket[playerTickets.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public static long getLatestActionDate() {
/* 297 */     long latestTicketDate = 0L;
/* 298 */     for (Map.Entry<Integer, Ticket> entry : tickets.entrySet()) {
/*     */       
/* 300 */       if (latestTicketDate < ((Ticket)entry.getValue()).getLatestActionDate())
/* 301 */         latestTicketDate = ((Ticket)entry.getValue()).getLatestActionDate(); 
/*     */     } 
/* 303 */     return latestTicketDate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Ticket[] getDirtyTickets() {
/* 313 */     Map<Integer, Ticket> dirtyTickets = new HashMap<>();
/* 314 */     for (Map.Entry<Integer, Ticket> entry : tickets.entrySet()) {
/*     */       
/* 316 */       Ticket ticket = entry.getValue();
/* 317 */       if (ticket.isDirty() && (ticket.isGlobal() || ticket.isClosed()))
/*     */       {
/* 319 */         dirtyTickets.put(entry.getKey(), entry.getValue());
/*     */       }
/*     */     } 
/* 322 */     return (Ticket[])dirtyTickets.values().toArray((Object[])new Ticket[dirtyTickets.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Ticket[] getArchiveTickets() {
/* 332 */     Map<Integer, Ticket> archiveTickets = new HashMap<>();
/* 333 */     for (Map.Entry<Integer, Ticket> entry : tickets.entrySet()) {
/*     */       
/* 335 */       Ticket ticket = entry.getValue();
/* 336 */       if (ticket.getArchiveState() == 2)
/*     */       {
/* 338 */         archiveTickets.put(entry.getKey(), entry.getValue());
/*     */       }
/*     */     } 
/* 341 */     return (Ticket[])archiveTickets.values().toArray((Object[])new Ticket[archiveTickets.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Ticket getTicket(int ticketNo) {
/* 352 */     return tickets.get(Integer.valueOf(ticketNo));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void removeTicket(int ticketNo) {
/* 363 */     tickets.remove(Integer.valueOf(ticketNo));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void playerStateChange(PlayerState pState) {
/* 373 */     for (Ticket ticket : tickets.values()) {
/*     */       
/* 375 */       if (ticket.getPlayerId() == pState.getPlayerId() && ticket.isOpen())
/*     */       {
/*     */ 
/*     */         
/* 379 */         Players.getInstance().sendTicket(ticket, null);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void dbLoadTicketNos() {
/* 389 */     Connection dbcon = null;
/* 390 */     PreparedStatement ps = null;
/* 391 */     ResultSet rs = null;
/* 392 */     boolean create = false;
/*     */     
/*     */     try {
/* 395 */       dbcon = DbConnector.getLoginDbCon();
/* 396 */       ps = dbcon.prepareStatement("SELECT * FROM TICKETNOS");
/* 397 */       rs = ps.executeQuery();
/*     */       
/* 399 */       if (rs.next()) {
/*     */         
/* 401 */         nextTicketId = rs.getInt("NEXTTICKETID");
/* 402 */         lastTicketId = rs.getInt("LASTTICKETID");
/* 403 */         nextBatch = rs.getInt("NEXTBATCH");
/*     */       }
/* 405 */       else if (Servers.isThisLoginServer()) {
/*     */ 
/*     */         
/* 408 */         nextTicketId = 10000;
/* 409 */         lastTicketId = 9999;
/* 410 */         nextBatch = 200000;
/* 411 */         create = true;
/*     */ 
/*     */ 
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */ 
/*     */         
/* 420 */         int lastDigits = Servers.getLocalServerId() % 100;
/* 421 */         nextTicketId = lastDigits * 10000;
/* 422 */         lastTicketId = nextTicketId + 9999;
/* 423 */         nextBatch = 0;
/* 424 */         create = true;
/*     */       } 
/* 426 */       if (create)
/*     */       {
/*     */         
/* 429 */         DbUtilities.closeDatabaseObjects(ps, rs);
/* 430 */         ps = dbcon.prepareStatement("INSERT INTO TICKETNOS (PK,NEXTTICKETID,LASTTICKETID,NEXTBATCH) VALUES(0,?,?,?)");
/* 431 */         ps.setInt(1, nextTicketId);
/* 432 */         ps.setInt(2, lastTicketId);
/* 433 */         ps.setInt(3, nextBatch);
/* 434 */         ps.executeUpdate();
/*     */       }
/*     */     
/* 437 */     } catch (SQLException sqx) {
/*     */       
/* 439 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 443 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 444 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void dbUpdateTicketNos() {
/* 453 */     Connection dbcon = null;
/* 454 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 457 */       dbcon = DbConnector.getLoginDbCon();
/* 458 */       ps = dbcon.prepareStatement("UPDATE TICKETNOS SET NEXTTICKETID=?, LASTTICKETID=?, NEXTBATCH=? WHERE PK=0");
/* 459 */       ps.setInt(1, nextTicketId);
/* 460 */       ps.setInt(2, lastTicketId);
/* 461 */       ps.setInt(3, nextBatch);
/* 462 */       if (ps.executeUpdate() == 0)
/*     */       {
/*     */         
/* 465 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 466 */         ps = dbcon.prepareStatement("INSERT INTO TICKETNOS (PK,NEXTTICKETID,LASTTICKETID,NEXTBATCH) VALUES(0,?,?,?)");
/* 467 */         ps.setInt(1, nextTicketId);
/* 468 */         ps.setInt(2, lastTicketId);
/* 469 */         ps.setInt(3, nextBatch);
/* 470 */         ps.executeUpdate();
/*     */       }
/*     */     
/* 473 */     } catch (SQLException sqx) {
/*     */       
/* 475 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 479 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 480 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void dbLoadAllTickets() {
/* 489 */     Connection dbcon = null;
/* 490 */     PreparedStatement ps = null;
/* 491 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 494 */       dbcon = DbConnector.getLoginDbCon();
/* 495 */       if (!DbConnector.isUseSqlite()) {
/*     */ 
/*     */ 
/*     */         
/* 499 */         ps = dbcon.prepareStatement("DELETE FROM TICKETACTIONS USING TICKETACTIONS LEFT JOIN TICKETS ON TICKETS.TICKETID = TICKETACTIONS.TICKETID WHERE TICKETS.TICKETID IS NULL");
/* 500 */         int purgedActions = ps.executeUpdate();
/* 501 */         if (purgedActions > 0)
/* 502 */           logger.log(Level.INFO, "Purged " + purgedActions + " Ticket Actions from database."); 
/* 503 */         DbUtilities.closeDatabaseObjects(ps, null);
/*     */       } 
/* 505 */       ps = dbcon.prepareStatement("SELECT * FROM TICKETS");
/* 506 */       rs = ps.executeQuery();
/* 507 */       while (rs.next()) {
/*     */         
/* 509 */         int aTicketId = rs.getInt("TICKETID");
/* 510 */         long aTicketDate = rs.getLong("TICKETDATE");
/* 511 */         long aPlayerWurmId = rs.getLong("PLAYERWURMID");
/* 512 */         String aPlayerName = rs.getString("PLAYERNAME");
/* 513 */         byte aTicketCategory = rs.getByte("CATEGORYCODE");
/* 514 */         int aServerId = rs.getInt("SERVERID");
/* 515 */         boolean aGloabl = rs.getBoolean("ISGLOBAL");
/* 516 */         long aClosedDate = rs.getLong("CLOSEDDATE");
/* 517 */         byte aState = rs.getByte("STATECODE");
/* 518 */         byte aLevel = rs.getByte("LEVELCODE");
/* 519 */         String aResponderName = rs.getString("RESPONDERNAME");
/* 520 */         String aDescription = rs.getString("DESCRIPTION");
/* 521 */         boolean isDirty = rs.getBoolean("ISDIRTY");
/* 522 */         short refFeedback = rs.getShort("REFFEEDBACK");
/* 523 */         String trelloFeedbackCardId = rs.getString("TRELLOFEEDBACKCARDID");
/* 524 */         String trelloCardId = rs.getString("TRELLOCARDID");
/* 525 */         byte trelloListCode = rs.getByte("TRELLOLISTCODE");
/* 526 */         boolean hasDescriptionChanged = rs.getBoolean("HASDESCRIPTIONCHANGED");
/* 527 */         boolean hasSummaryChanged = rs.getBoolean("HASSUMMARYCHANGED");
/* 528 */         boolean hasListChanged = rs.getBoolean("HASTRELLOLISTCHANGED");
/* 529 */         byte archiveCode = rs.getByte("ARCHIVESTATECODE");
/* 530 */         boolean acknowledged = rs.getBoolean("ACKNOWLEDGED");
/*     */         
/* 532 */         addTicket(new Ticket(aTicketId, aTicketDate, aPlayerWurmId, aPlayerName, aTicketCategory, aServerId, aGloabl, aClosedDate, aState, aLevel, aResponderName, aDescription, isDirty, refFeedback, trelloFeedbackCardId, trelloCardId, trelloListCode, hasDescriptionChanged, hasSummaryChanged, hasListChanged, archiveCode, acknowledged), false);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 540 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*     */ 
/*     */       
/* 543 */       ps = dbcon.prepareStatement("SELECT * FROM TICKETACTIONS");
/* 544 */       rs = ps.executeQuery();
/* 545 */       while (rs.next()) {
/*     */         
/* 547 */         int aTicketId = rs.getInt("TICKETID");
/* 548 */         short aActionNo = rs.getShort("ACTIONNO");
/* 549 */         long aActionDate = rs.getLong("ACTIONDATE");
/* 550 */         byte aAction = rs.getByte("ACTIONTYPE");
/* 551 */         String aByWhom = rs.getString("BYWHOM");
/* 552 */         String aNote = rs.getString("NOTE");
/* 553 */         byte aVisibilityLevel = rs.getByte("VISIBILITYLEVEL");
/* 554 */         boolean isDirty = rs.getBoolean("ISDIRTY");
/* 555 */         byte aQualityOfServiceCode = rs.getByte("QUALITYOFSERVICECODE");
/* 556 */         byte aCourteousCode = rs.getByte("COURTEOUSCODE");
/* 557 */         byte aKnowledgeableCode = rs.getByte("KNOWLEDGEABLECODE");
/* 558 */         byte aGeneralFlags = rs.getByte("GENERALFLAGS");
/* 559 */         byte aQualitiesFlags = rs.getByte("QUALITIESFLAGS");
/* 560 */         byte aIrkedFlags = rs.getByte("IRKEDFLAGS");
/* 561 */         String aTrelloLink = rs.getString("TRELLOCOMMENTID");
/*     */         
/* 563 */         getTicket(aTicketId).addTicketAction(new TicketAction(aTicketId, aActionNo, aAction, aActionDate, aByWhom, aNote, aVisibilityLevel, isDirty, aQualityOfServiceCode, aCourteousCode, aKnowledgeableCode, aGeneralFlags, aQualitiesFlags, aIrkedFlags, aTrelloLink));
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 568 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*     */     }
/* 570 */     catch (SQLException sqx) {
/*     */       
/* 572 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 576 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 577 */       DbConnector.returnConnection(dbcon);
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
/*     */   public static String convertTime(long time) {
/* 589 */     String fd = (new SimpleDateFormat("dd/MMM/yyyy HH:mm")).format(new Date(time));
/* 590 */     return fd;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void addTicketToSend(Ticket ticket, int numbActions, TicketAction action) {
/* 595 */     ticketsToSend.add(new TicketToSend(ticket, numbActions, action));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void addTicketToSend(Ticket ticket) {
/* 600 */     ticketsToSend.add(new TicketToSend(ticket));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void addTicketToSend(Ticket ticket, TicketAction action) {
/* 605 */     ticketsToSend.add(new TicketToSend(ticket, action));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void setTicketArchiveState(Ticket ticket, byte newArchiveState) {
/* 610 */     ticketsToUpdate.add(new TicketToUpdate(ticket, (byte)0, newArchiveState));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void setTicketTrelloCardId(Ticket ticket, String aTrelloCardId) {
/* 615 */     ticketsToUpdate.add(new TicketToUpdate(ticket, (byte)1, aTrelloCardId));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void setTicketTrelloFeedbackCardId(Ticket ticket, String aTrelloCardId) {
/* 620 */     ticketsToUpdate.add(new TicketToUpdate(ticket, (byte)2, aTrelloCardId));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void setTicketIsDirty(Ticket ticket, boolean isDirty) {
/* 625 */     ticketsToUpdate.add(new TicketToUpdate(ticket, (byte)3, isDirty));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void handleTicketsToSend() {
/* 630 */     TicketToSend ticketToSend = ticketsToSend.pollFirst();
/* 631 */     while (ticketToSend != null) {
/*     */       
/* 633 */       ticketToSend.send();
/* 634 */       ticketToSend = ticketsToSend.pollFirst();
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 644 */     TicketToUpdate ticketToUpdate = ticketsToUpdate.pollFirst();
/* 645 */     while (ticketToUpdate != null) {
/*     */       
/* 647 */       ticketToUpdate.update();
/* 648 */       ticketToUpdate = ticketsToUpdate.pollFirst();
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
/*     */   public static final void handleArchiveTickets() {
/* 661 */     for (Ticket ticket : tickets.values()) {
/*     */       
/* 663 */       if (ticket.getArchiveState() == 0 && ticket.isClosed() && ticket
/* 664 */         .getClosedDate() < System.currentTimeMillis() - 604800000L) {
/*     */         
/* 666 */         ticket.setArchiveState((byte)1);
/* 667 */         addTicketToSend(ticket);
/*     */       } 
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
/*     */   public static final boolean checkForFlooding(long playerId) {
/* 681 */     int count = 0;
/* 682 */     for (Ticket ticket : tickets.values()) {
/*     */       
/* 684 */       if (ticket.getPlayerId() == playerId && ticket.isOpen() && ticket
/* 685 */         .getTicketDate() > System.currentTimeMillis() - 10800000L)
/*     */       {
/* 687 */         count++;
/*     */       }
/*     */     } 
/* 690 */     return (count >= 3);
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void sendRequiresAckMessage(Player player) {
/* 695 */     for (Ticket ticket : tickets.values()) {
/*     */       
/* 697 */       if (ticket.getPlayerId() == player.getWurmId())
/*     */       {
/* 699 */         if (ticket.isWaitingAcknowledgement()) {
/*     */           
/* 701 */           String msg = "Your ticket " + ticket.getTicketName() + " has been ";
/* 702 */           if (ticket.getStateCode() == 2) {
/* 703 */             player.getCommunicator().sendSafeServerMessage(msg + "resolved."); continue;
/*     */           } 
/* 705 */           player.getCommunicator().sendSafeServerMessage(msg + "updated.");
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\support\Tickets.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */