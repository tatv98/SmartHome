package nuce.tatv.smarthome.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import nuce.tatv.smarthome.Fragments.ControlFragment;
import nuce.tatv.smarthome.Fragments.DashboardFragment;
import nuce.tatv.smarthome.Fragments.SettingFragment;
import nuce.tatv.smarthome.Fragments.SocketFragment;
import nuce.tatv.smarthome.Model.Data;
import nuce.tatv.smarthome.R;

import static nuce.tatv.smarthome.Fragments.ControlFragment.TOPPIC;
import static nuce.tatv.smarthome.Fragments.DashboardFragment.gvRainSenser;
import static nuce.tatv.smarthome.Fragments.SocketFragment.listData;
import static nuce.tatv.smarthome.Fragments.SocketFragment.lvData;
import static nuce.tatv.smarthome.Fragments.SocketFragment.socketAdapter;

public class MainActivity extends AppCompatActivity {
    public static String USER_NAME  = null;
    public static String USER_EMAIL = null;
    private TextView tvUser, tvEmail;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private FragmentManager fragmentManager;
    private ControlFragment controlFragment;
    private DashboardFragment dashboardFragment;
    private SettingFragment settingFragment;
    private SocketFragment socketFragment;


    private SharedPreferences sharedPreferences;
    private String mqttServer, mqttPort, mqttUser, mqttPass;

    public static MqttAndroidClient client;
    private MqttConnectOptions options;
    private String clientId;

    private static final String TAG = "mqtt";
    public static boolean checkConnect;

    public static LineGraphSeries<DataPoint> series;
    public static int lastX = 0;

    public static boolean checkYellow;
    public static boolean checkBlue;
    public static boolean checkRed;
    public static boolean checkOut;
    public static boolean checkWarning;
    public static boolean checkSpeaker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        checkConnect = false;
        checkYellow = false;
        checkBlue = false;
        checkRed = false;
        checkWarning = false;
        checkSpeaker = false;
        checkOut = false;
        series = new LineGraphSeries<DataPoint>();
        toolbar = findViewById(R.id.toolbar);
        mDrawer = findViewById(R.id.drawer_layout);
        nvDrawer = findViewById(R.id.nav_view);

        View view  = nvDrawer.getHeaderView(0);
        tvUser = view.findViewById(R.id.tvUser);
        tvEmail = view.findViewById(R.id.tvEmail);

        Intent intent = getIntent();
        tvUser.setText(intent.getStringExtra("user"));
        tvEmail.setText(intent.getStringExtra("email"));


