package vn.nguyen.wifefind

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_list_record.*
import java.util.ArrayList
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog








class listRecordActivity : AppCompatActivity() {
    var mStorage: StorageReference? = null
    private var mDatabase: DatabaseReference? = null
    private var myAdapter: ListAppReceiveAdapter? = null
    private val listRecorder = ArrayList<Upload>()
    var alertDialog:AlertDialog?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_record)
        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        mStorage = FirebaseStorage.getInstance().getReference("Uploads")


        mDatabase!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //dismissing the progress dialog
                listRecorder.clear()
                for (postSnapshot in snapshot.children) {
                    val upload = postSnapshot.getValue<Upload>(Upload::class.java!!)
                    listRecorder!!.add(upload!!)
                }
                //creating adapter
                myAdapter = ListAppReceiveAdapter(this@listRecordActivity, listRecorder)

                //adding adapter to recyclerview
                recyclerView.adapter = myAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        when (item.getItemId()) {
            R.id.menu_on -> {
               // addSomething()
              //  Toast.makeText(this@listRecordActivity, "on", Toast.LENGTH_SHORT).show()
                val database = FirebaseDatabase.getInstance()
                val myRef = database.getReference("message")

                myRef.setValue("true")
                return true
            }
            R.id.menu_off -> {
               // startSettings()
                val database = FirebaseDatabase.getInstance()
                val myRef = database.getReference("message")

                myRef.setValue("false")
               // Toast.makeText(this@listRecordActivity, "false", Toast.LENGTH_SHORT).show()

                return true
            }
            R.id.findLocation -> {
               // startSettings()
                val database = FirebaseDatabase.getInstance()
                val myRef = database.getReference("checkLocation")

                myRef.setValue("true")
               // Toast.makeText(this@listRecordActivity, "false", Toast.LENGTH_SHORT).show()

                return true
            }
            R.id.ShowLocation -> {
               // startSettings()
                val database = FirebaseDatabase.getInstance()
                val myRef = database.getReference("locationResult")


                myRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        try {
                            var address = dataSnapshot.getValue(String::class.java)
                              alertDialog = AlertDialog.Builder(this@listRecordActivity)
                                    //set icon
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    //set title
                                    .setTitle("Hô biến!! Đã tìm ra..")
                                    //set message
                                    .setMessage("Ng ấy đang ở: $address")
                                    //set positive button
                                    .setPositiveButton("Yes") { _, i ->
                                    alertDialog!!.dismiss()

                                    }
                                    //set negative button
                                    .setNegativeButton("No") { _, i ->
                                        alertDialog!!.dismiss()

                                    }
                                    .show()
                           // Toast.makeText(this@listRecordActivity,"Ng ây đang ở: $address",Toast.LENGTH_LONG).show()
                        }catch (e:Exception){
                            e.printStackTrace()
                        }

                        //do what you want with the email
                    }

                    override fun onCancelled(databaseError: DatabaseError) {

                    }
                })
               // Toast.makeText(this@listRecordActivity, "false", Toast.LENGTH_SHORT).show()

                return true
            }


            else -> return super.onOptionsItemSelected(item)
        }
    }
}
