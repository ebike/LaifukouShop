package com.sdjy.sdjymall.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.base.BaseActivity;
import com.sdjy.sdjymall.constants.FinalValues;
import com.sdjy.sdjymall.constants.StaticValues;
import com.sdjy.sdjymall.db.ProvinceInfoDao;
import com.sdjy.sdjymall.event.SelectPhotoEvent;
import com.sdjy.sdjymall.http.HttpMethods;
import com.sdjy.sdjymall.model.ImageItem;
import com.sdjy.sdjymall.model.LocationJson;
import com.sdjy.sdjymall.model.UserModel;
import com.sdjy.sdjymall.subscribers.ProgressSubscriber;
import com.sdjy.sdjymall.subscribers.SubscriberOnNextListener;
import com.sdjy.sdjymall.util.ImageCompress;
import com.sdjy.sdjymall.util.StringUtils;
import com.sdjy.sdjymall.view.ActionSheetDialog;
import com.sdjy.sdjymall.view.RowLabelValueView;
import com.sdjy.sdjymall.view.wheel.AddressThreeWheelViewDialog;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * 账号管理
 */
public class AccountManageActivity extends BaseActivity {

    @Bind(R.id.iv_header)
    ImageView headerView;
    @Bind(R.id.rlvv_username)
    RowLabelValueView userNameView;
    @Bind(R.id.rlvv_name)
    RowLabelValueView nameView;
    @Bind(R.id.rlvv_sex)
    RowLabelValueView sexView;
    @Bind(R.id.rlvv_phone)
    RowLabelValueView phoneView;
    @Bind(R.id.rlvv_id_card)
    RowLabelValueView idCardView;
    @Bind(R.id.rlvv_area)
    RowLabelValueView areaView;
    @Bind(R.id.rlvv_address)
    RowLabelValueView addressView;
    @Bind(R.id.rlvv_create_team)
    RowLabelValueView createTeamView;

    private AddressThreeWheelViewDialog dialog;
    private ProvinceInfoDao provinceDao;
    private List<LocationJson> mProvinceList;
    //拍照时间
    private long takePhotoTime;
    //图片压缩类
    private ImageCompress compress;

    @Override
    public void loadLoyout() {
        setContentView(R.layout.activity_account_manage);
        EventBus.getDefault().register(this);
    }

    @Override
    public void init() {
        if (StaticValues.userModel != null) {
            Glide.with(this)
                    .load(StaticValues.userModel.headPic)
                    .bitmapTransform(new CropCircleTransformation(this))
                    .error(R.mipmap.icon_comment_head)
                    .into(headerView);
            userNameView.setValue(StaticValues.userModel.loginName);
            nameView.setValue(StaticValues.userModel.name);
            if (StaticValues.userModel.sex == 1) {
                sexView.setValue("男");
            } else if (StaticValues.userModel.sex == 2) {
                sexView.setValue("女");
            } else {
                sexView.setValue("保密");
            }
            phoneView.setValue(StaticValues.userModel.phone);
            idCardView.setValue(StaticValues.userModel.idCard);
            if (!StringUtils.strIsEmpty(StaticValues.userModel.province)) {
                areaView.setValue(StaticValues.userModel.province + "-" + StaticValues.userModel.city + "-" + StaticValues.userModel.area);
            } else {
                areaView.setValue("");
            }
            addressView.setValue(StaticValues.userModel.address);
            if (StaticValues.userModel.userType == 2) {
                createTeamView.setVisibility(View.VISIBLE);
            } else {
                createTeamView.setVisibility(View.GONE);
            }
        }
        dialog = new AddressThreeWheelViewDialog(this);
        provinceDao = new ProvinceInfoDao(this);
        mProvinceList = provinceDao.queryAll();
        compress = new ImageCompress();
    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.rl_header)
    public void header(View view) {
        takePhotoTime = System.currentTimeMillis();
        showPhotoDialog(this, takePhotoTime);
    }

