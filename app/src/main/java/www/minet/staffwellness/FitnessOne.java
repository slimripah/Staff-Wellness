package www.minet.staffwellness;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Pair;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class FitnessOne extends AppCompatActivity {

    Button download;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fitness_one);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        download = findViewById(R.id.btn_download);

        download.setOnClickListener(v -> {

            String googleFitPackage = "com.google.android.apps.fitness";
            boolean isInstalled = false;

            // Check against list of installed packages
            List<PackageInfo> packages = getPackageManager().getInstalledPackages(0);
            for (PackageInfo packageInfo : packages) {
                if (packageInfo.packageName.equals(googleFitPackage)) {
                    isInstalled = true;
                    break;
                }
            }

            if (isInstalled) {
                // Proceed to next activity
                Intent intent = new Intent(FitnessOne.this, FitnessTwo.class);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<>(findViewById(R.id.btn_download), "fit");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(FitnessOne.this, pairs);
                startActivity(intent, options.toBundle());
            } else {
                // Open Google Fit in Play Store
                Intent playStoreIntent = new Intent(Intent.ACTION_VIEW);
                playStoreIntent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + googleFitPackage));
                playStoreIntent.setPackage("com.android.vending");
                startActivity(playStoreIntent);
            }

        });

    }

}