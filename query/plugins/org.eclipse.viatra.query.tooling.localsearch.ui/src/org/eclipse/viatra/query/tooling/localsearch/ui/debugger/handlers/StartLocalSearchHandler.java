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
package org.eclipse.viatra.query.tooling.localsearch.ui.debugger.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.viatra.query.runtime.api.AdvancedViatraQueryEngine;
import org.eclipse.viatra.query.runtime.api.IQuerySpecification;
import org.eclipse.viatra.query.runtime.exception.ViatraQueryException;
import org.eclipse.viatra.query.runtime.localsearch.matcher.LocalSearchMatcher;
import org.eclipse.viatra.query.runtime.localsearch.matcher.integration.LocalSearchBackendFactory;
import org.eclipse.viatra.query.runtime.localsearch.matcher.integration.LocalSearchResultProvider;
import org.eclipse.viatra.query.runtime.matchers.backend.IQueryBackend;
import org.eclipse.viatra.query.runtime.matchers.planning.QueryProcessingException;
import org.eclipse.viatra.query.tooling.localsearch.ui.debugger.LocalSearchDebugger;
import org.eclipse.viatra.query.tooling.ui.queryexplorer.content.matcher.PatternMatcherContent;

/**
 * 
 * This class is only for testing and introductory purposes and should be replaced soon.
 * 
 * @author Marton Bur
 *
 */
public class StartLocalSearchHandler extends AbstractHandler {

    public static Thread planExecutorThread = null;

    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException {

        try {
            final ISelection selection = HandlerUtil.getCurrentSelection(event);
            if (selection instanceof IStructuredSelection) {
                final Object obj = ((IStructuredSelection) selection).iterator().next();
                PatternMatcherContent content = (PatternMatcherContent) obj;
                final IQuerySpecification<?> specification = content.getSpecification();
                final AdvancedViatraQueryEngine engine = content.getParent().getKey().getEngine();
                final IQueryBackend lsBackend = engine.getQueryBackend(LocalSearchBackendFactory.INSTANCE);
                final Object[] adornment = content.getFilter();

                final LocalSearchResultProvider lsResultProvider = (LocalSearchResultProvider) lsBackend
                        .getResultProvider(specification.getInternalQueryRepresentation());

                
                // Create and start the matcher thread
                Runnable planExecutorRunnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final LocalSearchMatcher localSearchMatcher = lsResultProvider.newLocalSearchMatcher(adornment);
                            LocalSearchDebugger debugger = new LocalSearchDebugger();
                            localSearchMatcher.addAdapter(debugger);
                            debugger.setStartHandlerCalled(true);

                            // Initiate the matching
                            localSearchMatcher.getAllMatches();
                        } catch (final Exception e) {
                            final Shell shell = HandlerUtil.getActiveShell(event);
                            shell.getDisplay().asyncExec(new Runnable() {
                                
                                @Override
                                public void run() {
                                    MessageDialog.open(MessageDialog.ERROR, shell, "Local search debugger", "Error while initializing local search debugger: " + e.getMessage(), SWT.SHEET);
                                    
                                }
                            });
                            throw new RuntimeException(e);
                        }
                    }
                };

                if (planExecutorThread == null || !planExecutorThread.isAlive()) {
                    // Start the matching process if not started or in progress yet
                    planExecutorThread = new Thread(planExecutorRunnable);
                    planExecutorThread.start();
                } else if(planExecutorThread.isAlive()){
                    planExecutorThread.interrupt();
                    planExecutorThread = new Thread(planExecutorRunnable);
                    planExecutorThread.start();
                }
            }
        } catch (ViatraQueryException|QueryProcessingException e ) {
            throw new ExecutionException("Error starting local search debugger", e);
        }

        return null;
    }
}