    public static void showPhotoDialog(final Activity activity, final long takePhotoTime) {
        new ActionSheetDialog(activity)
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                File file = new File(FinalValues.CAMERA_PIC_PATH);
                                if (!file.exists()) {
                                    file.mkdirs();
                                }
                                File file2 = new File(FinalValues.CAMERA_PIC_PATH, takePhotoTime + ".jpg");
                                try {
                                    file2.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file2));
                                activity.startActivityForResult(intent, 2);
                            }
                        })
                .addSheetItem("从手机相册选择", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                Intent intent = new Intent(activity, PhotoAlbumListActivity.class);
                                activity.startActivity(intent);
                            }
                        }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //判断请求码
        switch (requestCode) {
            case 2://拍照
                //设置文件保存路径这里放在跟目录下
                File mFile = new File(FinalValues.CAMERA_PIC_PATH + takePhotoTime + ".jpg");
                if (mFile.length() != 0) {
                    ImageItem item = new ImageItem();
                    item.imageId = takePhotoTime + "";
                    item.picName = takePhotoTime + ".jpg";
                    item.size = String.valueOf(mFile.length());
                    item.sourcePath = FinalValues.CAMERA_PIC_PATH + takePhotoTime + ".jpg";
                    uploadImage(item);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //根据当前操作的照片进行赋值
    private void uploadImage(final ImageItem imageItem) {
        //由于目前没有查看图片，每次选择图片都是覆盖更新，所以，只用到路径字段，其他字段预留
        if (imageItem != null && !StringUtils.strIsEmpty(imageItem.sourcePath)) {
            //对图片做压缩处理
            Bitmap bitmap = compress.getimage(imageItem.sourcePath);
            if (null != bitmap) {
                try {
                    compress.compressAndGenImage(bitmap, imageItem.sourcePath, FinalValues.compressedImage + imageItem.picName, 100);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //压缩后的图片文件
            File file = new File(FinalValues.compressedImage + imageItem.picName);
            SubscriberOnNextListener listener = new SubscriberOnNextListener<UserModel>() {
                @Override
                public void onNext(UserModel model) {
                    StaticValues.userModel = model;
                    EventBus.getDefault().post(model);
                    Glide.with(AccountManageActivity.this)
                            .load(StaticValues.userModel.headPic)
                            .bitmapTransform(new CropCircleTransformation(AccountManageActivity.this))
                            .error(R.mipmap.icon_comment_head)
                            .into(headerView);
                }
            };
            HttpMethods.getInstance().updateHeadPic(new ProgressSubscriber<UserModel>(listener, this), file);
        }
    }

    //从相册选择
    public void onEvent(SelectPhotoEvent event) {
        if (event != null && event.getItem() != null) {
            uploadImage(event.getItem());
        }
    }

    @OnClick(R.id.rlvv_area)
    public void area() {
        if (StaticValues.userModel != null) {
            dialog.setData(mProvinceList, StaticValues.userModel.province, StaticValues.userModel.city, StaticValues.userModel.area);
        } else {
            dialog.setData(mProvinceList);
        }
        dialog.show(new AddressThreeWheelViewDialog.ConfirmAction() {
            @Override
            public void doAction(final LocationJson root, final LocationJson child, final LocationJson child2) {
                Map<String, String> map = new HashMap<>();
                map.put("province", root.getName());
                map.put("city", child.getName());
                map.put("area", child2.getName());
                SubscriberOnNextListener<UserModel> listener = new SubscriberOnNextListener<UserModel>() {
                    @Override
                    public void onNext(UserModel model) {
                        StaticValues.userModel = model;
                        EventBus.getDefault().post(model);
                        areaView.setValue(StaticValues.userModel.province + "-" + StaticValues.userModel.city + "-" + StaticValues.userModel.area);
                    }
                };
                HttpMethods.getInstance().updateUserData(new ProgressSubscriber<UserModel>(listener, AccountManageActivity.this), map);
            }
        });
    }

    @OnClick(R.id.rlvv_sex)
    public void sex() {
        startActivity(new Intent(this, UpdateSexActivity.class));
    }

    @OnClick({R.id.rlvv_name, R.id.rlvv_id_card, R.id.rlvv_address})
    public void updateField(View view) {
        Intent intent = new Intent(this, UpdateFieldActivity.class);
        switch (view.getId()) {
            case R.id.rlvv_name:
                intent.putExtra("title", "姓名");
                break;
            case R.id.rlvv_id_card:
                intent.putExtra("title", "身份证号");
                break;
            case R.id.rlvv_address:
                intent.putExtra("title", "详细地址");
                break;
        }
        startActivity(intent);
    }

    @OnClick(R.id.rlvv_receive_address)
    public void receiveAddress() {
        startActivity(new Intent(this, ReceiveAddressActivity.class));
    }

    @OnClick(R.id.rlvv_create_team)
    public void createTeam() {
        startActivity(new Intent(this, CreateTeamInfoActivity.class));
    }

    public void onEvent(UserModel user) {
        if (user != null) {
            init();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
