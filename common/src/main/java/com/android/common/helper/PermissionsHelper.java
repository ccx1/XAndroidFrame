package com.android.common.helper;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.android.common.model.Permission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;

/**
 * @author ccx
 * @date 2018/11/16
 */
public class PermissionsHelper {

    private static       Object                                  TRIGGER                  = new Object();
    private FragmentActivity mActivity;
    private final Fragment mFragment;
    private              Map<String, PublishSubject<Permission>> mSubjectMap              = new HashMap<>();
    private static final int                                     PERMISSIONS_REQUEST_CODE = 1002;

    public PermissionsHelper(FragmentActivity activity) {
        this(activity, null);
    }

    public PermissionsHelper(Fragment fragment) {
        this(null, fragment);
    }

    private PermissionsHelper(FragmentActivity activity, Fragment fragment) {
        this.mActivity = activity;
        this.mFragment = fragment;
        if (activity == null && fragment != null) {
            mActivity = fragment.getActivity();
        }
    }

    public Observable<Boolean> request(String... permissions) {
        // 发送
        // compose()
        // 是唯一一个能从流中获取原生Observable 的方法，
        // 因此，影响整个流的操作符（像subscribeOn()和observeOn()）需要使用compose()，
        // 相对的，如果你在flatMap()中使用subscribeOn()/observeOn()，
        // 它只影响你创建的flatMap()中的Observable,而不是整个流。
        return Observable.just(TRIGGER).compose(ensure(permissions));
    }

    private <T> ObservableTransformer<T, Boolean> ensure(final String... permissions) {
        // flatMap是请求转发，将原先的Observable.just(1),转发成Observable.just(true)
        // 通过类型转换，将其原本的int类型，转换成boolean类型
        return new ObservableTransformer<T, Boolean>() {
            @Override
            public ObservableSource<Boolean> apply(Observable<T> upstream) {
                return request(upstream, permissions)
                        .buffer(permissions.length)
                        .flatMap(new Function<List<Permission>, ObservableSource<Boolean>>() {
                            @Override
                            public ObservableSource<Boolean> apply(List<Permission> permissions) throws Exception {
                                if (permissions.isEmpty()) {
                                    return Observable.empty();
                                }
                                // 如果有一项不符合要求，则直接返回false。
                                // 运行时权限需要全部同意才能返回true
                                for (Permission p : permissions) {
                                    if (!p.isGranted()) {
                                        return Observable.just(false);
                                    }
                                }
                                return Observable.just(true);
                            }
                        });
            }
        };
    }

    private Observable<Permission> request(Observable<?> upstream, final String... permissions) {
        if (permissions == null || permissions.length == 0) {
            throw new IllegalArgumentException("permissions is null or length will 0");
        }
        // 合并成一个
        return oneOf(upstream, pending())
                .flatMap(new Function<Object, Observable<Permission>>() {
                    @Override
                    public Observable<Permission> apply(Object o) throws Exception {
                        return requestImplementation(permissions);
                    }
                });
    }

    @SuppressLint("CheckResult")
    @TargetApi(android.os.Build.VERSION_CODES.M)
    private Observable<Permission> requestImplementation(String... permissions) {

        List<Observable<Permission>> list = new ArrayList<>();
        // 没有被请求的对象
        List<String> unrequestedPermissions = new ArrayList<>();
        // 转发应该去需要请求
        for (String permission : permissions) {
            // 需要判断是否需要去请求权限
            if (mActivity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
                list.add(Observable.just(new Permission(permission, true)));
                continue;
            } else if (mActivity.getPackageManager().isPermissionRevokedByPolicy(permission, mActivity.getPackageName())) {
                // 是否是因为版本原因导致这个权限不需要被请求,大于24才需要
                list.add(Observable.just(new Permission(permission, false)));
                continue;
            }
            // 除去，已经同意和被版本原因解除的权限，剩下的则是需要请求的权限
            // 加入需要请求的权限
            // 创建一个Observable对象
            PublishSubject<Permission> subject = mSubjectMap.get(permission);
            if (subject == null) {
                unrequestedPermissions.add(permission);
                subject = PublishSubject.create();
                mSubjectMap.put(permission, subject);
            }
            list.add(subject);

        }
        // 如果权限池不为空，则需要请求权限
        if (!unrequestedPermissions.isEmpty()) {
            String[] unRequest = unrequestedPermissions.toArray(new String[unrequestedPermissions.size()]);
            if (mFragment != null) {
                mFragment.requestPermissions(unRequest, PERMISSIONS_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(mActivity, unRequest, PERMISSIONS_REQUEST_CODE);
            }

        }
        // 1. 顺序发送
        // 2. 根据迭代器的集合，进行分组转发，例如 Observable.just(1,2,3)
        return Observable.concat(Observable.fromIterable(list));
    }


    private Observable<?> pending() {
        return Observable.empty();
    }

    private Observable<?> oneOf(Observable<?> trigger, Observable<?> pending) {
        if (trigger == null) {
            return Observable.just(TRIGGER);
        }
        return Observable.merge(trigger, pending);
    }


    public void onRequestPermissionsResult(int requestCode, final String[] permissions, final int[] grantResults) {
        if (requestCode != PERMISSIONS_REQUEST_CODE) {
            return;
        }
        AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < permissions.length; i++) {
                    String                     permission = permissions[i];
                    PublishSubject<Permission> subject    = mSubjectMap.get(permission);
                    if (subject == null) {
                        return;
                    }
                    mSubjectMap.remove(permission);
                    boolean granted = grantResults[i] == PackageManager.PERMISSION_GRANTED;
                    subject.onNext(new Permission(permissions[i], granted));
                    subject.onComplete();
                }
            }
        });

    }
}
