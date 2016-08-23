package com.sdjy.sdjymall.activity;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.base.BaseActivity;
import com.sdjy.sdjymall.constants.StaticValues;
import com.sdjy.sdjymall.db.ProvinceInfoDao;
import com.sdjy.sdjymall.model.LocationJson;
import com.sdjy.sdjymall.model.UserModel;
import com.sdjy.sdjymall.util.ImageCompress;
import com.sdjy.sdjymall.view.RowLabelValueView;
import com.sdjy.sdjymall.view.wheel.AddressThreeWheelViewDialog;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
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
//            if (!StringUtils.strIsEmpty(StaticValues.userModel.)) {
//                nameView.setValue(AppConfig.userInfoBean.getUserName());
//            }
            if (StaticValues.userModel.sex == 0) {
                sexView.setValue("男");
            } else if (StaticValues.userModel.sex == 1) {
                sexView.setValue("女");
            } else {
                sexView.setValue("保密");
            }
            phoneView.setValue(StaticValues.userModel.phone);
            idCardView.setValue(StaticValues.userModel.idCard);
            areaView.setValue(StaticValues.userModel.province + "-" + StaticValues.userModel.city + "-" + StaticValues.userModel.area);
            addressView.setValue(StaticValues.userModel.address);
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

//    @Event(value = R.id.rl_header)
//    private void header(View view) {
//        takePhotoTime = System.currentTimeMillis();
//        showPhotoDialog(this, takePhotoTime);
//    }
//
//    public static void showPhotoDialog(final Activity activity, final long takePhotoTime) {
//        new ActionSheetDialog(activity)
//                .builder()
//                .setCancelable(true)
//                .setCanceledOnTouchOutside(true)
//                .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue,
//                        new ActionSheetDialog.OnSheetItemClickListener() {
//                            @Override
//                            public void onClick(int which) {
//                                File file = new File(AppConfig.CAMERA_PIC_PATH);
//                                if (!file.exists()) {
//                                    file.mkdirs();
//                                }
//                                File file2 = new File(AppConfig.CAMERA_PIC_PATH, takePhotoTime + ".jpg");
//                                try {
//                                    file2.createNewFile();
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file2));
//                                activity.startActivityForResult(intent, 2);
//                            }
//                        })
//                .addSheetItem("从手机相册选择", ActionSheetDialog.SheetItemColor.Blue,
//                        new ActionSheetDialog.OnSheetItemClickListener() {
//                            @Override
//                            public void onClick(int which) {
//                                Intent intent = new Intent(activity, PhotoAlbumListActivity.class);
//                                activity.startActivity(intent);
//                            }
//                        }).show();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        //判断请求码
//        switch (requestCode) {
//            case 2://拍照
//                //设置文件保存路径这里放在跟目录下
//                File mFile = new File(AppConfig.CAMERA_PIC_PATH + takePhotoTime + ".jpg");
//                if (mFile.length() != 0) {
//                    ImageItem item = new ImageItem();
//                    item.imageId = takePhotoTime + "";
//                    item.picName = takePhotoTime + ".jpg";
//                    item.size = String.valueOf(mFile.length());
//                    item.sourcePath = AppConfig.CAMERA_PIC_PATH + takePhotoTime + ".jpg";
//                    uploadImage(item);
//                }
//                break;
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//    //根据当前操作的照片进行赋值
//    private void uploadImage(final ImageItem imageItem) {
//        //由于目前没有查看图片，每次选择图片都是覆盖更新，所以，只用到路径字段，其他字段预留
//        if (imageItem != null && !CommonUtils.strIsEmpty(imageItem.sourcePath)) {
//            //对图片做压缩处理
//            Bitmap bitmap = compress.getimage(imageItem.sourcePath);
//            if (null != bitmap) {
//                try {
//                    compress.compressAndGenImage(bitmap, imageItem.sourcePath, AppConfig.compressedImage + imageItem.picName, 100);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            //压缩后的图片文件
//            File file = new File(AppConfig.compressedImage + imageItem.picName);
//            List<SendParamsBean> sendParamsBeans = new ArrayList<SendParamsBean>();
//            sendParamsBeans.add(new SendParamsBean("userId", AppConfig.userInfoBean.getUserId(), false));
//            sendParamsBeans.add(new SendParamsBean("headPic", file, true));
//            RequestParams params = DRequestParamsUtils.getRequestParamsHasFile_Header(HttpConstants.updateHeadPic(), sendParamsBeans);
//            DHttpUtils.post_String(this, true, params, new DCommonCallback<String>() {
//                @Override
//                public void onSuccess(String result) {
//                    ResponseBean<UserInfoBean> bean = new Gson().fromJson(result, new TypeToken<ResponseBean<UserInfoBean>>() {
//                    }.getType());
//                    if (bean.getCode() == 1) {
//                        AppConfig.userInfoBean = bean.getData();
//                        EventBus.getDefault().post(bean.getData());
//                        Glide.with(BaseInformationActivity.this)
//                                .load(AppConfig.userInfoBean.getHeadPic())
//                                .bitmapTransform(new CropCircleTransformation(BaseInformationActivity.this))
//                                .into(headerView);
//                    } else {
//                        showShortText(bean.getErrmsg());
//                    }
//                }
//            });
//        }
//    }
//
//    //从相册选择
//    public void onEvent(SelectPhotoEvent event) {
//        if (event != null && event.getItem() != null) {
//            uploadImage(event.getItem());
//        }
//    }
//
//    @Override
//    public void onClick(View view) {
//        Intent intent = null;
//        switch (view.getId()) {
//            case R.id.rlvv_name:
//                intent = new Intent(this, UpdateTextValueActivity.class);
//                intent.putExtra("fieldName_CH", getString(R.string.name));
//                intent.putExtra("fieldValue", AppConfig.userInfoBean.getUserName());
//                intent.putExtra("fieldName", "userName");
//                startActivity(intent);
//                break;
//            case R.id.rlvv_sex:
//                chooseSex();
//                break;
//            case R.id.rlvv_id_card:
//                intent = new Intent(this, UpdateTextValueActivity.class);
//                intent.putExtra("fieldName_CH", "身份证号");
//                intent.putExtra("fieldValue", AppConfig.userInfoBean.getIdNum());
//                intent.putExtra("fieldName", "idNum");
//                startActivity(intent);
//                break;
//            case R.id.rlvv_area:
//                if (AppConfig.userInfoBean != null) {
//                    dialog.setData(mProvinceList, AppConfig.userInfoBean.getProvince(), AppConfig.userInfoBean.getCity(), AppConfig.userInfoBean.getArea());
//                } else {
//                    dialog.setData(mProvinceList);
//                }
//                dialog.show(new AddressThreeWheelViewDialog.ConfirmAction() {
//                    @Override
//                    public void doAction(final LocationJson root, final LocationJson child, final LocationJson child2) {
//                        Map<String, String> map = new HashMap<>();
//                        map.put("userId", AppConfig.userInfoBean.getUserId());
//                        map.put("province", root.getName());
//                        map.put("city", child.getName());
//                        map.put("area", child2.getName());
//                        RequestParams params = DRequestParamsUtils.getRequestParams_Header(HttpConstants.getUpdateUserUrl(), map);
//                        DHttpUtils.post_String(BaseInformationActivity.this, true, params, new DCommonCallback<String>() {
//                            @Override
//                            public void onSuccess(String result) {
//                                ResponseBean<UserInfoBean> bean = new Gson().fromJson(result, new TypeToken<ResponseBean<UserInfoBean>>() {
//                                }.getType());
//                                if (bean.getCode() == 1) {
//                                    AppConfig.userInfoBean = bean.getData();
//                                    EventBus.getDefault().post(bean.getData());
//                                    areaView.setValue(AppConfig.userInfoBean.getProvince() + "-" + AppConfig.userInfoBean.getCity() + "-" + AppConfig.userInfoBean.getArea());
//                                    getSchool(root.getName(), child.getName(), child2.getName());
//                                } else {
//                                    showShortText(bean.getErrmsg());
//                                }
//                            }
//                        });
//                    }
//                });
//                break;
//            case R.id.rlvv_address:
//                if (AppConfig.userInfoBean.getUserType() == 1) {
//                    chooseSchool();
//                } else {
//                    intent = new Intent(this, UpdateTextValueActivity.class);
//                    intent.putExtra("fieldName_CH", getString(R.string.detailed_address));
//                    intent.putExtra("fieldValue", AppConfig.userInfoBean.getAddress());
//                    intent.putExtra("fieldName", "address");
//                    startActivity(intent);
//                }
//                break;
//        }
//    }
//
//    private void getSchool(String province, String city, String county) {
//        Map<String, String> map = new HashMap<>();
//        map.put("province", province);
//        map.put("city", city);
//        map.put("area", county);
//        RequestParams params = DRequestParamsUtils.getRequestParams(HttpConstants.getSchools(), map);
//        DHttpUtils.post_String(BaseInformationActivity.this, false, params, new DCommonCallback<String>() {
//            @Override
//            public void onSuccess(String result) {
//                ResponseBean<List<SchoolBean>> responseBean = new Gson().fromJson(result, new TypeToken<ResponseBean<List<SchoolBean>>>() {
//                }.getType());
//                if (responseBean.getCode() == 1) {
//                    schoolList = responseBean.getData();
//                } else {
//                    showShortText(responseBean.getErrmsg());
//                }
//            }
//        });
//    }
//
//    private void chooseSex() {
//        View view = LayoutInflater.from(this).inflate(R.layout.view_sex, null, false);
//        TextView manView = (TextView) view.findViewById(R.id.tv_man);
//        TextView womanView = (TextView) view.findViewById(R.id.tv_woman);
//        TextView otherView = (TextView) view.findViewById(R.id.tv_other);
//        Drawable drawable = getResources().getDrawable(R.mipmap.icon_sel);
//        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//        if (AppConfig.userInfoBean.getSex() == 0) {
//            manView.setCompoundDrawables(null, null, drawable, null);
//        } else if (AppConfig.userInfoBean.getSex() == 1) {
//            womanView.setCompoundDrawables(null, null, drawable, null);
//        } else if (AppConfig.userInfoBean.getSex() == 2) {
//            otherView.setCompoundDrawables(null, null, drawable, null);
//        }
//        final CustomDialog dialog = CommonUtils.showCustomDialog1(this, "选择性别", view);
//        manView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.cancel();
//                updateSex("0");
//            }
//        });
//        womanView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.cancel();
//                updateSex("1");
//            }
//        });
//        otherView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.cancel();
//                updateSex("2");
//            }
//        });
//    }
//
//    private void updateSex(final String sex) {
//        Map<String, String> map = new HashMap<String, String>();
//        map.put("userId", AppConfig.userInfoBean.getUserId());
//        map.put("sex", sex);
//        RequestParams params = DRequestParamsUtils.getRequestParams_Header(HttpConstants.getUpdateUserUrl(), map);
//        DHttpUtils.post_String(this, true, params, new DCommonCallback<String>() {
//            @Override
//            public void onSuccess(String result) {
//                ResponseBean<UserInfoBean> bean = new Gson().fromJson(result, new TypeToken<ResponseBean<UserInfoBean>>() {
//                }.getType());
//                if (bean.getCode() == 1) {
//                    AppConfig.userInfoBean = bean.getData();
//                    EventBus.getDefault().post(bean.getData());
//                    if ("0".equals(sex)) {
//                        sexView.setValue("男");
//                    } else if ("1".equals(sex)) {
//                        sexView.setValue("女");
//                    } else {
//                        sexView.setValue("保密");
//                    }
//                } else {
//                    showShortText(bean.getErrmsg());
//                }
//
//            }
//        });
//    }

    public void onEvent(UserModel user) {
        if (user != null) {
            init();
        }
    }
}
