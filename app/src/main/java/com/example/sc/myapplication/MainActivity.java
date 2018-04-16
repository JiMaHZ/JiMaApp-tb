package com.example.sc.myapplication;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements OnClickListener {

    // 三个tab布局
    private RelativeLayout quyulayout, tianqilayout, shezhilayout;

    // 底部标签切换的Fragment
    private Fragment quyuFragment, tianqiFragment, shezhiFragment,
            currentFragment;
    // 底部标签图片
    private ImageView quyuImg, tianqiImg, shezhiImg;
    // 底部标签的文本
    private TextView quyuTv, tianqiTv, shezhiTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        initTab();
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        quyulayout = (RelativeLayout) findViewById(R.id.rl_know);
        tianqilayout = (RelativeLayout) findViewById(R.id.rl_want_know);
        shezhilayout = (RelativeLayout) findViewById(R.id.rl_me);
        quyulayout.setOnClickListener(this);
        tianqilayout.setOnClickListener(this);
        shezhilayout.setOnClickListener(this);

        quyuImg = (ImageView) findViewById(R.id.iv_know);
        tianqiImg = (ImageView) findViewById(R.id.iv_i_want_know);
        shezhiImg = (ImageView) findViewById(R.id.iv_me);
        quyuTv = (TextView) findViewById(R.id.tv_know);
        tianqiTv = (TextView) findViewById(R.id.tv_i_want_know);
        shezhiTv = (TextView) findViewById(R.id.tv_me);

    }

    /**
     * 初始化底部标签
     */
    private void initTab() {
        if (quyuFragment == null) {
            quyuFragment = new FragmentQuyu();
        }

        if (!quyuFragment.isAdded()) {
            // 提交事务
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_layout, quyuFragment).commit();

            // 记录当前Fragment
            currentFragment = quyuFragment;
            // 设置图片文本的变化
            quyuImg.setImageResource(R.drawable.btn_know_pre);
            quyuTv.setTextColor(getResources()
                    .getColor(R.color.bottomtab_press));
            tianqiImg.setImageResource(R.drawable.btn_wantknow_nor);
            tianqiTv.setTextColor(getResources().getColor(
                    R.color.bottomtab_normal));
            shezhiImg.setImageResource(R.drawable.btn_my_nor);
            shezhiTv.setTextColor(getResources().getColor(R.color.bottomtab_normal));

        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_know:
                clickTab1Layout();
                break;
            case R.id.rl_want_know:
                clickTab2Layout();
                break;
            case R.id.rl_me:
                clickTab3Layout();
                break;
            default:
                break;
        }
    }

    /**
     * 点击第一个tab
     */
    private void clickTab1Layout() {
        if (quyuFragment == null) {
            quyuFragment = new FragmentQuyu();
        }
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), quyuFragment);

        // 设置底部tab变化
        quyuImg.setImageResource(R.drawable.btn_know_pre);
        quyuTv.setTextColor(getResources().getColor(R.color.bottomtab_press));
        tianqiImg.setImageResource(R.drawable.btn_wantknow_nor);
        tianqiTv.setTextColor(getResources().getColor(
                R.color.bottomtab_normal));
        shezhiImg.setImageResource(R.drawable.btn_my_nor);
        shezhiTv.setTextColor(getResources().getColor(R.color.bottomtab_normal));
    }

    /**
     * 点击第二个tab
     */
    private void clickTab2Layout() {
        if (tianqiFragment == null) {
            tianqiFragment = new FragmentTianqi();
        }
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), tianqiFragment);

        quyuImg.setImageResource(R.drawable.btn_know_nor);
        quyuTv.setTextColor(getResources().getColor(R.color.bottomtab_normal));
        tianqiImg.setImageResource(R.drawable.btn_wantknow_pre);
        tianqiTv.setTextColor(getResources().getColor(
                R.color.bottomtab_press));
        shezhiImg.setImageResource(R.drawable.btn_my_nor);
        shezhiTv.setTextColor(getResources().getColor(R.color.bottomtab_normal));

    }

    /**
     * 点击第三个tab
     */
    private void clickTab3Layout() {
        if (shezhiFragment == null) {
            shezhiFragment = new FragmentShezhi();
        }

        addOrShowFragment(getSupportFragmentManager().beginTransaction(), shezhiFragment);
        quyuImg.setImageResource(R.drawable.btn_know_nor);
        quyuTv.setTextColor(getResources().getColor(R.color.bottomtab_normal));
        tianqiImg.setImageResource(R.drawable.btn_wantknow_nor);
        tianqiTv.setTextColor(getResources().getColor(
                R.color.bottomtab_normal));
        shezhiImg.setImageResource(R.drawable.btn_my_pre);
        shezhiTv.setTextColor(getResources().getColor(R.color.bottomtab_press));

    }

    /**
     * 添加或者显示碎片
     *
     * @param transaction
     * @param fragment
     */
    private void addOrShowFragment(FragmentTransaction transaction,
                                   Fragment fragment) {
        if (currentFragment == fragment)
            return;

        if (!fragment.isAdded()) { // 如果当前fragment未被添加，则添加到Fragment管理器中
            transaction.hide(currentFragment)
                    .add(R.id.content_layout, fragment).commit();
        } else {
            transaction.hide(currentFragment).show(fragment).commit();
        }

        currentFragment = fragment;
    }

}
