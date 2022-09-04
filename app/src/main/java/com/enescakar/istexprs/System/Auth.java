package com.enescakar.istexprs.System;

import android.content.Context;
import android.widget.Toast;

import com.enescakar.istexprs.Model.User;

public class Auth {
    User mUser;
    public Auth(User user) {
        mUser = user;
    }


    public boolean register(Context context) {
        if (mUser.getPass().matches(mUser.getPassAgain())){
            //register
            return true;
        }
        Toast.makeText(context, "Sifreler eslesmedi!", Toast.LENGTH_SHORT).show();
        return false;
    }

    public boolean login() {
        return false;
    }
}
