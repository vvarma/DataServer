package com.nvr.data.service.annotation;


import com.nvr.data.service.IndexServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

/**
 * ContextRefresh listener that invokes methods, annotated with {@link PostInitialize} in the given {@link PostInitialize#order()}. Those methods can't have parameteres.
 *
 * @author jbaruch, zvizvi
 * @since Aug 7, 2008
 */
@Component
public class PostInitializerRunner implements ApplicationListener {
    private static final Logger LOG = LoggerFactory.getLogger(PostInitializerRunner.class);

    /**
     * If event instanceof {@link ContextRefreshedEvent} calls methods, annotated with {@link PostInitialize} in the given {@link PostInitialize#order()}. Those methods can't have parameteres.
     *
     * @param event the event type
     */
    @Override

    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            LOG.info("Scanning for Post Initializers...");
            long startTime = 0;
            if (LOG.isDebugEnabled()) startTime = System.nanoTime();
            ContextRefreshedEvent contextRefreshedEvent = (ContextRefreshedEvent) event;
            ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
            Map beans = applicationContext.getBeansOfType(Object.class, false, true);
            List<PostInitializingMethod> postInitializingMethods = new LinkedList<PostInitializingMethod>();
            for (Object beanNameObject : beans.keySet()) {
                String beanName = (String) beanNameObject;
                Object bean = beans.get(beanNameObject);
                Class<?> beanClass = bean.getClass();
                Method[] methods = beanClass.getMethods();
                for (Method method : methods) {

                    if (getAnnotation(method, PostInitialize.class) != null) {
                        if (method.getParameterTypes().length == 0) {
                            int order = getAnnotation(method, PostInitialize.class).order();
                            postInitializingMethods.add(new PostInitializingMethod(method, bean, order, beanName));
                        } else {
                            LOG.warn("Post Initializer method can't have any arguments. " + method.toGenericString() + " in bean " + beanName + " won't be invoked");
                        }
                    }
                }
            }
            Collections.sort(postInitializingMethods);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Application Context scan completed, took " + NANOSECONDS.toMillis(System.nanoTime() - startTime) + " ms, " + postInitializingMethods.size() + " post initializers found. Invoking now.");
            }
            for (PostInitializingMethod postInitializingMethod : postInitializingMethods) {
                transactionalStuffHere(postInitializingMethod);
            }
        }
    }
    @Transactional
    private void transactionalStuffHere(PostInitializingMethod postInitializingMethod){
        Method method = postInitializingMethod.getMethod();
        try {

            method.invoke(postInitializingMethod.getBeanInstance());
        } catch (Throwable e) {
            throw new BeanCreationException("Post Initialization of bean " + postInitializingMethod.getBeanName() + " failed.", e);
        }

    }
    private <T extends Annotation> T getAnnotation(Method method, Class<T> annotationClass) {
        do {
            if (method.isAnnotationPresent(annotationClass)) {
                return method.getAnnotation(annotationClass);
            }
        }
        while ((method = getSuperMethod(method)) != null);
        return null;
    }

    private Method getSuperMethod(Method method) {
        Class declaring = method.getDeclaringClass();
        if (declaring.getSuperclass() != null) {
            Class superClass = declaring.getSuperclass();
            try {
                Method superMethod = superClass.getMethod(method.getName(), method.getParameterTypes());
                if (superMethod != null) {
                    return superMethod;
                }
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }


    private class PostInitializingMethod implements Comparable<PostInitializingMethod> {
        private Method method;
        private Object beanInstance;
        private int order;
        private String beanName;

        private PostInitializingMethod(Method method, Object beanInstance, int order, String beanName) {
            this.method = method;
            this.beanInstance = beanInstance;
            this.order = order;
            this.beanName = beanName;
        }

        public Method getMethod() {
            return method;
        }

        public Object getBeanInstance() {
            return beanInstance;
        }

        public String getBeanName() {
            return beanName;
        }

        @Override
        public int compareTo(PostInitializingMethod anotherPostInitializingMethod) {
            int thisVal = this.order;
            int anotherVal = anotherPostInitializingMethod.order;
            return (thisVal < anotherVal ? -1 : (thisVal == anotherVal ? 0 : 1));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PostInitializingMethod that = (PostInitializingMethod) o;

            return order == that.order && !(beanName != null ? !beanName.equals(that.beanName) : that.beanName != null) && !(method != null ? !method.equals(that.method) : that.method != null);
        }

        @Override
        public int hashCode() {
            int result;
            result = (method != null ? method.hashCode() : 0);
            result = 31 * result + (beanInstance != null ? beanInstance.hashCode() : 0);
            result = 31 * result + order;
            result = 31 * result + (beanName != null ? beanName.hashCode() : 0);
            return result;
        }
    }
}
