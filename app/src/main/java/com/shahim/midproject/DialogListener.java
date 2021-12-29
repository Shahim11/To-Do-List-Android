package com.shahim.midproject;

import android.content.DialogInterface;

interface DialogListener {
    default void handleDialogClose(DialogInterface dialog) {

    }
}
