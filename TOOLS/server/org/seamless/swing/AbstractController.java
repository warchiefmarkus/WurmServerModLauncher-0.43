/*     */ package org.seamless.swing;
/*     */ 
/*     */ import java.awt.Container;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Logger;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.JFrame;
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
/*     */ public class AbstractController<V extends Container>
/*     */   implements Controller<V>
/*     */ {
/*  34 */   private static Logger log = Logger.getLogger(AbstractController.class.getName());
/*     */   
/*     */   private V view;
/*     */   private Controller parentController;
/*  38 */   private List<Controller> subControllers = new ArrayList<Controller>();
/*  39 */   private Map<String, DefaultAction> actions = new HashMap<String, DefaultAction>();
/*  40 */   private Map<Class, List<EventListener>> eventListeners = (Map)new HashMap<Class<?>, List<EventListener>>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractController(V view) {
/*  48 */     this.view = view;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractController() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractController(Controller parentController) {
/*  63 */     this(null, parentController);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractController(V view, Controller parentController) {
/*  73 */     this.view = view;
/*     */ 
/*     */     
/*  76 */     if (parentController != null) {
/*  77 */       this.parentController = parentController;
/*  78 */       parentController.getSubControllers().add(this);
/*     */     } 
/*     */   }
/*     */   
/*     */   public V getView() {
/*  83 */     return this.view;
/*     */   }
/*     */   
/*     */   public Controller getParentController() {
/*  87 */     return this.parentController;
/*     */   }
/*     */   
/*     */   public List<Controller> getSubControllers() {
/*  91 */     return this.subControllers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 100 */     log.fine("Disposing controller");
/* 101 */     Iterator<Controller> it = this.subControllers.iterator();
/* 102 */     while (it.hasNext()) {
/* 103 */       Controller subcontroller = it.next();
/* 104 */       subcontroller.dispose();
/* 105 */       it.remove();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerAction(AbstractButton source, DefaultAction action) {
/* 116 */     source.removeActionListener(this);
/* 117 */     source.addActionListener(this);
/* 118 */     this.actions.put(source.getActionCommand(), action);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerAction(AbstractButton source, String actionCommand, DefaultAction action) {
/* 129 */     source.setActionCommand(actionCommand);
/* 130 */     registerAction(source, action);
/*     */   }
/*     */   
/*     */   public void deregisterAction(String actionCommand) {
/* 134 */     this.actions.remove(actionCommand);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerEventListener(Class eventClass, EventListener eventListener) {
/* 144 */     log.fine("Registering listener: " + eventListener + " for event type: " + eventClass.getName());
/* 145 */     List<EventListener> listenersForEvent = this.eventListeners.get(eventClass);
/* 146 */     if (listenersForEvent == null) {
/* 147 */       listenersForEvent = new ArrayList<EventListener>();
/*     */     }
/* 149 */     listenersForEvent.add(eventListener);
/* 150 */     this.eventListeners.put(eventClass, listenersForEvent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fireEvent(Event event) {
/* 161 */     fireEvent(event, false);
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
/*     */   public void fireEventGlobal(Event event) {
/* 173 */     fireEvent(event, true);
/*     */   }
/*     */   
/*     */   public void fireEvent(Event event, boolean global) {
/* 177 */     if (!event.alreadyFired(this)) {
/* 178 */       log.finest("Event has not been fired already");
/* 179 */       if (this.eventListeners.get(event.getClass()) != null) {
/* 180 */         log.finest("Have listeners for this type of event: " + this.eventListeners.get(event.getClass()));
/* 181 */         for (EventListener<Event> eventListener : this.eventListeners.get(event.getClass())) {
/* 182 */           log.fine("Processing event: " + event.getClass().getName() + " with listener: " + eventListener.getClass().getName());
/* 183 */           eventListener.handleEvent(event);
/*     */         } 
/*     */       } 
/* 186 */       event.addFiredInController(this);
/* 187 */       log.fine("Passing event: " + event.getClass().getName() + " DOWN in the controller hierarchy");
/* 188 */       for (Controller subController : this.subControllers) subController.fireEvent(event, global); 
/*     */     } else {
/* 190 */       log.finest("Event already fired here, ignoring...");
/*     */     } 
/* 192 */     if (getParentController() != null && !event.alreadyFired(getParentController()) && global) {
/*     */ 
/*     */       
/* 195 */       log.fine("Passing event: " + event.getClass().getName() + " UP in the controller hierarchy");
/* 196 */       getParentController().fireEvent(event, global);
/*     */     } else {
/* 198 */       log.finest("Event does not propagate up the tree from here");
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
/*     */   public void actionPerformed(ActionEvent actionEvent) {
/*     */     try {
/* 215 */       AbstractButton button = (AbstractButton)actionEvent.getSource();
/* 216 */       String actionCommand = button.getActionCommand();
/* 217 */       DefaultAction action = this.actions.get(actionCommand);
/*     */       
/* 219 */       if (action != null) {
/*     */         
/* 221 */         log.fine("Handling command: " + actionCommand + " with action: " + action.getClass());
/*     */         try {
/* 223 */           preActionExecute();
/* 224 */           log.fine("Dispatching to action for execution");
/* 225 */           action.executeInController(this, actionEvent);
/* 226 */           postActionExecute();
/* 227 */         } catch (RuntimeException ex) {
/* 228 */           failedActionExecute();
/* 229 */           throw ex;
/* 230 */         } catch (Exception ex) {
/* 231 */           failedActionExecute();
/* 232 */           throw new RuntimeException(ex);
/*     */         } finally {
/* 234 */           finalActionExecute();
/*     */         }
/*     */       
/*     */       }
/* 238 */       else if (getParentController() != null) {
/* 239 */         log.fine("Passing action on to parent controller");
/* 240 */         this.parentController.actionPerformed(actionEvent);
/*     */       } else {
/* 242 */         throw new RuntimeException("Nobody is responsible for action command: " + actionCommand);
/*     */       }
/*     */     
/*     */     }
/* 246 */     catch (ClassCastException e) {
/* 247 */       throw new IllegalArgumentException("Action source is not an Abstractbutton: " + actionEvent);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void preActionExecute() {}
/*     */ 
/*     */   
/*     */   public void postActionExecute() {}
/*     */ 
/*     */   
/*     */   public void failedActionExecute() {}
/*     */ 
/*     */   
/*     */   public void finalActionExecute() {}
/*     */ 
/*     */   
/*     */   public void windowClosing(WindowEvent windowEvent) {
/* 266 */     dispose();
/* 267 */     ((JFrame)getView()).dispose();
/*     */   }
/*     */   
/*     */   public void windowOpened(WindowEvent windowEvent) {}
/*     */   
/*     */   public void windowClosed(WindowEvent windowEvent) {}
/*     */   
/*     */   public void windowIconified(WindowEvent windowEvent) {}
/*     */   
/*     */   public void windowDeiconified(WindowEvent windowEvent) {}
/*     */   
/*     */   public void windowActivated(WindowEvent windowEvent) {}
/*     */   
/*     */   public void windowDeactivated(WindowEvent windowEvent) {}
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamless\swing\AbstractController.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */