package com.merxury.blocker.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.merxury.blocker.core.root.utils.Shell;

import java.util.List;

/**
 * Created by rikka
 * https://github.com/RikkaApps/Shizuku/blob/master/manager/src/main/java/moe/shizuku/manager/service/ShellService.java
 */
public class ShellService extends Service {

    private static Shell.Interactive rootSession;
    private ShellServiceBinder mBinder = new ShellServiceBinder();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void run(String[] command, int code, Listener listener) {
        openRootShell(command, code, listener);
    }

    private void openRootShell(final String[] command, final int code, final Listener listener) {
        if (rootSession != null) {
            rootSession.addCommand(command, code, listener);
        } else {
            rootSession = new Shell.Builder()
                    .useSU()
                    .setWantSTDERR(true)
                    .setWatchdogTimeout(10)
                    .setMinimalLogging(true)
                    .open(new Shell.OnCommandResultListener() {

                        @Override
                        public void onCommandResult(int commandCode, int exitCode, List<String> output) {
                            if (exitCode != Shell.OnCommandResultListener.SHELL_RUNNING) {
                                if (listener != null) {
                                    listener.onFailed();
                                    rootSession = null;
                                }
                            } else {
                                rootSession.addCommand(command, code, listener);
                            }
                        }
                    });
        }
    }

    private void kill() {
        if (rootSession != null) {
            try {
                rootSession.kill();
            } catch (Exception ignored) {
            }
            rootSession = null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        kill();
    }

    public interface Listener extends Shell.OnCommandLineListener {
        void onFailed();
    }

    public class ShellServiceBinder extends Binder {
        public void run(String[] command, int code, Listener listener) {
            ShellService.this.run(command, code, listener);
        }
    }
}