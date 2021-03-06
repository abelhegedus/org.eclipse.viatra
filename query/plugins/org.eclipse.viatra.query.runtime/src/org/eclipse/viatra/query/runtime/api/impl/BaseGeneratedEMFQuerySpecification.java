/*******************************************************************************
 * Copyright (c) 2004-2010 Gabor Bergmann and Daniel Varro
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Gabor Bergmann - initial API and implementation
 *******************************************************************************/

package org.eclipse.viatra.query.runtime.api.impl;

import org.eclipse.viatra.query.runtime.api.IPatternMatch;
import org.eclipse.viatra.query.runtime.api.ViatraQueryMatcher;
import org.eclipse.viatra.query.runtime.api.scope.QueryScope;
import org.eclipse.viatra.query.runtime.emf.EMFScope;
import org.eclipse.viatra.query.runtime.exception.ViatraQueryException;
import org.eclipse.viatra.query.runtime.matchers.psystem.queries.PQuery;
import org.eclipse.viatra.query.runtime.matchers.psystem.queries.QueryInitializationException;

/**
 * Provides common functionality of pattern-specific generated query specifications over the EMF scope.
 *
 * @author Bergmann Gábor
 * @author Mark Czotter
 */
public abstract class BaseGeneratedEMFQuerySpecification<Matcher extends ViatraQueryMatcher<? extends IPatternMatch>> extends
        BaseQuerySpecification<Matcher> {
	
	
    /**
     * Instantiates query specification for the given internal query representation.
	 */
    public BaseGeneratedEMFQuerySpecification(PQuery wrappedPQuery) {
        super(wrappedPQuery);
    }
    
    protected static ViatraQueryException processInitializerError(ExceptionInInitializerError err) {
        Throwable cause1 = err.getCause();
        if (cause1 instanceof RuntimeException) {
            Throwable cause2 = ((RuntimeException) cause1).getCause();
            if (cause2 instanceof ViatraQueryException) {
                return (ViatraQueryException) cause2;
            } else if (cause2 instanceof QueryInitializationException) {
                return new ViatraQueryException((QueryInitializationException) cause2);
            } 
        }
        throw err;
    }
        
	@Override
	public Class<? extends QueryScope> getPreferredScopeClass() {
		return EMFScope.class;
	}
    
}
