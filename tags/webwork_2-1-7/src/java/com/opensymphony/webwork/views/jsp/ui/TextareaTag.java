/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.webwork.views.jsp.ui;

import com.opensymphony.xwork.util.OgnlValueStack;


/**
 * @author Matt Ho <a href="mailto:matt@enginegreen.com">&lt;matt@enginegreen.com&gt;</a>
 * @version $Id$
 */
public class TextareaTag extends AbstractUITag {
    //~ Static fields/initializers /////////////////////////////////////////////

    /**
     * The name of the default template for the TextareaTag
     */
    final public static String TEMPLATE = "textarea";

    //~ Instance fields ////////////////////////////////////////////////////////

    protected String colsAttr;
    protected String readonlyAttr;
    protected String rowsAttr;
    protected String wrapAttr;

    //~ Methods ////////////////////////////////////////////////////////////////

    public void setCols(String cols) {
        this.colsAttr = cols;
    }

    public void setReadonly(String readonly) {
        this.readonlyAttr = readonly;
    }

    public void setRows(String rows) {
        this.rowsAttr = rows;
    }

    public void setWrap(String wrap) {
        this.wrapAttr = wrap;
    }

    public void evaluateExtraParams(OgnlValueStack stack) {
        super.evaluateExtraParams(stack);

        if (readonlyAttr != null) {
            addParameter("readonly", findValue(readonlyAttr, Boolean.class));
        }

        if (colsAttr != null) {
            addParameter("cols", findString(colsAttr));
        }

        if (rowsAttr != null) {
            addParameter("rows", findString(rowsAttr));
        }

        if (wrapAttr != null) {
            addParameter("wrap", findString(wrapAttr));
        }
    }

    protected String getDefaultTemplate() {
        return TEMPLATE;
    }
}