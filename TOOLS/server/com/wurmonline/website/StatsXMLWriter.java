/*     */ package com.wurmonline.website;
/*     */ 
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.ServerEntry;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.WurmCalendar;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileWriter;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerFactory;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class StatsXMLWriter
/*     */ {
/*     */   public static final void createXML(File outputFile) throws Exception {
/*  57 */     DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
/*  58 */     DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
/*     */     
/*  60 */     Document doc = docBuilder.newDocument();
/*  61 */     Element rootElement = doc.createElement("statistics");
/*  62 */     doc.appendChild(rootElement);
/*     */     
/*  64 */     Element timestamp = doc.createElement("timestamp");
/*  65 */     timestamp.setTextContent(System.currentTimeMillis() / 1000L);
/*  66 */     rootElement.appendChild(timestamp);
/*     */     
/*  68 */     Element status = doc.createElement("status");
/*  69 */     status.setTextContent(Servers.localServer.maintaining ? "offline" : ((Server.getMillisToShutDown() > 0L) ? "shutting down" : "online"));
/*     */     
/*  71 */     if (Server.getMillisToShutDown() > 0L)
/*  72 */       timestamp.setAttribute("ttl", (new StringBuilder("" + (Server.getMillisToShutDown() / 1000L))).toString()); 
/*  73 */     rootElement.appendChild(status);
/*     */     
/*  75 */     Element uptime = doc.createElement("uptime");
/*  76 */     uptime.setTextContent(Server.getSecondsUptime());
/*  77 */     rootElement.appendChild(uptime);
/*     */     
/*  79 */     Element wurmtime = doc.createElement("wurmtime");
/*  80 */     wurmtime.setTextContent(WurmCalendar.getTime());
/*  81 */     rootElement.appendChild(wurmtime);
/*     */     
/*  83 */     Element weather = doc.createElement("weather");
/*  84 */     weather.setTextContent(Server.getWeather().getWeatherString(false));
/*  85 */     rootElement.appendChild(weather);
/*     */     
/*  87 */     Element serverselm = doc.createElement("servers");
/*     */     
/*  89 */     ServerEntry[] servers = Servers.getAllServers();
/*     */     
/*  91 */     int epic = 0;
/*  92 */     int epicMax = 0;
/*  93 */     for (ServerEntry entry : servers) {
/*     */       
/*  95 */       if (!entry.EPIC) {
/*     */         
/*  97 */         Element element = doc.createElement("server");
/*  98 */         if (!entry.isLocal) {
/*     */           
/* 100 */           element.setAttribute("name", entry.getName());
/* 101 */           element.setAttribute("players", entry.currentPlayers);
/* 102 */           element.setAttribute("maxplayers", entry.pLimit);
/*     */         }
/*     */         else {
/*     */           
/* 106 */           element.setAttribute("name", entry.getName());
/* 107 */           element.setAttribute("players", 
/* 108 */               Players.getInstance().getNumberOfPlayers());
/* 109 */           element.setAttribute("maxplayers", entry.pLimit);
/*     */         } 
/* 111 */         serverselm.appendChild(element);
/*     */       }
/*     */       else {
/*     */         
/* 115 */         epic += entry.currentPlayers;
/* 116 */         epicMax += entry.pLimit;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 121 */     Element srv = doc.createElement("server");
/* 122 */     srv.setAttribute("name", "Epic cluster");
/* 123 */     srv.setAttribute("players", epic);
/* 124 */     srv.setAttribute("maxplayers", epicMax);
/* 125 */     serverselm.appendChild(srv);
/* 126 */     rootElement.appendChild(serverselm);
/*     */     
/* 128 */     TransformerFactory transformerFactory = TransformerFactory.newInstance();
/* 129 */     Transformer transformer = transformerFactory.newTransformer();
/* 130 */     DOMSource source = new DOMSource(doc);
/* 131 */     StreamResult result = new StreamResult(new BufferedWriter(new FileWriter(outputFile)));
/*     */     
/* 133 */     transformer.transform(source, result);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\website\StatsXMLWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */