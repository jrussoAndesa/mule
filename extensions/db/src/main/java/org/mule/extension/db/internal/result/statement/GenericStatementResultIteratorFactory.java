/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.extension.db.internal.result.statement;

import org.mule.runtime.module.db.internal.domain.autogeneratedkey.AutoGeneratedKeyStrategy;
import org.mule.runtime.module.db.internal.domain.connection.DbConnection;
import org.mule.runtime.module.db.internal.domain.query.QueryTemplate;
import org.mule.runtime.module.db.internal.result.resultset.ResultSetHandler;

import java.sql.Statement;

/**
 * Creates {@link StatementResultIterator} for generic database configurations
 */
public class GenericStatementResultIteratorFactory implements StatementResultIteratorFactory
{

    private final ResultSetHandler resultSetHandler;

    public GenericStatementResultIteratorFactory(ResultSetHandler resultSetHandler)
    {
        this.resultSetHandler = resultSetHandler;
    }

    @Override
    public final StatementResultIterator create(DbConnection connection, Statement statement, QueryTemplate queryTemplate, AutoGeneratedKeyStrategy autoGeneratedKeyStrategy)
    {
        return doCreateStatementResultIterator(connection, statement, queryTemplate, autoGeneratedKeyStrategy, resultSetHandler);
    }

    protected StatementResultIterator doCreateStatementResultIterator(DbConnection connection, Statement statement, QueryTemplate queryTemplate, AutoGeneratedKeyStrategy autoGeneratedKeyStrategy, ResultSetHandler resultSetHandler)
    {
        return new StatementResultIterator(connection, statement, queryTemplate, autoGeneratedKeyStrategy, resultSetHandler);
    }
}
