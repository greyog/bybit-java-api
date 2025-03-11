package common;

import com.bybit.api.client.domain.GenericResponse;
import com.bybit.api.client.exception.BybitApiException;


public class ResponseValidator {

    public static void checkResult(GenericResponse<?> response) {
        if (response.getRetCode() != 0) {
            throw new BybitApiException("Code: " + response.getRetCode() + " , message: " + response.getRetMsg());
        }
        if (response.getRetExtInfo() != null
                && response.getRetExtInfo().getList() != null
                && !response.getRetExtInfo().getList().isEmpty()
        ) {
            response.getRetExtInfo().getList().stream()
                    .filter(retExtInfoEntry -> retExtInfoEntry.getCode() != 0)
                    .forEach(retExtInfoEntry ->
                            System.err.println("Code: " + retExtInfoEntry.getCode() + " , message: " + retExtInfoEntry.getMsg())
                    );
        }
    }

}
