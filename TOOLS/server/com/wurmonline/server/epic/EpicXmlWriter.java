/*     */ package com.wurmonline.server.epic;
/*     */ 
/*     */ import com.wurmonline.server.Constants;
/*     */ import com.wurmonline.server.Server;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
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
/*     */ public final class EpicXmlWriter
/*     */ {
/*  49 */   private static final Logger logger = Logger.getLogger(EpicXmlWriter.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void dumpEntities(HexMap map) {
/*  61 */     if (logger.isLoggable(Level.FINE))
/*     */     {
/*  63 */       logger.fine("Starting to dump Epic Entities to XML for HexMap: " + map);
/*     */     }
/*  65 */     long start = System.nanoTime();
/*     */ 
/*     */     
/*     */     try {
/*  69 */       EpicEntity[] entities = map.getAllEntities();
/*  70 */       Document document = createEntitiesXmlDocument(entities);
/*  71 */       TransformerFactory transformerFactory = TransformerFactory.newInstance();
/*  72 */       Transformer transformer = transformerFactory.newTransformer();
/*  73 */       DOMSource source = new DOMSource(document);
/*  74 */       File f = new File(Constants.webPath + File.separator + "entities.xml");
/*  75 */       logger.info("Dumping Epic entities to absolute path: " + f.getAbsolutePath());
/*  76 */       f.createNewFile();
/*  77 */       if (f != null)
/*     */       {
/*  79 */         StreamResult result = new StreamResult(new FileOutputStream(f));
/*  80 */         transformer.transform(source, result);
/*     */       }
/*     */     
/*  83 */     } catch (Exception ex) {
/*     */       
/*  85 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/*  89 */       long end = System.nanoTime();
/*  90 */       logger.info("Dumping Epic Entities to XML took " + ((float)(end - start) / 1000000.0F) + " ms");
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
/*     */ 
/*     */   
/*     */   static Document createEntitiesXmlDocument(EpicEntity[] entities) throws ParserConfigurationException {
/* 107 */     String root = "entities";
/* 108 */     DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
/* 109 */     DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
/* 110 */     Document document = documentBuilder.newDocument();
/*     */     
/* 112 */     Element rootElement = document.createElement("entities");
/* 113 */     document.appendChild(rootElement);
/* 114 */     for (EpicEntity entity : entities) {
/*     */       
/* 116 */       Element newElement = document.createElement("entity");
/* 117 */       rootElement.appendChild(newElement);
/* 118 */       createNode("name", entity.getName(), document, newElement);
/* 119 */       if (entity.getMapHex() != null) {
/*     */         
/* 121 */         createNode("hexnumber", String.valueOf(entity.getMapHex().getId()), document, newElement);
/* 122 */         createNode("hexname", entity.getMapHex().getName(), document, newElement);
/*     */       }
/*     */       else {
/*     */         
/* 126 */         createNode("hexnumber", "-1", document, newElement);
/* 127 */         createNode("hexname", "unknown", document, newElement);
/*     */       } 
/* 129 */       if (entity.isDeity() || entity.isWurm()) {
/*     */         
/* 131 */         if (System.currentTimeMillis() < entity.getTimeUntilLeave()) {
/*     */           
/* 133 */           long leaveTime = entity.getTimeUntilLeave() - System.currentTimeMillis();
/* 134 */           createNode("timetoleavehex", Server.getTimeFor(leaveTime), document, newElement);
/*     */         } 
/* 136 */         if (System.currentTimeMillis() < entity.getTimeToNextHex()) {
/*     */           
/* 138 */           long nextTime = entity.getTimeToNextHex() - System.currentTimeMillis();
/* 139 */           createNode("timetonexthex", Server.getTimeFor(nextTime), document, newElement);
/*     */         } 
/*     */       } 
/* 142 */       createNode("location", entity.getLocationStatus(), document, newElement);
/* 143 */       if (!((entity.isSource() || entity.isCollectable()) ? 1 : 0)) {
/*     */         
/* 145 */         createNode("enemy", entity.getEnemyStatus(), document, newElement);
/* 146 */         createNode("strength", String.valueOf(entity.getAttack()), document, newElement);
/* 147 */         createNode("vitality", String.valueOf(entity.getVitality()), document, newElement);
/* 148 */         int colls = entity.countCollectables();
/* 149 */         createNode("collectibles", String.valueOf(colls), document, newElement);
/* 150 */         if (colls > 0)
/* 151 */           createNode("collectiblename", entity.getCollectibleName(), document, newElement); 
/*     */       } 
/* 153 */       if (entity.getCarrier() != null) {
/* 154 */         createNode("carrier", entity.getCarrier().getName(), document, newElement);
/*     */       }
/*     */     } 
/* 157 */     return document;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void createNode(String element, String data, Document document, Element rootElement) {
/* 163 */     Element em = document.createElement(element);
/* 164 */     em.appendChild(document.createTextNode(data));
/* 165 */     rootElement.appendChild(em);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\epic\EpicXmlWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */