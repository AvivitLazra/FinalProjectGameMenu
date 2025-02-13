package com.example.finalprojectgamemenu.models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectgamemenu.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class GamesAdapter extends RecyclerView.Adapter<GamesAdapter.MyViewHolder> {

    private List<Games> gameList;
    private DatabaseReference userFavoritesRef;
    private String firebaseUid;
    private boolean isFavoritesMode; // ✅ בודק אם זה מסך המועדפים או מסך המשחקים

    public GamesAdapter(List<Games> gameList, boolean isFavoritesMode) {
        this.gameList = gameList;
        this.isFavoritesMode = isFavoritesMode; // ✅ אם זה מסך המועדפים - נתעדכן בהתאם
        this.firebaseUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.userFavoritesRef = FirebaseDatabase.getInstance().getReference("users").child(firebaseUid).child("favorites");
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView gameName;
        public ImageView gameImage;
        public ImageButton chatBtn;
        public Button heartBtn;

        public MyViewHolder(View view) {
            super(view);
            gameName = view.findViewById(R.id.textGameName);
            gameImage = view.findViewById(R.id.gameImage);
            chatBtn = view.findViewById(R.id.chatBtn);
            heartBtn = view.findViewById(R.id.heartBtn);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardgame, parent, false);
        return new MyViewHolder(itemView);
    }@Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Games game = gameList.get(holder.getAdapterPosition()); // ✅ שימוש במיקום מעודכן
        holder.gameName.setText(game.getgName());
        holder.gameImage.setImageResource(game.getgImage());

        String firebaseUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String storedUserId = userSnapshot.child("userId").getValue(String.class);
                    if (storedUserId != null && storedUserId.equals(firebaseUid)) {
                        String correctUserId = userSnapshot.getKey();
                        DatabaseReference favoritesRef = usersRef.child(correctUserId).child("favorites").child(game.getgName());

                        // ✅ בדיקה אם המשחק במועדפים - עדכון אוטומטי של הלב
                        favoritesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    holder.heartBtn.setBackgroundResource(R.drawable.heartlogofull);
                                } else {
                                    holder.heartBtn.setBackgroundResource(R.drawable.heartlogoempty);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e("GamesAdapter", "שגיאה בבדיקת המועדפים: " + error.getMessage());
                            }
                        });

                        holder.heartBtn.setOnClickListener(v -> {
                            int adapterPosition = holder.getAdapterPosition(); // ✅ שימוש במיקום מעודכן
                            if (adapterPosition == RecyclerView.NO_POSITION) return; // הגנה מפני קריסה

                            favoritesRef.get().addOnCompleteListener(task -> {
                                if (task.isSuccessful() && task.getResult().exists()) {
                                    // ✅ אם המשחק קיים במועדפים - מוחקים אותו
                                    favoritesRef.removeValue().addOnSuccessListener(aVoid -> {
                                        if (isFavoritesMode) {
                                            if (adapterPosition >= 0 && adapterPosition < gameList.size()) {
                                                gameList.remove(adapterPosition);
                                                notifyItemRemoved(adapterPosition);
                                            }
                                        }
                                        holder.heartBtn.setBackgroundResource(R.drawable.heartlogoempty);
                                        Log.d("GamesAdapter", "המשחק הוסר מהמועדפים: " + game.getgName());
                                    }).addOnFailureListener(e -> Log.e("GamesAdapter", "שגיאה בהסרה מהמועדפים", e));
                                } else {
                                    // ✅ אם המשחק לא במועדפים - מוסיפים אותו
                                    favoritesRef.setValue(game).addOnSuccessListener(aVoid -> {
                                        holder.heartBtn.setBackgroundResource(R.drawable.heartlogofull);
                                        Log.d("GamesAdapter", "המשחק נוסף למועדפים: " + game.getgName());
                                    }).addOnFailureListener(e -> Log.e("GamesAdapter", "שגיאה בהוספה למועדפים", e));
                                }
                            });
                        });

                        break; // ✅ עצירת הלולאה כי מצאנו את ה-userId הנכון
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "שגיאה בגישה לנתוני המשתמש: " + error.getMessage());
            }
        });

        holder.chatBtn.setOnClickListener(v -> {
            // כאן אפשר להוסיף מעבר לצ'אט
        });
    }

    private void checkIfFavorite(DatabaseReference gameRef, Button heartBtn) {
        gameRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                heartBtn.setBackgroundResource(R.drawable.heartlogofull); // 🔹 לב מלא אם המשחק במועדפים
            } else {
                heartBtn.setBackgroundResource(R.drawable.heartlogoempty); // 🔹 לב ריק אם המשחק לא במועדפים
            }
        });
    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }
}
