package com.nvr.data.controller;

import com.nvr.data.controller.annotation.AppController;
import com.nvr.data.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public ResponseEntity<String> getAllIndex(){
        System.out.println(indexService.getAllIndice().size());
        return new ResponseEntity<String>("works", HttpStatus.OK);
    }

    @RequestMapping(value = "/{indexName}")
    @ResponseBody
    public ResponseEntity<String> getAllSecurityGivenIndex(@PathVariable(value = "indexName") String indexName){
        return  new ResponseEntity<String>("works",HttpStatus.OK);
    }

}
