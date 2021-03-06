/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.core.util.monitor;

/**
 * <code>Expirable</code> implementing classes can be notified when they expire
 */

public interface Expirable
{
    void expired();
}
