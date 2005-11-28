/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.webwork.views.jsp.ui;


/**
 * @version $Id$
 * @author <a href="mailto:m.bogaert@intrasoft.be">Mathias Bogaert</a>
 */
public class DoubleSelectTag extends AbstractDoubleListTag {
    //~ Static fields/initializers /////////////////////////////////////////////

    /**
     * The name of the default template for the SelectTag
     */
    final public static String TEMPLATE = "doubleselect.vm";

    //~ Instance fields ////////////////////////////////////////////////////////

    private boolean multiple;
    private int size;

    //~ Methods ////////////////////////////////////////////////////////////////

    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }

    public boolean isMultiple() {
        return multiple;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public Object Multiple(boolean multiple) {
        setMultiple(multiple);

        return this;
    }

    public Object Size(int size) {
        setSize(size);

        return this;
    }

    /**
     * Clears all the instance variables to allow this instance to be reused.
     */
    public void release() {
        super.release();
        this.multiple = false;
        this.size = 0;
    }

    protected String getDefaultTemplate() {
        return TEMPLATE;
    }
}