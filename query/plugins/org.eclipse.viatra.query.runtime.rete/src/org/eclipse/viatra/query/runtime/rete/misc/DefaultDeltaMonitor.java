/*******************************************************************************
 * Copyright (c) 2004-2010 Gabor Bergmann and Daniel Varro
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Gabor Bergmann - initial API and implementation
 *******************************************************************************/

package org.eclipse.viatra.query.runtime.rete.misc;

import org.eclipse.viatra.query.runtime.matchers.tuple.Tuple;
import org.eclipse.viatra.query.runtime.rete.network.Network;
import org.eclipse.viatra.query.runtime.rete.network.ReteContainer;

/**
 * Default configuration for DeltaMonitor.
 * 
 * @author Gabor Bergmann
 * 
 */
public class DefaultDeltaMonitor extends DeltaMonitor<Tuple> {

    /**
     * @param reteContainer
     */
    public DefaultDeltaMonitor(ReteContainer reteContainer) {
        super(reteContainer);
    }

    /**
     * @param network
     */
    public DefaultDeltaMonitor(Network network) {
        super(network.getHeadContainer());
    }

    @Override
    public Tuple statelessConvert(Tuple tuple) {
        return tuple;
    }

}
