package com.example.minidouyin;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.minidouyin.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static com.example.minidouyin.utils.Utils.MEDIA_TYPE_IMAGE;
import static com.example.minidouyin.utils.Utils.MEDIA_TYPE_VIDEO;
import static com.example.minidouyin.utils.Utils.getOutputMediaFile;

public class PostActivity extends AppCompatActivity {

    private SurfaceView mSurfaceView;
    private boolean first = true;
    private Camera mCamera;
    private File videoFile;
    private ImageButton videoButton, okButton, resetButton;
    private MediaRecorder mMediaRecorder;
    private static final int REQUEST_EXTERNAL_CAMERA = 101;
    String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO

    };
    private int CAMERA_TYPE = Camera.CameraInfo.CAMERA_FACING_BACK;

    private boolean isRecording = false;

    private int rotationDegree = 90;

    private class SurfaceHolderCallback implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            try {
                mCamera.setPreviewDisplay(surfaceHolder);
                mCamera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (first == false) {
            buttonInitial();
            mCamera = getCamera(0);
            mSurfaceView = findViewById(R.id.img);
            SurfaceHolder surfaceHolder = mSurfaceView.getHolder();
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        first = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_post);

        check();

        videoButton = findViewById(R.id.imageButton);
        okButton = findViewById(R.id.id_ok);
        resetButton = findViewById(R.id.id_reset_camera);
        buttonInitial();
        mCamera = getCamera(0);
        mSurfaceView = findViewById(R.id.img);
        SurfaceHolder surfaceHolder = mSurfaceView.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        //todo 给SurfaceHolder添加Callback
        surfaceHolder.addCallback(new SurfaceHolderCallback());
        mSurfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRecording) {
                    mCamera.autoFocus(new Camera.AutoFocusCallback() {
                        @Override
                        public void onAutoFocus(boolean success, Camera camera) {
                            if (success) {
                                camera.cancelAutoFocus();
                            }
                        }
                    });
                }
            }
        });
        videoButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isRecording) {
                            //todo 停止录制//
                            buttonTodo();
                            mMediaRecorder.stop();
                            mMediaRecorder.reset();
                            mMediaRecorder.release();
                            mMediaRecorder = null;
                            mCamera.lock();
                            mCamera.stopPreview();

                            isRecording = false;
                        } else {
                            //todo 录制
                            isRecording = true;
                            mMediaRecorder = new MediaRecorder();
                            mCamera.unlock();
                            mMediaRecorder.setCamera(mCamera);
                            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
                            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                            mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
                            videoFile = getOutputMediaFile(MEDIA_TYPE_VIDEO);
                            mMediaRecorder.setOutputFile(videoFile.toString());
                            mMediaRecorder.setOrientationHint(90);
                            mMediaRecorder.setPreviewDisplay(mSurfaceView.getHolder().getSurface());
                            try {
                                mMediaRecorder.prepare();
                                mMediaRecorder.start();
                            } catch (Exception e) {
                                releaseMediaRecorder();
                                return;
                            }
                        }

                    }
                }
        );

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent jump = new Intent(PostActivity.this, PreviewActivity.class);
                jump.putExtra("path", videoFile.getAbsolutePath());
                startActivity(jump);
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCamera.startPreview();
                File toDelete = new File(videoFile.getAbsolutePath());
                if (toDelete.exists()) {
                    toDelete.delete();
                }
                buttonInitial();
            }
        });
//
//        findViewById(R.id.btn_facing).setOnClickListener(v -> {
//            //todo 切换前后摄像头
//            mCamera.stopPreview();
//            mCamera.release();
//            if(side == 0)
//            {
//                mCamera = getCamera(1);
//                side = 1;
//            }
//            else if(side == 1)
//            {
//                mCamera = getCamera(0);
//                side = 0;
//            }
//            try{
//                surfaceHolder.addCallback(new SurfaceHolderCallback());
//                mCamera.setPreviewDisplay(surfaceHolder);
//                mCamera.startPreview();
//            }catch(Exception e){
//            }
//        });

    }

    private void buttonInitial() {
        videoButton.setVisibility(View.VISIBLE);
        okButton.setVisibility(View.INVISIBLE);
        resetButton.setVisibility(View.INVISIBLE);
    }

    private void buttonTodo() {
        videoButton.setVisibility(View.INVISIBLE);
        okButton.setVisibility(View.VISIBLE);
        resetButton.setVisibility(View.VISIBLE);
    }

    public Camera getCamera(int position) {
        CAMERA_TYPE = position;
        if (mCamera != null) {
            releaseCameraAndPreview();
        }
        Camera cam = Camera.open(position);
        //todo 摄像头添加属性，例是否自动对焦，设置旋转方向等
//        cam.setDisplayOrientation(90);
        setCameraDisplayOrientation(this, 0, cam);

        return cam;
    }
    public static void setCameraDisplayOrientation(Activity activity,
                                                   int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }
    private void check() {
        if (Utils.isPermissionsReady(this, permissions)) {

        } else {
            //todo 权限检查
            Utils.reuqestPermissions(this, permissions, REQUEST_EXTERNAL_CAMERA);
        }
    }

    private static final int DEGREE_90 = 90;
    private static final int DEGREE_180 = 180;
    private static final int DEGREE_270 = 270;
    private static final int DEGREE_360 = 360;

    private int getCameraDisplayOrientation(int cameraId) {
        Camera.CameraInfo info =
                new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = DEGREE_90;
                break;
            case Surface.ROTATION_180:
                degrees = DEGREE_180;
                break;
            case Surface.ROTATION_270:
                degrees = DEGREE_270;
                break;
            default:
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % DEGREE_360;
            result = (DEGREE_360 - result) % DEGREE_360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + DEGREE_360) % DEGREE_360;
        }
        return result;
    }


    private void releaseCameraAndPreview() {
        //todo 释放camera资源
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;

    }

    Camera.Size size;

    private void startPreview(SurfaceHolder holder) {
        //todo 开始预览
        mMediaRecorder.setPreviewDisplay(holder.getSurface());
        mMediaRecorder.setOrientationHint(90);
        try {
            mMediaRecorder.prepare();
            mMediaRecorder.start();
        } catch (Exception e) {
            releaseMediaRecorder();
            return;
        }
    }


    private void prepareVideoRecorder() {
        //todo 准备MediaRecorder
        mMediaRecorder.setCamera(mCamera);
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
    }


    private void releaseMediaRecorder() {
        //todo 释放MediaRecorder
        mMediaRecorder.reset();
        mMediaRecorder.release();
        mMediaRecorder = null;
        mCamera.lock();
    }


    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {
            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null) {
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(bytes);
                fos.close();
            } catch (IOException e) {
                Log.d("mPicture", "Error accessing file: " + e.getMessage());
            }
            mCamera.startPreview();
        }
    };


    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) h / w;

        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = Math.min(w, h);

        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

}
