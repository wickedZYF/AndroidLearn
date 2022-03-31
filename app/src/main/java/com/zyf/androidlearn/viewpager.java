package com.zyf.androidlearn;

import androidx.appcompat.app.AppCompatActivity;
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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.imageview.ShapeableImageView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zyf.androidlearn.Bean.User;
import com.zyf.androidlearn.utils.BitmapUtils;
import com.zyf.androidlearn.utils.CameraUtils;
import com.zyf.androidlearn.utils.SPUtils;

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

    //权限请求
    private RxPermissions rxPermissions;

    //是否拥有权限
    private boolean hasPermissions = false;

    //底部弹窗
    private BottomSheetDialog bottomSheetDialog;
    //弹窗视图
    private View bottomView;

    //存储拍完照后的图片
    private File outputImagePath;
    //启动相机标识
    public static final int TAKE_PHOTO = 1;
    //启动相册标识
    public static final int SELECT_PHOTO = 2;

    //图片控件
    private ShapeableImageView ivHead;
    //Base64
    private String base64Pic;
    //拍照和相册获取图片的Bitmap
    private Bitmap orc_bitmap;

    //Glide请求图片选项配置
    private RequestOptions requestOptions = RequestOptions.circleCropTransform()
            .diskCacheStrategy(DiskCacheStrategy.NONE)//不做磁盘缓存
            .skipMemoryCache(true);//不做内存缓存

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);




        ivHead = findViewById(R.id.iv_head);
        //检查版本
        checkVersion();
        //取出缓存
        String imageUrl = SPUtils.getString("imageUrl",null,this);
        if(imageUrl != null){
            Glide.with(this).load(imageUrl).apply(requestOptions).into(ivHead);
        }




        //创建存放viewpager的list
        List<View> viewList =new ArrayList<>();
        LayoutInflater lf=getLayoutInflater().from(this);
        MyAdapter myAdapter = new MyAdapter(viewList);

        View view1=lf.inflate(R.layout.activity_shoujiemian,null);  //将activity_shoujiemian和activity_function的界面
        View view2=lf.inflate(R.layout.activity_function,null);     //放入viewpager
        viewList.add(view1);
        viewList.add(view2);



        //找到button这个view
        Button button1= view2.findViewById(R.id.button2);
        //设置点击事件
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("mytag","click");
                //跳转到changename
                  Intent intent=new Intent(viewpager.this,changename.class);
                  startActivity(intent);
            }
        });


        //找到button这个view
        Button button2= view2.findViewById(R.id.button3);
        //设置点击事件
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("mytag","click");
                //跳转到changepassword
                Intent intent=new Intent(viewpager.this,changepassword.class);
                startActivity(intent);
            }
        });

        //找到button这个view
        Button button3= view2.findViewById(R.id.tuichu);
        //设置点击事件
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("mytag","click");
                //跳转到MainActivity
                Intent intent = new Intent();

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); //登录后返回键失效

                intent.setClass(viewpager.this, MainActivity.class);

                startActivity(intent);
            }
        });

        //找到button这个view
        Button button4= view2.findViewById(R.id.button5);
        //设置点击事件
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("mytag","click");
                //跳转到Shangchuan
                Intent intent=new Intent(viewpager.this,Shangchuan.class);
                startActivity(intent);
            }
        });

        ViewPager viewPager=findViewById(R.id.vp);   //定位到activity_viewpager

        viewPager.setAdapter(myAdapter);


        //**********************************************显示登录内容*****************************************************************
        XingMing=findViewById(R.id.textView4);
        bawei=findViewById(R.id.textView5);

        mySQLiteOpenHelper=new MySQLiteOpenHelper(this);

        List<User> users=mySQLiteOpenHelper.queryFromDbByCardId2(LoginUser);
        String result="";
        for (User user : users) {
             result=user.getCardId();
        }
//        bawei.setText(result);
    }



    /**
     * 检查版本
     */
    private void checkVersion() {
        //Android6.0及以上版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //如果你是在Fragment中，则把this换成getActivity()
            rxPermissions = new RxPermissions(this);
            //权限请求
            rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(granted -> {
                        if (granted) {//申请成功
                            showMsg("已获取权限");
                            hasPermissions = true;
                        } else {//申请失败
                            showMsg("权限未开启");
                            hasPermissions = false;
                        }
                    });
        } else {
            //Android6.0以下
            showMsg("无需请求动态权限");
        }
    }

    /**
     * 更换头像
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

        //拍照
        tvTakePictures.setOnClickListener(v -> {
            takePhoto();
            showMsg("拍照");
            bottomSheetDialog.cancel();
        });
        //打开相册
        tvOpenAlbum.setOnClickListener(v -> {
            openAlbum();
            showMsg("打开相册");
            bottomSheetDialog.cancel();
        });
        //取消
        tvCancel.setOnClickListener(v -> {
            bottomSheetDialog.cancel();
        });
        //底部弹窗显示
        bottomSheetDialog.show();
    }

    /**
     * 拍照
     */
    private void takePhoto() {
        if (!hasPermissions) {
            showMsg("未获取到权限");
            checkVersion();
            return;
        }
        SimpleDateFormat timeStampFormat = new SimpleDateFormat(
                "yyyy_MM_dd_HH_mm_ss");
        String filename = timeStampFormat.format(new Date());
        outputImagePath = new File(getExternalCacheDir(),
                filename + ".jpg");
        Intent takePhotoIntent = CameraUtils.getTakePhotoIntent(this, outputImagePath);
        // 开启一个带有返回值的Activity，请求码为TAKE_PHOTO
        startActivityForResult(takePhotoIntent, TAKE_PHOTO);
    }

    /**
     * 打开相册
     */
    private void openAlbum() {
        if (!hasPermissions) {
            showMsg("未获取到权限");
            checkVersion();
            return;
        }
        startActivityForResult(CameraUtils.getSelectPhotoIntent(), SELECT_PHOTO);
    }

    /**
     * 返回到Activity
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //拍照后返回
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    //显示图片
                    displayImage(outputImagePath.getAbsolutePath());
                }
                break;
            //打开相册后返回
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    String imagePath = null;
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                        //4.4及以上系统使用这个方法处理图片
                        imagePath = CameraUtils.getImageOnKitKatPath(data, this);
                    } else {
                        imagePath = CameraUtils.getImageBeforeKitKatPath(data, this);
                    }
                    //显示图片
                    displayImage(imagePath);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 通过图片路径显示图片
     */
    private void displayImage(String imagePath) {
        if (!TextUtils.isEmpty(imagePath)) {

            //放入缓存
            SPUtils.putString("imageUrl",imagePath,this);

            //显示图片
            Glide.with(this).load(imagePath).apply(requestOptions).into(ivHead);

            //压缩图片
            orc_bitmap = CameraUtils.compression(BitmapFactory.decodeFile(imagePath));
            //转Base64
            base64Pic = BitmapUtils.bitmapToBase64(orc_bitmap);

        } else {
            showMsg("图片获取失败");
        }
    }


    /**
     * Toast提示
     *
     * @param msg
     */
    private void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


}