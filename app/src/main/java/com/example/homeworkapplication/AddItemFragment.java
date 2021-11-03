package com.example.homeworkapplication;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class AddItemFragment extends DialogFragment implements View.OnClickListener {
    private Bitmap image;
    private ImageView imageView;
    private EditText nameEditText;
    private Context activity;

    private final ActivityResultLauncher<String> getImage = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    try {
                        setImage(ImageDecoder.decodeBitmap(ImageDecoder.createSource(activity.getApplicationContext().getContentResolver(), uri)));
                    } catch (Exception e) {
                        Toast.makeText(activity, R.string.add_fragment_image_error, Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }
    );

    public AddItemFragment() {
        // Required empty public constructor
    }

    public static AddItemFragment newInstance() {
        return new AddItemFragment();
    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (context instanceof Activity) ? context : null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getTheme() {
        return R.style.AnimatedDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_add_item, container, false);
        imageView = (ImageView) root.findViewById(R.id.img_add_item);
        nameEditText = (EditText) root.findViewById(R.id.et_add_item_name);


        root.findViewById(R.id.img_add_item).setOnClickListener(this);
        root.findViewById(R.id.btn_add_item_image).setOnClickListener(this);
        root.findViewById(R.id.btn_all_confirm).setOnClickListener(this);
        root.findViewById(R.id.btn_all_back).setOnClickListener(this);
        nameEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    confirm();
                    return true;
                }
                return false;
            }
        });

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Log.d("click", String.valueOf(id));
        if (id == R.id.btn_add_item_image || id == R.id.img_add_item) {
            addImage();
        } else if (id == R.id.btn_all_confirm) {
            confirm();
        } else if (id == R.id.btn_all_back) {
            close();
        }
    }

    private void addImage() {
        getImage.launch("image/*");
    }

    private void setImage(Bitmap image) {
        this.image = image;
        imageView.setImageBitmap(image);
    }

    private void confirm() {
        String name = nameEditText.getText().toString();
        if (name.isEmpty())
            return;

        MainApplication mainApplication = (MainApplication) activity.getApplicationContext();
        mainApplication.addItem(new Item((image == null) ? BitmapFactory.decodeResource(activity.getResources(), R.drawable.image_placeholder) : image, name));
        ((MainActivity) activity).onItemsUpdated();
        close();
    }

    public void close() {
        if (getDialog() != null) {
            getDialog().dismiss();
        }
    }
}