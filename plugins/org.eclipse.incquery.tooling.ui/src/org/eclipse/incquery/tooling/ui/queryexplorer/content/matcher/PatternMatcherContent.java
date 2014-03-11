/*******************************************************************************
 * Copyright (c) 2010-2012, Zoltan Ujhelyi, Tamas Szabo, Istvan Rath and Daniel Varro
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Zoltan Ujhelyi, Tamas Szabo - initial API and implementation
 *******************************************************************************/

package org.eclipse.incquery.tooling.ui.queryexplorer.content.matcher;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.incquery.databinding.runtime.adapter.DatabindingAdapterUtil;
import org.eclipse.incquery.runtime.api.IPatternMatch;
import org.eclipse.incquery.runtime.api.IQuerySpecification;
import org.eclipse.incquery.runtime.api.IncQueryEngine;
import org.eclipse.incquery.runtime.api.IncQueryMatcher;
import org.eclipse.incquery.runtime.api.IncQueryModelUpdateListener;
import org.eclipse.incquery.runtime.exception.IncQueryException;
import org.eclipse.incquery.runtime.matchers.psystem.annotations.PAnnotation;
import org.eclipse.incquery.runtime.rete.misc.DeltaMonitor;
import org.eclipse.incquery.tooling.ui.queryexplorer.QueryExplorer;
import org.eclipse.incquery.tooling.ui.queryexplorer.util.DisplayUtil;
import org.eclipse.swt.widgets.Display;

/**
 * The middle level element in the tree viewer of the {@link QueryExplorer}. Instances of this class represent the
 * various patterns (generated or runtime) that are loaded during runtime.
 * 
 * @author Tamas Szabo (itemis AG)
 * 
 */
