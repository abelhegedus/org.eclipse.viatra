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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class DesignSpace implements IDesignSpace {

    Set<Object> statesView;

    Set<Object> rootStates;
    Set<Object> rootStatesView;

    Map<Object, List<Object>> statesAndActivations;

    Random random = new Random();

    public DesignSpace() {
        rootStates = new HashSet<>();
        rootStatesView = Collections.unmodifiableSet(rootStates);

        statesAndActivations = new HashMap<>();
        statesView = statesAndActivations.keySet();
    }

    @Override
    public synchronized Collection<Object> getStates() {
        return statesView;
    }

    @Override
    public synchronized Collection<Object> getRoots() {
        return rootStatesView;
    }

    @Override
    public synchronized void addState(Object sourceStateId, Object firedActivationId, Object newStateId) {

        List<Object> activtionIds = statesAndActivations.get(newStateId);

        if (activtionIds == null) {
            activtionIds = new ArrayList<Object>();
            statesAndActivations.put(newStateId, activtionIds);

            if (sourceStateId == null) {
                rootStates.add(newStateId);
                return;
            }
        }

        activtionIds = statesAndActivations.get(sourceStateId);

        if (activtionIds == null) {
            activtionIds = new ArrayList<Object>();
            activtionIds.add(firedActivationId);
            statesAndActivations.put(sourceStateId, activtionIds);
        } else {
            activtionIds.add(firedActivationId);
        }
    }

    public synchronized boolean isTraversed(Object stateId) {
        return statesAndActivations.containsKey(stateId);
    }

    @Override
    public synchronized Collection<Object> getActivationIds(Object stateId) {
        return statesAndActivations.get(stateId);
    }

    @Override
    public synchronized Object getRandomActivationId(Object stateId) {
        List<Object> activations = statesAndActivations.get(stateId);
        int index = random.nextInt(activations.size());
        return activations.get(index);
    }

    @Override
    public synchronized long getNumberOfStates() {
        return statesAndActivations.size();
    }

    @Override
    public synchronized long getNumberOfTransitions() {
        int numberOfTransitions = 0;
        for (List<Object> activations : statesAndActivations.values()) {
            numberOfTransitions += activations.size();
        }
        return numberOfTransitions;
    }

}
