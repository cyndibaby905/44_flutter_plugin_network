package com.hangchen.flutter_plugin_network;

import android.util.Log;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/** FlutterPluginNetworkPlugin */
public class FlutterPluginNetworkPlugin implements MethodCallHandler {
  /** Plugin registration. */
  private Registrar registrar;
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "flutter_plugin_network");
    channel.setMethodCallHandler(new FlutterPluginNetworkPlugin(registrar));
  }

  public FlutterPluginNetworkPlugin(Registrar registrar) {
    this.registrar = registrar;
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
    if (call.method.equals("doRequest")) {
      HashMap param = call.argument("param");
      String url = call.argument("url");
      doRequest(url,param,result);
    } else {
      result.notImplemented();
    }
  }



  void doRequest(String url, HashMap<String, String> param, final Result result) {

    OkHttpClient client = new OkHttpClient();

    HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
    for (String key : param.keySet()) {
      String value = param.get(key);
      urlBuilder.addQueryParameter(key,value);
    }

    //加入自定义通用参数
    urlBuilder.addQueryParameter("ppp", "yyyy");

    String requestUrl = urlBuilder.build().toString();

    final Request request = new Request.Builder()
            .url(requestUrl)
            .build();

    client.newCall(request).enqueue(new Callback() {
      @Override
      public void onFailure(Call call, IOException e) {
        e.printStackTrace();
      }

      @Override
      public void onResponse(Call call, final Response response) throws IOException {
        if (!response.isSuccessful()) {
          final String content = "Unexpected code " + response;
          registrar.activity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
              result.error("Error", content, null);
            }
          });

        } else {
          final String content = response.body().string();
          registrar.activity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
              result.success(content);
            }
          });


        }
      }
    });


  }
}
