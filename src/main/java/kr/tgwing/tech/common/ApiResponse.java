package kr.tgwing.tech.common;

public record ApiResponse<T> (String message, T data){

    public static <T> ApiResponse<T> ok() {
        return new ApiResponse<>("ok", null);
    }

    public static <T> ApiResponse<T> ok(T result) {
        return new ApiResponse<>("ok", result);
    }

    public static <T> ApiResponse<T> created(T result) {
        return new ApiResponse<>("created", result);
    }

    public static <T> ApiResponse<T> delete(T result) {
        return new ApiResponse<>("delete", result);
    }
}
