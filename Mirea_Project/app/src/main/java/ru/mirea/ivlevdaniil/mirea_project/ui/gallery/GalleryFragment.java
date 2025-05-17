package ru.mirea.ivlevdaniil.mirea_project.ui.gallery;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.*;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.*;

import java.util.ArrayList;

import ru.mirea.ivlevdaniil.mirea_project.R;

public class GalleryFragment extends Fragment {

    private RecyclerView recyclerView;
    private boolean selectMode = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        if (getArguments() != null) {
            selectMode = getArguments().getBoolean("select_mode", false);
        }

        ArrayList<Uri> imageUris = loadImages();
        GalleryAdapter adapter = new GalleryAdapter(imageUris, selectMode, this::onImageSelected);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private ArrayList<Uri> loadImages() {
        ArrayList<Uri> uris = new ArrayList<>();
        String[] projection = {MediaStore.Images.Media._ID};
        String selection = MediaStore.Images.Media.RELATIVE_PATH + " LIKE ?";
        String[] args = new String[]{"%Pictures/MireaProject%"};

        Cursor cursor = requireContext().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                args,
                MediaStore.Images.Media.DATE_ADDED + " DESC"
        );

        if (cursor != null) {
            int idIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            while (cursor.moveToNext()) {
                long id = cursor.getLong(idIndex);
                Uri uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, String.valueOf(id));
                uris.add(uri);
            }
            cursor.close();
        }

        return uris;
    }

    private void onImageSelected(Uri uri) {
        if (selectMode) {
            Bundle result = new Bundle();
            result.putParcelable("image_uri", uri);
            getParentFragmentManager().setFragmentResult("profile_image_result", result);
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main).popBackStack();
        }
    }
}
