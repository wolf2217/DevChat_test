package com.commandcenter.devchat.Helper;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Command Center on 11/12/2017.
 */

public class EmailHelper {

    Context context;
    private String email;
    private String subject;
    private String message;

    public EmailHelper(Context context, String email, String subject, String message) {
        this.context = context;
        this.email = email;
        this.subject = subject;
        this.message = message;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void sendEmail(String email, String subject, String message) {

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {email});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);

        context.startActivity(Intent.createChooser(emailIntent, "Select Email App :"));

    }
}
