package snapmeal.snapmeal.global.handler;

import snapmeal.snapmeal.global.GeneralException;
import snapmeal.snapmeal.global.code.BaseErrorCode;

public class UserHandler extends GeneralException {
    public UserHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
