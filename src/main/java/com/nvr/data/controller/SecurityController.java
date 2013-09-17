package com.nvr.data.controller;

import com.nvr.data.controller.annotation.AppController;
import com.nvr.data.domain.Security;
import com.nvr.data.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vvarma
 * Date: 9/15/13
 * Time: 4:19 AM
 * To change this template use File | Settings | File Templates.
 */
@AppController
@RequestMapping(value = "/security")
public class SecurityController {
    @Autowired
    SecurityService securityService;
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Security>> getAllSecurity() throws IOException, ParseException {
        List<Security> securities= securityService.getAllSecurities();
        return new ResponseEntity<List<Security>>(securities, HttpStatus.OK);
    }

}
