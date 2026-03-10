package sc.example.firebasestart;

import android.content.Intent;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {
     private Context context;
     private List<Player> playerList;
     private List<Player> fullList; // רשימה מלאה לא מסוננת

     public PlayerAdapter(Context context, List<Player> playerList) {
          this.context = context;
          this.playerList = playerList;
          this.fullList = new ArrayList<>(playerList);
     }

     public void filter(String position, String team, int minAge, int maxAge) {
          playerList.clear();
          for (Player p : fullList) {
               boolean matchPosition = position.equals("הכל") || p.getPosition().equals(position);
               boolean matchTeam = team.isEmpty() || p.getTeam().toLowerCase().contains(team.toLowerCase());
               
               int age = 0;
               try {
                   age = Integer.parseInt(p.getAge());
               } catch (NumberFormatException e) {
                   // Handle invalid age string if necessary
               }
               boolean matchAge = age >= minAge && age <= maxAge;

               if (matchPosition && matchTeam && matchAge) {
                    playerList.add(p);
               }
          }
          notifyDataSetChanged();
     }

     public void updateFullList(List<Player> newList) {
          fullList = new ArrayList<>(newList);
          playerList.clear();
          playerList.addAll(newList);
          notifyDataSetChanged();
     }

     @NonNull
     @Override
     public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
          View view = LayoutInflater.from(context).inflate(R.layout.item_player, parent, false);
          return new PlayerViewHolder(view);
     }

     @Override
     public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
         Player player = playerList.get(position);

          holder.tvPlayerName.setText(player.getName());
          holder.tvPlayerAge.setText("גיל: " + player.getAge());
          holder.tvPlayerPosition.setText("עמדה: " + player.getPosition());
          holder.tvPlayerTeam.setText("קבוצה: " + player.getTeam());

          holder.cardView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                    Toast.makeText(context, "נבחר: " + player.getName(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, PlayerDetailsActivity.class);
                    intent.putExtra("name",     player.getName());
                    intent.putExtra("age",      player.getAge());
                    intent.putExtra("position", player.getPosition());
                    intent.putExtra("team",     player.getTeam());
                    intent.putExtra("uid",      player.getUid());
                    context.startActivity(intent);
               }
          });
     }
     @Override
     public int getItemCount() {
          return playerList.size();
     }

     public static class PlayerViewHolder extends RecyclerView.ViewHolder {
          TextView tvPlayerName, tvPlayerAge, tvPlayerPosition, tvPlayerTeam, tvPlayerRating;
          CardView cardView;
          public PlayerViewHolder(@NonNull View itemView) {
               super(itemView);
               cardView = itemView.findViewById(R.id.cardViewPlayer);
               tvPlayerName = itemView.findViewById(R.id.tvPlayerName);
               tvPlayerAge = itemView.findViewById(R.id.tvPlayerAge);
               tvPlayerPosition = itemView.findViewById(R.id.tvPlayerPosition);
               tvPlayerTeam = itemView.findViewById(R.id.tvPlayerTeam);
               tvPlayerRating = itemView.findViewById(R.id.tvPlayerRating);
          }
     }
}
