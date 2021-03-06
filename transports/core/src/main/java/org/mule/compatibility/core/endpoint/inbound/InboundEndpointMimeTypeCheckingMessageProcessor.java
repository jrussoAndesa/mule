/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.compatibility.core.endpoint.inbound;

import static org.mule.runtime.core.api.config.MuleProperties.CONTENT_TYPE_PROPERTY;

import org.mule.compatibility.core.api.endpoint.InboundEndpoint;
import org.mule.runtime.api.metadata.DataType;
import org.mule.runtime.core.api.MessagingException;
import org.mule.runtime.core.api.MuleEvent;
import org.mule.runtime.core.api.MuleMessage;
import org.mule.runtime.core.api.processor.MessageProcessor;
import org.mule.runtime.core.config.i18n.CoreMessages;
import org.mule.runtime.core.util.ObjectUtils;

/**
 * Verify that the inbound mime type is acceptable by this endpoint.
 */
public class InboundEndpointMimeTypeCheckingMessageProcessor implements MessageProcessor
{
    private InboundEndpoint endpoint;

    public InboundEndpointMimeTypeCheckingMessageProcessor(InboundEndpoint endpoint)
    {
        this.endpoint = endpoint;
    }

    @Override
    public MuleEvent process(MuleEvent event) throws MessagingException
    {
        org.mule.runtime.api.metadata.MediaType endpointMimeType = endpoint.getMimeType();
        if (endpointMimeType != null)
        {
            MuleMessage message = event.getMessage();
            String contentType = message.getInboundProperty(CONTENT_TYPE_PROPERTY);
            if (contentType == null)
            {
                contentType = message.getOutboundProperty(CONTENT_TYPE_PROPERTY);
            }
            if (contentType == null)
            {
                event.setMessage(event.getMessage().transform(msg -> {
                    msg.setInboundProperty(CONTENT_TYPE_PROPERTY, endpointMimeType.toString());
                    return msg;
                }));
            }
            else
            {
                String messageMimeType = DataType.builder().mediaType(contentType).build().getMediaType().toString();
                if (!messageMimeType.equals(endpointMimeType.toString()))
                {
                    throw new MessagingException(
                            CoreMessages.unexpectedMIMEType(messageMimeType, endpointMimeType.toString()), event, this);
                }
            }
        }

        return event;
    }

    @Override
    public String toString()
    {
        return ObjectUtils.toString(this);
    }
}
