/*    */ package com.wurmonline.server.console;
/*    */ 
/*    */ import com.wurmonline.server.Server;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.InputStreamReader;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CommandReader
/*    */   implements Runnable
/*    */ {
/* 17 */   private static final Logger logger = Logger.getLogger(CommandReader.class.getName());
/*    */ 
/*    */ 
/*    */   
/*    */   private final Server server;
/*    */ 
/*    */   
/*    */   private final InputStream inputStream;
/*    */ 
/*    */   
/*    */   private static final String SHUTDOWN = "shutdown";
/*    */ 
/*    */ 
/*    */   
/*    */   public CommandReader(Server server, InputStream inputStream) {
/* 32 */     this.server = server;
/* 33 */     this.inputStream = inputStream;
/*    */   }
/*    */   
/*    */   public void run() {
/*    */     String nextLine;
/* 38 */     logger.info("Starting command reader for console input");
/* 39 */     BufferedReader consoleReader = new BufferedReader(new InputStreamReader(this.inputStream));
/*    */     
/*    */     do {
/*    */       try {
/* 43 */         nextLine = consoleReader.readLine();
/* 44 */         if (nextLine == null) {
/*    */           break;
/*    */         }
/* 47 */         if (nextLine.equals("shutdown")) {
/* 48 */           this.server.shutDown();
/*    */           break;
/*    */         } 
/* 51 */         logger.warning("Unknown command: " + nextLine);
/*    */       }
/* 53 */       catch (IOException e) {
/* 54 */         logger.log(Level.SEVERE, "Can't read from console", e);
/* 55 */         nextLine = null;
/*    */       } 
/* 57 */     } while (nextLine != null);
/* 58 */     logger.info("Console reader exiting.");
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\console\CommandReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */