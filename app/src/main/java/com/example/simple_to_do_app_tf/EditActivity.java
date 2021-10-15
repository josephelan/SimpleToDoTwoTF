package com.example.simple_to_do_app_tf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class EditActivity extends AppCompatActivity {

  EditText et_edit_task;
  Button btn_edit_task;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit);

    et_edit_task = findViewById(R.id.et_edit_task);
    btn_edit_task = findViewById(R.id.btn_edit_save);

    et_edit_task.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));

    // Save button
    btn_edit_task.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View view) {
        // Create intent that holds results
        Intent intent = new Intent();

        // Pass data that was edited (results of editing)
        intent.putExtra(MainActivity.KEY_ITEM_TEXT, et_edit_task.getText().toString());
        intent.putExtra(MainActivity.KEY_ITEM_POSITION,
                        getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));

        // Set result of the intent
        setResult(RESULT_OK, intent);

        // Finish activity, close screen, return to MainActivity
        finish();
      }
    });
  }
}