package vn.nguyen.wifefind

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance()
        btnSubmit.setOnClickListener {
            checkSingIn()
        }
    }

    private fun checkSingIn() {
        if (edUser.text.toString() == ""){
            Toast.makeText(this@MainActivity, "Vui lòng nhập tên", Toast.LENGTH_SHORT).show()
            return
        }
        if (edPass.text.toString() == ""){
            Toast.makeText(this@MainActivity, "Vui lòng nhập pass", Toast.LENGTH_SHORT).show()
            return
        }

        mAuth!!.signInWithEmailAndPassword(edUser.text.toString(), edPass.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = mAuth!!.currentUser
                        Toast.makeText(this@MainActivity, "Đăng nhập thành công!!!.", Toast.LENGTH_SHORT).show()
                        var intent = Intent(this@MainActivity, listRecordActivity()::class.java)
                        startActivity(intent)

                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(this@MainActivity, "Đăng nhập lỗi!!!. Vui lòng kiểm tra tên đănq nhập", Toast.LENGTH_SHORT).show()
                    }

                    // ...
                }
    }
}
