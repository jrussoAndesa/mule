/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.module.json.filters;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.mule.runtime.api.message.NullPayload;
import org.mule.runtime.core.DefaultMuleMessage;
import org.mule.tck.junit4.AbstractMuleContextTestCase;
import org.junit.Test;

public class IsJsonFilterTestCase extends AbstractMuleContextTestCase
{

    private IsJsonFilter filter;

    @Override
    protected void doSetUp() throws Exception 
    {
        filter = new IsJsonFilter();
        filter.setValidateParsing(true);
        filter.setMuleContext(muleContext);
    }
    
    @Test
    public void testFilterFalse() throws Exception
    {
        assertFalse(filter.accept(new DefaultMuleMessage("This is definitely not JSON.")));
    }

    @Test
    public void testFilterFalse2() throws Exception
    {
        assertFalse(filter.accept(new DefaultMuleMessage("{name=\"This may be JSON\",bool:}")));
    }
    
    @Test
    public void testFilterFalse3() throws Exception
    {
        assertFalse(filter.accept(new DefaultMuleMessage("[name=\"This may be JSON\",bool:]")));
    }
    
    @Test
    public void testFilterTrue() throws Exception
    {
        assertTrue(filter.accept(new DefaultMuleMessage("{\n" +
                "        \"in_reply_to_user_id\":null,\n" +
                                                        "        \"text\":\"test from Mule: 6ffca02b-9d52-475e-8b17-946acdb01492\"}")));
    }
    
    @Test
    public void testFilterNull() throws Exception
    {
        assertFalse(filter.accept(new DefaultMuleMessage(NullPayload.getInstance())));
    }

    @Test
    public void testFilterWithObject() throws Exception
    {
        assertFalse(filter.accept(new DefaultMuleMessage(new Object())));
    }

}
