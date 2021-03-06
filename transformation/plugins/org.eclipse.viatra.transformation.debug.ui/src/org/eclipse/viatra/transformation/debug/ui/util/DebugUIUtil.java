/**
 * Copyright (c) 2010-2016, Peter Lunk, IncQuery Labs Ltd.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Peter Lunk - initial API and implementation
 */
package org.eclipse.viatra.transformation.debug.ui.util;

import java.util.List;

import org.eclipse.viatra.transformation.debug.model.TransformationThread;
import org.eclipse.viatra.transformation.debug.model.TransformationThreadFactory;
import org.eclipse.viatra.transformation.evm.api.Activation;

public class DebugUIUtil {
    public static TransformationThread getActivationThread(Activation<?> activation){
        List<TransformationThread> threads = TransformationThreadFactory.getInstance().getTransformationThreads();
        for (TransformationThread transformationThread : threads) {
            if(transformationThread.containsActivation(activation)){
                return transformationThread;
            }
        }
        return null;
    }
}
