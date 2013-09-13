package com.nvr.data.service.annotation;

/**
 * Created with IntelliJ IDEA.
 * User: vvarma
 * Date: 9/13/13
 * Time: 11:13 PM
 * To change this template use File | Settings | File Templates.
 */
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Service
@Transactional
public @interface AppService {
    String value() default "";
}
