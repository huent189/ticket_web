package uet.japit.k62.exception.exception_handle;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uet.japit.k62.constant.MessageConstant;
import uet.japit.k62.constant.StatusCode;
import uet.japit.k62.exception.exception_define.*;
import uet.japit.k62.models.response.http_response.MessageResponse;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class UserApiExceptionHandle{
    @ExceptionHandler(WrongEmailOrPasswordException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public MessageResponse WrongEmailOrPasswordExceptionHandle() {
        return new MessageResponse(StatusCode.NOT_FOUND, MessageConstant.WRONG_EMAIL_OR_PASSWORD);
    }

    @ExceptionHandler(AccountWasLockedException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public MessageResponse AccountWasLockedExceptionHandle() {
        return new MessageResponse(StatusCode.BAD_REQUEST, MessageConstant.ACCOUNT_WAS_LOCKED);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public MessageResponse UserNotFoundExceptionHandle() {
        return new MessageResponse(StatusCode.BAD_REQUEST, MessageConstant.USER_NOT_FOUND);
    }

    @ExceptionHandler(UserExistedException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public MessageResponse UserExistedExceptionHandle() {
        return new MessageResponse(StatusCode.BAD_REQUEST, MessageConstant.USER_EXISTED);
    }

    @ExceptionHandler(NotUpdateSelfPermissionException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public MessageResponse NotUpdateSelfPermissionExceptionHandle() {
        return new MessageResponse(StatusCode.BAD_REQUEST, MessageConstant.CANT_SELF_UPDATE_PERMISSION);
    }
}