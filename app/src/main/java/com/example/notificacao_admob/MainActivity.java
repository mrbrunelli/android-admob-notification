package com.example.notificacao_admob;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class MainActivity extends AppCompatActivity {

    Button btnNotificacao, btnPublicidade, btnVideo;
    AdView adView;
    InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adView = findViewById(R.id.adView);
        AdRequest request = new AdRequest.Builder().build();
        adView.loadAd(request);
        criarPublicidade();
        criarPubVideo();
        btnPublicidade = findViewById(R.id.btnPublicidade);
        btnVideo = findViewById(R.id.btnVideo);
        btnNotificacao = findViewById(R.id.btnNotificacao);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) { }
        });

        btnNotificacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                criarNotificacao("Aula Android - Notificacao");
            }
        });

        btnPublicidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interstitialAd.show();
            }
        });

        btnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { }
        });
    }

    private void criarPublicidade() {
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        interstitialAd.loadAd(new AdRequest.Builder().build());
    }

    private void criarPubVideo() {}

    private void criarNotificacao(String texto) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "canal_1";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    "Notificacoes",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Descricao do canal");
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.setVibrationPattern(new long[] {0, 1000, 500, 1000});
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder notificacao = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificacao
            .setContentTitle("Titulo da notificacao")
            .setContentText(texto)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setTicker("Notificacao")
            .setWhen(System.currentTimeMillis())
            .setDefaults(Notification.DEFAULT_ALL)
            .setContentInfo("Info")
            .setAutoCancel(true);
        notificationManager.notify(1, notificacao.build());
    }
}