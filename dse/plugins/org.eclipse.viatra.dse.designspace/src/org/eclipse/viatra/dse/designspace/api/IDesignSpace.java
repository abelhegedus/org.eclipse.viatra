/*******************************************************************************
 * Copyright (c) 2010-2016, Andras Szabolcs Nagy and Daniel Varro
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *   Andras Szabolcs Nagy - initial API and implementation
 *******************************************************************************/
package org.eclipse.viatra.dse.designspace.api;

import java.util.Collection;

public interface IDesignSpace {

    Collection<Object> getStates();
    Collection<Object> getRoots();
    void addState(Object sourceStateId, Object firedActivationId, Object newStateId);

    boolean isTraversed(Object stateId);

    Collection<Object> getActivationIds(Object stateId);
    Object getRandomActivationId(Object stateId);

    long getNumberOfStates();
    long getNumberOfTransitions();

}
