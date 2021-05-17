package com.hly.july.auth.exception;

import com.hly.july.common.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.DefaultThrowableAnalyzer;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InsufficientScopeException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.web.util.ThrowableAnalyzer;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.io.IOException;

/**
 * @ClassName MyOauthWebResponseExceptionTranslator
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/5/13 16:36
 * @Version 1.0.0
 **/

@Slf4j
public class MyOauthWebResponseExceptionTranslator implements WebResponseExceptionTranslator {
    private ThrowableAnalyzer throwableAnalyzer = new DefaultThrowableAnalyzer();

    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
        Throwable[] causeChain = throwableAnalyzer.determineCauseChain(e);
        Exception ase=null;
        log.error("MyOauthWebResponseExceptionTranslator Exception:"+e.getMessage());
        // 异常栈获取 OAuth2Exception 异常
        ase = (OAuth2Exception) throwableAnalyzer.getFirstThrowableOfType(
                OAuth2Exception.class, causeChain);
        // 异常栈中有OAuth2Exception
        if (ase != null) {
            log.error("OAuth2Exception:"+ase.getMessage());
            return handleOAuth2Exception((OAuth2Exception) ase);
        }

        // UsernameNotFoundException
        ase = (UsernameNotFoundException) throwableAnalyzer.getFirstThrowableOfType(
                UsernameNotFoundException.class, causeChain);
        if (ase != null) {
            log.error("UsernameNotFoundException:"+ase.getMessage());
            return handleOAuth2Exception(new JulyUsernameInvalidException(e.getMessage(), e));
        }

        // DisabledException
        ase = (DisabledException) throwableAnalyzer.getFirstThrowableOfType(
                DisabledException.class, causeChain);
        if (ase != null) {
            log.error("DisabledException:"+ase.getMessage());
            return handleOAuth2Exception(new JulyForbiddenException(e.getMessage(), e));
        }

        // LockedException
        ase = (LockedException) throwableAnalyzer.getFirstThrowableOfType(
                LockedException.class, causeChain);
        if (ase != null) {
            log.error("LockedException:"+ase.getMessage());
            return handleOAuth2Exception(new JulyLockedException(e.getMessage(), e));
        }

        // AccountExpiredException
        ase = (AccountExpiredException) throwableAnalyzer.getFirstThrowableOfType(
                AccountExpiredException.class, causeChain);
        if (ase != null) {
            log.error("AccountExpiredException:"+ase.getMessage());
            return handleOAuth2Exception(new JulyAccountExpiredException(e.getMessage(), e));
        }

        ase = (AuthenticationException) throwableAnalyzer.getFirstThrowableOfType(AuthenticationException.class,
                causeChain);
        if (ase != null) {
            log.error("UnauthorizedException:"+ase.getMessage());
            return handleOAuth2Exception(new JulyUnauthorizedException(e.getMessage(), e));
        }

        ase = (AccessDeniedException) throwableAnalyzer
                .getFirstThrowableOfType(AccessDeniedException.class, causeChain);
        if (ase instanceof AccessDeniedException) {
            log.error("AccessDeniedException:"+ase.getMessage());
            return handleOAuth2Exception(new JulyUnauthorizedException(ase.getMessage(), ase));
        }

        ase = (HttpRequestMethodNotSupportedException) throwableAnalyzer
                .getFirstThrowableOfType(HttpRequestMethodNotSupportedException.class, causeChain);
        if (ase instanceof HttpRequestMethodNotSupportedException) {
            log.error("HttpRequestMethodNotSupportedException:"+ase.getMessage());
            return handleOAuth2Exception(new JulyMethodNotAllowedException(ase.getMessage(), ase));
        }

        // 不包含上述异常则服务器内部错误
        log.error("ServerErrorException:"+e.getMessage());
        return handleOAuth2Exception(new JulyServerErrorException(e));
    }

    //使用自定义的异常处理类处理异常
    private ResponseEntity<OAuth2Exception> handleOAuth2Exception(OAuth2Exception e) throws IOException {
        int status = e.getHttpErrorCode();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cache-Control", "no-store");
        headers.set("Pragma", "no-cache");
//        headers.setContentType(MediaType.APPLICATION_JSON);
        if (status == HttpStatus.UNAUTHORIZED.value() || (e instanceof InsufficientScopeException)) {
            headers.set("WWW-Authenticate", String.format("%s %s", OAuth2AccessToken.BEARER_TYPE, e.getSummary()));
        }
        OAuth2Exception exception = new OAuth2Exception(e.getMessage(), e);

        ResponseEntity<OAuth2Exception> response = new ResponseEntity<OAuth2Exception>(exception, headers,
                HttpStatus.OK);
        return response;

    }


    @SuppressWarnings("serial")
    private static class JulyForbiddenException extends OAuth2Exception {

        private ResultCode resultCode;

        public JulyForbiddenException() {
            super(ResultCode.AUTH_ACCOUNT_FORBIDDEN.getMsg());
            this.resultCode = ResultCode.AUTH_ACCOUNT_FORBIDDEN;
        }

        public JulyForbiddenException(Throwable t) {
            super(ResultCode.AUTH_ACCOUNT_FORBIDDEN.getMsg(), t);
            this.resultCode = ResultCode.AUTH_ACCOUNT_FORBIDDEN;
        }

        public JulyForbiddenException(String msg, Throwable t) {
            super(msg, t);
            this.resultCode = ResultCode.AUTH_ACCOUNT_FORBIDDEN;
        }

        public JulyForbiddenException(ResultCode resultCode, String msg, Throwable t) {
            super(msg, t);
            this.resultCode = resultCode;
        }

        public void setResultCode(ResultCode resultCode){
            this.resultCode = resultCode;
        }

        public ResultCode getResultCode(){
            return this.resultCode;
        }
    }

    @SuppressWarnings("serial")
    private static class JulyLockedException extends OAuth2Exception {

        private ResultCode resultCode;

        public JulyLockedException() {
            super(ResultCode.AUTH_ACCOUNT_LOCKED.getMsg());
            this.resultCode = ResultCode.AUTH_ACCOUNT_LOCKED;
        }

        public JulyLockedException(Throwable t) {
            super(ResultCode.AUTH_ACCOUNT_LOCKED.getMsg(), t);
            this.resultCode = ResultCode.AUTH_ACCOUNT_LOCKED;
        }

        public JulyLockedException(String msg, Throwable t) {
            super(msg, t);
            this.resultCode = ResultCode.AUTH_ACCOUNT_LOCKED;
        }

        public JulyLockedException(ResultCode resultCode, String msg, Throwable t) {
            super(msg, t);
            this.resultCode = resultCode;
        }

        public void setResultCode(ResultCode resultCode){
            this.resultCode = resultCode;
        }

        public ResultCode getResultCode(){
            return this.resultCode;
        }
    }

    @SuppressWarnings("serial")
    private static class JulyAccountExpiredException extends OAuth2Exception {

        private ResultCode resultCode;

        public JulyAccountExpiredException() {
            super(ResultCode.AUTH_ACCOUNT_EXPIRED.getMsg());
            this.resultCode = ResultCode.AUTH_ACCOUNT_EXPIRED;
        }

        public JulyAccountExpiredException(Throwable t) {
            super(ResultCode.AUTH_ACCOUNT_EXPIRED.getMsg(), t);
            this.resultCode = ResultCode.AUTH_ACCOUNT_EXPIRED;
        }

        public JulyAccountExpiredException(String msg, Throwable t) {
            super(msg, t);
            this.resultCode = ResultCode.AUTH_ACCOUNT_EXPIRED;
        }

        public JulyAccountExpiredException(ResultCode resultCode, String msg, Throwable t) {
            super(msg, t);
            this.resultCode = resultCode;
        }

        public void setResultCode(ResultCode resultCode){
            this.resultCode = resultCode;
        }

        public ResultCode getResultCode(){
            return this.resultCode;
        }
    }

    @SuppressWarnings("serial")
    private static class JulyUsernameInvalidException extends OAuth2Exception {

        private ResultCode resultCode;

        public JulyUsernameInvalidException() {
            super(ResultCode.AUTH_ACCOUNT_INVALID.getMsg());
            this.resultCode = ResultCode.AUTH_ACCOUNT_INVALID;
        }

        public JulyUsernameInvalidException(Throwable t) {
            super(ResultCode.AUTH_ACCOUNT_INVALID.getMsg(), t);
            this.resultCode = ResultCode.AUTH_ACCOUNT_INVALID;
        }

        public JulyUsernameInvalidException(String msg, Throwable t) {
            super(msg, t);
            this.resultCode = ResultCode.AUTH_ACCOUNT_INVALID;
        }

        public JulyUsernameInvalidException(ResultCode resultCode, String msg, Throwable t) {
            super(msg, t);
            this.resultCode = resultCode;
        }

        public void setResultCode(ResultCode resultCode){
            this.resultCode = resultCode;
        }

        public ResultCode getResultCode(){
            return this.resultCode;
        }
    }

    @SuppressWarnings("serial")
    private static class JulyPasswordErrorException extends OAuth2Exception {

        private ResultCode resultCode;

        public JulyPasswordErrorException() {
            super(ResultCode.AUTH_PASSWORD_ERROR.getMsg());
            this.resultCode = ResultCode.AUTH_PASSWORD_ERROR;
        }

        public JulyPasswordErrorException(Throwable t) {
            super(ResultCode.AUTH_PASSWORD_ERROR.getMsg(), t);
            this.resultCode = ResultCode.AUTH_PASSWORD_ERROR;
        }

        public JulyPasswordErrorException(String msg, Throwable t) {
            super(msg, t);
            this.resultCode = ResultCode.AUTH_PASSWORD_ERROR;
        }

        public JulyPasswordErrorException(ResultCode resultCode, String msg, Throwable t) {
            super(msg, t);
            this.resultCode = resultCode;
        }

        public void setResultCode(ResultCode resultCode){
            this.resultCode = resultCode;
        }

        public ResultCode getResultCode(){
            return this.resultCode;
        }
    }

    @SuppressWarnings("serial")
    private static class JulyAuthFailException extends OAuth2Exception {

        private ResultCode resultCode;

        public JulyAuthFailException() {
            super(ResultCode.AUTH_FAIL.getMsg());
            this.resultCode = ResultCode.AUTH_FAIL;
        }

        public JulyAuthFailException(Throwable t) {
            super(ResultCode.AUTH_FAIL.getMsg(), t);
            this.resultCode = ResultCode.AUTH_FAIL;
        }

        public JulyAuthFailException(String msg, Throwable t) {
            super(msg, t);
            this.resultCode = ResultCode.AUTH_FAIL;
        }

        public JulyAuthFailException(ResultCode resultCode, String msg, Throwable t) {
            super(msg, t);
            this.resultCode = resultCode;
        }

        public void setResultCode(ResultCode resultCode){
            this.resultCode = resultCode;
        }

        public ResultCode getResultCode(){
            return this.resultCode;
        }
    }

    @SuppressWarnings("serial")
    private static class JulyUnauthorizedException extends OAuth2Exception {

        private ResultCode resultCode;

        public JulyUnauthorizedException() {
            super(ResultCode.AUTH_UNAUTHORIZED.getMsg());
            this.resultCode = ResultCode.AUTH_UNAUTHORIZED;
        }

        public JulyUnauthorizedException(Throwable t) {
            super(ResultCode.AUTH_UNAUTHORIZED.getMsg(), t);
            this.resultCode = ResultCode.AUTH_UNAUTHORIZED;
        }

        public JulyUnauthorizedException(String msg, Throwable t) {
            super(msg, t);
            this.resultCode = ResultCode.AUTH_UNAUTHORIZED;
        }

        public JulyUnauthorizedException(ResultCode resultCode, String msg, Throwable t) {
            super(msg, t);
            this.resultCode = resultCode;
        }

        public void setResultCode(ResultCode resultCode){
            this.resultCode = resultCode;
        }

        public ResultCode getResultCode(){
            return this.resultCode;
        }
    }

    @SuppressWarnings("serial")
    private static class JulyServerErrorException extends OAuth2Exception {

        private ResultCode resultCode;

        public JulyServerErrorException() {
            super(ResultCode.API_FAIL_500.getMsg());
            this.resultCode = ResultCode.API_FAIL_500;
        }

        public JulyServerErrorException(Throwable t) {
            super(ResultCode.API_FAIL_500.getMsg(), t);
            this.resultCode = ResultCode.API_FAIL_500;
        }

        public JulyServerErrorException(String msg, Throwable t) {
            super(msg, t);
            this.resultCode = ResultCode.API_FAIL_500;
        }

        public JulyServerErrorException(ResultCode resultCode, String msg, Throwable t) {
            super(msg, t);
            this.resultCode = resultCode;
        }

        public void setResultCode(ResultCode resultCode){
            this.resultCode = resultCode;
        }

        public ResultCode getResultCode(){
            return this.resultCode;
        }
    }

    @SuppressWarnings("serial")
    private static class JulyMethodNotAllowedException extends OAuth2Exception {

        private ResultCode resultCode;

        public JulyMethodNotAllowedException() {
            super(ResultCode.API_NOT_ALLOWED.getMsg());
            this.resultCode = ResultCode.API_NOT_ALLOWED;
        }

        public JulyMethodNotAllowedException(Throwable t) {
            super(ResultCode.API_NOT_ALLOWED.getMsg(), t);
            this.resultCode = ResultCode.API_NOT_ALLOWED;
        }

        public JulyMethodNotAllowedException(String msg, Throwable t) {
            super(msg, t);
            this.resultCode = ResultCode.API_NOT_ALLOWED;
        }

        public JulyMethodNotAllowedException(ResultCode resultCode, String msg, Throwable t) {
            super(msg, t);
            this.resultCode = resultCode;
        }

        public void setResultCode(ResultCode resultCode){
            this.resultCode = resultCode;
        }

        public ResultCode getResultCode(){
            return this.resultCode;
        }
    }


}
