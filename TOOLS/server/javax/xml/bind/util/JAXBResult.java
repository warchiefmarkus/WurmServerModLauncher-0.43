/*     */ package javax.xml.bind.util;
/*     */ 
/*     */ import javax.xml.bind.JAXBContext;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.Unmarshaller;
/*     */ import javax.xml.bind.UnmarshallerHandler;
/*     */ import javax.xml.transform.sax.SAXResult;
/*     */ import org.xml.sax.ContentHandler;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JAXBResult
/*     */   extends SAXResult
/*     */ {
/*     */   private final UnmarshallerHandler unmarshallerHandler;
/*     */   
/*     */   public JAXBResult(JAXBContext context) throws JAXBException {
/*  66 */     this((context == null) ? assertionFailed() : context.createUnmarshaller());
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JAXBResult(Unmarshaller _unmarshaller) throws JAXBException {
/*  89 */     if (_unmarshaller == null) {
/*  90 */       throw new JAXBException(Messages.format("JAXBResult.NullUnmarshaller"));
/*     */     }
/*     */     
/*  93 */     this.unmarshallerHandler = _unmarshaller.getUnmarshallerHandler();
/*     */     
/*  95 */     setHandler((ContentHandler)this.unmarshallerHandler);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getResult() throws JAXBException {
/* 119 */     return this.unmarshallerHandler.getResult();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Unmarshaller assertionFailed() throws JAXBException {
/* 127 */     throw new JAXBException(Messages.format("JAXBResult.NullContext"));
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\xml\bin\\util\JAXBResult.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */