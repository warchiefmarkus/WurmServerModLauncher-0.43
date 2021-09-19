/*     */ package 1.0.com.sun.tools.xjc.generator.unmarshaller;
/*     */ 
/*     */ import com.sun.tools.xjc.generator.unmarshaller.automaton.Alphabet;
/*     */ import com.sun.tools.xjc.generator.unmarshaller.automaton.Automaton;
/*     */ import com.sun.tools.xjc.generator.unmarshaller.automaton.OrderComparator;
/*     */ import com.sun.tools.xjc.generator.unmarshaller.automaton.State;
/*     */ import com.sun.tools.xjc.generator.unmarshaller.automaton.Transition;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
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
/*     */ class TransitionTable
/*     */ {
/*     */   TransitionTable(Automaton a) {
/*  40 */     Iterator itr = a.states();
/*  41 */     while (itr.hasNext()) {
/*  42 */       State state = itr.next();
/*     */ 
/*     */       
/*  45 */       TreeMap tm = new TreeMap(OrderComparator.theInstance);
/*  46 */       for (Iterator jtr = state.transitions(); jtr.hasNext(); ) {
/*  47 */         Transition t = jtr.next();
/*  48 */         tm.put(t.alphabet, t);
/*     */       } 
/*     */       
/*  51 */       ArrayList r = new ArrayList();
/*     */       
/*  53 */       for (Iterator iterator = tm.entrySet().iterator(); iterator.hasNext(); ) {
/*  54 */         Map.Entry e = iterator.next();
/*  55 */         buildList(r, (Alphabet)e.getKey(), (Transition)e.getValue());
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/*  60 */       if (state.isFinalState()) {
/*  61 */         r.add(new Entry(Alphabet.EverythingElse.theInstance, Transition.REVERT_TO_PARENT, null));
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  67 */       Set alphabetsSeen = new HashSet();
/*  68 */       for (int i = 0; i < r.size(); ) {
/*  69 */         if (!alphabetsSeen.add(((Entry)r.get(i)).alphabet)) {
/*     */           
/*  71 */           r.remove(i); continue;
/*     */         } 
/*  73 */         i++;
/*     */       } 
/*     */       
/*  76 */       this.table.put(state, r.<Entry>toArray(new Entry[r.size()]));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void buildList(ArrayList r, Alphabet alphabet, Transition transition) {
/*  82 */     if (alphabet.isReference()) {
/*  83 */       Iterator itr = alphabet.asReference().head(true).iterator();
/*  84 */       while (itr.hasNext())
/*  85 */         buildList(r, itr.next(), transition); 
/*     */     } else {
/*  87 */       r.add(new Entry(alphabet, transition, null));
/*     */     } 
/*     */   }
/*     */   
/*  91 */   private final Map table = new HashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Entry[] list(State s) {
/*  98 */     Entry[] r = (Entry[])this.table.get(s);
/*  99 */     if (r == null) {
/* 100 */       return empty;
/*     */     }
/* 102 */     return r;
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
/* 119 */   private static final Entry[] empty = new Entry[0];
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generato\\unmarshaller\TransitionTable.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */