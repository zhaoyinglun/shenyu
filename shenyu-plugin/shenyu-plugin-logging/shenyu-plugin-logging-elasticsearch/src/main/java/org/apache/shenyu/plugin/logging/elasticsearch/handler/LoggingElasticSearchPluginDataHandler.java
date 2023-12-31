/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shenyu.plugin.logging.elasticsearch.handler;

import org.apache.shenyu.common.enums.PluginEnum;
import org.apache.shenyu.plugin.logging.common.collector.LogCollector;
import org.apache.shenyu.plugin.logging.common.config.GenericApiConfig;
import org.apache.shenyu.plugin.logging.common.handler.AbstractLogPluginDataHandler;
import org.apache.shenyu.plugin.logging.elasticsearch.client.ElasticSearchLogCollectClient;
import org.apache.shenyu.plugin.logging.elasticsearch.collector.ElasticSearchLogCollector;
import org.apache.shenyu.plugin.logging.elasticsearch.config.ElasticSearchLogCollectConfig;

/**
 * The type logging elasticsearch plugin data handler.
 */
public class LoggingElasticSearchPluginDataHandler extends AbstractLogPluginDataHandler<ElasticSearchLogCollectConfig.ElasticSearchLogConfig, GenericApiConfig> {

    private static final ElasticSearchLogCollectClient ELASTICSEARCH_LOG_COLLECT_CLIENT = new ElasticSearchLogCollectClient();

    /**
     * logCollector.
     */
    @Override
    protected LogCollector logCollector() {
        return ElasticSearchLogCollector.getInstance();
    }

    /**
     * doRefreshConfig.
     *
     * @param globalLogConfig globalLogConfig
     */
    @Override
    protected void doRefreshConfig(final ElasticSearchLogCollectConfig.ElasticSearchLogConfig globalLogConfig) {
        ElasticSearchLogCollectConfig.INSTANCE.setElasticSearchLogConfig(globalLogConfig);
        ELASTICSEARCH_LOG_COLLECT_CLIENT.initClient(globalLogConfig);
    }

    /**
     * plugin named.
     * @return plugin named.
     */
    @Override
    public String pluginNamed() {
        return PluginEnum.LOGGING_ELASTIC_SEARCH.getName();
    }

    /**
     * get elasticsearch log collect client.
     * @return elasticsearch log collect client.
     */
    public static ElasticSearchLogCollectClient getElasticSearchLogCollectClient() {
        return ELASTICSEARCH_LOG_COLLECT_CLIENT;
    }
}
