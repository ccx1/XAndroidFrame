package com.android.common.helper;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.android.common.base.SupportActivity;
import com.android.common.base.SupportFragment;
import com.android.common.manager.FragmentManager;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author ccx
 * @date 2018/11/15
 */
public class TransactionDelegate {


    static final String FRAGMENTATION_ARG_CONTAINER = "fragmentation_arg_container";
    private final SupportActivity support;
    private final FragmentActivity mActivity;


    public TransactionDelegate(SupportActivity supportActivity) {
        this.support = supportActivity;
        this.mActivity = (FragmentActivity) support;
    }

    @SuppressLint("CheckResult")
    public void dispatchStartTransaction(final androidx.fragment.app.FragmentManager fragmentManager, final SupportFragment fragment, final boolean closeCurrent) {
        Schedulers.io().createWorker().schedule(new Runnable() {
            @Override
            public void run() {
                SupportFragment topFragment = FragmentManager.getInstance().getTopFragment();
                int anInt = getContainerId((Fragment) topFragment);
                fragmentManager.beginTransaction().add(anInt, (Fragment) fragment, fragment.getClass().getName()).commit();
                // 每一级都需要存储容器值
                bindContainerId(anInt, fragment);
                FragmentManager.getInstance().pushOneFragment(fragment);
                if (closeCurrent) {
                    fragmentManager.beginTransaction().remove((Fragment) topFragment).commit();
                    FragmentManager.getInstance().PopOneFragment(topFragment);
                }
            }
        });
    }

    private int getContainerId(Fragment topFragment) {
        Bundle arguments = topFragment.getArguments();
        assert arguments != null;
        return arguments.getInt(FRAGMENTATION_ARG_CONTAINER);
    }

    @SuppressLint("CheckResult")
    public void loadRootTransaction(final androidx.fragment.app.FragmentManager supportFragmentManager, final int containerId, final SupportFragment tofragment, final String name) {
        Schedulers.io().createWorker().schedule(new Runnable() {
            @Override
            public void run() {
                // 重新查找fragment，activity重启的时候，会重新调用此方法，而此时，不要进行覆盖处理
                bindContainerId(containerId, tofragment);
                supportFragmentManager.beginTransaction().add(containerId, (Fragment) tofragment, name).commit();
                FragmentManager.getInstance().pushOneFragment(tofragment);
                // 由于被销毁一次。所以，需要重新绑定数据
            }
        });
    }

    private void bindContainerId(int containerId, SupportFragment tofragment) {
        if (tofragment != null) {
            Bundle arguments = getArguments((Fragment) tofragment);
            if (arguments != null) {
                arguments.putInt(FRAGMENTATION_ARG_CONTAINER, containerId);
            }
        }
    }


    private Bundle getArguments(Fragment fragment) {
        Bundle bundle = fragment.getArguments();
        if (bundle == null) {
            bundle = new Bundle();
            fragment.setArguments(bundle);
        }
        return bundle;
    }


