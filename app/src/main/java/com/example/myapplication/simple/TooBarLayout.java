package com.example.myapplication.simple;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myapplication.R;


/**
 * @author Jackdaw Forever
 * @description: 自定义通用的toolbar，方便统一管理
 * @date :2020/11/5 10:22
 */
public class TooBarLayout extends RelativeLayout {
    /* 0:不显示 1:显示文字菜单 2:显示图片按钮菜单 */
    public static final int MENU_NONE = 0, MENU_TEXT = 1, MENU_IMG_BTN = 2;
    private boolean isShowBackBtn = true;
    private boolean isShowTitle = true;
    private int showMenuType;
    private float titleSize;
    private int titleColor = Color.WHITE;
    private String tvTitleText;
    private String tvMenuText;
    private float tvMenuTextSize;
    private int tvMenuTextColor = Color.WHITE;
    private int btnMenuSrc;
    private String tvBackText;
    private int backSrc;

    protected View rootView;
    protected ImageView mIvBackBtn;
    protected TextView mTvTitle;
    protected ImageButton mImgBtnMenu;
    protected TextView mTvMenu;
    protected TextView mTvBack;
    protected RelativeLayout mRlBack;

    //文字点击事件
    private TvMenuListener tvMenuListener;
    //图片按钮点击事件
    private ImgBtnMenuListener imgBtnMenuListener;

    public TooBarLayout(Context context) {
        this(context, null);
    }

    public TooBarLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TooBarLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        initView(context, attrs, defStyle);
    }

    private void initView(Context context, AttributeSet attrs, int defStyle) {

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TooBarLayout, defStyle, 0);
        isShowBackBtn = a.getBoolean(R.styleable.TooBarLayout_tl_isShowBackBtn, isShowBackBtn);
        isShowTitle = a.getBoolean(R.styleable.TooBarLayout_tl_isShowTitle, isShowTitle);
        showMenuType = a.getInt(R.styleable.TooBarLayout_tl_showMenu, MENU_NONE);
        titleSize = a.getDimensionPixelSize(R.styleable.TooBarLayout_tl_titleSize, 18);
        titleColor = a.getColor(R.styleable.TooBarLayout_tl_titleColor, titleColor);
        tvMenuText = a.getString(R.styleable.TooBarLayout_tl_tvMenuText);
        tvMenuTextSize = a.getDimensionPixelSize(R.styleable.TooBarLayout_tl_tvMenuTextSize, 16);
        tvMenuTextColor = a.getColor(R.styleable.TooBarLayout_tl_tvMenuTextColor, tvMenuTextColor);
        btnMenuSrc = a.getResourceId(R.styleable.TooBarLayout_tl_btnMenuSrc, 0);
        tvTitleText = a.getString(R.styleable.TooBarLayout_tl_titleText);
        tvBackText = a.getString(R.styleable.TooBarLayout_tl_backText);
        backSrc = a.getResourceId(R.styleable.TooBarLayout_tl_backImg, 0);
        a.recycle();

        rootView = LayoutInflater.from(context).inflate(R.layout.layout_toobar, this, false);
        mIvBackBtn = (ImageView) rootView.findViewById(R.id.iv_back);
        mTvTitle = (TextView) rootView.findViewById(R.id.tv_title);
        mTvMenu = (TextView) rootView.findViewById(R.id.tv_menu);
        mImgBtnMenu = (ImageButton) rootView.findViewById(R.id.img_btn_menu);
        mTvBack = (TextView) rootView.findViewById(R.id.tv_back);
        mRlBack = (RelativeLayout) rootView.findViewById(R.id.rl_back);


        if (!isShowBackBtn) mRlBack.setVisibility(GONE);
        else mRlBack.setVisibility(VISIBLE);
        if (!isShowTitle) mTvTitle.setVisibility(GONE);
        else mTvTitle.setVisibility(VISIBLE);
        mTvTitle.setTextSize(titleSize);
        mTvTitle.setTextColor(titleColor);
        mTvTitle.setText(tvTitleText);
        mTvBack.setText(tvBackText);
        if (backSrc != 0)
            mIvBackBtn.setImageResource(backSrc);
        setMenu(showMenuType);
        this.addView(rootView);

//        if (Build.VERSION.SDK_INT > 21) {
//            Slice slice = new Slice(this);
//            slice.setElevation(8.0f);
//            slice.showBottomEdgeShadow(true);
//            slice.showBottomEdgeShadow(false);
//            slice.setRadius(0);
//        }

        requestLayout();
    }

    /**
     * 设置 menu 的属性
     *
     * @param showMenuType
     */
    private void setMenu(int showMenuType) {
        switch (showMenuType) {
            case MENU_NONE:
                mTvMenu.setVisibility(GONE);
                mImgBtnMenu.setVisibility(GONE);
                break;
            case MENU_TEXT:
                mTvMenu.setVisibility(VISIBLE);
                mImgBtnMenu.setVisibility(GONE);
                mTvMenu.setText(tvMenuText);
                mTvMenu.setTextSize(tvMenuTextSize);
                mTvMenu.setTextColor(tvMenuTextColor);
                if (tvMenuListener != null)
                    mTvMenu.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tvMenuListener.onTvMenuClick((TextView) v);
                        }
                    });
                break;
            case MENU_IMG_BTN:
                mTvMenu.setVisibility(GONE);
                mImgBtnMenu.setVisibility(VISIBLE);
                mImgBtnMenu.setImageResource(btnMenuSrc);
                if (imgBtnMenuListener != null)
                    mImgBtnMenu.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            imgBtnMenuListener.onImgBtnMenuClick((ImageButton) v);
                        }
                    });
                break;
        }
    }

    public void setTvMenuListener(TvMenuListener listener) {
        this.tvMenuListener = listener;
    }

    public void setImgBtnMenuListener(ImgBtnMenuListener listener) {
        this.imgBtnMenuListener = listener;
    }

    public interface TvMenuListener {
        void onTvMenuClick(TextView view);
    }

    public interface ImgBtnMenuListener {
        void onImgBtnMenuClick(ImageButton view);
    }

    public boolean isShowBackBtn() {
        return isShowBackBtn;
    }

    public void setShowBackBtn(boolean showBackBtn) {
        isShowBackBtn = showBackBtn;
        if (mIvBackBtn != null)
            if (showBackBtn)
                mIvBackBtn.setVisibility(GONE);
            else mIvBackBtn.setVisibility(VISIBLE);
        requestLayout();
    }

    public boolean isShowTitle() {
        return isShowTitle;
    }

    public void setShowTitle(boolean showTitle) {
        isShowTitle = showTitle;
        if (mTvTitle != null)
            if (showTitle)
                mTvTitle.setVisibility(VISIBLE);
            else mTvTitle.setVisibility(GONE);
        requestLayout();
    }

    public int getShowMenuType() {
        return showMenuType;
    }

    public void setShowMenuType(int showMenuType) {
        this.showMenuType = showMenuType;
        setMenu(showMenuType);
        requestLayout();
    }

    public float getTitleSize() {
        return titleSize;
    }

    public void setTitleSize(float titleSize) {
        this.titleSize = titleSize;
        if (mTvTitle != null)
            mTvTitle.setTextSize(titleSize);
        requestLayout();
    }

    public int getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(int titleColor) {
        this.titleColor = titleColor;
        if (mTvTitle != null)
            mTvTitle.setTextColor(titleColor);
        requestLayout();
    }

    public String getTvTitleText() {
        return tvTitleText;
    }

    public void setTvTitleText(String tvTitleText) {
        this.tvTitleText = tvTitleText;
        if (mTvTitle != null)
            mTvTitle.setText(tvTitleText);
        requestLayout();
    }
}