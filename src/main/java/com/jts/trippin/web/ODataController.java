package com.jts.trippin.web;

import org.apache.olingo.commons.api.edm.Edm;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataHttpHandler;
import org.apache.olingo.server.api.ServiceMetadata;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jts.trippin.data.Storage;
import com.jts.trippin.service.CustomDefaultProcessor;
import com.jts.trippin.service.CustomEntityProcessor;
import com.jts.trippin.service.DemoActionProcessor;
import com.jts.trippin.service.DemoBatchProcessor;
import com.jts.trippin.service.DemoEdmProvider;
import com.jts.trippin.service.DemoEntityCollectionProcessor;
import com.jts.trippin.service.DemoPrimitiveProcessor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/odata.svc/**")
@Slf4j
public class ODataController {

    private Storage storage;

    private Storage getStorage(OData odata, Edm edm) {
        if (this.storage == null) {
            this.storage = new Storage(odata, edm);
        }
        return this.storage;
    }

    @RequestMapping(
        method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PATCH, RequestMethod.PUT, RequestMethod.DELETE}
    )
    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {

        OData odata = OData.newInstance();
        ServiceMetadata edm = odata.createServiceMetadata(new DemoEdmProvider(), new ArrayList<>());
        try {
            HttpSession session = req.getSession(true);
            log.info("Session id: " + session.getId());
            Storage storage = getStorage(odata, edm.getEdm());
            //Storage storage = (Storage) session.getAttribute(Storage.class.getName());
            if (storage == null) {
                storage = new Storage(odata, edm.getEdm());
                session.setAttribute(Storage.class.getName(), storage);
            }

            log.info("Received request: " + req.getMethod() + ": " + req.getRequestURI() + (req.getQueryString() == null ? "" : "?" + req.getQueryString()));

            // create odata handler and configure it with EdmProvider and Processors
            ODataHttpHandler handler = odata.createHandler(edm);
            handler.register(new DemoEntityCollectionProcessor(storage));
            handler.register(new CustomEntityProcessor(storage));
            handler.register(new DemoPrimitiveProcessor(storage));
            handler.register(new DemoActionProcessor(storage));
            handler.register(new DemoBatchProcessor(storage));
            handler.register(new CustomDefaultProcessor());

            // let the handler do the work
            handler.process(req, resp);
        } catch (RuntimeException e) {
            log.error("Server Error occurred in ExampleServlet", e);
            throw new ServletException(e);
        }

    }
}
