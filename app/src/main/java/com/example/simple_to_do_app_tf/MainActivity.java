package com.example.simple_to_do_app_tf;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @brief Main activity for application
 */
public class MainActivity extends AppCompatActivity {

  public static final String KEY_ITEM_TEXT = "item_text";
  public static final String KEY_ITEM_POSITION = "item_position";
  public static final int EDIT_TEXT_CODE = 10;

  List<String> task_list;

  Button btn_add;
  EditText et_item;
  RecyclerView rv_item_list;
  ItemsAdapter itemsAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // Define view objects
    btn_add = findViewById(R.id.btn_add_task);
    et_item = findViewById(R.id.et_task);
    rv_item_list = findViewById(R.id.rv_task_list);

    // Load items from persistence
    loadTasks();
    ItemsAdapter.OnLongClickListener long_click_listener = new ItemsAdapter.OnLongClickListener() {

      /**
       * Delete item from model and notify adapter
       * @param position position of the clicked ViewHolder in recyclerview
       */
      @Override
      public void onItemLongClicked(int position) {
        task_list.remove(position); // Delete item from model
        itemsAdapter.notifyItemRemoved(position); // notify adapter of removed position
        Toast.makeText(getApplicationContext(),
                       "Item was removed.",
                       Toast.LENGTH_SHORT).show();
        saveTasks();
      }
    };
    ItemsAdapter.OnClickListener click_listener = new ItemsAdapter.OnClickListener() {
      @Override
      public void onItemClicked(int position) {
        // Log.d("MainActivity", "Single Click: " + position);

        // Create the new activity
        Intent i = new Intent(MainActivity.this, EditActivity.class);

        // Pass data being edited
        i.putExtra(KEY_ITEM_TEXT, task_list.get(position));
        i.putExtra(KEY_ITEM_POSITION, position);

        // Display the activity
        startActivityForResult(i, EDIT_TEXT_CODE);
      }
    };

    itemsAdapter = new ItemsAdapter(task_list, long_click_listener, click_listener);
    rv_item_list.setAdapter(itemsAdapter);
    rv_item_list.setLayoutManager(new LinearLayoutManager(this));

    btn_add.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String todoItem = et_item.getText().toString(); // Get text from user

        // Add item to model
        task_list.add(todoItem);

        // Notify adapter that item is added
        itemsAdapter.notifyItemInserted(task_list.size() - 1);

        // Clear ed_item field
        et_item.setText("");

        // Toast Give user response that item has been added
        Toast.makeText(getApplicationContext(),
                       "Item was added!",
                       Toast.LENGTH_SHORT).show();
        saveTasks();
      }
    });
  }

  /**
   * @brief Handle result of edit activity
   *
   * @param data data that was changed (Intent object)
   */
  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE) {
      // Retrieve the updated text value
      String itemText = data.getStringExtra(KEY_ITEM_TEXT);

      // Extract original position of the edited item
      int position = data.getExtras().getInt(KEY_ITEM_POSITION);

      // Update model at the position with new item text
      task_list.set(position, itemText);

      // Notify the adapter
      itemsAdapter.notifyItemChanged(position);

      // Persist the changes
      saveTasks();
      Toast.makeText(getApplicationContext(), "Task updated!", Toast.LENGTH_SHORT).show();

    } else {
      Log.w("MainActivity", "Unknown call to onActivityResult");
    }
  }

  /*
   *  Persistence methods
   */

  /**
   * @return persistence data file
   */
  private File getDatafile() {
    return new File(getFilesDir(), "otherdata.txt");
  }

  /**
   * load items from persistence into application run
   */
  private void loadTasks() {
    try {
      task_list = new ArrayList<>(FileUtils.readLines(getDatafile(), Charset.defaultCharset()));
    } catch (IOException e) {
      Log.e("MainActivity", "Error reading task_list from persistence", e);
      task_list = new ArrayList<>();
    }
  }

  /**
   * @brief save item into persistence data
   */
  private void saveTasks() {
    try {
      FileUtils.writeLines(getDatafile(), task_list);
    } catch (IOException e) {
      Log.e("MainActivity", "Error writing task list to persistence", e);
    }
  }
}