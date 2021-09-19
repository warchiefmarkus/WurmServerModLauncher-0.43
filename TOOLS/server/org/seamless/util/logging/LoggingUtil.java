/*     */ package org.seamless.util.logging;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.logging.Handler;
/*     */ import java.util.logging.LogManager;
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
/*     */ public class LoggingUtil
/*     */ {
/*     */   public static final String DEFAULT_CONFIG = "default-logging.properties";
/*     */   
/*     */   public static void loadDefaultConfiguration() throws Exception {
/*  60 */     loadDefaultConfiguration(null);
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
/*     */   public static void loadDefaultConfiguration(InputStream is) throws Exception {
/*  74 */     if (System.getProperty("java.util.logging.config.file") != null)
/*     */       return; 
/*  76 */     if (is == null)
/*     */     {
/*  78 */       is = Thread.currentThread().getContextClassLoader().getResourceAsStream("default-logging.properties");
/*     */     }
/*     */     
/*  81 */     if (is == null)
/*     */       return; 
/*  83 */     List<String> handlerNames = new ArrayList<String>();
/*     */     
/*  85 */     LogManager.getLogManager().readConfiguration(spliceHandlers(is, handlerNames));
/*     */ 
/*     */ 
/*     */     
/*  89 */     Handler[] handlers = instantiateHandlers(handlerNames);
/*  90 */     resetRootHandler(handlers);
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
/*     */   public static Handler[] instantiateHandlers(List<String> handlerNames) throws Exception {
/* 106 */     List<Handler> list = new ArrayList<Handler>();
/* 107 */     for (String handlerName : handlerNames) {
/* 108 */       list.add((Handler)Thread.currentThread().getContextClassLoader().loadClass(handlerName).newInstance());
/*     */     }
/*     */ 
/*     */     
/* 112 */     return list.<Handler>toArray(new Handler[list.size()]);
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
/*     */   
/*     */   public static InputStream spliceHandlers(InputStream is, List<String> handlers) throws IOException {
/* 131 */     Properties props = new Properties();
/* 132 */     props.load(is);
/*     */ 
/*     */     
/* 135 */     StringBuilder sb = new StringBuilder();
/* 136 */     List<String> handlersProperties = new ArrayList<String>();
/* 137 */     for (Map.Entry<Object, Object> entry : props.entrySet()) {
/*     */ 
/*     */       
/* 140 */       if (entry.getKey().equals("handlers")) {
/* 141 */         handlersProperties.add(entry.getValue().toString()); continue;
/*     */       } 
/* 143 */       sb.append(entry.getKey()).append("=").append(entry.getValue()).append("\n");
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 148 */     for (String handlersProperty : handlersProperties) {
/* 149 */       String[] handlerClasses = handlersProperty.trim().split(" ");
/* 150 */       for (String handlerClass : handlerClasses) {
/* 151 */         handlers.add(handlerClass.trim());
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 156 */     return new ByteArrayInputStream(sb.toString().getBytes("ISO-8859-1"));
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
/*     */   public static void resetRootHandler(Handler... h) {
/* 169 */     Logger rootLogger = LogManager.getLogManager().getLogger("");
/* 170 */     Handler[] handlers = rootLogger.getHandlers();
/* 171 */     for (Handler handler : handlers) {
/* 172 */       rootLogger.removeHandler(handler);
/*     */     }
/* 174 */     for (Handler handler : h) {
/* 175 */       if (handler != null)
/* 176 */         LogManager.getLogManager().getLogger("").addHandler(handler); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamles\\util\logging\LoggingUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */