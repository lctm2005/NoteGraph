package com.licong.notemap.web.exception;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ElementKind;
import javax.validation.Path;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@NoArgsConstructor
@ControllerAdvice
public class ValidationErrorHandler extends RestErrorHandlerTemplate {


    @Autowired
    private RestErrorMessageWriter restErrorMessageWriter;

    @ExceptionHandler(value = {IllegalArgumentException.class, ConstraintViolationException.class, MethodArgumentNotValidException.class, BindException.class})
    public void validExceptionHandler(HttpServletRequest request, HttpServletResponse response, Throwable throwable) throws Exception {
        restErrorMessageWriter.write(process(throwable, request), response, throwable);
    }


    @Override
    protected HttpStatus getHttpStatus(Throwable throwable, HttpServletRequest request) {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    protected HttpHeaders getHttpHeaders(Throwable throwable, HttpServletRequest request) {
        return null;
    }

    @Override
    protected String getCode(Throwable throwable, HttpServletRequest request) {
        return HttpStatus.BAD_REQUEST.getReasonPhrase().toUpperCase().replace(" ", "_");
    }

    @Override
    protected String getMessage(Throwable throwable) {
        if (ConstraintViolationException.class.isAssignableFrom(throwable.getClass())
                || MethodArgumentNotValidException.class.isAssignableFrom(throwable.getClass())
                || BindException.class.isAssignableFrom(throwable.getClass())) {
            return handleValidationException(throwable);
        } else {
            return throwable.getLocalizedMessage();
        }
    }

    /**
     * 处理参数校验错误
     *
     * @param throwable
     * @return
     * @throws java.io.IOException
     */
    protected String handleValidationException(Throwable throwable) {
        String errMsg = "";
        // Spring Validation
        if (ConstraintViolationException.class.isAssignableFrom(throwable.getClass())) {
            ConstraintViolationException exception = (ConstraintViolationException) throwable;
            Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
            if (violations != null) {
                for (Iterator<ConstraintViolation<?>> iterator = violations.iterator(); iterator.hasNext(); ) {
                    ConstraintViolation<?> next = iterator.next();
                    errMsg += getParamterName(next) + " " + next.getMessage() + "; ";
                }
            }
        }
        // Spring Mvc Bind Validation
        else if (MethodArgumentNotValidException.class.isAssignableFrom(throwable.getClass())) {
            MethodArgumentNotValidException exception = (MethodArgumentNotValidException) throwable;
            errMsg += transformBindingResult(exception.getBindingResult()) + "; ";
        }
        // Spring Mvc Bind Validation
        else if (BindException.class.isAssignableFrom(throwable.getClass())) {
            BindException exception = (BindException) throwable;
            errMsg += transformBindingResult(exception) + "; ";
        }
        return errMsg;
    }

    /**
     * 转换BindingResult为验证的Result
     *
     * @param bindingResult
     * @return
     */
    private String transformBindingResult(BindingResult bindingResult) {
        String errMsg = "";
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (int i = 0; i < fieldErrors.size(); i++) {
            FieldError fieldError = fieldErrors.get(i);
            if (i != 0) {
                errMsg += ",";
            }
            errMsg += fieldError.getField() + " " + fieldError.getDefaultMessage();
        }
        return errMsg;
    }

    /**
     * 通过错误取得参数名称
     *
     * @param violation
     * @return 参数名称
     */
    private String getParamterName(ConstraintViolation<?> violation) {
        try {
            Path.MethodNode methodNode = null;
            Path.ParameterNode parameterNode = null;
            Path.PropertyNode propertyNode = null;
            Iterator<Path.Node> iterator = violation.getPropertyPath().iterator();
            while (iterator.hasNext()) {
                Path.Node node = iterator.next();
                if (ElementKind.METHOD.equals(node.getKind())) {
                    methodNode = (Path.MethodNode) node;
                } else if (ElementKind.PARAMETER.equals(node.getKind())) {
                    parameterNode = (Path.ParameterNode) node;
                } else if (ElementKind.PROPERTY.equals(node.getKind())) {
                    propertyNode = (Path.PropertyNode) node;
                }
            }
            if (propertyNode != null) {
                return propertyNode.getName();
            }
            if (methodNode != null && parameterNode != null) {
                Method method = violation.getRootBeanClass().getMethod(methodNode.getName(), methodNode.getParameterTypes().toArray(new Class[0]));
                LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
                String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
                return parameterNames[parameterNode.getParameterIndex()];
            }
        } catch (NoSuchMethodException e) {
            log.warn("取得参数出错", e);
        }
        return null;
    }
}
