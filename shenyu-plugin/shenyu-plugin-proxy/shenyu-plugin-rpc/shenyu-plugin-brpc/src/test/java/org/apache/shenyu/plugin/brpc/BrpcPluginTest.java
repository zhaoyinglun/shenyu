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

package org.apache.shenyu.plugin.brpc;

import org.apache.shenyu.common.constant.Constants;
import org.apache.shenyu.common.dto.MetaData;
import org.apache.shenyu.common.dto.RuleData;
import org.apache.shenyu.common.dto.SelectorData;
import org.apache.shenyu.plugin.api.ShenyuPluginChain;
import org.apache.shenyu.plugin.api.context.ShenyuContext;
import org.apache.shenyu.plugin.brpc.proxy.BrpcProxyService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * The Test Case For {@link BrpcPlugin}.
 */
public class BrpcPluginTest {

    private static final String PARAM = "{\"name\":\"brpc\"}";

    private BrpcPlugin brpcPlugin;

    private ShenyuPluginChain chain;

    private SelectorData selectorData;

    private ServerWebExchange exchange;

    private RuleData ruleData;

    private MetaData metaData;

    private BrpcProxyService brpcProxyService;

    @BeforeEach
    public void setUp() {
        this.ruleData = mock(RuleData.class);
        this.chain = mock(ShenyuPluginChain.class);
        this.selectorData = mock(SelectorData.class);
        this.exchange = MockServerWebExchange.from(MockServerHttpRequest.get("localhost").build());
        this.metaData = new MetaData();
        this.metaData.setAppName("brpc");
        this.metaData.setContextPath("/brpc");
        this.metaData.setPath("/brpc/hello");
        this.metaData.setRpcType("brpc");
        this.metaData.setServiceName("org.apache.shenyu.examples.brpc.service.DemoService");
        this.metaData.setMethodName("hello");
        this.metaData.setParameterTypes("java.lang.String");
        this.metaData.setEnabled(true);
        metaData.setRpcExt("{\"methodInfo\":[{\"methodName\":\"hello\",\"params\":[{\"left\":\"java.lang.String\",\"right\":\"name\"}]}]}");
        ShenyuContext shenyuContext = mock(ShenyuContext.class);
        exchange.getAttributes().put(Constants.CONTEXT, shenyuContext);
        exchange.getAttributes().put(Constants.PARAM_TRANSFORM, PARAM);
        exchange.getAttributes().put(Constants.META_DATA, metaData);
        this.brpcProxyService = mock(BrpcProxyService.class);
        when(brpcProxyService.genericInvoker(PARAM, metaData, exchange)).thenReturn(Mono.empty());
        this.brpcPlugin = new BrpcPlugin(brpcProxyService);
    }

    @Test
    public void testDoExecute() {
        when(chain.execute(exchange)).thenReturn(Mono.empty());
        Mono<Void> result = brpcPlugin.doExecute(exchange, chain, selectorData, ruleData);
        StepVerifier.create(result).expectSubscription().verifyComplete();
    }

    @Test
    public void testNamed() {
        Assertions.assertEquals(brpcPlugin.named(), "brpc");
    }

    @Test
    public void testSkip() {
        final boolean result = brpcPlugin.skip(exchange);
        Assertions.assertTrue(result);
    }

    @Test
    public void testGetOrder() {
        Assertions.assertEquals(brpcPlugin.getOrder(), 310);
    }
}
