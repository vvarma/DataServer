package com.nvr.data.controller.annotation;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with IntelliJ IDEA.
 * User: vvarma
 * Date: 9/15/13
 * Time: 1:58 AM
 * To change this template use File | Settings | File Templates.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Controller
@Transactional
public @interface AppController {
    String value() default "";
}
