package com.example.mynotes.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.*;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Util clas to get image from Camera and Gallery. It prompt the user to select camera and gallery and returns the uri of the image captured or selected.
 */

public class ImagePickerUtils {

    /**
     * Single instance of this class.
     */
    private static ImagePickerUtils mSingleInstance;
    /**
     * Fragment Instance as we are using this class within the fragment so we need to get the activity result in fragment.
     */
    private Fragment mFragmentContext;


    public static final int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private ImagePickerListener mImagePickerListener;
    private String mImageTaskSelected;
    private int mID;
    private Uri mFileUri;


    /**
     * Image picker listener to get the uri of the image selected or show message in case image is not selected due to any reason.
     */
    public interface ImagePickerListener {
        void onImageSelected(Uri uri, int id);

        void onImageSelectionError(int id);
    }

    /**
     * Private constructor of {@link ImagePickerUtils} to restrict the access of the class.
     */
    private ImagePickerUtils() {

    }

//    private ImagePickerUtils(Fragment context, ImagePickerListener imagePickerListener) {
//
//    }

    /**
     * Attaching interface and context of the calling fragment so that callback will be sent to the respective class.
     *
     * @param context
     * @param imagePickerListener
     */
    private void initVariables(Fragment context, ImagePickerListener imagePickerListener) {
        mFragmentContext = context;
        mImagePickerListener = imagePickerListener;
    }

    /**
     * Used to get the single instance of this class.
     *
     * @param context
     * @param imagePickerListener
     * @return
     */
    public static final ImagePickerUtils getInstance(Fragment context, ImagePickerListener imagePickerListener) {
        if (mSingleInstance == null) {
            mSingleInstance = new ImagePickerUtils();
        }
        mSingleInstance.initVariables(context, imagePickerListener);
        return mSingleInstance;
    }

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;


    /**
     * Alert user with the importance of allowing permission.
     *
     * @param context
     */
    private void showPermissionAlert(final Context context) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage("External storage permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }
        });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    /**
     * We be able to check if the permission is granted by the user. Need to override the onRequestPermissionsResult in the fragment and send the result of the callback to this method.
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                /**
                 * check if gratn results length is greater than 0 and is equal to PERMISSION_GRANTED
                 */
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    /**
                     * Check if the user has camera or gallery to select the image.
                     */
                    if (mImageTaskSelected.equals("Take Photo"))
                        cameraIntent();
                    else if (mImageTaskSelected.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    /**
                     * Show alert in case user has denied the request.
                     */
                    showPermissionAlert(mFragmentContext.getActivity());
                    //code for deny
                }
                break;
        }
    }

    /**
     * Dialog to show the options to select the image (Camera/Gallery)
     */
    public void selectImage(int id) {
        mID = id;
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(mFragmentContext.getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                mImageTaskSelected = items[item].toString();
                if (items[item].equals("Take Photo")) {
                    cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    /**
     * Fire an intent to get the image from gallery in case user has selected the gallery option.
     */
    private void galleryIntent() {
/**
 * Used in Live
 */
//        Intent i = new Intent(Intent.ACTION_PICK,
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        mFragmentContext.startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_FILE);

        mFragmentContext.startActivityForResult(getPickImageChooserIntent(mFragmentContext.getActivity(), "Select Picture", false), SELECT_FILE);

/**
 * Used before Live
 */
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);//
//        mFragmentContext.startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);


    }

    /**
     * Fire an intent to get the image from camera in case user has selected the camera option.
     */
    private void cameraIntent() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mFileUri = FileProvider.getUriForFile(mFragmentContext.getActivity(), "com.ezlinq.fileprovider", getTempFile(mFragmentContext.getActivity()));
        } else {
            mFileUri = Uri.fromFile(getTempFile(mFragmentContext.getActivity()));
        }
        if (mFileUri == null) {
            ContentValues values = new ContentValues(1);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/*");
            mFileUri = mFragmentContext.getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mFileUri);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        mFragmentContext.startActivityForResult(intent, REQUEST_CAMERA);
    }

    /**
     * call aback for getting the image from camera or gallery
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        /**
         * Check if result is ok then perform actions based on the request code SELECT_FILE||REQUEST_CAMERA
         */
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    /**
     * Process the data fetched from onActivityResult to get the image URI and saves the image as temporary file to use it in future.
     *
     * @param data
     */
    private void onCaptureImageResult(Intent data) {

//        if (fileUri != null) {
//            mImagePickerListener.onImageSelected(fileUri, mID);
//        } else {
//            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
//            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
//
//            File destination = new File(Environment.getExternalStorageDirectory(),
//                    System.currentTimeMillis() + ".jpg");
//
//            FileOutputStream fo;
//            try {
//                destination.createNewFile();
//                fo = new FileOutputStream(destination);
//                fo.write(bytes.toByteArray());
//                fo.close();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            Uri uri = Uri.fromFile(destination);
//            mImagePickerListener.onImageSelected(uri, mID);
//        }


        mImagePickerListener.onImageSelected(mFileUri, mID);

    }

    /**
     * Process the data fetched from onActivityResult to the get the image URI.
     *
     * @param data
     */
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Uri uri = data.getData();
        mImagePickerListener.onImageSelected(uri, mID);

    }


    private static File getTempFile(Context context) {
        File imageFile = new File(context.getExternalCacheDir(), System.currentTimeMillis() + "tempImage");
        imageFile.getParentFile().mkdirs();
        return imageFile;
    }

    /**
     * Create a chooser intent to select the  source to get image from.<br>
     * The source can be camera's  (ACTION_IMAGE_CAPTURE) or gallery's (ACTION_GET_CONTENT).<br>
     * All possible sources are added to the intent chooser.
     *
     * @param context          used to access Android APIs, like content resolve, it is your activity/fragment/widget.
     * @param title            the title to use for the chooser UI
     * @param includeDocuments if to include KitKat documents activity containing all sources
     */
    public static Intent getPickImageChooserIntent(@NonNull Context context, CharSequence title, boolean includeDocuments) {

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();


        List<Intent> galleryIntents = getGalleryIntents(packageManager, Intent.ACTION_GET_CONTENT, includeDocuments);
        if (galleryIntents.size() == 0) {
            // if no intents found for get-content try pick intent action (Huawei P9).
            galleryIntents = getGalleryIntents(packageManager, Intent.ACTION_PICK, includeDocuments);
        }
        allIntents.addAll(galleryIntents);

        Intent target;
        if (allIntents.isEmpty()) {
            target = new Intent();
        } else {
            target = allIntents.get(allIntents.size() - 1);
            allIntents.remove(allIntents.size() - 1);
        }

        // Create a chooser from the main  intent
        Intent chooserIntent = Intent.createChooser(target, title);

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }


    /**
     * Get all Gallery intents for getting image from one of the apps of the device that handle images.
     */
    public static List<Intent> getGalleryIntents(@NonNull PackageManager packageManager, String action, boolean includeDocuments) {
        List<Intent> intents = new ArrayList<>();
        Intent galleryIntent = action == Intent.ACTION_GET_CONTENT ? new Intent(action)
                : new Intent(action, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            intents.add(intent);
        }

        // remove documents intent
        if (!includeDocuments) {
            for (Intent intent : intents) {
                if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                    intents.remove(intent);
                    break;
                }
            }
        }
        return intents;
    }


}
