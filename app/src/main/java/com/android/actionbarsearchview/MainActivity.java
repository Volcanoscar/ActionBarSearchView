package com.android.actionbarsearchview;

import android.app.ActionBar;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import java.util.List;
import java.util.Objects;

import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements SearchView.OnQueryTextListener {

    private SearchView mSearchView;
    private TextView mStatusView;
    private ActionBar mActionBar;
    private TextView mTitleTv;
    private TextView mSubtiltleTv;
    private ImageView mUpIv;
    private ImageView mHomeIv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        setContentView(R.layout.activity_main);
        mActionBar = getActionBar();
        mActionBar.setIcon(android.R.color.transparent);
        mStatusView = (TextView) findViewById(R.id.status_text);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initActionBarViews();
        setTitle("2016年");
        setSubtitle("周二");
        setMainTitle("7月");
    }

    private void initActionBarViews() {
        float textSize = 12 * getResources().getDisplayMetrics().density / 2;
        mHomeIv = getHomeView();
        mUpIv = getUpView();
        mTitleTv = getTitleView();
        mSubtiltleTv = getSubtitleView();
        mTitleTv.setTextColor(Color.WHITE);
        mSubtiltleTv.setTextColor(Color.WHITE);
        mTitleTv.setTextSize(textSize);
        mSubtiltleTv.setTextSize(textSize);
    }

    public Bitmap generateTitle(String title) {
        String mStr = title;
        Rect mBound = new Rect();
        TextPaint mPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(48 * getResources().getDisplayMetrics().density / 2);
        mPaint.setColor(Color.WHITE);
        mPaint.getTextBounds(mStr, 0, mStr.length(), mBound);
        int width = (int) (mBound.width() * 1.5f);
        int height = (int) (mBound.height() * 1.5f);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(bitmap);
        mCanvas.drawText(mStr, 0, mStr.length(), (width - mBound.width()) / 2, (height + mBound.height()) / 2, mPaint);
        return bitmap;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                //nothing
                getActionBar().setDisplayShowHomeEnabled(true);
                return true;
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                getActionBar().setDisplayShowHomeEnabled(false);
                return true;
            }
        });
        mSearchView = (SearchView) searchItem.getActionView();
        SearchViewStyle.on(mSearchView)
                .setCursorColor(Color.YELLOW)
                .setTextColor(Color.BLACK)
                .setHintTextColor(Color.GRAY)
                .setSearchButtonImageResource(android.R.drawable.ic_menu_save)
                .setCloseBtnImageResource(android.R.drawable.ic_menu_close_clear_cancel)
                .setGoBtnImageResource(android.R.drawable.ic_menu_search)
                .setCommitIcon(android.R.drawable.ic_menu_search)
                .setSearchPlateDrawableId(R.drawable.bg_searchview);
        setupSearchView(searchItem);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                getHomeView().setVisibility(View.GONE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupSearchView(MenuItem searchItem) {

        if (isAlwaysExpanded()) {
            mSearchView.setIconifiedByDefault(false);
        } else {
            searchItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM
                    | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        }

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            List<SearchableInfo> searchables = searchManager.getSearchablesInGlobalSearch();

            SearchableInfo info = searchManager.getSearchableInfo(getComponentName());
            /*for (SearchableInfo inf : searchables) {
                if (inf.getSuggestAuthority() != null
                        && inf.getSuggestAuthority().startsWith("applications")) {
                    info = inf;
                }
            }*/
            mSearchView.setSearchableInfo(info);
        }

        mSearchView.setOnQueryTextListener(this);
    }

    public boolean onQueryTextChange(String newText) {
        mStatusView.setText("Query = " + newText);
        return false;
    }

    public boolean onQueryTextSubmit(String query) {
        mStatusView.setText("Query = " + query + " : submitted");
        return false;
    }

    public boolean onClose() {
        mStatusView.setText("Closed!");
        return false;
    }

    protected boolean isAlwaysExpanded() {
        return false;
    }

    @Override
    public void setTitle(CharSequence title) {
        if (mActionBar != null) {
            mActionBar.setTitle(title);
        }
    }

    public void setSubtitle(String subTitle) {
        if (mActionBar != null) {
            mActionBar.setSubtitle(subTitle);
        }
    }

    public void setMainTitle(String title) {
        if (mHomeIv != null) {
            mHomeIv.setImageBitmap(generateTitle(title));
        }
    }

    public TextView getTitleView() {
        int abTitleId = getResources().getIdentifier("action_bar_title", "id", "android");
        return (TextView) findViewById(abTitleId);
    }

    public TextView getSubtitleView() {
        int abTitleId = getResources().getIdentifier("action_bar_subtitle", "id", "android");
        return (TextView) findViewById(abTitleId);
    }

    public ImageView getUpView() {
        int abTitleId = getResources().getIdentifier("up", "id", "android");
        return (ImageView) findViewById(abTitleId);
    }

    public ImageView getHomeView() {
        int abTitleId = getResources().getIdentifier("home", "id", "android");
        return (ImageView) findViewById(abTitleId);
    }

    public ViewGroup getActionBarView() {
        Window window = getWindow();
        View v = window.getDecorView();
        int resId = getResources().getIdentifier("action_bar_container", "id", "android");
        return (ViewGroup) v.findViewById(resId);
    }
}
