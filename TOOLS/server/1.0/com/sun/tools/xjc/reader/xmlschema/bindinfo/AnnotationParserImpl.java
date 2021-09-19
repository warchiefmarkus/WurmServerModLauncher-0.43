/*     */ package 1.0.com.sun.tools.xjc.reader.xmlschema.bindinfo;
/*     */ 
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.msv.verifier.jarv.RELAXNGFactoryImpl;
/*     */ import com.sun.tools.xjc.Options;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BindInfo;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.NGCCRuntimeEx;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.AnnotationState;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.NGCCHandler;
/*     */ import com.sun.xml.bind.JAXBAssertionError;
/*     */ import com.sun.xml.xsom.parser.AnnotationContext;
/*     */ import com.sun.xml.xsom.parser.AnnotationParser;
/*     */ import java.io.IOException;
/*     */ import org.iso_relax.verifier.Verifier;
/*     */ import org.iso_relax.verifier.VerifierConfigurationException;
/*     */ import org.iso_relax.verifier.impl.ForkContentHandler;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.SAXException;
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
/*     */ public class AnnotationParserImpl
/*     */   extends AnnotationParser
/*     */ {
/*     */   private AnnotationState parser;
/*     */   private final JCodeModel codeModel;
/*     */   private final Options options;
/*     */   
/*     */   public AnnotationParserImpl(JCodeModel cm, Options opts) {
/*  46 */     this.parser = null;
/*     */     this.codeModel = cm;
/*     */     this.options = opts;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ContentHandler getContentHandler(AnnotationContext context, String parentElementName, ErrorHandler errorHandler, EntityResolver entityResolver) {
/*     */     try {
/*  57 */       if (this.parser != null)
/*     */       {
/*     */         
/*  60 */         throw new JAXBAssertionError();
/*     */       }
/*     */       
/*  63 */       NGCCRuntimeEx runtime = new NGCCRuntimeEx(this.codeModel, this.options, errorHandler);
/*  64 */       this.parser = new AnnotationState(runtime);
/*  65 */       runtime.setRootHandler((NGCCHandler)this.parser);
/*     */ 
/*     */       
/*  68 */       RELAXNGFactoryImpl rELAXNGFactoryImpl = new RELAXNGFactoryImpl();
/*  69 */       rELAXNGFactoryImpl.setProperty("datatypeLibraryFactory", new DatatypeLibraryFactoryImpl(null));
/*  70 */       Verifier v = rELAXNGFactoryImpl.newVerifier(getClass().getClassLoader().getResourceAsStream("com/sun/tools/xjc/reader/xmlschema/bindinfo/binding.purified.rng"));
/*     */       
/*  72 */       v.setErrorHandler(errorHandler);
/*     */ 
/*     */       
/*  75 */       return (ContentHandler)new ForkContentHandler((ContentHandler)v.getVerifierHandler(), (ContentHandler)runtime);
/*  76 */     } catch (VerifierConfigurationException e) {
/*     */       
/*  78 */       e.printStackTrace();
/*  79 */       throw new InternalError();
/*  80 */     } catch (SAXException e) {
/*  81 */       e.printStackTrace();
/*  82 */       throw new InternalError();
/*  83 */     } catch (IOException e) {
/*  84 */       e.printStackTrace();
/*  85 */       throw new InternalError();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getResult(Object existing) {
/*  91 */     if (this.parser == null)
/*     */     {
/*     */       
/*  94 */       throw new JAXBAssertionError();
/*     */     }
/*  96 */     if (existing != null) {
/*  97 */       BindInfo bie = (BindInfo)existing;
/*  98 */       bie.absorb(this.parser.bi);
/*  99 */       return bie;
/*     */     } 
/* 101 */     return this.parser.bi;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\bindinfo\AnnotationParserImpl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */