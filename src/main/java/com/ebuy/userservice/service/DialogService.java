package com.ebuy.userservice.service;

import com.ebuy.userservice.entity.Dialog;
import org.springframework.stereotype.Service;

@Service
public class DialogService extends Dao<Dialog> {
    protected DialogService() {
        super(Dialog.class);
    }

    @Override
    public void save(Dialog dialog) {
        this.throwExceptionIfDialogWithYourself(dialog);
        super.save(dialog);
    }

    private void throwExceptionIfDialogWithYourself(Dialog dialog) {
        if (dialog.getFirstUser().equals(dialog.getSecondUser())) {
            throw new IllegalStateException("It is impossible to conduct a dialog with yourself");
        }
    }
}
