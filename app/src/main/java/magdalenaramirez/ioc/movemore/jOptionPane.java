package magdalenaramirez.ioc.movemore;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class jOptionPane extends DialogFragment {

    private String message;

    public jOptionPane() {
    }

    public static jOptionPane newInstance(String message) {
        jOptionPane pane = new jOptionPane();
        pane.setMessage(message);
        return pane;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Cierra el diálogo
                    }
                });
        return builder.create();
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void show(FragmentManager fragmentManager, String error_dialog) {
    }
}
