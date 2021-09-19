/*     */ package com.wurmonline.server.webinterface;
/*     */ 
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.WurmId;
/*     */ import com.wurmonline.server.support.Ticket;
/*     */ import com.wurmonline.server.support.TicketAction;
/*     */ import com.wurmonline.server.support.Tickets;
/*     */ import com.wurmonline.shared.util.StreamUtilities;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
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
/*     */ public class WcTicket
/*     */   extends WebCommand
/*     */   implements MiscConstants
/*     */ {
/*  44 */   private static final Logger logger = Logger.getLogger(WcTicket.class.getName());
/*     */ 
/*     */   
/*     */   public static final byte DO_NOTHING = 0;
/*     */ 
/*     */   
/*     */   public static final byte GET_BATCHNOS = 1;
/*     */   
/*     */   public static final byte THE_BATCHNOS = 2;
/*     */   
/*     */   public static final byte SEND_TICKET = 3;
/*     */   
/*     */   public static final byte CHECK_FOR_UPDATES = 4;
/*     */   
/*  58 */   private byte type = 0;
/*     */ 
/*     */   
/*  61 */   private int noBatchNos = 1;
/*     */ 
/*     */   
/*  64 */   private int firstBatchNos = 0;
/*  65 */   private int secondBatchNos = 0;
/*     */ 
/*     */   
/*  68 */   private Ticket ticket = null;
/*     */   
/*     */   private boolean sendActions = false;
/*     */   
/*  72 */   private TicketAction ticketAction = null;
/*     */   
/*  74 */   private long checkDate = 0L;
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
/*     */   
/*     */   public WcTicket(long aId, byte[] aData) {
/* 167 */     super(aId, (short)18, aData);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean autoForward() {
/* 178 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   byte[] encode() {
/* 189 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 190 */     DataOutputStream dos = null;
/* 191 */     byte[] barr = null;
/*     */     
/*     */     try {
/* 194 */       dos = new DataOutputStream(bos);
/* 195 */       dos.writeByte(this.type);
/* 196 */       switch (this.type) {
/*     */         
/*     */         case 1:
/* 199 */           dos.writeInt(this.noBatchNos);
/*     */           break;
/*     */         
/*     */         case 2:
/* 203 */           dos.writeInt(this.firstBatchNos);
/* 204 */           dos.writeInt(this.secondBatchNos);
/*     */           break;
/*     */         
/*     */         case 3:
/* 208 */           dos.writeInt(this.ticket.getTicketId());
/* 209 */           dos.writeLong(this.ticket.getTicketDate());
/* 210 */           dos.writeLong(this.ticket.getPlayerId());
/* 211 */           dos.writeUTF(this.ticket.getPlayerName());
/* 212 */           dos.writeByte(this.ticket.getCategoryCode());
/* 213 */           dos.writeInt(this.ticket.getServerId());
/* 214 */           dos.writeBoolean(this.ticket.isGlobal());
/* 215 */           dos.writeLong(this.ticket.getClosedDate());
/* 216 */           dos.writeByte(this.ticket.getStateCode());
/* 217 */           dos.writeByte(this.ticket.getLevelCode());
/* 218 */           dos.writeUTF(this.ticket.getResponderName());
/* 219 */           dos.writeUTF(this.ticket.getDescription());
/* 220 */           dos.writeShort(this.ticket.getRefFeedback());
/* 221 */           dos.writeBoolean(this.ticket.getAcknowledged());
/*     */           
/* 223 */           if (this.sendActions) {
/*     */             
/* 225 */             if (this.ticketAction == null) {
/*     */               
/* 227 */               TicketAction[] ticketActions = this.ticket.getTicketActions();
/* 228 */               dos.writeByte(ticketActions.length);
/* 229 */               for (TicketAction ta : ticketActions)
/*     */               {
/* 231 */                 addTicketAction(dos, ta);
/*     */               }
/*     */               
/*     */               break;
/*     */             } 
/* 236 */             dos.writeByte(1);
/* 237 */             addTicketAction(dos, this.ticketAction);
/*     */             
/*     */             break;
/*     */           } 
/* 241 */           dos.writeByte(0);
/*     */           break;
/*     */         
/*     */         case 4:
/* 245 */           dos.writeLong(this.checkDate);
/*     */           break;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 251 */       dos.flush();
/* 252 */       dos.close();
/*     */     }
/* 254 */     catch (Exception ex) {
/*     */       
/* 256 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 260 */       StreamUtilities.closeOutputStreamIgnoreExceptions(dos);
/* 261 */       barr = bos.toByteArray();
/* 262 */       StreamUtilities.closeOutputStreamIgnoreExceptions(bos);
/* 263 */       setData(barr);
/*     */     } 
/* 265 */     return barr;
/*     */   }
/*     */ 
/*     */   
/*     */   private void addTicketAction(DataOutputStream dos, TicketAction ta) throws IOException {
/* 270 */     dos.writeShort(ta.getActionNo());
/* 271 */     dos.writeByte(ta.getAction());
/* 272 */     dos.writeLong(ta.getDate());
/* 273 */     dos.writeUTF(ta.getByWhom());
/* 274 */     dos.writeUTF(ta.getNote());
/* 275 */     dos.writeByte(ta.getVisibilityLevel());
/* 276 */     if (ta.getAction() == 14) {
/*     */       
/* 278 */       dos.writeByte(ta.getQualityOfServiceCode());
/* 279 */       dos.writeByte(ta.getCourteousCode());
/* 280 */       dos.writeByte(ta.getKnowledgeableCode());
/* 281 */       dos.writeByte(ta.getGeneralFlags());
/* 282 */       dos.writeByte(ta.getQualitiesFlags());
/* 283 */       dos.writeByte(ta.getIrkedFlags());
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
/*     */   public void execute() {
/* 295 */     (new Thread()
/*     */       {
/*     */         
/*     */         public void run()
/*     */         {
/* 300 */           DataInputStream dis = null; try {
/*     */             int ticketId; long ticketDate, playerWurmId; String playerName; byte categoryCode; int serverId; boolean global; long closedDate; byte stateCode, levelCode; String responderName, description; short refFeedback; boolean acknowledge; int numbActions; TicketAction ta;
/*     */             int x;
/* 303 */             dis = new DataInputStream(new ByteArrayInputStream(WcTicket.this.getData()));
/* 304 */             WcTicket.this.type = dis.readByte();
/* 305 */             switch (WcTicket.this.type) {
/*     */               
/*     */               case 1:
/* 308 */                 WcTicket.this.noBatchNos = dis.readInt();
/*     */                 break;
/*     */               
/*     */               case 2:
/* 312 */                 WcTicket.this.firstBatchNos = dis.readInt();
/* 313 */                 WcTicket.this.secondBatchNos = dis.readInt();
/*     */                 break;
/*     */               
/*     */               case 3:
/* 317 */                 ticketId = dis.readInt();
/* 318 */                 ticketDate = dis.readLong();
/* 319 */                 playerWurmId = dis.readLong();
/* 320 */                 playerName = dis.readUTF();
/* 321 */                 categoryCode = dis.readByte();
/* 322 */                 serverId = dis.readInt();
/* 323 */                 global = dis.readBoolean();
/* 324 */                 closedDate = dis.readLong();
/* 325 */                 stateCode = dis.readByte();
/* 326 */                 levelCode = dis.readByte();
/* 327 */                 responderName = dis.readUTF();
/* 328 */                 description = dis.readUTF();
/* 329 */                 refFeedback = dis.readShort();
/* 330 */                 acknowledge = dis.readBoolean();
/*     */                 
/* 332 */                 WcTicket.this.ticket = new Ticket(ticketId, ticketDate, playerWurmId, playerName, categoryCode, serverId, global, closedDate, stateCode, levelCode, responderName, description, true, refFeedback, acknowledge);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/* 338 */                 WcTicket.this.ticket = Tickets.addTicket(WcTicket.this.ticket, true);
/*     */ 
/*     */                 
/* 341 */                 numbActions = dis.readByte();
/* 342 */                 ta = null;
/*     */                 
/* 344 */                 for (x = 0; x < numbActions; x++) {
/*     */                   
/* 346 */                   byte qualityOfServiceCode = 0;
/* 347 */                   byte courteousCode = 0;
/* 348 */                   byte knowledgeableCode = 0;
/* 349 */                   byte generalFlags = 0;
/* 350 */                   byte qualitiesFlags = 0;
/* 351 */                   byte irkedFlags = 0;
/*     */                   
/* 353 */                   short actionNo = dis.readShort();
/* 354 */                   byte action = dis.readByte();
/* 355 */                   long dated = dis.readLong();
/* 356 */                   String byWhom = dis.readUTF();
/* 357 */                   String note = dis.readUTF();
/* 358 */                   byte visLevel = dis.readByte();
/*     */                   
/* 360 */                   if (action == 14) {
/*     */                     
/* 362 */                     qualityOfServiceCode = dis.readByte();
/* 363 */                     courteousCode = dis.readByte();
/* 364 */                     knowledgeableCode = dis.readByte();
/* 365 */                     generalFlags = dis.readByte();
/* 366 */                     qualitiesFlags = dis.readByte();
/* 367 */                     irkedFlags = dis.readByte();
/*     */                   } 
/* 369 */                   ta = WcTicket.this.ticket.addTicketAction(actionNo, action, dated, byWhom, note, visLevel, qualityOfServiceCode, courteousCode, knowledgeableCode, generalFlags, qualitiesFlags, irkedFlags);
/*     */                 } 
/*     */ 
/*     */ 
/*     */                 
/* 374 */                 if (Servers.isThisLoginServer() && WcTicket.this.ticket.isGlobal()) {
/*     */                   
/* 376 */                   WcTicket wct = new WcTicket(WcTicket.this.getWurmId(), WcTicket.this.ticket, numbActions, ta);
/* 377 */                   wct.sendFromLoginServer();
/*     */                 } 
/* 379 */                 Tickets.addTicketToSend(WcTicket.this.ticket, numbActions, ta);
/*     */                 break;
/*     */ 
/*     */               
/*     */               case 4:
/* 384 */                 WcTicket.this.checkDate = dis.readLong();
/*     */                 break;
/*     */             } 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 391 */           } catch (IOException ex) {
/*     */             
/* 393 */             WcTicket.logger.log(Level.WARNING, "Unpack exception " + ex.getMessage(), ex);
/*     */ 
/*     */             
/*     */             return;
/*     */           } finally {
/* 398 */             StreamUtilities.closeInputStreamIgnoreExceptions(dis);
/*     */           } 
/*     */           
/* 401 */           if (WcTicket.this.type == 1 && Servers.isThisLoginServer()) {
/*     */ 
/*     */             
/* 404 */             WcTicket.this.firstBatchNos = Tickets.getNextBatchNo();
/* 405 */             if (WcTicket.this.noBatchNos > 1) {
/* 406 */               WcTicket.this.secondBatchNos = Tickets.getNextBatchNo();
/*     */             }
/* 408 */             WcTicket wt = new WcTicket(WcTicket.this.firstBatchNos, WcTicket.this.secondBatchNos);
/* 409 */             wt.sendToServer(WurmId.getOrigin(WcTicket.this.getWurmId()));
/*     */           }
/* 411 */           else if (WcTicket.this.type == 2 && !Servers.isThisLoginServer()) {
/*     */ 
/*     */ 
/*     */             
/* 415 */             Tickets.setNextBatchNo(WcTicket.this.firstBatchNos, WcTicket.this.secondBatchNos);
/*     */           }
/* 417 */           else if (WcTicket.this.type != 3) {
/*     */ 
/*     */ 
/*     */             
/* 421 */             if (WcTicket.this.type == 4)
/*     */             {
/*     */ 
/*     */               
/* 425 */               for (Ticket t : Tickets.getTicketsChangedSince(WcTicket.this.checkDate)) {
/*     */                 
/* 427 */                 WcTicket wt = new WcTicket(t);
/* 428 */                 wt.sendToServer(WurmId.getOrigin(WcTicket.this.getWurmId()));
/*     */               }  } 
/*     */           } 
/*     */         }
/* 432 */       }).start();
/*     */   }
/*     */   
/*     */   public WcTicket(int aNoBatchNos) {
/*     */     super(WurmId.getNextWCCommandId(), (short)18);
/*     */     this.type = 1;
/*     */     this.noBatchNos = aNoBatchNos;
/*     */   }
/*     */   
/*     */   public WcTicket(int aFirstBatchNos, int aSecondBatchNos) {
/*     */     super(WurmId.getNextWCCommandId(), (short)18);
/*     */     this.type = 2;
/*     */     this.firstBatchNos = aFirstBatchNos;
/*     */     this.secondBatchNos = aSecondBatchNos;
/*     */   }
/*     */   
/*     */   public WcTicket(Ticket aTicket) {
/*     */     super(WurmId.getNextWCCommandId(), (short)18);
/*     */     this.type = 3;
/*     */     this.ticket = aTicket;
/*     */     this.ticketAction = null;
/*     */     this.sendActions = true;
/*     */   }
/*     */   
/*     */   public WcTicket(long aId, Ticket aTicket, int aNumbActions, TicketAction aTicketAction) {
/*     */     super(aId, (short)18);
/*     */     this.type = 3;
/*     */     this.ticket = aTicket;
/*     */     if (aNumbActions > 1) {
/*     */       this.ticketAction = null;
/*     */     } else {
/*     */       this.ticketAction = aTicketAction;
/*     */     } 
/*     */     this.sendActions = (aNumbActions > 0);
/*     */   }
/*     */   
/*     */   public WcTicket(Ticket aTicket, TicketAction aTicketAction) {
/*     */     super(WurmId.getNextWCCommandId(), (short)18);
/*     */     this.type = 3;
/*     */     this.ticket = aTicket;
/*     */     this.ticketAction = aTicketAction;
/*     */     this.sendActions = true;
/*     */   }
/*     */   
/*     */   public WcTicket(long aDate) {
/*     */     super(WurmId.getNextWCCommandId(), (short)18);
/*     */     this.type = 4;
/*     */     this.checkDate = aDate;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\webinterface\WcTicket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */