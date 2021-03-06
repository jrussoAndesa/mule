/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.core.routing;

import org.mule.runtime.core.api.MuleEvent;
import org.mule.runtime.core.api.processor.MessageProcessor;
import org.mule.runtime.core.config.i18n.Message;
import org.mule.runtime.core.routing.RoutingFailedMessagingException;

import java.lang.Throwable;

/**
 * Exception thrown when a route for an event can not be found
 */
public class RouteResolverException extends RoutingFailedMessagingException
{

    public RouteResolverException(MuleEvent event, Throwable cause)
    {
        super(event, cause);
    }

    public RouteResolverException(Message message, MuleEvent event, Throwable cause)
    {
        super(message, event, cause);
    }

    public RouteResolverException(Message message, MuleEvent event)
    {
        super(message, event);
    }

}
