/*******************************************************************************
 * Copyright (c) 2010-2014, Marton Bur, Akos Horvath, Zoltan Ujhelyi, Istvan Rath and Daniel Varro
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Marton Bur - initial API and implementation
 *******************************************************************************/
package org.eclipse.viatra.query.runtime.localsearch.planner.cost.impl;

import java.util.Set;

import org.eclipse.viatra.query.runtime.localsearch.planner.cost.ICostEstimator;
import org.eclipse.viatra.query.runtime.matchers.planning.SubPlan;
import org.eclipse.viatra.query.runtime.matchers.psystem.PConstraint;
import org.eclipse.viatra.query.runtime.matchers.psystem.PVariable;
import org.eclipse.viatra.query.runtime.matchers.psystem.basicdeferred.ExportedParameter;
import org.eclipse.viatra.query.runtime.matchers.psystem.basicdeferred.NegativePatternCall;
import org.eclipse.viatra.query.runtime.matchers.psystem.basicdeferred.PatternMatchCounter;
import org.eclipse.viatra.query.runtime.matchers.psystem.basicenumerables.BinaryTransitiveClosure;
import org.eclipse.viatra.query.runtime.matchers.psystem.basicenumerables.ConstantValue;

/**
 * This class can be used to calculate cost of application of a constraint with a given adornment.
 * 
 * For now the logic is based on the following principles:
 * 
 * <li>The transitive closures, NACs and count finds are the most expensive operations
 * 
 * <li>The number of free variables increase the cost
 * 
 * <li>If all the variables of a constraint are free, then its cost equals to twice the number of its parameter
 * variables. This solves the problem of unnecessary iteration over instances at the beginning of a plan (thus causing
 * very long run times when executing the plan) by applying constraints based on structural features as soon as
 * possible.
 * 
 * <br>
 * 
 * @author Marton Bur
 * 
 * @deprecated This is used by {@link org.eclipse.viatra.query.runtime.localsearch.planner.LocalSearchPlannerStrategy}. 
 * Use the {@link org.eclipse.viatra.query.runtime.localsearch.planner.LocalSearchRuntimeBasedStrategy} and its belonging components instead.
 */
@Deprecated
public class VariableBindingBasedCostEstimator implements ICostEstimator {

    // Static cost definitions
    private static int MAX = 1000;
    private int exportedParameterCost = MAX - 20;
    private int binaryTransitiveClosureCost = MAX - 50;
    private int nacCost = MAX - 100;
    private int countCost = MAX - 200;
    private int constantCost = 0;

    @Override
    public double getCost(SubPlan currentPlan, PConstraint constraint) {
        Set<PVariable> affectedVariables = constraint.getAffectedVariables();

        int cost = 0;

        // For constants the cost is determined to be 0.0
        // The following constraints should be checks:
        // * Binary transitive closure
        // * NAC
        // * count
        // * exported parameter - only a metadata
        if (constraint instanceof ConstantValue) {
            cost = constantCost;
        } else if (constraint instanceof BinaryTransitiveClosure) {
            cost = binaryTransitiveClosureCost;
        } else if (constraint instanceof NegativePatternCall) {
            cost = nacCost;
        } else if (constraint instanceof PatternMatchCounter) {
            cost = countCost;
        } else if (constraint instanceof ExportedParameter) {
            cost = exportedParameterCost;
        } else {
            // In case of other constraints count the number of unbound variables
            for (PVariable pVariable : affectedVariables) {
                if (!currentPlan.getAllDeducedVariables().contains(pVariable)) {
                    // For each free variable ('without-value-variable') increase cost
                    cost += 1;
                }
            }
            if (cost == affectedVariables.size()) {
                // If all the variables are free, double the cost.
                // This ensures that iteration costs more
                cost *= 2;
            }

        }

        return (double) cost;
    }

}
