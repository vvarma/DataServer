package com.nvr.data.controller;

import com.nvr.data.controller.annotation.AppController;
import com.nvr.data.domain.Indice;
import com.nvr.data.domain.Security;
import com.nvr.data.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;
/**
 * Created with IntelliJ IDEA.
 * User: vvarma
 * Date: 9/15/13
 * Time: 1:57 AM
 * To change this template use File | Settings | File Templates.
 */
@AppController
@RequestMapping(value="/index")
public class IndexController {
    @Autowired
    IndexService indexService;
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Indice>> getAllIndex(){
        List<Indice> indices=indexService.getAllIndice();
        return new ResponseEntity<List<Indice>>(indices, HttpStatus.OK);
    }

    @RequestMapping(value = "/{indexName}")
    @ResponseBody
    public ResponseEntity<List<Security>> getAllSecurityGivenIndex(@PathVariable(value = "indexName") String indexName){
        List<Security> securities=indexService.getAllSecurity(indexName);
        return  new ResponseEntity<List<Security>>(securities,HttpStatus.OK);
    }

}
