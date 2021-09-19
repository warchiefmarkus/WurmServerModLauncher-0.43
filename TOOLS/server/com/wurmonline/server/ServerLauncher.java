/*     */ package com.wurmonline.server;
/*     */ 
/*     */ import com.wurmonline.server.console.CommandReader;
/*     */ import com.wurmonline.server.items.CreationEntryCreator;
/*     */ import com.wurmonline.server.utils.SimpleArgumentParser;
/*     */ import com.wurmonline.server.webinterface.RegistryStarter;
/*     */ import com.wurmonline.server.webinterface.WebInterfaceImpl;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.rmi.AlreadyBoundException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.logging.ConsoleHandler;
/*     */ import java.util.logging.FileHandler;
/*     */ import java.util.logging.Handler;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.LogRecord;
/*     */ import java.util.logging.Logger;
/*     */ import java.util.logging.SimpleFormatter;
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
/*     */ public final class ServerLauncher
/*     */ {
/*     */   private static final int MAX_BYTES_PER_LOGFILE = 10240000;
/*     */   private static final int NUM_LOG_FILES = 200;
/*     */   Server server;
/*     */   private static final Map<String, String> SPECIALIST_LOGGER_FILES;
/*     */   private static final Map<String, SimpleFormatter> SPECIALIST_LOGGER_FORMATS;
/*     */   
/*     */   static {
/*  63 */     HashMap<String, String> specialistLoggers = new HashMap<>();
/*  64 */     specialistLoggers.put("Cheaters", "cheaters.log");
/*  65 */     specialistLoggers.put("Money", "money.log");
/*  66 */     specialistLoggers.put("Chat", "chat.log");
/*  67 */     specialistLoggers.put("ca-help", "ca-help.log");
/*  68 */     specialistLoggers.put("IntraServer", "IntraServer.log");
/*  69 */     specialistLoggers.put("Reimbursements", "reimbursements.log");
/*  70 */     specialistLoggers.put("stacktraces", "stacktraces.log");
/*  71 */     specialistLoggers.put("deletions", "deletions.log");
/*     */     
/*  73 */     specialistLoggers.put("ItemDebug", "item-debug.log");
/*  74 */     specialistLoggers.put("affinities", "affinities.log");
/*  75 */     SPECIALIST_LOGGER_FILES = Collections.unmodifiableMap(specialistLoggers);
/*     */     
/*  77 */     SimpleFormatter chatFormat = new SimpleFormatter()
/*     */       {
/*     */         private static final String format = "[%1$tF %1$tT] %2$s %n";
/*     */         
/*     */         public synchronized String format(LogRecord record) {
/*  82 */           return String.format("[%1$tF %1$tT] %2$s %n", new Object[] { new Date(record.getMillis()), record.getMessage() });
/*     */         }
/*     */       };
/*  85 */     HashMap<String, SimpleFormatter> specialistFormatters = new HashMap<>();
/*  86 */     specialistFormatters.put("ca-help", chatFormat);
/*  87 */     specialistFormatters.put("Chat", chatFormat);
/*  88 */     SPECIALIST_LOGGER_FORMATS = Collections.unmodifiableMap(specialistFormatters);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  95 */   private static final Map<String, Logger> SPECIALIST_LOGGERS = new HashMap<>(SPECIALIST_LOGGER_FILES.size());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean started;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String ARG_PLAYER_SERVER = "ps";
/*     */ 
/*     */ 
/*     */   
/*     */   private static final Set<String> ALLOWED_ARGUMENTS;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServerLauncher() {
/* 115 */     this.started = false;
/*     */     createLoggers(); } public final Server getServer() {
/*     */     return this.server;
/*     */   } public void runServer() throws IOException {
/* 119 */     runServer(false, false);
/*     */   } public final boolean wasStarted() {
/*     */     return this.started;
/*     */   }
/*     */   public void runServer(boolean ps, boolean isOfflineServer) throws IOException {
/* 124 */     this.server = Server.getInstance();
/* 125 */     this.server.setIsPS(ps);
/* 126 */     this.server.steamHandler.setIsOfflienServer(isOfflineServer);
/*     */ 
/*     */     
/*     */     try {
/* 130 */       this.server.startRunning();
/* 131 */       this.started = true;
/*     */     }
/* 133 */     catch (Exception ex) {
/*     */       
/* 135 */       System.out.println(ex.getMessage());
/* 136 */       ex.printStackTrace();
/* 137 */       this.server.shutDown("Problem running the server - " + ex.getMessage(), ex);
/*     */     } 
/* 139 */     CreationEntryCreator.createCreationEntries();
/* 140 */     if (Constants.useIncomingRMI) {
/*     */       
/*     */       try {
/* 143 */         InetAddress byAddress = InetAddress.getByAddress(this.server.getInternalIp());
/* 144 */         RegistryStarter.startRegistry(new WebInterfaceImpl(Servers.localServer.REGISTRATION_PORT), byAddress, Servers.localServer.RMI_PORT);
/*     */         
/* 146 */         System.out.println("RMI listening on " + byAddress + ':' + Servers.localServer.RMI_PORT);
/* 147 */         System.out.println("RMI Registry listening on " + byAddress + ':' + Servers.localServer.REGISTRATION_PORT);
/*     */       }
/* 149 */       catch (AlreadyBoundException abe) {
/*     */         
/* 151 */         System.out.println("The port " + Servers.localServer.RMI_PORT + " is already bound./n Registry RMI communication won't work.");
/*     */       } 
/*     */     } else {
/*     */       
/* 155 */       System.out.println("Incoming RMI is disabled");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void stopLoggers() {
/* 161 */     Logger logger = Logger.getLogger("com.wurmonline");
/* 162 */     if (logger != null) {
/* 163 */       removeLoggerHandlers(logger);
/*     */     }
/* 165 */     for (String loggerName : SPECIALIST_LOGGERS.keySet()) {
/* 166 */       logger = Logger.getLogger(loggerName);
/* 167 */       if (logger != null) {
/* 168 */         removeLoggerHandlers(logger);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void createLoggers() {
/* 175 */     Logger logger = Logger.getLogger("com.wurmonline");
/* 176 */     String loggingProperty = System.getProperty("java.util.logging.config.file", null);
/* 177 */     if (loggingProperty == null) {
/*     */       
/* 179 */       System.out.println("java.util.logging.config.file system property is not set so hardcoding logging");
/* 180 */       logger.setUseParentHandlers(false);
/* 181 */       Handler[] h = logger.getHandlers();
/* 182 */       System.out.println("com.wurmonline logger handlers: " + Arrays.toString((Object[])h));
/* 183 */       for (int i = 0; i != h.length; i++)
/*     */       {
/* 185 */         logger.removeHandler(h[i]);
/*     */       }
/*     */       
/*     */       try {
/* 189 */         String logsPath = createLogPath();
/*     */         
/* 191 */         FileHandler fh = new FileHandler(logsPath + "wurm.log", 10240000, 200, true);
/* 192 */         fh.setFormatter(new SimpleFormatter());
/* 193 */         logger.addHandler(fh);
/* 194 */         if (Constants.devmode) {
/* 195 */           logger.addHandler(new ConsoleHandler());
/*     */         }
/* 197 */       } catch (IOException ie) {
/*     */         
/* 199 */         System.err.println("no redirection possible, stopping server");
/* 200 */         System.exit(1);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 205 */       System.out.println("java.util.logging.config.file system property is set to " + loggingProperty);
/* 206 */       System.out.println("com.wurmonline logger level: " + logger.getLevel());
/* 207 */       System.out.println("com.wurmonline logger UseParentHandlers: " + logger.getUseParentHandlers());
/*     */     } 
/* 209 */     logger.log(Level.OFF, "\n----------------------------------------------------------------\nWurm Server logging started at " + new Date() + "\n----------------------------------------------------------------");
/*     */ 
/*     */ 
/*     */     
/* 213 */     for (String loggerName : SPECIALIST_LOGGER_FILES.keySet()) {
/* 214 */       SPECIALIST_LOGGERS.put(loggerName, 
/* 215 */           createLoggerFileHandlers(loggerName, SPECIALIST_LOGGER_FILES.get(loggerName)));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void removeLoggerHandlers(Logger logger) {
/* 224 */     for (Handler handler : logger.getHandlers()) {
/* 225 */       if (handler != null) {
/*     */         try {
/* 227 */           handler.flush();
/* 228 */         } catch (Exception exception) {}
/*     */ 
/*     */         
/*     */         try {
/* 232 */           handler.close();
/* 233 */         } catch (Exception exception) {}
/*     */ 
/*     */         
/* 236 */         logger.removeHandler(handler);
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
/*     */ 
/*     */   
/*     */   private static Logger createLoggerFileHandlers(String loggerName, String logFileName) {
/* 252 */     Logger logger = Logger.getLogger(loggerName);
/* 253 */     logger.setUseParentHandlers(false);
/* 254 */     removeLoggerHandlers(logger);
/*     */     
/*     */     try {
/* 257 */       String logsPath = createLogPath();
/*     */       
/* 259 */       FileHandler fh = new FileHandler(logsPath + logFileName, 10240000, 200, true);
/* 260 */       fh.setFormatter(new SimpleFormatter());
/* 261 */       if (SPECIALIST_LOGGER_FORMATS.containsKey(loggerName))
/* 262 */         fh.setFormatter(SPECIALIST_LOGGER_FORMATS.get(loggerName)); 
/* 263 */       logger.addHandler(fh);
/* 264 */       if (Constants.devmode) {
/* 265 */         logger.addHandler(new ConsoleHandler());
/*     */       }
/* 267 */     } catch (IOException ie) {
/*     */       
/* 269 */       System.err.println("no redirection possible, stopping server");
/* 270 */       System.exit(1);
/*     */     } 
/* 272 */     return logger;
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
/*     */   static String getServerStartBanner() {
/* 284 */     StringBuilder lBuilder = new StringBuilder(1024);
/* 285 */     lBuilder.append("\n========================================================================================================\n\n");
/* 286 */     lBuilder.append("888       888                                     .d8888b. \n");
/* 287 */     lBuilder.append("888   o   888                                     d88P  Y88b  \n");
/* 288 */     lBuilder.append("888  d8b  888                                     Y88b.  \n");
/* 289 */     lBuilder.append("888 d888b 888 888  888 888d888 88888b.d88b.        'Y888b.    .d88b.  888d888 888  888  .d88b.  888d888 \n");
/* 290 */     lBuilder.append("888d88888b888 888  888 888P   888 '888 '88b          'Y88b. d8P  Y8b 888P'   888  888 d8P  Y8b 888P'   \n");
/* 291 */     lBuilder.append("88888P Y88888 888  888 888     888  888  888            '888 88888888 888     Y88  88P 88888888 888    \n");
/* 292 */     lBuilder.append("8888P   Y8888 Y88b 888 888     888  888  888      Y88b  d88P Y8b.     888      Y8bd8P  Y8b.     888   \n");
/* 293 */     lBuilder.append("888P     Y888  'Y88888 888     888  888  888       'Y8888P'   'Y8888  888       Y88P    'Y8888  888 \n");
/* 294 */     lBuilder.append("\n========================================================================================================\n");
/* 295 */     return lBuilder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/* 302 */     HashSet<String> allowedArguments = new HashSet<>();
/* 303 */     allowedArguments.add("ps");
/* 304 */     ALLOWED_ARGUMENTS = Collections.unmodifiableSet(allowedArguments);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] args) throws IOException {
/*     */     try {
/* 315 */       SimpleArgumentParser parser = new SimpleArgumentParser(args, ALLOWED_ARGUMENTS);
/* 316 */       if (parser.hasUnknownOptions()) {
/* 317 */         System.exit(1);
/*     */       }
/* 319 */       boolean isPs = parser.hasFlag("ps");
/*     */       
/* 321 */       Runtime lRuntime = Runtime.getRuntime();
/* 322 */       System.out.println(getServerStartBanner());
/* 323 */       System.out.println("Wurm Server application started at " + new Date());
/* 324 */       System.out.println("Operating system: " + System.getProperty("os.name") + " (arch: " + 
/* 325 */           System.getProperty("os.arch") + ", version: " + System.getProperty("os.version") + ")");
/* 326 */       System.out.println("Java version: " + System.getProperty("java.version"));
/* 327 */       System.out.println("Java home: " + System.getProperty("java.home"));
/* 328 */       System.out.println("Java vendor: " + System.getProperty("java.vendor") + " (" + 
/* 329 */           System.getProperty("java.vendor.url") + ")");
/* 330 */       System.out.println("Available CPUs: " + lRuntime.availableProcessors());
/* 331 */       System.out.println("Java Classpath: " + System.getProperty("java.class.path"));
/* 332 */       System.out.println("Free memory: " + (lRuntime.freeMemory() / 1048576L) + " MB, Total memory: " + (lRuntime
/* 333 */           .totalMemory() / 1048576L) + " MB, Max memory: " + (lRuntime.maxMemory() / 1048576L) + " MB");
/*     */       
/* 335 */       System.out.println("\n==================================================================\n");
/*     */       
/* 337 */       ServerLauncher lServerLauncher = new ServerLauncher();
/* 338 */       lServerLauncher.runServer(isPs, false);
/*     */ 
/*     */       
/* 341 */       (new Thread((Runnable)new CommandReader(lServerLauncher.getServer(), System.in), "Console Command Reader")).start();
/*     */     }
/* 343 */     catch (Exception ex) {
/*     */       
/* 345 */       throw ex;
/*     */     }
/*     */     finally {
/*     */       
/* 349 */       System.out.println("\n==================================================================\n");
/* 350 */       System.out.println("Wurm Server launcher finished at " + new Date());
/* 351 */       System.out.println("\n==================================================================\n");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String createLogPath() {
/* 361 */     String logsPath = Constants.dbHost + "/Logs/";
/* 362 */     File newDirectory = new File(logsPath);
/* 363 */     if (!newDirectory.exists())
/*     */     {
/* 365 */       newDirectory.mkdirs();
/*     */     }
/*     */     
/* 368 */     return logsPath;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\ServerLauncher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */