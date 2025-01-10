package com.example.joutiaadmin.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.example.joutiaadmin.R;

public class CustomCommandeDialog extends Dialog {
    public CustomCommandeDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.customcommande_dialog); // Remplacez par votre layout personnalisé
    }
}
