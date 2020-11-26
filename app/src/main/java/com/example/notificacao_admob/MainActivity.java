package com.example.notificacao_admob;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class MainActivity extends AppCompatActivity {

    Button btnNotificacao, btnPublicidade, btnVideo;
    AdView adView;
    InterstitialAd interstitialAd;
    RewardedAd rewardedAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adView = findViewById(R.id.adView);
        btnPublicidade = findViewById(R.id.btnPublicidade);
        btnVideo = findViewById(R.id.btnVideo);
        btnNotificacao = findViewById(R.id.btnNotificacao);
        AdRequest request = new AdRequest.Builder().build();
        adView.loadAd(request);
        criarPubVideo();
        criarPublicidade();

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
            public void onClick(View view) {
                RewardedAdCallback adCallback = new RewardedAdCallback() {
                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                        Toast.makeText(
                                MainActivity.this,
                                "Viu ate o final. Ganhou o premio",
                                Toast.LENGTH_SHORT
                        ).show();
                    }

                    @Override
                    public void onRewardedAdOpened() {
                        super.onRewardedAdOpened();
                        Toast.makeText(
                                MainActivity.this,
                                "Video foi aberto",
                                Toast.LENGTH_SHORT
                        ).show();
                    }

                    @Override
                    public void onRewardedAdClosed() {
                        super.onRewardedAdClosed();
                        Toast.makeText(
                                MainActivity.this,
                                "Video foi fechado",
                                Toast.LENGTH_SHORT
                        ).show();
                    }

                    @Override
                    public void onRewardedAdFailedToShow(AdError adError) {
                        super.onRewardedAdFailedToShow(adError);
                        Toast.makeText(
                                MainActivity.this,
                                "Falhou ao abrir",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                };
                rewardedAd.show(MainActivity.this, adCallback);
            }
        });
    }

    private void criarPublicidade() {
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        interstitialAd.loadAd(new AdRequest.Builder().build());
    }

    private void criarPubVideo() {
        rewardedAd = new RewardedAd(this, "ca-app-pub-3940256099942544/5224354917");
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                super.onRewardedAdLoaded();
                Toast.makeText(
                        MainActivity.this,
                        "Video carregado com sucesso.",
                        Toast.LENGTH_SHORT
                ).show();
            }

            @Override
            public void onRewardedAdFailedToLoad(LoadAdError loadAdError) {
                super.onRewardedAdFailedToLoad(loadAdError);
                Toast.makeText(
                        MainActivity.this,
                        "Erro ao carregar o video",
                        Toast.LENGTH_SHORT
                ).show();
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
    }

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