    @SuppressLint("CheckResult")
    public void pop(final androidx.fragment.app.FragmentManager fragmentManager) {
        Schedulers.io().createWorker().schedule(new Runnable() {
            @Override
            public void run() {
                SupportFragment topFragment = FragmentManager.getInstance().getTopFragment();
                try {
                    fragmentManager.beginTransaction().remove((Fragment) topFragment).commit();
                    FragmentManager.getInstance().PopOneFragment(topFragment);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void popTo(final Class<?> targetFragment, final boolean includeTargetFragment, final androidx.fragment.app.FragmentManager supportFragmentManager) {
        Schedulers.io().createWorker().schedule(new Runnable() {
            @Override
            public void run() {
                String name = targetFragment.getName();
                safePopTo(name, includeTargetFragment, supportFragmentManager);
            }
        });
    }


    @SuppressLint("CheckResult")
    private void safePopTo(String name, boolean includeTargetFragment, final androidx.fragment.app.FragmentManager supportFragmentManager) {
        if (supportFragmentManager == null) {
            return;
        }
        Fragment fragment = supportFragmentManager.findFragmentByTag(name);
        if (fragment == null) {
            return;
        }
        // 获取除了目标的栈上部fragment
        List<SupportFragment> willPopFragments = FragmentManager.getInstance().getWillPopFragments(supportFragmentManager, name, includeTargetFragment);
        if (willPopFragments.size() == 0) {
            return;
        }

        Fragment topFragment = (Fragment) willPopFragments.get(0);
        View container = findContainerById(topFragment, getContainerId(topFragment));
        if (container == null) {
            return;
        }
        // 得到需要关闭的fragment.进行安全关闭
        View fragmentView = topFragment.getView();
        if (fragmentView == null) {
            return;
        }
        Animation exitAnimation = support.getSupportDelegate().getExitAnimation();
        run(supportFragmentManager, willPopFragments, container, fragmentView, exitAnimation);

    }

    private void run(final androidx.fragment.app.FragmentManager supportFragmentManager, final List<SupportFragment> willPopFragments, final View container, final View fragmentView, final Animation exitAnimation) {
        Observable.just(container)
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<View, ViewGroup>() {
                    @Override
                    public ViewGroup apply(View view) throws Exception {
                        ((ViewGroup) view).removeViewInLayout(view);
                        return addMockView(fragmentView, (ViewGroup) view);
                    }
                })
                .observeOn(Schedulers.io())
                .map(new Function<ViewGroup, ViewGroup>() {

                    @Override
                    public ViewGroup apply(final ViewGroup view) throws Exception {
                        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
                        for (SupportFragment willPopFragment : willPopFragments) {
                            fragmentTransaction.remove((Fragment) willPopFragment);
                            FragmentManager.getInstance().PopOneFragment(willPopFragment);
                        }
                        fragmentTransaction.commitAllowingStateLoss();
                        // 让动画只执行一次。不能执行多次动画
                        // 当执行动画的时候，出现timeout。mock数据还会存在显影bug。
                        // 此时会出现timeout的bug。
                        view.startAnimation(exitAnimation);
                        return view;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .timeout(exitAnimation.getDuration(), TimeUnit.MILLISECONDS)
                .subscribe(new Observer<ViewGroup>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ViewGroup viewGroup) {
                        // 无论如何都要消失
                        Animation animation = viewGroup.getAnimation();
                        animation.cancel();
                        viewGroup.removeAllViews();
                        viewGroup.setVisibility(View.GONE);
                        ((ViewGroup) container).removeView(viewGroup);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // 无论如何都要消失
                        View mock = container.findViewWithTag("mock");
                        ((ViewGroup) mock).removeAllViews();
                        mock.setVisibility(View.GONE);
                        ((ViewGroup) container).removeView(mock);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 临时幕布
     *
     * @param view        幕布内容
     * @param containerId 幕布容器
     * @return
     */
    private ViewGroup addMockView(final View view, final ViewGroup containerId) {
        ViewGroup mock = new ViewGroup(mActivity) {
            @Override
            protected void onLayout(boolean changed, int l, int t, int r, int b) {
            }
        };
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }

        mock.addView(view);
        containerId.addView(mock);
        mock.setTag("mock");
        return mock;
    }

    private ViewGroup findContainerById(Fragment fragment, int containerId) {
        if (fragment.getView() == null) {
            return null;
        }

        View container;
        Fragment parentFragment = fragment.getParentFragment();
        if (parentFragment != null) {
            if (parentFragment.getView() != null) {
                container = parentFragment.getView().findViewById(containerId);
            } else {
                container = findContainerById(parentFragment, containerId);
            }
        } else {
            container = mActivity.findViewById(containerId);
        }

        if (container instanceof ViewGroup) {
            return (ViewGroup) container;
        }

        return null;
    }

    public void dispatchBackPressedEvent() {
        // 如果栈内有fragment，则操作fragment的回调事件，如果没有则操作activity的事件
        SupportFragment topFragment = FragmentManager.getInstance().getTopFragment();
        // 如果栈顶fragment== null。则调用Activity的Fragment
        if (topFragment == null) {
            support.onSupportBackPressed();
            return;
        }
        // 如果为true，则消费此事件。如果为false，则不消费，回传activity
        boolean b = topFragment.onSupportBackPressed();
        if (b) {
            return;
        }
        support.onSupportBackPressed();
    }


}
