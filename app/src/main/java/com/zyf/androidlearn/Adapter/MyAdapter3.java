package com.zyf.androidlearn.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zyf.androidlearn.Bean.Note;
import com.zyf.androidlearn.List_item.EditActivity;
import com.zyf.androidlearn.List_item.ShowActivity;
import com.zyf.androidlearn.R;
import com.zyf.androidlearn.SQLite.NoteDbOpenHelper;
import com.zyf.androidlearn.utils.ToastUtil;

import java.util.List;

public class MyAdapter3 extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<Note> mBeanList;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    private int viewType;
    public static int TYPE_LINEAR_LAYOUT = 0;
    public static int TYPE_GRID_LAYOUT = 1;

    private NoteDbOpenHelper mNoteDbOpenHelper;

    public MyAdapter3(Context context, List<Note> mBeanList){
        this.mBeanList = mBeanList;
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mNoteDbOpenHelper = new NoteDbOpenHelper(mContext);
    }



    @Override
    public int getItemViewType(int position) {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == TYPE_LINEAR_LAYOUT){
            View view = mLayoutInflater.inflate(R.layout.list_item_layout, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }else if(viewType == TYPE_GRID_LAYOUT){
            View view = mLayoutInflater.inflate(R.layout.list_item_grid_layout, parent, false);
            MyGridViewHolder myGridViewHolder = new MyGridViewHolder(view);
            return myGridViewHolder;
        }

        return null;
    }




    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder == null) {
            return;
        }
        if(holder instanceof MyViewHolder){
            bindMyViewHolder((MyViewHolder) holder, position);
        } else if (holder instanceof MyGridViewHolder) {
            bindGridViewHolder((MyGridViewHolder) holder, position);
        }
    }

    private void bindMyViewHolder(MyViewHolder holder, int position) {
        Note note = mBeanList.get(position);
        holder.mTvTitle.setText(note.getTitle());
        holder.mTvContent.setText(note.getContent());
        holder.mTvTime.setText(note.getCreateTime());
        holder.rlContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, ShowActivity.class);
                intent.putExtra("note", note);
                mContext.startActivity(intent);
            }
        });

//        holder.rlContainer.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                //长按弹出弹窗删除或者编辑                             给删除和编辑添加样式
//                Dialog dialog = new Dialog(mContext, android.R.style.ThemeOverlay_Material_Dialog_Alert);
//                View dialogView = mLayoutInflater.inflate(R.layout.list_item_dialog_layout, null);
//                TextView tvDelete = dialogView.findViewById(R.id.tv_delete);
//                TextView tvEdit = dialogView.findViewById(R.id.tv_edit);
//                //删除点击事件
//                tvDelete.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        int row = mNoteDbOpenHelper.deleteFromDbById(note.getId());
//                        if (row > 0) {//确定数据库被删除了，再从列表中删除
//                            removeData(position);
//                        }
//                        dialog.dismiss();
//                    }
//                });
//
//                //编辑点击事件
//                tvEdit.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(mContext, ShowActivity.class);
//                        intent.putExtra("note", note);
//                        mContext.startActivity(intent);
//                        dialog.dismiss();
//                    }
//                });
//                dialog.setContentView(dialogView);
//                dialog.setCanceledOnTouchOutside(true);
//                dialog.show();
//                return true;
//            }
//        });
    }
    private void bindGridViewHolder(MyGridViewHolder holder, int position) {
        Note note = mBeanList.get(position);
        holder.mTvTitle.setText(note.getTitle());
        holder.mTvContent.setText(note.getContent());
        holder.mTvTime.setText(note.getCreateTime());
        holder.rlContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, ShowActivity.class);
                intent.putExtra("note", note);
                mContext.startActivity(intent);
            }
        });