        setSupportActionBar(toolbar);
        drawerToggle = new ActionBarDrawerToggle(MainActivity.this, mDrawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nvDrawer.setItemIconTintList(null);
        // Setup drawer view
        setupDrawerContent(nvDrawer);
        //create
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction tranControl = fragmentManager.beginTransaction();
        controlFragment = new ControlFragment();
        tranControl.replace(R.id.flContent, controlFragment);
        tranControl.commit();


        sharedPreferences = getSharedPreferences("dataMQTT", Context.MODE_PRIVATE);
        mqttServer = sharedPreferences.getString("mqttServer", "");
        mqttPort = sharedPreferences.getString("mqttPort", "");
        mqttUser = sharedPreferences.getString("mqttUser", "");
        mqttPass = sharedPreferences.getString("mqttPass", "");

        listData = new ArrayList<>();
        if (mqttServer.equals("") || mqttPort.equals("") || mqttUser.equals("") || mqttPass.equals("")){
            Toast.makeText(this, "Bạn chưa cài đặt tài khoản MQTT, vui lòng vào phần Setting!", Toast.LENGTH_SHORT).show();
            FragmentTransaction tranSetting = fragmentManager.beginTransaction();
            settingFragment = new SettingFragment();
            tranSetting.replace(R.id.flContent, settingFragment);
            tranSetting.commit();
            mDrawer.closeDrawers();
        }else {

            options = new MqttConnectOptions();
            options.setUserName(mqttUser);
            options.setPassword(mqttPass.toCharArray());
            clientId = MqttClient.generateClientId();
//            connect();
        }

    }
    public void connect(){
        Log.d(TAG, "vào connect r");
        client = new MqttAndroidClient(getApplicationContext(), "tcp://" + mqttServer +":" + mqttPort, clientId);
        callback();
        try {
            Log.d(TAG, "vào try connect r");
            options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    checkConnect = true;
                    Log.d(TAG, "connect success");
                    subcribe(TOPPIC);
                    Toast.makeText(MainActivity.this, "Kết nối MQTT thành công!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d(TAG, "connect failure");
                    Toast.makeText(MainActivity.this, "Kết nối MQTT lỗi! vui lòng kiểm tra lại tài khoản", Toast.LENGTH_SHORT).show();

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    public static void disconnect(){
        try {
            IMqttToken disconToken = client.disconnect();
            disconToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // we are now successfully disconnected
                    checkConnect = false;
                    Log.d(TAG, "disconnect success");
                    listData.clear();
                    socketAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    // something went wrong, but probably we are disconnected anyway
                    Log.d(TAG, "disconnect failure");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    public static void publish(String topic, String payload){
        byte[] encodedPayload = new byte[0];
        try {
            encodedPayload = payload.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(encodedPayload);
            message.setRetained(true);
            client.publish(topic, message);
        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
        }
    }
    public static void subcribe(String topic){
        int qos = 1;
        try {
            IMqttToken subToken = client.subscribe(topic, qos);
            subToken.setActionCallback(new IMqttActionListener() {

                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // The message was published
                    Log.d(TAG, "subcribe success");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    // The subscription could not be performed, maybe the user was not
                    // authorized to subscribe on the specified topic e.g. using wildcards
                    Log.d(TAG, "subcribe failure");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    public static void unsubcribe(String topic){
        try {
            IMqttToken unsubToken = client.unsubscribe(topic);
            unsubToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // The subscription could successfully be removed from the client
                    Log.d(TAG, "unsubscribe success");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    // some error occurred, this is very unlikely as even if the client
                    // did not had a subscription to the topic the unsubscribe action
                    // will be successfully
                    Log.d(TAG, "unsubscribe failure");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    public void callback(){
        Log.d(TAG, "vào callback r");
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Toast.makeText(MainActivity.this, "Không thể kết nối MQTT", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Data data = new Data(topic, message.toString());
                Log.d(TAG, "callback - topic: " + data.getmTopic() + ", message: " + data.getmMessage());
                listData.add(data);
                Double rs = Double.parseDouble(data.getmMessage());
                if (rs <= 100 && rs >= 0 && data.getmTopic().equals(TOPPIC)){
                    Log.d(TAG, "thoa man dk");
                    series.appendData(new DataPoint(lastX++, rs), true, 10);
                    gvRainSenser.onDataChanged(false, false);
                }
                socketAdapter.notifyDataSetChanged();
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }

    public static LineGraphSeries<DataPoint> getSeries() {
        return series;
    }

    public static void setSeries(LineGraphSeries<DataPoint> series) {
        MainActivity.series = series;
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }



    public void selectDrawerItem(MenuItem menuItem) {
        switch(menuItem.getItemId()) {
            case R.id.nav_control:
                FragmentTransaction tranControl = fragmentManager.beginTransaction();
                controlFragment = new ControlFragment();
                tranControl.replace(R.id.flContent, controlFragment);
                tranControl.commit();
                menuItem.setChecked(true);
                mDrawer.closeDrawers();
                break;
            case R.id.nav_dashboard:
                FragmentTransaction tranDashboard = fragmentManager.beginTransaction();
                dashboardFragment = new DashboardFragment();
                tranDashboard.replace(R.id.flContent, dashboardFragment);
                tranDashboard.commit();
                menuItem.setChecked(true);
                mDrawer.closeDrawers();
                break;
            case R.id.nav_setting:
                FragmentTransaction tranSetting = fragmentManager.beginTransaction();
                settingFragment = new SettingFragment();
                tranSetting.replace(R.id.flContent, settingFragment);
                tranSetting.commit();
                menuItem.setChecked(true);
                mDrawer.closeDrawers();
                break;
            case R.id.nav_socketui:
                FragmentTransaction tranSocket = fragmentManager.beginTransaction();
                socketFragment = new SocketFragment();
                tranSocket.replace(R.id.flContent, socketFragment);
                tranSocket.commit();
                menuItem.setChecked(true);
                mDrawer.closeDrawers();
                break;
            case R.id.nav_share:
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Please install my application^^");
                shareIntent.setType("text/plain");
                startActivity(Intent.createChooser(shareIntent, "Share to:"));
                break;

            case R.id.nav_logout:
                logOut();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void logOut(){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
