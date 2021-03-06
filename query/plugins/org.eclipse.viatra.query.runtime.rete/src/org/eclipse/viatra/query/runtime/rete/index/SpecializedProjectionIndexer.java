/*******************************************************************************
 * Copyright (c) 2004-2012 Gabor Bergmann and Daniel Varro
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Gabor Bergmann - initial API and implementation
 *******************************************************************************/

package org.eclipse.viatra.query.runtime.rete.index;

import org.eclipse.viatra.query.runtime.matchers.tuple.TupleMask;
import org.eclipse.viatra.query.runtime.rete.network.Node;
import org.eclipse.viatra.query.runtime.rete.network.ReteContainer;
import org.eclipse.viatra.query.runtime.rete.network.Supplier;

/**
 * A specialized projection indexer that can be memory-less (relying on an external source of information).
 * 
 * @author Gabor Bergmann
 * 
 */
public abstract class SpecializedProjectionIndexer extends StandardIndexer implements ProjectionIndexer {

    protected Node activeNode;

    public SpecializedProjectionIndexer(ReteContainer reteContainer, TupleMask mask, Supplier parent, Node activeNode) {
        super(reteContainer, mask);
        this.parent = parent;
        this.activeNode = activeNode;
    }

    @Override
    public Node getActiveNode() {
        return activeNode;
    }

}
