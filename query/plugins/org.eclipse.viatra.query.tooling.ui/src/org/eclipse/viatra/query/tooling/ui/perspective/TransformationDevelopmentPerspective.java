/*******************************************************************************
 * Copyright (c) 2010-2016, Abel Hegedus, IncQuery Labs Ltd.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Abel Hegedus - initial API and implementation
 *******************************************************************************/
package org.eclipse.viatra.query.tooling.ui.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.progress.IProgressConstants;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.ui.JavaUI;

/**
 * The VIATRA Transformation Development perspective defines the default setup for the different views provided by the
 * Query and Transformation SDK.
 * 
 * @author Abel Hegedus
 *
 */
public class TransformationDevelopmentPerspective implements IPerspectiveFactory {

    private IPageLayout factory;

    public TransformationDevelopmentPerspective() {
        super();
    }

    public void createInitialLayout(IPageLayout factory) {
        this.factory = factory;
        addViews();
        addNewWizardShortcuts();
        addViewShortcuts();
    }

    private void addViews() {
        // Creates the overall folder layout.
        // Note that each new Folder uses a percentage of the remaining EditorArea.
        IFolderLayout left = factory.createFolder("left", // NON-NLS-1
                IPageLayout.LEFT, 0.25f, factory.getEditorArea());
        left.addView(IPageLayout.ID_PROJECT_EXPLORER);
        left.addPlaceholder(JavaUI.ID_TYPE_HIERARCHY);
        left.addPlaceholder(JavaUI.ID_PACKAGES);

        IFolderLayout bottom = factory.createFolder("bottomRight", // NON-NLS-1
                IPageLayout.BOTTOM, 0.75f, factory.getEditorArea());
        bottom.addView("org.eclipse.viatra.query.tooling.ui.queryexplorer.QueryExplorer"); // NON-NLS-1
        bottom.addView(IPageLayout.ID_PROP_SHEET);
        bottom.addView(IPageLayout.ID_PROBLEM_VIEW);
        bottom.addPlaceholder(IConsoleConstants.ID_CONSOLE_VIEW);
        bottom.addPlaceholder(IPageLayout.ID_BOOKMARKS);
        bottom.addPlaceholder(IProgressConstants.PROGRESS_VIEW_ID);

        IFolderLayout right = factory.createFolder("right", IPageLayout.RIGHT, 0.75f, factory.getEditorArea()); //$NON-NLS-1$
        right.addView(IPageLayout.ID_OUTLINE);

    }

    private void addNewWizardShortcuts() {
        factory.addNewWizardShortcut("org.eclipse.viatra.query.tooling.ui.newproject");// NON-NLS-1
        factory.addNewWizardShortcut("org.eclipse.viatra.query.tooling.ui.newvqlfile");// NON-NLS-1
        factory.addNewWizardShortcut("org.eclipse.viatra.query.tooling.ui.newvqlgenmodel");// NON-NLS-1
    }

    private void addViewShortcuts() {
        factory.addShowViewShortcut("org.eclipse.viatra.query.tooling.ui.queryexplorer.QueryExplorer"); // NON-NLS-1
    }

}
