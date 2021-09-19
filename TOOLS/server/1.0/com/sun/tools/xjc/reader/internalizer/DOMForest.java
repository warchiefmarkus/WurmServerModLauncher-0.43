/*     */ package 1.0.com.sun.tools.xjc.reader.internalizer;
/*     */ 
/*     */ import com.sun.tools.xjc.reader.internalizer.DOMBuilder;
/*     */ import com.sun.tools.xjc.reader.internalizer.DOMForestParser;
/*     */ import com.sun.tools.xjc.reader.internalizer.InternalizationLogic;
/*     */ import com.sun.tools.xjc.reader.internalizer.Internalizer;
/*     */ import com.sun.tools.xjc.reader.internalizer.LocatorTable;
/*     */ import com.sun.tools.xjc.reader.internalizer.VersionChecker;
/*     */ import com.sun.tools.xjc.reader.internalizer.WhitespaceStripper;
/*     */ import com.sun.xml.xsom.parser.JAXPParser;
/*     */ import com.sun.xml.xsom.parser.XMLParser;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import javax.xml.transform.TransformerFactory;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import org.w3c.dom.Document;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.XMLFilter;
/*     */ import org.xml.sax.XMLReader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class DOMForest
/*     */ {
/*  46 */   private final Map core = new HashMap();
/*     */ 
/*     */   
/*  49 */   public final LocatorTable locatorTable = new LocatorTable();
/*     */ 
/*     */   
/*  52 */   public final Set outerMostBindings = new HashSet();
/*     */ 
/*     */   
/*  55 */   private EntityResolver entityResolver = null;
/*     */ 
/*     */   
/*  58 */   private ErrorHandler errorHandler = null;
/*     */ 
/*     */   
/*     */   protected final InternalizationLogic logic;
/*     */ 
/*     */   
/*     */   private final SAXParserFactory parserFactory;
/*     */ 
/*     */   
/*     */   private final DocumentBuilder documentBuilder;
/*     */ 
/*     */   
/*     */   public DOMForest(SAXParserFactory parserFactory, DocumentBuilder documentBuilder, InternalizationLogic logic) {
/*  71 */     this.parserFactory = parserFactory;
/*  72 */     this.documentBuilder = documentBuilder;
/*  73 */     this.logic = logic;
/*     */   }
/*     */   
/*     */   public DOMForest(InternalizationLogic logic) throws ParserConfigurationException {
/*  77 */     DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
/*  78 */     dbf.setNamespaceAware(true);
/*  79 */     this.documentBuilder = dbf.newDocumentBuilder();
/*     */     
/*  81 */     this.parserFactory = SAXParserFactory.newInstance();
/*  82 */     this.parserFactory.setNamespaceAware(true);
/*     */     
/*  84 */     this.logic = logic;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Document get(String systemId) {
/*  92 */     Document doc = (Document)this.core.get(systemId);
/*     */     
/*  94 */     if (doc == null && systemId.startsWith("file:/") && !systemId.startsWith("file://"))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 101 */       doc = (Document)this.core.get("file://" + systemId.substring(5));
/*     */     }
/*     */     
/* 104 */     return doc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSystemId(Document dom) {
/* 113 */     for (Iterator itr = this.core.entrySet().iterator(); itr.hasNext(); ) {
/* 114 */       Map.Entry e = itr.next();
/* 115 */       if (e.getValue() == dom)
/* 116 */         return (String)e.getKey(); 
/*     */     } 
/* 118 */     return null;
/*     */   }
/*     */   
/*     */   public Document parse(InputSource source) throws SAXException, IOException {
/* 122 */     if (source.getSystemId() == null) {
/* 123 */       throw new IllegalArgumentException();
/*     */     }
/* 125 */     return parse(source.getSystemId(), source);
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
/*     */   public Document parse(String systemId) throws SAXException, IOException {
/* 137 */     if (this.core.containsKey(systemId))
/*     */     {
/* 139 */       return (Document)this.core.get(systemId);
/*     */     }
/* 141 */     InputSource is = null;
/*     */ 
/*     */     
/* 144 */     if (this.entityResolver != null)
/* 145 */       is = this.entityResolver.resolveEntity(null, systemId); 
/* 146 */     if (is == null) {
/* 147 */       is = new InputSource(systemId);
/*     */     }
/*     */     
/* 150 */     return parse(systemId, is);
/*     */   }
/*     */   
/*     */   public Document parse(String systemId, InputSource inputSource) throws SAXException, IOException {
/* 154 */     Document dom = this.documentBuilder.newDocument();
/*     */ 
/*     */ 
/*     */     
/* 158 */     this.core.put(systemId, dom);
/*     */     
/*     */     try {
/* 161 */       XMLReader reader = this.parserFactory.newSAXParser().getXMLReader();
/* 162 */       XMLFilter f = this.logic.createExternalReferenceFinder(this);
/* 163 */       f.setParent(reader);
/* 164 */       reader = f;
/*     */       
/* 166 */       VersionChecker versionChecker = new VersionChecker(reader);
/* 167 */       WhitespaceStripper whitespaceStripper = new WhitespaceStripper((XMLReader)versionChecker);
/* 168 */       whitespaceStripper.setContentHandler((ContentHandler)new DOMBuilder(dom, this.locatorTable, this.outerMostBindings));
/* 169 */       if (this.errorHandler != null)
/* 170 */         whitespaceStripper.setErrorHandler(this.errorHandler); 
/* 171 */       if (this.entityResolver != null)
/* 172 */         whitespaceStripper.setEntityResolver(this.entityResolver); 
/* 173 */       whitespaceStripper.parse(inputSource);
/* 174 */     } catch (ParserConfigurationException e) {
/*     */       
/* 176 */       e.printStackTrace();
/*     */     } 
/*     */     
/* 179 */     return dom;
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
/*     */   public void transform() throws SAXException {
/* 193 */     Internalizer.transform(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XMLParser createParser() {
/* 204 */     return (XMLParser)new DOMForestParser(this, (XMLParser)new JAXPParser());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public EntityResolver getEntityResolver() {
/* 210 */     return this.entityResolver;
/*     */   }
/*     */   
/*     */   public void setEntityResolver(EntityResolver entityResolver) {
/* 214 */     this.entityResolver = entityResolver;
/*     */   }
/*     */   
/*     */   public ErrorHandler getErrorHandler() {
/* 218 */     return this.errorHandler;
/*     */   }
/*     */   
/*     */   public void setErrorHandler(ErrorHandler errorHandler) {
/* 222 */     this.errorHandler = errorHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Document[] listDocuments() {
/* 229 */     return (Document[])this.core.values().toArray((Object[])new Document[this.core.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] listSystemIDs() {
/* 236 */     return (String[])this.core.keySet().toArray((Object[])new String[this.core.keySet().size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void dump(OutputStream out) throws IOException {
/*     */     try {
/* 247 */       Transformer it = TransformerFactory.newInstance().newTransformer();
/*     */       
/* 249 */       for (Iterator itr = this.core.entrySet().iterator(); itr.hasNext(); ) {
/* 250 */         Map.Entry e = itr.next();
/*     */         
/* 252 */         out.write(("---<< " + e.getKey() + "\n").getBytes());
/*     */         
/* 254 */         it.transform(new DOMSource((Document)e.getValue()), new StreamResult(out));
/*     */         
/* 256 */         out.write("\n\n\n".getBytes());
/*     */       } 
/* 258 */     } catch (TransformerException e) {
/* 259 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\internalizer\DOMForest.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */