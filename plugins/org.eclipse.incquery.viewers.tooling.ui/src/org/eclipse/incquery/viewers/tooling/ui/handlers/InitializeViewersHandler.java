package org.eclipse.incquery.viewers.tooling.ui.handlers;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.incquery.patternlanguage.patternLanguage.Pattern;
import org.eclipse.incquery.runtime.api.IModelConnectorTypeEnum;
import org.eclipse.incquery.runtime.api.IPatternMatch;
import org.eclipse.incquery.runtime.exception.IncQueryException;
import org.eclipse.incquery.tooling.ui.queryexplorer.adapters.EMFModelConnector;
import org.eclipse.incquery.tooling.ui.queryexplorer.content.matcher.ObservablePatternMatcher;
import org.eclipse.incquery.tooling.ui.queryexplorer.content.matcher.ObservablePatternMatcherRoot;
import org.eclipse.incquery.viewers.runtime.model.ViewerDataFilter;
import org.eclipse.incquery.viewers.tooling.ui.views.ViewersToolingViewsUtil;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;

public abstract class InitializeViewersHandler extends AbstractHandler {

	IModelConnectorTypeEnum type;
	
	public InitializeViewersHandler(IModelConnectorTypeEnum modelconnectortype) {
		super();
		type = modelconnectortype;
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
	        
	        ISelection selection = HandlerUtil.getActiveMenuSelection(event);
	        if (selection instanceof TreeSelection) {
	            ObservablePatternMatcherRoot root = getSelectedMatcherRoot(selection);
	            
	            try {
	                IEditorPart editorPart = root.getEditorPart();
	                if (editorPart instanceof IEditingDomainProvider) {
	                    IEditingDomainProvider providerEditor = (IEditingDomainProvider) editorPart;
	                    ResourceSet resourceSet = providerEditor.getEditingDomain().getResourceSet();
	                    if (resourceSet.getResources().size() > 0) {
	                        
	                        // calculate patterns that need to be passed to the ZestView
	                        
	                        ArrayList<Pattern> patterns = new ArrayList<Pattern>();
	                        
	                        for (ObservablePatternMatcher opm: root.getMatchers()) {
	                            patterns.add( opm.getMatcher().getPattern() );
	                        }
	                        
	                        
	//                        if (ViewersSandboxView.getInstance() != null) {
	                            ViewerDataFilter filter = prepareFilterInformation(root);
	                            // calculate the single resource that is of interest
	                            EMFModelConnector emc = new EMFModelConnector(editorPart);
	                            ViewersToolingViewsUtil.initializeContentsOnView(emc.getNotifier(type), patterns, filter);
	                    }
	                }
	            } catch (IncQueryException e) {
	                throw new ExecutionException("Error initializing pattern matcher.", e);
	            } catch (IllegalArgumentException e) {
	                throw new ExecutionException("Invalid selrection", e);
	            }
	        }
	        
	        return null;
	    }

	protected ObservablePatternMatcherRoot getSelectedMatcherRoot(ISelection selection) {
	    Object firstElement = ((TreeSelection) selection).getFirstElement();
	    if (firstElement instanceof ObservablePatternMatcherRoot) {
	        return (ObservablePatternMatcherRoot) firstElement;
	    } else if (firstElement instanceof ObservablePatternMatcher) {
	        return ((ObservablePatternMatcher) firstElement).getParent();
	    } else {
	        throw new IllegalArgumentException("Selection should contain an Pattern match from the query explorer");
	    }
	}

	protected ViewerDataFilter prepareFilterInformation(ObservablePatternMatcherRoot root) {
	    ViewerDataFilter dataFilter = new ViewerDataFilter();
	    for (ObservablePatternMatcher matcher : root.getMatchers()) {
	        final Object[] filter = matcher.getFilter();
	        if (Iterables.any(Arrays.asList(filter), Predicates.notNull())) {
	            final IPatternMatch filterMatch = matcher.getMatcher().newMatch(filter);
	            dataFilter.addSingleFilter(matcher.getMatcher().getPattern(), filterMatch);
	        }
	    }
	    return dataFilter;
	}

}