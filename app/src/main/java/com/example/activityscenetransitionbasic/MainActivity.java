package com.example.activityscenetransitionbasic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid);

        // Setup the GridView and set the adapter
        GridView grid = findViewById(R.id.grid);
        grid.setOnItemClickListener(mOnItemClickListener);
        GridAdapter adapter = new GridAdapter();
        grid.setAdapter(adapter);
    }

    private final AdapterView.OnItemClickListener mOnItemClickListener
            = new AdapterView.OnItemClickListener() {

        /**
         * Called when an item in the {@link android.widget.GridView} is clicked. Here will launch
         * the {@link DetailActivity}, using the Scene Transition animation functionality.
         */
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            Item item = (Item) adapterView.getItemAtPosition(position);

            // Construct an Intent as normal
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_PARAM_ID, item.getId());

            // BEGIN_INCLUDE(start_activity)
            /*
             * Now create an {@link android.app.ActivityOptions} instance using the
             * {@link ActivityOptionsCompat#makeSceneTransitionAnimation(Activity, Pair[])} factory
             * method.
             */
            @SuppressWarnings("unchecked")
            ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    MainActivity.this,

                    // Now we provide a list of Pair items which contain the view we can transitioning
                    // from, and the name of the view it is transitioning to, in the launched activity
                    new Pair<>(view.findViewById(R.id.imageview_item),
                            DetailActivity.VIEW_NAME_HEADER_IMAGE),
                    new Pair<>(view.findViewById(R.id.textview_name),
                            DetailActivity.VIEW_NAME_HEADER_TITLE));

            // Now we can start the Activity, providing the activity options as a bundle
            ActivityCompat.startActivity(MainActivity.this, intent, activityOptions.toBundle());
            // END_INCLUDE(start_activity)
        }
    };

    private class GridAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return Item.ITEMS.length;
        }

        @Override
        public Item getItem(int position) {
            return Item.ITEMS[position];
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).getId();
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.grid_item, viewGroup, false);
            }

            final Item item = (Item) getItem(position);

            // Load the thumbnail image
            ImageView image = view.findViewById(R.id.imageview_item);
            Picasso.with(image.getContext()).load(item.getThumbnailUrl()).into(image);

            // Set the TextView's contents
            TextView name = view.findViewById(R.id.textview_name);
            name.setText(item.getName());

            return view;
        }
    }
}
