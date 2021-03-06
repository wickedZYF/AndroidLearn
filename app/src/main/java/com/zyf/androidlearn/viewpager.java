package com.zyf.androidlearn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zyf.androidlearn.Adapter.MyAdapter;
import com.zyf.androidlearn.Adapter.MyAdapter2;
import com.zyf.androidlearn.Adapter.MyAdapter3;
import com.zyf.androidlearn.Bean.Note;
import com.zyf.androidlearn.Bean.User;
import com.zyf.androidlearn.List_item.AddActivity;
import com.zyf.androidlearn.List_item.Mycangku;
import com.zyf.androidlearn.SQLite.MySQLiteOpenHelper;
import com.zyf.androidlearn.SQLite.NoteDbOpenHelper;
import com.zyf.androidlearn.utils.BitmapUtils;
import com.zyf.androidlearn.utils.CameraUtils;
import com.zyf.androidlearn.utils.SPUtils;
import com.zyf.androidlearn.utils.SpfUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.zyf.androidlearn.MainActivity.LoginUser;

public class viewpager extends AppCompatActivity {
    private  TextView XingMing;
    private  TextView bawei;
    private MySQLiteOpenHelper mySQLiteOpenHelper;

    //????????????
    private RxPermissions rxPermissions;

    //??????????????????
    private boolean hasPermissions = false;

    //????????????
    private BottomSheetDialog bottomSheetDialog;
    //????????????
    private View bottomView;

    //???????????????????????????
    private File outputImagePath;
    //??????????????????
    public static final int TAKE_PHOTO = 1;
    //??????????????????
    public static final int SELECT_PHOTO = 2;

    //????????????
    private ShapeableImageView ivHead;
    //Base64
    private String base64Pic;
    //??????????????????????????????Bitmap
    private Bitmap orc_bitmap;

    //Glide????????????????????????
    private RequestOptions requestOptions = RequestOptions.circleCropTransform()
            .diskCacheStrategy(DiskCacheStrategy.NONE)//??????????????????
            .skipMemoryCache(true);//??????????????????

    //**********************************************????????????*******************************************************
    private RecyclerView mRecyclerView;
    private FloatingActionButton mBtnAdd;
    private List<Note> mNotes;
    private MyAdapter3 mMyAdapter;

    private NoteDbOpenHelper mNoteDbOpenHelper;
    public static final int MODE_LINEAR = 0;
    public static final int MODE_GRID = 1;

    public static final String KEY_LAYOUT_MODE = "key_layout_mode";

    private int currentListLayoutMode = MODE_LINEAR;
    //**********************************************????????????*******************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);


//        //??????ActionBar
//        getSupportActionBar().hide();
//        //??????????????????
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);







        //????????????viewpager???list
        List<View> viewList =new ArrayList<>();
        LayoutInflater lf=getLayoutInflater().from(this);
        MyAdapter myAdapter = new MyAdapter(viewList);

        View view1=lf.inflate(R.layout.activity_shoujiemian,null);  //???activity_shoujiemian???activity_function?????????
        View view2=lf.inflate(R.layout.activity_function,null);     //??????viewpager
        viewList.add(view1);
        viewList.add(view2);



        //??????button??????view
        Button button1= view2.findViewById(R.id.button2);
        //??????????????????
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("mytag","click");
                //?????????changename
                  Intent intent=new Intent(viewpager.this,changename.class);
                  startActivity(intent);
            }
        });


        //??????button??????view
        Button button2= view2.findViewById(R.id.button3);
        //??????????????????
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("mytag","click");
                //?????????changepassword
                Intent intent=new Intent(viewpager.this,changepassword.class);
                startActivity(intent);
            }
        });

        //??????button??????view
        Button button3= view2.findViewById(R.id.tuichu);
        //??????????????????
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("mytag","click");
                //?????????MainActivity
                Intent intent = new Intent();

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); //????????????????????????

                intent.setClass(viewpager.this, MainActivity.class);

                startActivity(intent);
            }
        });

        //??????button??????view
        Button button4= view2.findViewById(R.id.button6);
        //??????????????????
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("mytag","click");
                //?????????changepassword
                Intent intent=new Intent(viewpager.this, Mycangku.class);
                startActivity(intent);
            }
        });


        ViewPager viewPager=findViewById(R.id.vp);   //?????????activity_viewpager

        viewPager.setAdapter(myAdapter);



        ivHead =view2.findViewById(R.id.iv_head);
        //????????????
        checkVersion();
        //????????????
        String imageUrl = SPUtils.getString("imageUrl",null,this);
        if(imageUrl != null){
            Glide.with(this).load(imageUrl).apply(requestOptions).into(ivHead);
        }


        //**********************************************??????????????????*****************************************************************
        XingMing=view2.findViewById(R.id.textView4);
        bawei=view2.findViewById(R.id.textView5);


        //????????????
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String uid = intent.getStringExtra("cardId");

        XingMing.setText(name);
        bawei.setText("id:"+uid);



        mRecyclerView=view1.findViewById(R.id.rlv2);

        initData();
        initEvent();
    }

    //*************************************????????????******************************************************
    @Override
    protected void onResume() {
        super.onResume();
        refreshDataFromDb();
        setListLayout();
    }

    private void setListLayout() {
        currentListLayoutMode = SpfUtil.getIntWithDefault(this, KEY_LAYOUT_MODE, MODE_LINEAR);
        if (currentListLayoutMode == MODE_LINEAR) {
            setToLinearList();
        }else{
            setToGridList();
        }
    }

    private void refreshDataFromDb() {
        mNotes = getDataFromDB();
        //????????????????????????MyAdapter3????????????
        //??????MyAdapter3??????refreshData???????????????????????????
        mMyAdapter.refreshData(mNotes);
    }

    private void initEvent() {
        mMyAdapter=new MyAdapter3(this,mNotes);

        mRecyclerView.setAdapter(mMyAdapter);

        setListLayout();

    }

    private void initData() {
        mNotes =new ArrayList<>();
        mNoteDbOpenHelper =new NoteDbOpenHelper(this);

        mNotes = getDataFromDB();


    }
    //???????????????????????????
    private List<Note> getDataFromDB() {
        String name=LoginUser;
        return mNoteDbOpenHelper.queryAllFromDb();
    }

    //??????
    private String getCurrentTimeFormat(){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("YYYY???MM???dd HH:mm:ss");//??????????????????
        Date date=new Date();
        return simpleDateFormat.format(date);
    }




    public void add(View view) {
        Intent intent = new Intent();

        intent.setClass(viewpager.this, AddActivity.class);

        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override//?????????????????????????????????
            public boolean onQueryTextChange(String newText) {
                mNotes = mNoteDbOpenHelper.queryFromDbByTitle(newText);  //???????????????????????????????????????
                mMyAdapter.refreshData(mNotes);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    //????????????????????????????????????
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);

        switch (item.getItemId()) {
            //????????????
            case R.id.menu_linear:

                setToLinearList();

                currentListLayoutMode =MODE_LINEAR;
                SpfUtil.saveInt(this,KEY_LAYOUT_MODE,MODE_LINEAR);

                return true;
            //????????????
            case R.id.menu_grid:

                setToGridList();
                currentListLayoutMode =MODE_GRID;
                SpfUtil.saveInt(this,KEY_LAYOUT_MODE,MODE_GRID);

                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
    private void setToLinearList() {
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mMyAdapter.setViewType(MyAdapter3.TYPE_LINEAR_LAYOUT);//??????????????????
        mMyAdapter.notifyDataSetChanged();
    }


    private void setToGridList() {
        RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mMyAdapter.setViewType(MyAdapter3.TYPE_GRID_LAYOUT);//??????????????????
        mMyAdapter.notifyDataSetChanged();
    }

    //?????????????????????????????????????????????
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (currentListLayoutMode == MODE_LINEAR) {//??????????????????????????????????????????????????????
            MenuItem item = menu.findItem(R.id.menu_linear);
            item.setChecked(true);
        } else {   //????????????????????????
            menu.findItem(R.id.menu_grid).setChecked(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    //*************************************????????????******************************************************

    /**
     * ????????????
     */
    private void checkVersion() {
        //Android6.0???????????????
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //???????????????Fragment????????????this??????getActivity()
            rxPermissions = new RxPermissions(this);
            //????????????
            rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(granted -> {
                        if (granted) {//????????????
                            showMsg("???????????????");
                            hasPermissions = true;
                        } else {//????????????
                            showMsg("???????????????");
                            hasPermissions = false;
                        }
                    });
        } else {
            //Android6.0??????
            showMsg("????????????????????????");
        }
    }

    /**
     * ????????????
     *
     * @param view
     */
    public void changeAvatar(View view) {
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomView = getLayoutInflater().inflate(R.layout.dialog_bottom, null);
        bottomSheetDialog.setContentView(bottomView);
        bottomSheetDialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundColor(Color.TRANSPARENT);
        TextView tvTakePictures = bottomView.findViewById(R.id.tv_take_pictures);
        TextView tvOpenAlbum = bottomView.findViewById(R.id.tv_open_album);
        TextView tvCancel = bottomView.findViewById(R.id.tv_cancel);

        //??????
        tvTakePictures.setOnClickListener(v -> {
            takePhoto();
            showMsg("??????");
            bottomSheetDialog.cancel();
        });
        //????????????
        tvOpenAlbum.setOnClickListener(v -> {
            openAlbum();
            showMsg("????????????");
            bottomSheetDialog.cancel();
        });
        //??????
        tvCancel.setOnClickListener(v -> {
            bottomSheetDialog.cancel();
        });
        //??????????????????
        bottomSheetDialog.show();
    }

    /**
     * ??????
     */
    private void takePhoto() {
        if (!hasPermissions) {
            showMsg("??????????????????");
            checkVersion();
            return;
        }
        SimpleDateFormat timeStampFormat = new SimpleDateFormat(
                "yyyy_MM_dd_HH_mm_ss");
        String filename = timeStampFormat.format(new Date());
        outputImagePath = new File(getExternalCacheDir(),
                filename + ".jpg");
        Intent takePhotoIntent = CameraUtils.getTakePhotoIntent(this, outputImagePath);
        // ??????????????????????????????Activity???????????????TAKE_PHOTO
        startActivityForResult(takePhotoIntent, TAKE_PHOTO);
    }

    /**
     * ????????????
     */
    private void openAlbum() {
        if (!hasPermissions) {
            showMsg("??????????????????");
            checkVersion();
            return;
        }
        startActivityForResult(CameraUtils.getSelectPhotoIntent(), SELECT_PHOTO);
    }

    /**
     * ?????????Activity
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //???????????????
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    //????????????
                    displayImage(outputImagePath.getAbsolutePath());
                }
                break;
            //?????????????????????
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    String imagePath = null;
                    //???????????????????????????
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                        //4.4?????????????????????????????????????????????
                        imagePath = CameraUtils.getImageOnKitKatPath(data, this);
                    } else {
                        imagePath = CameraUtils.getImageBeforeKitKatPath(data, this);
                    }
                    //????????????
                    displayImage(imagePath);
                }
                break;
            default:
                break;
        }
    }

    /**
     * ??????????????????????????????
     */
    private void displayImage(String imagePath) {
        if (!TextUtils.isEmpty(imagePath)) {

            //????????????
            SPUtils.putString("imageUrl",imagePath,this);

            //????????????
            Glide.with(this).load(imagePath).apply(requestOptions).into(ivHead);

            //????????????
            orc_bitmap = CameraUtils.compression(BitmapFactory.decodeFile(imagePath));
            //???Base64
            base64Pic = BitmapUtils.bitmapToBase64(orc_bitmap);

        } else {
            showMsg("??????????????????");
        }
    }


    /**
     * Toast??????
     *
     * @param msg
     */
    private void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


}