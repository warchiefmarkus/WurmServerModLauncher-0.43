/*     */ package com.wurmonline.server.webinterface;
/*     */ 
/*     */ import com.wurmonline.server.Message;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.ServerEntry;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.WurmId;
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
/*     */ public final class WcCAHelpGroupMessage
/*     */   extends WebCommand
/*     */   implements MiscConstants
/*     */ {
/*  44 */   private static final Logger logger = Logger.getLogger(WcCAHelpGroupMessage.class.getName());
/*     */   
/*  46 */   private byte groupId = -1;
/*  47 */   private byte kingdom = 4;
/*  48 */   private String name = "";
/*  49 */   private String msg = "";
/*     */   private boolean emote = false;
/*  51 */   private int colourR = -1;
/*  52 */   private int colourG = -1;
/*  53 */   private int colourB = -1;
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
/*     */   public WcCAHelpGroupMessage(byte caHelpGroup, byte kingdom, String playerName, String message, boolean aEmote, int red, int green, int blue) {
/*  65 */     super(WurmId.getNextWCCommandId(), (short)23);
/*  66 */     this.groupId = caHelpGroup;
/*  67 */     this.kingdom = kingdom;
/*  68 */     this.name = playerName;
/*  69 */     this.msg = message;
/*  70 */     this.emote = aEmote;
/*  71 */     this.colourR = red;
/*  72 */     this.colourG = green;
/*  73 */     this.colourB = blue;
/*     */   }
/*     */ 
/*     */   
/*     */   public WcCAHelpGroupMessage(byte caHelpGroup) {
/*  78 */     super(WurmId.getNextWCCommandId(), (short)23);
/*  79 */     this.groupId = caHelpGroup;
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
/*     */   WcCAHelpGroupMessage(long aId, byte[] _data) {
/*  92 */     super(aId, (short)23, _data);
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
/* 103 */     return false;
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
/*     */   public byte[] encode() {
/* 116 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 117 */     DataOutputStream dos = null;
/* 118 */     byte[] barr = null;
/*     */     
/*     */     try {
/* 121 */       dos = new DataOutputStream(bos);
/* 122 */       dos.writeByte(this.groupId);
/* 123 */       dos.writeByte(this.kingdom);
/* 124 */       dos.writeUTF(this.name);
/* 125 */       dos.writeUTF(this.msg);
/* 126 */       dos.writeBoolean(this.emote);
/* 127 */       dos.writeInt(this.colourR);
/* 128 */       dos.writeInt(this.colourG);
/* 129 */       dos.writeInt(this.colourB);
/* 130 */       dos.flush();
/* 131 */       dos.close();
/*     */     }
/* 133 */     catch (Exception ex) {
/*     */       
/* 135 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 139 */       StreamUtilities.closeOutputStreamIgnoreExceptions(dos);
/* 140 */       barr = bos.toByteArray();
/* 141 */       StreamUtilities.closeOutputStreamIgnoreExceptions(bos);
/* 142 */       setData(barr);
/*     */     } 
/* 144 */     return barr;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() {
/* 151 */     DataInputStream dis = null;
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 156 */       dis = new DataInputStream(new ByteArrayInputStream(getData()));
/* 157 */       this.groupId = dis.readByte();
/* 158 */       this.kingdom = dis.readByte();
/* 159 */       this.name = dis.readUTF();
/* 160 */       this.msg = dis.readUTF();
/* 161 */       this.emote = dis.readBoolean();
/* 162 */       this.colourR = dis.readInt();
/* 163 */       this.colourG = dis.readInt();
/* 164 */       this.colourB = dis.readInt();
/*     */       
/* 166 */       if (this.groupId != -1 && this.msg.length() > 0)
/*     */       {
/* 168 */         if (Servers.isThisLoginServer()) {
/*     */ 
/*     */           
/* 171 */           WcCAHelpGroupMessage wchgm = new WcCAHelpGroupMessage(this.groupId, this.kingdom, this.name, this.msg, this.emote, this.colourR, this.colourG, this.colourB);
/*     */           
/* 173 */           for (ServerEntry se : Servers.getAllServers()) {
/*     */ 
/*     */             
/* 176 */             if (se.getCAHelpGroup() == this.groupId && se.getId() != Servers.getLocalServerId() && se
/* 177 */               .getId() != WurmId.getOrigin(getWurmId()))
/*     */             {
/* 179 */               wchgm.sendToServer(se.getId());
/*     */             }
/*     */           } 
/*     */         } 
/*     */       }
/*     */       
/* 185 */       if (this.msg.length() == 0) {
/*     */ 
/*     */         
/* 188 */         Servers.localServer.updateCAHelpGroup(this.groupId);
/*     */       }
/* 190 */       else if (Servers.localServer.getCAHelpGroup() == this.groupId) {
/*     */ 
/*     */         
/* 193 */         String chan = Players.getKingdomHelpChannelName(this.kingdom);
/* 194 */         if (chan.length() > 0) {
/*     */           Message mess;
/*     */           
/* 197 */           if (this.emote) {
/* 198 */             mess = new Message(null, (byte)6, chan, this.msg, this.colourR, this.colourG, this.colourB);
/*     */           } else {
/* 200 */             mess = new Message(null, (byte)12, chan, "<" + this.name + "> " + this.msg, this.colourR, this.colourG, this.colourB);
/*     */           } 
/* 202 */           if (this.kingdom == 4) {
/* 203 */             Players.getInstance().sendPaMessage(mess);
/*     */           } else {
/* 205 */             Players.getInstance().sendCaMessage(this.kingdom, mess);
/*     */           } 
/*     */         } 
/*     */       } 
/* 209 */     } catch (IOException ex) {
/*     */       
/* 211 */       logger.log(Level.WARNING, "Unpack exception " + ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 215 */       StreamUtilities.closeInputStreamIgnoreExceptions(dis);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\webinterface\WcCAHelpGroupMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */