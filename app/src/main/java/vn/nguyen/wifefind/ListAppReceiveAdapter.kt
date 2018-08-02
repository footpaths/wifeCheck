package vn.nguyen.wifefind

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import kotlin.collections.ArrayList
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference
import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import java.nio.file.Files.delete
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import java.nio.file.Files.delete
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener




/**
 * Created by PC on 12/12/2017.
 */
class ListAppReceiveAdapter(private val mContext: Context, var listApp: ArrayList<Upload>) : RecyclerView.Adapter<ListAppReceiveAdapter.MyHolder>() {
    private var mDatabase: DatabaseReference? = null

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.textViewName.text = listApp[position].name


        var mFirebaseStorage = FirebaseStorage.getInstance().reference.storage
        val photoRef = mFirebaseStorage.getReferenceFromUrl(listApp[position].url)
        val ref = FirebaseDatabase.getInstance().reference
        val applesQuery = ref.child(Constants.DATABASE_PATH_UPLOADS).orderByChild("url").equalTo(listApp[position].url)


        holder.btnDownload.setOnClickListener {
            try {
                holder.lnName.setBackgroundColor(Color.parseColor("#D81B60"))
                var browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(listApp[position].url))
                mContext.startActivity(browserIntent)
            } catch (e: Exception) {
                // make something
            }

        }
        holder.btnDelete.setOnClickListener {

            photoRef.delete().addOnSuccessListener {
                applesQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (appleSnapshot in dataSnapshot.children) {
                            appleSnapshot.ref.removeValue()
                            Toast.makeText(mContext, "Xóa thành công!!!", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {

                     }
                })

            }.addOnFailureListener {
                
            }



        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_images, parent, false)
        return MyHolder(v)
    }


    override fun getItemCount(): Int {
        return listApp.size
    }

    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        override fun onClick(p0: View?) {
            ListAppReceiveAdapter.clickListener?.OnItemClick(adapterPosition, p0!!)
        }

        var textViewName: TextView = itemView.findViewById(R.id.textViewName)
        var btnDownload: Button = itemView.findViewById(R.id.btnDownload)
        var lnName: LinearLayout = itemView.findViewById(R.id.lnName)
        var btnDelete: Button = itemView.findViewById(R.id.btnDelete)

        init {
            itemView.setOnClickListener(this)
        }
    }

    fun setOnItemClickListener(clickListener: ListAppReceiveAdapter.ClickListener) {
        ListAppReceiveAdapter.clickListener = clickListener
    }

    interface ClickListener {
        fun OnItemClick(position: Int, v: View)
        fun OnItemClickUpdate()

    }

    companion object {
        var clickListener: ClickListener? = null
    }
}