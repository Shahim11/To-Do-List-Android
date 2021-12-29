package com.shahim.midproject.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.shahim.midproject.Database.DatabaseHelper;
import com.shahim.midproject.MainActivity;
import com.shahim.midproject.NewTaskAdd;
import com.shahim.midproject.R;
import com.shahim.midproject.Model.Model;


public class Adapter extends RecyclerView.Adapter<Adapter.ViewHelper> {

    private List<Model> todoList;
    private final DatabaseHelper db;
    private final MainActivity activity;

    public Adapter(DatabaseHelper db, MainActivity activity) {
        this.db = db;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHelper onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task, parent, false);
        return new ViewHelper(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHelper holder, int position) {
        db.openDatabase();

        final Model item = todoList.get(position);
        holder.task.setText(item.getTask());
        holder.task.setChecked(toBoolean(item.getStatus()));
        holder.task.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                db.updateStatus(item.getId(), 1);
            } else {
                db.updateStatus(item.getId(), 0);
            }
        });
    }

    private boolean toBoolean(int n) {
        return n != 0;
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public Context getContext() {
        return activity;
    }


    @SuppressLint("NotifyDataSetChanged")
    public void setTasks(List<Model> todoList) {
        this.todoList = todoList;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        Model item = todoList.get(position);
        db.deleteTask(item.getId());
        todoList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position) {
        Model item = todoList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        NewTaskAdd fragment = new NewTaskAdd();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), NewTaskAdd.TAG);
    }

    public static class ViewHelper extends RecyclerView.ViewHolder {
        CheckBox task;

        ViewHelper(View view) {
            super(view);
            task = view.findViewById(R.id.todoCheckBox);
        }
    }
}
