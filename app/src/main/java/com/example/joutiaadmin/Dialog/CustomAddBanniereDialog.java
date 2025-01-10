package com.example.joutiaadmin.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.example.joutiaadmin.R;

public class CustomAddBanniereDialog extends Dialog {

    public CustomAddBanniereDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.customaddbanniere_dialog); // Remplacez par votre layout personnalisé
    }

}
