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

package org.apache.shenyu.plugin.base.utils;

import org.apache.shenyu.common.dto.RuleData;

/**
 * CacheKeyUtils.
 */
public enum CacheKeyUtils {
    
    /**
     * Inst singleton.
     */
    INST;
    
    /**
     * return rule handle cache key name.
     *
     * @param ruleData ruleData
     * @return string string
     */
    public String getKey(final RuleData ruleData) {
        return String.join("_", ruleData.getSelectorId(), ruleData.getId());
    }
    
    /**
     * Gets key.
     *
     * @param selectorId the selector id
     * @param ruleId the rule id
     * @return the key
     */
    public String getKey(final String selectorId, final String ruleId) {
        return String.join("_", selectorId, ruleId);
    }
}
