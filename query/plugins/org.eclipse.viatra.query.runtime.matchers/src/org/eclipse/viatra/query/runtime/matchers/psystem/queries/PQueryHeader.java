/*******************************************************************************
 * Copyright (c) 2010-2015, Bergmann Gabor, Istvan Rath and Daniel Varro
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Bergmann Gabor - initial API and implementation
 *******************************************************************************/
package org.eclipse.viatra.query.runtime.matchers.psystem.queries;

import java.util.List;

import org.eclipse.viatra.query.runtime.matchers.psystem.annotations.PAnnotation;

/**
 * Represents header information (metainfo) about a query. 
 * <p> To be implemented both by IQuerySpecifications intended for end users, 
 * and the internal query representation {@link PQuery}.
 * 
 * 
 * @author Bergmann Gabor
 * @since 0.9
 */
public interface PQueryHeader {

	/**
	 * Identifies the pattern for which matchers can be instantiated.
	 */
	public String getFullyQualifiedName();

	/**
	 * Return the list of parameter names
	 * 
	 * @return a non-null, but possibly empty list of parameter names
	 */
	public List<String> getParameterNames();

	/**
	 * Returns a list of parameter descriptions
	 * 
	 * @return a non-null, but possibly empty list of parameter descriptions
	 */
	public List<PParameter> getParameters();

	/**
	 * Returns the index of a named parameter
	 * 
	 * @param parameterName
	 * @return the index, or null of no such parameter is available
	 */
	public Integer getPositionOfParameter(String parameterName);

	/**
	 * Returns the list of annotations specified for this query
	 * 
	 * @return a non-null, but possibly empty list of annotations
	 */
	public List<PAnnotation> getAllAnnotations();

	/**
	 * Returns the list of annotations with a specified name
	 * 
	 * @param annotationName
	 * @return a non-null, but possibly empty list of annotations
	 */
	public List<PAnnotation> getAnnotationsByName(String annotationName);

	/**
	 * Returns the first annotation with a specified name
	 * 
	 * @param annotationName
	 * @return the found annotation, or null if non is available
	 */
	public PAnnotation getFirstAnnotationByName(String annotationName);

}