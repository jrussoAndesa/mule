/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.functional.junit4.runners;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Just a JUnit {@link Runner} that delegates to a decoratee.
 */
public abstract class RunnerDecorator extends Runner
{
    protected final transient Logger logger = LoggerFactory.getLogger(this.getClass());

    protected abstract Runner getDecoratee();

    @Override
    public Description getDescription()
    {
        if (getDecoratee() == null)
        {
            throw new IllegalStateException("The runner decorator has not defined a decoratee");
        }
        return getDecoratee().getDescription();
    }

    @Override
    public void run(RunNotifier notifier)
    {
        if (getDecoratee() == null)
        {
            throw new IllegalStateException("The runner decorator has not defined a decoratee");
        }
        getDecoratee().run(notifier);
    }
}