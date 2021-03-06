package com.lth.antonlundborg.antonsprojekt;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.google.android.gms.maps.MapView;
import com.lth.antonlundborg.antonsprojekt.Charts.BalticSeaPortalFragment;
import com.lth.antonlundborg.antonsprojekt.Charts.DmiFragment;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private Drawer drawer;
    private int place = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initiateGoogleMaps();
        setupNavigationDrawer();

        fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, new HomeFragment());
            fragmentTransaction.commit();
        }
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)

                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(defaultOptions)

                .build();
        ImageLoader.getInstance().init(config);
        ImageLoader.getInstance().clearDiskCache();

    }

    private void initiateGoogleMaps() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MapView mv = new MapView(getApplicationContext());
                    mv.onCreate(null);
                    mv.onPause();
                    mv.onDestroy();
                } catch (Exception ignored) {

                }
            }
        }).start();
    }

    private void setupNavigationDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Drawable drawable = getResources().getDrawable(R.drawable.drawer_background);
        AccountHeader accountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(drawable)
                .build();
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withActionBarDrawerToggleAnimated(true)
                .withSliderBackgroundColorRes(R.color.slider_background)
                .withToolbar(toolbar)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_home),
                        new SectionDrawerItem().withName(R.string.drawer_item_charts),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_baltic),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_dmi),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_wind)
                )
                .withOnDrawerItemClickListener(new MyOnDrawerItemClickListener())
                .withAccountHeader(accountHeader)
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view) {

    }


    private class MyOnDrawerItemClickListener implements Drawer.OnDrawerItemClickListener {

        @Override
        public boolean onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {
            drawer.closeDrawer();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            Fragment fragment;
            if (i == 0) {
                fragment = new HomeFragment();
                transaction.replace(R.id.fragment_container, fragment);
            } else if (i == 2) {
                ImageLoader.getInstance().clearDiskCache();
                fragment = new BalticSeaPortalFragment();
                getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                transaction.replace(R.id.fragment_container, fragment);
            } else if (i == 3) {
                ImageLoader.getInstance().clearDiskCache();
                Bundle args = new Bundle();
                args.putInt("place", place);
                fragment = new DmiFragment();
                fragment.setArguments(args);
                transaction.replace(R.id.fragment_container, fragment);
            } else if (i == 5) {
                fragment = SmhiMapFragment.newInstance();
                transaction.replace(R.id.fragment_container, fragment);
            }
            transaction.addToBackStack(null);
            transaction.commit();
            return true;
        }
    }
}
