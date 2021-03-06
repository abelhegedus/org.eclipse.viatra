/*******************************************************************************
 * Copyright (c) 2010-2014, Miklos Foldenyi, Andras Szabolcs Nagy, Abel Hegedus, Akos Horvath, Zoltan Ujhelyi and Daniel Varro
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *   Miklos Foldenyi - initial API and implementation
 *   Andras Szabolcs Nagy - initial API and implementation
 *******************************************************************************/
package org.eclipse.viatra.dse.designspace.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.viatra.dse.api.DSEException;
import org.eclipse.viatra.dse.api.SolutionTrajectory;
import org.eclipse.viatra.dse.statecode.IStateCoderFactory;
import org.eclipse.viatra.transformation.runtime.emf.rules.batch.BatchTransformationRule;

import com.google.common.base.Preconditions;

public class TrajectoryInfo implements Cloneable {

    private final List<Object> trajectory;
    private final List<Object> trajectoryView;
    private final List<BatchTransformationRule<?, ?>> rules;
    private final List<BatchTransformationRule<?, ?>> rulesView;
    private final List<Object> stateIds;
    private final List<Object> stateIdsView;
    private final List<Map<String, Double>> measuredCosts;

    public TrajectoryInfo(Object initialStateId) {
        Preconditions.checkNotNull(initialStateId);

        stateIds = new ArrayList<>();
        stateIds.add(initialStateId);

        trajectory = new ArrayList<>();
        rules = new ArrayList<>();
        measuredCosts = new ArrayList<>();

        trajectoryView = Collections.unmodifiableList(trajectory);
        stateIdsView = Collections.unmodifiableList(stateIds);
        rulesView = Collections.unmodifiableList(rules);
    }

    protected TrajectoryInfo(List<Object> stateIds, List<Object> trajectory, List<BatchTransformationRule<?, ?>> rules, List<Map<String, Double>> measuredCosts) {

        this.stateIds = new ArrayList<>(stateIds);
        this.trajectory = new ArrayList<>(trajectory);
        this.rules = new ArrayList<>(rules);
        trajectoryView = Collections.unmodifiableList(trajectory);
        stateIdsView = Collections.unmodifiableList(stateIds);
        rulesView = Collections.unmodifiableList(rules);
        this.measuredCosts = new ArrayList<>(measuredCosts);
    }

    public void addStep(Object activationId, BatchTransformationRule<?, ?> rule, Object newStateId, Map<String, Double> measuredCosts) {
        stateIds.add(newStateId);
        trajectory.add(activationId);
        rules.add(rule);
        this.measuredCosts.add(measuredCosts);
    }

    public void backtrack() {
        int size = trajectory.size();

        if (size == 0) {
            throw new DSEException("Cannot step back any further!");
        }

        trajectory.remove(size - 1);
        rules.remove(size - 1);
        stateIds.remove(size);
        measuredCosts.remove(size - 1);
    }

    public Object getInitialStateId() {
        return stateIds.get(0);
    }

    public Object getCurrentStateId() {
        return stateIds.get(stateIds.size() - 1);
    }

    public Object getLastActivationId() {
        return trajectory.get(trajectory.size() - 1);
    }

    public Object getLastStateId() {
        return stateIds.get(stateIds.size() - 1);
    }

    public List<Object> getTrajectory() {
        return trajectoryView;
    }

    public List<Object> getStateTrajectory() {
        return stateIdsView;
    }

    public List<BatchTransformationRule<?, ?>> getRules() {
        return rulesView;
    }

    public int getDepth() {
        return trajectory.size();
    }

    public List<Map<String, Double>> getMeasuredCosts() {
        return measuredCosts;
    }

    public SolutionTrajectory createSolutionTrajectory(final IStateCoderFactory stateCoderFactory) {

        List<Object> activationIds = new ArrayList<>(trajectory);
        List<BatchTransformationRule<?, ?>> rules = new ArrayList<>(this.rules);

        return new SolutionTrajectory(activationIds, rules, stateCoderFactory);
    }

    public boolean canStepBack() {
        return !trajectory.isEmpty();
    }

    @Override
    public TrajectoryInfo clone() {
        TrajectoryInfo clone = new TrajectoryInfo(stateIds, trajectory, rules, measuredCosts);
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Trajectory:\n");
        for (Object activationId : trajectory) {
            sb.append(activationId);
            sb.append("\n");
        }
        return sb.toString();
    }
}
