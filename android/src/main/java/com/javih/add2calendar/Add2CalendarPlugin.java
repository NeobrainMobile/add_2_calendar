package com.javih.add2calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** Add2CalendarPlugin */
public class Add2CalendarPlugin implements MethodCallHandler {
    private final Registrar mRegistrar;
/** Add2CalendarPlugin **/
public class Add2CalendarPlugin implements MethodCallHandler, FlutterPlugin, ActivityAware {

    public Add2CalendarPlugin(Registrar registrar) {
        mRegistrar = registrar;
    }
    private MethodChannel channel;
    private Activity activity;
    private Context context;

    /** Plugin registration. */
    /** backward compatibility with embedding v1 **/
    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "flutter.javih.com/add_2_calendar");
        channel.setMethodCallHandler(new Add2CalendarPlugin(registrar));
        Add2CalendarPlugin plugin = new Add2CalendarPlugin();
        plugin.activity = registrar.activity();
        plugin.context = registrar.context();
        plugin.setupMethodChannel(registrar.messenger());
    }

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
        context = binding.getApplicationContext();
        setupMethodChannel(binding.getBinaryMessenger());
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
        channel = null;
    }

    @Override
    public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
        activity = binding.getActivity();
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {
        activity = null;
    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
        activity = binding.getActivity();
    }

    @Override
    public void onDetachedFromActivity() {
        activity = null;
    }

    private void setupMethodChannel(BinaryMessenger messenger) {
        channel = new MethodChannel(messenger, "flutter.javih.com/add_2_calendar");
        channel.setMethodCallHandler(this);
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {
    public void onMethodCall(MethodCall call, @NonNull Result result) {
        if (call.method.equals("add2Cal")) {
            try {
                insert((String) call.argument("title"), (String) call.argument("desc"), (String) call.argument("location"), (long) call.argument("startDate"), (long) call.argument("endDate"), (String) call.argument("timeZone"), (boolean) call.argument("allDay"));
@@ -40,8 +85,8 @@ public void onMethodCall(MethodCall call, Result result) {
    }

    @SuppressLint("NewApi")
    public void insert(String title, String desc, String loc, long start, long end, String timeZone, boolean allDay) {
        Context context = getActiveContext();
    private void insert(String title, String desc, String loc, long start, long end, String timeZone, boolean allDay) {
        Context mContext = activity != null ? activity : context;
        Intent intent = new Intent(Intent.ACTION_INSERT, CalendarContract.Events.CONTENT_URI);
        intent.putExtra(CalendarContract.Events.TITLE, title);
        intent.putExtra(CalendarContract.Events.DESCRIPTION, desc);
@@ -51,10 +96,7 @@ public void insert(String title, String desc, String loc, long start, long end,
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, start);
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end);
        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, allDay);
        context.startActivity(intent);
    }

    private Context getActiveContext() {
        return (mRegistrar.activity() != null) ? mRegistrar.activity() : mRegistrar.context();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }
}