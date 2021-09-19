/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * @(#)$Id: Discarder.java,v 1.1 2004/06/25 21:15:21 kohsuke Exp $
 */
package com.sun.tools.xjc.runtime;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * UnmarshallingEventHandler implementation that discards the whole sub-tree.
 * 
 * @author
 *     Kohsuke Kawaguchi (kohsuke.kawaguchi@sun.com)
 */
class Discarder implements UnmarshallingEventHandler {
    
    private final UnmarshallingContext context;

    // nest level of elements.
    private int depth = 0;
    
    
    public Discarder(UnmarshallingContext _ctxt) {
        this.context = _ctxt;
    }

    public void enterAttribute(String uri, String local, String qname) throws SAXException {
    }

    public void enterElement(String uri, String local, String qname, Attributes atts) throws SAXException {
        depth++;
    }

    public void leaveAttribute(String uri, String local, String qname) throws SAXException {
    }

    public void leaveElement(String uri, String local, String qname) throws SAXException {
        depth--;
        if(depth==0)
            context.popContentHandler();
    }

    public Object owner() {
        return null;
    }

    public void text(String s) throws SAXException {
    }

    public void leaveChild(int nextState) throws SAXException {
    }

}
