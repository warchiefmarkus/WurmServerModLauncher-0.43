/*     */ package com.wurmonline.server;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.lang.annotation.ElementType;
/*     */ import java.lang.annotation.Retention;
/*     */ import java.lang.annotation.RetentionPolicy;
/*     */ import java.lang.annotation.Target;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
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
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class XMLSerializer
/*     */ {
/*  57 */   private static Logger logger = Logger.getLogger(XMLSerializer.class.getName());
/*     */   
/*     */   private static final String baseDirectory = "wurmDB";
/*     */   private static final String subDirectory = "test";
/*     */   private static final String subDirectoryDirectory = "base";
/*  62 */   private String fileName = "xmlTest.xml";
/*     */   private static final String dotXML = ".xml";
/*  64 */   private final Object[] emptyObjectArray = new Object[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LinkedList<Field> getSaveFields() {
/*  80 */     LinkedList<Field> result = new LinkedList<>();
/*  81 */     for (Class<?> c = getClass(); c != null; c = c.getSuperclass()) {
/*     */       
/*  83 */       Field[] fields = c.getDeclaredFields();
/*  84 */       for (Field classField : fields) {
/*     */         
/*  86 */         if (classField.getAnnotation(Saved.class) != null)
/*  87 */           result.add(classField); 
/*     */       } 
/*     */     } 
/*  90 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, Field> getSaveFieldsMap() {
/*  95 */     ConcurrentHashMap<String, Field> result = new ConcurrentHashMap<>();
/*  96 */     for (Class<?> c = getClass(); c != null; c = c.getSuperclass()) {
/*     */       
/*  98 */       Field[] fields = c.getDeclaredFields();
/*  99 */       for (Field classField : fields) {
/*     */         
/* 101 */         if (classField.getAnnotation(Saved.class) != null)
/* 102 */           result.put(classField.getName(), classField); 
/*     */       } 
/*     */     } 
/* 105 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean saveXML() {
/* 110 */     LinkedList<Field> result = getSaveFields();
/* 111 */     boolean saved = saveToDisk(result);
/* 112 */     return saved;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean saveToDisk(LinkedList<Field> result) {
/* 118 */     long start = System.nanoTime();
/*     */ 
/*     */     
/*     */     try {
/* 122 */       Document document = createFieldsXmlDocument(result);
/* 123 */       TransformerFactory transformerFactory = TransformerFactory.newInstance();
/* 124 */       Transformer transformer = transformerFactory.newTransformer();
/* 125 */       DOMSource source = new DOMSource(document);
/* 126 */       File dir = getRootDir();
/* 127 */       dir.mkdirs();
/* 128 */       File file = new File(dir + File.separator + this.fileName);
/* 129 */       logger.info("Dumping fields to absolute path: " + file.getAbsolutePath());
/* 130 */       file.createNewFile();
/* 131 */       if (file != null)
/*     */       {
/* 133 */         StreamResult sresult = new StreamResult(new FileOutputStream(file));
/* 134 */         transformer.transform(source, sresult);
/*     */       }
/*     */     
/* 137 */     } catch (Exception ex) {
/*     */       
/* 139 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/* 140 */       return false;
/*     */     }
/*     */     finally {
/*     */       
/* 144 */       long end = System.nanoTime();
/* 145 */       logger.info("Dumping fields to XML took " + ((float)(end - start) / 1000000.0F) + " ms");
/*     */     } 
/* 147 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public final File getRootDir() {
/* 152 */     return new File("wurmDB" + File.separator + "test" + File.separator + "base" + File.separator);
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
/*     */   public abstract Object createInstanceAndCallLoadXML(File paramFile);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void loadXML(File file) {
/*     */     try {
/* 174 */       DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
/* 175 */       DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
/* 176 */       Document doc = dBuilder.parse(file);
/*     */ 
/*     */ 
/*     */       
/* 180 */       doc.getDocumentElement().normalize();
/*     */       
/* 182 */       System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
/*     */       
/* 184 */       NodeList nList = doc.getElementsByTagName("FIELD");
/*     */       
/* 186 */       System.out.println("----------------------------");
/* 187 */       Map<String, Field> fieldsMap = getSaveFieldsMap();
/* 188 */       for (int temp = 0; temp < nList.getLength(); temp++) {
/*     */         
/* 190 */         Node nNode = nList.item(temp);
/*     */         
/* 192 */         System.out.println("\nCurrent Element :" + nNode.getNodeName());
/*     */         
/* 194 */         if (nNode.getNodeType() == 1) {
/*     */           
/* 196 */           Element eElement = (Element)nNode;
/* 197 */           String fieldName = eElement.getElementsByTagName("NAME").item(0).getTextContent();
/*     */           
/* 199 */           String value = eElement.getElementsByTagName("VALUE").item(0).getTextContent();
/* 200 */           System.out.println("NAME : " + eElement.getElementsByTagName("NAME").item(0).getTextContent());
/*     */           
/* 202 */           System.out.println("VALUE : " + eElement.getElementsByTagName("VALUE").item(0).getTextContent());
/*     */           
/* 204 */           Field f = fieldsMap.get(fieldName);
/*     */           
/*     */           try {
/* 207 */             if (f != null) {
/*     */               
/* 209 */               f.setAccessible(true);
/* 210 */               if (f.getType() == Boolean.class || f.getType() == boolean.class)
/*     */               {
/* 212 */                 f.set(this, Boolean.valueOf(Boolean.parseBoolean(value)));
/*     */               }
/* 214 */               if (f.getType() == Byte.class || f.getType() == byte.class) {
/*     */                 
/* 216 */                 f.set(this, Byte.valueOf(Byte.parseByte(value)));
/*     */               }
/* 218 */               else if (f.getType() == Short.class || f.getType() == short.class) {
/*     */                 
/* 220 */                 f.set(this, Short.valueOf(Short.parseShort(value)));
/*     */               }
/* 222 */               else if (f.getType() == Integer.class || f.getType() == int.class) {
/*     */                 
/* 224 */                 f.set(this, Integer.valueOf(Integer.parseInt(value)));
/*     */               }
/* 226 */               else if (f.getType() == Float.class || f.getType() == float.class) {
/*     */                 
/* 228 */                 f.set(this, Float.valueOf(Float.parseFloat(value)));
/*     */               }
/* 230 */               else if (f.getType() == Long.class || f.getType() == long.class) {
/*     */                 
/* 232 */                 f.set(this, Long.valueOf(Long.parseLong(value)));
/*     */               }
/* 234 */               else if (f.getType() == String.class) {
/*     */                 
/* 236 */                 f.set(this, value);
/*     */               } 
/*     */             } else {
/*     */               
/* 240 */               logger.log(Level.INFO, "Field " + fieldName + " is missing from Xml and will not be set");
/*     */             } 
/* 242 */           } catch (Exception ex) {
/*     */             
/* 244 */             logger.log(Level.WARNING, fieldName + ":" + ex.getMessage());
/*     */           }
/*     */         
/*     */         } 
/*     */       } 
/* 249 */     } catch (Exception e) {
/*     */       
/* 251 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] loadAllXMLData() {
/* 261 */     Set<Object> loadedObjects = new HashSet();
/* 262 */     File baseDir = new File("wurmDB" + File.separator + "test");
/* 263 */     for (File dir : baseDir.listFiles()) {
/*     */       
/* 265 */       if (dir.isDirectory())
/*     */       {
/* 267 */         for (File f : dir.listFiles()) {
/*     */           
/* 269 */           if (f.isDirectory()) {
/*     */             
/* 271 */             for (File toLoad : f.listFiles())
/*     */             {
/* 273 */               if (!toLoad.isDirectory()) {
/*     */                 
/* 275 */                 if (toLoad.getName().endsWith(".xml"))
/*     */                 {
/* 277 */                   loadedObjects.add(createInstanceAndCallLoadXML(toLoad));
/*     */                 }
/*     */               } else {
/*     */                 
/* 281 */                 logger.log(Level.INFO, "Not loading " + toLoad + " since it is a directory.");
/*     */               }
/*     */             
/*     */             }
/*     */           
/* 286 */           } else if (f.getName().endsWith(".xml")) {
/* 287 */             loadedObjects.add(createInstanceAndCallLoadXML(f));
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/* 292 */     if (!loadedObjects.isEmpty())
/* 293 */       return loadedObjects.toArray(new Object[loadedObjects.size()]); 
/* 294 */     return this.emptyObjectArray;
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
/*     */   Document createFieldsXmlDocument(LinkedList<Field> fields) throws ParserConfigurationException {
/* 310 */     String root = "fields";
/* 311 */     DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
/* 312 */     DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
/* 313 */     Document document = documentBuilder.newDocument();
/*     */     
/* 315 */     Element rootElement = document.createElement("fields");
/* 316 */     document.appendChild(rootElement);
/* 317 */     for (Field field : fields) {
/*     */       
/* 319 */       field.setAccessible(true);
/* 320 */       Element node = document.createElement("FIELD");
/* 321 */       rootElement.appendChild(node);
/*     */ 
/*     */       
/*     */       try {
/* 325 */         createNode("NAME", field.getName(), document, node);
/*     */         
/* 327 */         createNode("VALUE", field.get(this).toString(), document, node);
/*     */       }
/* 329 */       catch (IllegalAccessException iae) {
/*     */         
/* 331 */         logger.log(Level.WARNING, "Failed to write " + field.getName());
/*     */       } 
/*     */     } 
/* 334 */     return document;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void createNode(String element, String data, Document document, Element rootElement) {
/* 340 */     Element em = document.createElement(element);
/* 341 */     em.appendChild(document.createTextNode(data));
/* 342 */     rootElement.appendChild(em);
/*     */   }
/*     */   
/*     */   @Retention(RetentionPolicy.RUNTIME)
/*     */   @Target({ElementType.FIELD})
/*     */   public static @interface Saved {}
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\XMLSerializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */