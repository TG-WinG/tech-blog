package kr.tgwing.tech.common.exception;

public abstract class CommonException extends RuntimeException {

    protected CommonException () {
    }

    protected CommonException(Exception e) {
        super(e);
    }

    protected CommonException(String message) {
        super(message);
    }
}
