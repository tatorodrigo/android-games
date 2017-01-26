package br.com.tattobr.gametests.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.tattobr.gametests.R;
import br.com.tattobr.gametests.data.GameTestData;

public class MainActivity extends AppCompatActivity {
    private List<GameTestData> mData;

    private class GameTestViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView packageTextView;

        public GameTestViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(android.R.id.text1);
            packageTextView = (TextView) itemView.findViewById(android.R.id.text2);
        }
    }

    private class GameTestAdapter extends RecyclerView.Adapter<GameTestViewHolder> implements View.OnClickListener {
        @Override
        public GameTestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = getLayoutInflater();
            return new GameTestViewHolder(inflater.inflate(R.layout.game_test_data_item, parent, false));
        }

        @Override
        public void onBindViewHolder(GameTestViewHolder holder, int position) {
            GameTestData data = mData.get(position);
            holder.nameTextView.setText(data.getName());
            holder.packageTextView.setText(data.getClazz().getName());
            holder.itemView.setTag(data);
            holder.itemView.setOnClickListener(GameTestAdapter.this);
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        @Override
        public void onClick(View view) {
            GameTestData data = (GameTestData) view.getTag();
            startActivity(new Intent(getApplicationContext(), data.getClazz()));
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initRecyclerView();
    }

    private void initData() {
        mData = new ArrayList<>();
        mData.add(new GameTestData("Colored Game", ColoredScreenGame.class));
        mData.add(new GameTestData("Cannon Rotation", CannonRotationGame.class));
        mData.add(new GameTestData("Cannon Gravity", CannonGravityGame.class));
        mData.add(new GameTestData("Cannon Collision", CannonCollisionGame.class));
        mData.add(new GameTestData("Cannon Collision Camera", CannonCollisionCameraGame.class));
        mData.add(new GameTestData("Cannon Collision Texture Atlas", CannonCollisionTextureAtlasGame.class));
        mData.add(new GameTestData("Cannon Collision Texture Atlas Batch", CannonCollisionTextureAtlasBatchGame.class));
        mData.add(new GameTestData("Animation", AnimationGame.class));
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new GameTestAdapter());
    }
}
