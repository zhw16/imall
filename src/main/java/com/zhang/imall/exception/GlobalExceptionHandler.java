package com.zhang.imall.exception;

import com.zhang.imall.common.ApiRestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明 ：@ControllerAdvice 配合 @ExceptionHandler 实现全局异常处理
 * 描述： 处理统一异常的handler
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    //统一异常的处理，不对外开放具体系统错误信息。
    private final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理系统异常
     * 说明：   @ExceptionHandler(Exception.class)  进行全局的异常拦截
     * @param e 传递进来的异常
     * @return 系统异常，比较笼统,在处理@Valid有异常时候，无法看到详细信息
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object handlerException(Exception e) {
        //传进来的错误信息，log记录一下
        log.error("default exception", e);
        //进行异常的统一，不进行具体原因的展示
        return ApiRestResponse.error(ImallExceptionEnum.SYSTEM_ERROR);
    }

    /**
     * 拦截 @ExceptionHandler(ImallException.class) 业务上的异常
     * @param e 传递进来自定义的的业务上ImallException.error的异常，不敏感的数据异常
     * @return
     */
    @ExceptionHandler(ImallException.class)
    @ResponseBody
    public Object handlerImallException(ImallException e) {
        //传进来的错误信息，log记录一下
        log.error("Imall Exception", e);
        //进行异常的统一，不进行具体原因的展示
        return ApiRestResponse.error(e.getCode(), e.getMessage());
    }

    /**
     * 处理@Valid所引发的参数校验失败所引起的 MethodArgumentNotValidException 异常。
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ApiRestResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotNullException", e);
        return handleBingResult(e.getBindingResult());
    }

    /**
     * 处理MethodArgumentNotValidException异常，提取错误信息里面有用的数据，返回进行展示
     */
    private ApiRestResponse handleBingResult(BindingResult result) {
        //把【MethodArgumentNotValidException异常】处理为，对应的ApiRestResponse统一返回对象；
        //这儿创建一个List集合；后面我们在MethodArgumentNotValidException中获取的错误信息，都存放在这个集合中去；
        ArrayList<String> list = new ArrayList<>();
        if (result.hasErrors()) {
            //如果BindingResult包含错误，就把其中所有的错误信息全部放到allErrors
            List<ObjectError> allErrors = result.getAllErrors();
            //遍历所有的错误信息
            for (int i = 0; i < allErrors.size(); i++) {
                ObjectError objectError = allErrors.get(i);
                //提取具体的错误信息
                String defaultMessage = objectError.getDefaultMessage();
                //将错误信息保存到list集合
                list.add(defaultMessage);
            }
        }
        if (list.size() == 0) {
            //一般情况下到此步骤都是已经发生异常，但是list里面如果没有的数据，就证明是属于参数错误
            return ApiRestResponse.error(ImallExceptionEnum.REQUEST_PARAM_ERROR);
        }
        //根据MethodArgumentNotValidException异常发生的具体错误信息，返回构建的ApiResponse异常;错误码是属于定义好的，但是异常信息取控制台输出的有效信息。
        return ApiRestResponse.error(ImallExceptionEnum.REQUEST_PARAM_ERROR.getCode(), list.toString());

    }
}
