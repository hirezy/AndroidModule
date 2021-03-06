package me.shouheng.advanced;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

import androidx.annotation.Nullable;
import me.shouheng.advanced.aidl.INoteManager;
import me.shouheng.advanced.aidl.Note;
import me.shouheng.advanced.aidl.NoteService;
import me.shouheng.advanced.callback.ActResultRequest;
import me.shouheng.advanced.databinding.ActivityAdvancedBinding;
import me.shouheng.advanced.keepalive.LongLiveService;
import me.shouheng.advanced.messenger.MessengerService;
import me.shouheng.commons.config.BaseConstants;
import me.shouheng.commons.tools.LogUtils;
import me.shouheng.commons.tools.ToastUtils;
import me.shouheng.commons.view.activity.CommonActivity;

/**
 * @author shouh
 * @version $Id: MainActivity, v 0.1 2018/10/22 21:36 shouh Exp$
 */
@Route(path = BaseConstants.ADVANCED_MENU)
public class MainActivity extends CommonActivity<ActivityAdvancedBinding> {

    private INoteManager noteManager;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            noteManager = INoteManager.Stub.asInterface(service);
            try {
                Note note = noteManager.getNote(100);
                LogUtils.d(note);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) { }
    };

    public static final int MSG_REPLAY_ID = 0x0012;
    public static final String MSG_EXTRA_REPLAY_STRING = "__extra_replay_string";
    private boolean serviceConnected = false;
    private Messenger boundServiceMessenger = null;
    private final Messenger receiveMessenger = new Messenger(new ReceiveMessHandler());
    private ServiceConnection msgConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            boundServiceMessenger = new Messenger(service);
            serviceConnected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceConnected = false;
        }
    };
    private static class ReceiveMessHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REPLAY_ID:
                    ToastUtils.makeToast("?????????????????????"+msg.getData().getString(MSG_EXTRA_REPLAY_STRING));
                    break;
            }
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_advanced;
    }

    @Override
    protected void doCreateView(Bundle savedInstanceState) {
        /* ????????????????????? */
        getBinding().btnRemote.setOnClickListener(v ->
                ARouter.getInstance().build(BaseConstants.ADVANCED_REMOTE)
                        .withString(BaseConstants.ADVANCED_REMOTE_ARG_CONTENT, "Simple advanced content")
                        .navigation());
        getBinding().btnRemote2.setOnClickListener(v ->
                ARouter.getInstance().build(BaseConstants.ADVANCED_REMOTE2)
                        .withString(BaseConstants.ADVANCED_REMOTE2_ARG_CONTENT, "Simple advanced content 2")
                        .navigation());

        /* ???????????????????????????????????????????????? */
        getBinding().btnResult.setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity2.class);
            ActResultRequest request = new ActResultRequest(this);

            /* ???????????????????????????????????????????????? fragment ??????????????????????????????????????????????????????
             * ???????????????????????????????????? GG */
            request.startForResult(intent, 1, (resultCode, data) -> {
                String result = data.getStringExtra(Activity2.EXTRA_KEY_NAME);
                ToastUtils.makeToast(result);
            });
        });
        getBinding().btnResult2.setOnClickListener(v -> {
            /* ???????????????????????????????????????????????????????????????????????????????????? */
            Intent intent = new Intent(this, Activity2.class);
            startActivityForResult(intent, 0);
        });

        /* ??????????????????????????? */
        getBinding().btnDisplay.setOnClickListener(v -> {
            try {
                Note note = noteManager.getNote(100);
                ToastUtils.makeToast(note + "");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });

        /* ?????????????????? */
        getBinding().btnFore.setOnClickListener(v -> {
            Intent intent = new Intent(this, LongLiveService.class);
            startService(intent);
        });

        /* ?????? Messenger ???????????? */
        getBinding().btnMessenger.setOnClickListener(v -> {
            Message message = Message.obtain(null, MessengerService.MSG_SAY_SOMETHING);
            message.replyTo = receiveMessenger;
            Bundle bundle = new Bundle();
            bundle.putString(MessengerService.MSG_EXTRA_COMMAND, "11111");
            message.setData(bundle);
            try {
                boundServiceMessenger.send(message);
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        });

        /* ?????? NoteService */
//        Intent intent = new Intent(this, NoteService.class);
//        bindService(intent, connection, Context.BIND_AUTO_CREATE);

        /* ?????? MessengerService */
        Intent intent1 = new Intent(this, MessengerService.class);
        bindService(intent1, msgConn, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                assert data != null;
                String result = data.getStringExtra(Activity2.EXTRA_KEY_NAME);
                ToastUtils.makeToast(result);
                break;
            default:
                // do nothing
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
        if (serviceConnected) {
            unbindService(msgConn);
        }
    }
}
