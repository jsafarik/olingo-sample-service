/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.jts.trippin.web;

import org.apache.olingo.commons.api.edm.Edm;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataHttpHandler;
import org.apache.olingo.server.api.ServiceMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jts.trippin.data.Storage;
import com.jts.trippin.processor.ComplexProc;
import com.jts.trippin.processor.DefaultProc;
import com.jts.trippin.processor.EntityProc;
import com.jts.trippin.processor.ActionProc;
import com.jts.trippin.processor.BatchProc;
import com.jts.trippin.processor.DemoEdmProvider;
import com.jts.trippin.processor.EntityCollectionProc;
import com.jts.trippin.processor.PrimitiveProc;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TripPinServlet extends HttpServlet {

    private static final int serialVersionUID = 1;

    @Autowired
    private List<Class<?>> odataEntities;

    private Storage storage;

    private Storage getStorage(OData odata, Edm edm, HttpSession session) {
        if (this.storage == null) {
            this.storage = new Storage(odata, edm, odataEntities);
            session.setAttribute(Storage.class.getName(), storage);
        }
        return this.storage;
    }

    private void registerProcessors(ODataHttpHandler handler){
        handler.register(new EntityCollectionProc(storage));
        handler.register(new EntityProc(storage));
        handler.register(new PrimitiveProc(storage));
        handler.register(new ActionProc(storage));
        handler.register(new BatchProc(storage));
        handler.register(new DefaultProc());
        handler.register(new ComplexProc(storage));
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        OData odata = OData.newInstance();
        ServiceMetadata edm = odata.createServiceMetadata(new DemoEdmProvider(), new ArrayList<>());
        try {
            HttpSession session = req.getSession(true);
            log.info("---Start of request---");
            log.info("Session id: " + session.getId());
            Storage storage = getStorage(odata, edm.getEdm(), session);

            log.info("Received request: {}: {}", req.getMethod(), req.getRequestURI() + (req.getQueryString() == null ? "" : "?" + req.getQueryString()));

            // create odata handler and configure it with EdmProvider and Processors
            ODataHttpHandler handler = odata.createHandler(edm);
            registerProcessors(handler);

            // let the handler do the work
            handler.process(req, resp);
            log.info("----End of request----\n");
        } catch (RuntimeException e) {
            log.error("Server Error occurred in ExampleServlet", e);
            throw new ServletException(e);
        }

    }

}