public class PatternMatcherContent extends
        CompositeContent<PatternMatcherRootContent, PatternMatchContent> {

    private static int maxDisplayMatchSetSize = 100;
    private int actualMatchSetSize = 0;

    private static final String KEY_ATTRIBUTE_COMPARABLE_INTERFACE = "The key attribute does not implement the Comparable interface!";
    private static final String KEY_ATTRIBUTE_OF_ORDER_BY_ANNOTATION = "The key attribute of OrderBy annotation must look like \"ClassName.AttributeName\"!";
    private Map<IPatternMatch, PatternMatchContent> mapping;
    private final boolean generated;
    private IPatternMatch filter;
    private Object[] parameterFilter;
    private String orderParameter;
    private boolean descendingOrder;
    private final String exceptionMessage;
    private IncQueryModelUpdateListener modelUpdateListener;
    private IQuerySpecification<?> specification;
    IncQueryMatcher<IPatternMatch> matcher;
    private DeltaMonitor<IPatternMatch> deltaMonitor;
    private Runnable processMatchesRunnable;

    @SuppressWarnings({ "deprecation", "unchecked" })
    public PatternMatcherContent(PatternMatcherRootContent parent, IncQueryEngine engine,
            final IQuerySpecification<?> specification, boolean generated) {
        super(parent);
        this.specification = specification;

        String message = "";
        try {
            matcher = (IncQueryMatcher<IPatternMatch>) engine.getMatcher(specification);
        } catch(IncQueryException e) {
            message = e.getShortMessage();
        } catch (Exception e) {
            message = e.getMessage();
        }

        this.exceptionMessage = message;
        this.generated = generated;
        this.orderParameter = null;
        
        DisplayUtil.removeOrderByPatternWarning(specification.getFullyQualifiedName());

        if (this.matcher != null) {
            initOrdering();
            initFilter();
            
            setText(DisplayUtil.getMessage(matcher, actualMatchSetSize, specification.getFullyQualifiedName(), isCropped(),
                    isGenerated(), isFiltered(), exceptionMessage));
            
            this.mapping = new HashMap<IPatternMatch, PatternMatchContent>();

            this.deltaMonitor = this.matcher.newFilteredDeltaMonitor(true, filter);
            this.processMatchesRunnable = new Runnable() {
                @Override
                public void run() {
                    if (deltaMonitor.matchFoundEvents.size() > 0 || deltaMonitor.matchLostEvents.size() > 0) {
                        processNewMatches(deltaMonitor.matchFoundEvents);
                        processLostMatches(deltaMonitor.matchLostEvents);
                        deltaMonitor.clear();
                    }
                    setText(DisplayUtil.getMessage(matcher, actualMatchSetSize, specification.getFullyQualifiedName(), isCropped(),
                            isGenerated(), isFiltered(), exceptionMessage));
                }
            };

            modelUpdateListener = new IncQueryModelUpdateListener() {

                @Override
                public void notifyChanged(ChangeLevel changeLevel) {
                    // invoke the processing runnable on the UI thread, to ensure
                    // databinding does not complain if the model is not
                    // originally modified on the UI thread
                    Display.getDefault().asyncExec(processMatchesRunnable);
                }

                @Override
                public ChangeLevel getLevel() {
                    return ChangeLevel.MATCHSET;
                }
            };
            parent.getKey().getEngine().addModelUpdateListener(modelUpdateListener);
            this.processMatchesRunnable.run();
        }
    }

    /**
     * Initializes the matcher for ordering if the annotation is present.
     */
    private void initOrdering() {
        PAnnotation annotation = specification.getFirstAnnotationByName(DisplayUtil.ORDERBY_ANNOTATION);
        if (annotation != null) {
            for (Entry<String, Object> ap : annotation.getAllValues()) {
                if (ap.getKey().matches("key")) {
                    orderParameter = (String) ap.getValue();
                }
                if (ap.getKey().matches("direction")) {
                    String direction = ((String) ap.getValue());
                    if (direction.matches("desc")) {
                        descendingOrder = true;
                    } else {
                        descendingOrder = false;
                    }
                }
            }
        }
    }

    /**
     * Returns the index of the new match in the list based on the ordering set on the matcher.
     * 
     * @param match
     *            the match that will be inserted
     * @return -1 if the match should be inserted at the end of the list, else the actual index
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private int placeOfMatch(IPatternMatch match) {
        if (orderParameter != null) {
            String[] tokens = orderParameter.split("\\.");

            if (tokens.length == 2) {
                String orderParameterClass = tokens[0];
                String orderParameterAttribute = tokens[1];

                EObject obj = (EObject) match.get(orderParameterClass);
                EStructuralFeature feature = DatabindingAdapterUtil.getFeature(obj, orderParameterAttribute);
                Object value = obj.eGet(feature);
                if (value instanceof Comparable) {

                    for (int i = 0; i < children.size(); i++) {
                        IPatternMatch compMatch = ((PatternMatchContent) children.get(i))
                                .getPatternMatch();
                        EObject compObj = (EObject) compMatch.get(orderParameterClass);
                        EStructuralFeature compFeature = DatabindingAdapterUtil.getFeature(compObj,
                                orderParameterAttribute);
                        Comparable compValue = (Comparable) compObj.eGet(compFeature);
                        // descending order, the new position is the index where the current match param is greater than
                        // the actual element
                        if (descendingOrder) {
                            if (compValue.compareTo(value) <= 0) {
                                return i;
                            }
                        }
                        // ascending order, the new position is the index where the current match param is smaller than
                        // the actual element
                        else {
                            if (compValue.compareTo(value) >= 0) {
                                return i;
                            }
                        }
                    }
                } else {
                    DisplayUtil.addOrderByPatternWarning(this.matcher.getPatternName(),
                            KEY_ATTRIBUTE_COMPARABLE_INTERFACE);
                }
            } else {
                DisplayUtil.addOrderByPatternWarning(this.matcher.getPatternName(),
                        KEY_ATTRIBUTE_OF_ORDER_BY_ANNOTATION);
            }
        }
        return -1;
    }

    private void processNewMatches(Collection<IPatternMatch> matches) {
        for (IPatternMatch s : matches) {
            actualMatchSetSize++;
            addMatch(s);
        }
    }

    private void processLostMatches(Collection<IPatternMatch> matches) {
        for (IPatternMatch s : matches) {
            actualMatchSetSize--;
            removeMatch(s);
        }
    }

    private void addMatch(IPatternMatch match) {
        if (actualMatchSetSize <= maxDisplayMatchSetSize) {
            PatternMatchContent pm = new PatternMatchContent(this, match);
            this.mapping.put(match, pm);
            int index = placeOfMatch(match);

            if (index == -1) {
                this.children.addChild(pm);
            } else {
                this.children.addChild(index, pm);
            }
        }
    }

    private void removeMatch(IPatternMatch match) {
        // null checks - eclipse closing - issue 162
        PatternMatchContent observableMatch = this.mapping.remove(match);
        if (observableMatch != null) {
            this.children.removeChild(observableMatch);
            observableMatch.dispose();
        }
    }

    public IQuerySpecification<?> getSpecification() {
        return specification;
    }

    public IncQueryMatcher<IPatternMatch> getMatcher() {
        return matcher;
    }

    public String getPatternName() {
        return specification.getFullyQualifiedName();
    }

    private void initFilter() {
        if (matcher != null) {
            final int arity = this.matcher.getParameterNames().size();
            parameterFilter = new Object[arity];
            this.filter = this.matcher.newMatch(parameterFilter);
        }
    }

    @SuppressWarnings("deprecation")
    public void setFilter(Object[] parameterFilter) {
        this.parameterFilter = parameterFilter.clone();
        this.filter = this.matcher.newMatch(this.parameterFilter);

        Set<IPatternMatch> tmp = new HashSet<IPatternMatch>(mapping.keySet());

        for (IPatternMatch match : tmp) {
            removeMatch(match);
        }

        this.actualMatchSetSize = 0;
        this.deltaMonitor = this.matcher.newFilteredDeltaMonitor(true, filter);
        this.processMatchesRunnable.run();
    }

    private boolean isFiltered() {
        if (matcher != null) {
            for (int i = 0; i < this.matcher.getParameterNames().size(); i++) {
                if (parameterFilter[i] != null) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Returns the current filter used on the corresponding matcher.
     * 
     * @return the filter as an array of objects
     */
    public Object[] getFilter() {
        return parameterFilter;
    }

    /**
     * Returns true if the matcher is generated, false if it is generic.
     * 
     * @return true for generated, false for generic matcher
     */
    public boolean isGenerated() {
        return generated;
    }

    public boolean isCropped() {
        return actualMatchSetSize > maxDisplayMatchSetSize;
    }

    /**
     * Returns true if the RETE matcher was created for this observable matcher, false otherwise.
     * 
     * @return true if matcher could be created
     */
    public boolean isCreated() {
        return matcher != null;
    }

    /**
     * If the engine becomes tainted stop monitoring the matcher. This way the previous match set will remain stable and
     * the user can still observe the contents.
     */
    public void stopMonitoring() {
        ((PatternMatcherRootContent) parent).getKey().getEngine()
                .removeModelUpdateListener(modelUpdateListener);
    }

}