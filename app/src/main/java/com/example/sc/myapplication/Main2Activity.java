package com.example.sc.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Main2Activity extends FragmentActivity implements OnClickListener {

    private String region2;
    // 三个tab布局
    private RelativeLayout datalayout, devicelayout;

    // 底部标签切换的Fragment
    private Fragment dataFragment;
    private Fragment deviceFragment;
    private Fragment currentFragment;
    // 底部标签图片
    // 底部标签的文本
    private TextView dataTv, deviceTv;
    private TextView headerTv;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        region2 = getIntent().getStringExtra("region2");
        setContentView(R.layout.activity_main2);
        back = (ImageButton) findViewById(R.id.ic_back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.ic_back:
                        Main2Activity.this.finish();
                        break;
                    default:
                        break;
                }
            }
        });

        initUI();
        initTab();
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        datalayout = (RelativeLayout) findViewById(R.id.rl_know);
        devicelayout = (RelativeLayout) findViewById(R.id.rl_want_know);
        datalayout.setOnClickListener(this);
        devicelayout.setOnClickListener(this);

        dataTv = (TextView) findViewById(R.id.tv_know);
        deviceTv = (TextView) findViewById(R.id.tv_i_want_know);
        headerTv = (TextView) findViewById(R.id.header);

    }

    /**
     * 初始化底部标签
     */
    private void initTab() {
        if (dataFragment == null) {
            dataFragment = new DataActivity();
        }

        if (!dataFragment.isAdded()) {
            // 提交事务
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_layout, dataFragment).commit();

            // 记录当前Fragment
            currentFragment = dataFragment;
            // 设置图片文本的变化
            dataTv.setTextColor(getResources()
                    .getColor(R.color.bottomtab_press));
            deviceTv.setTextColor(getResources().getColor(
                    R.color.bottomtab_normal));
            headerTv.setText("设备数据(" + region2 + ")");

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
            default:
                break;
        }
    }

    /**
     * 点击第一个tab
     */
    private void clickTab1Layout() {
        if (dataFragment == null) {
            dataFragment = new DataActivity();
        }
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), dataFragment);

        // 设置底部tab变化
        dataTv.setTextColor(getResources().getColor(R.color.bottomtab_press));
        deviceTv.setTextColor(getResources().getColor(
                R.color.bottomtab_normal));
    }

    /**
     * 点击第二个tab
     */
    private void clickTab2Layout() {
        if (deviceFragment == null) {
            deviceFragment = new ControlActivity();
        }
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), deviceFragment);

        dataTv.setTextColor(getResources().getColor(R.color.bottomtab_normal));
        deviceTv.setTextColor(getResources().getColor(
                R.color.bottomtab_press));

    }

    /**
     * 点击第三个tab
     */
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
