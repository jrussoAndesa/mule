/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.runtime.module.db.integration.delete;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import org.mule.runtime.core.api.processor.MessageProcessor;
import org.mule.common.Result;
import org.mule.common.metadata.DefaultListMetaDataModel;
import org.mule.common.metadata.MetaData;
import org.mule.common.metadata.SimpleMetaDataModel;
import org.mule.common.metadata.datatype.DataType;
import org.mule.runtime.core.construct.Flow;
import org.mule.runtime.module.db.integration.AbstractDbIntegrationTestCase;
import org.mule.runtime.module.db.integration.model.AbstractTestDatabase;
import org.mule.runtime.module.db.integration.TestDbConfig;
import org.mule.runtime.module.db.internal.processor.PreparedBulkUpdateMessageProcessor;

import java.util.List;

import org.junit.Test;
import org.junit.runners.Parameterized;

public class DeleteBulkOutputMetadataTestCase extends AbstractDbIntegrationTestCase
{

    public DeleteBulkOutputMetadataTestCase(String dataSourceConfigResource, AbstractTestDatabase testDatabase)
    {
        super(dataSourceConfigResource, testDatabase);
    }

    @Parameterized.Parameters
    public static List<Object[]> parameters()
    {
        return TestDbConfig.getResources();
    }

    @Override
    protected String[] getFlowConfigurationResources()
    {
        return new String[] {"integration/delete/delete-bulk-output-metadata-config.xml"};
    }

    @Test
    public void returnsDeleteBulkMetadata() throws Exception
    {
        Flow flowConstruct = (Flow) muleContext.getRegistry().lookupFlowConstruct("deleteBulkMetadata");

        List<MessageProcessor> messageProcessors = flowConstruct.getMessageProcessors();
        PreparedBulkUpdateMessageProcessor queryMessageProcessor = (PreparedBulkUpdateMessageProcessor) messageProcessors.get(0);
        Result<MetaData> outputMetaData = queryMessageProcessor.getOutputMetaData(null);

        DefaultListMetaDataModel listMetaDataModel = (DefaultListMetaDataModel) outputMetaData.get().getPayload();
        assertThat(listMetaDataModel.isArray(), equalTo(true));
        SimpleMetaDataModel elementModel = (SimpleMetaDataModel) listMetaDataModel.getElementModel();
        assertThat(elementModel.getDataType(), equalTo(DataType.DOUBLE));
    }
}
