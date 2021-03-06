/*******************************************************************************
 * Copyright (c) 2010-2015, Marton Bur, Zoltan Ujhelyi, Akos Horvath, Istvan Rath and Daniel Varro
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Marton Bur - initial API and implementation
 *******************************************************************************/
package org.eclipse.viatra.query.runtime.matchers.psystem.rewriters;
import org.eclipse.viatra.query.runtime.matchers.psystem.basicenumerables.PositivePatternCall;

/**
 * @author Marton Bur
 *
 */
public class DefaultFlattenCallPredicate implements IFlattenCallPredicate {

	@Override
	public boolean shouldFlatten(PositivePatternCall positivePatternCall) {
		return true;
	}

}
