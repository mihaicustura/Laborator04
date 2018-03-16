package ro.pub.cs.systems.eim.lab04.contactsmanager;

import android.content.ContentValues;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ContactsManagerActivity extends AppCompatActivity {

    LinearLayout container2;
    TextView name;
    TextView phone;
    TextView email;
    TextView address;
    TextView jobTitle;
    TextView company;
    TextView website;
    TextView im;
    Button details;
    Button save;
    Button cancel;
    Boolean calledFromPhoneDialer = false;

    class Listener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Button button = (Button)v;

            switch (button.getId()) {
                case R.id.show_hide:
                    if (container2.getVisibility() == View.VISIBLE) {
                        container2.setVisibility(View.GONE);
                        button.setText("Show additional fields");
                    }
                    else {
                        container2.setVisibility(View.VISIBLE);
                        button.setText("Hide additional fields");
                    }
                    break;

                case R.id.save:
                    Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
                    intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                    if (name != null) {
                        intent.putExtra(ContactsContract.Intents.Insert.NAME, name.toString());
                    }
                    if (phone != null) {
                        intent.putExtra(ContactsContract.Intents.Insert.PHONE, phone.toString());
                    }
                    if (email != null) {
                        intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email.toString());
                    }
                    if (address != null) {
                        intent.putExtra(ContactsContract.Intents.Insert.POSTAL, address.toString());
                    }
                    if (jobTitle != null) {
                        intent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, jobTitle.toString());
                    }
                    if (company != null) {
                        intent.putExtra(ContactsContract.Intents.Insert.COMPANY, company.toString());
                    }
                    ArrayList<ContentValues> contactData = new ArrayList<ContentValues>();
                    if (website != null) {
                        ContentValues websiteRow = new ContentValues();
                        websiteRow.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE);
                        websiteRow.put(ContactsContract.CommonDataKinds.Website.URL, website.toString());
                        contactData.add(websiteRow);
                    }
                    if (im != null) {
                        ContentValues imRow = new ContentValues();
                        imRow.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE);
                        imRow.put(ContactsContract.CommonDataKinds.Im.DATA, im.toString());
                        contactData.add(imRow);
                    }
                    intent.putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, contactData);
                    if (!calledFromPhoneDialer)
                        startActivity(intent);
                    else
                        startActivityForResult(intent, Constants.CONTACTS_MANAGER_REQUEST_CODE);

                    break;

                case R.id.cancel:
                    finish();
            }
        }
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_manager);

        container2 = findViewById(R.id.hide_layout);
        container2.setVisibility(View.GONE);

        name = findViewById(R.id.name);
        phone = findViewById(R.id.phonenumber);
        email = findViewById(R.id.email);
        address = findViewById(R.id.address);
        jobTitle = findViewById(R.id.jobtitle);
        company = findViewById(R.id.company);
        website = findViewById(R.id.website);
        im = findViewById(R.id.im);
        details = findViewById(R.id.show_hide);
        save = findViewById(R.id.save);
        cancel = findViewById(R.id.cancel);

        Listener listener = new Listener();
        details.setOnClickListener(listener);
        save.setOnClickListener(listener);
        cancel.setOnClickListener(listener);

        Intent intent = getIntent();
        if (intent != null) {
            String phoneNumber = intent.getStringExtra("ro.pub.cs.systems.eim.lab04.contactsmanager.PHONE_NUMBER_KEY");
            calledFromPhoneDialer = true;
            if (phone != null) {
                phone.setText(phoneNumber);
            } else {
                Toast.makeText(this, getResources().getString(R.string.phone_error), Toast.LENGTH_LONG).show();
            }
        }
    }
}
