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

package org.apache.shenyu.plugin.mock.generator;

import org.apache.shenyu.common.utils.JsonUtils;
import org.apache.shenyu.plugin.mock.api.MockRequest;
import org.apache.shenyu.plugin.mock.util.EvaluationContextUtil;
import org.apache.shenyu.spi.Join;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.DataBindingPropertyAccessor;
import org.springframework.expression.spel.support.SimpleEvaluationContext;

import java.util.List;

/**
 * The simplified version of the SEPL parsing implementation does not support write function execution.
 *
 * @see SimpleEvaluationContext
 * @see DataBindingPropertyAccessor#forReadOnlyAccess()
 */
@Join
public class ExpressionGenerator implements Generator<String> {
    
    private static final ExpressionParser PARSER = new SpelExpressionParser();
    
    private static final EvaluationContext CONTEXT = initContext();
    
    @Override
    public String getName() {
        return "expression";
    }
    
    @Override
    public String doGenerate(final List<String> params, final String rule, final MockRequest mockRequest) {
        
        String expression = params.get(0);
        
        CONTEXT.setVariable("req", mockRequest);
        
        Object val = PARSER.parseExpression(expression).getValue(CONTEXT);
        return JsonUtils.toJson(val);
    }
    
    @Override
    public int getParamSize() {
        return 1;
    }
    
    @Override
    public boolean match(final String rule) {
        return rule.matches("^" + getName() + "\\|.+");
    }
    
    private static EvaluationContext initContext() {
        
        EvaluationContext context = SimpleEvaluationContext
                .forPropertyAccessors(DataBindingPropertyAccessor.forReadOnlyAccess(), new MapAccessor())
                .build();
        
        EvaluationContextUtil.init(context);
        return context;
    }
}
