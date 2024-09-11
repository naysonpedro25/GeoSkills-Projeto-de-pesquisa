package br.com.geoskills.adapter;

import android.annotation.SuppressLint;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import br.com.geoskills.R;
import br.com.geoskills.model.User;

import java.util.List;

public class RankRecyclerViewAdapter extends RecyclerView.Adapter<RankRecyclerViewAdapter.MyViewHolder> {
    private final List<User> users;
    private final String uuid;

    public RankRecyclerViewAdapter(List<User> users, String uuid) {
        this.users = users;
        this.uuid = uuid;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rank_recycler_item, parent, false);
        return new MyViewHolder(view);

    }

    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user = users.get(position);

        if (user.getName().length() > 15) {
            holder.textNameUser.setText(user.getName().substring(0, 15) + "...");
        } else
            holder.textNameUser.setText(user.getName());

        holder.textPoints.setText(String.valueOf(user.getPoints()));

        int resColor = ContextCompat.getColor(holder.imageMedal.getContext(), R.color.green_dark);
        if (user.getUuid().equals(uuid)) {
            holder.textNameUser.setText(user.getName());
            holder.textNameUser.setTextColor(resColor);
        } else {
            holder.textNameUser.setText(user.getName());
            holder.textNameUser.setTextColor(ContextCompat.getColor(holder.textNameUser.getContext(), R.color.black));
        }

        final GradientDrawable bg = (GradientDrawable) holder.root.getBackground();
        switch (position) {
            case 0:
                holder.textPosition.setVisibility(View.GONE);
                holder.imageMedal.setVisibility(View.VISIBLE);
                holder.imageMedal.setImageResource(R.drawable.medal_1);

                int colorResY = ContextCompat.getColor(holder.cardView.getContext(), R.color.yellow);
                holder.cardView.setCardBackgroundColor(colorResY);

                bg.setStroke(10, colorResY);
                break;
            case 1:
                holder.textPosition.setVisibility(View.GONE);
                holder.imageMedal.setVisibility(View.VISIBLE);
                holder.imageMedal.setImageResource(R.drawable.medal_2);

                int colorResG = ContextCompat.getColor(holder.cardView.getContext(), android.R.color.darker_gray);
                holder.cardView.setCardBackgroundColor(colorResG);
                bg.setStroke(10, colorResG);
                break;
            case 2:

                holder.textPosition.setVisibility(View.GONE);
                holder.imageMedal.setVisibility(View.VISIBLE);
                holder.imageMedal.setImageResource(R.drawable.medal_3);
                int colorResC = ContextCompat.getColor(holder.cardView.getContext(), R.color.copper);
                holder.cardView.setCardBackgroundColor(colorResC);
                bg.setStroke(10, colorResC);
                break;
            default:
                holder.textPosition.setText((position + 1) + "°");
                holder.textPosition.setVisibility(View.VISIBLE);
                holder.imageMedal.setVisibility(View.GONE);

                int colorResD = ContextCompat.getColor(holder.cardView.getContext(), R.color.gray);
                holder.cardView.setCardBackgroundColor(colorResD);
                bg.setStroke(10, colorResD);
        }

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView textPosition;
        private final TextView textNameUser;
        private final TextView textPoints;
        private final ImageView imageMedal;
        private final CardView cardView;
        private final View root;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textPosition = itemView.findViewById(R.id.text_position_rank);
            textNameUser = itemView.findViewById(R.id.text_name_user_rank);
            textPoints = itemView.findViewById(R.id.text_points_rank);
            imageMedal = itemView.findViewById(R.id.img_medal_rank);
            cardView = itemView.findViewById(R.id.card_view_rank);
            root = itemView;
        }
    }

}