//        holder.rlContainer.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                //长按弹出弹窗删除或者编辑                             给删除和编辑添加样式
//                Dialog dialog = new Dialog(mContext, android.R.style.ThemeOverlay_Material_Dialog_Alert);
//                View dialogView = mLayoutInflater.inflate(R.layout.list_item_dialog_layout, null);
//                TextView tvDelete = dialogView.findViewById(R.id.tv_delete);
//                TextView tvEdit = dialogView.findViewById(R.id.tv_edit);
//
//                //删除点击事件
//                tvDelete.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        int row = mNoteDbOpenHelper.deleteFromDbById(note.getId());
//                        if (row > 0) {//确定数据库被删除了，再从列表中删除
//                            removeData(position);
//                        }
//                        dialog.dismiss();
//                    }
//                });
//
//                //编辑点击事件
//                tvEdit.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(mContext, ShowActivity.class);
//                        intent.putExtra("note", note);
//                        mContext.startActivity(intent);
//                        dialog.dismiss();
//                    }
//                });
//                dialog.setContentView(dialogView);
//                dialog.setCanceledOnTouchOutside(true);
//                dialog.show();
//                return true;
//            }
//        });
    }

//        Note note = mBeanList.get(position);
//
//        holder.mTvTitle.setText(note.getTitle());
//        holder.mTvContent.setText(note.getContent());
//        holder.mTvTime.setText(note.getCreatedTime());
//        holder.rlContainer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, EditActivity.class);
//                intent.putExtra("note", note);
//                mContext.startActivity(intent);
//            }
//        });
//
//
//
//        holder.rlContainer.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                //长按弹出弹窗删除或者编辑                             给删除和编辑添加样式
//                Dialog dialog = new Dialog(mContext,android.R.style.ThemeOverlay_Material_Dialog_Alert);
//                View dialogView = mLayoutInflater.inflate(R.layout.list_item_dialog_layout, null);
//                TextView tvDelete = dialogView.findViewById(R.id.tv_delete);
//                TextView tvEdit = dialogView.findViewById(R.id.tv_edit);
//
//                //删除设置点击事件
//                tvDelete.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        int row = mNoteDbOpenHelper.deleteFromDbById(note.getId());//调用数据库删除方法，删除选中的那一条
//                        if (row > 0) {//确定数据库被删除了，再从列表中删除
//                            removeData(position);
//                            ToastUtil.toastShort(mContext,"删除成功！");
//                        }else {
//                            ToastUtil.toastShort(mContext,"删除失败！");
//                        }
//                        dialog.dismiss();
//
//                    }
//                });
//
//                //删除编辑点击事件
//                tvEdit.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(mContext, EditActivity.class);
//                        intent.putExtra("note", note);
//                        mContext.startActivity(intent);
//                        dialog.dismiss();
//                    }
//                });
//
//
//
//                dialog.setContentView(dialogView);
//                dialog.show();
//
//                return false;
//            }
//        });
//    }




    @Override
    public int getItemCount() {
        return mBeanList.size();
    }



    public void refreshData(List<Note> notes) {
        this.mBeanList = notes;
        notifyDataSetChanged();//通知数据集改变
    }

    public void removeData(int pos) {
        mBeanList.remove(pos);
        notifyItemRemoved(pos);
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView mTvTitle;
        TextView mTvContent;
        TextView mTvTime;
        ViewGroup rlContainer;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mTvTitle = itemView.findViewById(R.id.tv_title);
            this.mTvContent = itemView.findViewById(R.id.tv_content);
            this.mTvTime = itemView.findViewById(R.id.tv_time);
            this.rlContainer = itemView.findViewById(R.id.rl_item_container);
        }
    }
    class MyGridViewHolder extends RecyclerView.ViewHolder{

        TextView mTvTitle;
        TextView mTvContent;
        TextView mTvTime;
        ViewGroup rlContainer;

        public MyGridViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mTvTitle = itemView.findViewById(R.id.tv_title);
            this.mTvContent = itemView.findViewById(R.id.tv_content);
            this.mTvTime = itemView.findViewById(R.id.tv_time);
            this.rlContainer = itemView.findViewById(R.id.rl_item_container);
        }
    }





}
