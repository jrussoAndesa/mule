/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.core.transformer.simple;

import static java.nio.charset.StandardCharsets.US_ASCII;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import org.mule.runtime.api.metadata.DataType;
import org.mule.runtime.core.DefaultMuleMessage;
import org.mule.runtime.core.api.MuleContext;
import org.mule.runtime.core.api.MuleEvent;
import org.mule.runtime.core.api.MuleMessage;
import org.mule.runtime.core.api.MutableMuleMessage;
import org.mule.runtime.core.api.expression.ExpressionManager;
import org.mule.runtime.core.api.lifecycle.InitialisationException;
import org.mule.runtime.core.api.transformer.TransformerException;
import org.mule.runtime.core.util.StringUtils;
import org.mule.tck.junit4.AbstractMuleTestCase;
import org.mule.tck.size.SmallTest;

import java.io.Serializable;
import java.nio.charset.Charset;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

@SmallTest
@RunWith(MockitoJUnitRunner.class)
public class CopyPropertiesTransformerTestCase extends AbstractMuleTestCase
{
    public static final Charset ENCODING = US_ASCII;
    public static final String INBOUND_PROPERTY_KEY = "propKey";
    public static final DataType PROPERTY_DATA_TYPE = DataType.STRING;
    private static final Serializable PROPERTY_VALUE = StringUtils.EMPTY;

    @Mock(answer = RETURNS_DEEP_STUBS)
    private MuleContext mockMuleContext;

    private MutableMuleMessage muleMessage;
    @Mock
    private ExpressionManager mockExpressionManager;

    @Before
    public void setUp() throws Exception
    {
        when(mockMuleContext.getExpressionManager()).thenReturn(mockExpressionManager);
        Mockito.when(mockExpressionManager.parse(anyString(), Mockito.any(MuleEvent.class))).thenAnswer(
                invocation -> (String) invocation.getArguments()[0]);

        muleMessage = new DefaultMuleMessage("", PROPERTY_DATA_TYPE);
    }

    @Test
    public void testCopySingleProperty() throws TransformerException, InitialisationException
    {
        CopyPropertiesTransformer copyPropertiesTransformer = createCopyPropertiesTransformer(INBOUND_PROPERTY_KEY);
        muleMessage.setInboundProperty(INBOUND_PROPERTY_KEY, PROPERTY_VALUE, PROPERTY_DATA_TYPE);

        final MuleMessage transformed = (MuleMessage) copyPropertiesTransformer.transform(muleMessage, ENCODING);

        assertThat(transformed.getOutboundProperty(INBOUND_PROPERTY_KEY), is(PROPERTY_VALUE));
        assertThat(transformed.getInboundPropertyNames(), hasSize(1));
    }

    @Test
    public void testCopyNonExistentProperty() throws TransformerException, InitialisationException
    {
        CopyPropertiesTransformer copyPropertiesTransformer = createCopyPropertiesTransformer(INBOUND_PROPERTY_KEY);
        muleMessage.setInboundProperty(INBOUND_PROPERTY_KEY, null);

        final MuleMessage transformed = (MuleMessage) copyPropertiesTransformer.transform(muleMessage, ENCODING);

        assertThat(transformed.getInboundPropertyNames(), hasSize(0));
    }

    @Test
    public void testCopyUsingRegex() throws InitialisationException, TransformerException
    {
        CopyPropertiesTransformer copyPropertiesTransformer = createCopyPropertiesTransformer("MULE_*");

        muleMessage.setInboundProperty("MULE_ID", PROPERTY_VALUE, PROPERTY_DATA_TYPE);
        muleMessage.setInboundProperty("MULE_CORRELATION_ID", PROPERTY_VALUE, PROPERTY_DATA_TYPE);
        muleMessage.setInboundProperty("MULE_GROUP_ID", PROPERTY_VALUE, PROPERTY_DATA_TYPE);
        muleMessage.setInboundProperty("SomeVar", PROPERTY_VALUE, PROPERTY_DATA_TYPE);

        final MuleMessage transformed = (MuleMessage) copyPropertiesTransformer.transform(muleMessage, ENCODING);

        assertThat(transformed.getOutboundProperty("SomeVar"), is(nullValue()));
        assertThat(transformed.getOutboundProperty("MULE_ID"), is(PROPERTY_VALUE));
        assertThat(transformed.getOutboundProperty("MULE_CORRELATION_ID"), is(PROPERTY_VALUE));
        assertThat(transformed.getOutboundProperty("MULE_GROUP_ID"), is(PROPERTY_VALUE));
    }

    public CopyPropertiesTransformer createCopyPropertiesTransformer(String inboundPropertyKey) throws InitialisationException
    {
        CopyPropertiesTransformer copyPropertiesTransformer = new CopyPropertiesTransformer();
        copyPropertiesTransformer.setMuleContext(mockMuleContext);
        copyPropertiesTransformer.setPropertyName(inboundPropertyKey);
        copyPropertiesTransformer.initialise();

        return copyPropertiesTransformer;
    }
}
