package ba.lukic.petar.eknjiznica.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.StringRes;

import java.util.List;

import javax.inject.Inject;

import ba.lukic.petar.eknjiznica.R;


public class DialogFactory {


    public interface IDialogSelectCallback {
        void onClick(int position);
    }

    private Context context;
    public DialogFactory(Context context) {
        this.context = context;
    }


    public AlertDialog createCancelOkDialog(@StringRes int title, @StringRes int message, DialogInterface.OnClickListener onOk, DialogInterface.OnClickListener onCancel) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.OK,onOk)
                .setNegativeButton(R.string.Cancel, onCancel);
        return alertDialog.create();
    }

    public AlertDialog createCancelNeutralOkDialog(int positive, int neutral, int negative, int title, int message, DialogInterface.OnClickListener onOk
            , DialogInterface.OnClickListener onNeutral
            , DialogInterface.OnClickListener onCancel) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positive,onOk)
                .setNeutralButton( neutral,onNeutral)
                .setNegativeButton(negative, onCancel);
        return alertDialog.create();
    }


    public Dialog createSimpleListDialog(List<String> options, IDialogSelectCallback callback){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        CharSequence charOptions[]= options.toArray(new CharSequence[options.size()]);
        builder.setItems(charOptions, (dialog, which) -> callback.onClick(which));

        return builder.create();
    }





    public ProgressDialog createProgressDialog(String message) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        return progressDialog;
    }

    public ProgressDialog createProgressDialog(
            @StringRes int messageResource) {
        return createProgressDialog(context.getString(messageResource));
    }
}