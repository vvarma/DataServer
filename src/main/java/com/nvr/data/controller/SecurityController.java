package com.nvr.data.controller;

import com.nvr.data.controller.annotation.AppController;
import com.nvr.data.domain.Price;
import com.nvr.data.domain.Security;
import com.nvr.data.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    @RequestMapping(value = "/{symbol}",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Security> getSecurity(@PathVariable String symbol){
        return new ResponseEntity<Security>(securityService.getSecurity(symbol),new HttpHeaders(),HttpStatus.OK);
    }
    @RequestMapping(value = "/{symbol}/{series}",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Security> getSecurity(@PathVariable(value = "symbol") String symbol,@PathVariable(value = "series") String series){
        return new ResponseEntity<Security>(securityService.getSecurity(symbol,series),new HttpHeaders(),HttpStatus.OK);
    }
    @RequestMapping(value = "/{symbol}/{series}/{fromDate}/{toDate}")
    @ResponseBody
    public ResponseEntity<List<Price>> getPriceForSecurity(@PathVariable(value = "symbol") String symbol,@PathVariable(value = "series") String series,
                                                     @PathVariable(value = "fromDate") String fromDateSt,@PathVariable(value = "toDate") String toDateSt) throws ParseException {
        SimpleDateFormat fmt=new SimpleDateFormat("dd-MMM-yyyy");
        Date startDate=fmt.parse(fromDateSt);
        Date endDate=fmt.parse(toDateSt);
        return new ResponseEntity<List<Price>>(securityService.getPriceForSecurity(symbol,series,startDate,endDate),new HttpHeaders(), HttpStatus.OK);
    }

}
