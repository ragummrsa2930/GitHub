package com.ragu.github.ui.login


import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.ragu.github.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.repoName)
        val login = findViewById<Button>(R.id.login)

        password.apply {
            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        callGitAcitvity(
                                username.text.toString(),
                                password.text.toString()
                        )
                }
                false
            }

            login.setOnClickListener {
                callGitAcitvity(username.text.toString(), password.text.toString())
            }
        }
    }

    private fun callGitAcitvity(name: String, repo: String) {
        if(name.isNotEmpty() && repo.isNotEmpty()){
            val intent = Intent(this, GitDetailActivity::class.java)
            intent.putExtra("name", name)
            intent.putExtra("repo", repo)
            startActivity(intent)
        }
    }


}