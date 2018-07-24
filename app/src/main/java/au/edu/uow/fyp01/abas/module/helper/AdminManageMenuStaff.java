package au.edu.uow.fyp01.abas.module.helper;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import au.edu.uow.fyp01.abas.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminManageMenuStaff extends AppCompatActivity {

  private FirebaseRecyclerOptions firebaseRecyclerOptions;
  private FirebaseRecyclerAdapter<AdminManageMenuStaffRecyclerClass, AdminManageMenuStaffHolder> firebaseRecyclerAdapter;
  private String schID;
  private RecyclerView recyclerView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin_manage_menu_staff);
    recyclerView = findViewById(R.id.activity_admin_manage_menu_staff_recyclerview);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(AdminManageMenuStaff.this));
    recyclerView.addItemDecoration(new AdminManageMenuStaff.SpacesItemDecoration(5));

    Bundle bundle = getIntent().getExtras();
    schID = bundle.get("schID").toString();

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
        .child("Staff").child(schID);


    firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<AdminManageMenuStaffRecyclerClass>()
        .setQuery(databaseReference, AdminManageMenuStaffRecyclerClass.class).build();
    firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<AdminManageMenuStaffRecyclerClass, AdminManageMenuStaffHolder>(
        firebaseRecyclerOptions) {
      @Override
      protected void onBindViewHolder(@NonNull AdminManageMenuStaffHolder holder, int position,
          @NonNull final AdminManageMenuStaffRecyclerClass model) {
        holder.setfullname(model.getFullname());
        holder.setstatus(model.getUsertype());
        holder.setstaffID(model.getStaffID());

        holder.mView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Staff");
            ref.child(schID).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                  @Override
                  public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot detailSnao : dataSnapshot.getChildren()) {
                      if (detailSnao.getKey().equals("staffID")) {
                        if (detailSnao.getValue().toString().equals(model.getStaffID())) {
                          Toast.makeText(getApplicationContext(), "Your cannot modify your account",
                              Toast.LENGTH_LONG).show();
                        } else {

                          Intent i = new Intent(getApplicationContext(),
                              AdminManageMenuStaffDialog.class);
                          Bundle args = new Bundle();
                          args.putString("schID", schID);
                          args.putString("name", model.getFullname());
                          args.putString("type", model.getUsertype());
                          args.putString("staffID", model.getStaffID());

                          i.putExtras(args);
                          startActivity(i);

                        }
                      }
                    }

                  }

                  @Override
                  public void onCancelled(DatabaseError databaseError) {

                  }
                });

          }
        });


      }

      @NonNull
      @Override
      public AdminManageMenuStaffHolder onCreateViewHolder(@NonNull ViewGroup parent,
          int viewType) {
        View view1 = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.activity_admin_manage_menu_staff_recyclerview_item, parent, false);
        return new AdminManageMenuStaff.AdminManageMenuStaffHolder(view1);
      }
    };

    recyclerView.setAdapter(firebaseRecyclerAdapter);
  }

  @Override
  public void onStart() {
    super.onStart();
    firebaseRecyclerAdapter.startListening();
  }

  @Override
  public void onStop() {
    super.onStop();
    firebaseRecyclerAdapter.stopListening();
  }

  public static class AdminManageMenuStaffHolder extends RecyclerView.ViewHolder {

    private TextView fullnameTextView;
    private TextView statusTextView;
    private TextView staffIDTextView;
    private View mView;

    public AdminManageMenuStaffHolder(View itemView) {
      super(itemView);
      mView = itemView;
    }

    public void setfullname(String fullname) {
      fullnameTextView = mView
          .findViewById(R.id.activity_admin_manage_menu_staff_recyclerview_item_name);
      fullnameTextView.setText(fullname);
    }

    public void setstatus(String status) {
      statusTextView = mView
          .findViewById(R.id.activity_admin_manage_menu_staff_recyclerview_item_usertype);
      statusTextView.setText(status);
    }

    public void setstaffID(String staffID) {
      staffIDTextView = mView
          .findViewById(R.id.activity_admin_manage_menu_staff_recyclerview_item_staffid);
      staffIDTextView.setText(staffID);
    }

  }


  public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    private int halfSpace;

    public SpacesItemDecoration(int space) {
      this.halfSpace = space / 2;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
        RecyclerView.State state) {

      if (parent.getPaddingLeft() != halfSpace) {
        parent.setPadding(halfSpace, halfSpace, halfSpace, halfSpace);
        parent.setClipToPadding(false);
      }

      outRect.top = halfSpace;
      outRect.bottom = halfSpace;
      outRect.left = halfSpace;
      outRect.right = halfSpace;
    }
  }

}
