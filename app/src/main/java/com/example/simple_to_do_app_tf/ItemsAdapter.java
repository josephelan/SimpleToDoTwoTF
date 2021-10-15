package com.example.simple_to_do_app_tf;

import static android.view.LayoutInflater.from;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * @brief Objects display data from model into row of recyclerview
 */
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

  public interface OnLongClickListener {
    void onItemLongClicked(int position);
  }

  public interface OnClickListener {
    void onItemClicked(int position);
  }

  // Data members
  List<String> items_; // Data where tasks kept as string
  OnLongClickListener long_click_listener_;
  OnClickListener click_listener_;

  /**
   * @brief ItemsAdapter() initializes data member to argument
   * @param items
   */
  public ItemsAdapter(List<String> items, OnLongClickListener longClickListener,
                      OnClickListener onClickListener) {
    items_ = items;
    long_click_listener_ = longClickListener;
    click_listener_ = onClickListener;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

    // Use layout inflater to inflate a view
    // wrap it inside a ViewHolder and return it
    View todoView = from(parent.getContext()).inflate(
        android.R.layout.simple_list_item_1, parent,false);
    return new ViewHolder(todoView);
  }

  /**
   * @brief Takes data at a position putting into ViewHolder
   *        Responsible for binding data to a particular ViewHolder
   * @param holder  place in recyclerview we are binding item to
   * @param position position of item in list
   */
  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    // Get item at position
    String item = items_.get(position);

    // Bind item to specified ViewHolder
    holder.bind(item);
  }

  /**
   * @return number of items in item list
   */
  @Override
  public int getItemCount() {
    return items_.size();
  }

  /**
   *  @brief Container to provide access to views representing each row of rv_item_list
   */
  class ViewHolder extends RecyclerView.ViewHolder {

    TextView tvItem_;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      tvItem_ = itemView.findViewById(android.R.id.text1);
    }

    /**
     * @brief Update the view inside of the ViewHOlder with this data
     * @param item item to update
     */
    public void bind(String item) {
      tvItem_.setText(item);
      tvItem_.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

          // Notify main of the position that was licked
          click_listener_.onItemClicked(getAdapterPosition());
        }
      });

      tvItem_.setOnLongClickListener(new View.OnLongClickListener() {

        /**
         * @brief remove item from recyclerview
         * @param view View that was clicked
         */
        @Override
        public boolean onLongClick(View view) {

          // Notify main of the position that was long pressed
          long_click_listener_.onItemLongClicked(getAdapterPosition());
          return true;
        }
      });
    }
  }
}