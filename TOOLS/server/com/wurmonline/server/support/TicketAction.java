/*     */ package com.wurmonline.server.support;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.SQLException;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.Nullable;
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
/*     */ public final class TicketAction
/*     */   implements MiscConstants
/*     */ {
/*  42 */   private static final Logger logger = Logger.getLogger(TicketAction.class.getName());
/*     */   
/*     */   private static final String ADDTICKETACTION = "INSERT INTO TICKETACTIONS (TICKETID,ACTIONNO,ACTIONDATE,ACTIONTYPE,BYWHOM,NOTE,VISIBILITYLEVEL,ISDIRTY,QUALITYOFSERVICECODE,COURTEOUSCODE,KNOWLEDGEABLECODE,GENERALFLAGS,QUALITIESFLAGS,IRKEDFLAGS,TRELLOCOMMENTID) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
/*     */   
/*     */   private static final String UPDATETICKETACTIONISDIRTY = "UPDATE TICKETACTIONS SET ISDIRTY=? WHERE TICKETID=? AND ACTIONNO=?";
/*     */   
/*     */   private static final String UPDATETRELLOCOMMENTID = "UPDATE TICKETACTIONS SET TRELLOCOMMENTID=?,ISDIRTY=? WHERE TICKETID=? AND ACTIONNO=?";
/*     */   
/*     */   private final int ticketId;
/*     */   
/*     */   private final short actionNo;
/*     */   
/*     */   private final long date;
/*     */   
/*     */   private final String note;
/*     */   
/*     */   private final byte action;
/*     */   
/*     */   private final String byWhom;
/*     */   
/*     */   private final byte visibilityLevel;
/*     */   private boolean dirty = true;
/*  64 */   private String trelloCommentId = "";
/*  65 */   private byte qualityOfServiceCode = 0;
/*  66 */   private byte courteousCode = 0;
/*  67 */   private byte knowledgeableCode = 0;
/*  68 */   private byte generalFlags = 0;
/*  69 */   private byte qualitiesFlags = 0;
/*  70 */   private byte irkedFlags = 0;
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
/*     */   public TicketAction(int aTicketId, short aActionNo, byte aAction, long aDate, String aByWhom, String aNote, byte aVisibilityLevel, boolean isDirty, byte aQualityOfServiceCode, byte aCourteousCode, byte aKnowledgeableCode, byte aGeneralFlags, byte aQualitiesFlags, byte aIrkedFlags, String aTrelloLink) {
/*  96 */     this.ticketId = aTicketId;
/*  97 */     this.actionNo = aActionNo;
/*  98 */     this.action = aAction;
/*  99 */     this.date = aDate;
/* 100 */     this.byWhom = aByWhom;
/* 101 */     this.note = aNote;
/* 102 */     this.visibilityLevel = aVisibilityLevel;
/* 103 */     this.dirty = isDirty;
/* 104 */     this.trelloCommentId = aTrelloLink;
/*     */ 
/*     */     
/* 107 */     this.qualityOfServiceCode = aQualityOfServiceCode;
/* 108 */     this.courteousCode = aCourteousCode;
/* 109 */     this.knowledgeableCode = aKnowledgeableCode;
/* 110 */     this.generalFlags = aGeneralFlags;
/* 111 */     this.qualitiesFlags = aQualitiesFlags;
/* 112 */     this.irkedFlags = aIrkedFlags;
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
/*     */   public TicketAction(int aTicketId, short aActionNo, byte aAction, String aByWhom, String aNote, byte aVisibilityLevel, byte aQualityOfServiceCode, byte aCourteousCode, byte aKnowledgeableCode, byte aGeneralFlags, byte aQualitiesFlags, byte aIrkedFlags) {
/* 130 */     this.ticketId = aTicketId;
/* 131 */     this.actionNo = aActionNo;
/* 132 */     this.action = aAction;
/* 133 */     this.date = System.currentTimeMillis();
/* 134 */     this.byWhom = aByWhom;
/* 135 */     this.note = aNote;
/* 136 */     this.visibilityLevel = aVisibilityLevel;
/* 137 */     this.dirty = true;
/* 138 */     this.trelloCommentId = "";
/*     */ 
/*     */     
/* 141 */     this.qualityOfServiceCode = aQualityOfServiceCode;
/* 142 */     this.courteousCode = aCourteousCode;
/* 143 */     this.knowledgeableCode = aKnowledgeableCode;
/* 144 */     this.generalFlags = aGeneralFlags;
/* 145 */     this.qualitiesFlags = aQualitiesFlags;
/* 146 */     this.irkedFlags = aIrkedFlags;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getNote() {
/* 156 */     return this.note;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getNote(Player player) {
/* 167 */     if (canSeeNote(player))
/* 168 */       return this.note; 
/* 169 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getNotePlus(Player player) {
/* 180 */     if (canSeeNote(player))
/*     */     {
/* 182 */       return getVisPlusNote(player);
/*     */     }
/* 184 */     return "";
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
/*     */   public boolean canSeeNote(Player player) {
/* 197 */     if (player.mayHearDevTalk()) {
/* 198 */       return true;
/*     */     }
/* 200 */     if (player.mayHearMgmtTalk() && this.visibilityLevel == 1) {
/* 201 */       return true;
/*     */     }
/* 203 */     if (this.visibilityLevel == 0) {
/* 204 */       return true;
/*     */     }
/* 206 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private String getVisPlusNote(Player player) {
/* 211 */     if (this.note.length() == 0) {
/* 212 */       return "";
/*     */     }
/*     */ 
/*     */     
/* 216 */     if (player.mayHearDevTalk()) {
/* 217 */       return getVisPlusNote();
/*     */     }
/*     */     
/* 220 */     if (player.mayHearMgmtTalk() && this.visibilityLevel != 2) {
/* 221 */       return getVisPlusNote();
/*     */     }
/* 223 */     return this.note;
/*     */   }
/*     */ 
/*     */   
/*     */   private String getVisPlusNote() {
/* 228 */     switch (this.visibilityLevel) {
/*     */       
/*     */       case 2:
/* 231 */         return "(GM+) " + this.note;
/*     */       case 1:
/* 233 */         return "(CM+) " + this.note;
/*     */     } 
/* 235 */     return "(All) " + this.note;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getDate() {
/* 246 */     return this.date;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getAction() {
/* 256 */     return this.action;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short getActionNo() {
/* 266 */     return this.actionNo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getByWhom() {
/* 276 */     return this.byWhom;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getVisibilityLevel() {
/* 285 */     return this.visibilityLevel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDirty() {
/* 295 */     return this.dirty;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDirty(boolean isDirty) {
/* 305 */     if (this.dirty != isDirty) {
/*     */       
/* 307 */       this.dirty = isDirty;
/* 308 */       dbUpdateIsDirty();
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
/*     */   public boolean isActionAfter(long aDate) {
/* 320 */     return (this.date > aDate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTrelloCommentId() {
/* 328 */     return this.trelloCommentId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTrelloCommentId(String aTrelloCommentId) {
/* 338 */     this.trelloCommentId = aTrelloCommentId;
/* 339 */     this.dirty = false;
/* 340 */     dbUpdateTrelloCommentId();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getQualityOfServiceCode() {
/* 349 */     return this.qualityOfServiceCode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getCourteousCode() {
/* 358 */     return this.courteousCode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getKnowledgeableCode() {
/* 367 */     return this.knowledgeableCode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getGeneralFlags() {
/* 376 */     return this.generalFlags;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getQualitiesFlags() {
/* 385 */     return this.qualitiesFlags;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getIrkedFlags() {
/* 394 */     return this.irkedFlags;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getGeneralFlagString() {
/* 400 */     return Integer.toBinaryString(256 + this.generalFlags).substring(2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getQualitiesFlagsString() {
/* 406 */     return Integer.toBinaryString(256 + this.qualitiesFlags).substring(3);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getIrkedFlagsString() {
/* 412 */     return Integer.toBinaryString(256 + this.irkedFlags).substring(3);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean wasServiceSuperior() {
/* 417 */     return (this.qualityOfServiceCode == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean wasServiceGood() {
/* 422 */     return (this.qualityOfServiceCode == 2);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean wasServiceAverage() {
/* 427 */     return (this.qualityOfServiceCode == 3);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean wasServiceFair() {
/* 432 */     return (this.qualityOfServiceCode == 4);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean wasServicePoor() {
/* 437 */     return (this.qualityOfServiceCode == 5);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean wasCourteousStronglyAgree() {
/* 442 */     return (this.courteousCode == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean wasCourteousSomewhatAgree() {
/* 447 */     return (this.courteousCode == 2);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean wasCourteousNeutral() {
/* 452 */     return (this.courteousCode == 3);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean wasCourteousSomewhatDisagree() {
/* 457 */     return (this.courteousCode == 4);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean wasCourteousStronglyDisagree() {
/* 462 */     return (this.courteousCode == 5);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean wasKnowledgeableStronglyAgree() {
/* 467 */     return (this.knowledgeableCode == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean wasKnowledgeableSomewhatAgree() {
/* 472 */     return (this.knowledgeableCode == 2);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean wasKnowledgeableNeutral() {
/* 477 */     return (this.knowledgeableCode == 3);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean wasKnowledgeableSomewhatDisagree() {
/* 482 */     return (this.knowledgeableCode == 4);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean wasKnowledgeableStronglyDisagree() {
/* 487 */     return (this.knowledgeableCode == 5);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean wasGeneralWrongInfo() {
/* 492 */     return ((this.generalFlags & 0x1) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean wasGeneralNoUnderstand() {
/* 497 */     return ((this.generalFlags & 0x2) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean wasGeneralUnclear() {
/* 502 */     return ((this.generalFlags & 0x4) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean wasGeneralNoSolve() {
/* 507 */     return ((this.generalFlags & 0x8) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean wasGeneralDisorganized() {
/* 512 */     return ((this.generalFlags & 0x10) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean wasGeneralOther() {
/* 517 */     return ((this.generalFlags & 0x20) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean wasGeneralFine() {
/* 522 */     return ((this.generalFlags & 0x40) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean wasQualityPatient() {
/* 527 */     return ((this.qualitiesFlags & 0x1) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean wasQualityEnthusiastic() {
/* 532 */     return ((this.qualitiesFlags & 0x2) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean wasQualityListened() {
/* 537 */     return ((this.qualitiesFlags & 0x4) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean wasQualityFriendly() {
/* 542 */     return ((this.qualitiesFlags & 0x8) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean wasQualityResponsive() {
/* 547 */     return ((this.qualitiesFlags & 0x10) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean wasQualityNothing() {
/* 552 */     return ((this.qualitiesFlags & 0x20) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean wasIrkedPatient() {
/* 557 */     return ((this.irkedFlags & 0x1) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean wasIrkedEnthusiastic() {
/* 562 */     return ((this.irkedFlags & 0x2) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean wasIrkedListened() {
/* 567 */     return ((this.irkedFlags & 0x4) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean wasIrkedFriendly() {
/* 572 */     return ((this.irkedFlags & 0x8) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean wasIrkedResponsive() {
/* 577 */     return ((this.irkedFlags & 0x10) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean wasIrkedNothing() {
/* 582 */     return ((this.irkedFlags & 0x20) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTrelloComment() {
/* 587 */     if (this.note.length() == 0) {
/* 588 */       return getLine(null);
/*     */     }
/* 590 */     return getLine(null) + "\n" + getVisPlusNote();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLine(Player player) {
/* 599 */     return Tickets.convertTime(this.date) + " " + getActionAsString(player);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getActionAsString(@Nullable Player player) {
/* 609 */     String by = " (by " + this.byWhom + ")";
/* 610 */     switch (this.action) {
/*     */       
/*     */       case 0:
/* 613 */         return "Note (by " + this.byWhom + ")";
/*     */       case 1:
/* 615 */         return "Cancelled";
/*     */       case 2:
/* 617 */         return "CM " + this.byWhom + " Responded";
/*     */       case 3:
/* 619 */         return "GM " + this.byWhom + " Responded";
/*     */       case 4:
/* 621 */         return "GM " + this.byWhom + " Responded";
/*     */       case 5:
/* 623 */         return "DEV " + this.byWhom + " Responded";
/*     */       case 13:
/* 625 */         if (player == null || player.mayHearMgmtTalk())
/* 626 */           return "Fwd CM" + by; 
/* 627 */         return "Fwd CM";
/*     */       case 6:
/* 629 */         if (player == null || player.mayHearDevTalk())
/* 630 */           return "Fwd GM" + by; 
/* 631 */         return "Fwd GM";
/*     */       case 7:
/* 633 */         if (player == null || player.mayHearDevTalk())
/* 634 */           return "Fwd Arch" + by; 
/* 635 */         return "Fwd Arch";
/*     */       case 8:
/* 637 */         if (player == null || player.mayHearDevTalk())
/* 638 */           return "Fwd Dev" + by; 
/* 639 */         return "Fwd Dev";
/*     */       case 9:
/* 641 */         if (player == null || player.mayHearMgmtTalk())
/* 642 */           return "Resolved" + by; 
/* 643 */         return "Resolved";
/*     */       case 10:
/* 645 */         if (player == null || player.mayHearMgmtTalk())
/* 646 */           return "OnHold" + by; 
/* 647 */         return "OnHold";
/*     */       case 15:
/* 649 */         if (player == null || player.mayHearMgmtTalk())
/* 650 */           return "ReOpened" + by; 
/* 651 */         return "ReOpened";
/*     */       case 11:
/* 653 */         if (player == null || player.mayHearDevTalk())
/* 654 */           return "GM" + by; 
/* 655 */         return "GM";
/*     */       case 14:
/* 657 */         return "Feedback";
/*     */     } 
/* 659 */     return "";
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
/*     */   public boolean isActionShownTo(Player player) {
/* 671 */     if (player.mayHearDevTalk()) {
/* 672 */       return true;
/*     */     }
/* 674 */     if (player.mayHearMgmtTalk()) {
/*     */       
/* 676 */       if (this.action != 0 || this.visibilityLevel == 1 || this.visibilityLevel == 0)
/*     */       {
/*     */         
/* 679 */         return true; } 
/* 680 */       return false;
/*     */     } 
/*     */ 
/*     */     
/* 684 */     return (this.action != 0 || this.visibilityLevel == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void dbSave() {
/* 690 */     dbAddTicketAction();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void dbAddTicketAction() {
/* 698 */     Connection dbcon = null;
/* 699 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 702 */       dbcon = DbConnector.getLoginDbCon();
/* 703 */       ps = dbcon.prepareStatement("INSERT INTO TICKETACTIONS (TICKETID,ACTIONNO,ACTIONDATE,ACTIONTYPE,BYWHOM,NOTE,VISIBILITYLEVEL,ISDIRTY,QUALITYOFSERVICECODE,COURTEOUSCODE,KNOWLEDGEABLECODE,GENERALFLAGS,QUALITIESFLAGS,IRKEDFLAGS,TRELLOCOMMENTID) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
/* 704 */       ps.setInt(1, this.ticketId);
/* 705 */       ps.setShort(2, this.actionNo);
/* 706 */       ps.setLong(3, this.date);
/* 707 */       ps.setByte(4, this.action);
/* 708 */       ps.setString(5, this.byWhom);
/* 709 */       ps.setString(6, this.note);
/* 710 */       ps.setByte(7, this.visibilityLevel);
/* 711 */       ps.setBoolean(8, this.dirty);
/*     */       
/* 713 */       ps.setByte(9, this.qualityOfServiceCode);
/* 714 */       ps.setByte(10, this.courteousCode);
/* 715 */       ps.setByte(11, this.knowledgeableCode);
/* 716 */       ps.setByte(12, this.generalFlags);
/* 717 */       ps.setByte(13, this.qualitiesFlags);
/* 718 */       ps.setByte(14, this.irkedFlags);
/* 719 */       ps.setString(15, this.trelloCommentId);
/*     */       
/* 721 */       ps.executeUpdate();
/*     */     }
/* 723 */     catch (SQLException sqx) {
/*     */       
/* 725 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 729 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 730 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void dbUpdateIsDirty() {
/* 739 */     Connection dbcon = null;
/* 740 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 743 */       dbcon = DbConnector.getLoginDbCon();
/* 744 */       ps = dbcon.prepareStatement("UPDATE TICKETACTIONS SET ISDIRTY=? WHERE TICKETID=? AND ACTIONNO=?");
/* 745 */       ps.setBoolean(1, this.dirty);
/* 746 */       ps.setInt(2, this.ticketId);
/* 747 */       ps.setShort(3, this.actionNo);
/*     */       
/* 749 */       ps.executeUpdate();
/*     */     }
/* 751 */     catch (SQLException sqx) {
/*     */       
/* 753 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 757 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 758 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void dbUpdateTrelloCommentId() {
/* 767 */     Connection dbcon = null;
/* 768 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 771 */       dbcon = DbConnector.getLoginDbCon();
/* 772 */       ps = dbcon.prepareStatement("UPDATE TICKETACTIONS SET TRELLOCOMMENTID=?,ISDIRTY=? WHERE TICKETID=? AND ACTIONNO=?");
/* 773 */       ps.setString(1, this.trelloCommentId);
/* 774 */       ps.setBoolean(2, this.dirty);
/* 775 */       ps.setInt(3, this.ticketId);
/* 776 */       ps.setShort(4, this.actionNo);
/*     */       
/* 778 */       ps.executeUpdate();
/*     */     }
/* 780 */     catch (SQLException sqx) {
/*     */       
/* 782 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 786 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 787 */       DbConnector.returnConnection(dbcon);
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
/*     */   public String toString() {
/* 799 */     return "TicketAction [ticketId=" + this.ticketId + ", actionNo=" + this.actionNo + ", note=" + this.note + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\support\TicketAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */