package io.github.jzdayz.rs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class R<T> {

    private int code = 200;

    private String message = "success";

    private T data;

    public static <T> R<T> success() {
        return new R<>();
    }

    public static <T> R<T> success(T data) {
        R<T> success = R.<T>success();
        success.setData(data);
        return success;
    }

    public static <T> R<T> error(String message) {
        R<T> r = new R<>();
        r.setCode(300);
        r.setMessage(message);
        return r;
    }

}
