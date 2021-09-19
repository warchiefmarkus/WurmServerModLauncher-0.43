/*     */ package com.wurmonline.server.webinterface;
/*     */ 
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.epic.EpicServerStatus;
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
/*     */ public class WcCreateEpicMission
/*     */   extends WebCommand
/*     */   implements MiscConstants
/*     */ {
/*  39 */   private static final Logger logger = Logger.getLogger(WcCreateEpicMission.class.getName());
/*  40 */   private int collectiblesToWin = 5;
/*  41 */   private int collectiblesForWurmToWin = 8;
/*     */   private boolean spawnPointRequiredToWin = true;
/*  43 */   private int hexNumRequiredToWin = 0;
/*  44 */   private int scenarioNumber = 0;
/*  45 */   private int reasonPlusEffect = 0;
/*  46 */   private String scenarioName = "";
/*  47 */   private String scenarioQuest = "";
/*  48 */   public long entityNumber = 0L;
/*  49 */   private String entityName = "unknown";
/*  50 */   private int difficulty = 0;
/*  51 */   private long maxTimeSeconds = 0L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean destroyPreviousMissions = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WcCreateEpicMission(long aId, byte[] _data) {
/*  84 */     super(aId, (short)11, _data);
/*  85 */     this.isRestrictedEpic = true;
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
/*  96 */     return true;
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
/* 107 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 108 */     DataOutputStream dos = null;
/* 109 */     byte[] barr = null;
/*     */     
/*     */     try {
/* 112 */       dos = new DataOutputStream(bos);
/* 113 */       dos.writeUTF(this.scenarioName);
/* 114 */       dos.writeInt(this.scenarioNumber);
/* 115 */       dos.writeInt(this.reasonPlusEffect);
/* 116 */       dos.writeInt(this.collectiblesToWin);
/* 117 */       dos.writeInt(this.collectiblesForWurmToWin);
/* 118 */       dos.writeBoolean(this.spawnPointRequiredToWin);
/* 119 */       dos.writeInt(this.hexNumRequiredToWin);
/* 120 */       dos.writeUTF(this.scenarioQuest);
/* 121 */       dos.writeLong(this.entityNumber);
/* 122 */       dos.writeInt(this.difficulty);
/* 123 */       dos.writeUTF(this.entityName);
/* 124 */       dos.writeLong(this.maxTimeSeconds);
/* 125 */       dos.writeBoolean(this.destroyPreviousMissions);
/* 126 */       dos.flush();
/* 127 */       dos.close();
/*     */     }
/* 129 */     catch (Exception ex) {
/*     */       
/* 131 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 135 */       StreamUtilities.closeOutputStreamIgnoreExceptions(dos);
/* 136 */       barr = bos.toByteArray();
/* 137 */       StreamUtilities.closeOutputStreamIgnoreExceptions(bos);
/* 138 */       setData(barr);
/*     */     } 
/* 140 */     return barr;
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
/* 151 */     (new Thread()
/*     */       {
/*     */         
/*     */         public void run()
/*     */         {
/* 156 */           DataInputStream dis = null;
/*     */           
/*     */           try {
/* 159 */             dis = new DataInputStream(new ByteArrayInputStream(WcCreateEpicMission.this.getData()));
/* 160 */             WcCreateEpicMission.this.scenarioName = dis.readUTF();
/* 161 */             WcCreateEpicMission.this.scenarioNumber = dis.readInt();
/* 162 */             WcCreateEpicMission.this.reasonPlusEffect = dis.readInt();
/* 163 */             WcCreateEpicMission.this.collectiblesToWin = dis.readInt();
/* 164 */             WcCreateEpicMission.this.collectiblesForWurmToWin = dis.readInt();
/* 165 */             WcCreateEpicMission.this.spawnPointRequiredToWin = dis.readBoolean();
/* 166 */             WcCreateEpicMission.this.hexNumRequiredToWin = dis.readInt();
/* 167 */             WcCreateEpicMission.this.scenarioQuest = dis.readUTF();
/* 168 */             WcCreateEpicMission.this.entityNumber = dis.readLong();
/* 169 */             WcCreateEpicMission.this.difficulty = dis.readInt();
/* 170 */             WcCreateEpicMission.this.entityName = dis.readUTF();
/* 171 */             WcCreateEpicMission.this.maxTimeSeconds = dis.readLong();
/* 172 */             WcCreateEpicMission.this.destroyPreviousMissions = dis.readBoolean();
/* 173 */             if (EpicServerStatus.getCurrentScenario().getScenarioNumber() != WcCreateEpicMission.this.scenarioNumber) {
/*     */               
/* 175 */               EpicServerStatus.getCurrentScenario().saveScenario(false);
/* 176 */               EpicServerStatus.getCurrentScenario().setScenarioQuest(WcCreateEpicMission.this.scenarioQuest);
/* 177 */               EpicServerStatus.getCurrentScenario().setScenarioName(WcCreateEpicMission.this.scenarioName);
/* 178 */               EpicServerStatus.getCurrentScenario().setScenarioNumber(WcCreateEpicMission.this.scenarioNumber);
/* 179 */               EpicServerStatus.getCurrentScenario().setReasonPlusEffect(WcCreateEpicMission.this.reasonPlusEffect);
/* 180 */               EpicServerStatus.getCurrentScenario().setCollectiblesToWin(WcCreateEpicMission.this.collectiblesToWin);
/* 181 */               EpicServerStatus.getCurrentScenario().setCollectiblesForWurmToWin(WcCreateEpicMission.this.collectiblesForWurmToWin);
/* 182 */               EpicServerStatus.getCurrentScenario().setHexNumRequiredToWin(WcCreateEpicMission.this.hexNumRequiredToWin);
/* 183 */               EpicServerStatus.getCurrentScenario().saveScenario(true);
/*     */             } 
/* 185 */             EpicServerStatus es = new EpicServerStatus();
/* 186 */             es.generateNewMissionForEpicEntity((int)WcCreateEpicMission.this.entityNumber, WcCreateEpicMission.this.entityName, WcCreateEpicMission.this.difficulty, (int)WcCreateEpicMission.this.maxTimeSeconds, WcCreateEpicMission.this
/* 187 */                 .scenarioName, WcCreateEpicMission.this.scenarioNumber, WcCreateEpicMission.this.scenarioQuest, WcCreateEpicMission.this.destroyPreviousMissions);
/*     */           }
/* 189 */           catch (IOException ex) {
/*     */             
/* 191 */             WcCreateEpicMission.logger.log(Level.WARNING, "Unpack exception " + ex.getMessage(), ex);
/*     */           }
/*     */           finally {
/*     */             
/* 195 */             StreamUtilities.closeInputStreamIgnoreExceptions(dis);
/*     */           } 
/*     */         }
/* 198 */       }).start();
/*     */   }
/*     */   
/*     */   public WcCreateEpicMission(long a_id, String scenName, int scenNumber, int reasonEff, int collReq, int collReqWurm, boolean spawnP, int hexNumReq, String questString, long epicEntity, int diff, String epicEntityName, long maxTimeSecs, boolean destroyPrevMissions) {
/*     */     super(a_id, (short)11);
/*     */     this.scenarioName = scenName;
/*     */     this.scenarioNumber = scenNumber;
/*     */     this.reasonPlusEffect = reasonEff;
/*     */     this.collectiblesToWin = collReq;
/*     */     this.collectiblesForWurmToWin = collReqWurm;
/*     */     this.spawnPointRequiredToWin = spawnP;
/*     */     this.hexNumRequiredToWin = hexNumReq;
/*     */     this.scenarioQuest = questString;
/*     */     this.entityNumber = epicEntity;
/*     */     this.difficulty = diff;
/*     */     this.entityName = epicEntityName;
/*     */     this.maxTimeSeconds = maxTimeSecs;
/*     */     this.destroyPreviousMissions = destroyPrevMissions;
/*     */     this.isRestrictedEpic = true;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\webinterface\WcCreateEpicMission.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */