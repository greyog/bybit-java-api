package common;

import com.bybit.api.client.domain.GenericResponse;
import com.bybit.api.client.exception.BybitApiException;

import javax.swing.text.StyledEditorKit;
import java.util.concurrent.atomic.AtomicReference;


public class ResponseValidator {

    public static boolean checkResult(GenericResponse<?> response) {
        var result = new boolean[1];
        result[0] = true;
        if (response.getRetCode() != 0) {
            throw new BybitApiException("Code: " + response.getRetCode() + " , message: " + response.getRetMsg());
        }
        if (response.getRetExtInfo() != null
                && response.getRetExtInfo().getList() != null
                && !response.getRetExtInfo().getList().isEmpty()
        ) {
            response.getRetExtInfo().getList().stream()
                    .filter(retExtInfoEntry -> retExtInfoEntry.getCode() != 0)
                    .forEach(retExtInfoEntry -> {
                        System.err.println("Code: " + retExtInfoEntry.getCode() + " , message: " + retExtInfoEntry.getMsg());
                        result[0] = false;
                    });
        }
        return result[0];
    }

}
