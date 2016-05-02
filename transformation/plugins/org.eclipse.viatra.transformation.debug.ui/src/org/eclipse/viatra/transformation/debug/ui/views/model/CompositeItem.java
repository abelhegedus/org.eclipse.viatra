/**
 * Copyright (c) 2010-2016, Peter Lunk, IncQuery Labs Ltd.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Peter Lunk - initial API and implementation
 */
package org.eclipse.viatra.transformation.debug.ui.views.model;

public class CompositeItem{
    private String name;
    private Object[] children; 
    
    public CompositeItem(String name, Object[] children) {
        super();
        this.name = name;
        this.children = children;
    }
    
    public String getName() {
        return name;
    }
    
    public Object[] getChildren() {
        return children;
    }
}
