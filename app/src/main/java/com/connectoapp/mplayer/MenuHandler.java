package com.connectoapp.mplayer;

import android.animation.ObjectAnimator;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public class MenuHandler {
    private static float current_x = 0;
    public static String current_page = "home";

    public static void initializeMenuItemClick(View view, int width, MainActivity mainActivity) {
        view.findViewById(R.id.m_home).setOnClickListener(l -> {
            if (!current_page.equals("home")) {
                mainActivity.handleMenuChange("home", current_page);
                current_page = "home";
            }
            toggle(view, width);

        });
        view.findViewById(R.id.m_search).setOnClickListener(l -> {
            if (!current_page.equals("search")) {
                mainActivity.handleMenuChange("search", current_page);
                current_page = "search";
            }
            toggle(view, width);

        });
        view.findViewById(R.id.m_downloads).setOnClickListener(l -> {
            if (!current_page.equals("downloads")) {
                mainActivity.handleMenuChange("downloads", current_page);
                current_page = "downloads";
            }
            toggle(view, width);
        });

    }

    private static void openMenu(View v, int w) {
        View view = v.findViewById(R.id.mainView);

        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "TranslationX", w * 75 / 100f);
        animator.start();
        current_x = w * 75 / 100f;
        v.findViewById(R.id.menu).setVisibility(View.VISIBLE);
        MainActivity.isMenuOpen = true;
    }

    private static void closeMenu(View v, int w) {
        View view = v.findViewById(R.id.mainView);
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "TranslationX", 0);
        animator.start();
        MainActivity.isMenuOpen = false;
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        v.findViewById(R.id.menu).setVisibility(View.INVISIBLE);
                    }
                }, animator.getDuration()
        );
        current_x = 0;
    }

    public static void toggle(View v, int w) {

        if (!MainActivity.isMenuOpen) {
            openMenu(v, w);
        } else {
            closeMenu(v, w);
        }
    }

    public static void handleSwipe(View view, float deltaX, int w) {
        current_x += deltaX;
        if (current_x < 0) current_x = 0;
        else if (current_x > w * 75 / 100f) current_x = w * 75 / 100f;
        view.findViewById(R.id.mainView).setX(current_x);
    }

    public static void Apply(View v, int w, float vel) {
        if (vel > 0) {
            if (current_x >= w * 30 / 100f || Math.abs(vel) > 150) {
                View view = v.findViewById(R.id.mainView);
                ObjectAnimator animator = ObjectAnimator.ofFloat(view, "TranslationX", w * 75 / 100f);
                float distance = (w * 75 / 100f) - current_x;
                animator.setDuration((long) Math.abs(((long) distance / vel)));
                animator.start();
                MainActivity.isMenuOpen = true;
            } else {
                View view = v.findViewById(R.id.mainView);
                ObjectAnimator animator = ObjectAnimator.ofFloat(view, "TranslationX", 0);
                animator.start();
                MainActivity.isMenuOpen = false;
            }
        } else {
            if (Math.abs(w * 75 / 100f - current_x) > w * 30 / 100f || Math.abs(vel) > 150) {
                View view = v.findViewById(R.id.mainView);
                ObjectAnimator animator = ObjectAnimator.ofFloat(view, "TranslationX", 0);
                float distance = current_x;
                animator.setDuration((long) Math.abs(((long) distance / vel)));
                animator.start();
                MainActivity.isMenuOpen = false;
                new Timer().schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
                                v.findViewById(R.id.menu).setVisibility(View.INVISIBLE);
                            }
                        }, animator.getDuration()
                );
            } else {
                View view = v.findViewById(R.id.mainView);
                ObjectAnimator animator = ObjectAnimator.ofFloat(view, "TranslationX", w * 75 / 100f);
                animator.start();
                MainActivity.isMenuOpen = true;
                new Timer().schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
                                v.findViewById(R.id.menu).setVisibility(View.INVISIBLE);
                            }
                        }, animator.getDuration()
                );
            }
        }
    }
}
