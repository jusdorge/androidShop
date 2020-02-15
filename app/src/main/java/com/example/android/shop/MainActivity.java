package com.example.android.shop;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.android.shop.Adapters.MysqlUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText editText = (EditText)findViewById(R.id.result_editText);
        Button button = (Button) findViewById(R.id.search_button);
        editText.setText("");
        editText.setTextColor(Color.BLACK);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MysqlUtils conn = MysqlUtils.connect();
                if (conn.doesErrorExists()){
                    editText.setTextColor(Color.RED);
                    String message="";
                    for (int i=0;i<conn.getErrorMessage().size();i++) {
                        message = (i+1) + "- " + conn.getErrorNumber().get(i) + "\n" +
                                conn.getErrorMessage().get(i) + "\n"
                                + conn.getErrorCause().get(i);
                    }
                    editText.setText(message);
                }
                if (!conn.doesErrorExists()) {
                    conn.executeQuery("select * from produit");
                    conn.print();
                }
            }
        });

    }
}
