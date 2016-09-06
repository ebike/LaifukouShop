package com.sdjy.sdjymall.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hedgehog.ratingbar.RatingBar;
import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.base.BaseActivity;
import com.sdjy.sdjymall.common.util.T;
import com.sdjy.sdjymall.event.RefreshEvent;
import com.sdjy.sdjymall.http.HttpMethods;
import com.sdjy.sdjymall.model.GoodsItemModel;
import com.sdjy.sdjymall.subscribers.ProgressSubscriber;
import com.sdjy.sdjymall.subscribers.SubscriberOnNextListener;
import com.sdjy.sdjymall.util.StringUtils;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import de.greenrobot.event.EventBus;

public class GoodsEvaluationActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView titleView;
    @Bind(R.id.tv_right)
    TextView rightView;
    @Bind(R.id.ratingbar)
    RatingBar ratingBar;
    @Bind(R.id.et_content)
    EditText contentText;
    @Bind(R.id.tv_max_num)
    TextView maxNumView;
    @Bind(R.id.iv_pic)
    ImageView picView;

    private int ratingCount;
    private GoodsItemModel itemModel;

    @Override
    public void loadLoyout() {
        setContentView(R.layout.activity_goods_evaluation);
    }

    @Override
    public void init() {
        itemModel = (GoodsItemModel) getIntent().getSerializableExtra("GoodsItemModel");

        titleView.setText("商品评价");
        rightView.setText("提交");
        rightView.setVisibility(View.VISIBLE);

        Glide.with(this)
                .load(itemModel.pic)
                .error(R.mipmap.img_goods_default)
                .into(picView);

        ratingBar.setOnRatingChangeListener(new RatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(float RatingCount) {
                ratingCount = (int) RatingCount;
            }
        });
    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @OnTextChanged(R.id.et_content)
    public void changeContent(CharSequence text) {
        int num = 200 - text.length();
        maxNumView.setText(num + "/200");
    }

    @OnClick(R.id.tv_right)
    public void submit() {
        String content = contentText.getText().toString();
        if (ratingCount == 0) {
            T.showShort(this, "请给商品评分");
            return;
        }
        if (StringUtils.strIsEmpty(content)) {
            T.showShort(this, "请填写评价内容");
            return;
        }

        HttpMethods.getInstance().commentGoods(new ProgressSubscriber(new SubscriberOnNextListener() {
            @Override
            public void onNext(Object o) {
                T.showShort(GoodsEvaluationActivity.this, "评论成功");
                EventBus.getDefault().post(new RefreshEvent(OrderNoCommentActivity.class.getSimpleName()));
                GoodsEvaluationActivity.this.finish();
            }
        }, this), itemModel.orderItemId, itemModel.goodsId, ratingCount, content);
    }
}
