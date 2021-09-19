/*     */ package com.wurmonline.server.webinterface;
/*     */ 
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.ServerEntry;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.CreatureMove;
/*     */ import com.wurmonline.server.epic.HexMap;
/*     */ import com.wurmonline.server.epic.Valrei;
/*     */ import com.wurmonline.server.zones.FaithZone;
/*     */ import com.wurmonline.shared.exceptions.WurmServerException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.math.BigInteger;
/*     */ import java.net.MalformedURLException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.rmi.Naming;
/*     */ import java.rmi.NotBoundException;
/*     */ import java.rmi.RemoteException;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.util.LinkedList;
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
/*     */ public class WebInterfaceTest
/*     */   implements MiscConstants
/*     */ {
/*  48 */   private WebInterface wurm = null;
/*     */ 
/*     */   
/*  51 */   private static Logger logger = Logger.getLogger(WebInterfaceTest.class.getName());
/*  52 */   private static FaithZone[][] surfaceDomains = new FaithZone[40][40];
/*     */   
/*  54 */   private final String intraServerPassword = "";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void connect(String ip) throws MalformedURLException, RemoteException, NotBoundException {
/*  63 */     connect(ip, "7220");
/*     */   }
/*     */ 
/*     */   
/*     */   private void connect(String ip, String port) throws MalformedURLException, RemoteException, NotBoundException {
/*  68 */     if (this.wurm == null) {
/*     */       
/*  70 */       String name = "//" + ip + ":" + port + "/WebInterface";
/*  71 */       this.wurm = (WebInterface)Naming.lookup(name);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void shutDown(String host, String port, String user, String pass) {
/*  78 */     this.wurm = null;
/*     */     
/*     */     try {
/*  81 */       connect(host, port);
/*  82 */       if (this.wurm != null)
/*     */       {
/*  84 */         this.wurm.shutDown("", user, pass, "Console initiated shutdown.", 30);
/*     */         
/*  86 */         System.out.println("Two. Host " + host + " shutting down.");
/*     */       }
/*     */     
/*  89 */     } catch (Exception ex) {
/*     */       
/*  91 */       logger.log(Level.INFO, "failed to shut down localhost");
/*     */     } 
/*     */   }
/*     */   
/*     */   public void globalShutdown(String reason, int time, String user, String pass) {
/*  96 */     if (Servers.localServer != Servers.loginServer) {
/*     */       
/*  98 */       System.out.println("You must initiate a global shutdown from " + Servers.loginServer.getName() + ".");
/*     */       
/*     */       return;
/*     */     } 
/* 102 */     for (ServerEntry server : Servers.getAllServers()) {
/*     */       
/* 104 */       this.wurm = null;
/* 105 */       System.out.println("Sending shutdown command to " + server.getName() + " @ " + server.INTRASERVERADDRESS);
/*     */       
/*     */       try {
/* 108 */         connect(server.INTRASERVERADDRESS);
/* 109 */         if (this.wurm != null) {
/* 110 */           this.wurm.shutDown(server.INTRASERVERPASSWORD, user, pass, reason, time);
/*     */         } else {
/* 112 */           System.out.println("Failed to shutdown " + server.getName());
/*     */         } 
/* 114 */       } catch (Exception e) {
/*     */         
/* 116 */         System.out.println("Failed to shutdown " + server.getName());
/* 117 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void shutdownAll(String reason, int time) throws MalformedURLException, RemoteException, NotBoundException {}
/*     */ 
/*     */   
/* 127 */   private static final LinkedList<CreatureMove> list = new LinkedList<>();
/*     */   
/* 129 */   private static final LinkedList<CreatureMove> list2 = new LinkedList<>();
/*     */ 
/*     */   
/*     */   private CreatureMove getMove(int ts) {
/* 133 */     for (CreatureMove c : list) {
/*     */       
/* 135 */       if (c.timestamp == ts)
/* 136 */         return c; 
/*     */     } 
/* 138 */     for (CreatureMove c : list2) {
/*     */       
/* 140 */       if (c.timestamp == ts)
/* 141 */         return c; 
/*     */     } 
/* 143 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final double getModifiedEffect(double eff) {
/* 148 */     return (10000.0D - (100.0D - eff) * (100.0D - eff)) / 100.0D;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void runEpic() {
/* 153 */     (new Thread()
/*     */       {
/* 155 */         private final HexMap val = (HexMap)new Valrei();
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         private boolean loaded = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public void run() {
/* 167 */           if (!this.loaded) {
/*     */             
/* 169 */             this.val.loadAllEntities();
/* 170 */             this.loaded = true;
/*     */           } 
/*     */           
/*     */           while (true) {
/*     */             try {
/*     */               while (true) {
/* 176 */                 sleep(20L);
/* 177 */                 this.val.pollAllEntities(true);
/*     */               }  break;
/* 179 */             } catch (Exception ex) {
/*     */               
/* 181 */               WebInterfaceTest.logger.log(Level.INFO, ex.getMessage(), ex);
/*     */             } 
/*     */           } 
/*     */         }
/* 185 */       }).start();
/*     */   }
/*     */ 
/*     */   
/*     */   public static String encryptMD5(String plaintext) throws Exception {
/* 190 */     MessageDigest md = null;
/*     */     
/*     */     try {
/* 193 */       md = MessageDigest.getInstance("MD5");
/*     */     }
/* 195 */     catch (NoSuchAlgorithmException e) {
/*     */       
/* 197 */       throw new WurmServerException("No such algorithm 'MD5'", e);
/*     */     } 
/*     */     
/*     */     try {
/* 201 */       md.update(plaintext.getBytes("UTF-8"));
/*     */     }
/* 203 */     catch (UnsupportedEncodingException e) {
/*     */       
/* 205 */       throw new WurmServerException("No such encoding: UTF-8", e);
/*     */     } 
/* 207 */     byte[] raw = md.digest();
/* 208 */     BigInteger bi = new BigInteger(1, raw);
/* 209 */     String hash = bi.toString(16);
/* 210 */     return hash;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] args) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public static String bytesToStringUTFCustom(byte[] bytes) {
/* 221 */     char[] buffer = new char[bytes.length >> 1];
/*     */     
/* 223 */     for (int i = 0; i < buffer.length; i++) {
/*     */ 
/*     */       
/* 226 */       int bpos = i << 1;
/*     */       
/* 228 */       char c = (char)(((bytes[bpos] & 0xFF) << 8) + (bytes[bpos + 1] & 0xFF));
/*     */       
/* 230 */       buffer[i] = c;
/*     */     } 
/*     */ 
/*     */     
/* 234 */     return new String(buffer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String bytesToStringUTFNIO(byte[] bytes) {
/* 241 */     CharBuffer cBuffer = ByteBuffer.wrap(bytes).asCharBuffer();
/*     */     
/* 243 */     return cBuffer.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void printZone(int tilex, int tiley) {
/* 249 */     System.out.println(surfaceDomains[tilex >> 3][tiley >> 3].getStartX() + ", " + surfaceDomains[tilex >> 3][tiley >> 3]
/* 250 */         .getStartY() + ":" + surfaceDomains[tilex >> 3][tiley >> 3]
/* 251 */         .getCenterX() + ", " + surfaceDomains[tilex >> 3][tiley >> 3]
/* 252 */         .getCenterY());
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getDir(int ctx, int cty, int targetX, int targetY) {
/* 257 */     double newrot = Math.atan2(((targetY << 2) + 2 - (cty << 2) + 2), ((targetX << 2) + 2 - (ctx << 2) + 2));
/* 258 */     float attAngle = (float)(newrot * 57.29577951308232D) + 90.0F;
/* 259 */     attAngle = Creature.normalizeAngle(attAngle);
/* 260 */     float degree = 22.5F;
/* 261 */     if (attAngle >= 337.5D || attAngle < 22.5F) {
/* 262 */       return 0;
/*     */     }
/*     */     
/* 265 */     for (int x = 0; x < 8; x++) {
/*     */       
/* 267 */       if (attAngle < 22.5F + (45 * x))
/*     */       {
/* 269 */         return x;
/*     */       }
/*     */     } 
/*     */     
/* 273 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\webinterface\WebInterfaceTest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */