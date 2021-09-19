/*      */ package com.wurmonline.server.support;
/*      */ 
/*      */ import com.wurmonline.server.DbConnector;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.ServerEntry;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.players.PlayerInfoFactory;
/*      */ import com.wurmonline.server.utils.DbUtilities;
/*      */ import com.wurmonline.server.webinterface.WcTicket;
/*      */ import com.wurmonline.shared.constants.ProtoConstants;
/*      */ import com.wurmonline.shared.constants.TicketGroup;
/*      */ import java.sql.Connection;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.SQLException;
/*      */ import java.util.HashMap;
/*      */ import java.util.Map;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class Ticket
/*      */   implements MiscConstants, ProtoConstants, TimeConstants
/*      */ {
/*   50 */   private final Map<Long, TicketAction> ticketActions = new HashMap<>();
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String ADDTICKET = "INSERT INTO TICKETS (TICKETID,TICKETDATE,PLAYERWURMID,PLAYERNAME,CATEGORYCODE,SERVERID,ISGLOBAL,CLOSEDDATE,STATECODE,LEVELCODE,RESPONDERNAME,DESCRIPTION,ISDIRTY,REFFEEDBACK,TRELLOCARDID,TRELLOLISTCODE,HASDESCRIPTIONCHANGED,HASSUMMARYCHANGED,HASTRELLOLISTCHANGED,ACKNOWLEDGED) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String UPDATETICKET = "UPDATE TICKETS SET ISGLOBAL=?,CLOSEDDATE=?,STATECODE=?,LEVELCODE=?,RESPONDERNAME=?,ISDIRTY=?,REFFEEDBACK=?,TRELLOLISTCODE=?,HASDESCRIPTIONCHANGED=?,HASSUMMARYCHANGED=?,HASTRELLOLISTCHANGED=?,ACKNOWLEDGED=? WHERE TICKETID=?";
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String UPDATETICKETDESCRIPTION = "UPDATE TICKETS SET DESCRIPTION=?,ISDIRTY=?,HASDESCRIPTIONCHANGED=? WHERE TICKETID=?";
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String UPDATEISDIRTY = "UPDATE TICKETS SET ISDIRTY=?,TRELLOLISTCODE=?,HASDESCRIPTIONCHANGED=?,HASSUMMARYCHANGED=?,HASTRELLOLISTCHANGED=?,ACKNOWLEDGED=? WHERE TICKETID=?";
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String UPDATETRELLOCARDID = "UPDATE TICKETS SET TRELLOCARDID=?,ISDIRTY=?,TRELLOLISTCODE=?,HASDESCRIPTIONCHANGED=?,HASSUMMARYCHANGED=?,HASTRELLOLISTCHANGED=? WHERE TICKETID=?";
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String UPDATETRELLOFEEDBACKCARDID = "UPDATE TICKETS SET TRELLOFEEDBACKCARDID=?,ISDIRTY=? WHERE TICKETID=?";
/*      */ 
/*      */   
/*      */   private static final String UPDATEARCHIVESTATE = "UPDATE TICKETS SET ARCHIVESTATECODE=? WHERE TICKETID=?";
/*      */ 
/*      */   
/*      */   private static final String UPDATEACKNOWLEDGED = "UPDATE TICKETS SET ACKNOWLEDGED=? WHERE TICKETID=?";
/*      */ 
/*      */   
/*      */   private static final String DELETETICKETACTIONS = "DELETE FROM TICKETACTIONS WHERE TICKETID=?";
/*      */ 
/*      */   
/*      */   private static final String DELETETICKET = "DELETE FROM TICKETS WHERE TICKETID=?";
/*      */ 
/*      */   
/*   89 */   private static final Logger logger = Logger.getLogger(Ticket.class.getName());
/*      */   
/*      */   public static final byte STATE_NEW = 0;
/*      */   
/*      */   public static final byte STATE_ONHOLD = 1;
/*      */   
/*      */   public static final byte STATE_RESOLVED = 2;
/*      */   
/*      */   public static final byte STATE_RESPONDED = 3;
/*      */   public static final byte STATE_CANCELLED = 4;
/*      */   public static final byte STATE_WATCHING = 5;
/*      */   public static final byte STATE_TAKEN = 6;
/*      */   public static final byte STATE_FORWARDED = 7;
/*      */   public static final byte STATE_REOPENED = 8;
/*      */   public static final byte LEVEL_NONE = 0;
/*      */   public static final byte LEVEL_CM = 1;
/*      */   public static final byte LEVEL_GM = 2;
/*      */   public static final byte LEVEL_ARCH = 3;
/*      */   public static final byte LEVEL_DEV = 4;
/*      */   public static final byte ARCHIVE_NOT_YET = 0;
/*      */   public static final byte ARCHIVE_TELL_PLAYERS = 1;
/*      */   public static final byte ARCHIVE_UPDATE_TRELLO = 2;
/*      */   public static final byte ARCHIVE_REMOVE_FROM_DB = 3;
/*      */   private final int ticketId;
/*      */   private final long ticketDate;
/*      */   private final long playerWurmId;
/*      */   private final String playerName;
/*      */   private final byte categoryCode;
/*      */   private final int serverId;
/*      */   private boolean global = true;
/*  119 */   private long closedDate = 0L;
/*  120 */   private byte stateCode = 0;
/*  121 */   private byte levelCode = 0;
/*  122 */   private String responderName = "";
/*  123 */   private String description = "";
/*      */   private boolean dirty = true;
/*  125 */   private short refFeedback = 0;
/*  126 */   private String trelloCardId = "";
/*  127 */   private String trelloFeedbackCardId = "";
/*  128 */   private byte trelloListCode = 0;
/*      */   
/*      */   private boolean descriptionChanged = true;
/*      */   private boolean summaryChanged = true;
/*      */   private boolean trelloListChanged = true;
/*  133 */   private byte lastListCode = 0;
/*  134 */   private String lastDescription = "";
/*  135 */   private String lastSummary = "";
/*      */   
/*  137 */   private byte archiveState = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean acknowledged = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Ticket(int aTicketId, long aTicketDate, long aPlayerWurmId, String aPlayerName, byte aCategoryCode, int aServerId, boolean aGlobal, long aClosedDate, byte aStateCode, byte aLevelCode, String aResponderName, String aDescription, boolean isDirty, short aRefFeedback, String aTrelloFeedbackCardId, String aTrelloCardId, byte currentTrelloListCode, boolean hasDescriptionChanged, boolean hasSummaryChanged, boolean hasListChanged, byte theArchiveState, boolean isAcknowledged) {
/*  175 */     this.ticketId = aTicketId;
/*  176 */     this.ticketDate = aTicketDate;
/*  177 */     this.playerWurmId = aPlayerWurmId;
/*  178 */     this.playerName = aPlayerName;
/*  179 */     this.categoryCode = aCategoryCode;
/*  180 */     this.serverId = aServerId;
/*  181 */     this.global = true;
/*  182 */     this.closedDate = aClosedDate;
/*  183 */     this.stateCode = aStateCode;
/*  184 */     this.levelCode = aLevelCode;
/*  185 */     this.responderName = aResponderName;
/*  186 */     this.description = (aDescription.length() < 10240) ? aDescription : aDescription.substring(0, 10240);
/*  187 */     this.dirty = isDirty;
/*  188 */     this.refFeedback = aRefFeedback;
/*  189 */     this.trelloFeedbackCardId = aTrelloFeedbackCardId;
/*  190 */     this.trelloCardId = aTrelloCardId;
/*  191 */     this.trelloListCode = currentTrelloListCode;
/*  192 */     this.descriptionChanged = hasDescriptionChanged;
/*  193 */     this.summaryChanged = hasSummaryChanged;
/*  194 */     this.trelloListChanged = hasListChanged;
/*  195 */     this.archiveState = theArchiveState;
/*  196 */     this.acknowledged = isAcknowledged;
/*      */     
/*  198 */     if (!hasDescriptionChanged)
/*  199 */       this.lastDescription = this.description; 
/*  200 */     if (!hasSummaryChanged)
/*  201 */       this.lastSummary = getTrelloName(); 
/*  202 */     if (!hasListChanged) {
/*  203 */       this.lastListCode = this.trelloListCode;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Ticket(int aTicketId, long aTicketDate, long aPlayerWurmId, String aPlayerName, byte aCategoryCode, int aServerId, boolean aGlobal, long aClosedDate, byte aStateCode, byte aLevelCode, String aResponderName, String aDescription, boolean isDirty, short aRefFeedback, boolean aAcknowledge) {
/*  231 */     this(aTicketId, aTicketDate, aPlayerWurmId, aPlayerName, aCategoryCode, aServerId, true, aClosedDate, aStateCode, aLevelCode, aResponderName, aDescription, isDirty, aRefFeedback, "", "", (byte)0, false, false, false, (byte)0, aAcknowledge);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Ticket(long aPlayerWurmId, String aPlayerName, byte aCategoryCode, String aDescription) {
/*  248 */     this(Tickets.getNextTicketNo(), System.currentTimeMillis(), aPlayerWurmId, aPlayerName, aCategoryCode, 
/*      */         
/*  250 */         Servers.getLocalServerId(), true, 0L, (byte)0, (byte)1, "", aDescription
/*  251 */         .replace('"', '\''), true, (short)0, "", "", (byte)0, false, false, false, (byte)0, true);
/*      */ 
/*      */ 
/*      */     
/*  255 */     dbAddTicket();
/*  256 */     Players.getInstance().sendTicket(this);
/*  257 */     sendTicketGlobal();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getTicketId() {
/*  267 */     return this.ticketId;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getTicketName() {
/*  272 */     return "#" + this.ticketId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getWurmId() {
/*  282 */     return Tickets.calcWurmId(this.ticketId);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getTicketDate() {
/*  290 */     return this.ticketDate;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getDateAsString() {
/*  300 */     return Tickets.convertTime(this.ticketDate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isOpen() {
/*  310 */     return (this.closedDate == 0L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isClosed() {
/*  320 */     return (this.closedDate != 0L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getClosedDate() {
/*  330 */     return this.closedDate;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getStateCode() {
/*  340 */     return this.stateCode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setStateCode(byte aStateCode) {
/*  351 */     this.stateCode = aStateCode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getCategoryCode() {
/*  361 */     return this.categoryCode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getServerId() {
/*  371 */     return this.serverId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isGlobal() {
/*  381 */     return this.global;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isWaitingAcknowledgement() {
/*  386 */     return !this.acknowledged;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean getAcknowledged() {
/*  391 */     return this.acknowledged;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isDirty() {
/*  401 */     return this.dirty;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDirty(boolean isDirty) {
/*  411 */     if (isDirty) {
/*      */       
/*  413 */       this.dirty = isDirty;
/*  414 */       this.descriptionChanged = !this.lastDescription.equals(this.description);
/*  415 */       this.summaryChanged = !this.lastSummary.equals(getTrelloName());
/*  416 */       this.trelloListChanged = (this.lastListCode != getTrelloListCode());
/*      */     }
/*      */     else {
/*      */       
/*  420 */       this.dirty = isDirty;
/*  421 */       this.lastDescription = this.description;
/*  422 */       this.lastSummary = getTrelloName();
/*  423 */       this.lastListCode = getTrelloListCode();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTrelloCardId(String aTrelloCardId) {
/*  434 */     this.trelloCardId = aTrelloCardId;
/*  435 */     setDirty(false);
/*  436 */     dbUpdateTrelloCardId();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTrelloFeedbackCardId(String aTrelloCardId) {
/*  446 */     this.trelloFeedbackCardId = aTrelloCardId;
/*  447 */     setDirty(false);
/*  448 */     dbUpdateTrelloFeedbackCardId();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getLevelCode() {
/*  458 */     return this.levelCode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLevelCode(byte aLevelCode) {
/*  469 */     this.levelCode = aLevelCode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getResponderName() {
/*  479 */     return this.responderName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasFeedback() {
/*  489 */     return (this.refFeedback != 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public short getRefFeedback() {
/*  499 */     return this.refFeedback;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getTrelloCardId() {
/*  507 */     return this.trelloCardId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getTrelloFeedbackCardId() {
/*  515 */     return this.trelloFeedbackCardId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getArchiveState() {
/*  525 */     return this.archiveState;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setArchiveState(byte newArchiveState) {
/*  535 */     if (!Servers.isThisLoginServer() && newArchiveState == 2) {
/*      */ 
/*      */ 
/*      */       
/*  539 */       dbDelete();
/*  540 */       Tickets.removeTicket(this.ticketId);
/*      */     }
/*  542 */     else if (newArchiveState == 3) {
/*      */       
/*  544 */       dbDelete();
/*  545 */       Tickets.removeTicket(this.ticketId);
/*      */     }
/*      */     else {
/*      */       
/*  549 */       this.archiveState = newArchiveState;
/*  550 */       dbUpdateArchiveState();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasTicketChangedSince(long aDate) {
/*  563 */     for (TicketAction ta : this.ticketActions.values()) {
/*      */       
/*  565 */       if (ta.isActionAfter(aDate))
/*  566 */         return true; 
/*      */     } 
/*  568 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void update(boolean aGlobal, long aClosedDate, byte aStateCode, byte aLevelCode, String aResponderName, String aDescription, short aRefFeedback, boolean isAcknowledged) {
/*  585 */     this.global = true;
/*  586 */     this.closedDate = aClosedDate;
/*  587 */     this.stateCode = aStateCode;
/*  588 */     this.levelCode = aLevelCode;
/*  589 */     this.responderName = aResponderName;
/*  590 */     this.description = aDescription;
/*  591 */     this.refFeedback = aRefFeedback;
/*  592 */     this.acknowledged = isAcknowledged;
/*  593 */     setDirty(true);
/*  594 */     dbUpdateTicket();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void save() {
/*  602 */     dbAddTicket();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addTicketAction(TicketAction newTicketAction) {
/*  612 */     if (newTicketAction.getAction() == 14) {
/*  613 */       this.refFeedback = newTicketAction.getActionNo();
/*      */     }
/*  615 */     this.ticketActions.put(Long.valueOf(newTicketAction.getActionNo()), newTicketAction);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TicketAction addTicketAction(short aActionNo, byte aAction, long aDate, String aByWhom, String aNote, byte aVisibilityLevel, byte aQualityOfServiceCode, byte aCourteousCode, byte aKnowledgeableCode, byte aGeneralFlags, byte aQualitiesFlags, byte aIrkedFlags) {
/*  641 */     if (!this.ticketActions.containsKey(Long.valueOf(aActionNo))) {
/*      */       
/*  643 */       TicketAction ta = new TicketAction(this.ticketId, aActionNo, aAction, aDate, aByWhom, aNote, aVisibilityLevel, true, aQualityOfServiceCode, aCourteousCode, aKnowledgeableCode, aGeneralFlags, aQualitiesFlags, aIrkedFlags, "");
/*      */ 
/*      */ 
/*      */       
/*  647 */       ta.dbSave();
/*  648 */       addTicketAction(ta);
/*  649 */       Tickets.addTicketToSend(this, ta);
/*  650 */       return ta;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  656 */     return this.ticketActions.get(Long.valueOf(aActionNo));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void autoForwardToGM() {
/*  666 */     if (this.stateCode == 0 && this.serverId == 
/*  667 */       Servers.getLocalServerId() && this.ticketDate < 
/*  668 */       System.currentTimeMillis() - 900000L)
/*      */     {
/*  670 */       addNewTicketAction((byte)6, "Auto", "Auto forward to GMs", (byte)1);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addNewTicketAction(byte action, String byWhom, String note, byte visibilityLevel) {
/*  684 */     addNewTicketAction(action, byWhom, note, visibilityLevel, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addNewTicketAction(byte action, String byWhom, String note, byte visibilityLevel, byte aQualityOfServiceCode, byte aCourteousCode, byte aKnowledgeableCode, byte aGeneralFlags, byte aQualitiesFlags, byte aIrkedFlags) {
/*  701 */     TicketAction ta = new TicketAction(this.ticketId, (short)(this.ticketActions.size() + 1), action, byWhom, note, visibilityLevel, aQualityOfServiceCode, aCourteousCode, aKnowledgeableCode, aGeneralFlags, aQualitiesFlags, aIrkedFlags);
/*      */ 
/*      */ 
/*      */     
/*  705 */     ta.dbSave();
/*  706 */     addTicketAction(ta);
/*      */     
/*  708 */     boolean oldGlobal = this.global;
/*  709 */     this.global = true;
/*      */ 
/*      */     
/*  712 */     setAckFor(action, visibilityLevel);
/*      */     
/*  714 */     switch (action) {
/*      */ 
/*      */       
/*      */       case 1:
/*  718 */         this.stateCode = 4;
/*  719 */         this.closedDate = System.currentTimeMillis();
/*  720 */         this.responderName = "";
/*      */         break;
/*      */ 
/*      */       
/*      */       case 13:
/*  725 */         this.stateCode = 7;
/*  726 */         this.levelCode = 1;
/*  727 */         this.responderName = "";
/*      */         break;
/*      */ 
/*      */       
/*      */       case 6:
/*  732 */         this.stateCode = 7;
/*  733 */         this.levelCode = 2;
/*  734 */         this.responderName = "";
/*      */         break;
/*      */ 
/*      */       
/*      */       case 7:
/*  739 */         this.stateCode = 7;
/*  740 */         this.levelCode = 3;
/*  741 */         this.responderName = "";
/*      */         break;
/*      */ 
/*      */       
/*      */       case 8:
/*  746 */         this.stateCode = 7;
/*  747 */         this.levelCode = 4;
/*  748 */         this.responderName = "";
/*      */         break;
/*      */ 
/*      */       
/*      */       case 10:
/*  753 */         this.stateCode = 1;
/*  754 */         this.responderName = "";
/*      */         break;
/*      */ 
/*      */       
/*      */       case 3:
/*  759 */         if (this.levelCode != 3 && this.levelCode != 4)
/*  760 */           this.levelCode = 2; 
/*  761 */         this.stateCode = 3;
/*  762 */         this.responderName = byWhom;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 2:
/*  767 */         this.stateCode = 3;
/*  768 */         this.responderName = byWhom;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 11:
/*  773 */         if (this.levelCode != 3 && this.levelCode != 4)
/*  774 */           this.levelCode = 2; 
/*  775 */         this.stateCode = 6;
/*  776 */         this.responderName = byWhom;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 9:
/*  781 */         this.stateCode = 2;
/*  782 */         this.closedDate = System.currentTimeMillis();
/*  783 */         this.responderName = byWhom;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 15:
/*  788 */         if (this.categoryCode == 11) {
/*  789 */           this.stateCode = 5;
/*      */         } else {
/*  791 */           this.stateCode = 8;
/*  792 */         }  this.responderName = byWhom;
/*  793 */         this.closedDate = 0L;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 14:
/*  798 */         this.refFeedback = ta.getActionNo();
/*      */         break;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  810 */     setDirty(true);
/*  811 */     dbUpdateTicket();
/*      */ 
/*      */     
/*  814 */     Tickets.addTicketToSend(this, ta);
/*      */ 
/*      */     
/*  817 */     if (!oldGlobal) {
/*      */       
/*  819 */       sendTicketGlobal();
/*      */     } else {
/*      */       
/*  822 */       sendTicketGlobal(ta);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addSurvey(byte aQualityOfServiceCode, byte aCourteousCode, byte aKnowledgeableCode, byte aGeneralFlags, byte aQualitiesFlags, byte aIrkedFlags) {
/*  840 */     setDirty(true);
/*  841 */     dbUpdateTicket();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getShortDescription() {
/*  851 */     if (this.description.length() <= 400 || getStateCode() != 5)
/*  852 */       return getDescription(); 
/*  853 */     if (getTrelloCardId().length() == 0)
/*  854 */       return "Possible Client Hack: Soon in a trello near you!"; 
/*  855 */     return "Possible Client Hack: see trello https://trello.com/c/" + getTrelloCardId();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getDescription() {
/*  865 */     return this.description.replace('"', '\'');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void appendDescription(String aDescription) {
/*  876 */     if (this.description.length() == 0) {
/*  877 */       this.description = aDescription.replace('"', '\'');
/*      */     }
/*      */     else {
/*      */       
/*  881 */       this.description += "\n" + aDescription;
/*      */       
/*  883 */       if (this.description.length() > 400)
/*  884 */         this.description = this.description.substring(0, 399).replace('"', '\''); 
/*      */     } 
/*  886 */     setDirty(true);
/*  887 */     dbUpdateTicketDescription();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getTicketGroup(Player player) {
/*  899 */     if (player.mayHearDevTalk()) {
/*      */ 
/*      */       
/*  902 */       if (!isOpen())
/*  903 */         return TicketGroup.CLOSED.getId(); 
/*  904 */       if (this.categoryCode == 11)
/*  905 */         return TicketGroup.WATCH.getId(); 
/*  906 */       if (this.categoryCode == 4) {
/*  907 */         return TicketGroup.FORUM.getId();
/*      */       }
/*  909 */       return TicketGroup.OPEN.getId();
/*      */     } 
/*  911 */     if (player.mayHearMgmtTalk()) {
/*      */ 
/*      */       
/*  914 */       if (!isOpen()) {
/*  915 */         return TicketGroup.CLOSED.getId();
/*      */       }
/*  917 */       return TicketGroup.OPEN.getId();
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  922 */     return TicketGroup.NONE.getId();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getPlayerName() {
/*  933 */     return this.playerName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getPlayerId() {
/*  943 */     return this.playerWurmId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendTicketGlobal() {
/*  953 */     WcTicket wct = new WcTicket(this);
/*  954 */     if (Servers.isThisLoginServer()) {
/*  955 */       wct.sendFromLoginServer();
/*      */     } else {
/*  957 */       wct.sendToLoginServer();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void sendTicketGlobal(TicketAction ticketAction) {
/*  967 */     WcTicket wct = new WcTicket(this, ticketAction);
/*  968 */     if (Servers.isThisLoginServer()) {
/*  969 */       wct.sendFromLoginServer();
/*      */     } else {
/*  971 */       wct.sendToLoginServer();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getColourCode(Player player) {
/*  994 */     if (player.mayHearDevTalk() || player.mayHearMgmtTalk()) {
/*      */       
/*  996 */       if (isOpen()) {
/*      */         
/*  998 */         if (PlayerInfoFactory.isPlayerOnline(this.playerWurmId)) {
/*      */           
/* 1000 */           switch (this.levelCode) {
/*      */             
/*      */             case 2:
/* 1003 */               return 11;
/*      */             case 3:
/* 1005 */               return 8;
/*      */             case 4:
/* 1007 */               return 2;
/*      */           } 
/* 1009 */           return 3;
/*      */         } 
/*      */         
/* 1012 */         return 14;
/*      */       } 
/*      */       
/* 1015 */       return 0;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1020 */     if (isOpen()) {
/* 1021 */       return 3;
/*      */     }
/* 1023 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private String getLastAction() {
/* 1029 */     String lastAction = decodeState(this.stateCode);
/* 1030 */     if (this.stateCode != 2 && this.stateCode != 4 && this.stateCode != 0) {
/*      */ 
/*      */       
/* 1033 */       if (lastAction.length() > 0)
/* 1034 */         lastAction = lastAction + " "; 
/* 1035 */       lastAction = lastAction + decodeLevel(this.levelCode);
/*      */     } 
/* 1037 */     return lastAction;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasSummaryChanged() {
/* 1042 */     return this.summaryChanged;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasDescriptionChanged() {
/* 1047 */     return this.descriptionChanged;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasListChanged() {
/* 1052 */     return this.trelloListChanged;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getTrelloName() {
/* 1062 */     String lastAction = getLastAction();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1067 */     if (hasFeedback()) {
/* 1068 */       lastAction = lastAction + "*";
/*      */     }
/* 1070 */     return "#" + this.ticketId + " " + 
/* 1071 */       Tickets.convertTime(this.ticketDate) + " " + this.playerName + " : " + 
/*      */       
/* 1073 */       abbreviateCategory(this.categoryCode) + " (" + 
/* 1074 */       getServerAbbreviation() + ") " + lastAction + " " + this.responderName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getTrelloFeedbackTitle() {
/* 1083 */     if (hasFeedback()) {
/*      */       
/* 1085 */       TicketAction ta = getFeedback();
/* 1086 */       return "#" + this.ticketId + " " + 
/* 1087 */         Tickets.convertTime(this.closedDate) + " S:" + ta
/* 1088 */         .getQualityOfServiceCode() + " C:" + ta
/* 1089 */         .getCourteousCode() + " K:" + ta
/* 1090 */         .getKnowledgeableCode() + " G:" + ta
/* 1091 */         .getGeneralFlagString() + " Q:" + ta
/* 1092 */         .getQualitiesFlagsString() + " I:" + ta
/* 1093 */         .getIrkedFlagsString();
/*      */     } 
/* 1095 */     return "";
/*      */   }
/*      */ 
/*      */   
/*      */   public String getFeedbackText() {
/* 1100 */     if (hasFeedback())
/*      */     {
/* 1102 */       return getFeedback().getNote();
/*      */     }
/* 1104 */     return "";
/*      */   }
/*      */ 
/*      */   
/*      */   private String getServerAbbreviation() {
/* 1109 */     ServerEntry serverEntry = Servers.getServerWithId(this.serverId);
/* 1110 */     if (serverEntry == null) {
/*      */ 
/*      */       
/* 1113 */       String s = "unknown" + String.valueOf(this.serverId);
/*      */       
/* 1115 */       return s.substring(s.length() - 3);
/*      */     } 
/*      */     
/* 1118 */     return serverEntry.getAbbreviation();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getTicketSummary(Player player) {
/* 1129 */     if (player.mayHearDevTalk()) {
/* 1130 */       return getTrelloName();
/*      */     }
/* 1132 */     if (player.mayHearMgmtTalk())
/*      */     {
/*      */ 
/*      */       
/* 1136 */       return "#" + this.ticketId + " " + 
/* 1137 */         Tickets.convertTime(this.ticketDate) + " " + this.playerName + " : " + 
/*      */         
/* 1139 */         abbreviateCategory(this.categoryCode) + (
/* 1140 */         (this.serverId == Servers.getLocalServerId()) ? " " : (" (" + getServerAbbreviation() + ")")) + " " + 
/* 1141 */         getLastAction() + ((this.levelCode <= 1 || player
/* 1142 */         .mayMute()) ? (" " + this.responderName) : "");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1149 */     return "#" + this.ticketId + " " + 
/* 1150 */       Tickets.convertTime(this.ticketDate) + " : " + 
/* 1151 */       getLastAction();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getTrelloListCode() {
/* 1158 */     if (getStateCode() == 4 || 
/* 1159 */       getStateCode() == 2)
/* 1160 */       return 3; 
/* 1161 */     if (this.categoryCode == 11)
/* 1162 */       return 4; 
/* 1163 */     if (getLevelCode() == 3 || 
/* 1164 */       getLevelCode() == 4) {
/* 1165 */       return 2;
/*      */     }
/* 1167 */     return 1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TicketAction getFeedback() {
/* 1177 */     if (hasFeedback())
/*      */     {
/* 1179 */       return this.ticketActions.get(Long.valueOf(this.refFeedback));
/*      */     }
/* 1181 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TicketAction[] getTicketActions(Player player) {
/* 1192 */     Map<Long, TicketAction> playerTicketAction = new HashMap<>();
/* 1193 */     for (Map.Entry<Long, TicketAction> entry : this.ticketActions.entrySet()) {
/*      */       
/* 1195 */       if (((TicketAction)entry.getValue()).isActionShownTo(player))
/*      */       {
/* 1197 */         playerTicketAction.put(entry.getKey(), entry.getValue());
/*      */       }
/*      */     } 
/* 1200 */     return (TicketAction[])playerTicketAction.values().toArray((Object[])new TicketAction[playerTicketAction.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TicketAction[] getDirtyTicketActions() {
/* 1210 */     Map<Long, TicketAction> dirtyTicketAction = new HashMap<>();
/* 1211 */     for (Map.Entry<Long, TicketAction> entry : this.ticketActions.entrySet()) {
/*      */       
/* 1213 */       if (((TicketAction)entry.getValue()).isDirty())
/*      */       {
/* 1215 */         dirtyTicketAction.put(entry.getKey(), entry.getValue());
/*      */       }
/*      */     } 
/* 1218 */     return (TicketAction[])dirtyTicketAction.values().toArray((Object[])new TicketAction[dirtyTicketAction.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TicketAction[] getTicketActions() {
/* 1228 */     return (TicketAction[])this.ticketActions.values().toArray((Object[])new TicketAction[this.ticketActions.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getLatestActionDate() {
/* 1240 */     long newestAction = 0L;
/* 1241 */     for (Map.Entry<Long, TicketAction> entry : this.ticketActions.entrySet()) {
/*      */       
/* 1243 */       if (((TicketAction)entry.getValue()).getDate() > newestAction)
/* 1244 */         newestAction = ((TicketAction)entry.getValue()).getDate(); 
/*      */     } 
/* 1246 */     return newestAction;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isTicketShownTo(Player player) {
/* 1251 */     if (this.archiveState != 0) {
/* 1252 */       return false;
/*      */     }
/* 1254 */     if (player.mayHearDevTalk() && (Servers.getLocalServerId() == this.serverId || this.levelCode >= 2)) {
/* 1255 */       return true;
/*      */     }
/* 1257 */     if (player.mayHearMgmtTalk() && this.categoryCode != 11) {
/* 1258 */       return true;
/*      */     }
/* 1260 */     if (this.categoryCode != 11 && player.getWurmId() == this.playerWurmId)
/* 1261 */       return true; 
/* 1262 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void acknowledgeTicketUpdate(Player player) {
/* 1267 */     if (this.categoryCode != 11 && player.getWurmId() == this.playerWurmId)
/*      */     {
/* 1269 */       if (!this.acknowledged) {
/*      */         
/* 1271 */         this.acknowledged = true;
/* 1272 */         dbUpdateAchnowledged();
/* 1273 */         sendTicketGlobal();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String decodeState(byte aState) {
/* 1286 */     switch (aState) {
/*      */       
/*      */       case 0:
/* 1289 */         return "New";
/*      */       case 1:
/* 1291 */         return "OnHold";
/*      */       case 8:
/* 1293 */         return "ReOpened";
/*      */       case 2:
/* 1295 */         return "Resolved";
/*      */       case 4:
/* 1297 */         return "Cancelled";
/*      */       case 7:
/* 1299 */         return "Fwd";
/*      */     } 
/*      */ 
/*      */     
/* 1303 */     return "";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String abbreviateCategory(byte aCategory) {
/* 1315 */     switch (aCategory) {
/*      */       
/*      */       case 3:
/* 1318 */         return "Bug";
/*      */       case 8:
/* 1320 */         return "Paymt";
/*      */       case 7:
/* 1322 */         return "Pwd";
/*      */       case 1:
/* 1324 */         return "Acct";
/*      */       case 5:
/* 1326 */         return "Grief";
/*      */       case 9:
/* 1328 */         return "Stuck";
/*      */       case 2:
/* 1330 */         return "Boat";
/*      */       case 6:
/* 1332 */         return "Horse";
/*      */       case 11:
/* 1334 */         return "Watch";
/*      */       case 4:
/* 1336 */         return "Forum";
/*      */       case 10:
/* 1338 */         return "Other";
/*      */     } 
/* 1340 */     return "";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String decodeLevel(byte aLevel) {
/* 1352 */     switch (aLevel) {
/*      */       
/*      */       case 1:
/* 1355 */         return "CM";
/*      */       case 2:
/* 1357 */         return "GM";
/*      */       case 3:
/* 1359 */         return "Arch";
/*      */       case 4:
/* 1361 */         return "Admin";
/*      */     } 
/* 1363 */     return "";
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void setAckFor(byte action, byte noteVisibility) {
/* 1369 */     if (this.categoryCode == 11) {
/*      */       return;
/*      */     }
/* 1372 */     if (action != 1 && action != 14 && noteVisibility == 0) {
/* 1373 */       this.acknowledged = false;
/*      */     }
/* 1375 */     if (action == 9 || action == 15) {
/* 1376 */       this.acknowledged = false;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void dbAddTicket() {
/* 1384 */     Connection dbcon = null;
/* 1385 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1388 */       dbcon = DbConnector.getLoginDbCon();
/* 1389 */       ps = dbcon.prepareStatement("INSERT INTO TICKETS (TICKETID,TICKETDATE,PLAYERWURMID,PLAYERNAME,CATEGORYCODE,SERVERID,ISGLOBAL,CLOSEDDATE,STATECODE,LEVELCODE,RESPONDERNAME,DESCRIPTION,ISDIRTY,REFFEEDBACK,TRELLOCARDID,TRELLOLISTCODE,HASDESCRIPTIONCHANGED,HASSUMMARYCHANGED,HASTRELLOLISTCHANGED,ACKNOWLEDGED) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
/* 1390 */       ps.setInt(1, this.ticketId);
/* 1391 */       ps.setLong(2, this.ticketDate);
/* 1392 */       ps.setLong(3, this.playerWurmId);
/* 1393 */       ps.setString(4, this.playerName);
/* 1394 */       ps.setByte(5, this.categoryCode);
/* 1395 */       ps.setInt(6, this.serverId);
/* 1396 */       ps.setBoolean(7, this.global);
/* 1397 */       ps.setLong(8, this.closedDate);
/* 1398 */       ps.setByte(9, this.stateCode);
/* 1399 */       ps.setByte(10, this.levelCode);
/* 1400 */       ps.setString(11, this.responderName);
/* 1401 */       ps.setString(12, this.description.substring(0, Math.min(this.description.length(), 398)));
/* 1402 */       ps.setBoolean(13, this.dirty);
/* 1403 */       ps.setShort(14, this.refFeedback);
/* 1404 */       ps.setString(15, this.trelloCardId);
/* 1405 */       ps.setByte(16, this.trelloListCode);
/* 1406 */       ps.setBoolean(17, this.descriptionChanged);
/* 1407 */       ps.setBoolean(18, this.summaryChanged);
/* 1408 */       ps.setBoolean(19, this.trelloListChanged);
/* 1409 */       ps.setBoolean(20, this.acknowledged);
/*      */       
/* 1411 */       ps.executeUpdate();
/*      */     }
/* 1413 */     catch (SQLException sqx) {
/*      */       
/* 1415 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1419 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1420 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void dbUpdateTicket() {
/* 1429 */     Connection dbcon = null;
/* 1430 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1433 */       dbcon = DbConnector.getLoginDbCon();
/* 1434 */       ps = dbcon.prepareStatement("UPDATE TICKETS SET ISGLOBAL=?,CLOSEDDATE=?,STATECODE=?,LEVELCODE=?,RESPONDERNAME=?,ISDIRTY=?,REFFEEDBACK=?,TRELLOLISTCODE=?,HASDESCRIPTIONCHANGED=?,HASSUMMARYCHANGED=?,HASTRELLOLISTCHANGED=?,ACKNOWLEDGED=? WHERE TICKETID=?");
/* 1435 */       ps.setBoolean(1, this.global);
/* 1436 */       ps.setLong(2, this.closedDate);
/* 1437 */       ps.setByte(3, this.stateCode);
/* 1438 */       ps.setByte(4, this.levelCode);
/* 1439 */       ps.setString(5, this.responderName);
/* 1440 */       ps.setBoolean(6, this.dirty);
/* 1441 */       ps.setShort(7, this.refFeedback);
/* 1442 */       ps.setByte(8, this.trelloListCode);
/* 1443 */       ps.setBoolean(9, this.descriptionChanged);
/* 1444 */       ps.setBoolean(10, this.summaryChanged);
/* 1445 */       ps.setBoolean(11, this.trelloListChanged);
/* 1446 */       ps.setBoolean(12, this.acknowledged);
/* 1447 */       ps.setInt(13, this.ticketId);
/*      */       
/* 1449 */       ps.executeUpdate();
/*      */     }
/* 1451 */     catch (SQLException sqx) {
/*      */       
/* 1453 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1457 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1458 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void dbUpdateTicketDescription() {
/* 1467 */     Connection dbcon = null;
/* 1468 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1471 */       dbcon = DbConnector.getLoginDbCon();
/* 1472 */       ps = dbcon.prepareStatement("UPDATE TICKETS SET DESCRIPTION=?,ISDIRTY=?,HASDESCRIPTIONCHANGED=? WHERE TICKETID=?");
/* 1473 */       ps.setString(1, this.description);
/* 1474 */       ps.setBoolean(2, this.dirty);
/* 1475 */       ps.setBoolean(3, this.descriptionChanged);
/* 1476 */       ps.setInt(4, this.ticketId);
/*      */       
/* 1478 */       ps.executeUpdate();
/*      */     }
/* 1480 */     catch (SQLException sqx) {
/*      */       
/* 1482 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1486 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1487 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void dbUpdateIsDirty() {
/* 1496 */     Connection dbcon = null;
/* 1497 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1500 */       dbcon = DbConnector.getLoginDbCon();
/* 1501 */       ps = dbcon.prepareStatement("UPDATE TICKETS SET ISDIRTY=?,TRELLOLISTCODE=?,HASDESCRIPTIONCHANGED=?,HASSUMMARYCHANGED=?,HASTRELLOLISTCHANGED=?,ACKNOWLEDGED=? WHERE TICKETID=?");
/* 1502 */       ps.setBoolean(1, this.dirty);
/* 1503 */       ps.setByte(2, this.trelloListCode);
/* 1504 */       ps.setBoolean(3, this.descriptionChanged);
/* 1505 */       ps.setBoolean(4, this.summaryChanged);
/* 1506 */       ps.setBoolean(5, this.trelloListChanged);
/* 1507 */       ps.setBoolean(6, this.acknowledged);
/* 1508 */       ps.setInt(7, this.ticketId);
/*      */       
/* 1510 */       ps.executeUpdate();
/*      */     }
/* 1512 */     catch (SQLException sqx) {
/*      */       
/* 1514 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1518 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1519 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void dbUpdateTrelloCardId() {
/* 1528 */     Connection dbcon = null;
/* 1529 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1532 */       dbcon = DbConnector.getLoginDbCon();
/* 1533 */       ps = dbcon.prepareStatement("UPDATE TICKETS SET TRELLOCARDID=?,ISDIRTY=?,TRELLOLISTCODE=?,HASDESCRIPTIONCHANGED=?,HASSUMMARYCHANGED=?,HASTRELLOLISTCHANGED=? WHERE TICKETID=?");
/* 1534 */       ps.setString(1, this.trelloCardId);
/* 1535 */       ps.setBoolean(2, this.dirty);
/* 1536 */       ps.setByte(3, this.trelloListCode);
/* 1537 */       ps.setBoolean(4, this.descriptionChanged);
/* 1538 */       ps.setBoolean(5, this.summaryChanged);
/* 1539 */       ps.setBoolean(6, this.trelloListChanged);
/* 1540 */       ps.setInt(7, this.ticketId);
/*      */       
/* 1542 */       ps.executeUpdate();
/*      */     }
/* 1544 */     catch (SQLException sqx) {
/*      */       
/* 1546 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1550 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1551 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void dbUpdateTrelloFeedbackCardId() {
/* 1560 */     Connection dbcon = null;
/* 1561 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1564 */       dbcon = DbConnector.getLoginDbCon();
/* 1565 */       ps = dbcon.prepareStatement("UPDATE TICKETS SET TRELLOFEEDBACKCARDID=?,ISDIRTY=? WHERE TICKETID=?");
/* 1566 */       ps.setString(1, this.trelloFeedbackCardId);
/* 1567 */       ps.setBoolean(2, this.dirty);
/* 1568 */       ps.setInt(3, this.ticketId);
/*      */       
/* 1570 */       ps.executeUpdate();
/*      */     }
/* 1572 */     catch (SQLException sqx) {
/*      */       
/* 1574 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1578 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1579 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void dbUpdateArchiveState() {
/* 1585 */     Connection dbcon = null;
/* 1586 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1589 */       dbcon = DbConnector.getLoginDbCon();
/* 1590 */       ps = dbcon.prepareStatement("UPDATE TICKETS SET ARCHIVESTATECODE=? WHERE TICKETID=?");
/* 1591 */       ps.setByte(1, this.archiveState);
/* 1592 */       ps.setInt(2, this.ticketId);
/*      */       
/* 1594 */       ps.executeUpdate();
/*      */     }
/* 1596 */     catch (SQLException sqx) {
/*      */       
/* 1598 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1602 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1603 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void dbUpdateAchnowledged() {
/* 1609 */     Connection dbcon = null;
/* 1610 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1613 */       dbcon = DbConnector.getLoginDbCon();
/* 1614 */       ps = dbcon.prepareStatement("UPDATE TICKETS SET ACKNOWLEDGED=? WHERE TICKETID=?");
/* 1615 */       ps.setBoolean(1, this.acknowledged);
/* 1616 */       ps.setInt(2, this.ticketId);
/*      */       
/* 1618 */       ps.executeUpdate();
/*      */     }
/* 1620 */     catch (SQLException sqx) {
/*      */       
/* 1622 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1626 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1627 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void dbDelete() {
/* 1633 */     Connection dbcon = null;
/* 1634 */     PreparedStatement ps = null;
/*      */ 
/*      */     
/*      */     try {
/* 1638 */       dbcon = DbConnector.getLoginDbCon();
/* 1639 */       ps = dbcon.prepareStatement("DELETE FROM TICKETACTIONS WHERE TICKETID=?");
/* 1640 */       ps.setInt(1, this.ticketId);
/*      */       
/* 1642 */       ps.executeUpdate();
/* 1643 */       DbUtilities.closeDatabaseObjects(ps, null);
/*      */ 
/*      */       
/* 1646 */       ps = dbcon.prepareStatement("DELETE FROM TICKETS WHERE TICKETID=?");
/* 1647 */       ps.setInt(1, this.ticketId);
/*      */       
/* 1649 */       ps.executeUpdate();
/*      */     }
/* 1651 */     catch (SQLException sqx) {
/*      */       
/* 1653 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1657 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1658 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1670 */     return "Ticket [ticketId=" + this.ticketId + ", player=" + this.playerName + ", category=" + this.categoryCode + ", state=" + this.stateCode + ", level=" + this.levelCode + ", description=" + this.description + "]";
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\support\Ticket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */