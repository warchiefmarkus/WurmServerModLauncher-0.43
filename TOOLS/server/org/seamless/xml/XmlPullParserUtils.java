/*     */ package org.seamless.xml;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.logging.Logger;
/*     */ import org.xmlpull.v1.XmlPullParser;
/*     */ import org.xmlpull.v1.XmlPullParserException;
/*     */ import org.xmlpull.v1.XmlPullParserFactory;
/*     */ import org.xmlpull.v1.XmlSerializer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XmlPullParserUtils
/*     */ {
/*  33 */   private static final Logger log = Logger.getLogger(XmlPullParserUtils.class.getName());
/*     */   
/*     */   static XmlPullParserFactory xmlPullParserFactory;
/*     */   
/*     */   static {
/*     */     try {
/*  39 */       xmlPullParserFactory = XmlPullParserFactory.newInstance();
/*  40 */       xmlPullParserFactory.setNamespaceAware(true);
/*  41 */     } catch (XmlPullParserException e) {
/*  42 */       log.severe("cannot create XmlPullParserFactory instance: " + e);
/*     */     } 
/*     */   }
/*     */   public static XmlPullParser createParser(String xml) throws XmlPullParserException {
/*     */     InputStream is;
/*  47 */     XmlPullParser xpp = createParser();
/*     */     
/*     */     try {
/*  50 */       is = new ByteArrayInputStream(xml.getBytes("UTF-8"));
/*  51 */     } catch (UnsupportedEncodingException e) {
/*  52 */       throw new XmlPullParserException("UTF-8: unsupported encoding");
/*     */     } 
/*  54 */     xpp.setInput(is, "UTF-8");
/*  55 */     return xpp;
/*     */   }
/*     */   
/*     */   public static XmlPullParser createParser() throws XmlPullParserException {
/*  59 */     if (xmlPullParserFactory == null) throw new XmlPullParserException("no XML Pull parser factory"); 
/*  60 */     return xmlPullParserFactory.newPullParser();
/*     */   }
/*     */   
/*     */   public static XmlSerializer createSerializer() throws XmlPullParserException {
/*  64 */     if (xmlPullParserFactory == null) throw new XmlPullParserException("no XML Pull parser factory"); 
/*  65 */     return xmlPullParserFactory.newSerializer();
/*     */   }
/*     */ 
/*     */   
/*     */   public static void setSerializerIndentation(XmlSerializer serializer, int indent) {
/*  70 */     if (indent > 0) {
/*     */       try {
/*  72 */         serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
/*     */       }
/*  74 */       catch (Exception e) {
/*  75 */         log.warning("error setting feature of XmlSerializer: " + e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void skipTag(XmlPullParser xpp, String tag) throws IOException, XmlPullParserException {
/*     */     int event;
/*     */     do {
/*  84 */       event = xpp.next();
/*  85 */     } while (event != 1 && (event != 3 || !xpp.getName().equals(tag)));
/*     */   }
/*     */ 
/*     */   
/*     */   public static void searchTag(XmlPullParser xpp, String tag) throws IOException, XmlPullParserException {
/*     */     int event;
/*  91 */     while ((event = xpp.next()) != 1) {
/*  92 */       if (event == 2 && xpp.getName().equals(tag))
/*     */         return; 
/*     */     } 
/*  95 */     throw new IOException(String.format("tag '%s' not found", new Object[] { tag }));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void serializeIfNotNullOrEmpty(XmlSerializer serializer, String ns, String tag, String value) throws Exception {
/* 101 */     if (isNullOrEmpty(value))
/*     */       return; 
/* 103 */     serializer.startTag(ns, tag);
/* 104 */     serializer.text(value);
/* 105 */     serializer.endTag(ns, tag);
/*     */   }
/*     */   
/*     */   public static boolean isNullOrEmpty(String s) {
/* 109 */     return (s == null || s.length() == 0);
/*     */   }
/*     */   
/*     */   public static void serializeIfNotEqual(XmlSerializer serializer, String ns, String tag, Object value, Object forbiddenValue) throws Exception {
/* 113 */     if (value == null || value.equals(forbiddenValue))
/*     */       return; 
/* 115 */     serializer.startTag(ns, tag);
/* 116 */     serializer.text(value.toString());
/* 117 */     serializer.endTag(ns, tag);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String fixXMLEntities(String xml) {
/* 122 */     StringBuilder fixedXml = new StringBuilder(xml.length());
/*     */     
/* 124 */     boolean isFixed = false;
/*     */     
/* 126 */     for (int i = 0; i < xml.length(); i++) {
/*     */       
/* 128 */       char c = xml.charAt(i);
/* 129 */       if (c == '&') {
/*     */         
/* 131 */         String sub = xml.substring(i, Math.min(i + 10, xml.length()));
/* 132 */         if (!sub.startsWith("&#") && !sub.startsWith("&lt;") && !sub.startsWith("&gt;") && !sub.startsWith("&amp;") && !sub.startsWith("&apos;") && !sub.startsWith("&quot;")) {
/*     */           
/* 134 */           isFixed = true;
/* 135 */           fixedXml.append("&amp;");
/*     */         } else {
/* 137 */           fixedXml.append(c);
/*     */         } 
/*     */       } else {
/* 140 */         fixedXml.append(c);
/*     */       } 
/*     */     } 
/*     */     
/* 144 */     if (isFixed) {
/* 145 */       log.warning("fixed badly encoded entities in XML");
/*     */     }
/*     */     
/* 148 */     return fixedXml.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamless\xml\XmlPullParserUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */