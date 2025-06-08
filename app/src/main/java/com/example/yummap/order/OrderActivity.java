package com.example.yummap.order;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.example.yummap.R;
import com.example.yummap.utils.FunctionHelper;
import com.google.android.material.button.MaterialButton;

import java.util.HashMap;
import java.util.Map;

public class OrderActivity extends AppCompatActivity {

    public static final String DATA_TITLE = "TITLE";
    private String strTitle;
    private int paket1, paket2, paket3, paket4, paket5, paket6;
    private int itemCount1 = 0, itemCount2 = 0, itemCount3 = 0, itemCount4 = 0, itemCount5 = 0, itemCount6 = 0;
    private int countP1, countP2, countP3, countP4, countP5, countP6, totalItems, totalPrice;
    private ImageView imageAdd1, imageAdd2, imageAdd3, imageAdd4, imageAdd5, imageAdd6,
            imageMinus1, imageMinus2, imageMinus3, imageMinus4, imageMinus5, imageMinus6,
            imageView1, imageView2, imageView3, imageView4, imageView5, imageView6;
    private Toolbar toolbar;
    private TextView tvPaket1, tvPaket2, tvPaket3, tvPaket4, tvPaket5, tvPaket6,
            tvJumlahPorsi, tvTotalPrice, tvPrice1, tvPrice2, tvPrice3, tvPrice4, tvPrice5, tvPrice6;
    private MaterialButton btnCheckout;
    private OrderViewModel orderViewModel;
    private String menuName1, menuName2, menuName3, menuName4, menuName5, menuName6;

    // Define menu configurations for each category
    private static final Map<String, MenuItem[]> MENU_CONFIG = new HashMap<>();

    static {
        MENU_CONFIG.put("Complete Package", new MenuItem[]{
                new MenuItem("Fried Chicken Bento", 25000, R.drawable.fried_chicken_bento),
                new MenuItem("Chicken Teriyaki Bento", 30000, R.drawable.chicken_teriyaki_bento),
                new MenuItem("Beef Teriyaki Bento", 28000, R.drawable.beef_teriyaki_bento),
                new MenuItem("Mushroom Bento", 27000, R.drawable.mushrom_bento),
                new MenuItem("Seafood Bento", 26000, R.drawable.seafood_bento),
                new MenuItem("Beef Pepper Rice", 29000, R.drawable.beef_pepper_rice)
        });
        MENU_CONFIG.put("Saving Package", new MenuItem[]{
                new MenuItem("Udang Keju", 10000, R.drawable.udang_keju),
                new MenuItem("Onigiri", 10000, R.drawable.onigiri),
                new MenuItem("Udang Rambutan", 10000, R.drawable.udang_rambutan),
                new MenuItem("Risol Mayo", 5000, R.drawable.risol_mayo),
                new MenuItem("Pisang Coklat", 10000, R.drawable.pisang_coklat),
                new MenuItem("Takoyaki", 10000, R.drawable.takoyaki)
        });
        MENU_CONFIG.put("Healthy Package", new MenuItem[]{
                new MenuItem("Kebab", 13000, R.drawable.kebab),
                new MenuItem("Sandwich", 19000, R.drawable.sandwich),
                new MenuItem("Pasta Salad", 25000, R.drawable.pasta_salad),
                new MenuItem("Salad Sayur", 23000, R.drawable.salad_sayur),
                new MenuItem("Chicken Roll", 18500, R.drawable.chicken_rpr),
                new MenuItem("Shrimp Roll", 19500, R.drawable.shrip_rpr)
        });
        MENU_CONFIG.put("FastFood", new MenuItem[]{
                new MenuItem("Burger", 23000, R.drawable.burger),
                new MenuItem("Chicken Nuggets", 18000, R.drawable.nugget),
                new MenuItem("French Fries", 16000, R.drawable.kentang),
                new MenuItem("Pizza Mini", 15500, R.drawable.pizza),
                new MenuItem("Samyang Roll", 14500, R.drawable.samyang),
                new MenuItem("Chicken Karage", 17500, R.drawable.karage)
        });
        MENU_CONFIG.put("Event Packages", new MenuItem[]{
                new MenuItem("Tiramisu", 20000, R.drawable.tiramisu),
                new MenuItem("Brownies", 20000, R.drawable.brownies),
                new MenuItem("Cromboloni", 18000, R.drawable.cromboloni),
                new MenuItem("Cupcake", 18000, R.drawable.cupcake),
                new MenuItem("Mochi", 12000, R.drawable.mochi),
                new MenuItem("Pudding", 12000, R.drawable.pudding)
        });
        MENU_CONFIG.put("Others", new MenuItem[]{
                new MenuItem("Corndog", 22000, R.drawable.corndog),
                new MenuItem("Mini Sandwich", 23000, R.drawable.mini_sandwich),
                new MenuItem("Toppoki", 21000, R.drawable.toppoki),
                new MenuItem("Tamago", 24000, R.drawable.tamago),
                new MenuItem("Roti Bakar", 20000, R.drawable.roti),
                new MenuItem("Kimbab", 22500, R.drawable.kimbab)
        });
    }

    // Helper class to store menu item details
    private static class MenuItem {
        String name;
        int price;
        int imageResId;

        MenuItem(String name, int price, int imageResId) {
            this.name = name;
            this.price = price;
            this.imageResId = imageResId;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        setStatusbar();
        setInitLayout();
        setPaket1();
        setPaket2();
        setPaket3();
        setPaket4();
        setPaket5();
        setPaket6();
        setInputData();
    }

    private void setInitLayout() {
        // Initialize views
        toolbar = findViewById(R.id.toolbar);
        tvPaket1 = findViewById(R.id.tvPaket1);
        tvPaket2 = findViewById(R.id.tvPaket2);
        tvPaket3 = findViewById(R.id.tvPaket3);
        tvPaket4 = findViewById(R.id.tvPaket4);
        tvPaket5 = findViewById(R.id.tvPaket5);
        tvPaket6 = findViewById(R.id.tvPaket6);
        tvJumlahPorsi = findViewById(R.id.tvJumlahPorsi);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvPrice1 = findViewById(R.id.tvPrice1);
        tvPrice2 = findViewById(R.id.tvPrice2);
        tvPrice3 = findViewById(R.id.tvPrice3);
        tvPrice4 = findViewById(R.id.tvPrice4);
        tvPrice5 = findViewById(R.id.tvPrice5);
        tvPrice6 = findViewById(R.id.tvPrice6);
        imageAdd1 = findViewById(R.id.imageAdd1);
        imageAdd2 = findViewById(R.id.imageAdd2);
        imageAdd3 = findViewById(R.id.imageAdd3);
        imageAdd4 = findViewById(R.id.imageAdd4);
        imageAdd5 = findViewById(R.id.imageAdd5);
        imageAdd6 = findViewById(R.id.imageAdd6);
        imageMinus1 = findViewById(R.id.imageMinus1);
        imageMinus2 = findViewById(R.id.imageMinus2);
        imageMinus3 = findViewById(R.id.imageMinus3);
        imageMinus4 = findViewById(R.id.imageMinus4);
        imageMinus5 = findViewById(R.id.imageMinus5);
        imageMinus6 = findViewById(R.id.imageMinus6);
        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);
        imageView5 = findViewById(R.id.imageView5);
        imageView6 = findViewById(R.id.imageView6);
        btnCheckout = findViewById(R.id.btnCheckout);

        strTitle = getIntent().getStringExtra(DATA_TITLE);
        if (strTitle == null || !MENU_CONFIG.containsKey(strTitle)) {
            Toast.makeText(this, "Invalid menu category!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(strTitle);
        }

        // Set menu items from configuration
        MenuItem[] menuItems = MENU_CONFIG.get(strTitle);
        if (menuItems != null) {
            // Package 1
            if (menuItems.length > 0) {
                paket1 = menuItems[0].price;
                menuName1 = menuItems[0].name;
                tvPrice1.setText("Rp " + paket1);
                imageView1.setImageResource(menuItems[0].imageResId);
            }

            // Package 2
            if (menuItems.length > 1) {
                paket2 = menuItems[1].price;
                menuName2 = menuItems[1].name;
                tvPrice2.setText("Rp " + paket2);
                imageView2.setImageResource(menuItems[1].imageResId);
            }

            // Package 3
            if (menuItems.length > 2) {
                paket3 = menuItems[2].price;
                menuName3 = menuItems[2].name;
                tvPrice3.setText("Rp " + paket3);
                imageView3.setImageResource(menuItems[2].imageResId);
            }

            // Package 4
            if (menuItems.length > 3) {
                paket4 = menuItems[3].price;
                menuName4 = menuItems[3].name;
                tvPrice4.setText("Rp " + paket4);
                imageView4.setImageResource(menuItems[3].imageResId);
            }

            // Package 5
            if (menuItems.length > 4) {
                paket5 = menuItems[4].price;
                menuName5 = menuItems[4].name;
                tvPrice5.setText("Rp " + paket5);
                imageView5.setImageResource(menuItems[4].imageResId);
            }

            // Package 6
            if (menuItems.length > 5) {
                paket6 = menuItems[5].price;
                menuName6 = menuItems[5].name;
                tvPrice6.setText("Rp " + paket6);
                imageView6.setImageResource(menuItems[5].imageResId);
            }
        }

        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
    }

    private void setPaket1() {
        imageAdd1.setOnClickListener(v -> {
            itemCount1++;
            tvPaket1.setText(String.valueOf(itemCount1));
            countP1 = paket1 * itemCount1;
            setTotalPrice();
        });

        imageMinus1.setOnClickListener(v -> {
            if (itemCount1 > 0) {
                itemCount1--;
                tvPaket1.setText(String.valueOf(itemCount1));
            }
            countP1 = paket1 * itemCount1;
            setTotalPrice();
        });
    }

    private void setPaket2() {
        imageAdd2.setOnClickListener(v -> {
            itemCount2++;
            tvPaket2.setText(String.valueOf(itemCount2));
            countP2 = paket2 * itemCount2;
            setTotalPrice();
        });

        imageMinus2.setOnClickListener(v -> {
            if (itemCount2 > 0) {
                itemCount2--;
                tvPaket2.setText(String.valueOf(itemCount2));
            }
            countP2 = paket2 * itemCount2;
            setTotalPrice();
        });
    }

    private void setPaket3() {
        imageAdd3.setOnClickListener(v -> {
            itemCount3++;
            tvPaket3.setText(String.valueOf(itemCount3));
            countP3 = paket3 * itemCount3;
            setTotalPrice();
        });

        imageMinus3.setOnClickListener(v -> {
            if (itemCount3 > 0) {
                itemCount3--;
                tvPaket3.setText(String.valueOf(itemCount3));
            }
            countP3 = paket3 * itemCount3;
            setTotalPrice();
        });
    }

    private void setPaket4() {
        imageAdd4.setOnClickListener(v -> {
            itemCount4++;
            tvPaket4.setText(String.valueOf(itemCount4));
            countP4 = paket4 * itemCount4;
            setTotalPrice();
        });

        imageMinus4.setOnClickListener(v -> {
            if (itemCount4 > 0) {
                itemCount4--;
                tvPaket4.setText(String.valueOf(itemCount4));
            }
            countP4 = paket4 * itemCount4;
            setTotalPrice();
        });
    }

    private void setPaket5() {
        imageAdd5.setOnClickListener(v -> {
            itemCount5++;
            tvPaket5.setText(String.valueOf(itemCount5));
            countP5 = paket5 * itemCount5;
            setTotalPrice();
        });

        imageMinus5.setOnClickListener(v -> {
            if (itemCount5 > 0) {
                itemCount5--;
                tvPaket5.setText(String.valueOf(itemCount5));
            }
            countP5 = paket5 * itemCount5;
            setTotalPrice();
        });
    }

    private void setPaket6() {
        imageAdd6.setOnClickListener(v -> {
            itemCount6++;
            tvPaket6.setText(String.valueOf(itemCount6));
            countP6 = paket6 * itemCount6;
            setTotalPrice();
        });

        imageMinus6.setOnClickListener(v -> {
            if (itemCount6 > 0) {
                itemCount6--;
                tvPaket6.setText(String.valueOf(itemCount6));
            }
            countP6 = paket6 * itemCount6;
            setTotalPrice();
        });
    }

    private void setTotalPrice() {
        totalItems = itemCount1 + itemCount2 + itemCount3 + itemCount4 + itemCount5 + itemCount6;
        totalPrice = countP1 + countP2 + countP3 + countP4 + countP5 + countP6;

        tvJumlahPorsi.setText(totalItems + " items");
        tvTotalPrice.setText(FunctionHelper.rupiahFormat(totalPrice));
    }

    private void setInputData() {
        btnCheckout.setOnClickListener(v -> {
            if (totalItems == 0 || totalPrice == 0) {
                Toast.makeText(OrderActivity.this, "Ups, pilih menu makanan dulu!",
                        Toast.LENGTH_SHORT).show();
            } else if (totalItems < 1) {
                Toast.makeText(OrderActivity.this, "Ups, minimal 1 pesanan!",
                        Toast.LENGTH_SHORT).show();
            } else {
                // Save each non-zero item as a separate order
                if (itemCount1 > 0) {
                    orderViewModel.addDataOrder(menuName1, itemCount1, countP1);
                }
                if (itemCount2 > 0) {
                    orderViewModel.addDataOrder(menuName2, itemCount2, countP2);
                }
                if (itemCount3 > 0) {
                    orderViewModel.addDataOrder(menuName3, itemCount3, countP3);
                }
                if (itemCount4 > 0) {
                    orderViewModel.addDataOrder(menuName4, itemCount4, countP4);
                }
                if (itemCount5 > 0) {
                    orderViewModel.addDataOrder(menuName5, itemCount5, countP5);
                }
                if (itemCount6 > 0) {
                    orderViewModel.addDataOrder(menuName6, itemCount6, countP6);
                }
                Toast.makeText(OrderActivity.this,
                        "Yeay! Pesanan Anda sedang diproses, cek di menu riwayat ya!",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    public void setStatusbar() {
        if (Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public static void setWindowFlag(@NonNull Activity activity, final int bits, boolean on) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        if (on) {
            layoutParams.flags |= bits;
        } else {
            layoutParams.flags &= ~bits;
        }
        window.setAttributes(layoutParams);
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}