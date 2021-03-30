/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.karaf.boot.service;

import lombok.extern.java.Log;
import org.apache.karaf.boot.config.KarafConfig;

import java.util.Map;
import java.util.ServiceLoader;

@Log
public class ServiceManager {

    public ServiceManager(KarafConfig config) {
        ServiceLoader<org.apache.karaf.boot.spi.Service> services = ServiceLoader.load(org.apache.karaf.boot.spi.Service.class);
        services.forEach(service -> {
            log.info("Starting " + service.getName() + " service");
            Map<String, Object> properties = null;
            if (config != null && config.getLauncher() != null && config.getLauncher().getServices() != null) {
                for (org.apache.karaf.boot.config.Service serviceConfig : config.getLauncher().getServices()) {
                    if (serviceConfig.getName().equals(service.getName())) {
                        properties = serviceConfig.getProperties();
                    }
                }
            }
            try {
                service.init(properties);
            } catch (Exception e) {
                throw new RuntimeException("Can't start service " + service.getName(), e);
            }
        });
    }

}